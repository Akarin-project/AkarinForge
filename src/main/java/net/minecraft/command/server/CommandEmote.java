package net.minecraft.command.server;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public class CommandEmote extends CommandBase
{
    public String getName()
    {
        return "me";
    }

    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    public String getUsage(ICommandSender sender)
    {
        return "commands.me.usage";
    }

    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length <= 0)
        {
            throw new WrongUsageException("commands.me.usage", new Object[0]);
        }
        else
        {
            ITextComponent itextcomponent = getChatComponentFromNthArg(sender, args, 0, !(sender instanceof EntityPlayer));
            server.getPlayerList().sendMessage(new TextComponentTranslation("chat.type.emote", new Object[] {sender.getDisplayName(), itextcomponent}));
        }
    }

    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
        return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
    }
}