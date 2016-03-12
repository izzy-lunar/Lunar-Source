package com.ownxile.rs2;

public class Zone {

	public int x, y, x1, y1;

	public Zone(int x, int x1, int y, int y1) {
		this.x = x;
		this.y = y;
		this.x1 = x1;
		this.y1 = y1;
	}

	public boolean inZone(Entity entity) {
		return entity.absX >= x && entity.absX <= x1 && entity.absY >= y
				&& entity.absY <= y1;
	}
}
