package tgm.gameoflife;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests for the Game of Life
 */
public class GameOfLifeTest {

	@Test
	void generalGameStateTests() {
		String [][] tests = {
			{ /*input*/"0101"+
			"1010"+
			"1100"+
			"0011",
			/* benchmark */
			"0110"+
			"1010"+
			"1001"+
			"0110"},
		};

		for(String[] test : tests ) {
			GameOfLife game = new GameOfLife(test[0]);
			game.updateState();
			assertEquals(test[1], game.getStateString());
		}
	}

	@Test
	void testDeadState() {
		GameOfLife game = new GameOfLife("0000000000000000");
		game.updateState();
		assertEquals("0000000000000000", game.getStateString());
	}

	@Test
	void testOverpopulatedState() {
		GameOfLife game = new GameOfLife("1111111111111111");
		game.updateState();
		assertEquals("1001000000001001", game.getStateString());
	}

	@Test
	void testInvalidInput() {
		String result;
		try {
			GameOfLife game = new GameOfLife("1111121111111111");
			game.updateState();
			result = game.getStateString();
		}catch(IllegalArgumentException e) {
			result = "caughtException";
		}
		Assertions.assertEquals("caughtException",result);
	}

	@Test
	void readInputStateTest() {
		String startingState = "100"+
								"010"+
								"001";
		GameOfLife game = new GameOfLife(startingState);
		assertEquals(startingState, game.getStateString());
	}
}
