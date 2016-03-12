package com.ownxile.rs2.skills.cooking;

public enum CookingData {

	ANCHOVIES(321, 319, 323, 5, 45, 34, "anchovies"), BASS(363, 365, 367, 40,
			80, 60, "bass"), BREAD(2307, 2309, 2311, 1, 30, 33, "bread"), COD(
			341, 339, 343, 35, 70, 50, "cod"), HERRING(345, 347, 343, 1, 45,
			34, "herring"), LOBSTER(377, 379, 381, 40, 121, 74, "lobster"), MANTA_RAY(
			389, 391, 393, 91, 369, 99, "manta ray"), MONKFISH(7944, 7946,
			7948, 62, 151, 91, "monkfish"), PITTA(1863, 1865, 2311, 1, 30, 33,
			"pitta"), SALMON(331, 329, 343, 30, 91, 58, "salmon"), SARDINE(327,
			325, 323, 1, 45, 34, "sardine"), SEA_TURTLE(395, 397, 399, 82, 230,
			94, "sea turtle"), SHARK(383, 385, 387, 80, 210, 94, "shark"), SHRIMP(
			317, 315, 7954, 1, 31, 33, "shrimp"), SWORDFISH(371, 373, 375, 50,
			141, 86, "swordfish"), TROUT(335, 333, 343, 20, 71, 50, "trout"), TUNA(
			359, 361, 367, 35, 71, 55, "tuna");

	String name;
	int rawItem, cookedItem, burntItem, levelReq, xp, stopBurn;

	private CookingData(int rawItem, int cookedItem, int burntItem,
			int levelReq, int xp, int stopBurn, String name) {
		this.rawItem = rawItem;
		this.cookedItem = cookedItem;
		this.burntItem = burntItem;
		this.levelReq = levelReq;
		this.xp = xp;
		this.stopBurn = stopBurn;
		this.name = name;
	}

	public int getBurntItem() {
		return burntItem;
	}

	public int getCookedItem() {
		return cookedItem;
	}

	public int getLevelReq() {
		return levelReq;
	}

	public String getName() {
		return name;
	}

	public int getRawItem() {
		return rawItem;
	}

	public int getStopBurn() {
		return stopBurn;
	}

	public int getXp() {
		return xp;
	}
}
