package com.zerozipp.client.utils.settings;

import com.zerozipp.client.utils.base.Setting;
import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.types.Type;

@Aurora(Type.BASE)
@SuppressWarnings("unused")
public class Value extends Setting {
    public final float min, max;
    private float value, last;

    public Value(String name, float value, float min, float max) {
        super(name);
        this.min = min;
        this.max = max;
        setValue(value);
    }

    @Override
    public float getHeight() {
        return 15;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = Math.min(Math.max(value, min), max);
    }

    @Override
    public void runTick() {
        this.last = this.value;
    }

    public float getLast() {
        return last;
    }
}
