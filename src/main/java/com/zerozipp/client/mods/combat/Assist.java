package com.zerozipp.client.mods.combat;

import com.zerozipp.client.Invoker;
import com.zerozipp.client.utils.Entity;
import com.zerozipp.client.utils.utils.Rotation;
import com.zerozipp.client.utils.base.Module;
import com.zerozipp.client.utils.base.Raytrace;
import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.reflect.JClass;
import com.zerozipp.client.utils.reflect.JField;
import com.zerozipp.client.utils.settings.Active;
import com.zerozipp.client.utils.settings.Toggle;
import com.zerozipp.client.utils.settings.Value;
import com.zerozipp.client.utils.types.Type;
import com.zerozipp.client.utils.utils.Vector3;
import java.util.ArrayList;

@Aurora(Type.MODULE)
public class Assist extends Module {
    public Assist(String name, boolean active, Integer key) {
        super(name, active, key);
        ArrayList<Active.Listing> list = new ArrayList<>();
        list.add(new Active.Listing("Player", true));
        list.add(new Active.Listing("Animal", true));
        list.add(new Active.Listing("Mob", true));
        settings.add(new Value("Delay", 2, 1, 3));
        settings.add(new Active("Entity", list));
        settings.add(new Toggle("Invisible", true));
        settings.add(new Toggle("Screen", false));
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        Object mc = Invoker.client.MC();
        JClass c = JClass.getClass("minecraft");
        JField screen = c.getField("guiScreen");
        if(!((Toggle) settings.get(3)).isActive()) {
            if(screen.get(mc) != null) return;
        }

        Object player = c.getField("mcPlayer").get(mc);
        Class<?> p = JClass.getClass("livingBase").get();
        Object object = c.getField("objectMouseOver").get(mc);
        boolean in = ((Toggle) settings.get(2)).isActive();
        Vector3 pos = Entity.getEyes(player, 1.0f);
        if(object == null) return;
        Raytrace ray = new Raytrace(object);
        if(ray.typeOfHit().toString().equals("ENTITY")) {
            if(!isValid(ray.entityHit())) return;
            if(!p.isInstance(ray.entityHit())) return;
            if(!Entity.isLiving(ray.entityHit())) return;
            if(!in && Entity.isInvisible(ray.entityHit())) return;
            Vector3 eyes = Entity.getEyes(ray.entityHit(), 1.0f);
            Rotation rot = Entity.getRot(pos, eyes);
            this.onRotate(player, rot);
        }
    }

    private boolean isValid(Object entity) {
        Class<?> ep = JClass.getClass("entityPlayer").get();
        Class<?> ea = JClass.getClass("entityAnimal").get();
        Class<?> em = JClass.getClass("entityMob").get();
        ArrayList<Active.Listing> mobs = ((Active) settings.get(1)).listings;
        boolean player = mobs.get(0).isActive() && ep.isInstance(entity);
        boolean animal = mobs.get(1).isActive() && ea.isInstance(entity);
        boolean mob = mobs.get(2).isActive() && em.isInstance(entity);
        return player || animal || mob;
    }

    private void onRotate(Object entity, Rotation rot) {
        JClass e = JClass.getClass("entity");
        float yaw = (float) e.getField("rotationYaw").get(entity);
        float pitch = (float) e.getField("rotationPitch").get(entity);
        float delay = ((Value) this.settings.get(0)).getValue();
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
