package com.zerozipp.client.mods;

import com.zerozipp.client.Invoker;
import com.zerozipp.client.utils.Keybindings;
import com.zerozipp.client.utils.base.Module;
import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.reflect.JClass;
import com.zerozipp.client.utils.reflect.JField;
import com.zerozipp.client.utils.types.Type;

@Aurora(Type.MODULE)
public class Attack extends Module {
    public Attack(String name, boolean active, Integer key) {
        super(name, active, key);
    }

    public void onUpdate() {
        Object mc = Invoker.client.MC();
        Object settings = JClass.getClass("minecraft").getField("mcSettings").get(mc);
        Object attack = JClass.getClass("settings").getField("keyAttack").get(settings);
        JField pressTime = JClass.getClass("keyBind").getDecField("keyPressTime");
        if(Keybindings.isPressed(attack) && (Integer) pressTime.get(attack) != 1) {
            JClass.getClass("keyBind").getDecField("keyPressTime").set(attack, 1);
        }
    }
}
