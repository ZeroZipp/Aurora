package com.zerozipp.client.mods.camera;

import com.zerozipp.client.Invoker;
import com.zerozipp.client.utils.Entity;
import com.zerozipp.client.utils.Renderer;
import com.zerozipp.client.utils.base.Module;
import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.reflect.JClass;
import com.zerozipp.client.utils.reflect.JField;
import com.zerozipp.client.utils.settings.Active;
import com.zerozipp.client.utils.settings.Value;
import com.zerozipp.client.utils.types.Events;
import com.zerozipp.client.utils.types.Type;
import com.zerozipp.client.utils.utils.Color;
import com.zerozipp.client.utils.utils.Vector3;
import java.util.ArrayList;
import java.util.Collections;
import java.util.function.ToDoubleFunction;
import static java.util.Comparator.comparingDouble;

@Aurora(Type.MODULE)
@SuppressWarnings("unchecked")
public class Tracers extends Module {
    public Tracers(String name, boolean active, Integer key) {
        super(name, active, key);
        ArrayList<Active.Listing> list = new ArrayList<>();
        list.add(new Active.Listing("Player", true));
        list.add(new Active.Listing("Animal", true));
        list.add(new Active.Listing("Mob", true));
        settings.add(new Value("Size", 1, 1, 3));
        settings.add(new Active("Entity", list));
    }

    @Override
    public void onRender(float ticks) {
        super.onRender(ticks);
        Object mc = Invoker.client.MC();
        JClass w = JClass.getClass("world");
        JClass e = JClass.getClass("entity");
        JClass s = JClass.getClass("settings");
        JClass c = JClass.getClass("minecraft");
        JField v = s.getField("thirdPersonView");
        JField setting = c.getField("mcSettings");
        Object world = c.getField("mcWorld").get(mc);
        Class<?> p = JClass.getClass("livingBase").get();
        Object player = c.getMethod("getViewEntity").call(mc);
        Object entities = w.getField("loadedEntityList").get(world);
        ToDoubleFunction<Object> d = ent -> Entity.getDistance(player, ent);
        ArrayList<Object> entityList = (ArrayList<Object>) entities;
        float h = (float) e.getMethod("getEyeHeight").call(player);
        Vector3 pos = Entity.getEyes(player, ticks);
        entityList.sort(comparingDouble(d));
        Collections.reverse(entityList);
        for(Object entity : entityList) {
            if(!isValid(entity)) continue;
            if(!p.isInstance(entity)) continue;
            if(entity.equals(player)) continue;
            Vector3 start = Entity.getLook(player, ticks);
            boolean t = (int) v.get(setting.get(mc)) == 2;
            if(t) start = new Vector3(-start.x, -start.y, -start.z);
            start = start.add(new Vector3(0, h, 0));
            Vector3 eyes = Entity.getEyes(entity, ticks);
            Vector3 position = eyes.add(0, h, 0);
            position = position.add(-pos.x, -pos.y, -pos.z);
            Color color = new Color(255, 255, 255, 255);
            float size = ((Value) settings.get(0)).getValue();
            Renderer.drawTracer(start, position, color, size);
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

    @Override
    public boolean onEvent(Events event) {
        if(event == Events.NAMES) return true;
        if(event == Events.BOBBLING) return true;
        return super.onEvent(event);
    }
}
