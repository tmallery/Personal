package tgm.utils;

import org.junit.jupiter.api.Test;
import tgm.gameoflife.mine.MineGameOfLife;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ArrayUtilsTest {

	@Test
	void arrayEqualsTest() {
		assertTrue(ArrayUtils.getInstance().arrayEquals(new byte[][] {{0, 0, 0}, {1, 0, 1}, {0, 1, 0}},
						new byte[][] {{0, 0, 0}, {1, 0, 1}, {0, 1, 0}} ));
	}

	@Test
	void rotateTest() {
		byte[][] arr1 = new byte[][] {{0, 0, 0}, {1, 1, 1}, {0, 1, 0}};
		byte[][] rotated = ArrayUtils.getInstance().rotateArrayRight(arr1);
		assertTrue(ArrayUtils.getInstance().arrayEquals(rotated,
			new byte[][] {{0, 1, 0}, {1, 1, 0}, {0, 1, 0}} ));
	}

	@Test
	void mirrorTest() {
		MineGameOfLife miner = new MineGameOfLife();
		byte[][] arr1 = new byte[][] {{1, 0, 0, 0}, {0, 0, 1, 1}, {1, 0, 1, 0}, { 1, 1, 0, 0}};
		byte[][] mirror = ArrayUtils.getInstance().mirrorImage(arr1);
		assertTrue(ArrayUtils.getInstance().arrayEquals(mirror,
				new byte[][] {{0, 0, 0, 1}, {1, 1, 0, 0}, {0, 1, 0, 1}, {0, 0, 1, 1}}));
	}
}
