package com.zerozipp.client.mods.camera;

import com.zerozipp.client.Invoker;
import com.zerozipp.client.utils.base.Module;
import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.reflect.JClass;
import com.zerozipp.client.utils.settings.*;
import com.zerozipp.client.utils.types.Events;
import com.zerozipp.client.utils.types.Type;

@Aurora(Type.MODULE)
public class Camera extends Module {
    public Camera(String name, boolean active, Integer key) {
        super(name, active, key);
        settings.add(new Toggle("Swing", true));
        settings.add(new Toggle("Hurt", true));
    }

    @Override
    public boolean onEvent(Events event) {
        boolean bob = ((Toggle) settings.get(0)).isActive();
        boolean hurt = ((Toggle) settings.get(1)).isActive();
        if(event == Events.BOBBLING && hurt) return true;
        if(event == Events.OFFSET && bob) renderHand();
        if(event == Events.HURT && hurt) return true;
        if(event == Events.CAMERA) return true;
        return super.onEvent(event);
    }

    private void renderHand() {
        Object mc = Invoker.client.MC();
        JClass p = JClass.getClass("player");
        JClass e = JClass.getClass("entity");
        Object player = JClass.getClass("minecraft").getField("mcPlayer").get(mc);
        p.getField("prevRenderArmPitch").set(player, e.getField("rotationPitch").get(player));
        p.getField("prevRenderArmYaw").set(player, e.getField("rotationYaw").get(player));
        p.getField("renderArmPitch").set(player, e.getField("rotationPitch").get(player));
        p.getField("renderArmYaw").set(player, e.getField("rotationYaw").get(player));
    }
}
