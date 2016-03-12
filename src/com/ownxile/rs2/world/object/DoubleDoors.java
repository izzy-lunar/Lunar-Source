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

public class DoubleDoors {

	// Have not found any others yet. Maybe only 1 type of double
	// doors exist to operate.
	private static int[] openDoors = { 1520, 1517 };

	private static DoubleDoors singleton = null;

	public static DoubleDoors getSingleton() {
		if (singleton == null) {
			singleton = new DoubleDoors(FileConfig.DOUBLE_DOOR_FILE_DIR);
		}
		return singleton;
	}

	private int currentFace;

	private File doorFile;

	private int doorId;

	private List<DoubleDoors> doors = new ArrayList<DoubleDoors>();

	private int open;

	private int originalFace;

	private int originalId;

	private int originalX;

	private int originalY;
	private int x;
	private int y;
	private int z;

	public DoubleDoors(int id, int x, int y, int z, int f, int open) {
		this.doorId = id;
		this.originalId = id;
		this.open = open;
		this.x = x;
		this.originalX = x;
		this.y = y;
		this.z = z;
		this.originalY = y;
		this.currentFace = f;
		this.originalFace = f;
	}

	private DoubleDoors(String file) {
		doorFile = new File(file);
	}

	public void changeLeftDoor(DoubleDoors d) {
		int xAdjustment = 0, yAdjustment = 0;

		if (d.open == 0) {
			if (d.originalFace == 0 && d.currentFace == 0) {
				xAdjustment = -1;
			} else if (d.originalFace == 1 && d.currentFace == 1) {
				yAdjustment = 1;
			} else if (d.originalFace == 2 && d.currentFace == 2) {
				xAdjustment = +1;
			} else if (d.originalFace == 3 && d.currentFace == 3) {
				yAdjustment = -1;
			}
		} else if (d.open == 1) {
			if (d.originalFace == 0 && d.currentFace == 0) {
				yAdjustment = -1;
			} else if (d.originalFace == 1 && d.currentFace == 1) {
				xAdjustment = -1;
			} else if (d.originalFace == 2 && d.currentFace == 2) {
				xAdjustment = -1;
			} else if (d.originalFace == 3 && d.currentFace == 3) {
				xAdjustment = -1;
			}
		}
		if (xAdjustment != 0 || yAdjustment != 0) {
			World.getObjectHandler().placeObject(
					new TemporaryObject(-1, d.x, d.y, d.z, 0, 0, 0));
		}
		if (d.x == d.originalX && d.y == d.originalY) {
			d.x += xAdjustment;
			d.y += yAdjustment;
		} else {
			World.getObjectHandler().placeObject(
					new TemporaryObject(-1, d.x, d.y, d.z, 0, 0, 0));
			d.x = d.originalX;
			d.y = d.originalY;
		}
		if (d.doorId == d.originalId) {
			if (d.open == 0) {
				d.doorId += 1;
			} else if (d.open == 1) {
				d.doorId -= 1;
			}
		} else if (d.doorId != d.originalId) {
			if (d.open == 0) {
				d.doorId = d.originalId;
			} else if (d.open == 1) {
				d.doorId = d.originalId;
			}
		}
		World.getObjectHandler().placeObject(
				new TemporaryObject(d.doorId, d.x, d.y, d.z,
						getNextLeftFace(d), 0, 0));
	}

	public void changeRightDoor(DoubleDoors d) {
		int xAdjustment = 0, yAdjustment = 0;

		if (d.open == 0) {
			if (d.originalFace == 0 && d.currentFace == 0) {
				xAdjustment = -1;
			} else if (d.originalFace == 1 && d.currentFace == 1) {
				yAdjustment = 1;
			} else if (d.originalFace == 2 && d.currentFace == 2) {
				xAdjustment = +1;
			} else if (d.originalFace == 3 && d.currentFace == 3) {
				yAdjustment = -1;
			}
		} else if (d.open == 1) {
			if (d.originalFace == 0 && d.currentFace == 0) {
				xAdjustment = 1;
			} else if (d.originalFace == 1 && d.currentFace == 1) {
				xAdjustment = -1;
			} else if (d.originalFace == 2 && d.currentFace == 2) {
				yAdjustment = -1;
			} else if (d.originalFace == 3 && d.currentFace == 3) {
				xAdjustment = -1;
			}
		}
		if (xAdjustment != 0 || yAdjustment != 0) {
			World.getObjectHandler().placeObject(
					new TemporaryObject(-1, d.x, d.y, d.z, 0, 0, 0));
		}
		if (d.x == d.originalX && d.y == d.originalY) {
			d.x += xAdjustment;
			d.y += yAdjustment;
		} else {
			World.getObjectHandler().placeObject(
					new TemporaryObject(-1, d.x, d.y, d.z, 0, 0, 0));
			d.x = d.originalX;
			d.y = d.originalY;
		}
		if (d.doorId == d.originalId) {
			if (d.open == 0) {
				d.doorId += 1;
			} else if (d.open == 1) {
				d.doorId -= 1;
			}
		} else if (d.doorId != d.originalId) {
			if (d.open == 0) {
				d.doorId = d.originalId;
			} else if (d.open == 1) {
				d.doorId = d.originalId;
			}
		}
		World.getObjectHandler().placeObject(
				new TemporaryObject(d.doorId, d.x, d.y, d.z,
						getNextRightFace(d), 0, 0));
	}

	private DoubleDoors getDoor(int id, int x, int y, int z) {
		for (DoubleDoors d : doors) {
			if (d.doorId == id) {
				if (d.x == x && d.y == y && d.z == z) {
					return d;
				}
			}
		}
		return null;
	}

	private int getNextLeftFace(DoubleDoors d) {
		int f = d.originalFace;

		if (d.open == 0) {
			if (d.originalFace == 0 && d.currentFace == 0) {
				f = 3;
			} else if (d.originalFace == 1 && d.currentFace == 1) {
				f = 0;
			} else if (d.originalFace == 2 && d.currentFace == 2) {
				f = 1;
			} else if (d.originalFace == 3 && d.currentFace == 3) {
				f = 0;
			} else if (d.originalFace != d.currentFace) {
				f = d.originalFace;
			}
		} else if (d.open == 1) {
			if (d.originalFace == 0 && d.currentFace == 0) {
				f = 1;
			} else if (d.originalFace == 1 && d.currentFace == 1) {
				f = 2;
			} else if (d.originalFace == 2 && d.currentFace == 2) {
				f = 1;
			} else if (d.originalFace == 3 && d.currentFace == 3) {
				f = 2;
			} else if (d.originalFace != d.currentFace) {
				f = d.originalFace;
			}
		}
		d.currentFace = f;
		return f;
	}

	private int getNextRightFace(DoubleDoors d) {
		int f = d.originalFace;

		if (d.open == 0) {
			if (d.originalFace == 0 && d.currentFace == 0) {
				f = 1;
			} else if (d.originalFace == 1 && d.currentFace == 1) {
				f = 2;
			} else if (d.originalFace == 2 && d.currentFace == 2) {
				f = 3;
			} else if (d.originalFace == 3 && d.currentFace == 3) {
				f = 2;
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
		d.currentFace = f;
		return f;
	}

	public boolean handleDoor(int id, int x, int y, int z) {
		DoubleDoors doorClicked = getDoor(id, x, y, z);

		if (doorClicked == null) {
			return false;
		}
		if (doorClicked.open == 0) {
			if (doorClicked.originalFace == 0) {
				DoubleDoors lowerDoor = getDoor(id - 3, x, y - 1, z);
				DoubleDoors upperDoor = getDoor(id + 3, x, y + 1, z);
				if (lowerDoor != null) {
					changeLeftDoor(lowerDoor);
					changeRightDoor(doorClicked);
				} else if (upperDoor != null) {
					changeLeftDoor(doorClicked);
					changeRightDoor(upperDoor);
				}
			} else if (doorClicked.originalFace == 1) {
				DoubleDoors westDoor = getDoor(id - 3, x - 1, y, z);
				DoubleDoors eastDoor = getDoor(id + 3, x + 1, y, z);
				if (westDoor != null) {
					changeLeftDoor(westDoor);
					changeRightDoor(doorClicked);
				} else if (eastDoor != null) {
					changeLeftDoor(doorClicked);
					changeRightDoor(eastDoor);
				}
			} else if (doorClicked.originalFace == 2) {
				DoubleDoors lowerDoor = getDoor(id - 3, x, y + 1, z);
				DoubleDoors upperDoor = getDoor(id + 3, x, y - 1, z);
				if (lowerDoor != null) {
					changeLeftDoor(lowerDoor);
					changeRightDoor(doorClicked);
				} else if (upperDoor != null) {
					changeLeftDoor(doorClicked);
					changeRightDoor(upperDoor);
				}
			} else if (doorClicked.originalFace == 3) {
				DoubleDoors westDoor = getDoor(id + 3, x - 1, y, z);
				DoubleDoors eastDoor = getDoor(id - 3, x + 1, y, z);
				if (westDoor != null) {
					changeLeftDoor(westDoor);
					changeRightDoor(doorClicked);
				} else if (eastDoor != null) {
					changeLeftDoor(doorClicked);
					changeRightDoor(eastDoor);
				}
			}
		} else if (doorClicked.open == 1) {
			if (doorClicked.originalFace == 0) {
				DoubleDoors westDoor = getDoor(id - 3, x - 1, y, z);
				DoubleDoors upperDoor = getDoor(id + 3, x + 1, y, z);
				if (westDoor != null) {
					changeLeftDoor(westDoor);
					changeRightDoor(doorClicked);
				} else if (upperDoor != null) {
					changeLeftDoor(doorClicked);
					changeRightDoor(upperDoor);
				}
			} else if (doorClicked.originalFace == 1) {
				DoubleDoors northDoor = getDoor(id - 3, x, y + 1, z);
				DoubleDoors southDoor = getDoor(id + 3, x, y - 1, z);
				if (northDoor != null) {
					changeLeftDoor(northDoor);
					changeRightDoor(doorClicked);
				} else if (southDoor != null) {
					changeLeftDoor(doorClicked);
					changeRightDoor(southDoor);
				}
			} else if (doorClicked.originalFace == 2) {
				DoubleDoors westDoor = getDoor(id - 3, x - 1, y, z);
				DoubleDoors eastDoor = getDoor(id + 3, x, y - 1, z);
				if (westDoor != null) {
					changeLeftDoor(westDoor);
					changeRightDoor(doorClicked);
				} else if (eastDoor != null) {
					changeLeftDoor(doorClicked);
					changeRightDoor(eastDoor);
				}
			} else if (doorClicked.originalFace == 3) {
				DoubleDoors northDoor = getDoor(id - 3, x, y + 1, z);
				DoubleDoors southDoor = getDoor(id + 3, x, y - 1, z);
				if (northDoor != null) {
					changeLeftDoor(northDoor);
					changeRightDoor(doorClicked);
				} else if (southDoor != null) {
					changeLeftDoor(doorClicked);
					changeRightDoor(southDoor);
				}
			}
		}
		return true;
	}

	public void init() {
		try {
			singleton.processLineByLine();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public boolean isOpenDoor(int id) {
		for (int openDoor : openDoors) {
			if (id == openDoor || id + 3 == openDoor) {
				return true;
			}
		}
		return false;
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
				doors.add(new DoubleDoors(id, x, y, z, f, isOpenDoor(id) ? 1
						: 0));
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
