package com.zerozipp.client.utils.utils;

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

    public Vector2 add(float x, float y) {
        return new Vector2(this.x + x, this.y + y);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        if(obj instanceof Vector2) {
            Vector2 v = (Vector2) obj;
            return v.x == x && v.y == y;
        } else return false;
    }

}
