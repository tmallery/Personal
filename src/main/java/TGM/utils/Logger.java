package tgm.utils;

/**
 * Common log interface
 * @author Thomas Mallery
 */
public interface Logger {
	/**
	 * writes an error level message to the log
	 * @param msg [message to write]
	 */
	void error(String msg);

	/**
	 * writes a debug level message to the log
	 * @param msg [message to write]
	 */
	void debug(String msg);
}
