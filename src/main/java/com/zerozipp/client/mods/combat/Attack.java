package com.zerozipp.client.mods.combat;

import com.zerozipp.client.Invoker;
import com.zerozipp.client.utils.Entity;
import com.zerozipp.client.utils.Network;
import com.zerozipp.client.utils.utils.Rotation;
import com.zerozipp.client.utils.base.Rotating;
import com.zerozipp.client.utils.reflect.JField;
import com.zerozipp.client.utils.settings.Active;
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
import java.util.*;
import java.util.function.ToDoubleFunction;

@Aurora(Type.MODULE)
@SuppressWarnings("unchecked")
public class Attack extends Module {
    private final Timer timer;

    public Attack(String name, boolean active, Integer key) {
        super(name, active, key);
        timer = new Timer();
        ArrayList<Active.Listing> list = new ArrayList<>();
        list.add(new Active.Listing("Player", true));
        list.add(new Active.Listing("Animal", true));
        list.add(new Active.Listing("Mob", true));
        settings.add(new Value("Reach", 4, 2, 4.5f));
        settings.add(new Value("Delay", 2, 1, 8));
        settings.add(new Value("Miss", 2, 1, 5));
        settings.add(new Active("Entity", list));
        settings.add(new Toggle("Invisible", true));
        settings.add(new Toggle("Validate", true));
        settings.add(new Toggle("Screen", false));
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        Object mc = Invoker.client.MC();
        Network net = Invoker.client.network;
        JClass w = JClass.getClass("world");
        JClass c = JClass.getClass("minecraft");
        JField screen = c.getField("guiScreen");
        Object g = Invoker.client.network.getRotation();
        boolean s = ((Toggle) settings.get(6)).isActive();
        if((!s && screen.get(mc) != null) || g != null) return;
        Object player = c.getField("mcPlayer").get(mc);
        Object world = c.getField("mcWorld").get(mc);
        Class<?> p = JClass.getClass("livingBase").get();
        Object entities = w.getField("loadedEntityList").get(world);
        ToDoubleFunction<Object> d = entity -> Entity.getDistance(player, entity);
        ArrayList<Object> entityList = (ArrayList<Object>) entities;
        float reach = ((Value) settings.get(0)).getValue();
        boolean in = ((Toggle) settings.get(4)).isActive();
        float miss = ((Value) settings.get(2)).getValue();
        Vector3 pos = Entity.getEyes(player, 1.0f);
        entityList.sort(comparingDouble(d));
        for(Object entity : entityList) {
            if(!isValid(entity)) continue;
            if(!p.isInstance(entity)) continue;
            if(entity.equals(player)) continue;
            if(!Entity.isLiving(entity)) continue;
            if(!in && Entity.isInvisible(entity)) continue;
            if(Entity.getDistance(player, entity) < reach) {
                Vector3 eyes = Entity.getEyes(entity, 1.0f);
                Rotation rot = Entity.getRot(pos, eyes);
                Rotation set = Entity.getRotation(player, rot);
                Rotation last = Entity.getPacketRot(player);
                if(canAttack(mc, player, entity, set)) {
                    net.setRotation(set.pitch, set.yaw);
                    if(canAttack(mc, player, entity, last)) {
                        if(!hasTime()) break;
                        if(Math.random() * miss <= 1) {
                            onAttack(mc, player, last);
                        } else onMiss(player);
                    } break;
                }
            }
        }
    }

    private void onAttack(Object mc, Object player, Rotation rot) {
        JClass c = JClass.getClass("minecraft");
        JMethod clickMouse = c.getDecMethod("clickMouse");
        JField f = c.getField("objectMouseOver");
        Object oldRaytraceHit = f.get(mc);
        f.set(mc, getRaytrace(mc, player, rot));
        clickMouse.call(mc);
        f.set(mc, oldRaytraceHit);
    }

    private void onMiss(Object player) {
        JClass h = JClass.getClass("hand");
        JClass base = JClass.getClass("livingBase");
        JMethod m = base.getMethod("swingArm", h.get());
        JField hand = h.getField("armMain");
        System.out.println(hand.get(null));
        m.call(player, hand.get(null));
    }

    private boolean canAttack(Object mc, Object player, Object entity, Rotation rot) {
        boolean validate = ((Toggle) settings.get(5)).isActive();
        Object cast = getRaytrace(mc, player, rot);
        if(cast == null) return false;
        Raytrace raytrace = new Raytrace(cast);
        String type = raytrace.typeOfHit().toString();
        if(!validate) return type.equals("ENTITY");
        return entity.equals(raytrace.entityHit());
    }

    private Object getRaytrace(Object mc, Object player, Rotation rot) {
        JClass r = JClass.getClass("renderer");
        JClass c = JClass.getClass("minecraft");
        JField f = c.getField("objectMouseOver");
        JMethod m = r.getMethod("getMouseOver", float.class);
        Object render = c.getField("renderer").get(mc);
        Object oldRaytraceHit = f.get(mc);
        Rotating.pushRotation(player);
        Entity.setRotation(player, rot);
        m.call(render, 1.0F);
        Rotating.popRotation(player);
        Object trace = f.get(mc);
        f.set(mc, oldRaytraceHit);
        return trace;
    }

    private boolean hasTime() {
        float delay = ((Value) settings.get(1)).getValue();
        return timer.hasTime(delay * 80);
    }

    private boolean isValid(Object entity) {
        Class<?> ep = JClass.getClass("entityPlayer").get();
        Class<?> ea = JClass.getClass("entityAnimal").get();
        Class<?> em = JClass.getClass("entityMob").get();
        ArrayList<Active.Listing> mobs = ((Active) settings.get(3)).listings;
        boolean player = mobs.get(0).isActive() && ep.isInstance(entity);
        boolean animal = mobs.get(1).isActive() && ea.isInstance(entity);
        boolean mob = mobs.get(2).isActive() && em.isInstance(entity);
        return player || animal || mob;
    }
}
