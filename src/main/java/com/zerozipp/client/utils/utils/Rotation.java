package com.zerozipp.client.utils.utils;

import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.types.Type;

@Aurora(Type.BASE)
@SuppressWarnings("unused")
public class Rotation {
    public final float pitch, yaw;

    public Rotation(float pitch, float yaw) {
        this.pitch = pitch;
        this.yaw = yaw;
    }

    public Rotation add(float pitch, float yaw) {
        return new Rotation(this.pitch + pitch, this.yaw + yaw);
    }

    public Rotation add(Rotation rot) {
        return new Rotation(pitch + rot.pitch, yaw + rot.yaw);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        if(obj instanceof Rotation) {
            Rotation v = (Rotation) obj;
            return v.pitch == pitch && v.yaw == yaw;
        } else return false;
    }

}
