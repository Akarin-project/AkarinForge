/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang.StringUtils
 */
package org.bukkit.help;

import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.help.HelpTopic;

public class GenericCommandHelpTopic
extends HelpTopic {
    protected Command command;

    public GenericCommandHelpTopic(Command command) {
        this.command = command;
        this.name = command.getLabel().startsWith("/") ? command.getLabel() : "/" + command.getLabel();
        int i2 = command.getDescription().indexOf(10);
        this.shortText = i2 > 1 ? command.getDescription().substring(0, i2 - 1) : command.getDescription();
        StringBuilder sb2 = new StringBuilder();
        sb2.append((Object)ChatColor.GOLD);
        sb2.append("Description: ");
        sb2.append((Object)ChatColor.WHITE);
        sb2.append(command.getDescription());
        sb2.append("\n");
        sb2.append((Object)ChatColor.GOLD);
        sb2.append("Usage: ");
        sb2.append((Object)ChatColor.WHITE);
        sb2.append(command.getUsage().replace("<command>", this.name.substring(1)));
        if (command.getAliases().size() > 0) {
            sb2.append("\n");
            sb2.append((Object)ChatColor.GOLD);
            sb2.append("Aliases: ");
            sb2.append((Object)ChatColor.WHITE);
            sb2.append((Object)((Object)ChatColor.WHITE) + StringUtils.join(command.getAliases(), (String)", "));
        }
        this.fullText = sb2.toString();
    }

    @Override
    public boolean canSee(CommandSender sender) {
        if (!this.command.isRegistered()) {
            return false;
        }
        if (sender instanceof ConsoleCommandSender) {
            return true;
        }
        if (this.amendedPermission != null) {
            return sender.hasPermission(this.amendedPermission);
        }
        return this.command.testPermissionSilent(sender);
    }
}

