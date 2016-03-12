package com.ownxile.rs2.skills.slayer;

import com.ownxile.config.WildConfig;
import com.ownxile.util.Misc;

/**
 * @author Robbie
 *
 */
public enum SlayerTask {

	ABYSSAL_DEMON(new int[] { 1615 }, 85, 3), AL_KHARID_WARRIOR(
			new int[] { 18 }, 1, 1),

	BLACK_DEMON(new int[] { 84, 677 }, 1, 3), BLACK_DRAGON(
			new int[] { 50, 54 }, 1, 4), BARBARIAN(
			new int[] { 3246, 3250, 3261 }, 1, 0), GRIZZLY_BEAR(
			new int[] { 105 }, 1, 0), DARK_WIZARD(new int[] { 172 }, 1, 0), BLOODVELD(
			new int[] { 1618 }, 55, 2), BLUE_DRAGON(new int[] { 55 }, 1, 3), BRONZE_DRAGON(
			new int[] { 1590 }, 1, 3), CHAOS_DRUID(new int[] { 181, 193 }, 1, 0), CRAWLING_HAND(
			new int[] { 1648 }, 5, 1), DAGANNOTH(new int[] { 1338, 1339, 1340,
			1341, 1342, 1343, 1344, 1345, 1346, 1347 }, 1, 3), DARK_BEAST(
			new int[] { 2783 }, 90, 4), MITHRIL_DRAGON(new int[] { 5363 }, 88,
			4), TORMENTED_DEMONS(new int[] { 2349 }, 95, 4), SKELETAL_WYVERN(
			new int[] { 3068 }, 93, 4), GORAK(new int[] { 6218 }, 70, 3), DUSTDEVIL(
			new int[] { 1624 }, 65, 3), FIRE_GIANT(new int[] { 110, 1582, 1583,
			1584, 1585, 1586 }, 1, 2), GARGOYLE(new int[] { 1610 }, 75, 3), GIANT_BAT(
			new int[] { 78 }, 1, 1), GREEN_DRAGON(new int[] { 941 }, 1, 2), HELLHOUND(
			new int[] { 49, 6210 }, 1, 2), HILL_GIANTS(new int[] { 117 }, 1, 0), INFERNAL_MAGE(
			new int[] { 1643 }, 45, 2), IRON_DRAGON(new int[] { 1591 }, 1, 4), KALPHITE_SOLDIER(
			new int[] { 1154 }, 1, 2), KALPHITE_WORKER(
			new int[] { 1153, 1156 }, 1, 1), LESSER_DEMON(new int[] { 82, 4694,
			4695, 4696, 4697, }, 1, 2), MAGIC_AXE(new int[] { 127 }, 1, 1), MOSS_GIANT(
			new int[] { 112 }, 1, 1), NECHRYAEL(new int[] { 1613 }, 80, 3), ROCK_CRAB(
			new int[] { 1265, 1267 }, 1, 0), ROGUE(new int[] { 187 }, 1, 1), SKELETON(
			new int[] { 93, 94 }, 1, 1), STEEL_DRAGON(new int[] { 1592 }, 80, 4), RED_DRAGON(
			new int[] { 53 }, 1, 3), KALPHITE_GUARDIAN(
			new int[] { 1155, 1157 }, 1, 3), BABY_DRAGON(new int[] { 51, 52,
			1589 }, 1, 1), WHITE_KNIGHT(new int[] { 19, 3349, 3350, 3348 }, 1,
			1), GIANT_ROCK_CRABS(new int[] { 2885 }, 1, 3), ELF(new int[] {
			2360, 2361 }, 1, 3), GENERAL_GRAARDOR(new int[] { 6260 }, 1, 5), KALPHITE_QUEEN(
			new int[] { 1158 }, 1, 5), KREE_ARRA(new int[] { 6222 }, 1, 5), SEA_TROLL_QUEEN(
			new int[] { 3847 }, 1, 5), TZ_TOK_JAD(new int[] { 2745 }, 1, 5), CHAOS_ELEMENTAL(
			new int[] { 3200 }, 1, 5), CALLISTO(new int[] { 2552 }, 1, 5), CORPOREAL_beast(
			new int[] { 5422 }, 1, 5), INADAQUECY(new int[] { 5902 }, 1, 5), KING_BLACK_DRAGON(
			new int[] { 50 }, 1, 5), DAGANNOTH_KINGS(new int[] { 2881, 2882,
			2883 }, 1, 5), VETION(new int[] { WildConfig.VETION }, 1, 5), SCORPIA(
			new int[] { WildConfig.SCORPIA }, 1, 5), VENEATIS(
			new int[] { WildConfig.VENEATIS }, 1, 5), SMOKE_DEVIL(
			new int[] { 1625 }, 75, 3);

	int difficulty;

	int[] npcs;

	int req;

	private SlayerTask(int npcs[], int req, int difficulty) {
		this.req = req;
		this.difficulty = difficulty;
		this.npcs = npcs;
	}

	public int getDifficulty() {
		return difficulty;
	}

	public int[] getNpcs() {
		return npcs;
	}

	public int getReq() {
		return req;
	}

	public boolean isTask(int npc) {
		for (int i : npcs) {
			if (i == npc)
				return true;
		}
		return false;
	}

	public String toFormattedString() {
		return Misc.optimizeText(toString().toLowerCase().replaceAll("_", " "));
	}
}