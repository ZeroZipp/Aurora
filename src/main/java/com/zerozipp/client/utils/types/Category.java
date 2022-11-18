package com.zerozipp.client.utils.types;

import com.zerozipp.client.utils.base.Module;
import com.zerozipp.client.utils.interfaces.Aurora;
import java.util.ArrayList;

@Aurora(Type.BASE)
@SuppressWarnings("unused")
public enum Category {
    MOVEMENT("Movement"),
    RENDER("Render"),
    GAME("Game");

    public final String name;
    public final ArrayList<Module> modules;
    private boolean opened;

    Category(String nameIn) {
        name = nameIn;
        modules = new ArrayList<>();
        opened = true;
    }

    public void setOpened(boolean active) {
        opened = active;
    }

    public boolean isClosed() {
        return !opened;
    }
}
