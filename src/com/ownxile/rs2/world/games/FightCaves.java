package com.ownxile.rs2.world.games;

import com.ownxile.core.World;
import com.ownxile.rs2.npcs.NPCHandler;
import com.ownxile.rs2.player.Client;

/**
 * @author Robbie <Roboyto>
 * @category PvM Minigame
 */
public class FightCaves {

	private final static int[][] COORDINATES = { { 2403, 5094 },
			{ 2390, 5096 }, { 2392, 5077 }, { 2408, 5080 }, { 2413, 5108 },
			{ 2381, 5106 }, { 2379, 5072 }, { 2420, 5082 } };

	public static final int TZ_KIH = 2627, TZ_KEK_SPAWN = 2738, TZ_KEK = 2630,
			TOK_XIL = 2631, YT_MEJKOT = 2741, KET_ZEK = 2743, TZTOK_JAD = 2745;

	public static int getAtk(int npc) {
		switch (npc) {
		case TZ_KIH:
		case TZ_KEK_SPAWN:
			return 30;
		case TZ_KEK:
			return 50;
		case TOK_XIL:
			return 100;
		case YT_MEJKOT:
			return 150;
		case KET_ZEK:
			return 450;
		case TZTOK_JAD:
			return 650;
		}
		return 100;
	}

	public static int getDef(int npc) {
		switch (npc) {
		case TZ_KIH:
		case TZ_KEK_SPAWN:
			return 30;
		case TZ_KEK:
			return 50;
		case TOK_XIL:
			return 100;
		case YT_MEJKOT:
			return 150;
		case KET_ZEK:
			return 300;
		case TZTOK_JAD:
			return 500;
		}
		return 100;
	}

	public static int getHp(int npc) {
		switch (npc) {
		case TZ_KIH:
		case TZ_KEK_SPAWN:
			return 10;
		case TZ_KEK:
			return 20;
		case TOK_XIL:
			return 40;
		case YT_MEJKOT:
			return 80;
		case KET_ZEK:
			return 150;
		case TZTOK_JAD:
			return 250;
		}
		return 100;
	}

	public static int getMax(int npc) {
		switch (npc) {
		case TZ_KIH:
		case TZ_KEK_SPAWN:
			return 4;
		case TZ_KEK:
			return 7;
		case TOK_XIL:
			return 13;
		case YT_MEJKOT:
			return 28;
		case KET_ZEK:
			return 54;
		case TZTOK_JAD:
			return 97;
		}
		return 5;
	}

	/**
	 * Handles the correct tz-kek effect; when you kill tz-kek it splits into 2
	 * smaller version of itself.
	 * 
	 * @param c
	 *            The player.
	 * @param i
	 *            The npcId.
	 */
	public static void tzKekEffect(Client c, int i) {
		if (NPCHandler.npcs[i].npcType == TZ_KEK) {

			int x = NPCHandler.npcs[i].absX + 2;
			int y = NPCHandler.npcs[i].absY + 2;
			int x1 = NPCHandler.npcs[i].absX - 2;
			int y1 = NPCHandler.npcs[i].absY - 2;

			int hp = getHp(TZ_KEK_SPAWN);
			int max = getMax(TZ_KEK_SPAWN);
			int atk = getAtk(TZ_KEK_SPAWN);
			int def = getDef(TZ_KEK_SPAWN);

			if (c != null) {
				if (c.tzKekTimer == 0) {
					if (NPCHandler.npcs[i].isDead) {
						World.getNpcHandler().spawnNpc(c, TZ_KEK_SPAWN, x, y,
								c.absZ, 0, hp, max, atk, def, true, true);
						World.getNpcHandler().spawnNpc(c, TZ_KEK_SPAWN, x1, y1,
								c.absZ, 0, hp, max, atk, def, true, true);
					}
				}
			}
		}
	}

	/**
	 * Handles the correct tz-kih effect; prayer is drained by the formula:
	 * drain = damage + 1
	 * 
	 * @param c
	 *            The player
	 * @param i
	 *            The npcId
	 * @param damage
	 *            What the npchit
	 */
	public static void tzKihEffect(Client c, int i, int damage) {
		if (NPCHandler.npcs[i].npcType == TZ_KIH) {
			if (c != null) {
				if (c.playerLevel[5] > 0) {
					c.playerLevel[5] -= 1 + damage;
					c.getTask().refreshSkill(5);
				}
			}
		}
	}

	/**
	 * Holds the data for the 63 waves fight cave.
	 */
	private final int[][] WAVES = { /*
									 * {*TZ_KIH }, { TZ_KIH, TZ_KIH }, { TZ_KEK
									 * }, { TZ_KEK, TZ_KIH }, { TZ_KEK, TZ_KIH,
									 * TZ_KIH }, { TZ_KEK, TZ_KEK }, { TOK_XIL
									 * }, { TOK_XIL, TZ_KIH }, { TOK_XIL,
									 * TZ_KIH, TZ_KIH }, { TOK_XIL, TZ_KEK }, {
									 * TOK_XIL, TZ_KEK, TZ_KIH }, { TOK_XIL,
									 * TZ_KEK, TZ_KIH, TZ_KIH }, { TOK_XIL,
									 * TZ_KEK, TZ_KEK }, { TOK_XIL, TOK_XIL }, {
									 * YT_MEJKOT }, { YT_MEJKOT, TZ_KIH }, {
									 * YT_MEJKOT, TZ_KIH, TZ_KIH }, { YT_MEJKOT,
									 * TZ_KEK }, { YT_MEJKOT, TZ_KEK, TZ_KIH },
									 * { YT_MEJKOT, TZ_KEK, TZ_KIH, TZ_KIH }, {
									 * YT_MEJKOT, TZ_KEK, TZ_KEK }, { YT_MEJKOT,
									 * TOK_XIL }, { YT_MEJKOT, TOK_XIL, TZ_KIH
									 * }, { YT_MEJKOT, TOK_XIL, TZ_KIH, TZ_KIH
									 * }, { YT_MEJKOT, TOK_XIL, TZ_KEK }, {
									 * YT_MEJKOT, TOK_XIL, TZ_KEK, TZ_KIH }, {
									 * YT_MEJKOT, TOK_XIL, TZ_KEK, TZ_KIH,
									 * TZ_KIH }, { YT_MEJKOT, TOK_XIL, TZ_KEK,
									 * TZ_KEK }, { YT_MEJKOT, TOK_XIL, TOK_XIL
									 * }, { YT_MEJKOT, YT_MEJKOT }, { KET_ZEK },
									 * { KET_ZEK, TZ_KIH }, { KET_ZEK, TZ_KIH,
									 * TZ_KIH }, { KET_ZEK, TZ_KEK }, { KET_ZEK,
									 * TZ_KEK, TZ_KIH }, { KET_ZEK, TZ_KEK,
									 * TZ_KIH, TZ_KIH }, { KET_ZEK, TZ_KEK,
									 * TZ_KEK }, { KET_ZEK, TOK_XIL }, {
									 * KET_ZEK, TOK_XIL, TZ_KIH }, { KET_ZEK,
									 * TOK_XIL, TZ_KIH, TZ_KIH }, { KET_ZEK,
									 * TOK_XIL, TZ_KEK }, { KET_ZEK, TOK_XIL,
									 * TZ_KEK, TZ_KIH }, { KET_ZEK, TOK_XIL,
									 * TZ_KEK, TZ_KIH, TZ_KIH }, { KET_ZEK,
									 * TOK_XIL, TZ_KEK, TZ_KEK }, { KET_ZEK,
									 * TOK_XIL, TOK_XIL }, { KET_ZEK, YT_MEJKOT
									 * }, { KET_ZEK, YT_MEJKOT, TZ_KIH }, {
									 * KET_ZEK, YT_MEJKOT, TZ_KIH, TZ_KIH }, {
									 * KET_ZEK, YT_MEJKOT, TZ_KEK }, { KET_ZEK,
									 * YT_MEJKOT, TZ_KEK, TZ_KIH }, { KET_ZEK,
									 * YT_MEJKOT, TZ_KEK, TZ_KIH, TZ_KIH }, {
									 * KET_ZEK, YT_MEJKOT, TZ_KEK, TZ_KEK }, {
									 * KET_ZEK, YT_MEJKOT, TOK_XIL },
									 * 
									 * { KET_ZEK, YT_MEJKOT, TOK_XIL, TZ_KIH },
									 * { KET_ZEK, YT_MEJKOT, TOK_XIL, TZ_KIH,
									 * TZ_KIH }, { KET_ZEK, YT_MEJKOT, TOK_XIL,
									 * TZ_KEK }, { KET_ZEK, YT_MEJKOT, TOK_XIL,
									 * TZ_KEK, TZ_KIH }, { KET_ZEK, YT_MEJKOT,
									 * TOK_XIL, TZ_KEK, TZ_KIH, TZ_KIH }, {
									 * KET_ZEK, YT_MEJKOT, TOK_XIL, TZ_KEK,
									 * TZ_KEK }, { KET_ZEK, YT_MEJKOT, TOK_XIL,
									 * TOK_XIL }, { KET_ZEK, YT_MEJKOT,
									 * YT_MEJKOT }, { KET_ZEK, KET_ZEK },
									 */{ TZTOK_JAD } };

	public void npcChat(Client c, String s) {
		c.lastClickedNpcId = 2617;
		c.npcChat(s);
	}

	public void npcChat(Client c, String s, String s1) {
		c.lastClickedNpcId = 2617;
		c.npcChat(s, s1);
	}

	/**
	 * Handles spawning the next fightcave wave.
	 * 
	 * @param c
	 *            The player.
	 */
	public void spawnNextWave(Client c) {
		if (c != null) {
			if (c.waveId >= WAVES.length) {
				c.waveId = 0;
				return;
			}
			if (c.waveId < 0) {
				return;
			}
			int npcAmount = WAVES[c.waveId].length;
			for (int j = 0; j < npcAmount; j++) {
				int npc = WAVES[c.waveId][j];
				int X = COORDINATES[j][0];
				int Y = COORDINATES[j][1];
				int H = c.absZ;
				int hp = getHp(npc);
				int max = getMax(npc);
				int atk = getAtk(npc);
				int def = getDef(npc);
				if (npc == 2745) {
					npcChat(c, "Here comes Tz-tok Jad!");
				} else {

				}
				World.getNpcHandler().spawnNpc(c, npc, X, Y, H, 0, hp, max,
						atk, def, false, true);
			}
			c.tzhaarToKill = npcAmount;
			c.tzhaarKilled = 0;
		}
	}

}