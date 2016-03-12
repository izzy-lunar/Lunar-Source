package com.ownxile.rs2.skills.thieving;

import com.ownxile.config.SkillConfig;
import com.ownxile.core.World;
import com.ownxile.core.task.Task;
import com.ownxile.rs2.npcs.NPC;
import com.ownxile.rs2.player.Client;
import com.ownxile.util.Misc;

public class Thieving {

	private enum NPCData {
		ARDOUGNE_KNIGHT(55, 85, 1500, 3), BANDIT(53, 70, 1400, 3), ELF(85, 343,
				30000, 7), FARMER(10, 15, 350, 1), GNOME(75, 240, 15000, 4), GUARD(
				40, 47, 1000, 2), HAM_MEMBER(18, 20, 500, 1), HERO(80, 273,
				10000, 5), HUMANOID(1, 8, 200, 1), MASTER_FARMER(38, 44, 950, 2), PALADIN(
				70, 152, 2000, 4), ROGUE(32, 30, 900, 2), WARRIOR(25, 26, 750,
				1);

		private int levelReq, XP, reward, damage;

		private NPCData(int levelReq, int XP, int reward, int damage) {
			this.levelReq = levelReq;
			this.XP = XP;
			this.reward = reward;
			this.damage = damage;
		}

		public int getDamage() {
			return damage;
		}

		public int getReq() {
			return levelReq;
		}

		public int getReward() {
			return reward;
		}

		public int getXP() {
			return XP;
		}
	}

	private enum StallData {
		BAKER(11730, new int[] { 1897, 2307, 2309 }, 30, 1, 1), 
		FUR(11732, new int[] { 948 }, 50, 25, 1), 
		SILK(11729, new int[] { 950 }, 80, 50, 1), 
		SPICE(11733, new int[] { 2007 }, 150, 75, 1), 
		GEM(11731, new int[] { 1623, 1621, 1619, 1617, 1631 }, 250, 90, 1);

		private int objId, xp, level, amount;
		private int[] reward;

		private StallData(int objId, int[] id, int xp, int level, int amount) {
			this.objId = objId;
			this.reward = id;
			this.xp = xp;
			this.level = level;
			this.amount = amount;
		}

		public int getLevel() {
			return level;
		}

		public int getObjId() {
			return objId;
		}

		public int[] getReward() {
			return reward;
		}

		public int getXP() {
			return xp;
		}
	}

	public static StallData forStall(int id) {
		for (StallData sd : StallData.values()) {
			if (sd.getObjId() == id) {
				return sd;
			}
		}
		return null;
	}

	public static int getRandom(int stall) {
		StallData sd = forStall(stall);
		return sd.getReward()[(int) (sd.getReward().length * Math.random())];
	}

	private Client player;

	public Thieving(Client player) {
		this.player = player;
	}

	public NPCData getDataForNpc(int npcId) {
		switch (npcId) {
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
		case 6:
		case 24:
		case 25:
		case 351:
		case 352:
		case 353:
		case 354:
			return NPCData.HUMANOID;
		case 9:
		case 35:
		case 32:
		case 34:
			return NPCData.GUARD;
		case 1714:
		case 1715:
		case 1716:
		case 1717:
			return NPCData.HAM_MEMBER;
		case 187:
			return NPCData.ROGUE;
		case 18:
		case 15:
			return NPCData.WARRIOR;
		case 20:
		case 365:
			return NPCData.PALADIN;
		case 21:
			return NPCData.HERO;
		case 23:
		case 26:
			return NPCData.ARDOUGNE_KNIGHT;
		case 1926:
			return NPCData.BANDIT;
		case 2363:
		case 2364:
		case 2365:
		case 2367:
			return NPCData.ELF;
		}

		return null;
	}

	private int getEquipmentBonus() {
		return player.playerEquipment[player.playerHands] == 10075 ? 100 : 3;
	}

	public boolean randomFail(int levelReq) {
		return player.playerLevel[player.playerThieving] - levelReq
				+ getEquipmentBonus() < Misc.random(levelReq);
	}

	public boolean stealFromNPC(int id) {
		if (System.currentTimeMillis() - player.lastThieve < 2000) {
			return true;
		}
		final NPCData v = getDataForNpc(id);
		if (v != null) {
			if (player.playerLevel[player.playerThieving] >= v.getReq()) {
				String npcName = World.getNpcHandler().getNPCName(id);
				player.startAnimation(881);
				if (!randomFail(v.getReq())) {
					player.getItems().addItem(995, v.getReward());
					player.getFunction().addSkillXP(
							v.getXP() * SkillConfig.THIEVING_EXPERIENCE,
							player.playerThieving);
					player.lastThieve = System.currentTimeMillis();
					player.sendMessage("You pickpocket the " + npcName + ".");
				} else {
					player.lastThieve = System.currentTimeMillis() + 2000;
					World.getSynchronizedTaskScheduler().schedule(
							new Task(1, false) {
								@Override
								protected void execute() {
									player.gfx100(254);
									player.getHit(v.getDamage());
									stop();
								}
							});
					NPC npc = player.getFunction().getNpc();
					if (npc != null) {
						npc.forceChat("What do you think you're doing?");
					}
					player.sendMessage("You fail to pickpocket the " + npcName
							+ ".");
				}
			} else {
				player.sendMessage("You need a thieving level of " + v.getReq()
						+ " to pickpocket this NPC.");
			}
			return true;
		}
		return false;
	}

	public void stealFromStall(int stall) {
		if (System.currentTimeMillis() - player.lastThieve > 2000) {
			StallData sd = forStall(stall);
			if (sd != null) {
				if (!player.hasInventorySpace(1)) {
					player.sendMessage("You do not have enough space in your inventory to steal anything.");
					return;
				}
				if (player.playerLevel[player.playerThieving] >= sd.getLevel()) {
					player.getItems().addItem(getRandom(stall), sd.amount);
					player.startAnimation(832);
					player.getFunction().addSkillXP(
							sd.getXP() * SkillConfig.THIEVING_EXPERIENCE,
							player.playerThieving);
					player.lastThieve = System.currentTimeMillis();
					if (player.objectId == 24875)
						player.sendMessage("You take some coins.");
					else
						player.sendMessage("You steal from the stall.");
				} else {
					player.sendMessage("You need a thieving level of at least "
							+ sd.getLevel() + " to steal from this stall.");
				}
			}
		}
	}
}