package tgm.gameoflife.mine.jobqueue;

import tgm.permutationtree.PermutationTree;
import tgm.utils.Log;
import tgm.utils.ThreadManager;
import tgm.utils.ThreadResult;

import java.util.ArrayList;

/**
 * Manages the miner threads to make sure only a specific number of threads
 * are running at once and all are given a section of input to run until all
 * input has been processed, then the manager exits.
 */
public class GameRunnerThreadManager extends ThreadManager<GameRunnerThread> {
	private static final boolean DEBUG = false;

	//private Stack<GameRunnerThread> m_threadPool;
	private PermutationTree<Byte> m_inputState;
	//private int m_threadCount;
	//private int m_maxThreads;
	//private Semaphore m_availableThreads;
	private int m_statesPerJob;

	public GameRunnerThreadManager(int maxThreads, PermutationTree<Byte> input, int statesPerJob ) {
		super(maxThreads, new GameRunnerThreadFactory());
//		m_maxThreads = maxThreads;
//		m_threadPool = new Stack<>();
//		m_threadCount = 0;
//		m_availableThreads = new Semaphore(m_maxThreads, true);
		m_statesPerJob = statesPerJob;
		m_inputState = input;
	}

//	public void jobCompleted(GameRunnerThread thread) {
//		if(DEBUG) Log.debug("GameRunnerThread: job completed");
//		synchronized (m_threadPool) {
//			m_threadPool.push(thread);
//		}
//		m_availableThreads.release();
//	}
//
//	/**
//	 * @return an available Miner thread, blocks until one is available.
//	 */
//	private GameRunnerThread getAvailableMiner() throws InterruptedException {
//		m_availableThreads.acquire();
//
//		GameRunnerThread miner = null;
//		if( m_threadCount < m_maxThreads ) {
//			m_threadCount++;
//			miner = new GameRunnerThread(this);
//			miner.start();
//		} else {
//			// get one from the pool
//			synchronized (m_threadPool) {
//				if (m_threadPool.size() > 0) {
//					miner = m_threadPool.pop();
//
//				} else {
//					// well this should never happen
//					Log.error("some how we ended up without a free miner thread");
//				}
//			}
//
//		}
//
//		return miner;
//	}

	@Override
	public void run() {

		if(DEBUG) Log.debug("GameRunnerThreadManager starting");

		double numPermutations = Math.pow(2, m_inputState.getTreeDepth());
		double stepSize = Math.floor(numPermutations/32d);
		long permCount = 0;
		long startTime = System.currentTimeMillis();


		try {
			ArrayList<Byte> perm = null;
			do {
				// generate input for job
				ArrayList<ArrayList<Byte>> jobInput = new ArrayList<>(m_statesPerJob);
				for (int jobStateIdx = 0; jobStateIdx < m_statesPerJob && (perm = m_inputState.getNextPermutation()) != null; jobStateIdx++) {
					jobInput.add(perm);
					permCount++;
				}

				GameRunnerThreadJob job = new GameRunnerThreadJob();
				job.setInput(jobInput);
				GameRunnerThread thread = getAvailableThread();
				thread.setJob(job);
				double completd = ((permCount/numPermutations)*100d);
				if( Math.round(completd) % 10 == 0 ) {
					System.out.println("Miner Completed: " + completd  + " percent.");
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

	@Override
	public void startJob(ThreadResult res) {
		// no op, this is the first state
	}
}


