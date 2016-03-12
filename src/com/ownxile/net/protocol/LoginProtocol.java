package com.ownxile.net.protocol;

import java.math.BigInteger;

import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IoFuture;
import org.apache.mina.common.IoFutureListener;
import org.apache.mina.common.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import com.ownxile.config.Configuration;
import com.ownxile.config.FileConfig;
import com.ownxile.config.GameConfig;
import com.ownxile.core.World;
import com.ownxile.core.World.WorldStatus;
import com.ownxile.net.codec.GameCodecFactory;
import com.ownxile.net.packet.Packet;
import com.ownxile.net.packet.StaticPacketBuilder;
import com.ownxile.rs2.player.Client;
import com.ownxile.rs2.player.PlayerHandler;
import com.ownxile.rs2.player.PlayerSave;
import com.ownxile.util.ISAAC;
import com.ownxile.util.Misc;

public class LoginProtocol extends CumulativeProtocolDecoder {

	private static final BigInteger RSA_MODULUS = new BigInteger(
			"119254411130302925076874057481617081994583122342376187540972678778066524833060900416296135458612754734817029017625332895110397080963644639320467150448116541860843057284079657822066266725525367645963586171227380394121925251229975379918999678229305239209690740736979547579727151449340085076786067668856811817087");

	private static final BigInteger RSA_EXPONENT = new BigInteger(
			"80199260206554635089157990287086550678689237135021256537235910164708546553737981978849136735018364468656266489506979599601685169391209146488399975416181508133021505855965094125455220693711525826625060296381729289042079612384229222704012040716319779604449129960276930617236080598888943034918768264953604566353");

	@Override
	public void dispose(IoSession session) throws Exception {
		super.dispose(session);
	}

	@Override
	public boolean doDecode(final IoSession session, ByteBuffer in,
			ProtocolDecoderOutput out) {
		synchronized (session) {
			final Object loginStageObj = session.getAttribute("LOGIN_STAGE");
			int loginStage = 0;
			if (loginStageObj != null) {
				loginStage = (Integer) loginStageObj;
			} /*
			 * else { System.out.println("Unidentified connection request from "
			 * + session.getRemoteAddress() + "with " +
			 * session.getWrittenBytes() + " bytes of data."); }
			 */
			switch (loginStage) {
			case 0:
				if (2 <= in.remaining()) {
					final int protocol = in.get() & 0xff;
					@SuppressWarnings("unused")
					final int nameHash = in.get() & 0xff;
					if (protocol == 14) {
						final long serverSessionKey = ((long) (java.lang.Math
								.random() * 99999999D) << 32)
								+ (long) (java.lang.Math.random() * 99999999D);
						final StaticPacketBuilder s1Response = new StaticPacketBuilder();
						s1Response
								.setBare(true)
								.addBytes(new byte[] { 0, 0, 0, 0, 0, 0, 0, 0 })
								.addByte((byte) 0).addLong(serverSessionKey);
						session.setAttribute("SERVER_SESSION_KEY",
								serverSessionKey);
						session.write(s1Response.toPacket());
						session.setAttribute("LOGIN_STAGE", 1);
					}
					return true;
				} else {
					in.rewind();
					return false;
				}
			case 1:
				@SuppressWarnings("unused")
				int loginType = -1,
				loginPacketSize = -1,
				loginEncryptPacketSize = -1;
				if (2 <= in.remaining()) {
					loginType = in.get() & 0xff; // should be 16 or 18
					loginPacketSize = in.get() & 0xff;
					loginEncryptPacketSize = loginPacketSize - (36 + 1 + 1 + 2);
					if (loginPacketSize <= 0 || loginEncryptPacketSize <= 0) {
						// System.out.println("Zero or negative login size.");
						session.close();
						return false;
					}
				} else {
					in.rewind();
					return false;
				}
				if (loginPacketSize <= in.remaining()) {
					final int magic = in.get() & 0xff;
					final int version = in.getUnsignedShort();
					if (magic != 255) {
						session.close();
						return false;
					}
					if (version != 1) {
						// Dont Add Anything
					}
					@SuppressWarnings("unused")
					final int lowMem = in.get() & 0xff;
					for (int i = 0; i < 9; i++) {
						in.getInt();
					}
					loginEncryptPacketSize--;
					if (loginEncryptPacketSize != (in.get() & 0xff)) {
						System.out.println("Encrypted size mismatch.");
						session.close();
						return false;
					}
					byte[] encryptionBytes = new byte[loginEncryptPacketSize];
					in.get(encryptionBytes);
					ByteBuffer rsaBuffer = ByteBuffer.wrap(new BigInteger(
							encryptionBytes).modPow(RSA_EXPONENT, RSA_MODULUS)
							.toByteArray());
					if ((rsaBuffer.get() & 0xff) != 10) {
						System.out.println("Encrypted id != 10.");
						session.close();
						return false;
					}
					final long clientSessionKey = rsaBuffer.getLong();
					final long serverSessionKey = rsaBuffer.getLong();
					final int versionKey = rsaBuffer.getInt();

					if (versionKey == 0 || versionKey == 99735086) {
						session.close();
						System.out
								.println("The connection was blocked: code 1");
						return false;
					}

					final String name = readRS2String(rsaBuffer);
					final String pass = readRS2String(rsaBuffer);
					final int sessionKey[] = new int[4];
					sessionKey[0] = (int) (clientSessionKey >> 32);
					sessionKey[1] = (int) clientSessionKey;
					sessionKey[2] = (int) (serverSessionKey >> 32);
					sessionKey[3] = (int) serverSessionKey;
					final ISAAC inC = new ISAAC(sessionKey);
					for (int i = 0; i < 4; i++) {
						sessionKey[i] += 50;
					}
					final ISAAC outC = new ISAAC(sessionKey);

					load(session, versionKey, name, pass, inC, outC, version);
					session.getFilterChain().remove("protocolFilter");
					session.getFilterChain().addLast("protocolFilter",
							new ProtocolCodecFilter(new GameCodecFactory(inC)));

					return true;
				} else {
					in.rewind();
					return false;
				}
			}
		}
		return false;
	}

	private synchronized void load(final IoSession session, final int uid,
			String name, String pass, final ISAAC inC, final ISAAC outC,
			int version) {
		session.setAttribute("opcode", -1);
		session.setAttribute("size", -1);
		final int loginDelay = 20;
		int returnCode = 2;
		name = name.trim();
		name = name.toLowerCase();
		pass = pass.toLowerCase();
		final Client cl = new Client(session, -1);
		cl.playerName = Misc.optimizeText(name);
		cl.playerName2 = cl.playerName;
		cl.playerPass = pass;
		cl.setInStreamDecryption(inC);
		cl.setOutStreamDecryption(outC);
		cl.outStream.packetEncryption = outC;

		cl.saveCharacter = false;
		final char first = name.charAt(0);
		cl.properName = Character.toUpperCase(first)
				+ name.substring(1, name.length());

		final int index = World.getPlayerHandler().getFreeSlot();
		final int load = PlayerSave.loadGame(cl, cl.playerName, cl.playerPass);
		if (version != Configuration.VERSION) {
			System.out.println(version);
			returnCode = 6;

		} else if (load == 3) {
			returnCode = 3;
			cl.saveFile = false;
		} else if (PlayerHandler.isPlayerOn(name)) {
			returnCode = 5;
		} else if (index < 0) {
			returnCode = 8;
		} else if (!name.matches("[A-Za-z0-9 ]+")) {
			returnCode = 4;
		} else if (GameConfig.getWorld() == 2
				&& !FileConfig.donators.contains(cl.playerName)) {
			returnCode = 12;
		} else if (name.length() > 12) {
			returnCode = 8;
		} else if (FileConfig.bannedAccounts.contains(cl.playerName)
				|| FileConfig.permBannedAccounts.contains(cl.playerName)) {
			returnCode = 4;
		} else if (World.getWorldStatus().equals(WorldStatus.UPDATING)) {
			returnCode = 14;
		} else {
			returnCode = 2;
		}
		if (returnCode == 2) {
			for (int i = 0; i < cl.playerEquipment.length; i++) {
				if (cl.playerEquipment[i] == 0) {
					cl.playerEquipment[i] = -1;
					cl.playerEquipmentN[i] = 0;
				}
			}
			if (!World.getPlayerHandler().newPlayerClient(cl, index)) {
				returnCode = 7;
				cl.saveFile = false;
			} else {
				cl.saveFile = true;
			}

		}
		// System.out.println(returnCode);

		cl.packetType = -1;
		cl.packetSize = 0;

		final StaticPacketBuilder bldr = new StaticPacketBuilder();
		bldr.setBare(true);
		bldr.addByte((byte) returnCode);
		if (returnCode == 2) {
			cl.saveCharacter = true;
			bldr.addByte((byte) cl.playerRights);
		} else if (returnCode == 21) {
			bldr.addByte((byte) loginDelay);
		} else {
			bldr.addByte((byte) 0);
		}
		cl.isActive = true;
		bldr.addByte((byte) 0);
		final Packet pkt = bldr.toPacket();
		session.setAttachment(cl);
		session.write(pkt).addListener(new IoFutureListener() {
			@Override
			public void operationComplete(IoFuture arg0) {
				session.getFilterChain().remove("protocolFilter");
				session.getFilterChain().addFirst("protocolFilter",
						new ProtocolCodecFilter(new GameCodecFactory(inC)));
			}
		});
	}

	private synchronized String readRS2String(ByteBuffer in) {
		final StringBuilder sb = new StringBuilder();
		byte b;
		while ((b = in.get()) != 10) {
			sb.append((char) b);
		}
		return sb.toString();
	}

}
