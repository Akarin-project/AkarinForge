package net.minecraft.command.server;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class CommandStop extends CommandBase
{
    public String getName()
    {
        return "stop";
    }

    public String getUsage(ICommandSender sender)
    {
        return "commands.stop.usage";
    }

    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (server.worlds != null)
        {
            notifyCommandListener(sender, this, "commands.stop.start", new Object[0]);
        }

        server.initiateShutdown();
    }
}