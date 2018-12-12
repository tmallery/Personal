package tgm.utils;

import java.util.Stack;
import java.util.concurrent.Semaphore;

public abstract class ThreadManager<T extends WorkerThread> extends Thread {

	private static final boolean DEBUG = false;

	private final Stack<T> m_threadPool;
	private int m_threadCount;
	private int m_maxThreads;
	private Semaphore m_availableThreads;
	private ThreadFactory<T> m_threadFactory;
	private ThreadManager m_resultManager;

	public ThreadManager(int maxThreads, ThreadFactory<T> factory) {
		if( maxThreads < 0 ) {
			Log.error("Invalid max thread count, setting to 1");
			maxThreads = 1;
		}
		if( factory == null ) {
			throw new IllegalArgumentException("ThreadFactory must not be null");
		}
		m_maxThreads = maxThreads;
		m_threadPool = new Stack<>();
		m_threadCount = 0;
		m_threadFactory = factory;
		m_availableThreads = new Semaphore(m_maxThreads, true);
		m_resultManager = null;
	}

	/**
	 * This allows thread managers to be chained together
	 * @param other
	 */
	public void setResultManager(ThreadManager other) {
		m_resultManager = other;
	}

	/**
	 * Takes a result from another manager and converts it into a job for this manager
	 * and runs it.
	 * @param res
	 */
	public abstract void startJob(ThreadResult res);

	public final void jobCompleted(T thread, ThreadResult result) {
		if(DEBUG) Log.debug("ThreadManager: job completed");
		if(m_resultManager != null ) m_resultManager.startJob(result);
		synchronized (m_threadPool) {
			m_threadPool.push(thread);
		}
		m_availableThreads.release();
	}

	/**
	 * @return an available Miner thread, blocks until one is available.
	 */
	protected final T getAvailableThread() throws InterruptedException {
		m_availableThreads.acquire();

		T miner = null;
		if( m_threadCount < m_maxThreads ) {
			m_threadCount++;
			miner = m_threadFactory.newThread(this);
			miner.start();
		} else {
			// get one from the pool
			synchronized (m_threadPool) {
				if (m_threadPool.size() > 0) {
					miner = m_threadPool.pop();

				} else {
					// well this should never happen
					Log.error("some how we ended up without a free worker thread");
				}
			}

		}

		return miner;
	}

	/**
	 * Waits until all the worker threads have completed or an interrupted exception is thrown
	 */
	protected final void waitForAllThreadsToComplete() {
		try {
			m_availableThreads.acquire(m_maxThreads);
		} catch (InterruptedException e) {
			Log.error("GameRunnerThreadManager: Interrupted while waiting for miner jobs to complete");
		}
	}

	protected final void stopAllThreads() {
		// do clean up, stop all running threads
		for(T miner : m_threadPool ) {
			miner.exit();
			try {
				miner.join();
			} catch (InterruptedException e) {
				Log.error("MinerThreadManger: Interrupted while stopping miner thread");
			}
		}
	}
}
