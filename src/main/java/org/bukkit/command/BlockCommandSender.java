/*
 * Akarin Forge
 */
package org.bukkit.command;

import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;

public interface BlockCommandSender
extends CommandSender {
    public Block getBlock();
}

