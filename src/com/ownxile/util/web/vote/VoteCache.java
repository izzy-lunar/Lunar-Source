package com.ownxile.util.web.vote;

import java.util.ArrayList;

/**
 * @author Robbie
 * 
 */
public class VoteCache {

	private final static int HOUR_IN_MILLIS = 3600000;
	private final static int MINUTE_IN_MILLIS = 60000;
	private final static int HOURS_PER_CLAIM = 12;

	static ArrayList<CachedVoteClaim> cachedVotes = new ArrayList<CachedVoteClaim>();

	public static String getHoursTillVote(String name) {
		long currentTime = System.currentTimeMillis();
		for (CachedVoteClaim cached : cachedVotes) {
			if (cached.getUserName().equalsIgnoreCase(name)) {
				long voteTime = cached.getVoteTimeMillis();
				long calc = (HOUR_IN_MILLIS * HOURS_PER_CLAIM)
						- (currentTime - voteTime);
				short hours = (short) (calc / HOUR_IN_MILLIS);
				if (calc > HOUR_IN_MILLIS) {
					long timePassed = currentTime - voteTime;
					long hoursPassed = (short) timePassed % HOUR_IN_MILLIS;
					short minutes = (short) (60 - ((timePassed - hoursPassed) / MINUTE_IN_MILLIS));
					if (minutes == 60) {
						hours += 1;
						return hours + " hours";
					}
					return hours + " hours " + minutes + " minutes";
				} else
					return calc / MINUTE_IN_MILLIS + " minutes";
			}
		}
		return "Vote not found";
	}

	public static void addClaimToCache(String playerName) {
		cachedVotes.add(new CachedVoteClaim(playerName.toLowerCase()));
	}

	public static boolean canVote(String userName) {
		for (CachedVoteClaim cached : cachedVotes) {
			if (cached.getUserName().equalsIgnoreCase(userName)
					&& System.currentTimeMillis() - cached.getVoteTimeMillis() < HOUR_IN_MILLIS
							* HOURS_PER_CLAIM) {
				return false;
			}
		}
		return true;
	}

	public static void purgeCache() {
		int i = 0;
		for (CachedVoteClaim cached : cachedVotes) {
			if (System.currentTimeMillis() - cached.getVoteTimeMillis() < HOUR_IN_MILLIS
					* HOURS_PER_CLAIM) {
				cachedVotes.remove(cached);
				i++;
			}
		}
		if (i > 0)
			System.out.println("Cleared " + i + " cached votes.");
	}

}
