package com.ownxile.rs2.world.games;

import com.ownxile.core.World;
import com.ownxile.core.task.Task;
import com.ownxile.rs2.combat.prayer.Prayer;
import com.ownxile.rs2.player.Client;
import com.ownxile.rs2.player.Player;
import com.ownxile.rs2.player.PlayerFunction;
import com.ownxile.rs2.player.PlayerHandler;
import com.ownxile.util.Misc;

/**
 * @author Robbie <Roboyto>
 * @category PvP Minigame
 */
public class FightPits extends Task {

	private static final int PLAYERS_REQUIRED = 2;

	public static FightPits getInstance() {
		if (World.getFightPits() == null) {
			World.setFightPits(new FightPits());
		}
		return World.getFightPits();

	}

	private String champion = "Nobody";

	private boolean gameStarted;

	private int playersInGame = 0, gameTime;

	public FightPits() {
		super(2, false);
		gameTime = 30;
	}

	public void addPlayer(PlayerFunction p) {
		p.walkableInterface(-1);
		p.closeAllWindows();
		p.movePlayer(2392 + Misc.random(12), 5139 + Misc.random(25), 0);
		this.playersInGame++;
	}

	public void endGame() {
		gameStarted = false;
		for (Player player : PlayerHandler.players) {
			if (player == null) {
				continue;
			}
			if (player.inFightPits()) {
				Client c = (Client) player;
				removePlayer(c.getFunction());
			}
		}
		playersInGame = 0;
	}

	@Override
	protected void execute() {
		sendInterfaceUpdate();
		if (gameTime == 0) {
			if (getPlayersWaiting() >= PLAYERS_REQUIRED) {
				startGame();
			}
			gameTime = 30;
		} else if (!gameStarted) {
			gameTime--;
		}
	}

	public Client getLastPlayer(Player n) {
		for (Player player : PlayerHandler.players) {
			if (player == null || player == n) {
				continue;
			}
			if (player.inFightPits()) {
				return (Client) player;
			}
		}
		return null;
	}

	public int getPlayers() {
		return playersInGame;
	}

	private int getPlayersWaiting() {
		int i = 0;
		for (Player player : PlayerHandler.players) {
			if (player == null) {
				continue;
			}
			if (player.inFightPitsWait()) {
				i++;
			}
		}
		return i;
	}

	public void giveReward(Client p) {
		int amount = 11 * p.combatLevel;
		p.getItems().addItem(6529, amount);
		p.addPoints(15);
		p.sendMessage("You receive " + amount
				+ " tokkul and 15 OXP for winning Fight Pits.");
		GameBuilder.announce(p.playerName + " is the champion of Fight pits.");
		Prayer.resetPrayers(p);
		p.getFunction().resetStats();
		p.specAmount = 10;
		World.getFightCaves().npcChat(p,
				"Well done in the out there " + p.playerName + ",",
				"please accept this reward brave champion of the pits.");
	}

	public void removePlayer(PlayerFunction p) {
		p.movePlayer(2399, 5173, 0);
		this.playersInGame--;
		if (this.playersInGame < 1 && gameStarted) {
			giveReward(p.getClient());
			endGame();
		}
		p.walkableInterface(-1);
	}

	private void sendGameInterface(PlayerFunction p) {
		p.sendFrame126("Current Champion: JalYt-Ket-" + champion, 2805);
		p.sendFrame126("Foes remaining: " + (playersInGame - 1), 2806);
		p.sendFrame36(560, 1);
		p.walkableInterface(2804);
	}

	private void sendInterfaceUpdate() {
		for (Player player : PlayerHandler.players) {
			if (player == null) {
				continue;
			}
			if (player.inFightPitsWait()) {
				Client c = (Client) player;
				this.sendWaitInterface(c.getFunction());
			} else if (player.inFightPits()) {
				Client c = (Client) player;
				this.sendGameInterface(c.getFunction());
			}
		}
	}

	private void sendWaitInterface(PlayerFunction p) {
		p.sendFrame126("Current Champion: JalYt-Ket-" + champion, 2805);
		p.sendFrame126(gameStarted ? "Players remaining: " + playersInGame
				: "Next game starting in : " + gameTime, 2806);
		p.sendFrame36(560, 1);
		p.walkableInterface(2804);
	}

	public void setChampion(String name) {
		this.champion = name;
	}

	private void startGame() {
		gameStarted = true;
		playersInGame = 0;
		for (Player player : PlayerHandler.players) {
			if (player == null) {
				continue;
			}
			if (player.inFightPitsWait()) {
				Client c = (Client) player;
				addPlayer(c.getFunction());
				World.getFightCaves().npcChat(c, "FIGHT!");
			}
		}
		GameBuilder.announce(this.getPlayers()
				+ " players have started playing Fight Pits.");
	}

}