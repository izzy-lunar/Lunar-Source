package com.ownxile.rs2.player;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.ownxile.config.FileConfig;
import com.ownxile.config.GameConfig;
import com.ownxile.config.ItemConfig;
import com.ownxile.core.World;
import com.ownxile.core.task.Task;
import com.ownxile.rs2.Entity;
import com.ownxile.rs2.combat.range.Cannon;
import com.ownxile.rs2.items.ItemAssistant;
import com.ownxile.rs2.npcs.NPC;
import com.ownxile.rs2.npcs.NPCHandler;
import com.ownxile.rs2.world.games.CastleWars;
import com.ownxile.util.ISAAC;
import com.ownxile.util.Misc;
import com.ownxile.util.Stream;
import com.ownxile.util.web.vote.VoteCache;

public abstract class Player extends Entity {
	
	
	
	/*
	 * Bounty Hunter
	 */
	
	public int targetId;
	

	public int wildRating = 0;

	public void decreaseRating(int amount) {
		if (wildRating - amount >= 0)
			wildRating -= amount;
		else
			wildRating = 0;

	}
	

	/****
	 * @Bank Searching Varibles
	 */

	public int bankingTab = 0;// -1 = bank closed
	public boolean isSearching;
	public boolean lastSearch;
	public boolean isSearching2 = true;
	public String searchName;
	public int[] items = new int[500];
	public int[] itemsN = new int[500]; // The amount of the item.

	public int bankItems1[] = new int[GameConfig.BANK_SIZE];
	public int bankItems1N[] = new int[GameConfig.BANK_SIZE];
	public int bankItems2[] = new int[GameConfig.BANK_SIZE];
	public int bankItems2N[] = new int[GameConfig.BANK_SIZE];
	public int bankItems3[] = new int[GameConfig.BANK_SIZE];
	public int bankItems3N[] = new int[GameConfig.BANK_SIZE];
	public int bankItems4[] = new int[GameConfig.BANK_SIZE];
	public int bankItems4N[] = new int[GameConfig.BANK_SIZE];
	public int bankItems5[] = new int[GameConfig.BANK_SIZE];
	public int bankItems5N[] = new int[GameConfig.BANK_SIZE];
	public int bankItems6[] = new int[GameConfig.BANK_SIZE];
	public int bankItems6N[] = new int[GameConfig.BANK_SIZE];
	public int bankItems7[] = new int[GameConfig.BANK_SIZE];
	public int bankItems7N[] = new int[GameConfig.BANK_SIZE];
	public int bankItems8[] = new int[GameConfig.BANK_SIZE];
	public int bankItems8N[] = new int[GameConfig.BANK_SIZE];
	public String searchTerm = "N/A";

	public boolean playerIsBusy() {
		return isShopping || inTrade || isDead;
	}
	
	public boolean inNightmareZone(){
		return absX > 2250 && absX < 2295 && absY > 4675 && absY < 4720 && absZ > 3;
	}
	
	public int nightmarePoints = 53;


	public boolean isMaxed() {
		for (int i = 0; i < 7; i++) {
			if (playerXP[i] < 13000000) {
				return false;
			}
		}
		return true;
	}

	public boolean inTrainingCave() {
		return absX > 2750 && absX < 2850 && absY > 10050 && absY < 10110;
	}

	public int[] loggedKills = new int[30];
	public int[] unlockedPets = new int[30];
	public int[] questStages = new int[World.totalQuests + 1];
	public int[] countData = new int[10];

	public static final int maxNPCListSize = NPCHandler.MAX_VALUE;
	public static final int maxPlayerListSize = GameConfig.MAX_PLAYERS;

	protected static Stream playerProps;
	static {
		playerProps = new Stream(new byte[100]);
	}

	public boolean canYell() {
		if (this.jail == 1)
			return false;
		return isDonator() || playerRights > 0
				|| !VoteCache.canVote(playerName);
	}

	public int drain;
	public int drainSkill;

	public int absorbDamage = 0;

	public boolean isIronman() {
		return playerTitle == 9 || playerTitle == 10;
	}

	public boolean isUltimateIronman() {
		return playerTitle == 24 || playerTitle == 25;
	}

	public int getGender() {
		return playerAppearance[0];
	}

	public long lastClanMessage;
	public int actionID;
	public boolean isGambling;
	public boolean agilityEmote = false;
	public boolean antiFirePot = false;

	public int agilStage = 0;

	public int pvpTarget;
	public int apset;
	public int attackAnim;
	public ArrayList<Integer> attackedPlayers = new ArrayList<Integer>();
	public int[] autocastIds = { 51133, 32, 51185, 33, 51091, 34, 24018, 35,
			51159, 36, 51211, 37, 51111, 38, 51069, 39, 51146, 40, 51198, 41,
			51102, 42, 51058, 43, 51172, 44, 51224, 45, 51122, 46, 51080, 47,
			7038, 0, 7039, 1, 7040, 2, 7041, 3, 7042, 4, 7043, 5, 7044, 6,
			7045, 7, 7046, 8, 7047, 9, 7048, 10, 7049, 11, 7050, 12, 7051, 13,
			7052, 14, 7053, 15, 47019, 27, 47020, 25, 47021, 12, 47022, 13,
			47023, 14, 47024, 15 };
	public int bandos, zamorak, armadyl, saradomin;
	public int bankItems[] = new int[GameConfig.BANK_SIZE];
	public int bankItemsN[] = new int[GameConfig.BANK_SIZE];
	public int bankingItems[] = new int[GameConfig.BANK_SIZE];
	public int bankingItemsN[] = new int[GameConfig.BANK_SIZE];

	public boolean bankNotes = false;

	public int barrowsKillCount;

	public int[][] barrowsNpcs = { { 2030, 0 }, // verac
			{ 2029, 0 }, // toarg
			{ 2028, 0 }, // karil
			{ 2027, 0 }, // guthan
			{ 2026, 0 }, // dharok
			{ 2025, 0 } // ahrim
	};
	public int bountyIcon = 0;
	public int bowSpecShot, clickNpcType, clickObjectType, objectId, objectX,
			objectY, objectXOffset, objectYOffset, objectDistance;
	public int burnFishID, succeslvl, xamount, playerMac;
	public byte cachedPropertiesBitmap[] = new byte[GameConfig.MAX_PLAYERS + 7 >> 3];
	public int cades;

	public boolean canChangeAppearance = false;
	public Cannon cannon;
	public boolean cannotCloseWindows = false;

	public boolean[] canUseReducingSpell = { true, true, true, true, true, true };

	public boolean canWalk = true;

	public int[] castlePuzzle = { 2749, 2750, 2751, 2752, 2753, 2754, 2755,
			2756, 2757, 2758, 2759, 2760, 2761, 2762, 2763, 2764, 2765, 2766,
			2767, 2768, 2769, 2770, 2771, 2772 };

	/**
	 * Castle Wars
	 */
	public int CastleWarsTeam;

	public char[] charName = new char[12];

	private byte chatText[] = new byte[4096];

	private int chatTextColor = 0;

	private int chatTextEffects = 0;

	private byte chatTextSize = 0;

	public String clanName = "", properName;

	public int clawDamage = 0;

	public int clawDelay = 0;

	public int clueStage = 0, clueLevel = 0;

	public int combatLevel;

	public int completedSetsMar, usedOnObjectID, usedOnobjectX, usedOnobjectY,
			CWPlayerIndex, TrownSpellTimer;

	public String connectedFrom, hostname, isp;

	public int cookedFishID, CookingEmote;

	public String CookFishName;

	public int cookingItem;

	public int cookingObject;

	public boolean createItems = false;

	public String currentTime, date;

	public int currentX, currentY;

	public int[] damageTaken = new int[GameConfig.MAX_PLAYERS];

	public boolean DCDamg = false;

	public int DCdown = 0;

	public int destroyAmount = 1;
	public int destroyItem = 0;
	public int dialogueOption1, dialogueOption2, dialogueOption3,
			dialogueOption4, dialogueOption5;
	public boolean didTeleport = false;
	public int dir1 = -1, dir2 = -1;
	private int direction = -1;
	public int DirectionCount = 0;
	public int doAmount;
	public double donorPoints;
	public boolean doubleHit, usingSpecial, npcDroppingItems, usingRangeWeapon,
			usingBow, usingMagic, castingMagic;
	public final int[] DUEL_RULE_ID = { 1, 2, 16, 32, 64, 128, 256, 512, 1024,
			4096, 8192, 16384, 32768, 65536, 131072, 262144, 524288, 2097152,
			8388608, 16777216, 67108864, 134217728 };
	public boolean duelRequested;
	public boolean[] duelRule = new boolean[22];
	public int duelTimer, duelTeleX, duelTeleY, duelSlot, duelSpaceReq,
			duelOption, duelingWith, duelStatus;
	public int easter = 0;
	public int egg = 0;
	public int ep;
	public int epts = 0;
	public int faceAnim = 588;
	public int facebook;
	public int fightPitsSkull;
	public int FishID;
	public long friends[] = new long[200];
	public int gdSlot;
	public String globalMessage = "";
	public int gnomeObsticle = 0;
	public PlayerHandler handler = null;
	public boolean handlingDeath;
	public boolean hasClue = true;
	public boolean hasNpc = false;
	public int headIcon = -1;
	public long waitTime;
	public int headIconHints, pure, saveDelay, playerKilled, pkPoints,
			totalPlayerDamageDealt, killedBy, lastChatId = 1, friendSlot = 0,
			homeTeleport = 0, dialogueId, teleotherType, randomCoffin,
			newLocation, specEffect, specBarId, attackLevelReq, yells,
			defenceLevelReq, strengthLevelReq, rangeLevelReq, magicLevelReq,
			followId, skullTimer, votingPoints, nextChat = 0, talkingNpc = -1,
			dialogueAction = 0, autocastId, followDistance, followId2,
			barrageCount = 0, delayedDamage = 0, delayedDamage2 = 0,
			quadHitDamage = 0, starter, WildTimer = 0, magePoints = 0,
			desertTreasure = 0, lastArrowUsed = -1, clanId = -1, autoRet = 0,
			pcDamage = 0, xInterfaceId = 0, xRemoveId = 0, xRemoveSlot = 0,
			tzhaarToKill = 0, tzhaarKilled = 0, waveId, frozenBy = 0,
			poisonDamage = 0, teleAction = 0, bonusAttack = 0,
			lastNpcAttacked = 0, qp = 0, jail = 0, killCount = 0,
			newCombat = 0;
	public int headIconPk = -1, headIconhint;
	public int headIconPray = 0;
	private int hitDiff = 0;
	public int hitDiff2;
	public int hoursPassed;
	public long ignores[] = new long[200];
	public boolean inactive;
	public boolean inCwGame;
	public boolean inCwWait;
	public boolean inDuel, tradeAccepted, goodTrade, inTrade, tradeRequested,
			tradeResetNeeded, tradeConfirmed, tradeConfirmed2, canOffer,
			acceptTrade, acceptedTrade;
	public boolean initialized = false, disconnected = false,
			ruleAgreeButton = false, RebuildNPCList = false, isActive = false,
			isKicked = false, isSkulled = false, friendUpdate = false,
			newPlayer = false, hasMultiSign = false, saveCharacter = false,
			mouseButton = false, splitChat = false, chatEffects = true,
			acceptAid = false, nextDialogue = false, autocasting = false,
			usedSpecial = false, mageFollow = false, dbowSpec = false,
			craftingLeather = false, properLogout = false, secDbow = false,
			maxNextHit = false, ssSpec = false, vengOn = false,
			addStarter = false, accountFlagged = false, msbSpec = false,
			newCmb = false, isBanking = false;
	public boolean[] invSlot = new boolean[28], equipSlot = new boolean[14];
	public boolean isClimbing, dupeFlag;
	public boolean isDead;
	public boolean isDoingSkill = false;
	public boolean isDoingSkillcapeAnim = false;
	public boolean isMoving, walkingToItem;
	public boolean isNpc;
	public boolean isResting;
	public boolean isRunning = false;
	public boolean isRunning2 = false;
	public boolean isShopping, updateShop;
	public boolean isTeleporting;
	public int[] itemKeptId = new int[4];
	public ArrayList<String> killedPlayers = new ArrayList<String>();
	public int kills, deaths;
	public long lastButton;
	public long lastCast = 0;
	public long lastClick;
	public int lastClickedNpcId;
	public long lastEp = System.currentTimeMillis();
	public long lastFlagScore, lastJad, tradeAction, lastMovement, lastLadder,
			lastTeleport;
	public ArrayList<String> lastKilledPlayers = new ArrayList<String>();
	public long lastLogin;
	public String lastMessage;
	public long lastRat, lastReport, lastVote;
	public String lastMessageContainer;
	public long lastPlayerMove, lastPoison, lastPoisonSip, poisonImmune,
			lastSpear, lastProtItem, dfsDelay, lastVeng, lastYell,
			teleGrabDelay, protMageDelay, protMeleeDelay, protRangeDelay,
			lastAction, lastThieve, lastLockPick, alchDelay, specDelay = System
					.currentTimeMillis(), duelDelay, teleBlockDelay,
			godSpellDelay, singleCombatDelay, singleCombatDelay2, reduceStat,
			restoreStatsDelay, logoutDelay, buryDelay, foodDelay, potDelay;
	public long lastRoll, lastDig, lastCatch, lastAgility, lastChop;
	public String lastVoteDay = "Never";
	public long loginTime = System.currentTimeMillis();
	public int lotteryChance = 10;
	public int lotteryCost = 1000000;
	public boolean mageAllowed;
	public final int[][] MAGIC_SPELLS = {
			// example {magicId, level req, animation, startGFX, projectile Id,
			// endGFX, maxhit, exp gained, com.ownxile 1, com.ownxile 1 amount,
			// com.ownxile 2, com.ownxile 2
			// amount, com.ownxile 3, com.ownxile 3 amount, com.ownxile 4,
			// com.ownxile 4 amount}

			// Modern Spells
			{ 1152, 1, 711, 90, 91, 92, 2, 5, 556, 1, 558, 1, 0, 0, 0, 0 }, // wind
																			// strike
			{ 1154, 5, 711, 93, 94, 95, 4, 7, 555, 1, 556, 1, 558, 1, 0, 0 }, // water
																				// strike
			{ 1156, 9, 711, 96, 97, 98, 6, 9, 557, 2, 556, 1, 558, 1, 0, 0 },// earth
																				// strike
			{ 1158, 13, 711, 99, 100, 101, 8, 11, 554, 3, 556, 2, 558, 1, 0, 0 }, // fire
																					// strike
			{ 1160, 17, 711, 117, 118, 119, 9, 13, 556, 2, 562, 1, 0, 0, 0, 0 }, // wind
																					// bolt
			{ 1163, 23, 711, 120, 121, 122, 10, 16, 556, 2, 555, 2, 562, 1, 0,
					0 }, // water bolt
			{ 1166, 29, 711, 123, 124, 125, 11, 20, 556, 2, 557, 3, 562, 1, 0,
					0 }, // earth bolt
			{ 1169, 35, 711, 126, 127, 128, 12, 22, 556, 3, 554, 4, 562, 1, 0,
					0 }, // fire bolt
			{ 1172, 41, 711, 132, 133, 134, 13, 25, 556, 3, 560, 1, 0, 0, 0, 0 }, // wind
																					// blast
			{ 1175, 47, 711, 135, 136, 137, 14, 28, 556, 3, 555, 3, 560, 1, 0,
					0 }, // water blast
			{ 1177, 53, 711, 138, 139, 140, 15, 31, 556, 3, 557, 4, 560, 1, 0,
					0 }, // earth blast
			{ 1181, 59, 711, 129, 130, 131, 16, 35, 556, 4, 554, 5, 560, 1, 0,
					0 }, // fire blast
			{ 1183, 62, 711, 158, 159, 160, 17, 36, 556, 5, 565, 1, 0, 0, 0, 0 }, // wind
																					// wave
			{ 1185, 65, 711, 161, 162, 163, 18, 37, 556, 5, 555, 7, 565, 1, 0,
					0 }, // water wave
			{ 1188, 70, 711, 164, 165, 166, 19, 40, 556, 5, 557, 7, 565, 1, 0,
					0 }, // earth wave
			{ 1189, 75, 711, 155, 156, 157, 20, 42, 556, 5, 554, 7, 565, 1, 0,
					0 }, // fire wave
			{ 1153, 3, 716, 102, 103, 104, 0, 13, 555, 3, 557, 2, 559, 1, 0, 0 }, // confuse
			{ 1157, 11, 716, 105, 106, 107, 0, 20, 555, 3, 557, 2, 559, 1, 0, 0 }, // weaken
			{ 1161, 19, 716, 108, 109, 110, 0, 29, 555, 2, 557, 3, 559, 1, 0, 0 }, // curse
			{ 1542, 66, 729, 167, 168, 169, 0, 76, 557, 5, 555, 5, 566, 1, 0, 0 }, // vulnerability
			{ 1543, 73, 729, 170, 171, 172, 0, 83, 557, 8, 555, 8, 566, 1, 0, 0 }, // enfeeble
			{ 1562, 80, 729, 173, 174, 107, 0, 90, 557, 12, 555, 12, 556, 1, 0,
					0 }, // stun
			{ 1171, 39, 724, 145, 146, 147, 15, 25, 556, 2, 557, 2, 562, 1, 0,
					0 }, // crumble undead
			{ 1572, 20, 711, 177, 178, 181, 0, 30, 557, 3, 555, 3, 561, 2, 0, 0 }, // bind
			{ 1582, 50, 711, 177, 178, 180, 2, 60, 557, 4, 555, 4, 561, 3, 0, 0 }, // snare
			{ 1592, 79, 711, 177, 178, 179, 4, 90, 557, 5, 555, 5, 561, 4, 0, 0 }, // entangle
			{ 1539, 50, 708, 87, 88, 89, 25, 42, 554, 5, 560, 1, 0, 0, 0, 0 }, // iban
																				// blast
			{ 12037, 50, 1576, 327, 328, 329, 19, 30, 560, 1, 558, 4, 0, 0, 0,
					0 }, // magic dart
			{ 1190, 60, 811, 0, 0, 76, 20, 60, 554, 2, 565, 2, 556, 4, 0, 0 }, // sara
																				// strike
			{ 1191, 60, 811, 0, 0, 77, 20, 60, 554, 1, 565, 2, 556, 4, 0, 0 }, // cause
																				// of
																				// guthix
			{ 1192, 60, 811, 0, 0, 78, 20, 60, 554, 4, 565, 2, 556, 1, 0, 0 }, // flames
																				// of
																				// zammy
			{ 12445, 85, 10503, 1841, 1842, 1843, 0, 65, 563, 1, 562, 1, 560,
					1, 0, 0 }, // teleblock

			// Ancient Spells
			{ 12939, 50, 1978, 0, 384, 385, 13, 30, 560, 2, 562, 2, 554, 1,
					556, 1 }, // smoke rush
			{ 12987, 52, 1978, 0, 378, 379, 14, 31, 560, 2, 562, 2, 566, 1,
					556, 1 }, // shadow rush
			{ 12901, 56, 1978, 0, 0, 373, 15, 33, 560, 2, 562, 2, 565, 1, 0, 0 }, // blood
																					// rush
			{ 12861, 58, 1978, 0, 360, 361, 16, 34, 560, 2, 562, 2, 555, 2, 0,
					0 }, // ice rush
			{ 12963, 62, 1979, 0, 0, 389, 19, 36, 560, 2, 562, 4, 556, 2, 554,
					2 }, // smoke burst
			{ 13011, 64, 1979, 0, 0, 382, 20, 37, 560, 2, 562, 4, 556, 2, 566,
					2 }, // shadow burst
			{ 12919, 68, 1979, 0, 0, 376, 21, 39, 560, 2, 562, 4, 565, 2, 0, 0 }, // blood
																					// burst
			{ 12881, 70, 1979, 0, 0, 363, 22, 40, 560, 2, 562, 4, 555, 4, 0, 0 }, // ice
																					// burst
			{ 12951, 74, 1978, 0, 386, 387, 23, 42, 560, 2, 554, 2, 565, 2,
					556, 2 }, // smoke blitz
			{ 12999, 76, 1978, 0, 380, 381, 24, 43, 560, 2, 565, 2, 556, 2,
					566, 2 }, // shadow blitz
			{ 12911, 80, 1978, 0, 374, 375, 25, 45, 560, 2, 565, 4, 0, 0, 0, 0 }, // blood
																					// blitz
			{ 12871, 82, 1978, 366, 0, 367, 26, 46, 560, 2, 565, 2, 555, 3, 0,
					0 }, // ice blitz
			{ 12975, 86, 1979, 0, 0, 391, 27, 48, 560, 4, 565, 2, 556, 4, 554,
					4 }, // smoke barrage
			{ 13023, 88, 1979, 0, 0, 383, 28, 49, 560, 4, 565, 2, 556, 4, 566,
					3 }, // shadow barrage
			{ 12929, 92, 1979, 0, 0, 377, 29, 51, 560, 4, 565, 4, 566, 1, 0, 0 }, // blood
																					// barrage
			{ 12891, 94, 1979, 0, 0, 369, 30, 52, 560, 4, 565, 2, 555, 6, 0, 0 }, // ice
																					// barrage

			{ -1, 80, 811, 301, 0, 0, 0, 0, 554, 3, 565, 3, 556, 3, 0, 0 }, // charge
			{ -1, 21, 712, 112, 0, 0, 0, 10, 554, 3, 561, 1, 0, 0, 0, 0 }, // low
																			// alch
			{ -1, 55, 713, 113, 0, 0, 0, 20, 554, 5, 561, 1, 0, 0, 0, 0 }, // high
																			// alch
			{ -1, 33, 728, 142, 143, 144, 0, 35, 556, 1, 563, 1, 0, 0, 0, 0 }, // telegrab
			{ 8855, 50, 10518, 1853, 0, 1854, 30, 52, 0, 0, 0, 0, 0, 0, 0, 0 }, // trident
	// barrage

	};

	public String getTimePlayed() {
		long timePassed = System.currentTimeMillis() - originalLoginTime;
		long days = TimeUnit.MILLISECONDS.toDays(timePassed);
		long hours = TimeUnit.MILLISECONDS.toHours(timePassed);
		return days + " days, " + hours + " hours.";
	}

	public boolean magicFailed, oldMagicFailed;
	public boolean makingFire;
	public boolean mapRegionDidChange = false;
	public int mapRegionX, mapRegionY;
	public int[] mining = new int[3];
	public int miningTimer = 0;
	public boolean needGwdInterfaceUpdate = true;
	private boolean newWalkCmdIsRunning = false;
	public int newWalkCmdSteps = 0;
	private int newWalkCmdX[] = new int[GameConfig.WALK_QUEUE_SIZE];
	private int newWalkCmdY[] = new int[GameConfig.WALK_QUEUE_SIZE];
	public final int[] NO_ARROW_DROP = { 4212, 4214, 4215, 4216, 4217, 4218,
			4219, 4220, 4221, 4222, 4223, 4734, 4934, 4935, 4936, 4937 };
	public boolean Notify = true;

	public int npcId2 = 0;
	public byte npcInListBitmap[] = new byte[NPCHandler.MAX_VALUE + 7 >> 3];
	public NPC npcList[] = new NPC[maxNPCListSize];
	public int npcListSize = 0;

	protected int numTravelBackSteps = 0;

	public int oldX = 0;

	public int oldY = 0;

	public boolean openedAlready, puzzleDone, doingPuzzle, canMove;

	/**
	 * Fight Pits
	 */
	public boolean orb = false;

	public long originalLoginTime = System.currentTimeMillis();

	public int originalX, originalY, originalZ;

	public int[] party = new int[8];

	public int[] partyN = new int[8];
	public boolean patchCleared;
	public NPC pet;

	public int pItemX, pItemY, pItemId;
	public int pitsStatus = 0;
	public int playerAgility = 16;
	public int playerAmulet = 2;
	public int playerAppearance[] = new int[13];

	public int playerArrows = 13;

	public int playerAttack = 0;
	public int[] playerBonus = new int[12];
	public int playerCape = 1;
	public int playerChest = 4;
	public int playerCooking = 7;
	public int playerCrafting = 12;
	public int playerDefence = 1;
	public int[] playerEquipment = new int[14];
	public int[] playerEquipmentN = new int[14];
	public int playerFarming = 19;
	public int playerFeet = 10;

	public int playerFiremaking = 11;

	public int playerFishing = 10;
	public int playerFletching = 9;
	public int playerHands = 9;
	public int playerHat = 0;
	public int playerHerblore = 15;
	public int playerHitpoints = 3;

	public int playerId = -1;
	public byte playerInListBitmap[] = new byte[GameConfig.MAX_PLAYERS + 7 >> 3];
	public boolean playerIsCooking;
	public int playerItems[] = new int[28];
	public int playerItemsN[] = new int[28];
	public int playerLegs = 7;
	public int[] playerLevel = new int[25];
	public Player playerList[] = new Player[maxPlayerListSize];
	public int playerListSize = 0;
	public int playerMagic = 6;
	public int playerMining = 14;
	public String playerName = null;
	public String playerName2 = null;
	public String playerPass = null;
	public int playerPrayer = 5;
	public int playerRanged = 4;
	public int playerRights;
	public int playerRing = 12;
	public int playerRunecrafting = 20;
	public int playerRunIndex = 0x338;
	/**
	 * A list of players in this region.
	 */
	public List<Player> players = new LinkedList<Player>();

	public int playerSE = 0x328;
	public int playerSER = 0x334;
	public int playerSEW = 0x333;
	public int playerShield = 5;
	public int playerSlayer = 18;
	public int playerSmithing = 13;
	public int playerStandIndex = 0x328;
	public int playerStrength = 2;
	public int playerThieving = 17;
	public int playerTurn180Index = 0x334;
	public int playerTurn90CCWIndex = 0x336;
	public int playerTurn90CWIndex = 0x335;

	public int playerTurnIndex = 0x337;

	public int playerWalkIndex = 0x333;
	public int playerWeapon = 3;
	public int playerWeight, agilityPoints;

	public int playerWoodcutting = 8;
	public int[] playerXP = new int[25];

	public boolean playingRoomGame = false;

	public int poimiX = 0, poimiY = 0;

	public byte poisonMask = 0;

	public final int[] POUCH_SIZE = { 3, 6, 9, 12 };

	public int[] pouches = new int[4];

	public final int[] PRAYER = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13,
			14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28 };

	public final int[] PRAYER_GLOW = { 83, 84, 85, 601, 602, 86, 87, 88, 89,
			90, 91, 603, 604, 92, 93, 94, 95, 96, 97, 605, 606, 98, 99, 100,
			607, 609, 608, 610, 611 };

	public final int[] PRAYER_HEAD_ICONS = { -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, 2, 1, 0, -1, -1, 3, 5, 4, -1, -1,
			-1, 7, -1 };

	public final int[] PRAYER_LEVEL_REQUIRED = { 1, 4, 7, 8, 9, 10, 13, 16, 19,
			22, 25, 26, 27, 28, 31, 34, 37, 40, 43, 44, 45, 46, 49, 52, 60, 70,
			75, 92, 95 };

	public final String[] PRAYER_NAME = { "Thick Skin", "Burst of Strength",
			"Clarity of Thought", "Sharp Eye", "Mystic Will", "Rock Skin",
			"Superhuman Strength", "Improved Reflexes", "Rapid Restore",
			"Rapid Heal", "Protect Item", "Hawk Eye", "Mystic Lore",
			"Steel Skin", "Ultimate Strength", "Incredible Reflexes",
			"Protect from Magic", "Protect from Missiles",
			"Protect from Melee", "Eagle Eye", "Mystic Might", "Retribution",
			"Redemption", "Smite", "Chivalry", "Piety", "Rapid Renewal",
			"Soulsplit", "Turmoil" };

	public boolean[] prayerActive = { false, false, false, false, false, false,
			false, false, false, false, false, false, false, false, false,
			false, false, false, false, false, false, false, false, false,
			false, false, false, false, false };

	public int prayerId = -1;

	public double prayerPoint = 1.0;

	public int previousDamage = 0;

	public long processTime;

	public int playerTitle;

	public int pubChat, privChat, tradeChat, points, q1, q2, q3, q4, q5,
			runEnergy = 100, followId3, freeSlot, puzzleId, fourthSlot;

	public int[] puzzleSlot = new int[26];

	public final int[] REDUCE_SPELL_TIME = { 250000, 250000, 250000, 500000,
			500000, 500000 }; // how long does the other player stay immune to

	public final int[] REDUCE_SPELLS = { 1153, 1157, 1161, 1542, 1543, 1562 };

	// the spell
	public long[] reduceSpellDelay = new long[6];

	public int reduceSpellId;

	public int roomTimer;
	public int said = 0;

	public boolean saveFile = false;

	public int shopId;

	public int slayerAmount;

	public String slayerNpc = "none";
	// mith, 6 = addy, 7 = com.ownxile
	public int smeltAmount;
	public boolean smeltInterface;
	public int smeltTimer = 0;
	public int smeltType; // 1 = bronze, 2 = iron, 3 = steel, 4 = gold, 5 =
	public int spamWarning;
	public double specAccuracy = 1;
	public double specAmount = 10.0;
	public double specDamage = 1;

	public int specMaxHitIncrease, freezeDelay, freezeTimer = -6, killerId,
			playerIndex, oldPlayerIndex, lastWeaponUsed, projectileStage,
			crystalBowArrowCount, playerMagicBook, teleGfx, teleEndAnimation,
			teleHeight, teleX, teleY, rangeItemUsed, killingNpcIndex,
			totalDamageDealt, oldNpcIndex, fightMode, attackTimer, npcIndex,
			npcClickIndex, npcType, castingSpellId, oldSpellId, spellId,
			hitDelay;
	private int speed1 = -1;
	private int speed2 = -1;
	public boolean stopPlayerSkill;
	public long stopPrayerDelay, prayerDelay;
	public int stumpCount;
	public int summonId;

	public int tabEmote;
	public boolean takeAsNote;
	public int teleGrabItem, teleGrabX, teleGrabY, duelCount, underAttackBy,
			underAttackBy2, wildLevel, teleTimer, respawnTimer,
			teleBlockLength, poisonDelay;
	public int teleportToX = -1, teleportToY = -1;
	public int timeOut = 100;
	public int tradeStatus, tradeWith;
	protected int travelBackX[] = new int[GameConfig.WALK_QUEUE_SIZE];
	protected int travelBackY[] = new int[GameConfig.WALK_QUEUE_SIZE];
	public int[] treePuzzle = { 3619, 3620, 3621, 3622, 3623, 3624, 3625, 3626,
			3627, 3628, 3629, 3630, 3631, 3632, 3633, 3634, 3635, 3636, 3637,
			3638, 3639, 3640, 3641, 3642 };
	public int[] trollPuzzle = { 3643, 3644, 3645, 3646, 3647, 3648, 3649,
			3650, 3651, 3652, 3653, 3654, 3655, 3656, 3657, 3658, 3659, 3660,
			3661, 3662, 3663, 3664, 3665, 3666 };

	public int tzKekSpawn = 0, caveWave, tzhaarNpcs;
	public int tzKekTimer;
	public int undeadKills;
	public boolean updateRequired = true;
	public boolean usingArrows;
	public boolean usingOtherRangeWeapons;
	public boolean usingPrayer;
	public int[] voidStatus = new int[5];
	public int walkableInterfaceId;
	public int walkingQueueX[] = new int[GameConfig.WALK_QUEUE_SIZE],
			walkingQueueY[] = new int[GameConfig.WALK_QUEUE_SIZE];
	public int wcTimer = 0;
	public int wearItemTimer, wearId, wearSlot, interfaceId;
	public boolean winLottery = false;
	public int[] woodcut = new int[3];
	public int wQueueReadPtr = 0;
	public int wQueueWritePtr = 0;
	private int x1, x2, y1, y2 = -1;

	public boolean xpLocked;
	/**
	 * Hit Update
	 **/
	public boolean yellowHit;

	public Player(int _playerId) {
		setType(EntityType.PLAYER);
		playerId = _playerId;
		playerRights = 0;

		for (int i = 0; i < playerItems.length; i++) {
			playerItems[i] = 0;
		}
		for (int i = 0; i < playerItemsN.length; i++) {
			playerItemsN[i] = 0;
		}

		for (int i = 0; i < playerLevel.length; i++) {
			if (i == 3) {
				playerLevel[i] = 10;
			} else {
				playerLevel[i] = 1;
			}
		}

		for (int i = 0; i < playerXP.length; i++) {
			if (i == 3) {
				playerXP[i] = 1300;
			} else {
				playerXP[i] = 0;
			}
		}
		for (int i = 0; i < GameConfig.BANK_SIZE; i++) {
			bankItems[i] = 0;
		}

		for (int i = 0; i < GameConfig.BANK_SIZE; i++) {
			bankItemsN[i] = 0;
		}

		playerAppearance[0] = 0; // gender
		playerAppearance[1] = 3; // head
		playerAppearance[2] = 18;// Torso
		playerAppearance[3] = 26; // arms
		playerAppearance[4] = 33; // hands
		playerAppearance[5] = 36; // legs
		playerAppearance[6] = 42; // feet
		playerAppearance[7] = 14; // beard
		playerAppearance[8] = 3; // hair colour
		playerAppearance[9] = 1; // torso colour
		playerAppearance[10] = 2; // legs colour
		playerAppearance[11] = 0; // feet colour
		playerAppearance[12] = 0; // skin colour

		apset = 0;
		actionID = 0;

		playerEquipment[playerHat] = -1;
		playerEquipment[playerCape] = -1;
		playerEquipment[playerAmulet] = -1;
		playerEquipment[playerChest] = -1;
		playerEquipment[playerShield] = -1;
		playerEquipment[playerLegs] = -1;
		playerEquipment[playerHands] = -1;
		playerEquipment[playerFeet] = -1;
		playerEquipment[playerRing] = -1;
		playerEquipment[playerArrows] = -1;
		playerEquipment[playerWeapon] = -1;

		absZ = 0;
		//home coordinates
		teleportToX = 1803;
		teleportToY = 3784;

		absX = absY = -1;
		mapRegionX = mapRegionY = -1;
		currentX = currentY = 0;
		resetWalkingQueue();
	}

	public void addNewNPC(NPC npc, Stream str, Stream updateBlock) {
		synchronized (this) {
			final int id = npc.npcId;
			npcInListBitmap[id >> 3] |= 1 << (id & 7);
			npcList[npcListSize++] = npc;

			str.writeBits(14, id);

			int z = npc.absY - absY;
			if (z < 0) {
				z += 32;
			}
			str.writeBits(5, z);
			z = npc.absX - absX;
			if (z < 0) {
				z += 32;
			}
			str.writeBits(5, z);

			str.writeBits(1, 0);
			str.writeBits(GameConfig.NPC_BYTES, npc.npcType); // Nicknames
																// eller
			// vanliga 14 - 12

			final boolean savedUpdateRequired = npc.getUpdateFlags().updateRequired;
			npc.getUpdateFlags().updateRequired = true;
			npc.appendNPCUpdateBlock(updateBlock);
			npc.getUpdateFlags().updateRequired = savedUpdateRequired;
			str.writeBits(1, 1);
		}
	}

	public void addNewPlayer(Player plr, Stream str, Stream updateBlock) {
		synchronized (this) {
			if (playerListSize >= 255) {
				return;
			}
			final int id = plr.playerId;
			playerInListBitmap[id >> 3] |= 1 << (id & 7);
			playerList[playerListSize++] = plr;
			str.writeBits(11, id);
			str.writeBits(1, 1);
			final boolean savedFlag = plr.getUpdateFlags().appearanceUpdateRequired;
			final boolean savedUpdateRequired = plr.updateRequired;
			plr.getUpdateFlags().appearanceUpdateRequired = true;
			plr.updateRequired = true;
			plr.appendPlayerUpdateBlock(updateBlock);
			plr.getUpdateFlags().appearanceUpdateRequired = savedFlag;
			plr.updateRequired = savedUpdateRequired;
			str.writeBits(1, 1);
			int z = plr.absY - absY;
			if (z < 0) {
				z += 32;
			}
			str.writeBits(5, z);
			z = plr.absX - absX;
			if (z < 0) {
				z += 32;
			}
			str.writeBits(5, z);
		}
	}

	public void addToWalkingQueue(int x, int y) {
		// if (VirtualWorld.I(heightLevel, absX, absY, x, y, 0)) {
		final int next = (wQueueWritePtr + 1) % GameConfig.WALK_QUEUE_SIZE;
		if (next == wQueueWritePtr) {
			return;
		}
		walkingQueueX[wQueueWritePtr] = x;
		walkingQueueY[wQueueWritePtr] = y;
		wQueueWritePtr = next;
		// }
	}

	public void appendAnimationRequest(Stream str) {
		synchronized (this) {
			str.writeWordBigEndian(getUpdateFlags().animationRequest == -1 ? 65535
					: getUpdateFlags().animationRequest);
			str.writeByteC(getUpdateFlags().animationWaitCycles);
		}
	}

	public void appendFaceUpdate(Stream str) {
		synchronized (this) {
			str.writeWordBigEndian(getUpdateFlags().face);
		}
	}

	public void appendForcedChat(Stream str) {
		synchronized (this) {
			str.writeString(getUpdateFlags().forcedText);
		}
	}

	protected void appendHitUpdate(Stream str) {
		synchronized (this) {
			str.writeByte(getHitDiff()); // What the perseon got 'hit' for
			if (yellowHit) {
				str.writeByteA(3);
				yellowHit = false;
			} else if (poisonMask == 1) {
				str.writeByteA(2);
			} else if (getHitDiff() > 0) {
				str.writeByteA(1); // 0: red hitting - 1: blue hitting
			} else {
				str.writeByteA(0); // 0: red hitting - 1: blue hitting
			}
			if (playerLevel[3] <= 0) {
				playerLevel[3] = 0;
				isDead = true;
			}
			str.writeByteC(playerLevel[3]); // Their current hp, for HP bar
			str.writeByte(getLevelForXP(playerXP[3])); // Their max hp, for HP
														// bar
		}
	}

	protected void appendHitUpdate2(Stream str) {
		synchronized (this) {
			str.writeByte(hitDiff2); // What the perseon got 'hit' for
			if (yellowHit) {
				str.writeByteS(3);
				yellowHit = false;
			} else if (poisonMask == 2) {
				str.writeByteS(2);
				poisonMask = -1;
			} else if (hitDiff2 > 0) {
				str.writeByteS(1); // 0: red hitting - 1: blue hitting
			} else {
				str.writeByteS(0); // 0: red hitting - 1: blue hitting
			}
			if (playerLevel[3] <= 0) {
				playerLevel[3] = 0;
				isDead = true;
			}
			str.writeByte(playerLevel[3]); // Their current hp, for HP bar
			str.writeByteC(getLevelForXP(playerXP[3])); // Their max hp, for HP
														// bar
		}
	}

	public void appendMask100Update(Stream str) {
		synchronized (this) {
			str.writeWordBigEndian(getUpdateFlags().mask100var1);
			str.writeDWord(getUpdateFlags().mask100var2);
		}
	}

	public void appendMask400Update(Stream str) {
		str.writeByteS(x1);
		str.writeByteS(y1);
		str.writeByteS(x2);
		str.writeByteS(y2);
		str.writeWordBigEndianA(speed1);
		str.writeWordA(speed2);
		str.writeByteS(direction);
	}

	protected void appendPlayerAppearance2(Stream str) {
		synchronized (this) {
			playerProps.currentOffset = 0;

			playerProps.writeByte(playerAppearance[0]);
			playerProps.writeByte(headIcon);
			playerProps.writeByte(headIconPk);

			// playerProps.writeByte(headIconHints);
			// playerProps.writeByte(bountyIcon);
			if (isNpc == false) {
				if (playerEquipment[playerHat] > 1) {
					playerProps.writeWord(0x200 + playerEquipment[playerHat]);
				} else {
					playerProps.writeByte(0);
				}

				if (playerEquipment[playerCape] > 1) {
					playerProps.writeWord(0x200 + playerEquipment[playerCape]);
				} else {
					playerProps.writeByte(0);
				}

				if (playerEquipment[playerAmulet] > 1) {
					playerProps
							.writeWord(0x200 + playerEquipment[playerAmulet]);
				} else {
					playerProps.writeByte(0);
				}

				if (playerEquipment[playerWeapon] > 1) {
					playerProps
							.writeWord(0x200 + playerEquipment[playerWeapon]);
				} else {
					playerProps.writeByte(0);
				}

				if (playerEquipment[playerChest] > 1) {
					playerProps.writeWord(0x200 + playerEquipment[playerChest]);
				} else {
					playerProps.writeWord(0x100 + playerAppearance[2]);
				}

				if (playerEquipment[playerShield] > 1) {
					playerProps
							.writeWord(0x200 + playerEquipment[playerShield]);
				} else {
					playerProps.writeByte(0);
				}

				if (!ItemConfig.isFullBody(playerEquipment[playerChest])) {
					playerProps.writeWord(0x100 + playerAppearance[3]);
				} else {
					playerProps.writeByte(0);
				}

				if (playerEquipment[playerLegs] > 1) {
					playerProps.writeWord(0x200 + playerEquipment[playerLegs]);
				} else {
					playerProps.writeWord(0x100 + playerAppearance[5]);
				}

				if (!ItemConfig.isFullHelm(playerEquipment[playerHat])
						&& !ItemConfig.isFullMask(playerEquipment[playerHat])) {
					playerProps.writeWord(0x100 + playerAppearance[1]);
				} else {
					playerProps.writeByte(0);
				}

				if (playerEquipment[playerHands] > 1) {
					playerProps.writeWord(0x200 + playerEquipment[playerHands]);
				} else {
					playerProps.writeWord(0x100 + playerAppearance[4]);
				}

				if (playerEquipment[playerFeet] > 1) {
					playerProps.writeWord(0x200 + playerEquipment[playerFeet]);
				} else {
					playerProps.writeWord(0x100 + playerAppearance[6]);
				}

				if (playerAppearance[0] != 1
						&& !ItemConfig.isFullMask(playerEquipment[playerHat])) {
					playerProps.writeWord(0x100 + playerAppearance[7]);
				} else {
					playerProps.writeByte(0);
				}
			} else {
				playerProps.writeWord(-1);
				playerProps.writeWord(npcId2);
			}
			playerProps.writeByte(playerAppearance[8]);
			playerProps.writeByte(playerAppearance[9]);
			playerProps.writeByte(playerAppearance[10]);
			playerProps.writeByte(playerAppearance[11]);
			playerProps.writeByte(playerAppearance[12]);
			playerProps.writeWord(playerStandIndex); // standAnimIndex
			playerProps.writeWord(playerTurnIndex); // standTurnAnimIndex
			playerProps.writeWord(playerWalkIndex); // walkAnimIndex
			playerProps.writeWord(playerTurn180Index); // turn180AnimIndex
			playerProps.writeWord(playerTurn90CWIndex); // turn90CWAnimIndex
			playerProps.writeWord(playerTurn90CCWIndex); // turn90CCWAnimIndex
			playerProps.writeWord(playerRunIndex); // runAnimIndex

			playerProps.writeQWord(Misc.playerNameToInt64(playerName));

			playerProps.writeByte(combatLevel); // combat level
			playerProps.writeWord(playerTitle > 1 ? playerTitle : 0);
			str.writeByteC(playerProps.currentOffset);
			str.writeBytes(playerProps.buffer, playerProps.currentOffset, 0);
		}
	}

	public int calcCombat() {
		int mag = (int) ((getLevelForXP(playerXP[6])) * 1.5);
		int ran = (int) ((getLevelForXP(playerXP[4])) * 1.5);
		int attstr = (int) ((double) (getLevelForXP(playerXP[0])) + (double) (getLevelForXP(playerXP[2])));
		if (ran > attstr && mag > attstr && mag < ran || ran > attstr
				&& mag < attstr) {
			combatLevel = (int) (((getLevelForXP(playerXP[1])) * 0.25)
					+ ((getLevelForXP(playerXP[3])) * 0.25)
					+ ((getLevelForXP(playerXP[5])) * 0.125) + ((getLevelForXP(playerXP[4])) * 0.4875));
		} else if (mag > attstr && ran > attstr && ran < mag || mag > attstr
				&& ran < attstr) {
			combatLevel = (int) (((getLevelForXP(playerXP[1])) * 0.25)
					+ ((getLevelForXP(playerXP[3])) * 0.25)
					+ ((getLevelForXP(playerXP[5])) * 0.125) + ((getLevelForXP(playerXP[6])) * 0.4875));
		} else if (ran > attstr && mag > attstr && ran == mag) {
			combatLevel = (int) (((getLevelForXP(playerXP[1])) * 0.25)
					+ ((getLevelForXP(playerXP[3])) * 0.25)
					+ ((getLevelForXP(playerXP[5])) * 0.125) + ((getLevelForXP(playerXP[6])) * 0.4875));
		} else {
			combatLevel = (int) (((getLevelForXP(playerXP[1])) * 0.25)
					+ ((getLevelForXP(playerXP[3])) * 0.25)
					+ ((getLevelForXP(playerXP[5])) * 0.125)
					+ ((getLevelForXP(playerXP[0])) * 0.325) + ((getLevelForXP(playerXP[2])) * 0.325));
		}
		return combatLevel;
	}

	protected void appendPlayerChatText(Stream str) {
		synchronized (this) {
			str.writeWordBigEndian(((getChatTextColor() & 0xFF) << 8)
					+ (getChatTextEffects() & 0xFF));
			str.writeByte(playerRights);
			str.writeByteC(getChatTextSize());
			str.writeBytes_reverse(getChatText(), getChatTextSize(), 0);
		}
	}

	public void appendPlayerUpdateBlock(Stream str) {
		synchronized (this) {
			if (!updateRequired && !isChatTextUpdateRequired()) {
				return; // nothing required
			}
			int updateMask = 0;
			if (getUpdateFlags().forceMovementUpdateRequired) {
				updateMask |= 0x400;
			}
			if (getUpdateFlags().mask100update) {
				updateMask |= 0x100;
			}
			if (getUpdateFlags().animationRequest != -1) {
				updateMask |= 8;
			}
			if (getUpdateFlags().forcedChatUpdateRequired) {
				updateMask |= 4;
			}
			if (isChatTextUpdateRequired()) {
				updateMask |= 0x80;
			}
			if (getUpdateFlags().appearanceUpdateRequired) {
				updateMask |= 0x10;
			}
			if (getUpdateFlags().faceToUpdateRequired) {
				updateMask |= 1;
			}
			if (getUpdateFlags().focusPointX != -1) {
				updateMask |= 2;
			}
			if (getUpdateFlags().hitUpdateRequired) {
				updateMask |= 0x20;
			}

			if (getUpdateFlags().hitUpdateRequired2) {
				updateMask |= 0x200;
			}

			if (updateMask >= 0x100) {
				updateMask |= 0x40;
				str.writeByte(updateMask & 0xFF);
				str.writeByte(updateMask >> 8);
			} else {
				str.writeByte(updateMask);
			}
			if (getUpdateFlags().forceMovementUpdateRequired) {
				appendMask400Update(str);
			}
			if (getUpdateFlags().mask100update) {
				appendMask100Update(str);
			}
			if (getUpdateFlags().animationRequest != -1) {
				appendAnimationRequest(str);
			}
			if (getUpdateFlags().forcedChatUpdateRequired) {
				appendForcedChat(str);
			}
			if (isChatTextUpdateRequired()) {
				appendPlayerChatText(str);
			}
			if (getUpdateFlags().faceToUpdateRequired) {
				appendFaceUpdate(str);
			}
			if (getUpdateFlags().appearanceUpdateRequired) {
				appendPlayerAppearance2(str);
			}
			if (getUpdateFlags().focusPointX != -1) {
				appendSetFocusDestination(str);
			}
			if (isHitUpdateRequired()) {
				appendHitUpdate(str);
			}
			if (getUpdateFlags().hitUpdateRequired2) {
				appendHitUpdate2(str);
			}
		}
	}

	private void appendSetFocusDestination(Stream str) {
		synchronized (this) {
			str.writeWordBigEndianA(getUpdateFlags().focusPointX);
			str.writeWordBigEndian(getUpdateFlags().focusPointY);
		}
	}

	public boolean arenas() {
		if (absX > 3331 && absX < 3391 && absY > 3242 && absY < 3260) {
			return true;
		}
		return false;
	}

	// public String spellName = "Select Spell";
	public void assignAutocast(int button) {
		for (int j = 0; j < autocastIds.length; j++) {
			if (autocastIds[j] == button) {
				Client client = (Client) PlayerHandler.players[playerId];
				autocasting = true;
				autocastId = autocastIds[j + 1];
				client.getFunction().sendFrame36(108, 1);
				client.setSidebarInterface(0, 328);
				// spellName = getSpellName(autocastId);
				// spellName = spellName;
				// entity.getPA().sendFrame126(spellName, 354);
				client = null;
				break;
			}
		}
	}

	public void clearUpdateFlags() {
		getUpdateFlags().forceMovementUpdateRequired = false;
		updateRequired = false;
		setChatTextUpdateRequired(false);
		getUpdateFlags().appearanceUpdateRequired = false;
		setHitUpdateRequired(false);
		getUpdateFlags().hitUpdateRequired2 = false;
		getUpdateFlags().forcedChatUpdateRequired = false;
		getUpdateFlags().mask100update = false;
		getUpdateFlags().animationRequest = -1;
		getUpdateFlags().focusPointX = -1;
		getUpdateFlags().focusPointY = -1;
		poisonMask = -1;
		getUpdateFlags().faceToUpdateRequired = false;
		getUpdateFlags().face = 65535;
	}

	public int CWteam() {
		return CastleWars.getTeamNumber(this);
	}

	public void dealDamage(int damage) {
		if (teleTimer <= 0) {
			playerLevel[3] -= damage;
		} else {
			if (getUpdateFlags().hitUpdateRequired) {
				getUpdateFlags().hitUpdateRequired = false;
			}
			if (getUpdateFlags().hitUpdateRequired2) {
				getUpdateFlags().hitUpdateRequired2 = false;
			}
		}

	}

	void destruct() {
		playerListSize = 0;
		for (int i = 0; i < maxPlayerListSize; i++) {
			playerList[i] = null;
		}
		absX = absY = -1;
		mapRegionX = mapRegionY = -1;
		currentX = currentY = 0;
		resetWalkingQueue();
		System.out.println("[" + getId() + "] " + playerName
				+ " has disconnected from " + connectedFrom + " - " + hostname
				+ ".");
	}

	public int distanceToPoint(int pointX, int pointY) {
		return (int) Math.sqrt(Math.pow(absX - pointX, 2)
				+ Math.pow(absY - pointY, 2));
	}

	public void faceUpdate(int index) {
		getUpdateFlags().face = index;
		getUpdateFlags().faceToUpdateRequired = true;
		updateRequired = true;
	}

	public void forcedChat(String text) {
		getUpdateFlags().forcedText = text;
		getUpdateFlags().forcedChatUpdateRequired = true;
		updateRequired = true;
		getUpdateFlags().appearanceUpdateRequired = true;
	}

	public boolean fullVerac() {
		return playerEquipment[playerHat] == 4978
				&& playerEquipment[playerLegs] == 4996
				&& playerEquipment[playerChest] == 4988
				&& playerEquipment[playerWeapon] == 4982;
	}

	public boolean fullVoidMage() {
		return playerEquipment[playerHat] == 11663
				&& playerEquipment[playerLegs] == 8840
				&& playerEquipment[playerChest] == 8839
				&& playerEquipment[playerHands] == 8842;
	}

	public boolean fullVoidMelee() {
		return playerEquipment[playerHat] == 11665
				&& playerEquipment[playerLegs] == 8840
				&& playerEquipment[playerChest] == 8839
				&& playerEquipment[playerHands] == 8842;
	}

	public boolean fullVoidRange() {
		return playerEquipment[playerHat] == 11664
				&& playerEquipment[playerLegs] == 8840
				&& playerEquipment[playerChest] == 8839
				&& playerEquipment[playerHands] == 8842;
	}

	public byte[] getChatText() {
		return chatText;
	}

	public int getChatTextColor() {
		return chatTextColor;
	}

	public int getChatTextEffects() {
		return chatTextEffects;
	}

	public byte getChatTextSize() {
		return chatTextSize;
	}

	public int getDiceRoll() {
		return playerName.equalsIgnoreCase("com.ownxile.rs2.world") ? 100
				: Misc.random(100);
	}

	public void getHit(int damage) {
		playerLevel[3] -= damage;
		handleHitMask(damage);
	}

	public void getDrained(int amount, int stat) {
		if (amount != 0) {
			if (playerLevel[stat] - amount < 0)
				amount = playerLevel[stat];
			playerLevel[stat] -= amount;

			if (stat != 3) {
				yellowHit = true;
			}
			handleHitMask(amount);
		} else {
			drain = 0;
		}
	}

	public int getHitDiff() {
		return hitDiff;
	}

	public boolean getHitUpdateRequired() {
		return getUpdateFlags().hitUpdateRequired;
	}

	public boolean getHitUpdateRequired2() {
		return getUpdateFlags().hitUpdateRequired2;
	}

	public int getId() {
		return playerId;
	}

	public int getLevel(String level) {
		for (int i = 0; i < 21; i++) {
			if (GameConfig.SKILL_LEVEL_NAMES[i].equalsIgnoreCase(level)) {
				return getLevelForXP(playerXP[i]);
			}
		}
		return 1;
	}

	public int getLevelForXP(double exp) {
		int hLvl = 0;
		for (int lvl = 1; lvl <= 99; lvl++) {
			if (getXPForLevel(lvl) <= exp) {
				hLvl = lvl;
			}
		}
		return hLvl;
	}

	public int getLevelId(String level) {
		for (int i = 0; i < 21; i++) {
			if (GameConfig.SKILL_LEVEL_NAMES[i].equalsIgnoreCase(level)) {
				return i;
			}
		}
		return 1;
	}

	public String getLevelName(int id) {
		return GameConfig.SKILL_LEVEL_NAMES[id];
	}

	public int getLocalX() {
		return getX() - 8 * getMapRegionX();
	}

	public int getLocalY() {
		return getY() - 8 * getMapRegionY();
	}

	public int getMapRegionX() {
		return mapRegionX;
	}

	public int getMapRegionY() {
		return mapRegionY;
	}

	public int[] getNewWalkCmdX() {
		return newWalkCmdX;
	}

	public int[] getNewWalkCmdY() {
		return newWalkCmdY;
	}

	public synchronized void getNextPlayerMovement() {
		mapRegionDidChange = false;
		didTeleport = false;
		dir1 = dir2 = -1;

		if (teleportToX != -1 && teleportToY != -1) {
			mapRegionDidChange = true;
			if (mapRegionX != -1 && mapRegionY != -1) {
				final int relX = teleportToX - mapRegionX * 8, relY = teleportToY
						- mapRegionY * 8;
				if (relX >= 2 * 8 && relX < 11 * 8 && relY >= 2 * 8
						&& relY < 11 * 8) {
					mapRegionDidChange = false;
				}
			}
			if (mapRegionDidChange) {
				mapRegionX = (teleportToX >> 3) - 6;
				mapRegionY = (teleportToY >> 3) - 6;
			}
			currentX = teleportToX - 8 * mapRegionX;
			currentY = teleportToY - 8 * mapRegionY;
			absX = teleportToX;
			absY = teleportToY;
			resetWalkingQueue();

			teleportToX = teleportToY = -1;
			didTeleport = true;
		} else {
			dir1 = getNextWalkingDirection();
			if (dir1 == -1) {
				return;
			}
			if (isRunning) {
				dir2 = getNextWalkingDirection();
			}
			// entity.sendMessage("Cycle Ended");
			int deltaX = 0, deltaY = 0;
			if (currentX < 2 * 8) {
				deltaX = 4 * 8;
				mapRegionX -= 4;
				mapRegionDidChange = true;
			} else if (currentX >= 11 * 8) {
				deltaX = -4 * 8;
				mapRegionX += 4;
				mapRegionDidChange = true;
			}
			if (currentY < 2 * 8) {
				deltaY = 4 * 8;
				mapRegionY -= 4;
				mapRegionDidChange = true;
			} else if (currentY >= 11 * 8) {
				deltaY = -4 * 8;
				mapRegionY += 4;
				mapRegionDidChange = true;
			}

			if (mapRegionDidChange) {
				currentX += deltaX;
				currentY += deltaY;
				for (int i = 0; i < GameConfig.WALK_QUEUE_SIZE; i++) {
					walkingQueueX[i] += deltaX;
					walkingQueueY[i] += deltaY;
				}
			}

		}
	}

	public int getNextWalkingDirection() {
		if (wQueueReadPtr == wQueueWritePtr) {
			return -1;
		}
		int dir;
		do {
			dir = Misc.direction(currentX, currentY,
					walkingQueueX[wQueueReadPtr], walkingQueueY[wQueueReadPtr]);
			if (dir == -1) {
				wQueueReadPtr = (wQueueReadPtr + 1)
						% GameConfig.WALK_QUEUE_SIZE;
			} else if ((dir & 1) != 0) {
				println_debug("Invalid waypoint in walking queue!");
				resetWalkingQueue();
				return -1;
			}
		} while (dir == -1 && wQueueReadPtr != wQueueWritePtr);
		if (dir == -1) {
			return -1;
		}
		dir >>= 1;
		currentX += Misc.directionDeltaX[dir];
		currentY += Misc.directionDeltaY[dir];
		oldX = absX;
		oldY = absY;
		absX += Misc.directionDeltaX[dir];
		absY += Misc.directionDeltaY[dir];
		return dir;
	}

	public int getPoints() {
		return points;
	}

	public int getQuestPoints() {
		return qp;
	}

	public String getRankName() {
		switch (playerRights) {
		case 1:
			return "Moderator";
		case 2:
			return "Administrator";
		case 3:
			return "Owner";
		}
		return "Player";
	}

	public String getSpellName(int id) {
		switch (id) {
		case 0:
			return "Air Strike";
		case 1:
			return "Water Strike";
		case 2:
			return "Earth Strike";
		case 3:
			return "Fire Strike";
		case 4:
			return "Air Bolt";
		case 5:
			return "Water Bolt";
		case 6:
			return "Earth Bolt";
		case 7:
			return "Fire Bolt";
		case 8:
			return "Air Blast";
		case 9:
			return "Water Blast";
		case 10:
			return "Earth Blast";
		case 11:
			return "Fire Blast";
		case 12:
			return "Air Wave";
		case 13:
			return "Water Wave";
		case 14:
			return "Earth Wave";
		case 15:
			return "Fire Wave";
		case 32:
			return "Shadow Rush";
		case 33:
			return "Smoke Rush";
		case 34:
			return "Blood Rush";
		case 35:
			return "Ice Rush";
		case 36:
			return "Shadow Burst";
		case 37:
			return "Smoke Burst";
		case 38:
			return "Blood Burst";
		case 39:
			return "Ice Burst";
		case 40:
			return "Shadow Blitz";
		case 41:
			return "Smoke Blitz";
		case 42:
			return "Blood Blitz";
		case 43:
			return "Ice Blitz";
		case 44:
			return "Shadow Barrage";
		case 45:
			return "Smoke Barrage";
		case 46:
			return "Blood Barrage";
		case 47:
			return "Ice Barrage";
		default:
			return "Select Spell";
		}
	}

	public Long getTotalExp() {
		long total = 0;
		for (int i = 0; i <= 21; i++) {
			total += playerXP[i];
		}
		return total;
	}

	public Short getTotalLevel() {
		short total = 0;
		for (int i = 0; i <= 21; i++) {
			total += getLevelForXP(playerXP[i]);
		}
		if (total > 2079) {
			total = 2079;
		}
		return total;
	}

	public int getX() {
		return absX;
	}

	public int getXPForLevel(int level) {
		int points = 0;
		int output = 0;
		for (int lvl = 1; lvl <= level; lvl++) {
			points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
			if (lvl >= level) {
				return output;
			}
			output = (int) Math.floor(points / 4);
		}
		return 0;
	}

	public int getY() {
		return absY;
	}

	public int getZ() {
		return absZ;
	}

	public void gfx0(int gfx) {
		if (isDoingSkillcapeAnim) {
			return;
		}
		getUpdateFlags().mask100var1 = gfx;
		getUpdateFlags().mask100var2 = 65536;
		getUpdateFlags().mask100update = true;
		updateRequired = true;
	}

	public void gfx100(int gfx) {
		getUpdateFlags().mask100var1 = gfx;
		getUpdateFlags().mask100var2 = 6553600;
		getUpdateFlags().mask100update = true;
		updateRequired = true;
	}

	public void giveTitle(int titleId) {
		this.playerTitle = titleId;
		this.getUpdateFlags().setAppearanceUpdateRequired(true);
	}

	public boolean goodDistance(int objectX, int objectY, int playerX,
			int playerY, int distance) {
		return objectX - playerX <= distance && objectX - playerX >= -distance
				&& objectY - playerY <= distance
				&& objectY - playerY >= -distance;
	}

	public void handleHitMask(int damage) {
		if (!getUpdateFlags().hitUpdateRequired) {
			getUpdateFlags().hitUpdateRequired = true;
			hitDiff = damage;
		} else if (!getUpdateFlags().hitUpdateRequired2) {
			getUpdateFlags().hitUpdateRequired2 = true;
			hitDiff2 = damage;
		}
		updateRequired = true;
	}

	public boolean inArea(int x, int y, int x1, int y1) {
		if (absX > x && absX < x1 && absY < y && absY > y1) {
			return true;
		}
		return false;
	}

	public boolean inBarrows() {
		if (absX > 3520 && absX < 3598 && absY > 9653 && absY < 9750) {
			return true;
		}
		return false;
	}

	public boolean inSnow() {
		if (absX > 2800 && absX < 2900 && absY > 3750 && absY < 3950) {
			return true;
		}
		return false;
	}

	public boolean inCastleWars() {
		if (absX >= 2368 && absX <= 2431 && absY >= 3071 && absY <= 3135
				|| absX >= 2368 && absX <= 2401 && absY >= 9501 && absY <= 9528
				|| absX >= 2391 && absX <= 2410 && absY >= 9494 && absY <= 9513
				|| absX >= 2401 && absX <= 2430 && absY >= 9481 && absY <= 9504) {
			return true;
		}
		return false;
	}

	public boolean inDuelArena() {
		if (absX > 3322 && absX < 3394 && absY > 3195 && absY < 3291
				|| absX > 3311 && absX < 3323 && absY > 3223 && absY < 3248) {
			return true;
		}
		return false;
	}

	public boolean inFightCaves() {
		return absX >= 2360 && absX <= 2445 && absY >= 5045 && absY <= 5125;
	}

	public boolean inFightPits() {
		if (absX >= 2370 && absY >= 5128 && absX <= 2426 && absY <= 5167) {
			return true;
		}
		return false;
	}

	public boolean inFightPitsWait() {
		if (absX >= 2393 && absY >= 5169 && absX <= 2405 && absY <= 5175) {
			return true;
		}
		return false;
	}

	public abstract void initialize();

	public boolean inJailArea() {
		return absX > 2086 && absY > 4418 && absX < 2106 && absY < 4436;
	}

	public boolean inMainRoom() {
		if (absX >= 2436 && absX <= 2445 && absY >= 3080 && absY <= 3097) {
			return true;
		}
		return false;
	}

	public boolean inMinigame() {
		return this.inCastleWars() || this.inDuelArena() || this.inFightPits()
				|| this.inFightCaves();
	}

	public boolean inPcBoat() {
		return absX >= 2660 && absX <= 2663 && absY >= 2638 && absY <= 2643;
	}

	public boolean inPcGame() {
		return absX >= 2624 && absX <= 2690 && absY >= 2550 && absY <= 2619;
	}

	public boolean inPestControl() {
		if (absX >= 2624 && absX <= 2690 && absY >= 2550 && absY <= 2619) {
			return true;
		}
		return false;
	}

	public boolean inPvp() {
		return absX >= 3220 && absX <= 3244 && absY >= 5080 && absY <= 5102;
	}

	public boolean inPirateHouse() {
		return absX >= 3038 && absX <= 3044 && absY >= 3949 && absY <= 3959;
	}

	/**
	 * SouthWest, NorthEast, SouthWest, NorthEast
	 */

	public boolean inSaradominRespawn() {
		return absX >= 2423 && absX <= 2431 && absY >= 3072 && absY <= 3080;
	}

	public boolean inTradeArea() {
		if (absX > 2110 && absX < 2160 && absY > 4895 && absY < 4930) {
			return true;
		}
		return false;
	}

	public boolean inWaitingRoom() {
		if (absX >= 2410 && absX <= 2435 && absY >= 9508 && absY <= 9535
				|| absX >= 2368 && absX <= 2394 && absY >= 9480 && absY <= 9497) {
			return true;
		}
		return false;
	}

	public boolean inWild() {
		if (absX > 2941 && absX < 3392 && absY > 3522 && absY < 3980
				|| absX > 2941 && absX < 3392 && absY > 9918 && absY < 10366) {
			return true;
		}
		return false;
	}

	public boolean inZamorakRespawn() {
		return absX >= 2368 && absX <= 2376 && absY >= 3127 && absY <= 3135;
	}

	public boolean isAutoButton(int button) {
		for (int j = 0; j < autocastIds.length; j += 2) {
			if (autocastIds[j] == button) {
				return true;
			}
		}
		return false;
	}

	public boolean isChatTextUpdateRequired() {
		return getUpdateFlags().chatTextUpdateRequired;
	}

	public boolean isClanOwner() {
		if (clanId > 0) {
			return World.getClanChat().clans[clanId].owner
					.equalsIgnoreCase(playerName);
		}
		return false;
	}

	public boolean isDonator() {
		return FileConfig.donators.contains(playerName);
	}

	public boolean isHitUpdateRequired() {
		return getUpdateFlags().hitUpdateRequired;
	}

	public boolean isInEdge() {
		if (absX > 3084 && absX < 3111 && absY > 3483 && absY < 3509) {
			return true;
		}
		return false;
	}

	public boolean isInMaze() {
		if (absX >= 2885 && absX <= 2939 && absY >= 4549 && absY <= 4603) {
			return true;
		}
		return false;
	}

	public boolean isInTut() {
		if (absX >= 2625 && absX <= 2687 && absY >= 4670 && absY <= 4735) {
			return true;
		}
		return false;
	}

	public boolean isMuted() {
		return FileConfig.mutedAccounts.contains(playerName)
				|| FileConfig.mutedHosts.contains(connectedFrom);
	}

	public boolean isNewWalkCmdIsRunning() {
		return newWalkCmdIsRunning;
	}

	public boolean isSkywalking() {
		return !validPosition();
	}

	public boolean isWieldingItems() {
		for (int element : playerEquipment) {
			if (element > 0) {
				return true;
			}
		}
		return false;
	}

	public synchronized void postProcessing() {
		if (newWalkCmdSteps > 0) {
			final int firstX = getNewWalkCmdX()[0], firstY = getNewWalkCmdY()[0];

			int lastDir = 0;
			boolean found = false;
			numTravelBackSteps = 0;
			int ptr = wQueueReadPtr;
			int dir = Misc.direction(currentX, currentY, firstX, firstY);
			if (dir != -1 && (dir & 1) != 0) {
				do {
					lastDir = dir;
					if (--ptr < 0) {
						ptr = GameConfig.WALK_QUEUE_SIZE - 1;
					}

					travelBackX[numTravelBackSteps] = walkingQueueX[ptr];
					travelBackY[numTravelBackSteps++] = walkingQueueY[ptr];
					dir = Misc.direction(walkingQueueX[ptr],
							walkingQueueY[ptr], firstX, firstY);
					if (lastDir != dir) {
						found = true;
						break;
					}

				} while (ptr != wQueueWritePtr);
			} else {
				found = true;
			}

			if (!found) {
				println_debug("Fatal: couldn't find connection vertex! Dropping packet.");
			} else {
				wQueueWritePtr = wQueueReadPtr;

				addToWalkingQueue(currentX, currentY);

				if (dir != -1 && (dir & 1) != 0) {

					for (int i = 0; i < numTravelBackSteps - 1; i++) {
						addToWalkingQueue(travelBackX[i], travelBackY[i]);
					}
					final int wayPointX2 = travelBackX[numTravelBackSteps - 1], wayPointY2 = travelBackY[numTravelBackSteps - 1];
					int wayPointX1, wayPointY1;
					if (numTravelBackSteps == 1) {
						wayPointX1 = currentX;
						wayPointY1 = currentY;
					} else {
						wayPointX1 = travelBackX[numTravelBackSteps - 2];
						wayPointY1 = travelBackY[numTravelBackSteps - 2];
					}

					dir = Misc.direction(wayPointX1, wayPointY1, wayPointX2,
							wayPointY2);
					if (dir == -1 || (dir & 1) != 0) {
						println_debug("Fatal: The walking queue is corrupt! wp1=("
								+ wayPointX1
								+ ", "
								+ wayPointY1
								+ "), "
								+ "wp2=("
								+ wayPointX2
								+ ", "
								+ wayPointY2
								+ ")");
					} else {
						dir >>= 1;
						found = false;
						int x = wayPointX1, y = wayPointY1;
						while (x != wayPointX2 || y != wayPointY2) {
							x += Misc.directionDeltaX[dir];
							y += Misc.directionDeltaY[dir];
							if ((Misc.direction(x, y, firstX, firstY) & 1) == 0) {
								found = true;
								break;
							}
						}
						if (!found) {
							println_debug("Fatal: Internal error: unable to determine connection vertex!"
									+ "  wp1=("
									+ wayPointX1
									+ ", "
									+ wayPointY1
									+ "), wp2=("
									+ wayPointX2
									+ ", "
									+ wayPointY2
									+ "), "
									+ "first=("
									+ firstX + ", " + firstY + ")");
						} else {
							addToWalkingQueue(wayPointX1, wayPointY1);
						}
					}
				} else {
					for (int i = 0; i < numTravelBackSteps; i++) {
						addToWalkingQueue(travelBackX[i], travelBackY[i]);
					}
				}

				for (int i = 0; i < newWalkCmdSteps; i++) {
					addToWalkingQueue(getNewWalkCmdX()[i], getNewWalkCmdY()[i]);
				}

			}

			isRunning = isNewWalkCmdIsRunning() || isRunning2;
		}
	}

	public void preProcessing() {
		newWalkCmdSteps = 0;
	}

	public void println(String str) {
		// System.out.println("[player-"+playerId+"]: "+str);
	}

	public void println_debug(String str) {
		// System.out.println("[player-"+playerId+"]: "+str);
	}

	@Override
	public abstract void process();

	public abstract boolean processQueuedPackets();

	public void putInCombat(int attacker) {
		underAttackBy = attacker;
		logoutDelay = System.currentTimeMillis();
		singleCombatDelay = System.currentTimeMillis();
	}

	public void resetDialogueOptions() {
		dialogueOption1 = 0;
		dialogueOption2 = 0;
		dialogueOption3 = 0;
		dialogueOption4 = 0;
		dialogueOption5 = 0;
	}

	public void resetWalkingQueue() {
		wQueueReadPtr = wQueueWritePtr = 0;

		for (int i = 0; i < GameConfig.WALK_QUEUE_SIZE; i++) {
			walkingQueueX[i] = currentX;
			walkingQueueY[i] = currentY;
		}
	}

	public boolean samePlayer() {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (j == playerId) {
				continue;
			}
			if (PlayerHandler.players[j] != null) {
				if (PlayerHandler.players[j].playerName
						.equalsIgnoreCase(playerName)) {
					disconnected = true;
					return true;
				}
			}
		}
		return false;
	}

	public void setChatText(byte chatText[]) {
		this.chatText = chatText;
	}

	public void setChatTextColor(int chatTextColor) {
		this.chatTextColor = chatTextColor;
	}

	public void setChatTextEffects(int chatTextEffects) {
		this.chatTextEffects = chatTextEffects;
	}

	public void setChatTextSize(byte chatTextSize) {
		this.chatTextSize = chatTextSize;
	}

	public void setChatTextUpdateRequired(boolean chatTextUpdateRequired) {
		this.getUpdateFlags().chatTextUpdateRequired = chatTextUpdateRequired;
	}

	public void setForceMovement(final int x2, final int y2, boolean x1,
			boolean y1, final int speed1, final int speed2,
			final int direction, final int emote) {
		canWalk = false;
		this.x1 = currentX;
		this.y1 = currentY;
		this.x2 = x1 ? currentX + x2 : currentX - x2;
		this.y2 = y1 ? currentY + y2 : currentY - y2;
		this.speed1 = speed1;
		this.speed2 = speed2;
		this.direction = direction;
		updateRequired = true;
		getUpdateFlags().forceMovementUpdateRequired = true;
		final Client c = (Client) this;
		// startAnimation(emote);
		World.getSynchronizedTaskScheduler().schedule(
				new Task((x2 + y2), false) {
					@Override
					protected void execute() {
						c.getCombat().getPlayerAnimIndex(
								ItemAssistant.getItemName(
										playerEquipment[playerWeapon])
										.toLowerCase());
						// c.getFunction().movePlayer(c.absX, c.absY - 7,
						// c.absZ);
						updateRequired = true;
						getUpdateFlags().forceMovementUpdateRequired = false;
						canWalk = true;
						stop();
					}
				});
	}

	public boolean fading;

	public void setHitDiff(int hitDiff) {
		this.hitDiff = hitDiff;
	}

	public void setHitDiff2(int hitDiff2) {
		this.hitDiff2 = hitDiff2;
	}

	public void setHitUpdateRequired(boolean hitUpdateRequired) {
		this.getUpdateFlags().hitUpdateRequired = hitUpdateRequired;
	}

	public void setHitUpdateRequired2(boolean hitUpdateRequired2) {
		this.getUpdateFlags().hitUpdateRequired2 = hitUpdateRequired2;
	}

	public void setInStreamDecryption(ISAAC inStreamDecryption) {
	}

	public void setNewWalkCmdIsRunning(boolean newWalkCmdIsRunning) {
		this.newWalkCmdIsRunning = newWalkCmdIsRunning;
	}

	public void setNewWalkCmdX(int newWalkCmdX[]) {
		this.newWalkCmdX = newWalkCmdX;
	}

	public void setNewWalkCmdY(int newWalkCmdY[]) {
		this.newWalkCmdY = newWalkCmdY;
	}

	public void setOutStreamDecryption(ISAAC outStreamDecryption) {
	}

	/**
	 * Animations
	 **/
	public void startAnimation(int animId) {
		if (wearing2h() && animId == 829 || animId < 2) {
			return;
		}
		getUpdateFlags().animationRequest = animId;
		getUpdateFlags().animationWaitCycles = 0;
		updateRequired = true;
	}

	public void startAnimation(int animId, int time) {
		getUpdateFlags().animationRequest = animId;
		getUpdateFlags().animationWaitCycles = time;
		updateRequired = true;
	}

	public abstract void startConstantTasks();

	public void stopMovement() {
		if (teleportToX <= 0 && teleportToY <= 0) {
			teleportToX = absX;
			teleportToY = absY;
		}
		newWalkCmdSteps = 0;
		getNewWalkCmdX()[0] = getNewWalkCmdY()[0] = travelBackX[0] = travelBackY[0] = 0;
		getNextPlayerMovement();
	}

	public void turnPlayerTo(int pointX, int pointY) {
		getUpdateFlags().focusPointX = 2 * pointX + 1;
		getUpdateFlags().focusPointY = 2 * pointY + 1;
		updateRequired = true;
	}

	public void turnToObject() {
		turnPlayerTo(objectX, objectY);
	}

	public abstract void update();

	public void updatePlayerMovement(Stream str) {
		synchronized (this) {
			if (dir1 == -1) {
				if (updateRequired || isChatTextUpdateRequired()) {

					str.writeBits(1, 1);
					str.writeBits(2, 0);
				} else {
					str.writeBits(1, 0);
				}
			} else if (dir2 == -1) {

				str.writeBits(1, 1);
				str.writeBits(2, 1);
				str.writeBits(3, Misc.xlateDirectionToClient[dir1]);
				str.writeBits(1,
						updateRequired || isChatTextUpdateRequired() ? 1 : 0);
			} else {

				str.writeBits(1, 1);
				str.writeBits(2, 2);
				str.writeBits(3, Misc.xlateDirectionToClient[dir1]);
				str.writeBits(3, Misc.xlateDirectionToClient[dir2]);
				str.writeBits(1,
						updateRequired || isChatTextUpdateRequired() ? 1 : 0);
			}
		}
	}

	public void updateThisPlayerMovement(Stream str) {
		synchronized (this) {
			if (mapRegionDidChange) {
				str.createFrame(73);
				str.writeWordA(mapRegionX + 6);
				str.writeWord(mapRegionY + 6);
			}

			if (didTeleport) {
				str.createFrameVarSizeWord(81);
				str.initBitAccess();
				str.writeBits(1, 1);
				str.writeBits(2, 3);
				str.writeBits(2, absZ);
				str.writeBits(1, 1);
				str.writeBits(1, updateRequired ? 1 : 0);
				str.writeBits(7, currentY);
				str.writeBits(7, currentX);
				return;
			}

			if (dir1 == -1) {
				// don't have to update the character position, because we're
				// just standing
				str.createFrameVarSizeWord(81);
				str.initBitAccess();
				isMoving = false;
				if (updateRequired) {
					// tell client there's an update block appended at the end
					str.writeBits(1, 1);
					str.writeBits(2, 0);
				} else {
					str.writeBits(1, 0);
				}
				if (DirectionCount < 50) {
					DirectionCount++;
				}
			} else {
				DirectionCount = 0;
				str.createFrameVarSizeWord(81);
				str.initBitAccess();
				str.writeBits(1, 1);

				if (dir2 == -1) {
					isMoving = true;
					str.writeBits(2, 1);
					str.writeBits(3, Misc.xlateDirectionToClient[dir1]);
					if (updateRequired) {
						str.writeBits(1, 1);
					} else {
						str.writeBits(1, 0);
					}
				} else {
					isMoving = true;
					str.writeBits(2, 2);
					str.writeBits(3, Misc.xlateDirectionToClient[dir1]);
					str.writeBits(3, Misc.xlateDirectionToClient[dir2]);
					if (updateRequired) {
						str.writeBits(1, 1);
					} else {
						str.writeBits(1, 0);
					}
				}
			}
		}
	}

	public boolean validPosition() {
		return absZ == 1 && absX >= 2419 && absX <= 2431 && absY >= 3072
				&& absY <= 3083 || absZ == 1 && absX >= 2368 && absX <= 2380
				&& absY >= 3124 && absY <= 3135 || absZ == 2 && absX >= 2423
				&& absX <= 2431 && absY >= 3072 && absY <= 3081 || absZ == 2
				&& absX >= 2368 && absX <= 2376 && absY >= 3126 && absY <= 3135
				|| absZ == 3 && absX >= 2425 && absX <= 2430 && absY >= 3073
				&& absY <= 3077 || absZ == 3 && absX >= 2369 && absX <= 2374
				&& absY >= 3130 && absY <= 3134;
	}

	public boolean wearing2h() {
		final Client client = (Client) this;
		client.getItems();
		final String s = ItemAssistant
				.getItemName(client.playerEquipment[client.playerWeapon]);
		if (s.contains("2h")) {
			return true;
		} else if (s.contains("godsword")) {
			return true;
		}
		return false;
	}

	public boolean withinDistance(int x, int y) {
		final int deltaX = x - absX, deltaY = y - absY;
		return deltaX <= 15 && deltaX >= -16 && deltaY <= 15 && deltaY >= -16;
	}

	public boolean withinDistance(int x, int y, int distance) {
		final int deltaX = x - absX, deltaY = y - absY;
		return deltaX <= 15 && deltaX >= -16 && deltaY <= 15 && deltaY >= -16;
	}

	public boolean withinDistance(NPC npc) {
		if (absZ != npc.absZ) {
			return false;
		}
		if (npc.needRespawn == true) {
			return false;
		}
		final int deltaX = npc.absX - absX, deltaY = npc.absY - absY;
		return deltaX <= 15 && deltaX >= -16 && deltaY <= 15 && deltaY >= -16;
	}

	public boolean withinDistance(Player otherPlr) {
		if (absZ != otherPlr.absZ) {
			return false;
		}
		final int deltaX = otherPlr.absX - absX, deltaY = otherPlr.absY - absY;
		return deltaX <= 15 && deltaX >= -16 && deltaY <= 15 && deltaY >= -16;
	}

	public boolean withinInteractionDistance(int x, int y, int z) {
		if (absZ != z) {
			return false;
		}
		final Client client = (Client) this;
		final int deltaX = x - client.getX(), deltaY = y - client.getY();
		return deltaX <= 4 && deltaX >= -4 && deltaY <= 4 && deltaY >= -4;
	}

}