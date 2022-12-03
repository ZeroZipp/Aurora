package com.zerozipp.client.mods;

import com.zerozipp.client.Client;
import com.zerozipp.client.Invoker;
import com.zerozipp.client.utils.base.Resolution;
import com.zerozipp.client.utils.settings.*;
import com.zerozipp.client.utils.utils.Color;
import com.zerozipp.client.utils.utils.Vector2;
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
        settings.add(new Option("Pos", new String[] {"LEFT", "RIGHT"}, 0));
    }

    @Override
    public void onOverlay() {
        super.onOverlay();
        String m = "font/medium.ttf";
        if(font == null) font = Client.getFont(m, 35);
        if(tiny == null) tiny = Client.getFont(m, 30);
        Resolution res = new Resolution(Invoker.client.MC());
        int color = new Color(0, 0, 0, 130).getColor();
        boolean left = ((Option) settings.get(0)).getIndex() == 0;
        float sx = font.getStringWidth("B" + Invoker.client.build);
        float size = font.getStringWidth(Invoker.client.name) + 8 + sx;
        float x = left ? pos.x : res.getWidth() - size - this.pos.x;
        float position = x + 6 + font.getStringWidth(Invoker.client.name);
        Display.drawRect(x, pos.y, x + size, pos.y + 22, color);
        Display.drawRect(x, pos.y + 21, x + size, pos.y + 22, Color.text.getColor());
        font.drawString(Invoker.client.name, x + 4, pos.y - 3, Color.border.getColor(), true);
        tiny.drawString("B" + Invoker.client.build, position, pos.y, Color.text.getColor(), true);
    }
}
