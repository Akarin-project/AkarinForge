package org.bukkit.plugin.java;

import io.akarin.forge.server.layers.ClassInheritanceProvider;
import io.akarin.forge.server.layers.MappingLoader;
import io.akarin.forge.server.layers.SneakyRemapper;
import io.akarin.forge.server.layers.misc.Constants;
import io.akarin.forge.server.layers.reflection.ReflectionTransformer;
import net.md_5.specialsource.JarMapping;
import net.md_5.specialsource.JarRemapper;
import net.md_5.specialsource.provider.ClassLoaderProvider;
import net.md_5.specialsource.provider.InheritanceProvider;
import net.md_5.specialsource.provider.JointProvider;
import net.md_5.specialsource.repo.RuntimeRepo;
import net.minecraft.launchwrapper.LaunchClassLoader;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.security.CodeSigner;
import java.security.CodeSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.apache.commons.lang.Validate;
import org.bukkit.plugin.InvalidPluginException;
import org.bukkit.plugin.PluginDescriptionFile;

/**
 * A ClassLoader for plugins, to allow shared classes across multiple plugins
 */
final class PluginClassLoader extends URLClassLoader {
    private final JavaPluginLoader loader;
    private final Map<String, Class<?>> classes = new HashMap<String, Class<?>>();
    private final PluginDescriptionFile description;
    private final File dataFolder;
    private final File file;
    private final JarFile jar;
    private final Manifest manifest;
    private final URL url;
    final JavaPlugin plugin;
    private JavaPlugin pluginInit;
    private IllegalStateException pluginState;
    // Akarin start
    private JarRemapper remapper;
    private JarMapping jarMapping;
    
    private Class<?> remapClass(String name) throws ClassNotFoundException {
        Class<?> result = null;
        try {
            InputStream stream;
            String path = name.replace('.', '/').concat(".class");
            URL url = this.findResource(path);
            if (url != null && (stream = url.openStream()) != null) {
                byte[] bytecode = this.remapper.remapClassFile(stream, RuntimeRepo.getInstance());
                
                result = this.defineClass(name, bytecode = ReflectionTransformer.transform(bytecode), 0, bytecode.length, new CodeSource(((JarURLConnection) url.openConnection()).getJarFileURL(), new CodeSigner[0]));
                if (result != null) {
                    this.resolveClass(result);
                }
            }
        }
        catch (Throwable t) {
            throw new ClassNotFoundException("Failed to remap class " + name, t);
        }
        return result;
    }

    @Override
    protected Package getPackage(String name) {
        if (name.startsWith(Constants.OBC_PREFIX_DOMAIN))
            name = Constants.OBC_DOMAIN;
        
        return super.getPackage(name);
    }
    // Akarin end

    PluginClassLoader(final JavaPluginLoader loader, final ClassLoader parent, final PluginDescriptionFile description, final File dataFolder, final File file) throws IOException, InvalidPluginException, MalformedURLException {
        super(new URL[] {file.toURI().toURL()}, parent);
        Validate.notNull(loader, "Loader cannot be null");

        this.loader = loader;
        this.description = description;
        this.dataFolder = dataFolder;
        this.file = file;
        this.jar = new JarFile(file);
        this.manifest = jar.getManifest();
        this.url = file.toURI().toURL();
        // Akarin start
        this.jarMapping = MappingLoader.loadMapping();
        
        JointProvider provider = new JointProvider();
        provider.add((InheritanceProvider)new ClassInheritanceProvider());
        provider.add((InheritanceProvider)new ClassLoaderProvider((ClassLoader)this));
        this.jarMapping.setFallbackInheritanceProvider((InheritanceProvider)provider);
        
        this.remapper = new SneakyRemapper(this.jarMapping);
        // Akarin end

        try {
            Class<?> jarClass;
            try {
                jarClass = Class.forName(description.getMain(), true, this);
            } catch (ClassNotFoundException ex) {
                throw new InvalidPluginException("Cannot find main class `" + description.getMain() + "'", ex);
            }

            Class<? extends JavaPlugin> pluginClass;
            try {
                pluginClass = jarClass.asSubclass(JavaPlugin.class);
            } catch (ClassCastException ex) {
                throw new InvalidPluginException("main class `" + description.getMain() + "' does not extend JavaPlugin", ex);
            }

            plugin = pluginClass.newInstance();
        } catch (IllegalAccessException ex) {
            throw new InvalidPluginException("No public constructor", ex);
        } catch (InstantiationException ex) {
            throw new InvalidPluginException("Abnormal plugin type", ex);
        }
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        return findClass(name, true);
    }

    Class<?> findClass(String name, boolean checkGlobal) throws ClassNotFoundException {
    	// Akarin start - remapping NMS classes
        if (name.startsWith(Constants.NMS_DOMAIN)) {
            String remappedClass = this.jarMapping.classes.get(name.replaceAll("\\.", "\\/"));
            Class<?> clazz = ((LaunchClassLoader) MinecraftServer.instance().getClass().getClassLoader()).findClass(remappedClass);
            return clazz;
        }
        if (name.startsWith("org.bukkit.")) {
        // Akarin end
            throw new ClassNotFoundException(name);
        }
        Class<?> result = classes.get(name);

        if (result == null) {
            if (checkGlobal) {
                result = loader.getClassByName(name);
            }

            // Akarin start
            if (result == null && (result = this.remapClass(name)) != null) {
                this.loader.setClass(name, result);
            }
            
            if (result == null) {
                LaunchClassLoader classLoader = (LaunchClassLoader) MinecraftServer.instance().getClass().getClassLoader();
                
                try {
                    result = classLoader.findClass(name);
                } catch (Throwable throwable) {
                    classLoader.addTransformerExclusion(name);
                    result = classLoader.findClass(name);
                    @SuppressWarnings("deprecation")
					Set<?> set = ReflectionHelper.getPrivateValue(LaunchClassLoader.class, classLoader, "transformerExceptions");
                    set.remove(name);
                }
                
                if (result == null)
                    throw new ClassNotFoundException(name);
            }
            
            /* // Akarin end
            if (result == null) {
                String path = name.replace('.', '/').concat(".class");
                JarEntry entry = jar.getJarEntry(path);

                if (entry != null) {
                    byte[] classBytes;

                    try (InputStream is = jar.getInputStream(entry)) {
                        classBytes = ByteStreams.toByteArray(is);
                    } catch (IOException ex) {
                        throw new ClassNotFoundException(name, ex);
                    }

                    int dot = name.lastIndexOf('.');
                    if (dot != -1) {
                        String pkgName = name.substring(0, dot);
                        if (getPackage(pkgName) == null) {
                            try {
                                if (manifest != null) {
                                    definePackage(pkgName, manifest, url);
                                } else {
                                    definePackage(pkgName, null, null, null, null, null, null, null);
                                }
                            } catch (IllegalArgumentException ex) {
                                if (getPackage(pkgName) == null) {
                                    throw new IllegalStateException("Cannot find package " + pkgName);
                                }
                            }
                        }
                    }

                    CodeSigner[] signers = entry.getCodeSigners();
                    CodeSource source = new CodeSource(url, signers);

                    result = defineClass(name, classBytes, 0, classBytes.length, source);
                }

                if (result == null) {
                    result = super.findClass(name);
                }

                if (result != null) {
                    loader.setClass(name, result);
                }
            }
            */ // Akarin

            classes.put(name, result);
        }

        return result;
    }

    @Override
    public void close() throws IOException {
        try {
            super.close();
        } finally {
            jar.close();
        }
    }

    Set<String> getClasses() {
        return classes.keySet();
    }

    synchronized void initialize(JavaPlugin javaPlugin) {
        Validate.notNull(javaPlugin, "Initializing plugin cannot be null");
        Validate.isTrue(javaPlugin.getClass().getClassLoader() == this, "Cannot initialize plugin outside of this class loader");
        if (this.plugin != null || this.pluginInit != null) {
            throw new IllegalArgumentException("Plugin already initialized!", pluginState);
        }

        pluginState = new IllegalStateException("Initial initialization");
        this.pluginInit = javaPlugin;

        javaPlugin.init(loader, loader.server, description, dataFolder, file, this);
    }
}
