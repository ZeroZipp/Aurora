package com.zerozipp.client.utils.utils;

import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.types.Type;

@Aurora(Type.BASE)
@SuppressWarnings("unused")
public class Color {
    public static final Color background = new Color(0, 0, 0, 80);
    public static final Color window = new Color(50, 50, 50, 255);
    public static final Color border = new Color(0, 255, 0, 255);
    public static final Color module = new Color(78, 78, 78, 255);
    public static final Color setting = new Color(95, 95, 95, 255);
    public static final Color listing = new Color(110, 110, 110, 255);
    public static final Color enabled = new Color(103, 103, 103, 255);
    public static final Color slider = new Color(0, 255, 0, 60);
    public static final Color active = new Color(65, 65, 65, 255);
    public static final Color text = new Color(255, 255, 255, 255);
    public static final Color opened = new Color(210, 210, 210, 255);
    public static final Color keyBind = new Color(-12, -12, -12, 0);
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
