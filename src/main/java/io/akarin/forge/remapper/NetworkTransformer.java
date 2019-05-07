/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  net.minecraft.launchwrapper.IClassTransformer
 *  org.objectweb.asm.ClassReader
 *  org.objectweb.asm.ClassVisitor
 *  org.objectweb.asm.ClassWriter
 *  org.objectweb.asm.tree.ClassNode
 */
package io.akarin.forge.remapper;

import java.lang.reflect.Field;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

public class NetworkTransformer
implements IClassTransformer {
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        block8 : {
            if (basicClass == null) {
                return basicClass;
            }
            if (transformedName.equals("net.minecraftforge.fml.common.network.handshake.NetworkDispatcher$1")) {
                basicClass = this.transformClass(basicClass);
            }
        }
        return basicClass;
    }

    private byte[] transformClass(byte[] basicClass) {
        ClassNode classNode = new ClassNode();
        new ClassReader(basicClass).accept((ClassVisitor)classNode, 0);
        ClassWriter classWriter = new ClassWriter(1);
        classNode.access = 33;
        classNode.accept((ClassVisitor)classWriter);
        return classWriter.toByteArray();
    }
}

