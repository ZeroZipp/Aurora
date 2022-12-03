package com.zerozipp.client.utils.base;

import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.types.Type;

@Aurora(Type.BASE)
@SuppressWarnings("unused")
public class Keybind {
    private Integer key = null;

    public Keybind(Integer key) {
        setKey(key);
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public void resetKey() {
        this.key = null;
    }

    public Integer getKey() {
        return key;
    }
}
