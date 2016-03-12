package com.ownxile.util.time;

public class SimpleTimer {

	private long cachedTime;

	public SimpleTimer() {
		reset();
	}

	public long elapsed() {
		return System.currentTimeMillis() - cachedTime;
	}

	public void reset() {
		cachedTime = System.currentTimeMillis();
	}
}
