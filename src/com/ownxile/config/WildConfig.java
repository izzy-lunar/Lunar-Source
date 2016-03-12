package com.ownxile.config;

/**
 * @author Robbie
 * @description just a class to define wilderness boss configurations
 */
public class WildConfig {

	public static final int MAX_RATING = 3200;

	public static final int VETION = 4175;
	public static final int VENEATIS = 4173;
	public static final int SCORPIA = 4172;
	public static final int CALLISTO = 2552;
	public static final int SEA_TROLL_QUEEN = 3847;
	public static final int PENANCE_QUEEN = 5247;
	public static final int CHAOS_ELEMENTAL = 3200;

	/**
	 * @returns the amount of wilderness points required to fight a wilderness
	 *          boss
	 */
	public static int getNpcRequirement(int npcId) {
		switch (npcId) {
		case VETION:
			return 10;
		case VENEATIS:
			return 10;
		case SCORPIA:
			return 10;
		case CALLISTO:
			return 10;
		case SEA_TROLL_QUEEN:
			return 200;
		case CHAOS_ELEMENTAL:
			return 100;
		case PENANCE_QUEEN:
			return 10;
		}
		return 0;
	}

	/**
	 * @returns the amount of wilderness points awarded for killing a certain
	 *          npc
	 */
	public static int ratingAwardedForNPCKill(int npcId) {
		switch (npcId) {
		
		case 49://hellhound
		case 941://green dragon, rogue, magic axe, giant bat, lesser demon
		
			return 1;
		case VETION:
			return 20;
		case VENEATIS:
			return 20;
		case SCORPIA:
			return 20;
		case CALLISTO:
			return 15;
		case SEA_TROLL_QUEEN:
			return 15;
		case CHAOS_ELEMENTAL:
			return 25;
		case PENANCE_QUEEN:
			return 40;
		}
		return 0;

	}

	public static int getItemRatingCost(int itemID) {
		switch (itemID) {
		case 8013:
		case 11230:
		case 9244:
		case 9245:
		case 4740:
		case 3024:
		case 561:
		case 3144:
			return 1;
		case 13879:
		case 13883:
		case 11326:
			return 2;
		case 8844:
			return 250;
		case 8285:
			return 1500;
		case 11777:
			return 2250;
		case 11924:
		case 11926:
		case 1478:
		case 88:
			return 750;
		case 13899:
		case 13902:
		case 8294:
			return 1500;
		case 962:
		case 11863:
		case 11694:
			return 3000;
		case 1029:
		case 13905:
			return 2500;
		case 13887:
		case 13893:
		case 13890:
		case 13884:
			return 3000;
		case 12928:
		case 11795:
			return 1000;
		case 12855:
		case 12856:
			return WildConfig.MAX_RATING;
		case 2581:
		case 2577:
			return 1750;
		case 9470:
			return 2000;
		}
		return 500;
	}

	public static int calculateRatingForKill(int opponentRating) {
		if (opponentRating < 15)
			return 1;
		return 1 + opponentRating / 15;
	}

	public static int getRatingToRemove(int currentRating) {
		if (currentRating < 10)
			return 1;
		;
		return currentRating / 10;
	}

}
