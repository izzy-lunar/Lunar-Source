package com.ownxile.rs2.world.economy;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.ownxile.util.Logger;

public class CharacterItemRemoval {

	public static void main(String[] args) {

		System.setOut(new Logger(System.out));
		System.setErr(new Logger(System.err));

		final int[] items = { 12601, 12603, 12605, 8844, 11732, 13740, 555,
				560, 565, 10344, 10346, 10348, 10350, 10352, 10330, 10332,
				10334, 10336, 10338, 10340, 10342, 8284, 8286 };

		File charFolder;
		BufferedWriter bw;
		BufferedReader br;
		BufferedWriter tmpwr;
		BufferedReader tmpr;
		boolean overwrite = false;
		String read;
		final String dir = "data/accounts/";
		double done = 0;
		int percent = 0;
		int percentRounded = 0;

		charFolder = new File(dir);

		if (!charFolder.exists()) {
			System.out.println("The directory " + dir + " was not found.");
			return;
		} else if (charFolder.list().length == 0) {
			System.out.println("The specified directory is empty.");
			return;
		}
		final String file[] = charFolder.list();
		for (final String s : file) {
			try {
				final File charFile = new File(dir + s);
				final File tmpFile = new File(dir + s + ".tmp");
				tmpFile.createNewFile();
				br = new BufferedReader(new FileReader(charFile));
				tmpwr = new BufferedWriter(new FileWriter(tmpFile, true));

				while ((read = br.readLine()) != null) {

					if (read.equals("[ITEMS]") || read.equals("[EQUIPMENT]")) {
						overwrite = true;
					}

					if (read.equals("[FRIENDS]") || read.equals("[LOOK]")) {
						overwrite = false;
					}

					if (!overwrite) {
						tmpwr.write(read);
						tmpwr.newLine();
					} else {
						boolean found = false;
						for (final int i : items) {
							if (read.contains("\t" + i + "\t")
									|| read.contains("\t" + (i + 1) + "\t")) {
								found = true;
							}
						}
						if (!found) {
							tmpwr.write(read);
							tmpwr.newLine();
						}
					}
				}
				tmpwr.flush();
				tmpwr.close();
				br.close();

				charFile.delete();

				bw = new BufferedWriter(new FileWriter(charFile, true));
				tmpr = new BufferedReader(new FileReader(tmpFile));

				while ((read = tmpr.readLine()) != null) {
					bw.write(read);
					bw.newLine();
				}

				bw.flush();
				bw.close();
				tmpr.close();
				tmpFile.delete();

				done++;

				if ((percent = (int) (done / file.length * 100)) % 5 < 5
						&& percent - percent % 5 != percentRounded) {
					System.out.println((percentRounded = percent - percent % 5)
							+ "%");
				}
			} catch (final IOException Ioe) {
				Ioe.printStackTrace();
			}
		}
		System.out.println("Finished!");
	}
}