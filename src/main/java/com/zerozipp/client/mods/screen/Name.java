package com.zerozipp.client.mods.screen;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.function.ToDoubleFunction;

import static java.util.Comparator.comparingDouble;

@Aurora(Type.MODULE)
public class Name extends Module {
    private Render big, tiny, font;
    private final Vector2 pos;

    public Name(String name, boolean active, Integer key) {
        super(name, active, key);
        pos = new Vector2(12, 10);
        settings.add(new Option("Pos", new String[] {"LEFT", "RIGHT"}, 0));
        settings.add(new Toggle("Mods", true));
    }

    @Override
    public void onOverlay() {
        super.onOverlay();
        final float offset = 11;
        String n = "font/medium.ttf";
        if(big == null) big = Client.getFont(n, 35);
        if(tiny == null) tiny = Client.getFont(n, 30);
        if(font == null) font = Client.getFont(n, 18);
        boolean mod = ((Toggle) settings.get(1)).isActive();
        Resolution res = new Resolution(Invoker.client.MC());
        int color = new Color(0, 0, 0, 130).getColor();
        boolean left = ((Option) settings.get(0)).getIndex() == 0;
        float sx = big.getStringWidth("B" + Invoker.client.build);
        float size = big.getStringWidth(Invoker.client.name) + 8 + sx;
        float x = left ? pos.x : res.getWidth() - size - this.pos.x;
        float position = x + 6 + big.getStringWidth(Invoker.client.name);
        Display.drawRect(x, pos.y, x + size, pos.y + 22, color);
        Display.drawRect(x, pos.y + 21, x + size, pos.y + 22, Color.text.getColor());
        big.drawString(Invoker.client.name, x + 4, pos.y - 3, Color.border.getColor(), true);
        tiny.drawString("B" + Invoker.client.build, position, pos.y, Color.text.getColor(), true);
        ToDoubleFunction<Module> d = m -> big.stringWidth(m.name);
        ArrayList<Module> mods = Invoker.client.mods.getActive();
        mods.sort(comparingDouble(d));
        Collections.reverse(mods);
        float index = 32.0f;
        if(mod) for(Module m : mods) {
            int c = Color.text.getColor();
            float w = res.getWidth() - font.getStringWidth(m.name);
            if(left) font.drawString(m.name, 12, index, c, true);
            else font.drawString(m.name, w - 14, index, c, true);
            index += offset;
        }
    }
}
