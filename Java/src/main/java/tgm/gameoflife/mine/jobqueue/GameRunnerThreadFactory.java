package tgm.gameoflife.mine.jobqueue;

import tgm.utils.ThreadFactory;
import tgm.utils.ThreadManager;

public class GameRunnerThreadFactory implements ThreadFactory<GameRunnerThread> {

	@Override
	public GameRunnerThread newThread(ThreadManager manager) {
		return new GameRunnerThread(manager);
	}
}
