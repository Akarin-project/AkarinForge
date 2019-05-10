/*
 * Decompiled with CFR 0_119.
 * 
 * Could not load the following classes:
 *  net.minecraft.launchwrapper.IClassTransformer
 *  org.objectweb.asm.ClassReader
 *  org.objectweb.asm.ClassVisitor
 *  org.objectweb.asm.ClassWriter
 *  org.objectweb.asm.tree.ClassNode
 *  org.objectweb.asm.tree.MethodNode
 */
package io.akarin.forge.remapper;

import java.util.List;
import java.util.function.Predicate;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public class SideTransformer
implements IClassTransformer {
    private final Predicate<? super MethodNode> filter = method -> method.desc.contains("Lnet/minecraft/client/") && (method.desc.contains("Lnet/minecraft/client/util/ITooltipFlag") || method.desc.contains("Lnet/minecraft/client/renderer/") && !method.desc.contains("Lnet/minecraft/client/renderer/block/model/ModelResourceLocation"));

    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        if (basicClass == null) {
            return basicClass;
        }
        ClassReader reader = new ClassReader(basicClass);
        ClassNode node = new ClassNode();
        reader.accept((ClassVisitor)node, 0);
        boolean removed = node.methods.removeIf(this.filter);
        if (removed) {
            ClassWriter writer = new ClassWriter(0);
            node.accept((ClassVisitor)writer);
            return writer.toByteArray();
        }
        return basicClass;
    }
}

