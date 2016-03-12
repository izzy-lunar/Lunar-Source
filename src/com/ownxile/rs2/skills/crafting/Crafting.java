package com.ownxile.rs2.skills.crafting;

import com.ownxile.config.SkillConfig;
import com.ownxile.rs2.items.ItemAssistant;
import com.ownxile.rs2.player.Client;
import com.ownxile.util.Misc;

public class Crafting {

	public static enum amuletData {
		DIAMOND(1681, 1700), DRAGONSTONE(1683, 1702), EMERALD(1677, 1696), GOLD(
				1673, 1692), ONYX(6579, 6851), RUBY(1679, 1698), SAPPHIRE(1675,
				1694);
		private int amuletId, product;

		private amuletData(final int amuletId, final int product) {
			this.amuletId = amuletId;
			this.product = product;
		}

		public int getAmuletId() {
			return amuletId;
		}

		public int getProduct() {
			return product;
		}
	}

	public enum GemCrafting {

		DIAMOND(1617, 1601, 890, 43, 108, false), DRAGONSTONE(1631, 1615, 890,
				55, 138, false), EMERALD(1621, 1605, 889, 27, 68, false), JADE(
				1627, 1611, 891, 13, 20, true), ONYX(6571, 6573, 2717, 67, 168,
				false), OPAL(1625, 1609, 891, 1, 15, true), REDTOPAZ(1629,
				1613, 892, 16, 25, true), RUBY(1619, 1603, 887, 34, 85, false), SAPPHIRE(
				1623, 1607, 888, 1, 50, false);

		private boolean isSemiPrecious;
		private int uncutID, cutID, animation, levelReq, XP;

		private GemCrafting(int uncutID, int cutID, int animation,
				int levelReq, int XP, boolean semiPrecious) {
			this.uncutID = uncutID;
			this.cutID = cutID;
			this.animation = animation;
			this.levelReq = levelReq;
			this.XP = XP;
			this.isSemiPrecious = semiPrecious;
		}

		public int getAnim() {
			return animation;
		}

		public int getCut() {
			return cutID;
		}

		public int getReq() {
			return levelReq;
		}

		public int getUncut() {
			return uncutID;
		}

		public int getXP() {
			return XP;
		}

		public boolean isSemiPrecious() {
			return isSemiPrecious;
		}
	}

	public static enum jewelryData {

		AMULETS(new int[][] { { 2357, 1673, 8, 30 }, { 1607, 1675, 24, 65 },
				{ 1605, 1677, 31, 70 }, { 1603, 1679, 50, 85 },
				{ 1601, 1681, 70, 100 }, { 1615, 1683, 80, 150 },
				{ 6573, 6579, 90, 165 } }), NECKLACE(new int[][] {
				{ 2357, 1654, 6, 20 }, { 1607, 1656, 22, 55 },
				{ 1605, 1658, 29, 60 }, { 1603, 1660, 40, 75 },
				{ 1601, 1662, 56, 90 }, { 1615, 1664, 72, 105 },
				{ 6573, 6577, 82, 120 } }), RINGS(new int[][] {
				{ 2357, 1635, 5, 15 }, { 1607, 1637, 20, 40 },
				{ 1605, 1639, 27, 55 }, { 1603, 1641, 34, 70 },
				{ 1601, 1643, 43, 85 }, { 1615, 1645, 55, 100 },
				{ 6573, 6575, 67, 115 } });

		public int[][] item;

		private jewelryData(final int[][] item) {
			this.item = item;
		}
	}

	public enum LeatherCrafting {

		BLACKBODY(2509, 2503, 84, 258, 3), BLACKCHAPS(2509, 2497, 82, 172, 2), BLACKVAMBS(
				2509, 2491, 79, 86, 1),

		BLUEBODY(2505, 2499, 71, 210, 3), BLUECHAPS(2505, 2493, 68, 140, 2), BLUEVAMBS(
				2505, 2487, 66, 70, 1),

		GREENBODY(1745, 1135, 63, 186, 3), GREENCHAPS(1745, 1099, 60, 124, 2), GREENVAMBS(
				1745, 1065, 57, 62, 1),

		LEATHERBODY(1741, 1129, 18, 27, 1), LEATHERCHAPS(1741, 1095, 14, 25, 1), LEATHERVAMBS(
				1741, 1063, 11, 22, 1),

		REDBODY(2507, 2501, 77, 234, 3), REDCHAPS(2507, 2495, 75, 156, 2), REDVAMBS(
				2507, 2489, 73, 78, 1);

		private int leatherId, outcome, reqLevel, XP, reqAmt;

		private LeatherCrafting(int leatherId, int outcome, int reqLevel,
				int XP, int reqAmt) {
			this.leatherId = leatherId;
			this.outcome = outcome;
			this.reqLevel = reqLevel;
			this.XP = XP;
			this.reqAmt = reqAmt;
		}

		public int getLeather() {
			return leatherId;
		}

		public int getOutcome() {
			return outcome;
		}

		public int getReqAmt() {
			return reqAmt;
		}

		public int getReqLevel() {
			return reqLevel;
		}

		public int getXP() {
			return XP;
		}
	}

	// TODO tanning, snakeskin,
	private Client c;

	int[][] leathers = { { 1741, 1095, 1063, 1129 },
			{ 1745, 1099, 1065, 1135 }, { 2505, 2493, 2487, 2499 },
			{ 2507, 2495, 2489, 2501 }, { 2509, 2497, 2491, 2503 } };

	public Crafting(Client c) {
		this.c = c;
	}

	private void craftLeather(int id) {
		LeatherCrafting lea = forLeather(id);
		if (lea != null) {
			if (c.playerLevel[c.playerCrafting] >= lea.getReqLevel()) {
				if (c.getItems().playerHasItem(lea.getLeather(),
						lea.getReqAmt())) {
					c.startAnimation(1249);
					c.getItems().deleteItem(lea.getLeather(), lea.getReqAmt());
					c.getItems().deleteItem(1747, 1);
					c.getItems().addItem(lea.getOutcome(), 1);
					c.getFunction()
							.addSkillXP(
									(lea.getXP() * SkillConfig.CRAFTING_EXPERIENCE) * 3,
									c.playerCrafting);
					c.getItems();
					c.sendMessage("You craft the leather into "
							+ ItemAssistant.getItemName(lea.getOutcome()) + ".");
					resetCrafting();
				} else {
					c.sendMessage("You do not have enough items to craft this item.");
				}
			} else {
				c.sendMessage("You need a crafting level of "
						+ lea.getReqLevel() + " to craft this item.");
			}
			c.getFunction().removeAllWindows();
		}
	}

	private void cutGem(int id) {
		GemCrafting gem = forGem(id);
		if (gem != null) {
			if (c.getItems().playerHasItem(gem.getUncut(), 1)) {
				if (c.playerLevel[c.playerCrafting] >= gem.getReq()) {
					c.getItems().deleteItem(gem.getUncut(), 1);
					if (gem.isSemiPrecious() && Misc.random(100) == 37) {
						c.sendMessage("You accidently crush the gem!");
						c.getItems().addItem(1633, 1);
					} else {
						c.getItems().addItem(gem.getCut(), 1);
						c.getFunction().addSkillXP(
								gem.getXP() * SkillConfig.CRAFTING_EXPERIENCE,
								c.playerCrafting);
					}
					c.startAnimation(gem.getAnim());
				} else {
					c.sendMessage("You need a crafting level of "
							+ gem.getReq() + " to cut this gem.");
				}
			}
		}
	}

	private GemCrafting forGem(int id) {
		for (GemCrafting g : GemCrafting.values()) {
			if (g.getUncut() == id) {
				return g;
			}
		}
		return null;
	}

	private LeatherCrafting forLeather(int id) {
		for (LeatherCrafting lc : LeatherCrafting.values()) {
			if (lc.getOutcome() == id) {
				return lc;
			}
		}
		return null;
	}

	public void handleChisel(int id1, int id2) {
		cutGem(id1 == 1755 ? id2 : id1);
	}

	public void handleCraftingClick(int clickId) {
		switch (clickId) {
		case 34185: // Vambs
			switch (c.hideId) {
			case 1741:
				craftLeather(1063); // Leather vambs
				break;
			case 1745:
				craftLeather(1065); // Green d'hide vambs
				break;
			case 2505:
				craftLeather(2487); // Blue d'hide vambs
				break;
			case 2507:
				craftLeather(2489); // Red d'hide vambs
				break;
			case 2509:
				craftLeather(2491); // Black d'hide vambs
				break;
			}
			break;
		case 34189:
			switch (c.hideId) {
			case 1741:
				craftLeather(1095); // Leather chaps
				break;
			case 1745:
				craftLeather(1099); // Green d'hide chaps
				break;
			case 2505:
				craftLeather(2493); // Blue d'hide chaps
				break;
			case 2507:
				craftLeather(2495); // Red d'hide chaps
				break;
			case 2509:
				craftLeather(2497); // Black d'hide chaps
				break;
			}
			break;
		case 34193:
			switch (c.hideId) {
			case 1741:
				craftLeather(1129); // Leather body
				break;
			case 1745:
				craftLeather(1135); // Green d'hide body
				break;
			case 2505:
				craftLeather(2499); // Blue d'hide body
				break;
			case 2507:
				craftLeather(2501); // Red d'hide body
				break;
			case 2509:
				craftLeather(2503); // Black d'hide body
				break;
			}
			break;
		}
	}

	public void handleLeather(int item1, int item2) {
		openLeather(item1 == 1733 ? item2 : item1);
	}

	private void openLeather(int hide) {
		for (int[] leather : leathers) {
			if (leather[0] == hide) {
				c.getFunction().sendFrame164(8880); // leather
				c.getFunction().sendFrame126("What would you like to make?",
						8879);
				c.getFunction().sendFrame246(8884, 250, leather[1]); // middle
				c.getFunction().sendFrame246(8883, 250, leather[2]); // left
																		// picture
				c.getFunction().sendFrame246(8885, 250, leather[3]); // right
																		// pic
				c.getFunction().sendFrame126("Vambs", 8889);
				c.getFunction().sendFrame126("Chaps", 8893);
				c.getFunction().sendFrame126("Body", 8897);
			}
		}
		c.craftingLeather = true;
		c.hideId = hide;
	}

	public void resetCrafting() {
		c.craftingLeather = false;
		c.hideId = -1;
	}
}