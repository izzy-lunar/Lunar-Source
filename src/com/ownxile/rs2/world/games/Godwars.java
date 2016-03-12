package com.ownxile.rs2.world.games;

import com.ownxile.rs2.player.Client;

public class Godwars {

	public static final int[] ARMADYL_NPCS = { 6222, 6223, 6225, 6227, 6229,
			6230, 6231, 6232, 6233, 6234, 6235, 6236, 6237, 6238, 6239, 6240,
			6241, 6242, 6243, 6244, 6245, 6246 };
	public static final int[] BANDOS_NPCS = { 6260, 6261, 6263, 6265, 6267,
			6268, 6269, 6270, 6271, 6272, 6273, 6274, 6275, 6276, 6277, 6278,
			6279, 6280, 6281, 6282, 6283 };

	private static final int KILLCOUNT_REQUIRED = 40;

	public static final int[] SARADOMIN_NPCS = { 6247, 6248, 6250, 6252, 6254,
			6255, 6256, 6257, 6258, 6259 };

	public static final int[] ZAMORAK_NPCS = { 6203, 6204, 6206, 6208, 6210,
			6211, 6212, 6213, 6214, 6215, 6216, 6217, 6218, 6219, 6220, 6221 };

	public final int MINIGAME_INTERFACE = 16210;

	public boolean canEnterRoom(Client client, int god) {
		switch (god) {
		case 1:
			if (client.bandos >= KILLCOUNT_REQUIRED) {
				return true;
			}
		case 2:
			if (client.zamorak >= KILLCOUNT_REQUIRED) {
				return true;
			}
		case 3:
			if (client.armadyl >= KILLCOUNT_REQUIRED) {
				return true;
			}
		case 4:
			if (client.saradomin >= KILLCOUNT_REQUIRED) {
				return true;
			}
		}
		return false;
	}

	public boolean inDungeon(Client client) {
		return client.absX > 2817 && client.absY > 5252 && client.absX < 2961
				&& client.absY < 5376;
	}

	public void updateInterface(Client client) {
		client.getFunction().sendFrame126("" + client.bandos, 16217);
		client.getFunction().sendFrame126("" + client.armadyl, 16216);
		client.getFunction().sendFrame126("" + client.saradomin, 16218);
		client.getFunction().sendFrame126("" + client.zamorak, 16219);

	}

	public void updateKillCount(Client client, int npc) {
		for (int zamorak : ZAMORAK_NPCS) {
			if (zamorak == npc) {
				client.zamorak++;
				client.needGwdInterfaceUpdate = true;
			}
		}
		for (int saradomin : SARADOMIN_NPCS) {
			if (saradomin == npc) {
				client.saradomin++;
				client.needGwdInterfaceUpdate = true;
			}
		}
		for (int armadyl : ARMADYL_NPCS) {
			if (armadyl == npc) {
				client.armadyl++;
				client.needGwdInterfaceUpdate = true;
			}
		}
		for (int bandos : BANDOS_NPCS) {
			if (bandos == npc) {
				client.bandos++;
				client.needGwdInterfaceUpdate = true;
			}
		}
	}

}
