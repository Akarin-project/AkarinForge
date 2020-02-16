/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  org.apache.commons.lang3.Validate
 */
package org.bukkit.craftbukkit.v1_12_R1.help;

import org.apache.commons.lang3.Validate;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.help.HelpMap;
import org.bukkit.help.HelpTopic;

public class CommandAliasHelpTopic
extends HelpTopic {
    private final String aliasFor;
    private final HelpMap helpMap;

    public CommandAliasHelpTopic(String alias, String aliasFor, HelpMap helpMap) {
        this.aliasFor = aliasFor.startsWith("/") ? aliasFor : "/" + aliasFor;
        this.helpMap = helpMap;
        this.name = alias.startsWith("/") ? alias : "/" + alias;
        Validate.isTrue((boolean)(!this.name.equals(this.aliasFor)), (String)("Command " + this.name + " cannot be alias for itself"), (Object[])new Object[0]);
        this.shortText = (Object)((Object)ChatColor.YELLOW) + "Alias for " + (Object)((Object)ChatColor.WHITE) + this.aliasFor;
    }

    @Override
    public String getFullText(CommandSender forWho) {
        StringBuilder sb2 = new StringBuilder(this.shortText);
        HelpTopic aliasForTopic = this.helpMap.getHelpTopic(this.aliasFor);
        if (aliasForTopic != null) {
            sb2.append("\n");
            sb2.append(aliasForTopic.getFullText(forWho));
        }
        return sb2.toString();
    }

    @Override
    public boolean canSee(CommandSender commandSender) {
        if (this.amendedPermission == null) {
            HelpTopic aliasForTopic = this.helpMap.getHelpTopic(this.aliasFor);
            if (aliasForTopic != null) {
                return aliasForTopic.canSee(commandSender);
            }
            return false;
        }
        return commandSender.hasPermission(this.amendedPermission);
    }
}

