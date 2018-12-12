package tgm.gameoflife.mine.multithreaded;

import tgm.gameoflife.GameResult;
import tgm.gameoflife.mine.MineGameOfLife;
import tgm.utils.Log;
import tgm.utils.ThreadResult;
import tgm.utils.WorkerThread;
import tgm.utils.ThreadManager;

import java.util.ArrayList;

public class MinerThread extends WorkerThread<MinerJob> {

	private static final boolean DEBUG = false;
	private static final int ITERATION_MAX = 5;

	private final MineGameOfLife m_miner;

	public MinerThread(ThreadManager manager) {
		super(manager);

		m_miner = new MineGameOfLife();
	}

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


//				if( result.type == GameResultType.LOOP ) {
//					boolean unique = true;
//					outer:
//					for (GameResult otherResult : results) {
//
//						byte[][] otherCellState = GameOfLife.parseSquareState(otherResult.resultState);
//
//						for (int idx = result.loopStartIdx; idx < result.states.size(); idx++) {
//							//if(m_running == false) {Log.debug("on idx " + idx + " of " + result.states.size()); }
//							String stateStr = result.states.get(idx);
//							byte[][] cellState = GameOfLife.parseSquareState(stateStr);
//							if (MineGameOfLifeUtils.getInstance().matches(otherCellState, cellState)) {
//								unique = false;
//								break outer;
//							}
//						}
//					}
//					if (unique) {
			results.add(result);
//					}
//				}
		}

		//ResultThreadManager.saveAllResults(results);

		if(DEBUG) Log.debug("GameRunnerThread: Finished Job, found " + results.size());
		return new MineResult(results);
	}
}
