package com.zerozipp.client.utils.utils;

import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.types.Type;

@Aurora(Type.BASE)
@SuppressWarnings("unused")
public class Color {
    public final int red, green, blue, alpha;

    public Color(int r, int g, int b, int a) {
        red = r;
        green = g;
        blue = b;
        alpha = a;
    }

    public int getColor() {
        int r = red * 0x00010000;
        int g = green * 0x00000100;
        int a = alpha * 0x01000000;
        return r+g+blue+a;
    }

    public Color addColor(Color color) {
        int r = red + color.red;
        int g = green + color.green;
        int b = blue + color.blue;
        int a = alpha + color.alpha;
        return new Color(r, g, b, a);
    }
}
