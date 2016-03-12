package com.ownxile.rs2.npcs;

import com.ownxile.config.GameConfig;
import com.ownxile.core.World;
import com.ownxile.core.task.Task;
import com.ownxile.rs2.Entity;
import com.ownxile.util.Misc;
import com.ownxile.util.Stream;

public class NPC extends Entity {

	public boolean applyDead, isDead, needRespawn, respawns;

	public int attackAnim, blockAnim, deathAnim;

	/**
	 * attackType: 0 = melee, 1 = range, 2 = mage
	 */
	
	public int size;
	public int attackType, projectileId, endGfx, spawnedBy, hitDelayTimer, HP,
			MaxHP, hitDiff, animNumber, actionTimer, enemyX, enemyY;

	static int lastMoleX;
	public int firstAttacker;
	public Entity following;
	public String forcedText;
	public int freezeTimer, attackTimer, killerId, killedBy, oldIndex,
			underAttackBy;
	public int hitDiff2 = 0;
	public long lastDamageTaken;

	public int makeX, makeY, maxHit, defence, attack, moveX, moveY, direction,
			walkingType;
	public int mask80var1 = 0;
	public int mask80var2 = 0;
	public int npcId;
	public int npcType;
	public boolean randomWalk;
	public long recentlyInteracted;
	public int spawnX, spawnY;
	public int summonedBy;
	public boolean summoner;
	public int transformId;

	public boolean transformUpdateRequired = false;

	public int viewX, viewY;
	public boolean walkingHome, underAttack;

	public NPC(int _npcId, int _npcType) {
		setType(EntityType.NPC);
		npcId = _npcId;
		npcType = _npcType;
		direction = -1;
		isDead = false;
		applyDead = false;
		actionTimer = 0;
		randomWalk = true;
		size = World.getNpcHandler().getNPCSize(npcType);
	}

	public void appendAnimUpdate(Stream str) {
		str.writeWordBigEndian(animNumber);
		str.writeByte(1);
	}

	public void appendFaceEntity(Stream str) {
		str.writeWord(getUpdateFlags().face);
	}

	public void appendFaceToUpdate(Stream str) {
		str.writeWordBigEndian(viewX);
		str.writeWordBigEndian(viewY);
	}

	public void appendHitUpdate(Stream str) {
		if (HP <= 0) {
			isDead = true;
		}
		str.writeByteC(hitDiff);
		if (hitDiff > 0) {
			str.writeByteS(1);
		} else {
			str.writeByteS(0);
		}

		str.writeByteS(Misc.getCurrentHP(HP, MaxHP, 100));
		str.writeByteC(100);
		// str.writeByteS(HP);
		// str.writeByteC(MaxHP);
	}

	public void appendHitUpdate2(Stream str) {
		if (HP <= 0) {
			isDead = true;
		}
		str.writeByteA(hitDiff2);
		if (hitDiff2 > 0) {
			str.writeByteC(1);
		} else {
			str.writeByteC(0);
		}
		str.writeByteA(HP);
		str.writeByte(MaxHP);
	}

	public void appendMask80Update(Stream str) {
		str.writeWord(mask80var1);
		str.writeDWord(mask80var2);
	}

	public void appendNPCUpdateBlock(Stream str) {
		if (!getUpdateFlags().updateRequired) {
			return;
		}
		int updateMask = 0;
		if (getUpdateFlags().animUpdateRequired) {
			updateMask |= 0x10;
		}
		if (getUpdateFlags().hitUpdateRequired2) {
			updateMask |= 8;
		}
		if (getUpdateFlags().mask80update) {
			updateMask |= 0x80;
		}
		if (getUpdateFlags().dirUpdateRequired) {
			updateMask |= 0x20;
		}
		if (getUpdateFlags().forcedChatRequired) {
			updateMask |= 1;
		}
		if (getUpdateFlags().hitUpdateRequired) {
			updateMask |= 0x40;
		}
		if (getUpdateFlags().focusPointX != -1) {
			updateMask |= 4;
		}

		str.writeByte(updateMask);

		if (getUpdateFlags().animUpdateRequired) {
			appendAnimUpdate(str);
		}
		if (getUpdateFlags().hitUpdateRequired2) {
			appendHitUpdate2(str);
		}
		if (getUpdateFlags().mask80update) {
			appendMask80Update(str);
		}
		if (getUpdateFlags().dirUpdateRequired) {
			appendFaceEntity(str);
		}
		if (getUpdateFlags().forcedChatRequired) {
			str.writeString(forcedText);
		}
		if (getUpdateFlags().hitUpdateRequired) {
			appendHitUpdate(str);
		}
		if (getUpdateFlags().focusPointX != -1) {
			appendSetFocusDestination(str);
		}

	}

	/**
	 * 
	 Face
	 * 
	 **/

	public void appendSetFocusDestination(Stream str) {
		str.writeWordBigEndian(getUpdateFlags().focusPointX);
		str.writeWordBigEndian(getUpdateFlags().focusPointY);
	}

	public void appendTransformUpdate(Stream str) {
		str.writeWordBigEndianA(transformId);
	}

	public void attackNpc(final NPC npc, final int damage, final int gfx,
			final int animation) {
		this.turnNpc(npc.absX, npc.absY);
		final NPC ye = this;
		ye.startAnimation(animation);
		World.getSynchronizedTaskScheduler().schedule(new Task(1, false) {
			int i;

			@Override
			protected void execute() {
				i++;
				if (npc.HP < 1) {
					stop();
				} else if (i == 1) {
					ye.startAnimation(animation);
				} else if (i == 2) {
					npc.gfx0(gfx);
				} else if (i == 3) {
					npc.HP -= damage;
					npc.handleHitMask(damage);
				} else {
					stop();
				}

			}
		});
	}

	public void attackPlayer(int playerId) {
		underAttack = true;
		killerId = playerId;
	}

	public void clearUpdateFlags() {
		getUpdateFlags().updateRequired = false;
		getUpdateFlags().forcedChatRequired = false;
		getUpdateFlags().hitUpdateRequired = false;
		getUpdateFlags().hitUpdateRequired2 = false;
		getUpdateFlags().animUpdateRequired = false;
		getUpdateFlags().dirUpdateRequired = false;
		getUpdateFlags().mask80update = false;
		forcedText = null;
		moveX = 0;
		moveY = 0;
		direction = -1;
		getUpdateFlags().focusPointX = -1;
		getUpdateFlags().focusPointY = -1;
	}

	public void facePlayer(int player) {
		getUpdateFlags().face = player + 32768;
		getUpdateFlags().dirUpdateRequired = true;
		getUpdateFlags().updateRequired = true;
	}

	/**
	 * Text update
	 **/

	public void forceChat(String text) {
		forcedText = text;
		getUpdateFlags().forcedChatRequired = true;
		getUpdateFlags().updateRequired = true;
	}

	public int getLocalX() {
		return getX() - 8 * getRegionX();
	}

	public int getLocalY() {
		return getY() - 8 * getRegionY();
	}

	/**
	 * Graphics
	 **/

	public String getName() {
		return World.getNpcHandler().getNPCName(this.npcType);
	}

	public void getNextNPCMovement(int i) {
		direction = -1;
		if (NPCHandler.npcs[i].freezeTimer == 0) {
			if (GameConfig.CLIP_NPCS) {
				NPCHandler.clip2(this);
			}
			direction = getNextWalkingDirection();
		}

	}

	public int getNextWalkingDirection() {
		int dir;
		dir = Misc.direction(absX, absY, (absX + moveX), (absY + moveY));
		if (dir == -1) {
			return -1;
		}
		dir >>= 1;
		absX += moveX;
		absY += moveY;
		return dir;
	}

	/**
	 * Gets the region x coordinate.
	 * 
	 * @return The region x coordinate.
	 */
	public int getRegionX() {
		return (getX() >> 3) - 6;
	}

	/**
	 * Gets the region y coordinate.
	 * 
	 * @return The region y coordinate.
	 */
	public int getRegionY() {
		return (getY() >> 3) - 6;
	}

	public int getX() {
		return absX;
	}

	public int getY() {
		return absY;
	}

	public void gfx0(int gfx) {
		mask80var1 = gfx;
		mask80var2 = 65536;
		getUpdateFlags().mask80update = true;
		getUpdateFlags().updateRequired = true;
	}

	public void gfx100(int gfx) {
		mask80var1 = gfx;
		mask80var2 = 6553600;
		getUpdateFlags().mask80update = true;
		getUpdateFlags().updateRequired = true;
	}

	public void handleHitMask(int damage) {
		if (!getUpdateFlags().hitUpdateRequired) {
			getUpdateFlags().hitUpdateRequired = true;
			hitDiff = damage;
		} else if (!getUpdateFlags().hitUpdateRequired2) {
			getUpdateFlags().hitUpdateRequired2 = true;
			hitDiff2 = damage;
		}
		getUpdateFlags().updateRequired = true;
	}

	public boolean inWild() {
		if (absX > 2941 && absX < 3392 && absY > 3518 && absY < 3966
				|| absX > 2941 && absX < 3392 && absY > 9918 && absY < 10366) {
			return true;
		}
		return false;
	}

	@Override
	public void process() {

	}

	public void startAnimation(int id) {
		animNumber = id;
		getUpdateFlags().animUpdateRequired = true;
	}

	public void teleportNpc(final int x, final int y, final int h) {
		startAnimation(4731);
		gfx0(678);
		World.getSynchronizedTaskScheduler().schedule(new Task(3) {
			@Override
			protected void execute() {
				absX = x;
				absY = y;
				absZ = h;
				makeX = x;
				makeY = y;
				stop();
			}
		});
	}

	public void moleTele(final int x, final int y, final int h) {
		startAnimation(3314);
		forceChat("Squeak!");
		World.getSynchronizedTaskScheduler().schedule(new Task(3) {
			@Override
			protected void execute() {
				startAnimation(3315);
				absX = x;
				absY = y;
				makeX = x;
				makeY = y;
				absZ = h;
				lastMoleX = x;
				System.out.println(x + "x " + y + "y");
				freezeTimer = 10;
				getUpdateFlags().appearanceUpdateRequired = true;
				stop();
			}
		});
	}

	public void transform(int Id) {
		transformId = Id;
		transformUpdateRequired = true;
		getUpdateFlags().updateRequired = true;
	}

	public void turnNpc(int i, int j) {
		getUpdateFlags().focusPointX = 2 * i + 1;
		getUpdateFlags().focusPointY = 2 * j + 1;
		getUpdateFlags().updateRequired = true;

	}

	public void updateNPCMovement(Stream str) {
		if (direction == -1) {

			if (getUpdateFlags().updateRequired) {

				str.writeBits(1, 1);
				str.writeBits(2, 0);
			} else {
				str.writeBits(1, 0);
			}
		} else {

			str.writeBits(1, 1);
			str.writeBits(2, 1);
			str.writeBits(3, Misc.xlateDirectionToClient[direction]);
			if (getUpdateFlags().updateRequired) {
				str.writeBits(1, 1);
			} else {
				str.writeBits(1, 0);
			}
		}
	}

	public void walkTo(int toX, int toY) {
		moveX = World.getNpcHandler().GetMove(absX, toX);
		moveY = World.getNpcHandler().GetMove(absY, toY);

		getNextNPCMovement(this.npcType);
		getUpdateFlags().updateRequired = true;
	}

}
