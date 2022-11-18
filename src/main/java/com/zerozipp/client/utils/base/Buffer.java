package com.zerozipp.client.utils.base;

import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.reflect.JClass;
import com.zerozipp.client.utils.reflect.JMethod;
import com.zerozipp.client.utils.types.Type;

@Aurora(Type.BASE)
@SuppressWarnings("unused")
public class Buffer {
    private final Object buffer;

    public Buffer(Object buffer) {
        this.buffer = buffer;
    }

    public void begin(int i, String b) {
        Object vertex = JClass.getClass("vertexFormat").getField(b).get(null);
        JMethod m = JClass.getClass("buffer").getMethod("beginBuffer", int.class, JClass.getClass("vertex").get());
        m.call(buffer, i, vertex);
    }

    public void pos(double x, double y, double z) {
        JMethod end = JClass.getClass("buffer").getMethod("endBuffer");
        JMethod pos = JClass.getClass("buffer").getMethod("posBuffer", double.class, double.class, double.class);
        end.call(pos.call(buffer, x, y, z));
    }
}
