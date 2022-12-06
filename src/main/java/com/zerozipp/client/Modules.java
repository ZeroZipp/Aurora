package com.zerozipp.client;

import com.zerozipp.client.mods.camera.*;
import com.zerozipp.client.mods.combat.*;
import com.zerozipp.client.mods.game.*;
import com.zerozipp.client.mods.movement.*;
import com.zerozipp.client.mods.screen.*;
import com.zerozipp.client.utils.base.Packet;
import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.types.Category;
import com.zerozipp.client.utils.base.Module;
import com.zerozipp.client.utils.types.Events;
import com.zerozipp.client.utils.types.Type;
import org.lwjgl.input.Keyboard;
import java.util.ArrayList;

@Aurora(Type.BASE)
@SuppressWarnings("unused")
public class Modules {
    public Modules() {
        Category.MOVEMENT.modules.add(new Sneak("Sneak", false, Keyboard.KEY_C));
        Category.MOVEMENT.modules.add(new Flight("Flight", false, Keyboard.KEY_G));
        Category.MOVEMENT.modules.add(new Sprint("Sprint", true, null));
        Category.CAMERA.modules.add(new Camera("Camera", true, null));
        Category.CAMERA.modules.add(new Bright("Bright", true, null));
        Category.CAMERA.modules.add(new Outlines("Outlines", false, null));
        Category.CAMERA.modules.add(new Invisible("Invisible", false, null));
        Category.COMBAT.modules.add(new Attack("Attack", false, Keyboard.KEY_V));
        Category.COMBAT.modules.add(new Rotate("Rotate", false, null));
        Category.COMBAT.modules.add(new Trigger("Trigger", false, null));
        Category.GAME.modules.add(new Place("Place", false, null));
        Category.GAME.modules.add(new Ground("Ground", false, null));
        Category.GAME.modules.add(new Blink("Blink", false, Keyboard.KEY_B));
        Category.GAME.modules.add(new Timer("Timer", false, Keyboard.KEY_X));
        Category.SCREEN.modules.add(new Name("Name", true, null));
        Category.SCREEN.modules.add(new Keypad("Keypad", true, null));
        Category.SCREEN.modules.add(new Position("Position", true, null));
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

    public ArrayList<Module> getActive() {
        ArrayList<Module> mods = new ArrayList<>();
        for(Category category : Category.values()) {
            ArrayList<Module> modules = category.modules;
            for(Module mod : modules) if(mod.isActive()) mods.add(mod);
        } return mods;
    }
}
