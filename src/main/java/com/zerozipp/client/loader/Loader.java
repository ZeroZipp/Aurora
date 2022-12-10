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
        if(C.get("renderLiving").equals(name)) return renderLiving(bytes);
        if(C.get("screenMenu").equals(name)) return screenMenu(bytes);
        if(C.get("minecraft").equals(name)) return minecraft(bytes);
        if(C.get("renderer").equals(name)) return renderer(bytes);
        if(C.get("network").equals(name)) return network(bytes);
        if(C.get("overlay").equals(name)) return overlay(bytes);
        if(C.get("entity").equals(name)) return entity(bytes);
        if(C.get("player").equals(name)) return player(bytes);
        return bytes;
    }

    private byte[] minecraft(byte[] bytes) {
        bytes = injector.invokeStatic(bytes, M.get("mcStart"), "()V", "onStart", "()V", 0);
        bytes = injector.invokeStatic(bytes, M.get("keyPatch"), "()V", "onKeyboard", "()V", 0);
        bytes = injector.invokeStatic(bytes, M.get("runTick"), "()V", "onUpdate", "()V", 599);
        return bytes;
    }

    private byte[] renderer(byte[] bytes) {
        bytes = injector.invokeEvent(bytes, M.get("renderHand"), "(FI)V", "OFFSET", 0);
        bytes = injector.invokeParam(bytes, M.get("getFov"), "(FZ)F", "CAMERA", 2, 1, 0);
        bytes = injector.invokeParam(bytes, M.get("updateLightmap"), "(F)V", "BRIGHT", 17, 10F, 426);
        bytes = injector.invokeStatic(bytes, M.get("renderWorld"), "(FJ)V", "onRender", "()V", 109);
        bytes = injector.invokeReturn(bytes, M.get("applyBobbing"), "(F)V", "BOBBLING", 0);
        bytes = injector.invokeReturn(bytes, M.get("hurtCam"), "(F)V", "HURT", 0);
        return bytes;
    }

    private byte[] renderLiving(byte[] bytes) {
        String name = M.get("renderLiving"), params = "(Ljava/lang/Object;)V";
        bytes = injector.invokeStatic(bytes, name, "(Lvp;DDDFF)V", "endRender", params, 1, 448);
        bytes = injector.invokeStatic(bytes, name, "(Lvp;DDDFF)V", "onRender", params, 1, 0);
        bytes = injector.invokeReturn(bytes, M.get("canRenderName"), "(Lvp;)Z", "NAMES", 0, 0);
        return bytes;
    }

    private byte[] screenMenu(byte[] bytes) {
        String name = M.get("guiOnKey"), invoke = "keyTyped";
        bytes = injector.invokeInteger(bytes, name, "(CI)V", invoke, "(I)V", 2, 0);
        return bytes;
    }

    private byte[] overlay(byte[] bytes) {
        String name = M.get("renderOverlay"), invoke = "onOverlay";
        bytes = injector.invokeStatic(bytes, name, "(F)V", invoke, "()V", 918);
        return bytes;
    }

    private byte[] entity(byte[] bytes) {
        bytes = injector.invokeReturn(bytes, M.get("entityInvisible"), "()Z", "INVISIBLE", 0, 0);
        bytes = injector.invokeReturn(bytes, M.get("addVelocity"), "(DDD)V", "VELOCITY", 0);
        bytes = injector.invokeReturn(bytes, M.get("setVelocity"), "(DDD)V", "VELOCITY", 0);
        return bytes;
    }

    private byte[] player(byte[] bytes) {
        String name = M.get("walkPlayer"), params = "(Ljava/lang/Object;)V";
        bytes = injector.invokeStatic(bytes, name, "()V", "endMove", params, 0, 375);
        bytes = injector.invokeStatic(bytes, name, "()V", "onMove", params, 0, 0);
        return bytes;
    }

    private byte[] network(byte[] bytes) {
        String gfl = "io/netty/util/concurrent/GenericFutureListener";
        bytes = injector.invokePacket(bytes, M.get("sendPacket"), "(Lht;L" + gfl + ";L" + gfl + ";)V", 1, 0);
        bytes = injector.invokePacket(bytes, M.get("sendPacket"), "(Lht;)V", 1, 0);
        return bytes;
    }
}
