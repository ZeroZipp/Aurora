package com.zerozipp.client.mods.combat;

import com.zerozipp.client.Invoker;
import com.zerozipp.client.utils.base.Module;
import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.reflect.JClass;
import com.zerozipp.client.utils.reflect.JField;
import com.zerozipp.client.utils.settings.Value;
import com.zerozipp.client.utils.types.Type;
import org.lwjgl.input.Keyboard;

@Aurora(Type.MODULE)
public class Strafe extends Module {
    public Strafe(String name, boolean active, Integer key) {
        super(name, active, key);
        settings.add(new Value("Factor", 2, 1, 3));
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        Object mc = Invoker.client.MC();
        JClass entity = JClass.getClass("entity");
        JField motionX = entity.getField("motionX");
        JField motionZ = entity.getField("motionZ");
        JClass minecraft = JClass.getClass("minecraft");
        JField rotationYaw = entity.getField("rotationYaw");
        Object player = minecraft.getField("mcPlayer").get(mc);
        JField screen = minecraft.getField("guiScreen");
        float speed = ((Value) settings.get(0)).getValue() * 0.1f;
        motionX.set(player, 0.0f);
        motionZ.set(player, 0.0f);
        float forward = 0.0f, strafe = 0.0f;
        if(screen.get(Invoker.client.MC()) == null) {
            if(Keyboard.isKeyDown(Keyboard.KEY_W)) forward += speed;
            if(Keyboard.isKeyDown(Keyboard.KEY_S)) forward -= speed;
            if(Keyboard.isKeyDown(Keyboard.KEY_A)) strafe += speed;
            if(Keyboard.isKeyDown(Keyboard.KEY_D)) strafe -= speed;
            float yaw = (float) rotationYaw.get(player);
            double cos = Math.cos(Math.toRadians(yaw + 90.0F));
            double sin = Math.sin(Math.toRadians(yaw + 90.0F));
            motionX.set(player, forward * cos + strafe * sin);
            motionZ.set(player, forward * sin - strafe * cos);
        }
    }
}
