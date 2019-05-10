/*
 * Akarin Forge
 */
package org.bukkit.help;

import java.util.Collection;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.help.HelpTopic;

public class IndexHelpTopic
extends HelpTopic {
    protected String permission;
    protected String preamble;
    protected Collection<HelpTopic> allTopics;

    public IndexHelpTopic(String name, String shortText, String permission, Collection<HelpTopic> topics) {
        this(name, shortText, permission, topics, null);
    }

    public IndexHelpTopic(String name, String shortText, String permission, Collection<HelpTopic> topics, String preamble) {
        this.name = name;
        this.shortText = shortText;
        this.permission = permission;
        this.preamble = preamble;
        this.setTopicsCollection(topics);
    }

    protected void setTopicsCollection(Collection<HelpTopic> topics) {
        this.allTopics = topics;
    }

    @Override
    public boolean canSee(CommandSender sender) {
        if (sender instanceof ConsoleCommandSender) {
            return true;
        }
        if (this.permission == null) {
            return true;
        }
        return sender.hasPermission(this.permission);
    }

    @Override
    public void amendCanSee(String amendedPermission) {
        this.permission = amendedPermission;
    }

    @Override
    public String getFullText(CommandSender sender) {
        StringBuilder sb2 = new StringBuilder();
        if (this.preamble != null) {
            sb2.append(this.buildPreamble(sender));
            sb2.append("\n");
        }
        for (HelpTopic topic : this.allTopics) {
            if (!topic.canSee(sender)) continue;
            String lineStr = this.buildIndexLine(sender, topic).replace("\n", ". ");
            if (sender instanceof Player && lineStr.length() > 55) {
                sb2.append(lineStr.substring(0, 52));
                sb2.append("...");
            } else {
                sb2.append(lineStr);
            }
            sb2.append("\n");
        }
        return sb2.toString();
    }

    protected String buildPreamble(CommandSender sender) {
        return (Object)((Object)ChatColor.GRAY) + this.preamble;
    }

    protected String buildIndexLine(CommandSender sender, HelpTopic topic) {
        StringBuilder line = new StringBuilder();
        line.append((Object)ChatColor.GOLD);
        line.append(topic.getName());
        line.append(": ");
        line.append((Object)ChatColor.WHITE);
        line.append(topic.getShortText());
        return line.toString();
    }
}

