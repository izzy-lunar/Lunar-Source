package com.ownxile.rs2.npcs;

public class NPCAnimate {

	public static int getAttackEmote(int i) {
		if (NPCHandler.npcs[i].attackAnim > 0) {
			return NPCHandler.npcs[i].attackAnim;
		}
		switch (NPCHandler.npcs[i].npcType) {
		case 2208:
			return 7009;
		case 6600:
			return 153;
		case 2043:
			return 5806;
		case 411:
			return 1512;
		case 1734:
			return 3897;
		case 1724:
			return 3920;
		case 1709:
			return 3908;
		case 1704:
			return 3915;
		case 1699:
			return 3908;
		case 1689:
			return 3891;
		case 437:
			return -1;
		case 3209:
			return 4234;
		case 2037://Skeleton
		case 70:
			return 5485;
		case 2042:
		case 2044:
			return 5069;
		case 5054:
			return 6562;
		case 6528:
			return 711;
		case 6611:
		case 6612:
			 return 5487;
		case 6610:
			return 5327;
		case 419:
			return -1;
		case 3998:
			return 3991;
		case 6609: // Callisto
			return 4925;
		case 73:
		case 5399:
		case 751: // zombie
		case 77:
			return 5568;
		case 497:
			return 3618;
		case 484:
			return 1552;
		case 28:
		case 420:
		case 421:
		case 422:
		case 423:
			return 5568;
		case 5779: // giant mole
			return 3312;
		case 438:
		case 439:
		case 440:
		case 441:
		case 442: // Tree spirit
		case 443:
			return 94;
		case 391:
		case 392:
		case 393:
		case 394:
		case 395:// river troll
		case 396:
			return 284;
		case 891: // moss
			return 4658;
		case 85: // ghost
			return 5540;
		case 2834: // bats
			return 4915;
		case 414: // banshee
			return 1523;
		case 4005: // dark beast
			return 2731;
		case 2206:
			return 6376;
		case 2207:
			return 7018;
		case 2216:
		case 2217:
		case 2218:
			return 6154;
		case 3130:
		case 3131:
		case 3132:
			return 6945;
		case 135:
			return 6579;
		case 6575:
			return 6964;
		case 6576:
			return 6945;
		case 3163:
		case 3164:
		case 3165:
			return 6953;
		case 6573:
			return 6976;
		case 6267:
			return 359;
		case 6268:
			return 2930;
		case 6269:
			return 4652;
		case 6270:
			return 4652;
		case 6271:
			return 4320;
		case 6272:
			return 4320;
		case 6273:
			return 4320;
		case 6274:
			return 4320;
		case 1459:
			return 1402;
		case 86:
		case 87:
			return 4933;
		case 871:// Ogre Shaman
		case 5181:// Ogre Shaman
		case 5184:// Ogre Shaman
		case 5187:// Ogre Shaman
		case 5190:// Ogre Shaman
		case 5193:// Ogre Shaman
			return 359;

		case 2892:
		case 2894:
			return 2868;
		case 3116:
			return 2621;
		case 3120:
			return 2625;
		case 3123:
			return 2637;
		case 2746:
			return 2637;
		case 3121:
		case 2167:
			return 2611;
		case 3125:// 360
			return 2647;
		case 5247:
			return 5411;
		case 13: // wizards
			return 711;

		case 655:
			return 5532;

		case 424:
			return 1557;

		case 448:
			return 1590;

		case 415: // abby demon
			return 1537;

		case 11: // nech
			return 1528;

		case 1543:
		case 1611: // garg
			return 1519;

		case 417: // basilisk
			return 1546;

			// case 924: //skele
			// return 260;

		case 247:
		case 259:
		case 268:
		case 264:
		case 2919:
		case 270:
		case 1270:
		case 273:
		case 274:
			return 80;
		case 2840: // earth warrior
			return 390;

		case 803: // monk
			return 422;

		case 52: // baby drag
			return 25;

		case 58: // Shadow Spider
		case 59: // Giant Spider
		case 60: // Giant Spider
		case 61: // Spider
		case 62: // Jungle Spider
		case 63: // Deadly Red Spider
		case 64: // Ice Spider
		case 3021:
			return 143;

		case 105: // Bear
		case 106:// Bear
			return 41;

		case 412:
			// case 2834:
			return 30;

		case 2033: // rat
			return 138;

		case 2031: // bloodworm
			return 2070;

		case 1769:
		case 1770:
		case 1771:
		case 1772:
		case 1773:
		case 101: // goblin
			return 6184;

		case 1767:
		case 397:
		case 1766:
		case 1768:
		case 81: // cow
			return 5849;

		case 21: // hero
			return 451;

		case 41: // chicken
			return 55;

		case 9: // guard
		case 32: // guard
		case 20: // paladin
			return 451;

		case 1338: // dagannoth
		case 1340:
		case 1342:

			return 1341;

		case 19: // white knight
			return 406;

		case 2084:
		case 111: // ice giant
		case 2098:
		case 2463:
			return 4651;
		case 2452:
			return 1312;

		case 2889:
			return 2859;

		case 118:
		case 291:
			return 99;

		case 2006:// Lesser Demon
		case 2026:// Greater Demon
		case 1432:// Black Demon
		case 1472:// jungle demon
			return 64;

		case 1267:
		case 100:
			return 1312;

		case 2841: // ice warrior
		case 178:
			return 451;

		case 1153: // Kalphite Worker
		case 1154: // Kalphite Soldier
		case 1155: // Kalphite guardian
		case 1156: // Kalphite worker
		case 1157: // Kalphite guardian
			return 1184;

		case 123:
		case 122:
			return 164;

		case 1675: // karil
			return 2075;

		case 1672: // ahrim
			return 729;

		case 1673: // dharok
			return 2067;

		case 1674: // guthan
			return 2080;

		case 1676: // torag
			return 0x814;

		case 1677: // verac
			return 2062;

		case 6581: // supreme
			return 2855;

		case 6580: // prime
			return 2854;

		case 6579: // rex
			return 2851;

		case 2054:
			return 3146;


		default:
			return 0x326;
		}
	}

	/**
	 * @param NPC
	 *            ID
	 * @return Death Animation
	 */
	public static int getNPCDeathEmote(int i) {
		if (NPCHandler.npcs[i].deathAnim > 0) {
			return NPCHandler.npcs[i].deathAnim;
		}
		switch (NPCHandler.npcs[i].npcType) {

		case 1625:
			return 1558;
		case 49:
			return 6558;
		case 1192:
			return 6092;
		case 2349:
			return 10924;
		case 4175: // Vet'ion
			return 5491;
		case 4173: // Venenatis
			return 5329;
		case 4172: // Scorpia
			return 6256;
		case 5422:
			return 10059;
		case 3068:
			return 2987;
		case 3847:
			return 3993;
		case 5902:
			return 6322;
		case 5247:
			return 5412;
		case 6203:
			return 6946;

		case 1106:
		case 1115:
			return 287;
		case 3340:
			return 3310;
		case 5421:
			return 5619;
		case 5903:
			return 6347;
		case 89:// unicorn
			return 6377;
		case 113:
		case 114:
		case 115:
		case 116:
			return 361;
		case 104:
		case 655:
		case 5370:
			return 5534;

		case 103:
		case 749:
		case 491: // ghost
			return 5542;
		case 6204:
		case 6206:
		case 6208:
			return 67;

		case 1267:
		case 1265:
		case 2885:
			return 1314;

		case 3062:
		case 708: // imp
		case 709: // imp
			return 172;
		case 95:
		case 96:
		case 97:
		case 142:
		case 141:
			return 6558;
		case 6282:
			return 6182;
		case 6275:
			return 167;
		case 6276:
		case 6277:
			return 4321;
		case 6279:
		case 6280:
		case 6283:
			return 6182;
		case 6211:
			return 172;
		case 6212:
			return 6537;
		case 6219:
		case 6221:
		case 6254:
		case 6255:
		case 6256:
		case 6257:
		case 6258:
		case 6214:
			return 0x900;
		case 6216:
			return 1580;

		case 1351:
		case 1352:
		case 1353:
		case 1354:
		case 1355:
		case 1356:
			return 1342;
		case 6218:
			return 4302;
		case 6268:
			return 2938;
		case 6269:
		case 6270:
			return 4653;
		case 6229:
		case 6230:
		case 6231:
		case 6232:
		case 6233:
		case 6234:
		case 6235:
		case 6236:
		case 6237:
		case 6238:
		case 6239:
		case 6240:
		case 6241:
		case 6242:
		case 6243:
		case 6244:
		case 6245:
		case 6246:
			return 6956;
		case 6210:
			return 6576;
		case 6271:
		case 6272:
		case 6274:
			return 4321;

		case 1153: // Kalphite Worker
		case 1154: // Kalphite Soldier
		case 1155: // Kalphite guardian
		case 1156: // Kalphite worker
		case 1157: // Kalphite guardian
			return 6242;
		case 1158:
			return 6242;
		case 110:// Fire Giant
		case 111:// Ice Giant
		case 112:// Moss Giant
		case 117:// Hill Giant
		case 4291:
		case 4292:
		case 3058:
			return 4653;

		case 1160:
			return 6233;
		case 4527:
			return 4389;
		case 5529:
			return 5784;
		case 6247:
			return 6965;
		case 2554:
			return 6946;
		case 6250:
			return 6377;
		case 6248:
			return 7016;
		case 6252:
			return 7011;

		case 2033: // rat
		case 86:
		case 87:
			return 4935;
			// bandos gwd
		case 6265:
		case 6261:
		case 6263:
			return 6156;
		case 6260:
			return 7062;
		case 2892:
		case 2894:
			return 2865;
		case 1612: // banshee
			return 1524;
		case 6222:
			return 3503;
		case 6225:
		case 6227:
		case 6223:
			return 6956;
		case 2607:
			return 2607;
		case 2627:
			return 2620;
		case 2630:
		case 2738:
			return 2627;
		case 2631:
			return 2630;
		case 2741:
			return 2638;
		case 2746:
			return 2638;
		case 2743:
			return 2646;
		case 2745:
			return 2654;

		case 3777:
		case 3778:
		case 3779:
		case 3780:
			return -1;

		case 3200:
			return 3147;

		case 2035: // spider
			return 146;

		case 2031: // bloodvel
			return 2073;

		case 1770:
		case 1771:
		case 1772:
		case 1773:
		case 2678:
		case 2679:
		case 1774:
		case 1775:
		case 1776:// goblins
		case 100:
		case 101: // goblin
		case 102:
		case 3060:
			return 6182;

		case 81: // cow
			return 5851;
		case 1692:
		case 41: // chicken
			return 57;

		case 1338: // dagannoth
		case 1340:
		case 1342:
			return 1342;

		case 2881:
		case 2882:
		case 2883:
			return 2856;

		case 125: // ice warrior
			return 843;
		case 73:
		case 74:
		case 75:
		case 76:// zombie
		case 5408:
			return 5569;

		case 1626:
		case 1627:
		case 1628:
		case 1629:
		case 1630:
		case 1631:
		case 1632: // turoth!
			return 1597;

		case 1616: // basilisk
			return 1548;

		case 1653: // hand
			return 1590;

		case 82:// demons
		case 83:
		case 84:

		case 4694:
		case 4695:
		case 4696:
		case 4697:
		case 4698:
		case 4699:
		case 4700:
		case 4701:
		case 4702:
		case 4703:
		case 4704:
		case 4705:
		case 934:
		case 3604:
		case 1472:// jungle demon
			return 67;

		case 1605:// abby spec
			return 1508;

		case 51:// baby drags
		case 52:
		case 1589:
		case 3376:
			return 28;

		case 1610:
		case 1611:
			return 1518;

		case 1618:
		case 1619:
			return 1553;

		case 1620:
		case 1621:
			return 1563;

		case 2783:
			return 2732;

		case 1615:
			return 1538;

		case 1624:
			return 1558;

		case 1613:
			return 1530;

		case 1633:
		case 1634:
		case 1635:
		case 1636:
			return 1580;

		case 1648:
		case 1649:
		case 1650:
		case 1651:
		case 1652:
		case 1654:
		case 1655:
		case 1656:
		case 1657:
			return 1590;

		case 90:
		case 91:
		case 5359:
		case 5384:
		case 92:
		case 93: // skeleton
			return 5491;
		case 105:
		case 106:
		case 2552:
			return 4929;

		case 412:
		case 78:
			return 4917;

		case 122:
		case 123:
			return 167;

		case 58:
		case 59:
		case 60:
		case 61:
		case 62:
		case 63:
		case 64:
		case 134:
			return 146;

		case 118:
		case 119:
			return 102;

		case 50:// drags
		case 53:
		case 54:
		case 55:
		case 941:
		case 742:
		case 1590:
		case 1591:
		case 1592:

		case 5362:
		case 5363:
			return 92;

		case 914:
			return 196;
		default:
			return 2304;
		}
	}
}
