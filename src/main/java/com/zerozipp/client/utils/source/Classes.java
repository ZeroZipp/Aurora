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
        put("settings", "bid");
        put("entity", "vg");
        put("world", "amu");
        put("blockPos", "et");
        put("position", "bhe");
        put("blockState", "awt");
        put("block", "aow");
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
    }
}
