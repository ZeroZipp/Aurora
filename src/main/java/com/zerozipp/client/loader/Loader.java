package com.zerozipp.client.loader;

import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.types.Type;
import net.minecraft.launchwrapper.IClassTransformer;
import java.util.HashMap;
import static com.zerozipp.client.Invoker.injector;
import static com.zerozipp.client.utils.source.Classes.C;
import static com.zerozipp.client.utils.source.Methods.M;

@Aurora(Type.INJECT)
@SuppressWarnings("unused")
public class Loader implements IClassTransformer {
    private final HashMap<String, String> mapping = new HashMap<>();

    @Override
    public byte[] transform(String name, String transformed, byte[] bytes) {
        if(C.get("minecraft").equals(name)) return minecraft(bytes);
        if(C.get("entity").equals(name)) return entity(bytes);
        if(C.get("renderer").equals(name)) return renderer(bytes);
        return bytes;
    }

    private byte[] minecraft(byte[] bytes) {
        bytes = injector.invokeStatic(bytes, M.get("mcStart"), "()V", "onStart", "()V");
        bytes = injector.invokeStatic(bytes, M.get("keyPatch"), "()V", "onKeyboard", "()V");
        bytes = injector.invokeStatic(bytes, M.get("runTick"), "()V", "onUpdate", "()V");
        return bytes;
    }

    private byte[] entity(byte[] bytes) {
        bytes = injector.invokeReturn(bytes, M.get("entityOutlines"), "()Z", "OUTLINES", true);
        bytes = injector.invokeReturn(bytes, M.get("entityInvisible"), "()Z", "INVISIBLE", false);
        return bytes;
    }

    private byte[] renderer(byte[] bytes) {
        bytes = injector.invokeParam(bytes, M.get("getFov"), "(FZ)F", "CAMERA", 2, true);
        return bytes;
    }
}
