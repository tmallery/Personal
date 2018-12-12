package tgm.gameoflife.mine.multithreaded;

import tgm.utils.ThreadFactory;
import tgm.utils.ThreadManager;

public class MinerThreadFactory implements ThreadFactory<MinerThread> {
	@Override
	public MinerThread newThread(ThreadManager manager) {
		return new MinerThread(manager);
	}
}
