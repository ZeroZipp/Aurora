package com.zerozipp.client.utils;

import com.zerozipp.client.Invoker;
import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.reflect.JClass;
import com.zerozipp.client.utils.reflect.JField;
import com.zerozipp.client.utils.types.Type;

@Aurora(Type.BASE)
@SuppressWarnings("unused")
public class Keybinds {
    public static void setPressed(Object binding, boolean pressed) {
        JField field = JClass.getClass("keyBind").getDecField("isKeyPressed");
        field.set(binding, pressed);
    }

    public static void resetPressed(Object binding) {
        Object screen = JClass.getClass("minecraft").getField("guiScreen").get(Invoker.client.MC());
        if(screen != null) JClass.getClass("keyBind").getDecMethod("resetPressed").call(binding);
        else JClass.getClass("keyBind").getMethod("keyUpdateState").call(binding);
    }

    public static boolean isPressed(Object binding) {
        Object pressed = JClass.getClass("keyBind").getMethod("keyIsDown").call(binding);
        if(pressed instanceof Boolean) return (Boolean) pressed;
        else return false;
    }
}
