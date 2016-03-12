package com.ownxile.config;

import com.ownxile.util.file.ListFile;

public class FileConfig {
	public static final String ACCOUNT_FILE_PATH = "./etc/data/accounts/";
	public static final String BACKUP_FILE_PATH = "./etc/data/backup";
	public static ListFile bannedAccounts = new ListFile(
			"./etc/data/infractions/banned_accounts.txt");
	public static ListFile bannedHosts = new ListFile(
			"./etc/data/infractions/banned_hosts.txt");
	public static ListFile donators = new ListFile(
			"./etc/data/subscribers/donators.txt");
	public static final String DOUBLE_DOOR_FILE_DIR = "./etc/config/object/doubledoors.cfg";
	public static final String EQUIPMENT_DATA_DIR = "./etc/config/item/equipment.dat";
	public static final String GLOBAL_OBJECTS_DIR = "./etc/config/object/global-objects.cfg";
	public static final String INFRACTIONS_FILE_PATH = "./etc/data/infractions";
	public static final String INTERACTIVE_OBJECT_DATA_PATH = "./etc/config/object/interactive_objects.dat";
	public static final String ITEM_DEFINTIONS_FILE_DIR = "./etc/config/item/item_defintions.cfg";
	public static final String MAP_INDEX_FILE_DIR = "./etc/data/map/map_index";
	public static final String MAPDATA_DIRECTORY = "./etc/data/map/mapdata/";
	public static ListFile mutedAccounts = new ListFile(
			"./etc/data/infractions/muted_accounts.txt");
	public static ListFile mutedHosts = new ListFile(
			"./etc/data/infractions/muted_hosts.txt");
	public static final String NOTED_ITEM_DATA_DIR = "./etc/config/item/noted.dat";
	public static final String NPC_CONSTANTS_DIR = "./etc/config/npc/constant_drops.dat";
	public static final String NPC_DEF_DIR = "./etc/config/npc/npc_defs.dat";
	public static final String NPC_DROPS_DIR = "./etc/config/npc/drop_tables.dat";
	public static final String NPC_SPAWNS_DIR = "./etc/config/npc/npc_spawns.dat";
	public static ListFile permBannedAccounts = new ListFile(
			"./etc/data/infractions/perm_banned_accounts.txt");
	public static final String PLAYERS_ONLINE_FILE = "./etc/data/logs/players_online.txt";
	public static final String PRICES_DATA_DIR = "./etc/config/item/prices.dat";
	public static final String QUEST_DATA_DIR = "./etc/data/quest/";
	public static final String SCRIPT_DIRECTORY = "./etc/plugins/";
	public static final String SCRIPT_FILE_EXTENSION = ".py";
	public static final String SETTINGS_FILE_PATH = "./config.xml";
	public static final String SHOP_CONFIG_FILE_DIR = "./etc/config/item/shops.cfg";
	public static final String SINGLE_DOOR_FILE_DIR = "./etc/config/object/doors.cfg";
	public static final String STACKABLE_DATA_DIR = "./etc/config/item/stackable.dat";
}
