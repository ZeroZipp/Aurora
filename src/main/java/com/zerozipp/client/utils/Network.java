package com.zerozipp.client.utils;

import com.zerozipp.client.Invoker;
import com.zerozipp.client.utils.base.Packet;
import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.reflect.JClass;
import com.zerozipp.client.utils.reflect.JMethod;
import com.zerozipp.client.utils.types.Type;
import com.zerozipp.client.utils.utils.Rotation;
import com.zerozipp.client.utils.utils.Vector3;

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

    public void onUpdate() {
        if(!isPrevious()) {
            Object mc = Invoker.client.MC();
            JClass c = JClass.getClass("minecraft");
            Object player = c.getField("mcPlayer").get(mc);
            Rotation rot = Entity.getRotation(player);
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

    private void sendPacket(float pitch, float yaw) {
        Class<?> f = float.class, d = double.class;
        JClass mc = JClass.getClass("minecraft");
        JClass rot = JClass.getClass("cPlayerPosRot");
        Class<?> ht = JClass.getClass("packet").get();
        Constructor<?> c = rot.getConstructor(d, d, d, f, f, boolean.class);
        Object player = mc.getField("mcPlayer").get(Invoker.client.MC());
        JMethod send = JClass.getClass("netHandler").getMethod("sendPacket", ht);
        Object connection = JClass.getClass("player").getField("connection").get(player);
        Object ground = JClass.getClass("entity").getField("entityGround").get(player);
        Vector3 pos = Entity.getPosition(mc.getField("mcPlayer").get(Invoker.client.MC()));
        send.call(connection, rot.newInstance(c, pos.x, pos.y, pos.z, yaw, pitch, ground));
    }

    public void onPacket(Packet packet) {
        boolean player = packet.type.getName().equals(C.get("cPlayer"));
        if((player || packet.type.getName().startsWith(C.get("cPlayer") + "$")) && rotation != null) {
            packet.setValue("cPlayerPitch", rotation.pitch, true);
            packet.setValue("cPlayerYaw", rotation.yaw, true);
        }
    }
}
