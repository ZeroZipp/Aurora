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
        JClass c = JClass.getClass("buffer");
        Object vertex = JClass.getClass("vertexFormat").getField(b).get(null);
        JMethod m = c.getMethod("beginBuffer", int.class, JClass.getClass("vertex").get());
        m.call(buffer, i, vertex);
    }

    public Buffer pos(double x, double y, double z) {
        JClass c = JClass.getClass("buffer");
        JMethod pos = c.getMethod("posBuffer", double.class, double.class, double.class);
        return new Buffer(pos.call(buffer, x, y, z));
    }

    public Buffer color(float r, float g, float b, float a) {
        JClass c = JClass.getClass("buffer");
        JMethod color = c.getMethod("colorBuffer", float.class, float.class, float.class, float.class);
        return new Buffer(color.call(buffer, r, g, b, a));
    }

    public void end() {
        JClass c = JClass.getClass("buffer");
        JMethod end = c.getMethod("endBuffer");
        end.call(buffer);
    }
}
