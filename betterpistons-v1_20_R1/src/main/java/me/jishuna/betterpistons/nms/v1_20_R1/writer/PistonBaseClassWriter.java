package me.jishuna.betterpistons.nms.v1_20_R1.writer;

import static org.objectweb.asm.Opcodes.ALOAD;

import java.util.ListIterator;
import java.util.function.Supplier;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import me.jishuna.betterpistons.nms.v1_20_R1.Utils;
import me.jishuna.betterpistons.nms.v1_20_R1.visitor.CanPushNodeVisitor;
import me.jishuna.betterpistons.nms.v1_20_R1.visitor.MoveBlocksNodeVisitor;
import me.jishuna.betterpistons.nms.v1_20_R1.visitor.NodeVisitor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class PistonBaseClassWriter {
    private static final Type CAN_PUSH_TYPE = Type.getMethodType(Type.BOOLEAN_TYPE, Type.getType(BlockState.class), Type.getType(Level.class), Type.getType(BlockPos.class), Type.getType(Direction.class), Type.BOOLEAN_TYPE, Type.getType(Direction.class));
    private static final Type MOVE_BLOCKS_TYPE = Type.getMethodType(Type.BOOLEAN_TYPE, Type.getType(Level.class), Type.getType(BlockPos.class), Type.getType(Direction.class), Type.BOOLEAN_TYPE);
    private static final Multimap<Integer, Supplier<NodeVisitor>> PUSH_VISITORS = ArrayListMultimap.create();
    private static final Multimap<Integer, Supplier<NodeVisitor>> MOVE_VISITORS = ArrayListMultimap.create();

    static {
        PUSH_VISITORS.put(ALOAD, CanPushNodeVisitor::new);
        MOVE_VISITORS.put(ALOAD, MoveBlocksNodeVisitor::new);
    }

    private final ClassReader reader;
    private final ClassWriter writer;

    public PistonBaseClassWriter(byte[] contents) {
        reader = new ClassReader(contents);
        writer = new ClassWriter(reader, ClassWriter.COMPUTE_FRAMES);
    }

    public byte[] rewrite() {
        ClassNode classNode = new ClassNode();
        reader.accept(classNode, 0);

        for (MethodNode methodNode : classNode.methods) {
            Type type = Type.getMethodType(methodNode.desc);
            if (type.equals(CAN_PUSH_TYPE)) {
                processMethod(methodNode, PUSH_VISITORS);
            } else if (type.equals(MOVE_BLOCKS_TYPE)) {
                processMethod(methodNode, MOVE_VISITORS);
            }
        }

        classNode.accept(writer);
        return writer.toByteArray();
    }

    private void processMethod(MethodNode methodNode, Multimap<Integer, Supplier<NodeVisitor>> visitorSuppliers) {
        Multimap<Integer, NodeVisitor> visitors = Utils.makeMap(visitorSuppliers);

        ListIterator<AbstractInsnNode> iterator = methodNode.instructions.iterator();
        while (iterator.hasNext()) {
            AbstractInsnNode node = iterator.next();
            visitors.get(node.getOpcode()).forEach(visitor -> visitor.visitNode(node, methodNode.instructions));
        }
    }
}
