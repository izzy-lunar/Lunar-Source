package com.ownxile.util.file;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

public class FileLog {

	public static byte getByte(String file) {
		byte value = 0;
		try {
			DataInputStream dat = new DataInputStream(new FileInputStream(file));
			try {
				value = dat.readByte();
				dat.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return value;
	}

	public static void writeByte(String file, int i) {
		try {
			DataOutputStream writer = new DataOutputStream(
					new FileOutputStream(file));
			try {
				writer.writeByte(i);
			} finally {
				writer.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void writeLog(String filename, String text) {
		final String filePath = "./etc/data/logs/" + filename + ".txt";
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(filePath, true));
			bw.write(text);
			bw.newLine();
			bw.flush();
		} catch (final IOException ioe) {
			ioe.printStackTrace();
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (final IOException ioe2) {
				}
			}
		}
	}

	public static void writeLotteryWinner(String players) {
		final String filePath = "./etc/data/logs/lottery_winner.txt";
		File file = new File(filePath);
		if (file.exists()) {
			file.delete();
		}
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(filePath, true));
			bw.write(players);
			bw.newLine();
			bw.flush();
		} catch (final IOException ioe) {
			ioe.printStackTrace();
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (final IOException ioe2) {
				}
			}
		}
	}

	public static void writeLatestAchievment(String c) {
		final String filePath = "./etc/data/logs/latest_achievment.txt";
		File file = new File(filePath);
		if (file.exists()) {
			file.delete();
		}
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(filePath, true));
			bw.write(c);
			bw.newLine();
			bw.flush();
		} catch (final IOException ioe) {
			ioe.printStackTrace();
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (final IOException ioe2) {
				}
			}
		}
	}
}
