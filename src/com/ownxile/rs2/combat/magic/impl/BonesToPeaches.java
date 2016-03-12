package com.ownxile.rs2.combat.magic.impl;

import com.ownxile.rs2.combat.magic.MagicSpell;
import com.ownxile.rs2.player.Client;

public class BonesToPeaches extends MagicSpell {

	public BonesToPeaches() {
		setLevelRequired(60);
		setAnimation(722);
		setGfx(141);
		setExperience(25);
		requireItem(561, 2);
		requireItem(557, 4);
		requireItem(555, 4);
		setSpellId(62005);
	}

	@Override
	public boolean getSpecialRequirement(Client player) {
		return false;
	}

	@Override
	public void handleSpecialAftermath(Client player) {
		for (int i = 0; i < 28; i++) {
			if (player.getItems().playerHasItem(526, 1, i)) {
				player.getItems().replaceItem(526, 6883, 1, 1);
			}
		}
		player.sendMessage("You turn the bones into some peaches.");

	}

}
