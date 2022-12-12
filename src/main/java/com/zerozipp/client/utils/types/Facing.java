package com.zerozipp.client.utils.types;

import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.utils.Vector3;

@Aurora(Type.BASE)
@SuppressWarnings("unused")
public enum Facing {
    DOWN(0, -1, 0),
    UP(0, 1, 0),
    NORTH(0, 0, -1),
    SOUTH(0, 0, 1),
    WEST(-1, 0, 0),
    EAST(1, 0, 0);

    public final Vector3 pos;

    Facing(float x, float y, float z) {
        this.pos = new Vector3(x, y, z);
    }

    public Vector3 getOpposite() {
        return new Vector3(-pos.x, -pos.y, -pos.z);
    }
}
