package com.zerozipp.client.mods;

import com.zerozipp.client.Invoker;
import com.zerozipp.client.utils.Keybindings;
import com.zerozipp.client.utils.base.Module;
import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.reflect.JClass;
import com.zerozipp.client.utils.types.Type;

@Aurora(Type.MODULE)
public class Jump extends Module {
    public Jump(String name, boolean active, Integer key) {
        super(name, active, key);
    }

    @Override
    public void onDisable() {
        super.onDisable();
        Object mc = Invoker.client.MC();
        Object settings = JClass.getClass("minecraft").getField("mcSettings").get(mc);
        Object keyJump = JClass.getClass("settings").getField("keyJump").get(settings);
        if(keyJump != null) Keybindings.resetPressed(keyJump);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        Object mc = Invoker.client.MC();
        Object player = JClass.getClass("minecraft").getField("mcPlayer").get(mc);
        Object world = JClass.getClass("minecraft").getField("mcWorld").get(mc);
        Object settings = JClass.getClass("minecraft").getField("mcSettings").get(mc);
        Object ground = JClass.getClass("entity").getField("entityGround").get(player);
        Object keyJump = JClass.getClass("settings").getField("keyJump").get(settings);
        if(keyJump != null) Keybindings.resetPressed(keyJump);
        if(ground instanceof Boolean && (Boolean) ground) {
            JClass b = JClass.getClass("blockPos");
            Class<?> p = JClass.getClass("position").get();
            Class<?> s = JClass.getClass("blockState").get();
            Object pos = JClass.getClass("entity").getMethod("entityGetPos").call(player);
            Object blockPos = b.newInstance(b.getConstructor(p), pos);
            Object blockPosDown = JClass.getClass("blockPos").getMethod("blockPosDown").call(blockPos);
            Object blockState = JClass.getClass("world").getMethod("worldGetBlockState", b.get()).call(world, blockPosDown);
            Object block = JClass.getClass("blockState").getMethod("blockStateGetBlock").call(blockState);
            Object isTopSolid = JClass.getClass("block").getMethod("blockIsTopSolid", s).call(block, blockState);
            if(isTopSolid instanceof Boolean && !(Boolean) isTopSolid) {
                if(keyJump != null) Keybindings.setPressed(keyJump, true);
            }
        }
    }
}
