package com.ownxile.rs2.packets.item;

import com.ownxile.core.Plugin;
import com.ownxile.core.World;
import com.ownxile.core.task.Task;
import com.ownxile.rs2.npcs.NPC;
import com.ownxile.rs2.npcs.NPCHandler;
import com.ownxile.rs2.packets.Packet;
import com.ownxile.rs2.player.Client;
import com.ownxile.util.Misc;

public class ItemOnNpc implements Packet {

	final String[] randomResponses = { "Stop that!",
			"Do you have a death wish?", "I hate rotten tomatoes!",
			"What the...?", "Are you serious!", "I'll report you kid", "Ffs",
			"Omg..." };

	@Override
	public void processPacket(final Client c, int packetType, int packetSize) {
		final int itemId = c.getInStream().readSignedWordA();
		final int i = c.getInStream().readSignedWordA();
		final int slot = c.getInStream().readSignedWordBigEndian();
		if (!c.getItems().playerHasItem(itemId, 1, slot)) {
			return;
		}
		if (Plugin.execute("item_" + itemId + "_on_npc_"
				+ NPCHandler.npcs[i].npcType, c)) {
			return;
		}
		final NPC npc = NPCHandler.npcs[i];
		if (itemId == 2518) {
			c.deleteItem(2518);
			c.turnPlayerTo(npc.getX(), npc.getY());
			c.stopMovement();
			c.startAnimation(2779);
			World.getSynchronizedTaskScheduler().schedule(new Task(1, false) {
				int i = 0;

				@Override
				protected void execute() {
					i++;
					switch (i) {
					case 1:
						final int offY = (c.getX() - npc.getX()) * -1;
						final int offX = (c.getY() - npc.getY()) * -1;
						c.getFunction().createPlayersProjectile(c.getX(),
								c.getY(), offX, offY, 50, 40, 30, 30, 30, 1, 2);
						break;
					case 2:
						npc.forceChat(randomResponses[Misc
								.random(randomResponses.length - 1)]);
						npc.gfx0(31);
						npc.facePlayer(c.playerId);
						break;
					case 3:
						stop();
						break;
					}

				}
			});
		}
	}
}
