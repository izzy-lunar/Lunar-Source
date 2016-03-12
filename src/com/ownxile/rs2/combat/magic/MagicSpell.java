package com.ownxile.rs2.combat.magic;

import com.ownxile.rs2.player.Client;

/**
 * @author Robbie
 * @version 1.0
 * @since November 10th, 2013
 * @function A class to represent a magical spell
 */
public abstract class MagicSpell {

	public MagicSpell() {
		Magic.getSpells().add(this);
	}

	private int levelRequired;

	private int[] runeAmount = new int[5];
	private int[] runeId = new int[5];

	private int animation;
	private int gfx;
	private int spellId;

	public abstract boolean getSpecialRequirement(Client player);

	public abstract void handleSpecialAftermath(Client player);

	public int getLevelRequired() {
		return levelRequired;
	}

	public void setLevelRequired(int levelRequired) {
		this.levelRequired = levelRequired;
	}

	public int getAnimation() {
		return animation;
	}

	public void setAnimation(int animation) {
		this.animation = animation;
	}

	public int getGfx() {
		return gfx;
	}

	public void setGfx(int gfx) {
		this.gfx = gfx;
	}

	public int getExperience() {
		return experience;
	}

	public void setExperience(int experience) {
		this.experience = experience;
	}

	public int[] getRuneAmount() {
		return runeAmount;
	}

	public int[] getRuneId() {
		return runeId;
	}

	public int getIndex() {
		return index;
	}

	private int index;

	private int experience;

	public void requireItem(int runeId, int runeAmount) {
		this.runeId[index] = runeId;
		this.runeAmount[index] = runeAmount;
		this.index++;
	}

	/**
	 * @param spellId
	 *            the spellId to set
	 */
	public void setSpellId(int spellId) {
		this.spellId = spellId;
	}

	/**
	 * @return the spellId
	 */
	public int getSpellId() {
		return spellId;
	}
}
