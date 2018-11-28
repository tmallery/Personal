package tgm.utils;

/**
 * The logger:w
 * @author Thomas Mallery
 */
public abstract class Log {
	private static Logger s_instance = null;

	/**
	 * Enables logging using the default logger implementation
	 */
	public static void enable() {
		enable(null);
	}

	/**
	 * Enables logging using the provided logger implementation if non is specified the
	 * default logger implementation is used.
	 * @param log [logger to use]
	 */
	public static synchronized void enable(Logger log) {
		if( s_instance == null ) {
			if( log == null ) {
				log = new StandardErrorLogger();
			}
			s_instance = log;
		}
	}

	/**
	 * Record an error message in the log
	 * @param msg [message to write]
	 */
	public static void error(String msg) {
		if( s_instance != null ) {
			s_instance.error(msg);
		}
	}

	/**
	 * Record a debug message in the log
	 * @param msg [message to write]
	 */
	public static void debug(String msg) {
		if(s_instance != null ) {
			s_instance.debug(msg);
		}
	}
}
