package com.zerozipp.client.utils;

import com.zerozipp.client.Invoker;
import com.zerozipp.client.utils.base.Raytrace;
import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.reflect.JClass;
import com.zerozipp.client.utils.reflect.JMethod;
import com.zerozipp.client.utils.types.Type;
import com.zerozipp.client.utils.utils.Vector3;

@Aurora(Type.BASE)
@SuppressWarnings("unused")
public class Entity {
    public static Vector3 getEyes(Object entity) {
        JClass c = JClass.getClass("entity");
        JMethod m = c.getMethod("getPositionEyes", float.class);
        return new Vector3(m.call(entity, 1.0F));
    }

    public static boolean isLiving(Object entity) {
        JClass c = JClass.getClass("livingBase");
        JMethod m = c.getMethod("isAlive");
        return (boolean) m.call(entity);
    }

    public static double getDistance(Object entity, Object entity2) {
        Vector3 pos = Entity.getEyes(entity);
        Vector3 pos2 = Entity.getEyes(entity2);
        return pos.distance(pos2);
    }

    public static Raytrace getCast(Object entity, Rotation rot, float reach) {
        Class<?> bhe = JClass.getClass("vec3d").get();
        JClass c = JClass.getClass("world");
        Vector3 look = Space.getLook(rot.pitch, rot.yaw), eyes = Entity.getEyes(entity);
        Vector3 dir = eyes.add(look.x * reach, look.y * reach, look.z * reach);
        Object w = JClass.getClass("minecraft").getField("mcWorld").get(Invoker.client.MC());
        JMethod m = c.getMethod("rayTrace", bhe, bhe, boolean.class, boolean.class, boolean.class);
        Object out = m.call(w, eyes.get(), dir.get(), false, false, true);
        if(out == null) return null;
        return new Raytrace(out);
    }

    public static Rotation getRot(Vector3 p1, Vector3 p2) {
        double x = p2.x - p1.x;
        double y = p2.y - p1.y;
        double z = p2.z - p1.z;
        double d = Math.sqrt(Math.pow(x, 2) + Math.pow(z, 2));
        float yaw = (float)Math.toDegrees(-Math.atan(x/z));
        float pitch = (float)-Math.toDegrees(Math.atan(y/d));

        double v = Math.toDegrees(Math.atan(z/x));
        if(x < 0 && z < 0) {
            yaw = (float)(90 + v);
        }else if(x > 0 && z < 0) {
            yaw = (float)(-90 + v);
        }

        pitch = Math.min(Math.max(pitch, -90.0f), 90.0f);
        return new Rotation(pitch, yaw);
    }
}
