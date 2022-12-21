package com.zerozipp.client.mods.game;

import com.zerozipp.client.Invoker;
import com.zerozipp.client.utils.Entity;
import com.zerozipp.client.utils.World;
import com.zerozipp.client.utils.base.Module;
import com.zerozipp.client.utils.base.Raytrace;
import com.zerozipp.client.utils.base.Rotating;
import com.zerozipp.client.utils.base.Stack;
import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.reflect.JClass;
import com.zerozipp.client.utils.reflect.JField;
import com.zerozipp.client.utils.reflect.JMethod;
import com.zerozipp.client.utils.settings.Toggle;
import com.zerozipp.client.utils.types.Events;
import com.zerozipp.client.utils.types.Facing;
import com.zerozipp.client.utils.types.Type;
import com.zerozipp.client.utils.utils.Rotation;
import com.zerozipp.client.utils.utils.Vector3;

@Aurora(Type.BASE)
public class Bridge extends Module {
    public Bridge(String name, boolean active, Integer key) {
        super(name, active, key);
        settings.add(new Toggle("Select", true));
        settings.add(new Toggle("Screen", false));
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        Object mc = Invoker.client.MC();
        JClass c = JClass.getClass("minecraft");
        JField screen = c.getField("guiScreen");
        Object world = c.getField("mcWorld").get(mc);
        Object g = Invoker.client.network.getRotation();
        boolean s = ((Toggle) settings.get(1)).isActive();
        if((!s && screen.get(mc) != null) || g != null) return;
        Object player = c.getField("mcPlayer").get(mc);
        Vector3 pos = Entity.getPosition(player).round();
        Vector3 eyes = Entity.getEyes(player, 1);
        Vector3 down = pos.add(0, -1, 0);
        Rotation rot = Entity.getRot(eyes, down);
        Rotation set = Entity.getRotation(player, rot);
        Invoker.client.network.setRotation(set.pitch, set.yaw);
        if(World.isReplaceable(world, down)) {
            if(!canPlace(world, player, down)) {
                for(Facing face : Facing.values()) {
                    Vector3 pos2 = down.add(face.pos);
                    if(canPlace(world, player, pos2)) {
                        return;
                    }
                }
            }
        }
    }

    private boolean canPlace(Object world, Object player, Vector3 pos) {
        Object mc = Invoker.client.MC();
        for(Facing face : Facing.values()) {
            Vector3 pos2 = pos.add(face.pos);
            boolean r = World.isReplaceable(world, pos2);
            if(!r) onPlace(player, mc, pos2, face);
            if(!r) onRotate(player, pos2, face);
            if(!r) return true;
        } return false;
    }

    private void onPlace(Object player, Object mc, Vector3 pos, Facing face) {
        JClass item = JClass.getClass("itemBlock");
        boolean s = ((Toggle) settings.get(0)).isActive();
        int index = Entity.getHeldIndex(player);
        Stack held = Entity.getHeldItem(player);
        if(!held.isInstance(item.get())) {
            int slot = getIndex(player);
            if(slot < 0 || !s) return;
            Entity.setHeldIndex(player, slot);
        }

        JClass r = JClass.getClass("renderer");
        JClass c = JClass.getClass("minecraft");
        JField f = c.getField("objectMouseOver");
        JMethod mouse = c.getDecMethod("rightClickMouse");
        JMethod m = r.getMethod("getMouseOver", float.class);
        Object render = c.getField("renderer").get(mc);
        Rotation last = Entity.getPacketRot(player);
        Object oldRaytraceHit = f.get(mc);
        Rotating.pushRotation(player);
        Entity.setRotation(player, last);
        m.call(render, 1.0F);
        Rotating.popRotation(player);
        Object block = pos.getBlock();
        Vector3 side = face.getOpposite();
        if(canPlace(block, side)) mouse.call(mc);
        f.set(mc, oldRaytraceHit);
        Entity.setHeldIndex(player, index);
    }

    private int getIndex(Object player) {
        JClass item = JClass.getClass("itemBlock");
        for(int i = 0; i < 9; i++) {
            Stack stack = Entity.getStackInSlot(player, i);
            if(stack.isInstance(item.get())) return i;
        } return -1;
    }

    private void onRotate(Object player, Vector3 pos, Facing face) {
        Vector3 o = face.getOpposite();
        float x = o.x / 2, y = o.y / 2, z = o.z / 2;
        Vector3 eyes = Entity.getEyes(player, 1);
        Rotation rot = Entity.getRot(eyes, pos.add(x, y, z));
        Rotation set = Entity.getRotation(player, rot);
        Invoker.client.network.setRotation(set.pitch, set.yaw);
    }

    private boolean canPlace(Object block, Vector3 side) {
        Object mc = Invoker.client.MC();
        JClass fa = JClass.getClass("facing");
        JClass c = JClass.getClass("minecraft");
        JField f = c.getField("objectMouseOver");
        JMethod m = fa.getMethod("sideGetVec");
        if(f.get(mc) != null) {
            Raytrace raytrace = new Raytrace(f.get(mc));
            String ie = raytrace.typeOfHit().toString();
            if(ie.equals("BLOCK")) {
                Object hit = raytrace.blockPos();
                Object s = m.call(raytrace.sideHit());
                Vector3 sHit = new Vector3(s);
                boolean isHit = side.equals(sHit);
                return block.equals(hit) && isHit;
            } else return false;
        } else return false;
    }

    @Override
    public boolean onEvent(Events event) {
        if(event == Events.SNEAKING) return true;
        return super.onEvent(event);
    }
}
