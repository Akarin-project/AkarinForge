/*
 * Akarin Forge
 */
package org.bukkit.entity.minecart;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Minecart;

public interface CommandMinecart
extends Minecart,
CommandSender {
    public String getCommand();

    public void setCommand(String var1);

    public void setName(String var1);
}

