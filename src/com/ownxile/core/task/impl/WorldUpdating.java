package com.ownxile.core.task.impl;

import com.ownxile.core.World;
import com.ownxile.core.task.Task;
import com.ownxile.rs2.items.GroundItemHandler;
import com.ownxile.rs2.world.shops.ShopBuilder;

public class WorldUpdating extends Task {

	public WorldUpdating() {
		super();
	}

	boolean cycleDelay = true;

	protected void execute() {
		World.getObjectHandler().tick();
		World.getObjectManager().tick();
		GroundItemHandler.tick();
		ShopBuilder.tick();
		World.getCastleWars().tick();
		cycleDelay = !cycleDelay;
		if (cycleDelay)
			World.getPestControl().tick();
	}

}
