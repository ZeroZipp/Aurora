package com.zerozipp.client.mods;

import com.zerozipp.client.Invoker;
import com.zerozipp.client.utils.base.Module;
import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.reflect.JClass;
import com.zerozipp.client.utils.types.Type;

@Aurora(Type.MODULE)
public class Place extends Module {
    public Place(String name, boolean active, Integer key) {
        super(name, active, key);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        Object mc = Invoker.client.MC();
        JClass minecraft = JClass.getClass("minecraft");
        if((Integer) minecraft.getDecField("rightClickDelay").get(mc) != 0) {
            minecraft.getDecField("rightClickDelay").set(mc, 0);
        }
    }
}
