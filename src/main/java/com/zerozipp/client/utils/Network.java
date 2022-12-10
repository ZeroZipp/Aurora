package com.zerozipp.client.utils;

import com.zerozipp.client.utils.base.Packet;
import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.types.Type;
import com.zerozipp.client.utils.utils.Rotation;
import static com.zerozipp.client.utils.source.Classes.C;

@Aurora(Type.BASE)
@SuppressWarnings("unused")
public class Network {
    private Rotation rotation = null;
    private Rotation previous = null;

    public void setRotation(float pitch, float yaw) {
        rotation = new Rotation(pitch, yaw);
    }

    public Rotation getRotation() {
        return rotation;
    }

    public Rotation getPrevious() {
        return previous;
    }

    public void onReset() {
        previous = rotation;
        rotation = null;
    }

    public void onPacket(Packet packet) {
        boolean player = packet.type.getName().equals(C.get("cPlayer"));
        if((player || packet.type.getName().startsWith(C.get("cPlayer") + "$")) && rotation != null) {
            packet.setValue("cPlayerPitch", rotation.pitch, true);
            packet.setValue("cPlayerYaw", rotation.yaw, true);
        }
    }
}
