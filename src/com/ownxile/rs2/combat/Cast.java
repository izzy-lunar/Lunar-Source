package com.ownxile.rs2.combat;

import com.ownxile.rs2.player.Client;
import com.ownxile.rs2.player.PlayerHandler;

public class Cast {

	public static boolean castSpell(Client c, int index) {

		c.getFunction().hideMinimapFlag();
		if (!c.mageAllowed) {
			c.mageAllowed = true;
			return false;
		}

		if (index > PlayerHandler.players.length) {
			return false;
		}
		c.playerIndex = index;
		if (PlayerHandler.players[c.playerIndex] == null) {
			return false;
		}

		c.castingSpellId = c.getInStream().readSignedWordBigEndian();
		c.usingMagic = false;
		if (c.castingSpellId == 30298) {
			if (c.playerLevel[6] < 93) {
				c.sendMessage("You need a magic level of 93 to cast this spell.");
				return false;
			}
			c.getFunction().vengOther();
			return false;
		}

		for (int i = 0; i < c.MAGIC_SPELLS.length; i++) {
			if (c.castingSpellId == c.MAGIC_SPELLS[i][0]) {
				c.spellId = i;
				c.usingMagic = true;
				return false;
			}
		}

		if (c.autocasting) {
			c.autocasting = false;
		}
		if (c.castingSpellId != 30298) {
			if (!c.getCombat().checkReqs()) {
				return false;
			}
		}
		for (int r = 0; r < c.REDUCE_SPELLS.length; r++) { // reducing
															// spells,
															// confuse etc
			if (PlayerHandler.players[c.playerIndex].REDUCE_SPELLS[r] == c.MAGIC_SPELLS[c.spellId][0]) {
				if (System.currentTimeMillis()
						- PlayerHandler.players[c.playerIndex].reduceSpellDelay[r] < PlayerHandler.players[c.playerIndex].REDUCE_SPELL_TIME[r]) {
					c.sendMessage("That player is currently immune to this spell.");
					c.usingMagic = false;
					c.stopMovement();
					c.getCombat().resetPlayerAttack();
				}
				break;
			}
		}

		if (System.currentTimeMillis()
				- PlayerHandler.players[c.playerIndex].teleBlockDelay < PlayerHandler.players[c.playerIndex].teleBlockLength
				&& c.MAGIC_SPELLS[c.spellId][0] == 12445) {
			c.sendMessage("That player is already affected by this spell.");
			c.usingMagic = false;
			c.stopMovement();
			c.getCombat().resetPlayerAttack();
		}

		if (c.usingMagic) {
			if (c.goodDistance(c.getX(), c.getY(),
					PlayerHandler.players[c.playerIndex].getX(),
					PlayerHandler.players[c.playerIndex].getY(), 8)) {
				c.stopMovement();
			}
			if (c.getCombat().checkReqs()) {
				c.followId = c.playerIndex;
				c.mageFollow = true;
			}
			return true;
		}
		return false;
	}

}
