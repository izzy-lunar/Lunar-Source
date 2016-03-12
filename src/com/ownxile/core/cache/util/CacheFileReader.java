package com.ownxile.core.cache.util;

public class CacheFileReader {

	private final byte[] buffer;
	private int offset;

	public CacheFileReader(byte[] buffer) {
		this.buffer = buffer;
		offset = 0;
	}

	public byte getByte() {
		return buffer[offset++];
	}

	public byte[] getBytes() {
		final int i = offset;
		while (buffer[offset++] != 10) {
			;
		}
		final byte abyte0[] = new byte[offset - i - 1];
		System.arraycopy(buffer, i, abyte0, i - i, offset - 1 - i);
		return abyte0;
	}

	public int getInt() {
		return (getUByte() << 24) + (getUByte() << 16) + (getUByte() << 8)
				+ getUByte();
	}

	public long getLong() {
		return (getUByte() << 56) + (getUByte() << 48) + (getUByte() << 40)
				+ (getUByte() << 32) + (getUByte() << 24) + (getUByte() << 16)
				+ (getUByte() << 8) + getUByte();
	}

	public String getNString() {
		final int i = offset;
		while (buffer[offset++] != 0) {
			;
		}
		return new String(buffer, i, offset - i - 1);
	}

	public int getShort() {
		int val = (getByte() << 8) + getByte();
		if (val > 32767) {
			val -= 0x10000;
		}
		return val;
	}

	public int getUByte() {
		return buffer[offset++] & 0xff;
	}

	public int getUShort() {
		return (getUByte() << 8) + getUByte();
	}

	public int getUSmart() {
		final int i = buffer[offset] & 0xff;
		if (i < 128) {
			return getUByte();
		} else {
			return getUShort() - 32768;
		}
	}

	public int length() {
		return buffer.length;
	}

	public byte[] read(int length) {
		final byte[] b = new byte[length];
		for (int i = 0; i < length; i++) {
			b[i] = buffer[offset++];
		}
		return b;
	}

	public void setOffset(int position) {
		offset = position;
	}

	public void setOffset(long position) {
		offset = (int) position;
	}

	public void skip(int length) {
		offset += length;
	}

}
