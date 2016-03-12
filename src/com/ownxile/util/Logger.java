package com.ownxile.util;

import java.io.PrintStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.ownxile.util.time.SimpleTimer;

public class Logger extends PrintStream {

	private Date cachedDate = new Date();
	private final DateFormat dateFormat = new SimpleDateFormat();
	private final SimpleTimer refreshTimer = new SimpleTimer();

	public Logger(PrintStream out) {
		super(out);
	}

	private String getPrefix() {
		if (refreshTimer.elapsed() > 1000) {
			refreshTimer.reset();
			cachedDate = new Date();
		}
		return dateFormat.format(cachedDate);
	}

	@Override
	public void print(String str) {
		if (str.startsWith("debug:")) {
			super.print("[" + getPrefix() + "] DEBUG: " + str.substring(6));
		} else {
			super.print("[" + getPrefix() + "]: " + str);
		}
	}
}
