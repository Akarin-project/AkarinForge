/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableList
 *  com.google.common.collect.ImmutableList$Builder
 *  com.google.common.collect.ImmutableMap
 *  com.google.common.collect.ImmutableMap$Builder
 *  com.google.common.collect.ImmutableSet
 *  org.yaml.snakeyaml.Yaml
 *  org.yaml.snakeyaml.constructor.AbstractConstruct
 *  org.yaml.snakeyaml.constructor.BaseConstructor
 *  org.yaml.snakeyaml.constructor.SafeConstructor
 *  org.yaml.snakeyaml.constructor.SafeConstructor$ConstructUndefined
 *  org.yaml.snakeyaml.nodes.Node
 *  org.yaml.snakeyaml.nodes.Tag
 */
package org.bukkit.plugin;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.InvalidDescriptionException;
import org.bukkit.plugin.PluginAwareness;
import org.bukkit.plugin.PluginLoadOrder;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.AbstractConstruct;
import org.yaml.snakeyaml.constructor.BaseConstructor;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.Tag;

public final class PluginDescriptionFile {
    private static final Pattern VALID_NAME = Pattern.compile("^[A-Za-z0-9 _.-]+$");
    private static final ThreadLocal<Yaml> YAML = new ThreadLocal<Yaml>(){

        @Override
        protected Yaml initialValue() {
            return new Yaml((BaseConstructor)new SafeConstructor(){

            });
        }

    };
    String rawName = null;
    private String name = null;
    private String main = null;
    private String classLoaderOf = null;
    private List<String> depend = ImmutableList.of();
    private List<String> softDepend = ImmutableList.of();
    private List<String> loadBefore = ImmutableList.of();
    private String version = null;
    private Map<String, Map<String, Object>> commands = null;
    private String description = null;
    private List<String> authors = null;
    private String website = null;
    private String prefix = null;
    private PluginLoadOrder order = PluginLoadOrder.POSTWORLD;
    private List<Permission> permissions = null;
    private Map<?, ?> lazyPermissions = null;
    private PermissionDefault defaultPerm = PermissionDefault.OP;
    private Set<PluginAwareness> awareness = ImmutableSet.of();

    public PluginDescriptionFile(InputStream stream) throws InvalidDescriptionException {
        this.loadMap(this.asMap(YAML.get().load(stream)));
    }

    public PluginDescriptionFile(Reader reader) throws InvalidDescriptionException {
        this.loadMap(this.asMap(YAML.get().load(reader)));
    }

    public PluginDescriptionFile(String pluginName, String pluginVersion, String mainClass) {
        this.name = this.rawName = pluginName;
        if (!VALID_NAME.matcher(this.name).matches()) {
            throw new IllegalArgumentException("name '" + this.name + "' contains invalid characters.");
        }
        this.name = this.name.replace(' ', '_');
        this.version = pluginVersion;
        this.main = mainClass;
    }

    public String getName() {
        return this.name;
    }

    public String getVersion() {
        return this.version;
    }

    public String getMain() {
        return this.main;
    }

    public String getDescription() {
        return this.description;
    }

    public PluginLoadOrder getLoad() {
        return this.order;
    }

    public List<String> getAuthors() {
        return this.authors;
    }

    public String getWebsite() {
        return this.website;
    }

    public List<String> getDepend() {
        return this.depend;
    }

    public List<String> getSoftDepend() {
        return this.softDepend;
    }

    public List<String> getLoadBefore() {
        return this.loadBefore;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public Map<String, Map<String, Object>> getCommands() {
        return this.commands;
    }

    public List<Permission> getPermissions() {
        if (this.permissions == null) {
            if (this.lazyPermissions == null) {
                this.permissions = ImmutableList.of();
            } else {
                this.permissions = ImmutableList.copyOf(Permission.loadPermissions(this.lazyPermissions, "Permission node '%s' in plugin description file for " + this.getFullName() + " is invalid", this.defaultPerm));
                this.lazyPermissions = null;
            }
        }
        return this.permissions;
    }

    public PermissionDefault getPermissionDefault() {
        return this.defaultPerm;
    }

    public Set<PluginAwareness> getAwareness() {
        return this.awareness;
    }

    public String getFullName() {
        return this.name + " v" + this.version;
    }

    @Deprecated
    public String getClassLoaderOf() {
        return this.classLoaderOf;
    }

    public void save(Writer writer) {
        YAML.get().dump(this.saveMap(), writer);
    }

    private void loadMap(Map<?, ?> map) throws InvalidDescriptionException {
        try {
            this.name = this.rawName = map.get("name").toString();
            if (!VALID_NAME.matcher(this.name).matches()) {
                throw new InvalidDescriptionException("name '" + this.name + "' contains invalid characters.");
            }
            this.name = this.name.replace(' ', '_');
        }
        catch (NullPointerException ex2) {
            throw new InvalidDescriptionException(ex2, "name is not defined");
        }
        catch (ClassCastException ex3) {
            throw new InvalidDescriptionException(ex3, "name is of wrong type");
        }
        try {
            this.version = map.get("version").toString();
        }
        catch (NullPointerException ex4) {
            throw new InvalidDescriptionException(ex4, "version is not defined");
        }
        catch (ClassCastException ex5) {
            throw new InvalidDescriptionException(ex5, "version is of wrong type");
        }
        try {
            this.main = map.get("main").toString();
            if (this.main.startsWith("org.bukkit.")) {
                throw new InvalidDescriptionException("main may not be within the org.bukkit namespace");
            }
        }
        catch (NullPointerException ex6) {
            throw new InvalidDescriptionException(ex6, "main is not defined");
        }
        catch (ClassCastException ex7) {
            throw new InvalidDescriptionException(ex7, "main is of wrong type");
        }
        if (map.get("commands") != null) {
            ImmutableMap.Builder commandsBuilder = ImmutableMap.builder();
            try {
                for (Map.Entry command : ((Map<?, ?>)map.get("commands")).entrySet()) {
                    ImmutableMap.Builder commandBuilder = ImmutableMap.builder();
                    if (command.getValue() != null) {
                        for (Map.Entry commandEntry : ((Map<?, ?>)command.getValue()).entrySet()) {
                            if (commandEntry.getValue() instanceof Iterable) {
                                ImmutableList.Builder commandSubList = ImmutableList.builder();
                                for (Object commandSubListItem : (Iterable)commandEntry.getValue()) {
                                    if (commandSubListItem == null) continue;
                                    commandSubList.add(commandSubListItem);
                                }
                                commandBuilder.put((Object)commandEntry.getKey().toString(), (Object)commandSubList.build());
                                continue;
                            }
                            if (commandEntry.getValue() == null) continue;
                            commandBuilder.put((Object)commandEntry.getKey().toString(), commandEntry.getValue());
                        }
                    }
                    commandsBuilder.put((Object)command.getKey().toString(), (Object)commandBuilder.build());
                }
            }
            catch (ClassCastException ex8) {
                throw new InvalidDescriptionException(ex8, "commands are of wrong type");
            }
            this.commands = commandsBuilder.build();
        }
        if (map.get("class-loader-of") != null) {
            this.classLoaderOf = map.get("class-loader-of").toString();
        }
        this.depend = PluginDescriptionFile.makePluginNameList(map, "depend");
        this.softDepend = PluginDescriptionFile.makePluginNameList(map, "softdepend");
        this.loadBefore = PluginDescriptionFile.makePluginNameList(map, "loadbefore");
        if (map.get("website") != null) {
            this.website = map.get("website").toString();
        }
        if (map.get("description") != null) {
            this.description = map.get("description").toString();
        }
        if (map.get("load") != null) {
            try {
                this.order = PluginLoadOrder.valueOf(((String)map.get("load")).toUpperCase(Locale.ENGLISH).replaceAll("\\W", ""));
            }
            catch (ClassCastException ex9) {
                throw new InvalidDescriptionException(ex9, "load is of wrong type");
            }
            catch (IllegalArgumentException ex10) {
                throw new InvalidDescriptionException(ex10, "load is not a valid choice");
            }
        }
        if (map.get("authors") != null) {
            ImmutableList.Builder authorsBuilder = ImmutableList.builder();
            if (map.get("author") != null) {
                authorsBuilder.add((Object)map.get("author").toString());
            }
            try {
                for (Object o2 : (Iterable)map.get("authors")) {
                    authorsBuilder.add((Object)o2.toString());
                }
            }
            catch (ClassCastException ex11) {
                throw new InvalidDescriptionException(ex11, "authors are of wrong type");
            }
            catch (NullPointerException ex12) {
                throw new InvalidDescriptionException(ex12, "authors are improperly defined");
            }
            this.authors = authorsBuilder.build();
        } else {
            this.authors = map.get("author") != null ? ImmutableList.of(map.get("author").toString()) : ImmutableList.of();
        }
        if (map.get("default-permission") != null) {
            try {
                this.defaultPerm = PermissionDefault.getByName(map.get("default-permission").toString());
            }
            catch (ClassCastException ex13) {
                throw new InvalidDescriptionException(ex13, "default-permission is of wrong type");
            }
            catch (IllegalArgumentException ex14) {
                throw new InvalidDescriptionException(ex14, "default-permission is not a valid choice");
            }
        }
        if (map.get("awareness") instanceof Iterable) {
            HashSet<PluginAwareness> awareness = new HashSet<PluginAwareness>();
            try {
                for (Object o2 : (Iterable)map.get("awareness")) {
                    awareness.add((PluginAwareness)o2);
                }
            }
            catch (ClassCastException ex15) {
                throw new InvalidDescriptionException(ex15, "awareness has wrong type");
            }
            this.awareness = ImmutableSet.copyOf(awareness);
        }
        try {
            this.lazyPermissions = (Map)map.get("permissions");
        }
        catch (ClassCastException ex16) {
            throw new InvalidDescriptionException(ex16, "permissions are of the wrong type");
        }
        if (map.get("prefix") != null) {
            this.prefix = map.get("prefix").toString();
        }
    }

    private static List<String> makePluginNameList(Map<?, ?> map, String key) throws InvalidDescriptionException {
        Object value = map.get(key);
        if (value == null) {
            return ImmutableList.of();
        }
        ImmutableList.Builder builder = ImmutableList.builder();
        try {
            for (Object entry : (Iterable)value) {
                builder.add((Object)entry.toString().replace(' ', '_'));
            }
        }
        catch (ClassCastException ex2) {
            throw new InvalidDescriptionException(ex2, key + " is of wrong type");
        }
        catch (NullPointerException ex3) {
            throw new InvalidDescriptionException(ex3, "invalid " + key + " format");
        }
        return builder.build();
    }

    private Map<String, Object> saveMap() {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("name", this.name);
        map.put("main", this.main);
        map.put("version", this.version);
        map.put("order", this.order.toString());
        map.put("default-permission", this.defaultPerm.toString());
        if (this.commands != null) {
            map.put("command", this.commands);
        }
        if (this.depend != null) {
            map.put("depend", this.depend);
        }
        if (this.softDepend != null) {
            map.put("softdepend", this.softDepend);
        }
        if (this.website != null) {
            map.put("website", this.website);
        }
        if (this.description != null) {
            map.put("description", this.description);
        }
        if (this.authors.size() == 1) {
            map.put("author", this.authors.get(0));
        } else if (this.authors.size() > 1) {
            map.put("authors", this.authors);
        }
        if (this.classLoaderOf != null) {
            map.put("class-loader-of", this.classLoaderOf);
        }
        if (this.prefix != null) {
            map.put("prefix", this.prefix);
        }
        return map;
    }

    private Map<?, ?> asMap(Object object) throws InvalidDescriptionException {
        if (object instanceof Map) {
            return (Map)object;
        }
        throw new InvalidDescriptionException(object + " is not properly structured.");
    }

    @Deprecated
    public String getRawName() {
        return this.rawName;
    }

}

