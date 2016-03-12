package com.ownxile.rs2.items;

import com.ownxile.rs2.player.Client;

public class ItemControl {

	public static void deleteItems(Client player, int[] items) {
		for (int playerItem : player.playerItems) {
			for (int item : items) {
				if (item - 1 == playerItem) {
					player.getItems().deleteItem2((item - 1), 1);
					player.sendMessage("@blu@Found match inventory: "
							+ ItemAssistant.getItemName(item));
				}
			}
		}
		for (int bankSlot = 0; bankSlot < player.bankItems.length; bankSlot++) {
			// player.sendMessage("Bank Item found: " +
			// player.bankItems[bankSlot]);
			for (int item : items) {
				if (item + 1 == player.bankItems[bankSlot]) {
					player.bankItems[bankSlot] = -1;
					player.bankItemsN[bankSlot] = -1;
				}
			}
		}
		for (int equipmentSlot = 0; equipmentSlot < player.playerEquipment.length; equipmentSlot++) {
			// player.sendMessage("Equipped Item found: "
			// + player.playerEquipment[equipmentSlot]);
			for (int item : items) {
				if (item == player.playerEquipment[equipmentSlot]) {
					player.sendMessage("@blu@Found match in equipment: "
							+ ItemAssistant.getItemName(item));
					player.playerEquipment[equipmentSlot] = -1;
					player.playerEquipmentN[equipmentSlot] = -1;
					player.getItems().updateSlot(equipmentSlot);
				}
			}
		}
		player.flushOutStream();
		player.getFunction().requestUpdates();
		player.getItems().resetItems(3214);
	}

}
