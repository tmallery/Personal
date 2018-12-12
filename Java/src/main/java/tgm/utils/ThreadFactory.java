package tgm.utils;

import tgm.utils.ThreadManager;

public interface ThreadFactory<T> {
	T newThread(ThreadManager manager);
}
