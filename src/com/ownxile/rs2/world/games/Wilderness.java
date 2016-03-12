package com.ownxile.rs2.world.games;

/**
 * @author Robbie
 * @description just a class to define wilderness boss configurations
 */
public class Wilderness {

	private static final int VETION = 4175;
	private static final int VENEATIS = 4173;
	private static final int SCORPIA = 4172;
	private static final int CALLISTO = 2552;
	private static final int SEA_TROLL_QUEEN = 3847;
	private static final int PENANCE_QUEEN = 5247;
	private static final int CHAOS_ELEMENTAL = 3200;

	/**
	 * @returns the amount of wilderness points required to fight a wilderness
	 *          boss
	 */
	public static int getNpcRequirement(int npcId) {
		return 0;
	}

	/**
	 * @returns the amount of wilderness points awarded for killing a certain
	 *          npc
	 */
	public static int pointsAwardedForKill(int npcId) {
		return 0;

	}

}
