package com.ownxile;

import java.net.InetSocketAddress;

import org.apache.mina.common.IoAcceptor;
import org.apache.mina.transport.socket.nio.SocketAcceptor;
import org.apache.mina.transport.socket.nio.SocketAcceptorConfig;

import com.ownxile.config.GameConfig;
import com.ownxile.core.KillGame;
import com.ownxile.core.Plugin;
import com.ownxile.core.World;
import com.ownxile.net.connection.ConnectionHandler;
import com.ownxile.net.connection.ConnectionThrottleFilter;
import com.ownxile.util.Logger;
import com.ownxile.util.web.Database;

/**
 * @author Robbie
 * 
 */
public class GameServer {

	/**
	 * 
	 */
	private static IoAcceptor acceptor;

	/**
	 * 
	 */
	private static ConnectionHandler connectionHandler;

	/**
	 * 
	 */
	private static Database database = new Database();

	/**
	 * 
	 */
	private static ConnectionThrottleFilter throttleFilter;

	/**
	 * 
	 */
	private static void connect() {
		try {
			acceptor = new SocketAcceptor();
			connectionHandler = new ConnectionHandler();
			final SocketAcceptorConfig sac = new SocketAcceptorConfig();
			sac.getSessionConfig().setTcpNoDelay(false);
			sac.setReuseAddress(true);
			sac.setBacklog(100);
			throttleFilter = new ConnectionThrottleFilter(
					GameConfig.CONNECTION_DELAY);
			sac.getFilterChain().addFirst("throttleFilter", throttleFilter);
			getAcceptor().bind(new InetSocketAddress(getPort()),
					getConnectionHandler(), sac);
			try {
				World.initialize();
			} catch (Throwable e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return
	 */
	public static IoAcceptor getAcceptor() {
		return acceptor;
	}

	/**
	 * @return
	 */
	public static ConnectionHandler getConnectionHandler() {
		return connectionHandler;
	}

	/**
	 * @return
	 */
	public static Database getDatabase() {
		return database;
	}

	/**
	 * @param world
	 * @return the port that world is on
	 */
	public static int getPort() {
		return 43593 + getWorld();
	}

	/**
	 * @return world id
	 */
	public static int getWorld() {
		return GameConfig.WORLD_ID;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			startup();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @category Startup loading
	 * @throws Exception
	 */
	private static void startup() {
		try {
			World.getSettings().loadSettings();
			System.setOut(new Logger(System.out));
			System.setErr(new Logger(System.err));
			System.out.println("Attempting to run server startup.");
			if (World.getSettings().isDatabase()) {
				new Thread(database).run();
			}
			if (World.getSettings().loadPlugins()) {
				Plugin.load();
			}
			Runtime.getRuntime().addShutdownHook(new KillGame());
			connect();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
