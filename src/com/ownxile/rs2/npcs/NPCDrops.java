package com.ownxile.rs2.npcs;

import java.io.File;
import java.util.HashMap;
import java.util.Scanner;
import java.util.StringTokenizer;

import com.ownxile.config.FileConfig;
import com.ownxile.core.World;
import com.ownxile.rs2.items.ItemAssistant;
import com.ownxile.util.file.FileLog;

public class NPCDrops {

	public static HashMap<Integer, int[]> constantDrops = new HashMap<Integer, int[]>();

	public static HashMap<Integer, Integer> dropRarity = new HashMap<Integer, Integer>();
	public static HashMap<Integer, int[][]> normalDrops = new HashMap<Integer, int[][]>();
	public static HashMap<Integer, int[][]> rareDrops = new HashMap<Integer, int[][]>();

	public void loadConstants() {
		try {
			final File f = new File(FileConfig.NPC_CONSTANTS_DIR);
			final Scanner s = new Scanner(f);
			while (s.hasNextLine()) {
				final String line = s.nextLine();
				if (line.startsWith("#")) {
					continue;
				}
				final StringTokenizer constantTok = new StringTokenizer(line,
						"\t");
				final int npcId = Integer.parseInt(constantTok.nextToken());
				int count = 0;
				final int[] temp = new int[constantTok.countTokens()];
				while (constantTok.hasMoreTokens()) {
					temp[count] = Integer.parseInt(constantTok.nextToken());
					count++;
				}
				constantDrops.put(npcId, temp);
			}
			s.close();
		} catch (final Exception e) {
			e.printStackTrace();
		}

	}

	public void loadDrops() {
		try {
			final int[][][] npcDrops = new int[6500][][];
			final int[][][] rareDrops2 = new int[6500][][];
			final int[] itemRarity = new int[6500];
			final File f = new File(FileConfig.NPC_DROPS_DIR);
			final Scanner s = new Scanner(f);
			int loaded = 0;
			while (s.hasNextLine()) {
				String line = s.nextLine();
				if (line.startsWith("#")) {
					continue;
				}
				final StringTokenizer normalTok = new StringTokenizer(line,
						"\t");
				line = s.nextLine();
				if (line.startsWith("#")) {
					continue;
				}
				final StringTokenizer rareTok = new StringTokenizer(line, "\t");
				final String[] information = normalTok.nextToken().split(":");
				final int npcId = Integer.parseInt(information[0]);
				itemRarity[npcId] = Integer.parseInt(information[1]) - 1;
				npcDrops[npcId] = new int[normalTok.countTokens()][2];
				rareDrops2[npcId] = new int[rareTok.countTokens()][2];
				int count = 0;
				while (normalTok.hasMoreTokens()) {
					final String[] temp = normalTok.nextToken().split(":");
					npcDrops[npcId][count][0] = Integer.parseInt(temp[0]);
					npcDrops[npcId][count][1] = Integer.parseInt(temp[1]);
					count++;
				}
				count = 0;
				while (rareTok.hasMoreTokens()) {
					final String[] temp = rareTok.nextToken().split(":");
					rareDrops2[npcId][count][0] = Integer.parseInt(temp[0]);
					rareDrops2[npcId][count][1] = Integer.parseInt(temp[1]);
					count++;
				}
				normalDrops.put(npcId, npcDrops[npcId]);
				rareDrops.put(npcId, rareDrops2[npcId]);
				dropRarity.put(npcId, itemRarity[npcId]);

			}
			loaded = normalDrops.size() + rareDrops.size();
			System.out.println("Loaded " + loaded + " npc loot tables.");
			loadConstants();
			s.close();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	static String text = new String();

	public static void printDrops() {
		text = text
				+ "************ OwnXile full rare drop table database released by Robbie ************ \r\n";
		for (int i = 0; i < 6500; i++) {
			if (rareDrops.containsKey(i)) {
				double rarity = dropRarity.get(i);
				text = text + "************ "
						+ World.getNpcHandler().getNPCName(i)
						+ " ************\r\n";
				int[][] drops = normalDrops.get(i);
				text = text + "* Common drops \r\n";
				for (int d = 0; d < drops.length; d++) {
					text = text + ItemAssistant.getItemName(drops[d][0])
							+ "\r\n";
				}
				text = text + "* Rare drops (" + 100 / rarity + "%) \r\n";
				drops = rareDrops.get(i);

				for (int d = 0; d < drops.length; d++) {
					text = text + ItemAssistant.getItemName(drops[d][0])
							+ "\r\n";
				}
				text = text + "************************" + "\r\n";
			}
		}
		FileLog.writeLog("rare_drop_table_dump", text);
	}
}
