package com.ownxile.rs2.content.item;

import java.util.HashMap;

import com.ownxile.rs2.player.Client;

public class Food {

	public static enum FoodToEat {
		ANCHOVIES(319, 1, "Anchovies"), ANCHOVY_PIZZA(2297, 9, "Anchovy Pizza"), APPLE_PIE(
				2323, 7, "Apple Pie"), BAKED_POTATO(6701, 4, "Baked Potato"), BANANA(
				1963, 5, "Banana"), BANDAGES(4049, 6, "Bandage"), BASS(365, 13,
				"Bass"), BREAD(2309, 5, "Bread"), CAKE(1891, 4, "Cake"), CHILLI_POTATO(
				7054, 14, "Chilli Potato"), CHOCOLATE_CAKE(1897, 5,
				"Chocolate Cake"), COD(339, 7, "Cod"), EASTER_EGG(1961, 50,
				"Easter Egg"), EGG_POTATO(7056, 16, "Egg Potato"), HERRING(347,
				5, "Herring"), LOBSTER(379, 12, "Lobster"), MANTA(391, 22,
				"Manta Ray"), MEAT_PIE(2327, 6, "Meat Pie"), MEAT_PIZZA(2293,
				8, "Meat Pizza"), MONKFISH(7946, 16, "Monkfish"), MUSHROOM_POTATO(
				7058, 20, "Mushroom Potato"), ORANGE(2108, 2, "Orange"), PEACH(
				6883, 8, "Peach"), PIKE(351, 8, "Pike"), PINEAPPLE_CHUNKS(2116,
				2, "Pineapple Chunks"), PINEAPPLE_PIZZA(2301, 11,
				"Pineapple Pizza"), PINEAPPLE_RINGS(2118, 2, "Pineapple Rings"), PLAIN_PIZZA(
				2289, 7, "Plain Pizza"), POTATO(1942, 1, "Potato"), POTATO_WITH_BUTTER(
				6703, 14, "Potato with Butter"), POTATO_WITH_CHEESE(6705, 18,
				"Potato with Cheese"), PUMPKIN(1959, 10, "Pumpkin"), PURPLE_SWEETS(
				10476, 2, "Purple Sweet"), REDBERRY_PIE(2325, 5, "Redberry Pie"), SALMON(
				329, 9, "Salmon"), SARDINE(325, 4, "Sardine"), SEA_TURTLE(397,
				24, "Sea Turtle"), SHARK(385, 20, "Shark"), SHRIMPS(315, 3,
				"Shrimps"), SWORDFISH(373, 14, "Swordfish"), TROUT(333, 7,
				"Trout"), TUNA(361, 10, "Tuna"), TUNA_POTATO(7060, 15,
				"Tuna Potato"), CAVIAR(11326, 26,
						"Caviar");

		public static HashMap<Integer, FoodToEat> food = new HashMap<Integer, FoodToEat>();
		static {
			for (final FoodToEat f : FoodToEat.values()) {
				food.put(f.getId(), f);
			}
		}

		public static FoodToEat forId(int id) {
			return food.get(id);
		}

		private int heal;

		private int id;

		private String name;

		private FoodToEat(int id, int heal, String name) {
			this.id = id;
			this.heal = heal;
			this.name = name;
		}

		public int getHeal() {
			return heal;
		}

		public int getId() {
			return id;
		}

		public String getName() {
			return name;
		}
	}

	private final Client client;

	public Food(Client client) {
		this.client = client;
	}

	public void eat(int id, int slot) {
		if (client.duelRule[6]) {
			client.sendMessage("You may not eat in this duel.");
			return;
		}
		if (System.currentTimeMillis() - client.foodDelay >= 1500
				&& client.playerLevel[3] > 0) {
			client.getCombat().resetPlayerAttack();
			client.attackTimer += 2;
			client.startAnimation(829);
			client.getItems().deleteItem(id, slot, 1);
			final FoodToEat f = FoodToEat.food.get(id);
			if (client.playerLevel[3] < client
					.getLevelForXP(client.playerXP[3])) {
				client.playerLevel[3] += f.getHeal();
				if (client.playerLevel[3] > client
						.getLevelForXP(client.playerXP[3])) {
					client.playerLevel[3] = client
							.getLevelForXP(client.playerXP[3]);
				}
			}
			client.foodDelay = System.currentTimeMillis();
			client.getFunction().refreshSkill(3);
			client.sendMessage("You eat the " + f.getName() + ".");
		}
	}

	public boolean isFood(int id) {
		return FoodToEat.food.containsKey(id);
	}

}