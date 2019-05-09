package net.minecraft.command;

import java.util.Objects;
import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

public class CommandSenderWrapper implements ICommandSender
{
    private final ICommandSender delegate;
    @Nullable
    private final Vec3d positionVector;
    @Nullable
    private final BlockPos position;
    @Nullable
    private final Integer permissionLevel;
    @Nullable
    private final Entity entity;
    @Nullable
    private final Boolean sendCommandFeedback;

    public CommandSenderWrapper(ICommandSender delegateIn, @Nullable Vec3d positionVectorIn, @Nullable BlockPos positionIn, @Nullable Integer permissionLevelIn, @Nullable Entity entityIn, @Nullable Boolean sendCommandFeedbackIn)
    {
        this.delegate = delegateIn;
        this.positionVector = positionVectorIn;
        this.position = positionIn;
        this.permissionLevel = permissionLevelIn;
        this.entity = entityIn;
        this.sendCommandFeedback = sendCommandFeedbackIn;
    }

    public static CommandSenderWrapper create(ICommandSender sender)
    {
        return sender instanceof CommandSenderWrapper ? (CommandSenderWrapper)sender : new CommandSenderWrapper(sender, (Vec3d)null, (BlockPos)null, (Integer)null, (Entity)null, (Boolean)null);
    }

    public CommandSenderWrapper withEntity(Entity entityIn, Vec3d p_193997_2_)
    {
        return this.entity == entityIn && Objects.equals(this.positionVector, p_193997_2_) ? this : new CommandSenderWrapper(this.delegate, p_193997_2_, new BlockPos(p_193997_2_), this.permissionLevel, entityIn, this.sendCommandFeedback);
    }

    public CommandSenderWrapper withPermissionLevel(int level)
    {
        return this.permissionLevel != null && this.permissionLevel.intValue() <= level ? this : new CommandSenderWrapper(this.delegate, this.positionVector, this.position, level, this.entity, this.sendCommandFeedback);
    }

    public CommandSenderWrapper withSendCommandFeedback(boolean sendCommandFeedbackIn)
    {
        return this.sendCommandFeedback == null || this.sendCommandFeedback.booleanValue() && !sendCommandFeedbackIn ? new CommandSenderWrapper(this.delegate, this.positionVector, this.position, this.permissionLevel, this.entity, sendCommandFeedbackIn) : this;
    }

    public CommandSenderWrapper computePositionVector()
    {
        return this.positionVector != null ? this : new CommandSenderWrapper(this.delegate, this.getPositionVector(), this.getPosition(), this.permissionLevel, this.entity, this.sendCommandFeedback);
    }

    public String getName()
    {
        return this.entity != null ? this.entity.getName() : this.delegate.getName();
    }

    public ITextComponent getDisplayName()
    {
        return this.entity != null ? this.entity.getDisplayName() : this.delegate.getDisplayName();
    }

    public void sendMessage(ITextComponent component)
    {
        if (this.sendCommandFeedback == null || this.sendCommandFeedback.booleanValue())
        {
            this.delegate.sendMessage(component);
        }
    }

    public boolean canUseCommand(int permLevel, String commandName)
    {
        return this.permissionLevel != null && this.permissionLevel.intValue() < permLevel ? false : this.delegate.canUseCommand(permLevel, commandName);
    }

    public BlockPos getPosition()
    {
        if (this.position != null)
        {
            return this.position;
        }
        else
        {
            return this.entity != null ? this.entity.getPosition() : this.delegate.getPosition();
        }
    }

    public Vec3d getPositionVector()
    {
        if (this.positionVector != null)
        {
            return this.positionVector;
        }
        else
        {
            return this.entity != null ? this.entity.getPositionVector() : this.delegate.getPositionVector();
        }
    }

    public World getEntityWorld()
    {
        return this.entity != null ? this.entity.getEntityWorld() : this.delegate.getEntityWorld();
    }

    @Nullable
    public Entity getCommandSenderEntity()
    {
        return this.entity != null ? this.entity.getCommandSenderEntity() : this.delegate.getCommandSenderEntity();
    }

    public boolean sendCommandFeedback()
    {
        return this.sendCommandFeedback != null ? this.sendCommandFeedback.booleanValue() : this.delegate.sendCommandFeedback();
    }

    public void setCommandStat(CommandResultStats.Type type, int amount)
    {
        if (this.entity != null)
        {
            this.entity.setCommandStat(type, amount);
        }
        else
        {
            this.delegate.setCommandStat(type, amount);
        }
    }

    @Nullable
    public MinecraftServer getServer()
    {
        return this.delegate.getServer();
    }
}