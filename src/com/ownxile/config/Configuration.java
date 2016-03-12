package com.ownxile.config;

import java.io.FileInputStream;
import java.util.Properties;

/**
 * XML {GameSettings} class by com.ownxile.rs2.world <Roboyto>
 */
public class Configuration {

	public static final int VERSION = 474;

	private String database, dbname, dbuser, dbpass, pwnscript, members,
			highscores, doubleXp;
	public int loginAttempts;
	private Properties p = new Properties();

	public String getDbName() {
		return dbname;
	}

	public String getDbPass() {
		return dbpass;
	}

	public String getDbUser() {
		return dbuser;
	}

	public boolean highscoresEnabled() {
		return Boolean.parseBoolean(highscores);
	}

	public boolean isDatabase() {
		return Boolean.parseBoolean(database);
	}

	public boolean isDoubleExp() {
		return Boolean.parseBoolean(doubleXp);
	}

	public boolean loadPlugins() {
		return Boolean.parseBoolean(pwnscript);
	}

	public void loadSetting(String string) {
		if (p.getProperty(string).length() > 0) {
			string = p.getProperty(string);
		}
	}

	public void loadSettings() {
		try {
			p.loadFromXML(new FileInputStream(FileConfig.SETTINGS_FILE_PATH));
			dbname = p.getProperty("database_name");
			dbuser = p.getProperty("database_user");
			dbpass = p.getProperty("database_pass");
			database = p.getProperty("database_enabled");
			pwnscript = p.getProperty("load_plugins");
			members = p.getProperty("members_world");
			highscores = p.getProperty("highscores_enabled");
			doubleXp = p.getProperty("double_xp");
		} catch (final Exception e) {
			e.printStackTrace();
			return;
		}
	}

	public boolean membersWorld() {
		return Boolean.parseBoolean(members);
	}

}
