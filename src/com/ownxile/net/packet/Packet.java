package com.ownxile.net.packet;

import org.apache.mina.common.IoSession;

/**
 * Immutable packet object.
 * 
 * @author Graham
 * 
 */
public final class Packet {

	public static enum Size {
		Fixed, VariableByte, VariableShort
	};

	private static final char[] hex = "0123456789ABCDEF".toCharArray();

	private static String byteToHex(byte b, boolean forceLeadingZero) {
		final StringBuilder out = new StringBuilder();
		final int ub = b & 0xff;
		if (ub / 16 > 0 || forceLeadingZero) {
			out.append(hex[ub / 16]);
		}
		out.append(hex[ub % 16]);
		return out.toString();
	}

	/**
	 * Whether this packet is without the standard packet header
	 */
	private final boolean bare;
	/**
	 * The current index into the payload buffer for reading
	 */
	private int caret = 0;
	/**
	 * The payload
	 */
	private final byte[] pData;
	/**
	 * The ID of the packet
	 */
	private final int pID;
	/**
	 * The length of the payload
	 */
	private final int pLength;

	/**
	 * The associated IO session
	 */
	private final IoSession session;

	private Size size = Size.Fixed;

	/**
	 * Creates a new packet with the specified parameters. The packet is
	 * considered not to be a bare packet.
	 * 
	 * @param session
	 *            The session to associate with the packet
	 * @param pID
	 *            The ID of the packet
	 * @param pData
	 *            The payload the packet
	 */
	public Packet(IoSession session, int pID, byte[] pData) {
		this(session, pID, pData, false);
	}

	/**
	 * Creates a new packet with the specified parameters.
	 * 
	 * @param session
	 *            The session to associate with the packet
	 * @param pID
	 *            The ID of the packet
	 * @param pData
	 *            The payload of the packet
	 * @param bare
	 *            Whether this packet is bare, which means that it does not
	 *            include the standard packet header
	 */
	public Packet(IoSession session, int pID, byte[] pData, boolean bare) {
		this(session, pID, pData, bare, Size.Fixed);
	}

	public Packet(IoSession session, int pID, byte[] pData, boolean bare, Size s) {
		this.session = session;
		this.pID = pID;
		this.pData = pData;
		pLength = pData.length;
		this.bare = bare;
		size = s;
	}

	/**
	 * Returns the entire payload data of this packet.
	 * 
	 * @return The payload <code>byte</code> array
	 */
	public byte[] getData() {
		return pData;
	}

	/**
	 * Returns the packet ID.
	 * 
	 * @return The packet ID
	 */
	public int getId() {
		return pID;
	}

	/**
	 * Returns the length of the payload of this packet.
	 * 
	 * @return The length of the packet's payload
	 */
	public int getLength() {
		return pLength;
	}

	/**
	 * Returns the remaining payload data of this packet.
	 * 
	 * @return The payload <code>byte</code> array
	 */
	public byte[] getRemainingData() {
		final byte[] data = new byte[pLength - caret];
		for (int i = 0; i < data.length; i++) {
			data[i] = pData[i + caret];
		}
		caret += data.length;
		return data;

	}

	/**
	 * Returns the IO session associated with the packet, if any.
	 * 
	 * @return The <code>IoSession</code> object, or <code>null</code> if none.
	 */
	public IoSession getSession() {
		return session;
	}

	public Size getSize() {
		return size;
	}

	/**
	 * Checks if this packet is considered to be a bare packet, which means that
	 * it does not include the standard packet header (ID and length values).
	 * 
	 * @return Whether this packet is a bare packet
	 */
	public boolean isBare() {
		return bare;
	}

	/**
	 * Reads the next <code>byte</code> from the payload.
	 * 
	 * @return A <code>byte</code>
	 */
	public byte readByte() {
		return pData[caret++];
	}

	public byte readByteC() {
		return (byte) -readByte();
	}

	public void readBytes(byte[] buf, int off, int len) {
		for (int i = 0; i < len; i++) {
			buf[off + i] = pData[caret++];
		}
	}

	public byte readByteS() {
		return (byte) (128 - readByte());
	}

	/**
	 * Reads the next <code>int</code> from the payload.
	 * 
	 * @return An <code>int</code>
	 */
	public int readInt() {
		return (pData[caret++] & 0xff) << 24 | (pData[caret++] & 0xff) << 16
				| (pData[caret++] & 0xff) << 8 | pData[caret++] & 0xff;
	}

	public int readLEInt() {
		return pData[caret++] & 0xff | (pData[caret++] & 0xff) << 8
				| (pData[caret++] & 0xff) << 16 | (pData[caret++] & 0xff) << 24;
	}

	public int readLEShort() {
		int i = (pData[caret++] & 0xff) + ((pData[caret++] & 0xff) << 8);
		if (i > 32767) {
			i -= 0x10000;
		}
		return i;
	}

	public int readLEShortA() {
		int i = (pData[caret++] - 128 & 0xff) + ((pData[caret++] & 0xff) << 8);
		if (i > 32767) {
			i -= 0x10000;
		}
		return i;
	}

	/**
	 * Reads the next <code>long</code> from the payload.
	 * 
	 * @return A <code>long</code>
	 */
	public long readLong() {
		return (long) (pData[caret++] & 0xff) << 56
				| (long) (pData[caret++] & 0xff) << 48
				| (long) (pData[caret++] & 0xff) << 40
				| (long) (pData[caret++] & 0xff) << 32
				| (long) (pData[caret++] & 0xff) << 24
				| (long) (pData[caret++] & 0xff) << 16
				| (long) (pData[caret++] & 0xff) << 8 | pData[caret++] & 0xff;
	}

	public String readRS2String() {
		final int start = caret;
		while (pData[caret++] != 0) {
			;
		}
		return new String(pData, start, caret - start - 1);
	}

	/**
	 * Reads the next <code>short</code> from the payload.
	 * 
	 * @return A <code>short</code>
	 */
	public short readShort() {
		return (short) ((short) ((pData[caret++] & 0xff) << 8) | (short) (pData[caret++] & 0xff));
	}

	public int readShortA() {
		caret += 2;
		return ((pData[caret - 2] & 0xFF) << 8)
				+ (pData[caret - 1] - 128 & 0xFF);
	}

	/**
	 * Reads the string which is formed by the unread portion of the payload.
	 * 
	 * @return A <code>String</code>
	 */
	public String readString() {
		return readString(pLength - caret);
	}

	/**
	 * Reads a string of the specified length from the payload.
	 * 
	 * @param length
	 *            The length of the string to be read
	 * @return A <code>String</code>
	 */
	public String readString(int length) {
		final String rv = new String(pData, caret, length);
		caret += length;
		return rv;
	}

	public int readUnsignedWord() {
		caret += 2;
		return ((pData[caret - 2] & 0xff) << 8) + (pData[caret - 1] & 0xff);
	}

	public int readUnsignedWordA() {
		caret += 2;
		return ((pData[caret - 2] & 0xff) << 8)
				+ (pData[caret - 1] - 128 & 0xff);
	}

	public int remaining() {
		return pData.length - caret;
	}

	/**
	 * Skips the specified number of bytes in the payload.
	 * 
	 * @param x
	 *            The number of bytes to be skipped
	 */
	public void skip(int x) {
		caret += x;
	}

	/**
	 * Returns this packet in string form.
	 * 
	 * @return A <code>String</code> representing this packet
	 */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("[id=" + pID + ",len=" + pLength + ",data=0x");
		for (int x = 0; x < pLength; x++) {
			sb.append(byteToHex(pData[x], true));
		}
		sb.append("]");
		return sb.toString();
	}

}
