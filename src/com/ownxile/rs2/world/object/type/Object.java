package com.ownxile.rs2.world.object.type;

import com.ownxile.core.World;

public class Object {

	public int face;
	public int height;
	public int newId;
	public int objectId;
	public int objectX;
	public int objectY;
	public int tick;
	public int type;

	public Object(int id, int x, int y, int height, int face, int type,
			int newId, int ticks) {
		objectId = id;
		objectX = x;
		objectY = y;
		this.height = height;
		this.face = face;
		this.type = type;
		this.newId = newId;
		tick = ticks;
		World.getObjectManager().addObject(this);
	}

}