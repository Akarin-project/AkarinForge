package net.minecraft.command;

import java.util.UUID;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;

public class CommandEntityData extends CommandBase
{
    public String getName()
    {
        return "entitydata";
    }

    public int getRequiredPermissionLevel()
    {
        return 2;
    }

    public String getUsage(ICommandSender sender)
    {
        return "commands.entitydata.usage";
    }

    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length < 2)
        {
            throw new WrongUsageException("commands.entitydata.usage", new Object[0]);
        }
        else
        {
            Entity entity = getEntity(server, sender, args[0]);

            if (entity instanceof EntityPlayer)
            {
                throw new CommandException("commands.entitydata.noPlayers", new Object[] {entity.getDisplayName()});
            }
            else
            {
                NBTTagCompound nbttagcompound = entityToNBT(entity);
                NBTTagCompound nbttagcompound1 = nbttagcompound.copy();
                NBTTagCompound nbttagcompound2;

                try
                {
                    nbttagcompound2 = JsonToNBT.getTagFromJson(buildString(args, 1));
                }
                catch (NBTException nbtexception)
                {
                    throw new CommandException("commands.entitydata.tagError", new Object[] {nbtexception.getMessage()});
                }

                UUID uuid = entity.getUniqueID();
                nbttagcompound.merge(nbttagcompound2);
                entity.setUniqueId(uuid);

                if (nbttagcompound.equals(nbttagcompound1))
                {
                    throw new CommandException("commands.entitydata.failed", new Object[] {nbttagcompound.toString()});
                }
                else
                {
                    entity.readFromNBT(nbttagcompound);
                    notifyCommandListener(sender, this, "commands.entitydata.success", new Object[] {nbttagcompound.toString()});
                }
            }
        }
    }

    public boolean isUsernameIndex(String[] args, int index)
    {
        return index == 0;
    }
}