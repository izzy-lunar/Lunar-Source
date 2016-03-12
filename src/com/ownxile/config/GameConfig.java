package com.ownxile.config;

import com.ownxile.rs2.Entity;
import com.ownxile.rs2.Entity.EntityType;
import com.ownxile.rs2.world.games.CastleWars;

/**
 * @author Robbie holds general game constants
 */
public class GameConfig {

	public static int getClanCrown(int rights) {
		if (rights > 3)
			return 0;
		return rights;
	}

	public static final String[] KILL_VERBS = { "destroyed", "smacked",
			"wrecked", "slayed", "ruined", "crushed", "owned", "smoked",
			"toked", "seen off" };

	public static final boolean ADMIN_CAN_SELL_ITEMS = false;
	public static final boolean ADMIN_CAN_TRADE = false;
	public static final boolean ADMIN_DROP_ITEMS = false;
	public static final String AL_KHARID = "";
	public static final int AL_KHARID_X = 2662;
	public static final int AL_KHARID_Y = 3647;
	public static final int ANNAKARL_X = 3288;
	public static final int ANNAKARL_Y = 3886;
	public static final String ARDOUGNE = "";
	public static final int ARDOUGNE_X = 2662;
	public static final int ARDOUGNE_Y = 3305;
	public static final int BANK_SIZE = 352;
	public static final int[] BANNED_ITEMS = { 10566, 10637, 10778, 9948, 9949,
			10646, 11789, 11790, 10296 };
	public static final int BUFFER_SIZE = 10000;
	public static final String CAMELOT = "";
	public static final int CAMELOT_X = 2757;
	public static final int CAMELOT_Y = 3477;
	public static final EntityType CANNON_TARGET = Entity.EntityType.NPC;
	public static final int CARRALLANGAR_X = 3156;
	public static final int CARRALLANGAR_Y = 3666;
	public static final boolean CLIP_NPCS = true;
	public static final boolean COMBAT_LEVEL_DIFFERENCE = true;
	public static final int CONNECTION_DELAY = 100;
	public static final boolean CORRECT_ARROWS = true;
	public static final boolean CRYSTAL_BOW_DEGRADES = true;
	public static final int DAREEYAK_X = 3161;
	public static final int DAREEYAK_Y = 3671;

	public static final boolean PRINT_PLUGIN_DIRECTORIES = false;
	public static final boolean DISABLE_SAME_IP_TRADING = true;

	public static final int[] DONATOR_ITEMS = {};

	public static final int[] QUEST_ID = { 7332, 7333, 7334, 7336, 7383, 7339,
			7338, 7340, 7346, 7341, 7342, 7337, 7343, 7335, 7344, 7345, 7347,
			7348, 12772, 673, 7352, 17510, 7353, 12129, 8438, 12852, 7353,
			7354, 7355, 7356 };

	public static final int[] NON_SPAWNABLE = { 11694, 11695, 11726, 11727,
			11724, 11725, 6570, 1038, 1039, 1040, 1041, 1042, 1043, 1044, 1045,
			1046, 1047, 1048, 1049, 1050, 1051, 1053, 1054, 1055, 1056, 1057,
			1058, 11483, 11484, 14485, 10330, 10331, 10332, 10333, 10334,
			10335, 10336, 10337, 10338, 10339, 10340, 10341, 10342, 10343,
			10344, 10345, 10346, 10347, 10348, 10349, 10350, 10351, 10352,
			10353, 4024, 4025, 4026, 4027, 4028, 4029, 4030 };

	public static boolean isNonSpawnable(int itemId) {
		for (int i = 0; i < NON_SPAWNABLE.length; i++) {
			if (itemId == NON_SPAWNABLE[i]) {
				return true;
			}
		}
		return false;
	}

	/*
	 * public static DonatorItem dragonClaws = new DonatorItem(11777,
	 * "./data/subscribers/dragon_claws_users.txt"); public static DonatorItem
	 * armadylGodsword = new DonatorItem(11694,
	 * "./data/subscribers/armadyl_godsword_users.txt");
	 */
	public static final String EDGEVILLE = "";

	public static final int FAILED_ATTEMPTS = 15;

	public static final String FALADOR = "";
	public static final int FALADOR_X = 2964;
	public static final int FALADOR_Y = 3378;
	public static final int[] FUN_WEAPONS = { 2460, 2461, 2462, 2463, 2464,
			2465, 2466, 2467, 2468, 2469, 2470, 2471, 2471, 2473, 2474, 2475,
			2476, 2477 };

	public static final String GAME_UPTIME_FILE = "./data/logs/game_uptime.txt";
	public static final int GHORROCK_X = 2977;
	public static final int GHORROCK_Y = 3873;
	// to cast spell
	public static final int GOD_SPELL_CHARGE = 300000; // how long does god
	public static final int INCREASE_SPECIAL_AMOUNT = 15000; // how fast your

	public static final int INTERACTIVE_OBJECT_COUNT = 8000;

	public static final int ITEM_LIMIT = 16000;
	public static final int[] ITEM_SELLABLE = { 15486, 13887, 13893, 6199,
			6700, 13890, 13869, 13896, 13899, 13902, 11702, 11703, 11704,
			11705, 10735, 4597, 7433, 1411, 4677, 4678, 1554, 6858, 6859, 3841,
			3842, 3843, 3844, 3839, 3840, 8844, 8845, 8846, 8847, 8848, 8849,
			8850, 10548, 10551, 6570, 7462, 7461, 7460, 7459, 7458, 7457, 7456,
			7455, 7454, 8837, 8838, 8839, 8840, 8842, 11663, 11664, 11665,
			10499, 9748, 9754, 9751, 9769, 9757, 9760, 9763, 9802, 9808, 9784,
			9799, 9805, 9781, 9796, 9793, 9775, 9772, 9778, 9787, 9811, 9766,
			9749, 9755, 9752, 9770, 9758, 9761, 9764, 9803, 9809, 9785, 9800,
			9806, 9782, 9797, 9794, 9776, 9773, 9779, 9788, 9812, 9767, 9747,
			9753, 9750, 9768, 9756, 9759, 9762, 9801, 9807, 9783, 9798, 9804,
			9780, 9795, 9792, 9774, 9771, 9777, 9786, 9810, 9765, 995, 14876,
			14877, 14878, 14879, 14880, 14881, 14882, 14883, 14884, 14885,
			14886, 14887, 14888, 14889, 14890, 14891, 14892 };

	public static final boolean ITEMS_REQUIRE_STATS = true;
	public static final String KARAMJA = "";
	public static final int KARAMJA_X = 2772;
	public static final int KARAMJA_Y = 3223;

	public static final boolean KEEP_UNTRADEABLES_IN_INVENTORY = false;
	public static final int KHARYRLL_X = 3492;
	public static final int KHARYRLL_Y = 3471;
	public static final int LASSAR_X = 3006;

	public static final int LASSAR_Y = 3471;

	public static final String[] LOGIN_MESSAGE = {
			"You have begun in the home city of @blu@Edgeville@bla@.",
			"We suggest you speak to @dre@The Lunar Guide@bla@ who will attempt to.",
			"answer any questions you may have. Good luck and if you need",
			"further support then visit the forums. Have fun out there." };

	public static final String LOGIN_SERVER_HOST = "localhost";
	public static final int[] LOTTERY_ITEMS = { 4716, 4718, 4720, 4722, 4710,
			4712, 4736, 4738, 4745, 4759, 4714 };

	public static final String LUMBY = "";

	public static final int LUMBY_X = 3222;

	public static final int LUMBY_Y = 3218;
	public static final String MAGEBANK = "";
	public static final int MAGEBANK_X = 3052;
	public static final int MAGEBANK_Y = 3498;

	// level to use
	// different
	// prayers
	public static final boolean MAGIC_LEVEL_REQUIRED = true; // need magic level
	public static final int MAX_CLANS = 200;
	public static final int MAX_PLAYERS = 4096;// causes some glitch with
												// specials on certain npcs when
												// lowered, re-evaluatepls
	public static final int MAX_QP = 38;

	public static final int MAXITEM_AMOUNT = Integer.MAX_VALUE;

	public static int MESSAGE_DELAY = 6000;

	public static final int NEW_PLAYER_WAIT = 0;
	public static final String NO_PRAYER_MESSAGE = "You have run out of prayer points, you must recharge at an altar.";
	public static final int NO_TELEPORT_WILD_LEVEL = 20; // level you can't tele
	public static final int NPC_BYTES = 14;
	public static final int NPC_FOLLOW_DISTANCE = 10; // how far can the npc
	public static final int NPC_RANDOM_WALK_DISTANCE = 3; // the square created

	public static final String[] OWNERS = { "Izzy" };

	public static final int packetSizes[] = { 0, 0, 0, 1, -1, 0, 0, 0, 0, 0, // 0
			0, 0, 0, 0, 8, 0, 6, 2, 2, 0, // 10
			0, 2, 0, 6, 0, 12, 0, 0, 0, 0, // 20
			0, 0, 0, 0, 0, 8, 4, 0, 0, 2, // 30
			2, 6, 0, 6, 0, -1, 0, 0, 0, 0, // 40
			0, 0, 0, 12, 0, 0, 0, 8, 8, 12, // 50
			8, 8, 0, 0, 0, 0, 0, 0, 0, 0, // 60
			6, 0, 2, 2, 8, 6, 0, -1, 0, 6, // 70
			0, 0, 0, 0, 0, 1, 4, 6, 0, 0, // 80
			0, 0, 0, 0, 0, 3, 0, 0, -1, 0, // 90
			0, 13, 0, -1, 0, 0, 0, 0, 0, 0,// 100
			0, 0, 0, 0, 0, 0, 0, 6, 0, 0, // 110
			1, 0, 6, 0, 0, 0, -1, 0, 2, 6, // 120
			0, 4, 6, 8, 0, 6, 0, 0, 0, 2, // 130
			0, 0, 0, 0, 0, 6, 0, 0, 0, 0, // 140
			0, 0, 1, 2, 0, 2, 6, 0, 0, 0, // 150
			0, 0, 0, 0, -1, -1, 0, 0, 0, 0,// 160
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 170
			0, 8, 0, 3, 0, 2, 0, 0, 8, 1, // 180
			0, 0, 12, 0, 0, 0, 0, 0, 0, 0, // 190
			2, 0, 0, 0, 0, 0, 0, 0, 4, 0, // 200
			4, 0, 0, 0, 7, 8, 0, 0, 10, 0, // 210
			0, 0, 0, 0, 0, 0, -1, 0, 6, 0, // 220
			1, 0, 0, 0, 6, 0, 6, 8, 1, 0, // 230
			0, 4, 0, 0, 0, 0, -1, 0, -1, 4,// 240
			0, 0, 6, 6, 0, 0, 0 // 250
	};
	public static final int PADDEWWA_X = 3098;
	public static final int PADDEWWA_Y = 9884;
	public static final char playerNameXlateTable[] = { '_', 'a', 'b', 'c',
			'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p',
			'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2',
			'3', '4', '5', '6', '7', '8', '9', '<' };
	public static final boolean PRAYER_LEVEL_REQUIRED = true; // need prayer
	public static final int PVP_DIFFERENCE = 12;
	public static int REGION_AMOUNT = 70;
	public static int REGION_DECREASE = 6;
	public static int REGION_NORMALREGION = 32;
	public static int REGION_SIZE = 0;
	public static boolean sendServerPackets = false;
	public static final int RESPAWN_X = 1803;
	public static final int RESPAWN_Y = 3784;
	// spell charge last?
	public static final boolean RUNES_REQUIRED = true;
	public static final int SENNTISTEN_X = 3322;

	// ancient

	public static final int SENNTISTEN_Y = 3336;
	public static final boolean SERVER_DEBUG = false;

	public static final String SERVER_NAME = "Lunar";

	// last for.
	public static final boolean SINGLE_AND_MULTI_ZONES = true; // multi and

	public static final String SKILL_LEVEL_NAMES[] = { "Attack", "Defence",
			"Strength", "Hitpoints", "Ranged", "Prayer", "Magic", "Cooking",
			"Woodcutting", "Fletching", "Fishing", "Firemaking", "Crafting",
			"Smithing", "Mining", "Herblore", "Agility", "Thieving", "Slayer",
			"Farming", "Runecrafting", "Hunter", "Summoning", "Construction",
			"Dungeoneering" };
	// on and above
	public static final int SKULL_TIMER = 1200; // how long does the skull last?

	/**
	 * Slayer Variables
	 */
	public static final int[][] SLAYER_TASKS = { { 1, 87, 90, 4, 5 }, // low
																		// tasks
			{ 6, 7, 8, 9, 10 }, // med tasks
			{ 11, 12, 13, 14, 15 }, // high tasks
			{ 1, 1, 15, 20, 25 }, // low reqs
			{ 30, 35, 40, 45, 50 }, // med reqs
			{ 60, 75, 80, 85, 90 } }; // high reqs

	public static final String[] SUPER_ADMIN_PRIVILEGES = { "Izzy"};

	public static final int TELEBLOCK_DELAY = 20000;
	// public static final int TOTAL_QUESTS = 25;

	public static final String TROLLHEIM = "";

	public static final int TROLLHEIM_X = 3243;
	public static final int TROLLHEIM_Y = 3513;

	// follow you from it's
	// spawn point,
	public static final int[] UNDROPPABLE_ITEMS = {};
	public static final int[] UNTRADEABLE_ITEMS = { 11037, 4447, 12855, 12856,
			10835, 10129, 3062, 2413, 2414, 7633, 11702, 11703, 11704, 11705,
			10735, 1037, 4597, 7433, 1411, 4677, 4678, 1554, 6856, 6857, 6858,
			6859, 6860, 6861, 6862, 6863, 8850, 10548, 10551, 8837, 8838, 8839,
			8840, 8842, 11663, 11664, 11665, 3842, 3844, 3840, 8844, 8845,
			8846, 8847, 8848, 8849, 8850, 10551, 6570, 7462, 7461, 7460, 7459,
			7458, 7457, 7456, 7455, 7454, 8839, 8840, 8842, 11663, 11664,
			11665, 10499, 10498, 9748, 9754, 9751, 9769, 9757, 9760, 9763,
			9802, 9808, 9784, 9799, 9805, 9781, 9796, 9793, 9775, 9772, 9778,
			9787, 9811, 9766, 9749, 9755, 9752, 9770, 9758, 9761, 9764, 9803,
			9809, 9785, 9800, 9806, 9782, 9797, 9794, 9776, 9773, 9779, 9788,
			9812, 9767, 9747, 9753, 9750, 9768, 9756, 9759, 9762, 9801, 9807,
			9783, 9798, 9804, 9780, 9795, 9792, 9774, 9771, 9777, 9786, 9810,
			10476, 10477, 9765, 14876, 14877, 14878, 14879, 14880, 14881,
			14882, 14883, 14884, 14885, 14886, 14887, 14888, 14889, 14890,
			14891, 14892, 6529, 4024, 4026, 4027, 4029, 1052,
			CastleWars.TICKET, 4068, 4069, 4070, 4071, 4072, 4503, 4504, 4505,
			4506, 4507, 4508, 4509, 4510, 4511, 4512, 10586, 772, 10486, 10487,
			8851, 1029, 1030, 11716, 11717, 11863, 4565, 7409, 7410, 9020 };
	public static final String VARROCK = "";
	/**
	 * Teleport Spells
	 **/
	// modern
	public static final int VARROCK_X = 3087;
	public static final int VARROCK_Y = 3500;
	public static final int WALK_QUEUE_SIZE = 50;
	public static final String WATCHTOWER = "";

	public static final int WATCHTOWER_X = 3087;
	public static final int WATCHTOWER_Y = 3500;
	public static final int WORLD_ID = 1;
	public static final boolean WORLD_LIST_FIX = false;

	public static final int[] QUEST_COMPLETE_ID = {

	};

	public static final int RANDOM_DUELING_RESPAWN = 3;

	public static final int DUELING_RESPAWN_X = 3356;

	public static final int DUELING_RESPAWN_Y = 3269;

	public static double getDonorItemValue(int id) {
		switch (id) {
		case 10835:
			return 75;
		case 962:// cracker
			return 30;
		case 11694:// ags
			return 10;
		case 1053:// masks
		case 1055:
		case 1057:
			return 25;
		case 1050:// santa
			return 30;
		case 10586:// combat
			return 2;
		case 11918:// gift
			return 10;
		case 4029:// greegree
		case 4024:
			return 15;
		case 4026:
		case 4027:
			return 20;
		case 11777:
			return 15;
		case 3062:// strange box
			return 0.1;
		case 8285:// tentacle
			return 2.5;
		case 13884:// statius
		case 13890:
			return 10;
		case 4447:
			return 5;
		case 13896:// stat helm
		case 13864:// zuriel hood
			return 5;
		case 13887:// vesta
		case 13893:
			return 10;
		case 11795:// arma c bow
			return 10;
		case 1029:
			return 15;
		case 13858:// zuriel robe
		case 13861:
		case 13870:// morrigan
		case 13873:
			return 10;
		case 13738:
		case 13742:
		case 13744:
			return 5;
		case 13740:
			return 10;

		}
		return 40;
	}

	public static int getSpecialItemValue(int id) {
		switch (id) {
		case 962:
			return 10000;
		case 13893:
		case 13887:
			return 2250;
		case 7765:
		case 7769:
		case 7761:
			return 2500;
		case 10586:
			return 250;
		case 7583:
			return 5000;
		case 13905:
			return 1500;
		case 13899:
		case 12391:
			return 5000;
		case 13867:
		case 13902:
			return 3000;
		case 12601:
		case 12603:
		case 12605:
			return 2000;
		case 7771:
			return 2000;
		case 8132:
			return 1000;
		case 13738:
			return 1500;
		case 8291:
			return 500;
		case 1495:
			return 2000;
		case 8284:
		case 8286:
		case 12103:
		case 12105:
		case 12107:
			return 1000;
		case 10547:
		case 10548:
		case 10549:
		case 10550:
		case 10552:
		case 10553:
			return 50;
		case 10344:
		case 10346:
		case 10348:
		case 10350:
		case 10352:
			return 500;
		case 981:
		case 1989:
			return 25000;
		case 1419:
			return 30000;
		case 6541:
		case 3481:
		case 3483:
			return 15000;
		case 3486:
		case 3488:
		case 6666:
		case 11862:
		case 12371:
			return 10000;
		case 4566:
		case 1037:
			return 25000;
		case 6889:
		case 6914:
			return 500;
		case 4565:
			return 2000;
		case 6916:
		case 6918:
		case 6920:
		case 6922:
		case 6924:
			return 30;
		case 11663:
		case 11664:
		case 11665:
		case 8842:
			return 70;
		case 8839:
		case 8840:
			return 100;
		case 10499:
			return 20;
		case 8845:
		case 7791:
		case 7779:
		case 7797:
		case 7794:
		case 7788:
			return 5;
		case 8846:
			return 10;
		case 8847:
			return 15;
		case 8848:
			return 20;
		case 8849:
		case 8850:
			return 50;

		case 7457:
			return 5;
		case 7458:
		case 7459:
			return 10;
		case 7460:
			return 15;
		case 7461:
			return 25;
		case 7462:
			return 250;
		case 10551:
			return 100;
		case 6570:
			return 800;
		case 299:
			return 5;
		case 9739:
			return 15;
		case 11730:
			return 500;
		case 11732:
			return 600;
		case 11700:
		case 11698:
		case 11696:
			return 1800;
		case 10075:
			return 250;
		case 775:
			return 500;
		case 6585:
			return 500;
		case 2577:
		case 2581:
			return 100;
		case 6199:
			return 5;
		case 4151:
		case 12006:
			return 50000;
		case 11726:
		case 11724:
			return 1450;
		case 11728:
			return 1250;
		case 11694:
		case 11777:
			return 2500;
		case 7336:
		case 7342:
			return 850;
		case 1046:
		case 11720:
		case 11722:
			return 1500;
		case 1044:
			return 1700;
		case 1040:
			return 1700;
		case 1038:
			return 1900;
		case 15486:
			return 600;
		case 1048:
			return 1800;
		case 1053:
			return 1500;
		case 1055:
			return 1500;
		case 1057:
			return 1500;
		case 1050:
			return 1500;
		case 1042:
			return 2000;
		case 6180:
		case 6181:
		case 6182:
			return 5;
		case 6731:
		case 6733:
		case 6735:
		case 6737:
			return 500;

		case 13734:
			return 60;
		case 6:
			return 5000;
		case 2:
			return 1;
		case 3034:
			return 10;
		case 6335:
		case 6337:
		case 6339:
			return 500;
		case 8844:
			return 500;
		case 6562:
			return 750;
		case 8294:
			return 2000;
			/*
			 * TOKKULLL
			 */

		case 6522:
			return 375;
		case 6523:
			return 60000;
		case 6524:
			return 67500;
		case 6525:
			return 37500;
		case 6526:
			return 525000;
		case 6527:
			return 45000;
		case 6528:
			return 75000;
		case 6568:
			return 90000;
		case 6571:
			return 1000000;

			/* CASTLE WARS */
		case 4068:
		case 4069:
		case 4070:
		case 4071:
		case 4072:
			return 5;

		case 4503:
		case 4504:
		case 4505:
		case 4506:
		case 4507:
			return 50;

		case 4508:
		case 4509:
		case 4510:
		case 4511:
		case 4512:
			return 500;

		}
		return 200;
	}

	public static int getWorld() {
		return WORLD_ID;
	}

	public static boolean isOwner(String name) {
		for (String element : GameConfig.OWNERS) {
			if (name.equalsIgnoreCase(element)) {
				return true;
			}
		}
		return false;
	}
}
