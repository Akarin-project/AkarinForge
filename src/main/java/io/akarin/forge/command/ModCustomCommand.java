package io.akarin.forge.command;

import java.util.List;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.util.math.BlockPos;

import org.apache.commons.lang.Validate;
import org.bukkit.Server;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.ProxiedCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.command.CraftBlockCommandSender;
import org.bukkit.craftbukkit.command.ProxiedNativeCommandSender;
import org.bukkit.craftbukkit.entity.CraftMinecartCommand;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.CommandMinecart;

public class ModCustomCommand extends Command {
    private final ICommand vanillaCommand;

    public ModCustomCommand(ICommand command) {
        super(command.getName());
        this.vanillaCommand = command;
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        return false;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        Validate.notNull((Object)sender, (String)"Sender cannot be null");
        Validate.notNull((Object)args, (String)"Arguments cannot be null");
        Validate.notNull((Object)alias, (String)"Alias cannot be null");
        return this.vanillaCommand.getTabCompletions(MinecraftServer.getServerInst(), this.getListener(sender), args, new BlockPos(0, 0, 0));
    }

    private ICommandSender getListener(CommandSender sender) {
        if (sender instanceof Player) {
            return ((CraftPlayer)sender).getHandle();
        }
        if (sender instanceof BlockCommandSender) {
            return ((CraftBlockCommandSender)sender).getTileEntity();
        }
        if (sender instanceof CommandMinecart) {
            return ((CraftMinecartCommand)sender).getHandle().getCommandSenderEntity();
        }
        if (sender instanceof RemoteConsoleCommandSender) {
            return ((DedicatedServer)MinecraftServer.getServerInst()).rconConsoleSource;
        }
        if (sender instanceof ConsoleCommandSender) {
            return ((CraftServer)sender.getServer()).getServer();
        }
        if (sender instanceof ProxiedCommandSender) {
            return ((ProxiedNativeCommandSender)sender).getHandle();
        }
        throw new IllegalArgumentException("Cannot make " + sender + " a vanilla command listener");
    }
}

