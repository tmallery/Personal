package tgm.gameoflife.mine.jobqueue;

import tgm.gameoflife.*;
import tgm.gameoflife.mine.MineGameOfLifeUtils;
import tgm.utils.Log;
import tgm.utils.ThreadManager;
import tgm.utils.ThreadResult;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Thread for managing results from the miner threads. The results will
 * be written to a file. All results are first checked to see if we're
 * reasonably sure that they're unique.
 */
public class ResultThreadManager extends ThreadManager<ResultFilterThread> {

	private static final boolean DEBUG = false;


	private FileWriter m_output;
	private boolean m_running = true;
	private Queue<GameResult> m_queue = new ConcurrentLinkedQueue<>();
	// save all unique results, so we can keep from writing out results that we've already seen
	private ArrayList<GameResult> m_filteredResults = new ArrayList<>();
	private HashSet<GameResult> m_writtenResults = new HashSet<>();
	private int m_resultsPerThread;

	public ResultThreadManager(int maxThreads, int resultsPerThread, File outputFile) throws IOException {
		super(maxThreads, new ResultFilterThreadFactory());

		m_resultsPerThread = resultsPerThread;
		m_output = new FileWriter(outputFile);

		setResultManager(this);
	}

	/**
	 * Converts the result of the previous stage into something this one can process
	 * @param res
	 */
	public void startJob(ThreadResult res) {
		//saveResult(res.getResults());
		if( res instanceof ResultThreadResult ) {
			ResultThreadResult result = (ResultThreadResult)res;
			if( result.getResults().size() > 0 ) {
				synchronized (m_filteredResults) {
					MineGameOfLifeUtils.getInstance().combine(m_filteredResults, result.getResults());
					for(GameResult gr : m_filteredResults) {
						if( !m_writtenResults.contains(gr) ) {
							m_writtenResults.add(gr);
							writeResults(gr);
						}
					}
				}
			}
		}
		else if( res instanceof GameRunnerThreadResult) {
			GameRunnerThreadResult result = (GameRunnerThreadResult) res;
			saveResult(result.getResults());
		}
	}

	public void saveResult(ArrayList<GameResult> results) {
		if( results != null && results.size() > 0 ) {
			if(DEBUG) Log.debug("ResultThreadManager.saveResult() num results:" + results.size());
			synchronized (m_queue) {
				m_queue.addAll(results);
				m_queue.notifyAll();
			}
		}
	}

	private void writeResults(GameResult result) {
		// after all threads have completed, write the
		if (m_output != null) {
			synchronized (m_output) {
				// write the result to the output file
				try {
					m_output.write("type: ");
					m_output.write(result.type.toString());
					m_output.write(", initialState: ");
					m_output.write(result.initialState);
					m_output.write(", resultState: ");
					m_output.write(result.resultState);
					m_output.write("\n");
					m_output.flush();
				} catch (IOException ioe) {

				}
			}
		}
	}

	public void exit() {
		if(DEBUG) Log.debug("ResultThreadManager.exit()");
		synchronized (m_queue) {
			m_running = false;
			m_queue.notifyAll();
		}
	}

	/**
	 * Primary run method.
	 */
	@Override
	public void run() {
		if(DEBUG) Log.debug("ResultThreadManger.run()");
		try {
			while (m_running) {
				if (DEBUG) Log.debug("ResultThreadManager: waiting on queue");
				synchronized (m_queue) {
					if( m_running && m_queue.size() == 0 ) {
						try {
							m_queue.wait();
						} catch (InterruptedException e) {
							Log.error("ResultThreadManager, interrupted");
						}
					}
				}
				if (DEBUG)
					Log.debug("ResultThreadManager: after wait, m_queue.size " + m_queue.size() );

				while (m_queue.size() > 0) {
					ArrayList<GameResult> results = new ArrayList<>();
					int resultCounter = 0;
					for (GameResult result; m_queue.size() > 0 && resultCounter < m_resultsPerThread && (result = m_queue.remove()) != null; resultCounter++) {
						results.add(result);
					}

					if( results.size() > 0 ) {
						ResultFilterThread thread = getAvailableThread();
						ResultThreadJob job = new ResultThreadJob();
						job.setInput(results);
						thread.setJob(job);
					}
				}
			}

			if(DEBUG) Log.debug("waiting for all result filter threads to complete");

			waitForAllThreadsToComplete();

		}
		catch(InterruptedException e) {
			Log.error("ResultThreadManager: stopped due to exception " + e);
		}
		finally {
			if( m_output != null ) {
				try { m_output.close(); } catch ( IOException ioe ) {}
			}

			stopAllThreads();
		}

		if(DEBUG) Log.debug("ResultThreadManager: finished");
	}
}
