/**
 *  MineGameOfLife.java, Copyright 2018 Thomas Mallery
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
package tgm.gameoflife;

import tgm.permutationtree.PermutationTree;
import tgm.utils.ArrayUtils;
import tgm.utils.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * The purpose of this object is to find inputs to the Game of Life which
 * result in these states: DEAD, STEADY, LOOP.
 */
public class MineGameOfLife {

	private static HashSet<GameResult> s_results = new HashSet<GameResult>();

	/**
	 * Returns true if all cells in the state are dead
	 * @param state
	 * @return
	 */
	private boolean isDead(String state) {
		return state.indexOf(String.valueOf(GameOfLife.ALIVE_STATE)) == -1;
	}

	/**
	 * Returns true if the current state is the same as the previous state, ie cells are
	 * @param states
	 * @return
	 */
	private boolean isSteadyState(ArrayList<String> states) {
		boolean isSteady = false;
		if( states.size() > 1 ) {
			String last = states.get(states.size()-1);
			String beforeLast = states.get(states.size()-2);
			isSteady = last.equals(beforeLast);
		}
		return isSteady;
	}

	/**
	 * Returns true if the current state
	 * @param states
	 * @return
	 */
	private int isInLoop(ArrayList<String> states) {
		int foundLoopIdx = -1;

		if( states.size() > 1 ) {
			String last = states.get(states.size()-1);
			for(int idx = 0; idx < states.size()-1; idx++ ){
				if(states.get(idx).equals(last) ) {
					foundLoopIdx = idx;
					break;
				}
			}
		}

		return foundLoopIdx;
	}
	private GameResult mine(String state, int iterationMax) {
		GameResult result = new GameResult();
		result.initialState = state;
		result.type = GameResultType.UNKNOWN;

		GameOfLife game = new GameOfLife(state);

		ArrayList<String> pastStates = new ArrayList<>();

		for(int loopCount = 0; loopCount < iterationMax; loopCount++) {
			game.updateState();

			// now look to see if we've hit a goal state
			String currentState = game.getStateString();

			if( isDead(currentState) ) {
				result.type = GameResultType.DEAD;
				result.iterationCount = loopCount+1;
				break;
			}


			pastStates.add(currentState);


			if( isSteadyState(pastStates) ) {
				result.type = GameResultType.STEADY;
				result.iterationCount = loopCount+1;
				result.resultState = currentState;
				break;
			}

			int loopStartIdx;
			if( (loopStartIdx = isInLoop(pastStates)) != -1 ) {
				result.type = GameResultType.LOOP;
				result.loopStartIdx = (loopStartIdx);
				result.iterationCount = loopCount+1;
				result.states = pastStates;
				result.resultState = currentState;

				break;
			}
		}

		return result;
	}

	private byte[] generateEmptyState(int size) {
		byte[] state = new byte[size*size];
		int stateIdx = 0;
		for( int y = 0; y < size; y++) {
			for (int x = 0; x < size; x++) {
				state[stateIdx++] = '0';
			}
		}

		return state;
	}

	public void mine(int size, int iterationMax) {
		byte[] state = generateEmptyState(size);

		for(int idx = 0; idx < state.length; idx++ ) {
			mine(state, iterationMax, idx);
			state[idx] = '1';
		}
	}

	private void mine(byte[] prevState, int iterationMax, int idx) {
		// mine as is
		String prevStateStr = new String(prevState);
		GameResult result = mine(prevStateStr, iterationMax);
		if (result.type == GameResultType.LOOP || result.type == GameResultType.STEADY) {
			Log.debug("Found a match for type " + result.type + ", state " + prevStateStr + ", result " + result.resultState);
		}
		// then move the live cell throughout the string
		for(int curPos = idx; curPos < prevState.length; curPos++ ) {
			byte[] state = prevState.clone();

			state[curPos] = '1';
			String curState = new String(state);

		}
	}



	/**
	 * Creates the smallest grid possible by removing side
	 * rows and columns that have no live cells
	 * @param cells
	 * @return
	 */
	byte[][] createSmallestCellGrid(byte[][] cells ) {
		/*
		 [ 0, 0, 0 ]
		 [ 0, 1, 1 ]
		 [ 0, 0, 1 ]

		 [ 1, 1 ]
		 [ 0, 1 ]
		 */

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

	private boolean matchesByRotation(byte[][] cell1, byte[][] cell2){
		boolean foundMatch = false;
		for(int i = 0; !foundMatch && i < 4; i++ ) {
			if( ArrayUtils.getInstance().arrayEquals(cell1, cell2) ) {
				foundMatch = true;
				break;
			}

			// rotate array
			cell2 = ArrayUtils.getInstance().rotateArrayRight(cell2);
		}

		return foundMatch;
	}

	/**
	 * Returns true if the live cell pattern of the two arrays match
	 * @param a1
	 * @param a2
	 * @return
	 */
	boolean matches(byte [][] a1, byte [][] a2 ) {
		boolean result = false;

		if( a1 != null && a2 != null) {

			byte [][]cell1 = createSmallestCellGrid(a1.clone()),
				cell2 = createSmallestCellGrid(a2.clone());


			if( cell1.length > 0 && cell2.length > 0 )
			{
				// see if the two arrays are equal when rotated
				try {
					result = matchesByRotation(cell1, cell2);
				}catch(Exception e) {
					System.out.println("cell1:\n" + printArray(cell1));
					System.out.println("cell2:\n" + printArray(cell2));
					System.err.print(e);
					throw e;
				}

				if( !result ) {
					cell1 = ArrayUtils.getInstance().mirrorImage(cell1);

					result = matchesByRotation(cell1, cell2);
				}
			}
		}

		return result;
	}

	public static String printArray(byte[][] arr) {
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

	private HashSet<GameResult> mineByTreeSet(int itermax) throws IOException {
		HashSet<GameResult> results = new HashSet<>();

		long startMine = System.currentTimeMillis();

		PermutationTree<Byte> stateTree = new PermutationTree<>(36, new Byte[]{0,1});

		long permCount = 0;
		for(ArrayList<Byte> arr; (arr=stateTree.getNextPermutation()) != null; ) {

			StringBuilder s= new StringBuilder();
			for(Byte b : arr ) {
				s.append(b);
			}
			//System.out.println("running perm: " + permCount++ + ", state: "+ s.toString());
			GameResult result = mine(s.toString(), itermax);
			if (result.type == GameResultType.LOOP ) {
				boolean unique = true;
				outer:
				for(GameResult otherResult : results ) {
					for(int idx = 0; idx < result.states.size(); idx++ ) {
						String stateStr =  result.states.get(idx);
						byte[][] cellState = GameOfLife.parseSquareState(stateStr);
						byte[][] otherCellState = GameOfLife.parseSquareState(otherResult.resultState);
						if (matches(otherCellState, cellState)) {
							unique = false;
							break outer;
						}
					}
				}
				if( unique ) {
					results.add(result);
				}
			}
		}

		long endMine = System.currentTimeMillis();
		System.out.println("Time to mine: " + ((endMine- startMine)/(1000d * 60d )) + " minutes.");
		return results;
	}

	public static void main(String [] args ) throws Exception {
		Log.enable();
		int size = 5;
		int iterationMax = 50;

		MineGameOfLife miner = new MineGameOfLife();

		HashSet<GameResult> results = miner.mineByTreeSet(iterationMax);

		for( GameResult result : results ) {
			Log.debug("Found a match for type " + result.type + ", state " + result.initialState + ", result " + result.resultState);
		}
	}
}

enum GameResultType { DEAD, STEADY, LOOP, UNKNOWN }
class GameResult {
	GameResultType type = null;
	String initialState = null;
	int loopStartIdx= -1;
	int iterationCount = 0;
	ArrayList<String> states = null;
	String resultState = null;
}
