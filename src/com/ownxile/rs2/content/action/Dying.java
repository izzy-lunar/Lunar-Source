package com.ownxile.rs2.content.action;

import com.ownxile.config.GameConfig;
import com.ownxile.config.WildConfig;
import com.ownxile.core.World;
import com.ownxile.core.task.Task;
import com.ownxile.rs2.Point;
import com.ownxile.rs2.combat.prayer.Prayer;
import com.ownxile.rs2.npcs.KillLog;
import com.ownxile.rs2.npcs.PetHandler;
import com.ownxile.rs2.player.Client;
import com.ownxile.rs2.player.PlayerHandler;
import com.ownxile.rs2.player.PlayerSave;
import com.ownxile.rs2.world.games.CastleWars;
import com.ownxile.util.Misc;

public class Dying {

	private final Client player;

	public Dying(Client Client) {
		player = Client;
	}

	private int getDeathAnimation() {
		switch (player.playerEquipment[player.playerWeapon]) {
		case 4024:
		case 4029:
			return 1390;
		case 4026:
		case 4027:
			return 1404;
		}
		return 836;// default
	}

	public Point getRespawnPoint() {
		return null;
	}

	public void init() {
		initialize();
		World.getSynchronizedTaskScheduler().schedule(new Task(1, true) {
			@Override
			protected void execute() {
				player.startAnimation(getDeathAnimation());
				stop();
			}
		});
		World.getSynchronizedTaskScheduler().schedule(new Task(6, false) {
			@Override
			protected void execute() {
				respawn();
				stop();
			}
		});
	}

	private void initialize() {
		player.killerId = player.getFunction().findKiller();
		final Client killer = (Client) PlayerHandler.players[player.killerId];
		killer.logoutDelay = System.currentTimeMillis();
		if (killer != null && player.killerId != player.playerId) {
			Prayer.handleRetribution(killer, player);
			if (killer.duelStatus == 5) {
				killer.duelStatus++;
			}
		}
		player.isDead = true;
		player.poisonDamage = -1;
		player.faceUpdate(0);
		player.npcIndex = 0;
		player.playerIndex = 0;
		player.drain = 0;
		player.stopMovement();
		player.getFunction().resetDamageDone();
		player.getFunction().resetFollowers();
	}

	public void handleRating(Client killer, Client killed) {
		int add = WildConfig.calculateRatingForKill(killed.wildRating);
		killer.increaseRating(add);

		int remove = WildConfig.getRatingToRemove(killed.wildRating);
		killed.decreaseRating(remove);
	}

	public void respawn() {
		final int weapon = player.playerEquipment[player.playerWeapon];
		player.killerId = player.getFunction().findKiller();
		final Client killer = (Client) PlayerHandler.players[player.killerId];
		if (weapon == CastleWars.SARA_BANNER
				|| weapon == CastleWars.ZAMMY_BANNER) {
			CastleWars.dropFlag(player, weapon);
			CastleWars.deleteWeapon(player);
		}
		player.freezeTimer = 0;
		if (killer.playerId != player.playerId && killer.inWild()) {

			killer.sendMessage(player.getFunction().KILL_MESSAGES[Misc
					.random(player.getFunction().KILL_MESSAGES.length - 1)]
					+ player.playerName + ".");

			World.sendMessage("@blu@Kill Feed@bla@: @dre@"
					+ killer.playerName
					+ " ("
					+ killer.wildRating
					+ ")@bla@ has "
					+ GameConfig.KILL_VERBS[Misc
							.random(GameConfig.KILL_VERBS.length - 1)]
					+ " @dre@" + player.playerName + " (" + player.wildRating
					+ ")@bla@ in level " + killer.wildLevel + " wilderness.");
			handleRating(killer, player);
		}

		if (!player.inDuelArena() && !player.inFightPits()
				&& !player.inFightCaves() && !player.inFightPitsWait()
				&& !player.inCastleWars() && !player.inMainRoom()
				&& !player.inPestControl() && !player.inTrainingCave() && killer != null) {
			if (player.playerRights < 2 && killer.playerRights < 2) {
				player.getItems().resetKeepItems();
				if (!player.isSkulled) {
					player.getItems().keepItem(0, true);
					player.getItems().keepItem(1, true);
					player.getItems().keepItem(2, true);
				}
				if (player.prayerActive[10]
						&& System.currentTimeMillis() - player.lastProtItem > 700) {
					player.getItems().keepItem(3, true);
				}
				player.getItems().itemsUponDeath(); // drop all items
				if (!player.isSkulled) {
					for (int i1 = 0; i1 < 3; i1++) {
						if (player.itemKeptId[i1] > 0) {
							player.getItems().addItem(player.itemKeptId[i1], 1);
						}
					}
				}
				if (player.prayerActive[10]) {
					if (player.itemKeptId[3] > 0) {
						player.getItems().addItem(player.itemKeptId[3], 1);
					}
				}
				player.getItems().resetKeepItems();
			}
		}
		Prayer.resetPrayers(player);
		if (player.playerRights < 2) {
			for (int i = 0; i < 20; i++) {
				player.playerLevel[i] = player.getFunction().getLevelForXP(
						player.playerXP[i]);
				player.getFunction().refreshSkill(i);
			}
		} else {
			player.playerLevel[3] = player.getFunction().getLevelForXP(
					player.playerXP[3]);
			player.getFunction().refreshSkill(3);
			player.playerLevel[5] = player.getFunction().getLevelForXP(
					player.playerXP[5]);
			player.getFunction().refreshSkill(5);
		}
		if (player.inFightPits()) {
			World.getFightPits().removePlayer(player.getFunction());
			if (World.getFightPits().getPlayers() < 2) {
				Client p = World.getFightPits().getLastPlayer(player);
				if (p != null) {
					p.headIconPk = 1;
					p.updateRequired = true;
					p.getUpdateFlags().appearanceUpdateRequired = true;
					World.getFightPits().setChampion(p.playerName);
				}
			}
		} else if (CastleWars.isInCw(player)) {
			if (CastleWars.SARA_BANNER == player.playerEquipment[player.playerWeapon]
					|| CastleWars.ZAMMY_BANNER == player.playerEquipment[player.playerWeapon]) {
				CastleWars.dropFlag(player,
						player.playerEquipment[player.playerWeapon]);
				CastleWars.deleteWeapon(player);
			}
			if (CastleWars.getTeamNumber(player) == 1) {
				player.getFunction().movePlayer(2426 + Misc.random(3),
						3076 - Misc.random(3), 1);
			} else {
				player.getFunction().movePlayer(2373 + Misc.random(3),
						3131 - Misc.random(3), 1);
			}
		} else if (player.inDuelArena()) {
			final Client o = (Client) PlayerHandler.players[player.duelingWith];
			if (o != null && o.inDuelArena()) {
				o.getFunction().createPlayerHint(10, -1);
				if (o.duelStatus == 6) {
					o.getDuel().duelVictory();
					o.addPoints(2);
				}
			}
			player.getFunction().movePlayer(3360 + Misc.random(18),
					3274 + Misc.random(3), 0);
			PlayerSave.saveGame(o);
			PlayerSave.saveGame(player);
			if (player.duelStatus != 6) {
				player.getDuel().resetDuel();
			}
		} else if (player.inFightCaves()) {
			player.getFunction().resetTzhaar();
		} else if (player.inPestControl()) {
			player.getFunction().movePlayer(2657, 2639, 0);	} 
		else if (player.inTrainingCave()) {
				player.getFunction().movePlayer(2766, 10067, 0);

		} else {
			player.getFunction().movePlayer(GameConfig.RESPAWN_X,
					GameConfig.RESPAWN_Y, 0);
			player.isSkulled = false;
			player.skullTimer = 0;
			player.attackedPlayers.clear();
			PlayerSave.saveGame(killer);
		}
		player.vengOn = false;
		PlayerSave.saveGame(player);
		PlayerSave.saveGame(killer);
		player.getCombat().resetPlayerAttack();
		player.getFunction().resetTb();
		player.isSkulled = false;
		player.attackedPlayers.clear();
		player.headIconPk = -1;
		player.skullTimer = -1;
		player.handlingDeath = false;
		player.damageTaken = new int[GameConfig.MAX_PLAYERS];
		player.getFunction().resetAnimation();
		if (player.ep > 0) {
			player.sendMessage("You lose " + player.ep + " EP rating.");
			player.ep = 0;
		}
		player.specAmount = 10;
		player.getItems().addSpecialBar(
				player.playerEquipment[player.playerWeapon]);
		player.sendMessage("Oh dear, you have died.");
		CastleWars.deleteFlag(player);
		if (player.summonId > 0
				&& (KillLog.isBossPet(player.summonId) || player.summonId == 98))
			PetHandler.killPet(player, false);
		int total = player.getTotalLevel();
		if (player.isUltimateIronman()) {
			player.boxMessage("@dre@Game over!",
					"You are no longer an ultimate ironman.");
			if (total > 99)
				World.sendMessage("@grd@"
						+ player.playerName
						+ " has died on Ultimate Ironman with a total level of "
						+ total + "!");
			player.playerTitle = 9 + player.getGender();

		}
		player.isDead = false;
	}
	/*
	 * if (killer.inPvP() && player.inPvP() &&
	 * Engine.getPvpHandler().hasWealth(killer) && killer.ep > 0) {
	 * ItemHandler.createGroundItem(killer, Engine.getPvpHandler()
	 * .getRandomDrop(killer), player.absX, player.absY, player.heightLevel, 1,
	 * killer.playerId); killer.ep = 0; killer.getFunction().sendFrame126("EP: "
	 * + player.ep + "%", 16130); }
	 */
}