package com.zerozipp.client.mods.game;

import com.zerozipp.client.Invoker;
import com.zerozipp.client.utils.reflect.JMethod;
import com.zerozipp.client.utils.settings.Value;
import com.zerozipp.client.utils.utils.Timer;
import com.zerozipp.client.utils.base.Module;
import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.reflect.JClass;
import com.zerozipp.client.utils.reflect.JField;
import com.zerozipp.client.utils.types.Type;
import java.util.ArrayList;

@Aurora(Type.BASE)
@SuppressWarnings("unchecked")
public class Stealer extends Module {
    private final Timer timer;

    public Stealer(String name, boolean active, Integer key) {
        super(name, active, key);
        settings.add(new Value("Delay", 2, 1, 8));
        timer = new Timer();
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        JClass mc = JClass.getClass("minecraft");
        Class<?> gc = JClass.getClass("guiChest").get();
        Class<?> gs = JClass.getClass("guiShulker").get();
        float value = ((Value) settings.get(0)).getValue();
        JField s = mc.getField("guiScreen");
        if(s.get(Invoker.client.MC()) != null) {
            Object screen = s.get(Invoker.client.MC());
            if(!timer.hasTime((value - 1) * 150)) return;
            if(gc.isInstance(screen)) lootScreen(screen);
            if(gs.isInstance(screen)) lootScreen(screen);
        }
    }

    private void lootScreen(Object screen) {
        Object mc = Invoker.client.MC();
        JClass s = JClass.getClass("slot");
        JClass c = JClass.getClass("container");
        JClass st = JClass.getClass("itemStack");
        JClass gc = JClass.getClass("guiContainer");
        JClass m = JClass.getClass("minecraft");
        JField f = gc.getField("inventorySlots");
        JField is = c.getField("cInventorySlots");
        JMethod se = st.getMethod("stackEmpty");
        JMethod gs = s.getMethod("getStack");
        Object slots = is.get(f.get(screen));
        ArrayList<Object> list = (ArrayList<Object>) slots;
        Object player = m.getField("mcPlayer").get(mc);
        for(Object slot : list) {
            Object stack = gs.call(slot);
            boolean inv = isInventory(slot, player);
            boolean empty = (boolean) se.call(stack);
            if(!empty && !inv) clickSlot(f.get(screen), slot, player);
            if(!empty && !inv) return;
        }
    }

    private boolean isInventory(Object slot, Object player) {
        JClass s = JClass.getClass("slot");
        JClass c = JClass.getClass("container");
        JField is = c.getField("cInventorySlots");
        JMethod gs = s.getMethod("getStack");
        JClass ep = JClass.getClass("entityPlayer");
        JField ic = ep.getField("inventoryContainer");
        Object slots = is.get(ic.get(player));
        ArrayList<Object> list = (ArrayList<Object>) slots;
        for(Object invSlot : list) {
            Object stack = gs.call(slot);
            Object iStack = gs.call(invSlot);
            if(stack.equals(iStack)) return true;
        } return false;
    }

    private void clickSlot(Object container, Object slot, Object player) {
        Object mc = Invoker.client.MC();
        JClass s = JClass.getClass("slot");
        JClass cc = JClass.getClass("container");
        JClass ep = JClass.getClass("entityPlayer");
        JClass ct = JClass.getClass("clickType");
        Class<?> i = int.class, t = ct.get(), p = ep.get();
        JClass m = JClass.getClass("minecraft");
        JField f = m.getField("controller");
        JClass c = JClass.getClass("controller");
        Object type = ct.getField("quickMove").get(null);
        int num = (int) s.getField("slotNumber").get(slot);
        int id = (int) cc.getField("windowId").get(container);
        JMethod click = c.getMethod("windowClick", i, i, i, t, p);
        click.call(f.get(mc), id, num, 0, type, player);
    }
}
