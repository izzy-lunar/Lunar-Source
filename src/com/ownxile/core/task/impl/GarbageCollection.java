package com.ownxile.core.task.impl;

import com.ownxile.core.task.Task;

public class GarbageCollection extends Task {

	public GarbageCollection(int delay) {
		super(delay);
	}

	@Override
	protected void execute() {
		System.gc();
		System.runFinalization();
	}
}
