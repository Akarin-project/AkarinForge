/*
 * Akarin Forge
 */
package org.bukkit.permissions;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionRemovedExecutor;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;

public class PermissionAttachment {
    private PermissionRemovedExecutor removed;
    private final Map<String, Boolean> permissions = new LinkedHashMap<String, Boolean>();
    private final Permissible permissible;
    private final Plugin plugin;

    public PermissionAttachment(Plugin plugin, Permissible Permissible2) {
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null");
        }
        if (!plugin.isEnabled()) {
            throw new IllegalArgumentException("Plugin " + plugin.getDescription().getFullName() + " is disabled");
        }
        this.permissible = Permissible2;
        this.plugin = plugin;
    }

    public Plugin getPlugin() {
        return this.plugin;
    }

    public void setRemovalCallback(PermissionRemovedExecutor ex2) {
        this.removed = ex2;
    }

    public PermissionRemovedExecutor getRemovalCallback() {
        return this.removed;
    }

    public Permissible getPermissible() {
        return this.permissible;
    }

    public Map<String, Boolean> getPermissions() {
        return new LinkedHashMap<String, Boolean>(this.permissions);
    }

    public void setPermission(String name, boolean value) {
        this.permissions.put(name.toLowerCase(Locale.ENGLISH), value);
        this.permissible.recalculatePermissions();
    }

    public void setPermission(Permission perm, boolean value) {
        this.setPermission(perm.getName(), value);
    }

    public void unsetPermission(String name) {
        this.permissions.remove(name.toLowerCase(Locale.ENGLISH));
        this.permissible.recalculatePermissions();
    }

    public void unsetPermission(Permission perm) {
        this.unsetPermission(perm.getName());
    }

    public boolean remove() {
        try {
            this.permissible.removeAttachment(this);
            return true;
        }
        catch (IllegalArgumentException ex2) {
            return false;
        }
    }
}

