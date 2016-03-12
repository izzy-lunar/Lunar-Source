package com.ownxile.rs2.combat.range;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.ownxile.rs2.Entity;
import com.ownxile.rs2.player.Client;

public class Cannon extends Entity {

	public enum FacingState {

		EAST(2, 517), NORTH(0, 515), NORTH_EAST(1, 516), NORTH_WEST(7, 514), SOUTH(
				4, 519), SOUTH_EAST(3, 518), SOUTH_WEST(5, 520), WEST(6, 521);

		private static List<FacingState> facingStates = new ArrayList<FacingState>();

		static {
			for (FacingState facingState : FacingState.values()) {
				facingStates.add(facingState);
			}
		}

		public static FacingState forId(int id) {
			for (FacingState facingState : facingStates) {
				if (facingState.getId() == id) {
					return facingState;
				}
			}
			return null;
		}

		private int animationId;

		private int id;

		FacingState(int id, int animationId) {
			this.id = id;
			this.animationId = animationId;
		}

		public int getAnimationId() {
			return animationId;
		}

		public int getId() {
			return id;
		}
	}

	private FacingState facingState;
	public List<Integer> nearbyNpcs = new ArrayList<Integer>();
	public List<Integer> nearbyPlayers = new ArrayList<Integer>();
	public Object object;
	private Client owner;
	public Random random;

	private int x, y, height, anim, cannonBalls, stage;

	public Cannon(Client owner, int x, int y, int height) {
		this.owner = owner;
		this.x = x;
		this.y = y;
		this.height = height;
		this.anim = 516;
		this.cannonBalls = 0;
		this.stage = 1;
		this.facingState = FacingState.EAST;
	}

	public void addBalls(int add) {
		cannonBalls = cannonBalls + add;
	}

	public int getAnim() {
		return anim;
	}

	public int getBalls() {
		return cannonBalls;
	}

	public int getHeight() {
		return height;
	}

	public Client getOwner() {
		return owner;
	}

	public int getStage() {
		return stage;
	}

	public FacingState getState() {
		return facingState;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public boolean inFightPits() {
		if (x >= 2370 && y >= 5128 && x <= 2426 && y <= 5167) {
			return true;
		}
		return false;
	}

	@Override
	public void process() {

	}

	public void removeBall() {
		cannonBalls -= 1;
	}

	public void setAnim(int anim) {
		this.anim = anim;
	}

	public void setState(FacingState f) {
		facingState = f;
	}

}
