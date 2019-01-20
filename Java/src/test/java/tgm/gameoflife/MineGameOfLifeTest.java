package tgm.gameoflife;

import org.junit.jupiter.api.Test;
import tgm.gameoflife.mine.MineGameOfLifeUtils;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MineGameOfLifeTest {

	@Test
	void arrayReduceTest() {
		MineGameOfLifeUtils miner = MineGameOfLifeUtils.getInstance();

		byte[][] input = new byte [][] {{0, 0, 0},{0, 1, 1},{0, 0, 1}};
		byte[][] arg = miner.createSmallestCellGrid(input);
		assertEquals("[1,1]\n" +
				"[0,1]\n", miner.printArray(arg));
	}

	@Test
	void matchesTest() {
		byte [][] input = new byte[][] {{1,1},{0,1}};
		byte [][] input2 = new byte[][] {{0,1},{1,1}};
		MineGameOfLifeUtils miner = MineGameOfLifeUtils.getInstance();
		assertTrue(miner.matches(input, input2));


		byte[][]arr1 = new byte[][]{
			{1,1,1,0},
			{1,0,0,1},
			{0,0,0,1},
			{0,0,0,1},
			{1,1,1,0}};

		byte[][] arr2 =  new byte[][]{
			{0,1,0,0},
			{0,1,1,1},
			{0,0,1,1},
			{1,0,0,1},
			{1,0,1,0}};

		assertFalse(miner.matches(arr1, arr2));

		arr1 = new byte[][] {
			{1}, {1}, {1}
		};

		arr2 = new byte[][] {
			{1,1,1}
		};

		assertTrue(miner.matches(arr1, arr2));
	}
}
