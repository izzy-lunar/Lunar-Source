package com.ownxile.rs2.world.region;

import com.ownxile.core.World;
import com.ownxile.rs2.Entity;
import com.ownxile.rs2.npcs.NPC;

public class TileControl {

	public static int calculateDistance(Entity entity, Entity following) {

		Tile[] tiles = getTiles(entity);

		int[] location = currentLocation(entity);
		int[] pointer = new int[tiles.length];

		int lowestCount = 20, count = 0;

		for (Tile newTiles : tiles) {
			if (newTiles.getTile() == location) {
				pointer[count++] = 0;
			} else {
				pointer[count++] = calculateDistance(newTiles, following);
			}
		}
		for (int element : pointer) {
			if (element < lowestCount) {
				lowestCount = element;
			}
		}

		return lowestCount;
	}

	public static int calculateDistance(int[] location, Entity other) {
		int X = Math.abs(location[0] - other.getAbsX());
		int Y = Math.abs(location[1] - other.getAbsY());
		return X > Y ? X : Y;
	}

	public static int calculateDistance(int[] location, int[] other) {
		int X = Math.abs(location[0] - other[0]);
		int Y = Math.abs(location[1] - other[1]);
		return X > Y ? X : Y;
	}

	public static int calculateDistance(Tile location, Entity other) {
		int X = Math.abs(location.getTile()[0] - other.getAbsX());
		int Y = Math.abs(location.getTile()[1] - other.getAbsY());
		return X > Y ? X : Y;
	}

	public static int[] currentLocation(Entity entity) {
		int[] currentLocation = new int[3];
		if (entity != null) {
			currentLocation[0] = entity.getAbsX();
			currentLocation[1] = entity.getAbsY();
			currentLocation[2] = entity.getHeightLevel();
		}
		return currentLocation;
	}

	public static int[] currentLocation(Tile tileLocation) {

		int[] currentLocation = new int[3];

		if (tileLocation != null) {
			currentLocation[0] = tileLocation.getTile()[0];
			currentLocation[1] = tileLocation.getTile()[1];
			currentLocation[2] = tileLocation.getTile()[2];
		}
		return currentLocation;
	}

	public static Tile generate(int x, int y, int z) {
		return new Tile(x, y, z);
	}

	public static Tile[] getTiles(Entity entity) {

		int size = 1, tileCount = 0;

		if (entity instanceof NPC) {
			size = World.getNpcHandler().getNPCSize(((NPC) entity).npcId);
			// size = 1;
		}
		Tile[] tiles = new Tile[size * size];

		if (tiles.length == 1) {
			tiles[0] = generate(entity.getAbsX(), entity.getAbsY(),
					entity.getHeightLevel());
		} else {
			for (int x = 0; x < size; x++) {
				for (int y = 0; y < size; y++) {
					tiles[tileCount++] = generate(entity.getAbsX() + x,
							entity.getAbsY() + y, entity.getHeightLevel());
				}
			}
		}
		return tiles;
	}

	public static Tile[] getTiles(Entity entity, int[] location) {

		int size = 1, tileCount = 0;

		if (entity instanceof NPC) {
			size = World.getNpcHandler().getNPCSize(((NPC) entity).npcId);
		}

		Tile[] tiles = new Tile[size * size];

		if (tiles.length == 1) {
			tiles[0] = generate(location[0], location[1], location[2]);
		} else {
			for (int x = 0; x < size; x++) {
				for (int y = 0; y < size; y++) {
					tiles[tileCount++] = generate(location[0] + x, location[1]
							+ y, location[2]);
				}
			}
		}
		return tiles;
	}
}
