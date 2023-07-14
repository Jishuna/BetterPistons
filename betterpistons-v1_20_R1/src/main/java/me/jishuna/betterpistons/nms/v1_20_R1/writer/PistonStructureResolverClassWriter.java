package me.jishuna.betterpistons.nms.v1_20_R1.writer;

import static org.objectweb.asm.Opcodes.BIPUSH;

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
import me.jishuna.betterpistons.nms.v1_20_R1.visitor.AddBlockLineNodeVisitor;
import me.jishuna.betterpistons.nms.v1_20_R1.visitor.NodeVisitor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public class PistonStructureResolverClassWriter {
    private static final Type ADD_BLOCK_LINE_TYPE = Type.getMethodType(Type.BOOLEAN_TYPE, Type.getType(BlockPos.class), Type.getType(Direction.class));
    private static final Multimap<Integer, Supplier<NodeVisitor>> VISITORS = ArrayListMultimap.create();

    static {
        VISITORS.put(BIPUSH, AddBlockLineNodeVisitor::new);
    }
    
    private final ClassReader reader;
    private final ClassWriter writer;

    public PistonStructureResolverClassWriter(byte[] contents) {
        reader = new ClassReader(contents);
        writer = new ClassWriter(reader, ClassWriter.COMPUTE_FRAMES);
    }

    public byte[] rewrite() {
        ClassNode classNode = new ClassNode();
        reader.accept(classNode, 0);

        for (MethodNode methodNode : classNode.methods) {
            Type type = Type.getMethodType(methodNode.desc);
            if (type.equals(ADD_BLOCK_LINE_TYPE)) {
                processMethod(methodNode);
            }
        }

        classNode.accept(writer);
        return writer.toByteArray();
    }

    private void processMethod(MethodNode methodNode) {
        Multimap<Integer, NodeVisitor> visitors = Utils.makeMap(VISITORS);

        ListIterator<AbstractInsnNode> iterator = methodNode.instructions.iterator();
        while (iterator.hasNext()) {
            AbstractInsnNode node = iterator.next();
            visitors.get(node.getOpcode()).forEach(visitor -> visitor.visitNode(node, methodNode.instructions));
        }
    }
}
