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
        put("keyAttack", "ae");
        put("keySneak", "Y");
        put("keyJump", "X");
        put("keyPressTime", "j");
        put("rightClickDelay", "as");
        put("leftClickCounter", "ai");
        put("entityGround", "z");
        put("isKeyPressed", "i");
    }
}
