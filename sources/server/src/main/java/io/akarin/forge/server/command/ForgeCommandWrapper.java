package io.akarin.forge.server.command;

import java.util.Iterator;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityMinecartCommandBlock;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.tileentity.CommandBlockBaseLogic;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.translation.I18n;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.Level;
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

public class ForgeCommandWrapper extends org.bukkit.command.Command {
    private final ICommand command;

    public ForgeCommandWrapper(ICommand command) {
        super(command.getName(), "A Forge provided command.", I18n.translateToLocal(command.getUsage(null)), command.getAliases());
        this.command = command;
        this.setPermission("forge.command." + command.getName());
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!testPermission(sender)) return true;
        
        ICommandSender icommandlistener = getListener(sender);
        try {
            dispatchVanillaCommand(sender, icommandlistener, args);
        } catch (CommandException commandexception) {
            // Taken from CommandHandler
            TextComponentTranslation chatmessage = new TextComponentTranslation(commandexception.getMessage(), commandexception.getErrorObjects());
            chatmessage.getStyle().setColor(TextFormatting.RED);
            icommandlistener.sendMessage(chatmessage);
        }
        return true;
    }
    
    public final int dispatchVanillaCommand(CommandSender bSender, ICommandSender icommandlistener, String[] as) throws CommandException {
        int i = getPlayerListSize(as);
        int j = 0;
        MinecraftServer server = MinecraftServer.instance();

        try {
            if (command.checkPermission(server, icommandlistener)) {
                if (i > -1) {
                    List<Entity> list = ((List<Entity>)EntitySelector.matchEntities(icommandlistener, as[i], Entity.class));
                    String s2 = as[i];

                    icommandlistener.setCommandStat(CommandResultStats.Type.AFFECTED_ENTITIES, list.size());
                    Iterator<Entity> iterator = list.iterator();

                    while (iterator.hasNext()) {
                        Entity entity = iterator.next();

                        try {
                            as[i] = entity.getUniqueID().toString();
                            command.execute(server, icommandlistener, as);
                            j++;
                        } catch (WrongUsageException exceptionusage) {
                            TextComponentTranslation chatmessage = new TextComponentTranslation("commands.generic.usage", new Object[] { new TextComponentTranslation(exceptionusage.getMessage(), exceptionusage.getErrorObjects())});
                            chatmessage.getStyle().setColor(TextFormatting.RED);
                            icommandlistener.sendMessage(chatmessage);
                        } catch (CommandException commandexception) {
                            CommandBase.notifyCommandListener(icommandlistener, command, 0, commandexception.getMessage(), commandexception.getErrorObjects());
                        }
                    }
                    as[i] = s2;
                } else {
                    icommandlistener.setCommandStat(CommandResultStats.Type.AFFECTED_ENTITIES, 1);
                    command.execute(server, icommandlistener, as);
                    j++;
                }
            } else {
                TextComponentTranslation chatmessage = new TextComponentTranslation("commands.generic.permission", new Object[0]);
                chatmessage.getStyle().setColor(TextFormatting.RED);
                icommandlistener.sendMessage(chatmessage);
            }
        } catch (WrongUsageException exceptionusage) {
            TextComponentTranslation chatmessage1 = new TextComponentTranslation("commands.generic.usage", new Object[] { new TextComponentTranslation(exceptionusage.getMessage(), exceptionusage.getErrorObjects()) });
            chatmessage1.getStyle().setColor(TextFormatting.RED);
            icommandlistener.sendMessage(chatmessage1);
        } catch (CommandException commandexception) {
            CommandBase.notifyCommandListener(icommandlistener, command, 0, commandexception.getMessage(), commandexception.getErrorObjects());
        } catch (Throwable throwable) {
            TextComponentTranslation chatmessage3 = new TextComponentTranslation("commands.generic.exception", new Object[0]);
            chatmessage3.getStyle().setColor(TextFormatting.RED);
            icommandlistener.sendMessage(chatmessage3);
            if (icommandlistener.getCommandSenderEntity() instanceof EntityMinecartCommandBlock) {
                MinecraftServer.LOGGER.log(Level.WARN, String.format("MinecartCommandBlock at (%d,%d,%d) failed to handle command", icommandlistener.getPosition().getX(), icommandlistener.getPosition().getY(), icommandlistener.getPosition().getZ()), throwable);
            } else if(icommandlistener instanceof CommandBlockBaseLogic) {
                CommandBlockBaseLogic listener = (CommandBlockBaseLogic) icommandlistener;
                MinecraftServer.LOGGER.log(Level.WARN, String.format("CommandBlock at (%d,%d,%d) failed to handle command", listener.getPosition().getX(), listener.getPosition().getY(), listener.getPosition().getZ()), throwable);
            } else {
                MinecraftServer.LOGGER.log(Level.WARN, String.format("Unknown CommandBlock failed to handle command"), throwable);
            }
        } finally {
            icommandlistener.setCommandStat(CommandResultStats.Type.SUCCESS_COUNT, j);
        }
        return j;
    }
    
    private int getPlayerListSize(String as[]) throws CommandException {
        for (int i = 0; i < as.length; i++) {
            if (command.isUsernameIndex(as, i) && EntitySelector.matchesMultiplePlayers(as[i])) {
                return i;
            }
        }
        return -1;
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

