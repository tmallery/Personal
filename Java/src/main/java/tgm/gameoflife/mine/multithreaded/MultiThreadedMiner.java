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
package tgm.gameoflife.mine.multithreaded;

import tgm.utils.Log;

import java.io.File;

/**
 * A multi threaded miner of Game of Life. The purpose is to find unique results.
 * @author Thomas Mallery
 */
public class MultiThreadedMiner {

	private static final void printUsage() {
		System.out.println("missing argument(s):\njava MultiThreadedMiner {number of threads} {number of inputs per thread} {length of input string}");
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

				MinerThreadManager manager = new MinerThreadManager(numThreads, inputsPerThread, inputLength, new File("miningResults.txt"));
				long startTime = System.currentTimeMillis();
				manager.start();
				Log.debug("waiting on worker threads");
				manager.join();
				System.out.println("Total elapsed processing time: " + ((System.currentTimeMillis()-startTime)/(1000d*60d)) + " minutes.");
				manager.printResults();
			}catch(Exception e ) {
				Log.error("MultiThreadedMiner: exited with exception " + e);
				System.exit(-1);
			}
		}
	}
}







