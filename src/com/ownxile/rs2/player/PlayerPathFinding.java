package com.ownxile.rs2.player;

import java.util.LinkedList;

import com.ownxile.rs2.npcs.NPC;
import com.ownxile.rs2.world.region.RegionManager;

public class PlayerPathFinding {

	private static final PlayerPathFinding pathFinder = new PlayerPathFinding();

	public static PlayerPathFinding getPathFinder() {
		return pathFinder;
	}

	public void findRoute(Client player, int destX, int destY,
			boolean moveNear, int xLength, int yLength) {
		if (destX == player.getLocalX() && destY == player.getLocalY()
				&& !moveNear) {
			return;
		}
		destX = destX - 8 * player.getMapRegionX();
		destY = destY - 8 * player.getMapRegionY();
		final int[][] via = new int[104][104];
		final int[][] cost = new int[104][104];
		final LinkedList<Integer> tileQueueX = new LinkedList<Integer>();
		final LinkedList<Integer> tileQueueY = new LinkedList<Integer>();
		for (int xx = 0; xx < 104; xx++) {
			for (int yy = 0; yy < 104; yy++) {
				cost[xx][yy] = 99999999;
			}
		}
		int curX = player.getLocalX();
		int curY = player.getLocalY();
		via[curX][curY] = 99;
		cost[curX][curY] = 0;
		int tail = 0;
		tileQueueX.add(curX);
		tileQueueY.add(curY);
		boolean foundPath = false;
		final int pathLength = 4000;
		while (tail != tileQueueX.size() && tileQueueX.size() < pathLength) {
			curX = tileQueueX.get(tail);
			curY = tileQueueY.get(tail);
			final int curAbsX = player.getMapRegionX() * 8 + curX;
			final int curAbsY = player.getMapRegionY() * 8 + curY;
			if (curX == destX && curY == destY) {
				foundPath = true;
				break;
			}
			tail = (tail + 1) % pathLength;
			final int thisCost = cost[curX][curY] + 1;
			if (curY > 0
					&& via[curX][curY - 1] == 0
					&& (RegionManager.getClipping(curAbsX, curAbsY - 1,
							player.absZ) & 0x1280102) == 0) {
				tileQueueX.add(curX);
				tileQueueY.add(curY - 1);
				via[curX][curY - 1] = 1;
				cost[curX][curY - 1] = thisCost;
			}
			if (curX > 0
					&& via[curX - 1][curY] == 0
					&& (RegionManager.getClipping(curAbsX - 1, curAbsY,
							player.absZ) & 0x1280108) == 0) {
				tileQueueX.add(curX - 1);
				tileQueueY.add(curY);
				via[curX - 1][curY] = 2;
				cost[curX - 1][curY] = thisCost;
			}
			if (curY < 104 - 1
					&& via[curX][curY + 1] == 0
					&& (RegionManager.getClipping(curAbsX, curAbsY + 1,
							player.absZ) & 0x1280120) == 0) {
				tileQueueX.add(curX);
				tileQueueY.add(curY + 1);
				via[curX][curY + 1] = 4;
				cost[curX][curY + 1] = thisCost;
			}
			if (curX < 104 - 1
					&& via[curX + 1][curY] == 0
					&& (RegionManager.getClipping(curAbsX + 1, curAbsY,
							player.absZ) & 0x1280180) == 0) {
				tileQueueX.add(curX + 1);
				tileQueueY.add(curY);
				via[curX + 1][curY] = 8;
				cost[curX + 1][curY] = thisCost;
			}
			if (curX > 0
					&& curY > 0
					&& via[curX - 1][curY - 1] == 0
					&& (RegionManager.getClipping(curAbsX - 1, curAbsY - 1,
							player.absZ) & 0x128010e) == 0
					&& (RegionManager.getClipping(curAbsX - 1, curAbsY,
							player.absZ) & 0x1280108) == 0
					&& (RegionManager.getClipping(curAbsX, curAbsY - 1,
							player.absZ) & 0x1280102) == 0) {
				tileQueueX.add(curX - 1);
				tileQueueY.add(curY - 1);
				via[curX - 1][curY - 1] = 3;
				cost[curX - 1][curY - 1] = thisCost;
			}
			if (curX > 0
					&& curY < 104 - 1
					&& via[curX - 1][curY + 1] == 0
					&& (RegionManager.getClipping(curAbsX - 1, curAbsY + 1,
							player.absZ) & 0x1280138) == 0
					&& (RegionManager.getClipping(curAbsX - 1, curAbsY,
							player.absZ) & 0x1280108) == 0
					&& (RegionManager.getClipping(curAbsX, curAbsY + 1,
							player.absZ) & 0x1280120) == 0) {
				tileQueueX.add(curX - 1);
				tileQueueY.add(curY + 1);
				via[curX - 1][curY + 1] = 6;
				cost[curX - 1][curY + 1] = thisCost;
			}
			if (curX < 104 - 1
					&& curY > 0
					&& via[curX + 1][curY - 1] == 0
					&& (RegionManager.getClipping(curAbsX + 1, curAbsY - 1,
							player.absZ) & 0x1280183) == 0
					&& (RegionManager.getClipping(curAbsX + 1, curAbsY,
							player.absZ) & 0x1280180) == 0
					&& (RegionManager.getClipping(curAbsX, curAbsY - 1,
							player.absZ) & 0x1280102) == 0) {
				tileQueueX.add(curX + 1);
				tileQueueY.add(curY - 1);
				via[curX + 1][curY - 1] = 9;
				cost[curX + 1][curY - 1] = thisCost;
			}
			if (curX < 104 - 1
					&& curY < 104 - 1
					&& via[curX + 1][curY + 1] == 0
					&& (RegionManager.getClipping(curAbsX + 1, curAbsY + 1,
							player.absZ) & 0x12801e0) == 0
					&& (RegionManager.getClipping(curAbsX + 1, curAbsY,
							player.absZ) & 0x1280180) == 0
					&& (RegionManager.getClipping(curAbsX, curAbsY + 1,
							player.absZ) & 0x1280120) == 0) {
				tileQueueX.add(curX + 1);
				tileQueueY.add(curY + 1);
				via[curX + 1][curY + 1] = 12;
				cost[curX + 1][curY + 1] = thisCost;
			}
		}
		if (!foundPath) {
			if (moveNear) {
				int i_223_ = 1000;
				int thisCost = 100;
				final int i_225_ = 10;
				for (int x = destX - i_225_; x <= destX + i_225_; x++) {
					for (int y = destY - i_225_; y <= destY + i_225_; y++) {
						if (x >= 0 && y >= 0 && x < 104 && y < 104
								&& cost[x][y] < 100) {
							int i_228_ = 0;
							if (x < destX) {
								i_228_ = destX - x;
							} else if (x > destX + xLength - 1) {
								i_228_ = x - (destX + xLength - 1);
							}
							int i_229_ = 0;
							if (y < destY) {
								i_229_ = destY - y;
							} else if (y > destY + yLength - 1) {
								i_229_ = y - (destY + yLength - 1);
							}
							final int i_230_ = i_228_ * i_228_ + i_229_
									* i_229_;
							if (i_230_ < i_223_ || i_230_ == i_223_
									&& cost[x][y] < thisCost) {
								i_223_ = i_230_;
								thisCost = cost[x][y];
								curX = x;
								curY = y;
							}
						}
					}
				}
				if (i_223_ == 1000) {
					return;
				}
			} else {
				return;
			}
		}
		tail = 0;
		tileQueueX.set(tail, curX);
		tileQueueY.set(tail++, curY);
		int l5;
		for (int j5 = l5 = via[curX][curY]; curX != player.getLocalX()
				|| curY != player.getLocalY(); j5 = via[curX][curY]) {
			if (j5 != l5) {
				l5 = j5;
				tileQueueX.set(tail, curX);
				tileQueueY.set(tail++, curY);
			}
			if ((j5 & 2) != 0) {
				curX++;
			} else if ((j5 & 8) != 0) {
				curX--;
			}
			if ((j5 & 1) != 0) {
				curY++;
			} else if ((j5 & 4) != 0) {
				curY--;
			}
		}
		player.resetWalkingQueue();
		final int size = tail--;
		int pathX = player.getMapRegionX() * 8 + tileQueueX.get(tail);
		int pathY = player.getMapRegionY() * 8 + tileQueueY.get(tail);
		player.addToWalkingQueue(localize(pathX, player.getMapRegionX()),
				localize(pathY, player.getMapRegionY()));
		for (int i = 1; i < size; i++) {
			tail--;
			pathX = player.getMapRegionX() * 8 + tileQueueX.get(tail);
			pathY = player.getMapRegionY() * 8 + tileQueueY.get(tail);
			player.addToWalkingQueue(localize(pathX, player.getMapRegionX()),
					localize(pathY, player.getMapRegionY()));
		}
	}

	public void findRoute(NPC npc, int index, int destX, int destY,
			boolean moveNear, int xLength, int yLength) {
		if (destX == npc.getLocalX() && destY == npc.getLocalY() && !moveNear) {
			return;
		}
		destX = destX - 8 * npc.getRegionX();
		destY = destY - 8 * npc.getRegionY();
		final int[][] via = new int[104][104];
		final int[][] cost = new int[104][104];
		final LinkedList<Integer> tileQueueX = new LinkedList<Integer>();
		final LinkedList<Integer> tileQueueY = new LinkedList<Integer>();
		for (int xx = 0; xx < 104; xx++) {
			for (int yy = 0; yy < 104; yy++) {
				cost[xx][yy] = 99999999;
			}
		}
		int curX = npc.getLocalX();
		int curY = npc.getLocalY();
		via[curX][curY] = 99;
		cost[curX][curY] = 0;
		int tail = 0;
		tileQueueX.add(curX);
		tileQueueY.add(curY);
		boolean foundPath = false;
		final int pathLength = 4000;
		while (tail != tileQueueX.size() && tileQueueX.size() < pathLength) {
			curX = tileQueueX.get(tail);
			curY = tileQueueY.get(tail);
			final int curAbsX = npc.getRegionX() * 8 + curX;
			final int curAbsY = npc.getRegionY() * 8 + curY;
			if (curX == destX && curY == destY) {
				foundPath = true;
				break;
			}
			tail = (tail + 1) % pathLength;
			final int thisCost = cost[curX][curY] + 1;
			if (curY > 0
					&& via[curX][curY - 1] == 0
					&& (RegionManager.getClipping(curAbsX, curAbsY - 1,
							npc.absZ) & 0x1280102) == 0) {
				tileQueueX.add(curX);
				tileQueueY.add(curY - 1);
				via[curX][curY - 1] = 1;
				cost[curX][curY - 1] = thisCost;
			}
			if (curX > 0
					&& via[curX - 1][curY] == 0
					&& (RegionManager.getClipping(curAbsX - 1, curAbsY,
							npc.absZ) & 0x1280108) == 0) {
				tileQueueX.add(curX - 1);
				tileQueueY.add(curY);
				via[curX - 1][curY] = 2;
				cost[curX - 1][curY] = thisCost;
			}
			if (curY < 104 - 1
					&& via[curX][curY + 1] == 0
					&& (RegionManager.getClipping(curAbsX, curAbsY + 1,
							npc.absZ) & 0x1280120) == 0) {
				tileQueueX.add(curX);
				tileQueueY.add(curY + 1);
				via[curX][curY + 1] = 4;
				cost[curX][curY + 1] = thisCost;
			}
			if (curX < 104 - 1
					&& via[curX + 1][curY] == 0
					&& (RegionManager.getClipping(curAbsX + 1, curAbsY,
							npc.absZ) & 0x1280180) == 0) {
				tileQueueX.add(curX + 1);
				tileQueueY.add(curY);
				via[curX + 1][curY] = 8;
				cost[curX + 1][curY] = thisCost;
			}
			if (curX > 0
					&& curY > 0
					&& via[curX - 1][curY - 1] == 0
					&& (RegionManager.getClipping(curAbsX - 1, curAbsY - 1,
							npc.absZ) & 0x128010e) == 0
					&& (RegionManager.getClipping(curAbsX - 1, curAbsY,
							npc.absZ) & 0x1280108) == 0
					&& (RegionManager.getClipping(curAbsX, curAbsY - 1,
							npc.absZ) & 0x1280102) == 0) {
				tileQueueX.add(curX - 1);
				tileQueueY.add(curY - 1);
				via[curX - 1][curY - 1] = 3;
				cost[curX - 1][curY - 1] = thisCost;
			}
			if (curX > 0
					&& curY < 104 - 1
					&& via[curX - 1][curY + 1] == 0
					&& (RegionManager.getClipping(curAbsX - 1, curAbsY + 1,
							npc.absZ) & 0x1280138) == 0
					&& (RegionManager.getClipping(curAbsX - 1, curAbsY,
							npc.absZ) & 0x1280108) == 0
					&& (RegionManager.getClipping(curAbsX, curAbsY + 1,
							npc.absZ) & 0x1280120) == 0) {
				tileQueueX.add(curX - 1);
				tileQueueY.add(curY + 1);
				via[curX - 1][curY + 1] = 6;
				cost[curX - 1][curY + 1] = thisCost;
			}
			if (curX < 104 - 1
					&& curY > 0
					&& via[curX + 1][curY - 1] == 0
					&& (RegionManager.getClipping(curAbsX + 1, curAbsY - 1,
							npc.absZ) & 0x1280183) == 0
					&& (RegionManager.getClipping(curAbsX + 1, curAbsY,
							npc.absZ) & 0x1280180) == 0
					&& (RegionManager.getClipping(curAbsX, curAbsY - 1,
							npc.absZ) & 0x1280102) == 0) {
				tileQueueX.add(curX + 1);
				tileQueueY.add(curY - 1);
				via[curX + 1][curY - 1] = 9;
				cost[curX + 1][curY - 1] = thisCost;
			}
			if (curX < 104 - 1
					&& curY < 104 - 1
					&& via[curX + 1][curY + 1] == 0
					&& (RegionManager.getClipping(curAbsX + 1, curAbsY + 1,
							npc.absZ) & 0x12801e0) == 0
					&& (RegionManager.getClipping(curAbsX + 1, curAbsY,
							npc.absZ) & 0x1280180) == 0
					&& (RegionManager.getClipping(curAbsX, curAbsY + 1,
							npc.absZ) & 0x1280120) == 0) {
				tileQueueX.add(curX + 1);
				tileQueueY.add(curY + 1);
				via[curX + 1][curY + 1] = 12;
				cost[curX + 1][curY + 1] = thisCost;
			}
		}
		if (!foundPath) {
			if (moveNear) {
				int i_223_ = 1000;
				int thisCost = 100;
				final int i_225_ = 10;
				for (int x = destX - i_225_; x <= destX + i_225_; x++) {
					for (int y = destY - i_225_; y <= destY + i_225_; y++) {
						if (x >= 0 && y >= 0 && x < 104 && y < 104
								&& cost[x][y] < 100) {
							int i_228_ = 0;
							if (x < destX) {
								i_228_ = destX - x;
							} else if (x > destX + xLength - 1) {
								i_228_ = x - (destX + xLength - 1);
							}
							int i_229_ = 0;
							if (y < destY) {
								i_229_ = destY - y;
							} else if (y > destY + yLength - 1) {
								i_229_ = y - (destY + yLength - 1);
							}
							final int i_230_ = i_228_ * i_228_ + i_229_
									* i_229_;
							if (i_230_ < i_223_ || i_230_ == i_223_
									&& cost[x][y] < thisCost) {
								i_223_ = i_230_;
								thisCost = cost[x][y];
								curX = x;
								curY = y;
							}
						}
					}
				}
				if (i_223_ == 1000) {
					return;
				}
			} else {
				return;
			}
		}
		tail = 0;
		tileQueueX.set(tail, curX);
		tileQueueY.set(tail++, curY);
		int l5;
		for (int j5 = l5 = via[curX][curY]; curX != npc.getLocalX()
				|| curY != npc.getLocalY(); j5 = via[curX][curY]) {
			if (j5 != l5) {
				l5 = j5;
				tileQueueX.set(tail, curX);
				tileQueueY.set(tail++, curY);
			}
			if ((j5 & 2) != 0) {
				curX++;
			} else if ((j5 & 8) != 0) {
				curX--;
			}
			if ((j5 & 1) != 0) {
				curY++;
			} else if ((j5 & 4) != 0) {
				curY--;
			}
		}
		final int size = tail--;
		final int pathX = npc.getRegionX() * 8 + tileQueueX.get(tail);
		final int pathY = npc.getRegionY() * 8 + tileQueueY.get(tail);
		npc.moveX = pathX;
		npc.moveY = pathY;
		npc.getNextNPCMovement(index);
		npc.getUpdateFlags().updateRequired = true;
		for (int i = 1; i < size; i++) {
			tail--;
			npc.moveX = npc.getRegionX() * 8 + tileQueueX.get(tail);
			npc.moveY = npc.getRegionY() * 8 + tileQueueY.get(tail);
			npc.getNextNPCMovement(index);
			npc.getUpdateFlags().updateRequired = true;
		}
	}

	public int localize(int x, int mapRegion) {
		return x - 8 * mapRegion;
	}

}
