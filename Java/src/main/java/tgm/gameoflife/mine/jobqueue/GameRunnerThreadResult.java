package tgm.gameoflife.mine.jobqueue;

import tgm.gameoflife.GameResult;
import tgm.utils.ThreadResult;

import java.util.ArrayList;

public class GameRunnerThreadResult implements ThreadResult {
	private ArrayList<GameResult> m_results;
	public GameRunnerThreadResult(ArrayList<GameResult> results) {
		m_results = results;
	}

	public ArrayList<GameResult> getResults() {
		return m_results;
	}
}
