package tgm.gameoflife;

/**
 * The purpose of this object is to find inputs to the Game of Life which
 * result in these states: DEAD, STEADY, LOOP. The game must reach the state
 * within
 */
public class MineGameOfLife {

	private boolean isDead(String state) {
		return state.indexOf(GameOfLife.ALIVE_STATE) == -1;
	}
}

enum GameResultType { DEAD, STEADY, LOOP }
class GameResult {
	GameResultType type;
	String initialState;
	int iterationsInLoop;
	int iterationCount;
}
