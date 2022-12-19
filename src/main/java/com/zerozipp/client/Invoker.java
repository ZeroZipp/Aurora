package com.zerozipp.client;

import com.zerozipp.client.utils.Entity;
import com.zerozipp.client.utils.Injector;
import com.zerozipp.client.utils.utils.Rotation;
import com.zerozipp.client.utils.base.Packet;
import com.zerozipp.client.utils.base.Rotating;
import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.reflect.JClass;
import com.zerozipp.client.utils.reflect.JMethod;
import com.zerozipp.client.utils.types.Events;
import com.zerozipp.client.utils.types.Type;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.input.Keyboard;

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

    public static void onSettings() {
        client.onSettings();
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

    public static boolean isSneaking(Object player) {
        JClass ent = JClass.getClass("entity");
        JMethod m = ent.getMethod("entitySneaking");
        boolean sneaking = (boolean) m.call(player);
        if(onEvent(Events.SNEAKING)) return true;
        return sneaking;
    }

    public static void onMove(Object entity) {
        Object mc = Invoker.client.MC();
        JClass c = JClass.getClass("minecraft");
        Object player = c.getField("mcPlayer").get(mc);
        if(player != null && player.equals(entity)) {
            Rotating.pushRotation(entity);
            Rotation rot = client.network.getRotation();
            if(rot != null) Entity.setRotation(entity, rot);
        }
    }

    public static void endMove(Object entity) {
        Object mc = Invoker.client.MC();
        JClass c = JClass.getClass("minecraft");
        Object player = c.getField("mcPlayer").get(mc);
        if(player != null && player.equals(entity)) {
            Rotating.popRotation(entity);
        }
    }

    public static void onRender() {
        Object mc = client.MC();
        JClass c = JClass.getClass("timer");
        JClass renderer = JClass.getClass("renderer");
        JClass minecraft = JClass.getClass("minecraft");
        Object timer = minecraft.getDecField("gameTimer").get(mc);
        boolean pause = (boolean) minecraft.getDecField("isGamePaused").get(mc);
        JMethod setup = renderer.getDecMethod("setupCamera", float.class, int.class);
        float pauseTicks = (float) minecraft.getDecField("renderTicksPaused").get(mc);
        float ticks = (float) c.getDecField("timerTicks").get(timer);
        Object r = minecraft.getDecField("renderer").get(mc);
        setup.call(r, pause ? pauseTicks : ticks, 2);
        client.onRender(pause ? pauseTicks : ticks);
    }

    public static void onRender(Object entity) {
        Object mc = Invoker.client.MC();
        JClass c = JClass.getClass("minecraft");
        Object player = c.getField("mcPlayer").get(mc);
        Class<?> p = JClass.getClass("livingBase").get();
        if(player != null && player.equals(entity)) {
            Rotating.pushRotation(entity);
            Rotation rot = client.network.getRotation();
            Rotation prev = client.network.getPrevious();
            if(prev == null) prev = Entity.getPrevRotation(entity);
            if(rot == null) rot = Entity.getRotation(entity);
            Entity.setYawOffset(entity, rot.yaw, prev.yaw);
            Entity.setYawHead(entity, rot.yaw, prev.yaw);
            Entity.setPrevRotation(entity, prev);
            Entity.setRotation(entity, rot);
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

    public static void keyTyped(int key) {
        if(key == Keyboard.KEY_TAB) {
            JClass c = JClass.getClass("minecraft");
            JClass screen = JClass.nativeClass("Session");
            Class<?> s = JClass.getClass("screen").get();
            Object last = c.getField("guiScreen").get(client.MC());
            Object guiScreen = screen.newInstance(screen.getConstructor(s), last);
            JMethod setScreen = c.getMethod("mcSetScreen", s);
            setScreen.call(client.MC(), guiScreen);
        }
    }
}
