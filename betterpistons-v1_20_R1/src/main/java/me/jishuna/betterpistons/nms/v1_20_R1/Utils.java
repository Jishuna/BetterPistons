package me.jishuna.betterpistons.nms.v1_20_R1;

import java.util.Map.Entry;
import java.util.function.Supplier;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import me.jishuna.betterpistons.nms.v1_20_R1.visitor.NodeVisitor;

public class Utils {
    public static Multimap<Integer, NodeVisitor> makeMap(Multimap<Integer, Supplier<NodeVisitor>> visitorSuppliers) {
        Multimap<Integer, NodeVisitor> visitors = ArrayListMultimap.create();

        for (Entry<Integer, Supplier<NodeVisitor>> entry : visitorSuppliers.entries()) {
            visitors.put(entry.getKey(), entry.getValue().get());
        }
        return visitors;
    }
}
