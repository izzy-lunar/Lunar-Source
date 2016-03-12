package com.ownxile.rs2.items;

public class ItemRequirement {

	public static int attackReq(int ItemID) {

		if (ItemID == 1203/* Iron_dagger */
				|| ItemID == 1205/* Bronze_dagger */
				|| ItemID == 1219/* Iron_dagger(p) */
				|| ItemID == 1221/* Bronze_dagger(p) */
				|| ItemID == 1237/* Bronze_spear */
				|| ItemID == 1239/* Iron_spear */
				|| ItemID == 1251/* Bronze_spear(p) */
				|| ItemID == 1253/* Iron_spear(p) */
				|| ItemID == 1265/* Bronze_pickaxe */
				|| ItemID == 1267/* Iron_pickaxe */
				|| ItemID == 1277/* Bronze_sword */
				|| ItemID == 1279/* Iron_sword */
				|| ItemID == 1291/* Bronze_longsword */
				|| ItemID == 1293/* Iron_longsword */
				|| ItemID == 1307/* Bronze_2h_sword */
				|| ItemID == 1309/* Iron_2h_sword */
				|| ItemID == 1321/* Bronze_scimitar */
				|| ItemID == 1323/* Iron_scimitar */
				|| ItemID == 1335/* Iron_warhammer */
				|| ItemID == 1337/* Bronze_warhammer */
				|| ItemID == 1349/* Iron_axe */
				|| ItemID == 1351/* Bronze_axe */
				|| ItemID == 1363/* Iron_battleaxe */
				|| ItemID == 1375/* Bronze_battleaxe */
				|| ItemID == 1420/* Iron_mace */
				|| ItemID == 1422/* Bronze_mace */
				|| ItemID == 3095/* Bronze_claws */
				|| ItemID == 3096/* Iron_claws */
				|| ItemID == 3170/* Bronze_spear(kp) */
				|| ItemID == 3171/* Iron_spear(kp) */
				|| ItemID == 3190/* Bronze_halberd */
				|| ItemID == 3192/* Iron_halberd */
				|| ItemID == 5668/* Iron_dagger(+) */
				|| ItemID == 5670/* Bronze_dagger(+) */
				|| ItemID == 5686/* Iron_dagger(s) */
				|| ItemID == 5688/* Bronze_dagger(s) */
				|| ItemID == 5704/* Bronze_spear(+) */
				|| ItemID == 5706/* Iron_spear(+) */
				|| ItemID == 5718/* Bronze_spear(s) */
				|| ItemID == 5720/* Iron_spear(s) */
		) {
			return 1;
		}
		if (ItemID == 1207/* Steel_dagger */
				|| ItemID == 1223/* Steel_dagger(p) */
				|| ItemID == 1241/* Steel_spear */
				|| ItemID == 1255/* Steel_spear(p) */
				|| ItemID == 1269/* Steel_pickaxe */
				|| ItemID == 1281/* Steel_sword */
				|| ItemID == 1295/* Steel_longsword */
				|| ItemID == 1311/* Steel_2h_sword */
				|| ItemID == 1325/* Steel_scimitar */
				|| ItemID == 1339/* Steel_warhammer */
				|| ItemID == 1353/* Steel_axe */
				|| ItemID == 1365/* Steel_battleaxe */
				|| ItemID == 1424/* Steel_mace */
				|| ItemID == 3097/* Steel_claws */
				|| ItemID == 3172/* Steel_spear(kp) */
				|| ItemID == 3194/* Steel_halberd */
				|| ItemID == 5672/* Steel_dagger(+) */
				|| ItemID == 5690/* Steel_dagger(s) */
				|| ItemID == 5708/* Steel_spear(+) */
				|| ItemID == 5722/* Steel_spear(s) */
		) {
			return 5;
		}
		if (ItemID == 1217/* Black_dagger */
				|| ItemID == 1233/* Black_dagger(p) */
				|| ItemID == 1283/* Black_sword */
				|| ItemID == 1297/* Black_longsword */
				|| ItemID == 1313/* Black_2h_sword */
				|| ItemID == 1327/* Black_scimitar */
				|| ItemID == 1341/* Black_warhammer */
				|| ItemID == 1361/* Black_axe */
				|| ItemID == 1367/* Black_battleaxe */
				|| ItemID == 1426/* Black_mace */
				|| ItemID == 3098/* Black_claws */
				|| ItemID == 3196/* Black_halberd */
				|| ItemID == 4580/* Black_spear */
				|| ItemID == 4582/* Black_spear(p) */
				|| ItemID == 4584/* Black_spear(kp) */
				|| ItemID == 5682/* Black_dagger(+) */
				|| ItemID == 5700/* Black_dagger(s) */
				|| ItemID == 5734/* Black_spear(+) */
				|| ItemID == 5736/* Black_spear(s) */
				|| ItemID == 6587/* White_claws */
				|| ItemID == 6589/* White_battleaxe */
				|| ItemID == 6591/* White_dagger */
				|| ItemID == 6593/* White_dagger(p) */
				|| ItemID == 6595/* White_dagger(+) */
				|| ItemID == 6597/* White_dagger(s) */
				|| ItemID == 6599/* White_halberd */
				|| ItemID == 6601/* White_mace */
				|| ItemID == 6605/* White_sword */
				|| ItemID == 6607/* White_longsword */
				|| ItemID == 6609/* White_2h_sword */
				|| ItemID == 6611/* White_scimitar */
				|| ItemID == 6613/* White_warhammer */
		) {
			return 10;
		}
		if (ItemID == 1209/* Mithril_dagger */
				|| ItemID == 1225/* Mithril_dagger(p) */
				|| ItemID == 1243/* Mithril_spear */
				|| ItemID == 1257/* Mithril_spear(p) */
				|| ItemID == 1273/* Mithril_pickaxe */
				|| ItemID == 1285/* Mithril_sword */
				|| ItemID == 1299/* Mithril_longsword */
				|| ItemID == 1315/* Mithril_2h_sword */
				|| ItemID == 1329/* Mithril_scimitar */
				|| ItemID == 1343/* Mithril_warhammer */
				|| ItemID == 1355/* Mithril_axe */
				|| ItemID == 1369/* Mithril_battleaxe */
				|| ItemID == 1428/* Mithril_mace */
				|| ItemID == 3099/* Mithril_claws */
				|| ItemID == 3173/* Mithril_spear(kp) */
				|| ItemID == 3198/* Mithril_halberd */
				|| ItemID == 5674/* Mithril_dagger(+) */
				|| ItemID == 5692/* Mithril_dagger(s) */
				|| ItemID == 5710/* Mithril_spear(+) */
				|| ItemID == 5724/* Mithril_spear(s) */
		) {
			return 20;
		}
		if (ItemID == 1211/* Adamant_dagger */
				|| ItemID == 1227/* Adamant_dagger(p) */
				|| ItemID == 1245/* Adamant_spear */
				|| ItemID == 1259/* Adamant_spear(p) */
				|| ItemID == 1271/* Adamant_pickaxe */
				|| ItemID == 1287/* Adamant_sword */
				|| ItemID == 1301/* Adamant_longsword */
				|| ItemID == 1317/* Adamant_2h_sword */
				|| ItemID == 1331/* Adamant_scimitar */
				|| ItemID == 1345/* Adamnt_warhammer */
				|| ItemID == 1357/* Adamant_axe */
				|| ItemID == 1371/* Adamant_battleaxe */
				|| ItemID == 1430/* Adamant_mace */
				|| ItemID == 3100/* Adamant_claws */
				|| ItemID == 3174/* Adamant_spear(kp) */
				|| ItemID == 3200/* Adamant_halberd */
				|| ItemID == 3757/* Fremennik_blade */
				|| ItemID == 5676/* Adamant_dagger(+) */
				|| ItemID == 5694/* Adamant_dagger(s) */
				|| ItemID == 5712/* Adamant_spear(+) */
				|| ItemID == 5726/* Adamant_spear(s) */
		) {
			return 30;
		}
		if (ItemID == 1213/* Rune_dagger */
				|| ItemID == 1229/* Rune_dagger(p) */
				|| ItemID == 1261/* Rune_spear(p) */
				|| ItemID == 1275/* Rune_pickaxe */
				|| ItemID == 1289/* Rune_sword */
				|| ItemID == 1303/* Rune_longsword */
				|| ItemID == 1319/* Rune_2h_sword */
				|| ItemID == 1333/* Rune_scimitar */
				|| ItemID == 1347/* Rune_warhammer */
				|| ItemID == 1359/* Rune_axe */
				|| ItemID == 1373/* Rune_battleaxe */
				|| ItemID == 1432/* Rune_mace */
				|| ItemID == 3101/* Rune_claws */
				|| ItemID == 3175/* Rune_spear(kp) */
				|| ItemID == 3202/* Rune_halberd */
				|| ItemID == 5678/* Rune_dagger(+) */
				|| ItemID == 5696/* Rune_dagger(s) */
				|| ItemID == 5714/* Rune_spear(+) */
				|| ItemID == 5728/* Rune_spear(s) */
		) {
			return 40;
		}
		if (ItemID == 4675/* Ancient_staff */
				|| ItemID == 4153/* Granite_maul */
				|| ItemID == 4158/* Leaf-bladed_spear */
		) {
			return 50;
		}
		if (ItemID == 1215/* Dragon_dagger */
				|| ItemID == 1231/* Dragon_dagger(p) */
				|| ItemID == 1249/* Dragon_spear */
				|| ItemID == 1263/* Dragon_spear(p) */
				|| ItemID == 1305/* Dragon_longsword */
				|| ItemID == 1377/* Dragon_battleaxe */
				|| ItemID == 1434/* Dragon_mace */
				|| ItemID == 3176/* Dragon_spear(kp) */
				|| ItemID == 3204/* Dragon_halberd */
				|| ItemID == 4587/* Dragon_scimitar */
				|| ItemID == 5680/* Dragon_dagger(+) */
				|| ItemID == 5698/* Dragon_dagger(s) */
				|| ItemID == 5716/* Dragon_spear(+) */
				|| ItemID == 5730/* Dragon_spear(s) */
				|| ItemID == 6523/* Toktz-xil-ak */
				|| ItemID == 6525/* Toktz-xil-ek */
				|| ItemID == 6526/* Toktz-mej-tal */
				|| ItemID == 6527/* Tzhaar-ket-em */
				|| ItemID == 6739/* Dragon_Axe */
				|| ItemID == 7158/* Dragon_2h_Sword */
		) {
			return 60;
		}
		if (ItemID == 4151/* Abyssal_whip */
				|| ItemID == 12006/* Ahrims_staff */
				|| ItemID == 4710/* Ahrims_staff */
				|| ItemID == 4718/* Dharoks_greataxe */
				|| ItemID == 4726/* Guthans_warspear */
				|| ItemID == 4747/* Torags_hammers */
				|| ItemID == 4755/* Veracs_flail */
				|| ItemID == 4862/* Ahrims_staff_100 */
				|| ItemID == 4863/* Ahrims_staff_75 */
				|| ItemID == 4864/* Ahrims_staff_50 */
				|| ItemID == 4865/* Ahrims_staff_25 */
				|| ItemID == 4886/* Dharoks_axe_100 */
				|| ItemID == 4887/* Dharoks_axe_75 */
				|| ItemID == 4888/* Dharoks_axe_50 */
				|| ItemID == 4889/* Dharoks_axe_25 */
				|| ItemID == 4910/* Guthans_spear_100 */
				|| ItemID == 4911/* Guthans_spear_75 */
				|| ItemID == 4912/* Guthans_spear_50 */
				|| ItemID == 4913/* Guthans_spear_25 */
				|| ItemID == 4958/* Torags_hammer_100 */
				|| ItemID == 4959/* Torags_hammer_75 */
				|| ItemID == 4960/* Torags_hammer_50 */
				|| ItemID == 4961/* Torags_hammer_25 */
				|| ItemID == 4982/* Veracs_flail_100 */
				|| ItemID == 4983/* Veracs_flail_75 */
				|| ItemID == 4984/* Veracs_flail_50 */
				|| ItemID == 4985/* Veracs_flail_25 */
		) {
			return 70;
		}
		return 1;
	}

	public static int defenceReq(int ItemID) {

		if (ItemID == 1067/* Iron_platelegs */
				|| ItemID == 1075/* Bronze_platelegs */
				|| ItemID == 1081/* Iron_plateskirt */
				|| ItemID == 1087/* Bronze_plateskirt */
				|| ItemID == 1101/* Iron_chainbody */
				|| ItemID == 1103/* Bronze_chainbody */
				|| ItemID == 1115/* Iron_platebody */
				|| ItemID == 1117/* Bronze_platebody */
				|| ItemID == 1137/* Iron_med_helm */
				|| ItemID == 1139/* Bronze_med_helm */
				|| ItemID == 1153/* Iron_full_helm */
				|| ItemID == 1155/* Bronze_full_helm */
				|| ItemID == 1173/* Bronze_sq_shield */
				|| ItemID == 1175/* Iron_sq_shield */
				|| ItemID == 1189/* Bronze_kiteshield */
				|| ItemID == 1191/* Iron_kiteshield */
				|| ItemID == 4119/* Bronze_boots */
				|| ItemID == 4121/* Iron_boots */
		) {
			return 1;
		}
		if (ItemID == 1069/* Steel_platelegs */
				|| ItemID == 1083/* Steel_plateskirt */
				|| ItemID == 1105/* Steel_chainbody */
				|| ItemID == 1119/* Steel_platebody */
				|| ItemID == 1141/* Steel_med_helm */
				|| ItemID == 1157/* Steel_full_helm */
				|| ItemID == 1177/* Steel_sq_shield */
				|| ItemID == 1193/* Steel_kiteshield */
				|| ItemID == 4068/* Decorative_sword */
				|| ItemID == 4069/* Decorative_armour */
				|| ItemID == 4070/* Decorative_armour */
				|| ItemID == 4071/* Decorative_helm */
				|| ItemID == 4072/* Decorative_shield */
				|| ItemID == 4123/* Steel_boots */
				|| ItemID == 4551 /* Spiny_helmet */
		) {
			return 5;
		}
		if (ItemID == 1077/* Black_platelegs */
				|| ItemID == 1089/* Black_plateskirt */
				|| ItemID == 1107/* Black_chainbody */
				|| ItemID == 1125/* Black_platebody */
				|| ItemID == 1131/* Hardleather_body */
				|| ItemID == 1151/* Black_med_helm */
				|| ItemID == 1165/* Black_full_helm */
				|| ItemID == 1179/* Black_sq_shield */
				|| ItemID == 1195/* Black_kiteshield */
				|| ItemID == 2583/* Black_platebody_(t) */
				|| ItemID == 2585/* Black_platelegs_(t) */
				|| ItemID == 2587/* Black_full_helm_(t) */
				|| ItemID == 2589/* Black_kiteshield_(t) */
				|| ItemID == 2591/* Black_platebody_(g) */
				|| ItemID == 2593/* Black_platelegs_(g) */
				|| ItemID == 2595/* Black_full_helm_(g) */
				|| ItemID == 2597/* Black_kiteshield_(g) */
				|| ItemID == 3472/* Black_plateskirt_(t) */
				|| ItemID == 3473/* Black_plateskirt_(g) */
				|| ItemID == 4125/* Black_boots */
				|| ItemID == 4503/* Decorative_sword */
				|| ItemID == 4504/* Decorative_armour */
				|| ItemID == 4505/* Decorative_armour */
				|| ItemID == 4506/* Decorative_helm */
				|| ItemID == 4507/* Decorative_shield */
				|| ItemID == 6615/* White_chainbody */
				|| ItemID == 6617/* White_platebody */
				|| ItemID == 6619/* White_boots */
				|| ItemID == 6621/* White_med_helm */
				|| ItemID == 6623/* White_full_helm */
				|| ItemID == 6625/* White_platelegs */
				|| ItemID == 6627/* White_plateskirt */
				|| ItemID == 6629/* White_gloves */
				|| ItemID == 6631/* White_sq_shield */
				|| ItemID == 6633/* White_kiteshield */
				|| ItemID == 7332/* Black_kiteshield(h) */
				|| ItemID == 7338/* Black_kiteshield(h) */
				|| ItemID == 7344/* Black_kiteshield(h) */
				|| ItemID == 7350/* Black_kiteshield(h) */
				|| ItemID == 7356/* Black_kiteshield(h) */
		) {
			return 10;
		}
		if (ItemID == 4508/* Decorative_sword */
				|| ItemID == 4509/* Decorative_armour */
				|| ItemID == 4510/* Decorative_armour */
				|| ItemID == 4511/* Decorative_helm */
				|| ItemID == 4512/* Decorative_shield */
		) {
			return 15;
		}
		if (ItemID == 1071/* Mithril_platelegs */
				|| ItemID == 1085/* Mithril_plateskirt */
				|| ItemID == 1109/* Mithril_chainbody */
				|| ItemID == 1121/* Mithril_platebody */
				|| ItemID == 1133/* Studded_body */
				|| ItemID == 1143/* Mithril_med_helm */
				|| ItemID == 1159/* Mithril_full_helm */
				|| ItemID == 1181/* Mithril_sq_shield */
				|| ItemID == 1197/* Mithril_kiteshield */
				|| ItemID == 4089/* Mystic_hat */
				|| ItemID == 4099/* Mystic_hat */
				|| ItemID == 4109/* Mystic_hat */
				|| ItemID == 4091/* Mystic_robe_top */
				|| ItemID == 4101/* Mystic_robe_top */
				|| ItemID == 4111/* Mystic_robe_top */
				|| ItemID == 4093/* Mystic_robe_bottom */
				|| ItemID == 4103/* Mystic_robe_bottom */
				|| ItemID == 4113/* Mystic_robe_bottom */
				|| ItemID == 4095/* Mystic_gloves */
				|| ItemID == 4105/* Mystic_gloves */
				|| ItemID == 4115/* Mystic_gloves */
				|| ItemID == 4097/* Mystic_boots */
				|| ItemID == 4107/* Mystic_boots */
				|| ItemID == 4117/* Mystic_boots */
				|| ItemID == 4127/* Mithril_boots */
				|| ItemID == 4156/* Mirror_shield */
				|| ItemID == 5574/* Initiate_helm */
				|| ItemID == 5575/* Initiate_platemail */
				|| ItemID == 5576/* Initiate_platelegs */
				|| ItemID == 7362/* Studded_body_(g) */
				|| ItemID == 7364/* Studded_body_(t) */
				|| ItemID == 7398/* Enchanted_robe */
				|| ItemID == 7399/* Enchanted_top */
				|| ItemID == 7400/* Enchanted_hat */
		) {
			return 20;
		}
		if (ItemID == 6916/* Infinity_Top */
				|| ItemID == 6918/* Infinity_Hat */
				|| ItemID == 6920/* Infinity_Boots */
				|| ItemID == 6922/* Infinity_Gloves */
				|| ItemID == 6924/* Infinity_Bottom */
		) {
			return 25;
		}
		if (ItemID == 1073/* Adamant_platelegs */
				|| ItemID == 1091/* Adamant_plateskirts */
				|| ItemID == 1111/* Adamant_chainbody */
				|| ItemID == 1123/* Adamant_platebody */
				|| ItemID == 1145/* Adamant_med_helm */
				|| ItemID == 1161/* Adamant_full_helm */
				|| ItemID == 1183/* Adamant_sq_shield */
				|| ItemID == 1199/* Adamant_kiteshield */
				|| ItemID == 2599/* Adam_platebody_(t) */
				|| ItemID == 2601/* Adam_platelegs_(t) */
				|| ItemID == 2603/* Adam_kiteshield_(t) */
				|| ItemID == 2605/* Adam_full_helm_(t) */
				|| ItemID == 2607/* Adam_platebody_(g) */
				|| ItemID == 2609/* Adam_platelegs_(g) */
				|| ItemID == 2611/* Adam_kiteshield_(g) */
				|| ItemID == 2613/* Adam_full_helm_(g) */
				|| ItemID == 3474/* Adam_plateskirt_(t) */
				|| ItemID == 3475/* Adam_plateskirt_(g) */
				|| ItemID == 4129/* Adamant_boots */
				|| ItemID == 6322/* Snakeskin_body */
				|| ItemID == 6324/* Snakeskin_chaps */
				|| ItemID == 6326/* Snakeskin_bandana */
				|| ItemID == 6328/* Snakeskin_boots */
				|| ItemID == 6330/* Snakeskin_v'brace */
				|| ItemID == 7334/* Adam_kiteshield(h) */
				|| ItemID == 7340/* Adam_kiteshield(h) */
				|| ItemID == 7346/* Adam_kiteshield(h) */
				|| ItemID == 7352/* Adam_kiteshield(h) */
				|| ItemID == 7358/* Adam_kiteshield(h) */
		) {
			return 30;
		}
		if (ItemID == 1079/* Rune_platelegs */
				|| ItemID == 1093/* Rune_plateskirt */
				|| ItemID == 1113/* Rune_chainbody */
				|| ItemID == 1127/* Rune_platebody */
				|| ItemID == 1135/* Dragonhide_body */
				|| ItemID == 1147/* Rune_med_helm */
				|| ItemID == 1163/* Rune_full_helm */
				|| ItemID == 1185/* Rune_sq_shield */
				|| ItemID == 1201/* Rune_kiteshield */
				|| ItemID == 2499/* Dragonhide_body */
				|| ItemID == 2501/* Dragonhide_body */
				|| ItemID == 2503/* Dragonhide_body */
				|| ItemID == 2615/* Rune_platebody_(g) */
				|| ItemID == 2617/* Rune_platelegs_(g) */
				|| ItemID == 2619/* Rune_full_helm_(g) */
				|| ItemID == 2621/* Rune_kiteshield_(g) */
				|| ItemID == 2623/* Rune_platebody_(t) */
				|| ItemID == 2625/* Rune_platelegs_(t) */
				|| ItemID == 2627/* Rune_full_helm_(t) */
				|| ItemID == 2629/* Rune_kiteshield_(t) */
				|| ItemID == 2653/* Zamorak_platebody */
				|| ItemID == 2655/* Zamorak_platelegs */
				|| ItemID == 2657/* Zamorak_full_helm */
				|| ItemID == 2659/* Zamorak_kiteshield */
				|| ItemID == 2661/* Saradomin_plate */
				|| ItemID == 2663/* Saradomin_legs */
				|| ItemID == 2665/* Saradomin_full */
				|| ItemID == 2667/* Saradomin_kite */
				|| ItemID == 2669/* Guthix_platebody */
				|| ItemID == 2671/* Guthix_platelegs */
				|| ItemID == 2673/* Guthix_full_helm */
				|| ItemID == 2675/* Guthix_kiteshield */
				|| ItemID == 3385/* Splitbark_helm */
				|| ItemID == 3387/* Splitbark_body */
				|| ItemID == 3389/* Splitbark_legs */
				|| ItemID == 3391/* Splitbark_gauntlets */
				|| ItemID == 3393/* Splitbark_greaves */
				|| ItemID == 3476/* Rune_plateskirt_(g) */
				|| ItemID == 3477/* Rune_plateskirt_(t) */
				|| ItemID == 3478/* Zamorak_plateskirt */
				|| ItemID == 3479/* Saradomin_skirt */
				|| ItemID == 3480/* Guthix_plateskirt */
				|| ItemID == 3481/* Gilded_platebody */
				|| ItemID == 3483/* Gilded_platelegs */
				|| ItemID == 3485/* Gilded_plateskirt */
				|| ItemID == 3486/* Gilded_full_helm */
				|| ItemID == 3488/* Gilded_kiteshield */
				|| ItemID == 4131/* Rune_boots */
				|| ItemID == 6128/* Rock-shell_helm */
				|| ItemID == 6129/* Rock-shell_plate */
				|| ItemID == 6130/* Rock-shell_legs */
				|| ItemID == 6131/* Spined_helm */
				|| ItemID == 6133/* Spined_body */
				|| ItemID == 6135/* Spined_chaps */
				|| ItemID == 6137/* Skeletal_helm */
				|| ItemID == 6139/* Skeletal_top */
				|| ItemID == 6141/* Skeletal_bottoms */
				|| ItemID == 6143/* Spined_boots */
				|| ItemID == 6145/* Rock-shell_boots */
				|| ItemID == 6147/* Skeletal_boots */
				|| ItemID == 6149/* Spined_gloves */
				|| ItemID == 6151/* Rock-shell_gloves */
				|| ItemID == 6153/* Skeletal_gloves */
				|| ItemID == 7336/* Rune_kiteshield(h) */
				|| ItemID == 7342/* Rune_kiteshield(h) */
				|| ItemID == 7348/* Rune_kiteshield(h) */
				|| ItemID == 7354/* Rune_kiteshield(h) */
				|| ItemID == 7360/* Rune_kiteshield(h) */
				|| ItemID == 7370/* D-hide_body(g) */
				|| ItemID == 7372/* D-hide_body_(t) */
				|| ItemID == 7374/* D-hide_body_(g) */
				|| ItemID == 7376/* D-hide_body_(t) */
		) {
			return 40;
		}
		if (ItemID == 3749/* Archer_helm */
				|| ItemID == 3751/* Berserker_helm */
				|| ItemID == 3753/* Warrior_helm */
				|| ItemID == 3755/* Farseer_helm */
		) {
			return 45;
		}
		if (ItemID == 3122/* Granite_shield */
				|| ItemID == 6809/* Granite_legs */
		) {
			return 50;
		}
		if (ItemID == 1149/* Dragon_med_helm */
				|| ItemID == 1187/* Dragon_sq_shield */
				|| ItemID == 3140/* Dragon_chainbody */
				|| ItemID == 4087/* Dragon_platelegs */
				|| ItemID == 4585/* Dragon_plateskirt */
				|| ItemID == 6524/* Toktz-ket-xil */
		) {
			return 60;
		}
		if (ItemID == 4224/* New_crystal_shield */
				|| ItemID == 4225/* Crystal_shield_full */
				|| ItemID == 4226/* Crystal_shield_9/10 */
				|| ItemID == 4227/* Crystal_shield_8/10 */
				|| ItemID == 4228/* Crystal_shield_7/10 */
				|| ItemID == 4229/* Crystal_shield_6/10 */
				|| ItemID == 4230/* Crystal_shield_5/10 */
				|| ItemID == 4231/* Crystal_shield_4/10 */
				|| ItemID == 4232/* Crystal_shield_3/10 */
				|| ItemID == 4233/* Crystal_shield_2/10 */
				|| ItemID == 4234/* Crystal_shield_1/10 */
				|| ItemID == 4708/* Ahrims_hood */
				|| ItemID == 4712/* Ahrims_robetop */
				|| ItemID == 4714/* Ahrims_robeskirt */
				|| ItemID == 4716/* Dharoks_helm */
				|| ItemID == 4720/* Dharoks_platebody */
				|| ItemID == 4722/* Dharoks_platelegs */
				|| ItemID == 4724/* Guthans_helm */
				|| ItemID == 4728/* Guthans_platebody */
				|| ItemID == 4730/* Guthans_chainskirt */
				|| ItemID == 4732/* Karils_coif */
				|| ItemID == 4736/* Karils_leathertop */
				|| ItemID == 4738/* Karils_leatherskirt */
				|| ItemID == 4745/* Torags_helm */
				|| ItemID == 4749/* Torags_platebody */
				|| ItemID == 4751/* Torags_platelegs */
				|| ItemID == 4753/* Veracs_helm */
				|| ItemID == 4757/* Veracs_brassard */
				|| ItemID == 4759/* Veracs_plateskirt */
				|| ItemID == 4856/* Ahrims_hood_100 */
				|| ItemID == 4857/* Ahrims_hood_75 */
				|| ItemID == 4858/* Ahrims_hood_50 */
				|| ItemID == 4859/* Ahrims_hood_25 */
				|| ItemID == 4868/* Ahrims_top_100 */
				|| ItemID == 4869/* Ahrims_top_75 */
				|| ItemID == 4870/* Ahrims_top_50 */
				|| ItemID == 4871/* Ahrims_top_25 */
				|| ItemID == 4874/* Ahrims_skirt_100 */
				|| ItemID == 4875/* Ahrims_skirt_75 */
				|| ItemID == 4876/* Ahrims_skirt_50 */
				|| ItemID == 4877/* Ahrims_skirt_25 */
				|| ItemID == 4880/* Dharoks_helm_100 */
				|| ItemID == 4881/* Dharoks_helm_75 */
				|| ItemID == 4882/* Dharoks_helm_50 */
				|| ItemID == 4883/* Dharoks_helm_25 */
				|| ItemID == 4892/* Dharoks_body_100 */
				|| ItemID == 4893/* Dharoks_body_75 */
				|| ItemID == 4894/* Dharoks_body_50 */
				|| ItemID == 4895/* Dharoks_body_25 */
				|| ItemID == 4898/* Dharoks_legs_100 */
				|| ItemID == 4899/* Dharoks_legs_75 */
				|| ItemID == 4900/* Dharoks_legs_50 */
				|| ItemID == 4901/* Dharoks_legs_25 */
				|| ItemID == 4904/* Guthans_helm_100 */
				|| ItemID == 4905/* Guthans_helm_75 */
				|| ItemID == 4906/* Guthans_helm_50 */
				|| ItemID == 4907/* Guthans_helm_25 */
				|| ItemID == 4916/* Guthans_body_100 */
				|| ItemID == 4917/* Guthans_body_75 */
				|| ItemID == 4918/* Guthans_body_50 */
				|| ItemID == 4919/* Guthans_body_25 */
				|| ItemID == 4922/* Guthans_skirt_100 */
				|| ItemID == 4923/* Guthans_skirt_75 */
				|| ItemID == 4924/* Guthans_skirt_50 */
				|| ItemID == 4925/* Guthans_skirt_25 */
				|| ItemID == 4928/* Karils_coif_100 */
				|| ItemID == 4929/* Karils_coif_75 */
				|| ItemID == 4930/* Karils_coif_50 */
				|| ItemID == 4931/* Karils_coif_25 */
				|| ItemID == 4940/* Karils_top_100 */
				|| ItemID == 4941/* Karils_top_75 */
				|| ItemID == 4942/* Karils_top_50 */
				|| ItemID == 4943/* Karils_top_25 */
				|| ItemID == 4946/* Karils_skirt_100 */
				|| ItemID == 4947/* Karils_skirt_75 */
				|| ItemID == 4948/* Karils_skirt_50 */
				|| ItemID == 4949/* Karils_skirt_25 */
				|| ItemID == 4952/* Torags_helm_100 */
				|| ItemID == 4953/* Torags_helm_75 */
				|| ItemID == 4954/* Torags_helm_50 */
				|| ItemID == 4955/* Torags_helm_25 */
				|| ItemID == 4964/* Torags_body_100 */
				|| ItemID == 4965/* Torags_body_75 */
				|| ItemID == 4966/* Torags_body_50 */
				|| ItemID == 4967/* Torags_body_25 */
				|| ItemID == 4970/* Torags_legs_100 */
				|| ItemID == 4971/* Torags_legs_75 */
				|| ItemID == 4972/* Torags_legs_50 */
				|| ItemID == 4973/* Torags_legs_25 */
				|| ItemID == 4976/* Veracs_helm_100 */
				|| ItemID == 4977/* Veracs_helm_75 */
				|| ItemID == 4978/* Veracs_helm_50 */
				|| ItemID == 4979/* Veracs_helm_25 */
				|| ItemID == 4988/* Veracs_top_100 */
				|| ItemID == 4989/* Veracs_top_75 */
				|| ItemID == 4990/* Veracs_top_50 */
				|| ItemID == 4991/* Veracs_top_25 */
				|| ItemID == 4994/* Veracs_skirt_100 */
				|| ItemID == 4995/* Veracs_skirt_75 */
				|| ItemID == 4996/* Veracs_skirt_50 */
				|| ItemID == 4997/* Veracs_skirt_25 */
		) {
			return 70;
		}
		return 1;
	}

	public static int GetCLAgility(int ItemID) {
		/* Level 50 Item's */
		if (ItemID == 4212/* New_crystal_bow */
				|| ItemID == 4214/* Crystal_bow_full */
				|| ItemID == 4215/* Crystal_bow_9/10 */
				|| ItemID == 4216/* Crystal_bow_8/10 */
				|| ItemID == 4217/* Crystal_bow_7/10 */
				|| ItemID == 4218/* Crystal_bow_6/10 */
				|| ItemID == 4219/* Crystal_bow_5/10 */
				|| ItemID == 4220/* Crystal_bow_4/10 */
				|| ItemID == 4221/* Crystal_bow_3/10 */
				|| ItemID == 4222/* Crystal_bow_2/10 */
				|| ItemID == 4223/* Crystal_bow_1/10 */
				|| ItemID == 4224/* New_crystal_shield */
				|| ItemID == 4225/* Crystal_shield_full */
				|| ItemID == 4226/* Crystal_shield_9/10 */
				|| ItemID == 4227/* Crystal_shield_8/10 */
				|| ItemID == 4228/* Crystal_shield_7/10 */
				|| ItemID == 4229/* Crystal_shield_6/10 */
				|| ItemID == 4230/* Crystal_shield_5/10 */
				|| ItemID == 4231/* Crystal_shield_4/10 */
				|| ItemID == 4232/* Crystal_shield_3/10 */
				|| ItemID == 4233/* Crystal_shield_2/10 */
				|| ItemID == 4234/* Crystal_shield_1/10 */
		) {
			return 50;
		}
		return 1;
	}

	public static int magicReq(int ItemID) {
		if (ItemID == 2579/* Wizard_boots */
		) {
			return 20;
		}
		if (ItemID == 6215/* Broodoo_shield_(10) */
				|| ItemID == 6217/* Broodoo_shield_(9) */
				|| ItemID == 6219/* Broodoo_shield_(8) */
				|| ItemID == 6221/* Broodoo_shield_(7) */
				|| ItemID == 6223/* Broodoo_shield_(6) */
				|| ItemID == 6225/* Broodoo_shield_(5) */
				|| ItemID == 6227/* Broodoo_shield_(4) */
				|| ItemID == 6229/* Broodoo_shield_(3) */
				|| ItemID == 6231/* Broodoo_shield_(2) */
				|| ItemID == 6233/* Broodoo_shield_(1) */
				|| ItemID == 6235/* Broodoo_shield */
				|| ItemID == 6237/* Broodoo_shield_(10) */
				|| ItemID == 6239/* Broodoo_shield_(9) */
				|| ItemID == 6241/* Broodoo_shield_(8) */
				|| ItemID == 6243/* Broodoo_shield_(7) */
				|| ItemID == 6245/* Broodoo_shield_(6) */
				|| ItemID == 6247/* Broodoo_shield_(5) */
				|| ItemID == 6249/* Broodoo_shield_(4) */
				|| ItemID == 6251/* Broodoo_shield_(3) */
				|| ItemID == 6253/* Broodoo_shield_(2) */
				|| ItemID == 6255/* Broodoo_shield_(1) */
				|| ItemID == 6257/* Broodoo_shield */
				|| ItemID == 6259/* Broodoo_shield_(10) */
				|| ItemID == 6261/* Broodoo_shield_(9) */
				|| ItemID == 6263/* Broodoo_shield_(8) */
				|| ItemID == 6265/* Broodoo_shield_(7) */
				|| ItemID == 6267/* Broodoo_shield_(6) */
				|| ItemID == 6269/* Broodoo_shield_(5) */
				|| ItemID == 6271/* Broodoo_shield_(4) */
				|| ItemID == 6273/* Broodoo_shield_(3) */
				|| ItemID == 6275/* Broodoo_shield_(2) */
				|| ItemID == 6277/* Broodoo_shield_(1) */
				|| ItemID == 6279/* Broodoo_shield */
		) {
			return 25;
		}
		if (ItemID == 4089/* Mystic_hat */
				|| ItemID == 4099/* Mystic_hat */
				|| ItemID == 4109/* Mystic_hat */
				|| ItemID == 4091/* Mystic_robe_top */
				|| ItemID == 4101/* Mystic_robe_top */
				|| ItemID == 4111/* Mystic_robe_top */
				|| ItemID == 4093/* Mystic_robe_bottom */
				|| ItemID == 4103/* Mystic_robe_bottom */
				|| ItemID == 4113/* Mystic_robe_bottom */
				|| ItemID == 4095/* Mystic_gloves */
				|| ItemID == 4105/* Mystic_gloves */
				|| ItemID == 4115/* Mystic_gloves */
				|| ItemID == 4097/* Mystic_boots */
				|| ItemID == 4107/* Mystic_boots */
				|| ItemID == 4117/* Mystic_boots */
				|| ItemID == 6137/* Skeletal_helm */
				|| ItemID == 6139/* Skeletal_top */
				|| ItemID == 6141/* Skeletal_bottoms */
				|| ItemID == 6147/* Skeletal_boots */
				|| ItemID == 6153/* Skeletal_gloves */
				|| ItemID == 7398/* Enchanted_robe */
				|| ItemID == 7399/* Enchanted_top */
				|| ItemID == 7400/* Enchanted_hat */
		) {
			return 40;
		}
		if (ItemID == 3385/* Splitbark_helm */
				|| ItemID == 3387/* Splitbark_body */
				|| ItemID == 3389/* Splitbark_legs */
				|| ItemID == 3391/* Splitbark_gauntlets */
				|| ItemID == 3393/* Splitbark_greaves */
				|| ItemID == 6908/* Beginner_wand */
		) {
			return 45;
		}
		if (ItemID == 4170/* Slayer's_staff */
				|| ItemID == 4675/* Ancient_staff */
				|| ItemID == 6910/* Apprentice_wand */
				|| ItemID == 6916/* Infinity_Top */
				|| ItemID == 6918/* Infinity_Hat */
				|| ItemID == 6920/* Infinity_Boots */
				|| ItemID == 6922/* Infinity_Gloves */
				|| ItemID == 6924/* Infinity_Bottom */
		) {
			return 50;
		}
		if (ItemID == 6912/* Teacher_wand */
		) {
			return 55;
		}
		if (ItemID == 2412/* Saradomin_cape */
				|| ItemID == 2413/* Guthix_cape */
				|| ItemID == 2414/* Zamorak_cape */
				|| ItemID == 2415/* Saradomin_staff */
				|| ItemID == 2416/* Guthix_staff */
				|| ItemID == 2417/* Zamorak_staff */
				|| ItemID == 6526/* Toktz-mej-tal */
				|| ItemID == 6914/* Master_wand */
		) {
			return 60;
		}
		if (ItemID == 4708/* Ahrims_hood */
				|| ItemID == 4710/* Ahrims_staff */
				|| ItemID == 4712/* Ahrims_robetop */
				|| ItemID == 4714/* Ahrims_robeskirt */
				|| ItemID == 4856/* Ahrims_hood_100 */
				|| ItemID == 4857/* Ahrims_hood_75 */
				|| ItemID == 4858/* Ahrims_hood_50 */
				|| ItemID == 4859/* Ahrims_hood_25 */
				|| ItemID == 4862/* Ahrims_staff_100 */
				|| ItemID == 4863/* Ahrims_staff_75 */
				|| ItemID == 4864/* Ahrims_staff_50 */
				|| ItemID == 4865/* Ahrims_staff_25 */
				|| ItemID == 4868/* Ahrims_top_100 */
				|| ItemID == 4869/* Ahrims_top_75 */
				|| ItemID == 4870/* Ahrims_top_50 */
				|| ItemID == 4871/* Ahrims_top_25 */
				|| ItemID == 4874/* Ahrims_skirt_100 */
				|| ItemID == 4875/* Ahrims_skirt_75 */
				|| ItemID == 4876/* Ahrims_skirt_50 */
				|| ItemID == 4877/* Ahrims_skirt_25 */
		) {
			return 70;
		}
		return 1;
	}

	public static int rangedReq(int ItemID) {

		if (ItemID == 800/* Bronze_thrownaxe */
				|| ItemID == 801/* Iron_thrownaxe */
				|| ItemID == 806/* Bronze_dart */
				|| ItemID == 807/* Iron_dart */
				|| ItemID == 812/* Bronze_dart(p) */
				|| ItemID == 813/* Iron_dart(p) */
				|| ItemID == 825/* Bronze_javelin */
				|| ItemID == 826/* Iron_javelin */
				|| ItemID == 831/* Bronze_javelin(p) */
				|| ItemID == 832/* Iron_javelin(p) */
				|| ItemID == 839/* Longbow */
				|| ItemID == 841/* Shortbow */
				|| ItemID == 863/* Iron_knife */
				|| ItemID == 864/* Bronze_knife */
				|| ItemID == 870/* Bronze_knife(p) */
				|| ItemID == 871/* Iron_knife(p) */
				|| ItemID == 882/* Bronze_arrow */
				|| ItemID == 883/* Bronze_arrow(p) */
				|| ItemID == 884/* Iron_arrow */
				|| ItemID == 885/* Iron_arrow(p) */
				|| ItemID == 942/* Bronze_fire_arrows */
				|| ItemID == 2532/* Iron_fire_arrows */
				|| ItemID == 2533/* Iron_fire_arrows */
				|| ItemID == 4773/* Bronze_brutal */
				|| ItemID == 4778/* Iron_brutal */
				|| ItemID == 5616/* Bronze_arrow(+) */
				|| ItemID == 5617/* Iron_arrow(+) */
				|| ItemID == 5622/* Bronze_arrow(s) */
				|| ItemID == 5623/* Iron_arrow(s) */
				|| ItemID == 5628/* Bronze_dart(+) */
				|| ItemID == 5629/* Iron_dart(+) */
				|| ItemID == 5635/* Bronze_dart(s) */
				|| ItemID == 5636/* Iron_dart(s) */
				|| ItemID == 5642/* Bronze_javelin(+) */
				|| ItemID == 5643/* Iron_javelin(+) */
				|| ItemID == 5648/* Bronze_javelin(s) */
				|| ItemID == 5649/* Iron_javelin(s) */
				|| ItemID == 5654/* Bronze_knife(+) */
				|| ItemID == 5655/* Iron_knife(+) */
				|| ItemID == 5661/* Bronze_knife(s) */
				|| ItemID == 5662/* Iron_knife(s) */
		) {
			return 1;
		}
		if (ItemID == 802/* Steel_thrownaxe */
				|| ItemID == 808/* Steel_dart */
				|| ItemID == 814/* Steel_dart(p) */
				|| ItemID == 827/* Steel_javelin */
				|| ItemID == 833/* Steel_javelin(p) */
				|| ItemID == 843/* Oak_shortbow */
				|| ItemID == 845/* Oak_longbow */
				|| ItemID == 865/* Steel_knife */
				|| ItemID == 872/* Steel_knife(p) */
				|| ItemID == 886/* Steel_arrow */
				|| ItemID == 887/* Steel_arrow(p) */
				|| ItemID == 2534/* Steel_fire_arrows */
				|| ItemID == 2535/* Steel_fire_arrows */
				|| ItemID == 4236/* Oak_longbow */
				|| ItemID == 4783/* Steel_brutal */
				|| ItemID == 5618/* Steel_arrow(+) */
				|| ItemID == 5624/* Steel_arrow(s) */
				|| ItemID == 5630/* Steel_dart(+) */
				|| ItemID == 5637/* Steel_dart(s) */
				|| ItemID == 5644/* Steel_javelin(+) */
				|| ItemID == 5650/* Steel_javelin(s) */
				|| ItemID == 5656/* Steel_knife(+) */
				|| ItemID == 5663/* Steel_knife(s) */
		) {
			return 5;
		}
		if (ItemID == 869/* Black_knife */
				|| ItemID == 874/* Black_knife(p) */
				|| ItemID == 3093/* Black_dart */
				|| ItemID == 3094/* Black_dart(p) */
				|| ItemID == 4788/* Black_brutal */
				|| ItemID == 5631/* Black_dart(+) */
				|| ItemID == 5638/* Black_dart(s) */
				|| ItemID == 5658/* Black_knife(+) */
				|| ItemID == 5665/* Black_knife(s) */
		) {
			return 10;
		}
		if (ItemID == 803/* Mithril_thrownaxe */
				|| ItemID == 809/* Mithril_dart */
				|| ItemID == 815/* Mithril_dart(p) */
				|| ItemID == 828/* Mithril_javelin */
				|| ItemID == 834/* Mithril_javelin(p) */
				|| ItemID == 847/* Willow_longbow */
				|| ItemID == 849/* Willow_shortbow */
				|| ItemID == 866/* Mithril_knife */
				|| ItemID == 873/* Mithril_knife(p) */
				|| ItemID == 888/* Mithril_arrow */
				|| ItemID == 889/* Mithril_arrow(p) */
				|| ItemID == 1133/* Studded_body */
				|| ItemID == 1169/* Coif */
				|| ItemID == 2536/* Mithril_fire_arrows */
				|| ItemID == 2537/* Mithril_fire_arrows */
				|| ItemID == 4793/* Mithril_brutal */
				|| ItemID == 5619/* Mithril_arrow(+) */
				|| ItemID == 5625/* Mithril_arrow(s) */
				|| ItemID == 5632/* Mithril_dart(+) */
				|| ItemID == 5639/* Mithril_dart(s) */
				|| ItemID == 5645/* Mithril_javelin(+) */
				|| ItemID == 5651/* Mithril_javelin(s) */
				|| ItemID == 5657/* Mithril_knife(+) */
				|| ItemID == 5664/* Mithril_knife(s) */
				|| ItemID == 7362/* Studded_body_(g) */
				|| ItemID == 7364/* Studded_body_(t) */
				|| ItemID == 7366/* Studded_chaps_(g) */
				|| ItemID == 7368/* Studded_chaps_(t) */
		) {
			return 20;
		}
		if (ItemID == 804/* Adamnt_thrownaxe */
				|| ItemID == 810/* Adamant_dart */
				|| ItemID == 816/* Adamant_dart(p) */
				|| ItemID == 829/* Adamant_javelin */
				|| ItemID == 835/* Adamant_javelin(p) */
				|| ItemID == 851/* Maple_longbow */
				|| ItemID == 853/* Maple_shortbow */
				|| ItemID == 867/* Adamant_knife */
				|| ItemID == 875/* Adamant_knife(p) */
				|| ItemID == 890/* Adamant_arrow */
				|| ItemID == 891/* Adamant_arrow(p) */
				|| ItemID == 2538/* Adamnt_fire_arrows */
				|| ItemID == 2539/* Adamnt_fire_arrows */
				|| ItemID == 2866/* Ogre_arrow */
				|| ItemID == 2883/* Ogre_bow */
				|| ItemID == 4798/* Adamant_brutal */
				|| ItemID == 4827/* Comp_ogre_bow */
				|| ItemID == 5620/* Adamant_arrow(+) */
				|| ItemID == 5626/* Adamant_arrow(s) */
				|| ItemID == 5633/* Adamant_dart(+) */
				|| ItemID == 5640/* Adamant_dart(s) */
				|| ItemID == 5646/* Adamant_javelin(+) */
				|| ItemID == 5652/* Adamant_javelin(s) */
				|| ItemID == 5659/* Adamant_knife(+) */
				|| ItemID == 5666/* Adamant_knife(s) */
				|| ItemID == 6322/* Snakeskin_body */
				|| ItemID == 6324/* Snakeskin_chaps */
				|| ItemID == 6326/* Snakeskin_bandana */
				|| ItemID == 6328/* Snakeskin_boots */
				|| ItemID == 6330/* Snakeskin_v'brace */
		) {
			return 30;
		}
		if (ItemID == 805/* Rune_thrownaxe */
				|| ItemID == 811/* Rune_dart */
				|| ItemID == 817/* Rune_dart(p) */
				|| ItemID == 830/* Rune_javelin */
				|| ItemID == 836/* Rune_javelin(p) */
				|| ItemID == 6131/* Spined_helm */
				|| ItemID == 6133/* Spined_body */
				|| ItemID == 6135/* Spined_chaps */
				|| ItemID == 6143/* Spined_boots */
				|| ItemID == 6149/* Spined_gloves */
				|| ItemID == 855/* Yew_longbow */
				|| ItemID == 857/* Yew_shortbow */
				|| ItemID == 868/* Rune_knife */
				|| ItemID == 876/* Rune_knife(p) */
				|| ItemID == 892/* Rune_arrow */
				|| ItemID == 893/* Rune_arrow(p) */
				|| ItemID == 1065/* Dragon_vambraces */
				|| ItemID == 1099/* Dragonhide_chaps */
				|| ItemID == 1135/* Dragonhide_body */
				|| ItemID == 2540/* Rune_fire_arrows */
				|| ItemID == 2541/* Rune_fire_arrows */
				|| ItemID == 2577/* Ranger_boots */
				|| ItemID == 2581/* Robin_hood_hat */
				|| ItemID == 4803/* Rune_brutal */
				|| ItemID == 5621/* Rune_arrow(+) */
				|| ItemID == 5627/* Rune_arrow(s) */
				|| ItemID == 5634/* Rune_dart(+) */
				|| ItemID == 5641/* Rune_dart(s) */
				|| ItemID == 5647/* Rune_javelin(+) */
				|| ItemID == 5653/* Rune_javelin(s) */
				|| ItemID == 5660/* Rune_knife(+) */
				|| ItemID == 5667/* Rune_knife(s) */
				|| ItemID == 7378/* D-hide_chaps_(g) */
				|| ItemID == 7380/* D-hide_chaps_(t) */
				|| ItemID == 7382/* D-hide_chaps_(g) */
				|| ItemID == 7384/* D-hide_chaps_(t) */
		) {
			return 40;
		}
		if (ItemID == 859/* Magic_longbow */
				|| ItemID == 861/* Magic_shortbow */
				|| ItemID == 2487/* Dragon_vambraces */
				|| ItemID == 2493/* Dragonhide_chaps */
				|| ItemID == 2499/* Dragonhide_body */
				|| ItemID == 4160/* Broad_arrows */
				|| ItemID == 6724/* Seercull */
		) {
			return 50;
		}
		if (ItemID == 2489/* Dragon_vambraces */
				|| ItemID == 2495/* Dragonhide_chaps */
				|| ItemID == 2501/* Dragonhide_body */
				|| ItemID == 6522/* Toktz-xil-ul */
		) {
			return 60;
		}
		if (ItemID == 2491/* Dragon_vambraces */
				|| ItemID == 2497/* Dragonhide_chaps */
				|| ItemID == 2503/* Dragonhide_body */
				|| ItemID == 4212/* New_crystal_bow */
				|| ItemID == 4214/* Crystal_bow_full */
				|| ItemID == 4215/* Crystal_bow_9/10 */
				|| ItemID == 4216/* Crystal_bow_8/10 */
				|| ItemID == 4217/* Crystal_bow_7/10 */
				|| ItemID == 4218/* Crystal_bow_6/10 */
				|| ItemID == 4219/* Crystal_bow_5/10 */
				|| ItemID == 4220/* Crystal_bow_4/10 */
				|| ItemID == 4221/* Crystal_bow_3/10 */
				|| ItemID == 4222/* Crystal_bow_2/10 */
				|| ItemID == 4223/* Crystal_bow_1/10 */
				|| ItemID == 4734/* Karils_crossbow */
				|| ItemID == 4934/* Karils_x-bow_100 */
				|| ItemID == 4935/* Karils_x-bow_75 */
				|| ItemID == 4936/* Karils_x-bow_50 */
				|| ItemID == 4937/* Karils_x-bow_25 */
		) {
			return 70;
		}
		return 1;
	}

	public static int slayerReq(int ItemID) {

		if (ItemID == 4156/* Mirror_shield */
		) {
			return 25;
		}
		if (ItemID == 7051/* Unlit_bug_lantern */
				|| ItemID == 7053/* Lit_bug_lantern */
		) {
			return 33;
		}
		if (ItemID == 7159/* Slayer_boots */
		) {
			return 37;
		}
		if (ItemID == 6720/* Slayer_gloves */
		) {
			return 42;
		}
		if (ItemID == 4158/* Leaf-bladed_spear */
				|| ItemID == 4160/* Broad_arrows */
				|| ItemID == 4170/* Slayer's_staff */
		) {
			return 55;
		}
		return 1;
	}

	public static int strengthReq(int ItemID) {

		if (ItemID == 3196/* Black_halberd */
		) {
			return 5;
		}
		if (ItemID == 3198/* Mithril_halberd */
		) {
			return 10;
		}
		if (ItemID == 3200/* Adamant_halberd */
		) {
			return 15;
		}
		if (ItemID == 3202/* Rune_halberd */
		) {
			return 20;
		}
		if (ItemID == 3204/* Dragon_halberd */
		) {
			return 30;
		}
		if (ItemID == 3122/* Granite_shield */
				|| ItemID == 4153/* Granite_maul */
				|| ItemID == 6809/* Granite_legs */
		) {
			return 50;
		}
		if (ItemID == 6528/* Tzhaar-ket-om */
		) {
			return 50;
		}
		if (ItemID == 4718/* Dharoks_greataxe */
				|| ItemID == 4747/* Torags_hammers */
				|| ItemID == 4886/* Dharoks_axe_100 */
				|| ItemID == 4887/* Dharoks_axe_75 */
				|| ItemID == 4888/* Dharoks_axe_50 */
				|| ItemID == 4889/* Dharoks_axe_25 */
				|| ItemID == 4958/* Torags_hammer_100 */
				|| ItemID == 4959/* Torags_hammer_75 */
				|| ItemID == 4960/* Torags_hammer_50 */
				|| ItemID == 4961/* Torags_hammer_25 */
		) {
			return 70;
		}
		return 1;
	}

}
