package com.ownxile.rs2.world.object;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.ownxile.config.FileConfig;
import com.ownxile.core.World;
import com.ownxile.rs2.world.object.type.TemporaryObject;

public class Doors {

	private static int[] openDoors = { 1504, 1514, 1517, 1520, 1531, 1534,
			2033, 2035, 2037, 2998, 3271, 4468, 4697, 6101, 6103, 6105, 6107,
			6109, 6111, 6113, 6115, 6976, 6978, 8696, 8819, 10261, 10263,
			10265, 11708, 11710, 11712, 11715, 11994, 12445, 13002, 24375 };

	private static Doors singleton = null;

	public static Doors getSingleton() {
		if (singleton == null) {
			singleton = new Doors(FileConfig.SINGLE_DOOR_FILE_DIR);
		}
		return singleton;
	}

	private int currentFace;

	private File doorFile;

	private int doorId;

	private List<Doors> doors = new ArrayList<Doors>();

	private int doorX;

	private int doorY;

	private int doorZ;

	private int open;

	private int originalFace;

	private int originalId;

	private int originalX;
	private int originalY;
	private int type;

	private Doors(int door, int x, int y, int z, int face, int type, int open) {
		this.doorId = door;
		this.originalId = door;
		this.doorX = x;
		this.doorY = y;
		this.originalX = x;
		this.originalY = y;
		this.doorZ = z;
		this.originalFace = face;
		this.currentFace = face;
		this.type = type;
		this.open = open;
	}

	private Doors(String file) {
		doorFile = new File(file);
	}

	private boolean alreadyOpen(int id) {
		for (int openDoor : openDoors) {
			if (openDoor == id) {
				return true;
			}
		}
		return false;
	}

	private Doors getDoor(int id, int x, int y, int z) {
		for (Doors d : doors) {
			if (d.doorId == id) {
				if (d.doorX == x && d.doorY == y && d.doorZ == z) {
					return d;
				}
			}
		}
		return null;
	}

	private int getNextFace(Doors d) {
		int f = d.originalFace;
		if (d.type == 0) {
			if (d.open == 0) {
				if (d.originalFace == 0 && d.currentFace == 0) {
					f = 1;
				} else if (d.originalFace == 1 && d.currentFace == 1) {
					f = 2;
				} else if (d.originalFace == 2 && d.currentFace == 2) {
					f = 3;
				} else if (d.originalFace == 3 && d.currentFace == 3) {
					f = 0;
				} else if (d.originalFace != d.currentFace) {
					f = d.originalFace;
				}
			} else if (d.open == 1) {
				if (d.originalFace == 0 && d.currentFace == 0) {
					f = 3;
				} else if (d.originalFace == 1 && d.currentFace == 1) {
					f = 0;
				} else if (d.originalFace == 2 && d.currentFace == 2) {
					f = 1;
				} else if (d.originalFace == 3 && d.currentFace == 3) {
					f = 2;
				} else if (d.originalFace != d.currentFace) {
					f = d.originalFace;
				}
			}
		} else if (d.type == 9) {
			if (d.open == 0) {
				if (d.originalFace == 0 && d.currentFace == 0) {
					f = 3;
				} else if (d.originalFace == 1 && d.currentFace == 1) {
					f = 2;
				} else if (d.originalFace == 2 && d.currentFace == 2) {
					f = 1;
				} else if (d.originalFace == 3 && d.currentFace == 3) {
					f = 0;
				} else if (d.originalFace != d.currentFace) {
					f = d.originalFace;
				}
			} else if (d.open == 1) {
				if (d.originalFace == 0 && d.currentFace == 0) {
					f = 3;
				} else if (d.originalFace == 1 && d.currentFace == 1) {
					f = 0;
				} else if (d.originalFace == 2 && d.currentFace == 2) {
					f = 1;
				} else if (d.originalFace == 3 && d.currentFace == 3) {
					f = 2;
				} else if (d.originalFace != d.currentFace) {
					f = d.originalFace;
				}
			}
		}
		d.currentFace = f;
		return f;
	}

	public boolean handleDoor(int id, int x, int y, int z) {

		Doors d = getDoor(id, x, y, z);

		if (d == null) {
			if (DoubleDoors.getSingleton().handleDoor(id, x, y, z)) {
				return true;
			}
			return false;
		}
		int xAdjustment = 0, yAdjustment = 0;
		if (d.type == 0) {
			if (d.open == 0) {
				if (d.originalFace == 0 && d.currentFace == 0) {
					xAdjustment = -1;
				} else if (d.originalFace == 1 && d.currentFace == 1) {
					yAdjustment = 1;
				} else if (d.originalFace == 2 && d.currentFace == 2) {
					xAdjustment = 1;
				} else if (d.originalFace == 3 && d.currentFace == 3) {
					yAdjustment = -1;
				}
			} else if (d.open == 1) {
				if (d.originalFace == 0 && d.currentFace == 0) {
					yAdjustment = 1;
				} else if (d.originalFace == 1 && d.currentFace == 1) {
					xAdjustment = 1;
				} else if (d.originalFace == 2 && d.currentFace == 2) {
					yAdjustment = -1;
				} else if (d.originalFace == 3 && d.currentFace == 3) {
					xAdjustment = -1;
				}
			}
		} else if (d.type == 9) {
			if (d.open == 0) {
				if (d.originalFace == 0 && d.currentFace == 0) {
					xAdjustment = 1;
				} else if (d.originalFace == 1 && d.currentFace == 1) {
					xAdjustment = 1;
				} else if (d.originalFace == 2 && d.currentFace == 2) {
					xAdjustment = -1;
				} else if (d.originalFace == 3 && d.currentFace == 3) {
					xAdjustment = -1;
				}
			} else if (d.open == 1) {
				if (d.originalFace == 0 && d.currentFace == 0) {
					xAdjustment = 1;
				} else if (d.originalFace == 1 && d.currentFace == 1) {
					xAdjustment = 1;
				} else if (d.originalFace == 2 && d.currentFace == 2) {
					xAdjustment = -1;
				} else if (d.originalFace == 3 && d.currentFace == 3) {
					xAdjustment = -1;
				}
			}
		}
		if (xAdjustment != 0 || yAdjustment != 0) {
			TemporaryObject o = new TemporaryObject(-1, d.doorX, d.doorY,
					d.doorZ, 0, d.type, 0);
			World.getObjectHandler().placeObject(o);
		}
		if (d.doorX == d.originalX && d.doorY == d.originalY) {
			d.doorX += xAdjustment;
			d.doorY += yAdjustment;
		} else {
			TemporaryObject o = new TemporaryObject(-1, d.doorX, d.doorY,
					d.doorZ, 0, d.type, 0);
			World.getObjectHandler().placeObject(o);
			d.doorX = d.originalX;
			d.doorY = d.originalY;
		}
		if (d.doorId == d.originalId) {
			if (d.open == 0) {
				d.doorId += 1;
			} else if (d.open == 1) {
				d.doorId -= 1;
			}
		} else if (d.doorId != d.originalId) {
			if (d.open == 0) {
				d.doorId -= 1;
			} else if (d.open == 1) {
				d.doorId += 1;
			}
		}
		World.getObjectHandler().placeObject(
				new TemporaryObject(d.doorId, d.doorX, d.doorY, d.doorZ,
						getNextFace(d), d.type, 0));
		return true;
	}

	public void init() {
		try {
			singleton.processLineByLine();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	protected void processLine(String line) {
		Scanner scanner = new Scanner(line);
		scanner.useDelimiter(" ");
		try {
			while (scanner.hasNextLine()) {
				int id = Integer.parseInt(scanner.next());
				int x = Integer.parseInt(scanner.next());
				int y = Integer.parseInt(scanner.next());
				int f = Integer.parseInt(scanner.next());
				int z = Integer.parseInt(scanner.next());
				int t = Integer.parseInt(scanner.next());
				doors.add(new Doors(id, x, y, z, f, t, alreadyOpen(id) ? 1 : 0));
			}
		} finally {
			scanner.close();
		}
	}

	private final void processLineByLine() throws FileNotFoundException {
		Scanner scanner = new Scanner(new FileReader(doorFile));
		try {
			while (scanner.hasNextLine()) {
				processLine(scanner.nextLine());
			}
		} finally {
			scanner.close();
		}
	}

}
