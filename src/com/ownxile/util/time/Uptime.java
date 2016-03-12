package com.ownxile.util.time;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

public class Uptime {

	private static final long DAY_MS = 86400000;
	private static final long HOUR_MS = 3600000;
	private static final long MINUTE_MS = 60000;

	public static String get() {
		RuntimeMXBean mx = ManagementFactory.getRuntimeMXBean();
		long uptime = mx.getUptime();
		long result = uptime;
		StringBuilder sb = new StringBuilder();
		if (result >= DAY_MS) {
			long days = result / DAY_MS;
			result = result - days * DAY_MS;
			sb.append(days + " days ");
		}
		if (result >= HOUR_MS) {
			long hours = result / HOUR_MS;
			result = result - hours * HOUR_MS;
			sb.append(hours + " hours ");
		}
		if (result >= MINUTE_MS) {
			long minutes = result / MINUTE_MS;
			sb.append(minutes + " minutes");
		}
		return sb.toString();
	}

}
