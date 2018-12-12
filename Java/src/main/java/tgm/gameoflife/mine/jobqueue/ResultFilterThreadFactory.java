package tgm.gameoflife.mine.jobqueue;

import tgm.utils.ThreadFactory;
import tgm.utils.ThreadManager;

public class ResultFilterThreadFactory implements ThreadFactory<ResultFilterThread> {
	@Override
	public ResultFilterThread newThread(ThreadManager manager) {
		return new ResultFilterThread(manager);
	}
}
