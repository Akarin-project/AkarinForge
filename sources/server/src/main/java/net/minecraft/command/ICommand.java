package net.minecraft.command;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public interface ICommand extends Comparable<ICommand>
{
    String getName();

    String getUsage(ICommandSender sender);

    List<String> getAliases();

    void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException;

    boolean checkPermission(MinecraftServer server, ICommandSender sender);

    List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos);

    boolean isUsernameIndex(String[] args, int index);
}