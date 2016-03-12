package com.ownxile.rs2.combat;

import com.ownxile.config.GameConfig;
import com.ownxile.rs2.combat.range.RangeConfiguration;
import com.ownxile.rs2.items.ItemAssistant;
import com.ownxile.rs2.player.Client;
import com.ownxile.rs2.player.PlayerHandler;

public class Attack {

	public static boolean attackPlayer(Client c, int index) {
		if (index > PlayerHandler.players.length) {
			return false;
		}
		c.playerIndex = index;
		if (PlayerHandler.players[c.playerIndex] == null) {
			return false;
		}
		if (c.autocastId > 0) {
			c.autocasting = true;
		}

		if (!c.autocasting && c.spellId > 0) {
			c.spellId = 0;
		}
		c.mageFollow = false;
		c.spellId = 0;
		c.usingMagic = false;
		c.usingArrows = false;
		c.usingBow = false;
		c.usingOtherRangeWeapons = false;
		for (final int bowId : RangeConfiguration.BOWS) {
			if (c.playerEquipment[c.playerWeapon] == bowId) {
				c.usingBow = true;
				for (final int arrowId : RangeConfiguration.ARROWS) {
					if (c.playerEquipment[c.playerArrows] == arrowId) {
						c.usingArrows = true;
					}
				}
			}
		}
		for (final int otherRangeId : RangeConfiguration.OTHER_RANGE_WEAPONS) {
			if (c.playerEquipment[c.playerWeapon] == otherRangeId) {
				c.usingOtherRangeWeapons = true;
			}
		}
		if (c.duelStatus == 4) {
			c.boxMessage("Duel hasn't started yet.");
			return false;
		}
		if (c.duelStatus == 5) {
			if (c.duelRule[9]) {
				boolean canUseWeapon = false;
				for (final int funWeapon : GameConfig.FUN_WEAPONS) {
					if (c.playerEquipment[c.playerWeapon] == funWeapon) {
						canUseWeapon = true;
					}
				}
				if (!canUseWeapon) {
					c.sendMessage("You can only use fun weapons in this duel!");
					return false;
				}
			}

			if (c.duelRule[2] && (c.usingBow || c.usingOtherRangeWeapons)) {
				c.sendMessage("Range has been disabled in this duel!");
				return false;
			}
			if (c.duelRule[3] && !c.usingBow && !c.usingOtherRangeWeapons) {
				c.sendMessage("Melee has been disabled in this duel!");
				return false;
			}
		}

		if ((c.usingBow || c.autocasting)
				&& c.goodDistance(c.getX(), c.getY(),
						PlayerHandler.players[c.playerIndex].getX(),
						PlayerHandler.players[c.playerIndex].getY(), 8)) {
			c.stopMovement();
		}

		if (c.usingOtherRangeWeapons
				&& c.goodDistance(c.getX(), c.getY(),
						PlayerHandler.players[c.playerIndex].getX(),
						PlayerHandler.players[c.playerIndex].getY(), 4)) {
			c.usingRangeWeapon = true;
			c.stopMovement();
		}
		if (!c.usingOtherRangeWeapons) {
			c.usingRangeWeapon = false;
		}

		if (!RangeConfiguration.usingCrossbow(c) && !c.usingArrows
				&& c.usingBow && c.playerEquipment[c.playerWeapon] < 4212
				&& c.playerEquipment[c.playerWeapon] > 4223) {
			c.sendMessage("You have run out of arrows!");
			return false;
		}
		if (RangeConfiguration.correctBowAndArrows(c) < c.playerEquipment[c.playerArrows]
				&& GameConfig.CORRECT_ARROWS
				&& c.usingBow
				&& !RangeConfiguration.usingCrystalBow(c)
				&& !RangeConfiguration.usingCrossbow(c)) {
			c.sendMessage("You can't use "
					+ ItemAssistant.getItemName(
							c.playerEquipment[c.playerArrows]).toLowerCase()
					+ "s with a "
					+ ItemAssistant.getItemName(
							c.playerEquipment[c.playerWeapon]).toLowerCase()
					+ ".");
			c.stopMovement();
			c.getCombat().resetPlayerAttack();
			return false;
		}
		if (RangeConfiguration.usingCrossbow(c) && !c.getCombat().properBolts()) {
			c.sendMessage("You must use bolts with a crossbow.");
			c.stopMovement();
			c.getCombat().resetPlayerAttack();
			return false;
		}
		if (c.getCombat().checkReqs()) {

			c.followId = c.playerIndex;
			if (!c.usingMagic && !c.usingBow && !c.usingOtherRangeWeapons) {
				c.followDistance = 1;
				c.getFunction().combatFollowPlayer();
			}
			return true;
		}
		return false;
	}

}
