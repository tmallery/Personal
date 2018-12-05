package tgm.permutationtree;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests for the permutation tree
 */
public class PermutationTreeTest {

	@Test
	void simpleTest() {
		PermutationTree<Byte> byteTree = new PermutationTree<>(2, new Byte[]{0,1});

		HashSet<String> perms = new HashSet<>(4);
		perms.add("00");
		perms.add("01");
		perms.add("10");
		perms.add("11");
		for(ArrayList<Byte> arr; (arr = byteTree.getNextPermutation()) != null; ) {

			StringBuilder sb = new StringBuilder();
			for(Byte b : arr) {
				sb.append(b);
			}
			assertTrue(perms.remove(sb.toString()), "found permutation is not in the benchmark set");
		}

		assertTrue(perms.isEmpty(), "not all permutations were found");
	}
}
