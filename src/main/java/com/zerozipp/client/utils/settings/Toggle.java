package com.zerozipp.client.utils.settings;

import com.zerozipp.client.utils.base.Setting;
import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.types.Type;

@Aurora(Type.BASE)
@SuppressWarnings("unused")
public class Toggle extends Setting {
    private boolean active;

    public Toggle(String name, boolean active) {
        super(name);
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
