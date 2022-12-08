package com.zerozipp.client.utils.source;

import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.types.Type;
import java.util.HashMap;

@Aurora(Type.MAPPING)
@SuppressWarnings("unused")
public class Fields extends HashMap<String, String> {
    public static final Fields F = new Fields();

    public Fields() {
        put("minecraft", "R");
        put("mcPlayer", "h");
        put("mcWorld", "f");
        put("mcSettings", "t");
        put("guiScreen", "m");
        put("vertexPosition", "e");
        put("vertexPositionColor", "f");
        put("entityGround", "z");
        put("isKeyPressed", "i");
        put("cPlayerPitch", "e");
        put("cPlayerYaw", "d");
        put("cPlayerGround", "f");
        put("prevRenderArmPitch", "bY");
        put("prevRenderArmYaw", "bX");
        put("renderArmPitch", "bW");
        put("renderArmYaw", "bV");
        put("prevRotationPitch", "y");
        put("prevRotationYaw", "x");
        put("rotationPitch", "w");
        put("rotationYaw", "v");
        put("connection", "d");
        put("vecX", "b");
        put("vecY", "c");
        put("vecZ", "d");
        put("rBlockPos", "e");
        put("rTypeOfHit", "a");
        put("rSideHit", "b");
        put("rHitVec", "c");
        put("rEntityHit", "d");
        put("keySneak", "Y");
        put("keySprint", "Z");
        put("motionX", "s");
        put("motionY", "t");
        put("motionZ", "u");
        put("rightClickDelay", "as");
        put("keyAttack", "ae");
        put("keyJump", "X");
        put("keyPressTime", "j");
        put("leftClickCounter", "ai");
        put("loadedEntityList", "e");
        put("controller", "c");
        put("armMain", "a");
        put("armOffhand", "b");
        put("ticksSwing", "aE");
        put("objectMouseOver", "s");
        put("prevRenderYawOffset", "aO");
        put("prevRotationYawHead", "aQ");
        put("renderYawOffset", "aN");
        put("rotationYawHead", "aP");
        put("gameTimer", "Y");
        put("tickLength", "e");
        put("isGamePaused", "ag");
        put("renderTicksPaused", "ah");
        put("timerTicks", "b");
        put("renderer", "o");
        put("session", "af");
    }
}
