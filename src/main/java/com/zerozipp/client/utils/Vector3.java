package com.zerozipp.client.utils;

import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.reflect.JClass;
import com.zerozipp.client.utils.types.Type;
import java.lang.reflect.Constructor;

import static com.zerozipp.client.utils.source.Classes.C;

@Aurora(Type.BASE)
@SuppressWarnings("unused")
public class Vector3 {
    public final float x, y, z;

    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3(Object vec3d) {
        if(vec3d.getClass().getName().equals(C.get("vec3d"))) {
            JClass c = JClass.getClass("vec3d");
            x = (float) c.getDecField("vecX").get(vec3d);
            y = (float) c.getDecField("vecY").get(vec3d);
            z = (float) c.getDecField("vecZ").get(vec3d);
        } else throw new RuntimeException("Not a 'Vector3D'");
    }

    public Vector3 add(float x, float y, float z) {
        return new Vector3(this.x + x, this.y + y, this.z + z);
    }

    public double distance(Vector3 vec) {
        double x = vec.x - this.x;
        double y = vec.y - this.y;
        double z = vec.z - this.z;
        return Math.sqrt(x * x + y * y + z * z);
    }

    public Object get() {
        JClass b = JClass.getClass("vec3d");
        Constructor<?> c = b.getConstructor(double.class, double.class, double.class);
        return JClass.getClass("vec3d").newInstance(c, x, y, z);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        if(obj instanceof Vector3) {
            Vector3 v = (Vector3) obj;
            return v.x == x && v.y == y && v.z == z;
        } else return false;
    }
}
