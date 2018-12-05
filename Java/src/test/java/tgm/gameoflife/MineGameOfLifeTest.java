package tgm.gameoflife;

import org.junit.jupiter.api.Test;
import tgm.utils.ArrayUtils;

import static org.junit.jupiter.api.Assertions.*;

public class MineGameOfLifeTest {

	@Test
	void arrayReduceTest() {
		MineGameOfLife miner = new MineGameOfLife();

		byte[][] input = new byte [][] {{0, 0, 0},{0, 1, 1},{0, 0, 1}};
		//System.out.prbyteln(prbyteArray(input));
		byte[][] arg = miner.createSmallestCellGrid(input);
		//System.out.prbyteln(prbyteArray(arg));
		assertEquals("[1,1]\n" +
				"[0,1]\n", miner.printArray(arg));
	}






	@Test
	void matchesTest() {
		byte [][] input = new byte[][] {{1,1},{0,1}};
		byte [][] input2 = new byte[][] {{0,1},{1,1}};
		MineGameOfLife miner = new MineGameOfLife();
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
