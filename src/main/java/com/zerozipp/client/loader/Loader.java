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
        if(C.get("renderer").equals(name)) return renderer(bytes);
        if(C.get("network").equals(name)) return network(bytes);
        if(C.get("overlay").equals(name)) return overlay(bytes);
        return bytes;
    }

    private byte[] minecraft(byte[] bytes) {
        bytes = injector.invokeStatic(bytes, M.get("mcStart"), "()V", "onStart", "()V", 0);
        bytes = injector.invokeStatic(bytes, M.get("keyPatch"), "()V", "onKeyboard", "()V", 0);
        bytes = injector.invokeStatic(bytes, M.get("runTick"), "()V", "onUpdate", "()V", 0);
        return bytes;
    }

    private byte[] renderer(byte[] bytes) {
        bytes = injector.invokeEvent(bytes, M.get("renderHand"), "(FI)V", "BOBBLING", 0);
        bytes = injector.invokeParam(bytes, M.get("getFov"), "(FZ)F", "CAMERA", 2, 1, 0);
        bytes = injector.invokeParam(bytes, M.get("updateLightmap"), "(F)V", "BRIGHT", 17, 10F, 426);
        bytes = injector.invokeReturn(bytes, M.get("hurtCam"), "(F)V", "CAMERA", 0);
        return bytes;
    }

    private byte[] overlay(byte[] bytes) {
        String name = M.get("renderOverlay"), invoke = "onOverlay";
        bytes = injector.invokeStatic(bytes, name, "(F)V", invoke, "()V", 918);
        return bytes;
    }

    private byte[] network(byte[] bytes) {
        String gfl = "io/netty/util/concurrent/GenericFutureListener";
        bytes = injector.invokePacket(bytes, M.get("sendPacket"), "(Lht;L" + gfl + ";L" + gfl + ";)V", 1, 0);
        bytes = injector.invokePacket(bytes, M.get("sendPacket"), "(Lht;)V", 1, 0);
        return bytes;
    }
}
