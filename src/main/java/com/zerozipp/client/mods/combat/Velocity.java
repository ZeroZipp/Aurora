package com.zerozipp.client.mods.combat;

import com.zerozipp.client.utils.base.Module;
import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.types.Events;
import com.zerozipp.client.utils.types.Type;

@Aurora(Type.MODULE)
public class Velocity extends Module {
    public Velocity(String name, boolean active, Integer key) {
        super(name, active, key);
    }

    @Override
    public boolean onEvent(Events event) {
        if(event == Events.VELOCITY) return true;
        return super.onEvent(event);
    }
}
