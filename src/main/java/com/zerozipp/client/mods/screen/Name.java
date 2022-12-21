package com.zerozipp.client.mods.screen;

import com.zerozipp.client.Client;
import com.zerozipp.client.Invoker;
import com.zerozipp.client.utils.Rainbow;
import com.zerozipp.client.utils.settings.Toggle;
import com.zerozipp.client.utils.settings.Value;
import com.zerozipp.client.utils.utils.Color;
import com.zerozipp.client.utils.utils.Vector2;
import com.zerozipp.client.utils.base.Module;
import com.zerozipp.client.utils.font.Render;
import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.types.Type;

@Aurora(Type.MODULE)
public class Name extends Module {
    public static final Color cText = new Color(255, 255, 255, 255);
    public static final Color cName = new Color(233, 233, 233, 255);
    private final Vector2 pos;
    private Render big, font;

    public Name(String name, boolean active, Integer key) {
        super(name, active, key);
        pos = new Vector2(12, 6);
        settings.add(new Toggle("Rainbow", false));
        settings.add(new Value("Offset", 4, 2, 8));
    }

    @Override
    public void onOverlay(float ticks) {
        super.onOverlay(ticks);
        String n = "font/medium.ttf";
        Value v = (Value) settings.get(1);
        if(big == null) big = Client.getFont(n, 40);
        if(font == null) font = Client.getFont(n, 20);
        float off = v.getLast() + (v.getValue() - v.getLast()) * ticks;
        Vector2 pos = this.pos.add(off, off);
        float position = pos.x + 1 + big.getStringWidth(Invoker.client.name);
        int color = Rainbow.getRainbow(4, 0.8F, 1, 0);
        if(!((Toggle) settings.get(0)).isActive()) color = cName.getColor();
        big.drawString(Invoker.client.name, pos.x, pos.y - 3, color, true);
        font.drawString("B" + Invoker.client.build, position, pos.y + 2, cText.getColor(), true);
    }
}
