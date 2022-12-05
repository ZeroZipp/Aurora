package com.zerozipp.client.mods.screen;

import com.zerozipp.client.Client;
import com.zerozipp.client.Invoker;
import com.zerozipp.client.utils.base.Module;
import com.zerozipp.client.utils.font.Render;
import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.types.Type;
import com.zerozipp.client.utils.utils.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.function.ToDoubleFunction;
import static java.util.Comparator.comparingDouble;

@Aurora(Type.MODULE)
public class Mods extends Module {
    private Render font = null;

    public Mods(String name, boolean active, Integer key) {
        super(name, active, key);
    }

    @Override
    public void onOverlay() {
        super.onOverlay();
    }
}
