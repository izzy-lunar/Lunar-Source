package com.ownxile.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.python.core.PyObject.ConversionException;

import com.ownxile.GameServer;
import com.ownxile.config.FileConfig;
import com.ownxile.config.Configuration;
import com.ownxile.core.cache.def.ObjectDefinition;
import com.ownxile.core.task.Task;
import com.ownxile.core.task.TaskScheduler;
import com.ownxile.core.task.impl.AnnouncementMessage;
import com.ownxile.core.task.impl.DataBackup;
import com.ownxile.core.task.impl.EntityUpdating;
import com.ownxile.core.task.impl.GarbageCollection;
import com.ownxile.core.task.impl.LotteryUpdating;
import com.ownxile.core.task.impl.PenanceBoss;
import com.ownxile.core.task.impl.PlayersOnline;
import com.ownxile.core.task.impl.UptimeLogger;
import com.ownxile.core.task.impl.WorldUpdating;
import com.ownxile.rs2.combat.magic.Magic;
import com.ownxile.rs2.combat.range.CannonHandler;
import com.ownxile.rs2.content.cmd.OwnerCommands;
import com.ownxile.rs2.content.cmd.SuperAdminCommands;
import com.ownxile.rs2.content.cmd.UniversalCommands;
import com.ownxile.rs2.content.quest.QuestConfigData;
import com.ownxile.rs2.items.DefaultGroundItem;
import com.ownxile.rs2.items.DonatorItem;
import com.ownxile.rs2.items.GroundItemHandler;
import com.ownxile.rs2.npcs.NPC;
import com.ownxile.rs2.npcs.NPCDrops;
import com.ownxile.rs2.npcs.NPCHandler;
import com.ownxile.rs2.packets.PacketHandler;
import com.ownxile.rs2.player.Client;
import com.ownxile.rs2.player.Player;
import com.ownxile.rs2.player.PlayerHandler;
import com.ownxile.rs2.skills.agility.Agility;
import com.ownxile.rs2.world.clan.ClanChatHandler;
import com.ownxile.rs2.world.games.CastleWars;
import com.ownxile.rs2.world.games.FightCaves;
import com.ownxile.rs2.world.games.FightPits;
import com.ownxile.rs2.world.games.Godwars;
import com.ownxile.rs2.world.games.PestControl;
import com.ownxile.rs2.world.object.CustomObjects;
import com.ownxile.rs2.world.object.Doors;
import com.ownxile.rs2.world.object.DoubleDoors;
import com.ownxile.rs2.world.object.ObjectHandler;
import com.ownxile.rs2.world.object.ObjectManager;
import com.ownxile.rs2.world.region.RegionManager;
import com.ownxile.rs2.world.shops.ShopBuilder;
import com.ownxile.util.file.ListFile;
import com.ownxile.util.web.Hiscores;

/**
 * Collection of entities that create the game world
 * 
 * @author Robbie
 */
public class World {

	public enum WorldStatus {
		ONLINE, UPDATING;
	}

	public void allEntitiesAnimation(int animId) {
		for (Player player : World.getPlayers()) {
			if (player == null)
				continue;
			player.startAnimation(animId);
		}
		for (NPC npc : World.getNpcs()) {
			if (npc == null)
				continue;
			npc.startAnimation(animId);
		}
	}

	public static Player[] getPlayers() {
		return PlayerHandler.players;
	}

	public static NPC[] getNpcs() {
		return NPCHandler.npcs;
	}

	private static AnnouncementMessage announcementMessage = new AnnouncementMessage();

	private static CastleWars castleWars = new CastleWars();
	private static PestControl pestControl = null;
	private static ClanChatHandler clanChat = new ClanChatHandler();
	public static List<DonatorItem> donatorItems = new ArrayList<DonatorItem>();
	public static boolean donorsCanYell = true;
	private static FightCaves fightCaves = new FightCaves();
	private static FightPits fightPits = null;
	public static boolean fourSevenFour = true;
	private static GameServer gameServer = new GameServer();
	private static Godwars godwars = new Godwars();
	private static Hiscores hiscores = new Hiscores();
	public static List<ListFile> listFiles = new ArrayList<ListFile>();
	private static NPCDrops npcDrops = new NPCDrops();
	private static NPCHandler npcHandler = new NPCHandler();
	private static ObjectHandler objectHandler = new ObjectHandler();
	private static ObjectManager objectManager = new ObjectManager();
	private static OwnerCommands ownerCommands = new OwnerCommands();
	private static PlayerHandler playerHandler = new PlayerHandler();
	public static QuestConfigData[] questConfigData = null;
	public static ArrayList<String> receivedStarter = new ArrayList<String>();
	private static Configuration configuration = new Configuration();
	private static ShopBuilder shopBuilder = new ShopBuilder();
	private static SuperAdminCommands superAdminCommands = new SuperAdminCommands();
	private static TaskScheduler syncronizedTaskScheduler = new TaskScheduler();
	public static int totalCashAmount;
	public static int totalRPAmount;;
	private static UniversalCommands universalCommands = new UniversalCommands();
	public static String[] voterNames = new String[5];
	public static int votes;
	private static WorldStatus worldStatus = WorldStatus.UPDATING;
	public static int totalQuests;

	public static QuestConfigData[] cachedQuestConfig;

	public static void loadQuests() {

		try {
			World.totalQuests = Plugin.pythonInterpreter.get("total_quests")
					.asInt(0);
		} catch (ConversionException e) {
			e.printStackTrace();
		}
		World.questConfigData = new QuestConfigData[World.totalQuests + 1];
		World.cachedQuestConfig = new QuestConfigData[World.totalQuests + 1];
		for (int i = 0; i <= World.totalQuests; i++) {
			Plugin.execute("configure_quest_" + i);
		}
		Arrays.sort(World.questConfigData);
		System.out.println("Loaded " + World.totalQuests + " quests.");
	}

	public static NPC addCombatNpc(int id, int x, int y, int z, int i, int hp,
			int maxHit, int attack, int defence) {
		return getNpcHandler().createNpcSpawn(id, x, y, z, i, hp, maxHit,
				attack, defence);
	}

	public static NPC addNonCombatNpc(int id, int x, int y, int z, int i) {
		return getNpcHandler().createNpcSpawn(id, x, y, z, i, 0, 0, 0, 0);
	}

	public static void addObject(int id, int x, int y) {
		CustomObjects.add(id, x, y, 0, 10);
		for (Player player : PlayerHandler.players) {
			if (player == null) {
				continue;
			}
			Client c = (Client) player;
			if (c.withinDistance(x, y)) {
				c.getFunction().addObject(id, x, y, 0, 0, 10);
			}
		}
	}

	public static void addObject(int id, int x, int y, int z) {
		CustomObjects.add(id, x, y, 0, 10);
		for (Player player : PlayerHandler.players) {
			if (player == null) {
				continue;
			}
			Client c = (Client) player;
			if (c.withinDistance(x, y)) {
				c.getFunction().addObject(id, x, y, z, 0, 10);
			}
		}
	}

	public static void addQuest(int id, String name, int stages) {
		questConfigData[id] = new QuestConfigData(id, name, stages);
		cachedQuestConfig[id] = new QuestConfigData(id, name, stages);
	}

	public static void delayFunction(final String functionName,
			final Object object, int cycleDelay) {
		getSynchronizedTaskScheduler().schedule(new Task(cycleDelay, false) {
			@Override
			protected void execute() {
				Plugin.execute(functionName, object);
				stop();
			}
		});
	}

	public static TaskScheduler getAsynchronousTaskScheduler() {
		return syncronizedTaskScheduler;
	}

	public static CastleWars getCastleWars() {
		return castleWars;
	}

	public static ClanChatHandler getClanChat() {
		return clanChat;
	}

	public static FightCaves getFightCaves() {
		return fightCaves;
	}

	public static FightPits getFightPits() {
		return fightPits;
	}

	public static Godwars getGodwars() {
		return godwars;
	}

	public static Hiscores getHighscores() {
		return hiscores;
	}

	public static NPCDrops getNpcDrops() {
		return npcDrops;
	}

	public static NPCHandler getNpcHandler() {
		return npcHandler;
	}

	public static ObjectHandler getObjectHandler() {
		return objectHandler;
	}

	public static ObjectManager getObjectManager() {
		return objectManager;
	}

	public static OwnerCommands getOwnerCommands() {
		return ownerCommands;
	}

	public static PlayerHandler getPlayerHandler() {
		return playerHandler;
	}

	public static GameServer getServer() {
		return gameServer;
	}

	public static Configuration getSettings() {
		return configuration;
	}

	public static ShopBuilder getShopHandler() {
		return shopBuilder;
	}

	public static SuperAdminCommands getSuperAdminCommands() {
		return superAdminCommands;
	}

	public static TaskScheduler getSynchronizedTaskScheduler() {
		return syncronizedTaskScheduler;
	}

	public static UniversalCommands getUniversalCommands() {
		return universalCommands;
	}

	public static WorldStatus getWorldStatus() {
		return worldStatus;
	}

	public static void initialize() {
		GroundItemHandler.loadItemList();
		GroundItemHandler.loadItemPrices();
		System.out.println("Loaded " + PacketHandler.getTotalWorkingPackets()
				+ " packets.");
		World.getNpcHandler().loadNPCDefintions();
		World.npcHandler.loadAutoSpawn(FileConfig.NPC_SPAWNS_DIR);
		ObjectDefinition.loadConfig();
		RegionManager.loadConfig();
		CustomObjects.loadCustomObjects();
		Agility.createCourses();
		ShopBuilder.loadShops();
		getClanChat().loadDefaultClans();
		getHighscores().init();
		World.getNpcDrops().loadDrops();
		World.getNpcDrops().loadConstants();
		CannonHandler.init();
		ShopBuilder.init();
		Doors.getSingleton().init();
		DoubleDoors.getSingleton().init();
		World.loadQuests();
		DefaultGroundItem.init();
		Magic.loadSpells();
		System.out.println("Loaded " + ShopBuilder.shops.size() + " shops.");
		getSynchronizedTaskScheduler().schedule(new EntityUpdating());
		getSynchronizedTaskScheduler().schedule(FightPits.getInstance());
		getAsynchronousTaskScheduler().schedule(new WorldUpdating());
		getAsynchronousTaskScheduler().schedule(new GarbageCollection(500));
		getAsynchronousTaskScheduler().schedule(new DataBackup());
		getAsynchronousTaskScheduler().schedule(new PenanceBoss());
		pestControl = new PestControl();
		getAsynchronousTaskScheduler().schedule(new LotteryUpdating(18000));
		getAsynchronousTaskScheduler().schedule(new PlayersOnline(50));
		getAsynchronousTaskScheduler().schedule(new UptimeLogger(100));
		System.gc();
		System.out.println("Now accepting connections on "
				+ GameServer.getPort() + ".");
		World.setStatus(WorldStatus.ONLINE);
		
	 NPCDrops.printDrops();

	}

	public static void reloadLists() {
		for (ListFile listFile : listFiles) {
			listFile.loadData();
		}
	}

	public static void sendMessage(String s) {
		System.out.println(s);
		for (Player player : PlayerHandler.players) {
			if (player == null) {
				continue;
			}
			Client c = (Client) player;
			c.sendMessage(s);
		}
	}

	public static void setFightPits(FightPits fightPits2) {
		fightPits = fightPits2;

	}

	public static void setGodwars(Godwars godwars) {
		World.godwars = godwars;
	}

	public static void setShopHandler(ShopBuilder s) {
		shopBuilder = s;
	}

	public static void setStatus(WorldStatus s) {
		worldStatus = s;
	}

	public static void setSuperAdminCommands(
			SuperAdminCommands superAdminCommands) {
		World.superAdminCommands = superAdminCommands;
	}

	/**
	 * @param pestControl
	 *            the pestControl to set
	 */
	public static void setPestControl(PestControl pestControl) {
		World.pestControl = pestControl;
	}

	/**
	 * @return the pestControl
	 */
	public static PestControl getPestControl() {
		return pestControl;
	}

	/**
	 * @return the announcementMessage
	 */
	public static AnnouncementMessage getAnnouncementMessage() {
		return announcementMessage;
	}

	/**
	 * @param announcementMessage
	 *            the announcementMessage to set
	 */
	public static void setAnnouncementMessage(
			AnnouncementMessage announcementMessage) {
		World.announcementMessage = announcementMessage;
	}

}
