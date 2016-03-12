package com.ownxile.rs2.packets;

import com.ownxile.config.GameConfig;
import com.ownxile.rs2.content.action.Talking;
import com.ownxile.rs2.packets.clicking.ClickNPC;
import com.ownxile.rs2.packets.clicking.ClickObject;
import com.ownxile.rs2.packets.clicking.ClickingButtons;
import com.ownxile.rs2.packets.clicking.ClickingInGame;
import com.ownxile.rs2.packets.clicking.ClickingStuff;
import com.ownxile.rs2.packets.clicking.FirstClickItem;
import com.ownxile.rs2.packets.clicking.SecondClickItem;
import com.ownxile.rs2.packets.clicking.ThirdClickItem;
import com.ownxile.rs2.packets.item.DropItem;
import com.ownxile.rs2.packets.item.ItemOnGroundItem;
import com.ownxile.rs2.packets.item.ItemOnItem;
import com.ownxile.rs2.packets.item.ItemOnNpc;
import com.ownxile.rs2.packets.item.ItemOnObject;
import com.ownxile.rs2.packets.item.MagicOnFloorItems;
import com.ownxile.rs2.packets.item.MagicOnItems;
import com.ownxile.rs2.packets.item.MoveItems;
import com.ownxile.rs2.packets.item.PickupItem;
import com.ownxile.rs2.packets.item.RemoveItem;
import com.ownxile.rs2.packets.item.WearItem;
import com.ownxile.rs2.packets.misc.ChangeAppearance;
import com.ownxile.rs2.packets.misc.ChangeRegions;
import com.ownxile.rs2.packets.misc.ClanChat;
import com.ownxile.rs2.packets.misc.Dialogue;
import com.ownxile.rs2.packets.misc.IdlePackt;
import com.ownxile.rs2.packets.misc.PrivateMessaging;
import com.ownxile.rs2.packets.misc.ReportAbuse;
import com.ownxile.rs2.packets.misc.SilentPacket;
import com.ownxile.rs2.packets.option.Option10;
import com.ownxile.rs2.packets.option.Option5;
import com.ownxile.rs2.packets.option.OptionAll;
import com.ownxile.rs2.packets.option.OptionX1;
import com.ownxile.rs2.packets.option.OptionX2;
import com.ownxile.rs2.packets.requests.FollowRequest;
import com.ownxile.rs2.packets.requests.RequestAttack;
import com.ownxile.rs2.packets.requests.RequestCommand;
import com.ownxile.rs2.packets.requests.RequestTrade;
import com.ownxile.rs2.packets.requests.RequestWalk;
import com.ownxile.rs2.player.Client;

public class PacketHandler {

	private static Packet packetId[] = new Packet[256];

	public static int getTotalWorkingPackets() {
		int i = 0;
		for (Packet packet : packetId) {
			if (packet != null)
				i++;
		}
		return i;
	}

	static {

		final SilentPacket u = new SilentPacket();
		packetId[3] = u;
		packetId[202] = u;
		packetId[77] = u;
		packetId[86] = u;
		packetId[78] = u;
		packetId[36] = u;
		packetId[226] = u;
		packetId[246] = u;
		packetId[148] = u;
		packetId[183] = u;
		packetId[230] = u;
		packetId[136] = u;
		packetId[189] = u;
		packetId[152] = u;
		packetId[200] = u;
		packetId[85] = u;
		packetId[165] = u;
		packetId[238] = u;
		packetId[150] = u;
		packetId[40] = new Dialogue();
		final ClickObject co = new ClickObject();
		packetId[132] = co;
		packetId[252] = co;
		packetId[70] = co;
		packetId[57] = new ItemOnNpc();
		final ClickNPC cn = new ClickNPC();
		packetId[72] = cn;
		packetId[131] = cn;
		packetId[155] = cn;
		packetId[17] = cn;
		packetId[21] = cn;
		packetId[16] = new SecondClickItem();
		packetId[75] = new ThirdClickItem();
		packetId[122] = new FirstClickItem();
		packetId[241] = new ClickingInGame();
		packetId[4] = new Talking();
		packetId[236] = new PickupItem();
		packetId[87] = new DropItem();
		packetId[185] = new ClickingButtons();
		packetId[130] = new ClickingStuff();
		packetId[103] = new RequestCommand();
		packetId[214] = new MoveItems();
		packetId[237] = new MagicOnItems();
		packetId[181] = new MagicOnFloorItems();
		packetId[202] = new IdlePackt();
		final RequestAttack ap = new RequestAttack();
		packetId[73] = ap;
		packetId[249] = ap;
		// packetId[128] = new ChallengePlayer();
		packetId[139] = new FollowRequest();
		packetId[39] = new RequestTrade();
		packetId[41] = new WearItem();
		packetId[145] = new RemoveItem();
		packetId[117] = new Option5();
		packetId[43] = new Option10();
		packetId[129] = new OptionAll();
		packetId[101] = new ChangeAppearance();
		final PrivateMessaging pm = new PrivateMessaging();
		packetId[188] = pm;
		packetId[126] = pm;
		packetId[215] = pm;
		packetId[59] = pm;
		packetId[95] = pm;
		packetId[133] = pm;
		packetId[135] = new OptionX1();
		packetId[208] = new OptionX2();
		final RequestWalk w = new RequestWalk();
		packetId[98] = w;
		packetId[164] = w;
		packetId[248] = w;
		packetId[53] = new ItemOnItem();
		packetId[192] = new ItemOnObject();
		packetId[25] = new ItemOnGroundItem();
		final ChangeRegions cr = new ChangeRegions();
		packetId[121] = cr;
		packetId[210] = cr;
		packetId[60] = new ClanChat();
		packetId[218] = new ReportAbuse();
	}

	public static void processPacket(Client c, int packetType, int packetSize) {
		if (packetType == -1) {
			return;
		}

		Packet p = packetId[packetType];
		if (p != null && packetType > 0 && packetType < 257
				&& packetType == c.packetType && packetSize == c.packetSize) {
			if (GameConfig.sendServerPackets && c.playerRights == 3) {
				c.sendMessage("PacketType: " + packetType + ". PacketSize: "
						+ packetSize + ".");
			}
			try {
				// System.out.println(packetType);
				p.processPacket(c, packetType, packetSize);
				c.timeOut = 100;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			// c.disconnected = true;
			System.out.println(c.playerName
					+ " is sending invalid PacketType: " + packetType
					+ ". PacketSize: " + packetSize);
			/*
			 * FileLog.writeLog("invalid_packets", c.playerName +
			 * "is sending invalid PacketType: " + packetType + ". PacketSize: "
			 * + packetSize);
			 */
		}
	}

}
