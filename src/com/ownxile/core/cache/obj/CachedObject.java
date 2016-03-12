package com.ownxile.core.cache.obj;

public class CachedObject {

	private final int objectId;
	private int objectOrientation;
	private final int objectType;

	public CachedObject(int objectId, int objectType, int objectOrientation) {
		this.objectId = objectId;
		this.objectType = objectType;
		this.objectOrientation = objectOrientation;
	}

	public void changeOrientation(int o) {
		objectOrientation = o;
	}

	public int getId() {
		return objectId;
	}

	public int getOrientation() {
		return objectOrientation;
	}

	public int getType() {
		return objectType;
	}

}