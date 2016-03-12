package com.ownxile.rs2.content.object;

import java.util.ArrayList;
import java.util.Collections;

import com.ownxile.rs2.player.Client;
import com.ownxile.rs2.player.Player;
import com.ownxile.rs2.player.PlayerHandler;

public class Leaderboard {

	private static ArrayList<PlayerRating> ratings = new ArrayList<PlayerRating>();

	public static void displayLeadingRating(Client p) {
		ratings.clear();
		for (Player player : PlayerHandler.players) {
			if (player == null)
				continue;
			if (player.wildRating > 10) {
				PlayerRating rating = new PlayerRating();
				rating.name = player.playerName;
				rating.setRating(player.wildRating);
				ratings.add(rating);
			}
		}
		Collections.sort(ratings);

		p.getFunction().sendFrame126("Wilderness Rating - Top Active Players",
				903); // Title - -2
		p.getFunction().sendFrame126("  ", 14165); // Bottom Left - Page-2
		p.getFunction().sendFrame126(" ", 14166); // Bottom Right - Page-2

		for (int i = 0; i < 22; i++) {
			if (i < ratings.size()) {
				PlayerRating rating = ratings.get(i);
				p.getFunction().sendFrame126(
						"@dre@" + (i + 1) + "@bla@: " + rating.name + " (@dre@"
								+ rating.getRating() + "@bla@)", 843 + i);
			} else {
				p.getFunction().sendFrame126("", 843 + i);
			}
		}
		p.getFunction().showInterface(837);

	}

}
