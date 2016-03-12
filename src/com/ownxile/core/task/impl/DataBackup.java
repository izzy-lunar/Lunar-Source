package com.ownxile.core.task.impl;

import java.io.File;
import java.io.IOException;

import com.ownxile.config.FileConfig;
import com.ownxile.core.World;
import com.ownxile.core.World.WorldStatus;
import com.ownxile.core.task.Task;
import com.ownxile.util.CopyDirectory;
import com.ownxile.util.time.DateAndTime;

public class DataBackup extends Task {

	private final static int CHECKS_PER_DAY = 2;
	private final static File file = new File(FileConfig.ACCOUNT_FILE_PATH);
	private final static int ONE_DAY = 144000;

	public DataBackup() {
		super(ONE_DAY / CHECKS_PER_DAY, true);
	}

	@Override
	protected void execute() {
		World.setStatus(WorldStatus.UPDATING);
		File to = new File(FileConfig.BACKUP_FILE_PATH + "/"
				+ DateAndTime.getTodaysDate());
		System.out
				.println("Backing up latest accounts to " + to.getPath() + "");
		try {
			CopyDirectory.copyDirectory(file, to);
		} catch (IOException e) {
			e.printStackTrace();
		}
		World.setStatus(WorldStatus.ONLINE);
	}
}
