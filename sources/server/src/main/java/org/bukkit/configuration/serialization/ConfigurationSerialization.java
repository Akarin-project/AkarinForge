/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang.Validate
 */
package org.bukkit.configuration.serialization;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang.Validate;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.block.banner.Pattern;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.util.BlockVector;
import org.bukkit.util.Vector;

public class ConfigurationSerialization {
    public static final String SERIALIZED_TYPE_KEY = "==";
    private final Class<? extends ConfigurationSerializable> clazz;
    private static Map<String, Class<? extends ConfigurationSerializable>> aliases = new HashMap<String, Class<? extends ConfigurationSerializable>>();

    protected ConfigurationSerialization(Class<? extends ConfigurationSerializable> clazz) {
        this.clazz = clazz;
    }

    protected Method getMethod(String name, boolean isStatic) {
        try {
            Method method = this.clazz.getDeclaredMethod(name, Map.class);
            if (!ConfigurationSerializable.class.isAssignableFrom(method.getReturnType())) {
                return null;
            }
            if (Modifier.isStatic(method.getModifiers()) != isStatic) {
                return null;
            }
            return method;
        }
        catch (NoSuchMethodException ex2) {
            return null;
        }
        catch (SecurityException ex3) {
            return null;
        }
    }

    protected Constructor<? extends ConfigurationSerializable> getConstructor() {
        try {
            return this.clazz.getConstructor(Map.class);
        }
        catch (NoSuchMethodException ex2) {
            return null;
        }
        catch (SecurityException ex3) {
            return null;
        }
    }

    protected ConfigurationSerializable deserializeViaMethod(Method method, Map<String, ?> args) {
        block3 : {
            try {
                ConfigurationSerializable result = (ConfigurationSerializable)method.invoke(null, args);
                if (result == null) {
                    Logger.getLogger(ConfigurationSerialization.class.getName()).log(Level.SEVERE, "Could not call method '" + method.toString() + "' of " + this.clazz + " for deserialization: method returned null");
                    break block3;
                }
                return result;
            }
            catch (Throwable ex2) {
                Logger.getLogger(ConfigurationSerialization.class.getName()).log(Level.SEVERE, "Could not call method '" + method.toString() + "' of " + this.clazz + " for deserialization", ex2 instanceof InvocationTargetException ? ex2.getCause() : ex2);
            }
        }
        return null;
    }

    protected ConfigurationSerializable deserializeViaCtor(Constructor<? extends ConfigurationSerializable> ctor, Map<String, ?> args) {
        try {
            return ctor.newInstance(args);
        }
        catch (Throwable ex2) {
            Logger.getLogger(ConfigurationSerialization.class.getName()).log(Level.SEVERE, "Could not call constructor '" + ctor.toString() + "' of " + this.clazz + " for deserialization", ex2 instanceof InvocationTargetException ? ex2.getCause() : ex2);
            return null;
        }
    }

    public ConfigurationSerializable deserialize(Map<String, ?> args) {
        Constructor<? extends ConfigurationSerializable> constructor;
        Validate.notNull(args, (String)"Args must not be null");
        ConfigurationSerializable result = null;
        Method method = null;
        if (result == null && (method = this.getMethod("deserialize", true)) != null) {
            result = this.deserializeViaMethod(method, args);
        }
        if (result == null && (method = this.getMethod("valueOf", true)) != null) {
            result = this.deserializeViaMethod(method, args);
        }
        if (result == null && (constructor = this.getConstructor()) != null) {
            result = this.deserializeViaCtor(constructor, args);
        }
        return result;
    }

    public static ConfigurationSerializable deserializeObject(Map<String, ?> args, Class<? extends ConfigurationSerializable> clazz) {
        return new ConfigurationSerialization(clazz).deserialize(args);
    }

    public static ConfigurationSerializable deserializeObject(Map<String, ?> args) {
        Class<? extends ConfigurationSerializable> clazz;
        block5 : {
            clazz = null;
            if (args.containsKey("==")) {
                try {
                    String alias = (String)args.get("==");
                    if (alias == null) {
                        throw new IllegalArgumentException("Cannot have null alias");
                    }
                    clazz = ConfigurationSerialization.getClassByAlias(alias);
                    if (clazz == null) {
                        throw new IllegalArgumentException("Specified class does not exist ('" + alias + "')");
                    }
                    break block5;
                }
                catch (ClassCastException ex2) {
                    ex2.fillInStackTrace();
                    throw ex2;
                }
            }
            throw new IllegalArgumentException("Args doesn't contain type key ('==')");
        }
        return new ConfigurationSerialization(clazz).deserialize(args);
    }

    public static void registerClass(Class<? extends ConfigurationSerializable> clazz) {
        DelegateDeserialization delegate = clazz.getAnnotation(DelegateDeserialization.class);
        if (delegate == null) {
            ConfigurationSerialization.registerClass(clazz, ConfigurationSerialization.getAlias(clazz));
            ConfigurationSerialization.registerClass(clazz, clazz.getName());
        }
    }

    public static void registerClass(Class<? extends ConfigurationSerializable> clazz, String alias) {
        aliases.put(alias, clazz);
    }

    public static void unregisterClass(String alias) {
        aliases.remove(alias);
    }

    public static void unregisterClass(Class<? extends ConfigurationSerializable> clazz) {
        while (aliases.values().remove(clazz)) {
        }
    }

    public static Class<? extends ConfigurationSerializable> getClassByAlias(String alias) {
        return aliases.get(alias);
    }

    public static String getAlias(Class<? extends ConfigurationSerializable> clazz) {
        SerializableAs alias;
        DelegateDeserialization delegate = clazz.getAnnotation(DelegateDeserialization.class);
        if (delegate != null) {
            if (delegate.value() == null || delegate.value() == clazz) {
                delegate = null;
            } else {
                return ConfigurationSerialization.getAlias(delegate.value());
            }
        }
        if (delegate == null && (alias = clazz.getAnnotation(SerializableAs.class)) != null && alias.value() != null) {
            return alias.value();
        }
        return clazz.getName();
    }

    static {
        ConfigurationSerialization.registerClass(Vector.class);
        ConfigurationSerialization.registerClass(BlockVector.class);
        ConfigurationSerialization.registerClass(ItemStack.class);
        ConfigurationSerialization.registerClass(Color.class);
        ConfigurationSerialization.registerClass(PotionEffect.class);
        ConfigurationSerialization.registerClass(FireworkEffect.class);
        ConfigurationSerialization.registerClass(Pattern.class);
        ConfigurationSerialization.registerClass(Location.class);
        ConfigurationSerialization.registerClass(AttributeModifier.class);
    }
}

