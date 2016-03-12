package com.ownxile.util.file;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class DataFile {

	private DataInputStream dataInputStream;
	private DataOutputStream dataOutputStream;
	private String fileName;

	public DataFile(String file) {
		this.fileName = file;
	}

	public void close() {
		try {
			dataInputStream.close();
			dataOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void initialize() {
		try {
			dataOutputStream = new DataOutputStream(new FileOutputStream(
					fileName));
			dataInputStream = new DataInputStream(new FileInputStream(fileName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public byte readByte() {
		byte b = 0;
		try {
			b = dataInputStream.readByte();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return b;
	}

	public void writeByte(int o) {
		try {
			dataOutputStream.writeByte(o);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
