package net.minecraft.command.server;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.WorldServer;

public class CommandSaveOn extends CommandBase
{
    public String getName()
    {
        return "save-on";
    }

    public String getUsage(ICommandSender sender)
    {
        return "commands.save-on.usage";
    }

    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        boolean flag = false;

        for (int i = 0; i < server.worlds.length; ++i)
        {
            if (server.worlds[i] != null)
            {
                WorldServer worldserver = server.worlds[i];

                if (worldserver.disableLevelSaving)
                {
                    worldserver.disableLevelSaving = false;
                    flag = true;
                }
            }
        }

        if (flag)
        {
            notifyCommandListener(sender, this, "commands.save.enabled", new Object[0]);
        }
        else
        {
            throw new CommandException("commands.save-on.alreadyOn", new Object[0]);
        }
    }
}