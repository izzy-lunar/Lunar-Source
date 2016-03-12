package com.ownxile.rs2.combat.sounds;

public enum WeaponSounds {

	ABBYSAL_WHIP(4151, 1081);

	public static WeaponSounds forId(int id) {
		for (final WeaponSounds w : values()) {
			if (w.getId() == id) {
				return w;
			}
		}
		return null;
	}

	private final int id, sound;

	WeaponSounds(int id, int sound) {
		this.id = id;
		this.sound = sound;
	}

	public int getId() {
		return id;
	}

	public int getSound() {
		return sound;
	}

}
