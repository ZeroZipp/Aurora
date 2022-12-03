package com.zerozipp.client;

import com.zerozipp.client.mods.*;
import com.zerozipp.client.utils.base.Packet;
import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.types.Category;
import com.zerozipp.client.utils.base.Module;
import com.zerozipp.client.utils.types.Events;
import com.zerozipp.client.utils.types.Type;
import java.util.ArrayList;

@Aurora(Type.BASE)
@SuppressWarnings("unused")
public class Modules {
    public Modules() {
        Category.CAMERA.modules.add(new Camera("Camera", true, null));
        Category.CAMERA.modules.add(new Bright("Bright", true, null));
        Category.SCREEN.modules.add(new Name("Name", true, null));
        Category.SCREEN.modules.add(new Keypad("Keypad", true, null));
    }

    public void onUpdate() {
        for(Category category : Category.values()) {
            ArrayList<Module> modules = category.modules;
            for(Module mod : modules) {
                if(mod.isActive()) mod.onUpdate();
            }
        }
    }

    public void onKey(Integer code) {
        for(Category category : Category.values()) {
            ArrayList<Module> modules = category.modules;
            for(Module mod : modules) {
                Integer key = mod.keybind.getKey();
                if(key != null && key.equals(code)) {
                    mod.setActive(!mod.isActive());
                }
            }
        }
    }

    public void onOverlay() {
        for(Category category : Category.values()) {
            ArrayList<Module> modules = category.modules;
            for(Module mod : modules) {
                if(mod.isActive()) mod.onOverlay();
            }
        }
    }

    public boolean onEvent(Events event) {
        for(Category category : Category.values()) {
            ArrayList<Module> modules = category.modules;
            for(Module mod : modules) if(isEvent(mod, event)) return true;
        } return false;
    }

    public Packet onPacket(Packet packet) {
        for(Category category : Category.values()) {
            ArrayList<Module> modules = category.modules;
            for(Module mod : modules) if(mod.isActive()) mod.onPacket(packet);
        } return packet;
    }

    public boolean isEvent(Module mod, Events event) {
        return mod.isActive() && mod.onEvent(event);
    }
}
