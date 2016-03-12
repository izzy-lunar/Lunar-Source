package com.ownxile.rs2.combat;

import com.ownxile.rs2.items.ItemAssistant;
import com.ownxile.rs2.npcs.NPCHandler;
import com.ownxile.rs2.player.Client;
import com.ownxile.rs2.player.PlayerHandler;

public class SpecialAttack {

	public static void activateSpecial(Client player, int i) {
		if (NPCHandler.npcs[i] == null && player.npcIndex > 0) {
			return;
		}
		if (PlayerHandler.players[i] == null && player.playerIndex > 0) {
			return;
		}
		player.doubleHit = false;
		player.specEffect = 0;
		player.projectileStage = 0;
		player.specMaxHitIncrease = 2;
		if (player.npcIndex > 0) {
			player.oldNpcIndex = i;
		} else if (player.playerIndex > 0) {
			player.oldPlayerIndex = i;
			PlayerHandler.players[i].underAttackBy = player.playerId;
			PlayerHandler.players[i].logoutDelay = System.currentTimeMillis();
			PlayerHandler.players[i].singleCombatDelay = System
					.currentTimeMillis();
			PlayerHandler.players[i].killerId = player.playerId;
		}
		int weapon = player.playerEquipment[player.playerWeapon];
		switch (weapon) {
		case 13883: // Morrigan Throwing Axe

			player.usingRangeWeapon = true;
			player.rangeItemUsed = player.playerEquipment[player.playerArrows];
			player.getItems().deleteArrow();
			player.lastWeaponUsed = weapon;
			player.startAnimation(10501);
			player.gfx0(1836);
			player.hitDelay = 3;
			player.specAccuracy = 2.90;
			player.specDamage = 1.30;
			player.projectileStage = 1;
			player.hitDelay = player.getCombat().getHitDelay(
					ItemAssistant.getItemName(
							player.playerEquipment[player.playerWeapon])
							.toLowerCase());
			if (player.fightMode == 2)
				player.attackTimer--;
			if (player.playerIndex > 0)
				player.getCombat().fireProjectilePlayer();
			else if (player.npcIndex > 0)
				player.getCombat().fireProjectileNpc();
			break;

		case 13879: // Morrigan Javeline
			player.usingRangeWeapon = true;
			player.rangeItemUsed = player.playerEquipment[player.playerArrows];
			player.getItems().deleteArrow();
			player.lastWeaponUsed = weapon;
			player.startAnimation(10504);
			player.gfx0(1838);
			player.specAccuracy = 2.00;
			player.specDamage = 1.30;
			player.hitDelay = 3;
			player.projectileStage = 1;
			player.hitDelay = player.getCombat().getHitDelay(
					ItemAssistant.getItemName(
							player.playerEquipment[player.playerWeapon])
							.toLowerCase());
			if (player.fightMode == 2)
				player.attackTimer--;
			if (player.playerIndex > 0)
				player.getCombat().fireProjectilePlayer();
			else if (player.npcIndex > 0)
				player.getCombat().fireProjectileNpc();
			break;
		case 1305: // dragon long
			player.gfx100(248);
			player.startAnimation(1058);
			player.hitDelay = player.getCombat().getHitDelay(
					ItemAssistant.getItemName(
							player.playerEquipment[player.playerWeapon])
							.toLowerCase());
			player.specAccuracy = 1.30;
			player.specDamage = 1.30;
			break;
		case 7806: // anger
			player.gfx100(1176);
			player.startAnimation(1914);
			player.hitDelay = player.getCombat().getHitDelay(
					ItemAssistant.getItemName(
							player.playerEquipment[player.playerWeapon])
							.toLowerCase());
			player.specAccuracy = 1.70;
			player.specDamage = 1.20;
			player.doubleHit = true;
			break;

		case 1215: // dragon daggers
		case 1231:
		case 5680:
		case 5698:
			player.gfx100(252);
			player.startAnimation(1062);
			player.hitDelay = player.getCombat().getHitDelay(
					ItemAssistant.getItemName(
							player.playerEquipment[player.playerWeapon])
							.toLowerCase());
			player.doubleHit = true;
			player.specAccuracy = 1.80;
			player.specDamage = 1.15;
			break;

		case 11730:
			player.gfx100(1224);
			player.startAnimation(7072);
			player.hitDelay = player.getCombat().getHitDelay(
					ItemAssistant.getItemName(
							player.playerEquipment[player.playerWeapon])
							.toLowerCase());
			player.doubleHit = true;
			player.ssSpec = true;
			player.specAccuracy = 2.00;
			player.specDamage = 1.10;
			break;
		case 11777:
			player.gfx100(1950);
			player.startAnimation(10961);
			player.hitDelay = player.getCombat().getHitDelay(
					ItemAssistant.getItemName(
							player.playerEquipment[player.playerWeapon])
							.toLowerCase());
			player.doubleHit = false;
			player.quadHit = true;
			player.ssSpec = true;
			player.specAccuracy = 5.50;
			player.specDamage = 1.00;
			break;
		case 8285:
		case 4151: // whips
		case 12006:
			if (NPCHandler.npcs[i] != null) {
				NPCHandler.npcs[i].gfx100(341);
			}
			player.specAccuracy = 2.00;
			player.startAnimation(1658);
			player.hitDelay = player.getCombat().getHitDelay(
					ItemAssistant.getItemName(
							player.playerEquipment[player.playerWeapon])
							.toLowerCase());
			break;

		case 11802: // ags
			player.startAnimation(7074);
			player.specDamage = 1.40;
			player.specAccuracy = 7.50;
			player.gfx0(1222);
			player.hitDelay = player.getCombat().getHitDelay(
					ItemAssistant.getItemName(
							player.playerEquipment[player.playerWeapon])
							.toLowerCase());
			break;

		case 13899: // vls
			player.startAnimation(10502);
			player.specDamage = 1.00;
			player.specAccuracy = 6.50;
			player.hitDelay = player.getCombat().getHitDelay(
					ItemAssistant.getItemName(
							player.playerEquipment[player.playerWeapon])
							.toLowerCase());
			break;

		case 13902:// stat
			player.gfx100(1840);
			player.startAnimation(10505);
			player.specDamage = 1.20;
			player.specAccuracy = 1.50;
			player.hitDelay = player.getCombat().getHitDelay(
					ItemAssistant.getItemName(
							player.playerEquipment[player.playerWeapon])
							.toLowerCase());
			break;
		case 13905:// vesta spear
			player.gfx100(1835);
			player.startAnimation(10499);
			player.specDamage = 1.20;
			player.doubleHit = true;
			player.specAccuracy = 1.50;
			player.hitDelay = player.getCombat().getHitDelay(
					ItemAssistant.getItemName(
							player.playerEquipment[player.playerWeapon])
							.toLowerCase());
			break;

		case 11700:
			player.startAnimation(7070);
			player.gfx0(1221);
			player.specDamage = 1.25;
			player.specAccuracy = 1.55;
			player.hitDelay = player.getCombat().getHitDelay(
					ItemAssistant.getItemName(
							player.playerEquipment[player.playerWeapon])
							.toLowerCase());
			player.specEffect = 2;
			break;

		case 11696:
			player.startAnimation(7073);
			player.gfx0(1223);
			player.specDamage = 1.25;
			player.specAccuracy = 1.65;
			player.hitDelay = player.getCombat().getHitDelay(
					ItemAssistant.getItemName(
							player.playerEquipment[player.playerWeapon])
							.toLowerCase());
			break;

		case 11698:
			player.startAnimation(7071);
			player.gfx0(1220);
			player.specDamage = 1.25;
			player.specAccuracy = 1.75;
			player.specEffect = 4;
			player.hitDelay = player.getCombat().getHitDelay(
					ItemAssistant.getItemName(
							player.playerEquipment[player.playerWeapon])
							.toLowerCase());
			break;

		case 1249:
			player.startAnimation(405);
			player.gfx100(253);
			break;

		case 10887:
			player.startAnimation(5870);
			player.gfx100(1027);
			player.specDamage = 1.45;
			player.specAccuracy = 1.80;
			player.hitDelay = player.getCombat().getHitDelay(
					ItemAssistant.getItemName(
							player.playerEquipment[player.playerWeapon])
							.toLowerCase());
			break;

		case 3204: // d hally
			player.gfx100(282);
			player.startAnimation(1203);
			player.hitDelay = player.getCombat().getHitDelay(
					ItemAssistant.getItemName(
							player.playerEquipment[player.playerWeapon])
							.toLowerCase());
			if (NPCHandler.npcs[i] != null && player.npcIndex > 0) {
				player.doubleHit = true;
			}
			if (PlayerHandler.players[i] != null && player.playerIndex > 0) {
				player.doubleHit = true;
			}
			break;

		case 4153: // maul
			player.startAnimation(1667);
			player.hitDelay = player.getCombat().getHitDelay(
					ItemAssistant.getItemName(
							player.playerEquipment[player.playerWeapon])
							.toLowerCase());
			/*
			 * if (player.playerIndex > 0) gmaulPlayer(i); else gmaulNpc(i);
			 */
			player.gfx100(337);
			break;

		case 4587: // dscimmy
			player.gfx100(347);
			player.specEffect = 1;
			player.startAnimation(1872);
			player.hitDelay = player.getCombat().getHitDelay(
					ItemAssistant.getItemName(
							player.playerEquipment[player.playerWeapon])
							.toLowerCase());
			break;

		case 1434: // mace
			player.startAnimation(1060);
			player.gfx100(251);
			player.specMaxHitIncrease = 3;
			player.hitDelay = player.getCombat().getHitDelay(
					ItemAssistant.getItemName(
							player.playerEquipment[player.playerWeapon])
							.toLowerCase()) + 1;
			player.specDamage = 1.40;
			player.specAccuracy = 1.25;
			break;

		case 859: // magic long
			player.usingBow = true;
			player.bowSpecShot = 3;
			player.rangeItemUsed = player.playerEquipment[player.playerArrows];
			player.getItems().deleteArrow();
			player.lastWeaponUsed = weapon;
			player.startAnimation(426);
			player.gfx100(250);
			player.hitDelay = player.getCombat().getHitDelay(
					ItemAssistant.getItemName(
							player.playerEquipment[player.playerWeapon])
							.toLowerCase());
			player.projectileStage = 1;
			if (player.fightMode == 2) {
				player.attackTimer--;
			}
			break;

		case 861: // magic short
			player.usingBow = true;
			player.bowSpecShot = 1;
			player.rangeItemUsed = player.playerEquipment[player.playerArrows];
			player.getItems().deleteArrow();
			player.lastWeaponUsed = weapon;
			player.startAnimation(1074);
			player.hitDelay = 2;
			player.projectileStage = 1;
			player.hitDelay = player.getCombat().getHitDelay(
					ItemAssistant.getItemName(
							player.playerEquipment[player.playerWeapon])
							.toLowerCase());
			if (player.fightMode == 2) {
				player.attackTimer--;
			}
			if (player.playerIndex > 0) {
				player.getCombat().fireProjectilePlayer();
			} else if (player.npcIndex > 0) {
				player.getCombat().fireProjectileNpc();
			}
			break;

		case 868: // knife
			player.usingBow = true;
			player.bowSpecShot = 1;
			player.rangeItemUsed = player.playerEquipment[player.playerArrows];
			player.getItems().deleteArrow();
			player.lastWeaponUsed = weapon;
			player.startAnimation(10501);
			player.hitDelay = 3;
			player.projectileStage = 1;
			player.hitDelay = player.getCombat().getHitDelay(
					ItemAssistant.getItemName(
							player.playerEquipment[player.playerWeapon])
							.toLowerCase());
			if (player.fightMode == 2) {
				player.attackTimer--;
			}
			break;

		case 11235: // dark bow
			player.usingBow = true;
			player.dbowSpec = true;
			player.rangeItemUsed = player.playerEquipment[player.playerArrows];
			player.getItems().deleteArrow();
			player.getItems().deleteArrow();
			player.lastWeaponUsed = weapon;
			player.hitDelay = 3;
			player.startAnimation(426);
			player.projectileStage = 1;
			player.gfx100(player.getCombat().getRangeStartGFX());
			player.hitDelay = player.getCombat().getHitDelay(
					ItemAssistant.getItemName(
							player.playerEquipment[player.playerWeapon])
							.toLowerCase());
			if (player.fightMode == 2) {
				player.attackTimer--;
			}
			if (player.playerIndex > 0) {
				player.getCombat().fireProjectilePlayer();
			} else if (player.npcIndex > 0) {
				player.getCombat().fireProjectileNpc();
			}
			player.specAccuracy = 4.75;
			player.specDamage = 1.60;
			break;
		}
		player.usingSpecial = false;
		player.getItems().updateSpecialBar();
	}
}
