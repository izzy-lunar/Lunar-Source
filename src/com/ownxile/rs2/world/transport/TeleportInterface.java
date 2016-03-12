package com.ownxile.rs2.world.transport;

import com.ownxile.rs2.player.Client;

public class TeleportInterface {


	
	public static void openTraining(Client c) {
		c.getFunction().showInterface(22900);
		c.getFunction().sendFrame126("Rock Crabs @gr8@(Low Combat)", 22931);
		c.getFunction().sendFrame126("Edgeville Dungeon @ye9@(Slayer)", 22932);
		c.getFunction().sendFrame126("Taverly Dungeon @ye9@(Slayer)", 22933);
		c.getFunction().sendFrame126("Brimhaven Dungeon @ye9@(Slayer)",	22934);
		c.getFunction().sendFrame126("Kalphite Caves @ye9@(Slayer)", 22935);
		c.getFunction().sendFrame126("Slayer Tower @ye9@(Slayer)", 22936);
	}	
	public static void openMinigame(Client c) {
		c.getFunction().showInterface(22800);
		c.getFunction().sendFrame126("Barrows", 22831);
		c.getFunction().sendFrame126("Castle Wars", 22832);
		c.getFunction().sendFrame126("Duel Arena @red@(Be Careful!)", 22833);
		c.getFunction().sendFrame126("Fight Caves", 22834);
		c.getFunction().sendFrame126("Pest Control", 22835);
		c.getFunction().sendFrame126("Warrior's Guild", 22836);
	}	
	public static void openBossing(Client c) {
		c.getFunction().showInterface(22700);
		c.getFunction().sendFrame126("King Black Dragon @red@(Dangerous)",22731);
		c.getFunction().sendFrame126("Godwars Dungeon", 22732);
		c.getFunction().sendFrame126("Zul-randa", 22733);
		c.getFunction().sendFrame126("Kraken", 22734);
		c.getFunction().sendFrame126("Cerberus @gr8@(New)", 22735);
		c.getFunction().sendFrame126("Abyssal Sire @gr8@(New)", 22736);
	}	
	public static void openSkilling(Client c) {
		c.getFunction().showInterface(22600);
		c.getFunction().sendFrame126("Camelot @gr8@(Woodcutting)", 22631);
		c.getFunction().sendFrame126("Catherby @gr8@(Farming)", 22632);
		c.getFunction().sendFrame126("Agility Courses", 22633);
		c.getFunction().sendFrame126("Mining & Smithing", 22634);
		c.getFunction().sendFrame126("Fishing Guild", 22635);
		c.getFunction().sendFrame126("Cooking Guild", 22636);
	}
	public static void openPVP(Client c) {
		c.getFunction().showInterface(22500);
		c.getFunction().sendFrame126("Green Dragons @red@(East: 12)", 22531);
		c.getFunction().sendFrame126("Green Dragons @red@(West: 18)", 22532);
		c.getFunction().sendFrame126("Edgeville @gr8@(Safe)", 22533);
		c.getFunction().sendFrame126("Mage Bank @red@(Dangerous)", 22534);
		c.getFunction().sendFrame126("", 22535);
		c.getFunction().sendFrame126("", 22536);
	}
	
	
}
