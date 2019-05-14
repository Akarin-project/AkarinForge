package io.akarin.forge.server.layers.reflection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.Maps;

import io.akarin.forge.server.layers.MappingLoader;
import io.akarin.forge.server.layers.Remaps;
import io.akarin.forge.server.layers.misc.Constants;

public class ReflectionMethodHandler {
    private static final Map<String, String> METHOD_DESC_MAPPING = Maps.newConcurrentMap();

    public static MethodHandle findSpecial(MethodHandles.Lookup lookup, Class<?> refc, String name, MethodType type, Class<?> specialCaller) throws NoSuchMethodException, IllegalAccessException {
        if (refc.getName().startsWith(Constants.MINECRAFT_DOMAIN)) {
            name = Remaps.remapMethod(refc, name, type.parameterArray());
        }
        return lookup.findSpecial(refc, name, type, specialCaller);
    }

    public static MethodHandle findVirtual(MethodHandles.Lookup lookup, Class<?> refc, String name, MethodType oldType) throws NoSuchMethodException, IllegalAccessException {
        if (refc.getName().startsWith(Constants.MINECRAFT_DOMAIN)) {
            name = Remaps.remapMethod(refc, name, oldType.parameterArray());
        } else if (refc.getName().equals("java.lang.Class") || refc.getName().equals("java.lang.ClassLoader")) {
            switch (name) {
                case "getField": 
                case "getDeclaredField": 
                case "getMethod": 
                case "getDeclaredMethod": 
                case "getSimpleName": 
                case "getName": 
                case "loadClass": {
                    Class<?>[] newParArr = new Class[oldType.parameterArray().length + 1];
                    newParArr[0] = refc.getName().equals("java.lang.Class") ? Class.class : ClassLoader.class;
                    System.arraycopy(oldType.parameterArray(), 0, newParArr, 1, oldType.parameterArray().length);
                    MethodType newType = MethodType.methodType(oldType.returnType(), newParArr);
                    MethodHandle handle = lookup.findStatic(ReflectionMethodMapper.class, name, newType);
                    return handle;
                }
            }
        }
        return lookup.findVirtual(refc, name, oldType);
    }

    public static MethodHandle findStatic(MethodHandles.Lookup lookup, Class<?> refc, String name, MethodType type) throws NoSuchMethodException, IllegalAccessException {
        if (refc.getName().startsWith(Constants.MINECRAFT_DOMAIN)) {
            name = Remaps.remapMethod(refc, name, type.parameterArray());
        } else if (refc.getName().equals("java.lang.Class") && name.equals("forName")) {
            refc = ReflectionMethodMapper.class;
        }
        return lookup.findStatic(refc, name, type);
    }

    public static MethodType fromMethodDescriptorString(String descriptor, ClassLoader loader) {
        String remapDesc = METHOD_DESC_MAPPING.getOrDefault(descriptor, descriptor);
        return MethodType.fromMethodDescriptorString(remapDesc, loader);
    }

    public static MethodHandle unreflect(MethodHandles.Lookup lookup, Method m2) throws IllegalAccessException {
        if (m2.getDeclaringClass().getName().equals("java.lang.Class")) {
            switch (m2.getName()) {
                case "forName": {
                    return ReflectionMethodHandler.getClassReflectionMethod(lookup, m2.getName(), String.class);
                }
                case "getField": 
                case "getDeclaredField": {
                    return ReflectionMethodHandler.getClassReflectionMethod(lookup, m2.getName(), Class.class, String.class);
                }
                case "getMethod": 
                case "getDeclaredMethod": {
                    return ReflectionMethodHandler.getClassReflectionMethod(lookup, m2.getName(), Class.class, String.class, Class[].class);
                }
                case "getSimpleName": {
                    return ReflectionMethodHandler.getClassReflectionMethod(lookup, m2.getName(), Class.class);
                }
            }
        } else if (m2.getName().equals("getName")) {
            if (m2.getDeclaringClass().getName().equals("java.lang.reflect.Field")) {
                return ReflectionMethodHandler.getClassReflectionMethod(lookup, m2.getName(), Field.class);
            }
            if (m2.getDeclaringClass().getName().equals("java.lang.reflect.Method")) {
                return ReflectionMethodHandler.getClassReflectionMethod(lookup, m2.getName(), Method.class);
            }
        } else if (m2.getName().equals("loadClass") && m2.getDeclaringClass().getName().equals("java.lang.ClassLoader")) {
            return ReflectionMethodHandler.getClassReflectionMethod(lookup, m2.getName(), ClassLoader.class, String.class);
        }
        return lookup.unreflect(m2);
    }

    private static /* varargs */ MethodHandle getClassReflectionMethod(MethodHandles.Lookup lookup, String name, Class<?> ... p2) {
        try {
            return lookup.unreflect(ReflectionMethodMapper.class.getMethod(name, p2));
        }
        catch (IllegalAccessException | NoSuchMethodException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public static void loadMappings(BufferedReader reader) throws IOException {
        String line;
        while ((line = reader.readLine()) != null) {
            int commentIndex = line.indexOf(35);
            if (commentIndex != -1) {
                line = line.substring(0, commentIndex);
            }
            if (line.isEmpty() || !line.startsWith("MD: ")) continue;
            String[] sp2 = line.split("\\s+");
            String firDesc = sp2[2];
            String secDesc = sp2[4];
            METHOD_DESC_MAPPING.put(firDesc, secDesc);
        }
    }

    static {
        try {
            ReflectionMethodHandler.loadMappings(new BufferedReader(new InputStreamReader(MappingLoader.class.getClassLoader().getResourceAsStream("mappings/" + Constants.NMS_VERSION + "/spigot.asrg"))));
        }
        catch (IOException e2) {
            e2.printStackTrace();
        }
    }
}

