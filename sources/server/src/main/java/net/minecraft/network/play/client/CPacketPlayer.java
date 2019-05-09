package net.minecraft.network.play.client;

import java.io.IOException;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.INetHandlerPlayServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CPacketPlayer implements Packet<INetHandlerPlayServer>
{
    protected double x;
    protected double y;
    protected double z;
    protected float yaw;
    protected float pitch;
    protected boolean onGround;
    protected boolean moving;
    protected boolean rotating;

    public CPacketPlayer()
    {
    }

    @SideOnly(Side.CLIENT)
    public CPacketPlayer(boolean onGroundIn)
    {
        this.onGround = onGroundIn;
    }

    public void processPacket(INetHandlerPlayServer handler)
    {
        handler.processPlayer(this);
    }

    public void readPacketData(PacketBuffer buf) throws IOException
    {
        this.onGround = buf.readUnsignedByte() != 0;
    }

    public void writePacketData(PacketBuffer buf) throws IOException
    {
        buf.writeByte(this.onGround ? 1 : 0);
    }

    public double getX(double defaultValue)
    {
        return this.moving ? this.x : defaultValue;
    }

    public double getY(double defaultValue)
    {
        return this.moving ? this.y : defaultValue;
    }

    public double getZ(double defaultValue)
    {
        return this.moving ? this.z : defaultValue;
    }

    public float getYaw(float defaultValue)
    {
        return this.rotating ? this.yaw : defaultValue;
    }

    public float getPitch(float defaultValue)
    {
        return this.rotating ? this.pitch : defaultValue;
    }

    public boolean isOnGround()
    {
        return this.onGround;
    }

    public static class Position extends CPacketPlayer
        {
            public Position()
            {
                this.moving = true;
            }

            @SideOnly(Side.CLIENT)
            public Position(double xIn, double yIn, double zIn, boolean onGroundIn)
            {
                this.x = xIn;
                this.y = yIn;
                this.z = zIn;
                this.onGround = onGroundIn;
                this.moving = true;
            }

            public void readPacketData(PacketBuffer buf) throws IOException
            {
                this.x = buf.readDouble();
                this.y = buf.readDouble();
                this.z = buf.readDouble();
                super.readPacketData(buf);
            }

            public void writePacketData(PacketBuffer buf) throws IOException
            {
                buf.writeDouble(this.x);
                buf.writeDouble(this.y);
                buf.writeDouble(this.z);
                super.writePacketData(buf);
            }
        }

    public static class PositionRotation extends CPacketPlayer
        {
            public PositionRotation()
            {
                this.moving = true;
                this.rotating = true;
            }

            @SideOnly(Side.CLIENT)
            public PositionRotation(double xIn, double yIn, double zIn, float yawIn, float pitchIn, boolean onGroundIn)
            {
                this.x = xIn;
                this.y = yIn;
                this.z = zIn;
                this.yaw = yawIn;
                this.pitch = pitchIn;
                this.onGround = onGroundIn;
                this.rotating = true;
                this.moving = true;
            }

            public void readPacketData(PacketBuffer buf) throws IOException
            {
                this.x = buf.readDouble();
                this.y = buf.readDouble();
                this.z = buf.readDouble();
                this.yaw = buf.readFloat();
                this.pitch = buf.readFloat();
                super.readPacketData(buf);
            }

            public void writePacketData(PacketBuffer buf) throws IOException
            {
                buf.writeDouble(this.x);
                buf.writeDouble(this.y);
                buf.writeDouble(this.z);
                buf.writeFloat(this.yaw);
                buf.writeFloat(this.pitch);
                super.writePacketData(buf);
            }
        }

    public static class Rotation extends CPacketPlayer
        {
            public Rotation()
            {
                this.rotating = true;
            }

            @SideOnly(Side.CLIENT)
            public Rotation(float yawIn, float pitchIn, boolean onGroundIn)
            {
                this.yaw = yawIn;
                this.pitch = pitchIn;
                this.onGround = onGroundIn;
                this.rotating = true;
            }

            public void readPacketData(PacketBuffer buf) throws IOException
            {
                this.yaw = buf.readFloat();
                this.pitch = buf.readFloat();
                super.readPacketData(buf);
            }

            public void writePacketData(PacketBuffer buf) throws IOException
            {
                buf.writeFloat(this.yaw);
                buf.writeFloat(this.pitch);
                super.writePacketData(buf);
            }
        }
}