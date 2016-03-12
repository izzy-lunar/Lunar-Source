package com.ownxile.util.web.vote;

/**
 * @author Robbie
 * @description a class to hold vote claim variables
 */
public class CachedVoteClaim {

	/**
	 * cache variables
	 * 
	 */
	private long voteTimeMillis;
	private String userName;

	/**
	 * Constructs a cached vote claim
	 * 
	 * @param username
	 */
	public CachedVoteClaim(String name) {
		setVoteTimeMillis(System.currentTimeMillis());
		setUserName(name);
	}

	/**
	 * sets the cached vote time
	 * 
	 * @param vote
	 *            time in miliseconds
	 */
	public void setVoteTimeMillis(long voteTimeMillis) {
		this.voteTimeMillis = voteTimeMillis;
	}

	/**
	 * gets the cached vote time
	 * 
	 * @return the voteTimeMillis
	 */
	public long getVoteTimeMillis() {
		return voteTimeMillis;
	}

	/**
	 * sets the username
	 * 
	 * @param userName
	 *            the userName to set
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * returns the username
	 * 
	 * @return the userName
	 */
	public String getUserName() {
		return userName;
	}

}
