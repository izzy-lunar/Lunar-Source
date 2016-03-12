package com.ownxile.rs2.packets.clicking;

import com.ownxile.config.GameConfig;
import com.ownxile.rs2.combat.range.RangeConfiguration;
import com.ownxile.rs2.content.click.NpcClickReaction;
import com.ownxile.rs2.items.ItemAssistant;
import com.ownxile.rs2.npcs.NPCHandler;
import com.ownxile.rs2.packets.Packet;
import com.ownxile.rs2.player.Client;

/**
 * Click NPC
 */
public class ClickNPC implements Packet {
	public static final int ATTACK_NPC = 72, MAGE_NPC = 131, FIRST_CLICK = 155,
			SECOND_CLICK = 17, THIRD_CLICK = 21;

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		c.npcIndex = 0;
		c.npcClickIndex = 0;
		c.playerIndex = 0;
		c.clickNpcType = 0;
		c.faceUpdate(0);
		c.getFunction().resetFollow();
		if (System.currentTimeMillis() - c.lastClick < 600) {
			return;
		}
		c.lastClick = System.currentTimeMillis();
		switch (packetType) {

		/**
		 * Attack npc melee or range
		 **/
		case ATTACK_NPC:
			if (!c.mageAllowed) {
				c.mageAllowed = true;
				c.sendMessage("I can't reach that.");
				break;
			}
			c.npcIndex = c.getInStream().readUnsignedWordA();
			c.getFunction().clickedNpcIndex = c.npcIndex;
			if (NPCHandler.npcs[c.npcIndex] == null) {
				c.npcIndex = 0;
				break;
			}
			if (NPCHandler.npcs[c.npcIndex].MaxHP == 0) {
				c.npcIndex = 0;
				break;
			}
			if (NPCHandler.npcs[c.npcIndex] == null) {
				break;
			}
			if (c.inTrade) {
				break;
			}
			if (c.autocastId > 0) {
				c.autocasting = true;
			}
			if (!c.autocasting && c.spellId > 0) {
				c.spellId = 0;
			}
			c.faceUpdate(c.npcIndex);
			c.usingMagic = false;
			boolean usingBow = false;
			boolean usingOtherRangeWeapons = false;
			boolean usingArrows = false;
			final boolean usingCross = RangeConfiguration.usingCrossbow(c);
			if (c.playerEquipment[c.playerWeapon] >= 4214
					&& c.playerEquipment[c.playerWeapon] <= 4223) {
				usingBow = true;
			}
			for (final int bowId : RangeConfiguration.BOWS) {
				if (c.playerEquipment[c.playerWeapon] == bowId) {
					usingBow = true;
					for (final int arrowId : RangeConfiguration.ARROWS) {
						if (c.playerEquipment[c.playerArrows] == arrowId) {
							usingArrows = true;
						}
					}
				}
			}
			for (final int otherRangeId : RangeConfiguration.OTHER_RANGE_WEAPONS) {
				if (c.playerEquipment[c.playerWeapon] == otherRangeId) {
					usingOtherRangeWeapons = true;
				}
			}
			if ((usingBow || c.autocasting)
					&& c.goodDistance(c.getX(), c.getY(),
							NPCHandler.npcs[c.npcIndex].getX(),
							NPCHandler.npcs[c.npcIndex].getY(), 7)) {
				c.stopMovement();
			}

			if (usingOtherRangeWeapons
					&& c.goodDistance(c.getX(), c.getY(),
							NPCHandler.npcs[c.npcIndex].getX(),
							NPCHandler.npcs[c.npcIndex].getY(), 4)) {
				c.stopMovement();
			}
			if (!usingCross && !usingArrows && usingBow
					&& c.playerEquipment[c.playerWeapon] < 4212
					&& c.playerEquipment[c.playerWeapon] > 4223 && !usingCross) {
				c.sendMessage("You have run out of arrows!");
				break;
			}
			if (RangeConfiguration.correctBowAndArrows(c) < c.playerEquipment[c.playerArrows]
					&& GameConfig.CORRECT_ARROWS
					&& usingBow
					&& !RangeConfiguration.usingCrystalBow(c)
					&& !RangeConfiguration.usingCrossbow(c)) {
				c.getItems();
				c.getItems();
				c.sendMessage("You can't use "
						+ ItemAssistant.getItemName(
								c.playerEquipment[c.playerArrows])
								.toLowerCase()
						+ "s with a "
						+ ItemAssistant.getItemName(
								c.playerEquipment[c.playerWeapon])
								.toLowerCase() + ".");
				c.stopMovement();
				c.getCombat().resetPlayerAttack();
				return;
			}
			if (RangeConfiguration.usingCrossbow(c)
					&& !c.getCombat().properBolts()) {
				c.sendMessage("You must use bolts with a crossbow.");
				c.stopMovement();
				c.getCombat().resetPlayerAttack();
				return;
			}

			if (c.followId > 0 || c.followId3 > 0) {
				c.getFunction().resetFollow();
			}
			if (c.attackTimer <= 0) {
				c.getCombat().attackNpc(c.npcIndex);
				c.attackTimer++;
			}

			break;

		/**
		 * Attack npc with magic
		 **/
		case MAGE_NPC:
			if (!c.mageAllowed) {
				c.mageAllowed = true;
				c.sendMessage("I can't reach that.");
				break;
			}
			// c.usingSpecial = false;
			// c.getItems().updateSpecialBar();

			c.npcIndex = c.getInStream().readSignedWordBigEndianA();
			final int castingSpellId = c.getInStream().readSignedWordA();
			c.usingMagic = false;

			if (NPCHandler.npcs[c.npcIndex] == null) {
				break;
			}

			if (NPCHandler.npcs[c.npcIndex].MaxHP == 0
					|| NPCHandler.npcs[c.npcIndex].npcType == 944) {
				c.sendMessage("You can't attack this npc.");
				break;
			}

			for (int i = 0; i < c.MAGIC_SPELLS.length; i++) {
				if (castingSpellId == c.MAGIC_SPELLS[i][0]) {
					c.spellId = i;
					c.usingMagic = true;
					break;
				}
			}
			/*
			 * if(!c.getCombat().checkMagicReqs(c.spellId)) { c.stopMovement();
			 * break; }
			 */

			if (c.autocasting) {
				c.autocasting = false;
			}

			if (c.usingMagic) {
				if (c.goodDistance(c.getX(), c.getY(),
						NPCHandler.npcs[c.npcIndex].getX(),
						NPCHandler.npcs[c.npcIndex].getY(), 6)) {
					c.stopMovement();
				}
				if (c.attackTimer <= 0) {
					c.getCombat().attackNpc(c.npcIndex);
					c.attackTimer++;
				}
			}

			break;

		case FIRST_CLICK:
			c.npcClickIndex = c.inStream.readSignedWordBigEndian();
			c.npcType = NPCHandler.npcs[c.npcClickIndex].npcType;
			if (c.goodDistance(NPCHandler.npcs[c.npcClickIndex].getX(),
					NPCHandler.npcs[c.npcClickIndex].getY(), c.getX(),
					c.getY(), 1)) {
				c.turnPlayerTo(NPCHandler.npcs[c.npcClickIndex].getX(),
						NPCHandler.npcs[c.npcClickIndex].getY());
				NPCHandler.npcs[c.npcClickIndex].facePlayer(c.playerId);
				NPCHandler.npcs[c.npcClickIndex].recentlyInteracted = System
						.currentTimeMillis();

				NpcClickReaction.executeClick(c, NpcClickReaction.FIRST_CLICK,
						c.npcType);
			} else {
				c.clickNpcType = 1;
			}
			break;

		case SECOND_CLICK:
			c.npcClickIndex = c.inStream.readUnsignedWordBigEndianA();
			c.npcType = NPCHandler.npcs[c.npcClickIndex].npcType;
			if (c.goodDistance(NPCHandler.npcs[c.npcClickIndex].getX(),
					NPCHandler.npcs[c.npcClickIndex].getY(), c.getX(),
					c.getY(), 1)) {
				c.turnPlayerTo(NPCHandler.npcs[c.npcClickIndex].getX(),
						NPCHandler.npcs[c.npcClickIndex].getY());
				NPCHandler.npcs[c.npcClickIndex].recentlyInteracted = System
						.currentTimeMillis();
				NPCHandler.npcs[c.npcClickIndex].facePlayer(c.playerId);
				NpcClickReaction.executeClick(c, NpcClickReaction.SECOND_CLICK,
						c.npcType);
			} else {
				c.clickNpcType = 2;
			}
			break;

		case THIRD_CLICK:
			c.npcClickIndex = c.inStream.readSignedWord();
			c.npcType = NPCHandler.npcs[c.npcClickIndex].npcType;
			if (c.goodDistance(NPCHandler.npcs[c.npcClickIndex].getX(),
					NPCHandler.npcs[c.npcClickIndex].getY(), c.getX(),
					c.getY(), 1)) {
				c.turnPlayerTo(NPCHandler.npcs[c.npcClickIndex].getX(),
						NPCHandler.npcs[c.npcClickIndex].getY());
				NPCHandler.npcs[c.npcClickIndex].facePlayer(c.playerId);
				NPCHandler.npcs[c.npcClickIndex].recentlyInteracted = System
						.currentTimeMillis();
				NpcClickReaction.executeClick(c, NpcClickReaction.THIRD_CLICK,
						c.npcType);
			} else {
				c.clickNpcType = 3;
			}
			break;
		}

	}
}
