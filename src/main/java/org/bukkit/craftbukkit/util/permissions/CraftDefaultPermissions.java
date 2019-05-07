/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.util.permissions;

import org.bukkit.craftbukkit.util.permissions.CommandPermissions;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.util.permissions.DefaultPermissions;

public final class CraftDefaultPermissions {
    private static final String ROOT = "minecraft";

    private CraftDefaultPermissions() {
    }

    public static void registerCorePermissions() {
        Permission parent = DefaultPermissions.registerPermission("minecraft", "Gives the user the ability to use all vanilla utilities and commands");
        CommandPermissions.registerPermissions(parent);
        DefaultPermissions.registerPermission("minecraft.autocraft", "Gives the user the ability to use autocraft functionality", PermissionDefault.OP, parent);
        parent.recalculatePermissibles();
    }
}

