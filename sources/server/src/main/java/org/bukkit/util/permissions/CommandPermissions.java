/*
 * Akarin Forge
 */
package org.bukkit.util.permissions;

import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.util.permissions.DefaultPermissions;

public final class CommandPermissions {
    private static final String ROOT = "bukkit.command";
    private static final String PREFIX = "bukkit.command.";

    private CommandPermissions() {
    }

    public static Permission registerPermissions(Permission parent) {
        Permission commands = DefaultPermissions.registerPermission("bukkit.command", "Gives the user the ability to use all CraftBukkit commands", parent);
        DefaultPermissions.registerPermission("bukkit.command.help", "Allows the user to view the vanilla help menu", PermissionDefault.TRUE, commands);
        DefaultPermissions.registerPermission("bukkit.command.plugins", "Allows the user to view the list of plugins running on this server", PermissionDefault.TRUE, commands);
        DefaultPermissions.registerPermission("bukkit.command.reload", "Allows the user to reload the server settings", PermissionDefault.OP, commands);
        DefaultPermissions.registerPermission("bukkit.command.version", "Allows the user to view the version of the server", PermissionDefault.TRUE, commands);
        commands.recalculatePermissibles();
        return commands;
    }
}

