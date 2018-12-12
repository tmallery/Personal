/**
 *  JobQueueMiner.java, Copyright 2018 Thomas Mallery
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
package tgm.gameoflife.mine.jobqueue;

import tgm.permutationtree.PermutationTree;
import tgm.utils.Log;

import java.io.File;

/**
 * A multi threaded miner of Game of Life. The purpose is to find unique results.
 * @author Thomas Mallery
 */
public class JobQueueMiner {

	private static final void printUsage() {
		System.out.println("missing argument(s):\njava JobQueueMiner {number of threads} {number of inputs per thread} {length of input string}");
		System.out.println("  Results are written to miningResults.txt");
	}

	public static void main(String [] args ) {
		Log.enable();

		if( args == null || args.length < 3 ) {
			printUsage();
			return;
		}

		Integer numThreads = null,
				inputsPerThread = null,
				inputLength = null;
		boolean validInput = false;

		try{
			numThreads = Integer.parseInt(args[0]);
			inputsPerThread = Integer.parseInt(args[1]);
			inputLength = Integer.parseInt(args[2]);
			validInput = true;
		}catch(NumberFormatException nmfe) {
			System.err.println("Invalid input " + nmfe);
		}

		if(validInput) {

			try {
				PermutationTree<Byte> input = new PermutationTree<>(inputLength, new Byte[]{0, 1});

				ResultThreadManager resultThreadManager = new ResultThreadManager(numThreads, inputsPerThread,new File("miningResults.txt"));
				resultThreadManager.start();

				GameRunnerThreadManager manager = new GameRunnerThreadManager(2, input, inputsPerThread );
				manager.setResultManager(resultThreadManager);
				long startTime = System.currentTimeMillis();
				manager.start();
				Log.debug("waiting on worker threads");
				manager.join();
				Log.debug("miners completed");
				resultThreadManager.exit();
				Log.debug("waiting on result manager");
				resultThreadManager.join();
				System.out.println("Total elapsed processing time: " + ((System.currentTimeMillis()-startTime)/(1000d*60d)) + " minutes.");
			}catch(Exception e ) {
				Log.error("JobQueueMiner: exited with exception " + e);
				System.exit(-1);
			}
		}
	}
}







