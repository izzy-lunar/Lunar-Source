package com.ownxile.rs2.world.economy;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class CharacterItemReset {

	private static final File CHAR_DIR = new File("./etc/data/accounts/");

	public static void main(String[] args) {
		int count = 0;
		if (CHAR_DIR.exists() && CHAR_DIR.isDirectory()) {
			final File[] charFiles = CHAR_DIR.listFiles();
			for (File charFile : charFiles) {
				resetCharacter(charFile);
				System.out.println(count + ": Reset account: "
						+ charFile.toString());
				count++;
			}
		}
	}

	private static void resetCharacter(File charFile) {
		try {

			String tempData, tempAdd = "";
			int curEquip = 0;
			final File tempCharFile = new File(CHAR_DIR.toString()
					+ "ECOBOOST$TEMP");
			final BufferedReader fileStream = new BufferedReader(
					new FileReader(charFile));
			final BufferedWriter tempOut = new BufferedWriter(new FileWriter(
					tempCharFile));
			while ((tempData = fileStream.readLine()) != null) {
				if (!tempData.trim().startsWith("character-item =")
						&& !tempData.trim().startsWith("character-bank =")) {
					tempAdd = tempData.trim();

					if (tempData.trim().startsWith("character-equip =")) {
						tempAdd = "character-equip = " + curEquip + "\t-1\t0";
						curEquip++;
					}
					tempOut.write(tempAdd);
					tempOut.newLine();
				}
			}
			fileStream.close();
			tempOut.close();
			charFile.delete();
			tempCharFile.renameTo(charFile);
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
}
