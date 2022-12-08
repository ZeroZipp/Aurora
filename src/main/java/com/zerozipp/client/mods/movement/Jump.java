package com.zerozipp.client.mods.movement;

import com.zerozipp.client.Invoker;
import com.zerozipp.client.utils.base.Module;
import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.reflect.JClass;
import com.zerozipp.client.utils.reflect.JField;
import com.zerozipp.client.utils.settings.Value;
import com.zerozipp.client.utils.types.Type;

@Aurora(Type.MODULE)
public class Jump extends Module {
    public Jump(String name, boolean active, Integer key) {
        super(name, active, key);
        settings.add(new Value("Factor", 4, 1, 8));
        setActive(false);
    }

    @Override
    public void onEnable() {
        super.onEnable();
        setActive(false);
        Object mc = Invoker.client.MC();
        JClass entity = JClass.getClass("entity");
        JField motionX = entity.getField("motionX");
        JField motionY = entity.getField("motionY");
        JField motionZ = entity.getField("motionZ");
        JClass minecraft = JClass.getClass("minecraft");
        JField rotationYaw = entity.getField("rotationYaw");
        Object player = minecraft.getField("mcPlayer").get(mc);
        JField ground = entity.getField("entityGround");
        if(!(boolean) ground.get(player)) return;
        float speed = ((Value) settings.get(0)).getValue() * 0.2f;
        float yaw = (float) rotationYaw.get(player);
        double cos = Math.cos(Math.toRadians(yaw + 90.0F));
        double sin = Math.sin(Math.toRadians(yaw + 90.0F));
        motionX.set(player, speed * cos);
        motionY.set(player, speed / 2);
        motionZ.set(player, speed * sin);
    }
}
