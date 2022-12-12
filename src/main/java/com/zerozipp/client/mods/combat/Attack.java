package com.zerozipp.client.mods.combat;

import com.zerozipp.client.Invoker;
import com.zerozipp.client.utils.Entity;
import com.zerozipp.client.utils.Renderer;
import com.zerozipp.client.utils.utils.Color;
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
    private Object entity = null;

    public Attack(String name, boolean active, Integer key) {
        super(name, active, key);
        timer = new Timer();
        ArrayList<Active.Listing> list = new ArrayList<>();
        list.add(new Active.Listing("Player", true));
        list.add(new Active.Listing("Animal", true));
        list.add(new Active.Listing("Mob", true));
        settings.add(new Value("Reach", 4, 2, 4.5f));
        settings.add(new Value("Delay", 2, 1, 8));
        settings.add(new Active("Entity", list));
        settings.add(new Toggle("Invisible", true));
        settings.add(new Toggle("Screen", false));
    }

    @Override
    public void onDisable() {
        super.onDisable();
        entity = null;
    }

    @Override
    public void onRender(float ticks) {
        super.onRender(ticks);
        if(entity == null) return;
        Object mc = Invoker.client.MC();
        JClass c = JClass.getClass("minecraft");
        JField player = c.getField("mcPlayer");
        Vector3 eyes = Entity.getPosition(player.get(mc), ticks);
        Vector3 pos = Entity.getPosition(entity, ticks);
        pos = pos.add(-eyes.x, -eyes.y, -eyes.z);
        Color color = new Color(255, 0, 0, 255);
        Renderer.drawCircle(pos, 0.8f, color, 1);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        this.entity = null;
        Object mc = Invoker.client.MC();
        JClass w = JClass.getClass("world");
        JClass c = JClass.getClass("minecraft");
        JField screen = c.getField("guiScreen");
        Object g = Invoker.client.network.getRotation();
        boolean s = ((Toggle) settings.get(4)).isActive();
        if((!s && screen.get(mc) != null) || g != null) return;
        JClass r = JClass.getClass("renderer");
        JField f = c.getField("objectMouseOver");
        Object er = c.getField("renderer").get(mc);
        Object player = c.getField("mcPlayer").get(mc);
        Object world = c.getField("mcWorld").get(mc);
        Class<?> p = JClass.getClass("livingBase").get();
        JMethod clickMouse = c.getDecMethod("clickMouse");
        JMethod m = r.getMethod("getMouseOver", float.class);
        Object entities = w.getField("loadedEntityList").get(world);
        ToDoubleFunction<Object> d = entity -> Entity.getDistance(player, entity);
        ArrayList<Object> entityList = (ArrayList<Object>) entities;
        float reach = ((Value) settings.get(0)).getValue();
        boolean in = ((Toggle) settings.get(3)).isActive();
        Vector3 pos = Entity.getEyes(player, 1.0f);
        entityList.sort(comparingDouble(d));
        for(Object entity : entityList) {
            if(!isValid(entity)) continue;
            if(!p.isInstance(entity)) continue;
            if(entity.equals(player)) continue;
            if(!Entity.isLiving(entity)) continue;
            if(!in && Entity.isInvisible(entity)) continue;
            if(Entity.getDistance(player, entity) < reach) {
                Rotation rot = Entity.getRot(pos, Entity.getEyes(entity, 1.0f));
                float dist = (float) Entity.getDistance(player, entity);
                Raytrace trace = Entity.getCast(player, rot, dist);
                Rotation newRot = this.getRotation(player, rot);
                if(trace != null && trace.typeOfHit().toString().equals("MISS")) {
                    Invoker.client.network.setRotation(newRot.pitch, newRot.yaw);
                    Rotation last = Entity.getPacketRot(player);
                    Object oldRaytraceHit = f.get(mc);
                    Rotating.pushRotation(player);
                    Entity.setRotation(player, last);
                    m.call(er, 1.0F);
                    Rotating.popRotation(player);
                    if(onAttack() && hasTime()) {
                        clickMouse.call(mc);
                    } f.set(mc, oldRaytraceHit);
                    this.entity = entity;
                    break;
                }
            }
        }
    }

    private boolean onAttack() {
        Object mc = Invoker.client.MC();
        JClass c = JClass.getClass("minecraft");
        JField f = c.getField("objectMouseOver");
        if(f.get(mc) != null) {
            Raytrace raytrace = new Raytrace(f.get(mc));
            String ie = raytrace.typeOfHit().toString();
            return ie.equals("ENTITY");
        } else return false;
    }

    private boolean hasTime() {
        float delay = ((Value) settings.get(1)).getValue();
        return timer.hasTime(delay * 80);
    }

    private boolean isValid(Object entity) {
        Class<?> ep = JClass.getClass("entityPlayer").get();
        Class<?> ea = JClass.getClass("entityAnimal").get();
        Class<?> em = JClass.getClass("entityMob").get();
        ArrayList<Active.Listing> mobs = ((Active) settings.get(2)).listings;
        boolean player = mobs.get(0).isActive() && ep.isInstance(entity);
        boolean animal = mobs.get(1).isActive() && ea.isInstance(entity);
        boolean mob = mobs.get(2).isActive() && em.isInstance(entity);
        return player || animal || mob;
    }

    private Rotation getRotation(Object entity, Rotation rot) {
        JClass e = JClass.getClass("entity");
        float yaw = (float) e.getField("rotationYaw").get(entity);
        float pitch = (float) e.getField("rotationPitch").get(entity);
        float cPitch = rot.pitch - pitch;
        float cYaw = yaw % 360.0F;
        float delta = rot.yaw - cYaw;
        if(180.0F < delta) delta -= 360.0F;
        else if(-180.0F > delta) delta += 360.0F;
        return new Rotation(pitch + cPitch, yaw + delta);
    }
}
