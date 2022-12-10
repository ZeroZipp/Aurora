package com.zerozipp.client.utils;

import com.zerozipp.client.Invoker;
import com.zerozipp.client.utils.base.Raytrace;
import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.reflect.JClass;
import com.zerozipp.client.utils.reflect.JMethod;
import com.zerozipp.client.utils.types.Type;
import com.zerozipp.client.utils.utils.Rotation;
import com.zerozipp.client.utils.utils.Vector3;

@Aurora(Type.BASE)
@SuppressWarnings({
        "unused",
        "BooleanMethodIsAlwaysInverted"
})
public class Entity {
    public static Vector3 getViewRot(Object entity, float ticks) {
        return getVector(entity, getView(entity, ticks));
    }

    public static Rotation getView(Object entity, float ticks) {
        JClass ea = JClass.getClass("entityAnimal");
        Rotation rot = getRotation(entity, ticks);
        float rotYaw = getYawHead(entity, ticks);
        boolean e = ea.get().isInstance(entity);
        if(e) rot = new Rotation(rot.pitch, rotYaw);
        return rot;
    }

    public static Vector3 getEyes(Object entity, float ticks) {
        JClass c = JClass.getClass("entity");
        JMethod m = c.getMethod("getPositionEyes", float.class);
        return new Vector3(m.call(entity, ticks));
    }

    public static float getYawHead(Object entity, float ticks) {
        float prevYaw = getPrevYawHead(entity);
        float yaw = getYawHead(entity);
        if(ticks != 1.0F) {
            return prevYaw + (yaw - prevYaw) * ticks;
        } else return yaw;
    }

    public static Rotation getRotation(Object entity, float ticks) {
        Rotation prevRot = getPrevRotation(entity);
        Rotation rot = getRotation(entity);
        if(ticks != 1.0F) {
            float pitch = prevRot.pitch + (rot.pitch - prevRot.pitch) * ticks;
            float yaw = prevRot.yaw + (rot.yaw - prevRot.yaw) * ticks;
            return new Rotation(pitch, yaw);
        } else return rot;
    }

    public static Vector3 getPosition(Object entity, float ticks) {
        JClass e = JClass.getClass("entity");
        JMethod m = e.getMethod("getEyeHeight");
        float h = (float) m.call(entity);
        Vector3 pos = getEyes(entity, ticks);
        return pos.add(0, -h, 0);
    }

    public static Vector3 getVector(Object entity, Rotation rot) {
        float f = (float) Math.cos(-rot.yaw * 0.017453292F - (float) Math.PI);
        float f1 = (float) Math.sin(-rot.yaw * 0.017453292F - (float) Math.PI);
        float f2 = (float) -Math.cos(-rot.pitch * 0.017453292F);
        float f3 = (float) Math.sin(-rot.pitch * 0.017453292F);
        return new Vector3(f1 * f2, f3, f * f2);
    }

    public static Vector3 getLook(Object entity, float ticks) {
        JClass c = JClass.getClass("entity");
        JMethod m = c.getMethod("getLook", float.class);
        return new Vector3(m.call(entity, ticks));
    }

    public static boolean isInvisible(Object entity) {
        JClass c = JClass.getClass("entity");
        JMethod m = c.getMethod("entityInvisible");
        return (boolean) m.call(entity);
    }

    public static Vector3 getPosition(Object entity) {
        JClass c = JClass.getClass("entity");
        JMethod m = c.getMethod("entityGetPos");
        return new Vector3(m.call(entity));
    }

    public static boolean isLiving(Object entity) {
        JClass c = JClass.getClass("livingBase");
        JMethod m = c.getMethod("isAlive");
        return (boolean) m.call(entity);
    }

    public static double getDistance(Object entity, Object entity2) {
        Vector3 pos = Entity.getEyes(entity, 1.0f);
        Vector3 pos2 = Entity.getEyes(entity2, 1.0f);
        return pos.distance(pos2);
    }

    public static Raytrace getCast(Object entity, Rotation rot, float reach) {
        Class<?> bhe = JClass.getClass("vec3d").get();
        JClass c = JClass.getClass("world");
        Vector3 look = Space.getLook(rot.pitch, rot.yaw), eyes = Entity.getEyes(entity, 1.0f);
        Vector3 dir = eyes.add(look.x * reach, look.y * reach, look.z * reach);
        Object w = JClass.getClass("minecraft").getField("mcWorld").get(Invoker.client.MC());
        JMethod m = c.getMethod("rayTrace", bhe, bhe, boolean.class, boolean.class, boolean.class);
        Object out = m.call(w, eyes.get(), dir.get(), false, false, true);
        if(out == null) return null;
        return new Raytrace(out);
    }

    public static void setRotation(Object entity, Rotation rot) {
        JClass e = JClass.getClass("entity");
        e.getField("rotationPitch").set(entity, rot.pitch);
        e.getField("rotationYaw").set(entity, rot.yaw);
    }

    public static void setPrevRotation(Object entity, Rotation rot) {
        JClass e = JClass.getClass("entity");
        e.getField("prevRotationPitch").set(entity, rot.pitch);
        e.getField("prevRotationYaw").set(entity, rot.yaw);
    }

    public static void setYawOffset(Object entity, float yaw, float prev) {
        JClass e = JClass.getClass("livingBase");
        e.getField("prevRenderYawOffset").set(entity, prev);
        e.getField("renderYawOffset").set(entity, yaw);
    }

    public static void setYawHead(Object entity, float yaw, float prev) {
        JClass e = JClass.getClass("livingBase");
        e.getField("prevRotationYawHead").set(entity, prev);
        e.getField("rotationYawHead").set(entity, yaw);
    }

    public static float getYawHead(Object entity) {
        JClass e = JClass.getClass("livingBase");
        Object yaw = e.getField("rotationYawHead").get(entity);
        return (float) yaw;
    }

    public static float getPrevYawHead(Object entity) {
        JClass e = JClass.getClass("livingBase");
        Object yaw = e.getField("prevRotationYawHead").get(entity);
        return (float) yaw;
    }

    public static float getYawOffset(Object entity) {
        JClass e = JClass.getClass("livingBase");
        Object yaw = e.getField("renderYawOffset").get(entity);
        return (float) yaw;
    }

    public static float getPrevYawOffset(Object entity) {
        JClass e = JClass.getClass("livingBase");
        Object yaw = e.getField("prevRenderYawOffset").get(entity);
        return (float) yaw;
    }

    public static Rotation getRotation(Object entity) {
        JClass e = JClass.getClass("entity");
        Object pitch = e.getField("rotationPitch").get(entity);
        Object yaw = e.getField("rotationYaw").get(entity);
        return new Rotation((float) pitch, (float) yaw);
    }

    public static Rotation getPrevRotation(Object entity) {
        JClass e = JClass.getClass("entity");
        Object pitch = e.getField("prevRotationPitch").get(entity);
        Object yaw = e.getField("prevRotationYaw").get(entity);
        return new Rotation((float) pitch, (float) yaw);
    }

    public static Rotation getRot(Vector3 p1, Vector3 p2) {
        double x = p2.x - p1.x;
        double y = p2.y - p1.y;
        double z = p2.z - p1.z;
        double d = Math.sqrt(Math.pow(x, 2) + Math.pow(z, 2));
        float yaw = (float)Math.toDegrees(-Math.atan(x/z));
        float pitch = (float)-Math.toDegrees(Math.atan(y/d));
        double v = Math.toDegrees(Math.atan(z/x));
        if(x < 0 && z < 0) yaw = (float)(90 + v);
        else if(x > 0 && z < 0) yaw = (float)(-90 + v);
        pitch = Math.min(Math.max(pitch, -90.0f), 90.0f);
        return new Rotation(pitch, yaw);
    }

    public static Rotation getPacketRot(Object player) {
        JClass e = JClass.getClass("player");
        Object pitch = e.getDecField("reportedPitch").get(player);
        Object yaw = e.getDecField("reportedYaw").get(player);
        return new Rotation((float) pitch, (float) yaw);
    }
}
