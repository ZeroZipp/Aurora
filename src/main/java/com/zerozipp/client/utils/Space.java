package com.zerozipp.client.utils;

import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.types.Type;
import com.zerozipp.client.utils.utils.Vector3;

@Aurora(Type.BASE)
@SuppressWarnings("unused")
public class Space {
    public static Vector3 getLook(float pitch, float yaw) {
        float var3 = (float) Math.cos(-yaw * 0.017453292F - Math.PI);
        float var4 = (float) Math.sin(-yaw * 0.017453292F - Math.PI);
        float var5 = (float) -Math.cos(-pitch * 0.017453292F);
        float var6 = (float) Math.sin(-pitch * 0.017453292F);
        return new Vector3(var4 * var5, var6, var3 * var5);
    }
}
