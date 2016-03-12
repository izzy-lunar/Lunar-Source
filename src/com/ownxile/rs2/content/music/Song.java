package com.ownxile.rs2.content.music;

import com.ownxile.rs2.Zone;

public final class Song {

	private int id;

	private Zone zone;

	/**
	 * @param id
	 * @param area
	 */
	public Song(int id, Zone zone) {
		this.setId(id);
		this.setZone(zone);
		SongLoader.addSong(this);
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the zone
	 */
	public Zone getZone() {
		return zone;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @param zone
	 *            the zone to set
	 */
	public void setZone(Zone zone) {
		this.zone = zone;
	}

}
