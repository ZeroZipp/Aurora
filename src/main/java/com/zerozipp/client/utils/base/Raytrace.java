package com.zerozipp.client.utils.base;

import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.reflect.JClass;
import com.zerozipp.client.utils.reflect.JField;
import com.zerozipp.client.utils.types.Type;
import static com.zerozipp.client.utils.source.Classes.C;

@Aurora(Type.BASE)
@SuppressWarnings("unused")
public class Raytrace {
    private final Object obj;

    public Raytrace(Object obj) {
        String name = obj.getClass().getName();
        if(name.equals(C.get("raytrace"))) this.obj = obj;
        else throw new RuntimeException("Not a 'Raytrace'");
    }

    public Object get() {
        return obj;
    }

    public Object blockPos() {
        return getObject("rBlockPos");
    }

    public Object typeOfHit() {
        return getObject("rTypeOfHit");
    }

    public Object sideHit() {
        return getObject("rSideHit");
    }

    public Object hitVec() {
        return getObject("rHitVec");
    }

    public Object entityHit() {
        return getObject("rEntityHit");
    }

    private Object getObject(String name) {
        JField f = JClass.getClass("raytrace").getDecField(name);
        return f.get(obj);
    }
}
