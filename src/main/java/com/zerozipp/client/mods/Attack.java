package com.zerozipp.client.mods;

import com.zerozipp.client.Invoker;
import com.zerozipp.client.utils.Entity;
import com.zerozipp.client.utils.Rotation;
import com.zerozipp.client.utils.reflect.JField;
import com.zerozipp.client.utils.settings.Toggle;
import com.zerozipp.client.utils.settings.Value;
import com.zerozipp.client.utils.utils.Timer;
import com.zerozipp.client.utils.base.Module;
import com.zerozipp.client.utils.base.Raytrace;
import com.zerozipp.client.utils.interfaces.Aurora;
import static java.util.Comparator.comparingDouble;
import com.zerozipp.client.utils.reflect.JClass;
import com.zerozipp.client.utils.reflect.JMethod;
import com.zerozipp.client.utils.types.Type;
import com.zerozipp.client.utils.utils.Vector3;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.function.ToDoubleFunction;

@Aurora(Type.MODULE)
@SuppressWarnings("unchecked")
public class Attack extends Module {
    private final Timer timer;

    public Attack(String name, boolean active, Integer key) {
        super(name, active, key);
        timer = new Timer();
        settings.add(new Value("Reach", 5, 2, 6));
        settings.add(new Value("Delay", 2, 1, 8));
        settings.add(new Toggle("Cast", true));
        settings.add(new Toggle("Wait", false));
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        Object mc = Invoker.client.MC();
        JClass w = JClass.getClass("world");
        JClass c = JClass.getClass("minecraft");
        JClass con = JClass.getClass("controller");
        Object player = c.getField("mcPlayer").get(mc);
        Object world = c.getField("mcWorld").get(mc);
        Class<?> e = JClass.getClass("entity").get();
        Class<?> p = JClass.getClass("livingBase").get();
        Class<?> ep = JClass.getClass("entityPlayer").get();
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
                    Invoker.client.network.setRotation(rot.pitch, rot.yaw);
                    float delay = ((Value) settings.get(1)).getValue();
                    if(((Toggle) settings.get(3)).isActive()) {
                        JClass el = JClass.getClass("livingBase");
                        JField t = el.getDecField("ticksSwing");
                        if((int) t.get(player) <= 12) break;
                    } else if(!timer.hasTime(delay * 80)) break;
                    JMethod a = con.getMethod("attackEntity", ep, e);
                    Object co = c.getField("controller").get(mc);
                    a.call(co, player, entity);
                    JClass h = JClass.getClass("hand");
                    sendPacket(h.getField("armMain").get(null));
                    break;
                }
            }
        }
    }

    private void sendPacket(Object hand) {
        JClass h = JClass.getClass("hand");
        JClass arm = JClass.getClass("cSwingArm");
        Constructor<?> c = arm.getConstructor(h.get());
        Class<?> ht = JClass.getClass("packet").get();
        JMethod send = JClass.getClass("netHandler").getMethod("sendPacket", ht);
        Object player = JClass.getClass("minecraft").getField("mcPlayer").get(Invoker.client.MC());
        Object connection = JClass.getClass("player").getField("connection").get(player);
        send.call(connection, arm.newInstance(c, hand));
    }
}
