package com.zerozipp.client;

import com.zerozipp.client.utils.Entity;
import com.zerozipp.client.utils.Injector;
import com.zerozipp.client.utils.Rotation;
import com.zerozipp.client.utils.base.Packet;
import com.zerozipp.client.utils.base.Rotating;
import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.reflect.JClass;
import com.zerozipp.client.utils.types.Events;
import com.zerozipp.client.utils.types.Type;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Aurora(Type.INJECT)
@SuppressWarnings("unused")
public class Invoker {
    public static final Injector injector = new Injector(Invoker.class, Events.class, Packet.class);
    public static final Client client = new Client("Aurora", 2);
    public static final Logger logger = LogManager.getLogger();

    public static void onStart() {
        client.onStart();
    }

    public static void onKeyboard() {
        client.onKeyboard();
    }

    public static void onUpdate() {
        client.onUpdate();
    }

    public static void onOverlay() {
        client.onOverlay();
    }

    public static boolean onEvent(Events event) {
        return client.onEvent(event);
    }

    public static Packet onPacket(Packet packet) {
        return client.onPacket(packet);
    }

    public static void onRender(Object entity) {
        Object mc = Invoker.client.MC();
        JClass c = JClass.getClass("minecraft");
        Object player = c.getField("mcPlayer").get(mc);
        Class<?> p = JClass.getClass("livingBase").get();
        if(player != null && player.equals(entity)) {
            Rotating.pushRotation(entity);
            Rotation prev = client.network.getPrevious();
            if(client.network.getRotation() != null) {
                Rotation rot = client.network.getRotation();
                if(prev == null) prev = Entity.getPrevRotation(entity);
                Entity.setYawOffset(entity, rot.yaw, prev.yaw);
                Entity.setYawHead(entity, rot.yaw, prev.yaw);
                Entity.setPrevRotation(entity, prev);
                Entity.setRotation(entity, rot);
            }
        }
    }

    public static void endRender(Object entity) {
        Object mc = Invoker.client.MC();
        JClass c = JClass.getClass("minecraft");
        Object player = c.getField("mcPlayer").get(mc);
        if(player != null && player.equals(entity)) {
            Rotating.popRotation(entity);
        }
    }
}
