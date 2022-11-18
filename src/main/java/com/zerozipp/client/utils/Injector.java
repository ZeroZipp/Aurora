package com.zerozipp.client.utils;

import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.types.Type;
import org.objectweb.asm.Label;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import static org.objectweb.asm.Opcodes.*;

@Aurora(Type.BASE)
@SuppressWarnings("unused")
public class Injector {
    private final String invoker;
    private final String event;

    public Injector(Class<?> invoker, Class<?> event) {
        this.invoker = invoker.getName().replace(".", "/");
        this.event = event.getName().replace(".", "/");
    }

    private ClassWriter writeReturn(MethodNode method, String type, boolean ret) {
        final Label B = new Label();
        final MethodNode injected = new MethodNode();
        String called = "(L" + event + ";)Z";
        injected.visitFieldInsn(GETSTATIC, event, type, "L" + event + ";");
        injected.visitMethodInsn(INVOKESTATIC, invoker, "onEvent", called);
        injected.visitJumpInsn(IFEQ, B);
        injected.visitInsn(ret ? ICONST_1 : ICONST_0);
        injected.visitInsn(IRETURN);
        injected.visitLabel(B);
        method.instructions.insert(injected.instructions);
        return new ClassWriter(1 | 2);
    }

    private ClassWriter writeParam(MethodNode method, String type, int n, boolean m) {
        final Label B = new Label();
        final MethodNode injected = new MethodNode();
        String called = "(L" + event + ";)Z";
        injected.visitFieldInsn(GETSTATIC, event, type, "L" + event + ";");
        injected.visitMethodInsn(INVOKESTATIC, invoker, "onEvent", called);
        injected.visitJumpInsn(IFEQ, B);
        injected.visitInsn(m ? ICONST_1 : ICONST_0);
        injected.visitVarInsn(ISTORE, n);
        injected.visitLabel(B);
        method.instructions.insert(injected.instructions);
        return new ClassWriter(1 | 2);
    }

    private ClassWriter writeInvoke(MethodNode method, String name, String type) {
        final MethodNode injectedMethod = new MethodNode();
        injectedMethod.visitMethodInsn(INVOKESTATIC, invoker, name, type);
        method.instructions.insert(injectedMethod.instructions);
        return new ClassWriter(1 | 2);
    }

    public byte[] invokeStatic(byte[] bytes, String method, String type, String invoke, String params) {
        ClassNode classNode = getNode(bytes);
        ClassWriter classWriter = null;
        MethodNode methodNode = getMethod(classNode, method, type);
        if(methodNode != null) classWriter = writeInvoke(methodNode, invoke, params);
        if(classWriter != null) {
            classNode.accept(classWriter);
            return classWriter.toByteArray();
        } else return bytes;
    }

    public byte[] invokeReturn(byte[] bytes, String method, String type, String event, boolean ret) {
        ClassNode classNode = getNode(bytes);
        ClassWriter classWriter = null;
        MethodNode methodNode = getMethod(classNode, method, type);
        if(methodNode != null) classWriter = writeReturn(methodNode, event, ret);
        if(classWriter != null) {
            classNode.accept(classWriter);
            return classWriter.toByteArray();
        } else return bytes;
    }

    public byte[] invokeParam(byte[] bytes, String method, String type, String event, int param, boolean act) {
        ClassNode classNode = getNode(bytes);
        ClassWriter classWriter = null;
        MethodNode methodNode = getMethod(classNode, method, type);
        if(methodNode != null) classWriter = writeParam(methodNode, event, param, act);
        if(classWriter != null) {
            classNode.accept(classWriter);
            return classWriter.toByteArray();
        } else return bytes;
    }

    public MethodNode getMethod(ClassNode node, String method, String desc) {
        for(Object m : node.methods) {
            MethodNode methodNode = (MethodNode) m;
            if(methodNode.name.equals(method) && methodNode.desc.equals(desc)) {
                return methodNode;
            }
        } return null;
    }

    public ClassNode getNode(byte[] bytes) {
        final ClassNode classNode = new ClassNode();
        final ClassReader classReader = new ClassReader(bytes);
        classReader.accept(classNode, ClassReader.EXPAND_FRAMES);
        return classNode;
    }
}
