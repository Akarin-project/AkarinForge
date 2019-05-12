package net.minecraft.command;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class CommandHandler implements ICommandManager
{
    private static final Logger LOGGER = LogManager.getLogger();
    private final Map<String, ICommand> commandMap = Maps.<String, ICommand>newHashMap();
    private final Set<ICommand> commandSet = Sets.<ICommand>newHashSet();

    public int executeCommand(ICommandSender sender, String rawCommand)
    {
        rawCommand = rawCommand.trim();

        if (rawCommand.startsWith("/"))
        {
            rawCommand = rawCommand.substring(1);
        }

        String[] astring = rawCommand.split(" ");
        String s = astring[0];
        astring = dropFirstString(astring);
        ICommand icommand = this.commandMap.get(s);
        int i = 0;

        try
        {
            int j = this.getUsernameIndex(icommand, astring);

            if (icommand == null)
            {
                TextComponentTranslation textcomponenttranslation1 = new TextComponentTranslation("commands.generic.notFound", new Object[0]);
                textcomponenttranslation1.getStyle().setColor(TextFormatting.RED);
                sender.sendMessage(textcomponenttranslation1);
            }
            else if (icommand.checkPermission(this.getServer(), sender))
            {
                net.minecraftforge.event.CommandEvent event = new net.minecraftforge.event.CommandEvent(icommand, sender, astring);
                if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event))
                {
                    if (event.getException() != null)
                    {
                        com.google.common.base.Throwables.throwIfUnchecked(event.getException());
                    }
                    return 1;
                }
                if (event.getParameters() != null) astring = event.getParameters();

                if (j > -1)
                {
                    List<Entity> list = EntitySelector.<Entity>matchEntities(sender, astring[j], Entity.class);
                    String s1 = astring[j];
                    sender.setCommandStat(CommandResultStats.Type.AFFECTED_ENTITIES, list.size());

                    if (list.isEmpty())
                    {
                        throw new PlayerNotFoundException("commands.generic.selector.notFound", new Object[] {astring[j]});
                    }

                    for (Entity entity : list)
                    {
                        astring[j] = entity.getCachedUniqueIdString();

                        if (this.tryExecute(sender, astring, icommand, rawCommand))
                        {
                            ++i;
                        }
                    }

                    astring[j] = s1;
                }
                else
                {
                    sender.setCommandStat(CommandResultStats.Type.AFFECTED_ENTITIES, 1);

                    if (this.tryExecute(sender, astring, icommand, rawCommand))
                    {
                        ++i;
                    }
                }
            }
            else
            {
                TextComponentTranslation textcomponenttranslation2 = new TextComponentTranslation("commands.generic.permission", new Object[0]);
                textcomponenttranslation2.getStyle().setColor(TextFormatting.RED);
                sender.sendMessage(textcomponenttranslation2);
            }
        }
        catch (CommandException commandexception)
        {
            TextComponentTranslation textcomponenttranslation = new TextComponentTranslation(commandexception.getMessage(), commandexception.getErrorObjects());
            textcomponenttranslation.getStyle().setColor(TextFormatting.RED);
            sender.sendMessage(textcomponenttranslation);
        }

        sender.setCommandStat(CommandResultStats.Type.SUCCESS_COUNT, i);
        return i;
    }

    protected boolean tryExecute(ICommandSender sender, String[] args, ICommand command, String input)
    {
        try
        {
            command.execute(this.getServer(), sender, args);
            return true;
        }
        catch (WrongUsageException wrongusageexception)
        {
            TextComponentTranslation textcomponenttranslation2 = new TextComponentTranslation("commands.generic.usage", new Object[] {new TextComponentTranslation(wrongusageexception.getMessage(), wrongusageexception.getErrorObjects())});
            textcomponenttranslation2.getStyle().setColor(TextFormatting.RED);
            sender.sendMessage(textcomponenttranslation2);
        }
        catch (CommandException commandexception)
        {
            TextComponentTranslation textcomponenttranslation1 = new TextComponentTranslation(commandexception.getMessage(), commandexception.getErrorObjects());
            textcomponenttranslation1.getStyle().setColor(TextFormatting.RED);
            sender.sendMessage(textcomponenttranslation1);
        }
        catch (Throwable throwable)
        {
            TextComponentTranslation textcomponenttranslation = new TextComponentTranslation("commands.generic.exception", new Object[0]);
            textcomponenttranslation.getStyle().setColor(TextFormatting.RED);
            sender.sendMessage(textcomponenttranslation);
            LOGGER.warn("Couldn't process command: " + input, throwable);
        }

        return false;
    }

    protected abstract MinecraftServer getServer();

    public ICommand registerCommand(ICommand command)
    {
    	if (MinecraftServer.instance().server != null) MinecraftServer.instance().server.registerCommand(command); // Akarin - register command to Bukkit later for mods
        this.commandMap.put(command.getName(), command);
        this.commandSet.add(command);

        for (String s : command.getAliases())
        {
            ICommand icommand = this.commandMap.get(s);

            if (icommand == null || !icommand.getName().equals(s))
            {
                this.commandMap.put(s, command);
            }
        }

        return command;
    }

    private static String[] dropFirstString(String[] input)
    {
        String[] astring = new String[input.length - 1];
        System.arraycopy(input, 1, astring, 0, input.length - 1);
        return astring;
    }

    public List<String> getTabCompletions(ICommandSender sender, String input, @Nullable BlockPos pos)
    {
        String[] astring = input.split(" ", -1);
        String s = astring[0];

        if (astring.length == 1)
        {
            List<String> list = Lists.<String>newArrayList();

            for (Entry<String, ICommand> entry : this.commandMap.entrySet())
            {
                if (CommandBase.doesStringStartWith(s, entry.getKey()) && ((ICommand)entry.getValue()).checkPermission(this.getServer(), sender))
                {
                    list.add(entry.getKey());
                }
            }

            return list;
        }
        else
        {
            if (astring.length > 1)
            {
                ICommand icommand = this.commandMap.get(s);

                if (icommand != null && icommand.checkPermission(this.getServer(), sender))
                {
                    return icommand.getTabCompletions(this.getServer(), sender, dropFirstString(astring), pos);
                }
            }

            return Collections.<String>emptyList();
        }
    }

    public List<ICommand> getPossibleCommands(ICommandSender sender)
    {
        List<ICommand> list = Lists.<ICommand>newArrayList();

        for (ICommand icommand : this.commandSet)
        {
            if (icommand.checkPermission(this.getServer(), sender))
            {
                list.add(icommand);
            }
        }

        return list;
    }

    public Map<String, ICommand> getCommands()
    {
        return this.commandMap;
    }

    private int getUsernameIndex(ICommand command, String[] args) throws CommandException
    {
        if (command == null)
        {
            return -1;
        }
        else
        {
            for (int i = 0; i < args.length; ++i)
            {
                if (command.isUsernameIndex(args, i) && EntitySelector.matchesMultiplePlayers(args[i]))
                {
                    return i;
                }
            }

            return -1;
        }
    }
}