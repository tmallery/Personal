package tgm.utils;

/**
 *
 * @param <JobType>
 */
abstract public class WorkerThread<JobType> extends Thread {
	private static final boolean DEBUG = false;
	protected final ThreadManager m_manager;
	private final Object m_waitObj;

	private JobType m_job;
	protected boolean m_running;

	public WorkerThread(ThreadManager manager) {
		m_manager = manager;
		m_running = true;
		m_job = null;
		m_waitObj = new Object();
	}

	public void exit() {
		synchronized (m_waitObj) {
			m_running = false;
			m_waitObj.notifyAll();
		}
	}

	public void setJob(JobType job) {
		if(DEBUG) Log.debug("Thread " + this+" inside set job " + job);
		synchronized (m_waitObj) {
			m_job = job;
			m_waitObj.notifyAll();
		}
	}

	protected abstract ThreadResult processJob(JobType job);

	@Override
	public void run() {
		while( m_running ) {
			try {
				synchronized (m_waitObj) {
					if( m_job == null && m_running ) {
						if(DEBUG) Log.debug("Thread " + this + " is waiting.");
						m_waitObj.wait();
					}
				}
			}catch(InterruptedException e) {
			}

			if( m_job != null ) {
				if(DEBUG) Log.debug("Thread " + this + " is running.");
				ThreadResult result = processJob(m_job);
				m_job = null;
				m_manager.jobCompleted(this, result);
			}
		}

		if(DEBUG) Log.debug("Thread " + this + " is exiting");
	}
}
