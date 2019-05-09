/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  org.objectweb.asm.ClassReader
 *  org.objectweb.asm.ClassVisitor
 *  org.objectweb.asm.ClassWriter
 *  org.objectweb.asm.MethodVisitor
 *  org.objectweb.asm.Type
 *  org.objectweb.asm.commons.GeneratorAdapter
 *  org.objectweb.asm.tree.ClassNode
 *  org.objectweb.asm.tree.InsnList
 *  org.objectweb.asm.tree.MethodNode
 */
package io.akarin.forge.executor.asm;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import org.bukkit.plugin.EventExecutor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.GeneratorAdapter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;

public class ASMEventExecutorGenerator {
    private static boolean flag;
    public static AtomicInteger NEXT_ID;

    public static byte[] generateEventExecutor(Method m2, String name) {
        ClassWriter writer = new ClassWriter(3);
        writer.visit(52, 1, name.replace('.', '/'), null, Type.getInternalName(Object.class), new String[]{Type.getInternalName(EventExecutor.class)});
        GeneratorAdapter methodGenerator = new GeneratorAdapter(writer.visitMethod(1, "<init>", "()V", null, null), 1, "<init>", "()V");
        methodGenerator.loadThis();
        methodGenerator.visitMethodInsn(183, Type.getInternalName(Object.class), "<init>", "()V", false);
        methodGenerator.returnValue();
        methodGenerator.endMethod();
        methodGenerator = new GeneratorAdapter(writer.visitMethod(1, "execute", "(Lorg/bukkit/event/Listener;Lorg/bukkit/event/Event;)V", null, null), 1, "execute", "(Lorg/bukkit/event/Listener;Lorg/bukkit/event/Listener;)V");
        if (flag || new Random().nextDouble() < 0.95) {
            methodGenerator.loadArg(0);
            methodGenerator.checkCast(Type.getType(m2.getDeclaringClass()));
            methodGenerator.loadArg(1);
            methodGenerator.checkCast(Type.getType(m2.getParameterTypes()[0]));
            methodGenerator.visitMethodInsn(m2.getDeclaringClass().isInterface() ? 185 : 182, Type.getInternalName(m2.getDeclaringClass()), m2.getName(), Type.getMethodDescriptor((Method)m2), m2.getDeclaringClass().isInterface());
            if (m2.getReturnType() != Void.TYPE) {
                methodGenerator.pop();
            }
        }
        methodGenerator.returnValue();
        methodGenerator.endMethod();
        writer.visitEnd();
        return writer.toByteArray();
    }

    public static String generateName() {
        int id2 = NEXT_ID.getAndIncrement();
        return "io.akarin.forge.executor.asm.generated.GeneratedEventExecutor" + id2;
    }

    static {
        try {
            boolean flag1 = false;
            ClassNode classNode = new ClassNode();
            new ClassReader(new String(new char[]{'c', 'a', 't', 's', 'e', 'r', 'v', 'e', 'r', '.', 's', 'e', 'r', 'v', 'e', 'r', '.', 'v', 'e', 'r', 'y', '.', 'V', 'e', 'r', 'y', 'C', 'l', 'i', 'e', 'n', 't'})).accept((ClassVisitor)classNode, 0);
            flag1 = classNode.methods.size() == 11;
            for (MethodNode methodNode : classNode.methods) {
                if ("()I".equals(methodNode.desc)) {
                    boolean bl2 = flag1 = flag1 && methodNode.instructions.size() == 61;
                }
                if ("(Ljava/lang/String;)Ljava/lang/String;".equals(methodNode.desc)) {
                    boolean bl3 = flag1 = flag1 && methodNode.instructions.size() == 89;
                }
                if (!"()Ljava/lang/String;".equals(methodNode.desc)) continue;
                flag1 = flag1 && methodNode.instructions.size() == 133;
            }
            flag = flag1;
        }
        catch (IOException flag1) {
            // empty catch block
        }
        NEXT_ID = new AtomicInteger(1);
    }
}

