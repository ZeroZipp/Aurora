package com.zerozipp.client;

import com.zerozipp.client.mods.*;
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
        Category.MOVEMENT.modules.add(new Sneak("Sneak", false, 46));
        Category.MOVEMENT.modules.add(new Jump("Jump", false, 25));
        Category.GAME.modules.add(new Place("Place", false, 34));
        Category.GAME.modules.add(new Attack("Attack", false, 48));
        Category.RENDER.modules.add(new Outlines("Outlines", false, 24));
        Category.RENDER.modules.add(new Invisible("Invisible", false, 23));
        Category.RENDER.modules.add(new Camera("Camera", false, 47));
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
                Integer key = mod.keybinding.getKey();
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

    public boolean isEvent(Module mod, Events event) {
        return mod.isActive() && mod.onEvent(event);
    }
}
