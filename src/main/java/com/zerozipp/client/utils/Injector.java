package com.zerozipp.client.utils;

import com.zerozipp.client.utils.interfaces.Aurora;
import com.zerozipp.client.utils.types.Type;
import org.objectweb.asm.Label;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import static org.objectweb.asm.Opcodes.*;

@Aurora(Type.INJECT)
@SuppressWarnings({"unused", "deprecation"})
public class Injector {
    private final String invoker, event, packet;

    public Injector(Class<?> invoker, Class<?> event, Class<?> packet) {
        this.invoker = invoker.getName().replace(".", "/");
        this.event = event.getName().replace(".", "/");
        this.packet = packet.getName().replace(".", "/");
    }

    private ClassWriter writeReturn(MethodNode method, String type, int ret, int line) {
        final Label B = new Label();
        final MethodNode injected = new MethodNode();
        String called = "(L" + event + ";)Z";
        injected.visitFieldInsn(GETSTATIC, event, type, "L" + event + ";");
        injected.visitMethodInsn(INVOKESTATIC, invoker, "onEvent", called);
        injected.visitJumpInsn(IFEQ, B);
        injected.visitLdcInsn(ret);
        injected.visitInsn(IRETURN);
        injected.visitLabel(B);
        AbstractInsnNode a = method.instructions.get(line);
        method.instructions.insert(a, injected.instructions);
        return new ClassWriter(1 | 2);
    }

    private ClassWriter writeFloat(MethodNode method, String type, float ret, int line) {
        final Label B = new Label();
        final MethodNode injected = new MethodNode();
        String called = "(L" + event + ";)Z";
        injected.visitFieldInsn(GETSTATIC, event, type, "L" + event + ";");
        injected.visitMethodInsn(INVOKESTATIC, invoker, "onEvent", called);
        injected.visitJumpInsn(IFEQ, B);
        injected.visitLdcInsn(ret);
        injected.visitInsn(FRETURN);
        injected.visitLabel(B);
        AbstractInsnNode a = method.instructions.get(line);
        method.instructions.insert(a, injected.instructions);
        return new ClassWriter(1 | 2);
    }

    private ClassWriter writeReturn(MethodNode method, String type, int line) {
        final Label B = new Label();
        final MethodNode injected = new MethodNode();
        String called = "(L" + event + ";)Z";
        injected.visitFieldInsn(GETSTATIC, event, type, "L" + event + ";");
        injected.visitMethodInsn(INVOKESTATIC, invoker, "onEvent", called);
        injected.visitJumpInsn(IFEQ, B);
        injected.visitInsn(RETURN);
        injected.visitLabel(B);
        AbstractInsnNode a = method.instructions.get(line);
        method.instructions.insert(a, injected.instructions);
        return new ClassWriter(1 | 2);
    }

    private ClassWriter writeEvent(MethodNode method, String type, int line) {
        final MethodNode injected = new MethodNode();
        String called = "(L" + event + ";)Z";
        injected.visitFieldInsn(GETSTATIC, event, type, "L" + event + ";");
        injected.visitMethodInsn(INVOKESTATIC, invoker, "onEvent", called);
        AbstractInsnNode a = method.instructions.get(line);
        method.instructions.insert(a, injected.instructions);
        return new ClassWriter(1 | 2);
    }

    private ClassWriter writeParam(MethodNode method, String type, int n, Object m, int line) {
        final Label B = new Label();
        final MethodNode injected = new MethodNode();
        String called = "(L" + event + ";)Z";
        injected.visitFieldInsn(GETSTATIC, event, type, "L" + event + ";");
        injected.visitMethodInsn(INVOKESTATIC, invoker, "onEvent", called);
        int ret = (m instanceof Float) ? FSTORE : ISTORE;
        injected.visitJumpInsn(IFEQ, B);
        injected.visitLdcInsn(m);
        injected.visitVarInsn(ret, n);
        injected.visitLabel(B);
        AbstractInsnNode a = method.instructions.get(line);
        method.instructions.insert(a, injected.instructions);
        return new ClassWriter(1 | 2);
    }

    private ClassWriter writePacket(MethodNode method, int n, int line) {
        final Label A = new Label(), B = new Label();
        final MethodNode injected = new MethodNode();
        String invoke = "(L" + packet + ";)L" + packet + ";";
        String called = "(Ljava/lang/Object;)V";
        injected.visitTypeInsn(NEW, packet);
        injected.visitInsn(DUP);
        injected.visitVarInsn(ALOAD, n);
        injected.visitMethodInsn(INVOKESPECIAL, packet, "<init>", called);
        injected.visitMethodInsn(INVOKESTATIC, invoker, "onPacket", invoke);
        injected.visitMethodInsn(INVOKEVIRTUAL, packet, "isCanceled", "()Z");
        injected.visitJumpInsn(IFEQ, B);
        injected.visitInsn(RETURN);
        injected.visitLabel(B);
        AbstractInsnNode a = method.instructions.get(line);
        method.instructions.insert(a, injected.instructions);
        return new ClassWriter(1 | 2);
    }

    private ClassWriter writeInvoke(MethodNode method, String name, String type, int line) {
        final MethodNode injected = new MethodNode();
        injected.visitMethodInsn(INVOKESTATIC, invoker, name, type);
        AbstractInsnNode a = method.instructions.get(line);
        method.instructions.insert(a, injected.instructions);
        return new ClassWriter(1 | 2);
    }

    private ClassWriter writeInvoke(MethodNode method, String name, String type, int param, int line) {
        final MethodNode injected = new MethodNode();
        injected.visitVarInsn(ALOAD, param);
        injected.visitMethodInsn(INVOKESTATIC, invoker, name, type);
        AbstractInsnNode a = method.instructions.get(line);
        method.instructions.insert(a, injected.instructions);
        return new ClassWriter(1 | 2);
    }

    private ClassWriter writeInteger(MethodNode method, String name, String type, int param, int line) {
        final MethodNode injected = new MethodNode();
        injected.visitVarInsn(ILOAD, param);
        injected.visitMethodInsn(INVOKESTATIC, invoker, name, type);
        AbstractInsnNode a = method.instructions.get(line);
        method.instructions.insert(a, injected.instructions);
        return new ClassWriter(1 | 2);
    }

    public byte[] invokeStatic(byte[] bytes, String method, String type, String invoke, String params, int line) {
        ClassNode classNode = getNode(bytes);
        ClassWriter classWriter = null;
        MethodNode methodNode = getMethod(classNode, method, type);
        if(methodNode != null) classWriter = writeInvoke(methodNode, invoke, params, line);
        if(classWriter != null) {
            classNode.accept(classWriter);
            return classWriter.toByteArray();
        } else return bytes;
    }

    public byte[] invokeStatic(byte[] bytes, String method, String type, String invoke, String params, int param, int line) {
        ClassNode classNode = getNode(bytes);
        ClassWriter classWriter = null;
        MethodNode methodNode = getMethod(classNode, method, type);
        if(methodNode != null) classWriter = writeInvoke(methodNode, invoke, params, param, line);
        if(classWriter != null) {
            classNode.accept(classWriter);
            return classWriter.toByteArray();
        } else return bytes;
    }

    public byte[] invokeInteger(byte[] bytes, String method, String type, String invoke, String params, int param, int line) {
        ClassNode classNode = getNode(bytes);
        ClassWriter classWriter = null;
        MethodNode methodNode = getMethod(classNode, method, type);
        if(methodNode != null) classWriter = writeInteger(methodNode, invoke, params, param, line);
        if(classWriter != null) {
            classNode.accept(classWriter);
            return classWriter.toByteArray();
        } else return bytes;
    }

    public byte[] invokeReturn(byte[] bytes, String method, String type, String event, int ret, int line) {
        ClassNode classNode = getNode(bytes);
        ClassWriter classWriter = null;
        MethodNode methodNode = getMethod(classNode, method, type);
        if(methodNode != null) classWriter = writeReturn(methodNode, event, ret, line);
        if(classWriter != null) {
            classNode.accept(classWriter);
            return classWriter.toByteArray();
        } else return bytes;
    }

    public byte[] invokeReturn(byte[] bytes, String method, String type, String event, int line) {
        ClassNode classNode = getNode(bytes);
        ClassWriter classWriter = null;
        MethodNode methodNode = getMethod(classNode, method, type);
        if(methodNode != null) classWriter = writeReturn(methodNode, event, line);
        if(classWriter != null) {
            classNode.accept(classWriter);
            return classWriter.toByteArray();
        } else return bytes;
    }

    public byte[] invokeFloat(byte[] bytes, String method, String type, String event, float ret, int line) {
        ClassNode classNode = getNode(bytes);
        ClassWriter classWriter = null;
        MethodNode methodNode = getMethod(classNode, method, type);
        if(methodNode != null) classWriter = writeFloat(methodNode, event, ret, line);
        if(classWriter != null) {
            classNode.accept(classWriter);
            return classWriter.toByteArray();
        } else return bytes;
    }

    public byte[] invokeEvent(byte[] bytes, String method, String type, String event, int line) {
        ClassNode classNode = getNode(bytes);
        ClassWriter classWriter = null;
        MethodNode methodNode = getMethod(classNode, method, type);
        if(methodNode != null) classWriter = writeEvent(methodNode, event, line);
        if(classWriter != null) {
            classNode.accept(classWriter);
            return classWriter.toByteArray();
        } else return bytes;
    }

    public byte[] invokeParam(byte[] bytes, String method, String type, String event, int param, Object act, int line) {
        ClassNode classNode = getNode(bytes);
        ClassWriter classWriter = null;
        MethodNode methodNode = getMethod(classNode, method, type);
        if(methodNode != null) classWriter = writeParam(methodNode, event, param, act, line);
        if(classWriter != null) {
            classNode.accept(classWriter);
            return classWriter.toByteArray();
        } else return bytes;
    }

    public byte[] invokePacket(byte[] bytes, String method, String type, int param, int line) {
        ClassNode classNode = getNode(bytes);
        ClassWriter classWriter = null;
        MethodNode methodNode = getMethod(classNode, method, type);
        if(methodNode != null) classWriter = writePacket(methodNode, param, line);
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
