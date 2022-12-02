package com.zerozipp.client.utils.base;

import java.util.HashMap;
import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.types.Type;
import static org.lwjgl.opengl.GL11.*;

@Aurora(Type.BASE)
@SuppressWarnings("unused")
public class Rendering {
    private static final HashMap<Integer, Boolean> map = new HashMap<>();

    public static void pushIndex(int idx) {
        map.put(idx, glIsEnabled(idx));
    }

    public static void popIndex(int idx) {
        if(map.get(idx)) glEnable(idx);
        else glDisable(idx);
        map.remove(idx);
    }
}
