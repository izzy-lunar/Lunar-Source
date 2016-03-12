package com.ownxile.rs2.content;

import java.util.HashMap;
import java.util.Map;

import com.ownxile.rs2.player.Client;

public final class Teleporting {

	public enum teleports {

		CAMELOT(2000, "fags", "fags");

		public int price;

		public String rec;

		public String des;

		private teleports(int price, String rec, String des) {
			this.price = price;
			this.rec = rec;
			this.des = des;
		}
		
		private static Map<Integer, teleports> teleMaps = new HashMap<Integer, teleports>();
	
		static {
			for (teleports tele : teleports.values()) {
				teleMaps.put(tele.price, tele);
			}
		}
		
	}

	/**public static void grab_teleport_details(Client c, String string,
			int action_button_id) {
		if (action_button_id == 89127) {
			c.getFunction().sendFrame126("Camelot @gr8@(Woodcutting)", 22930);
			c.getFunction().sendFrame126("Catherby @gr8@(Farming)", 22931);
			c.getFunction().sendFrame126("Mining & Smithing", 22933);
			c.getFunction().sendFrame126("Fishing Guild", 22934);
			c.getFunction().sendFrame126("Cooking Guild", 22935);
		}**/

	}
