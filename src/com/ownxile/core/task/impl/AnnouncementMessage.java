package com.ownxile.core.task.impl;

import com.ownxile.core.World;
import com.ownxile.core.task.Task;

public class AnnouncementMessage extends Task {

	public AnnouncementMessage() {
		super(200, true);
	}

	private String message;

	/**
	 * @return the message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	private String managerName;

	public String getManagerName() {
		return managerName;
	}

	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}

	public void endEvent() {
		World.sendMessage("@dre@The " + getMessage()
				+ " community event has ended, special thanks to "
				+ getManagerName() + ".");
		message = null;
		stop();
	}

	@Override
	protected void execute() {
		if (message != null) {
			World.sendMessage("@dre@There is currently a community event at "
					+ getMessage() + " being run by " + managerName + ".");
		} else {
			message = null;
			stop();
		}

	}
}
