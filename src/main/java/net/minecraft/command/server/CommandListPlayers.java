package net.minecraft.command.server;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandListPlayers extends CommandBase
{
    public String getName()
    {
        return "list";
    }

    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    public String getUsage(ICommandSender sender)
    {
        return "commands.players.usage";
    }

    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        int i = server.getCurrentPlayerCount();
        sender.sendMessage(new TextComponentTranslation("commands.players.list", new Object[] {i, server.getMaxPlayers()}));
        sender.sendMessage(new TextComponentString(server.getPlayerList().getFormattedListOfPlayers(args.length > 0 && "uuids".equalsIgnoreCase(args[0]))));
        sender.setCommandStat(CommandResultStats.Type.QUERY_RESULT, i);
    }
}