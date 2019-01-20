package tgm.utils;


/**
 * Generic directed map interface with nodes that have an identifier object type
 * specified by T
 * @param <T>
 */
public interface DirectedGraph<T> {
	/**
	 * Returns the number of nodes in the directed graph
	 * @return
	 */
	public int size();

	/**
	 * Returns the node which ahs the corosponding identifier integer
	 * @param id
	 * @return
	 */
	public GraphNode get(T id);
}
