package com.zerozipp.client.utils.base;

import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.types.Type;

@Aurora(Type.BASE)
@SuppressWarnings("unused")
public class Setting {
    public final String name;

    public Setting(String name) {
        this.name = name;
    }

    public float getHeight() {
        return 11;
    }

    public void runTick() {}
}
