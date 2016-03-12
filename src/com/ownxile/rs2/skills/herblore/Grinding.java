package com.ownxile.rs2.skills.herblore;

import com.ownxile.rs2.items.ItemAssistant;
import com.ownxile.rs2.player.Client;

public class Grinding {

	public enum Data {
		ASHES(592, 8865, 380), ASTRAL(11156, 11155, 600), BAT(530, 2391, 445), CHARCOAL(
				973, 704, 460), CHOCO(1973, 1975, 50), COD(341, 7528, 490), CRABMEAT(
				7518, 7527, 540), DIAMOND(14703, 14704, 225), GARLIC(1550,
				4698, 765), GOAT(9735, 9736, 320), KEBBIT(10109, 10111, 100), KELP(
				7516, 7517, 520), MUD(4698, 9594, 350), MUSHROOM(4620, 4622,
				840), NEST(5075, 6693, 65), SCALE(243, 241, 175), SHARDS(6466,
				6467, 300), SQUAH(9079, 9082, 650), THISLE(3263, 3264, 710), UNICORN(
				237, 235, 20), WEED1(401, 6683, 400), WEED2(403, 6683, 420);

		/**
		 * End id.
		 */
		public int end;

		/**
		 * Start id.
		 */
		public int id;

		/**
		 * Amount of experience gained.
		 */
		public int xp;

		/**
		 * 
		 * @param id
		 *            start id for the uncrushed items.
		 * @param end
		 *            end id for the crushed items.
		 * @param xp
		 *            xp gained from grinding items.
		 */
		Data(int id, int end, int xp) {
			this.id = id;
			this.end = end;
			this.xp = xp;
		}

		/**
		 * Getter for End id.
		 * 
		 * @return
		 */
		public int getEnd() {
			return end;
		}

		/**
		 * Getter for Id.
		 * 
		 * @return
		 */
		public int getId() {
			return id;
		}

		/**
		 * Getter for xp gained.
		 * 
		 * @return
		 */
		private int getXp() {
			return xp;
		}
	}

	/**
	 * Id of Pestle and Mortar
	 */
	public static final int PESTLE_AND_MORTAR = 233;

	/**
	 * Initialize method for grinding the items.
	 * 
	 * @param c
	 * @param itemUsed
	 * @param useWith
	 */
	public static void init(Client c, int itemUsed, int useWith) {
		for (Data d : Data.values()) {
			if (itemUsed == PESTLE_AND_MORTAR && useWith == d.getId()
					|| itemUsed == d.getId() && useWith == PESTLE_AND_MORTAR) {
				c.startAnimation(364);
				c.getItems().deleteItem(d.getId(), 1);
				c.getItems().addItem(d.getEnd(), 1);
				c.sendMessage("You carefully grind the "
						+ ItemAssistant.getItemName(d.getId()) + ".");
				c.getFunction().addSkillXP(d.getXp(), c.playerHerblore);
			}
		}
	}
}
