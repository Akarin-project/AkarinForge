/*
 * Akarin Forge
 */
package org.bukkit.permissions;

import java.util.Set;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.permissions.ServerOperator;
import org.bukkit.plugin.Plugin;

public interface Permissible
extends ServerOperator {
    public boolean isPermissionSet(String var1);

    public boolean isPermissionSet(Permission var1);

    public boolean hasPermission(String var1);

    public boolean hasPermission(Permission var1);

    public PermissionAttachment addAttachment(Plugin var1, String var2, boolean var3);

    public PermissionAttachment addAttachment(Plugin var1);

    public PermissionAttachment addAttachment(Plugin var1, String var2, boolean var3, int var4);

    public PermissionAttachment addAttachment(Plugin var1, int var2);

    public void removeAttachment(PermissionAttachment var1);

    public void recalculatePermissions();

    public Set<PermissionAttachmentInfo> getEffectivePermissions();
}

