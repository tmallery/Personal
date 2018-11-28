package tgm.dictionary;

import java.util.ArrayList;
import java.util.Map;

/**
 * This is the same as a word node except it indicates the end of a sentence / phrase / title.
 * @author Thomas Mallery
 */
class WordTerminatorNode extends WordNode {

	/**
	 * Value associated with the phrase in the dictionary
	 */
	private Object m_goalValue;


	/**
	 * Creates a terminator token from an existing token. Takes its word
	 * and position in the dictionary.
	 * @param node [word node which this node will be a copy of]
	 * @param goalVal [value associated with the term that reaches this node]
	 */
	WordTerminatorNode(WordNode node, Object goalVal) {
		super(node.getWord());
		setParent(node.getParent());
		setChildren(node.getChildren());
		m_goalValue = goalVal;
	}

	/**
	 * Creates a terminator token which is unassociated with the dictionary
	 * @param word [string word]
	 * @param goalVal [value associated with the term]
	 */
	WordTerminatorNode(String word, Object goalVal) {
		super(word);
		m_goalValue = goalVal;
	}

	/**
	 * Overriding the print method to mark this word as a terminator and then
	 * printing child nodes which would be longer phrases.
	 * @param sb [string builder which will hold this node and all it's children's words]
	 */
	@Override
	public void print(StringBuilder sb) {
		// first this token is the end of a word so mark it.
		sb.append(getWord());
		sb.append("\n");

		// then print extra words if there are longer phrases
		if( getChildren().size() > 0 ) {
			for (Map.Entry<String, WordNode> entry : getChildren().entrySet()) {
				sb.append(getWord());
				sb.append(" ");
				entry.getValue().print(sb);
			}
		}
	}

	/**
	 * Returns the value associated with this phrase in the dictionary
	 * @return the goal value
	 */
	private Object getGoalValue() {
		return m_goalValue;
	}

	/**
	 * If the term is empty here then, return the goal value. Otherwise
	 * continue down the chain.
	 * @param sentence [list of tokens which will be removed until we find
	 *                 a match]
	 * @return the goal value if the term is in the dictionary
	 */
	@Override
	protected Object lookupTerm(ArrayList<String> sentence) {

		return (sentence.size() == 0 ? getGoalValue() : super.lookupTerm(sentence));
	}
}
