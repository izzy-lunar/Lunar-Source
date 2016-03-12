package com.ownxile.core.task.impl;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.ownxile.config.GameConfig;
import com.ownxile.core.task.Task;
import com.ownxile.util.time.Uptime;

public class UptimeLogger extends Task {

	private final File file = new File(GameConfig.GAME_UPTIME_FILE);

	public UptimeLogger(int ticks) {
		super(ticks);
	}

	@Override
	protected void execute() {
		write(Uptime.get());
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
		}
	}

}
