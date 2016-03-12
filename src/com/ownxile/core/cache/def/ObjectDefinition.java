package com.ownxile.core.cache.def;

import com.ownxile.core.cache.util.CacheDataReader;
import com.ownxile.core.cache.util.CacheFileReader;
import com.ownxile.core.cache.util.MemoryArchive;

public final class ObjectDefinition {

	public static MemoryArchive archive;

	public static ObjectDefinition[] cache;

	public static int cacheIndex;

	public static boolean lowMem;

	public static byte[] getBuffer(String s) {
		try {

			final java.io.File f = new java.io.File("./etc/data/map/" + s);
			if (!f.exists()) {
				return null;
			}
			final byte[] buffer = new byte[(int) f.length()];
			final java.io.DataInputStream dis = new java.io.DataInputStream(
					new java.io.FileInputStream(f));
			dis.readFully(buffer);
			dis.close();
			return buffer;
		} catch (final Exception e) {
		}
		return null;
	}

	public static ObjectDefinition getObjectDef(int i) {
		for (int j = 0; j < 20; j++) {
			if (cache[j].type == i) {
				return cache[j];
			}
		}

		cacheIndex = (cacheIndex + 1) % 20;
		final ObjectDefinition class46 = cache[cacheIndex];
		class46.type = i;
		class46.setDefaults();
		final byte[] buffer = archive.get(i);
		if (buffer != null && buffer.length > 0) {
			class46.readValues(new CacheDataReader(buffer));
		}
		return class46;
	}

	public static void loadConfig() {
		archive = new MemoryArchive(new CacheFileReader(getBuffer("loc.dat")),
				new CacheFileReader(getBuffer("loc.idx")));
		cache = new ObjectDefinition[20];
		for (int k = 0; k < 20; k++) {
			cache[k] = new ObjectDefinition();
		}
	}

	public boolean aBoolean736;

	public boolean aBoolean757;

	public boolean aBoolean762;

	public boolean aBoolean764;

	public boolean aBoolean767;

	public boolean aBoolean779;
	public byte aByte737;
	public byte aByte742;
	public String actions[];
	public int anInt738;
	public int anInt740;
	public int anInt744;
	public int anInt745;
	public int anInt746;
	public int anInt748;
	public int anInt749;
	public int anInt758;
	public int anInt761;
	public int anInt768;
	public int anInt774;
	public int anInt775;
	public int anInt781;
	public int anInt783;
	public int[] anIntArray773;
	public int[] anIntArray776;
	public int childrenIDs[];
	public byte description[];
	public boolean hasActions;
	public int[] modifiedModelColors;
	public String name;
	public int[] originalModelColors;
	public int type;

	public ObjectDefinition() {
		type = -1;
	}

	public boolean aBoolean767() {
		return aBoolean767;
	}

	public boolean hasActions() {
		return hasActions;
	}

	public boolean hasName() {
		return name != null && name.length() > 1;
	}

	public void readValues(CacheDataReader stream) {
		do {
			final int type = stream.readUnsignedByte();
			if (type == 0) {
				break;
			}
			if (type == 1) {
				final int len = stream.readUnsignedByte();
				if (len > 0) {
					if (anIntArray773 == null || lowMem) {
						anIntArray776 = new int[len];
						anIntArray773 = new int[len];
						for (int k1 = 0; k1 < len; k1++) {
							anIntArray773[k1] = stream.readUnsignedWord();
							anIntArray776[k1] = stream.readUnsignedByte();
						}
					} else {
						stream.currentOffset += len * 3;
					}
				}
			} else if (type == 2) {
				name = stream.readNewString();
			} else if (type == 5) {
				final int len = stream.readUnsignedByte();
				if (len > 0) {
					if (anIntArray773 == null || lowMem) {
						anIntArray776 = null;
						anIntArray773 = new int[len];
						for (int l1 = 0; l1 < len; l1++) {
							anIntArray773[l1] = stream.readUnsignedWord();
						}
					} else {
						stream.currentOffset += len * 2;
					}
				}
			} else if (type == 14) {
				anInt744 = stream.readUnsignedByte();
			} else if (type == 15) {
				anInt761 = stream.readUnsignedByte();
			} else if (type == 17) {
				aBoolean767 = false;
			} else if (type == 18) {
				aBoolean757 = false;
			} else if (type == 19) {
				hasActions = stream.readUnsignedByte() == 1;
			} else if (type == 21) {
				aBoolean762 = true;
			} else if (type == 22) {
			} else if (type == 23) {
				aBoolean764 = true;
			} else if (type == 24) {
				anInt781 = stream.readUnsignedWord();
				if (anInt781 == 65535) {
					anInt781 = -1;
				}
			} else if (type == 27) {
				continue;
			} else if (type == 28) {
				anInt775 = stream.readUnsignedByte();
			} else if (type == 29) {
				aByte737 = stream.readSignedByte();
			} else if (type == 39) {
				aByte742 = stream.readSignedByte();
			} else if (type >= 30 && type < 39) {
				if (actions == null) {
					actions = new String[5];
				}
				actions[type - 30] = stream.readNewString();
				hasActions = true;
				if (actions[type - 30].equalsIgnoreCase("hidden")) {
					actions[type - 30] = null;
				}
			} else if (type == 40) {
				final int i1 = stream.readUnsignedByte();
				modifiedModelColors = new int[i1];
				originalModelColors = new int[i1];
				for (int i2 = 0; i2 < i1; i2++) {
					modifiedModelColors[i2] = stream.readUnsignedWord();
					originalModelColors[i2] = stream.readUnsignedWord();
				}

			} else if (type == 41) {
				final int l = stream.readUnsignedByte();
				stream.skip(l * 4);
			} else if (type == 42) {
				final int l = stream.readUnsignedByte();
				stream.skip(l);
			} else if (type == 60) {
				anInt746 = stream.readUnsignedWord();
			} else if (type == 62) {
			} else if (type == 64) {
				aBoolean779 = false;
			} else if (type == 65) {
				anInt748 = stream.readUnsignedWord();
			} else if (type == 66) {
				stream.readUnsignedWord();
			} else if (type == 67) {
				anInt740 = stream.readUnsignedWord();
			} else if (type == 68) {
				anInt758 = stream.readUnsignedWord();
			} else if (type == 69) {
				anInt768 = stream.readUnsignedByte();
			} else if (type == 70) {
				anInt738 = stream.readSignedWord();
			} else if (type == 71) {
				anInt745 = stream.readSignedWord();
			} else if (type == 72) {
				anInt783 = stream.readSignedWord();
			} else if (type == 73) {
				aBoolean736 = true;
			} else if (type == 74) {
			} else if (type == 75) {
				stream.readUnsignedByte();
			} else if (type == 77 || type == 92) {
				anInt774 = stream.readUnsignedWord();
				if (anInt774 == 65535) {
					anInt774 = -1;
				}
				anInt749 = stream.readUnsignedWord();
				if (anInt749 == 65535) {
					anInt749 = -1;
				}
				int endChild = -1;
				if (type == 92) {
					endChild = stream.readUnsignedWord();
					if (endChild == 65535) {
						endChild = -1;
					}
				}
				final int j1 = stream.readUnsignedByte();
				childrenIDs = new int[j1 + 2];
				for (int j2 = 0; j2 <= j1; j2++) {
					childrenIDs[j2] = stream.readUnsignedWord();
					if (childrenIDs[j2] == 65535) {
						childrenIDs[j2] = -1;
					}
				}
				childrenIDs[j1 + 1] = endChild;
			} else if (type == 78) {
				stream.skip(3);
			} else if (type == 79) {
				stream.skip(5);
				final int l = stream.readUnsignedByte();
				stream.skip(l * 2);
			} else if (type == 81) {
				stream.skip(1);
			} else if (type == 82 || type == 88 || type == 89 || type == 90
					|| type == 91 || type == 94 || type == 95 || type == 96
					|| type == 97) {
				continue;
			} else if (type == 93) {
				stream.skip(2);
			} else if (type == 249) {
				final int l = stream.readUnsignedByte();
				for (int ii = 0; ii < l; ii++) {
					final boolean b = stream.readUnsignedByte() == 1;
					stream.skip(3);
					if (b) {
						stream.readNewString();
					} else {
						stream.skip(4);
					}
				}
			} else {
				System.out.println("Unknown config: " + type);
			}
		} while (true);
		/*
		 * if (flag == -1) { hasActions = anIntArray773 != null &&
		 * (anIntArray776 == null || anIntArray776[0] == 10); if (actions !=
		 * null) hasActions = true; }
		 */
	}

	public void setDefaults() {
		anIntArray773 = null;
		anIntArray776 = null;
		name = null;
		description = null;
		modifiedModelColors = null;
		originalModelColors = null;
		anInt744 = 1;
		anInt761 = 1;
		aBoolean767 = true;
		aBoolean757 = true;
		hasActions = false;
		aBoolean762 = false;
		aBoolean764 = false;
		anInt781 = -1;
		anInt775 = 16;
		aByte737 = 0;
		aByte742 = 0;
		actions = null;
		anInt746 = -1;
		anInt758 = -1;
		aBoolean779 = true;
		anInt748 = 128;
		anInt740 = 128;
		anInt768 = 0;
		anInt738 = 0;
		anInt745 = 0;
		anInt783 = 0;
		aBoolean736 = false;
		anInt774 = -1;
		anInt749 = -1;
		childrenIDs = null;
	}

	public boolean solid() {
		return aBoolean779;
	}

	public int xLength() {
		return anInt744;
	}

	public int yLength() {
		return anInt761;
	}

}
