package io.akarin.forge.remapper;

import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;
import java.security.CodeSigner;
import java.security.CodeSource;
import java.util.Map;
import net.md_5.specialsource.JarMapping;
import net.md_5.specialsource.JarRemapper;
import net.md_5.specialsource.provider.ClassLoaderProvider;
import net.md_5.specialsource.provider.InheritanceProvider;
import net.md_5.specialsource.provider.JointProvider;
import net.md_5.specialsource.repo.ClassRepo;
import net.md_5.specialsource.repo.RuntimeRepo;
import net.minecraft.launchwrapper.LaunchClassLoader;
import net.minecraft.server.MinecraftServer;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.ConfigurationSerialization;

import com.google.common.collect.Maps;

import io.akarin.forge.remapper.reflection.ReflectionTransformer;
import io.akarin.forge.server.utility.Constants;

public class MappedClassLoader extends URLClassLoader {
    private JarMapping jarMapping = MappingLoader.loadMapping();
    private JarRemapper remapper;
    private final Map<String, Class<?>> classes = Maps.newHashMap();

    public MappedClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
        initalizeRemapper();
    }

    public MappedClassLoader(URL[] urls) {
        super(urls);
        initalizeRemapper();
    }

    public MappedClassLoader(URL[] urls, ClassLoader parent, URLStreamHandlerFactory factory) {
        super(urls, parent, factory);
        initalizeRemapper();
    }
    
    private void initalizeRemapper() {
        JointProvider provider = new JointProvider();
        provider.add((InheritanceProvider)new ClassInheritanceProvider());
        provider.add((InheritanceProvider)new ClassLoaderProvider((ClassLoader)this));
        this.jarMapping.setFallbackInheritanceProvider((InheritanceProvider)provider);
        this.remapper = new SneakyRemapper(this.jarMapping);
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        return this.findClass(name, true);
    }

    private Class<?> findClass(String name, boolean checkGlobal) throws ClassNotFoundException {
        if (name.startsWith("net.minecraft.server." + Constants.NMS_VERSION)) {
            String remappedClass = (String)this.jarMapping.classes.get(name.replaceAll("\\.", "\\/"));
            Class<?> clazz = ((LaunchClassLoader)MinecraftServer.instance().getClass().getClassLoader()).findClass(remappedClass);
            return clazz;
        }
        Class<?> result = this.classes.get(name);
        String clazz = name.intern();
        synchronized (clazz) {
            if (result == null) {
                result = this.remappedFindClass(name);
                if (result != null) {
                    this.setClass(name, result);
                }
                if (result == null) {
                    try {
                        result = super.findClass(name);
                    }
                    catch (ClassNotFoundException e2) {
                        result = ((LaunchClassLoader)MinecraftServer.instance().getClass().getClassLoader()).findClass(name);
                    }
                }
                if (result == null) {
                    throw new ClassNotFoundException(name);
                }
                this.classes.put(name, result);
            }
        }
        return result;
    }

    private Class<?> remappedFindClass(String name) throws ClassNotFoundException {
        Class<?> result = null;
        try {
            InputStream stream;
            String path = name.replace('.', '/').concat(".class");
            URL url = this.findResource(path);
            if (url != null && (stream = url.openStream()) != null) {
                byte[] bytecode = this.remapper.remapClassFile(stream, (ClassRepo)RuntimeRepo.getInstance());
                result = this.defineClass(name, bytecode = ReflectionTransformer.transform(bytecode), 0, bytecode.length, new CodeSource(((JarURLConnection) url.openConnection()).getJarFileURL(), new CodeSigner[0]));
                if (result != null) {
                    this.resolveClass(result);
                }
            }
        }
        catch (Throwable t2) {
            throw new ClassNotFoundException("Failed to remap class " + name, t2);
        }
        return result;
    }

    void setClass(String name, Class<?> clazz) {
        if (!this.classes.containsKey(name)) {
            this.classes.put(name, clazz);
            if (ConfigurationSerializable.class.isAssignableFrom(clazz)) {
                Class<? extends ConfigurationSerializable> serializable = clazz.asSubclass(ConfigurationSerializable.class);
                ConfigurationSerialization.registerClass(serializable);
            }
        }
    }
}

