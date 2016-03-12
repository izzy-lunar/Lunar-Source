package com.ownxile.util.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import com.ownxile.rs2.world.clan.Clan;

/**
 * @author Robbie <Roboyto>
 * @see Contains {@link Serializable} data
 */
public class SerializableData {

	public static Clan loadData(File file) {
		try {
			FileInputStream fileIn = new FileInputStream(file);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			Clan p = null;
			try {
				p = (Clan) in.readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			in.close();
			fileIn.close();
			return p;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void saveData(Clan clan, File file) {
		try {
			FileOutputStream fileOut = new FileOutputStream(file);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(clan);
			out.close();
			fileOut.close();
		} catch (IOException i) {
			i.printStackTrace();
		}
	}

}
