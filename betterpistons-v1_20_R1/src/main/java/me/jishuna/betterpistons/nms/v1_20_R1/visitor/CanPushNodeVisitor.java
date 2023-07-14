package me.jishuna.betterpistons.nms.v1_20_R1.visitor;

import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.H_INVOKESTATIC;
import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.IRETURN;

import java.lang.invoke.ConstantCallSite;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.InvokeDynamicInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.VarInsnNode;

import me.jishuna.betterpistons.nms.v1_20_R1.Bootstraper;
import me.jishuna.betterpistons.nms.v1_20_R1.InternalPistonHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class CanPushNodeVisitor implements NodeVisitor {
    private static final Handle BOOTSTRAP_METHOD = new Handle(H_INVOKESTATIC, Bootstraper.class.getCanonicalName().replace('.', '/'), "bootstrap", MethodType.methodType(ConstantCallSite.class, MethodHandles.Lookup.class, String.class, MethodType.class, String.class).toMethodDescriptorString(), false);
    private static final Type BOOTSTRAP_TYPE = Type.getMethodType(Type.BOOLEAN_TYPE, Type.getType(BlockState.class), Type.getType(Level.class), Type.getType(BlockPos.class), Type.getType(Direction.class), Type.BOOLEAN_TYPE, Type.getType(Direction.class));

    private int index;

    @Override
    public void visitNode(AbstractInsnNode node, InsnList instructions) {
        if (index++ == 0) {
            instructions.clear();

            Label label0 = new Label();
            LabelNode labelNode = new LabelNode(label0);
            instructions.add(labelNode);
            instructions.add(new LineNumberNode(68, labelNode));
            instructions.add(new VarInsnNode(ALOAD, 0));
            instructions.add(new VarInsnNode(ALOAD, 1));
            instructions.add(new VarInsnNode(ALOAD, 2));
            instructions.add(new VarInsnNode(ALOAD, 3));
            instructions.add(new VarInsnNode(ILOAD, 4));
            instructions.add(new VarInsnNode(ALOAD, 5));
            instructions.add(new InvokeDynamicInsnNode("canPush", BOOTSTRAP_TYPE.getDescriptor(), BOOTSTRAP_METHOD, InternalPistonHandler.class.getName()));
            instructions.add(new InsnNode(IRETURN));
        }
    }
}
