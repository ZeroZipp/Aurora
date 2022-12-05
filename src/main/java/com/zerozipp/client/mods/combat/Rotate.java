package com.zerozipp.client.mods.combat;

import com.zerozipp.client.Invoker;
import com.zerozipp.client.utils.Entity;
import com.zerozipp.client.utils.Rotation;
import com.zerozipp.client.utils.settings.Toggle;
import com.zerozipp.client.utils.settings.Value;
import com.zerozipp.client.utils.base.Module;
import com.zerozipp.client.utils.base.Raytrace;
import com.zerozipp.client.utils.interfaces.Aurora;
import static java.util.Comparator.comparingDouble;
import com.zerozipp.client.utils.reflect.JClass;
import com.zerozipp.client.utils.types.Type;
import com.zerozipp.client.utils.utils.Vector3;
import java.util.*;
import java.util.function.ToDoubleFunction;

@Aurora(Type.MODULE)
@SuppressWarnings("unchecked")
public class Rotate extends Module {
    public Rotate(String name, boolean active, Integer key) {
        super(name, active, key);
        settings.add(new Value("Reach", 5, 2, 6));
        settings.add(new Value("Delay", 2, 1, 3));
        settings.add(new Toggle("Cast", true));
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        Object mc = Invoker.client.MC();
        JClass w = JClass.getClass("world");
        JClass c = JClass.getClass("minecraft");
        Object player = c.getField("mcPlayer").get(mc);
        Object world = c.getField("mcWorld").get(mc);
        Class<?> p = JClass.getClass("livingBase").get();
        Object entities = w.getField("loadedEntityList").get(world);
        ToDoubleFunction<Object> d = entity -> Entity.getDistance(player, entity);
        ArrayList<Object> entityList = (ArrayList<Object>) entities;
        float reach = ((Value) settings.get(0)).getValue();
        entityList.sort(comparingDouble(d));
        Vector3 pos = Entity.getEyes(player);
        for(Object entity : entityList) {
            if(!p.isInstance(entity)) continue;
            if(!Entity.isLiving(entity)) continue;
            if(entity.equals(player)) continue;
            if(Entity.getDistance(player, entity) < reach) {
                Rotation rot = Entity.getRot(pos, Entity.getEyes(entity));
                float dist = (float) Entity.getDistance(player, entity);
                Raytrace trace = Entity.getCast(player, rot, dist);
                boolean ray = ((Toggle) settings.get(2)).isActive();
                if((trace != null && trace.typeOfHit().toString().equals("MISS")) || !ray) {
                    this.onRotate(player, rot);
                    break;
                }
            }
        }
    }

    private void onRotate(Object entity, Rotation rot) {
        JClass e = JClass.getClass("entity");
        float yaw = (float) e.getField("rotationYaw").get(entity);
        float pitch = (float) e.getField("rotationPitch").get(entity);
        float delay = ((Value) this.settings.get(1)).getValue();
        float cPitch = rot.pitch - pitch;
        float cYaw = yaw % 360.0F;
        float delta = rot.yaw - cYaw;
        if(180.0F < delta) delta -= 360.0F;
        else if(-180.0F > delta) delta += 360.0F;
        pitch = pitch + cPitch / delay;
        yaw = yaw + delta / delay;
        Rotation x = new Rotation(pitch, yaw);
        Entity.setRotation(entity, x);
    }
}
