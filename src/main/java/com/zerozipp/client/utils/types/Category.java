package com.zerozipp.client.utils.types;

import com.zerozipp.client.utils.base.Module;
import com.zerozipp.client.utils.interfaces.Aurora;
import java.util.ArrayList;

@Aurora(Type.BASE)
@SuppressWarnings("unused")
public enum Category {
    MOVEMENT("Movement"),
    CAMERA("Camera"),
    GAME("Game"),
    COMBAT("Combat"),
    SCREEN("Screen");

    public final String name;
    public final ArrayList<Module> modules;

    Category(String nameIn) {
        name = nameIn;
        modules = new ArrayList<>();
    }
}
