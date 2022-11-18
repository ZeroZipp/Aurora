package com.zerozipp.client.utils.reflect;

import com.zerozipp.client.utils.exceptions.JException;
import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.types.Type;
import java.lang.reflect.Method;

@Aurora(Type.BASE)
@SuppressWarnings("unused")
public class JMethod {
    private final Method m;

    public JMethod(Method m) {
        m.setAccessible(true);
        this.m = m;
    }

    public Object call(Object access, Object... params) {
        try {
            return m.invoke(access, params);
        } catch(Exception e) {
            e.printStackTrace();
            throw new JException();
        }
    }
}
