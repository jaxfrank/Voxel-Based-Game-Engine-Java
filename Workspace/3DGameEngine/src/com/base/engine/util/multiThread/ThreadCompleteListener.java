package com.base.engine.util.multiThread;

public interface ThreadCompleteListener {
	void notifyOfThreadComplete(final Runnable thread);
}
