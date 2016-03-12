package com.ownxile.rs2.skills.farming;

public class PatchSpot {

	public PatchSpot(int x, int y) {
		absX = x;
		absY = y;
	}

	private int absX, absY;
	private int stage = -1;
	private int herbId = -1;
	public int herbs;

	/**
	 * @param stage
	 *            the stage to set
	 */
	public void setStage(int stage) {
		this.stage = stage;
	}

	/**
	 * @return the stage
	 */
	public int getStage() {
		return stage;
	}

	/**
	 * @param absY
	 *            the absY to set
	 */
	public void setAbsY(int absY) {
		this.absY = absY;
	}

	/**
	 * @return the absY
	 */
	public int getAbsY() {
		return absY;
	}

	/**
	 * @param absX
	 *            the absX to set
	 */
	public void setAbsX(int absX) {
		this.absX = absX;
	}

	/**
	 * @return the absX
	 */
	public int getAbsX() {
		return absX;
	}

	public int getHerbId() {
		// TODO Auto-generated method stub
		return herbId;
	}

	public void setHerbId(int i) {
		herbId = i;

	}

}
