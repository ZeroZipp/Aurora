package com.zerozipp.client.utils.base;

import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.types.Events;
import com.zerozipp.client.utils.types.Type;

@Aurora(Type.BASE)
@SuppressWarnings("unused")
public class Module {
    private boolean active;
    public final String name;
    public final Keybinding keybinding;

    public Module(String name, boolean active, Integer key) {
        this.name = name;
        this.active = active;
        this.keybinding = new Keybinding(key);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        if(this.active != active) {
            this.active = active;
            if(this.active) onEnable();
            else onDisable();
        }
    }

    public void onEnable() {}

    public void onUpdate() {}

    public void onDisable() {}

    public void onOverlay() {}

    public boolean onEvent(Events event) {
        return false;
    }
}
