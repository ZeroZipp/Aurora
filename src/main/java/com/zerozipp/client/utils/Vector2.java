package com.zerozipp.client.utils;

import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.types.Type;

@Aurora(Type.BASE)
@SuppressWarnings("unused")
public class Vector2 {
    public final float x, y;

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
