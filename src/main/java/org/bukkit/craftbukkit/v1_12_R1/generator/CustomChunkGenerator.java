/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.generator;

import java.util.List;
import java.util.Random;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.block.CraftBlock;
import org.bukkit.craftbukkit.v1_12_R1.generator.CraftChunkData;
import org.bukkit.craftbukkit.v1_12_R1.generator.InternalChunkGenerator;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;

public class CustomChunkGenerator
extends InternalChunkGenerator {
    private final ChunkGenerator generator;
    private final oo world;
    private final Random random;
    private final bbs strongholdGen = new bbs();

    public CustomChunkGenerator(amu world, long seed, ChunkGenerator generator) {
        this.world = (oo)world;
        this.generator = generator;
        this.random = new Random(seed);
    }

    @Override
    public axw a(int x2, int z2) {
        axx[] csect;
        axw chunk;
        int sec;
        int i2;
        this.random.setSeed((long)x2 * 341873128712L + (long)z2 * 132897987541L);
        CustomBiomeGrid biomegrid = new CustomBiomeGrid();
        biomegrid.biome = new anh[256];
        this.world.C().b(biomegrid.biome, x2 << 4, z2 << 4, 16, 16);
        CraftChunkData data = (CraftChunkData)this.generator.generateChunkData(this.world.getWorld(), this.random, x2, z2, biomegrid);
        if (data != null) {
            char[][] sections = data.getRawChunkData();
            chunk = new axw(this.world, x2, z2);
            csect = chunk.h();
            int scnt = Math.min(csect.length, sections.length);
            for (sec = 0; sec < scnt; ++sec) {
                if (sections[sec] == null) continue;
                char[] section = sections[sec];
                char emptyTest = '\u0000';
                for (i2 = 0; i2 < 4096; ++i2) {
                    if (aow.i.a(section[i2]) == null) {
                        section[i2] = '\u0000';
                    }
                    emptyTest = (char)(emptyTest | section[i2]);
                }
                if (emptyTest == '\u0000') continue;
                csect[sec] = new axx(sec << 4, true, section);
            }
        } else {
            aow b2;
            short[][] xbtypes = this.generator.generateExtBlockSections(this.world.getWorld(), this.random, x2, z2, biomegrid);
            if (xbtypes != null) {
                chunk = new axw(this.world, x2, z2);
                csect = chunk.h();
                int scnt = Math.min(csect.length, xbtypes.length);
                for (sec = 0; sec < scnt; ++sec) {
                    if (xbtypes[sec] == null) continue;
                    char[] secBlkID = new char[4096];
                    short[] bdata = xbtypes[sec];
                    for (i2 = 0; i2 < bdata.length; ++i2) {
                        b2 = aow.c(bdata[i2]);
                        secBlkID[i2] = (char)aow.i.a(b2.t());
                    }
                    csect[sec] = new axx(sec << 4, true, secBlkID);
                }
            } else {
                byte[][] btypes = this.generator.generateBlockSections(this.world.getWorld(), this.random, x2, z2, biomegrid);
                if (btypes != null) {
                    chunk = new axw(this.world, x2, z2);
                    axx[] csect2 = chunk.h();
                    int scnt = Math.min(csect2.length, btypes.length);
                    for (int sec2 = 0; sec2 < scnt; ++sec2) {
                        if (btypes[sec2] == null) continue;
                        char[] secBlkID = new char[4096];
                        for (i2 = 0; i2 < secBlkID.length; ++i2) {
                            b2 = aow.c(btypes[sec2][i2] & 255);
                            secBlkID[i2] = (char)aow.i.a(b2.t());
                        }
                        csect2[sec2] = new axx(sec2 << 4, true, secBlkID);
                    }
                } else {
                    byte[] types = this.generator.generate(this.world.getWorld(), this.random, x2, z2);
                    int ydim = types.length / 256;
                    int scnt = ydim / 16;
                    chunk = new axw(this.world, x2, z2);
                    axx[] csect3 = chunk.h();
                    scnt = Math.min(scnt, csect3.length);
                    for (int sec3 = 0; sec3 < scnt; ++sec3) {
                        char[] csbytes = null;
                        for (int cy2 = 0; cy2 < 16; ++cy2) {
                            int cyoff = cy2 | sec3 << 4;
                            for (int cx2 = 0; cx2 < 16; ++cx2) {
                                int cxyoff = cx2 * ydim * 16 + cyoff;
                                for (int cz2 = 0; cz2 < 16; ++cz2) {
                                    byte blk2 = types[cxyoff + cz2 * ydim];
                                    if (blk2 == 0) continue;
                                    if (csbytes == null) {
                                        csbytes = new char[4096];
                                    }
                                    aow b3 = aow.c(blk2 & 255);
                                    csbytes[cy2 << 8 | cz2 << 4 | cx2] = (char)aow.i.a(b3.t());
                                }
                            }
                        }
                        if (csbytes == null) continue;
                        axx cs2 = csect3[sec3] = new axx(sec3 << 4, true, csbytes);
                        cs2.e();
                    }
                }
            }
        }
        byte[] biomeIndex = chunk.l();
        for (int i3 = 0; i3 < biomeIndex.length; ++i3) {
            biomeIndex[i3] = (byte)(anh.p.a(biomegrid.biome[i3]) & 255);
        }
        chunk.b();
        return chunk;
    }

    @Override
    public boolean a(axw chunk, int i2, int i1) {
        return false;
    }

    @Override
    public byte[] generate(World world, Random random, int x2, int z2) {
        return this.generator.generate(world, random, x2, z2);
    }

    @Override
    public byte[][] generateBlockSections(World world, Random random, int x2, int z2, ChunkGenerator.BiomeGrid biomes) {
        return this.generator.generateBlockSections(world, random, x2, z2, biomes);
    }

    @Override
    public short[][] generateExtBlockSections(World world, Random random, int x2, int z2, ChunkGenerator.BiomeGrid biomes) {
        return this.generator.generateExtBlockSections(world, random, x2, z2, biomes);
    }

    public axw getChunkAt(int x2, int z2) {
        return this.a(x2, z2);
    }

    @Override
    public boolean canSpawn(World world, int x2, int z2) {
        return this.generator.canSpawn(world, x2, z2);
    }

    @Override
    public List<BlockPopulator> getDefaultPopulators(World world) {
        return this.generator.getDefaultPopulators(world);
    }

    @Override
    public List<anh.c> a(vr type, et position) {
        anh biomebase = this.world.b(position);
        return biomebase == null ? null : biomebase.a(type);
    }

    @Override
    public boolean a(amu world, String type, et position) {
        return "Stronghold".equals(type) && this.strongholdGen != null ? this.strongholdGen.b(position) : false;
    }

    @Override
    public et a(amu world, String type, et position, boolean flag) {
        return "Stronghold".equals(type) && this.strongholdGen != null ? this.strongholdGen.a(world, position, flag) : null;
    }

    @Override
    public void b(int i2, int j2) {
    }

    @Override
    public void b(axw chunk, int i2, int j2) {
        this.strongholdGen.a(this.world, i2, j2, null);
    }

    private static class CustomBiomeGrid
    implements ChunkGenerator.BiomeGrid {
        anh[] biome;

        private CustomBiomeGrid() {
        }

        @Override
        public Biome getBiome(int x2, int z2) {
            return CraftBlock.biomeBaseToBiome(this.biome[z2 << 4 | x2]);
        }

        @Override
        public void setBiome(int x2, int z2, Biome bio) {
            this.biome[z2 << 4 | x2] = CraftBlock.biomeToBiomeBase(bio);
        }
    }

}

