package com.zerozipp.client.loader;

import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.types.Type;
import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.LaunchClassLoader;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Aurora(Type.INJECT)
@SuppressWarnings("unused")
public class Tweaker implements ITweaker {
    private ArrayList<String> args;

    @Override
    public void acceptOptions(List<String> args, File gameDir, File assetsDir, String version) {
        this.args = new ArrayList<>(args);
        if(gameDir != null) addArgs("gameDir", gameDir.getAbsolutePath());
        if(assetsDir != null) addArgs("assetsDir", assetsDir.getAbsolutePath());
        addArgs("version", version);
    }

    private void addArgs(String name, String value) {
        this.args.add("--" + name);
        this.args.add(value);
    }

    @Override
    public void injectIntoClassLoader(LaunchClassLoader classLoader) {
        classLoader.registerTransformer("com.zerozipp.client.loader.Loader");
    }

    @Override
    public String getLaunchTarget() {
        return "net.minecraft.client.main.Main";
    }

    @Override
    public String[] getLaunchArguments() {
        return args.toArray(new String[0]);
    }
}