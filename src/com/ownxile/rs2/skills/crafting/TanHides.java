package com.ownxile.rs2.skills.crafting;

import com.ownxile.rs2.player.Client;

public class TanHides {

	private enum Tan {
		BLACKDHIDE(1747, 2509, 20), BLUEDHIDE(1751, 2505, 20), GREENDHIDE(1753,
				1745, 20), HARDLEATHER(1739, 1743, 3), REDDHIDE(1749, 2507, 20), SOFTLEATHER(
				1739, 1741, 1);

		private int hide, tannedHide, cost;

		private Tan(int hide, int tannedHide, int cost) {
			this.hide = hide;
			this.tannedHide = tannedHide;
			this.cost = cost;
		}

		public int getCost() {
			return cost;
		}

		public int getHide() {
			return hide;
		}

		public int getTannedHide() {
			return tannedHide;
		}
	}

	private Client c;

	public TanHides(Client c) {
		this.c = c;
	}

	private Tan forHide(int id) {
		for (Tan t : Tan.values()) {
			if (t.getTannedHide() == id) {
				return t;
			}
		}
		return null;
	}

	public void handleActionButton(int abutton) {
		switch (abutton) {
		case 57225: // Soft leather
			tanHide(Tan.SOFTLEATHER);
			break;
		case 57226: // Hard leather
			tanHide(Tan.HARDLEATHER);
			break;
		case 57227: // Green d-hide
			tanHide(Tan.GREENDHIDE);
			break;
		case 57228: // Blue d-hide
			tanHide(Tan.BLUEDHIDE);
			break;
		case 57229: // Red d-hide
			tanHide(Tan.REDDHIDE);
			break;
		case 57230: // Black d-hide
			tanHide(Tan.BLACKDHIDE);
			break;
		case 57231: // Unused
			c.sendMessage("This option is currently not featured.");
			break;
		case 57232: // Unused
			c.sendMessage("This option is currently not featured.");
			break;
		}
	}

	private void resetCrafting() {
		c.tanning = false;
	}

	public void setupInterface() {
		for (int i = 14791; i < 14797; i++) {
			c.getFunction().sendFrame126("", i);
		}
		c.getFunction().sendFrame126("@dre@Leather", 14777);
		c.getFunction().sendFrame126("@dre@Hard Leather", 14778);
		c.getFunction().sendFrame126("@dre@Green D'hide", 14779);
		c.getFunction().sendFrame126("@dre@Blue D'hide", 14780);
		c.getFunction().sendFrame126("@dre@Red D'hide", 14781);
		c.getFunction().sendFrame126("@dre@Black D'hide", 14782);
		c.getFunction().sendFrame126("@dre@1 Coin", 14785);
		c.getFunction().sendFrame126("@dre@3 Coins", 14786);
		c.getFunction().sendFrame126("@dre@20 Coins", 14787);
		c.getFunction().sendFrame126("@dre@20 Coins", 14788);
		c.getFunction().sendFrame126("@dre@20 Coins", 14789);
		c.getFunction().sendFrame126("@dre@20 Coins", 14790);
		c.getFunction().showInterface(14670);
		c.getFunction().itemOnInterface(14769, 220, 1739);
		c.getFunction().itemOnInterface(14770, 220, 1739);
		c.getFunction().itemOnInterface(14771, 220, 1753);
		c.getFunction().itemOnInterface(14772, 220, 1751);
		c.getFunction().itemOnInterface(14773, 220, 1749);
		c.getFunction().itemOnInterface(14774, 220, 1747);
		c.tanning = true;
	}

	public void tanHide(Tan hide) {
		Tan t = forHide(hide.getTannedHide());
		if (t != null) {
			c.getFunction().closeAllWindows();
			int amtOfHides, cost;
			if (c.getItems().getItemCount(t.getHide()) > 0) {
				amtOfHides = c.getItems().getItemCount(t.getHide());
				cost = amtOfHides * t.getCost();
				if (c.getItems().playerHasItem(995, cost)) {
					amtOfHides = c.getItems().getItemCount(t.getHide());
				} else {
					c.sendMessage("You do not have enough money with you to tan all the hides at once.");
					return;
				}
				c.getItems().deleteItem(t.getHide(), amtOfHides);
				c.getItems().deleteItem(995, c.getItems().getItemSlot(995),
						cost);
				c.getItems().addItem(t.getTannedHide(), amtOfHides);
				c.sendMessage("You tan " + amtOfHides
						+ (amtOfHides > 1 ? " hides" : " hide") + " for "
						+ cost + " coins.");
			} else {
				c.sendMessage("You don't have any hides you can tan.");
			}
			resetCrafting();
		}
	}

}