package com.ownxile.rs2.content.item;

import com.ownxile.rs2.player.Client;

public class WeaponPoisoning {

	public static int switchItem(int wep) {
		switch (wep) {
		case 806:
			return 812;
		case 807:
			return 813;
		case 808:
			return 814;
		case 809:
			return 815;
		case 810:
			return 816;
		case 811:
			return 817;
		case 825:
			return 831;
		case 826:
			return 832;
		case 827:
			return 833;
		case 828:
			return 834;
		case 829:
			return 835;
		case 830:
			return 836;
		case 863:
			return 871;
		case 864:
			return 870;
		case 865:
			return 872;
		case 866:
			return 873;
		case 867:
			return 875;
		case 868:
			return 876;
		case 869:
			return 874;
		case 882:
			return 883;
		case 884:
			return 885;
		case 886:
			return 887;
		case 888:
			return 889;
		case 890:
			return 891;
		case 892:
			return 893;
		case 1217:
			return 1233;
		case 1203:
			return 1219;
		case 1205:
			return 1221;
		case 1207:
			return 1223;
		case 1209:
			return 1225;
		case 1211:
			return 1227;
		case 1213:
			return 1229;
		case 1215:
			return 5698;
		case 1237:
			return 1251;
		case 1239:
			return 1253;
		case 1241:
			return 1255;
		case 1243:
			return 1257;
		case 1245:
			return 1259;
		case 1247:
			return 1261;
		case 1249:
			return 1263;
		}
		return 0;
	}

	public static void switchWeapons(Client client, int pot, int wep) {
		if (switchItem(pot) > 0) {
			final int b = wep;
			wep = pot;
			pot = b;
		}
		if (pot != 187) {
			return;
		}
		if (switchItem(wep) == 0) {
			return;
		}
		client.getItems().deleteItem(pot, 1);
		client.getItems().deleteItem(wep, 1);
		client.getItems().addItem(switchItem(wep), 1);
		client.getItems().addItem(229, 1);
		client.sendMessage("You dip your weapon in the poison.");
	}

}