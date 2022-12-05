package com.zerozipp.client.utils.base;

import com.zerozipp.client.utils.Entity;
import com.zerozipp.client.utils.Rotation;
import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.types.Type;
import java.util.HashMap;

@Aurora(Type.BASE)
@SuppressWarnings("unused")
public class Rotating {
    private static final HashMap<Object, Rotating> map = new HashMap<>();
    public final float yawHead, prevYawHead, yawOffset, prevYawOffset;
    public final Rotation prevRot, rot;

    private Rotating(Rotation r1, Rotation r2, float f1, float f2, float f3, float f4) {
        this.prevYawOffset = f4;
        this.prevYawHead = f2;
        this.yawOffset = f3;
        this.yawHead = f1;
        this.prevRot = r1;
        this.rot = r2;
    }

    public static void pushRotation(Object entity) {
        float f1 = Entity.getYawHead(entity);
        float f2 = Entity.getPrevYawHead(entity);
        float f3 = Entity.getPrevYawOffset(entity);
        float f4 = Entity.getYawOffset(entity);
        Rotation prev = Entity.getPrevRotation(entity);
        Rotation rot = Entity.getRotation(entity);
        Rotating r = new Rotating(prev, rot, f1, f2, f4, f3);
        map.put(entity, r);
    }

    public static void popRotation(Object entity) {
        Rotating r = map.get(entity);
        Entity.setYawHead(entity, r.yawHead, r.prevYawHead);
        Entity.setYawOffset(entity, r.yawOffset, r.prevYawOffset);
        Entity.setPrevRotation(entity, r.prevRot);
        Entity.setRotation(entity, r.rot);
        map.remove(entity);
    }
}
