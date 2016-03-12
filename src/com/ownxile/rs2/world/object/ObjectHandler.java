package com.ownxile.rs2.world.object;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.ownxile.config.FileConfig;
import com.ownxile.rs2.player.Client;
import com.ownxile.rs2.player.Player;
import com.ownxile.rs2.player.PlayerHandler;
import com.ownxile.rs2.world.object.type.ObjectPosition;
import com.ownxile.rs2.world.object.type.TemporaryObject;
import com.ownxile.util.Misc;

public class ObjectHandler {

	public List<TemporaryObject> globalObjects = new ArrayList<TemporaryObject>();

	public List<ObjectPosition> objectPositions = new ArrayList<ObjectPosition>();

	public ObjectHandler() {
		loadGlobalObjects(FileConfig.GLOBAL_OBJECTS_DIR);
	}

	/**
	 * Adds object to list
	 **/
	public void addObject(TemporaryObject object) {
		globalObjects.add(object);
	}

	/**
	 * Creates the object for anyone who is within 60 squares of the object
	 **/
	public void globalObject(int aa, int bb, int cc, int height, int ee) {
		for (final Player p : PlayerHandler.players) {
			if (p != null) {
				final Client person = (Client) p;
				if (person != null) {
					if (person.distanceToPoint(bb, cc) <= 60) {
						person.getFunction().addObject(aa, bb, cc, height, 0,
								ee);
					}
				}
			}
		}
	}

	/**
	 * Creates the object for anyone who is within 60 squares of the object
	 **/
	public void globalObject2(int aa, int bb, int cc, int height, int ee,
			int face) {
		for (final Player p : PlayerHandler.players) {
			if (p != null) {
				final Client person = (Client) p;
				if (person != null) {
					if (person.distanceToPoint(bb, cc) <= 60) {
						person.getFunction().addObject(aa, bb, cc, height,
								face, ee);
					}
				}
			}
		}
	}

	public boolean loadGlobalObjects(String fileName) {
		String line = "";
		String token = "";
		String token2 = "";
		String token2_2 = "";
		String[] token3 = new String[10];
		boolean EndOfFile = false;
		BufferedReader objectFile = null;
		try {
			objectFile = new BufferedReader(new FileReader("./" + fileName));
		} catch (final FileNotFoundException fileex) {
			Misc.println(fileName + ": file not found.");
			return false;
		}
		try {
			line = objectFile.readLine();
		} catch (final IOException ioexception) {
			Misc.println(fileName + ": error loading file.");
			try {
				objectFile.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return false;
		}
		while (EndOfFile == false && line != null) {
			line = line.trim();
			final int spot = line.indexOf("=");
			if (spot > -1) {
				token = line.substring(0, spot);
				token = token.trim();
				token2 = line.substring(spot + 1);
				token2 = token2.trim();
				token2_2 = token2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token3 = token2_2.split("\t");
				if (token.equals("object")) {
					final TemporaryObject object = new TemporaryObject(
							Integer.parseInt(token3[0]),
							Integer.parseInt(token3[1]),
							Integer.parseInt(token3[2]),
							Integer.parseInt(token3[3]),
							Integer.parseInt(token3[4]),
							Integer.parseInt(token3[5]), 0);
					addObject(object);
				}
			} else {
				if (line.equals("[ENDOFOBJECTLIST]")) {
					try {
						objectFile.close();
					} catch (final IOException ioexception) {
					}
				}
			}
			try {
				line = objectFile.readLine();
			} catch (final IOException ioexception1) {
				EndOfFile = true;
			}
		}
		try {
			objectFile.close();
		} catch (final IOException ioexception) {
		}
		if (EndOfFile)
			return true;
		return false;
	}

	/**
	 * Does object exist
	 **/
	public TemporaryObject objectExists(int objectX, int objectY,
			int objectHeight) {
		for (final TemporaryObject o : globalObjects) {
			if (o.getObjectX() == objectX && o.getObjectY() == objectY
					&& o.getObjectHeight() == objectHeight) {
				return o;
			}
		}
		return null;
	}

	public boolean objectExists2(int objectX, int objectY, int objectHeight) {
		for (final TemporaryObject o : globalObjects) {
			if (o.getObjectX() == objectX && o.getObjectY() == objectY
					&& o.getObjectHeight() == objectHeight) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Creates the object for anyone who is within 60 squares of the object
	 **/
	public void placeObject(TemporaryObject o) {
		for (final Player p : PlayerHandler.players) {
			if (p != null) {
				removePlacedObject(o);
				globalObjects.add(o);
				final Client person = (Client) p;
				if (person != null) {
					if (person.absZ == o.getObjectHeight()
							&& o.objectTicks == 0) {
						if (person.distanceToPoint(o.getObjectX(),
								o.getObjectY()) <= 60) {
							person.getFunction().object(o.getObjectId(),
									o.getObjectX(), o.getObjectY(),
									o.getObjectHeight(), o.getObjectFace(),
									o.getObjectType());
						}
					}
				}
			}
		}
	}

	/**
	 * Removes object from list
	 **/
	public void removeObject(TemporaryObject object) {
		globalObjects.remove(object);
	}

	private void removePlacedObject(TemporaryObject o) {
		for (TemporaryObject s : globalObjects) {
			if (s.getObjectX() == o.objectX && s.getObjectY() == o.objectY
					&& s.getObjectHeight() == o.getObjectHeight()) {
				globalObjects.remove(s);
				break;
			}
		}
	}

	public void tick() {
		try {
			for (int j = 0; j < globalObjects.size(); j++) {
				if (globalObjects.get(j) != null) {
					final TemporaryObject o = globalObjects.get(j);
					if (o.objectTicks > 0) {
						o.objectTicks--;
					}
					if (o.objectTicks == 1) {
						final TemporaryObject deleteObject = objectExists(
								o.getObjectX(), o.getObjectY(),
								o.getObjectHeight());
						removeObject(deleteObject);
						o.objectTicks = 0;
						removeObject(o);
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Update objects when entering a new region or logging in
	 **/
	public void updateObjects(Client c) {
		for (final TemporaryObject o : globalObjects) {
			if (c != null) {
				if (c.absZ == o.getObjectHeight() && o.objectTicks == 0) {
					if (c.distanceToPoint(o.getObjectX(), o.getObjectY()) <= 60) {
						c.getFunction().object(o.getObjectId(), o.getObjectX(),
								o.getObjectY(), o.getObjectHeight(),
								o.getObjectFace(), o.getObjectType());
					}
				}
			}
		}
		if (c.distanceToPoint(2813, 3463) <= 60) {
			c.getFarming().updateHerbPatch(c.objectX, c.objectY);
		}
	}

}
