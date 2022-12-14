package com.zerozipp.client.mods.movement;

import com.zerozipp.client.utils.base.Module;
import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.types.Events;
import com.zerozipp.client.utils.types.Type;

@Aurora(Type.MODULE)
public class Sneak extends Module {
    public Sneak(String name, boolean active, Integer key) {
        super(name, active, key);
    }

    @Override
    public boolean onEvent(Events event) {
        if(event == Events.SNEAKING) return true;
        return super.onEvent(event);
    }
}