package com.ownxile.rs2.combat.magic.impl;

import com.ownxile.rs2.combat.magic.MagicSpell;
import com.ownxile.rs2.player.Client;

public class Humidify extends MagicSpell {

	public Humidify() {
		setLevelRequired(68);
		setAnimation(4412);
		setGfx(1061);
		setExperience(1200);
		requireItem(554, 1);
		requireItem(555, 3);
		requireItem(9075, 1);
		setSpellId(117104);
	}

	@Override
	public boolean getSpecialRequirement(Client player) {
		if (!player.getItems().playerHasItem(229)) {
			player.sendMessage("You need some empty vials to cast this spell.");
			return true;
		}
		return false;
	}

	@Override
	public void handleSpecialAftermath(Client player) {
		for (int i = 0; i < 28; i++) {
			if (player.getItems().playerHasItem(229, 1, i)) {
				player.getItems().replaceItem(229, 227, 1, 1);
			}
		}
		player.sendMessage("You fill the vials with water.");
	}

}
