package com.ownxile.rs2.content.cmd;

import com.ownxile.rs2.player.Client;

public interface CommandHandler {

	public void handleCommand(Client player, String cmd);
}
