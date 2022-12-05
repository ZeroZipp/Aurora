package com.zerozipp.client.mods.movement;

import com.zerozipp.client.Invoker;
import com.zerozipp.client.utils.Keybinds;
import com.zerozipp.client.utils.base.Module;
import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.reflect.JClass;
import com.zerozipp.client.utils.types.Type;

@Aurora(Type.MODULE)
public class Sprint extends Module {
    public Sprint(String name, boolean active, Integer key) {
        super(name, active, key);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        Object mc = Invoker.client.MC();
        Object settings = JClass.getClass("minecraft").getField("mcSettings").get(mc);
        Object keySprint = JClass.getClass("settings").getField("keySprint").get(settings);
        if(keySprint != null) Keybinds.resetPressed(keySprint);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        Object mc = Invoker.client.MC();
        Object settings = JClass.getClass("minecraft").getField("mcSettings").get(mc);
        Object keySprint = JClass.getClass("settings").getField("keySprint").get(settings);
        if(keySprint != null) Keybinds.setPressed(keySprint, true);
    }
}
