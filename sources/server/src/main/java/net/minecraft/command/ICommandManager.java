package net.minecraft.command;

import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.util.math.BlockPos;

public interface ICommandManager
{
    int executeCommand(ICommandSender sender, String rawCommand);

    List<String> getTabCompletions(ICommandSender sender, String input, @Nullable BlockPos pos);

    List<ICommand> getPossibleCommands(ICommandSender sender);

    Map<String, ICommand> getCommands();
}