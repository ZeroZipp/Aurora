package com.zerozipp.client.mods.screen;

import com.zerozipp.client.Client;
import com.zerozipp.client.Invoker;
import com.zerozipp.client.utils.Rainbow;
import com.zerozipp.client.utils.base.Resolution;
import com.zerozipp.client.utils.settings.Toggle;
import com.zerozipp.client.utils.utils.Color;
import com.zerozipp.client.utils.utils.Vector2;
import com.zerozipp.client.utils.base.Module;
import com.zerozipp.client.utils.font.Render;
import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.types.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.function.ToDoubleFunction;
import static java.util.Comparator.comparingDouble;

@Aurora(Type.MODULE)
public class Mods extends Module {
    public static final Color cText = new Color(255, 255, 255, 255);
    private final Vector2 pos;
    private Render font;

    public Mods(String name, boolean active, Integer key) {
        super(name, active, key);
        pos = new Vector2(10, 4);
        settings.add(new Toggle("Rainbow", false));
    }

    @Override
    public void onOverlay() {
        super.onOverlay();
        final float offset = 11;
        String n = "font/medium.ttf";
        if(font == null) font = Client.getFont(n, 18);
        Resolution res = new Resolution(Invoker.client.MC());
        ToDoubleFunction<Module> d = m -> font.stringWidth(m.name);
        ArrayList<Module> mods = Invoker.client.mods.getActive();
        mods.sort(comparingDouble(d));
        Collections.reverse(mods);
        float index = pos.y;
        for(Module m : mods) {
            long idx = (long) (index - pos.y) * 5;
            float w = res.getWidth() - font.getStringWidth(m.name);
            int color = Rainbow.getRainbow(4, 0.8f, 1, idx);
            if(!((Toggle) settings.get(0)).isActive()) color = cText.getColor();
            font.drawString(m.name, w - pos.x, index, color, true);
            index += offset;
        }
    }
}
