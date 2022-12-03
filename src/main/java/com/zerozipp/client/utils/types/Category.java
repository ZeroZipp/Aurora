package com.zerozipp.client.utils.types;

import com.zerozipp.client.utils.base.Module;
import com.zerozipp.client.utils.interfaces.Aurora;
import java.util.ArrayList;

@Aurora(Type.BASE)
@SuppressWarnings("unused")
public enum Category {
    CAMERA("Camera"),
    SCREEN("Screen");

    public final String name;
    public final ArrayList<Module> modules;
    private boolean opened;
    private Integer index;

    Category(String nameIn) {
        name = nameIn;
        modules = new ArrayList<>();
        opened = true;
        index = null;
    }

    public Module getIndex() {
        if(index == null) return null;
        int size = modules.size() - 1;
        int offset = Math.min(Math.max(index, 0), size);
        return modules.get(offset);
    }

    public void setIndex(Integer index) {
        if(index != null) {
            int size = modules.size() - 1;
            index = Math.min(Math.max(index, 0), size);
        } this.index = index;
    }

    public void setOpened(boolean active) {
        opened = active;
    }

    public boolean isClosed() {
        return !opened;
    }
}
