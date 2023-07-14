package me.jishuna.betterpistons.nms.v1_20_R1.visitor;

import static org.objectweb.asm.Opcodes.H_INVOKESTATIC;

import java.lang.invoke.ConstantCallSite;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import org.objectweb.asm.Handle;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InvokeDynamicInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;

import me.jishuna.betterpistons.nms.v1_20_R1.Bootstraper;
import me.jishuna.betterpistons.nms.v1_20_R1.InternalPistonHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class TickNodeVisitor implements NodeVisitor {
    private static final Handle BOOTSTRAP_METHOD = new Handle(H_INVOKESTATIC, Bootstraper.class.getCanonicalName().replace('.', '/'), "bootstrap", MethodType.methodType(ConstantCallSite.class, MethodHandles.Lookup.class, String.class, MethodType.class, String.class).toMethodDescriptorString(), false);

    private static final Type TARGET_TYPE = Type.getMethodType(Type.BOOLEAN_TYPE, Type.getType(BlockPos.class), Type.getType(BlockState.class), Type.INT_TYPE);
    private static final Type BOOTSTRAP_TYPE = Type.getMethodType(Type.BOOLEAN_TYPE, Type.getType(Level.class), Type.getType(BlockPos.class), Type.getType(BlockState.class), Type.INT_TYPE);

    @Override
    public void visitNode(AbstractInsnNode node, InsnList instructions) {
        if (node instanceof MethodInsnNode methodNode && Type.getType(methodNode.desc).equals(TARGET_TYPE)) {
            instructions.insertBefore(methodNode, new InvokeDynamicInsnNode("finishMovingBlock", BOOTSTRAP_TYPE.getDescriptor(), BOOTSTRAP_METHOD, InternalPistonHandler.class.getName()));
            instructions.remove(methodNode);

        }
    }

}
