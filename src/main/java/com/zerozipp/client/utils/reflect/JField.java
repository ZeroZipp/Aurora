package com.zerozipp.client.utils.reflect;

import com.zerozipp.client.utils.exceptions.JException;
import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.types.Type;
import java.lang.reflect.Field;

@Aurora(Type.BASE)
@SuppressWarnings("unused")
public class JField {
    private final Field f;

    public JField(Field f) {
        f.setAccessible(true);
        this.f = f;
    }

    public void set(Object access, Object value) {
        try {
            f.set(access, value);
        } catch(Exception e) {
            e.printStackTrace();
            throw new JException();
        }
    }

    public Object get(Object access) {
        try {
            return f.get(access);
        } catch(Exception e) {
            e.printStackTrace();
            throw new JException();
        }
    }
}
