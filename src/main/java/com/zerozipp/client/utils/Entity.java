package com.zerozipp.client.utils;

import com.zerozipp.client.Invoker;
import com.zerozipp.client.utils.base.Raytrace;
import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.reflect.JClass;
import com.zerozipp.client.utils.reflect.JMethod;
import com.zerozipp.client.utils.types.Type;

@Aurora(Type.BASE)
@SuppressWarnings("unused")
public class Entity {
    public static Vector3 getEyes(Object entity) {
        JClass c = JClass.getClass("entity");
        JMethod m = c.getMethod("getPositionEyes", float.class);
        return new Vector3(m.call(entity, 1.0F));
    }

    public static boolean isLiving(Object entity) {
        JClass c = JClass.getClass("entityLiving");
        JMethod m = c.getMethod("isAlive");
        return (boolean) m.call(entity);
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
}
