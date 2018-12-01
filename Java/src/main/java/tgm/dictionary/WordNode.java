package tgm.dictionary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Node that represents one word of a larger term or phrase
 *
 * @author Thomas Mallery
 */
public class WordNode {
	private WordNode m_parent;
	private HashMap<String, WordNode> m_children;
	private final String m_word;

	/**
	 * Creates a new Word Node
	 * @param word [string word]
	 */
	WordNode(String word) {
		m_word = word;
		m_parent = null;
		m_children = new HashMap<>();
	}

	/**
	 * Gets the word
	 * @return the word associated with this node
	 */
	String getWord() {
		return m_word;
	}

	/**
	 * Set the parent reference for this node
	 * @param node [parent node]
	 */
	void setParent(WordNode node) {
		m_parent = node;
	}

	/**
	 * Gets the parent word
	 * @return the parent reference for this node
	 */
	WordNode getParent() { return m_parent; }

	/**
	 * Add a child node, and set the parent reference
	 * @param node [child node]
	 */
	private void addChild(WordNode node) {
		m_children.put(node.getWord(), node);
		node.setParent(this);
	}

	/**
	 * Returns the map of child nodes. Modifying this map will affect the
	 * tree structure.
	 * @return all children of this node in a Map
	 */
	HashMap<String,WordNode> getChildren() { return m_children; }

	/**
	 * Sets the map of child nodes. This will wipe out any previous map of
	 * child nodes.
	 * @param nodes [map of nodes]
	 */
	void setChildren(HashMap<String,WordNode> nodes) { m_children = nodes; }

	/**
	 * Adds a term to the dictionary. The first token is removed and added
	 * as a child of this node, then the process repeats until all tokens
	 * have been added to the tree.
	 *
	 * @param term [list of tokens, added tokens are removed from the map]
	 * @param goalValue [value associated with the term]
	 */
	void addTerm(ArrayList<String> term, Object goalValue) {
		if( term.size() > 0 ) {
			String word = term.remove(0);

			boolean isTerminator = term.size() == 0;

			WordNode child = m_children.get(word);

			// we either already have the word as a child node or
			// we don't. If we don't have one, then we just need
			// to make and append a new child node. The node type
			// is determined by whether or not the word is the end
			// of the term.
			// if we have a child node, then we either have to, do
			// nothing special or if the word is the end and the
			// node is not a terminator, we must create a terminator
			if (child == null) {
				if (isTerminator) {
					child = new WordTerminatorNode(word, goalValue);
				} else {
					child = new WordNode(word);
				}
				addChild(child);
			} else if( isTerminator ) {
			 	if( !(child instanceof WordTerminatorNode) ) {
					child = new WordTerminatorNode(child, goalValue);
					// replace the existing child with the new terminator node
					m_children.put(word, child);
				}
			}

			child.addTerm(term, goalValue);
		}
	}

	/**
	 * Removes the first token and checks to see if it's a child of this node. If it
	 * is, then the process repeats until we run out of tokens and are on a terminator
	 * node.
	 * @see WordTerminatorNode
	 * @param term [list of tokens which will be removed during look up]
	 * @return the goal value if the term is in the dictionary
	 */
	protected Object lookupTerm(ArrayList<String> term) {
		WordNode child = null;
		if( term.size() > 0 ) {
			String word = term.remove(0);

			child = m_children.get(word);
		}

		return child != null ? child.lookupTerm(term) : null;
	}

	/**
	 * Prints this node and all it's child nodes to the provided string builder.
	 * @param sb [string builder which will the words of this node and it's children
	 *           will be added to]
	 */
	public void print(StringBuilder sb) {
		if( m_children.size() > 0 ) {
			for (Map.Entry<String, WordNode> entry : m_children.entrySet()) {
				sb.append(getWord());
				sb.append(" ");
				entry.getValue().print(sb);
			}
		}
	}
}
