package net.minecraft.client.multiplayer;

import com.google.common.collect.Lists;
import java.io.File;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SideOnly(Side.CLIENT)
public class ServerList
{
    private static final Logger LOGGER = LogManager.getLogger();
    private final Minecraft mc;
    private final List<ServerData> servers = Lists.<ServerData>newArrayList();

    public ServerList(Minecraft mcIn)
    {
        this.mc = mcIn;
        this.loadServerList();
    }

    public void loadServerList()
    {
        try
        {
            this.servers.clear();
            NBTTagCompound nbttagcompound = CompressedStreamTools.read(new File(this.mc.mcDataDir, "servers.dat"));

            if (nbttagcompound == null)
            {
                return;
            }

            NBTTagList nbttaglist = nbttagcompound.getTagList("servers", 10);

            for (int i = 0; i < nbttaglist.tagCount(); ++i)
            {
                this.servers.add(ServerData.getServerDataFromNBTCompound(nbttaglist.getCompoundTagAt(i)));
            }
        }
        catch (Exception exception)
        {
            LOGGER.error("Couldn't load server list", (Throwable)exception);
        }
    }

    public void saveServerList()
    {
        try
        {
            NBTTagList nbttaglist = new NBTTagList();

            for (ServerData serverdata : this.servers)
            {
                nbttaglist.appendTag(serverdata.getNBTCompound());
            }

            NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setTag("servers", nbttaglist);
            CompressedStreamTools.safeWrite(nbttagcompound, new File(this.mc.mcDataDir, "servers.dat"));
        }
        catch (Exception exception)
        {
            LOGGER.error("Couldn't save server list", (Throwable)exception);
        }
    }

    public ServerData getServerData(int index)
    {
        return this.servers.get(index);
    }

    public void removeServerData(int index)
    {
        this.servers.remove(index);
    }

    public void addServerData(ServerData server)
    {
        this.servers.add(server);
    }

    public int countServers()
    {
        return this.servers.size();
    }

    public void swapServers(int pos1, int pos2)
    {
        ServerData serverdata = this.getServerData(pos1);
        this.servers.set(pos1, this.getServerData(pos2));
        this.servers.set(pos2, serverdata);
        this.saveServerList();
    }

    public void set(int index, ServerData server)
    {
        this.servers.set(index, server);
    }

    public static void saveSingleServer(ServerData server)
    {
        ServerList serverlist = new ServerList(Minecraft.getMinecraft());
        serverlist.loadServerList();

        for (int i = 0; i < serverlist.countServers(); ++i)
        {
            ServerData serverdata = serverlist.getServerData(i);

            if (serverdata.serverName.equals(server.serverName) && serverdata.serverIP.equals(server.serverIP))
            {
                serverlist.set(i, server);
                break;
            }
        }

        serverlist.saveServerList();
    }
}