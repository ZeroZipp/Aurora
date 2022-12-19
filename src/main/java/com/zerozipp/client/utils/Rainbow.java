package com.zerozipp.client.utils;

import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.types.Type;
import java.awt.Color;

@Aurora(Type.BASE)
@SuppressWarnings("unused")
public class Rainbow {
    public static int getRainbow(float seconds, float sat, float brightness, long index) {
        float hue = ((System.currentTimeMillis()+index) % (int)(seconds*1000) / (seconds*1000));
        return Color.HSBtoRGB(hue, sat, brightness);
    }
}
