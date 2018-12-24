package tgm.gameoflife.mine.multithreaded;

import tgm.gameoflife.GameResult;
import tgm.gameoflife.mine.MineGameOfLife;
import tgm.utils.Log;
import tgm.utils.ThreadResult;
import tgm.utils.WorkerThread;
import tgm.utils.ThreadManager;

import java.util.ArrayList;

/**
 * Worker bee for running iterations of the GameOfLife.
 */
public class MinerThread extends WorkerThread<MinerJob> {

	private static final boolean DEBUG = false;
	private static final int ITERATION_MAX = 5;

	private final MineGameOfLife m_miner;

	public MinerThread(ThreadManager manager) {
		super(manager);

		m_miner = new MineGameOfLife();
	}

	/**
	 * Runs through each of the inputs for this job and returns the results.
	 */
	@Override
	protected ThreadResult processJob(MinerJob job) {
		StringBuilder sb = new StringBuilder();
		ArrayList<GameResult> results = new ArrayList<>(job.getInput().size());
		for(ArrayList<Byte> bytelist : job.getInput())
		{
			sb.setLength(0);
			for(Byte b : bytelist ) {
				sb.append(b);
			}

			GameResult result = m_miner.mine(sb.toString(), ITERATION_MAX);

			results.add(result);
		}

		if(DEBUG) Log.debug("GameRunnerThread: Finished Job, found " + results.size());
		return new MineResult(results);
	}
}
