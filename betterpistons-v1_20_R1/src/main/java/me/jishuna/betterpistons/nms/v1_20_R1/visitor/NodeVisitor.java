package me.jishuna.betterpistons.nms.v1_20_R1.visitor;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;

public interface NodeVisitor {
    public void visitNode(AbstractInsnNode node, InsnList instructions);
}
