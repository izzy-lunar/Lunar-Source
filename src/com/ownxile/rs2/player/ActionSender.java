package com.ownxile.rs2.player;

import com.ownxile.util.Stream;

public class ActionSender {

	private Player client;
	private Stream stream;

	public ActionSender(Client client) {
		this.stream = client.getOutStream();
		this.client = client;
	}

	/**
	 * @packet 50 - adds friends to friend list
	 * @param playerName
	 * @param world
	 */
	public ActionSender addToFriendsList(long playerName, int world) {
		if (world != 0) {
			world += 9;
		}
		stream.createFrame(50);
		stream.writeQWord(playerName);
		stream.writeByte(world);
		return this;
	}

	/**
	 * @packet 254 - shows hint (npc)
	 * @param id
	 */
	public ActionSender createNpcHint(int id) {
		stream.createFrame(254);
		stream.writeByte(1);
		stream.writeWord(id);
		stream.write3Byte(0);
		return this;
	}

	/**
	 * @packet 254 shows hint - (object)
	 * @param x
	 * @param y
	 * @param height
	 * @param pos
	 */
	public ActionSender createObjectHints(int x, int y, int height, int pos) {
		stream.createFrame(254);
		stream.writeByte(pos);
		stream.writeWord(x);
		stream.writeWord(y);
		stream.writeByte(height);
		return this;
	}

	/**
	 * @packet 254 - shows hint (player)
	 * @param type
	 * @param id
	 */
	public ActionSender createPlayerHints(int type, int id) {
		stream.createFrame(254);
		stream.writeByte(type);
		stream.writeWord(id);
		stream.write3Byte(0);
		return this;
	}

	/**
	 * @packet 85 - create a projectile
	 * @param x
	 * @param y
	 * @param offX
	 * @param offY
	 * @param angle
	 * @param speed
	 * @param gfxMoving
	 * @param startHeight
	 * @param endHeight
	 * @param lockon
	 * @param time
	 */
	public ActionSender createProjectile(int x, int y, int offX, int offY,
			int angle, int speed, int gfxMoving, int startHeight,
			int endHeight, int lockon, int time) {
		stream.createFrame(85);
		stream.writeByteC(y - client.getMapRegionY() * 8 - 2);
		stream.writeByteC(x - client.getMapRegionX() * 8 - 3);
		stream.createFrame(117);
		stream.writeByte(angle);
		stream.writeByte(offY);
		stream.writeByte(offX);
		stream.writeWord(lockon);
		stream.writeWord(gfxMoving);
		stream.writeByte(startHeight);
		stream.writeByte(endHeight);
		stream.writeWord(time);
		stream.writeWord(speed);
		stream.writeByte(16);
		stream.writeByte(64);
		return this;
	}

	/**
	 * @packet 85 - creates a sloped projectile
	 * @param x
	 * @param y
	 * @param offX
	 * @param offY
	 * @param angle
	 * @param speed
	 * @param gfxMoving
	 * @param startHeight
	 * @param endHeight
	 * @param lockon
	 * @param time
	 * @param slope
	 */
	public ActionSender createSlopedProjectile(int x, int y, int offX,
			int offY, int angle, int speed, int gfxMoving, int startHeight,
			int endHeight, int lockon, int time, int slope) {
		stream.createFrame(85);
		stream.writeByteC(y - client.getMapRegionY() * 8 - 2);
		stream.writeByteC(x - client.getMapRegionX() * 8 - 3);
		stream.createFrame(117);
		stream.writeByte(angle);
		stream.writeByte(offY);
		stream.writeByte(offX);
		stream.writeWord(lockon);
		stream.writeWord(gfxMoving);
		stream.writeByte(startHeight);
		stream.writeByte(endHeight);
		stream.writeWord(time);
		stream.writeWord(speed);
		stream.writeByte(slope);
		stream.writeByte(64);
		return this;
	}

	/**
	 * @packet 70 - moves an interface component
	 * @param componentId
	 * @param x
	 * @param y
	 */
	public ActionSender moveInterfaceComponent(int componentId, int x, int y) {
		stream.createFrame(70);
		stream.writeWord(x);
		stream.writeWordBigEndian(y);
		stream.writeWordBigEndian(componentId);
		return this;
	}

	/**
	 * @return @packet 107
	 */
	public ActionSender resetCamera() {
		stream.createFrame(107);
		return this;
	}

	/**
	 * @return @packet 68
	 */
	public ActionSender resetSettings() {
		stream.createFrame(68);
		return this;
	}

	/**
	 * @packet 177
	 * @param x
	 * @param y
	 * @param height
	 * @param speed
	 * @param angle
	 * @return
	 */
	public ActionSender sendCameraChange(int x, int y, int height, int speed,
			int angle) {
		stream.createFrame(177);
		stream.writeByte(x / 64);
		stream.writeByte(y / 64);
		stream.writeWord(height);
		stream.writeByte(speed);
		stream.writeByte(angle);
		return this;
	}

	/**
	 * @packet 166
	 * @param x
	 * @param y
	 * @param height
	 * @param speed
	 * @param angle
	 */
	public ActionSender sendCameraSpin(int x, int y, int height, int speed,
			int angle) {
		stream.createFrame(166);
		stream.writeByte(x);
		stream.writeByte(y);
		stream.writeWord(height);
		stream.writeByte(speed);
		stream.writeByte(angle);
		return this;
	}

	/**
	 * @packet 219 - closes all interfaces
	 */
	public ActionSender sendCloseRequest() {
		stream.createFrame(219);
		return this;
	}

	/**
	 * @packet 36/86 (Handles Configs)
	 * @param config
	 *            id
	 * @param configvalue
	 */
	public ActionSender sendConfig(int id, int value) {
		if (value < Byte.MIN_VALUE || value > Byte.MAX_VALUE) {
			stream.createFrame(86);
			stream.writeWordBigEndian(id);
			stream.writeDWord(value);
		} else {
			stream.createFrame(36);
			stream.writeWordBigEndian(id);
			stream.writeByte(value);
		}
		return this;
	}

	/**
	 * @packet 208 / 97
	 * @param id
	 * @param walkable
	 * @return
	 */
	public ActionSender sendInterface(int id, boolean walkable) {
		if (walkable) {
			stream.createFrame(208);
			stream.writeWordBigEndian_dup(id);
		} else {
			stream.createFrame(97);
			stream.writeWord(id);
		}
		return this;
	}

	/**
	 * @packet 126 - sends text on interface
	 * @param s
	 * @param id
	 */
	public ActionSender sendInterfaceText(String s, int id) {
		stream.createFrameVarSizeWord(126);
		stream.writeString(s);
		stream.writeWordA(id);
		stream.endFrameVarSizeWord();
		return this;
	}

	/**
	 * @packet 99 - sets the map state
	 * @param map
	 *            state
	 */
	public ActionSender sendMapState(int state) {
		stream.createFrame(99);
		stream.writeByte(state);
		return this;
	}

	/**
	 * @packet 253
	 * @param message
	 * @return
	 */
	public ActionSender sendMessage(String message) {
		stream.createFrameVarSize(253);
		stream.writeString(message);
		stream.endFrameVarSize();
		return this;
	}

	/**
	 * @packet 61 - sends multi flag
	 * @param i1
	 */
	public ActionSender sendMultiFlag(int i1) {
		stream.createFrame(61);
		stream.writeByte(i1);
		return this;
	}

	/**
	 * @packet 74 - tells client to player music
	 * @param music
	 */
	public ActionSender sendMusic(int music) {
		stream.createFrame(74);
		stream.writeWordBigEndian(music);
		return this;
	}

	/**
	 * @packet 196 - sends a private message
	 * @param name
	 * @param rights
	 * @param chatmessage
	 * @param messagesize
	 */
	public ActionSender sendPM(long name, int rights, byte[] chatmessage,
			int messagesize) {
		stream.createFrameVarSize(196);
		stream.writeQWord(name);
		stream.writeDWord(client.lastChatId++);
		stream.writeByte(rights);
		stream.writeBytes(chatmessage, messagesize, 0);
		stream.endFrameVarSize();
		return this;
	}

	/**
	 * @packet 35
	 * @param verticleAmount
	 * @param verticleSpeed
	 * @param horizontalAmount
	 * @param horizontalSpeed
	 * @return
	 */
	public ActionSender sendShakeScreen(int verticleAmount, int verticleSpeed,
			int horizontalAmount, int horizontalSpeed) {
		stream.createFrame(35);
		stream.writeByte(verticleAmount);
		stream.writeByte(verticleSpeed);
		stream.writeByte(horizontalAmount);
		stream.writeByte(horizontalSpeed);
		return this;
	}

	/**
	 * @packet 134 - sends skill level data to client
	 * @param id
	 * @param level
	 * @param xp
	 */
	public ActionSender sendSkillLevel(int id, int level, int xp) {
		stream.createFrame(134);
		stream.writeByte(id);
		stream.writeDWord_v1(xp);
		stream.writeByte(level);
		return this;
	}

	/**
	 * @packet 174 - tells client to play sound
	 * @param id
	 * @param volume
	 * @param delay
	 */
	public ActionSender sendSound(int id, int volume, int delay) {
		stream.createFrameVarSize(174);
		stream.writeWord(id);
		stream.writeByte(volume);
		stream.writeWord(delay);
		stream.endFrameVarSize();
		return this;
	}

	/**
	 * @packet 121 - play a temporary song
	 * @param songId
	 * @param i
	 */
	public ActionSender sendTemporarySong(int songId, int i) {
		stream.createFrame(121);
		stream.writeWordBigEndian(songId);
		stream.writeWordBigEndian(i);
		return this;
	}

	/**
	 * @packet 187 - opens a url
	 * @param s
	 */
	public ActionSender sendUrl(String s) {
		stream.createFrameVarSizeWord(187);
		stream.writeString(s);
		return this;
	}

	/**
	 * @packet 206 - Sends chatbox options
	 * @param pubChat
	 *            id
	 * @param privChat
	 *            id
	 * @param trade
	 *            id
	 */
	public ActionSender setChatOptions(int pubChat, int privChat, int trade) {
		stream.createFrame(206);
		stream.writeByte(pubChat);
		stream.writeByte(privChat);
		stream.writeByte(trade);
		return this;
	}

	/**
	 * @packet 71 - sets sidebar interface
	 * @param menuId
	 * @param form
	 */
	public ActionSender setSidebarInterface(int menuId, int form) {
		stream.createFrame(71);
		stream.writeWord(form);
		stream.writeByteA(menuId);
		return this;
	}

	/**
	 * @packet 104 - shows an option
	 * @param i
	 * @param l
	 * @param s
	 */
	public ActionSender showOption(int i, int l, String s) {
		stream.createFrameVarSize(104);
		stream.writeByteC(i);
		stream.writeByteA(l);
		stream.writeString(s);
		stream.endFrameVarSize();
		return this;
	}

	/**
	 * @packet 85 - sends a still graphic
	 * @param id
	 * @param x
	 * @param y
	 * @param height
	 * @param time
	 */
	public ActionSender stillGfx(int id, int x, int y, int height, int time) {
		stream.createFrame(85);
		stream.writeByteC(y - client.getMapRegionY() * 8);
		stream.writeByteC(x - client.getMapRegionX() * 8);
		stream.createFrame(4);
		stream.writeByte(0);
		stream.writeWord(id);
		stream.writeByte(height);
		stream.writeWord(time);
		return this;
	}

}
