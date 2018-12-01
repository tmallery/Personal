package tgm.utils;

/**
 * Logger which uses the System objects, standard out and standard
 * error for reporting.
 * @author Thomas Mallery
 */
public class StandardErrorLogger implements Logger {

	/**
	 * Writes a message to standard error
	 * @param msg [message to write]
	 */
	@Override
	public void error(String msg) {
		System.err.println(msg);
	}

	/**
	 * Writes a message to standard out
	 * @param msg [message to write]
	 */
	@Override
	public void debug(String msg) {

	}
}
