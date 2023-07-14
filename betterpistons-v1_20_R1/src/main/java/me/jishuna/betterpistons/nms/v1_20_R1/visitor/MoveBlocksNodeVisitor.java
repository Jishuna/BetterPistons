package me.jishuna.betterpistons.nms.v1_20_R1.visitor;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.H_INVOKESTATIC;
import static org.objectweb.asm.Opcodes.ILOAD;

import java.lang.invoke.ConstantCallSite;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.List;

import org.objectweb.asm.Handle;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InvokeDynamicInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import me.jishuna.betterpistons.nms.v1_20_R1.Bootstraper;
import me.jishuna.betterpistons.nms.v1_20_R1.InternalPistonHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

public class MoveBlocksNodeVisitor implements NodeVisitor {
    private static final Handle BOOTSTRAP_METHOD = new Handle(H_INVOKESTATIC, Bootstraper.class.getCanonicalName().replace('.', '/'), "bootstrap", MethodType.methodType(ConstantCallSite.class, MethodHandles.Lookup.class, String.class, MethodType.class, String.class).toMethodDescriptorString(), false);
    private static final Type BOOTSTRAP_TYPE = Type.getMethodType(Type.VOID_TYPE, Type.getType(Level.class), Type.getType(BlockPos.class), Type.getType(Direction.class), Type.BOOLEAN_TYPE, Type.getType(List.class));

    private int index;

    @Override
    public void visitNode(AbstractInsnNode node, InsnList instructions) {
        if (node instanceof VarInsnNode varNode && varNode.var == 10 && index++ == 1) {
            instructions.insertBefore(varNode, new VarInsnNode(ALOAD, 1));
            instructions.insertBefore(varNode, new VarInsnNode(ALOAD, 2));
            instructions.insertBefore(varNode, new VarInsnNode(ALOAD, 3));
            instructions.insertBefore(varNode, new VarInsnNode(ILOAD, 4));
            instructions.insertBefore(varNode, new VarInsnNode(ALOAD, 15));
            instructions.insertBefore(varNode, new InvokeDynamicInsnNode("startMoving", BOOTSTRAP_TYPE.getDescriptor(), BOOTSTRAP_METHOD, InternalPistonHandler.class.getName()));
        }
    }
}
