package com.zerozipp.client.utils;

import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.reflect.JClass;
import com.zerozipp.client.utils.reflect.JMethod;
import com.zerozipp.client.utils.types.Type;
import com.zerozipp.client.utils.utils.Vector3;

@Aurora(Type.BASE)
@SuppressWarnings("unused")
public class World {
    public static boolean isReplaceable(Object world, Vector3 pos) {
        JClass w = JClass.getClass("world");
        JClass mat = JClass.getClass("material");
        Class<?> c = JClass.getClass("blockPos").get();
        JClass prop = JClass.getClass("blockProperties");
        JMethod state = w.getMethod("worldGetBlockState", c);
        JMethod getMat = prop.getMethod("getMaterial");
        JMethod rep = mat.getMethod("isReplaceable");
        Object blockPos = pos.getBlock();
        Object st = state.call(world, blockPos);
        Object material = getMat.call(st);
        return (boolean) rep.call(material);
    }

    public static Object getBlock(Object world, Vector3 pos) {
        JClass w = JClass.getClass("world");
        JClass mat = JClass.getClass("material");
        JClass prop = JClass.getClass("blockState");
        Class<?> c = JClass.getClass("blockPos").get();
        JMethod state = w.getMethod("worldGetBlockState", c);
        JMethod getBlock = prop.getMethod("getBlock");
        JMethod rep = mat.getMethod("isReplaceable");
        Object blockPos = pos.getBlock();
        Object st = state.call(world, blockPos);
        return getBlock.call(st);
    }
}
