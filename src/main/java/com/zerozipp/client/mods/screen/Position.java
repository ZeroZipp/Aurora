package com.zerozipp.client.mods.screen;

import com.zerozipp.client.Client;
import com.zerozipp.client.Invoker;
import com.zerozipp.client.utils.Entity;
import com.zerozipp.client.utils.base.Module;
import com.zerozipp.client.utils.base.Resolution;
import com.zerozipp.client.utils.font.Render;
import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.reflect.JClass;
import com.zerozipp.client.utils.types.Type;
import com.zerozipp.client.utils.utils.Color;
import com.zerozipp.client.utils.utils.Vector2;
import com.zerozipp.client.utils.utils.Vector3;

@Aurora(Type.MODULE)
public class Position extends Module {
    private Render font = null;
    private final Vector2 pos;

    public Position(String name, boolean active, Integer key) {
        super(name, active, key);
        pos = new Vector2(0, 12);
    }

    @Override
    public void onOverlay() {
        super.onOverlay();
        String m = "font/medium.ttf";
        Object mc = Invoker.client.MC();
        int color = Color.text.getColor();
        Resolution res = new Resolution(mc);
        JClass c = JClass.getClass("minecraft");
        if(font == null) font = Client.getFont(m, 16);
        Object player = c.getField("mcPlayer").get(mc);
        Vector3 position = Entity.getPosition(player);
        float x = Math.round(position.x) + 0.5f;
        float y = Math.round(position.y);
        float z = Math.round(position.z) + 0.5f;
        String text = "X:" + x + " Y:" + y + " Z:" + z;
        float width = res.getWidth() / 2 - font.floatStringWidth(text) / 2;
        font.drawString(text, width + pos.x, pos.y, color, true);
    }
}
