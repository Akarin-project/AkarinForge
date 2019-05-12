package io.akarin.forge.server.command;

import java.util.List;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;

import org.apache.commons.lang3.Validate;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.ProxiedCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.command.CraftBlockCommandSender;
import org.bukkit.craftbukkit.v1_12_R1.command.ProxiedNativeCommandSender;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftMinecartCommand;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.entity.minecart.CommandMinecart;

public class ModCommandWrapper extends org.bukkit.command.Command {
    private final ICommand command;

    public ModCommandWrapper(ICommand command) {
        super(command.getName(), "A Mod provided command.", I18n.translateToLocal(command.getUsage(null)), command.getAliases());
        this.command = command;
        //this.setPermission("mod.command." + command.getName());
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        return false;
    }

    @Override
    public List<String> tabComplete(CommandSender bukkitSender, String alias, String[] args) throws IllegalArgumentException {
        Validate.notNull(bukkitSender, "Sender cannot be null");
        Validate.notNull(args, "Arguments cannot be null");
        Validate.notNull(alias, "Alias cannot be null");
        
        return this.command.getTabCompletions(MinecraftServer.instance(), this.getListener(bukkitSender), args, BlockPos.ORIGIN);
    }

    private ICommandSender getListener(CommandSender bukkitSender) {
        if (bukkitSender instanceof Player) {
            return ((CraftPlayer) bukkitSender).getHandle();
        }
        
        if (bukkitSender instanceof BlockCommandSender) {
            return ((CraftBlockCommandSender) bukkitSender).getTileEntity();
        }
        
        if (bukkitSender instanceof CommandMinecart) {
            return ((CraftMinecartCommand) bukkitSender).getHandle().getCommandSenderEntity();
        }
        
        if (bukkitSender instanceof RemoteConsoleCommandSender) {
            return ((DedicatedServer) MinecraftServer.instance()).rconConsoleSource;
        }
        
        if (bukkitSender instanceof ConsoleCommandSender) {
            return ((CraftServer) bukkitSender.getServer()).getServer();
        }
        
        if (bukkitSender instanceof ProxiedCommandSender) {
            return ((ProxiedNativeCommandSender) bukkitSender).getHandle();
        }
        
        // We are safe due to the sender is coming from bukkit
        throw new IllegalArgumentException("Cannot make " + bukkitSender + " a vanilla command listener");
    }
}

