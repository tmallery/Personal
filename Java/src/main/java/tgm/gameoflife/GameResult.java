package tgm.gameoflife;

import java.util.ArrayList;

/**
 * Structure to hold a game result
 */
public class GameResult {
	public GameResultType type = null;
	public String initialState = null;
	public int loopStartIdx= -1;
	public int iterationCount = 0;
	public ArrayList<String> states = null;
	public String resultState = null;
}
