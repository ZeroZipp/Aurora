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
    }
}
