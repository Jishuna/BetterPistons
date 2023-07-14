package me.jishuna.betterpistons.nms.v1_20_R1.visitor;

import static org.objectweb.asm.Opcodes.H_INVOKESTATIC;

import java.lang.invoke.ConstantCallSite;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import org.objectweb.asm.Handle;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.InvokeDynamicInsnNode;

import me.jishuna.betterpistons.nms.v1_20_R1.Bootstraper;
import me.jishuna.betterpistons.nms.v1_20_R1.InternalPistonHandler;

public class AddBlockLineNodeVisitor implements NodeVisitor {
    private static final Handle BOOTSTRAP_METHOD = new Handle(H_INVOKESTATIC, Bootstraper.class.getCanonicalName().replace('.', '/'), "bootstrap", MethodType.methodType(ConstantCallSite.class, MethodHandles.Lookup.class, String.class, MethodType.class, String.class).toMethodDescriptorString(), false);

    private static final Type BOOTSTRAP_TYPE = Type.getMethodType(Type.INT_TYPE);

    @Override
    public void visitNode(AbstractInsnNode node, InsnList instructions) {
        if (node instanceof IntInsnNode intNode && intNode.operand == 12) {
            instructions.insertBefore(intNode, new InvokeDynamicInsnNode("getPistonPushLimit", BOOTSTRAP_TYPE.getDescriptor(), BOOTSTRAP_METHOD, InternalPistonHandler.class.getName()));
            instructions.remove(intNode);
        }
    }
}
