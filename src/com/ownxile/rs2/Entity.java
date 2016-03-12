package com.ownxile.rs2;

import java.util.HashMap;

public abstract class Entity {

	public enum EntityType {
		NPC, PLAYER;
	}

	public int absX, absY, absZ;

	private HashMap<String, Object> attributes = new HashMap<String, Object>();

	public boolean dead, frozen;

	private EntityType entityType = null;

	private UpdateFlags updateFlags = new UpdateFlags();

	public void addAttribute(String key, Object value) {
		if (attributes.containsKey(key)) {
			attributes.remove(key);
		}
		attributes.put(key, value);
	}

	public int getAbsX() {
		return absX;
	}

	public int getAbsY() {
		return absY;
	}

	public Object getAttribute(String key) {
		return attributes.get(key);
	}

	public HashMap<String, Object> getAttributes() {
		return attributes;
	}

	public EntityType getEntityType() {
		return entityType;
	}

	public int getHeightLevel() {
		return absZ;
	}

	public UpdateFlags getUpdateFlags() {
		return updateFlags;
	}

	public boolean hasAttribute(String key) {
		return attributes.containsKey(key);
	}

	public boolean inMulti() {
		if (absX >= 2817 && absY >= 5252 && absX <= 2961 && absY <= 5376
				|| absX > 3471 && absX < 3550 && absY > 9480 && absY < 9540
				|| absX >= 3136 && absX <= 3327 && absY >= 3519 && absY <= 3607
				|| absX >= 2607 && absX <= 2644 && absY >= 3296 && absY <= 3332
				|| absX >= 2949 && absX <= 3001 && absY >= 3370 && absY <= 3392
				|| absX >= 3250 && absX <= 3342 && absY >= 9800 && absY <= 9870
				|| absX >= 3190 && absX <= 3327 && absY >= 3648 && absY <= 3839
				|| absX >= 3200 && absX <= 3390 && absY >= 3840 && absY <= 3967
				|| absX >= 2992 && absX <= 3007 && absY >= 3912 && absY <= 3967
				|| absX >= 2946 && absX <= 2959 && absY >= 3816 && absY <= 3831
				|| absX >= 3008 && absX <= 3199 && absY >= 3856 && absY <= 3903
				|| absX >= 3008 && absX <= 3071 && absY >= 3600 && absY <= 3711
				|| absX >= 3072 && absX <= 3327 && absY >= 3608 && absY <= 3647
				|| absX >= 2624 && absX <= 2690 && absY >= 2550 && absY <= 2619
				|| absX >= 2371 && absX <= 2422 && absY >= 5062 && absY <= 5117
				|| absX >= 2896 && absX <= 2927 && absY >= 3595 && absY <= 3630
				|| absX >= 2892 && absX <= 2932 && absY >= 4435 && absY <= 4464
				|| absX >= 3147 && absX <= 3165 && absY >= 3915 && absY <= 3945
				|| absX >= 2943 && absX <= 3008 && absY >= 3319 && absY <= 3388
				|| absX >= 2368 && absX <= 2431 && absY >= 3071 && absY <= 3135
				|| absX >= 2368 && absX <= 2401 && absY >= 9501 && absY <= 9528
				|| absX >= 2391 && absX <= 2410 && absY >= 9494 && absY <= 9513
				|| absX >= 2401 && absX <= 2430 && absY >= 9481 && absY <= 9504
				|| absX >= 2256 && absX <= 2287 && absY >= 4680 && absY <= 4711
				|| absX >= 2661 && absX <= 2690 && absY >= 3711 && absY <= 3735
				|| absX >= 2509 && absX <= 2543 && absY >= 4630 && absY <= 4670
				|| absX >= 1850 && absX <= 1970 && absY >= 4350 && absY <= 4450
				|| absX >= 1810 && absX <= 1930 && absY >= 5135 && absY <= 5194
				|| absX >= 3072 && absX <= 3172 && absY >= 9923
				&& absY <= 10023

		) {
			return true;
		}
		return false;
	}

	public abstract void process();

	public void removeAttribute(String key) {
		attributes.remove(key);
	}

	public void setType(EntityType t) {
		entityType = t;
	}

}
