package net.minecraft.client.multiplayer;

import com.google.common.base.MoreObjects;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import javax.annotation.Nullable;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.EmptyChunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@SideOnly(Side.CLIENT)
public class ChunkProviderClient implements IChunkProvider
{
    private static final Logger LOGGER = LogManager.getLogger();
    private final Chunk blankChunk;
    private final Long2ObjectMap<Chunk> chunkMapping = new Long2ObjectOpenHashMap<Chunk>(8192)
    {
        protected void rehash(int p_rehash_1_)
        {
            if (p_rehash_1_ > this.key.length)
            {
                super.rehash(p_rehash_1_);
            }
        }
    };
    private final World world;

    public ChunkProviderClient(World worldIn)
    {
        this.blankChunk = new EmptyChunk(worldIn, 0, 0);
        this.world = worldIn;
    }

    public void unloadChunk(int x, int z)
    {
        Chunk chunk = this.provideChunk(x, z);

        if (!chunk.isEmpty())
        {
            chunk.onUnload();
        }

        this.chunkMapping.remove(ChunkPos.asLong(x, z));
    }

    @Nullable
    public Chunk getLoadedChunk(int x, int z)
    {
        return (Chunk)this.chunkMapping.get(ChunkPos.asLong(x, z));
    }

    public Chunk loadChunk(int chunkX, int chunkZ)
    {
        Chunk chunk = new Chunk(this.world, chunkX, chunkZ);
        this.chunkMapping.put(ChunkPos.asLong(chunkX, chunkZ), chunk);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.world.ChunkEvent.Load(chunk));
        chunk.markLoaded(true);
        return chunk;
    }

    public Chunk provideChunk(int x, int z)
    {
        return (Chunk)MoreObjects.firstNonNull(this.getLoadedChunk(x, z), this.blankChunk);
    }

    public boolean tick()
    {
        long i = System.currentTimeMillis();
        ObjectIterator objectiterator = this.chunkMapping.values().iterator();

        while (objectiterator.hasNext())
        {
            Chunk chunk = (Chunk)objectiterator.next();
            chunk.onTick(System.currentTimeMillis() - i > 5L);
        }

        if (System.currentTimeMillis() - i > 100L)
        {
            LOGGER.info("Warning: Clientside chunk ticking took {} ms", (long)(System.currentTimeMillis() - i));
        }

        return false;
    }

    public String makeString()
    {
        return "MultiplayerChunkCache: " + this.chunkMapping.size() + ", " + this.chunkMapping.size();
    }

    public boolean isChunkGeneratedAt(int x, int z)
    {
        return this.chunkMapping.containsKey(ChunkPos.asLong(x, z));
    }
}