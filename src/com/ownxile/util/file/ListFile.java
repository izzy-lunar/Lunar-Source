package com.ownxile.util.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.ownxile.core.World;

public class ListFile {

	public ArrayList<String> arrayList = new ArrayList<String>();

	private String filePath;

	public ListFile(String filePath) {
		this.setFilePath(filePath);
		loadData();
		World.listFiles.add(this);
	}

	public void add(String name) {
		name = name.toLowerCase();
		addToList(name);
		arrayList.add(name);
	}

	private void addToList(String name) {
		try {
			final BufferedWriter out = new BufferedWriter(new FileWriter(
					filePath, true));
			try {
				out.newLine();
				out.write(name);
			} finally {
				out.close();
			}
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	public boolean contains(String name) {
		if (arrayList.contains(name.toLowerCase())) {
			return true;
		}
		return false;
	}

	public String getFilePath() {
		return filePath;
	}

	public int getSize() {
		return arrayList.size();
	}

	public void loadData() {
		String fileLine = null;
		if (!arrayList.isEmpty()) {
			arrayList.clear();
		}
		try {
			BufferedReader in = new BufferedReader(new FileReader(filePath));
			while ((fileLine = in.readLine()) != null) {
				arrayList.add(fileLine);
			}
			in.close();
			in = null;
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public void remove(String name) {
		name = name.toLowerCase();
		removeFromList(name);
		arrayList.remove(name);
	}

	private void removeFromList(String name) {
		try {
			final BufferedReader r = new BufferedReader(
					new FileReader(filePath));
			final ArrayList<String> contents = new ArrayList<String>();
			while (true) {
				String line = r.readLine();
				if (line == null) {
					break;
				} else {
					line = line.trim();
				}
				if (!line.equalsIgnoreCase(name)) {
					contents.add(line);
				}
			}
			r.close();
			final BufferedWriter w = new BufferedWriter(
					new FileWriter(filePath));
			for (final String line : contents) {
				w.write(line, 0, line.length());
				w.newLine();
			}
			w.flush();
			w.close();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

}
