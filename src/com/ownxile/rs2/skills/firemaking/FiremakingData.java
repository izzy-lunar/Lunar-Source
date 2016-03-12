package com.ownxile.rs2.skills.firemaking;

import java.util.HashMap;

public enum FiremakingData {

	MAGIC_LOGS(1513, 75, 6000), MAPLE_LOGS(1517, 45, 3500), OAK_LOGS(1521, 15,
			2000), REGULAR_LOGS(1511, 1, 1000), WILLOW_LOGS(1519, 30, 3000), YEW_LOGS(
			1515, 60, 4500);

	static HashMap<Integer, FiremakingData> firemakingData = new HashMap<Integer, FiremakingData>();

	static {
		for (final FiremakingData i : FiremakingData.values()) {
			FiremakingData.firemakingData.put(i.logid, i);
		}
	}

	public static int getLevelForTree(int logs) {
		for (FiremakingData f : FiremakingData.values()) {
			if (logs == f.logid) {
				return f.level;
			}
		}
		return -1;
	}

	public int logid, level, xp;

	private FiremakingData(int logid, int level, int xp) {
		this.logid = logid;
		this.level = level;
		this.xp = xp;
	}

}
