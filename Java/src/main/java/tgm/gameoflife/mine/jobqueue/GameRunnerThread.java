package tgm.gameoflife.mine.jobqueue;

import tgm.gameoflife.*;
import tgm.gameoflife.mine.MineGameOfLife;
import tgm.utils.Log;
import tgm.utils.ThreadManager;
import tgm.utils.ThreadResult;
import tgm.utils.WorkerThread;

import java.util.ArrayList;

/**
 * MinerThreads responsibilities
 *  - process a set of input
 *  - save results
 *  - notify manager of completion
 */
public class GameRunnerThread extends WorkerThread<GameRunnerThreadJob> {
	private static final boolean DEBUG = false;

	private MineGameOfLife m_miner;

	public GameRunnerThread(ThreadManager manager) {
		super(manager);

		m_miner = new MineGameOfLife();
	}



	@Override
	public ThreadResult processJob(GameRunnerThreadJob job) {
		if(DEBUG) Log.debug("GameRunnerThread: processing job");

			StringBuilder sb = new StringBuilder();
			ArrayList<GameResult> results = new ArrayList<>(job.getInput().size());
			for(ArrayList<Byte> bytelist : job.getInput())
			{
				sb.setLength(0);
				for(Byte b : bytelist ) {
					sb.append(b);
				}

				GameResult result = m_miner.mine(sb.toString(), 5);


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


		return new GameRunnerThreadResult(results);
	}
}
