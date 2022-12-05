package com.zerozipp.client.mods.game;

import com.zerozipp.client.Invoker;
import com.zerozipp.client.utils.base.Module;
import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.reflect.JClass;
import com.zerozipp.client.utils.settings.Value;
import com.zerozipp.client.utils.types.Type;

@Aurora(Type.MODULE)
public class Timer extends Module {
    public Timer(String name, boolean active, Integer key) {
        super(name, active, key);
        settings.add(new Value("Speed", 25, 5, 50));
    }

    @Override
    public void onDisable() {
        super.onDisable();
        Object mc = Invoker.client.MC();
        JClass t = JClass.getClass("timer");
        JClass c = JClass.getClass("minecraft");
        Object timer = c.getDecField("gameTimer").get(mc);
        t.getDecField("tickLength").set(timer, 1000.0F / 20.0F);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        Object mc = Invoker.client.MC();
        JClass t = JClass.getClass("timer");
        JClass c = JClass.getClass("minecraft");
        Object timer = c.getDecField("gameTimer").get(mc);
        float speed = 1000.0F / ((Value) settings.get(0)).getValue();
        t.getDecField("tickLength").set(timer, speed);
    }
}
