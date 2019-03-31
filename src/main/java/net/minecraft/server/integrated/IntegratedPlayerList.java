package net.minecraft.server.integrated;

import com.mojang.authlib.GameProfile;
import java.net.SocketAddress;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.management.PlayerList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class IntegratedPlayerList extends PlayerList
{
    private NBTTagCompound hostPlayerData;

    public IntegratedPlayerList(IntegratedServer server)
    {
        super(server);
        this.setViewDistance(10);
    }

    protected void writePlayerData(EntityPlayerMP playerIn)
    {
        if (playerIn.getName().equals(this.getServerInstance().getServerOwner()))
        {
            this.hostPlayerData = playerIn.writeToNBT(new NBTTagCompound());
        }

        super.writePlayerData(playerIn);
    }

    public String allowUserToConnect(SocketAddress address, GameProfile profile)
    {
        return profile.getName().equalsIgnoreCase(this.getServerInstance().getServerOwner()) && this.getPlayerByUsername(profile.getName()) != null ? "That name is already taken." : super.allowUserToConnect(address, profile);
    }

    public IntegratedServer getServerInstance()
    {
        return (IntegratedServer)super.getServerInstance();
    }

    public NBTTagCompound getHostPlayerData()
    {
        return this.hostPlayerData;
    }
}