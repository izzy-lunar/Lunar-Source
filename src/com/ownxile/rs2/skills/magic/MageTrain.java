package com.ownxile.rs2.skills.magic;

import com.ownxile.rs2.items.ItemAssistant;
import com.ownxile.rs2.player.Client;

public class MageTrain {

	public enum MageShop {

		Armadylbow(11796, 1, 0, 0, 0, 0), ArmadylGodsword(11694, 1, 0, 0, 0, 0), DragonClaws(
				11724, 1, 0, 0, 0, 0), StatiusWarhammer(11726, 1, 0, 0, 0, 0), VestaLongsword(
				11732, 1, 0, 0, 0, 0),

		;
		// ArenaBook(200);
		private int telekinetic, alchemist, enchantment, graveyard, id, amount;

		private MageShop(int id, int amount, int telekinetic, int alchemist,
				int enchantment, int graveyard) {
			this.telekinetic = telekinetic;
			this.alchemist = alchemist;
			this.enchantment = enchantment;
			this.graveyard = graveyard;
			this.id = id;
			this.amount = amount;
		}

		public int getAlchemist() {
			return alchemist;
		}

		public int getAmount() {
			return amount;
		}

		public int getEnchantment() {
			return enchantment;
		}

		public int getGraveyard() {
			return graveyard;
		}

		public int getId() {
			return id;
		}

		public int getTelekinetic() {
			return telekinetic;
		}
	}

	public static String getValueMessage(Client client, int itemId) {
		for (int index = 0; index < MageShop.values().length; index++) {
			MageShop item = MageShop.values()[index];
			if (item != null) {
				if (itemId == item.getId()) {
					client.getItems();
					return ItemAssistant.getItemName(itemId) + " costs "
							+ item.getTelekinetic() + " melee points, "
							+ item.getAlchemist() + " range points,\n "
							+ item.getEnchantment() + " magic points, and "
							+ item.getGraveyard() + " bonus points.";
				}
			}
		}
		return null;
	}

	public static void openShop(Client client) {
		client.getFunction().sendFrame248(15944, 13293);
		client.getItems().resetItems(13294);
		resetMageTrainShop(client);
		client.getFunction().sendFrame126("Equipment Upgrade Store", 15950); // tele
		client.getFunction().sendFrame126("Melee points", 15951); // tele
		client.getFunction().sendFrame126("Range points", 15952); // tele
		client.getFunction().sendFrame126("Magic points", 15953); // tele
		client.getFunction().sendFrame126("Bonus points", 15954); // tele

		client.getFunction().sendFrame126("0", 15955); // tele
		client.getFunction().sendFrame126("0", 15956); // ench
		client.getFunction().sendFrame126("0", 15957); // alch
		client.getFunction().sendFrame126("0", 15958); // grave
	}

	public static void resetMageTrainShop(Client client) {
		client.outStream.createFrameVarSizeWord(53);
		client.outStream.writeWord(15948); // bank
		client.outStream.writeWord(MageShop.values().length); // number of items
		for (int i = 0; i < MageShop.values().length; i++) {
			MageShop item = MageShop.values()[i];
			if (item.getAmount() > 254) {
				client.outStream.writeByte(255);
				client.outStream.writeDWord_v2(item.getAmount());
			} else {
				client.outStream.writeByte(item.getAmount());
			}
			client.outStream.writeWordBigEndianA(item.getId() + 1); // itemID
		}
		client.outStream.endFrameVarSizeWord();
	}
}