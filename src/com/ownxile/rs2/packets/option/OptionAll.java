package com.ownxile.rs2.packets.option;

import com.ownxile.config.ItemConfig;
import com.ownxile.rs2.items.GameItem;
import com.ownxile.rs2.packets.Packet;
import com.ownxile.rs2.player.Client;
import com.ownxile.rs2.world.games.PartyRoom;
import com.ownxile.rs2.world.shops.Shopping;

/**
 * Bank All Items
 **/
public class OptionAll implements Packet {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		final int removeSlot = c.getInStream().readUnsignedWordA();
		final int interfaceId = c.getInStream().readUnsignedWord();
		final int removeId = c.getInStream().readUnsignedWordA();
		switch (interfaceId) {
		case 3900:
			Shopping.buyItem(c, removeId, removeSlot, 10);
			break;

		case 3823:
			if (!c.getItems().playerHasItem(removeId))
				return;
			Shopping.sellItem(c, removeId, removeSlot, 10);
			break;

		case 5064:
			if (c.inTrade) {
				c.sendMessage("You can't store items while trading!");
				return;
			}
			if (!c.getItems().playerHasItem(removeId))
				return;
			if (c.isBanking) {
				if (ItemConfig.itemStackable[removeId]) {
					c.getItems().bankItem(c.playerItems[removeSlot],
							removeSlot, c.playerItemsN[removeSlot]);
				} else {
					c.getItems().bankItem(c.playerItems[removeSlot],
							removeSlot,
							c.getItems().itemAmount(c.playerItems[removeSlot]));
				}
			} else {
				PartyRoom.depositItem(c, removeId,
						c.getItems().itemAmount(c.playerItems[removeSlot]));
			}
			break;

		case 5382:
			if (c.bankingTab == 0) {
				c.getItems().fromBank(removeId, removeSlot,
						c.bankItemsN[removeSlot]);
			} else if (c.bankingTab == 1) {
				c.getItems().fromBank(removeId, removeSlot,
						c.bankItems1N[removeSlot]);
			} else if (c.bankingTab == 2) {
				c.getItems().fromBank(removeId, removeSlot,
						c.bankItems2N[removeSlot]);
			} else if (c.bankingTab == 3) {
				c.getItems().fromBank(removeId, removeSlot,
						c.bankItems3N[removeSlot]);
			} else if (c.bankingTab == 4) {
				c.getItems().fromBank(removeId, removeSlot,
						c.bankItems4N[removeSlot]);
			} else if (c.bankingTab == 5) {
				c.getItems().fromBank(removeId, removeSlot,
						c.bankItems5N[removeSlot]);
			} else if (c.bankingTab == 6) {
				c.getItems().fromBank(removeId, removeSlot,
						c.bankItems6N[removeSlot]);
			} else if (c.bankingTab == 7) {
				c.getItems().fromBank(removeId, removeSlot,
						c.bankItems7N[removeSlot]);
			} else if (c.bankingTab == 8) {
				c.getItems().fromBank(removeId, removeSlot,
						c.bankItems8N[removeSlot]);
			} else {
				c.sendMessage("Error");
				return;
			}
			break;

		case 3322:
			if (c.duelStatus <= 0) {
				if (ItemConfig.itemStackable[removeId]) {
					c.getTrade().tradeItem(removeId, removeSlot,
							c.playerItemsN[removeSlot]);
				} else {
					c.getTrade().tradeItem(removeId, removeSlot, 28);
				}
			} else {

				if (ItemConfig.itemStackable[removeId]
						|| ItemConfig.itemIsNote[removeId]) {
					c.getDuel().stakeItem(removeId, removeSlot,
							c.playerItemsN[removeSlot]);
				} else {
					c.getDuel().stakeItem(removeId, removeSlot, 28);
				}
			}
			break;

		case 3415:
			if (c.duelStatus <= 0) {
				if (ItemConfig.itemStackable[removeId]) {
					for (final GameItem item : c.getTrade().offeredItems) {
						if (item.id == removeId) {
							c.getTrade()
									.fromTrade(
											removeId,
											removeSlot,
											c.getTrade().offeredItems
													.get(removeSlot).amount);
						}
					}
				} else {
					for (final GameItem item : c.getTrade().offeredItems) {
						if (item.id == removeId) {
							c.getTrade().fromTrade(removeId, removeSlot, 28);
						}
					}
				}
			}
			break;

		case 6669:
			if (ItemConfig.itemStackable[removeId]
					|| ItemConfig.itemIsNote[removeId]) {
				for (final GameItem item : c.getDuel().stakedItems) {
					if (item.id == removeId) {
						c.getDuel().fromDuel(removeId, removeSlot,
								c.getDuel().stakedItems.get(removeSlot).amount);
					}
				}

			} else {
				c.getDuel().fromDuel(removeId, removeSlot, 28);
			}
			break;

		}
	}

}
