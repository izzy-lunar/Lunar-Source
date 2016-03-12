package com.ownxile.core.task.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.ownxile.config.FileConfig;
import com.ownxile.core.task.Task;
import com.ownxile.rs2.player.PlayerHandler;

public class PlayersOnline extends Task {

	private final File file = new File(FileConfig.PLAYERS_ONLINE_FILE);

	public PlayersOnline(int cycles) {
		super(cycles);
	}

	@Override
	protected void execute() {
		write(PlayerHandler.getPlayerCount() + "");
	}

	private void write(String data) {
		if (file.exists()) {
			file.delete();
		}
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(file, true));
			bw.write(data);
			bw.flush();
		} catch (final IOException ioe) {
			// ioe.printStackTrace();
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (final IOException ioe2) {
				}
			}
			// VoteCache.purgeCache();
		}
	}

}
