package com.zerozipp.client.utils.source;

import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.types.Type;
import java.util.HashMap;

@Aurora(Type.MAPPING)
@SuppressWarnings("unused")
public class Classes extends HashMap<String, String> {
    public static final Classes C = new Classes();

    public Classes() {
        put("minecraft", "bib");
        put("entity", "vg");
        put("entityLiving", "vp");
        put("player", "bud");
        put("world", "amu");
        put("keyBind", "bhy");
        put("screen", "blk");
        put("texture", "cdg");
        put("glState", "bus");
        put("tessellator", "bve");
        put("buffer", "buk");
        put("vertexFormat", "cdy");
        put("vertex", "cea");
        put("renderer", "buq");
        put("clickGui", "Screen");
        put("network", "gw");
        put("cPlayer", "lk");
        put("cPlayerRot", "lk$c");
        put("netHandler", "brz");
        put("packet", "ht");
        put("vec3d", "bhe");
        put("raytrace", "bhc");
        put("overlay", "biq");
        put("resolution", "bit");
    }
}
