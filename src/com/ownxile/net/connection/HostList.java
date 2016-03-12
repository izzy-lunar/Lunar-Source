package com.ownxile.net.connection;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import org.apache.mina.common.IoSession;

import com.ownxile.config.FileConfig;
import com.ownxile.config.GameConfig;
import com.ownxile.core.World;
import com.ownxile.core.World.WorldStatus;

public class HostList {

	private static HostList list = new HostList();

	public static HostList getHostList() {
		return list;
	}

	private final Map<String, Integer> connections = new HashMap<String, Integer>();

	public synchronized boolean add(IoSession session) {
		final String addr = ((InetSocketAddress) session.getRemoteAddress())
				.getAddress().getHostAddress();
		Integer amt = connections.get(addr);
		if (amt == null) {
			amt = 1;
		} else if (World.getWorldStatus() == WorldStatus.ONLINE) {
			amt += 1;
		}
		if (amt > GameConfig.FAILED_ATTEMPTS
				| FileConfig.bannedHosts.contains(addr)) {
			return false;
		} else {
			connections.put(addr, amt);
			return true;
		}
	}

	public synchronized void remove(IoSession session) {
		if (session.getAttribute("inList") != Boolean.TRUE) {
			return;
		}
		final String addr = ((InetSocketAddress) session.getRemoteAddress())
				.getAddress().getHostAddress();
		Integer amt = connections.get(addr);
		if (amt == null) {
			return;
		}
		amt -= 1;
		if (amt <= 0) {
			connections.remove(addr);
		} else {
			connections.put(addr, amt);
		}
	}

}
