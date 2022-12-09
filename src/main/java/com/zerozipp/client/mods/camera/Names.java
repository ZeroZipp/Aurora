package com.zerozipp.client.mods.camera;

import com.zerozipp.client.Client;
import com.zerozipp.client.Invoker;
import com.zerozipp.client.utils.Entity;
import com.zerozipp.client.utils.Renderer;
import com.zerozipp.client.utils.base.Module;
import com.zerozipp.client.utils.font.Render;
import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.reflect.JClass;
import com.zerozipp.client.utils.settings.Active;
import com.zerozipp.client.utils.settings.Value;
import com.zerozipp.client.utils.types.Events;
import com.zerozipp.client.utils.types.Type;
import com.zerozipp.client.utils.utils.Vector3;
import java.util.ArrayList;
import java.util.Collections;
import java.util.function.ToDoubleFunction;
import static java.util.Comparator.comparingDouble;

@Aurora(Type.MODULE)
@SuppressWarnings("unchecked")
public class Names extends Module {
    private Render font = null;

    public Names(String name, boolean active, Integer key) {
        super(name, active, key);
        ArrayList<Active.Listing> list = new ArrayList<>();
        list.add(new Active.Listing("Player", true));
        list.add(new Active.Listing("Animal", true));
        list.add(new Active.Listing("Mob", true));
        settings.add(new Value("Size", 10, 5, 13));
        settings.add(new Active("Entity", list));
    }

    @Override
    public boolean onEvent(Events event) {
        if(event == Events.NAMES) return true;
        return super.onEvent(event);
    }

    @Override
    public void onRender(float ticks) {
        super.onRender(ticks);
        String m = "font/medium.ttf";
        Object mc = Invoker.client.MC();
        JClass w = JClass.getClass("world");
        JClass e = JClass.getClass("entity");
        JClass c = JClass.getClass("minecraft");
        if(font == null) font = Client.getFont(m, 30);
        Object world = c.getField("mcWorld").get(mc);
        Class<?> p = JClass.getClass("livingBase").get();
        Object player = c.getMethod("getViewEntity").call(mc);
        Object entities = w.getField("loadedEntityList").get(world);
        ToDoubleFunction<Object> d = ent -> Entity.getDistance(player, ent);
        float h = (float) e.getMethod("getEyeHeight").call(player);
        ArrayList<Object> entityList = (ArrayList<Object>) entities;
        Vector3 pos = Entity.getEyes(player, ticks);
        entityList.sort(comparingDouble(d));
        Collections.reverse(entityList);
        for(Object entity : entityList) {
            if(!isValid(entity)) continue;
            if(!p.isInstance(entity)) continue;
            if(entity.equals(player)) continue;
            Vector3 eyes = Entity.getEyes(entity, ticks);
            Vector3 position = eyes.add(0, h + 0.5f, 0);
            position = position.add(-pos.x, -pos.y, -pos.z);
            float size = ((Value) settings.get(0)).getValue();
            String n = (String) e.getMethod("getEntityName").call(entity);
            float dist = (float) Entity.getDistance(player, entity) * 0.002f;
            Renderer.drawNameplate(player, n, position, font, size * 0.001f + dist);
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
}
