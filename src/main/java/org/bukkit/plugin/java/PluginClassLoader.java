/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  net.md_5.specialsource.JarMapping
 *  net.md_5.specialsource.JarRemapper
 *  net.md_5.specialsource.provider.ClassLoaderProvider
 *  net.md_5.specialsource.provider.InheritanceProvider
 *  net.md_5.specialsource.provider.JointProvider
 *  net.md_5.specialsource.repo.ClassRepo
 *  net.md_5.specialsource.repo.RuntimeRepo
 *  net.minecraft.launchwrapper.LaunchClassLoader
 *  org.apache.commons.lang.Validate
 */
package org.bukkit.plugin.java;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.security.CodeSigner;
import java.security.CodeSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import net.md_5.specialsource.JarMapping;
import net.md_5.specialsource.JarRemapper;
import net.md_5.specialsource.provider.ClassLoaderProvider;
import net.md_5.specialsource.provider.InheritanceProvider;
import net.md_5.specialsource.provider.JointProvider;
import net.md_5.specialsource.repo.ClassRepo;
import net.md_5.specialsource.repo.RuntimeRepo;
import net.minecraft.launchwrapper.LaunchClassLoader;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import org.apache.commons.lang.Validate;
import org.bukkit.Server;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLoader;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import io.akarin.forge.AkarinForge;
import io.akarin.forge.remapper.AkarinServerRemapper;
import io.akarin.forge.remapper.ClassInheritanceProvider;
import io.akarin.forge.remapper.MappingLoader;
import io.akarin.forge.remapper.ReflectionTransformer;

final class PluginClassLoader
extends URLClassLoader {
    private final JavaPluginLoader loader;
    private final Map<String, Class<?>> classes = new HashMap();
    private final PluginDescriptionFile description;
    private final File dataFolder;
    private final File file;
    private final JarFile jar;
    private final Manifest manifest;
    private final URL url;
    final JavaPlugin plugin;
    private JavaPlugin pluginInit;
    private IllegalStateException pluginState;
    private JarRemapper remapper;
    private JarMapping jarMapping;

    PluginClassLoader(JavaPluginLoader loader, ClassLoader parent, PluginDescriptionFile description, File dataFolder, File file) throws IOException, InvalidPluginException, MalformedURLException {
        super(new URL[]{file.toURI().toURL()}, parent);
        Validate.notNull((Object)loader, (String)"Loader cannot be null");
        this.loader = loader;
        this.description = description;
        this.dataFolder = dataFolder;
        this.file = file;
        this.jar = new JarFile(file);
        this.manifest = this.jar.getManifest();
        this.url = file.toURI().toURL();
        this.jarMapping = MappingLoader.loadMapping();
        JointProvider provider = new JointProvider();
        provider.add((InheritanceProvider)new ClassInheritanceProvider());
        provider.add((InheritanceProvider)new ClassLoaderProvider((ClassLoader)this));
        this.jarMapping.setFallbackInheritanceProvider((InheritanceProvider)provider);
        this.remapper = new AkarinServerRemapper(this.jarMapping);
        try {
            Class jarClass;
            Class<JavaPlugin> pluginClass;
            try {
                jarClass = Class.forName(description.getMain(), true, this);
            }
            catch (ClassNotFoundException ex2) {
                throw new InvalidPluginException("Cannot find main class `" + description.getMain() + "'", ex2);
            }
            try {
                pluginClass = jarClass.asSubclass(JavaPlugin.class);
            }
            catch (ClassCastException ex3) {
                throw new InvalidPluginException("main class `" + description.getMain() + "' does not extend JavaPlugin", ex3);
            }
            this.plugin = pluginClass.newInstance();
        }
        catch (IllegalAccessException ex4) {
            throw new InvalidPluginException("No public constructor", ex4);
        }
        catch (InstantiationException ex5) {
            throw new InvalidPluginException("Abnormal plugin type", ex5);
        }
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        return this.findClass(name, true);
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    Class<?> findClass(String name, boolean checkGlobal) throws ClassNotFoundException {
        if (name.startsWith("net.minecraft.server." + AkarinForge.getNativeVersion())) {
            String remappedClass = (String)this.jarMapping.classes.get(name.replaceAll("\\.", "\\/"));
            Class clazz = ((LaunchClassLoader)MinecraftServer.getServerInst().getClass().getClassLoader()).findClass(remappedClass);
            return clazz;
        }
        if (name.startsWith("org.bukkit.")) {
            throw new ClassNotFoundException(name);
        }
        Class result = this.classes.get(name);
        String clazz = name.intern();
        synchronized (clazz) {
            if (result == null) {
                if (checkGlobal) {
                    result = this.loader.getClassByName(name);
                }
                if (result == null && (result = this.remappedFindClass(name)) != null) {
                    this.loader.setClass(name, result);
                }
                if (result == null) {
                    LaunchClassLoader lw2 = (LaunchClassLoader)MinecraftServer.getServerInst().getClass().getClassLoader();
                    try {
                        result = lw2.findClass(name);
                    }
                    catch (Throwable throwable) {
                        try {
                            lw2.addTransformerExclusion(name);
                            result = lw2.findClass(name);
                        }
                        catch (Throwable throwable1) {
                            try {
                                result = null;
                            }
                            catch (Throwable throwable2) {
                                Set set = (Set)ReflectionHelper.getPrivateValue(LaunchClassLoader.class, lw2, new String[]{"transformerExceptions"});
                                set.remove(name);
                                throw throwable2;
                            }
                            Set set = (Set)ReflectionHelper.getPrivateValue(LaunchClassLoader.class, lw2, new String[]{"transformerExceptions"});
                            set.remove(name);
                        }
                        Set set = (Set)ReflectionHelper.getPrivateValue(LaunchClassLoader.class, lw2, new String[]{"transformerExceptions"});
                        set.remove(name);
                    }
                    if (result == null) {
                        throw new ClassNotFoundException(name);
                    }
                }
                this.classes.put(name, result);
            }
        }
        return result;
    }

    @Override
    public void close() throws IOException {
        try {
            super.close();
        }
        finally {
            this.jar.close();
        }
    }

    Set<String> getClasses() {
        return this.classes.keySet();
    }

    synchronized void initialize(JavaPlugin javaPlugin) {
        Validate.notNull((Object)javaPlugin, (String)"Initializing plugin cannot be null");
        Validate.isTrue((boolean)(javaPlugin.getClass().getClassLoader() == this), (String)"Cannot initialize plugin outside of this class loader");
        if (this.plugin != null || this.pluginInit != null) {
            throw new IllegalArgumentException("Plugin already initialized!", this.pluginState);
        }
        this.pluginState = new IllegalStateException("Initial initialization");
        this.pluginInit = javaPlugin;
        javaPlugin.init(this.loader, this.loader.server, this.description, this.dataFolder, this.file, this);
    }

    private Class<?> remappedFindClass(String name) throws ClassNotFoundException {
        Class result = null;
        try {
            InputStream stream;
            String path = name.replace('.', '/').concat(".class");
            URL url = this.findResource(path);
            if (url != null && (stream = url.openStream()) != null) {
                JarURLConnection jarURLConnection;
                URL jarURL;
                CodeSource codeSource;
                byte[] bytecode = null;
                bytecode = this.remapper.remapClassFile(stream, (ClassRepo)RuntimeRepo.getInstance());
                result = this.defineClass(name, bytecode = ReflectionTransformer.transform(bytecode), 0, bytecode.length, codeSource = new CodeSource(jarURL = (jarURLConnection = (JarURLConnection)url.openConnection()).getJarFileURL(), new CodeSigner[0]));
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

    @Override
    protected Package getPackage(String name) {
        if (name == "org.bukkit.craftbukkit") {
            name = "org.bukkit.craftbukkit." + AkarinForge.getNativeVersion();
        }
        return super.getPackage(name);
    }
}

