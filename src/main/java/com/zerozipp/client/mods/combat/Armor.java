package com.zerozipp.client.mods.combat;

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
public class Armor extends Module {
    private final Timer timer;

    public Armor(String name, boolean active, Integer key) {
        super(name, active, key);
        settings.add(new Value("Delay", 2, 1, 8));
        timer = new Timer();
    }

    @Override
    public void onUpdate() {
        super.onUpdate();
        Object mc = Invoker.client.MC();
        JClass m = JClass.getClass("minecraft");
        JField screen = m.getField("guiScreen");
        if(screen.get(mc) != null) return;
        JClass it = JClass.getClass("items");
        JClass base = JClass.getClass("livingBase");
        JClass eq = JClass.getClass("equipmentSlot");
        Object player = m.getField("mcPlayer").get(mc);
        JMethod his = base.getMethod("hasItemInSlot", eq.get());
        Enum<?>[] constants = (Enum<?>[]) eq.get().getEnumConstants();
        float value = ((Value) settings.get(0)).getValue();
        if(!timer.hasTime((value - 1) * 150)) return;
        for(Enum<?> slot : constants) {
            if(!(boolean) his.call(player, slot)) {
                ArrayList<Object> items = new ArrayList<>();
                if(slot.toString().equals("HEAD")) {
                    items.add(it.getField("itemsDiamondHelmet").get(null));
                    items.add(it.getField("itemsIronHelmet").get(null));
                    items.add(it.getField("itemsChainmailHelmet").get(null));
                    items.add(it.getField("itemsGoldenHelmet").get(null));
                    items.add(it.getField("itemsLeatherHelmet").get(null));
                    if(this.onClick(items)) return;
                } else if(slot.toString().equals("CHEST")) {
                    items.add(it.getField("itemsDiamondChestplate").get(null));
                    items.add(it.getField("itemsIronChestplate").get(null));
                    items.add(it.getField("itemsChainmailChestplate").get(null));
                    items.add(it.getField("itemsGoldenChestplate").get(null));
                    items.add(it.getField("itemsLeatherChestplate").get(null));
                    if(this.onClick(items)) return;
                } else if(slot.toString().equals("LEGS")) {
                    items.add(it.getField("itemsDiamondLeggings").get(null));
                    items.add(it.getField("itemsIronLeggings").get(null));
                    items.add(it.getField("itemsChainmailLeggings").get(null));
                    items.add(it.getField("itemsGoldenLeggings").get(null));
                    items.add(it.getField("itemsLeatherLeggings").get(null));
                    if(this.onClick(items)) return;
                } else if(slot.toString().equals("FEET")) {
                    items.add(it.getField("itemsDiamondBoots").get(null));
                    items.add(it.getField("itemsIronBoots").get(null));
                    items.add(it.getField("itemsChainmailBoots").get(null));
                    items.add(it.getField("itemsGoldenBoots").get(null));
                    items.add(it.getField("itemsLeatherBoots").get(null));
                    if(this.onClick(items)) return;
                }
            }
        }
    }

    public boolean onClick(ArrayList<Object> items) {
        Object mc = Invoker.client.MC();
        JClass s = JClass.getClass("slot");
        JClass c = JClass.getClass("container");
        JField is = c.getField("cInventorySlots");
        JMethod gs = s.getMethod("getStack");
        JClass m = JClass.getClass("minecraft");
        JClass ep = JClass.getClass("entityPlayer");
        JField ic = ep.getField("inventoryContainer");
        Object player = m.getField("mcPlayer").get(mc);
        JClass st = JClass.getClass("itemStack");
        JMethod si = st.getMethod("stackGetItem");
        Object slots = is.get(ic.get(player));
        ArrayList<Object> list = (ArrayList<Object>) slots;
        for(Object i : items) for(Object slot : list) {
            Object stack = gs.call(slot);
            boolean armor = i.equals(si.call(stack));
            if(armor) clickSlot(ic.get(player), slot, player);
            if(armor) return true;
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
