package com.zerozipp.client.mods.game;

import com.zerozipp.client.Invoker;
import com.zerozipp.client.utils.Entity;
import com.zerozipp.client.utils.Network;
import com.zerozipp.client.utils.utils.Rotation;
import com.zerozipp.client.utils.base.Rotating;
import com.zerozipp.client.utils.reflect.JField;
import com.zerozipp.client.utils.settings.Active;
import com.zerozipp.client.utils.settings.Toggle;
import com.zerozipp.client.utils.settings.Value;
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
public class Destroy extends Module {
    public Destroy(String name, boolean active, Integer key) {
        super(name, active, key);
        ArrayList<Active.Listing> list = new ArrayList<>();
        list.add(new Active.Listing("Bed", true));
        list.add(new Active.Listing("Chest", true));
        settings.add(new Value("Reach", 4, 2, 6));
        settings.add(new Active("Objects", list));
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
        boolean s = ((Toggle) settings.get(2)).isActive();
        if((!s && screen.get(mc) != null) || g != null) return;
        Object player = c.getField("mcPlayer").get(mc);
        Object world = c.getField("mcWorld").get(mc);
        Object entities = w.getField("loadedTileEntityList").get(world);
        ToDoubleFunction<Object> d = entity -> Entity.getTileDistance(player, entity);
        ArrayList<Object> entityList = (ArrayList<Object>) entities;
        float reach = ((Value) settings.get(0)).getValue();
        Vector3 pos = Entity.getEyes(player, 1.0f);
        entityList.sort(comparingDouble(d));
        for(Object entity : entityList) {
            if(!isValid(entity)) continue;
            if(Entity.getTileDistance(player, entity) < reach) {
                Vector3 eyes = Entity.getTilePos(entity);
                Rotation rot = Entity.getRot(pos, eyes);
                Rotation set = Entity.getRotation(player, rot);
                Rotation last = Entity.getPacketRot(player);
                Object block = Entity.getTileBlock(entity);
                if(canBreak(mc, player, block, set)) {
                    net.setRotation(set.pitch, set.yaw);
                    if(canBreak(mc, player, block, last)) {
                        onBreak(mc, player, last);
                    } break;
                }
            }
        }
    }

    private void onBreak(Object mc, Object player, Rotation rot) {
        Class<?> b = boolean.class;
        JClass c = JClass.getClass("minecraft");
        JMethod click = c.getDecMethod("sendClickBlock", b);
        JField f = c.getField("objectMouseOver");
        Object oldRaytraceHit = f.get(mc);
        f.set(mc, getRaytrace(mc, player, rot));
        click.call(mc, true);
        f.set(mc, oldRaytraceHit);
    }

    private boolean canBreak(Object mc, Object player, Object pos, Rotation rot) {
        Object cast = getRaytrace(mc, player, rot);
        if(cast == null) return false;
        Raytrace raytrace = new Raytrace(cast);
        String type = raytrace.typeOfHit().toString();
        if(!type.equals("BLOCK")) return false;
        return pos.equals(raytrace.blockPos());
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

    private boolean isValid(Object entity) {
        Class<?> eb = JClass.getClass("tileEntityBed").get();
        Class<?> ec = JClass.getClass("tileEntityChest").get();
        ArrayList<Active.Listing> mobs = ((Active) settings.get(1)).listings;
        boolean chest = mobs.get(1).isActive() && ec.isInstance(entity);
        boolean bed = mobs.get(0).isActive() && eb.isInstance(entity);
        return chest || bed;
    }
}
