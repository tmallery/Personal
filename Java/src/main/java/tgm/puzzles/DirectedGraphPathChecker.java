
package tgm.puzzles;

import tgm.utils.DirectedGraph;
import tgm.utils.DirectedIntegerGraph;
import tgm.utils.GraphNode;

import java.util.LinkedHashMap;


/**
 * Finds a path between two nodes within a Directed Graph
 */
public class DirectedGraphPathChecker {

	/**
	 * Returns the directed path between two nodes in a Directed Graph
	 * @param node1
	 * @param node2
	 * @return
	 */
	public GraphNode[] findDirectedPath(GraphNode node1, GraphNode node2) {
		// there's no guarantee on the order of the nodes, so try to find a path from one to the other.
		// if that does't work, then look at the other direction
		GraphNode[] path = findDirectedPathInternal(node1, node2);
		if( path == null ) {
			path = findDirectedPathInternal(node2, node1);
		}
		return path;
	}

	/**
	 * Returns the directed path from the first node to the second, else null
	 * @param node1 starting node
	 * @param node2 ending node
	 * @return path between them if one exists from the starting node
	 */
	private GraphNode[] findDirectedPathInternal(GraphNode node1, GraphNode node2) {
		GraphNode[] path = null;

		if( node1 != null && node2 != null ) {
			if( node1 == node2 ) {
				path = new GraphNode[]{node1};
			} else {
				LinkedHashMap<Object, GraphNode> pathNodes = new LinkedHashMap<>();
				boolean foundGoal = false;
				for(GraphNode pathNode = node1; pathNode != null; pathNode = pathNode.connectedTo()) {

					if (pathNodes.containsKey(pathNode.getId())) {
						// we found	a node which we previously visited, lets just stop so we don't
						// loop for-ev-er
						break;
					} else {

						pathNodes.put(pathNode.getId(), pathNode);
						if( pathNode == node2 ) {
							foundGoal = true;
							break;
						}
					}
				}
				if( foundGoal ) {
					path = pathNodes.values().toArray(new GraphNode[]{});
				}
			}
		}

		return path;
	}

	/**
	 * Converts the provided array of GraphNodes into a string
	 * @param nodes
	 * @return
	 */
	public String pathToString(GraphNode[] nodes) {
		StringBuilder sb = new StringBuilder();
		if(nodes != null) {
			for(int idx = 0; idx < nodes.length; idx++ ) {

				if(idx > 0 ) { sb.append(","); }

				GraphNode node = nodes[idx];

				sb.append("[");
				sb.append(node.getId());
				sb.append("]");

			}
		} else {
			sb.append("no path");
		}

		return sb.toString();
	}

	private static void printUsage() {
		System.out.println("Usage:\njava tgm.puzzles.DirectedGraphPathChecker {graph} {id of node1} {id of node2}" );
		System.out.println("    {graph}: [0,1] [1,2] [3,2]");
		System.out.println("    {id}: non negative number");
	}

	public static void main(String [] args) {

		if( args.length != 3) {
			printUsage();
		} else {
			Integer idOne = null, idTwo = null;
			try{
				idOne = Integer.parseInt(args[1]);
				idTwo = Integer.parseInt(args[2]);
			}catch(NumberFormatException e) {
				System.err.println(e);
			}

			if( idOne >= 0 && idTwo >= 0 ) {

				DirectedGraph graph = DirectedIntegerGraph.parse(args[0]);

				GraphNode node1 = graph.get(idOne);
				GraphNode node2 = graph.get(idTwo);

				DirectedGraphPathChecker pathFinder = new DirectedGraphPathChecker();

				System.out.println("path:");
				System.out.println(pathFinder.pathToString(pathFinder.findDirectedPath(node1, node2)));
			}
		}
	}
}
