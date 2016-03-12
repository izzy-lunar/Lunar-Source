package com.ownxile.rs2.world.games;

import com.ownxile.rs2.content.object.Ladder;
import com.ownxile.rs2.player.Client;

public class GameObjects {

	public static void handleObject(Client client, int id, int x, int y) {
		if (!client.inCastleWars()) {
			client.sendMessage("This object can only be used inside the Castle Wars arena.");
			return;
		}
		if (client.freezeTimer > 0) {
			client.getFunction().state(
					"You cannot interact with this object while frozen.");
			return;
		}
		switch (id) {
		case 4423:
		case 4424:
			if (x == 2427 || x == 2426) {
				if (client.getY() == 3088) {
					client.getFunction().movePlayer(client.getX(), 3087,
							client.absZ);
				} else if (client.getY() == 3087) {
					client.getFunction().movePlayer(client.getX(), 3088,
							client.absZ);
				}
			}
			break;
		case 4427:
		case 4428:
			if (x == 2373 || x == 2372) {
				if (client.getY() == 3120) {
					client.getFunction().movePlayer(client.getX(), 3119,
							client.absZ);
				} else if (client.getY() == 3119) {
					client.getFunction().movePlayer(client.getX(), 3120,
							client.absZ);
				}
			}
			break;
		case 4467:
			if (y == 3134) {
				if (client.getX() == 2385) {
					client.getFunction().movePlayer(2384, 3134, client.absZ);
				} else if (client.getX() == 2384) {
					client.getFunction().movePlayer(2385, 3134, client.absZ);
				}
			}
			break;
		case 4469:
			if (CastleWars.getTeamNumber(client) == 2) {
				client.sendMessage("You are not allowed in the other teams spawn point.");
				break;
			}
			if (x == 2426) {
				if (client.getY() == 3080) {
					client.getFunction().movePlayer(2426, 3081, client.absZ);
				} else if (client.getY() == 3081) {
					client.getFunction().movePlayer(2426, 3080, client.absZ);
				}
			} else if (x == 2422) {
				if (client.getX() == 2422) {
					client.getFunction().movePlayer(2423, 3076, client.absZ);
				} else if (client.getX() == 2423) {
					client.getFunction().movePlayer(2422, 3076, client.absZ);
				}
			}
			break;
		case 4470:
			if (CastleWars.getTeamNumber(client) == 1) {
				client.sendMessage("You are not allowed in the other teams spawn point.");
				break;
			}
			if (x == 2373 && y == 3126) {
				if (client.getY() == 3126) {
					client.getFunction().movePlayer(2373, 3127, 1);
				} else if (client.getY() == 3127) {
					client.getFunction().movePlayer(2373, 3126, 1);
				}
			} else if (x == 2377 && y == 3131) {
				if (client.getX() == 2376) {
					client.getFunction().movePlayer(2377, 3131, 1);
				} else if (client.getX() == 2377) {
					client.getFunction().movePlayer(2376, 3131, 1);
				}
			}
			break;
		case 4417:
			if (x == 2428 && y == 3081 && client.absZ == 1) {
				client.getFunction().movePlayer(2430, 3080, 2);
			}
			if (x == 2425 && y == 3074 && client.absZ == 2) {
				client.getFunction().movePlayer(2426, 3074, 3);
			}
			if (x == 2419 && y == 3078 && client.absZ == 0) {
				client.getFunction().movePlayer(2420, 3080, 1);
			}
			break;
		case 4415:
			if (x == 2419 && y == 3080 && client.absZ == 1) {
				client.getFunction().movePlayer(2419, 3077, 0);
			}
			if (x == 2430 && y == 3081 && client.absZ == 2) {
				client.getFunction().movePlayer(2427, 3081, 1);
			}
			if (x == 2425 && y == 3074 && client.absZ == 3) {
				client.getFunction().movePlayer(2425, 3077, 2);
			}
			if (x == 2374 && y == 3133 && client.absZ == 3) {
				client.getFunction().movePlayer(2374, 3130, 2);
			}
			if (x == 2369 && y == 3126 && client.absZ == 2) {
				client.getFunction().movePlayer(2372, 3126, 1);
			}
			if (x == 2380 && y == 3127 && client.absZ == 1) {
				client.getFunction().movePlayer(2380, 3130, 0);
			}
			break;
		case 4411:
			client.getTask().jumpTo(x, y);
			break;

		case 4911:
			if (x == 2421 && y == 3073 && client.absZ == 1) {
				client.getFunction().movePlayer(2421, 3074, 0);
			}
			if (x == 2378 && y == 3134 && client.absZ == 1) {
				client.getFunction().movePlayer(2378, 3133, 0);
			}
			break;
		case 1747:
			if (x == 2421 && y == 3073 && client.absZ == 0) {
				Ladder.climb(client, 2421, 3074, client.absZ + 1);
			}
			if (x == 2378 && y == 3134 && client.absZ == 0) {
				Ladder.climb(client, 2378, 3133, client.absZ + 1);
			}
			break;
		case 4912:
			if (x == 2430 && y == 3082 && client.absZ == 0) {
				client.getFunction().movePlayer(client.getX(),
						client.getY() + 6400, 0);
			}
			if (x == 2369 && y == 3125 && client.absZ == 0) {
				client.getFunction().movePlayer(client.getX(),
						client.getY() + 6400, 0);
			}
			break;
		case 1757:
			client.getFunction().movePlayer(x, client.getY() - 6400, 0);
			break;

		case 4418:
			if (x == 2380 && y == 3127 && client.absZ == 0) {
				client.getFunction().movePlayer(2379, 3127, 1);
			}
			if (x == 2369 && y == 3126 && client.absZ == 1) {
				client.getFunction().movePlayer(2369, 3127, 2);
			}
			if (x == 2374 && y == 3131 && client.absZ == 2) {
				client.getFunction().movePlayer(2373, 3133, 3);
			}
			break;
		case 4420:
			if (x == 2382 && y == 3131 && client.absZ == 0) {
				if (client.getX() >= 2383 && client.getX() <= 2385) {
					client.getFunction().movePlayer(2382, 3130, 0);
				} else {
					client.getFunction().movePlayer(2383, 3133, 0);
				}
			}
			break;
		case 4437:
			if (x == 2400 && y == 9512) {
				client.getFunction().movePlayer(2400, 9514, 0);
			} else if (x == 2391 && y == 9501) {
				client.getFunction().movePlayer(2393, 9502, 0);
			} else if (x == 2409 && y == 9503) {
				client.getFunction().movePlayer(2411, 9503, 0);
			} else if (x == 2401 && y == 9494) {
				client.getFunction().movePlayer(2401, 9493, 0);
			}
			break;
		case 6281:
			client.getFunction().movePlayer(2370, 3132, 2);
			break;
		case 4472:
			switch (CastleWars.getTeamNumber(client)) {
			case CastleWars.SARA:
				client.sendMessage("The trapdoor is locked.");
				break;
			case CastleWars.ZAMMY:
				client.getFunction().movePlayer(2370, 3132, 1);
				break;
			}
			break;
		case 4471:
			switch (CastleWars.getTeamNumber(client)) {
			case CastleWars.SARA:
				client.getFunction().movePlayer(2429, 3075, 1);
				break;
			case CastleWars.ZAMMY:
				client.sendMessage("The trapdoor is locked.");
				break;
			}
			break;
		case 6280:
			client.getFunction().movePlayer(2429, 3075, 2);
			break;
		case 4406:
			CastleWars.removePlayerFromCw(client);
			break;
		case 4407:
			CastleWars.removePlayerFromCw(client);
			break;
		case 4458:
			client.startAnimation(881);
			client.getItems().addItem(4049, 1);
			client.sendMessage("You take some bandages.");
			break;
		case 4902: // sara flag
		case 4377:
			if (client.absZ == 3) {
				switch (CastleWars.getTeamNumber(client)) {
				case 1:
					CastleWars.returnFlag(client,
							client.playerEquipment[client.playerWeapon]);
					break;
				case 2:
					CastleWars.captureFlag(client);
					break;
				}
			}
			break;
		case 4903: // zammy flag
		case 4378:
			if (client.absZ == 3) {
				switch (CastleWars.getTeamNumber(client)) {
				case 1:
					CastleWars.captureFlag(client);
					break;
				case 2:
					CastleWars.returnFlag(client,
							client.playerEquipment[client.playerWeapon]);
					break;
				}
			}
			break;
		case 4461: // barricades
			client.sendMessage("You take a barricade.");
			client.getItems().addItem(4053, 1);
			client.startAnimation(881);
			break;
		case 4463: // explosive potion!
			client.sendMessage("You take an explosive potion.");
			client.getItems().addItem(4045, 1);
			client.startAnimation(881);
			break;
		case 4464: // pickaxe table
			client.sendMessage("You take a bronzen pickaxe for mining.");
			client.getItems().addItem(1265, 1);
			client.startAnimation(881);
			break;
		case 4900:
		case 4901:
			CastleWars.pickupFlag(client);
		default:
			break;

		}
	}
}