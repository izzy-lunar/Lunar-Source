package com.ownxile.rs2.player;

public class PlayerOptions {

	public static int ACCEPT_AID = 427, PRIVATE_CHAT = 287, CHAT_EFFECTS = 171,
			MOUSE_BUTTONS = 170, BRIGHTNESS = 166;

	private Client client;
	public boolean acceptAid, privateChat, chatEffects, mouseButtons;
	public int brightness = 4;

	public PlayerOptions(Client client) {
		this.client = client;
	}

	public boolean handleClickButton(int buttonId) {
		switch (buttonId) {
		case 3145:
			mouseButtons = !mouseButtons;
			return true;
		case 3147:
			chatEffects = !chatEffects;
			return true;
		case 3189:
			privateChat = !privateChat;
			return true;
		case 48176:
			acceptAid = !acceptAid;
			return true;
		case 3138:
			brightness = 1;
			return true;
		case 3140:
			brightness = 2;
			return true;
		case 3142:
			brightness = 3;
			return true;
		case 3144:
			brightness = 4;
			return true;
		}

		return false;
	}

	private void sendConfig(int id, int value) {
		if (value < Byte.MIN_VALUE || value > Byte.MAX_VALUE) {
			client.getOutStream().createFrame(86);
			client.getOutStream().writeWordBigEndian(id);
			client.getOutStream().writeDWord(value);
		} else {
			client.getOutStream().createFrame(36);
			client.getOutStream().writeWordBigEndian(id);
			client.getOutStream().writeByte(value);
		}
	}

	public void updateSettings() {
		sendConfig(MOUSE_BUTTONS, mouseButtons ? 1 : 0);
		sendConfig(CHAT_EFFECTS, chatEffects ? 1 : 0);
		sendConfig(PRIVATE_CHAT, privateChat ? 1 : 0);
		sendConfig(ACCEPT_AID, acceptAid ? 1 : 0);
		sendConfig(BRIGHTNESS, brightness);
	}
}
