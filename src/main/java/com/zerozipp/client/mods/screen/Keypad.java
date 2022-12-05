package com.zerozipp.client.mods.screen;

import com.zerozipp.client.Client;
import com.zerozipp.client.Invoker;
import com.zerozipp.client.utils.settings.*;
import com.zerozipp.client.utils.utils.Color;
import com.zerozipp.client.utils.utils.Vector2;
import com.zerozipp.client.utils.base.Display;
import com.zerozipp.client.utils.base.Module;
import com.zerozipp.client.utils.base.Resolution;
import com.zerozipp.client.utils.font.Render;
import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.reflect.JClass;
import com.zerozipp.client.utils.reflect.JField;
import com.zerozipp.client.utils.types.Type;
import org.lwjgl.input.Keyboard;

@Aurora(Type.MODULE)
public class Keypad extends Module {
    private Render font = null;
    private final Vector2 pos;

    public Keypad(String name, boolean active, Integer key) {
        super(name, active, key);
        pos = new Vector2(28, 28);
        settings.add(new Value("Size", 23, 20, 26));
        settings.add(new Option("Pos", new String[] {"LEFT", "RIGHT"}, 1));
    }

    @Override
    public void onOverlay() {
        super.onOverlay();
        String m = "font/medium.ttf";
        Color active = Color.border, text = Color.text;
        float size = ((Value) settings.get(0)).getValue();
        if(font == null) font = Client.getFont(m, 20);
        Resolution res = new Resolution(Invoker.client.MC());
        boolean l = ((Option) settings.get(1)).getIndex() == 0;
        JClass minecraft = JClass.getClass("minecraft");
        JField screen = minecraft.getField("guiScreen");
        boolean isOverlay = screen.get(Invoker.client.MC()) == null;
        float x = l ? pos.x : res.getWidth() - ((size + 2) * 3 - 2) - pos.x;
        Color a = (isOverlay && Keyboard.isKeyDown(Keyboard.KEY_A)) ? active : text;
        Color s = (isOverlay && Keyboard.isKeyDown(Keyboard.KEY_S)) ? active : text;
        Color d = (isOverlay && Keyboard.isKeyDown(Keyboard.KEY_D)) ? active : text;
        Color w = (isOverlay && Keyboard.isKeyDown(Keyboard.KEY_W)) ? active : text;
        Color space = (isOverlay && Keyboard.isKeyDown(Keyboard.KEY_SPACE)) ? active : text;
        onKey("A", x, res.getHeight() - pos.y - (size + 2), size, size, a.getColor());
        onKey("S", x + (size + 2), res.getHeight() - pos.y - (size + 2), size, size, s.getColor());
        onKey("D", x + (size + 2) * 2, res.getHeight() - pos.y - (size + 2), size, size, d.getColor());
        onKey("W", x + (size + 2), res.getHeight() - pos.y - (size + 2) * 2, size, size,w.getColor());
        onKey("SPACE", x, res.getHeight() - pos.y, (size + 2) * 3 - 2, size, space.getColor());
    }

    private void onKey(String text, float x, float y, float w, float h, int c) {
        int b = new Color(0, 0, 0, 130).getColor();
        Display.drawRect(x, y - h, x + w, y, b);
        Display.drawRect(x, y - 1, x + w, y, c);
        float tx = w / 2 - font.stringWidth(text) / 3.5f;
        float ty = h / 2 - (float) font.getFontHeight() / 2;
        font.drawString(text, x + tx, y - h + ty, c, false);
    }
}
