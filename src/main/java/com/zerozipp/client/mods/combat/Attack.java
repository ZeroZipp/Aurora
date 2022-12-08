package com.zerozipp.client.mods.combat;

import com.zerozipp.client.Invoker;
import com.zerozipp.client.utils.Entity;
import com.zerozipp.client.utils.Rotation;
import com.zerozipp.client.utils.base.Rotating;
import com.zerozipp.client.utils.reflect.JField;
import com.zerozipp.client.utils.settings.Active;
import com.zerozipp.client.utils.settings.Option;
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
        ArrayList<Active.Listing> list = new ArrayList<>();
        list.add(new Active.Listing("Player", true));
        list.add(new Active.Listing("Animal", true));
        list.add(new Active.Listing("Mob", true));
        settings.add(new Option("Mode", new String[] {"Packet", "Mouse"}, 0));
        settings.add(new Value("Reach", 5, 2, 6));
        settings.add(new Value("Delay", 2, 1, 8));
        settings.add(new Toggle("Cast", true));
        settings.add(new Toggle("Wait", false));
        settings.add(new Active("Entity", list));
        settings.add(new Toggle("Screen", false));
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        Object mc = Invoker.client.MC();
        JClass c = JClass.getClass("minecraft");
        JField screen = c.getField("guiScreen");
        if(!((Toggle) settings.get(6)).isActive()) {
            if(screen.get(mc) != null) return;
        }

        JClass w = JClass.getClass("world");
        JClass con = JClass.getClass("controller");
        Object player = c.getField("mcPlayer").get(mc);
        Object world = c.getField("mcWorld").get(mc);
        Class<?> e = JClass.getClass("entity").get();
        Class<?> p = JClass.getClass("livingBase").get();
        Class<?> ep = JClass.getClass("entityPlayer").get();
        Object entities = w.getField("loadedEntityList").get(world);
        ToDoubleFunction<Object> d = entity -> Entity.getDistance(player, entity);
        ArrayList<Object> entityList = (ArrayList<Object>) entities;
        float reach = ((Value) settings.get(1)).getValue();
        entityList.sort(comparingDouble(d));
        Vector3 pos = Entity.getEyes(player);
        for(Object entity : entityList) {
            if(!isValid(entity)) continue;
            if(!p.isInstance(entity)) continue;
            if(!Entity.isLiving(entity)) continue;
            if(entity.equals(player)) continue;
            if(Entity.getDistance(player, entity) < reach) {
                Rotation rot = Entity.getRot(pos, Entity.getEyes(entity));
                float dist = (float) Entity.getDistance(player, entity);
                Raytrace trace = Entity.getCast(player, rot, dist);
                Rotation newRot = this.getRot(player, rot);
                boolean ray = ((Toggle) settings.get(3)).isActive();
                if((trace != null && trace.typeOfHit().toString().equals("MISS")) || !ray) {
                    Invoker.client.network.setRotation(newRot.pitch, newRot.yaw);
                    float delay = ((Value) settings.get(2)).getValue();
                    if(((Toggle) settings.get(4)).isActive()) {
                        JClass el = JClass.getClass("livingBase");
                        JField t = el.getDecField("ticksSwing");
                        if((int) t.get(player) <= 12) break;
                    } else if(!timer.hasTime(delay * 80)) break;
                    if(((Option) settings.get(0)).getIndex() == 0) {
                        JMethod a = con.getMethod("attackEntity", ep, e);
                        Object co = c.getField("controller").get(mc);
                        Invoker.client.network.onUpdate();
                        a.call(co, player, entity);
                        JClass h = JClass.getClass("hand");
                        sendPacket(h.getField("armMain").get(null));
                    } else if(((Option) settings.get(0)).getIndex() == 1) {
                        JClass r = JClass.getClass("renderer");
                        JField f = c.getField("objectMouseOver");
                        JMethod m = r.getMethod("getMouseOver", float.class);
                        Object er = c.getField("renderer").get(mc);
                        Object object = f.get(mc);
                        Rotating.pushRotation(player);
                        Entity.setPrevRotation(player, rot);
                        Entity.setRotation(player, rot);
                        m.call(er, 1.0F);
                        Rotating.popRotation(player);
                        c.getDecMethod("clickMouse").call(mc);
                        f.set(mc, object);
                    } break;
                }
            }
        }
    }

    private boolean isValid(Object entity) {
        Class<?> ep = JClass.getClass("entityPlayer").get();
        Class<?> ea = JClass.getClass("entityAnimal").get();
        Class<?> em = JClass.getClass("entityMob").get();
        ArrayList<Active.Listing> mobs = ((Active) settings.get(5)).listings;
        boolean player = mobs.get(0).isActive() && ep.isInstance(entity);
        boolean animal = mobs.get(1).isActive() && ea.isInstance(entity);
        boolean mob = mobs.get(2).isActive() && em.isInstance(entity);
        return player || animal || mob;
    }

    private Rotation getRot(Object entity, Rotation rot) {
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
