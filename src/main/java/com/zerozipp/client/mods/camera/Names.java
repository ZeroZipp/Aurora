package com.zerozipp.client.mods.camera;

import com.zerozipp.client.utils.base.Module;
import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.settings.Active;
import com.zerozipp.client.utils.types.Events;
import com.zerozipp.client.utils.types.Type;
import java.util.ArrayList;

@Aurora(Type.MODULE)
public class Names extends Module {
    public Names(String name, boolean active, Integer key) {
        super(name, active, key);
        ArrayList<Active.Listing> list = new ArrayList<>();
        list.add(new Active.Listing("Player", true));
        list.add(new Active.Listing("Animal", true));
        list.add(new Active.Listing("Mob", true));
        list.add(new Active.Listing("Other", true));
        settings.add(new Active("Entity", list));
    }

    @Override
    public boolean onEvent(Events event) {
        if(event == Events.NAMES) return true;
        return super.onEvent(event);
    }
}
