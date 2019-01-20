package tgm.puzzles;

import org.junit.jupiter.api.Test;
import tgm.utils.DirectedGraph;
import tgm.utils.DirectedIntegerGraph;
import tgm.utils.GraphNode;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for finding paths in a directed graph
 */
public class DirectedGraphPathCheckerTest {

	/**
	 * Simple generic does point a connect to point b tests
	 */
	@Test
	public void pathTest() {
		DirectedGraphPathChecker pathFinder = new DirectedGraphPathChecker();

		DirectedIntegerGraph graph = DirectedIntegerGraph.parse("[0,1][1,2][3,2][2,4]");

		GraphNode[] path = pathFinder.findDirectedPath(graph.get(0), graph.get(4));
		assertNotNull(path);
		assertEquals(4, path.length);
		assertEquals("[0],[1],[2],[4]", pathFinder.pathToString(path));
		path = pathFinder.findDirectedPath(graph.get(0), graph.get(3));
		assertNull(path);


		path = pathFinder.findDirectedPath(graph.get(4), graph.get(0));
		assertNotNull(path);
		assertEquals(4, path.length);
		assertEquals("[0],[1],[2],[4]", pathFinder.pathToString(path));
	}

	/**
	 * Tests which work with loops in the graph, such that could cause the path
	 * finder to infinitely loop
	 */
	@Test
	public void infiniteLoopTest() {
		DirectedGraphPathChecker pathFinder = new DirectedGraphPathChecker();
		DirectedIntegerGraph graph = DirectedIntegerGraph.parse("[1,2][2,3][3,4][4,2][5,0]");
		GraphNode[] path = pathFinder.findDirectedPath(graph.get(1), graph.get(5));
		assertNull(path);
		path = pathFinder.findDirectedPath(graph.get(2), graph.get(5));
		assertNull(path);
		path = pathFinder.findDirectedPath(graph.get(3), graph.get(1));
		assertNotNull(path);
		assertEquals(3, path.length);
		assertEquals("[1],[2],[3]", pathFinder.pathToString(path));
		path = pathFinder.findDirectedPath(graph.get(4), graph.get(1));
		assertNotNull(path);
		assertEquals(4, path.length);
		assertEquals("[1],[2],[3],[4]", pathFinder.pathToString(path));
	}
}
