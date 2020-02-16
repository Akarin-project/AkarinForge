package net.minecraft.tileentity;

import io.netty.buffer.ByteBuf;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javax.annotation.Nullable;

import org.apache.logging.log4j.Level;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_12_R1.command.VanillaCommandWrapper;
import org.bukkit.event.server.ServerCommandEvent;

import com.google.common.base.Joiner;

import net.minecraft.command.CommandException;
import net.minecraft.command.CommandResultStats;
import net.minecraft.command.CommandSenderWrapper;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.ICommandSender;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.entity.item.EntityMinecartCommandBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.rcon.RConConsoleSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.util.ReportedException;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class CommandBlockBaseLogic implements ICommandSender
{
    private static final SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("HH:mm:ss");
    private long lastExecution = -1L;
    private boolean updateLastExecution = true;
    private int successCount;
    private boolean trackOutput = true;
    private ITextComponent lastOutput;
    private String commandStored = "";
    private String customName = "@";
    private final CommandResultStats resultStats = new CommandResultStats();
    protected org.bukkit.command.CommandSender sender; // CraftBukkit - add sender

    public int getSuccessCount()
    {
        return this.successCount;
    }

    public void setSuccessCount(int successCountIn)
    {
        this.successCount = successCountIn;
    }

    public ITextComponent getLastOutput()
    {
        return (ITextComponent)(this.lastOutput == null ? new TextComponentString("") : this.lastOutput);
    }

    public NBTTagCompound writeToNBT(NBTTagCompound p_189510_1_)
    {
        p_189510_1_.setString("Command", this.commandStored);
        p_189510_1_.setInteger("SuccessCount", this.successCount);
        p_189510_1_.setString("CustomName", this.customName);
        p_189510_1_.setBoolean("TrackOutput", this.trackOutput);

        if (this.lastOutput != null && this.trackOutput)
        {
            p_189510_1_.setString("LastOutput", ITextComponent.Serializer.componentToJson(this.lastOutput));
        }

        p_189510_1_.setBoolean("UpdateLastExecution", this.updateLastExecution);

        if (this.updateLastExecution && this.lastExecution > 0L)
        {
            p_189510_1_.setLong("LastExecution", this.lastExecution);
        }

        this.resultStats.writeStatsToNBT(p_189510_1_);
        return p_189510_1_;
    }

    public void readDataFromNBT(NBTTagCompound nbt)
    {
        this.commandStored = nbt.getString("Command");
        this.successCount = nbt.getInteger("SuccessCount");

        if (nbt.hasKey("CustomName", 8))
        {
            this.customName = nbt.getString("CustomName");
        }

        if (nbt.hasKey("TrackOutput", 1))
        {
            this.trackOutput = nbt.getBoolean("TrackOutput");
        }

        if (nbt.hasKey("LastOutput", 8) && this.trackOutput)
        {
            try
            {
                this.lastOutput = ITextComponent.Serializer.jsonToComponent(nbt.getString("LastOutput"));
            }
            catch (Throwable throwable)
            {
                this.lastOutput = new TextComponentString(throwable.getMessage());
            }
        }
        else
        {
            this.lastOutput = null;
        }

        if (nbt.hasKey("UpdateLastExecution"))
        {
            this.updateLastExecution = nbt.getBoolean("UpdateLastExecution");
        }

        if (this.updateLastExecution && nbt.hasKey("LastExecution"))
        {
            this.lastExecution = nbt.getLong("LastExecution");
        }
        else
        {
            this.lastExecution = -1L;
        }

        this.resultStats.readStatsFromNBT(nbt);
    }

    public boolean canUseCommand(int permLevel, String commandName)
    {
        return permLevel <= 2;
    }

    public void setCommand(String command)
    {
        this.commandStored = command;
        this.successCount = 0;
    }

    public String getCommand()
    {
        return this.commandStored;
    }

    public boolean trigger(World worldIn)
    {
        if (!worldIn.isRemote && worldIn.getTotalWorldTime() != this.lastExecution)
        {
            if ("Searge".equalsIgnoreCase(this.commandStored))
            {
                this.lastOutput = new TextComponentString("#itzlipofutzli");
                this.successCount = 1;
                return true;
            }
            else
            {
                MinecraftServer minecraftserver = this.getServer();

                if (minecraftserver != null && minecraftserver.isAnvilFileSet() && minecraftserver.isCommandBlockEnabled())
                {
                    try
                    {
                        this.lastOutput = null;
                        this.successCount = executeSafely(this, sender, this.commandStored); // CraftBukkit - Handle command block commands using Bukkit dispatcher
                    }
                    catch (Throwable throwable)
                    {
                        CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Executing command block");
                        CrashReportCategory crashreportcategory = crashreport.makeCategory("Command to be executed");
                        crashreportcategory.addDetail("Command", new ICrashReportDetail<String>()
                        {
                            public String call() throws Exception
                            {
                                return CommandBlockBaseLogic.this.getCommand();
                            }
                        });
                        crashreportcategory.addDetail("Name", new ICrashReportDetail<String>()
                        {
                            public String call() throws Exception
                            {
                                return CommandBlockBaseLogic.this.getName();
                            }
                        });
                        throw new ReportedException(crashreport);
                    }
                }
                else
                {
                    this.successCount = 0;
                }

                if (this.updateLastExecution)
                {
                    this.lastExecution = worldIn.getTotalWorldTime();
                }
                else
                {
                    this.lastExecution = -1L;
                }

                return true;
            }
        }
        else
        {
            return false;
        }
    }
    // CraftBukkit start
    public static int executeSafely(ICommandSender sender, org.bukkit.command.CommandSender bSender, String command) {
        try {
            return executeCommand(sender, bSender, command);
        } catch (CommandException commandexception) {
            // Taken from CommandHandler
            TextComponentTranslation chatmessage = new TextComponentTranslation(commandexception.getMessage(), commandexception.getErrorObjects());
            chatmessage.getStyle().setColor(TextFormatting.RED);
            sender.sendMessage(chatmessage);
        }

        return 0;
    }

    public static int executeCommand(ICommandSender sender, org.bukkit.command.CommandSender bSender, String command) throws CommandException {
        org.bukkit.command.SimpleCommandMap commandMap = sender.getEntityWorld().getServer().getCommandMap();
        Joiner joiner = Joiner.on(" ");
        if (command.startsWith("/")) {
            command = command.substring(1);
        }

        ServerCommandEvent event = new ServerCommandEvent(bSender, command);
        org.bukkit.Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            return 0;
        }
        command = event.getCommand();

        String[] args = command.split(" ");
        ArrayList<String[]> commands = new ArrayList<String[]>();

        String cmd = args[0];
        if (cmd.startsWith("minecraft:")) cmd = cmd.substring("minecraft:".length());
        if (cmd.startsWith("bukkit:")) cmd = cmd.substring("bukkit:".length());

        // Block disallowed commands
        if (cmd.equalsIgnoreCase("stop") || cmd.equalsIgnoreCase("kick") || cmd.equalsIgnoreCase("op")
                || cmd.equalsIgnoreCase("deop") || cmd.equalsIgnoreCase("ban") || cmd.equalsIgnoreCase("ban-ip")
                || cmd.equalsIgnoreCase("pardon") || cmd.equalsIgnoreCase("pardon-ip") || cmd.equalsIgnoreCase("reload")) {
            return 0;
        }

        // Handle vanilla commands;
        org.bukkit.command.Command commandBlockCommand = commandMap.getCommand(args[0]);
        if (sender.getEntityWorld().getServer().getCommandBlockOverride(args[0])) {
            commandBlockCommand = commandMap.getCommand("minecraft:" + args[0]);
        }
        if (commandBlockCommand instanceof VanillaCommandWrapper) {
            command = command.trim();
            if (command.startsWith("/")) {
                command = command.substring(1);
            }
            String as[] = command.split(" ");
            as = VanillaCommandWrapper.dropFirstArgument(as);
            if (!sender.getEntityWorld().getServer().getPermissionOverride(sender) && !((VanillaCommandWrapper) commandBlockCommand).testPermission(bSender)) {
                return 0;
            }
            return ((VanillaCommandWrapper) commandBlockCommand).dispatchVanillaCommand(bSender, sender, as);
        }

        // Make sure this is a valid command
        if (commandMap.getCommand(args[0]) == null) {
            return 0;
        }

        commands.add(args);

        {
            ArrayList<String[]> newCommands = new ArrayList<String[]>();
            for (int i = 0; i < args.length; i++) {
                if (EntitySelector.isSelector(args[i])) {
                    for (int j = 0; j < commands.size(); j++) {
                        newCommands.addAll(buildCommands(sender, commands.get(j), i));
                    }
                    ArrayList<String[]> temp = commands;
                    commands = newCommands;
                    newCommands = temp;
                    newCommands.clear();
                }
            }
        }

        int completed = 0;

        // Now dispatch all of the commands we ended up with
        for (int i = 0; i < commands.size(); i++) {
            try {
                if (commandMap.dispatch(bSender, joiner.join(java.util.Arrays.asList(commands.get(i))))) {
                    completed++;
                }
            } catch (Throwable exception) {
                if (sender.getCommandSenderEntity() instanceof EntityMinecartCommandBlock) {
                    MinecraftServer.LOGGER.warn(String.format("MinecartCommandBlock at (%d,%d,%d) failed to handle command", sender.getPosition().getX(), sender.getPosition().getY(), sender.getPosition().getZ()), exception);
                } else if (sender instanceof CommandBlockBaseLogic) {
                    CommandBlockBaseLogic listener = (CommandBlockBaseLogic) sender;
                    MinecraftServer.LOGGER.warn(String.format("CommandBlock at (%d,%d,%d) failed to handle command", listener.getPosition().getX(), listener.getPosition().getY(), listener.getPosition().getZ()), exception);
                } else {
                    MinecraftServer.LOGGER.warn(String.format("Unknown CommandBlock failed to handle command"), exception);
                }
            }
        }

        return completed;
    }

    private static ArrayList<String[]> buildCommands(ICommandSender sender, String[] args, int pos) throws CommandException {
        ArrayList<String[]> commands = new ArrayList<String[]>();
        java.util.List<EntityPlayerMP> players = (java.util.List<EntityPlayerMP>)EntitySelector.matchEntities(sender, args[pos], EntityPlayerMP.class);

        if (players != null) {
            for (EntityPlayerMP player : players) {
                if (player.world != sender.getEntityWorld()) {
                    continue;
                }
                String[] command = args.clone();
                command[pos] = player.getName();
                commands.add(command);
            }
        }

        return commands;
    }

    public static CommandSender unwrapSender(ICommandSender listener) {
        org.bukkit.command.CommandSender sender = null;
        while (sender == null) {
            if (listener instanceof DedicatedServer) {
                sender = ((DedicatedServer) listener).console;
            } else if (listener instanceof RConConsoleSource) {
                sender = ((RConConsoleSource) listener).getServer().remoteConsole;
            } else if (listener instanceof CommandBlockBaseLogic) {
                sender = ((CommandBlockBaseLogic) listener).sender;
            } else if (listener instanceof CommandSenderWrapper) {
                listener = ((CommandSenderWrapper) listener).delegate; // Search deeper
            } else if (VanillaCommandWrapper.lastSender != null) {
                sender = VanillaCommandWrapper.lastSender;
            } else if (listener.getCommandSenderEntity() != null) {
                sender = listener.getCommandSenderEntity().getBukkitEntity();
            } else {
                throw new RuntimeException("Unhandled executor " + listener.getClass().getSimpleName());
            }
        }

        return sender;
    }
    // CraftBukkit end

    public String getName()
    {
        return this.customName;
    }

    public void setName(String name)
    {
        this.customName = name;
    }

    public void sendMessage(ITextComponent component)
    {
        if (this.trackOutput && this.getEntityWorld() != null && !this.getEntityWorld().isRemote)
        {
            this.lastOutput = (new TextComponentString("[" + TIMESTAMP_FORMAT.format(new Date()) + "] ")).appendSibling(component);
            this.updateCommand();
        }
    }

    public boolean sendCommandFeedback()
    {
        MinecraftServer minecraftserver = this.getServer();
        return minecraftserver == null || !minecraftserver.isAnvilFileSet() || minecraftserver.worlds[0].getGameRules().getBoolean("commandBlockOutput");
    }

    public void setCommandStat(CommandResultStats.Type type, int amount)
    {
        this.resultStats.setCommandStatForSender(this.getServer(), this, type, amount);
    }

    public abstract void updateCommand();

    @SideOnly(Side.CLIENT)
    public abstract int getCommandBlockType();

    @SideOnly(Side.CLIENT)
    public abstract void fillInInfo(ByteBuf buf);

    public void setLastOutput(@Nullable ITextComponent lastOutputMessage)
    {
        this.lastOutput = lastOutputMessage;
    }

    public void setTrackOutput(boolean shouldTrackOutput)
    {
        this.trackOutput = shouldTrackOutput;
    }

    public boolean shouldTrackOutput()
    {
        return this.trackOutput;
    }

    public boolean tryOpenEditCommandBlock(EntityPlayer playerIn)
    {
        if (!playerIn.canUseCommandBlock())
        {
            return false;
        }
        else
        {
            if (playerIn.getEntityWorld().isRemote)
            {
                playerIn.displayGuiEditCommandCart(this);
            }

            return true;
        }
    }

    public CommandResultStats getCommandResultStats()
    {
        return this.resultStats;
    }
}