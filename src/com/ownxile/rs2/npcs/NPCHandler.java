package com.ownxile.rs2.npcs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.ownxile.config.FileConfig;
import com.ownxile.config.GameConfig;
import com.ownxile.config.WildConfig;
import com.ownxile.core.Plugin;
import com.ownxile.core.World;
import com.ownxile.core.cache.def.NPCDefinition;
import com.ownxile.core.task.Task;
import com.ownxile.core.task.impl.PenanceBoss;
import com.ownxile.rs2.items.GroundItemHandler;
import com.ownxile.rs2.player.Client;
import com.ownxile.rs2.player.Player;
import com.ownxile.rs2.player.PlayerHandler;
import com.ownxile.rs2.player.PlayerPathFinding;
import com.ownxile.rs2.world.games.ChampionsChallenge;
import com.ownxile.rs2.world.games.FightCaves;
import com.ownxile.rs2.world.games.WarriorsGuild;
import com.ownxile.rs2.world.region.RegionManager;
import com.ownxile.util.Misc;

public class NPCHandler {

	public static final int MAX_VALUE = 10000;
	public static NPC npcs[] = new NPC[10000];
	private static final String[] UNDEAD = { "ankous", "banshee",
			"crawling hand", "revenant", "ghost", "ghast", "mummy", "shade",
			"skeleton", "undead", "zombie", "zogre", "skorgres" };

	@SuppressWarnings("unused")
	private static final int[][] MOLE = { { 1738, 5228 }, { 1778, 5236 },
			{ 1739, 5194 }, { 1737, 5157 } };

	public static int getSlotForNpc(int npcType) {
		for (int i = 0; i < npcs.length; i++) {
			if (npcs[i] == null)
				continue;
			if (npcs[i].npcType == npcType)
				return i;
		}
		return -1;
	}

	public static void clip2(NPC npc) {
		if (npc.moveX == 1 && npc.moveY == 1) {
			if ((RegionManager
					.getClipping(npc.absX + 1, npc.absY + 1, npc.absZ) & 0x12801e0) != 0) {
				npc.moveX = 0;
				npc.moveY = 0;
				if ((RegionManager
						.getClipping(npc.absX, npc.absY + 1, npc.absZ) & 0x1280120) == 0) {
					npc.moveY = 1;
				} else {
					npc.moveX = 1;
				}
			}
		} else if (npc.moveX == -1 && npc.moveY == -1) {
			if ((RegionManager
					.getClipping(npc.absX - 1, npc.absY - 1, npc.absZ) & 0x128010e) != 0) {
				npc.moveX = 0;
				npc.moveY = 0;
				if ((RegionManager
						.getClipping(npc.absX, npc.absY - 1, npc.absZ) & 0x1280102) == 0) {
					npc.moveY = -1;
				} else {
					npc.moveX = -1;
				}
			}
		} else if (npc.moveX == 1 && npc.moveY == -1) {
			if ((RegionManager
					.getClipping(npc.absX + 1, npc.absY - 1, npc.absZ) & 0x1280183) != 0) {
				npc.moveX = 0;
				npc.moveY = 0;
				if ((RegionManager
						.getClipping(npc.absX, npc.absY - 1, npc.absZ) & 0x1280102) == 0) {
					npc.moveY = -1;
				} else {
					npc.moveX = 1;
				}
			}
		} else if (npc.moveX == -1 && npc.moveY == 1) {
			if ((RegionManager
					.getClipping(npc.absX - 1, npc.absY + 1, npc.absZ) & 0x128013) != 0) {
				npc.moveX = 0;
				npc.moveY = 0;
				if ((RegionManager
						.getClipping(npc.absX, npc.absY + 1, npc.absZ) & 0x1280120) == 0) {
					npc.moveY = 1;
				} else {
					npc.moveX = -1;
				}
			}
		} // Checking Diagonal movement.

		if (npc.moveY == -1) {
			if ((RegionManager.getClipping(npc.absX, npc.absY - 1, npc.absZ) & 0x1280102) != 0) {
				npc.moveY = 0;
			}
		} else if (npc.moveY == 1) {
			if ((RegionManager.getClipping(npc.absX, npc.absY + 1, npc.absZ) & 0x1280120) != 0) {
				npc.moveY = 0;
			}
		} // Checking Y movement.
		if (npc.moveX == 1) {
			if ((RegionManager.getClipping(npc.absX + 1, npc.absY, npc.absZ) & 0x1280180) != 0) {
				npc.moveX = 0;
			}
		} else if (npc.moveX == -1) {
			if ((RegionManager.getClipping(npc.absX - 1, npc.absY, npc.absZ) & 0x1280108) != 0) {
				npc.moveX = 0;
			}
		}
	}

	public static void countNpcs() {
		int i = 0;
		for (NPC n : npcs) {
			if (n == null) {
				continue;
			}
			i++;
		}
		System.out.println("Found " + i + " npcs.");
	}

	private static int getNextDagganoth(int id) {
		if (id > 1347 && id < 1356) {
			return id + 1;
		}
		return 0;
	}

	public static int getNpcSlot(int npcId) {
		for (NPC npc : npcs) {
			if (npc == null) {
				continue;
			}
			if (npc.npcType == npcId) {
				return npc.npcType;
			}
		}
		return 0;
	}

	public static double getRange(int npcX, int npcY, int npcWidth,
			int npcHeight, int playerX, int playerY) {
		return Math.hypot(npcX + npcWidth / 2.0D - playerX - 0.5D, npcY
				+ npcHeight / 2.0D - playerY - 0.5D);
	}

	public static boolean inRange(int npcX, int npcY, int npcWidth,
			int npcHeight, int playerX, int playerY, int distance) {
		return distance >= getRange(npcX, npcY, npcWidth, npcHeight, playerX,
				playerY);
	}

	private NPCDefinition npcDefinitions[] = new NPCDefinition[MAX_VALUE];

	public NPCHandler() {
		for (int i = 0; i < MAX_VALUE; i++) {
			npcs[i] = null;
		}
		for (int i = 0; i < MAX_VALUE; i++) {
			npcDefinitions[i] = null;
		}
	}

	public void addNpcDefinition(int slot, String name, String desc,
			int combatLevel, int size) {
		NPCDefinition npcDefinition = new NPCDefinition(combatLevel, size,
				name, desc);
		npcDefinitions[slot] = npcDefinition;
	}

	public void applyDamage(int i) {
		if (npcs[i] != null) {
			if (PlayerHandler.players[npcs[i].oldIndex] == null) {
				return;
			}
			if (npcs[i].isDead) {
				return;
			}
			final Client entity = (Client) PlayerHandler.players[npcs[i].oldIndex];
			if (multiAttacks(i)) {
				multiAttackDamage(i);
				return;
			}
			if (entity.playerIndex <= 0 && entity.npcIndex <= 0) {
				if (entity.autoRet == 1) {
					entity.npcIndex = i;
				}
			}
			if (entity.attackTimer <= 3 || entity.attackTimer == 0
					&& entity.npcIndex == 0 && entity.oldNpcIndex == 0) {
				entity.startAnimation(entity.getCombat().getBlockEmote());
			}
			int damage = 0;
			if (!entity.isDead) {
				if (npcs[i].attackType == 0) {
					damage = Misc.random(npcs[i].maxHit);
					if (10 + Misc.random(entity.getCombat()
							.calculateMeleeDefence()) > Misc
							.random(npcs[i].attack)) {
						damage = 0;
					}
					if (damage > 9) {
						if (entity.playerEquipment[entity.playerShield] == 13740) {
							damage = (int) damage * 85 / 100;
							entity.sendMessage("Your shield reduces the damage received by 30%.");
						}
						if (entity.playerEquipment[entity.playerShield] == 13742) {
							if (Misc.random(10) < 7) {
								damage = (int) damage * 75 / 100;
								entity.sendMessage("Your shield reduces damage received by 25%.");
							}

						}
					}
					if (entity.prayerActive[18]) {
						if (npcs[i].npcType == 1158 || npcs[i].npcType == 1160) {
							damage = damage / 2;
						} else {
							damage = 0;
						}
						if (entity.playerLevel[3] - damage < 0) {
							damage = entity.playerLevel[3];
						}
					}
				}

				if (entity.playerLevel[3] - damage < 0) {
					damage = entity.playerLevel[3];
				}
			}

			if (npcs[i].attackType == 1) { // range
				damage = Misc.random(npcs[i].maxHit);
				if (10 + Misc
						.random(entity.getCombat().calculateRangeDefence()) > Misc
						.random(NPCHandler.npcs[i].attack)) {
					damage = 0;
				}
				if (damage > 9) {
					if (entity.playerEquipment[entity.playerShield] == 13740) {
						damage = (int) damage * 85 / 100;
						entity.sendMessage("Your shield reduces the damage received by 30%.");
					}
					if (entity.playerEquipment[entity.playerShield] == 13742) {
						if (Misc.random(10) < 7) {
							damage = (int) damage * 75 / 100;
							entity.sendMessage("Your shield reduces damage received by 25%.");
						}

					}
				}
				if (entity.prayerActive[17]) { // protect from range
					damage = 0;
				}
				if (entity.playerLevel[3] - damage < 0) {
					damage = entity.playerLevel[3];
				}
			}

			if (npcs[i].attackType == 2) { // magic
				damage = Misc.random(npcs[i].maxHit);
				boolean magicFailed = false;
				if (10 + Misc.random(entity.getCombat().mageDef()) > Misc
						.random(NPCHandler.npcs[i].attack)) {
					damage = 0;
					magicFailed = true;
				}
				if (damage > 9) {
					if (entity.playerEquipment[entity.playerShield] == 13740) {
						damage = (int) damage * 85 / 100;
						entity.sendMessage("Your shield reduces the damage received by 30%.");
					}
					if (entity.playerEquipment[entity.playerShield] == 13742) {
						if (Misc.random(10) < 7) {
							damage = (int) damage * 75 / 100;
							entity.sendMessage("Your shield reduces damage received by 25%.");
						}

					}
				}
				if (entity.prayerActive[16]) { // protect from magic
					damage = 0;
					magicFailed = true;
				}
				if (entity.playerLevel[3] - damage < 0) {
					damage = entity.playerLevel[3];
				}
				if (npcs[i].endGfx > 0 && (!magicFailed || isFightCaveNpc(i))) {
					entity.gfx0(npcs[i].endGfx);
				} else {
					entity.gfx100(85);
				}
			}

			if (npcs[i].attackType == 3) {
				if (entity.getFunction().hasAntiFireShield()) {
					damage = 0 + Misc.random(6);
					entity.sendMessage("You manage to resist some of the dragonfire.");
				} else {
					damage = Misc.random(55);
					if (damage > 19)
						entity.sendMessage("You are horribly burned by the dragonfire!");
				}
				if (entity.playerLevel[3] - damage < 0) {
					damage = entity.playerLevel[3];
				}
				entity.gfx100(npcs[i].endGfx);
			}
			if (NPCHandler.npcs[i].npcType == 5902
					|| NPCHandler.npcs[i].npcType == 2552
					|| NPCHandler.npcs[i].npcType == 3847
					|| NPCHandler.npcs[i].npcType == 2349
					|| NPCHandler.npcs[i].npcType == 3068) {
				if (entity != null) {
					entity.drain = 10;
					entity.drainSkill = 5;
				}
			}

			if (NPCHandler.npcs[i].npcType == 5903) {
				if (entity != null) {
					entity.drain = 10;
					entity.drainSkill = 1;
				}
			}
			if (NPCHandler.npcs[i].npcType == 5247) {
				if (entity != null) {
					entity.drain = 40;
					entity.drainSkill = 5;
				}
			}
			/*
			 * if (NPCHandler.npcs[i].npcType == 3340 && Misc.random(6) == 1) {
			 * if (entity != null) { int random = Misc.random(3); if
			 * (NPC.lastMoleX != MOLE[random][0])
			 * NPCHandler.npcs[i].moleTele(MOLE[random][0], MOLE[random][1], 0);
			 * return; } }
			 */
			if (entity.vengOn && damage > 0) {
				entity.getCombat().appendVengeanceNpc(i, damage);
			}
			entity.getCombat().applyRecoilNpc(damage, i);
			handlePoisonEffects(entity, i, damage);
			FightCaves.tzKihEffect(entity, i, damage);
			entity.logoutDelay = System.currentTimeMillis(); // logout delay
			// entity.setHitDiff(damage);

			entity.handleHitMask(damage);
			entity.playerLevel[3] -= damage;
			entity.getFunction().refreshSkill(3);
			entity.updateRequired = true;
			// entity.setHitUpdateRequired(true);
		}
	}

	public void attackPlayer(Client entity, int i) {
		if (npcs[i] != null) {
			if (npcs[i].isDead) {
				return;
			}

			if (!npcs[i].inMulti() && npcs[i].underAttackBy > 0
					&& npcs[i].underAttackBy != entity.playerId) {
				npcs[i].killerId = 0;
				return;
			}
			if (!npcs[i].inMulti()
					&& (entity.underAttackBy > 0 || entity.underAttackBy2 > 0
							&& entity.underAttackBy2 != i)) {
				npcs[i].killerId = 0;
				return;
			}
			if (npcs[i].absZ != entity.absZ) {
				npcs[i].killerId = 0;
				return;
			}
			// if (RegionManager.pathBlocked(npcs[i], entity))
			// return;
			npcs[i].facePlayer(entity.playerId);
			final boolean special = false;// specialCase(entity,i);
			if (goodDistance(npcs[i].getX(), npcs[i].getY(), entity.getX(),
					entity.getY(), distanceRequired(i)) || special) {
				if (!entity.isDead) {
					npcs[i].facePlayer(entity.playerId);
					npcs[i].attackTimer = getNpcDelay(i);
					npcs[i].hitDelayTimer = getHitDelay(i);
					npcs[i].attackType = 0;

					if (special) {
						loadSpell2(i);
					} else {
						loadSpell(i);
					}
					if (multiAttacks(i)) {
						multiAttackGfx(i, npcs[i].projectileId);
						startAnimation(NPCAnimate.getAttackEmote(i), i);
						npcs[i].oldIndex = entity.playerId;
						return;
					}
					if (npcs[i].projectileId > 0) {
						final int nX = NPCHandler.npcs[i].getX() + offset(i);
						final int nY = NPCHandler.npcs[i].getY() + offset(i);
						final int pX = entity.getX();
						final int pY = entity.getY();
						final int offX = (nY - pY) * -1;
						final int offY = (nX - pX) * -1;
						entity.getFunction().createPlayersProjectile(nX, nY,
								offX, offY, 50, getProjectileSpeed(i),
								npcs[i].projectileId, 43, 31,
								-entity.getId() - 1, 65);
					}
					entity.underAttackBy2 = i;
					entity.singleCombatDelay2 = System.currentTimeMillis();
					npcs[i].oldIndex = entity.playerId;
					startAnimation(NPCAnimate.getAttackEmote(i), i);
					entity.getFunction().removeAllWindows();
				}
			}
		}
	}

	// id of bones dropped by npcs
	public int boneDrop(int type) {
		switch (type) {
		case 1:// normal bones
		case 9:
		case 100:
		case 12:
		case 17:
		case 803:
		case 18:
		case 81:
		case 101:
		case 41:
		case 19:
		case 90:
		case 75:
		case 86:
		case 78:
		case 912:
		case 913:
		case 914:
		case 1648:
		case 1643:
		case 1618:
		case 1624:
		case 181:
		case 119:
		case 49:
		case 26:
		case 1341:
			return 526;
		case 117:
			return 532;// big bones
		case 50:// drags
		case 53:
		case 54:
		case 55:
		case 941:
		case 1590:
		case 1591:
		case 1592:
		case 5362:
		case 5363:
		case 742:
			return 536;
		case 84:
		case 1615:
		case 1613:
		case 82:
		case 3200:
			return 592;
		case 2881:
		case 2882:
		case 2883:
			return 6729;
		default:
			return -1;
		}
	}

	public NPC createNpcSpawn(int npcType, int x, int y, int heightLevel,
			int WalkingType, int HP, int maxHit, int attack, int defence) {
		// first, search for a free slot
		int slot = -1;
		for (int i = 1; i < MAX_VALUE; i++) {
			if (npcs[i] == null) {
				slot = i;
				break;
			}
		}

		if (slot == -1) {
			return null; // no free slot found
		}

		final NPC newNPC = new NPC(slot, npcType);
		newNPC.absX = x;
		newNPC.absY = y;
		newNPC.makeX = x;
		newNPC.makeY = y;
		newNPC.absZ = heightLevel;
		newNPC.walkingType = WalkingType;
		newNPC.HP = HP;
		newNPC.MaxHP = HP;
		newNPC.maxHit = maxHit;
		newNPC.attack = attack;
		newNPC.defence = defence;
		npcs[slot] = newNPC;
		return newNPC;
	}

	public void deleteAllNpcs() {
		for (int i = 0; i < MAX_VALUE; i++) {
			npcs[i] = null;
		}
	}

	/**
	 * Distanced required to attack
	 **/
	public int distanceRequired(int i) {
		switch (npcs[i].npcType) {
		case 2025:
		case 2028:
		case 1590:
		case 1591:
		case 1592:
		case 5362:
		case 5363:
		case 912:
		case 913:
		case 914:
		case 4383:
		case 1472:
		case 2552:
			return 3;
		case 5666:
		case 5422:
		case 2349:
			return 8;
		case 1158:
		case 1160:
		case 5902:
		case 50:
		case 3847:
			return 10;
		case 6247:
		case 5903:
			return 2;
		case 2881:// dag kings
		case 2882:
		case 3200:// chaos ele
		case 2743:
		case 2631:
		case 2745:
			return 8;
		case 2883:// rex
			return 1;
		case 3340:
			return 3;
		case 6261:
		case 6263:
		case 2556:
		case 2557:
		case 6222:
		case 6225:
		case 6227:
		case 6248:
		case 6252:
			return 9;
		case 2892:
		case 2894:
		case 1351:
		case 1352:
		case 1353:
		case 1354:
		case 1355:
		case 1356:
			return 10;
		default:
			return 1;
		}
	}

	private boolean doesPoison(int npcType) {
		switch (npcType) {
		case 2892:
		case 2894:
		case 108:
		case 134:
		case 1009:
		case 2497:
		case 62:
		case 5247:
			return true;
		}
		return false;
	}

	public void dropItems(int i) {
		// long start = System.currentTimeMillis();

		final Client entity = (Client) PlayerHandler.players[npcs[i].killedBy];
		if (entity != null) {
			KillLog.handleKill(entity, npcs[i].npcType);
			if (npcs[i].npcType == 4292 && Misc.random(2) == 1) {
				GroundItemHandler.createGroundItem(entity,
						WarriorsGuild.getDefender(entity), npcs[i].absX,
						npcs[i].absY, npcs[i].absZ, 1, entity.playerId);
				if (entity.defenderDrop != 8851)
					entity.defenderDrop += 1;
			}

			if (entity.champion == npcs[i].npcType && entity.champion > 0) {
				entity.sendMessage("You have defeated the " + getNPCName(entity.champion) + "!");
				World.getSynchronizedTaskScheduler().schedule(new Task(2, false) {
					@Override
					protected void execute() {
					ChampionsChallenge.getInstance().handleDeath(entity);
						stop();
					}
				});
			}
			int dagannoth = NPCHandler.getNextDagganoth(npcs[i].npcType);
			if (dagannoth > 0) {
				World.getNpcHandler().spawnNpc(entity, dagannoth, 2511, 10020,
						entity.absZ, 0, 80, 25, 170, 170, true, true);
			}
			if (WarriorsGuild.isAnimated(npcs[i].npcType)) {
				GroundItemHandler.createGroundItem(entity, 8851, npcs[i].absX,
						npcs[i].absY, npcs[i].absZ,
						WarriorsGuild.getTokenAmount(npcs[i].npcType),
						entity.playerId);
			}
			if (World.getGodwars().inDungeon(entity)) {
				World.getGodwars().updateKillCount(entity, npcs[i].npcType);
			}
			if (npcs[i].npcType == 912 || npcs[i].npcType == 913
					|| npcs[i].npcType == 914) {
				entity.magePoints += 1;
			}
			if (entity.getSlayer().isSlayerTask(npcs[i].npcType)) {
				entity.getSlayer().decreaseKillAmount(
						World.getNpcHandler()
								.getNPCCombatLevel(npcs[i].npcType));
			}
			if (entity.inWild()) {
				int wildRating = WildConfig
						.ratingAwardedForNPCKill(npcs[i].npcType);
				if (wildRating > 0) {
					entity.increaseRating(wildRating);
				}
			}

			if (NPCDrops.constantDrops.get(npcs[i].npcType) != null) {
				for (final int item : NPCDrops.constantDrops
						.get(npcs[i].npcType)) {
					GroundItemHandler.createGroundItem(entity, item,
							npcs[i].absX, npcs[i].absY, npcs[i].absZ, 1,
							entity.playerId);
					/*
					 * if (entity.clanId >= 0)
					 * Tick.clanChat.handleLootShare(entity, item, 1);
					 */
				}
			}

			if (NPCDrops.dropRarity.get(npcs[i].npcType) != null) {
				if (rareDrops(i)) {
					final int random = Misc.random(NPCDrops.rareDrops
							.get(npcs[i].npcType).length - 1);

					if (npcs[i].npcType == 3847)
						GroundItemHandler
								.createGroundItem(
										entity,
										NPCDrops.rareDrops.get(npcs[i].npcType)[random][0],
										entity.absX,
										entity.absY,
										npcs[i].absZ,
										NPCDrops.rareDrops.get(npcs[i].npcType)[random][1],
										entity.playerId);

					else
						GroundItemHandler
								.createGroundItem(
										entity,
										NPCDrops.rareDrops.get(npcs[i].npcType)[random][0],
										npcs[i].absX,
										npcs[i].absY,
										npcs[i].absZ,
										NPCDrops.rareDrops.get(npcs[i].npcType)[random][1],
										entity.playerId);

					if (entity.clanId >= 0 && KillLog.isNpc(npcs[i].npcType))
						World.getClanChat()
								.handleLootShare(
										entity,
										NPCDrops.rareDrops.get(npcs[i].npcType)[random][0],
										NPCDrops.rareDrops.get(npcs[i].npcType)[random][1],
										npcs[i].npcType);

				} else {
					final int random = Misc.random(NPCDrops.normalDrops
							.get(npcs[i].npcType).length - 1);
					if (npcs[i].npcType == 3847)
						GroundItemHandler
								.createGroundItem(
										entity,
										NPCDrops.normalDrops
												.get(npcs[i].npcType)[random][0],
										entity.absX,
										entity.absY,
										npcs[i].absZ,
										NPCDrops.normalDrops
												.get(npcs[i].npcType)[random][1],
										entity.playerId);

					else
						GroundItemHandler
								.createGroundItem(
										entity,
										NPCDrops.normalDrops
												.get(npcs[i].npcType)[random][0],
										npcs[i].absX,
										npcs[i].absY,
										npcs[i].absZ,
										NPCDrops.normalDrops
												.get(npcs[i].npcType)[random][1],
										entity.playerId);
					/*
					 * Tick.clanChat.handleLootShare(entity,
					 * NPCDrops.normalDrops.get(npcs[i].npcType)[random][0],
					 * NPCDrops.normalDrops.get(npcs[i].npcType)[random][1]);
					 */
				}
			}

		}

		if (npcs[i].npcType == 5247) {
			World.sendMessage("@dre@"
					+ entity.playerName
					+ " has slain the Penance Queen, it will respawn in 1 hour.");
			PenanceBoss.getNpc().absX = 100;
			PenanceBoss.getNpc().absY = 100;
			PenanceBoss.getNpc().makeX = 100;
			PenanceBoss.getNpc().makeY = 100;

		}
		// System.out.println("Took: " + (System.currentTimeMillis() - start));
	}

	public int followDistance(int i) {
		switch (npcs[i].npcType) {
		case 6260:
		case 6265:
		case 6247:
		case 6250:
			return 8;
		case 2883:
			return 4;
		case 2881:
		case 2882:
			return 1;
		case 2745:
		case 5384:
		case 4383:
		case 5408:
		case 5903:
			return 12;

		}
		return 0;

	}

	public boolean followPlayer(int i) {
		switch (npcs[i].npcType) {
		case 2892:
		case 2894:
			return false;
		}
		return true;
	}

	/*
	 * public int getCloseRandomPlayer(int i) { //final ArrayList<Integer>
	 * players = new ArrayList<Integer>(); /*for (int j = 0; j <
	 * PlayerHandler.players.length; j++) { if (PlayerHandler.players[j] !=
	 * null) { if (goodDistance(PlayerHandler.players[j].absX,
	 * PlayerHandler.players[j].absY, npcs[i].absX, npcs[i].absY, 2 +
	 * distanceRequired(i) + followDistance(i)) || isFightCaveNpc(i)) { if
	 * (PlayerHandler.players[j].underAttackBy <= 0 &&
	 * PlayerHandler.players[j].underAttackBy2 <= 0 ||
	 * PlayerHandler.players[j].inMulti()) if
	 * (PlayerHandler.players[j].heightLevel == npcs[i].heightLevel) return j; }
	 * } } /*if (players.size() > 0) return
	 * players.get(Misc.random(players.size() - 1)); else return 0; }
	 */

	public void followPlayer(int i, int playerId) {
		if (PlayerHandler.players[playerId] == null || npcs[i] == null) {
			return;
		}
		if (PlayerHandler.players[playerId].isDead) {
			npcs[i].facePlayer(0);
			npcs[i].randomWalk = true;
			npcs[i].underAttack = false;
			return;
		}
		if (npcs[i].isDead) {
			return;
		}
		if (!followPlayer(i)) {
			npcs[i].facePlayer(playerId);
			return;
		}
		npcs[i].randomWalk = false;
		int playerX = PlayerHandler.players[playerId].absX;
		int playerY = PlayerHandler.players[playerId].absY;
		if (goodDistance(npcs[i].getX(), npcs[i].getY(), playerX, playerY,
				distanceRequired(i))) {
			return;
		}
		if (npcs[i].spawnedBy > 0
				|| npcs[i].absX < npcs[i].makeX
						+ GameConfig.NPC_FOLLOW_DISTANCE
				&& npcs[i].absX > npcs[i].makeX
						- GameConfig.NPC_FOLLOW_DISTANCE
				&& npcs[i].absY < npcs[i].makeY
						+ GameConfig.NPC_FOLLOW_DISTANCE
				&& npcs[i].absY > npcs[i].makeY
						- GameConfig.NPC_FOLLOW_DISTANCE) {
			NPCPathFinder.follow(npcs[i], PlayerHandler.players[playerId]);
			npcs[i].facePlayer(playerId);
			npcs[i].getNextNPCMovement(i);
			npcs[i].facePlayer(playerId);
			npcs[i].getUpdateFlags().updateRequired = true;
			/*
			 * if (npcs[i].absZ == PlayerHandler.players[playerId].absZ) { if
			 * (PlayerHandler.players[playerId] != null && npcs[i] != null) { if
			 * (playerY < npcs[i].absY) { npcs[i].moveX = GetMove(npcs[i].absX,
			 * playerX); npcs[i].moveY = GetMove(npcs[i].absY, playerY); } else
			 * if (playerY > npcs[i].absY) { npcs[i].moveX =
			 * GetMove(npcs[i].absX, playerX); npcs[i].moveY =
			 * GetMove(npcs[i].absY, playerY); } else if (playerX <
			 * npcs[i].absX) { npcs[i].moveX = GetMove(npcs[i].absX, playerX);
			 * npcs[i].moveY = GetMove(npcs[i].absY, playerY); } else if
			 * (playerX > npcs[i].absX) { npcs[i].moveX = GetMove(npcs[i].absX,
			 * playerX); npcs[i].moveY = GetMove(npcs[i].absY, playerY); } else
			 * if (playerX == npcs[i].absX || playerY == npcs[i].absY) { final
			 * int o = Misc.random(3); switch (o) { case 0: npcs[i].moveX =
			 * GetMove(npcs[i].absX, playerX); npcs[i].moveY =
			 * GetMove(npcs[i].absY, playerY + 1); break;
			 * 
			 * case 1: npcs[i].moveX = GetMove(npcs[i].absX, playerX);
			 * npcs[i].moveY = GetMove(npcs[i].absY, playerY - 1); break;
			 * 
			 * case 2: npcs[i].moveX = GetMove(npcs[i].absX, playerX + 1);
			 * npcs[i].moveY = GetMove(npcs[i].absY, playerY); break;
			 * 
			 * case 3: npcs[i].moveX = GetMove(npcs[i].absX, playerX - 1);
			 * npcs[i].moveY = GetMove(npcs[i].absY, playerY); break; } }
			 * npcs[i].facePlayer(playerId); npcs[i].getNextNPCMovement(i);
			 * npcs[i].facePlayer(playerId);
			 * npcs[i].getUpdateFlags().updateRequired = true; } }
			 */
		} else {
			npcs[i].facePlayer(0);
			npcs[i].randomWalk = true;
			npcs[i].underAttack = false;
		}
	}

	public String formatNpcName(String name) {
		return Misc.optimizeText(name.toLowerCase().replaceAll("_", " "));
	}

	public int getClosePlayer(int i) {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				if (j == npcs[i].spawnedBy) {
					return j;
				}
				if (goodDistance(PlayerHandler.players[j].absX,
						PlayerHandler.players[j].absY, npcs[i].absX,
						npcs[i].absY, 2 + distanceRequired(i)
								+ followDistance(i))
						|| isFightCaveNpc(i)) {
					if (PlayerHandler.players[j].underAttackBy <= 0
							&& PlayerHandler.players[j].underAttackBy2 <= 0
							|| PlayerHandler.players[j].inMulti()) {
						if (PlayerHandler.players[j].absZ == npcs[i].absZ
								&& !PlayerHandler.players[j].isDead) {
							return j;
						}
					}
				}
			}
		}
		return 0;
	}

	/**
	 * Hit delays
	 **/
	public int getHitDelay(int i) {
		switch (npcs[i].npcType) {
		case 181:
			return 2;
		case 2881:
		case 2882:
		case 3200:
		case 2892:
		case 2894:
		case 3847:
			return 3;
		case 1158:
		case 1160:
			if (npcs[i].attackType == 1 || npcs[i].attackType == 2) {
				return 3;
			} else {
				return 2;
			}

		case 2743:
		case 2631:
		case 6222:
		case 6225:
		case 6227:
		case 2554:
		case 2349:
			return 3;

		case 2745:
			if (npcs[i].attackType == 1 || npcs[i].attackType == 2) {
				return 5;
			} else {
				return 2;
			}

		case 1590:
		case 1591:
		case 1592:
		case 5362:
		case 5363:
		case 5902:
			return 5;
		case 912:
		case 913:
		case 914:
		case 2025:
		case 4383:
		case 1472:
			return 4;
		case 2028:
			return 3;

		default:
			return 2;
		}
	}

	public int getHpByCombat(int combat) {
		if (combat < 2) {
			return 0;
		}
		if (combat < 6) {
			return 5;
		}
		double split = 0;
		if (combat > 300) {
			split = combat / 8;
		} else {
			split = combat / 4;
		}
		return (int) (split * 5);
	}

	public int getMaxHit(int i) {
		switch (npcs[i].npcType) {
		case 6222:
			if (npcs[i].attackType == 2) {
				return 28;
			} else {
				return 68;
			}
		case 1158:
		case 2349:
			return 40;
		case 6247:
			return 31;
		case 6260:
			return 36;
		case 5666:
		case 5902:
		case 3847:
		case 5422:
			return 65;
		}
		return 1;
	}

	/**
	 * Npc Follow Player
	 **/

	public int GetMove(int Place1, int Place2) {
		if (Place1 - Place2 == 0) {
			return 0;
		} else if (Place1 - Place2 < 0) {
			return 1;
		} else if (Place1 - Place2 > 0) {
			return -1;
		}
		return 0;
	}

	public int getNPCByName(String name) {
		for (int i = 0; i < npcDefinitions.length; i++) {
			if (npcDefinitions[i] != null) {
				if (formatNpcName(npcDefinitions[i].getName()).equals(name)) {
					return i;
				}
			}
		}
		return -1;
	}

	public int getNPCCombatLevel(int id) {
		NPCDefinition npc = getNPCDefinition(id);
		if (npc != null) {
			return npc.getCombatLevel();
		}
		return 0;
	}

	public NPCDefinition getNPCDefinition(int id) {
		return npcDefinitions[id];
	}

	/**
	 * Attack delays
	 **/
	public int getNpcDelay(int i) {
		switch (npcs[i].npcType) {
		case 2025:
		case 2028:
		case 5422:
			return 7;

		case 2745:
			return 8;
		case 2349:
			if (npcs[i].attackType == 2)
				return 4;
			else if (npcs[i].attackType == 1)
				return 7;
			else if (npcs[i].attackType == 0)
				return 8;
		case 6222:
		case 6225:
		case 6227:
		case 6223:
		case 6260:
			return 6;
			// saradomin gw boss
		case 6247:
			return 2;

		default:
			return 5;
		}
	}

	public String getNPCDescription(int id) {
		NPCDefinition npc = getNPCDefinition(id);
		if (npc != null) {
			return npc.getDescription();
		}
		return "Nothing interesting happens.";
	}

	/**
	 * Npc killer id?
	 **/

	public int getNpcKillerId(int npcId) {
		int oldDamage = 0;
		int killerId = 0;
		for (int p = 1; p < GameConfig.MAX_PLAYERS; p++) {
			if (PlayerHandler.players[p] != null) {
				if (PlayerHandler.players[p].lastNpcAttacked == npcId) {
					if (PlayerHandler.players[p].totalDamageDealt > oldDamage) {
						oldDamage = PlayerHandler.players[p].totalDamageDealt;
						killerId = p;
					}
					PlayerHandler.players[p].totalDamageDealt = 0;
				}
			}
		}
		return killerId;
	}

	public String getNPCName(int id) {
		NPCDefinition npc = getNPCDefinition(id);
		if (npc != null) {
			return npc.getName().replaceAll("_", " ");
		}
		return "Unknown NPC";
	}

	public int getNPCSize(int id) {
		NPCDefinition npc = getNPCDefinition(id);
		if (npc != null) {
			return npc.getSize();
		}
		return 1;
	}

	public int getProjectileSpeed(int i) {
		switch (npcs[i].npcType) {
		case 2881:
		case 2882:
		case 3200:
			return 85;
		case 1158:
		case 1160:
		case 1351:
		case 1352:
		case 1353:
		case 1354:
		case 1355:
		case 1356:
			return 90;
		case 2745:
		case 1590:
		case 1591:
		case 1592:
		case 1472:
		case 5362:
		case 5363:
		case 5902:
			return 130;

		case 50:
		case 4383:
			return 90;

		case 2025:
			return 85;

		case 2028:
			return 80;

		default:
			return 85;
		}
	}

	/**
	 * Npc respawn time
	 **/
	public int getRespawnTime(int i) {
		switch (npcs[i].npcType) {
		case 2881:
		case 2882:
		case 2883:
		case 6222:
		case 6225:
		case 6227:
		case 6223:
		case 6247:
		case 6250:
		case 6248:
		case 6260:
		case 6265:
		case 6261:
		case 6263:
		case 2554:
		case 2552:
		case 5666:
		case 3847:
		case 5902:
		case 5422:
		case 2349:
			return 120;
		case 5903:
		case 6142:
		case 6143:
		case 6144:
		case 6145:
			return 500;
		case 4383:
		case 912:
		case 913:
		case 914:
			return 300;
		case 1160:
			return -1;
		default:
			return 25;
		}
	}

	public boolean getsPulled(int i) {
		switch (npcs[i].npcType) {
		case 6260:
			if (npcs[i].firstAttacker > 0) {
				return false;
			}
			break;
		}
		return true;
	}

	public int getStackedDropAmount(int itemId, int npcId) {
		switch (itemId) {
		case 995:
			switch (npcId) {
			case 1:
				return 50 + Misc.random(50);
			case 9:
				return 133 + Misc.random(100);
			case 1624:
				return 1000 + Misc.random(300);
			case 1618:
				return 1000 + Misc.random(300);
			case 1643:
				return 1000 + Misc.random(300);
			case 1610:
				return 1000 + Misc.random(1000);
			case 1613:
				return 1500 + Misc.random(1250);
			case 1615:
				return 3000;
			case 18:
				return 500;
			case 101:
				return 60;
			case 913:
			case 912:
			case 914:
				return 750 + Misc.random(500);
			case 1612:
				return 250 + Misc.random(500);
			case 1648:
				return 250 + Misc.random(250);
			case 90:
				return 200;
			case 82:
				return 1000 + Misc.random(455);
			case 52:
				return 400 + Misc.random(200);
			case 49:
				return 1500 + Misc.random(2000);
			case 1341:
				return 1500 + Misc.random(500);
			case 26:
				return 500 + Misc.random(100);
			case 20:
				return 750 + Misc.random(100);
			case 21:
				return 890 + Misc.random(125);
			case 117:
				return 500 + Misc.random(250);
			case 2607:
				return 500 + Misc.random(350);
			}
			break;
		case 11212:
			return 10 + Misc.random(4);
		case 565:
		case 561:
			return 10;
		case 560:
		case 563:
		case 562:
			return 15;
		case 555:
		case 554:
		case 556:
		case 557:
			return 20;
		case 892:
			return 40;
		case 886:
			return 100;
		case 6522:
			return 6 + Misc.random(5);

		}

		return 1;
	}

	public boolean goodDistance(int objectX, int objectY, int playerX,
			int playerY, int distance) {
		for (int i = 0; i <= distance; i++) {
			for (int j = 0; j <= distance; j++) {
				if (objectX + i == playerX
						&& (objectY + j == playerY || objectY - j == playerY || objectY == playerY)) {
					return true;
				} else if (objectX - i == playerX
						&& (objectY + j == playerY || objectY - j == playerY || objectY == playerY)) {
					return true;
				} else if (objectX == playerX
						&& (objectY + j == playerY || objectY - j == playerY || objectY == playerY)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Handles the death of tztok-jad by ending the game and rewarding the
	 * player with a fire cape.
	 * 
	 * @param i
	 *            The npc.
	 */
	public void handleJadDeath(int i) {
		final Client c = (Client) PlayerHandler.players[npcs[i].spawnedBy];
		c.startAnimation(862);

		World.getSynchronizedTaskScheduler().schedule(new Task(4, false) {
			@Override
			protected void execute() {
				c.getTask().resetTzhaar();
				c.getItems().addItem(6570, 1);
				c.getFunction().playTempSong(474);
				c.waveId = 300;
				World.getFightCaves().npcChat(c,
						"You even defeated Tz-tok Jad, I am most impressed!",
						"Please accept this gift as a reward.");
				stop();
			}
		});
	}

	public void handlePoisonEffects(Client entity, int i, int damage) {
		if (doesPoison(npcs[i].npcType)) {
			if (damage > 0) {
				entity.getFunction().appendPoison(12);
			}
		}
	}

	public boolean isAggressive(int i) {
		switch (npcs[i].npcType) {
		case 5422:
		case 50:
		case 5247:
		case 3340:
		case 5384:
		case 4383:
		case 5370:
		case 5408:
		case 1590:
		case 5421:
		case 1591:
		case 1157:
		case 1154:
		case 1592:
		case 5902:
		case 5903:
		case 1158:
		case 1160:
		case 6260:
		case 6265:
		case 6261:
		case 6263:
		case 2554:
		case 6222:
		case 6225:
		case 6227:
		case 6223:
		case 6247:
		case 6250:
		case 6248:
		case 6252:
		case 2892:
		case 2894:
		case 2881:
		case 2882:
		case 3733:
		case 3734:
		case 3735:
		case 3737:
		case 3760:
		case 3765:
		case 2883:
		case 941:
			return true;
		}
		if (npcs[i].inWild() && npcs[i].MaxHP > 0) {
			return true;
		}
		if (isFightCaveNpc(i)) {
			return true;
		}
		return false;
	}

	/**
	 * Checks if something is a fight cave npc.
	 * 
	 * @param i
	 *            The npc.
	 * @return Whether or not it is a fight caves npc.
	 */
	public boolean isFightCaveNpc(int i) {
		switch (npcs[i].npcType) {
		case FightCaves.TZ_KIH:
		case FightCaves.TZ_KEK:
		case FightCaves.TOK_XIL:
		case FightCaves.YT_MEJKOT:
		case FightCaves.KET_ZEK:
		case FightCaves.TZTOK_JAD:
			return true;
		}
		return false;
	}

	public boolean isUndead(int index) {
		String name = getNPCName(npcs[index].npcType);
		for (String s : UNDEAD) {
			if (s.equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 */
	private void killedBarrow(int i) {
		final Client entity = (Client) PlayerHandler.players[npcs[i].killedBy];
		if (entity != null) {
			for (int o = 0; o < entity.barrowsNpcs.length; o++) {
				if (npcs[i].npcType == entity.barrowsNpcs[o][0]) {
					entity.barrowsNpcs[o][1] = 2; // 2 for dead
					entity.barrowsKillCount++;

				}
			}
		}
	}

	/**
	 * Raises the count of tzhaarKilled, if tzhaarKilled is equal to the amount
	 * needed to kill to move onto the next wave it raises wave id then starts
	 * next wave.
	 * 
	 * @param i
	 *            The npc.
	 */
	private void killedTzhaar(int i) {
		final Client c2 = (Client) PlayerHandler.players[npcs[i].spawnedBy];
		c2.tzhaarKilled++;
		if (c2.tzhaarKilled == c2.tzhaarToKill) {
			c2.waveId++;
			World.getSynchronizedTaskScheduler().schedule(new Task(12, false) {
				@Override
				protected void execute() {
					if (c2 != null) {
						World.getFightCaves().spawnNextWave(c2);
					}
					stop();
				}
			});

		}
	}

	public boolean loadAutoSpawn(String fileName) {
		String line = "";
		String token = "";
		String token2 = "";
		String token2_2 = "";
		String[] token3 = new String[10];
		boolean EndOfFile = false;
		BufferedReader characterfile = null;
		try {
			characterfile = new BufferedReader(new FileReader("./" + fileName));
		} catch (final FileNotFoundException fileex) {
			Misc.println(fileName + ": file not found.");
			return false;
		}
		try {
			line = characterfile.readLine();
		} catch (final IOException ioexception) {
			Misc.println(fileName + ": error loading file.");
			try {
				characterfile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return false;
		}
		int count = 0;
		while (EndOfFile == false && line != null) {
			line = line.trim();
			final int spot = line.indexOf("=");
			if (spot > -1) {
				token = line.substring(0, spot);
				token = token.trim();
				token2 = line.substring(spot + 1);
				token2 = token2.trim();
				token2_2 = token2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token3 = token2_2.split("\t");
				if (token.equals("spawn")) {
					createNpcSpawn(Integer.parseInt(token3[0]),
							Integer.parseInt(token3[1]),
							Integer.parseInt(token3[2]),
							Integer.parseInt(token3[3]),
							Integer.parseInt(token3[4]),
							getHpByCombat(getNPCCombatLevel(Integer
									.parseInt(token3[0]))),
							Integer.parseInt(token3[5]),
							Integer.parseInt(token3[6]),
							Integer.parseInt(token3[7]));
					count++;

				}
			} else {
				if (line.equals("[ENDOFSPAWNLIST]")) {
					try {
						characterfile.close();
					} catch (final IOException ioexception) {
					}
					System.out.println("Loaded " + count + " npc spawns.");
				}
			}
			try {
				line = characterfile.readLine();
			} catch (final IOException ioexception1) {
				EndOfFile = true;
			}
		}
		try {
			characterfile.close();
		} catch (final IOException ioexception) {
		}
		if (EndOfFile)
			return true;
		return false;
	}

	public void loadNPCDefintions() {
		String line = null;
		File file = new File(FileConfig.NPC_DEF_DIR);
		if (!file.exists()) {
			System.out.println("File not found!");
		}
		int count = 0;
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			while ((line = br.readLine()) != null) {
				if (line.length() > 3) {
					String[] args = line.split("%");
					addNpcDefinition(Integer.parseInt(args[0]), args[1],
							args[2], Integer.parseInt(args[3]),
							Integer.parseInt(args[4]));
					// System.out.println(args[0]);
					count++;
				}
			}
			br.close();
			System.out.println("Loaded " + count + " npc definitions.");
		} catch (IOException e) {
			System.err.println("Error: " + e);
		}

	}

	public void loadSpell(int i) {
		int random;
		switch (npcs[i].npcType) {
		case 1158:// kq first form
			int kqRandom = Misc.random(3);
			if (kqRandom == 2) {
				npcs[i].projectileId = 280; // gfx
				npcs[i].attackType = 2;
				npcs[i].endGfx = 279;
			} else {
				npcs[i].attackType = 0;
			}
			break;
		case 5422:
			if (goodDistance(npcs[i].absX, npcs[i].absY,
					PlayerHandler.players[npcs[i].killerId].absX,
					PlayerHandler.players[npcs[i].killerId].absY, 3))
				random = Misc.random(2);
			else
				random = Misc.random(1);
			if (random == 0) {
				npcs[i].attackType = 2;
				npcs[i].endGfx = -1;
				npcs[i].projectileId = 1828;
			} else if (random == 1) {
				npcs[i].attackType = 1;
				npcs[i].endGfx = -1;
				npcs[i].projectileId = 1839;
			} else if (random == 2) {
				npcs[i].attackType = 0;
				npcs[i].gfx100(1834);
				npcs[i].projectileId = -1;
			}
			break;
		case 2349:
			if (goodDistance(npcs[i].absX, npcs[i].absY,
					PlayerHandler.players[npcs[i].killerId].absX,
					PlayerHandler.players[npcs[i].killerId].absY, 2))
				random = Misc.random(2);
			else
				random = Misc.random(1);
			if (random == 0) {
				npcs[i].attackType = 2;
				npcs[i].gfx0(1885);
				npcs[i].projectileId = 1884;
			} else if (random == 1) {
				npcs[i].attackType = 1;
				npcs[i].projectileId = 1889;
			} else if (random == 2) {
				npcs[i].attackType = 0;
				npcs[i].gfx100(1886);
				npcs[i].projectileId = -1;
				npcs[i].forceChat("Feel my wrath!");
			}
			break;
		case 1160:// kq secondform
			int kqRandom2 = Misc.random(3);
			if (kqRandom2 == 2) {
				npcs[i].projectileId = 279; // gfx
				npcs[i].attackType = 1 + Misc.random(1);
				npcs[i].endGfx = 278;
			} else {
				npcs[i].attackType = 0;
			}
			break;
		case 2892:
			npcs[i].projectileId = 94;
			npcs[i].attackType = 2;
			npcs[i].endGfx = 95;
			break;
		case 2894:
			npcs[i].projectileId = 298;
			npcs[i].attackType = 1;
			break;
		case 50:
			random = Misc.random(4);
			if (random == 0) {
				npcs[i].projectileId = 393; // red
				npcs[i].endGfx = 430;
				npcs[i].attackType = 3;
			} else if (random == 1) {
				npcs[i].projectileId = 394; // green
				npcs[i].endGfx = 429;
				npcs[i].attackType = 3;
			} else if (random == 2) {
				npcs[i].projectileId = 395; // white
				npcs[i].endGfx = 431;
				npcs[i].attackType = 3;
			} else if (random == 3) {
				npcs[i].projectileId = 396; // blue
				npcs[i].endGfx = 428;
				npcs[i].attackType = 3;
			} else if (random == 4) {
				npcs[i].projectileId = -1; // melee
				npcs[i].endGfx = -1;
				npcs[i].attackType = 0;
			}
			break;
		// arma npcs
		case 6223:
			npcs[i].attackType = 0;
			break;
		case 6227:
			npcs[i].attackType = 1;
			npcs[i].projectileId = 1190;
			break;
		case 6225:
			npcs[i].attackType = 2;
			npcs[i].projectileId = 1203;
			break;
		case 6222:
			random = Misc.random(1);
			npcs[i].attackType = 1 + random;
			if (npcs[i].attackType == 1) {
				npcs[i].projectileId = 1197;
			} else {
				npcs[i].attackType = 2;
				npcs[i].projectileId = 1198;
			}
			break;
		// sara npcs
		case 6247: // sara
			random = Misc.random(1);
			if (random == 0) {
				npcs[i].attackType = 2;
				npcs[i].endGfx = 1224;
				npcs[i].projectileId = -1;
			} else if (random == 1) {
				npcs[i].attackType = 0;
			}
			break;
		case 6250: // star
			npcs[i].attackType = 0;
			break;
		case 6248: // growler
			npcs[i].attackType = 2;
			npcs[i].projectileId = 1203;
			break;
		case 6252: // bree
			npcs[i].attackType = 1;
			npcs[i].projectileId = 9;
			break;
		case 3847:// sea troll queen
			npcs[i].attackType = 2;
			npcs[i].projectileId = 174;
			npcs[i].endGfx = 175;
			break;
		// bandos npcs
		case 5666:
			random = Misc.random(2);
			if (random != 2) {
				npcs[i].attackType = 0;
			} else {
				npcs[i].endGfx = 1028;
				npcs[i].attackType = 1;
			}
			break;
		case 6260:
			random = Misc.random(2);
			if (random == 0 || random == 1) {
				npcs[i].attackType = 0;
			} else {
				npcs[i].attackType = 1;
				npcs[i].endGfx = 1211;
				npcs[i].projectileId = 288;
			}
			break;

		case 5902:
			npcs[i].attackType = 2;
			npcs[i].projectileId = 1067;
			npcs[i].endGfx = 1064;
			break;

		case 6265:
			npcs[i].attackType = 0;
			break;
		case 913:
			npcs[i].endGfx = 76;
			npcs[i].attackType = 2;
			break;
		case 912:
			npcs[i].endGfx = 78;
			npcs[i].attackType = 2;
			break;
		case 181:
			npcs[i].projectileId = 178;
			npcs[i].endGfx = 181;
			npcs[i].attackType = 2;
			break;

		case 914:
			npcs[i].endGfx = 77;
			npcs[i].attackType = 2;
			break;
		case 4383:
			npcs[i].projectileId = 368;
			npcs[i].endGfx = 369;
			npcs[i].attackType = 2;
			break;
		case 1472:
			int ran = Misc.random(2);
			npcs[i].attackType = 2;
			switch (ran) {
			case 0:
				npcs[i].projectileId = 159;
				npcs[i].endGfx = 160;
				break;
			case 1:
				npcs[i].projectileId = 156;
				npcs[i].endGfx = 157;
				break;
			case 2:
				npcs[i].projectileId = 162;
				npcs[i].endGfx = 163;
				break;
			}
			break;

		case 1351:
		case 1352:
		case 1353:
		case 1354:
		case 1355:
		case 1356:
			npcs[i].projectileId = 294;
			npcs[i].attackType = 1;
			break;
		case 1590:
		case 1591:
		case 1592:
		case 5362:
		case 5363:
			npcs[i].projectileId = 393;
			npcs[i].endGfx = 430;
			npcs[i].attackType = 3;
			break;
		case 6261:
			npcs[i].attackType = 2;
			npcs[i].projectileId = 1203;
			break;
		case 6263:
			npcs[i].attackType = 1;
			npcs[i].projectileId = 1206;
			break;
		case 2025:
			npcs[i].attackType = 2;
			final int r = Misc.random(3);
			if (r == 0) {
				npcs[i].gfx100(158);
				npcs[i].projectileId = 159;
				npcs[i].endGfx = 160;
			}
			if (r == 1) {
				npcs[i].gfx100(161);
				npcs[i].projectileId = 162;
				npcs[i].endGfx = 163;
			}
			if (r == 2) {
				npcs[i].gfx100(164);
				npcs[i].projectileId = 165;
				npcs[i].endGfx = 166;
			}
			if (r == 3) {
				npcs[i].gfx100(155);
				npcs[i].projectileId = 156;
			}
			break;
		case 2881:// supreme
			npcs[i].attackType = 1;
			npcs[i].projectileId = 298;
			break;

		case 2882:// prime
			npcs[i].attackType = 2;
			npcs[i].projectileId = 162;
			npcs[i].endGfx = 477;
			break;

		case 2028:
			npcs[i].attackType = 1;
			npcs[i].projectileId = 27;
			break;

		case 3200:
			final int r2 = Misc.random(1);
			if (r2 == 0) {
				npcs[i].attackType = 1;
				npcs[i].gfx100(550);
				npcs[i].projectileId = 551;
				npcs[i].endGfx = 552;
			} else {
				npcs[i].attackType = 2;
				npcs[i].gfx100(553);
				npcs[i].projectileId = 554;
				npcs[i].endGfx = 555;
			}
			break;
		case FightCaves.TZTOK_JAD:
			int r3 = 0;
			if (goodDistance(npcs[i].absX, npcs[i].absY,
					PlayerHandler.players[npcs[i].spawnedBy].absX,
					PlayerHandler.players[npcs[i].spawnedBy].absY, 1)) {
				r3 = Misc.random(2);
			} else {
				r3 = Misc.random(1);
			}
			if (r3 == 0) {
				npcs[i].attackType = 2;
				npcs[i].endGfx = 157;
				npcs[i].projectileId = 448;
			} else if (r3 == 1) {
				npcs[i].attackType = 1;
				npcs[i].endGfx = 451;
				npcs[i].projectileId = -1;
			} else if (r3 == 2) {
				npcs[i].attackType = 0;
				npcs[i].projectileId = -1;
			}
			break;

		case FightCaves.KET_ZEK:
			npcs[i].attackType = 2;
			npcs[i].projectileId = 445;
			npcs[i].endGfx = 446;
			break;

		case FightCaves.TOK_XIL:
			npcs[i].attackType = 1;
			npcs[i].projectileId = 443;
			break;
		}
	}

	/**
	 * load spell
	 **/
	public void loadSpell2(int i) {
		npcs[i].attackType = 3;
		int random = Misc.random(3);
		if (random == 0) {
			npcs[i].projectileId = 393; // red
			npcs[i].endGfx = 430;
		} else if (random == 1) {
			npcs[i].projectileId = 394; // green
			npcs[i].endGfx = 429;
		} else if (random == 2) {
			npcs[i].projectileId = 395; // white
			npcs[i].endGfx = 431;
		} else if (random == 3) {
			npcs[i].projectileId = 396; // blue
			npcs[i].endGfx = 428;
		}

	}

	public void multiAttackDamage(int i) {
		int max = getMaxHit(i);
		for (Player player : PlayerHandler.players) {
			if (player != null) {
				final Client entity = (Client) player;
				if (entity.isDead || entity.absZ != npcs[i].absZ) {
					continue;
				}
				if (player.goodDistance(entity.absX, entity.absY, npcs[i].absX,
						npcs[i].absY, 15)) {
					if (max > entity.playerLevel[3])
						max = entity.playerLevel[3];
					if (npcs[i].attackType == 2) {
						if (!entity.prayerActive[16]) {
							if (Misc.random(500) + 200 > Misc.random(entity
									.getCombat().mageDef())) {
								final int dam = Misc.random(max);
								entity.dealDamage(dam);
								entity.handleHitMask(dam);
							} else {
								entity.dealDamage(0);
								entity.handleHitMask(0);
							}
						} else {
							entity.dealDamage(0);
							entity.handleHitMask(0);
						}
					} else if (npcs[i].attackType == 1) {
						if (!entity.prayerActive[17]) {
							final int dam = Misc.random(max);
							if (Misc.random(500) + 200 > Misc.random(entity
									.getCombat().calculateRangeDefence())) {
								entity.dealDamage(dam);
								entity.handleHitMask(dam);
							} else {
								entity.dealDamage(0);
								entity.handleHitMask(0);
							}
						} else {
							entity.dealDamage(0);
							entity.handleHitMask(0);
						}
					}
					if (npcs[i].endGfx > 0) {
						entity.gfx0(npcs[i].endGfx);
					}
				}
				entity.getFunction().refreshSkill(3);
			}
		}
	}

	public void multiAttackGfx(int i, int gfx) {
		if (npcs[i].projectileId < 0) {
			return;
		}
		for (Player player : PlayerHandler.players) {
			if (player != null) {
				final Client entity = (Client) player;
				if (entity.absZ != npcs[i].absZ) {
					continue;
				}
				if (player.goodDistance(entity.absX, entity.absY, npcs[i].absX,
						npcs[i].absY, 15)) {
					final int nX = NPCHandler.npcs[i].getX() + offset(i);
					final int nY = NPCHandler.npcs[i].getY() + offset(i);
					final int pX = entity.getX();
					final int pY = entity.getY();
					final int offX = (nY - pY) * -1;
					final int offY = (nX - pX) * -1;
					entity.getFunction().createPlayersProjectile(nX, nY, offX,
							offY, 50, getProjectileSpeed(i),
							npcs[i].projectileId, 43, 31, -entity.getId() - 1,
							65);
				}
			}
		}
	}

	public boolean multiAttacks(int i) {
		switch (npcs[i].npcType) {
		case 6222:
		case 3847:
			return true;
		case 5422:
			if (npcs[i].attackType == 2)
				return true;
		case 6247:
			if (npcs[i].attackType == 2) {
				return true;
			}
		case 6260:
		case 5902:
			if (npcs[i].attackType == 2) {
				return true;
			}
		case 1158: // kq
			if (npcs[i].attackType == 2) {
				return true;
			}
		case 1160: // kq
		case 5666:
			if (npcs[i].attackType == 1) {
				return true;
			}
		default:
			return false;
		}

	}

	public void npcWalk(NPC c, int index, int x, int y) {
		PlayerPathFinding.getPathFinder().findRoute(c, index, x, y, true, 1, 1);
	}

	public int offset(int i) {
		switch (npcs[i].npcType) {
		case 50:
		case 2552:
			return 2;
		case 2881:
		case 2882:
		case 1590:
		case 1591:
		case 1592:
		case 5362:
		case 5363:
		case 1472:
		case 1351:
		case 1352:
		case 1353:
		case 1354:
		case 1355:
		case 1356:
		case 5666:
		case 3847:
			return 1;
		case 2745:
		case 2743:
			return 1;
		case 1158:
		case 1160:
		case 5902:
			return 2;
		}
		return 0;
	}

	/**
	 * Dropping Items!
	 **/

	public boolean rareDrops(int i) {
		return Misc.random(NPCDrops.dropRarity.get(npcs[i].npcType)) == 0;
	}

	/**
	 * Resets players in combat
	 */

	public void resetPlayersInCombat(int i) {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				if (PlayerHandler.players[j].underAttackBy2 == i) {
					PlayerHandler.players[j].underAttackBy2 = 0;
				}
			}
		}
	}

	public boolean retaliates(int npcType) {
		return npcType < 6142 || npcType > 6145
				&& !(npcType >= 2440 && npcType <= 2446);
	}

	public NPC spawnNormalNpc(int npcType, int x, int y, int heightLevel,
			int WalkingType, int HP, int maxHit, int attack, int defence) {
		// first, search for a free slot
		int slot = -1;
		for (int i = 1; i < MAX_VALUE; i++) {
			if (npcs[i] == null) {
				slot = i;
				break;
			}
		}
		if (slot == -1) {
			// Misc.println("No Free Slot");
			return null; // no free slot found
		}
		final NPC newNPC = new NPC(slot, npcType);
		newNPC.absX = x;
		newNPC.absY = y;
		newNPC.makeX = x;
		newNPC.makeY = y;
		newNPC.absZ = heightLevel;
		newNPC.walkingType = WalkingType;
		newNPC.HP = HP;
		newNPC.MaxHP = HP;
		newNPC.maxHit = maxHit;
		newNPC.attack = attack;
		newNPC.defence = defence;
		npcs[slot] = newNPC;
		return newNPC;
	}

	/**
	 * Summon npc, barrows, etc
	 **/
	public NPC spawnNpc(Client entity, int npcType, int x, int y,
			int heightLevel, int WalkingType, int HP, int maxHit, int attack,
			int defence, boolean attackPlayer, boolean headIcon) {
		// first, search for a free slot
		int slot = -1;
		for (int i = 1; i < MAX_VALUE; i++) {
			if (npcs[i] == null) {
				slot = i;
				break;
			}
		}
		if (slot == -1) {
			// Misc.println("No Free Slot");
			return null; // no free slot found
		}
		final NPC newNPC = new NPC(slot, npcType);
		newNPC.absX = x;
		newNPC.absY = y;
		newNPC.makeX = x;
		newNPC.makeY = y;
		newNPC.absZ = heightLevel;
		newNPC.walkingType = WalkingType;
		newNPC.HP = HP;
		newNPC.MaxHP = HP;
		newNPC.maxHit = maxHit;
		newNPC.attack = attack;
		newNPC.defence = defence;
		if (entity != null) {
			newNPC.spawnedBy = entity.getId();
			if (headIcon) {
				entity.getFunction().drawHeadicon(1, slot, 0, 0);
			}
			if (attackPlayer) {
				newNPC.underAttack = true;
				if (entity != null) {
					if (com.ownxile.rs2.world.games.Barrows.COFFIN_AND_BROTHERS[entity.randomCoffin][1] != newNPC.npcType) {
						if (newNPC.npcType == 2025 || newNPC.npcType == 2026
								|| newNPC.npcType == 2027
								|| newNPC.npcType == 2028
								|| newNPC.npcType == 2029
								|| newNPC.npcType == 2030) {
							newNPC.forceChat("You dare disturb my rest!");
						}
					}
					if (com.ownxile.rs2.world.games.Barrows.COFFIN_AND_BROTHERS[entity.randomCoffin][1] == newNPC.npcType) {
						newNPC.forceChat("You dare steal from us!");
					}

					newNPC.killerId = entity.playerId;
				}
			}
		}
		npcs[slot] = newNPC;
		return newNPC;
	}

	public NPC spawnNpc2(int npcType, int x, int y, int heightLevel,
			int WalkingType, int HP, int maxHit, int attack, int defence) {
		// first, search for a free slot
		int slot = -1;
		for (int i = 1; i < MAX_VALUE; i++) {
			if (npcs[i] == null) {
				slot = i;
				break;
			}
		}
		if (slot == -1) {
			// Misc.println("No Free Slot");
			return null; // no free slot found
		}
		final NPC newNPC = new NPC(slot, npcType);
		newNPC.absX = x;
		newNPC.absY = y;
		newNPC.makeX = x;
		newNPC.makeY = y;
		newNPC.absZ = heightLevel;
		newNPC.walkingType = WalkingType;
		newNPC.HP = HP;
		newNPC.MaxHP = HP;
		newNPC.maxHit = maxHit;
		newNPC.attack = attack;
		newNPC.defence = defence;
		npcs[slot] = newNPC;
		return newNPC;
	}

	public int spawnNpc3(Client c, int npcType, int x, int y, int heightLevel,
			int WalkingType, int HP, int maxHit, int attack, int defence,
			boolean attackPlayer, boolean headIcon, boolean summonFollow) {
		int slot = -1;
		for (int i = 1; i < MAX_VALUE; i++) {
			if (npcs[i] == null) {
				slot = i;
				break;
			}
		}
		if (slot == -1) {
			return slot;
		}
		NPC newNPC = new NPC(slot, npcType);
		newNPC.absX = x;
		newNPC.absY = y;
		newNPC.makeX = x;
		newNPC.makeY = y;
		newNPC.absZ = heightLevel;
		newNPC.walkingType = WalkingType;
		newNPC.HP = HP;
		newNPC.MaxHP = HP;
		newNPC.maxHit = maxHit;
		newNPC.attack = attack;
		newNPC.defence = defence;
		newNPC.spawnedBy = c.getId();
		newNPC.underAttack = true;
		newNPC.facePlayer(c.playerId);
		c.rememberNpcIndex = slot;
		if (headIcon) {
			c.getFunction().drawHeadicon(1, slot, 0, 0);
		}
		if (summonFollow) {
			newNPC.summoner = true;
			newNPC.summonedBy = c.playerId;
			c.hasNpc = true;
		}
		if (attackPlayer) {
			newNPC.underAttack = true;
			if (c != null) {
				newNPC.killerId = c.playerId;
			}
		}
		npcs[slot] = newNPC;
		return slot;
	}

	public boolean specialCase(Client entity, int i) { // responsible for npcs
														// that
														// much
		if (goodDistance(npcs[i].getX(), npcs[i].getY(), entity.getX(),
				entity.getY(), 8)
				&& !goodDistance(npcs[i].getX(), npcs[i].getY(), entity.getX(),
						entity.getY(), distanceRequired(i))) {
			return true;
		}
		return false;
	}

	public void startAnimation(int animId, int i) {
		npcs[i].animNumber = animId;
		npcs[i].getUpdateFlags().animUpdateRequired = true;
		npcs[i].getUpdateFlags().updateRequired = true;
	}

	private void stepAway(Client c, int i) {
		// TODO Auto-generated method stub
		int otherX = NPCHandler.npcs[i].getX();
		int otherY = NPCHandler.npcs[i].getY();
		if (otherX == c.getX() && otherY == c.getY()) {
			if (RegionManager
					.getClipping(c.getX() - 1, c.getY(), c.absZ, -1, 0)) {
				npcs[i].moveX = -1;
			} else if (RegionManager.getClipping(c.getX() + 1, c.getY(),
					c.absZ, 1, 0)) {
				npcs[i].moveX = 1;
			} else if (RegionManager.getClipping(c.getX(), c.getY() - 1,
					c.absZ, 0, -1)) {
				npcs[i].moveY = -1;
			} else if (RegionManager.getClipping(c.getX(), c.getY() + 1,
					c.absZ, 0, 1)) {
				npcs[i].moveY = 1;
			}
			npcs[i].getNextNPCMovement(i);
			npcs[i].getUpdateFlags().updateRequired = true;
		}
	}

	public void stepAway(int i) {
		if (RegionManager.getClipping(npcs[i].getX() - 1, npcs[i].getY(),
				npcs[i].absZ, -1, 0)) {
			npcs[i].moveX = -1;
			npcs[i].moveY = 0;
		} else if (RegionManager.getClipping(npcs[i].getX() + 1,
				npcs[i].getY(), npcs[i].absZ, 1, 0)) {
			npcs[i].moveX = 1;
			npcs[i].moveY = 0;
		} else if (RegionManager.getClipping(npcs[i].getX(),
				npcs[i].getY() - 1, npcs[i].absZ, 0, -1)) {
			npcs[i].moveX = 0;
			npcs[i].moveY = -1;
		} else if (RegionManager.getClipping(npcs[i].getX(),
				npcs[i].getY() + 1, npcs[i].absZ, 0, 1)) {
			npcs[i].moveX = 0;
			npcs[i].moveY = 1;
		}
		npcs[i].getNextNPCMovement(i);
	}

	public void summonNpc(Client entity, int npcType, int x, int y,
			int heightLevel) {
		int slot = -1;
		for (int i = 1; i < MAX_VALUE; i++) {
			if (npcs[i] == null) {
				slot = i;
				break;
			}
		}
		if (slot == -1) {
			return;
		}
		final NPC newNPC = new NPC(slot, npcType);
		newNPC.absX = x;
		newNPC.absY = y;
		newNPC.makeX = x;
		newNPC.makeY = y;
		newNPC.absZ = heightLevel;
		newNPC.walkingType = 0;
		newNPC.HP = 100;
		newNPC.MaxHP = 100;
		newNPC.spawnedBy = entity.getId();
		newNPC.underAttack = true;
		newNPC.facePlayer(entity.playerId);
		newNPC.summoner = true;
		newNPC.summonedBy = entity.playerId;
		entity.summonId = npcType;
		entity.hasNpc = true;
		newNPC.underAttack = true;
		if (entity != null) {
			newNPC.killerId = entity.playerId;
		}
		npcs[slot] = newNPC;
	}

	public boolean switchesAttackers(int i) {
		switch (npcs[i].npcType) {
		case 6265:
		case 6261:
		case 6263:
		case 2554:
		case 6225:
		case 6227:
		case 6223:
		case 6250:
		case 6248:
		case 6252:
		case 2892:
		case 2894:
			return true;
		}

		return false;
	}

	public void tick() {
		for (int i = 0; i < MAX_VALUE; i++) {
			if (npcs[i] == null) {
				continue;
			}
			npcs[i].clearUpdateFlags();

		}
		for (int i = 0; i < MAX_VALUE; i++) {
			if (npcs[i] != null) {
				if (npcs[i].actionTimer > 0) {
					npcs[i].actionTimer--;
				}
				Client slaveOwner = PlayerHandler.players[npcs[i].summonedBy] != null ? (Client) PlayerHandler.players[npcs[i].summonedBy]
						: null;
				if (npcs[i] != null && slaveOwner == null && npcs[i].summoner) {
					npcs[i].absX = 0;
					npcs[i].absY = 0;
				}
				if (npcs[i] != null
						&& slaveOwner != null
						&& slaveOwner.hasNpc
						&& (!slaveOwner.goodDistance(npcs[i].getX(),
								npcs[i].getY(), slaveOwner.absX,
								slaveOwner.absY, 15) || slaveOwner.absZ != npcs[i].absZ)
						&& npcs[i].summoner) {
					npcs[i].absX = slaveOwner.absX;
					npcs[i].absY = slaveOwner.absY;
					npcs[i].absZ = slaveOwner.absZ;
					npcs[i].facePlayer(slaveOwner.playerId);
				}
				if (npcs[i] != null && slaveOwner != null && slaveOwner.hasNpc
						&& slaveOwner.absX == npcs[i].getX()
						&& slaveOwner.absY == npcs[i].getY()
						&& npcs[i].summoner) {
					stepAway(i);
				}
				if (npcs[i].freezeTimer > 0) {
					npcs[i].freezeTimer--;
				}

				if (npcs[i].hitDelayTimer > 0) {
					npcs[i].hitDelayTimer--;
				}

				if (npcs[i].hitDelayTimer == 1) {
					npcs[i].hitDelayTimer = 0;
					applyDamage(i);
				}

				if (npcs[i].attackTimer > 0) {
					npcs[i].attackTimer--;
				}
				/*
				 * if (npcs[i].npcType == catId) { followPlayer(i,
				 * entity.playerId); }
				 */
				if (npcs[i].spawnedBy > 0) { // delete summons npc
					if (PlayerHandler.players[npcs[i].spawnedBy] == null
							|| PlayerHandler.players[npcs[i].spawnedBy].absZ != npcs[i].absZ
							|| PlayerHandler.players[npcs[i].spawnedBy].isDead
							|| !PlayerHandler.players[npcs[i].spawnedBy]
									.goodDistance(
											npcs[i].getX(),
											npcs[i].getY(),
											PlayerHandler.players[npcs[i].spawnedBy]
													.getX(),
											PlayerHandler.players[npcs[i].spawnedBy]
													.getY(), 20)) {

						if (PlayerHandler.players[npcs[i].spawnedBy] != null) {
							for (int o = 0; o < PlayerHandler.players[npcs[i].spawnedBy].barrowsNpcs.length; o++) {
								if (npcs[i].npcType == PlayerHandler.players[npcs[i].spawnedBy].barrowsNpcs[o][0]) {
									if (PlayerHandler.players[npcs[i].spawnedBy].barrowsNpcs[o][1] == 1) {
										PlayerHandler.players[npcs[i].spawnedBy].barrowsNpcs[o][1] = 0;
									}
								}
							}
						}
						npcs[i] = null;
					}
				}
				if (npcs[i] == null) {
					continue;
				}
				/**
				 * Attacking player
				 **/
				if (isAggressive(i) && !npcs[i].underAttack && !npcs[i].isDead
						&& !switchesAttackers(i)) {
					npcs[i].killerId = getClosePlayer(i);
				} else if (isAggressive(i) && !npcs[i].underAttack
						&& !npcs[i].isDead && switchesAttackers(i)) {
					npcs[i].killerId = getClosePlayer(i);
				}
				if (System.currentTimeMillis() - npcs[i].lastDamageTaken > 5000) {
					npcs[i].underAttackBy = 0;
				}

				if ((npcs[i].killerId > 0 || npcs[i].underAttack)
						&& retaliates(npcs[i].npcType)) {
					if (!npcs[i].isDead) {
						final int p = npcs[i].killerId;
						if (PlayerHandler.players[p] != null) {
							if (npcs[i].summoner == false) {
								final Client entity = (Client) PlayerHandler.players[p];
								followPlayer(i, entity.playerId);
								stepAway(entity, p);
								if (npcs[i] == null) {
									continue;
								}
								if (npcs[i].attackTimer == 0) {
									{
										try {
											attackPlayer(entity, i);
										} catch (Exception e) {
										}
									}
								}
							} else {
								final Client entity = (Client) PlayerHandler.players[p];
								followPlayer(i, entity.playerId);

							}

						} else {
							npcs[i].killerId = 0;
							npcs[i].underAttack = false;
							npcs[i].facePlayer(0);
						}
					}
				}

				/**
				 * Random walking and walking home
				 **/
				if (npcs[i] == null) {
					continue;
				}
				if ((!npcs[i].underAttack || npcs[i].walkingHome)
						&& npcs[i].randomWalk && !npcs[i].isDead) {
					if (System.currentTimeMillis() - npcs[i].recentlyInteracted > 6000) {
						npcs[i].facePlayer(0);
						npcs[i].killerId = 0;
						if (npcs[i].spawnedBy == 0) {
							if (npcs[i].absX > npcs[i].makeX
									+ GameConfig.NPC_RANDOM_WALK_DISTANCE
									|| npcs[i].absX < npcs[i].makeX
											- GameConfig.NPC_RANDOM_WALK_DISTANCE
									|| npcs[i].absY > npcs[i].makeY
											+ GameConfig.NPC_RANDOM_WALK_DISTANCE
									|| npcs[i].absY < npcs[i].makeY
											- GameConfig.NPC_RANDOM_WALK_DISTANCE) {
								npcs[i].walkingHome = true;
							}
						}

						if (npcs[i].walkingHome
								&& npcs[i].absX == npcs[i].makeX
								&& npcs[i].absY == npcs[i].makeY) {
							npcs[i].walkingHome = false;
						} else if (npcs[i].walkingHome) {
							npcs[i].moveX = GetMove(npcs[i].absX, npcs[i].makeX);
							npcs[i].moveY = GetMove(npcs[i].absY, npcs[i].makeY);
							npcs[i].getNextNPCMovement(i);
							npcs[i].getUpdateFlags().updateRequired = true;
						}
						if (npcs[i].walkingType == 1) {
							if (Misc.random(3) == 1 && !npcs[i].walkingHome) {
								int MoveX = 0;
								int MoveY = 0;
								final int Rnd = Misc.random(9);
								int distance = 1 + Misc.random(3);
								if (Rnd == 1) {
									MoveX = distance;
									MoveY = distance;
								} else if (Rnd == 2) {
									MoveX = -1;
								} else if (Rnd == 3) {
									MoveY = -1;
								} else if (Rnd == 4) {
									MoveX = distance;
								} else if (Rnd == 5) {
									MoveY = distance;
								} else if (Rnd == 6) {
									MoveX = -1;
									MoveY = -1;
								} else if (Rnd == 7) {
									MoveX = -1;
									MoveY = distance;
								} else if (Rnd == 8) {
									MoveX = distance;
									MoveY = -1;
								}

								if (MoveX == 1) {
									if (npcs[i].absX + MoveX < npcs[i].makeX + 1) {
										npcs[i].moveX = MoveX;
									} else {
										npcs[i].moveX = 0;
									}
								}

								if (MoveX == -1) {
									if (npcs[i].absX - MoveX > npcs[i].makeX - 1) {
										npcs[i].moveX = MoveX;
									} else {
										npcs[i].moveX = 0;
									}
								}

								if (MoveY == 1) {
									if (npcs[i].absY + MoveY < npcs[i].makeY + 1) {
										npcs[i].moveY = MoveY;
									} else {
										npcs[i].moveY = 0;
									}
								}

								if (MoveY == -1) {
									if (npcs[i].absY - MoveY > npcs[i].makeY - 1) {
										npcs[i].moveY = MoveY;
									} else {
										npcs[i].moveY = 0;
									}
								}

								npcs[i].getNextNPCMovement(i);
								npcs[i].getUpdateFlags().updateRequired = true;
							}
						}
					}
				}

				if (npcs[i].isDead == true) {
					if (npcs[i].actionTimer == 0 && npcs[i].applyDead == false
							&& npcs[i].needRespawn == false) {
						npcs[i].getUpdateFlags().updateRequired = true;
						npcs[i].facePlayer(0);
						npcs[i].killedBy = getNpcKillerId(i);
						if (!(npcs[i].npcType > 6141 && npcs[i].npcType < 6146))
							npcs[i].animNumber = NPCAnimate.getNPCDeathEmote(i); // dead
																					// emote
						npcs[i].getUpdateFlags().animUpdateRequired = true;
						npcs[i].freezeTimer = 0;
						npcs[i].applyDead = true;
						killedBarrow(i);
						if (isFightCaveNpc(i)) {
							killedTzhaar(i);
						}

						npcs[i].actionTimer = 4; // delete time
						resetPlayersInCombat(i);
					} else if (npcs[i].actionTimer == 0
							&& npcs[i].applyDead == true
							&& npcs[i].needRespawn == false) {
						npcs[i].needRespawn = true;
						npcs[i].actionTimer = getRespawnTime(i); // respawn time
						dropItems(i); // npc drops items!
						tzhaarDeathHandler(i);
						Client client = (Client) PlayerHandler.players[npcs[i].killedBy];
						Plugin.execute("kill_npc_" + npcs[i].npcType, client);
						npcs[i].absX = npcs[i].makeX;
						npcs[i].absY = npcs[i].makeY;
						npcs[i].HP = npcs[i].MaxHP;
						npcs[i].animNumber = 0x328;
						npcs[i].getUpdateFlags().updateRequired = true;
						npcs[i].getUpdateFlags().animUpdateRequired = true;
						if (npcs[i].npcType >= 2440 && npcs[i].npcType <= 2446) {
							World.getObjectManager().removeObject(npcs[i].absX,
									npcs[i].absY, npcs[i].absZ);
						}
						// NPCKill.getKill(npcs[i].npcType, entity);
						/* entity.getPA().loadNpcDeaths(npcs[i].npcType); */
						if (npcs[i].npcType == 2745) {
							handleJadDeath(i);

						}

					} else if (npcs[i].actionTimer == 0
							&& npcs[i].needRespawn == true) {
						if (npcs[i].spawnedBy > 0) {
							npcs[i] = null;
						} else {
							final int old1 = npcs[i].npcType;
							final int old2 = npcs[i].makeX;
							final int old3 = npcs[i].makeY;
							final int old4 = npcs[i].absZ;
							final int old5 = npcs[i].walkingType;
							final int old6 = npcs[i].MaxHP;
							final int old7 = npcs[i].maxHit;
							final int old8 = npcs[i].attack;
							final int old9 = npcs[i].defence;

							npcs[i] = null;
							if (old1 == 5247)
								PenanceBoss.setNpc(createNpcSpawn(old1, old2,
										old3, old4, old5, old6, old7, old8,
										old9));
							else if (old1 == 6142)
								World.getPestControl().portal1 = createNpcSpawn(
										old1, old2, old3, old4, old5, old6,
										old7, old8, old9);
							else if (old1 == 6143)
								World.getPestControl().portal2 = createNpcSpawn(
										old1, old2, old3, old4, old5, old6,
										old7, old8, old9);
							else if (old1 == 6144)
								World.getPestControl().portal3 = createNpcSpawn(
										old1, old2, old3, old4, old5, old6,
										old7, old8, old9);
							else if (old1 == 6145)
								World.getPestControl().portal4 = createNpcSpawn(
										old1, old2, old3, old4, old5, old6,
										old7, old8, old9);

							else
								createNpcSpawn(old1, old2, old3, old4, old5,
										old6, old7, old8, old9);
						}
					}
				}
			}
		}
	}

	/**
	 * Checks if a tzhaar npc has been killed, if so then it checks if it needs
	 * to do the tz-kek effect. If tzKek spawn has been killed twice or didn't
	 * need to be killed it calls killedTzhaar.
	 * 
	 * @param i
	 *            The npc.
	 */
	private void tzhaarDeathHandler(int i) {
		if (isFightCaveNpc(i) && npcs[i].npcType != FightCaves.TZ_KEK) {
			killedTzhaar(i);
		}
		if (npcs[i].npcType == FightCaves.TZ_KEK_SPAWN) {
			int p = npcs[i].killerId;
			if (PlayerHandler.players[p] != null) {
				Client c = (Client) PlayerHandler.players[p];
				c.tzKekSpawn += 1;
				if (c.tzKekSpawn == 2) {
					killedTzhaar(i);
					c.tzKekSpawn = 0;
				}
			}
		}
		if (npcs[i].npcType == FightCaves.TZ_KEK) {
			int p = npcs[i].killerId;
			if (PlayerHandler.players[p] != null) {
				Client c = (Client) PlayerHandler.players[p];
				FightCaves.tzKekEffect(c, i);
			}
		}
	}

}
