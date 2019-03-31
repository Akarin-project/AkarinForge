package net.minecraft.world.gen;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;

public interface IChunkGenerator
{
    Chunk generateChunk(int x, int z);

    void populate(int x, int z);

    boolean generateStructures(Chunk chunkIn, int x, int z);

    List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos);

    @Nullable
    BlockPos getNearestStructurePos(World worldIn, String structureName, BlockPos position, boolean findUnexplored);

    void recreateStructures(Chunk chunkIn, int x, int z);

    boolean isInsideStructure(World worldIn, String structureName, BlockPos pos);
}