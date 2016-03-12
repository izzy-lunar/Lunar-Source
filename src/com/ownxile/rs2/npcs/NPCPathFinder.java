package com.ownxile.rs2.npcs;

import com.ownxile.rs2.Entity;
import com.ownxile.rs2.world.region.RegionManager;
import com.ownxile.rs2.world.region.Tile;
import com.ownxile.rs2.world.region.TileControl;
import com.ownxile.util.Misc;

public class NPCPathFinder {

	private static final int NORTH = 0, EAST = 1, SOUTH = 2, WEST = 3;

	public static void follow(NPC npc, Entity following) {

		/** the tiles the npc is occupying **/
		Tile[] npcTiles = TileControl.getTiles(npc);

		/** locations **/
		int[] npcLocation = TileControl.currentLocation(npc);
		int[] followingLocation = TileControl.currentLocation(following);

		/** test 4 movements **/
		boolean[] moves = new boolean[4];

		int dir = -1;// the direction to go.

		int distance = TileControl.calculateDistance(npc, following);

		if (distance > 16) {
			npc.following = null;
			return;
		}
		npc.turnNpc(following.absX, following.absY);
		if (distance > 1) {

			/** set all our moves to true **/
			for (int i = 0; i < moves.length; i++) {
				moves[i] = true;
			}

			/** remove false moves **/
			if (npcLocation[0] < followingLocation[0]) {
				moves[EAST] = true;
				moves[WEST] = false;
			} else if (npcLocation[0] > followingLocation[0]) {
				moves[WEST] = true;
				moves[EAST] = false;
			} else {
				moves[EAST] = false;
				moves[WEST] = false;
			}
			if (npcLocation[1] > followingLocation[1]) {
				moves[SOUTH] = true;
				moves[NORTH] = false;
			} else if (npcLocation[1] < followingLocation[1]) {
				moves[NORTH] = true;
				moves[SOUTH] = false;
			} else {
				moves[NORTH] = false;
				moves[SOUTH] = false;
			}
			for (Tile tiles : npcTiles) {
				if (tiles.getTile()[0] == following.getAbsX()) { // same x line
					moves[EAST] = false;
					moves[WEST] = false;
				} else if (tiles.getTile()[1] == following.getAbsY()) { // same
																		// y
																		// line
					moves[NORTH] = false;
					moves[SOUTH] = false;
				}
			}
			boolean[] blocked = new boolean[3];

			if (moves[NORTH] && moves[EAST]) {
				for (Tile tiles : npcTiles) {
					if (RegionManager.blockedNorth(tiles.getTileX(),
							tiles.getTileY(), tiles.getTileHeight())) {
						blocked[0] = true;
					}
					if (RegionManager.blockedEast(tiles.getTileX(),
							tiles.getTileY(), tiles.getTileHeight())) {
						blocked[1] = true;
					}
					if (RegionManager.blockedNorthEast(tiles.getTileX(),
							tiles.getTileY(), tiles.getTileHeight())) {
						blocked[2] = true;
					}
				}
				if (!blocked[2] && !blocked[0] && !blocked[1]) { // northeast
					dir = 2;
				} else if (!blocked[0]) { // north
					dir = 0;
				} else if (!blocked[1]) { // east
					dir = 4;
				}

			} else if (moves[NORTH] && moves[WEST]) {
				for (Tile tiles : npcTiles) {
					if (RegionManager.blockedNorth(tiles.getTileX(),
							tiles.getTileY(), tiles.getTileHeight())) {
						blocked[0] = true;
					}
					if (RegionManager.blockedWest(tiles.getTileX(),
							tiles.getTileY(), tiles.getTileHeight())) {
						blocked[1] = true;
					}
					if (RegionManager.blockedNorthWest(tiles.getTileX(),
							tiles.getTileY(), tiles.getTileHeight())) {
						blocked[2] = true;
					}
				}
				if (!blocked[2] && !blocked[0] && !blocked[1]) { // north-west
					dir = 14;
				} else if (!blocked[0]) { // north
					dir = 0;
				} else if (!blocked[1]) { // west
					dir = 12;
				}
			} else if (moves[SOUTH] && moves[EAST]) {
				for (Tile tiles : npcTiles) {
					if (RegionManager.blockedSouth(tiles.getTileX(),
							tiles.getTileY(), tiles.getTileHeight())) {
						blocked[0] = true;
					}
					if (RegionManager.blockedEast(tiles.getTileX(),
							tiles.getTileY(), tiles.getTileHeight())) {
						blocked[1] = true;
					}
					if (RegionManager.blockedSouthEast(tiles.getTileX(),
							tiles.getTileY(), tiles.getTileHeight())) {
						blocked[2] = true;
					}
				}
				if (!blocked[2] && !blocked[0] && !blocked[1]) { // south-east
					dir = 6;
				} else if (!blocked[0]) { // south
					dir = 8;
				} else if (!blocked[1]) { // east
					dir = 4;
				}
			} else if (moves[SOUTH] && moves[WEST]) {
				for (Tile tiles : npcTiles) {
					if (RegionManager.blockedSouth(tiles.getTileX(),
							tiles.getTileY(), tiles.getTileHeight())) {
						blocked[0] = true;
					}
					if (RegionManager.blockedWest(tiles.getTileX(),
							tiles.getTileY(), tiles.getTileHeight())) {
						blocked[1] = true;
					}
					if (RegionManager.blockedSouthWest(tiles.getTileX(),
							tiles.getTileY(), tiles.getTileHeight())) {
						blocked[2] = true;
					}
				}
				if (!blocked[2] && !blocked[0] && !blocked[1]) { // south-west
					dir = 10;
				} else if (!blocked[0]) { // south
					dir = 8;
				} else if (!blocked[1]) { // west
					dir = 12;
				}

			} else if (moves[NORTH]) {
				dir = 0;
				for (Tile tiles : npcTiles) {
					if (RegionManager.blockedNorth(tiles.getTileX(),
							tiles.getTileY(), tiles.getTileHeight())) {
						dir = -1;
					}
				}
			} else if (moves[EAST]) {
				dir = 4;
				for (Tile tiles : npcTiles) {
					if (RegionManager.blockedEast(tiles.getTileX(),
							tiles.getTileY(), tiles.getTileHeight())) {
						dir = -1;
					}
				}
			} else if (moves[SOUTH]) {
				dir = 8;
				for (Tile tiles : npcTiles) {
					if (RegionManager.blockedSouth(tiles.getTileX(),
							tiles.getTileY(), tiles.getTileHeight())) {
						dir = -1;
					}
				}
			} else if (moves[WEST]) {
				dir = 12;
				for (Tile tiles : npcTiles) {
					if (RegionManager.blockedWest(tiles.getTileX(),
							tiles.getTileY(), tiles.getTileHeight())) {
						dir = -1;
					}
				}
			}
		} else if (distance == 0) {
			for (int i = 0; i < moves.length; i++) {
				moves[i] = true;
			}
			for (Tile tiles : npcTiles) {

				if (RegionManager.blockedNorth(tiles.getTileX(),
						tiles.getTileY(), tiles.getTileHeight())) {
					moves[NORTH] = false;
				}
				if (RegionManager.blockedEast(tiles.getTileX(),
						tiles.getTileY(), tiles.getTileHeight())) {
					moves[EAST] = false;
				}
				if (RegionManager.blockedSouth(tiles.getTileX(),
						tiles.getTileY(), tiles.getTileHeight())) {
					moves[SOUTH] = false;
				}
				if (RegionManager.blockedWest(tiles.getTileX(),
						tiles.getTileY(), tiles.getTileHeight())) {
					moves[WEST] = false;
				}
			}
			int randomSelection = Misc.random(3);

			if (moves[randomSelection]) {
				dir = randomSelection * 4;
			} else if (moves[NORTH]) {
				dir = 0;
			} else if (moves[EAST]) {
				dir = 4;
			} else if (moves[SOUTH]) {
				dir = 8;
			} else if (moves[WEST]) {
				dir = 12;
			}
		}
		if (dir == -1) {
			return;
		}
		dir >>= 1;

		if (dir < 0) {
			return;
		}
		int x = Misc.directionDeltaX[dir];
		int y = Misc.directionDeltaY[dir];
		// npc.setWalk(npcLocation[0] + x, npcLocation[1] + y, false);
		npc.moveX = x;
		npc.moveY = y;
		// npc.getNextNPCMovement(npc.get)
		// npc.walkTo(npcLocation[0] + x, npcLocation[1] + y);
	}

}