package tgm.gameoflife.mine.multithreaded;

import tgm.gameoflife.GameResult;
import tgm.gameoflife.mine.MineGameOfLifeUtils;
import tgm.permutationtree.PermutationTree;
import tgm.utils.Log;
import tgm.utils.ThreadResult;
import tgm.utils.ThreadManager;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This represents the manager/driver for completing all possible permutations of the specified size
 * of the game of life to find loops. This solution uses multiple pipeline threads to complete the
 * work. A single pipeline thread:<br/>
 * 1) Computes game state
 * 2) Finds loops
 * 3) Filters results to only return the unique looping game states
 */
public class MinerThreadManager extends ThreadManager<MinerThread> {

	private static final boolean DEBUG = false;

	private final int m_inputPerThread;
	private final int m_inputLength;
	private final List<GameResult> m_filteredResults;
	private final Set<GameResult> m_writtenResults;
	private final PrintStream m_output;

	public MinerThreadManager(int maxThreads, int inputPerThread, int inputLength, File outputFile) throws IOException {
		super(maxThreads, new MinerThreadFactory());

		m_inputPerThread = inputPerThread;
		m_inputLength = inputLength;
		m_filteredResults = new ArrayList<>();
		m_writtenResults = new HashSet<>();

		m_output = new PrintStream(new FileOutputStream(outputFile));

		setResultManager(this);
	}


	private void writeResult(PrintStream output, GameResult result) {
		// after all threads have completed, write the
		if (output != null) {
			synchronized (output) {
				// write the result to the output file
				output.print("type: ");
				output.print(result.type.toString());
				output.print(", initialState: ");
				output.print(result.initialState);
				output.print(", resultState: ");
				output.print(result.resultState);
				output.print("\n");
				output.flush();
			}
		}
	}

	public void printResults() {
		synchronized (m_filteredResults) {
			for(GameResult result : m_filteredResults) {
				writeResult(System.out, result);
			}
		}
	}

	/**
	 * Each thread is going to call this method with the results that each one found
	 * @param res
	 */
	@Override
	public void startJob(ThreadResult res) {
		if( res != null ) {
			if( res instanceof MineResult ) {
				ArrayList<GameResult> results = ((MineResult)res).getFilteredResults();
				if( results != null && results.size() > 0 ) {
					synchronized (m_filteredResults) {
						MineGameOfLifeUtils.getInstance().combine(m_filteredResults, results);
						for(GameResult gr : m_filteredResults) {
							if( !m_writtenResults.contains(gr) ) {
								m_writtenResults.add(gr);
								writeResult(m_output, gr);
							}
						}
					}
				}
			}
		}
	}

	@Override
	public void run() {

		if(DEBUG) Log.debug("GameRunnerThreadManager starting");

		PermutationTree<Byte> inputState = new PermutationTree<>(m_inputLength, new Byte[]{0, 1});

		double numPermutations = Math.pow(2, inputState.getTreeDepth());
		long permCount = 0;
		long startTime = System.currentTimeMillis();


		try {
			ArrayList<Byte> perm = null;
			double prevCompleted = 0d;
			do {
				// generate input for job
				ArrayList<ArrayList<Byte>> jobInput = new ArrayList<>(m_inputPerThread);
				for (int jobStateIdx = 0; jobStateIdx < m_inputPerThread && (perm = inputState.getNextPermutation()) != null; jobStateIdx++) {
					jobInput.add(perm);
					permCount++;
				}

				MinerJob job = new MinerJob();
				job.setInput(jobInput);
				MinerThread thread = getAvailableThread();
				thread.setJob(job);
				double completed = Math.floor(((permCount/numPermutations)*100d));

				if( completed != prevCompleted && completed % 10 == 0 ) {
					System.out.println("Mining Status: " + Math.round(completed)  + " Percent Completed. time elapsed " + (System.currentTimeMillis() - startTime)/(60 * 1000d) + " minutes.");
					prevCompleted = completed;
				}
			}
			while(perm != null);

		}catch(InterruptedException e) {
			Log.error("FATAL: GameRunnerThreadManager was interrupted");
		}

		if(DEBUG) Log.debug("GameRunnerThreadManager: waiting for all mainer jobs to complete");
		// wait until all jobs have completed
		waitForAllThreadsToComplete();

		if(DEBUG) Log.debug("GameRunnerThreadManager: all miner jobs have completed");

		stopAllThreads();

		Log.debug("Elapsed mining time " + ( (System.currentTimeMillis() - startTime) / (60d * 1000d) ) + " minutes.");

		if(DEBUG) Log.debug("GameRunnerThreadManager: exiting");
	}
}
