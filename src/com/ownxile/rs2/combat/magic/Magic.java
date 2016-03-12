package com.ownxile.rs2.combat.magic;

import java.util.ArrayList;
import java.util.List;
import com.ownxile.rs2.combat.magic.impl.*;

import com.ownxile.rs2.player.Client;

/**
 * @author Robbie
 * @version 1.0
 * @since November 10th, 2013
 * @function dynamically handles magic spell data
 */
public class Magic {

	public static void loadSpells() {
		new BonesToPeaches();
		new BonesOnBananas();
		new Humidify();
	}

	private static List<MagicSpell> magicSpells = new ArrayList<MagicSpell>();

	public static List<MagicSpell> getSpells() {
		return magicSpells;
	}

	public static MagicSpell getMagicSpellForId(int spellId) {
		for (MagicSpell magicSpell : magicSpells) {
			if (magicSpell.getSpellId() == spellId)
				return magicSpell;
		}
		return null;
	}

	public static boolean isSpellButton(Client player, int buttonId) {
		MagicSpell spell = getMagicSpellForId(buttonId);
		if (spell != null) {
			castSpell(player, spell);
			return true;
		}
		return false;
	}

	public static void castSpell(Client player, MagicSpell spell) {
		if (!hasRunes(player, spell.getRuneId(), spell.getRuneAmount())) {
			player.sendMessage("You do not have the runes required to cast this spell.");
		} else if (player.playerLevel[6] < spell.getLevelRequired()) {
			player.sendMessage("You need a magic level of "
					+ spell.getLevelRequired() + " to cast this spell.");
		} else if (!spell.getSpecialRequirement(player)) {
			deleteRunesOnCast(player, spell.getRuneId(), spell.getRuneAmount());
			player.startAnimation(spell.getAnimation());
			player.gfx100(spell.getGfx());
			player.getFunction().addSkillXP(spell.getExperience(), 6);
			spell.handleSpecialAftermath(player);
		}
	}

	private static void deleteRunesOnCast(Client player, int[] runes,
			int[] runeAmounts) {
		for (int i = 0; i < runes.length; i++) {
			player.getItems().deleteItem2(runes[i], runeAmounts[i]);
		}
	}

	private static boolean hasRunes(Client player, int[] runes,
			int[] runeAmounts) {
		for (int i = 0; i < runes.length; i++) {
			if (!player.getItems().playerHasItem(runes[i], runeAmounts[i])) {
				return false;
			}
		}
		return true;
	}

}
