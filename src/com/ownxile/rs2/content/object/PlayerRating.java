package com.ownxile.rs2.content.object;

public class PlayerRating implements Comparable<PlayerRating> {

	private int rating;
	public String name;

	@Override
	public int compareTo(PlayerRating o) {
		if (rating < o.rating)
			return 1;
		else if (rating == o.rating)
			return 0;
		return -1;
	}

	/**
	 * @return the rating
	 */
	public int getRating() {
		return rating;
	}

	/**
	 * @param rating
	 *            the rating to set
	 */
	public void setRating(int rating) {
		this.rating = rating;
	}

}
