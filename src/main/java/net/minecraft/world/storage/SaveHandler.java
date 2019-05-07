package net.minecraft.world.storage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import javax.annotation.Nullable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.datafix.DataFixer;
import net.minecraft.util.datafix.FixTypes;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.gen.structure.template.TemplateManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.craftbukkit.entity.CraftPlayer;

public class SaveHandler implements ISaveHandler, IPlayerFileData
{
    private static final Logger LOGGER = LogManager.getLogger();
    private final File worldDirectory;
    private final File playersDirectory;
    private final File mapDataDir;
    private final long initializationTime = MinecraftServer.getCurrentTimeMillis();
    private final String saveDirectoryName;
    private final TemplateManager structureTemplateManager;
    protected final DataFixer dataFixer;

    public SaveHandler(File p_i46648_1_, String saveDirectoryNameIn, boolean p_i46648_3_, DataFixer dataFixerIn)
    {
        this.dataFixer = dataFixerIn;
        this.worldDirectory = new File(p_i46648_1_, saveDirectoryNameIn);
        this.worldDirectory.mkdirs();
        this.playersDirectory = new File(this.worldDirectory, "playerdata");
        this.mapDataDir = new File(this.worldDirectory, "data");
        this.mapDataDir.mkdirs();
        this.saveDirectoryName = saveDirectoryNameIn;

        if (p_i46648_3_)
        {
            this.playersDirectory.mkdirs();
            this.structureTemplateManager = new TemplateManager((new File(this.worldDirectory, "structures")).toString(), dataFixerIn);
        }
        else
        {
            this.structureTemplateManager = null;
        }

        this.setSessionLock();
    }

    private void setSessionLock()
    {
        try
        {
            File file1 = new File(this.worldDirectory, "session.lock");
            DataOutputStream dataoutputstream = new DataOutputStream(new FileOutputStream(file1));

            try
            {
                dataoutputstream.writeLong(this.initializationTime);
            }
            finally
            {
                dataoutputstream.close();
            }
        }
        catch (IOException ioexception)
        {
            ioexception.printStackTrace();
            throw new RuntimeException("Failed to check session lock, aborting");
        }
    }

    public File getWorldDirectory()
    {
        return this.worldDirectory;
    }

    public void checkSessionLock() throws MinecraftException
    {
        try
        {
            File file1 = new File(this.worldDirectory, "session.lock");
            DataInputStream datainputstream = new DataInputStream(new FileInputStream(file1));

            try
            {
                if (datainputstream.readLong() != this.initializationTime)
                {
                    throw new MinecraftException("The save is being accessed from another location, aborting");
                }
            }
            finally
            {
                datainputstream.close();
            }
        }
        catch (IOException var7)
        {
            throw new MinecraftException("Failed to check session lock, aborting");
        }
    }

    public IChunkLoader getChunkLoader(WorldProvider provider)
    {
        throw new RuntimeException("Old Chunk Storage is no longer supported.");
    }

    @Nullable
    public WorldInfo loadWorldInfo()
    {
        File file1 = new File(this.worldDirectory, "level.dat");

        if (file1.exists())
        {
            WorldInfo worldinfo = SaveFormatOld.loadAndFix(file1, this.dataFixer, this);

            if (worldinfo != null)
            {
                return worldinfo;
            }
        }

        net.minecraftforge.fml.common.FMLCommonHandler.instance().confirmBackupLevelDatUse(this);
        file1 = new File(this.worldDirectory, "level.dat_old");
        return file1.exists() ? SaveFormatOld.loadAndFix(file1, this.dataFixer, this) : null;
    }

    public void saveWorldInfoWithPlayer(WorldInfo worldInformation, @Nullable NBTTagCompound tagCompound)
    {
        NBTTagCompound nbttagcompound = worldInformation.cloneNBTCompound(tagCompound);
        NBTTagCompound nbttagcompound1 = new NBTTagCompound();
        nbttagcompound1.setTag("Data", nbttagcompound);

        net.minecraftforge.fml.common.FMLCommonHandler.instance().handleWorldDataSave(this, worldInformation, nbttagcompound1);

        try
        {
            File file1 = new File(this.worldDirectory, "level.dat_new");
            File file2 = new File(this.worldDirectory, "level.dat_old");
            File file3 = new File(this.worldDirectory, "level.dat");
            CompressedStreamTools.writeCompressed(nbttagcompound1, new FileOutputStream(file1));

            if (file2.exists())
            {
                file2.delete();
            }

            file3.renameTo(file2);

            if (file3.exists())
            {
                file3.delete();
            }

            file1.renameTo(file3);

            if (file1.exists())
            {
                file1.delete();
            }
        }
        catch (Exception exception)
        {
            exception.printStackTrace();
        }
    }

    public void saveWorldInfo(WorldInfo worldInformation)
    {
        this.saveWorldInfoWithPlayer(worldInformation, (NBTTagCompound)null);
    }

    public void writePlayerData(EntityPlayer player)
    {
        try
        {
            NBTTagCompound nbttagcompound = player.writeToNBT(new NBTTagCompound());
            File file1 = new File(this.playersDirectory, player.getCachedUniqueIdString() + ".dat.tmp");
            File file2 = new File(this.playersDirectory, player.getCachedUniqueIdString() + ".dat");
            CompressedStreamTools.writeCompressed(nbttagcompound, new FileOutputStream(file1));

            if (file2.exists())
            {
                file2.delete();
            }

            file1.renameTo(file2);
            net.minecraftforge.event.ForgeEventFactory.firePlayerSavingEvent(player, this.playersDirectory, player.getUniqueID().toString());
        }
        catch (Exception var5)
        {
            LOGGER.warn("Failed to save player data for {}", (Object)player.getName());
        }
    }

    @Nullable
    public NBTTagCompound readPlayerData(EntityPlayer player)
    {
        NBTTagCompound nbttagcompound = null;

        try
        {
            File file1 = new File(this.playersDirectory, player.getCachedUniqueIdString() + ".dat");

            if (file1.exists() && file1.isFile())
            {
                nbttagcompound = CompressedStreamTools.readCompressed(new FileInputStream(file1));
            }
        }
        catch (Exception var4)
        {
            LOGGER.warn("Failed to load player data for {}", (Object)player.getName());
        }

        if (nbttagcompound != null)
        {
            // Akarin start
            if (player instanceof EntityPlayerMP) {
                CraftPlayer cplayer = (CraftPlayer) player.getBukkitEntity();
                // Only update first played if it is older than the one we have
                long modified = new File(this.playersDirectory, player.getUniqueID().toString() + ".dat").lastModified();
                if (modified < cplayer.getFirstPlayed()) {
                    cplayer.setFirstPlayed(modified);
                }
            }
            // Akarin end
            player.readFromNBT(this.dataFixer.process(FixTypes.PLAYER, nbttagcompound));
        }

        net.minecraftforge.event.ForgeEventFactory.firePlayerLoadingEvent(player, playersDirectory, player.getUniqueID().toString());
        return nbttagcompound;
    }

    public IPlayerFileData getPlayerNBTManager()
    {
        return this;
    }

    public String[] getAvailablePlayerDat()
    {
        String[] astring = this.playersDirectory.list();

        if (astring == null)
        {
            astring = new String[0];
        }

        for (int i = 0; i < astring.length; ++i)
        {
            if (astring[i].endsWith(".dat"))
            {
                astring[i] = astring[i].substring(0, astring[i].length() - 4);
            }
        }

        return astring;
    }

    public void flush()
    {
    }

    public File getMapFileFromName(String mapName)
    {
        return new File(this.mapDataDir, mapName + ".dat");
    }

    public TemplateManager getStructureTemplateManager()
    {
        return this.structureTemplateManager;
    }

    public NBTTagCompound getPlayerNBT(net.minecraft.entity.player.EntityPlayerMP player)
    {
        try
        {
            File file1 = new File(this.playersDirectory, player.getUniqueID().toString() + ".dat");

            if (file1.exists() && file1.isFile())
            {
                return CompressedStreamTools.readCompressed(new FileInputStream(file1));
            }
        }
        catch (Exception exception)
        {
            LOGGER.warn("Failed to load player data for " + player.getName());
        }
        return null;
    }
    // Akarin start
    private UUID uuid = null; // CraftBukkit
    
    @Override
    public UUID getUUID() {
        if (uuid != null) return uuid;
        File file1 = new File(this.worldDirectory, "uid.dat");
        if (file1.exists()) {
            DataInputStream dis = null;
            try {
                dis = new DataInputStream(new FileInputStream(file1));
                return uuid = new UUID(dis.readLong(), dis.readLong());
            } catch (IOException ex) {
                LOGGER.warn("Failed to read " + file1 + ", generating new random UUID", ex);
            } finally {
                if (dis != null) {
                    try {
                        dis.close();
                    } catch (IOException ex) {
                        // NOOP
                    }
                }
            }
        }
        uuid = UUID.randomUUID();
        DataOutputStream dos = null;
        try {
            dos = new DataOutputStream(new FileOutputStream(file1));
            dos.writeLong(uuid.getMostSignificantBits());
            dos.writeLong(uuid.getLeastSignificantBits());
        } catch (IOException ex) {
            LOGGER.warn("Failed to write " + file1, ex);
        } finally {
            if (dos != null) {
                try {
                    dos.close();
                } catch (IOException ex) {
                    // NOOP
                }
            }
        }
        return uuid;
    }
    
    public File getPlayerDir() {
        return playersDirectory;
    }
    
    public NBTTagCompound getPlayerData(String s) {
        try {
            File file1 = new File(this.playersDirectory, s + ".dat");

            if (file1.exists()) {
                return CompressedStreamTools.readCompressed((InputStream) (new FileInputStream(file1)));
            }
        } catch (Exception exception) {
            LOGGER.warn("Failed to load player data for " + s);
        }

        return null;
    }
    
    
}