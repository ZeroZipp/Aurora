package com.zerozipp.client;

import com.zerozipp.client.utils.Network;
import com.zerozipp.client.utils.base.Display;
import com.zerozipp.client.utils.base.Packet;
import com.zerozipp.client.utils.font.Render;
import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.reflect.JClass;
import com.zerozipp.client.utils.reflect.JField;
import com.zerozipp.client.utils.reflect.JMethod;
import com.zerozipp.client.utils.types.Events;
import com.zerozipp.client.utils.types.Type;
import org.lwjgl.input.Keyboard;
import java.awt.*;
import java.io.InputStream;
import static com.zerozipp.client.Invoker.*;

@Aurora(Type.CLIENT)
@SuppressWarnings("unused")
public class Client {
    public final Modules mods;
    public final Network network;
    public final Integer build;
    public final String name;

    public Object MC() {
        JClass c = JClass.getClass("minecraft");
        return c.getDecField("minecraft").get(null);
    }

    public Client(String name, Integer build) {
        this.network = new Network();
        this.mods = new Modules();
        this.build = build;
        this.name = name;
    }

    public static Render getFont(String path, int size) {
        Font font = new Font("default", Font.PLAIN, size);
        try { font = Font.createFont(Font.TRUETYPE_FONT, getResource(path)); }
        catch(Exception e) { e.printStackTrace(); }
        font = font.deriveFont(Font.PLAIN, (float) size);
        return Render.create(font, true, true);
    }

    public static InputStream getResource(String path) {
        ClassLoader loader = ClassLoader.getSystemClassLoader();
        return java.util.Objects.requireNonNull(loader.getResourceAsStream("assets/" + path));
    }

    public void onStart() {
        logger.info("Successfully loaded: " + name);
    }

    public void onUpdate() {
        network.onReset();
        JClass mc = JClass.getClass("minecraft");
        Object player = mc.getField("mcPlayer").get(MC());
        Object world = mc.getField("mcWorld").get(MC());
        if(player != null && world != null) {
            mods.onUpdate();
            network.onUpdate();
        }
    }

    public void onOverlay() {
        Display.pushScreen();
        mods.onOverlay();
        Display.popScreen();
    }

    public void onRender(float ticks) {
        mods.onRender(ticks);
    }

    public boolean onEvent(Events event) {
        return mods.onEvent(event);
    }

    public Packet onPacket(Packet packet) {
        packet = mods.onPacket(packet);
        network.onPacket(packet);
        return packet;
    }

    public void onKeyboard() {
        JClass mc = JClass.getClass("minecraft");
        JField screen = mc.getField("guiScreen");
        if(Keyboard.getEventKeyState()) {
            if(screen.get(Invoker.client.MC()) == null) {
                if(Keyboard.getEventKey() == 54) setScreen();
                else mods.onKey(Keyboard.getEventKey());
            }
        }
    }

    public void setScreen() {
        JClass screen = JClass.nativeClass("Screen");
        Class<?> s = JClass.getClass("screen").get();
        Object guiScreen = screen.newInstance(screen.getConstructor());
        JMethod setScreen = JClass.getClass("minecraft").getMethod("mcSetScreen", s);
        setScreen.call(MC(), guiScreen);
    }
}
