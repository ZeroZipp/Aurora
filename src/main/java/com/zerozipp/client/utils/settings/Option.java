package com.zerozipp.client.utils.settings;

import com.zerozipp.client.utils.base.Setting;
import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.types.Type;

@Aurora(Type.BASE)
@SuppressWarnings("unused")
public class Option extends Setting {
    public final String[] values;
    private int index;

    public Option(String name, String[] values, int index) {
        super(name);
        this.values = values;
        setIndex(index);
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = Math.min(Math.max(index, 0), values.length - 1);
    }
}
