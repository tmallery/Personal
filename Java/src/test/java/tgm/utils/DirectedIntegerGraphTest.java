package tgm.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DirectedIntegerGraphTest {
	@Test
	public void parsTest() {
		DirectedGraph graph = DirectedIntegerGraph.parse("[0, 1], [1, 2], [2, 3], [4, 3]");
		assertEquals(5, graph.size());
		assertEquals(1, graph.get(0).connectedTo().getId());
		assertEquals(2, graph.get(1).connectedTo().getId());
		assertEquals(3, graph.get(2).connectedTo().getId());
		assertEquals(3, graph.get(4).connectedTo().getId());
		assertEquals(null, graph.get(3).connectedTo());
	}
}
