package io.akarin.forge.command;

import com.google.common.collect.ImmutableList;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.network.rcon.RConConsoleSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.CommandBlockBaseLogic;
import net.minecraft.util.math.BlockPos;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.command.CraftBlockCommandSender;
import org.bukkit.craftbukkit.command.CraftRemoteConsoleCommandSender;
import org.bukkit.craftbukkit.entity.CraftEntity;

public class BukkitCommandWrapper implements ICommand {
    private final CommandSender bukkitSender;
    private final String name;
    private final Command command;

    public BukkitCommandWrapper(CommandSender bukkitSender, String name, Command command) {
        this.bukkitSender = bukkitSender;
        this.command = command;
        this.name = name;
    }

    @Override
    public int compareTo(ICommand o2) {
        return 0;
    }

    @Override
    public String getName() {
        return this.command.getName();
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return this.command.getDescription();
    }

    @Override
    public List<String> getAliases() {
        return this.command.getAliases();
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        try {
            this.command.execute(this.bukkitSender, this.name, args);
        }
        catch (Exception e2) {
            throw new CommandException(e2.getMessage(), new Object[0]);
        }
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return this.command.testPermission(this.bukkitSender);
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos targetPos) {
        try {
            return this.command.tabComplete(this.bukkitSender, this.name, args);
        }
        catch (Exception e2) {
            e2.printStackTrace();
            return ImmutableList.of();
        }
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }

    @Nullable
    public static BukkitCommandWrapper toNMSCommand(ICommandSender sender, String name) {
        CommandSender bukkitSender;
        Command command = MinecraftServer.getServerInst().server.getCommandMap().getCommand(name);
        if (command != null && (bukkitSender = BukkitCommandWrapper.toBukkitSender(sender)) != null) {
            return new BukkitCommandWrapper(bukkitSender, name, command);
        }
        return null;
    }

    @Nullable
    public static CommandSender toBukkitSender(ICommandSender sender) {
        if (sender instanceof MinecraftServer) {
            return MinecraftServer.getServerInst().console;
        }
        if (sender instanceof RConConsoleSource) {
            return new CraftRemoteConsoleCommandSender((RConConsoleSource)sender);
        }
        if (sender instanceof CommandBlockBaseLogic) {
            return new CraftBlockCommandSender(sender);
        }
        if (sender instanceof Entity) {
            return ((Entity)sender).getBukkitEntity();
        }
        return null;
    }
}

