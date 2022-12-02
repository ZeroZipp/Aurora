package com.zerozipp.client.utils.base;

import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.reflect.JClass;
import com.zerozipp.client.utils.reflect.JMethod;
import com.zerozipp.client.utils.types.Type;
import java.lang.reflect.Constructor;

@Aurora(Type.BASE)
@SuppressWarnings("unused")
public class Resolution {
    private final Object obj;

    public Resolution(Object mc) {
        JClass c = JClass.getClass("minecraft");
        JClass res = JClass.getClass("resolution");
        Constructor<?> con = res.getConstructor(c.get());
        obj = res.newInstance(con, mc);
    }

    public float getHeight() {
        JClass res = JClass.getClass("resolution");
        JMethod get = res.getMethod("getScaledHeight");
        return (Integer) get.call(obj);
    }

    public float getWidth() {
        JClass res = JClass.getClass("resolution");
        JMethod get = res.getMethod("getScaledWidth");
        return (Integer) get.call(obj);
    }
}
