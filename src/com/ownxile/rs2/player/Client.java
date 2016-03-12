package com.ownxile.rs2.player;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Future;

import org.apache.mina.common.IoSession;

import com.ownxile.config.GameConfig;
import com.ownxile.config.ItemConfig;
import com.ownxile.config.WildConfig;
import com.ownxile.core.World;
import com.ownxile.core.task.Task;
import com.ownxile.net.connection.HostList;
import com.ownxile.net.packet.Packet;
import com.ownxile.net.packet.StaticPacketBuilder;
import com.ownxile.rs2.Point;
import com.ownxile.rs2.Point.Position;
import com.ownxile.rs2.combat.CombatHandler;
import com.ownxile.rs2.combat.range.CannonHandler;
import com.ownxile.rs2.content.ChatDialogues;
import com.ownxile.rs2.content.action.Dying;
import com.ownxile.rs2.content.action.Trading;
import com.ownxile.rs2.content.click.NpcClickReaction;
import com.ownxile.rs2.content.click.ObjectClickReaction;
import com.ownxile.rs2.content.item.Food;
import com.ownxile.rs2.content.item.Potion;
import com.ownxile.rs2.content.item.WaterSkin;
import com.ownxile.rs2.content.quest.Quest;
import com.ownxile.rs2.content.quest.QuestHandler;
import com.ownxile.rs2.content.quest.QuestReward;
import com.ownxile.rs2.items.GroundItemHandler;
import com.ownxile.rs2.items.ItemAssistant;
import com.ownxile.rs2.npcs.NPCHandler;
import com.ownxile.rs2.packets.PacketHandler;
import com.ownxile.rs2.skills.crafting.Crafting;
import com.ownxile.rs2.skills.crafting.TanHides;
import com.ownxile.rs2.skills.farming.Farming;
import com.ownxile.rs2.skills.firemaking.Firemaking;
import com.ownxile.rs2.skills.fletching.Fletching;
import com.ownxile.rs2.skills.herblore.Herblore;
import com.ownxile.rs2.skills.herblore.Mixing;
import com.ownxile.rs2.skills.mining.Mining;
import com.ownxile.rs2.skills.slayer.Slayer;
import com.ownxile.rs2.skills.smithing.Smithing;
import com.ownxile.rs2.skills.smithing.SmithingInterface;
import com.ownxile.rs2.skills.thieving.Thieving;
import com.ownxile.rs2.world.games.BountyHunter;
import com.ownxile.rs2.world.games.CastleWars;
import com.ownxile.rs2.world.games.DuelArena;
import com.ownxile.rs2.world.shops.Shop;
import com.ownxile.util.Stream;

public class Client extends Player {

	private ActionSender actionSender = new ActionSender(this);
	public byte buffer[] = null;
	private final CombatHandler combatHandler = new CombatHandler(this);
	private final Crafting crafting = new Crafting(this);

	public int currentSong = 0;

	private Future<?> currentTask;
	double damage4;

	public int defenderDrop = 8845;

	public Task cannonTask;

	private final ChatDialogues dialogueHandler = new ChatDialogues(this);

	private Shop currentShop = new Shop();

	public void increaseRating(int amount) {
		if (wildRating + amount <= WildConfig.MAX_RATING)
			wildRating += amount;
		else
			wildRating = WildConfig.MAX_RATING;
		sendMessage("You receive an increase of @dre@" + amount
				+ "@bla@ to your wilderness rating.");
	}


	private final PlayerOptions playerOptions = new PlayerOptions(this);

	public void handleNewPlayer() {
		if (World.receivedStarter.contains(connectedFrom)) {
			getFunction().addStarter(true);
			sendMessage("Previous starter pack claim detected.");
			sendMessage("You have received a reduced amount of items in your starter pack.");
		} else {
			World.receivedStarter.add(connectedFrom);
			getFunction().addStarter(false);
		}
	}

	private final DuelArena duelArena = new DuelArena(this);

	public final Dying dying = new Dying(this);

	private final Farming farming = new Farming(this);

	private final Firemaking firemaking = new Firemaking(this);

	private final Fletching fletching = new Fletching(this);

	private final Food food = new Food(this);

	private final Herblore herblore = new Herblore(this);
	public int hideId;
	public Stream inStream = null, outStream = null;

	private final ItemAssistant itemAssistant = new ItemAssistant(this);

	private final Mining mine = new Mining(this);

	int modY;

	public int packetSize = 0, packetType = -1;

	private final PlayerFunction playerFunction = new PlayerFunction(this);

	private final PlayerLogin playerLogin = new PlayerLogin(this);

	private final Mixing potionMixing = new Mixing(this);

	private final Potion potions = new Potion(this);

	private final QuestHandler questHandler = new QuestHandler(this);

	private final Queue<Packet> queuedPackets = new LinkedList<Packet>();

	public int rememberNpcIndex;

	private IoSession session;

	private final Slayer slayer = new Slayer(this);

	private final Smithing smith = new Smithing(this);
	private final SmithingInterface smithInt = new SmithingInterface(this);
	private TanHides tan = new TanHides(this);
	public boolean tanning;
	private final Thieving thieving = new Thieving(this);
	private final Trading trade = new Trading(this);
	public int summonIndex;
	public int barrowsCount;

	public Client(int _playerId) {
		super(_playerId);
		synchronized (this) {
			outStream = new Stream(new byte[GameConfig.BUFFER_SIZE]);
			outStream.currentOffset = 0;
		}
		inStream = new Stream(new byte[GameConfig.BUFFER_SIZE]);
		inStream.currentOffset = 0;
		buffer = new byte[GameConfig.BUFFER_SIZE];
	}

	public Client(IoSession s, int _playerId) {
		super(_playerId);
		session = s;
		synchronized (this) {
			outStream = new Stream(new byte[GameConfig.BUFFER_SIZE]);
			outStream.currentOffset = 0;
		}
		inStream = new Stream(new byte[GameConfig.BUFFER_SIZE]);
		inStream.currentOffset = 0;
		buffer = new byte[GameConfig.BUFFER_SIZE];
	}

	public void openShop(int id) {
		getTask().openShop(id);
	}

	public void addCash(int amount) {
		if (getFunction().checkSpace(1)) {
			getItems().addItem(995, amount);
		} else {
			getItems().addBankItem(995, amount);
		}
	}

	public void addItem(int id) {
		if (getFunction().checkSpace(1)) {
			getItems().addItem(id, 1);
		} else {
			getItems().addBankItem(id, 1);
		}
	}

	public void addItem(int id, int amount) {
		if (ItemConfig.itemStackable[id]) {
			if (getFunction().checkSpace(1)) {
				getItems().addItem(id, amount);
			} else {
				getItems().addBankItem(id, amount);
			}

		} else {
			if (getFunction().checkSpace(amount)) {
				getItems().addItem(id, amount);
			} else {
				getItems().addBankItem(id, amount);
			}
		}
	}

	public void addPoints(int amt) {
		if (isIronman() || isUltimateIronman())
			amt = amt * 2;

		if (points + amt < 25000) {
			points += amt;
		} else {
			points = 25000;
		}
		sendMessage("You receive " + amt + " points and now have a total of "
				+ points + " oxp.");
	}

	public void boxMessage(String s) {
		getFunction().state(s);
	}

	public void boxMessage(String s, String s1) {
		getFunction().state(s, s1);
	}

	public void boxMessage(String s, String s1, String s2) {
		getFunction().state(s, s1, s2);
	}

	public void boxMessage(String s, String s1, String s2, String s3) {
		getFunction().state(s, s1, s2, s3);
	}

	public void completeQuest(String questName, QuestReward questReward) {
		getQuestFunction().completeQuest(questName, questReward);
	}

	public void completeQuest(String questName, QuestReward questReward,
			int itemId) {
		getQuestFunction().completeQuest(questName, questReward, itemId);
	}

	public void deleteItem(int i) {
		getItems().deleteItem2(i, 1);
	}

	public void deleteItem(int i, int amount) {
		getItems().deleteItem2(i, amount);
	}

	@Override
	public void destruct() {
		synchronized (this) {
			if (session == null) {
				return;
			}
			PlayerSave.saveGame(this);
			if (cannon != null) {
				CannonHandler.removeCannon(cannon);
			}
			if (CastleWars.isInCwWait(this)) {
				CastleWars.leaveWaitingRoom(this);
			}
			if (CastleWars.isInCw(this)) {
				CastleWars.removePlayerFromCw(this);
			}
			if (inFightPits()) {
				World.getFightPits().removePlayer(getFunction());
			}
			if (clanId >= 0) {
				World.getClanChat().leaveClan(playerId, clanId);
			}
			if (inTrade) {
				this.getTrade().declineTrade();
			}
			HostList.getHostList().remove(session);
			disconnected = true;
			session.close();
			session = null;
			inStream = null;
			outStream = null;
			isActive = false;
			buffer = null;
			super.destruct();
		}
	}

	public void dialogueOption(String s1, int d1, String s2, int d2) {
		getChat().sendOption2(s1, s2);
		dialogueOption1 = d1;
		dialogueOption2 = d2;
		dialogueAction = 0;
	}

	public void dialogueOption(String s1, int d1, String s2, int d2, String s3,
			int d3) {
		getChat().sendOption3(s1, s2, s3);
		dialogueOption1 = d1;
		dialogueOption2 = d2;
		dialogueOption3 = d3;
		dialogueAction = 0;
	}

	public void dialogueOption(String s1, int d1, String s2, int d2, String s3,
			int d3, String s4, int d4) {
		getChat().sendOption4(s1, s2, s3, s4);
		dialogueOption1 = d1;
		dialogueOption2 = d2;
		dialogueOption3 = d3;
		dialogueOption4 = d4;
		dialogueAction = 0;
	}

	public void dialogueOption(String s1, int d1, String s2, int d2, String s3,
			int d3, String s4, int d4, String s5, int d5) {
		getChat().sendOption5(s1, s2, s3, s4, s5);
		dialogueOption1 = d1;
		dialogueOption2 = d2;
		dialogueOption3 = d3;
		dialogueOption4 = d4;
		dialogueOption5 = d5;
		dialogueAction = 0;
	}

	public void dialogueQuestion(String title, String s1, int d1, String s2,
			int d2) {
		getChat().sendTitledOption2(title, s1, s2);
		dialogueOption1 = d1;
		dialogueOption2 = d2;
		dialogueAction = 0;
	}

	public void endChat() {
		nextChat = 0;
	}

	public void flushOutStream() {
		if (disconnected || outStream.currentOffset == 0) {
			return;
		}
		synchronized (this) {
			final StaticPacketBuilder out = new StaticPacketBuilder()
					.setBare(true);
			final byte[] temp = new byte[outStream.currentOffset];
			System.arraycopy(outStream.buffer, 0, temp, 0, temp.length);
			out.addBytes(temp);
			session.write(out.toPacket());
			outStream.currentOffset = 0;
		}
	}

	public ActionSender getActionSender() {
		return actionSender;
	}

	public ChatDialogues getChat() {
		return dialogueHandler;
	}

	public CombatHandler getCombat() {
		return combatHandler;
	}

	public Crafting getCrafting() {
		return crafting;
	}

	public Future<?> getCurrentTask() {
		return currentTask;
	}

	public DuelArena getDuel() {
		return duelArena;
	}

	public Farming getFarming() {
		return farming;
	}

	public Firemaking getFiremaking() {
		return firemaking;
	}

	public Fletching getFletching() {
		return fletching;
	}

	public Food getFood() {
		return food;
	}

	public PlayerFunction getFunction() {
		return playerFunction;
	}

	public Herblore getHerblore() {
		return herblore;
	}

	public synchronized Stream getInStream() {
		return inStream;
	}

	public ItemAssistant getItems() {
		return itemAssistant;
	}

	public Mining getMining() {
		return mine;
	}

	public synchronized Stream getOutStream() {
		return outStream;
	}

	public synchronized int getPacketSize() {
		return packetSize;
	}

	public synchronized int getPacketType() {
		return packetType;
	}

	public Potion getPotions() {
		return potions;
	}

	public Mixing getPotMixing() {
		return potionMixing;
	}

	public Quest getQuest(int id) {
		return quests[id];
	}

	public Quest[] quests = new Quest[World.totalQuests + 1];
	public int champion;
	public boolean quadHit;
	public int playerTarget;
	public byte insert;

	public void loadQuests() {
		for (int i = 0; i <= World.totalQuests; i++) {
			quests[i] = new Quest(i, this);
		}
	}

	public QuestHandler getQuestFunction() {
		return questHandler;
	}

	public IoSession getSession() {
		return session;
	}

	public Slayer getSlayer() {
		return slayer;
	}

	public Smithing getSmithing() {
		return smith;
	}

	public SmithingInterface getSmithingInt() {
		return smithInt;
	}

	/**
	 * @return the tan
	 */
	public TanHides getTan() {
		return tan;
	}

	public PlayerFunction getTask() {
		return this.playerFunction;
	}

	public Thieving getThieving() {
		return thieving;
	}

	public Trading getTrade() {
		return trade;
	}

	public boolean hasInventorySpace(int space) {
		return getFunction().checkSpace(space);
	}

	public boolean hasItem(int item) {
		return getItems().playerHasItem(item);
	}

	public boolean hasItem(int item, int amount) {
		return getItems().playerHasItem(item, amount);
	}

	@Override
	public void initialize() {
		playerLogin.login();
	}

	public void logout() {
		if (inTrade) {
			this.getTrade().declineTrade();
			return;
		}
		if (isDead)
			return;
		if (isDoingSkill) {
			sendMessage("You can't logout while doing an action.");
			return;
		}
		if (System.currentTimeMillis() - logoutDelay > 10000) {
			outStream.createFrame(109);
			properLogout = true;
			PlayerSave.saveGame(this);
		} else {
			sendMessage("You must wait a few seconds from being out of combat to logout.");
		}
	}

	public void move(Point point) {
		getFunction().movePlayer(point.x, point.y, point.z);
	}

	public void move(Position position) {
		getFunction().movePlayer(position.x, position.y, position.z);
	}

	public void nextChat(int i) {
		nextChat = i;
	}

	public void npcChat(String s) {
		String name = World.getNpcHandler().getNPCName(lastClickedNpcId);
		getChat().sendNpcChat1(s, lastClickedNpcId, name);
	}

	public void npcChat(String s, String s1) {
		String name = World.getNpcHandler().getNPCName(lastClickedNpcId);
		getChat().sendNpcChat2(s, s1, lastClickedNpcId, name);
	}

	public void npcChat(String s, String s1, String s2) {
		String name = World.getNpcHandler().getNPCName(lastClickedNpcId);
		getChat().sendNpcChat3(s, s1, s2, lastClickedNpcId, name);
	}

	public void npcChat(String s, String s1, String s2, String s3) {
		String name = World.getNpcHandler().getNPCName(lastClickedNpcId);
		getChat().sendNpcChat4(s, s1, s2, s3, lastClickedNpcId, name);
	}

	public void playerChat(String s) {
		getChat().sendPlayerChat1(s);
	}

	public void playerChat(String s, String s1) {
		getChat().sendPlayerChat2(s, s1);
	}

	public void playerChat(String s, String s1, String s2) {
		getChat().sendPlayerChat3(s, s1, s2);
	}

	public void playerChat(String s, String s1, String s2, String s3) {
		getChat().sendPlayerChat4(s, s1, s2, s3);
	}

	@Override
	public void process() {

		timeOut--;
		if (smeltTimer > 0 && smeltType > 0) {
			smeltTimer--;
		} else if (smeltTimer == 0 && smeltType > 0) {
			getSmithing().smelt(smeltType);
		}

		if (clickObjectType > 0
				&& goodDistance(objectX + objectXOffset, objectY
						+ objectYOffset, getX(), getY(), objectDistance)) {
			if (clickObjectType == 1) {
				turnToObject();
				ObjectClickReaction
						.firstClick(this, objectId, objectX, objectY);
			}
			if (clickObjectType == 2) {
				turnToObject();
				ObjectClickReaction.secondClick(this, objectId, objectX,
						objectY);
			}
			if (clickObjectType == 3) {
				turnToObject();
				ObjectClickReaction
						.thirdClick(this, objectId, objectX, objectY);
			}
		}

		if (clickNpcType > 0 && NPCHandler.npcs[npcClickIndex] != null) {
			if (goodDistance(getX(), getY(),
					NPCHandler.npcs[npcClickIndex].getX(),
					NPCHandler.npcs[npcClickIndex].getY(), 2)) {
				if (clickNpcType == 1) {
					turnPlayerTo(NPCHandler.npcs[npcClickIndex].getX(),
							NPCHandler.npcs[npcClickIndex].getY());
					NPCHandler.npcs[npcClickIndex].recentlyInteracted = System
							.currentTimeMillis();
					NPCHandler.npcs[npcClickIndex].facePlayer(playerId);
					NpcClickReaction.executeClick(this,
							NpcClickReaction.FIRST_CLICK, npcType);
				}
				if (clickNpcType == 2) {
					turnPlayerTo(NPCHandler.npcs[npcClickIndex].getX(),
							NPCHandler.npcs[npcClickIndex].getY());
					NPCHandler.npcs[npcClickIndex].recentlyInteracted = System
							.currentTimeMillis();
					NPCHandler.npcs[npcClickIndex].facePlayer(playerId);
					NpcClickReaction.executeClick(this,
							NpcClickReaction.SECOND_CLICK, npcType);
				}
				if (clickNpcType == 3) {
					turnPlayerTo(NPCHandler.npcs[npcClickIndex].getX(),
							NPCHandler.npcs[npcClickIndex].getY());
					NPCHandler.npcs[npcClickIndex].recentlyInteracted = System
							.currentTimeMillis();
					NPCHandler.npcs[npcClickIndex].facePlayer(playerId);
					NpcClickReaction.executeClick(this,
							NpcClickReaction.THIRD_CLICK, npcType);
				}
			}
		}

		if (walkingToItem) {
			if (getX() == pItemX && getY() == pItemY
					|| goodDistance(getX(), getY(), pItemX, pItemY, 1)) {
				walkingToItem = false;
				GroundItemHandler.removeGroundItem(this, pItemId, pItemX,
						pItemY, true);
			}
		}

		if (followId3 > 0) {
			getFunction().followPlayer();
		} else if (followId > 0) {
			getFunction().combatFollowPlayer();
		} else if (followId2 > 0) {
			getFunction().followNpc();
		}

		getCombat().handlePrayerDrain();
		if (System.currentTimeMillis() - singleCombatDelay > 3300) {
			underAttackBy = 0;
		}
		if (System.currentTimeMillis() - singleCombatDelay2 > 3300) {
			underAttackBy2 = 0;
		}
		if (World.getGodwars().inDungeon(this)) {
			World.getGodwars().updateInterface(this);
			getFunction().walkableInterface(16210);

		} else if (inBarrows()) {
			getFunction().sendFrame126("Kill Count: " + barrowsKillCount, 4536);
			getFunction().walkableInterface(4535);} 
		else if (inNightmareZone()) {
				getFunction().sendFrame126(nightmarePoints + "", 922);
				getFunction().walkableInterface(920);
		} else if (inCastleWars() || playerRights == 5) {
			CastleWars.updateGameInterface(this);
			getFunction().showOption(3, 0, "Attack");
		} else if (absZ == 0) {
			if (inWild()) {
				modY = absY > 6400 ? absY - 6400 : absY;
				wildLevel = (modY - 3520) / 8 + 1;
				getFunction().walkableInterface(27300);
				getFunction().sendFrame126("@yel@Level: " + wildLevel, 199);
				getFunction().showOption(3, 0, "Attack");
			} else {
				getFunction().sendFrame126(" ", 199);
			}
			if (inDuelArena()) {
				getFunction().walkableInterface(201);
				if (duelStatus == 5 || duelStatus == 4) {
					getFunction().showOption(3, 0, "Attack");
				} else {
					getFunction().showOption(3, 0, "Challenge");
				}
			} else if (inSnow()) {
				getFunction().walkableInterface(11877);
			} else if (inPcBoat()) {
				getFunction().walkableInterface(21119);
			} else if (inPcGame()) {
				getFunction().walkableInterface(22100);
			} else if (inFightPits() && !inFightPitsWait()) {
				getFunction().showOption(3, 0, "Attack");
			} else if (!inWaitingRoom() && !inFightPitsWait() && !inWild()) {
				getFunction().walkableInterface(-1);
				if (getFunction().playerHasChicken()) {
					getFunction().showOption(3, 0, "Whack");
				} else {
					getFunction().showOption(3, 0, "Null");
				}
			}
		}
		if (inMulti()) {
			getFunction().multiWay(1);
		} else {
			getFunction().multiWay(-1);
		}

		if (skullTimer > 0) {
			skullTimer--;
		}

		if (freezeTimer > -6) {
			freezeTimer--;
			if (frozenBy > 0) {
				if (PlayerHandler.players[frozenBy] == null) {
					freezeTimer = -1;
					frozenBy = -1;
				} else if (!goodDistance(absX, absY,
						PlayerHandler.players[frozenBy].absX,
						PlayerHandler.players[frozenBy].absY, 20)) {
					freezeTimer = -1;
					frozenBy = -1;
				}
			}
		}

		if (hitDelay > 0) {
			hitDelay--;
		}
		if (isDead && !handlingDeath) {
			handlingDeath = true;
			dying.init();
		}
		if (teleTimer > 0) {
			teleTimer--;
			if (!isDead) {
				if (teleTimer == 1 && newLocation > 0) {
					teleTimer = 0;
					getFunction().changeLocation();
				}
				if (teleTimer == 5) {
					teleTimer--;
					getFunction().processTeleport();
				}
				if (teleTimer == 9 && teleGfx > 0) {
					teleTimer--;
					gfx100(teleGfx);
				}
			} else {
				teleTimer = 0;
			}
		}

		if (hitDelay == 1) {
			if (oldNpcIndex > 0) {
				getCombat().delayedHit(oldNpcIndex);
			}
			if (oldPlayerIndex > 0) {
				getCombat().playerDelayedHit(oldPlayerIndex);
			}
		}

		if (attackTimer > 0) {
			attackTimer--;
		}

		if (attackTimer == 1) {
			if (npcIndex > 0 && clickNpcType == 0) {
				getCombat().attackNpc(npcIndex);
			}
			if (playerIndex > 0) {
				getCombat().attackPlayer(playerIndex);
			}
		} else if (attackTimer <= 0 && (npcIndex > 0 || playerIndex > 0)) {
			if (npcIndex > 0) {
				attackTimer = 0;
				getCombat().attackNpc(npcIndex);
			} else if (playerIndex > 0) {
				attackTimer = 0;
				getCombat().attackPlayer(playerIndex);
			}
		}

		if (inTrade && tradeResetNeeded) {
			final Client o = (Client) PlayerHandler.players[tradeWith];
			if (o != null) {
				if (o.tradeResetNeeded) {
					getTrade().resetTrade();
					o.getTrade().resetTrade();
				}
			}
		}
	}

	public synchronized boolean processPacket(Packet p) {
		synchronized (this) {
			if (p == null) {
				return false;
			}
			inStream.currentOffset = 0;
			packetType = p.getId();
			packetSize = p.getLength();
			inStream.buffer = p.getData();
			if (packetType > 0) {
				PacketHandler.processPacket(this, packetType, packetSize);
			}
			return true;
		}
	}

	@Override
	public synchronized boolean processQueuedPackets() {
		Packet p = null;
		synchronized (queuedPackets) {
			p = queuedPackets.poll();
		}
		if (p == null) {
			return false;
		}
		inStream.currentOffset = 0;
		packetType = p.getId();
		packetSize = p.getLength();
		inStream.buffer = p.getData();
		if (packetType > 0) {
			// sendMessage("PacketType: " + packetType);
			PacketHandler.processPacket(this, packetType, packetSize);
		}
		return true;
	}

	public void queueMessage(Packet arg1) {
		synchronized (queuedPackets) {
			queuedPackets.add(arg1);
		}
	}

	public void refreshQuestTab() {
		getQuestFunction().refreshQuestTab();
	}

	public void restoreSpecialAmount2() {
		specDelay = System.currentTimeMillis();
		if (specAmount < 10) {
			specAmount += 1;
			if (specAmount > 10) {
				specAmount = 10;
			}
			getItems().addSpecialBar(playerEquipment[playerWeapon]);
			if (inWild())
				sendMessage("Your special attack energy has regenerated to "
						+ (int) specAmount * 10 + "%.");
		}
	}

	public void restoreStats() {
		restoreStatsDelay = System.currentTimeMillis();
		for (int level = 0; level < playerLevel.length; level++) {
			if (playerLevel[level] < getLevelForXP(playerXP[level])) {
				if (level != 5 && level != 3) { // prayer doesn't restore
					playerLevel[level] += 1;
					getFunction().setSkillLevel(level, playerLevel[level],
							playerXP[level]);
					getFunction().refreshSkill(level);
				}
			} else if (playerLevel[level] > getLevelForXP(playerXP[level])) {
				playerLevel[level] -= 1;
				getFunction().setSkillLevel(level, playerLevel[level],
						playerXP[level]);
				getFunction().refreshSkill(level);
			}
		}
	}

	public void sendMessage(String message) {
		if (getOutStream() != null && message != lastMessage) {
			outStream.createFrameVarSize(253);
			outStream.writeString(message);
			outStream.endFrameVarSize();
			lastMessage = message;
		}
	}

	public void setCurrentTask(Future<?> task) {
		currentTask = task;
	}

	public void setSidebarInterface(int menuId, int form) {
		if (getOutStream() != null) {
			outStream.createFrame(71);
			outStream.writeWord(form);
			outStream.writeByteA(menuId);
		}
	}

	public void skullCheck() {
		if (skullTimer == 0) {
			isSkulled = false;
			attackedPlayers.clear();
			headIconPk = -1;
			skullTimer = -1;
			getFunction().requestUpdates();
		}
	}

	public void startChat(int dialogueId) {
		getChat().sendChat(dialogueId, lastClickedNpcId);
	}

	@Override
	public void startConstantTasks() {
		World.getSynchronizedTaskScheduler().schedule(new WaterSkin(this));
		World.getSynchronizedTaskScheduler().schedule(new Task(100, false) {
			@Override
			protected void execute() {
				restoreStats();
			}
		});

		World.getSynchronizedTaskScheduler().schedule(new Task(50, false) {
			@Override
			protected void execute() {
				if (timeOut < 1) {
					disconnected = true;
				}
				if (poisonDamage > 0) {
					updatePoison();
				}
				if (drain > 0) {
					updateDrain();
				}
				getFarming().growPlants();
				restoreSpecialAmount2();
				skullCheck();
			}
		});

	}

	public boolean teleport(Point point) {
		if (getFunction().canTeleport(true))
			return getFunction().spellTeleport(point.x, point.y, point.z);
		return false;
	}

	public boolean teleport(Position point) {
		if (getFunction().canTeleport(true))
			return getFunction().spellTeleport(point.x, point.y, point.z);
		return false;
	}

	@Override
	public void update() {
		handler.updatePlayer(this, outStream);
		handler.updateNPC(this, outStream);
		flushOutStream();
	}

	public void updatePoison() {
		final int damage = poisonDamage / 2;
		if (damage > 0) {
			if (!getHitUpdateRequired()) {
				setHitUpdateRequired(true);
				setHitDiff(damage);
				updateRequired = true;
				poisonMask = 1;
			} else if (!getHitUpdateRequired2()) {
				setHitUpdateRequired2(true);
				setHitDiff2(damage);
				updateRequired = true;
				poisonMask = 2;
			}
			lastPoison = System.currentTimeMillis();
			poisonDamage--;
			dealDamage(damage);
		} else {
			poisonDamage = -1;
			sendMessage("You are no longer poisoned.");
		}
		getTask().refreshSkill(3);
	}

	public void updateDrain() {
		final int amount = drain / 2;
		if (amount > 0) {
			getDrained(amount, drainSkill);
			getFunction().refreshSkill(drainSkill);
			drain--;
			if (amount > 3)
				sendMessage("You suddenly feel very drained.");
			else
				sendMessage("You feel slightly drained.");
		}
	}

	/**
	 * @return the toolTab
	 */
	public PlayerOptions getPlayerOptions() {
		return playerOptions;
	}

	/**
	 * @param currentShop
	 *            the currentShop to set
	 */
	public void setCurrentShop(Shop currentShop) {
		this.currentShop = currentShop;
	}

	/**
	 * @return the currentShop
	 */
	public Shop getCurrentShop() {
		return currentShop;
	}

	public PlayerFunction getShop() {
		return playerFunction;
	}


}