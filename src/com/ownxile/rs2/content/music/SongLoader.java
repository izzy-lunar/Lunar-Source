package com.ownxile.rs2.content.music;

import java.util.ArrayList;
import java.util.List;

import com.ownxile.rs2.player.Client;
import com.ownxile.rs2.player.Player;

public class SongLoader {

	private static List<Song> songs = new ArrayList<Song>();

	public static void addSong(Song song) {
		songs.add(song);
	}

	@SuppressWarnings("unused")
	private static int getSong(Player player) {
		for (Song song : getSongs()) {
			if (song.getZone().inZone(player)) {
				return song.getId();
			}
		}
		return 0;
	}

	/**
	 * @return the songs
	 */
	public static List<Song> getSongs() {
		return songs;
	}

	public static void loadSong(Client client) {
		int song = 0;// getSong(client);
		if (song > 0) {
			client.getFunction().playSong(song);
		}
	}

}
