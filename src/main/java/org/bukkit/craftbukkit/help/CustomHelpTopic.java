/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.help;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.help.HelpTopic;

public class CustomHelpTopic
extends HelpTopic {
    private final String permissionNode;

    public CustomHelpTopic(String name, String shortText, String fullText, String permissionNode) {
        this.permissionNode = permissionNode;
        this.name = name;
        this.shortText = shortText;
        this.fullText = shortText + "\n" + fullText;
    }

    @Override
    public boolean canSee(CommandSender sender) {
        if (sender instanceof ConsoleCommandSender) {
            return true;
        }
        if (!this.permissionNode.equals("")) {
            return sender.hasPermission(this.permissionNode);
        }
        return true;
    }
}

