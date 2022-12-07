package com.zerozipp.client.utils.base;

import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.reflect.JClass;
import com.zerozipp.client.utils.types.Type;

@Aurora(Type.BASE)
@SuppressWarnings("unused")
public class Packet {
    public final Class<?> type;
    public final Object packet;
    private boolean canceled = false;

    public Packet(Object packet) {
        type = packet.getClass();
        this.packet = packet;
    }

    public void setCanceled() {
        canceled = true;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public void setValue(String name, Object value, boolean base) {
        String baseClass = type.getName().split("\\$")[0];
        String className = base ? baseClass : type.getName();
        JClass c = JClass.nativeClass(className);
        c.getDecField(name).set(packet, value);
    }
}
