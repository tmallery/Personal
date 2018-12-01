package tgm.dictionary;

/**
 * What it means to be a dictionary, we must be able
 * to put a string and value, check to see if a string
 * is in it, and finally get the stored value for a
 * given string.
 *
 * The tokenization process is left up to individual
 * implementations.
 *
 * @author Thomas Mallery
 */
public interface Dictionary {

	/**
	 * Adds a string into the dictionary with a specific value
	 * @param entry string to put
	 * @param value value to associate with the string, must not be
	 *              null
	 */
	void put(String entry, Object value);

	/**
	 * Checks to see if the string is in the dictionary
	 * @param entry string to check
	 * @return returns true if the string is in the dictionary
	 */
	boolean isIn(String entry);

	/**
	 * Gets the associated value for the provided string.
	 * @param entry string to check
	 * @return returns the associated value if the string is in the
	 * 					dictionary, else null.
	 */
	Object get(String entry);
}
