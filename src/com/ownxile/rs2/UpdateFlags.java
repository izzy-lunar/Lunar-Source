package com.ownxile.rs2;

public class UpdateFlags {

	public boolean dirUpdateRequired, animUpdateRequired, hitUpdateRequired,
			updateRequired, forcedChatRequired, faceToUpdateRequired,
			hitUpdateRequired2, mask80update, mask100update,
			forceMovementUpdateRequired, forcedChatUpdateRequired,
			appearanceUpdateRequired, chatTextUpdateRequired;

	public int focusPointX, focusPointY, face, mask80var1, mask80var2,
			mask100var1, mask100var2, animationRequest, animationWaitCycles;

	public String forcedText;

	public void setAppearanceUpdateRequired(boolean b) {
		appearanceUpdateRequired = b;

	}

}
