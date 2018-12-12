package tgm.gameoflife.mine.jobqueue;

import tgm.gameoflife.*;
import tgm.gameoflife.mine.MineGameOfLifeUtils;
import tgm.utils.ThreadManager;
import tgm.utils.WorkerThread;

import java.util.ArrayList;

public class ResultFilterThread extends WorkerThread<ResultThreadJob> {

	public ResultFilterThread(ThreadManager manager) {
		super(manager);
	}


	@Override
	protected ResultThreadResult processJob(ResultThreadJob job) {

		ArrayList<GameResult> filtered = new ArrayList<>();
		MineGameOfLifeUtils.getInstance().combine(filtered, job.getInput());

		return new ResultThreadResult(filtered);
	}
}
