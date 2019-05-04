/*
 * Akarin Forge
 */
package org.spigotmc;

import net.minecraft.server.MinecraftServer;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class TicksPerSecondCommand
extends Command {
    public TicksPerSecondCommand(String name) {
        super(name);
        this.description = "Gets the current ticks per second for the server";
        this.usageMessage = "/tps";
        this.setPermission("bukkit.command.tps");
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!this.testPermission(sender)) {
            return true;
        }
        StringBuilder sb2 = new StringBuilder((Object)((Object)ChatColor.GOLD) + "TPS from last 1m, 5m, 15m: ");
        for (double tps : MinecraftServer.getServerInst().recentTps) {
            sb2.append(this.format(tps));
            sb2.append(", ");
        }
        sender.sendMessage(sb2.substring(0, sb2.length() - 2));
        return true;
    }

    private String format(double tps) {
        return (tps > 18.0 ? ChatColor.GREEN : (tps > 16.0 ? ChatColor.YELLOW : ChatColor.RED)).toString() + (tps > 20.0 ? "*" : "") + Math.min((double)Math.round(tps * 100.0) / 100.0, 20.0);
    }
}

