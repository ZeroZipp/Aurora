package com.zerozipp.client.mods;

import com.zerozipp.client.utils.base.Module;
import com.zerozipp.client.utils.base.Packet;
import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.types.Type;
import static com.zerozipp.client.utils.source.Classes.C;

@Aurora(Type.MODULE)
public class Ground extends Module {
    public Ground(String name, boolean active, Integer key) {
        super(name, active, key);
    }

    @Override
    public void onPacket(Packet packet) {
        if(packet.type.getName().equals(C.get("cPlayer"))) setValue(packet);
        if(packet.type.getName().startsWith(C.get("cPlayer") + "$")) setValue(packet);
        super.onPacket(packet);
    }

    private void setValue(Packet packet) {
        packet.setValue("cPlayerGround", true, true);
    }
}
