package tgm.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * A group of nodes whom have a single directed outward connection and each node has an Integer
 * identifier
 */
public class DirectedIntegerGraph implements DirectedGraph<Integer> {
	private Map<Integer, GraphNode<Integer>> m_nodes;

	public DirectedIntegerGraph(Map<Integer, GraphNode<Integer>> nodes) {
		m_nodes = nodes;
	}

	/**
	 * Returns the number of nodes in the directed graph
	 * @return
	 */
	public int size() {
		return m_nodes.size();
	}

	/**
	 * Returns the node which ahs the corosponding identifier integer
	 * @param id
	 * @return
	 */
	public GraphNode get(Integer id) {
		return m_nodes.get(id);
	}

	/**
	 * Graph
	 * [{this node}, {edge created to node}]
	 *
	 * [0, 3], [1, 2], [2, 3]
	 * @param input
	 * @return
	 */
	public static DirectedIntegerGraph parse(String input) {

		HashMap<Integer, GraphNode<Integer>> nodes = new HashMap<>();
		HashMap<Integer, Integer> connectedTo = new HashMap<>();

		Pattern regex = Pattern.compile("\\[\\s*(\\d+)\\s*,\\s*(\\d+)\\s*\\]");

		Matcher matches = regex.matcher(input);

		while(matches.find()) {
			int id = Integer.parseInt(matches.group(1));
			GraphNode<Integer> node = new GraphNode<>(id);
			nodes.put(id, node);
			connectedTo.put(id, Integer.parseInt(matches.group(2)));
		}

		for( Map.Entry<Integer, Integer> entry : connectedTo.entrySet()) {
			GraphNode<Integer> node = nodes.get(entry.getKey());

			GraphNode<Integer> otherNode = nodes.get(entry.getValue());
			if( otherNode == null ) {
				otherNode = new GraphNode<>(entry.getValue());
				nodes.put(entry.getValue(), otherNode);
			}
			node.connectedTo(otherNode);
		}

		return new DirectedIntegerGraph(nodes);
	}
}
