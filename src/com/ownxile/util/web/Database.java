package com.ownxile.util.web;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.ownxile.core.World;

public class Database implements Runnable {

	private Connection conn = null;
	private long lastUsed = System.currentTimeMillis();
	private final Logger LOG = Logger.getLogger(Database.class.getName());
	public Statement stm;

	public void close() throws Exception {
		if (conn == null) {
			throw new Exception("Connection is null");
		}
		conn.close();
	}

	private void connect() throws Exception {
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
		} catch (final Exception e) {
			e.printStackTrace();
		}
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/"
					+ World.getSettings().getDbName(), World.getSettings()
					.getDbUser(), World.getSettings().getDbPass());
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public Connection getConnection() throws Exception {
		if (conn == null) {
			throw new Exception("Connection is null");
		}
		if (System.currentTimeMillis() - lastUsed > 500000) {
			try {
				lastUsed = System.currentTimeMillis();
				conn.close();
				connect();
			} catch (final Exception e) {
				LOG.log(Level.SEVERE, "Error refreshing database connection", e);
				throw new Exception("Error refreshing database connection");
			}
		}
		return conn;
	}

	@Override
	public void run() {
		try {
			connect();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
}