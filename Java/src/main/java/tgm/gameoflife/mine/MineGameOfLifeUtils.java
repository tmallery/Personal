/**
 *  MineGameOfLifeUtils.java, Copyright 2018 Thomas Mallery
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package tgm.gameoflife.mine;

import tgm.gameoflife.GameOfLife;
import tgm.gameoflife.GameResult;
import tgm.gameoflife.GameResultType;
import tgm.utils.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Utilities for mining Game of Life.
 * @author Thomas Mallery
 */
public class MineGameOfLifeUtils {

	private static final MineGameOfLifeUtils s_instance = new MineGameOfLifeUtils();

	public static MineGameOfLifeUtils getInstance() {
		return s_instance;
	}

	private MineGameOfLifeUtils(){}


	/**
	 * Creates the smallest grid possible by removing side
	 * rows and columns that have no live cells
	 *
	 * An input like:<br/>
	 * [ 0, 0, 0 ]<br/>
	 * [ 0, 1, 1 ]<br/>
	 * [ 0, 0, 1 ]<br/>
	 *
	 * Is returned as:<br/>
	 * [ 1, 1 ]<br/>
	 * [ 0, 1 ]<br/>
	 *
	 * @param cells
	 * @return
	 */
	public byte[][] createSmallestCellGrid(byte[][] cells ) {

		int lowerY = cells.length, lowerX = cells.length, upperY = 0, upperX = 0;

		for(int y = 0; y < cells.length; y++ ) {

			// check each row for a column that's alive
			for(int x = 0; x < cells[y].length; x++ ) {
				if( cells[y][x] == GameOfLife.ALIVE_STATE ) {

					if( lowerY > y ) {
						lowerY = y;
					}

					if( lowerX > x ) {
						lowerX = x;
					}

					if( upperY < y ) {
						upperY = y;
					}

					if( upperX < x ) {
						upperX = x;
					}
				}
			}
		}

		byte [][] result = new byte[upperY-lowerY+1][upperX - lowerX+1];

		// shift values over
		for(int y = lowerY; y <= upperY; y++ ) {
			for(int x = lowerX; x <= upperX; x++ ) {
				result[y-lowerY][x-lowerX] = cells[y][x];
			}
		}

		return result;
	}

	/**
	 *
	 * @param cell1 a game state cell array
	 * @param cell2 another game state cell array
	 * @return true if the two cell arrays match by rotating one, up to
	 */
	private boolean matchesByRotation(byte[][] cell1, byte[][] cell2){
		boolean foundMatch = false;

		for(int rotationCount = 0; rotationCount < 4; rotationCount++ ) {
			if( ArrayUtils.getInstance().arrayEquals(cell1, cell2) ) {
				foundMatch = true;
				break;
			}

			// no need to rotate the array back to the starting position
			if( rotationCount < 3 ) {
				// rotate array
				cell2 = ArrayUtils.getInstance().rotateArrayRight(cell2);
			}
		}

		return foundMatch;
	}

	/**
	 * Returns true if the live cell pattern of the two arrays match
	 * @param a1 a game state cell array
	 * @param a2 another game state cell array
	 * @return true if they're a match, false otherwise
	 */
	public boolean matches(byte [][] a1, byte [][] a2 ) {
		boolean result = false;

		if( a1 != null && a2 != null) {

			byte [][]cell1 = createSmallestCellGrid(a1.clone()),
				cell2 = createSmallestCellGrid(a2.clone());


			if( cell1.length > 0 && cell2.length > 0 )
			{
				// see if the two arrays are equal when rotated
				result = matchesByRotation(cell1, cell2);

				if( !result ) {
					cell1 = ArrayUtils.getInstance().mirrorImage(cell1);

					result = matchesByRotation(cell1, cell2);
				}
			}
		}

		return result;
	}

	/**
	 * Prints out the game array into something that looks like:<br/>
	 * [0,1,1]<br/>
	 * [1,0,1]<br/>
	 * [0,1,0]<br/>
	 * @param arr game state array
	 * @return a string with the cells of the array pretty printed
	 */
	public String printArray(byte[][] arr) {
		StringBuilder sb = new StringBuilder();

		if( arr != null ) {
			for (int i = 0; i < arr.length; i++) {
				sb.append("[");
				for (int j = 0; j < arr[i].length; j++) {
					if (j > 0) sb.append(",");
					sb.append(arr[i][j]);
				}
				sb.append("]\n");
			}
		}

		return sb.toString();
	}

	/**
	 * Combines inputs with the existing filtered result list
	 * @param filtered [existing filtered results]
	 * @param inputs [set of new inputs to add iff they don't already exist in the filtered list]
	 */
	public void combine(List<GameResult> filtered, List<GameResult> inputs) {
		for(GameResult result : inputs ) {
			if (result.type == GameResultType.LOOP) {

				boolean unique = true; // true if we haven't seen this game state before

				filteredResultLoop:
				for (GameResult uniqueResult : filtered) {

					byte[][] otherCellState = GameOfLife.parseSquareState(uniqueResult.resultState);

					for (int idx = result.loopStartIdx; idx < result.states.size(); idx++) {
						String stateStr = result.states.get(idx);
						byte[][] cellState = GameOfLife.parseSquareState(stateStr);
						if (matches(otherCellState, cellState)) {
							unique = false;
							break filteredResultLoop;
						}
					}
				}
				if (unique) {
					filtered.add(result);
				}
			}
		}
	}
}
