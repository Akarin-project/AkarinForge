package io.akarin.forge.command;

import java.util.Arrays;
import java.util.regex.Pattern;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.craftbukkit.entity.CraftPlayer;

import io.akarin.forge.command.ModCustomCommand;

public class CraftSimpleCommandMap extends SimpleCommandMap {
    private static final Pattern PATTERN_ON_SPACE = Pattern.compile(" ", 16);
    private ICommandSender vanillaConsoleSender;

    public CraftSimpleCommandMap(Server server) {
        super(server);
    }

    @Override
    public boolean dispatch(CommandSender sender, String commandLine) throws CommandException {
        String[] args = PATTERN_ON_SPACE.split(commandLine);
        if (args.length == 0) {
            return false;
        }
        String sentCommandLabel = args[0].toLowerCase();
        Command target = this.getCommand(sentCommandLabel);
        if (target == null) {
            return false;
        }
        try {
            if (target instanceof ModCustomCommand) {
                if (!target.testPermission(sender)) {
                    return true;
                }
                if (sender instanceof ConsoleCommandSender) {
                    FMLCommonHandler.instance().getMinecraftServerInstance().getCommandManager().executeCommand(this.vanillaConsoleSender, commandLine);
                } else {
                    FMLCommonHandler.instance().getMinecraftServerInstance().getCommandManager().executeCommand(((CraftPlayer)sender).getHandle(), commandLine);
                }
            } else {
                target.execute(sender, sentCommandLabel, Arrays.copyOfRange(args, 1, args.length));
            }
        }
        catch (CommandException ex2) {
            throw ex2;
        }
        catch (Throwable ex3) {
            throw new CommandException("Unhandled exception executing '" + commandLine + "' in " + target, ex3);
        }
        return true;
    }

    public void setVanillaConsoleSender(ICommandSender console) {
        this.vanillaConsoleSender = console;
    }
}

