package com.ownxile.rs2.world.games;

import java.util.HashMap;

import com.ownxile.core.World;
import com.ownxile.core.task.Task;
import com.ownxile.rs2.Point;
import com.ownxile.rs2.npcs.NPC;
import com.ownxile.rs2.player.Client;
import com.ownxile.util.Misc;

public class ChampionsChallenge {

	private static enum Champion {
		ABERRANTS_PECTRE(15356, 8987, new String[][] {
				{ "Champion of Aberrant Spectres" },
				{ "I smell defeat for you if you dare join me for a" },
				{ "fight at the Champions' Guild." } }), BANSHEE(15357, 8986,
				new String[][] { { "Champion of Banshees" },
						{ "Come duel me at the Champions' GUild and I shall" },
						{ "scream to your defeat." } }), EARTH_WARRIOR(
				6798,
				3057,
				new String[][] {
						{ "Champion of Earth Warriors" },
						{ "I challenge you to a duel, come to the arena" },
						{ "beneath the Champions' Guild and fight me if you dare." } }), GHOUL(
				6799, 3059, new String[][] { { "Champion of Ghouls" },
						{ "Come duel me at the Champions' Guild. I'll" },
						{ "make sure nothing goes to waste." } }), GIANT(
				6800,
				3058,
				new String[][] {
						{ "Champion of Giants" },
						{ "Get yourself to the Champions' Guild, if you dare" },
						{ "to face me, puny human." } }), GOBLIN(
				6801,
				3060,
				new String[][] {
						{ "Champion of Goblins" },
						{ "Fight me if you think you can, human. I'll wait for" },
						{ "you in the Champions' Guild." } }), HOBGOBLIN(6802,
				3061, new String[][] { { "Champion of Hobgoblins" },
						{ "You won't defeat me, though you're welcome to" },
						{ "try at the Champions' Guild." } }), IMP(6803, 3062,
				new String[][] { { "Champion of Imps" },
						{ "'Ow about picking on some'un yer own size? I'll" },
						{ "see you at the Champions' Guild, guv." } }), JOGRE(
				6804, 3063, new String[][] { { "Champion of Jogres" },
						{ "You think you can defeat me? Come to the" },
						{ "Champions' Guild and prove it!" } }), LESSER_DEMON(
				6805, 3064, new String[][] { { "Champion of Lesser Demons" },
						{ "Come to the Champions' Guild so I can banish" },
						{ "you mortal!" } }), MUMMY(15355, 8988,
				new String[][] { { "Champion of Mummies" },
						{ "I challenge you to a fight! Meet me at the" },
						{ "Champions' Guild so we can wrap this up." } }), SKELETON(
				6806,
				3065,
				new String[][] {
						{ "Champion of Skeletons" },
						{ "I'll be waiting at the Champions' Guild to collect" },
						{ "your bones." } }), ZOMBIE(6807, 3066,
				new String[][] { { "Champion of Zombies" },
						{ "You come to Champions' Guild, you fight me, I" },
						{ "squish you, I get brains!" } });

		private static HashMap<Integer, Champion> champion = new HashMap<Integer, Champion>();

		static {
			for (Champion c : Champion.values()) {
				champion.put(c.getItemId(), c);
			}
		}

		private static Champion forId(int id) {
			return champion.get(id);
		}

		private String interfaceText[][];

		private int itemId;

		private int npcId;

		private Champion(int itemId, int npcId, String[][] interfaceText) {
			this.itemId = itemId;
			this.npcId = npcId;
			this.interfaceText = interfaceText;
		}

		private String getInterfaceText(int row) {
			return interfaceText[row][0];
		}

		private int getItemId() {
			return itemId;
		}

		private int getNpcId() {
			return npcId;
		}
	}

	private static final ChampionsChallenge INSTANCE = new ChampionsChallenge();

	public static ChampionsChallenge getInstance() {
		return INSTANCE;
	}

	private final Point npcSpawnPoint = new Point(3172, 9758, 0);

	private final Point playerSpawnPoint = new Point(3181, 9758, 0);

	public void handleItem(Client client, int itemId) {
		Champion champion = Champion.forId(itemId);
		client.getTask().sendFrame126(champion.getInterfaceText(1), 1139);
		client.getTask().sendFrame126(champion.getInterfaceText(2), 1140);
		client.getTask().sendFrame126(champion.getInterfaceText(0), 1142);
		client.getTask().showInterface(1136);
	}

	public boolean isChampion(int itemId) {
		Champion champion = Champion.forId(itemId);
		if (champion != null) {
			return true;
		}
		return false;
	}

	public void spawnChampion(final Client client, final int item) {
		client.move(playerSpawnPoint);
		client.boxMessage("Prepare to fight!");
		final Champion champ = Champion.forId(item);
		client.deleteItem(item);
		client.champion = champ.getNpcId();
		client.lastClickedNpcId = client.champion;
		World.getSynchronizedTaskScheduler().schedule(new Task(5, false) {
			@Override
			protected void execute() {
			NPC champion = World.getNpcHandler().spawnNpc(client, champ.getNpcId(),
						npcSpawnPoint.x, npcSpawnPoint.y, 0, 0, 120, 25, 150,
						150, true, true);
			champion.forceChat("YOU WILL NOT DEFEAT ME!");
				stop();
			}
		});
	}

	public void handleDeath(Client entity) {
		entity.champion = -1;
		int slayerExp = 5000 + Misc.random(28000);
		int hitpointExp = 5000 + Misc.random(40000);
		entity.move(new Point(3185, 9758));
		/*entity.getFunction().sendFrame126(
				"Well done, you defeated the "
						+ World.getNpcHandler().getNPCName(champ.getNpcId())
						+ "!", 15835);*/
		entity.getFunction().sendFrame126(slayerExp + " Slayer Xp", 15839);
		entity.getFunction().sendFrame126(hitpointExp + " Hitpoint Xp", 15840);
		entity.getFunction().addSkillXP(slayerExp, 18);
		entity.getFunction().addSkillXP(hitpointExp, 3);
		entity.getFunction().showInterface(15831);
		entity.addPoints(50);

	}

}
