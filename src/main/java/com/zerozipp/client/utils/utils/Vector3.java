package com.zerozipp.client.utils.utils;

import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.reflect.JClass;
import com.zerozipp.client.utils.types.Type;
import java.lang.reflect.Constructor;

@Aurora(Type.BASE)
@SuppressWarnings("unused")
public class Vector3 {
    public final float x, y, z;

    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Vector3(Object vec3) {
        JClass vec3d = JClass.getClass("vec3d");
        JClass vec3i = JClass.getClass("vec3i");
        if(vec3d.get().isInstance(vec3)) {
            x = (float) (double) vec3d.getDecField("vecXd").get(vec3);
            y = (float) (double) vec3d.getDecField("vecYd").get(vec3);
            z = (float) (double) vec3d.getDecField("vecZd").get(vec3);
        } else if(vec3i.get().isInstance(vec3)) {
            x = (float) (int) vec3i.getDecField("vecXi").get(vec3);
            y = (float) (int) vec3i.getDecField("vecYi").get(vec3);
            z = (float) (int) vec3i.getDecField("vecZi").get(vec3);
        } else throw new RuntimeException("Not a 'Vector3'");
    }

    public Vector3 add(float x, float y, float z) {
        return new Vector3(this.x + x, this.y + y, this.z + z);
    }

    public Vector3 add(Vector3 look) {
        return new Vector3(x + look.x, y + look.y, z + look.z);
    }

    public Vector3 round() {
        float x = Math.round(this.x + 0.5) - 0.5f;
        float y = Math.round(this.y + 0.5) - 0.5f;
        float z = Math.round(this.z + 0.5) - 0.5f;
        return new Vector3(x, y, z);
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

    public Object getBlock() {
        JClass b = JClass.getClass("vec3d");
        JClass bp = JClass.getClass("blockPos");
        Constructor<?> c = bp.getConstructor(b.get());
        return b.newInstance(c, get());
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
