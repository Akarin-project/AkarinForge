package io.akarin.forge.server.layers.reflection;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

import io.akarin.forge.server.layers.ClassInheritanceProvider;
import io.akarin.forge.server.layers.MappedClassLoader;
import io.akarin.forge.server.layers.MappingLoader;
import io.akarin.forge.server.layers.SneakyRemapper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import net.md_5.specialsource.JarMapping;
import net.md_5.specialsource.provider.InheritanceProvider;
import net.md_5.specialsource.provider.JointProvider;
import net.minecraft.server.MinecraftServer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;

public class ReflectionTransformer {
	private static final String MAPPED_CLASS_LOADER = MappedClassLoader.class.getName().replace('.', '/');
	
    public static final String DESC_REFLECTION_METHOD_MAPPER  = Type.getInternalName(ReflectionMethodMapper.class);
    public static final String DESC_REFLECTION_METHOD_HANDLER = Type.getInternalName(ReflectionMethodHandler.class);
    
    public static JarMapping jarMapping;
    public static SneakyRemapper remapper;
    public static final HashMap<String, String>  REVERSE_CLASS_MAPPING     = Maps.newHashMap();
    public static final Multimap<String, String> REVERSE_METHOD_MAPPING    = HashMultimap.create();
    public static final Multimap<String, String> REVERSE_FIELD_MAPPING     = HashMultimap.create();
    public static final Multimap<String, String> SIMPLE_CLASS_NAME_MAPPING = HashMultimap.create();

    public static void init() {
        try {
            Reflections.getCallerClassloader();
        } catch (Throwable t) {
            MinecraftServer.LOGGER.error("Unsupported Java version, NMS remapping is disabled!", t);
            return;
        }
        
        jarMapping = MappingLoader.loadMapping();
        JointProvider provider = new JointProvider();
        provider.add((InheritanceProvider)new ClassInheritanceProvider());
        jarMapping.setFallbackInheritanceProvider((InheritanceProvider)provider);
        remapper = new SneakyRemapper(jarMapping);
        
        ReflectionTransformer.jarMapping.classes.forEach((k, v) -> REVERSE_CLASS_MAPPING    .put(v, k));
        ReflectionTransformer.jarMapping.methods.forEach((k, v) -> REVERSE_METHOD_MAPPING   .put(v, k));
        ReflectionTransformer.jarMapping.fields .forEach((k, v) -> REVERSE_FIELD_MAPPING    .put(v, k));
        
        ReflectionTransformer.jarMapping.methods.forEach((k, v) -> SIMPLE_CLASS_NAME_MAPPING.put(k.split("\\s+")[0], k));
    }

    @SuppressWarnings("deprecation")
	public static byte[] transform(byte[] code) {
        ClassReader reader = new ClassReader(code);
        ClassNode node = new ClassNode();
        reader.accept((ClassVisitor)node, 0);
        boolean remapCL = false;
        if (node.superName.equals("java/net/URLClassLoader")) {
            node.superName = MAPPED_CLASS_LOADER;
            remapCL = true;
        }
        for (MethodNode method : node.methods) {
            Iterator<AbstractInsnNode> iterator = method.instructions.iterator();
            while (iterator.hasNext()) {
                AbstractInsnNode next = iterator.next();
                TypeInsnNode insn;
                if (next instanceof TypeInsnNode && (insn = (TypeInsnNode)next).getOpcode() == 187 && insn.desc.equals("java/net/URLClassLoader")) {
                    insn.desc = MAPPED_CLASS_LOADER;
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
                method.instructions.insertBefore(min, new MethodInsnNode(184, "java/lang/ClassLoader", "getSystemClassLoader", "()Ljava/lang/ClassLoader;"));
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
            method.owner = DESC_REFLECTION_METHOD_HANDLER;
        }
        if (!method.owner.equals("java/lang/Class") || !method.name.equals("forName")) {
            return;
        }
        method.owner = DESC_REFLECTION_METHOD_MAPPER;
    }
    
    public static void remapVirtual(AbstractInsnNode insn) {
        MethodInsnNode method = (MethodInsnNode) insn;
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
                    ReflectionTransformer.virtualToStatic(method, DESC_REFLECTION_METHOD_HANDLER);
                }
            }
        }
        if (remapFlag) {
            ReflectionTransformer.virtualToStatic(method, DESC_REFLECTION_METHOD_MAPPER);
        }
    }

    private static void remapURLClassLoader(MethodInsnNode method) {
        if (!method.owner.equals("java/net/URLClassLoader") || !method.name.equals("<init>")) {
            return;
        }
        method.owner = MAPPED_CLASS_LOADER;
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
}

