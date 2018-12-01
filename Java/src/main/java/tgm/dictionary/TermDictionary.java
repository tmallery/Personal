package tgm.dictionary;

import tgm.utils.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
 * A case sensitive term dictionary of space separated tokens. Works
 * like a TreeMap except the input stream is tokenized and the depth
 * of the tree is determined by the number of tokens.
 *
 * This is not thread safe
 */
public class TermDictionary extends WordNode implements Dictionary {

	private static final Object s_defaultGoalValue = Boolean.TRUE;

	/**
	 * Creates a new and empty dictionary
	 */
	public TermDictionary() {
		super(null);
	}

	/**
	 * Turns the passed in term into a list of tokens.
	 * @param term [string of space separated words]
	 * @return An arraylist of the tokens of the provided term string
	 */
	protected ArrayList<String> tokenize(String term) {
		ArrayList<String> words;
		if( term != null ) {
			String[] tokens = term.split(" ");

			words = new ArrayList<>(tokens.length);
			words.addAll(Arrays.asList(tokens));
		} else {
			words = new ArrayList<>(0);
		}

		return words;
	}

	/**
	 * Adds a new term to the dictionary
	 * @param term [string of space separated words]
	 */
	public void put(String term) {
		put(term, s_defaultGoalValue);
	}

	/**
	 * Adds a new term to the dictionary with the specific non null goal value
	 * @param term [string of space separated words]
	 * @param val [Goal Value]
	 */
	public void put(String term, Object val ) {
		if( term == null ) {
			Log.error("TermDictionary: cannot put null to the dictionary");
		}
		else if( val == null ) {
			Log.error("TermDictionary: cannot put a term with a null goal value to the dictionary");
		}
		else {
			addTerm(tokenize(term), val);
		}
	}

	/**
	 * Prints out all terms in the dictionary
	 * @return a string of all the terms in the dictionary
	 */
	public String print() {
		StringBuilder sb = new StringBuilder();
		for(Map.Entry<String, WordNode> entry : getChildren().entrySet()) {
			entry.getValue().print(sb);
		}

		return sb.toString();
	}

	/**
	 * look up the provided term in the dictionary and return true if we found a match
	 * @param term [string of space separated words]
	 * @return Returns true if the term is in the dictionary
	 */
	private boolean isIn(ArrayList<String> term) {

		return lookupTerm(term) != null;
	}

	/**
	 * Returns true if the term is in the dictionary
	 * @param term [string of space separated words]
	 * @return Returns true if the term is in the dictionary
	 */
	public boolean isIn(String term) {
		return isIn(tokenize(term));
	}

	/**
	 * Gets the value associated with the provided term in the dictionary, else null
	 * @param term [string of space separated words]
	 * @return the value for the term in the dictionary else null
	 */
	private Object get(ArrayList<String> term) {

		return lookupTerm(term);
	}

	/**
	 * Gets the value associated with the provided term in the dictionary, else null
	 * @param term [string of space separated words]
	 * @return the value for the term in the dictionary else null
	 */
	public Object get(String term) {
		return get(tokenize(term));
	}
}
