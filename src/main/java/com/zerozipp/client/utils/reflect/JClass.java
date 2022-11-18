package com.zerozipp.client.utils.reflect;

import com.zerozipp.client.utils.exceptions.JException;
import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.types.Type;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import static com.zerozipp.client.utils.source.Classes.C;
import static com.zerozipp.client.utils.source.Fields.F;
import static com.zerozipp.client.utils.source.Methods.M;

@Aurora(Type.BASE)
@SuppressWarnings("unused")
public class JClass {
    private final Class<?> c;

    public JClass(Class<?> c) {
        this.c = c;
    }

    public JField getField(String name) {
        try {
            Field f = c.getField(F.get(name));
            return new JField(f);
        } catch(Exception e) {
            e.printStackTrace();
            throw new JException();
        }
    }

    public JField getDecField(String name) {
        try {
            Field f = c.getDeclaredField(F.get(name));
            return new JField(f);
        } catch(Exception e) {
            e.printStackTrace();
            throw new JException();
        }
    }

    public JMethod getMethod(String name, Class<?>... args) {
        try {
            return new JMethod(c.getMethod(M.get(name), args));
        } catch(Exception e) {
            e.printStackTrace();
            throw new JException();
        }
    }

    public JMethod getDecMethod(String name, Class<?>... args) {
        try {
            return new JMethod(c.getDeclaredMethod(M.get(name), args));
        } catch(Exception e) {
            e.printStackTrace();
            throw new JException();
        }
    }

    public Constructor<?> getConstructor(Class<?>... args) {
        try {
            return c.getDeclaredConstructor(args);
        } catch(Exception e) {
            e.printStackTrace();
            throw new JException();
        }
    }

    public Object newInstance(Constructor<?> pack, Object... args) {
        try {
            return pack.newInstance(args);
        } catch(Exception e) {
            e.printStackTrace();
            throw new JException();
        }
    }

    public Class<?> get() {
        return c;
    }

    public static JClass getClass(String name) {
        try {
            return new JClass(Class.forName(C.get(name)));
        } catch(Exception e) {
            e.printStackTrace();
            throw new JException();
        }
    }
}
