/*
 * Akarin Forge
 * 
 * Could not load the following classes:
 *  joptsimple.OptionSet
 */
package org.spigotmc;

import java.io.File;
import joptsimple.OptionSet;
import net.minecraft.server.MinecraftServer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.spigotmc.SpigotConfig;
import org.spigotmc.SpigotWorldConfig;

public class SpigotCommand
extends Command {
    public SpigotCommand(String name) {
        super(name);
        this.description = "Spigot related commands";
        this.usageMessage = "/spigot reload";
        this.setPermission("bukkit.command.spigot");
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }
        if (args.length != 1) {
            sender.sendMessage((Object)((Object)ChatColor.RED) + "Usage: " + this.usageMessage);
            return false;
        }
        if (args[0].equals("reload")) {
            Command.broadcastCommandMessage(sender, (Object)((Object)ChatColor.RED) + "Please note that this command is not supported and may cause issues.");
            Command.broadcastCommandMessage(sender, (Object)((Object)ChatColor.RED) + "If you encounter any issues please use the /stop command to restart your server.");
            MinecraftServer console = MinecraftServer.getServerInst();
            SpigotConfig.init((File)console.options.valueOf("spigot-settings"));
            for (oo world : console.d) {
                world.spigotConfig.init();
            }
            ++console.server.reloadCount;
            Command.broadcastCommandMessage(sender, (Object)((Object)ChatColor.GREEN) + "Reload complete.");
        }
        return true;
    }
}

