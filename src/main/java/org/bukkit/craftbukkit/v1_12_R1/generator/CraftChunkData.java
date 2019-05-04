/*
 * Akarin Forge
 */
package org.bukkit.craftbukkit.v1_12_R1.generator;

import java.util.Arrays;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.material.MaterialData;

public final class CraftChunkData
implements ChunkGenerator.ChunkData {
    private final int maxHeight;
    private final char[][] sections;

    public CraftChunkData(World world) {
        this(world.getMaxHeight());
    }

    CraftChunkData(int maxHeight) {
        if (maxHeight > 256) {
            throw new IllegalArgumentException("World height exceeded max chunk height");
        }
        this.maxHeight = maxHeight;
        this.sections = new char[16][];
    }

    @Override
    public int getMaxHeight() {
        return this.maxHeight;
    }

    @Override
    public void setBlock(int x2, int y2, int z2, Material material) {
        this.setBlock(x2, y2, z2, material.getId());
    }

    @Override
    public void setBlock(int x2, int y2, int z2, MaterialData material) {
        this.setBlock(x2, y2, z2, material.getItemTypeId(), material.getData());
    }

    @Override
    public void setRegion(int xMin, int yMin, int zMin, int xMax, int yMax, int zMax, Material material) {
        this.setRegion(xMin, yMin, zMin, xMax, yMax, zMax, material.getId());
    }

    @Override
    public void setRegion(int xMin, int yMin, int zMin, int xMax, int yMax, int zMax, MaterialData material) {
        this.setRegion(xMin, yMin, zMin, xMax, yMax, zMax, material.getItemTypeId(), material.getData());
    }

    @Override
    public Material getType(int x2, int y2, int z2) {
        return Material.getBlockMaterial(this.getTypeId(x2, y2, z2));
    }

    @Override
    public MaterialData getTypeAndData(int x2, int y2, int z2) {
        return this.getType(x2, y2, z2).getNewData(this.getData(x2, y2, z2));
    }

    @Override
    public void setRegion(int xMin, int yMin, int zMin, int xMax, int yMax, int zMax, int blockId) {
        this.setRegion(xMin, yMin, zMin, xMax, yMax, zMax, blockId, 0);
    }

    @Override
    public void setRegion(int xMin, int yMin, int zMin, int xMax, int yMax, int zMax, int blockId, int data) {
        if (xMin > 15 || yMin >= this.maxHeight || zMin > 15) {
            return;
        }
        if (xMin < 0) {
            xMin = 0;
        }
        if (yMin < 0) {
            yMin = 0;
        }
        if (zMin < 0) {
            zMin = 0;
        }
        if (xMax > 16) {
            xMax = 16;
        }
        if (yMax > this.maxHeight) {
            yMax = this.maxHeight;
        }
        if (zMax > 16) {
            zMax = 16;
        }
        if (xMin >= xMax || yMin >= yMax || zMin >= zMax) {
            return;
        }
        char typeChar = (char)(blockId << 4 | data);
        if (xMin == 0 && xMax == 16) {
            if (zMin == 0 && zMax == 16) {
                for (int y2 = yMin & 240; y2 < yMax; y2 += 16) {
                    char[] section = this.getChunkSection(y2, true);
                    if (y2 <= yMin) {
                        if (y2 + 16 > yMax) {
                            Arrays.fill(section, (yMin & 15) << 8, (yMax & 15) << 8, typeChar);
                            continue;
                        }
                        Arrays.fill(section, (yMin & 15) << 8, 4096, typeChar);
                        continue;
                    }
                    if (y2 + 16 > yMax) {
                        Arrays.fill(section, 0, (yMax & 15) << 8, typeChar);
                        continue;
                    }
                    Arrays.fill(section, 0, 4096, typeChar);
                }
            } else {
                for (int y3 = yMin; y3 < yMax; ++y3) {
                    char[] section = this.getChunkSection(y3, true);
                    int offsetBase = (y3 & 15) << 8;
                    int min = offsetBase | zMin << 4;
                    int max = offsetBase + (zMax << 4);
                    Arrays.fill(section, min, max, typeChar);
                }
            }
        } else {
            for (int y4 = yMin; y4 < yMax; ++y4) {
                char[] section = this.getChunkSection(y4, true);
                int offsetBase = (y4 & 15) << 8;
                for (int z2 = zMin; z2 < zMax; ++z2) {
                    int offset = offsetBase | z2 << 4;
                    Arrays.fill(section, offset | xMin, offset + xMax, typeChar);
                }
            }
        }
    }

    @Override
    public void setBlock(int x2, int y2, int z2, int blockId) {
        this.setBlock(x2, y2, z2, blockId, 0);
    }

    @Override
    public void setBlock(int x2, int y2, int z2, int blockId, byte data) {
        this.setBlock(x2, y2, z2, (char)(blockId << 4 | data));
    }

    @Override
    public int getTypeId(int x2, int y2, int z2) {
        if (x2 != (x2 & 15) || y2 < 0 || y2 >= this.maxHeight || z2 != (z2 & 15)) {
            return 0;
        }
        char[] section = this.getChunkSection(y2, false);
        if (section == null) {
            return 0;
        }
        return section[(y2 & 15) << 8 | z2 << 4 | x2] >> 4;
    }

    @Override
    public byte getData(int x2, int y2, int z2) {
        if (x2 != (x2 & 15) || y2 < 0 || y2 >= this.maxHeight || z2 != (z2 & 15)) {
            return 0;
        }
        char[] section = this.getChunkSection(y2, false);
        if (section == null) {
            return 0;
        }
        return (byte)(section[(y2 & 15) << 8 | z2 << 4 | x2] & 15);
    }

    private void setBlock(int x2, int y2, int z2, char type) {
        if (x2 != (x2 & 15) || y2 < 0 || y2 >= this.maxHeight || z2 != (z2 & 15)) {
            return;
        }
        char[] section = this.getChunkSection(y2, true);
        section[(y2 & 15) << 8 | z2 << 4 | x2] = type;
    }

    private char[] getChunkSection(int y2, boolean create) {
        char[] section = this.sections[y2 >> 4];
        if (create && section == null) {
            section = new char[4096];
            this.sections[y2 >> 4] = section;
        }
        return section;
    }

    char[][] getRawChunkData() {
        return this.sections;
    }
}

