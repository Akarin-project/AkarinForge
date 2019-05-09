/*
 * Akarin Forge
 */
package org.bukkit.permissions;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.Bukkit;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.permissions.PermissionRemovedExecutor;
import org.bukkit.permissions.ServerOperator;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitScheduler;

public class PermissibleBase
implements Permissible {
    private ServerOperator opable = null;
    private Permissible parent;
    private final List<PermissionAttachment> attachments;
    private final Map<String, PermissionAttachmentInfo> permissions;

    public PermissibleBase(ServerOperator opable) {
        this.parent = this;
        this.attachments = new LinkedList<PermissionAttachment>();
        this.permissions = new HashMap<String, PermissionAttachmentInfo>();
        this.opable = opable;
        if (opable instanceof Permissible) {
            this.parent = (Permissible)opable;
        }
        this.recalculatePermissions();
    }

    @Override
    public boolean isOp() {
        if (this.opable == null) {
            return false;
        }
        return this.opable.isOp();
    }

    @Override
    public void setOp(boolean value) {
        if (this.opable == null) {
            throw new UnsupportedOperationException("Cannot change op value as no ServerOperator is set");
        }
        this.opable.setOp(value);
    }

    @Override
    public boolean isPermissionSet(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Permission name cannot be null");
        }
        return this.permissions.containsKey(name.toLowerCase(Locale.ENGLISH));
    }

    @Override
    public boolean isPermissionSet(Permission perm) {
        if (perm == null) {
            throw new IllegalArgumentException("Permission cannot be null");
        }
        return this.isPermissionSet(perm.getName());
    }

    @Override
    public boolean hasPermission(String inName) {
        if (inName == null) {
            throw new IllegalArgumentException("Permission name cannot be null");
        }
        String name = inName.toLowerCase(Locale.ENGLISH);
        PermissionAttachmentInfo info = this.permissions.get(name);
        if (info != null) {
            return info.getValue();
        }
        Permission perm = Bukkit.getServer().getPluginManager().getPermission(name);
        if (perm != null) {
            return perm.getDefault().getValue(this.isOp());
        }
        return Permission.DEFAULT_PERMISSION.getValue(this.isOp());
    }

    @Override
    public boolean hasPermission(Permission perm) {
        if (perm == null) {
            throw new IllegalArgumentException("Permission cannot be null");
        }
        String name = perm.getName().toLowerCase(Locale.ENGLISH);
        PermissionAttachmentInfo info = this.permissions.get(name);
        if (info != null) {
            return info.getValue();
        }
        return perm.getDefault().getValue(this.isOp());
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value) {
        if (name == null) {
            throw new IllegalArgumentException("Permission name cannot be null");
        }
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null");
        }
        if (!plugin.isEnabled()) {
            throw new IllegalArgumentException("Plugin " + plugin.getDescription().getFullName() + " is disabled");
        }
        PermissionAttachment result = this.addAttachment(plugin);
        result.setPermission(name, value);
        this.recalculatePermissions();
        return result;
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin) {
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null");
        }
        if (!plugin.isEnabled()) {
            throw new IllegalArgumentException("Plugin " + plugin.getDescription().getFullName() + " is disabled");
        }
        PermissionAttachment result = new PermissionAttachment(plugin, this.parent);
        this.attachments.add(result);
        this.recalculatePermissions();
        return result;
    }

    @Override
    public void removeAttachment(PermissionAttachment attachment) {
        if (attachment == null) {
            throw new IllegalArgumentException("Attachment cannot be null");
        }
        if (this.attachments.contains(attachment)) {
            this.attachments.remove(attachment);
            PermissionRemovedExecutor ex2 = attachment.getRemovalCallback();
            if (ex2 != null) {
                ex2.attachmentRemoved(attachment);
            }
        } else {
            throw new IllegalArgumentException("Given attachment is not part of Permissible object " + this.parent);
        }
        this.recalculatePermissions();
    }

    @Override
    public void recalculatePermissions() {
        this.clearPermissions();
        Set<Permission> defaults = Bukkit.getServer().getPluginManager().getDefaultPermissions(this.isOp());
        Bukkit.getServer().getPluginManager().subscribeToDefaultPerms(this.isOp(), this.parent);
        for (Permission perm : defaults) {
            String name = perm.getName().toLowerCase(Locale.ENGLISH);
            this.permissions.put(name, new PermissionAttachmentInfo(this.parent, name, null, true));
            Bukkit.getServer().getPluginManager().subscribeToPermission(name, this.parent);
            this.calculateChildPermissions(perm.getChildren(), false, null);
        }
        for (PermissionAttachment attachment : this.attachments) {
            this.calculateChildPermissions(attachment.getPermissions(), false, attachment);
        }
    }

    public synchronized void clearPermissions() {
        Set<String> perms = this.permissions.keySet();
        for (String name : perms) {
            Bukkit.getServer().getPluginManager().unsubscribeFromPermission(name, this.parent);
        }
        Bukkit.getServer().getPluginManager().unsubscribeFromDefaultPerms(false, this.parent);
        Bukkit.getServer().getPluginManager().unsubscribeFromDefaultPerms(true, this.parent);
        this.permissions.clear();
    }

    private void calculateChildPermissions(Map<String, Boolean> children, boolean invert, PermissionAttachment attachment) {
        for (Map.Entry<String, Boolean> entry : children.entrySet()) {
            String name = entry.getKey();
            Permission perm = Bukkit.getServer().getPluginManager().getPermission(name);
            boolean value = entry.getValue() ^ invert;
            String lname = name.toLowerCase(Locale.ENGLISH);
            this.permissions.put(lname, new PermissionAttachmentInfo(this.parent, lname, attachment, value));
            Bukkit.getServer().getPluginManager().subscribeToPermission(name, this.parent);
            if (perm == null) continue;
            this.calculateChildPermissions(perm.getChildren(), !value, attachment);
        }
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, String name, boolean value, int ticks) {
        if (name == null) {
            throw new IllegalArgumentException("Permission name cannot be null");
        }
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null");
        }
        if (!plugin.isEnabled()) {
            throw new IllegalArgumentException("Plugin " + plugin.getDescription().getFullName() + " is disabled");
        }
        PermissionAttachment result = this.addAttachment(plugin, ticks);
        if (result != null) {
            result.setPermission(name, value);
        }
        return result;
    }

    @Override
    public PermissionAttachment addAttachment(Plugin plugin, int ticks) {
        if (plugin == null) {
            throw new IllegalArgumentException("Plugin cannot be null");
        }
        if (!plugin.isEnabled()) {
            throw new IllegalArgumentException("Plugin " + plugin.getDescription().getFullName() + " is disabled");
        }
        PermissionAttachment result = this.addAttachment(plugin);
        if (Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new RemoveAttachmentRunnable(result), (long)ticks) == -1) {
            Bukkit.getServer().getLogger().log(Level.WARNING, "Could not add PermissionAttachment to " + this.parent + " for plugin " + plugin.getDescription().getFullName() + ": Scheduler returned -1");
            result.remove();
            return null;
        }
        return result;
    }

    @Override
    public Set<PermissionAttachmentInfo> getEffectivePermissions() {
        return new HashSet<PermissionAttachmentInfo>(this.permissions.values());
    }

    private static class RemoveAttachmentRunnable
    implements Runnable {
        private PermissionAttachment attachment;

        public RemoveAttachmentRunnable(PermissionAttachment attachment) {
            this.attachment = attachment;
        }

        @Override
        public void run() {
            this.attachment.remove();
        }
    }

}

