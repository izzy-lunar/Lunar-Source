package com.ownxile.rs2.packets.misc;

import com.ownxile.core.World;
import com.ownxile.rs2.combat.range.CannonHandler;
import com.ownxile.rs2.content.music.SongLoader;
import com.ownxile.rs2.items.GroundItemHandler;
import com.ownxile.rs2.npcs.NPC;
import com.ownxile.rs2.npcs.NPCHandler;
import com.ownxile.rs2.player.Client;

public class ChangeRegions implements com.ownxile.rs2.packets.Packet {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		GroundItemHandler.reloadItems(c);
		World.getObjectManager().loadObjects(c);
		World.getObjectHandler().updateObjects(c);
		c.getFarming().loadPlants();
		if (c.nextChat > 0 && !c.cannotCloseWindows) {
			c.endChat();
		}
		if (c.skullTimer > 0) {
			c.isSkulled = true;
			c.headIconPk = 0;
			c.getFunction().requestUpdates();
		}
		SongLoader.loadSong(c);
		CannonHandler.changeRegion(c);
		for (int i = 0; i < NPCHandler.npcs.length; i++) {
			if (NPCHandler.npcs[i] == null)
				continue;
			if (!c.withinDistance(NPCHandler.npcs[i]))
				continue;
			NPC npc = NPCHandler.npcs[i];
			int x = (npc.getUpdateFlags().focusPointX / 2) - 1;
			int y = (npc.getUpdateFlags().focusPointY / 2) - 1;
			NPCHandler.npcs[i].turnNpc(x, y);
			if (NPCHandler.npcs[i].walkingType > 1) {
				switch (NPCHandler.npcs[i].walkingType) {
				case 2:
					NPCHandler.npcs[i].turnNpc(NPCHandler.npcs[i].absX - 1,
							NPCHandler.npcs[i].absY);
					break;
				case 3:
					NPCHandler.npcs[i].turnNpc(NPCHandler.npcs[i].absX + 1,
							NPCHandler.npcs[i].absY);
					break;
				case 4:
					NPCHandler.npcs[i].turnNpc(NPCHandler.npcs[i].absX,
							NPCHandler.npcs[i].absY + 1);
					break;
				default:
					if (NPCHandler.npcs[i].walkingType >= 0) {
						NPCHandler.npcs[i].turnNpc(NPCHandler.npcs[i].absX,
								NPCHandler.npcs[i].absY);
					}
					break;
				}
			}
		}
	}

}
