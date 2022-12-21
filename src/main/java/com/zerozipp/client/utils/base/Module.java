package com.zerozipp.client.utils.base;

import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.types.Events;
import com.zerozipp.client.utils.types.Type;
import java.util.ArrayList;

@Aurora(Type.BASE)
@SuppressWarnings("unused")
public class Module {
    private boolean active;
    private boolean opened;
    public final String name;
    public final Keybind keybind;
    public final ArrayList<Setting> settings;

    public Module(String name, boolean active, Integer key) {
        this.name = name;
        this.active = active;
        this.keybind = new Keybind(key);
        this.settings = new ArrayList<>();
        this.opened = false;
    }

    public boolean isActive() {
        return active;
    }

    public boolean isOpened() {
        return opened;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
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

    public void onOverlay(float ticks) {}

    public boolean onEvent(Events event) {
        return false;
    }

    public void onPacket(Packet packet) {}

    public void onRender(float ticks) {}
}
