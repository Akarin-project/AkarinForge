/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang.Validate
 */
package org.bukkit.configuration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.lang.Validate;
import org.bukkit.Color;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationOptions;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;

public class MemorySection
implements ConfigurationSection {
    protected final Map<String, Object> map = new LinkedHashMap<String, Object>();
    private final Configuration root;
    private final ConfigurationSection parent;
    private final String path;
    private final String fullPath;

    protected MemorySection() {
        if (!(this instanceof Configuration)) {
            throw new IllegalStateException("Cannot construct a root MemorySection when not a Configuration");
        }
        this.path = "";
        this.fullPath = "";
        this.parent = null;
        this.root = (Configuration)((Object)this);
    }

    protected MemorySection(ConfigurationSection parent, String path) {
        Validate.notNull((Object)parent, (String)"Parent cannot be null");
        Validate.notNull((Object)path, (String)"Path cannot be null");
        this.path = path;
        this.parent = parent;
        this.root = parent.getRoot();
        Validate.notNull((Object)this.root, (String)"Path cannot be orphaned");
        this.fullPath = MemorySection.createPath(parent, path);
    }

    @Override
    public Set<String> getKeys(boolean deep) {
        ConfigurationSection defaults;
        LinkedHashSet<String> result = new LinkedHashSet<String>();
        Configuration root = this.getRoot();
        if (root != null && root.options().copyDefaults() && (defaults = this.getDefaultSection()) != null) {
            result.addAll(defaults.getKeys(deep));
        }
        this.mapChildrenKeys(result, this, deep);
        return result;
    }

    @Override
    public Map<String, Object> getValues(boolean deep) {
        ConfigurationSection defaults;
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>();
        Configuration root = this.getRoot();
        if (root != null && root.options().copyDefaults() && (defaults = this.getDefaultSection()) != null) {
            result.putAll(defaults.getValues(deep));
        }
        this.mapChildrenValues(result, this, deep);
        return result;
    }

    @Override
    public boolean contains(String path) {
        return this.contains(path, false);
    }

    @Override
    public boolean contains(String path, boolean ignoreDefault) {
        return (ignoreDefault ? this.get(path, null) : this.get(path)) != null;
    }

    @Override
    public boolean isSet(String path) {
        Configuration root = this.getRoot();
        if (root == null) {
            return false;
        }
        if (root.options().copyDefaults()) {
            return this.contains(path);
        }
        return this.get(path, null) != null;
    }

    @Override
    public String getCurrentPath() {
        return this.fullPath;
    }

    @Override
    public String getName() {
        return this.path;
    }

    @Override
    public Configuration getRoot() {
        return this.root;
    }

    @Override
    public ConfigurationSection getParent() {
        return this.parent;
    }

    @Override
    public void addDefault(String path, Object value) {
        Validate.notNull((Object)path, (String)"Path cannot be null");
        Configuration root = this.getRoot();
        if (root == null) {
            throw new IllegalStateException("Cannot add default without root");
        }
        if (root == this) {
            throw new UnsupportedOperationException("Unsupported addDefault(String, Object) implementation");
        }
        root.addDefault(MemorySection.createPath(this, path), value);
    }

    @Override
    public ConfigurationSection getDefaultSection() {
        Configuration defaults;
        Configuration root = this.getRoot();
        Configuration configuration = defaults = root == null ? null : root.getDefaults();
        if (defaults != null && defaults.isConfigurationSection(this.getCurrentPath())) {
            return defaults.getConfigurationSection(this.getCurrentPath());
        }
        return null;
    }

    @Override
    public void set(String path, Object value) {
        int i2;
        Validate.notEmpty((String)path, (String)"Cannot set to an empty path");
        Configuration root = this.getRoot();
        if (root == null) {
            throw new IllegalStateException("Cannot use section without a root");
        }
        char separator = root.options().pathSeparator();
        int i1 = -1;
        ConfigurationSection section = this;
        do {
            i2 = i1 + 1;
            i1 = path.indexOf(separator, i2);
            if (i1 == -1) break;
            String node = path.substring(i2, i1);
            ConfigurationSection subSection = section.getConfigurationSection(node);
            if (subSection == null) {
                if (value == null) {
                    return;
                }
                section = section.createSection(node);
                continue;
            }
            section = subSection;
        } while (true);
        String key = path.substring(i2);
        if (section == this) {
            if (value == null) {
                this.map.remove(key);
            } else {
                this.map.put(key, value);
            }
        } else {
            section.set(key, value);
        }
    }

    @Override
    public Object get(String path) {
        return this.get(path, this.getDefault(path));
    }

    @Override
    public Object get(String path, Object def) {
        ConfigurationSection section;
        int i2;
        block4 : {
            Validate.notNull((Object)path, (String)"Path cannot be null");
            if (path.length() == 0) {
                return this;
            }
            Configuration root = this.getRoot();
            if (root == null) {
                throw new IllegalStateException("Cannot access section without a root");
            }
            char separator = root.options().pathSeparator();
            int i1 = -1;
            section = this;
            do {
                i2 = i1 + 1;
                i1 = path.indexOf(separator, i2);
                if (i1 == -1) break block4;
            } while ((section = section.getConfigurationSection(path.substring(i2, i1))) != null);
            return def;
        }
        String key = path.substring(i2);
        if (section == this) {
            Object result = this.map.get(key);
            return result == null ? def : result;
        }
        return section.get(key, def);
    }

    @Override
    public ConfigurationSection createSection(String path) {
        int i2;
        Validate.notEmpty((String)path, (String)"Cannot create section at empty path");
        Configuration root = this.getRoot();
        if (root == null) {
            throw new IllegalStateException("Cannot create section without a root");
        }
        char separator = root.options().pathSeparator();
        int i1 = -1;
        ConfigurationSection section = this;
        do {
            i2 = i1 + 1;
            i1 = path.indexOf(separator, i2);
            if (i1 == -1) break;
            String node = path.substring(i2, i1);
            ConfigurationSection subSection = section.getConfigurationSection(node);
            if (subSection == null) {
                section = section.createSection(node);
                continue;
            }
            section = subSection;
        } while (true);
        String key = path.substring(i2);
        if (section == this) {
            MemorySection result = new MemorySection(this, key);
            this.map.put(key, result);
            return result;
        }
        return section.createSection(key);
    }

    @Override
    public ConfigurationSection createSection(String path, Map<?, ?> map) {
        ConfigurationSection section = this.createSection(path);
        for (Map.Entry entry : map.entrySet()) {
            if (entry.getValue() instanceof Map) {
                section.createSection(entry.getKey().toString(), (Map)entry.getValue());
                continue;
            }
            section.set(entry.getKey().toString(), entry.getValue());
        }
        return section;
    }

    @Override
    public String getString(String path) {
        Object def = this.getDefault(path);
        return this.getString(path, def != null ? def.toString() : null);
    }

    @Override
    public String getString(String path, String def) {
        Object val = this.get(path, def);
        return val != null ? val.toString() : def;
    }

    @Override
    public boolean isString(String path) {
        Object val = this.get(path);
        return val instanceof String;
    }

    @Override
    public int getInt(String path) {
        Object def = this.getDefault(path);
        return this.getInt(path, def instanceof Number ? NumberConversions.toInt(def) : 0);
    }

    @Override
    public int getInt(String path, int def) {
        Object val = this.get(path, def);
        return val instanceof Number ? NumberConversions.toInt(val) : def;
    }

    @Override
    public boolean isInt(String path) {
        Object val = this.get(path);
        return val instanceof Integer;
    }

    @Override
    public boolean getBoolean(String path) {
        Object def = this.getDefault(path);
        return this.getBoolean(path, def instanceof Boolean ? (Boolean)def : false);
    }

    @Override
    public boolean getBoolean(String path, boolean def) {
        Object val = this.get(path, def);
        return val instanceof Boolean ? (Boolean)val : def;
    }

    @Override
    public boolean isBoolean(String path) {
        Object val = this.get(path);
        return val instanceof Boolean;
    }

    @Override
    public double getDouble(String path) {
        Object def = this.getDefault(path);
        return this.getDouble(path, def instanceof Number ? NumberConversions.toDouble(def) : 0.0);
    }

    @Override
    public double getDouble(String path, double def) {
        Object val = this.get(path, def);
        return val instanceof Number ? NumberConversions.toDouble(val) : def;
    }

    @Override
    public boolean isDouble(String path) {
        Object val = this.get(path);
        return val instanceof Double;
    }

    @Override
    public long getLong(String path) {
        Object def = this.getDefault(path);
        return this.getLong(path, def instanceof Number ? NumberConversions.toLong(def) : 0);
    }

    @Override
    public long getLong(String path, long def) {
        Object val = this.get(path, def);
        return val instanceof Number ? NumberConversions.toLong(val) : def;
    }

    @Override
    public boolean isLong(String path) {
        Object val = this.get(path);
        return val instanceof Long;
    }

    @Override
    public List<?> getList(String path) {
        Object def = this.getDefault(path);
        return this.getList(path, def instanceof List ? (List)def : null);
    }

    @Override
    public List<?> getList(String path, List<?> def) {
        List val = this.get(path, def);
        return val instanceof List ? val : def;
    }

    @Override
    public boolean isList(String path) {
        Object val = this.get(path);
        return val instanceof List;
    }

    @Override
    public List<String> getStringList(String path) {
        List list = this.getList(path);
        if (list == null) {
            return new ArrayList<String>(0);
        }
        ArrayList<String> result = new ArrayList<String>();
        for (Object object : list) {
            if (!(object instanceof String) && !this.isPrimitiveWrapper(object)) continue;
            result.add(String.valueOf(object));
        }
        return result;
    }

    @Override
    public List<Integer> getIntegerList(String path) {
        List list = this.getList(path);
        if (list == null) {
            return new ArrayList<Integer>(0);
        }
        ArrayList<Integer> result = new ArrayList<Integer>();
        for (Object object : list) {
            if (object instanceof Integer) {
                result.add((Integer)object);
                continue;
            }
            if (object instanceof String) {
                try {
                    result.add(Integer.valueOf((String)object));
                }
                catch (Exception exception) {}
                continue;
            }
            if (object instanceof Character) {
                result.add(Integer.valueOf(((Character)object).charValue()));
                continue;
            }
            if (!(object instanceof Number)) continue;
            result.add(((Number)object).intValue());
        }
        return result;
    }

    @Override
    public List<Boolean> getBooleanList(String path) {
        List list = this.getList(path);
        if (list == null) {
            return new ArrayList<Boolean>(0);
        }
        ArrayList<Boolean> result = new ArrayList<Boolean>();
        for (Object object : list) {
            if (object instanceof Boolean) {
                result.add((Boolean)object);
                continue;
            }
            if (!(object instanceof String)) continue;
            if (Boolean.TRUE.toString().equals(object)) {
                result.add(true);
                continue;
            }
            if (!Boolean.FALSE.toString().equals(object)) continue;
            result.add(false);
        }
        return result;
    }

    @Override
    public List<Double> getDoubleList(String path) {
        List list = this.getList(path);
        if (list == null) {
            return new ArrayList<Double>(0);
        }
        ArrayList<Double> result = new ArrayList<Double>();
        for (Object object : list) {
            if (object instanceof Double) {
                result.add((Double)object);
                continue;
            }
            if (object instanceof String) {
                try {
                    result.add(Double.valueOf((String)object));
                }
                catch (Exception exception) {}
                continue;
            }
            if (object instanceof Character) {
                result.add(Double.valueOf(((Character)object).charValue()));
                continue;
            }
            if (!(object instanceof Number)) continue;
            result.add(((Number)object).doubleValue());
        }
        return result;
    }

    @Override
    public List<Float> getFloatList(String path) {
        List list = this.getList(path);
        if (list == null) {
            return new ArrayList<Float>(0);
        }
        ArrayList<Float> result = new ArrayList<Float>();
        for (Object object : list) {
            if (object instanceof Float) {
                result.add((Float)object);
                continue;
            }
            if (object instanceof String) {
                try {
                    result.add(Float.valueOf((String)object));
                }
                catch (Exception exception) {}
                continue;
            }
            if (object instanceof Character) {
                result.add(Float.valueOf(((Character)object).charValue()));
                continue;
            }
            if (!(object instanceof Number)) continue;
            result.add(Float.valueOf(((Number)object).floatValue()));
        }
        return result;
    }

    @Override
    public List<Long> getLongList(String path) {
        List list = this.getList(path);
        if (list == null) {
            return new ArrayList<Long>(0);
        }
        ArrayList<Long> result = new ArrayList<Long>();
        for (Object object : list) {
            if (object instanceof Long) {
                result.add((Long)object);
                continue;
            }
            if (object instanceof String) {
                try {
                    result.add(Long.valueOf((String)object));
                }
                catch (Exception exception) {}
                continue;
            }
            if (object instanceof Character) {
                result.add(Long.valueOf(((Character)object).charValue()));
                continue;
            }
            if (!(object instanceof Number)) continue;
            result.add(((Number)object).longValue());
        }
        return result;
    }

    @Override
    public List<Byte> getByteList(String path) {
        List list = this.getList(path);
        if (list == null) {
            return new ArrayList<Byte>(0);
        }
        ArrayList<Byte> result = new ArrayList<Byte>();
        for (Object object : list) {
            if (object instanceof Byte) {
                result.add((Byte)object);
                continue;
            }
            if (object instanceof String) {
                try {
                    result.add(Byte.valueOf((String)object));
                }
                catch (Exception exception) {}
                continue;
            }
            if (object instanceof Character) {
                result.add(Byte.valueOf((byte)((Character)object).charValue()));
                continue;
            }
            if (!(object instanceof Number)) continue;
            result.add(Byte.valueOf(((Number)object).byteValue()));
        }
        return result;
    }

    @Override
    public List<Character> getCharacterList(String path) {
        List list = this.getList(path);
        if (list == null) {
            return new ArrayList<Character>(0);
        }
        ArrayList<Character> result = new ArrayList<Character>();
        for (Object object : list) {
            if (object instanceof Character) {
                result.add((Character)object);
                continue;
            }
            if (object instanceof String) {
                String str = (String)object;
                if (str.length() != 1) continue;
                result.add(Character.valueOf(str.charAt(0)));
                continue;
            }
            if (!(object instanceof Number)) continue;
            result.add(Character.valueOf((char)((Number)object).intValue()));
        }
        return result;
    }

    @Override
    public List<Short> getShortList(String path) {
        List list = this.getList(path);
        if (list == null) {
            return new ArrayList<Short>(0);
        }
        ArrayList<Short> result = new ArrayList<Short>();
        for (Object object : list) {
            if (object instanceof Short) {
                result.add((Short)object);
                continue;
            }
            if (object instanceof String) {
                try {
                    result.add(Short.valueOf((String)object));
                }
                catch (Exception exception) {}
                continue;
            }
            if (object instanceof Character) {
                result.add((short)((Character)object).charValue());
                continue;
            }
            if (!(object instanceof Number)) continue;
            result.add(((Number)object).shortValue());
        }
        return result;
    }

    @Override
    public List<Map<?, ?>> getMapList(String path) {
        List list = this.getList(path);
        ArrayList result = new ArrayList();
        if (list == null) {
            return result;
        }
        for (Object object : list) {
            if (!(object instanceof Map)) continue;
            result.add((Map)object);
        }
        return result;
    }

    @Override
    public <T extends ConfigurationSerializable> T getSerializable(String path, Class<T> clazz) {
        Validate.notNull(clazz, (String)"ConfigurationSerializable class cannot be null");
        Object def = this.getDefault(path);
        return this.getSerializable(path, clazz, def != null && clazz.isInstance(def) ? (ConfigurationSerializable)clazz.cast(def) : null);
    }

    @Override
    public <T extends ConfigurationSerializable> T getSerializable(String path, Class<T> clazz, T def) {
        Validate.notNull(clazz, (String)"ConfigurationSerializable class cannot be null");
        Object val = this.get(path);
        return (T)(val != null && clazz.isInstance(val) ? (ConfigurationSerializable)clazz.cast(val) : def);
    }

    @Override
    public Vector getVector(String path) {
        return this.getSerializable(path, Vector.class);
    }

    @Override
    public Vector getVector(String path, Vector def) {
        return this.getSerializable(path, Vector.class, def);
    }

    @Override
    public boolean isVector(String path) {
        return this.getSerializable(path, Vector.class) != null;
    }

    @Override
    public OfflinePlayer getOfflinePlayer(String path) {
        return this.getSerializable(path, OfflinePlayer.class);
    }

    @Override
    public OfflinePlayer getOfflinePlayer(String path, OfflinePlayer def) {
        return this.getSerializable(path, OfflinePlayer.class, def);
    }

    @Override
    public boolean isOfflinePlayer(String path) {
        return this.getSerializable(path, OfflinePlayer.class) != null;
    }

    @Override
    public ItemStack getItemStack(String path) {
        return this.getSerializable(path, ItemStack.class);
    }

    @Override
    public ItemStack getItemStack(String path, ItemStack def) {
        return this.getSerializable(path, ItemStack.class, def);
    }

    @Override
    public boolean isItemStack(String path) {
        return this.getSerializable(path, ItemStack.class) != null;
    }

    @Override
    public Color getColor(String path) {
        return this.getSerializable(path, Color.class);
    }

    @Override
    public Color getColor(String path, Color def) {
        return this.getSerializable(path, Color.class, def);
    }

    @Override
    public boolean isColor(String path) {
        return this.getSerializable(path, Color.class) != null;
    }

    @Override
    public ConfigurationSection getConfigurationSection(String path) {
        Object val = this.get(path, null);
        if (val != null) {
            return val instanceof ConfigurationSection ? (ConfigurationSection)val : null;
        }
        val = this.get(path, this.getDefault(path));
        return val instanceof ConfigurationSection ? this.createSection(path) : null;
    }

    @Override
    public boolean isConfigurationSection(String path) {
        Object val = this.get(path);
        return val instanceof ConfigurationSection;
    }

    protected boolean isPrimitiveWrapper(Object input) {
        return input instanceof Integer || input instanceof Boolean || input instanceof Character || input instanceof Byte || input instanceof Short || input instanceof Double || input instanceof Long || input instanceof Float;
    }

    protected Object getDefault(String path) {
        Validate.notNull((Object)path, (String)"Path cannot be null");
        Configuration root = this.getRoot();
        Configuration defaults = root == null ? null : root.getDefaults();
        return defaults == null ? null : defaults.get(MemorySection.createPath(this, path));
    }

    protected void mapChildrenKeys(Set<String> output, ConfigurationSection section, boolean deep) {
        if (section instanceof MemorySection) {
            MemorySection sec = (MemorySection)section;
            for (Map.Entry<String, Object> entry : sec.map.entrySet()) {
                output.add(MemorySection.createPath(section, entry.getKey(), this));
                if (!deep || !(entry.getValue() instanceof ConfigurationSection)) continue;
                ConfigurationSection subsection = (ConfigurationSection)entry.getValue();
                this.mapChildrenKeys(output, subsection, deep);
            }
        } else {
            Set<String> keys = section.getKeys(deep);
            for (String key : keys) {
                output.add(MemorySection.createPath(section, key, this));
            }
        }
    }

    protected void mapChildrenValues(Map<String, Object> output, ConfigurationSection section, boolean deep) {
        if (section instanceof MemorySection) {
            MemorySection sec = (MemorySection)section;
            for (Map.Entry<String, Object> entry : sec.map.entrySet()) {
                output.put(MemorySection.createPath(section, entry.getKey(), this), entry.getValue());
                if (!(entry.getValue() instanceof ConfigurationSection) || !deep) continue;
                this.mapChildrenValues(output, (ConfigurationSection)entry.getValue(), deep);
            }
        } else {
            Map<String, Object> values = section.getValues(deep);
            for (Map.Entry<String, Object> entry : values.entrySet()) {
                output.put(MemorySection.createPath(section, entry.getKey(), this), entry.getValue());
            }
        }
    }

    public static String createPath(ConfigurationSection section, String key) {
        return MemorySection.createPath(section, key, section == null ? null : section.getRoot());
    }

    public static String createPath(ConfigurationSection section, String key, ConfigurationSection relativeTo) {
        Validate.notNull((Object)section, (String)"Cannot create path without a section");
        Configuration root = section.getRoot();
        if (root == null) {
            throw new IllegalStateException("Cannot create path without a root");
        }
        char separator = root.options().pathSeparator();
        StringBuilder builder = new StringBuilder();
        if (section != null) {
            for (ConfigurationSection parent = section; parent != null && parent != relativeTo; parent = parent.getParent()) {
                if (builder.length() > 0) {
                    builder.insert(0, separator);
                }
                builder.insert(0, parent.getName());
            }
        }
        if (key != null && key.length() > 0) {
            if (builder.length() > 0) {
                builder.append(separator);
            }
            builder.append(key);
        }
        return builder.toString();
    }

    public String toString() {
        Configuration root;
        return this.getClass().getSimpleName() + "[path='" + this.getCurrentPath() + "', root='" + ((root = this.getRoot()) == null ? null : root.getClass().getSimpleName()) + "']";
    }
}

