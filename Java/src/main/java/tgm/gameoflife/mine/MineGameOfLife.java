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
package tgm.gameoflife.mine;

import tgm.gameoflife.GameOfLife;
import tgm.gameoflife.GameResult;
import tgm.gameoflife.GameResultType;
import tgm.permutationtree.PermutationTree;
import tgm.utils.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * The purpose of this object is to find inputs to the Game of Life which
 * result in these states: DEAD, STEADY, LOOP.
 *
 * @author Thomas Mallery
 */
public class MineGameOfLife {

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
	public GameResult mine(String state, int iterationMax) {
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

	private HashSet<GameResult> mineByTreeSet(int depth, int itermax) throws IOException {
		HashSet<GameResult> results = new HashSet<>();

		long startMine = System.currentTimeMillis();

		PermutationTree<Byte> stateTree = new PermutationTree<>(depth, new Byte[]{0,1});

		for(ArrayList<Byte> arr; (arr=stateTree.getNextPermutation()) != null; ) {

			StringBuilder s= new StringBuilder();
			for(Byte b : arr ) {
				s.append(b);
			}
			GameResult result = mine(s.toString(), itermax);
			if (result.type == GameResultType.LOOP ) {
				boolean unique = true;
				outer:
				for(GameResult otherResult : results ) {
					for(int idx = 0; idx < result.states.size(); idx++ ) {
						String stateStr =  result.states.get(idx);
						byte[][] cellState = GameOfLife.parseSquareState(stateStr);
						byte[][] otherCellState = GameOfLife.parseSquareState(otherResult.resultState);
						if (MineGameOfLifeUtils.getInstance().matches(otherCellState, cellState)) {
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
		int size = 25;
		int iterationMax = 50;

		MineGameOfLife miner = new MineGameOfLife();

		HashSet<GameResult> results = miner.mineByTreeSet(size, iterationMax);

		for( GameResult result : results ) {
			Log.debug("Found a match for type " + result.type + ", state " + result.initialState + ", result " + result.resultState);
		}
	}
}


