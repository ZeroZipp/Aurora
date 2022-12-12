package com.zerozipp.client.utils.base;

import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.reflect.JClass;
import com.zerozipp.client.utils.reflect.JMethod;
import com.zerozipp.client.utils.types.Type;

@Aurora(Type.BASE)
@SuppressWarnings("unused")
public class Stack {
    private final Object stack;

    public Stack(Object stack) {
        this.stack = stack;
    }

    public boolean isInstance(Class<?> c) {
        JClass s = JClass.getClass("itemStack");
        JMethod o = s.getMethod("stackGetItem");
        return c.isInstance(o.call(stack));
    }

    public Object get() {
        return stack;
    }
}
