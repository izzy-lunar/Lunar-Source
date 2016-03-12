package com.ownxile.core.task.impl;

import com.ownxile.core.World;
import com.ownxile.core.task.Task;

public class EntityUpdating extends Task {

	public EntityUpdating() {
		super();
	}

	@Override
	protected void execute() {
		World.getPlayerHandler().tick();
		World.getNpcHandler().tick();

	}

}
