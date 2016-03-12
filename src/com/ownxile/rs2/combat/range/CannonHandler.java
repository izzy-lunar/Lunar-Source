package com.ownxile.rs2.combat.range;

import java.util.ArrayList;
import java.util.List;
import com.ownxile.core.World;
import com.ownxile.core.task.Task;
import com.ownxile.rs2.npcs.NPC;
import com.ownxile.rs2.npcs.NPCHandler;
import com.ownxile.rs2.player.Client;
import com.ownxile.rs2.player.PlayerHandler;
import com.ownxile.util.Misc;

public class CannonHandler {

	private static final int CANNON_BALLS = 2;
	public static List<Cannon> cannons = new ArrayList<Cannon>();
	private static final int MAX_HIT = 30;
	private static final int SHOOTING_DISTANCE = 10;

	private static final int XP_PER_DAMAGE = 100;

	public static void addBalls(Client user, Cannon cannon) {
		if (user != cannon.getOwner()) {
			user.sendMessage("This cannon belongs to "
					+ cannon.getOwner().playerName + ".");
		} else if (!user.getItems().playerHasItem(CANNON_BALLS, 1)) {
			user.sendMessage("You need some cannon balls to do this action.");
		} else if (cannon.getBalls() > 29) {
			user.sendMessage("This cannon cannot store anymore cannonballs.");
		} else {
			int playerAmount = user.getItems().getItemAmount(CANNON_BALLS);
			int cannonAmount = cannon.getBalls();
			int freeSlots = 30 - cannonAmount;
			if (playerAmount >= freeSlots) {
				user.getItems().deleteItem2(CANNON_BALLS, freeSlots);
				cannon.addBalls(freeSlots);
				user.sendMessage("You load the cannon with " + freeSlots
						+ " cannonballs.");
			} else if (cannonAmount > playerAmount) {
				user.getItems().deleteItem2(CANNON_BALLS, playerAmount);
				cannon.addBalls(playerAmount);
				user.sendMessage("You load the cannon with " + playerAmount
						+ " cannonballs.");
			}
		}

	}

	public static void addCannon(Cannon cannon) {
		cannons.add(cannon);
		World.getObjectHandler().globalObject(6, cannon.getX(), cannon.getY(),
				cannon.getHeight(), 10);

	}

	public static boolean canHitNpc(Cannon c, NPC npc) {
		int i = c.getState().getId();
		int xRange = npc.getX() - c.getX();
		int yRange = npc.getY() - c.getY();
		switch (i - 1) {
		case -1:
			return xRange < 0 && yRange == 0;
		case 0:
			return xRange < 0 && yRange > 0;
		case 1:
			return xRange == 0 && yRange > 0;
		case 2:
			return xRange > 0 && yRange > 0;
		case 3:
			return xRange > 0 && yRange == 0;
		case 4:
			return xRange > 0 && yRange < 0;
		case 5:
			return xRange == 0 && yRange < 0;
		case 6:
			return xRange < 0 && yRange < 0;
		}
		return false;

	}

	public static void changeRegion(Client c) {
		for (Cannon cannon : cannons) {
			if (cannon != null)
				c.getFunction().addObject(6, cannon.getX(), cannon.getY(),
						cannon.getHeight(), 0, 10);
		}
	}

	public static void fireCannon(final Cannon c) {
		if (c.getBalls() == 0) {
			c.getOwner().sendMessage("Your cannon has run out of cannonballs.");
			return;
		}
		if (!c.getOwner().inMulti()) {
			return;
		}
		getNearbyNpcs(c);
		if (c.nearbyNpcs.size() > 0) {
			for (int i = 0; i < c.nearbyNpcs.size(); i++) {

				final NPC npc = NPCHandler.npcs[c.nearbyNpcs.get(i)];
				if (canHitNpc(c, npc) && c.getBalls() > 0) {
					final int offY = (c.getX() + 1 - npc.getX()) * -1;
					final int offX = (c.getY() + 1 - npc.getY()) * -1;
					c.getOwner()
							.getFunction()
							.createPlayersProjectile(c.getX() + 1,
									c.getY() + 1, offX, offY, 50, 100, 53, 37,
									37, 0, 2);
					c.getOwner().cannonTask = new Task(2, false) {
						@Override
						protected void execute() {
							int hit = Misc.random(MAX_HIT);
							if (hit > npc.HP) {
								hit = npc.HP;
							}
							npc.handleHitMask(hit);
							npc.HP -= hit;
							npc.underAttack = true;
							npc.killerId = c.getOwner().playerId;
							c.getOwner().totalDamageDealt += hit;
							c.getOwner().getFunction()
									.addSkillXP(XP_PER_DAMAGE * hit, 4);
							c.removeBall();
							stop();
						}
					};
					World.getSynchronizedTaskScheduler().schedule(
							c.getOwner().cannonTask);
				}
			}
		}
	}

	public static void firstClickObject(Client client) {
		Cannon cannon = getCannon(client.objectX, client.objectY, client.absZ);
		if (cannon != null) {
			addBalls(client, cannon);
		}
	}

	public static Cannon getCannon(int x, int y, int h) {
		for (Cannon cannon : cannons) {
			if (cannon.getX() == x && cannon.getY() == y
					&& cannon.getHeight() == h) {
				return cannon;
			}
		}
		return null;
	}

	public static void getNearbyNpcs(Cannon cannon) {
		if (!cannon.nearbyNpcs.isEmpty()) {
			cannon.nearbyNpcs.clear();
		}
		for (int j = 0; j < NPCHandler.npcs.length; j++) {
			if (NPCHandler.npcs[j] == null || NPCHandler.npcs[j].isDead) {
				continue;
			}
			if (World.getNpcHandler().goodDistance(NPCHandler.npcs[j].absX,
					NPCHandler.npcs[j].absY, cannon.getX(), cannon.getY(),
					SHOOTING_DISTANCE)) {
				if (cannon.getHeight() == NPCHandler.npcs[j].absZ
						&& canHitNpc(cannon, NPCHandler.npcs[j])
						&& NPCHandler.npcs[j].HP > 0) {
					cannon.nearbyNpcs.add(j);
				}
			}
		}
	}

	public static void getNearbyPlayers(Cannon cannon) {
		if (!cannon.nearbyPlayers.isEmpty()) {
			cannon.nearbyPlayers.clear();
		}
		for (int i = 0; i < PlayerHandler.players.length; i++) {
			if (PlayerHandler.players[i] == null
					|| PlayerHandler.players[i].isDead
					|| !PlayerHandler.players[i].inFightPits()
					|| i == cannon.getOwner().playerId) {
				continue;
			}
			if (World.getNpcHandler().goodDistance(
					PlayerHandler.players[i].absX,
					PlayerHandler.players[i].absY, cannon.getX(),
					cannon.getY(), SHOOTING_DISTANCE)) {
				if (cannon.getHeight() == PlayerHandler.players[i].absZ) {
					cannon.nearbyPlayers.add(i);
				}
			}
		}
	}

	private static boolean hasCannonParts(Client user) {
		return user.hasItem(6) && user.hasItem(8) && user.hasItem(10)
				&& user.hasItem(12);
	}

	public static void init() {
		World.getAsynchronousTaskScheduler().schedule(new Task(2, false) {
			@Override
			protected void execute() {
				CannonHandler.tick();
			}
		});
	}

	public static void removeCannon(Cannon cannon) {
		if (cannon.getBalls() > 0)
			cannon.getOwner().addItem(2, cannon.getBalls());
		cannons.remove(cannon);
		World.getObjectHandler().globalObject(-1, cannon.getX(), cannon.getY(),
				cannon.getHeight(), 10);
		cannon.getOwner().getItems().addItem(6, 1);
		cannon.getOwner().getItems().addItem(8, 1);
		cannon.getOwner().getItems().addItem(10, 1);
		cannon.getOwner().getItems().addItem(12, 1);
		cannon.getOwner().cannon = null;
	}

	public static void rotateCannon(Cannon c) {
		c.getOwner()
				.getFunction()
				.objectAnim(c.getX(), c.getY(), c.getState().getAnimationId(),
						10, -1);
		int id = c.getState().getId();
		if (id == 7) {
			id = -1;
		}
		c.setState(Cannon.FacingState.forId(id + 1));

	}

	public static void secondClickObject(Client client) {
		Cannon cannon = getCannon(client.objectX, client.objectY, client.absZ);
		if (client != cannon.getOwner()) {
			client.sendMessage("This cannon belongs to "
					+ cannon.getOwner().playerName + ".");
			return;
		}
		if (cannon != null) {
			if (client.getItems().freeSlots() > 3) {
				removeCannon(cannon);
				client.startAnimation(827);
			} else {
				client.sendMessage("You don't have enough inventory space to take this cannon.");
			}
		}
	}

	public static void setupCannon(final Client player) {
		if (player.cannon != null) {
			player.boxMessage("You already have a cannon setup.");
			return;
		}
		/*
		 * if (player.getQuest(21).getStage() != 7) { player.boxMessage(
		 * "You need to have completed the @dre@Dwarf Cannon@bla@ quest.");
		 * return; }
		 */
		if (!hasCannonParts(player)) {
			player.boxMessage("You need all the parts to setup the cannon.");
			return;
		}
		final int x = player.absX;
		final int y = player.absY;
		player.getItems().deleteItem2(6, 1);
		player.getItems().deleteItem2(8, 1);
		player.getItems().deleteItem2(10, 1);
		player.getItems().deleteItem2(12, 1);
		player.cannon = new Cannon(player, player.absX, player.absY,
				player.absZ);
		World.getSynchronizedTaskScheduler().schedule(new Task(1, false) {
			int stage = 0;

			@Override
			protected void execute() {
				stage++;
				if (stage == 1) {
					player.turnPlayerTo(x + 1, y + 1);
				} else if (stage == 2) {
					player.startAnimation(827);
				} else if (stage == 3) {
					CannonHandler.addCannon(player.cannon);
				} else {
					stop();
				}
			}
		});
	}

	public static void tick() {
		for (Cannon cannon : cannons) {
			if (cannon == null)
				continue;
			if (cannon.getBalls() > 0) {
				if (cannon.getOwner().withinDistance(cannon.getX(),
						cannon.getY())) {
					fireCannon(cannon);
					rotateCannon(cannon);
				}
			}
		}
	}
}