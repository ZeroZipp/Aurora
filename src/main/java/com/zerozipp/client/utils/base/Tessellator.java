package com.zerozipp.client.utils.base;

import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.reflect.JClass;
import com.zerozipp.client.utils.types.Type;

@Aurora(Type.BASE)
@SuppressWarnings("unused")
public class Tessellator {
    private final Object object;

    public Tessellator() {
        object = JClass.getClass("tessellator").getMethod("getTessellator").call(null);
    }

    public Object getBuffer() {
        return JClass.getClass("tessellator").getMethod("getBuffer").call(object);
    }

    public void draw() {
        JClass.getClass("tessellator").getMethod("drawTessellator").call(object);
    }
}
