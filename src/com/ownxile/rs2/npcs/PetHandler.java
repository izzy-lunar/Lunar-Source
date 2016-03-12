package com.ownxile.rs2.npcs;

import com.ownxile.core.World;
import com.ownxile.rs2.player.Client;
import com.ownxile.rs2.player.PlayerSave;
import com.ownxile.rs2.world.region.RegionManager;

public class PetHandler {

	public static enum Pets {

		HELL_PUPPY(13247, 964),
		ABYSSAL_ORPHAN(13262, 5883), 
		GRAARDOR(12650, 6632), 
		KREE(12649, 6643), 
		ZILLY(12651, 6633), 
		TSUT(12652, 6634), 
		PRIME(12644, 6627), 
		REX(12645, 6630), 
		SUPREME(12643, 6626), 
		CHAOS(11995, 2055), 
		KBD(12653, 6636), 
		KRAKEN(12655, 6640),   
		CALLISTO(13178, 5558), 
		ZULRAH_GREEN(12921, 2127), 
		ZULRAH_RED(12939, 2128), 
		ZULRAH_BLUE(12940, 2129), 
		VETION(13179, 5559), 
		VENENATIS(13177, 5557),
		SCORPIA(13181, 5561);

		private int itemId, npcId;

		private Pets(int itemId, int npcId) {
			this.itemId = itemId;
			this.setNpcId(npcId);
		}

		public int getNpcId() {
			return npcId;
		}

		public void setNpcId(int npcId) {
			this.npcId = npcId;
		}
	}

	public static void interactBossPet(Client c) {
		if (NPCHandler.npcs[c.lastClickedNpcId].summonedBy != c.getId()) {
			c.boxMessage("This is not your pet.");
			return;
		}
		c.dialogueOption("Abandon", 86, "Insure", 1919582705);
	}

	public static boolean canCatSpeak(Client c) {
		return c.playerEquipment[c.playerAmulet] == 4677;
	}

	public static Pets forItem(int id) {
		for (Pets t : Pets.values()) {
			if (t.itemId == id) {
				return t;
			}
		}
		return null;
	}

	public static Pets forNpc(int id) {
		for (Pets t : Pets.values()) {
			if (t.getNpcId() == id) {
				return t;
			}
		}
		return null;
	}

	public static int getItemIdForNpcId(int npcId) {
		return forNpc(npcId).itemId;
	}

	public static void interactWith(Client c, int npcId) {
		if (npcId != 2549)
			c.startChat(905510);
		else
			c.playerChat("I don't think it speaks english...");
	}

	public static boolean isPetNpc(int id) {
		for (Pets t : Pets.values()) {
			if (t.getNpcId() == id) {
				return true;
			}
		}
		return false;
	}

	public static boolean pickupPet(Client c, int npcId) {
		Pets pets = forNpc(npcId);
		if (pets == null) {
			return false;
		}
		if (NPCHandler.npcs[c.rememberNpcIndex].summonedBy != c.getId()
				|| npcId != c.summonId) {
			c.sendMessage("This is not your pet.");
			return false;
		}
		if (c.getItems().freeSlots() < 1) {
			c.sendMessage("You do not have enough inventory space to do this.");
			return false;
		}
		int itemId = pets.itemId;
		NPCHandler.npcs[c.rememberNpcIndex].absX = 0;
		NPCHandler.npcs[c.rememberNpcIndex].absY = 0;
		NPCHandler.npcs[c.rememberNpcIndex] = null;
		c.startAnimation(827);
		c.getItems().addItem(itemId, 1);
		c.summonId = -1;
		c.hasNpc = false;
		c.sendMessage("You pick up your pet.");

		return true;
	}

	public static boolean spawnPet(Client c, int itemId, int slot,
			boolean ignore) {
		Pets pets = forItem(itemId);
		if (pets != null) {
			int npcId = pets.getNpcId();
			if (c.hasNpc && !ignore) {
				c.boxMessage("You already have a follower.");
				return true;
			}
			int offsetX = 0;
			int offsetY = 0;
			c.summonId = npcId;
			if (RegionManager
					.getClipping(c.getX() - 1, c.getY(), c.absZ, -1, 0)) {
				offsetX = -1;
			} else if (RegionManager.getClipping(c.getX() + 1, c.getY(),
					c.absZ, 1, 0)) {
				offsetX = 1;
			} else if (RegionManager.getClipping(c.getX(), c.getY() - 1,
					c.absZ, 0, -1)) {
				offsetY = -1;
			} else if (RegionManager.getClipping(c.getX(), c.getY() + 1,
					c.absZ, 0, 1)) {
				offsetY = 1;
			}
			int index = World.getNpcHandler().spawnNpc3(c, npcId,
					c.absX + offsetX, c.absY + offsetY, c.absZ, 0, 120, 25,
					200, 200, true, false, true);
			if (!ignore) {
				c.getItems().deleteItem(itemId, slot, c.playerItemsN[slot]);
				c.startAnimation(827);
				c.hasNpc = true;
				PlayerSave.saveGame(c);
			}
			NPCHandler.npcs[index].summonedBy = c.getId();
			NPCHandler.npcs[index].turnNpc(c.absX, c.absY);
			NPCHandler.npcs[index].facePlayer(c.playerId);
			NPCHandler.npcs[index].forceChat(getNpcTalk(npcId));
			return true;
		}
		return false;

	}

	public static String getNpcTalk(int id) {
		switch (id) {
		case 4001:
			return "RRARARRRR";
		case 98:
			return "Woof!";
		case 4020:
		case 2549:
			return "Erk!";
		case 5883:
			return "eek";
		case 964:
			return "Grrrr";
		}
		return "yipeeee!";
	}

	public static boolean killPet(Client c, boolean shoo) {
		if (shoo) {
			NPCHandler.npcs[c.summonIndex].absX = 0;
			NPCHandler.npcs[c.summonIndex].absY = 0;
			NPCHandler.npcs[c.summonIndex].absZ = 0;
			c.startAnimation(863);
			if (NPCHandler.npcs[c.rememberNpcIndex].summonedBy != c.getId()) {
				c.sendMessage("This is not your pet.");
				return false;
			}
		} else
			NPCHandler.npcs[c.summonIndex] = null;
		c.hasNpc = false;
		if (shoo)
			c.boxMessage("You shoo away your pet.", "That's not very nice!");
		else
			c.sendMessage("Your pet has run away, it probably went back to the zoo.");
		c.summonId = -1;
		return true;
	}

	public static boolean summonPet(Client c, int npcId) {
		if (c.hasNpc) {
			return false;
		}
		if (isPetNpc(c.summonId)) {
			spawnPet(c, getItemIdForNpcId(c.summonId), 0, false);
			return true;
		}
		int offsetX = 0;
		int offsetY = 0;
		if (RegionManager.getClipping(c.getX() - 1, c.getY(), c.absZ, -1, 0)) {
			offsetX = -1;
		} else if (RegionManager.getClipping(c.getX() + 1, c.getY(), c.absZ, 1,
				0)) {
			offsetX = 1;
		} else if (RegionManager.getClipping(c.getX(), c.getY() - 1, c.absZ, 0,
				-1)) {
			offsetY = -1;
		} else if (RegionManager.getClipping(c.getX(), c.getY() + 1, c.absZ, 0,
				1)) {
			offsetY = 1;
		}
		int index = World.getNpcHandler().spawnNpc3(c, npcId, c.absX + offsetX,
				c.absY + offsetY, c.absZ, 0, 120, 25, 200, 200, true, false,
				true);
		c.hasNpc = true;
		c.summonId = npcId;
		c.summonIndex = index;
		PlayerSave.saveGame(c);
		NPC pet = NPCHandler.npcs[index];
		pet.summonedBy = c.getId();
		pet.turnNpc(c.absX, c.absY);
		pet.facePlayer(c.playerId);
		// pet.forceChat(getNpcTalk(c.summonId));
		return true;

	}

	public static void talkTo(Client c, int npcId) {
		c.startChat(905500);
	}

}