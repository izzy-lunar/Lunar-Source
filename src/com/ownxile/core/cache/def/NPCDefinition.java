package com.ownxile.core.cache.def;

public class NPCDefinition {

	private int combatLevel, size;

	private String name, description;

	/**
	 * @param combatLevel
	 * @param size
	 * @param name
	 * @param description
	 */
	public NPCDefinition(int combatLevel, int size, String name,
			String description) {
		this.combatLevel = combatLevel;
		this.size = size;
		this.name = name;
		this.description = description;
	}

	/**
	 * @return the combatLevel
	 */
	public int getCombatLevel() {
		return combatLevel;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the size
	 */
	public int getSize() {
		return size;
	}

	/**
	 * @param combatLevel
	 *            the combatLevel to set
	 */
	public void setCombatLevel(int combatLevel) {
		this.combatLevel = combatLevel;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @param size
	 *            the size to set
	 */
	public void setSize(int size) {
		this.size = size;
	}

}
