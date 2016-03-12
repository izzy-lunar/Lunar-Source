package com.ownxile.rs2.combat;

import com.ownxile.config.CombatConfig;
import com.ownxile.config.GameConfig;
import com.ownxile.core.World;
import com.ownxile.core.task.Task;
import com.ownxile.rs2.combat.melee.MeleeFormula;
import com.ownxile.rs2.combat.prayer.Prayer;
import com.ownxile.rs2.combat.prayer.PrayerData;
import com.ownxile.rs2.combat.range.RangeConfiguration;
import com.ownxile.rs2.combat.sounds.WeaponSounds;
import com.ownxile.rs2.content.action.Resting;
import com.ownxile.rs2.items.GroundItemHandler;
import com.ownxile.rs2.items.ItemAssistant;
import com.ownxile.rs2.npcs.NPC;
import com.ownxile.rs2.npcs.NPCHandler;
import com.ownxile.rs2.player.Client;
import com.ownxile.rs2.player.Player;
import com.ownxile.rs2.player.PlayerHandler;
import com.ownxile.rs2.player.PlayerSave;
import com.ownxile.rs2.world.games.CastleWars;
import com.ownxile.rs2.world.region.RegionManager;
import com.ownxile.util.Misc;

public class CombatHandler {

	private final Client player;

	public CombatHandler(Client client) {
		player = client;
	}

	public void activatePrayer(int i) {
		Prayer.activePrayer(player, i);
	}

	public void appendMultiBarrage(int playerId, boolean splashed) {
		if (PlayerHandler.players[playerId] != null) {
			final Client c2 = (Client) PlayerHandler.players[playerId];
			if (c2.isDead) {
				return;
			}
			if (checkMultiBarrageReqs(playerId)) {
				player.barrageCount++;
				if (Misc.random(mageAtk()) > Misc.random(mageDef())
						&& !player.magicFailed) {
					if (getEndGfxHeight() == 100) { // end GFX
						c2.gfx100(player.MAGIC_SPELLS[player.oldSpellId][5]);
					} else {
						c2.gfx0(player.MAGIC_SPELLS[player.oldSpellId][5]);
					}
					int damage = Misc
							.random(player.MAGIC_SPELLS[player.oldSpellId][6]);
					if (c2.prayerActive[12]) {
						damage *= (int) .60;
					}
					if (c2.playerLevel[3] - damage < 0) {
						damage = c2.playerLevel[3];
					}
					player.getFunction()
							.addSkillXP(
									(player.MAGIC_SPELLS[player.oldSpellId][7] + damage
											* CombatConfig
													.getCombatXP(player.playerTitle)),
									6);
					player.getFunction()
							.addSkillXP(
									(player.MAGIC_SPELLS[player.oldSpellId][7] + damage
											* CombatConfig.getCombatXP(player.playerTitle)
											/ 3), 3);
					PlayerHandler.players[playerId].handleHitMask(damage);
					PlayerHandler.players[playerId].dealDamage(damage);
					PlayerHandler.players[playerId].damageTaken[player.playerId] += damage;
					c2.getFunction().refreshSkill(3);
					player.totalPlayerDamageDealt += damage;
					multiSpellEffect(playerId, damage);
				} else {
					c2.gfx100(85);
				}
			}
		}
	}

	public void appendVengeance(int otherPlayer, int damage) {
		if (damage <= 0) {
			return;
		}
		final Player o = PlayerHandler.players[otherPlayer];
		o.getUpdateFlags().forcedText = "Taste Vengeance!";
		o.getUpdateFlags().forcedChatUpdateRequired = true;
		o.updateRequired = true;
		o.vengOn = false;
		if (o.playerLevel[3] - damage > 0) {
			damage = (int) (damage * 0.75);
			if (damage > player.playerLevel[3]) {
				damage = player.playerLevel[3];
			}
			player.setHitDiff2(damage);
			player.setHitUpdateRequired2(true);
			player.playerLevel[3] -= damage;
			player.getFunction().refreshSkill(3);
		}
		player.updateRequired = true;
	}

	public void appendVengeanceNpc(int i, int damage) {
		if (damage <= 0) {
			return;
		}
		NPC o = NPCHandler.npcs[i];
		player.forcedChat("Taste Vengeance!");
		player.updateRequired = true;
		player.vengOn = false;
		if (o.HP - damage > 0) {
			damage = (int) (damage * 0.75);
			o.HP -= (int) damage;
			o.handleHitMask((int) damage);
		}
		player.updateRequired = true;
	}

	public void applyNpcMeleeDamage(int i, int damageMask) {
		int damage = Misc.random(MeleeFormula.calculateMeleeMaxHit(player));
		final boolean fullVeracsEffect = player.getFunction().fullVeracs()
				&& Misc.random(3) == 1;
		if (!fullVeracsEffect) {
			if (Misc.random(NPCHandler.npcs[i].defence) > 10 + Misc
					.random(calculateMeleeAttack())) {
				damage = 0;
			} else if (NPCHandler.npcs[i].npcType == 2882
					|| NPCHandler.npcs[i].npcType == 2883) {
				damage = 0;
			}
		}
		boolean guthansEffect = false;
		if (player.getFunction().fullGuthans()) {
			if (Misc.random(2) == 1) {
				guthansEffect = true;
			}
		}
		if (player.fightMode == 3) {
			player.getFunction()
					.addSkillXP(
							(damage
									* CombatConfig
											.getCombatXP(player.playerTitle) / 3),
							0);
			player.getFunction()
					.addSkillXP(
							(damage
									* CombatConfig
											.getCombatXP(player.playerTitle) / 3),
							1);
			player.getFunction()
					.addSkillXP(
							(damage
									* CombatConfig
											.getCombatXP(player.playerTitle) / 3),
							2);
			player.getFunction()
					.addSkillXP(
							(damage
									* CombatConfig
											.getCombatXP(player.playerTitle) / 3),
							3);
			player.getFunction().refreshSkill(0);
			player.getFunction().refreshSkill(1);
			player.getFunction().refreshSkill(2);
			player.getFunction().refreshSkill(3);
		} else {
			player.getFunction().addSkillXP(
					(damage * CombatConfig.getCombatXP(player.playerTitle)),
					player.fightMode);
			player.getFunction()
					.addSkillXP(
							(damage
									* CombatConfig
											.getCombatXP(player.playerTitle) / 3),
							3);
			player.getFunction().refreshSkill(player.fightMode);
			player.getFunction().refreshSkill(3);
		}
		if (damage > 0) {
			if (player.inPestControl()) {
				player.pcDamage += damage;
				player.getFunction().sendFrame126("" + player.pcDamage + "",
						25116);
			}
		}
		if (damage > 0 && guthansEffect) {
			player.playerLevel[3] += damage;
			if (player.playerLevel[3] > player
					.getLevelForXP(player.playerXP[3])) {
				player.playerLevel[3] = player
						.getLevelForXP(player.playerXP[3]);
			}
			player.getFunction().refreshSkill(3);
			NPCHandler.npcs[i].gfx0(398);
		}
		if (player.getSlayer().getTask() != null) {
			if (player.playerEquipment[player.playerHat] == 8291
					&& player.getSlayer().getTask()
							.isTask(NPCHandler.npcs[i].npcType)) {
				damage = (int) (damage * 1.15);
			}
		}
		if (NPCHandler.npcs[i].HP - damage < 0) {
			damage = NPCHandler.npcs[i].HP;
		}
		applySoulsplit(player.playerId, damage);
		NPCHandler.npcs[i].underAttack = true;
		// Tick.npcHandler.npcs[i].killerId = player.playerId;
		player.killingNpcIndex = player.npcIndex;
		player.lastNpcAttacked = i;
		switch (damageMask) {
		case 1:
			NPCHandler.npcs[i].hitDiff = damage;
			NPCHandler.npcs[i].HP -= damage;
			player.totalDamageDealt += damage;
			NPCHandler.npcs[i].getUpdateFlags().hitUpdateRequired = true;
			NPCHandler.npcs[i].getUpdateFlags().updateRequired = true;
			break;

		case 2:
			NPCHandler.npcs[i].hitDiff2 = damage;
			NPCHandler.npcs[i].HP -= damage;
			player.totalDamageDealt += damage;
			NPCHandler.npcs[i].getUpdateFlags().hitUpdateRequired2 = true;
			NPCHandler.npcs[i].getUpdateFlags().updateRequired = true;
			player.doubleHit = false;
			break;

		}
	}

	public int updateDamage(int i, int damageMask, int damage) {
		final Client o = (Client) PlayerHandler.players[i];
		if (o == null || o.playerLevel[3] < 1) {
			return 0;
		}
		player.previousDamage = damage;
		boolean veracsEffect = false;
		boolean guthansEffect = false;
		if (player.getFunction().fullVeracs()) {
			if (Misc.random(2) == 1) {
				veracsEffect = true;
			}
		}
		if (player.getFunction().fullGuthans()) {
			if (Misc.random(2) == 1) {
				guthansEffect = true;
			}
		}
		if (damageMask == 1) {
			damage = player.delayedDamage;
			player.delayedDamage = 0;
		} else {
			damage = player.delayedDamage2;
			player.delayedDamage2 = 0;
		}
		if (Misc.random(o.getCombat().calculateMeleeDefence()) > Misc
				.random(calculateMeleeAttack()) && !veracsEffect) {
			damage = 0;
			player.bonusAttack = 0;
		} else if (player.playerEquipment[player.playerWeapon] == 5698
				&& o.poisonDamage <= 0 && Misc.random(3) == 1) {
			o.getFunction().appendPoison(13);
			player.bonusAttack += damage / 3;
		} else {
			player.bonusAttack += damage / 3;
		}
		if (player.maxNextHit) {
			damage = MeleeFormula.calculateMeleeMaxHit(player);
		}
		if (damage > 0 && guthansEffect) {
			player.playerLevel[3] += damage;
			if (player.playerLevel[3] > player
					.getLevelForXP(player.playerXP[3])) {
				player.playerLevel[3] = player
						.getLevelForXP(player.playerXP[3]);
			}
			player.getFunction().refreshSkill(3);
			o.gfx0(398);
		}
		if (player.ssSpec && damageMask == 2) {
			damage = 5 + Misc.random(11);
			player.ssSpec = false;
		}
		if (o.prayerActive[18]
				&& System.currentTimeMillis() - o.protMeleeDelay > 1500
				&& !veracsEffect) { // if prayer active reduce damage by 40%
			damage = (int) damage * 60 / 100;
		}
		if (damage > 9) {
			if (o.playerEquipment[player.playerShield] == 13740) {
				damage = (int) damage * 85 / 100;
				o.sendMessage("Your shield reduces the damage received by 30%.");
			}
			if (o.playerEquipment[player.playerShield] == 13742) {
				if (Misc.random(10) < 7) {
					damage = (int) damage * 75 / 100;
					o.sendMessage("Your shield reduces damage received by 25%.");
				}

			}
		}
		if (PlayerHandler.players[i].playerLevel[3] - damage < 0) {
			damage = PlayerHandler.players[i].playerLevel[3];
		}
		if (o.playerLevel[3] < 20)
			Prayer.handleRedemption(o);

		if (player.fightMode == 3) {
			player.getFunction()
					.addSkillXP(
							(damage
									* CombatConfig
											.getCombatXP(player.playerTitle) / 3),
							0);
			player.getFunction()
					.addSkillXP(
							(damage
									* CombatConfig
											.getCombatXP(player.playerTitle) / 3),
							1);
			player.getFunction()
					.addSkillXP(
							(damage
									* CombatConfig
											.getCombatXP(player.playerTitle) / 3),
							2);
			player.getFunction()
					.addSkillXP(
							(damage
									* CombatConfig
											.getCombatXP(player.playerTitle) / 3),
							3);
			player.getFunction().refreshSkill(0);
			player.getFunction().refreshSkill(1);
			player.getFunction().refreshSkill(2);
			player.getFunction().refreshSkill(3);
		} else {
			player.getFunction().addSkillXP(
					(damage * CombatConfig.getCombatXP(player.playerTitle)),
					player.fightMode);
			player.getFunction()
					.addSkillXP(
							(damage
									* CombatConfig
											.getCombatXP(player.playerTitle) / 3),
							3);
			player.getFunction().refreshSkill(player.fightMode);
			player.getFunction().refreshSkill(3);
		}
		return damage;
	}

	public void applyPlayerDamage(int i, int damageMask, int damage) {
		final Client o = (Client) PlayerHandler.players[i];
		if (o.playerLevel[3] - damage < 0)
			damage = o.playerLevel[3];
		switch (player.specEffect) {
		case 1: // dragon scimmy special
			if (damage > 0) {
				if (o.prayerActive[16] || o.prayerActive[17]
						|| o.prayerActive[18]) {
					o.headIcon = -1;
					o.getFunction().sendFrame36(player.PRAYER_GLOW[16], 0);
					o.getFunction().sendFrame36(player.PRAYER_GLOW[17], 0);
					o.getFunction().sendFrame36(player.PRAYER_GLOW[18], 0);
				}
				o.sendMessage("You have been injured!");
				o.stopPrayerDelay = System.currentTimeMillis();
				o.prayerActive[16] = false;
				o.prayerActive[17] = false;
				o.prayerActive[18] = false;
				o.getFunction().requestUpdates();
			}
			break;
		case 2:
			if (damage > 0) {
				if (o.freezeTimer <= 0) {
					o.freezeTimer = 30;
				}
				o.gfx0(369);
				o.sendMessage("You have been frozen.");
				o.frozenBy = player.playerId;
				o.stopMovement();
				player.sendMessage("You freeze your enemy.");
			}
			break;
		case 3:
			if (damage > 0) {
				o.playerLevel[1] -= damage;
				o.sendMessage("You feel weak.");
				if (o.playerLevel[1] < 1) {
					o.playerLevel[1] = 1;
				}
				o.getFunction().refreshSkill(1);
			}
			break;
		case 4:
			if (damage > 0) {
				if (player.playerLevel[3] + damage > player
						.getLevelForXP(player.playerXP[3])) {
					if (player.playerLevel[3] > player
							.getLevelForXP(player.playerXP[3])) {
						;
					} else {
						player.playerLevel[3] = player
								.getLevelForXP(player.playerXP[3]);
					}
				} else {
					player.playerLevel[3] += damage;
				}
				player.getFunction().refreshSkill(3);
			}
			break;
		case 5:
			player.clawDelay = 2;
			break;
		}
		if (o.vengOn && damage > 0) {
			appendVengeance(i, damage);
		}
		if (damage > 0) {
			applyRecoil(damage, i);
		}
		player.specEffect = 0;
		PlayerHandler.players[i].logoutDelay = System.currentTimeMillis();
		PlayerHandler.players[i].underAttackBy = player.playerId;
		PlayerHandler.players[i].killerId = player.playerId;
		PlayerHandler.players[i].singleCombatDelay = System.currentTimeMillis();
		if (player.killedBy != PlayerHandler.players[i].playerId) {
			player.totalPlayerDamageDealt = 0;
		}
		player.killedBy = PlayerHandler.players[i].playerId;
		applySmite(i, damage);
		applySoulsplit(i, damage);
		switch (damageMask) {
		case 1:
			/*
			 * if (!Server.playerHandler.players[i].getHitUpdateRequired()){
			 * Server.playerHandler.players[i].setHitDiff(damage);
			 * Server.playerHandler.players[i].setHitUpdateRequired(true); }
			 * else { Server.playerHandler.players[i].setHitDiff2(damage);
			 * Server.playerHandler.players[i].setHitUpdateRequired2(true); }
			 */
			// Server.playerHandler.players[i].playerLevel[3] -= damage;
			PlayerHandler.players[i].dealDamage(damage);
			PlayerHandler.players[i].damageTaken[player.playerId] += damage;
			player.totalPlayerDamageDealt += damage;
			PlayerHandler.players[i].updateRequired = true;
			o.getFunction().refreshSkill(3);
			break;

		case 2:
			/*
			 * if (!Server.playerHandler.players[i].getHitUpdateRequired2()){
			 * Server.playerHandler.players[i].setHitDiff2(damage);
			 * Server.playerHandler.players[i].setHitUpdateRequired2(true); }
			 * else { Server.playerHandler.players[i].setHitDiff(damage);
			 * Server.playerHandler.players[i].setHitUpdateRequired(true); }
			 */
			// Server.playerHandler.players[i].playerLevel[3] -= damage;
			PlayerHandler.players[i].dealDamage(damage);
			PlayerHandler.players[i].damageTaken[player.playerId] += damage;
			player.totalPlayerDamageDealt += damage;
			PlayerHandler.players[i].updateRequired = true;
			player.doubleHit = false;
			o.getFunction().refreshSkill(3);
			break;
		}
		PlayerHandler.players[i].handleHitMask(damage);
	}

	public void applyRecoil(int damage, int i) {
		if (damage > 0
				&& PlayerHandler.players[i].playerEquipment[player.playerRing] == 2550) {
			final int recDamage = damage / 10 + 1;
			if (!player.getHitUpdateRequired()) {
				player.setHitDiff(recDamage);
				player.setHitUpdateRequired(true);
			} else if (!player.getHitUpdateRequired2()) {
				player.setHitDiff2(recDamage);
				player.setHitUpdateRequired2(true);
			}
			player.dealDamage(recDamage);
			player.updateRequired = true;
		}
	}

	public void applyRecoilNpc(int d, int i) {
		if (d > 3 && player.playerEquipment[player.playerRing] == 2550) {
			int damage = d / 10 + 1;
			NPC o = NPCHandler.npcs[i];
			if (o.HP - damage > 0) {
				o.HP -= (int) damage;
				o.handleHitMask((int) damage);
			} else {
				damage = o.HP;
				o.HP -= (int) damage;
				o.handleHitMask((int) damage);
			}
		}
	}

	public void applySmite(int index, int damage) {
		if (!player.prayerActive[23]) {
			return;
		}
		if (damage <= 0) {
			return;
		}
		if (PlayerHandler.players[index] != null) {
			final Client c2 = (Client) PlayerHandler.players[index];
			c2.playerLevel[5] -= (int) (damage / 4);
			if (c2.playerLevel[5] <= 0) {
				c2.playerLevel[5] = 0;
				Prayer.resetPrayers(c2);
			}
			c2.getFunction().refreshSkill(5);
		}

	}

	public void applySoulsplit(int index, int damage) {
		if (!player.prayerActive[27])
			return;
		if (damage <= 0)
			return;
		if (PlayerHandler.players[index] != null) {
			Client c2 = (Client) PlayerHandler.players[index];
			if (player.prayerActive[27]
					&& player.playerLevel[3] < player.getTask().getLevelForXP(
							player.playerXP[3])) {
				int heal = (int) (damage / 5);
				if (player.playerLevel[3] + heal >= player.getTask()
						.getLevelForXP(player.playerXP[3])) {
					player.playerLevel[3] = player.getTask().getLevelForXP(
							player.playerXP[3]);
				} else {
					player.playerLevel[3] += heal;
					if (heal > 5)
						player.sendMessage("The soulsplit prayer heals you "
								+ heal + " hitpoints.");
				}
				player.getTask().refreshSkill(3);
			}
			c2.playerLevel[5] -= (int) (damage / 4);
			if (c2.playerLevel[5] <= 0) {
				c2.playerLevel[5] = 0;
				Prayer.resetPrayers(c2);
			}
			c2.getTask().refreshSkill(5);
		}

	}

	public boolean armaNpc(int i) {
		switch (NPCHandler.npcs[i].npcType) {
		case 2558:
		case 2559:
		case 2560:
		case 2561:
			return true;
		}
		return false;
	}

	public void attackNpc(int i) {
		if (NPCHandler.npcs[i] != null) {
			if (NPCHandler.npcs[i].isDead || NPCHandler.npcs[i].MaxHP <= 0
					|| NPCHandler.npcs[i].HP < 1) {
				player.usingMagic = false;
				player.faceUpdate(0);
				player.npcIndex = 0;
				return;
			}
			if(player.playerEquipment[player.playerWeapon] == 8286){
				//player.usingMagic = true;
				player.autocastId = 52;
				player.autocasting = true;
			}
			player.usingArrows = false;
			player.usingBow = false;
			player.usingOtherRangeWeapons = false;
			if (!player.usingMagic) {
				for (final int bowId : RangeConfiguration.BOWS) {
					if (player.playerEquipment[player.playerWeapon] == bowId) {
						player.usingBow = true;
						for (final int arrowId : RangeConfiguration.ARROWS) {
							if (player.playerEquipment[player.playerArrows] == arrowId) {
								player.usingArrows = true;
							}
						}
					}
				}

				for (final int otherRangeId : RangeConfiguration.OTHER_RANGE_WEAPONS) {
					if (player.playerEquipment[player.playerWeapon] == otherRangeId) {
						player.usingOtherRangeWeapons = true;
					}
				}
			}
			boolean sameSpot = player.absX == NPCHandler.npcs[i].absX
					&& player.absY == NPCHandler.npcs[i].absY;
			if (RegionManager.pathBlocked(player, NPCHandler.npcs[i])
					&& !player.usingOtherRangeWeapons && !player.usingBow
					&& !sameSpot && !player.usingMagic && !player.autocasting) {
				return;
			}
			if (player.isDead || player.isTeleporting) {
				player.npcIndex = 0;
				resetPlayerAttack();
				return;
			}
			if (NPCHandler.npcs[i].npcType == 757
					&& player.getQuest(15).getId() == 4) {
				player.playerChat("Didn't I kill that guy?!",
						"I must be seeing things...");
				resetPlayerAttack();
				return;
			}
			// if (sameSpot) {
			// player.getFunction().walkTo(0, -1);
			// // player.getFunction().stopDiagonal(NPCHandler.npcs[i].absX,
			// NPCHandler.npcs[i].absY - 1);
			// return;
			// }
			if (NPCHandler.npcs[i].underAttackBy > 0
					&& NPCHandler.npcs[i].underAttackBy != player.playerId
					&& !NPCHandler.npcs[i].inMulti()) {
				player.npcIndex = 0;
				player.sendMessage("This monster is already in combat.");
				return;
			}
			if ((player.underAttackBy > 0 || player.underAttackBy2 > 0)
					&& player.underAttackBy2 != i && !player.inMulti()) {
				resetPlayerAttack();
				player.sendMessage("I am already under attack.");
				return;
			}

			if (player.usingMagic
					&& player.MAGIC_SPELLS[player.spellId][0] == 1171) {
				if (!World.getNpcHandler().isUndead(i)) {
					player.sendMessage("This spell only affects skeletons, zombies, ghosts and shades.");
					player.getCombat().resetPlayerAttack();
					player.stopMovement();
					player.npcIndex = 0;
					return;
				}
			}
			if (!player.getSlayer().canAttackSlayerNpc(
					NPCHandler.npcs[i].npcType)) {
				resetPlayerAttack();
				return;
			}
			if (NPCHandler.npcs[i].spawnedBy != player.playerId
					&& NPCHandler.npcs[i].spawnedBy > 0) {
				resetPlayerAttack();
				player.sendMessage("This monster was not spawned for you.");
				return;
			}
			player.followId2 = i;
			player.followId = 0;
			player.followId3 = 0;
			if (player.attackTimer <= 0) {
				player.bonusAttack = 0;
				player.rangeItemUsed = 0;
				player.projectileStage = 0;
				PlayerSave.saveGame(player);
				if (player.autocasting) {
					player.spellId = player.autocastId;
					player.usingMagic = true;
				}
				if (player.spellId > 0) {
					player.usingMagic = true;
				}
				player.attackTimer = getAttackDelay(ItemAssistant.getItemName(
						player.playerEquipment[player.playerWeapon])
						.toLowerCase());
				player.specAccuracy = 1.0;
				player.specDamage = 1.0;

				if (armaNpc(i) && !RangeConfiguration.usingCrossbow(player)
						&& !player.usingBow && !player.usingMagic
						&& !RangeConfiguration.usingCrystalBow(player)
						&& !player.usingOtherRangeWeapons) {
					resetPlayerAttack();
					return;
				}
				if (!player
						.goodDistance(player.getX(), player.getY(),
								NPCHandler.npcs[i].getX(),
								NPCHandler.npcs[i].getY(), 2)
						&& usingHally()
						&& !player.usingOtherRangeWeapons
						&& !player.usingBow
						&& !player.usingMagic
						|| !player.goodDistance(player.getX(), player.getY(),
								NPCHandler.npcs[i].getX(),
								NPCHandler.npcs[i].getY(), 4)
						&& player.usingOtherRangeWeapons
						&& !player.usingBow
						&& !player.usingMagic
						|| !player.goodDistance(player.getX(), player.getY(),
								NPCHandler.npcs[i].getX(),
								NPCHandler.npcs[i].getY(), 1)
						&& !player.usingOtherRangeWeapons
						&& !usingHally()
						&& !player.usingBow
						&& !player.usingMagic
						|| !player.goodDistance(player.getX(), player.getY(),
								NPCHandler.npcs[i].getX(),
								NPCHandler.npcs[i].getY(), 8)
						&& (player.usingBow || player.usingMagic)) {
					player.attackTimer = 2;
					return;
				}

				if (!RangeConfiguration.usingCrossbow(player)
						&& !player.usingArrows
						&& player.usingBow
						&& (player.playerEquipment[player.playerWeapon] < 4212 || player.playerEquipment[player.playerWeapon] > 4223)) {
					player.sendMessage("You have run out of arrows.");
					player.stopMovement();
					player.npcIndex = 0;
					return;
				}
				if (RangeConfiguration.correctBowAndArrows(player) < player.playerEquipment[player.playerArrows]
						&& GameConfig.CORRECT_ARROWS
						&& player.usingBow
						&& !RangeConfiguration.usingCrystalBow(player)
						&& !RangeConfiguration.usingCrossbow(player)) {
					player.sendMessage("You can't use "
							+ ItemAssistant
									.getItemName(
											player.playerEquipment[player.playerArrows])
									.toLowerCase()
							+ "s with a "
							+ ItemAssistant
									.getItemName(
											player.playerEquipment[player.playerWeapon])
									.toLowerCase() + ".");
					player.stopMovement();
					player.npcIndex = 0;
					return;
				}
				if (RangeConfiguration.usingCrossbow(player) && !properBolts()) {
					player.sendMessage("You must use bolts with a crossbow.");
					player.stopMovement();
					resetPlayerAttack();
					return;
				}

				if (player.usingBow
						|| player.usingMagic
						|| player.usingOtherRangeWeapons
						|| player.goodDistance(player.getX(), player.getY(),
								NPCHandler.npcs[i].getX(),
								NPCHandler.npcs[i].getY(), 2) && usingHally()) {
					player.stopMovement();
				}

				if (!checkMagicReqs(player.spellId)) {
					player.stopMovement();
					player.npcIndex = 0;
					return;
				}

				player.faceUpdate(i);
				// player.specAccuracy = 1.0;
				// player.specDamage = 1.0;
				NPCHandler.npcs[i].underAttackBy = player.playerId;
				NPCHandler.npcs[i].lastDamageTaken = System.currentTimeMillis();
				if (player.usingSpecial && !player.usingMagic) {
					if (checkSpecAmount(player.playerEquipment[player.playerWeapon])) {
						player.lastWeaponUsed = player.playerEquipment[player.playerWeapon];
						player.lastArrowUsed = player.playerEquipment[player.playerArrows];
						SpecialAttack.activateSpecial(player, i);
						return;
					} else {
						player.sendMessage("You don't have the required special energy to use this attack.");
						player.usingSpecial = false;
						player.getItems().updateSpecialBar();
						player.npcIndex = 0;
						return;
					}
				}
				if (player.usingBow || player.usingMagic
						|| player.usingOtherRangeWeapons) {
					player.mageFollow = true;
				} else {
					player.mageFollow = false;
				}
				player.specMaxHitIncrease = 0;
				if (!player.usingMagic) {
					player.getItems();
					player.startAnimation(getWepAnim(ItemAssistant.getItemName(
							player.playerEquipment[player.playerWeapon])
							.toLowerCase()));
				} else {
					player.startAnimation(player.MAGIC_SPELLS[player.spellId][2]);
				}
				player.lastWeaponUsed = player.playerEquipment[player.playerWeapon];
				player.lastArrowUsed = player.playerEquipment[player.playerArrows];
				if (!player.usingBow && !player.usingMagic
						&& !player.usingOtherRangeWeapons) { // melee
					// hit
					// delay
					player.hitDelay = getHitDelay(ItemAssistant.getItemName(
							player.playerEquipment[player.playerWeapon])
							.toLowerCase());
					player.projectileStage = 0;
					player.oldNpcIndex = i;
				}

				if (player.usingBow && !player.usingOtherRangeWeapons
						&& !player.usingMagic
						|| RangeConfiguration.usingCrossbow(player)) { // range
																		// hit
					// delay
					if (RangeConfiguration.usingCrossbow(player)) {
						player.usingBow = true;
					}
					if (player.fightMode == 2) {
						player.attackTimer--;
					}
					player.lastArrowUsed = player.playerEquipment[player.playerArrows];
					player.lastWeaponUsed = player.playerEquipment[player.playerWeapon];
					player.gfx100(getRangeStartGFX());
					player.getItems();
					player.hitDelay = getHitDelay(ItemAssistant.getItemName(
							player.playerEquipment[player.playerWeapon])
							.toLowerCase());
					player.projectileStage = 1;
					player.oldNpcIndex = i;
					if (player.playerEquipment[player.playerWeapon] >= 4212
							&& player.playerEquipment[player.playerWeapon] <= 4223) {
						player.rangeItemUsed = player.playerEquipment[player.playerWeapon];
						player.crystalBowArrowCount++;
						player.lastArrowUsed = 0;
					} else {
						player.rangeItemUsed = player.playerEquipment[player.playerArrows];
						player.getItems().deleteArrow();
					}
					fireProjectileNpc();
				}

				if (player.usingOtherRangeWeapons && !player.usingMagic
						&& !player.usingBow) { // knives,
					// darts,
					// etc
					// hit
					// delay
					player.rangeItemUsed = player.playerEquipment[player.playerWeapon];
					player.getItems().deleteEquipment();
					player.gfx100(getRangeStartGFX());
					player.lastArrowUsed = 0;
					player.getItems();
					player.hitDelay = getHitDelay(ItemAssistant.getItemName(
							player.playerEquipment[player.playerWeapon])
							.toLowerCase());
					player.projectileStage = 1;
					player.oldNpcIndex = i;
					if (player.fightMode == 2) {
						player.attackTimer--;
					}
					fireProjectileNpc();
				}

				if (player.usingMagic) { // magic hit delay
					final int pX = player.getX();
					final int pY = player.getY();
					final int nX = NPCHandler.npcs[i].getX();
					final int nY = NPCHandler.npcs[i].getY();
					final int offX = (pY - nY) * -1;
					final int offY = (pX - nX) * -1;
					player.castingMagic = true;
					player.projectileStage = 2;
					if (player.MAGIC_SPELLS[player.spellId][3] > 0) {
						if (getStartGfxHeight() == 100) {
							player.gfx100(player.MAGIC_SPELLS[player.spellId][3]);
						} else {
							player.gfx0(player.MAGIC_SPELLS[player.spellId][3]);
						}
					}
					if (player.MAGIC_SPELLS[player.spellId][4] > 0) {
						player.getFunction().createPlayersProjectile(pX, pY,
								offX, offY, 50, 78,
								player.MAGIC_SPELLS[player.spellId][4],
								getStartHeight(), getEndHeight(), i + 1, 50);
					}
					player.hitDelay = getHitDelay(ItemAssistant.getItemName(
							player.playerEquipment[player.playerWeapon])
							.toLowerCase());
					player.oldNpcIndex = i;
					player.oldSpellId = player.spellId;
					player.spellId = 0;
					if (!player.autocasting) {
						player.npcIndex = 0;
					}
				}

				if (player.usingBow && GameConfig.CRYSTAL_BOW_DEGRADES) { // crystal
					// bow
					// degrading
					if (player.playerEquipment[player.playerWeapon] == 4212) { // new
						// crystal
						// bow
						// becomes
						// full
						// bow
						// on
						// the
						// first
						// shot
						player.getItems().wearItem(4214, 1, 3);
					}

					if (player.crystalBowArrowCount >= 250) {
						switch (player.playerEquipment[player.playerWeapon]) {

						case 4223: // 1/10 bow
							player.getItems().wearItem(-1, 1, 3);
							player.sendMessage("Your crystal bow has fully degraded.");
							if (!player.getItems().addItem(4207, 1)) {
								GroundItemHandler.createGroundItem(player,
										4207, player.getX(), player.getY(),
										player.getZ(), 1, player.getId());
							}
							player.crystalBowArrowCount = 0;
							break;

						default:
							player.getItems()
									.wearItem(
											++player.playerEquipment[player.playerWeapon],
											1, 3);
							player.sendMessage("Your crystal bow degrades.");
							player.crystalBowArrowCount = 0;
							break;

						}
					}
				}
			}
		}
	}

	public void attackPlayer(int i) {

		if (PlayerHandler.players[i] != null) {

			final boolean sameSpot = player.absX == PlayerHandler.players[i]
					.getX() && player.absY == PlayerHandler.players[i].getY();
			if (RegionManager.pathBlocked(player, PlayerHandler.players[i])
					&& !sameSpot && !player.usingBow
					&& !player.usingOtherRangeWeapons && !player.usingMagic
					&& !player.autocasting) {
				return;
			}
			if (player.isDead || PlayerHandler.players[i].isDead
					|| PlayerHandler.players[i].playerLevel[3] < 1
					|| player.playerLevel[3] < 1 || player.isTeleporting) {
				resetPlayerAttack();
				return;
			}

			if(player.playerEquipment[player.playerWeapon] == 8286){
				player.usingMagic = true;
				player.autocastId = 52;
				player.autocasting = true;
			}
			/*
			 * if (player.teleTimer > 0 ||
			 * Server.playerHandler.players[i].teleTimer > 0) {
			 * resetPlayerAttack(); return; }
			 */

			if (!player.getCombat().checkReqs()) {
				resetPlayerAttack();
				return;
			}
			if (player.playerEquipment[player.playerWeapon] == 9703) {
				player.sendMessage("You can't use a training sword in pvp combat.");
				resetPlayerAttack();
				return;
			}
			final Player other = PlayerHandler.players[i];
			if (other.inZamorakRespawn()
					&& CastleWars.getTeamNumber(player) == CastleWars.SARA
					|| other.inSaradominRespawn()
					&& CastleWars.getTeamNumber(player) == CastleWars.ZAMMY) {
				if (player.absZ == 1) {
					player.sendMessage("You cannot attack players in the oter teams respawn room.");
					resetPlayerAttack();
					return;
				}
			}
			/*
			 * if (player.connectedFrom.equalsIgnoreCase(other.connectedFrom)) {
			 * player.sendMessage(
			 * "You cannot attack players that are connected on same network as you."
			 * ); resetPlayerAttack(); return; }
			 */
			if (player.inZamorakRespawn() && player.absZ == 1
					|| player.inSaradominRespawn() && player.absZ == 1) {
				resetPlayerAttack();
				return;
			}
			if (other.isResting) {
				Resting.stopResting(other);
			}
			if (player.goodDistance(other.getAbsX(), other.getAbsY(),
					player.getX(), player.getY(), 1)) {
				if (other.getAbsX() != player.getAbsX()
						&& other.getAbsY() != player.getAbsY()
						&& !player.usingRangeWeapon && !player.usingBow
						&& !player.usingOtherRangeWeapons && !player.usingMagic) {
					player.getFunction().stopDiagonal(other.getAbsX(),
							other.getAbsY());
					return;
				}
			}
			if (!player.goodDistance(PlayerHandler.players[i].getX(),
					PlayerHandler.players[i].getY(), player.getX(),
					player.getY(), 25)
					&& !sameSpot) {
				resetPlayerAttack();
				return;
			}

			if (PlayerHandler.players[i].isDead) {
				PlayerHandler.players[i].playerIndex = 0;
				resetPlayerAttack();
				return;
			}
			if (player.getFunction().playerHasChicken()) {
				resetPlayerAttack();
				player.startAnimation(1833);
				PlayerHandler.players[i].startAnimation(404);
				return;
			}
			if (PlayerHandler.players[i].absZ != player.absZ) {
				resetPlayerAttack();
				return;
			}
			player.followId = i;
			player.followId2 = 0;
			player.followId3 = 0;
			if (player.attackTimer <= 0) {
				player.usingBow = false;
				player.specEffect = 0;
				player.usingRangeWeapon = false;
				player.rangeItemUsed = 0;
				player.projectileStage = 0;

				player.usingArrows = false;
				player.usingBow = false;
				player.usingOtherRangeWeapons = false;
				if (!player.usingMagic) {
					for (final int bowId : RangeConfiguration.BOWS) {
						if (player.playerEquipment[player.playerWeapon] == bowId) {
							player.usingBow = true;
							for (final int arrowId : RangeConfiguration.ARROWS) {
								if (player.playerEquipment[player.playerArrows] == arrowId) {
									player.usingArrows = true;
								}
							}
						}
					}

					for (final int otherRangeId : RangeConfiguration.OTHER_RANGE_WEAPONS) {
						if (player.playerEquipment[player.playerWeapon] == otherRangeId) {
							player.usingOtherRangeWeapons = true;
						}
					}
				}
				if (player.absX == PlayerHandler.players[i].absX
						&& player.absY == PlayerHandler.players[i].absY) {
					if (player.freezeTimer > 0) {
						resetPlayerAttack();
						return;
					}
					player.followId = i;
					player.attackTimer = 0;
					return;
				}

				/*
				 * if ((player.inPirateHouse() &&
				 * !Server.playerHandler.players[i].inPirateHouse()) ||
				 * (Server.playerHandler.players[i].inPirateHouse() &&
				 * !player.inPirateHouse())) { resetPlayerAttack(); return; }
				 */
				// player.sendMessage("Made it here1.");

				if (player.autocasting) {
					player.spellId = player.autocastId;
					player.usingMagic = true;
				}
				// player.sendMessage("Made it here2.");
				if (player.spellId > 0) {
					player.usingMagic = true;
				}
				player.getItems();
				player.attackTimer = getAttackDelay(ItemAssistant.getItemName(
						player.playerEquipment[player.playerWeapon])
						.toLowerCase());

				if (player.duelRule[9]) {
					boolean canUseWeapon = false;
					for (final int funWeapon : GameConfig.FUN_WEAPONS) {
						if (player.playerEquipment[player.playerWeapon] == funWeapon) {
							canUseWeapon = true;
						}
					}
					if (!canUseWeapon) {
						player.sendMessage("You can only use fun weapons in this duel!");
						resetPlayerAttack();
						return;
					}
				}
				// player.sendMessage("Made it here3.");
				if (player.duelRule[2]
						&& (player.usingBow || player.usingOtherRangeWeapons)) {
					player.sendMessage("Range has been disabled in this duel!");
					return;
				}
				if (player.duelRule[3] && !player.usingBow
						&& !player.usingOtherRangeWeapons && !player.usingMagic) {
					player.sendMessage("Melee has been disabled in this duel!");
					return;
				}

				if (player.duelRule[4] && player.usingMagic) {
					player.sendMessage("Magic has been disabled in this duel!");
					resetPlayerAttack();
					return;
				}

				if (!player.goodDistance(player.getX(), player.getY(),
						PlayerHandler.players[i].getX(),
						PlayerHandler.players[i].getY(), 4)
						&& player.usingOtherRangeWeapons
						&& !player.usingBow
						&& !player.usingMagic
						|| !player.goodDistance(player.getX(), player.getY(),
								PlayerHandler.players[i].getX(),
								PlayerHandler.players[i].getY(), 2)
						&& !player.usingOtherRangeWeapons
						&& usingHally()
						&& !player.usingBow
						&& !player.usingMagic
						|| !player.goodDistance(player.getX(), player.getY(),
								PlayerHandler.players[i].getX(),
								PlayerHandler.players[i].getY(),
								getRequiredDistance())
						&& !player.usingOtherRangeWeapons
						&& !usingHally()
						&& !player.usingBow
						&& !player.usingMagic
						|| !player.goodDistance(player.getX(), player.getY(),
								PlayerHandler.players[i].getX(),
								PlayerHandler.players[i].getY(), 10)
						&& (player.usingBow || player.usingMagic)) {
					// player.sendMessage("Setting attack timer to 1");
					player.attackTimer = 1;
					if (!player.usingBow && !player.usingMagic
							&& !player.usingOtherRangeWeapons
							&& player.freezeTimer > 0) {
						resetPlayerAttack();
					}
					return;
				}

				if (!RangeConfiguration.usingCrossbow(player)
						&& !player.usingArrows
						&& player.usingBow
						&& (player.playerEquipment[player.playerWeapon] < 4212 || player.playerEquipment[player.playerWeapon] > 4223)
						&& !player.usingMagic) {
					player.sendMessage("You have run out of arrows!");
					player.stopMovement();
					resetPlayerAttack();
					return;
				}
				if (RangeConfiguration.correctBowAndArrows(player) < player.playerEquipment[player.playerArrows]
						&& GameConfig.CORRECT_ARROWS
						&& player.usingBow
						&& !RangeConfiguration.usingCrystalBow(player)
						&& !RangeConfiguration.usingCrossbow(player)
						&& !player.usingMagic) {
					player.sendMessage("You can't use "
							+ ItemAssistant
									.getItemName(
											player.playerEquipment[player.playerArrows])
									.toLowerCase()
							+ "s with a "
							+ ItemAssistant
									.getItemName(
											player.playerEquipment[player.playerWeapon])
									.toLowerCase() + ".");
					player.stopMovement();
					resetPlayerAttack();
					return;
				}
				if (RangeConfiguration.usingCrossbow(player) && !properBolts()
						&& !player.usingMagic) {
					player.sendMessage("You must use bolts with a crossbow.");
					player.stopMovement();
					resetPlayerAttack();
					return;
				}

				if (player.usingBow || player.usingMagic
						|| player.usingOtherRangeWeapons || usingHally()) {
					player.stopMovement();
				}

				if (!checkMagicReqs(player.spellId)) {
					player.stopMovement();
					resetPlayerAttack();
					return;
				}

				player.faceUpdate(i + 32768);

				if (player.duelStatus != 5 || !player.inFightPits()
						|| !player.inCastleWars()) {
					if (!player.attackedPlayers.contains(player.playerIndex)
							&& !PlayerHandler.players[player.playerIndex].attackedPlayers
									.contains(player.playerId)) {
						player.attackedPlayers.add(player.playerIndex);
						player.isSkulled = true;
						player.skullTimer = GameConfig.SKULL_TIMER;
						player.headIconPk = 0;
						player.getFunction().requestUpdates();
					}
				}
				player.specAccuracy = 1.0;
				player.specDamage = 1.0;
				player.delayedDamage = player.delayedDamage2 = 0;
				if (!player.usingBow && !player.usingMagic
						&& !player.usingOtherRangeWeapons) { // melee
					// hit
					// delay
					player.followId = PlayerHandler.players[player.playerIndex].playerId;

					player.getFunction().combatFollowPlayer();
					player.hitDelay = getHitDelay(ItemAssistant.getItemName(
							player.playerEquipment[player.playerWeapon])
							.toLowerCase());
					player.delayedDamage = Misc.random(MeleeFormula
							.calculateMeleeMaxHit(player));

					if (player.usingSpecial) {
						player.delayedDamage2 = Misc.random(MeleeFormula
								.calculateMeleeMaxHit(player));

					}
					player.delayedDamage = updateDamage(i, 1,
							player.delayedDamage);
					player.delayedDamage2 = updateDamage(i, 2,
							player.delayedDamage2);
					player.projectileStage = 0;
					player.oldPlayerIndex = i;
				}
				if (player.usingSpecial && !player.usingMagic) {
					if (player.duelRule[10] && player.duelStatus == 5) {
						player.sendMessage("Special attacks have been disabled during this duel!");
						player.usingSpecial = false;
						player.getItems().updateSpecialBar();
						resetPlayerAttack();
						return;
					}
					if (checkSpecAmount(player.playerEquipment[player.playerWeapon])) {
						player.lastArrowUsed = player.playerEquipment[player.playerArrows];
						SpecialAttack.activateSpecial(player, i);
						player.getItems().addSpecialBar(
								player.playerEquipment[player.playerWeapon]);
						player.followId = player.playerIndex;
						return;
					} else {
						player.sendMessage("You don't have the required special energy to use this attack.");
						player.usingSpecial = false;
						player.getItems().updateSpecialBar();
						player.playerIndex = 0;
						return;
					}
				}

				if (!player.usingMagic) {
					player.startAnimation(getWepAnim(ItemAssistant.getItemName(
							player.playerEquipment[player.playerWeapon])
							.toLowerCase()));
					player.mageFollow = false;
				} else {
					player.startAnimation(player.MAGIC_SPELLS[player.spellId][2]);
					player.mageFollow = true;
					player.followId = player.playerIndex;
				}
				PlayerHandler.players[i].underAttackBy = player.playerId;
				PlayerHandler.players[i].logoutDelay = System
						.currentTimeMillis();
				PlayerHandler.players[i].singleCombatDelay = System
						.currentTimeMillis();
				PlayerHandler.players[i].killerId = player.playerId;
				player.lastArrowUsed = 0;
				player.rangeItemUsed = 0;

				if (player.usingBow && !player.usingOtherRangeWeapons
						&& !player.usingMagic
						|| RangeConfiguration.usingCrossbow(player)) { // range
																		// hit
					// delay
					if (player.playerEquipment[player.playerWeapon] >= 4212
							&& player.playerEquipment[player.playerWeapon] <= 4223) {
						player.rangeItemUsed = player.playerEquipment[player.playerWeapon];
						player.crystalBowArrowCount++;
					} else {
						player.rangeItemUsed = player.playerEquipment[player.playerArrows];
						player.getItems().deleteArrow();
					}
					if (player.fightMode == 2) {
						player.attackTimer--;
					}
					if (RangeConfiguration.usingCrossbow(player)) {
						player.usingBow = true;
					}
					player.usingBow = true;
					player.followId = PlayerHandler.players[player.playerIndex].playerId;
					player.getFunction().combatFollowPlayer();
					player.lastWeaponUsed = player.playerEquipment[player.playerWeapon];
					player.lastArrowUsed = player.playerEquipment[player.playerArrows];
					player.gfx100(getRangeStartGFX());
					player.getItems();
					player.hitDelay = getHitDelay(ItemAssistant.getItemName(
							player.playerEquipment[player.playerWeapon])
							.toLowerCase());
					player.projectileStage = 1;
					player.oldPlayerIndex = i;
					fireProjectilePlayer();
				}

				if (player.usingOtherRangeWeapons) { // knives, darts, etc hit
														// delay
					player.rangeItemUsed = player.playerEquipment[player.playerWeapon];
					player.getItems().deleteEquipment();
					player.usingRangeWeapon = true;
					player.followId = PlayerHandler.players[player.playerIndex].playerId;
					player.getFunction().combatFollowPlayer();
					player.gfx100(getRangeStartGFX());
					if (player.fightMode == 2) {
						player.attackTimer--;
					}
					player.getItems();
					player.hitDelay = getHitDelay(ItemAssistant.getItemName(
							player.playerEquipment[player.playerWeapon])
							.toLowerCase());
					player.projectileStage = 1;
					player.oldPlayerIndex = i;
					fireProjectilePlayer();
				}

				if (player.usingMagic) { // magic hit delay
					final int pX = player.getX();
					final int pY = player.getY();
					final int nX = PlayerHandler.players[i].getX();
					final int nY = PlayerHandler.players[i].getY();
					final int offX = (pY - nY) * -1;
					final int offY = (pX - nX) * -1;
					player.castingMagic = true;
					player.projectileStage = 2;
					if (player.MAGIC_SPELLS[player.spellId][3] > 0) {
						if (getStartGfxHeight() == 100) {
							player.gfx100(player.MAGIC_SPELLS[player.spellId][3]);
						} else {
							player.gfx0(player.MAGIC_SPELLS[player.spellId][3]);
						}
					}
					if (player.MAGIC_SPELLS[player.spellId][4] > 0) {
						player.getFunction().createPlayersProjectile(pX, pY,
								offX, offY, 50, 78,
								player.MAGIC_SPELLS[player.spellId][4],
								getStartHeight(), getEndHeight(), -i - 1,
								getStartDelay());
					}
					if (player.autocastId > 0) {
						player.followId = player.playerIndex;
						player.followDistance = 5;
					}
					player.hitDelay = getHitDelay(ItemAssistant.getItemName(
							player.playerEquipment[player.playerWeapon])
							.toLowerCase());
					player.oldPlayerIndex = i;
					player.oldSpellId = player.spellId;
					player.spellId = 0;
					final Client o = (Client) PlayerHandler.players[i];
					if (player.MAGIC_SPELLS[player.oldSpellId][0] == 12891
							&& o.isMoving) {
						// player.sendMessage("Barrage projectile..");
						player.getFunction().createPlayersProjectile(pX, pY,
								offX, offY, 50, 85, 368, 25, 25, -i - 1,
								getStartDelay());
					}
					if (Misc.random(o.getCombat().mageDef()) > Misc
							.random(mageAtk())) {
						player.magicFailed = true;
					} else {
						player.magicFailed = false;
					}
					final int freezeDelay = getFreezeTime();// freeze time
					if (freezeDelay > 0
							&& PlayerHandler.players[i].freezeTimer <= -3
							&& !player.magicFailed) {
						PlayerHandler.players[i].freezeTimer = freezeDelay;
						o.resetWalkingQueue();
						o.frozenBy = player.playerId;
					}
					if (!player.autocasting && player.spellId <= 0) {
						player.playerIndex = 0;
					}
				}

				if (player.usingBow && GameConfig.CRYSTAL_BOW_DEGRADES) { // crystal
					// bow
					// degrading
					if (player.playerEquipment[player.playerWeapon] == 4212) { // new
						// crystal
						// bow
						// becomes
						// full
						// bow
						// on
						// the
						// first
						// shot
						player.getItems().wearItem(4214, 1, 3);
					}

					if (player.crystalBowArrowCount >= 250) {
						switch (player.playerEquipment[player.playerWeapon]) {

						case 4223: // 1/10 bow
							player.getItems().wearItem(-1, 1, 3);
							player.sendMessage("Your crystal bow has fully degraded.");
							if (!player.getItems().addItem(4207, 1)) {
								GroundItemHandler.createGroundItem(player,
										4207, player.getX(), player.getY(),
										player.absZ, 1, player.getId());
							}
							player.crystalBowArrowCount = 0;
							break;

						default:
							player.getItems()
									.wearItem(
											++player.playerEquipment[player.playerWeapon],
											1, 3);
							player.sendMessage("Your crystal bow degrades.");
							player.crystalBowArrowCount = 0;
							break;
						}
					}
				}
			}
		}
	}

	public int bestMeleeAtk() {
		if (player.playerBonus[0] > player.playerBonus[1]
				&& player.playerBonus[0] > player.playerBonus[2]) {
			return 0;
		}
		if (player.playerBonus[1] > player.playerBonus[0]
				&& player.playerBonus[1] > player.playerBonus[2]) {
			return 1;
		}
		return player.playerBonus[2] <= player.playerBonus[1]
				|| player.playerBonus[2] <= player.playerBonus[0] ? 0 : 2;
	}

	public int bestMeleeDef() {
		if (player.playerBonus[5] > player.playerBonus[6]
				&& player.playerBonus[5] > player.playerBonus[7]) {
			return 5;
		}
		if (player.playerBonus[6] > player.playerBonus[5]
				&& player.playerBonus[6] > player.playerBonus[7]) {
			return 6;
		}
		return player.playerBonus[7] <= player.playerBonus[5]
				|| player.playerBonus[7] <= player.playerBonus[6] ? 5 : 7;
	}

	public boolean calculateBlockedHit(int defence) {
		if (defence > 450 && Misc.random(5) == 1) {
			return true;
		}
		if (defence > 400 && Misc.random(5) == 1) {
			return true;
		}
		if (defence > 350 && Misc.random(6) == 1) {
			return true;
		}
		if (defence > 300 && Misc.random(6) == 1) {
			return true;
		}
		if (Misc.random(6) == 1 && defence > 150) {
			return true;
		}
		if (defence > 10 && Misc.random(7) == 1) {
			return true;
		}
		return false;
	}

	public int calculateDefenceDamageReduction(int i, int damage) {
		final Client o = (Client) PlayerHandler.players[i];
		final int defence = o.getCombat().calculateMeleeDefence();
		if (calculateBlockedHit(defence)) {
			return 0;
		}
		if (defence > 450) {
			return damage *= .635;
		}
		if (defence > 400) {
			return damage *= .655;
		}
		if (defence > 350) {
			return damage *= .715;
		}
		if (defence > 300) {
			return damage *= .775;
		}
		if (defence > 250) {
			return damage *= .835;
		}
		if (defence > 200) {
			return damage *= .85;
		}
		if (defence > 150) {
			return damage *= .9125;
		}
		if (defence > 100) {
			return damage *= .975;
		}
		if (defence > 10) {
			return damage *= .99;
		}
		return damage;
	}

	/**
	 * Melee
	 **/

	public int calculateMeleeAttack() {
		int attackLevel = player.playerLevel[0];
		// 2, 5, 11, 18, 19
		if (player.prayerActive[2]) {
			attackLevel += player
					.getLevelForXP(player.playerXP[player.playerAttack]) * 0.05;
		} else if (player.prayerActive[7]) {
			attackLevel += player
					.getLevelForXP(player.playerXP[player.playerAttack]) * 0.1;
		} else if (player.prayerActive[15]) {
			attackLevel += player
					.getLevelForXP(player.playerXP[player.playerAttack]) * 0.14;
		} else if (player.prayerActive[24]) {
			attackLevel += player
					.getLevelForXP(player.playerXP[player.playerAttack]) * 0.16;
		} else if (player.prayerActive[25]) {
			attackLevel += player
					.getLevelForXP(player.playerXP[player.playerAttack]) * 0.18;
		} else if (player.prayerActive[28]) {
			attackLevel += player
					.getLevelForXP(player.playerXP[player.playerAttack]) * 0.20;
		}
		if (player.fullVoidMelee()) {
			attackLevel += player
					.getLevelForXP(player.playerXP[player.playerAttack]) * 0.1;
		}
		attackLevel *= player.specAccuracy;
		// player.sendMessage("Attack: " + (attackLevel +
		// (player.playerBonus[bestMeleeAtk()] * 2)));
		int i = player.playerBonus[bestMeleeAtk()];
		i += player.bonusAttack;
		if (player.playerEquipment[player.playerAmulet] == 11128
				&& player.playerEquipment[player.playerWeapon] == 6528) {
			i *= 1.10;
		}
		return (int) (attackLevel + attackLevel * 0.15 + i + i * 0.05);
	}

	public int calculateMeleeDefence() {
		int defenceLevel = player.playerLevel[1];
		final int i = player.playerBonus[bestMeleeDef()];
		if (player.prayerActive[0]) {
			defenceLevel += player
					.getLevelForXP(player.playerXP[player.playerDefence]) * 0.05;
		} else if (player.prayerActive[5]) {
			defenceLevel += player
					.getLevelForXP(player.playerXP[player.playerDefence]) * 0.1;
		} else if (player.prayerActive[13]) {
			defenceLevel += player
					.getLevelForXP(player.playerXP[player.playerDefence]) * 0.14;
		} else if (player.prayerActive[24]) {
			defenceLevel += player
					.getLevelForXP(player.playerXP[player.playerDefence]) * 0.16;
		} else if (player.prayerActive[25]) {
			defenceLevel += player
					.getLevelForXP(player.playerXP[player.playerDefence]) * 0.18;
		}
		return (int) (defenceLevel * 0.15  + i * 0.05);
	}

	/**
	 * Range
	 **/

	public int calculateRangeAttack() {
		int attackLevel = player.playerLevel[4];
		attackLevel *= player.specAccuracy;
		if (player.fullVoidRange()) {
			attackLevel += player
					.getLevelForXP(player.playerXP[player.playerRanged]) * 0.1;
		}
		if (player.prayerActive[3]) {
			attackLevel *= 1.05;
		} else if (player.prayerActive[11]) {
			attackLevel *= 1.10;
		} else if (player.prayerActive[19]) {
			attackLevel *= 1.15;
		}
		// dbow spec
		if (player.fullVoidRange() && player.specAccuracy > 1.15) {
			attackLevel *= 1.75;
		}
		return (int) (attackLevel + player.playerBonus[4] * 1.95);
	}

	public int calculateRangeDefence() {
		int defenceLevel = player.playerLevel[1];
		if (player.prayerActive[0]) {
			defenceLevel += player
					.getLevelForXP(player.playerXP[player.playerDefence]) * 0.05;
		} else if (player.prayerActive[5]) {
			defenceLevel += player
					.getLevelForXP(player.playerXP[player.playerDefence]) * 0.1;
		} else if (player.prayerActive[13]) {
			defenceLevel += player
					.getLevelForXP(player.playerXP[player.playerDefence]) * 0.15;
		} else if (player.prayerActive[24]) {
			defenceLevel += player
					.getLevelForXP(player.playerXP[player.playerDefence]) * 0.18;
		} else if (player.prayerActive[25]) {
			defenceLevel += player
					.getLevelForXP(player.playerXP[player.playerDefence]) * 0.25;
		}
		return (int) (defenceLevel + player.playerBonus[9] + player.playerBonus[9] / 2);
	}

	public boolean checkMagicReqs(int spell) {
		if (player.usingMagic && GameConfig.RUNES_REQUIRED) { // check for
																// runes
			if (!player.getItems().playerHasItem(player.MAGIC_SPELLS[spell][8],
					player.MAGIC_SPELLS[spell][9])
					&& !wearingStaff(player.MAGIC_SPELLS[spell][8])
					|| !player.getItems().playerHasItem(
							player.MAGIC_SPELLS[spell][10],
							player.MAGIC_SPELLS[spell][11])
					&& !wearingStaff(player.MAGIC_SPELLS[spell][10])
					|| !player.getItems().playerHasItem(
							player.MAGIC_SPELLS[spell][12],
							player.MAGIC_SPELLS[spell][13])
					&& !wearingStaff(player.MAGIC_SPELLS[spell][12])
					|| !player.getItems().playerHasItem(
							player.MAGIC_SPELLS[spell][14],
							player.MAGIC_SPELLS[spell][15])
					&& !wearingStaff(player.MAGIC_SPELLS[spell][14])) {
				player.sendMessage("You don't have the required runes to cast this spell.");
				return false;
			}
		}

		if (player.usingMagic && player.playerIndex > 0) {
			if (PlayerHandler.players[player.playerIndex] != null) {
				for (int r = 0; r < player.REDUCE_SPELLS.length; r++) { // reducing
					// spells,
					// confuse
					// etc
					if (PlayerHandler.players[player.playerIndex].REDUCE_SPELLS[r] == player.MAGIC_SPELLS[spell][0]) {
						player.reduceSpellId = r;
						if (System.currentTimeMillis()
								- PlayerHandler.players[player.playerIndex].reduceSpellDelay[player.reduceSpellId] > PlayerHandler.players[player.playerIndex].REDUCE_SPELL_TIME[player.reduceSpellId]) {
							PlayerHandler.players[player.playerIndex].canUseReducingSpell[player.reduceSpellId] = true;
						} else {
							PlayerHandler.players[player.playerIndex].canUseReducingSpell[player.reduceSpellId] = false;
						}
						break;
					}
				}
				if (!PlayerHandler.players[player.playerIndex].canUseReducingSpell[player.reduceSpellId]) {
					player.sendMessage("That player is currently immune to this spell.");
					player.usingMagic = false;
					player.stopMovement();
					resetPlayerAttack();
					return false;
				}
			}
		}

		final int staffRequired = getStaffNeeded();
		if (player.usingMagic && staffRequired > 0 && GameConfig.RUNES_REQUIRED) { // staff
																					// required
			if (player.playerEquipment[player.playerWeapon] != staffRequired) {
				player.getItems();
				player.sendMessage("You need a "
						+ ItemAssistant.getItemName(staffRequired)
								.toLowerCase() + " to cast this spell.");
				return false;
			}
		}

		if (player.usingMagic && GameConfig.MAGIC_LEVEL_REQUIRED) { // check
			// magic
			// level
			if (player.playerLevel[6] < player.MAGIC_SPELLS[spell][1]) {
				player.sendMessage("You need to have a magic level of "
						+ player.MAGIC_SPELLS[spell][1]
						+ " to cast this spell.");
				return false;
			}
		}
		if (player.usingMagic && GameConfig.RUNES_REQUIRED) {
			if (player.MAGIC_SPELLS[spell][8] > 0) { // deleting runes
				if (!wearingStaff(player.MAGIC_SPELLS[spell][8])) {
					player.getItems().deleteItem(
							player.MAGIC_SPELLS[spell][8],
							player.getItems().getItemSlot(
									player.MAGIC_SPELLS[spell][8]),
							player.MAGIC_SPELLS[spell][9]);
				}
			}
			if (player.MAGIC_SPELLS[spell][10] > 0) {
				if (!wearingStaff(player.MAGIC_SPELLS[spell][10])) {
					player.getItems().deleteItem(
							player.MAGIC_SPELLS[spell][10],
							player.getItems().getItemSlot(
									player.MAGIC_SPELLS[spell][10]),
							player.MAGIC_SPELLS[spell][11]);
				}
			}
			if (player.MAGIC_SPELLS[spell][12] > 0) {
				if (!wearingStaff(player.MAGIC_SPELLS[spell][12])) {
					player.getItems().deleteItem(
							player.MAGIC_SPELLS[spell][12],
							player.getItems().getItemSlot(
									player.MAGIC_SPELLS[spell][12]),
							player.MAGIC_SPELLS[spell][13]);
				}
			}
			if (player.MAGIC_SPELLS[spell][14] > 0) {
				if (!wearingStaff(player.MAGIC_SPELLS[spell][14])) {
					player.getItems().deleteItem(
							player.MAGIC_SPELLS[spell][14],
							player.getItems().getItemSlot(
									player.MAGIC_SPELLS[spell][14]),
							player.MAGIC_SPELLS[spell][15]);
				}
			}
		}
		return true;
	}

	public boolean checkMultiBarrageReqs(int i) {
		if (PlayerHandler.players[i] == null) {
			return false;
		}
		if (i == player.playerId) {
			return false;
		}
		if (!PlayerHandler.players[i].inWild()) {
			return false;
		}
		if (GameConfig.COMBAT_LEVEL_DIFFERENCE) {
			final int combatDif1 = player.getCombat().getCombatDifference(
					player.combatLevel, PlayerHandler.players[i].combatLevel);
			if (combatDif1 > player.wildLevel
					|| combatDif1 > PlayerHandler.players[i].wildLevel) {
				player.sendMessage("Your combat level difference is too great to attack that player here.");
				return false;
			}
		}

		if (GameConfig.SINGLE_AND_MULTI_ZONES) {
			if (!PlayerHandler.players[i].inMulti()) { // single combat zones
				if (PlayerHandler.players[i].underAttackBy != player.playerId
						&& PlayerHandler.players[i].underAttackBy != 0) {
					return false;
				}
				if (PlayerHandler.players[i].playerId != player.underAttackBy
						&& player.underAttackBy != 0) {
					player.sendMessage("You are already in combat.");
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Wildy and duel info
	 **/

	public boolean checkReqs() {
		if (player.playerRights == 5)
			return true;
		if (PlayerHandler.players[player.playerIndex] == null) {
			return false;
		}
		if (PlayerHandler.players[player.playerIndex].playerLevel[player.playerHitpoints] < 1) {
			return false;
		}
		if (player.playerIndex == player.playerId) {
			return false;
		}
		if (player.duelStatus == 5
				&& PlayerHandler.players[player.playerIndex].duelStatus == 5) {
			if (PlayerHandler.players[player.playerIndex].duelingWith == player.getId()) {
				return true;
			} else {
				player.sendMessage("This isn't your opponent!");
				return false;
			}
		}
		if (player.CWteam() == 1
				&& ((Client) PlayerHandler.players[player.playerIndex])
						.CWteam() == 1) {
			return false;
		}
		if (player.CWteam() == 2
				&& ((Client) PlayerHandler.players[player.playerIndex])
						.CWteam() == 2) {
			return false;
		}
		if (player.orb || PlayerHandler.players[player.playerIndex].orb) {
			return false;
		}
		if (player.inFightPits()
				&& PlayerHandler.players[player.playerIndex].inFightPits()) {
			return true;
		}
		if (player.inCastleWars()
				&& PlayerHandler.players[player.playerIndex].inCastleWars()) {
			return true;
		}
		if (player.getFunction().playerHasChicken()) {
			return true;
		}
		if (PlayerHandler.players[player.playerIndex].inDuelArena()
				&& player.duelStatus != 5 && !player.usingMagic) {
			if (player.arenas() || player.duelStatus == 5) {
				player.sendMessage("You can't challenge inside the arena!");
				return false;
			}
			player.getDuel().requestDuel(player.playerIndex);
			return false;
		}
		if (!PlayerHandler.players[player.playerIndex].inWild()) {
			player.sendMessage("You cannot attack players outside of the wilderness.");
			player.stopMovement();
			player.getCombat().resetPlayerAttack();
			return false;
		}
		if (!player.inWild()) {
			player.sendMessage("You are not in the wilderness.");
			player.stopMovement();
			player.getCombat().resetPlayerAttack();
			return false;
		}
		if (GameConfig.COMBAT_LEVEL_DIFFERENCE) {
			final int combatDif1 = player.getCombat().getCombatDifference(
					player.combatLevel,
					PlayerHandler.players[player.playerIndex].combatLevel);
			if (combatDif1 > player.wildLevel
					|| combatDif1 > PlayerHandler.players[player.playerIndex].wildLevel) {
				player.sendMessage("Your combat level difference is too great to attack that player here.");
				player.stopMovement();
				player.getCombat().resetPlayerAttack();
				return false;
			}
		}

		if (GameConfig.SINGLE_AND_MULTI_ZONES) {
			if (!PlayerHandler.players[player.playerIndex].inMulti()) {
				if (PlayerHandler.players[player.playerIndex].underAttackBy != player.playerId
						&& PlayerHandler.players[player.playerIndex].underAttackBy != 0) {
					player.sendMessage("Someone else is already fighting your opponent.");
					player.stopMovement();
					player.getCombat().resetPlayerAttack();
					return false;
				}
				if (PlayerHandler.players[player.playerIndex].playerId != player.underAttackBy
						&& player.underAttackBy != 0
						|| player.underAttackBy2 > 0) {
					player.sendMessage("You are already in combat.");
					player.stopMovement();
					player.getCombat().resetPlayerAttack();
					return false;
				}
			}
		}
		return true;
	}

	public boolean checkSpecAmount(int weapon) {
		switch (weapon) {
		case 1249:
		case 1215:
		case 1231:
		case 5680:
		case 5698:
		case 1305:
		case 1434:
		case 13899:
		case 13883:
			if (player.specAmount >= 2.5) {
				player.specAmount -= 2.5;
				player.getItems().addSpecialBar(weapon);
				return true;
			}
			return false;

		case 868:
		case 13879:
		case 11694:
		case 11777:
		case 11698:
		case 4153:
		case 11863:
		case 10887:
		case 13905:
		case 13902:
		case 8285:
			if (player.specAmount >= 5) {
				player.specAmount -= 5;
				player.getItems().addSpecialBar(weapon);
				return true;
			}
			return false;

		case 4151:
		case 12006:
			if (player.specAmount >= 3) {
				player.specAmount -= 3;
				player.getItems().addSpecialBar(weapon);
				return true;
			}
			return false;

		case 1377:
		case 11696:
		case 11730:
		case 8286:
		case 7806:
			if (player.specAmount >= 10) {
				player.specAmount -= 10;
				player.getItems().addSpecialBar(weapon);
				return true;
			}
			return false;

		case 4587:
		case 859:
		case 861:
		case 11235:
		case 11700:
		case 3204:
			if (player.specAmount >= 5.5) {
				player.specAmount -= 5.5;
				player.getItems().addSpecialBar(weapon);
				return true;
			}
			return false;

		default:
			return true; // incase u want to test a weapon
		}
	}

	public void delayedHit(int i) { // npc hit delay
		if (NPCHandler.npcs[i] != null) {
			if (NPCHandler.npcs[i].isDead) {
				player.npcIndex = 0;
				return;
			}
			NPCHandler.npcs[i].facePlayer(player.playerId);

			if (NPCHandler.npcs[i].underAttackBy > 0
					&& World.getNpcHandler().getsPulled(i)) {
				NPCHandler.npcs[i].killerId = player.playerId;
			} else if (NPCHandler.npcs[i].underAttackBy < 0
					&& !World.getNpcHandler().getsPulled(i)) {
				NPCHandler.npcs[i].killerId = player.playerId;
			}
			player.lastNpcAttacked = i;
			if (player.projectileStage == 0) { // melee hit damage
				applyNpcMeleeDamage(i, 1);

				if (player.doubleHit) {
					applyNpcMeleeDamage(i, 2);
				}

			}

			if (!player.castingMagic && player.projectileStage > 0) { // range
																		// hit
																		// damage
				int damage = Misc.random(rangeMaxHit());
				int damage2 = -1;
				if (player.lastWeaponUsed == 11235 || player.bowSpecShot == 1) {
					damage2 = Misc.random(rangeMaxHit());
				}
				boolean ignoreDef = false;
				if (Misc.random(5) == 1 && player.lastArrowUsed == 9243) {
					ignoreDef = true;
					NPCHandler.npcs[i].gfx0(758);
				}

				if (Misc.random(NPCHandler.npcs[i].defence) > Misc
						.random(10 + calculateRangeAttack()) && !ignoreDef) {
					damage = 0;
				} else if (NPCHandler.npcs[i].npcType == 2881
						|| NPCHandler.npcs[i].npcType == 2883 && !ignoreDef) {
					damage = 0;
				}

				if (Misc.random(4) == 1 && player.lastArrowUsed == 9242
						&& damage > 0) {
					NPCHandler.npcs[i].gfx0(754);
					damage = NPCHandler.npcs[i].HP / 5;
					player.handleHitMask(player.playerLevel[3] / 10);
					player.dealDamage(player.playerLevel[3] / 10);
					player.gfx0(754);
				}

				if (player.lastWeaponUsed == 11235 || player.bowSpecShot == 1) {
					if (Misc.random(NPCHandler.npcs[i].defence) > Misc
							.random(10 + calculateRangeAttack())) {
						damage2 = 0;
					}
				}
				if (player.dbowSpec) {
					NPCHandler.npcs[i].gfx100(1100);
					if (damage < 8) {
						damage = 8;
					}
					if (damage2 < 8) {
						damage2 = 8;
					}
					player.dbowSpec = false;
				}
				if (damage > 20 && Misc.random(4) == 1
						&& player.lastArrowUsed == 9244) {
					damage *= 1.50;
					NPCHandler.npcs[i].gfx0(756);
				}
				if (damage > 25 && Misc.random(3) == 1
						&& player.lastArrowUsed == 9245) {
					damage *= 1.60;
					NPCHandler.npcs[i].gfx0(753);
				}

				if (NPCHandler.npcs[i].HP - damage < 0) {
					damage = NPCHandler.npcs[i].HP;
				}
				if (NPCHandler.npcs[i].HP - damage <= 0 && damage2 > 0) {
					damage2 = 0;
				}
				if (damage > 0) {
					if (player.inPestControl()) {
						player.pcDamage += damage;
						player.getFunction().sendFrame126(
								"" + player.pcDamage + "", 25116);
					}
				}
				if (player.fightMode == 3) {
					player.getFunction()
							.addSkillXP(
									(damage
											/ 2
											* CombatConfig
													.getCombatXP(player.playerTitle) / 3),
									4);
					player.getFunction()
							.addSkillXP(
									(damage
											/ 2
											* CombatConfig
													.getCombatXP(player.playerTitle) / 3),
									1);
					player.getFunction()
							.addSkillXP(
									(damage
											/ 4
											* CombatConfig
													.getCombatXP(player.playerTitle) / 3),
									3);
					player.getFunction().refreshSkill(1);
					player.getFunction().refreshSkill(3);
					player.getFunction().refreshSkill(4);
				} else {
					player.getFunction()
							.addSkillXP(
									(damage * CombatConfig
											.getCombatXP(player.playerTitle)),
									4);
					player.getFunction()
							.addSkillXP(
									(damage
											/ 4
											* CombatConfig
													.getCombatXP(player.playerTitle) / 3),
									3);
					player.getFunction().refreshSkill(3);
					player.getFunction().refreshSkill(4);
				}
				boolean dropArrows = true;

				for (final int noArrowId : player.NO_ARROW_DROP) {
					if (player.lastWeaponUsed == noArrowId) {
						dropArrows = false;
						break;
					}
				}
				if (dropArrows) {
					player.getItems().dropArrowNpc();
				}
				NPCHandler.npcs[i].underAttack = true;
				NPCHandler.npcs[i].hitDiff = damage;
				NPCHandler.npcs[i].HP -= damage;
				if (damage2 > -1) {
					NPCHandler.npcs[i].hitDiff2 = damage2;
					NPCHandler.npcs[i].HP -= damage2;
					player.totalDamageDealt += damage2;
				}
				if (player.killingNpcIndex != player.oldNpcIndex) {
					player.totalDamageDealt = 0;
				}
				player.killingNpcIndex = player.oldNpcIndex;
				player.totalDamageDealt += damage;
				NPCHandler.npcs[i].getUpdateFlags().hitUpdateRequired = true;
				if (damage2 > -1) {
					NPCHandler.npcs[i].getUpdateFlags().hitUpdateRequired2 = true;
				}
				NPCHandler.npcs[i].getUpdateFlags().updateRequired = true;

			} else if (player.projectileStage > 0) { // magic hit damage
				int damage = Misc
						.random(player.MAGIC_SPELLS[player.oldSpellId][6]);
				if (godSpells()) {
					if (System.currentTimeMillis() - player.godSpellDelay < GameConfig.GOD_SPELL_CHARGE) {
						damage += Misc.random(10);
					}
				}
				boolean magicFailed = false;
				// player.npcIndex = 0;
				final int bonusAttack = getBonusAttack(i);
				if (Misc.random(NPCHandler.npcs[i].defence) > 10
						+ Misc.random(mageAtk()) + bonusAttack) {
					damage = 0;
					magicFailed = true;
				} else if (NPCHandler.npcs[i].npcType == 2881
						|| NPCHandler.npcs[i].npcType == 2882) {
					damage = 0;
					magicFailed = true;
				}

				if (NPCHandler.npcs[i].HP - damage < 0) {
					damage = NPCHandler.npcs[i].HP;
				}

				player.getFunction()
						.addSkillXP(
								(player.MAGIC_SPELLS[player.oldSpellId][7] + damage
										* CombatConfig
												.getCombatXP(player.playerTitle)),
								6);
				player.getFunction().addSkillXP(
						(player.MAGIC_SPELLS[player.oldSpellId][7] + damage
								* CombatConfig.getCombatXP(player.playerTitle)
								/ 3), 3);
				player.getFunction().refreshSkill(3);
				player.getFunction().refreshSkill(6);
				if (damage > 0) {
					if (player.inPestControl()) {
						player.pcDamage += damage;
						player.getFunction().sendFrame126(
								"" + player.pcDamage + "", 22116);
					}
				}
				if (getEndGfxHeight() == 100 && !magicFailed) { // end GFX
					NPCHandler.npcs[i]
							.gfx100(player.MAGIC_SPELLS[player.oldSpellId][5]);
				} else if (!magicFailed) {
					NPCHandler.npcs[i]
							.gfx0(player.MAGIC_SPELLS[player.oldSpellId][5]);
				}

				if (magicFailed) {
					NPCHandler.npcs[i].gfx100(85);
				}
				if (!magicFailed) {
					final int freezeDelay = getFreezeTime();// freeze
					if (freezeDelay > 0 && NPCHandler.npcs[i].freezeTimer == 0) {
						NPCHandler.npcs[i].freezeTimer = freezeDelay;
					}
					switch (player.MAGIC_SPELLS[player.oldSpellId][0]) {
					case 12901:
					case 12919: // blood spells
					case 12911:
					case 12929:
						final int heal = Misc.random(damage / 2);
						if (player.playerLevel[3] + heal >= player
								.getFunction()
								.getLevelForXP(player.playerXP[3])) {
							player.playerLevel[3] = player.getFunction()
									.getLevelForXP(player.playerXP[3]);
						} else {
							player.playerLevel[3] += heal;
						}
						player.getFunction().refreshSkill(3);
						break;
					}

				}
				NPCHandler.npcs[i].underAttack = true;
				if (player.MAGIC_SPELLS[player.oldSpellId][6] != 0) {
					player.getFunction().hideMinimapFlag();
					NPCHandler.npcs[i].hitDiff = damage;
					NPCHandler.npcs[i].HP -= damage;
					NPCHandler.npcs[i].getUpdateFlags().hitUpdateRequired = true;
					player.totalDamageDealt += damage;
				}
				player.killingNpcIndex = player.oldNpcIndex;
				NPCHandler.npcs[i].getUpdateFlags().updateRequired = true;
				player.usingMagic = false;
				player.castingMagic = false;
				player.oldSpellId = 0;
			}
		}

		if (player.bowSpecShot <= 0) {
			player.oldNpcIndex = 0;
			player.projectileStage = 0;
			player.doubleHit = false;
			player.lastWeaponUsed = 0;
			player.bowSpecShot = 0;
		}
		if (player.bowSpecShot >= 2) {
			player.bowSpecShot = 0;
			// player.attackTimer =
			// getAttackDelay(player.getItems().getItemName(player.playerEquipment[player.playerWeapon]).toLowerCase());
		}
		if (player.bowSpecShot == 1) {
			fireProjectileNpc();
			player.hitDelay = 2;
			player.bowSpecShot = 0;
		}
	}

	public void fireProjectileNpc() {
		if (player.oldNpcIndex > 0) {
			if (NPCHandler.npcs[player.oldNpcIndex] != null) {
				player.projectileStage = 2;
				final int pX = player.getX();
				final int pY = player.getY();
				final int nX = NPCHandler.npcs[player.oldNpcIndex].getX();
				final int nY = NPCHandler.npcs[player.oldNpcIndex].getY();
				final int offX = (pY - nY) * -1;
				final int offY = (pX - nX) * -1;
				player.getFunction().createPlayersProjectile(pX, pY, offX,
						offY, 50, getProjectileSpeed(),
						getRangeProjectileGFX(), 43, 31,
						player.oldNpcIndex + 1, getStartDelay());
				if (RangeConfiguration.usingDarkBow(player)) {
					player.getFunction().createPlayersProjectile2(pX, pY, offX,
							offY, 50, getProjectileSpeed(),
							getRangeProjectileGFX(), 60, 31,
							player.oldNpcIndex + 1, getStartDelay(), 35);
				}
			}
		}
	}

	public void fireProjectilePlayer() {
		if (player.oldPlayerIndex > 0) {
			if (PlayerHandler.players[player.oldPlayerIndex] != null) {
				player.projectileStage = 2;
				final int pX = player.getX();
				final int pY = player.getY();
				final int oX = PlayerHandler.players[player.oldPlayerIndex]
						.getX();
				final int oY = PlayerHandler.players[player.oldPlayerIndex]
						.getY();
				final int offX = (pY - oY) * -1;
				final int offY = (pX - oX) * -1;
				if (!player.msbSpec) {
					player.getFunction().createPlayersProjectile(pX, pY, offX,
							offY, 50, getProjectileSpeed(),
							getRangeProjectileGFX(), 43, 31,
							-player.oldPlayerIndex - 1, getStartDelay());
				} else if (player.msbSpec) {
					player.getFunction().createPlayersProjectile2(pX, pY, offX,
							offY, 50, getProjectileSpeed(),
							getRangeProjectileGFX(), 43, 31,
							-player.oldPlayerIndex - 1, getStartDelay(), 10);
					player.msbSpec = false;
				}
				if (RangeConfiguration.usingDarkBow(player)) {
					player.getFunction().createPlayersProjectile2(pX, pY, offX,
							offY, 50, getProjectileSpeed(),
							getRangeProjectileGFX(), 60, 31,
							-player.oldPlayerIndex - 1, getStartDelay(), 35);
				}
			}
		}
	}

	public void freezePlayer(int i) {

	}

	/**
	 * Weapon and magic attack speed!
	 **/

	public int getAttackDelay(String s) {
		if (player.usingMagic) {
			return 5;
		}
		if (player.playerEquipment[player.playerWeapon] == -1) {
			return 4;// unarmed
		}

		switch (player.playerEquipment[player.playerWeapon]) {
		case 11235:
			return 9;
		case 11730:
		case 9703:
			return 4;
		case 6528:
			return 7;
		case 10033:
		case 10034:
		case 4029:
		case 4024:
		case 4026:
		case 4027:
		case 9705:
		case 13883:
			return 4;
		}

		if (s.endsWith("greataxe")) {
			return 7;
		} else if (s.equals("ahrims staff")) {
			return 6;
		} else if (s.contains("staff")) {
			if (s.contains("zamarok") || s.contains("guthix")
					|| s.contains("saradomian") || s.contains("slayer")
					|| s.contains("ancient")) {
				return 4;
			} else {
				return 5;
			}
		} else if (s.contains("bow")) {
			if (s.contains("composite") || s.equals("seercull")) {
				return 5;
			} else if (s.contains("aril")) {
				return 3;
			} else if (s.contains("short") || s.contains("hunt")
					|| s.contains("sword")) {
				return 4;
			} else if (s.contains("long") || s.contains("crystal")) {
				return 6;
			} else if (s.contains("'bow")) {
				return 7;
			}

			return 5;

		} else if (s.contains("dagger")) {
			return 4;
		} else if (s.contains("godsword") || s.contains("2h")) {
			return 6;
		} else if (s.contains("sword")) {
			return 4;
		} else if (s.contains("scimitar")) {
			return 4;
		} else if (s.contains("battleaxe")) {
			return 6;
		} else if (s.contains("warhammer")) {
			return 6;
		} else if (s.contains("2h")) {
			return 7;
		} else if (s.contains("claw")) {
			return 4;
		} else if (s.contains("halberd")) {
			return 7;
		} else if (s.equals("granite maul")) {
			return 7;
		} else if (s.equals("toktz-xil-ak")) {
			return 4;
		} else if (s.equals("tzhaar-ket-om")) {
			return 7;
		} else if (s.equals("toktz-xil-ek")) {
			return 4;
		} else if (s.equals("toktz-xil-ul")) {
			return 4;
		} else if (s.equals("toktz-mej-tal")) {
			return 6;
		} else if (s.contains("abyssa")) {
			return 4;
		} else if (s.contains("dart")) {
			return 3;
		} else if (s.contains("knife")) {
			return 3;
		} else if (s.contains("javelin")) {
			return 6;
		}
		return 5;
	}

	public int getAttackSound() {
		final WeaponSounds wep = WeaponSounds
				.forId(player.playerEquipment[player.playerWeapon]);
		if (wep != null) {
			return wep.getId();
		}
		return -1;
	}

	/**
	 * Block emotes
	 */
	public int getBlockEmote() {
		if (player.playerEquipment[player.playerShield] >= 8844
				&& player.playerEquipment[player.playerShield] <= 8850) {
			return 4177;
		}
		switch (player.playerEquipment[player.playerWeapon]) {
		case 4755:
			return 2063;

		case 4153:
		case 11863:
			return 1666;

		case 4151:
		case 12006:
			return 1659;
		case 4029:
		case 4024:
		case 4025:
			return 1393;
		case 4026:
		case 4027:
			return 1403;
		case 10033:
		case 10034:
			return 3176;
		case 11698:
		case 11700:
		case 11696:
		case 11694:
		case 11730:
			return 7050;
		default:
			return 424;
		}
	}

	public int getBonusAttack(int i) {
		switch (NPCHandler.npcs[i].npcType) {
		case 2883:
			return Misc.random(50) + 30;
		case 2026:
		case 2027:
		case 2029:
		case 2030:
			return Misc.random(50) + 30;
		}
		return 0;
	}

	public int getCombatDifference(int combat1, int combat2) {
		if (combat1 > combat2) {
			return combat1 - combat2;
		}
		if (combat2 > combat1) {
			return combat2 - combat1;
		}
		return 0;
	}

	public int getEndGfxHeight() {
		switch (player.MAGIC_SPELLS[player.oldSpellId][0]) {
		case 12987:
		case 12901:
		case 12861:
		case 12445:
		case 1192:
		case 13011:
		case 12919:
		case 12881:
		case 12999:
		case 12911:
		case 12871:
		case 13023:
		case 12929:
		case 12891:
			return 0;

		default:
			return 100;
		}
	}

	public int getEndHeight() {
		switch (player.MAGIC_SPELLS[player.spellId][0]) {
		case 1562: // stun
			return 10;

		case 12939: // smoke rush
			return 20;

		case 12987: // shadow rush
			return 28;

		case 12861: // ice rush
			return 10;

		case 12951: // smoke blitz
			return 28;

		case 12999: // shadow blitz
			return 15;

		case 12911: // blood blitz
			return 10;

		default:
			return 31;
		}
	}

	public int getFreezeTime() {
		switch (player.MAGIC_SPELLS[player.oldSpellId][0]) {
		case 1572:
		case 12861: // ice rush
			return 10;

		case 1582:
		case 12881: // ice burst
			return 17;

		case 1592:
		case 12871: // ice blitz
			return 25;

		case 12891: // ice barrage
			return 33;

		default:
			return 0;
		}
	}

	/**
	 * How long it takes to hit your enemy
	 **/
	public int getHitDelay(String weaponName) {
		if (player.usingMagic) {
			switch (player.MAGIC_SPELLS[player.spellId][0]) {
			case 12891:
				return 4;
			case 12871:
				return 6;
			default:
				return 4;
			}
		} else {
			if (weaponName.contains("knife") || weaponName.contains("dart")
					|| weaponName.contains("javelin")
					|| weaponName.contains("thrownaxe")) {
				return 3;
			}
			if (weaponName.contains("cross") || weaponName.contains("c'bow")) {
				return 4;
			}
			if (weaponName.contains("bow") && !player.dbowSpec
					&& !player.msbSpec) {
				return 4;
			} else if (player.dbowSpec) {
				return 4;
			}
			if (player.msbSpec)
				return 1;

			switch (player.playerEquipment[player.playerWeapon]) {
			case 6522: // Toktz-xil-ul
				return 3;

			default:
				return 2;
			}
		}
	}

	/**
	 * Get killer id
	 **/

	public int getKillerId(int playerId) {
		int oldDamage = 0;
		int killerId = 0;
		for (int i = 1; i < GameConfig.MAX_PLAYERS; i++) {
			if (PlayerHandler.players[i] != null) {
				if (PlayerHandler.players[i].killedBy == playerId) {
					if (PlayerHandler.players[i]
							.withinDistance(PlayerHandler.players[playerId])) {
						if (PlayerHandler.players[i].totalPlayerDamageDealt > oldDamage) {
							oldDamage = PlayerHandler.players[i].totalPlayerDamageDealt;
							killerId = i;
						}
					}
					PlayerHandler.players[i].totalPlayerDamageDealt = 0;
					PlayerHandler.players[i].killedBy = 0;
				}
			}
		}
		return killerId;
	}

	/**
	 * Weapon stand, walk, run, etc emotes
	 **/

	public void getPlayerAnimIndex(String weaponName) {
		player.playerStandIndex = 0x328;
		player.playerTurnIndex = 0x337;
		player.playerWalkIndex = 0x333;
		player.playerTurn180Index = 0x334;
		player.playerTurn90CWIndex = 0x335;
		player.playerTurn90CCWIndex = 0x336;
		player.playerRunIndex = 0x338;

		if (weaponName.contains("halberd") || weaponName.contains("spear")) {
			player.playerStandIndex = 809;
			player.playerWalkIndex = 1146;
			player.playerRunIndex = 1210;
			return;
		}
		if (weaponName.contains("dharok")) {
			player.playerStandIndex = 0x811;
			player.playerWalkIndex = 0x67F;
			player.playerRunIndex = 0x680;
			return;
		}
		if (weaponName.contains("ahrim")) {
			player.playerStandIndex = 809;
			player.playerWalkIndex = 1146;
			player.playerRunIndex = 1210;
			return;
		}
		if (weaponName.contains("verac")) {
			player.playerStandIndex = 1832;
			player.playerWalkIndex = 1830;
			player.playerRunIndex = 1831;
			return;
		}
		if (player.playerEquipment[player.playerWeapon] != 15486) {
			if (weaponName.contains("wand") || weaponName.contains("staff")
					|| weaponName.endsWith("flag")) {
				player.playerStandIndex = 809;
				player.playerRunIndex = 1210;
				player.playerWalkIndex = 1146;
				return;
			}
		}

		if (weaponName.contains("karil")) {
			player.playerStandIndex = 2074;
			player.playerWalkIndex = 2076;
			player.playerRunIndex = 2077;
			return;
		}
		if (weaponName.contains("2h sword") || weaponName.contains("godsword")
				|| weaponName.contains("saradomin sw")) {
			player.playerStandIndex = 7047;
			player.playerWalkIndex = 7046;
			player.playerRunIndex = 7039;
			player.playerTurnIndex = 7045;
			return;
		}
		if (weaponName.contains("bow")) {
			player.playerStandIndex = 808;
			player.playerWalkIndex = 819;
			player.playerRunIndex = 824;
			return;
		}

		switch (player.playerEquipment[player.playerWeapon]) {
		case 10887:
			player.playerStandIndex = 5869;
			player.playerWalkIndex = 5867;
			player.playerRunIndex = 5868;
			break;
		case 15486:
			player.playerStandIndex = 808;
			break;
		case 4151:
		case 8285:
		case 12006:
			player.playerStandIndex = 1832;
			player.playerWalkIndex = 1660;
			player.playerRunIndex = 1661;
			break;
		case 6528:
			player.playerStandIndex = 0x811;
			player.playerWalkIndex = 2064;
			player.playerRunIndex = 1664;
			break;
		case 11863:
		case 4153:
			player.playerStandIndex = 1662;
			player.playerWalkIndex = 1663;
			player.playerRunIndex = 1664;
			break;
		case 4026:
		case 4027:
			player.playerStandIndex = 1401;
			player.playerWalkIndex = 1399;
			player.playerRunIndex = 1400;
			break;
		case 4029:
			player.playerStandIndex = 1388;
			player.playerWalkIndex = 1382;
			player.playerRunIndex = 1381;
			break;
		case 4024:
		case 4025:
			player.playerStandIndex = 1388;
			player.playerWalkIndex = 1380;
			player.playerRunIndex = 1381;
			break;
		case 4565:
			player.playerStandIndex = 1836;
			player.playerWalkIndex = 1836;
			player.playerRunIndex = 1836;
			break;
		case 11694:
		case 11696:
		case 11730:
		case 11698:
		case 11700:
			player.playerStandIndex = 7047;
			player.playerWalkIndex = 7046;
			player.playerRunIndex = 7039;
			break;
		case 6082:
			player.playerStandIndex = 2316;
			player.playerWalkIndex = 2318;
			player.playerRunIndex = 2322;

			break;
		case 1305:
			player.playerStandIndex = 809;
			break;
		}
	}

	public int getProjectileShowDelay() {
		switch (player.playerEquipment[player.playerWeapon]) {
		case 863:
		case 864:
		case 865:
		case 866: // knives
		case 867:
		case 868:
		case 869:
		case 806:
		case 807:
		case 808:
		case 809: // darts
		case 810:
		case 811:
		case 11230:

		case 13879:
		case 13883:
		case 825:
		case 826:
		case 827: // javelin
		case 828:
		case 829:
		case 830:

		case 800:
		case 801:
		case 802:
		case 803: // axes
		case 804:
		case 805:

		case 4734:
		case 9185:
		case 11795:
		case 4935:
		case 4936:
		case 4937:
			return 15;

		default:
			return 5;
		}
	}

	public int getProjectileSpeed() {
		if (player.dbowSpec) {
			return 100;
		}
		if (player.playerEquipment[player.playerWeapon] == 4214)
			return 100;
		if (player.playerEquipment[player.playerWeapon] == 13879
				|| player.playerEquipment[player.playerWeapon] == 13883)
			return 80;
		switch (player.playerEquipment[player.playerWeapon]) {
		case 10033:
			return 40;
		}
		return 70;
	}

	public int getRangeProjectileGFX() {
		if (player.dbowSpec) {
			return 1099;
		}
		if (player.bowSpecShot > 0) {
			switch (player.rangeItemUsed) {
			default:
				return 249;
			}
		}
		if (player.playerEquipment[player.playerWeapon] == 9185
				|| player.playerEquipment[player.playerWeapon] == 11795) {
			return 27;
		}
		switch (player.rangeItemUsed) {
		case 10034:
			return 909;
		case 10033:
			return 908;
		case 9705:
			return 806;
		case 863:
			return 213;
		case 864:
			return 212;

		case 13879:
			return 1837;
		case 13883:
			return 1839;
		case 865:
			return 214;
		case 866: // knives
			return 216;
		case 867:
			return 217;
		case 868:
			return 218;
		case 869:
			return 215;

		case 806:
			return 226;
		case 807:
			return 227;
		case 808:
			return 228;
		case 809: // darts
			return 229;
		case 810:
			return 230;
		case 811:
			return 231;

		case 825:
			return 200;
		case 826:
			return 201;
		case 827: // javelin
			return 202;
		case 828:
			return 203;
		case 829:
			return 204;
		case 830:
			return 205;

		case 6522: // Toktz-xil-ul
			return 442;

		case 800:
			return 36;
		case 801:
			return 35;
		case 802:
			return 37; // axes
		case 803:
			return 38;
		case 804:
			return 39;
		case 805:
			return 40;

		case 882:
			return 10;

		case 884:
			return 9;

		case 886:
			return 11;

		case 888:
			return 12;

		case 890:
			return 13;

		case 892:
			return 15;

		case 11212:
			return 17;

		case 4740: // bolt rack
			return 27;

		case 4212:
		case 4214:
		case 4215:
		case 4216:
		case 4217:
		case 4218:
		case 4219:
		case 4220:
		case 4221:
		case 4222:
		case 4223:
			return 249;

		}
		return -1;
	}

	public int getRangeStartGFX() {
		switch (player.rangeItemUsed) {

		case 863:
			return 220;
		case 864:
			return 219;
		case 865:
			return 221;
		case 866: // knives
			return 223;
		case 867:
			return 224;
		case 868:
			return 225;
		case 869:
			return 222;

		case 806:
			return 232;
		case 807:
			return 233;
		case 808:
			return 234;
		case 809: // darts
			return 235;
		case 810:
			return 236;
		case 811:
			return 237;

		case 825:
			return 206;
		case 826:
			return 207;
		case 827: // javelin
			return 208;
		case 828:
			return 209;
		case 829:
			return 210;
		case 830:
			return 211;

		case 800:
			return 42;
		case 801:
			return 43;
		case 802:
			return 44; // axes
		case 803:
			return 45;
		case 804:
			return 46;
		case 805:
			return 48;

		case 882:
			return 19;

		case 884:
			return 18;

		case 886:
			return 20;

		case 888:
			return 21;

		case 890:
			return 22;

		case 892:
			return 24;

		case 11212:
			return 26;

		case 4212:
		case 4214:
		case 4215:
		case 4216:
		case 4217:
		case 4218:
		case 4219:
		case 4220:
		case 4221:
		case 4222:
		case 4223:
			return 250;

		}
		return -1;
	}

	public int getRangeStr(int i) {
		int str = 0;
		int[][] data = { { 877, 10 }, { 9140, 46 }, { 9145, 36 }, { 9141, 64 },
				{ 9142, 82 }, { 9143, 100 }, { 9144, 115 }, { 9236, 14 },
				{ 9237, 30 }, { 9238, 48 }, { 9239, 66 }, { 9240, 83 },
				{ 9241, 85 }, { 9242, 103 }, { 9243, 108 }, { 9244, 117 },
				{ 9245, 124 }, { 882, 7 }, { 884, 10 }, { 886, 16 },
				{ 888, 22 }, { 890, 31 }, { 892, 44 }, { 4740, 65 },
				{ 11212, 60 }, { 806, 1 }, { 807, 3 }, { 808, 4 }, { 809, 7 },
				{ 810, 10 }, { 811, 14 }, { 11230, 20 }, { 864, 3 },
				{ 863, 4 }, { 865, 7 }, { 866, 10 }, { 867, 14 }, { 868, 24 },
				{ 13883, 75 }, { 13879, 150 }, { 825, 6 }, { 826, 10 },
				{ 827, 12 }, { 828, 18 }, { 829, 28 }, { 830, 42 }, { 800, 5 },
				{ 801, 7 }, { 802, 11 }, { 803, 16 }, { 804, 23 }, { 805, 36 },
				{ 9976, 0 }, { 9977, 15 }, { 4212, 70 }, { 4214, 70 },
				{ 4215, 70 }, { 4216, 70 }, { 4217, 70 }, { 4218, 70 },
				{ 4219, 70 }, { 4220, 70 }, { 4221, 70 }, { 4222, 70 },
				{ 4223, 70 }, { 6522, 49 }, { 10034, 15 }, { 15243, 150 } };
		for (int l = 0; l < data.length; l++) {
			if (i == data[l][0]) {
				str = data[l][1];
			}
		}
		return str;
	}

	public int getRequiredDistance() {
		if (player.followId > 0 && player.freezeTimer <= 0 && !player.isMoving) {
			return 2;
		} else if (player.followId > 0 && player.freezeTimer <= 0
				&& player.isMoving) {
			return 3;
		} else {
			return 1;
		}
	}

	/*
	 * public int rangeMaxHit() { int rangehit = 0; rangehit +=
	 * player.playerLevel[4] / 7.5; int weapon = player.lastWeaponUsed; int
	 * Arrows = player.lastArrowUsed; if (weapon == 4223) {//Cbow 1/10 rangehit
	 * = 2; rangehit += player.playerLevel[4] / 7; } else if (weapon == 4222)
	 * {//Cbow 2/10 rangehit = 3; rangehit += player.playerLevel[4] / 7; } else
	 * if (weapon == 4221) {//Cbow 3/10 rangehit = 3; rangehit +=
	 * player.playerLevel[4] / 6.5; } else if (weapon == 4220) {//Cbow 4/10
	 * rangehit = 4; rangehit += player.playerLevel[4] / 6.5; } else if (weapon
	 * == 4219) {//Cbow 5/10 rangehit = 4; rangehit += player.playerLevel[4] /
	 * 6; } else if (weapon == 4218) {//Cbow 6/10 rangehit = 5; rangehit +=
	 * player.playerLevel[4] / 6; } else if (weapon == 4217) {//Cbow 7/10
	 * rangehit = 5; rangehit += player.playerLevel[4] / 5.5; } else if (weapon
	 * == 4216) {//Cbow 8/10 rangehit = 6; rangehit += player.playerLevel[4] /
	 * 5.5; } else if (weapon == 4215) {//Cbow 9/10 rangehit = 6; rangehit +=
	 * player.playerLevel[4] / 5; } else if (weapon == 4214) {//Cbow Full
	 * rangehit = 7; rangehit += player.playerLevel[4] / 5; } else if (weapon ==
	 * 6522) { rangehit = 5; rangehit += player.playerLevel[4] / 6; } else if
	 * (weapon == 9029) {//dragon darts rangehit = 8; rangehit +=
	 * player.playerLevel[4] / 10; } else if (weapon == 811 || weapon == 868)
	 * {//com.ownxile darts rangehit = 2; rangehit += player.playerLevel[4] /
	 * 8.5; } else if (weapon == 810 || weapon == 867) {//adamant darts rangehit
	 * = 2; rangehit += player.playerLevel[4] / 9; } else if (weapon == 809 ||
	 * weapon == 866) {//mithril darts rangehit = 2; rangehit +=
	 * player.playerLevel[4] / 9.5; } else if (weapon == 808 || weapon == 865)
	 * {//Steel darts rangehit = 2; rangehit += player.playerLevel[4] / 10; }
	 * else if (weapon == 807 || weapon == 863) {//Iron darts rangehit = 2;
	 * rangehit += player.playerLevel[4] / 10.5; } else if (weapon == 806 ||
	 * weapon == 864) {//Bronze darts rangehit = 1; rangehit +=
	 * player.playerLevel[4] / 11; } else if (Arrows == 4740 && weapon == 4734)
	 * {//BoltRacks rangehit = 3; rangehit += player.playerLevel[4] / 6; } else
	 * if (Arrows == 11212) {//dragon arrows rangehit = 4; rangehit +=
	 * player.playerLevel[4] / 5.5; } else if (Arrows == 892) {//com.ownxile
	 * arrows rangehit = 3; rangehit += player.playerLevel[4] / 6; } else if
	 * (Arrows == 890) {//adamant arrows rangehit = 2; rangehit +=
	 * player.playerLevel[4] / 7; } else if (Arrows == 888) {//mithril arrows
	 * rangehit = 2; rangehit += player.playerLevel[4] / 7.5; } else if (Arrows
	 * == 886) {//steel arrows rangehit = 2; rangehit += player.playerLevel[4] /
	 * 8; } else if (Arrows == 884) {//Iron arrows rangehit = 2; rangehit +=
	 * player.playerLevel[4] / 9; } else if (Arrows == 882) {//Bronze arrows
	 * rangehit = 1; rangehit += player.playerLevel[4] / 9.5; } else if (Arrows
	 * == 9244) { rangehit = 8; rangehit += player.playerLevel[4] / 3; } else if
	 * (Arrows == 9139) { rangehit = 12; rangehit += player.playerLevel[4] / 4;
	 * } else if (Arrows == 9140) { rangehit = 2; rangehit +=
	 * player.playerLevel[4] / 7; } else if (Arrows == 9141) { rangehit = 3;
	 * rangehit += player.playerLevel[4] / 6; } else if (Arrows == 9142) {
	 * rangehit = 4; rangehit += player.playerLevel[4] / 6; } else if (Arrows ==
	 * 9143) { rangehit = 7; rangehit += player.playerLevel[4] / 5; } else if
	 * (Arrows == 9144) { rangehit = 7; rangehit += player.playerLevel[4] / 4.5;
	 * } int bonus = 0; bonus -= rangehit / 10; rangehit += bonus; if
	 * (player.specDamage != 1) rangehit *= player.specDamage; if (rangehit ==
	 * 0) rangehit++; if (player.fullVoidRange()) { rangehit *= 1.10; } if
	 * (player.prayerActive[3]) rangehit *= 1.05; else if
	 * (player.prayerActive[11]) rangehit *= 1.10; else if
	 * (player.prayerActive[19]) rangehit *= 1.15; return rangehit; }
	 */

	public int getStaffNeeded() {
		switch (player.MAGIC_SPELLS[player.spellId][0]) {
		case 1539:
			return 1409;

		case 12037:
			return 4170;

		case 1190:
			return 2415;

		case 1191:
			return 2416;

		case 1192:
			return 2417;

		default:
			return 0;
		}
	}

	public int getStartDelay() {
		switch (player.MAGIC_SPELLS[player.spellId][0]) {
		case 1539:
			return 60;

		default:
			return 53;
		}
	}

	public int getStartGfxHeight() {
		switch (player.MAGIC_SPELLS[player.spellId][0]) {
		case 12871:
		case 12891:
			return 0;

		default:
			return 100;
		}
	}

	public int getStartHeight() {
		switch (player.MAGIC_SPELLS[player.spellId][0]) {
		case 1562: // stun
			return 25;

		case 12939:// smoke rush
			return 35;

		case 12987: // shadow rush
			return 38;

		case 12861: // ice rush
			return 15;

		case 12951: // smoke blitz
			return 38;

		case 12999: // shadow blitz
			return 25;

		case 12911: // blood blitz
			return 25;

		default:
			return 43;
		}
	}

	/**
	 * Weapon emotes
	 **/

	public int getWepAnim(String weaponName) {
		if (player.playerEquipment[player.playerWeapon] <= 0) {
			switch (player.fightMode) {
			case 0:
			case 2:
				return 423;
			case 1:
				return 422;
			}
		}
		if (player.playerEquipment[player.playerWeapon] == 13883) {
			return 10501;
		}
		if (player.playerEquipment[player.playerWeapon] == 4718) {
			if (player.fightMode > 1)
				return 2067;
			else
				return 2066;
		}
		if (weaponName.contains("knife") || weaponName.contains("dart")

		|| weaponName.contains("thrownaxe")) {
			return 806;
		}
		if (weaponName.contains("halberd")) {
			return 440;
		}
		if (weaponName.startsWith("dragon dagger")) {
			return 402;
		}
		if (weaponName.startsWith("staff ") || weaponName.endsWith("staff")
				|| weaponName.endsWith("flag") || weaponName.startsWith("flag")) {
			return 419;
		}
		if (weaponName.endsWith("dagger")) {
			return 402;
		}
		if (weaponName.contains("2h sword") || weaponName.contains("godsword")
				|| weaponName.contains("aradomin sword")) {
			switch (player.fightMode) {
			case 1:
				return 7048;
			case 2:
				return 7041;
			}
			return 7042;
		}
		if(weaponName.contains("spear")){
			return 2080;
		}
		if (weaponName.contains("sword")) {
			return 451;
		}
		if (weaponName.contains("karil")) {
			return 2075;
		}
		if (weaponName.contains("bow") && !weaponName.contains("'bow")) {
			return 426;
		}
		if (weaponName.contains("'bow")) {
			return 4230;
		}

		switch (player.playerEquipment[player.playerWeapon]) { // if you don't
																// want to use
		// strings
		case 13879:
			return 10501;
		case 11037:
		case 9703:
			return 400;
		case 4024:
		case 4025:
		case 4029:
			return 1392;
		case 4026:
		case 4027:
			return 1402;
		case 6522:
			return 2614;
		case 10033:
		case 10034:
			return 2779;
		case 6082:
			return 2324;
		case 5698:
		case 10581:// keris
			return 402;
		case 4153: // granite maul
		case 11863:
			return 1665;
		case 4747: // torag
			return 0x814;
		case 4710: // ahrim
			return 406;
		case 4755: // verac
			return 2062;
		case 4734: // karil
			return 2075;
		case 4151:
		case 8285:
		case 12006:
			return 1658;
		case 13899:
			return 412;
		case 6528:
			return 2661;
		case 10887:
			return 5865;
		default:
			return 451;
		}
	}

	public boolean godSpells() {
		switch (player.MAGIC_SPELLS[player.spellId][0]) {
		case 1190:
			return true;

		case 1191:
			return true;

		case 1192:
			return true;

		default:
			return false;
		}
	}

	public void handleArcher() {
		if (System.currentTimeMillis() - player.dfsDelay > 60000) {
			if (player.playerIndex > 0
					&& PlayerHandler.players[player.playerIndex] != null) {
				final int damage = Misc.random(10) + 7;
				player.startAnimation(369);
				player.gfx0(369);
				PlayerHandler.players[player.playerIndex].playerLevel[3] -= damage;
				PlayerHandler.players[player.playerIndex].hitDiff2 = damage;
				player.getUpdateFlags().forcedText = "Feel the power of the Archer Ring!";
				PlayerHandler.players[player.playerIndex].getUpdateFlags().hitUpdateRequired2 = true;
				PlayerHandler.players[player.playerIndex].updateRequired = true;
				player.dfsDelay = System.currentTimeMillis();
			} else {
				player.sendMessage("I should be in combat before using this.");
			}
		} else {
			player.sendMessage("My ring hasn't finished recharging yet (60 Seconds)");
		}
	}

	public void handleDfsNPC() {
		if (System.currentTimeMillis() - player.dfsDelay > 30000) {
			if (player.npcIndex > 0 && NPCHandler.npcs[player.npcIndex] != null) {
				final int damage = Misc.random(15) + 5;
				player.startAnimation(2836);
				player.gfx0(600);
				NPCHandler.npcs[player.npcIndex].HP -= damage;
				NPCHandler.npcs[player.npcIndex].hitDiff2 = damage;
				NPCHandler.npcs[player.npcIndex].getUpdateFlags().hitUpdateRequired2 = true;
				NPCHandler.npcs[player.npcIndex].getUpdateFlags().updateRequired = true;
				player.dfsDelay = System.currentTimeMillis();
			} else {
				player.sendMessage("I should be in combat before using this.");
			}
		} else {
			player.sendMessage("My shield hasn't finished recharging yet.");
		}
	}

	public void handleGmaulPlayer() {
		if (player.playerIndex > 0) {
			final Client o = (Client) PlayerHandler.players[player.playerIndex];
			if (player.goodDistance(player.getX(), player.getY(), o.getX(),
					o.getY(), getRequiredDistance())) {
				if (checkReqs()) {
					if (checkSpecAmount(4153) || checkSpecAmount(11863)) {
						final boolean hit = Misc.random(calculateMeleeAttack()) > Misc
								.random(o.getCombat().calculateMeleeDefence());
						int damage = 0;
						if (hit) {
							damage = Misc.random(MeleeFormula
									.calculateMeleeMaxHit(player) - 5);
						}
						o.handleHitMask(damage);
						player.startAnimation(1667);
						player.gfx100(337);
						o.dealDamage(damage);
					}
				}
			}
		}
	}

	public void handlePrayerDrain() {
		player.usingPrayer = false;
		double toRemove = 0.0;
		for (int j = 0; j < PrayerData.PRAYER_DATA.length; j++) {
			if (player.prayerActive[j]) {
				toRemove += PrayerData.PRAYER_DATA[j] / 20;
				player.usingPrayer = true;
			}
		}
		if (toRemove > 0) {
			toRemove /= 1 + 0.035 * player.playerBonus[11];
		}
		player.prayerPoint -= toRemove;
		if (player.prayerPoint <= 0) {
			player.prayerPoint = 1.0 + player.prayerPoint;
			reducePrayerLevel();
		}

	}

	public void handleSeers() {

		player.castingMagic = true;
		if (System.currentTimeMillis() - player.dfsDelay > 60000) {
			if (player.playerIndex > 0
					&& PlayerHandler.players[player.playerIndex] != null) {
				final int damage = Misc.random(10) + 7;
				player.startAnimation(1979);
				PlayerHandler.players[player.playerIndex].gfx0(369);
				player.gfx0(368);
				PlayerHandler.players[player.playerIndex].freezeTimer = 15;
				PlayerHandler.players[player.playerIndex].resetWalkingQueue();
				PlayerHandler.players[player.playerIndex].frozenBy = player.playerId;
				PlayerHandler.players[player.playerIndex].playerLevel[3] -= damage;
				player.getUpdateFlags().forcedText = "Feel the power of the Seers Ring!";
				PlayerHandler.players[player.playerIndex].hitDiff2 = damage;

				PlayerHandler.players[player.playerIndex].getUpdateFlags().hitUpdateRequired2 = true;
				PlayerHandler.players[player.playerIndex].updateRequired = true;
				player.dfsDelay = System.currentTimeMillis();
			} else {
				player.sendMessage("I should be in combat before using this.");
			}
		} else {
			player.sendMessage("My ring hasn't finished recharging yet (60 Seconds)");
		}
	}

	public void handleWarrior() {
		if (System.currentTimeMillis() - player.dfsDelay > 60000) {
			if (player.playerIndex > 0
					&& PlayerHandler.players[player.playerIndex] != null) {
				final int damage = Misc.random(10) + 7;
				player.startAnimation(369);
				player.gfx0(369);
				PlayerHandler.players[player.playerIndex].playerLevel[3] -= damage;
				player.getUpdateFlags().forcedText = "Feel the power of the Warrior Ring!";
				PlayerHandler.players[player.playerIndex].hitDiff2 = damage;
				PlayerHandler.players[player.playerIndex].getUpdateFlags().hitUpdateRequired2 = true;
				PlayerHandler.players[player.playerIndex].updateRequired = true;
				player.dfsDelay = System.currentTimeMillis();
			} else {
				player.sendMessage("I should be in combat before using this.");
			}
		} else {
			player.sendMessage("My ring hasn't finished recharging yet (60 Seconds)");
		}
	}

	public void handleZerker() {

		if (System.currentTimeMillis() - player.dfsDelay > 60000) {
			if (player.playerIndex > 0
					&& PlayerHandler.players[player.playerIndex] != null) {
				final int damage = Misc.random(10) + 7;
				player.startAnimation(369);
				player.gfx0(369);
				PlayerHandler.players[player.playerIndex].playerLevel[3] -= damage;
				PlayerHandler.players[player.playerIndex].hitDiff2 = damage;
				player.getUpdateFlags().forcedText = "Feel the power of the Berserker Ring!";
				PlayerHandler.players[player.playerIndex].getUpdateFlags().hitUpdateRequired2 = true;
				PlayerHandler.players[player.playerIndex].updateRequired = true;
				player.dfsDelay = System.currentTimeMillis();
			} else {
				player.sendMessage("I should be in combat before using this.");
			}
		} else {
			player.sendMessage("My ring hasn't finished recharging yet (60 Seconds)");
		}
	}

	/**
	 * MAGIC
	 **/

	public int mageAtk() {
		int attackLevel = player.playerLevel[6];
		if (player.fullVoidMage()) {
			attackLevel += player.getLevelForXP(player.playerXP[6]) * 0.2;
		}
		if (player.prayerActive[4]) {
			attackLevel *= 1.05;
		} else if (player.prayerActive[12]) {
			attackLevel *= 1.10;
		} else if (player.prayerActive[20]) {
			attackLevel *= 1.15;
		}
		return (int) (attackLevel + player.playerBonus[3] * 2);
	}

	public int mageDef() {
		int defenceLevel = player.playerLevel[1] / 2 + player.playerLevel[6]
				/ 2;
		if (player.prayerActive[0]) {
			defenceLevel += player
					.getLevelForXP(player.playerXP[player.playerDefence]) * 0.05;
		} else if (player.prayerActive[3]) {
			defenceLevel += player
					.getLevelForXP(player.playerXP[player.playerDefence]) * 0.1;
		} else if (player.prayerActive[9]) {
			defenceLevel += player
					.getLevelForXP(player.playerXP[player.playerDefence]) * 0.15;
		} else if (player.prayerActive[18]) {
			defenceLevel += player
					.getLevelForXP(player.playerXP[player.playerDefence]) * 0.2;
		} else if (player.prayerActive[19]) {
			defenceLevel += player
					.getLevelForXP(player.playerXP[player.playerDefence]) * 0.25;
		}
		return (int) (defenceLevel + player.playerBonus[8] + player.playerBonus[8] / 3);
	}

	public boolean multis() {
		switch (player.MAGIC_SPELLS[player.oldSpellId][0]) {
		case 12891:
		case 12881:
		case 13011:
		case 13023:
		case 12919: // blood spells
		case 12929:
		case 12963:
		case 12975:
			return true;
		}
		return false;

	}

	public void multiSpellEffect(int playerId, int damage) {
		switch (player.MAGIC_SPELLS[player.oldSpellId][0]) {
		case 13011:
		case 13023:
			if (System.currentTimeMillis()
					- PlayerHandler.players[playerId].reduceStat > 35000) {
				PlayerHandler.players[playerId].reduceStat = System
						.currentTimeMillis();
				PlayerHandler.players[playerId].playerLevel[0] -= PlayerHandler.players[playerId]
						.getLevelForXP(PlayerHandler.players[playerId].playerXP[0]) * 10 / 100;
			}
			break;
		case 12919: // blood spells
		case 12929:
			final int heal = (int) (damage / 4);
			if (player.playerLevel[3] + heal >= player.getFunction()
					.getLevelForXP(player.playerXP[3])) {
				player.playerLevel[3] = player.getFunction().getLevelForXP(
						player.playerXP[3]);
			} else {
				player.playerLevel[3] += heal;
			}
			player.getFunction().refreshSkill(3);
			break;
		case 12891:
		case 12881:
			if (PlayerHandler.players[playerId].freezeTimer < -4) {
				PlayerHandler.players[playerId].freezeTimer = getFreezeTime();
				PlayerHandler.players[playerId].stopMovement();
			}
			break;
		}
	}

	public void playerDelayedHit(final int i) {
		if (PlayerHandler.players[i] != null) {
			if (PlayerHandler.players[i].isDead || player.isDead
					|| PlayerHandler.players[i].playerLevel[3] <= 0
					|| player.playerLevel[3] <= 0) {
				player.playerIndex = 0;
				return;
			}
			if (PlayerHandler.players[i].isDead) {
				player.faceUpdate(0);
				player.playerIndex = 0;
				return;
			}
			final Client o = (Client) PlayerHandler.players[i];
			o.getFunction().removeAllWindows();
			if (o.playerIndex <= 0 && o.npcIndex <= 0) {
				if (o.autoRet == 1) {
					o.playerIndex = player.playerId;
				}
			}
			if (o.attackTimer <= 3 || o.attackTimer == 0 && o.playerIndex == 0
					&& !player.castingMagic) { // block animation
				o.startAnimation(o.getCombat().getBlockEmote());
			}
			if (o.inTrade) {
				o.getTrade().declineTrade();
			}
			if (player.projectileStage == 0 && !player.usingMagic
					&& !player.castingMagic) { // melee
				// hit
				// damage
				if (o.playerLevel[3] - player.delayedDamage < 1)
					player.delayedDamage = o.playerLevel[3];

				applyPlayerDamage(i, 1, player.delayedDamage + player.delayedDamage2);

				if (player.quadHit) {
					final int[] clawsHit = new int[] {
							Misc.random(player.delayedDamage / 2) + player.delayedDamage2 / 2, player.delayedDamage2 / 2 };
					applyPlayerDamage(i, 1, player.delayedDamage);

					World.getSynchronizedTaskScheduler().schedule(
							new Task(1, true) {

								protected void execute() {
									applyPlayerDamage(i, 1, clawsHit[0]);
									applyPlayerDamage(i, 2, clawsHit[1]);
									stop();
								}
							});
					player.quadHit = false;
					player.quadHitDamage = 0;
				}

			}
			if (player.doubleHit) {

				if (o.playerLevel[3] - player.delayedDamage2 < 1)
					player.delayedDamage2 = o.playerLevel[3];
				applyPlayerDamage(i, 2, player.delayedDamage2);

			}

			if (!player.castingMagic && player.projectileStage > 0) { // range
																		// hit
																		// damage
				int damage = Misc.random(rangeMaxHit());
				int damage2 = -1;
				if (player.lastWeaponUsed == 11235 || player.bowSpecShot == 1) {
					damage2 = Misc.random(rangeMaxHit());
				}
				boolean ignoreDef = false;
				if (Misc.random(4) == 1 && player.lastArrowUsed == 9243
						&& RangeConfiguration.usingCrossbow(player)) {
					ignoreDef = true;
					o.gfx0(758);
				}
				if (Misc.random(10 + o.getCombat().calculateRangeDefence()) > Misc
						.random(10 + calculateRangeAttack()) && !ignoreDef) {
					damage = 0;
				}
				if (Misc.random(4) == 1 && player.lastArrowUsed == 9242
						&& damage > 0
						&& RangeConfiguration.usingCrossbow(player)) {
					o.gfx0(754);
					damage = PlayerHandler.players[i].playerLevel[3] / 10;
				}

				if (player.lastWeaponUsed == 11235 || player.bowSpecShot == 1) {
					if (Misc.random(10 + o.getCombat().calculateRangeDefence()) > Misc
							.random(10 + calculateRangeAttack())) {
						damage2 = 0;
					}
				}

				if (player.dbowSpec) {
					o.gfx100(1100);
					if (damage < 8) {
						damage = 8;
					}
					if (damage2 < 8) {
						damage2 = 8;
					}
					player.dbowSpec = false;
				}
				if (damage > 20 && Misc.random(3) == 1
						&& player.lastArrowUsed == 9244
						&& RangeConfiguration.usingCrossbow(player)) {
					damage *= 1.50;
					o.gfx0(756);
				}
				if (damage > 25 && Misc.random(3) == 1
						&& player.lastArrowUsed == 9245
						&& RangeConfiguration.usingCrossbow(player)) {
					damage *= 1.60;
					o.gfx0(753);
				}
				if (o.prayerActive[17]
						&& System.currentTimeMillis() - o.protRangeDelay > 1500) { // if
																					// prayer
																					// active
																					// reduce
																					// damage
																					// by
																					// half
					damage = (int) damage * 60 / 100;
					if (player.lastWeaponUsed == 11235
							|| player.bowSpecShot == 1) {
						damage2 = (int) damage2 * 60 / 100;
					}
				}
				if (PlayerHandler.players[i].playerLevel[3] - damage < 0) {
					damage = PlayerHandler.players[i].playerLevel[3];
				}
				if (PlayerHandler.players[i].playerLevel[3] - damage - damage2 < 0) {
					damage2 = PlayerHandler.players[i].playerLevel[3] - damage;
				}
				if (damage < 0) {
					damage = 0;
				}
				if (damage2 < 0 && damage2 != -1) {
					damage2 = 0;
				}
				if (o.vengOn) {
					appendVengeance(i, damage);
					appendVengeance(i, damage2);
				}
				if (damage > 0) {
					applyRecoil(damage, i);
				}
				if (damage2 > 0) {
					applyRecoil(damage2, i);
				}
				boolean dropArrows = true;

				for (final int noArrowId : player.NO_ARROW_DROP) {
					if (player.lastWeaponUsed == noArrowId) {
						dropArrows = false;
						break;
					}
				}
				if (dropArrows) {
					player.getItems().dropArrowPlayer();
				}
				PlayerHandler.players[i].underAttackBy = player.playerId;
				PlayerHandler.players[i].logoutDelay = System
						.currentTimeMillis();
				PlayerHandler.players[i].singleCombatDelay = System
						.currentTimeMillis();
				PlayerHandler.players[i].killerId = player.playerId;
				// Server.playerHandler.players[i].setHitDiff(damage);
				// Server.playerHandler.players[i].playerLevel[3] -= damage;
				PlayerHandler.players[i].dealDamage(damage);
				PlayerHandler.players[i].damageTaken[player.playerId] += damage;
				player.killedBy = PlayerHandler.players[i].playerId;
				PlayerHandler.players[i].handleHitMask(damage);
				if (damage2 != -1) {
					// Server.playerHandler.players[i].playerLevel[3] -=
					// damage2;
					PlayerHandler.players[i].dealDamage(damage2);
					PlayerHandler.players[i].damageTaken[player.playerId] += damage2;
					PlayerHandler.players[i].handleHitMask(damage2);

				}
				o.getFunction().refreshSkill(3);

				// Server.playerHandler.players[i].setHitUpdateRequired(true);
				PlayerHandler.players[i].updateRequired = true;
				applySmite(i, damage);
				applySoulsplit(i, damage);
				if (damage2 != -1) {
					applySmite(i, damage2);
					applySoulsplit(i, damage2);
				}

			} else if (player.projectileStage > 0) { // magic hit damage
				int damage = Misc
						.random(player.MAGIC_SPELLS[player.oldSpellId][6]);
				if (godSpells()) {
					if (System.currentTimeMillis() - player.godSpellDelay < GameConfig.GOD_SPELL_CHARGE) {
						damage += 10;
					}
				}
				// player.playerIndex = 0;
				if (player.magicFailed) {
					damage = 0;
				}

				if (o.prayerActive[16]
						&& System.currentTimeMillis() - o.protMageDelay > 1500) { // if
																					// prayer
																					// active
																					// reduce
																					// damage
																					// by
																					// half
					damage = (int) damage * 60 / 100;
				}
				if (player.playerEquipment[player.playerWeapon] == 8294)// sotd
					damage = (int) (damage * 1.15);
				if (player.playerEquipment[player.playerAmulet] == 8284)// occult
					damage = (int) (damage * 1.10);
				if (PlayerHandler.players[i].playerLevel[3] - damage < 0) {
					damage = PlayerHandler.players[i].playerLevel[3];
				}
				if (o.vengOn) {
					appendVengeance(i, damage);
				}
				if (damage > 0) {
					applyRecoil(damage, i);
				}
				player.getFunction()
						.addSkillXP(
								(player.MAGIC_SPELLS[player.oldSpellId][7] + damage
										* CombatConfig
												.getCombatXP(player.playerTitle)),
								6);
				player.getFunction().addSkillXP(
						(player.MAGIC_SPELLS[player.oldSpellId][7] + damage
								* CombatConfig.getCombatXP(player.playerTitle)
								/ 3), 3);
				player.getFunction().refreshSkill(3);
				player.getFunction().refreshSkill(6);

				if (getEndGfxHeight() == 100 && !player.magicFailed) { // end
																		// GFX
					PlayerHandler.players[i]
							.gfx100(player.MAGIC_SPELLS[player.oldSpellId][5]);
				} else if (!player.magicFailed) {
					PlayerHandler.players[i]
							.gfx0(player.MAGIC_SPELLS[player.oldSpellId][5]);
				} else if (player.magicFailed) {
					PlayerHandler.players[i].gfx100(85);
				}

				if (!player.magicFailed) {
					if (System.currentTimeMillis()
							- PlayerHandler.players[i].reduceStat > 35000) {
						PlayerHandler.players[i].reduceStat = System
								.currentTimeMillis();
						switch (player.MAGIC_SPELLS[player.oldSpellId][0]) {
						case 12987:
						case 13011:
						case 12999:
						case 13023:
							PlayerHandler.players[i].playerLevel[0] -= o
									.getFunction()
									.getLevelForXP(
											PlayerHandler.players[i].playerXP[0]) * 10 / 100;
							break;
						}
					}

					switch (player.MAGIC_SPELLS[player.oldSpellId][0]) {
					case 12445: // teleblock
						if (System.currentTimeMillis() - o.teleBlockDelay > o.teleBlockLength) {
							o.teleBlockDelay = System.currentTimeMillis();
							o.sendMessage("You have been teleblocked.");
							if (o.prayerActive[16]
									&& System.currentTimeMillis()
											- o.protMageDelay > 1500) {
								o.teleBlockLength = 150000;
							} else {
								o.teleBlockLength = 300000;
							}
						}
						break;

					case 12901:
					case 12919: // blood spells
					case 12911:
					case 12929:
						final int heal = (int) (damage / 4);
						if (player.playerLevel[3] + heal > player.getFunction()
								.getLevelForXP(player.playerXP[3])) {
							player.playerLevel[3] = player.getFunction()
									.getLevelForXP(player.playerXP[3]);
						} else {
							player.playerLevel[3] += heal;
						}
						player.getFunction().refreshSkill(3);
						break;

					case 1153:
						PlayerHandler.players[i].playerLevel[0] -= o
								.getFunction().getLevelForXP(
										PlayerHandler.players[i].playerXP[0]) * 5 / 100;
						o.sendMessage("Your attack level has been reduced!");
						PlayerHandler.players[i].reduceSpellDelay[player.reduceSpellId] = System
								.currentTimeMillis();
						o.getFunction().refreshSkill(0);
						break;

					case 1157:
						PlayerHandler.players[i].playerLevel[2] -= o
								.getFunction().getLevelForXP(
										PlayerHandler.players[i].playerXP[2]) * 5 / 100;
						o.sendMessage("Your strength level has been reduced!");
						PlayerHandler.players[i].reduceSpellDelay[player.reduceSpellId] = System
								.currentTimeMillis();
						o.getFunction().refreshSkill(2);
						break;

					case 1161:
						PlayerHandler.players[i].playerLevel[1] -= o
								.getFunction().getLevelForXP(
										PlayerHandler.players[i].playerXP[1]) * 5 / 100;
						o.sendMessage("Your defence level has been reduced!");
						PlayerHandler.players[i].reduceSpellDelay[player.reduceSpellId] = System
								.currentTimeMillis();
						o.getFunction().refreshSkill(1);
						break;

					case 1542:
						PlayerHandler.players[i].playerLevel[1] -= o
								.getFunction().getLevelForXP(
										PlayerHandler.players[i].playerXP[1]) * 10 / 100;
						o.sendMessage("Your defence level has been reduced!");
						PlayerHandler.players[i].reduceSpellDelay[player.reduceSpellId] = System
								.currentTimeMillis();
						o.getFunction().refreshSkill(1);
						break;

					case 1543:
						PlayerHandler.players[i].playerLevel[2] -= o
								.getFunction().getLevelForXP(
										PlayerHandler.players[i].playerXP[2]) * 10 / 100;
						o.sendMessage("Your strength level has been reduced!");
						PlayerHandler.players[i].reduceSpellDelay[player.reduceSpellId] = System
								.currentTimeMillis();
						o.getFunction().refreshSkill(2);
						break;

					case 1562:
						PlayerHandler.players[i].playerLevel[0] -= o
								.getFunction().getLevelForXP(
										PlayerHandler.players[i].playerXP[0]) * 10 / 100;
						o.sendMessage("Your attack level has been reduced!");
						PlayerHandler.players[i].reduceSpellDelay[player.reduceSpellId] = System
								.currentTimeMillis();
						o.getFunction().refreshSkill(0);
						break;
					}
				}

				PlayerHandler.players[i].logoutDelay = System
						.currentTimeMillis();
				PlayerHandler.players[i].underAttackBy = player.playerId;
				PlayerHandler.players[i].killerId = player.playerId;
				PlayerHandler.players[i].singleCombatDelay = System
						.currentTimeMillis();
				if (player.MAGIC_SPELLS[player.oldSpellId][6] != 0) {
					// Server.playerHandler.players[i].playerLevel[3] -= damage;
					PlayerHandler.players[i].dealDamage(damage);
					PlayerHandler.players[i].damageTaken[player.playerId] += damage;
					player.totalPlayerDamageDealt += damage;
					if (!player.magicFailed) {
						// Server.playerHandler.players[i].setHitDiff(damage);
						// Server.playerHandler.players[i].setHitUpdateRequired(true);
						PlayerHandler.players[i].handleHitMask(damage);
					}
				}
				applySmite(i, damage);
				player.killedBy = PlayerHandler.players[i].playerId;
				o.getFunction().refreshSkill(3);
				PlayerHandler.players[i].updateRequired = true;
				player.usingMagic = false;
				player.castingMagic = false;
				if (o.inMulti() && multis()) {
					player.barrageCount = 0;
					for (int j = 0; j < PlayerHandler.players.length; j++) {
						if (PlayerHandler.players[j] != null) {
							if (j == o.playerId) {
								continue;
							}
							if (player.barrageCount >= 9) {
								break;
							}
							if (o.goodDistance(o.getX(), o.getY(),
									PlayerHandler.players[j].getX(),
									PlayerHandler.players[j].getY(), 1)) {
								appendMultiBarrage(j, player.magicFailed);
							}
						}
					}
				}
				player.getFunction().refreshSkill(3);
				player.getFunction().refreshSkill(6);
				player.oldSpellId = 0;
			}
		}
		player.getFunction().requestUpdates();
		if (player.bowSpecShot <= 0) {
			player.oldPlayerIndex = 0;
			player.projectileStage = 0;
			player.lastWeaponUsed = 0;
			player.doubleHit = false;
			player.bowSpecShot = 0;
		}
		if (player.bowSpecShot != 0) {
			player.bowSpecShot = 0;
		}
	}

	public boolean properBolts() {
		return player.playerEquipment[player.playerArrows] >= 9140
				&& player.playerEquipment[player.playerArrows] <= 9144
				|| player.playerEquipment[player.playerArrows] >= 9240
				&& player.playerEquipment[player.playerArrows] <= 9245;
	}

	public int rangeMaxHit() {
		final int rangeLevel = player.playerLevel[4];
		double modifier = 1.0;
		final double wtf = player.specDamage;
		final int itemUsed = player.usingBow ? player.lastArrowUsed
				: player.lastWeaponUsed;
		if (player.prayerActive[3]) {
			modifier += 0.05;
		} else if (player.prayerActive[11]) {
			modifier += 0.10;
		} else if (player.prayerActive[19]) {
			modifier += 0.15;
		}
		if (player.fullVoidRange()) {
			modifier += 0.15;
		}
		if (player.playerTitle == 4) {
			modifier += 0.05;
		}
		if (player.isIronman()) {
			modifier += 0.10;
		}
		if (player.isUltimateIronman()) {
			modifier += 0.25;
		}
		if (player.playerEquipment[player.playerWeapon] == 4212)
			modifier += 1;
		final double player = modifier * rangeLevel;
		final int rangeStr = getRangeStr(itemUsed);
		double max = (player + 8) * (rangeStr + 64) / 640;
		if (wtf != 1) {
			max *= wtf;
		}
		if (max < 1) {
			max = 1;
		}
		return (int) max;
	}

	public void reducePrayerLevel() {
		if (player.playerLevel[5] - 1 > 0) {
			player.playerLevel[5] -= 1;
		} else {
			player.sendMessage(GameConfig.NO_PRAYER_MESSAGE);
			player.playerLevel[5] = 0;
			Prayer.resetPrayers(player);
			player.prayerId = -1;
		}
		player.getFunction().refreshSkill(5);
	}

	public void resetPlayerAttack() {
		player.usingMagic = false;
		player.npcIndex = 0;
		player.faceUpdate(0);
		player.playerIndex = 0;
		player.getFunction().resetFollow();
		// player.sendMessage("Reset attack.");
	}

	public boolean usingHally() {
		switch (player.playerEquipment[player.playerWeapon]) {
		case 3190:
		case 3192:
		case 3194:
		case 3196:
		case 3198:
		case 3200:
		case 3202:
		case 3204:
			return true;

		default:
			return false;
		}
	}

	public boolean wearingStaff(int runeId) {
		final int wep = player.playerEquipment[player.playerWeapon];
		switch (runeId) {
		case 554:
			if (wep == 1387) {
				return true;
			}
			break;
		case 555:
			if (wep == 1383) {
				return true;
			}
			break;
		case 556:
			if (wep == 1381) {
				return true;
			}
			break;
		case 557:
			if (wep == 1385) {
				return true;
			}
			break;
		}
		return false;
	}

}
