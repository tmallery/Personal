package tgm.utils;

/**
 * A node for a directed graph. Each node can connect to exactly one other node, and
 * each node contains an identifier of the specified type
 */
public class GraphNode<T> {

	private GraphNode m_connected;
	private T m_id;

	public GraphNode(T id) {
		m_connected = null;
		m_id = id;
	}

	public void connectedTo(GraphNode graphNode) {
		m_connected = graphNode;
	}

	public GraphNode connectedTo() {
		return m_connected;
	}

	public T getId() {
		return m_id;
	}
}
