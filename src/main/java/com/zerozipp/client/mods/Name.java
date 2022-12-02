package com.zerozipp.client.mods;

import com.zerozipp.client.Client;
import com.zerozipp.client.Invoker;
import com.zerozipp.client.utils.Color;
import com.zerozipp.client.utils.Vector2;
import com.zerozipp.client.utils.base.Display;
import com.zerozipp.client.utils.base.Module;
import com.zerozipp.client.utils.font.Render;
import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.types.Type;

@Aurora(Type.MODULE)
public class Name extends Module {
    private Render font = null, tiny = null;
    private final Vector2 pos;

    public Name(String name, boolean active, Integer key) {
        super(name, active, key);
        pos = new Vector2(12, 10);
    }

    @Override
    public void onOverlay() {
        super.onOverlay();
        String m = "font/medium.ttf";
        if(font == null) font = Client.getFont(m, 35);
        if(tiny == null) tiny = Client.getFont(m, 30);
        int color = new Color(0, 0, 0, 130).getColor();
        float position = pos.x + 6 + font.getStringWidth(Invoker.client.name);
        float sx = font.getStringWidth("B" + Invoker.client.build);
        float size = font.getStringWidth(Invoker.client.name) + 8 + sx;
        Display.drawRect(pos.x, pos.y, pos.x + size, pos.y + 22, color);
        Display.drawRect(pos.x, pos.y + 21, pos.x + size, pos.y + 22, Color.text.getColor());
        font.drawString(Invoker.client.name, pos.x + 4, pos.y - 3, Color.border.getColor(), true);
        tiny.drawString("B" + Invoker.client.build, position, pos.y, Color.text.getColor(), true);
    }
}
