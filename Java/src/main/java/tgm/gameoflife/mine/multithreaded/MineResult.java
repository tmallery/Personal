package tgm.gameoflife.mine.multithreaded;

import tgm.gameoflife.GameResult;
import tgm.utils.ThreadResult;

import java.util.ArrayList;

public class MineResult implements ThreadResult {
	private ArrayList<GameResult> m_results;
	public MineResult(ArrayList<GameResult> results) {
		m_results = results;
	}

	public ArrayList<GameResult> getFilteredResults() {
		return m_results;
	}
}
