package com.zerozipp.client.utils;

import com.zerozipp.client.Invoker;
import com.zerozipp.client.utils.base.Packet;
import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.reflect.JClass;
import com.zerozipp.client.utils.reflect.JMethod;
import com.zerozipp.client.utils.types.Type;
import java.lang.reflect.Constructor;
import static com.zerozipp.client.utils.source.Classes.C;

@Aurora(Type.BASE)
@SuppressWarnings("unused")
public class Network {
    private Rotation rotation = null;
    private Rotation previous = null;

    public void setRotation(float pitch, float yaw) {
        rotation = new Rotation(pitch, yaw);
    }

    public void onReset() {
        previous = rotation;
        rotation = null;
    }

    public void onUpdate() {
        if(!isPrevious()) {
            Rotation rot = getRotation();
            if(rotation == null) sendPacket(rot.pitch, rot.yaw);
            else sendPacket(rotation.pitch, rotation.yaw);
        }
    }

    public boolean isPrevious() {
        if(previous == rotation) return true;
        if(previous == null) return false;
        if(rotation == null) return false;
        return rotation.equals(previous);
    }

    private Rotation getRotation() {
        Object mc = Invoker.client.MC();
        JClass e = JClass.getClass("entity");
        Object player = JClass.getClass("minecraft").getField("mcPlayer").get(mc);
        float x = (float) e.getField("rotationPitch").get(player);
        float y = (float) e.getField("rotationYaw").get(player);
        return new Rotation(x, y);
    }

    private void sendPacket(float pitch, float yaw) {
        JClass rot = JClass.getClass("cPlayerRot");
        Class<?> ht = JClass.getClass("packet").get();
        Constructor<?> c = rot.getConstructor(float.class, float.class, boolean.class);
        JMethod send = JClass.getClass("netHandler").getMethod("sendPacket", ht);
        Object player = JClass.getClass("minecraft").getField("mcPlayer").get(Invoker.client.MC());
        Object connection = JClass.getClass("player").getField("connection").get(player);
        Object ground = JClass.getClass("entity").getField("entityGround").get(player);
        send.call(connection, rot.newInstance(c, yaw, pitch, ground));
    }

    public void onPacket(Packet packet) {
        boolean player = packet.type.getName().equals(C.get("cPlayer"));
        if((player || packet.type.getName().startsWith(C.get("cPlayer") + "$")) && rotation != null) {
            packet.setValue("cPlayerPitch", rotation.pitch, true);
            packet.setValue("cPlayerYaw", rotation.yaw, true);
        }
    }
}
