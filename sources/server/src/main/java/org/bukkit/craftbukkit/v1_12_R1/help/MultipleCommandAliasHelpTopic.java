/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.help;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.MultipleCommandAlias;
import org.bukkit.help.HelpTopic;

public class MultipleCommandAliasHelpTopic
extends HelpTopic {
    private final MultipleCommandAlias alias;

    public MultipleCommandAliasHelpTopic(MultipleCommandAlias alias) {
        this.alias = alias;
        this.name = "/" + alias.getLabel();
        StringBuilder sb2 = new StringBuilder();
        for (int i2 = 0; i2 < alias.getCommands().length; ++i2) {
            if (i2 != 0) {
                sb2.append((Object)((Object)ChatColor.GOLD) + " > " + (Object)((Object)ChatColor.WHITE));
            }
            sb2.append("/");
            sb2.append(alias.getCommands()[i2].getLabel());
        }
        this.shortText = sb2.toString();
        this.fullText = (Object)((Object)ChatColor.GOLD) + "Alias for: " + (Object)((Object)ChatColor.WHITE) + this.getShortText();
    }

    @Override
    public boolean canSee(CommandSender sender) {
        if (this.amendedPermission == null) {
            if (sender instanceof ConsoleCommandSender) {
                return true;
            }
            for (Command command : this.alias.getCommands()) {
                if (command.testPermissionSilent(sender)) continue;
                return false;
            }
            return true;
        }
        return sender.hasPermission(this.amendedPermission);
    }
}

