package io.akarin.forge.remapper.reflection;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

import io.akarin.forge.remapper.ClassInheritanceProvider;
import io.akarin.forge.remapper.MappingLoader;
import io.akarin.forge.remapper.SneakyRemapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import net.md_5.specialsource.JarMapping;
import net.md_5.specialsource.provider.InheritanceProvider;
import net.md_5.specialsource.provider.JointProvider;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;

public class ReflectionTransformer {
    public static final String DESC_ReflectionMethods = Type.getInternalName(ReflectionMethodMapper.class);
    public static final String DESC_RemapMethodHandle = Type.getInternalName(ReflectionMethodHandler.class);
    public static JarMapping jarMapping;
    public static SneakyRemapper remapper;
    public static final HashMap<String, String> classDeMapping;
    public static final Multimap<String, String> methodDeMapping;
    public static final Multimap<String, String> fieldDeMapping;
    public static final Multimap<String, String> methodFastMapping;
    private static boolean disable;

    public static void init() {
        try {
            Reflections.getCallerClassloader();
        }
        catch (Throwable e2) {
            new RuntimeException("Unsupported Java version, disabled reflection remap!", e2).printStackTrace();
            disable = true;
        }
        jarMapping = MappingLoader.loadMapping();
        JointProvider provider = new JointProvider();
        provider.add((InheritanceProvider)new ClassInheritanceProvider());
        jarMapping.setFallbackInheritanceProvider((InheritanceProvider)provider);
        remapper = new SneakyRemapper(jarMapping);
        ReflectionTransformer.jarMapping.classes.forEach((k2, v2) -> {
            classDeMapping.put(v2, k2);
        }
        );
        ReflectionTransformer.jarMapping.methods.forEach((k2, v2) -> {
            methodDeMapping.put(v2, k2);
        }
        );
        ReflectionTransformer.jarMapping.fields.forEach((k2, v2) -> {
            fieldDeMapping.put(v2, k2);
        }
        );
        ReflectionTransformer.jarMapping.methods.forEach((k2, v2) -> {
            methodFastMapping.put(k2.split("\\s+")[0], k2);
        }
        );
        try {
            Class.forName("io.akarin.forge.remapper.reflection.ReflectionMethodHandler");
        }
        catch (ClassNotFoundException e3) {
            e3.printStackTrace();
        }
    }

    public static byte[] transform(byte[] code) {
        ClassReader reader = new ClassReader(code);
        ClassNode node = new ClassNode();
        reader.accept((ClassVisitor)node, 0);
        boolean remapCL = false;
        if (node.superName.equals("java/net/URLClassLoader")) {
            node.superName = "io/akarin/forge/remapper/MappedClassLoader";
            remapCL = true;
        }
        for (MethodNode method : node.methods) {
            Iterator<AbstractInsnNode> iterator = method.instructions.iterator();
            while (iterator.hasNext()) {
                AbstractInsnNode next = iterator.next();
                TypeInsnNode insn;
                if (next instanceof TypeInsnNode && (insn = (TypeInsnNode)next).getOpcode() == 187 && insn.desc.equals("java/net/URLClassLoader")) {
                    insn.desc = "io/akarin/forge/remapper/MappedClassLoader";
                    remapCL = true;
                }
                if (!(next instanceof MethodInsnNode)) continue;
                
                MethodInsnNode min = (MethodInsnNode) next;
                switch (min.getOpcode()) {
                    case 182: {
                        ReflectionTransformer.remapVirtual((AbstractInsnNode)min);
                        break;
                    }
                    case 184: {
                        ReflectionTransformer.remapForName((AbstractInsnNode)min);
                        break;
                    }
                    case 183: {
                        if (!remapCL) break;
                        ReflectionTransformer.remapURLClassLoader((MethodInsnNode)min);
                    }
                }
                
                if (!min.owner.equals("javax/script/ScriptEngineManager") || !min.desc.equals("()V") || !min.name.equals("<init>")) continue;
                
                min.desc = "(Ljava/lang/ClassLoader;)V";
                method.instructions.insertBefore((AbstractInsnNode)min, (AbstractInsnNode)new MethodInsnNode(184, "java/lang/ClassLoader", "getSystemClassLoader", "()Ljava/lang/ClassLoader;"));
                ++method.maxStack;
            }
        }
        ClassWriter writer = new ClassWriter(0);
        node.accept((ClassVisitor)writer);
        return writer.toByteArray();
    }

    public static void remapForName(AbstractInsnNode insn) {
        MethodInsnNode method = (MethodInsnNode)insn;
        if (method.owner.equals("java/lang/invoke/MethodType") && method.name.equals("fromMethodDescriptorString")) {
            method.owner = DESC_RemapMethodHandle;
        }
        if (disable || !method.owner.equals("java/lang/Class") || !method.name.equals("forName")) {
            return;
        }
        method.owner = DESC_ReflectionMethods;
    }

    public static void remapVirtual(AbstractInsnNode insn) {
        MethodInsnNode method = (MethodInsnNode)insn;
        boolean remapFlag = false;
        if (method.owner.equals("java/lang/Class")) {
            switch (method.name) {
                case "getField": 
                case "getDeclaredField": 
                case "getMethod": 
                case "getDeclaredMethod": 
                case "getSimpleName": {
                    remapFlag = true;
                }
            }
        } else if (method.name.equals("getName")) {
            switch (method.owner) {
                case "java/lang/reflect/Field": 
                case "java/lang/reflect/Method": {
                    remapFlag = true;
                }
            }
        } else if (method.owner.equals("java/lang/ClassLoader") && method.name.equals("loadClass")) {
            remapFlag = true;
        } else if (method.owner.equals("java/lang/invoke/MethodHandles$Lookup")) {
            switch (method.name) {
                case "findVirtual": 
                case "findStatic": 
                case "findSpecial": 
                case "unreflect": {
                    ReflectionTransformer.virtualToStatic(method, DESC_RemapMethodHandle);
                }
            }
        }
        if (remapFlag) {
            ReflectionTransformer.virtualToStatic(method, DESC_ReflectionMethods);
        }
    }

    private static void remapURLClassLoader(MethodInsnNode method) {
        if (!method.owner.equals("java/net/URLClassLoader") || !method.name.equals("<init>")) {
            return;
        }
        method.owner = "io/akarin/forge/remapper/MappedClassLoader";
    }

    private static void virtualToStatic(MethodInsnNode method, String desc) {
        Type returnType = Type.getReturnType((String)method.desc);
        ArrayList<Type> args = new ArrayList<Type>();
        args.add(Type.getObjectType((String)method.owner));
        args.addAll(Arrays.asList(Type.getArgumentTypes((String)method.desc)));
        method.setOpcode(184);
        method.owner = desc;
        method.desc = Type.getMethodDescriptor((Type)returnType, (Type[])args.toArray(new Type[0]));
    }

    static {
        classDeMapping = Maps.newHashMap();
        methodDeMapping = ArrayListMultimap.create();
        fieldDeMapping = ArrayListMultimap.create();
        methodFastMapping = ArrayListMultimap.create();
        disable = false;
    }
}

