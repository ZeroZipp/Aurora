package com.zerozipp.client.mods.screen;

import com.zerozipp.client.Client;
import com.zerozipp.client.Invoker;
import com.zerozipp.client.utils.Rainbow;
import com.zerozipp.client.utils.settings.Toggle;
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
        pos = new Vector2(16, 10);
        settings.add(new Toggle("Rainbow", false));
    }

    @Override
    public void onOverlay() {
        super.onOverlay();
        String n = "font/medium.ttf";
        if(big == null) big = Client.getFont(n, 40);
        if(font == null) font = Client.getFont(n, 20);
        float position = pos.x + 1 + big.getStringWidth(Invoker.client.name);
        int color = Rainbow.getRainbow(4, 0.8F, 1, 0);
        if(!((Toggle) settings.get(0)).isActive()) color = cName.getColor();
        big.drawString(Invoker.client.name, pos.x, pos.y - 3, color, true);
        font.drawString("B" + Invoker.client.build, position, pos.y + 2, cText.getColor(), true);
    }
}
