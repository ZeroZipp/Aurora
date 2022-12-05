package com.zerozipp.client.mods.combat;

import com.zerozipp.client.Invoker;
import com.zerozipp.client.utils.Entity;
import com.zerozipp.client.utils.base.Module;
import com.zerozipp.client.utils.base.Raytrace;
import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.reflect.JClass;
import com.zerozipp.client.utils.settings.Value;
import com.zerozipp.client.utils.types.Type;
import com.zerozipp.client.utils.utils.Timer;

@Aurora(Type.MODULE)
public class Trigger extends Module {
    private final Timer timer;

    public Trigger(String name, boolean active, Integer key) {
        super(name, active, key);
        timer = new Timer();
        settings.add(new Value("Delay", 2, 1, 8));
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        Object mc = Invoker.client.MC();
        JClass c = JClass.getClass("minecraft");
        Class<?> p = JClass.getClass("livingBase").get();
        Object object = c.getField("objectMouseOver").get(mc);
        float delay = ((Value) settings.get(0)).getValue();
        assert object != null;
        Raytrace ray = new Raytrace(object);
        if(ray.typeOfHit().toString().equals("ENTITY")) {
            if(!timer.hasTime(delay * 80)) return;
            if(!p.isInstance(ray.entityHit())) return;
            assert Entity.isLiving(ray.entityHit());
            c.getDecMethod("clickMouse").call(mc);
        }
    }
}
