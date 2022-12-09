package com.zerozipp.client.utils.source;

import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.types.Type;
import java.util.HashMap;

@Aurora(Type.MAPPING)
@SuppressWarnings("unused")
public class Methods extends HashMap<String, String> {
    public static final Methods M = new Methods();

    public Methods() {
        put("keyUpdateState", "a");
        put("mcSetScreen", "a");
        put("keyIsDown", "e");
        put("mcStart", "a");
        put("keyPatch", "W");
        put("runTick", "t");
        put("getFov", "a");
        put("beginBuffer", "a");
        put("posBuffer", "b");
        put("colorBuffer", "a");
        put("endBuffer", "d");
        put("getTessellator", "a");
        put("getBuffer", "c");
        put("drawTessellator", "b");
        put("bindTexture", "i");
        put("getGlTextureId", "b");
        put("enableAlpha", "e");
        put("resetPressed", "k");
        put("sendPacket", "a");
        put("hurtCam", "d");
        put("renderHand", "b");
        put("rayTrace", "a");
        put("getPositionEyes", "f");
        put("isAlive", "aC");
        put("renderOverlay", "a");
        put("getScaledHeight", "b");
        put("getScaledWidth", "a");
        put("updateLightmap", "g");
        put("entityGetPos", "d");
        put("blockPosDown", "b");
        put("worldGetBlockState", "o");
        put("blockStateGetBlock", "u");
        put("blockIsTopSolid", "k");
        put("entityOutlines", "aW");
        put("entityInvisible", "aX");
        put("attackEntity", "a");
        put("criticalHit", "a");
        put("clickMouse", "aA");
        put("renderLiving", "a");
        put("canRenderName", "a");
        put("renderWorld", "b");
        put("setupCamera", "a");
        put("guiOnKey", "a");
        put("getMouseOver", "a");
        put("addVelocity", "f");
        put("setVelocity", "h");
        put("getViewEntity", "aa");
        put("getEntityName", "h_");
        put("getEyeHeight", "by");
        put("getLook", "e");
        put("applyBobbing", "e");
    }
}
