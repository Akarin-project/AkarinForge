package net.minecraft.util.math;

import com.google.common.collect.AbstractIterator;
import com.google.common.collect.Lists;
import java.util.Iterator;
import java.util.List;
import javax.annotation.concurrent.Immutable;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Immutable
public class BlockPos extends Vec3i
{
    private static final Logger LOGGER = LogManager.getLogger();
    public static final BlockPos ORIGIN = new BlockPos(0, 0, 0);
    private static final int NUM_X_BITS = 1 + MathHelper.log2(MathHelper.smallestEncompassingPowerOfTwo(30000000));
    private static final int NUM_Z_BITS = NUM_X_BITS;
    private static final int NUM_Y_BITS = 64 - NUM_X_BITS - NUM_Z_BITS;
    private static final int Y_SHIFT = 0 + NUM_Z_BITS;
    private static final int X_SHIFT = Y_SHIFT + NUM_Y_BITS;
    private static final long X_MASK = (1L << NUM_X_BITS) - 1L;
    private static final long Y_MASK = (1L << NUM_Y_BITS) - 1L;
    private static final long Z_MASK = (1L << NUM_Z_BITS) - 1L;

    public BlockPos(int x, int y, int z)
    {
        super(x, y, z);
    }

    public BlockPos(double x, double y, double z)
    {
        super(x, y, z);
    }

    public BlockPos(Entity source)
    {
        this(source.posX, source.posY, source.posZ);
    }

    public BlockPos(Vec3d vec)
    {
        this(vec.x, vec.y, vec.z);
    }

    public BlockPos(Vec3i source)
    {
        this(source.getX(), source.getY(), source.getZ());
    }

    public BlockPos add(double x, double y, double z)
    {
        return x == 0.0D && y == 0.0D && z == 0.0D ? this : new BlockPos((double)this.getX() + x, (double)this.getY() + y, (double)this.getZ() + z);
    }

    public BlockPos add(int x, int y, int z)
    {
        return x == 0 && y == 0 && z == 0 ? this : new BlockPos(this.getX() + x, this.getY() + y, this.getZ() + z);
    }

    public BlockPos add(Vec3i vec)
    {
        return this.add(vec.getX(), vec.getY(), vec.getZ());
    }

    public BlockPos subtract(Vec3i vec)
    {
        return this.add(-vec.getX(), -vec.getY(), -vec.getZ());
    }

    public BlockPos up()
    {
        return this.up(1);
    }

    public BlockPos up(int n)
    {
        return this.offset(EnumFacing.UP, n);
    }

    public BlockPos down()
    {
        return this.down(1);
    }

    public BlockPos down(int n)
    {
        return this.offset(EnumFacing.DOWN, n);
    }

    public BlockPos north()
    {
        return this.north(1);
    }

    public BlockPos north(int n)
    {
        return this.offset(EnumFacing.NORTH, n);
    }

    public BlockPos south()
    {
        return this.south(1);
    }

    public BlockPos south(int n)
    {
        return this.offset(EnumFacing.SOUTH, n);
    }

    public BlockPos west()
    {
        return this.west(1);
    }

    public BlockPos west(int n)
    {
        return this.offset(EnumFacing.WEST, n);
    }

    public BlockPos east()
    {
        return this.east(1);
    }

    public BlockPos east(int n)
    {
        return this.offset(EnumFacing.EAST, n);
    }

    public BlockPos offset(EnumFacing facing)
    {
        return this.offset(facing, 1);
    }

    public BlockPos offset(EnumFacing facing, int n)
    {
        return n == 0 ? this : new BlockPos(this.getX() + facing.getFrontOffsetX() * n, this.getY() + facing.getFrontOffsetY() * n, this.getZ() + facing.getFrontOffsetZ() * n);
    }

    public BlockPos rotate(Rotation rotationIn)
    {
        switch (rotationIn)
        {
            case NONE:
            default:
                return this;
            case CLOCKWISE_90:
                return new BlockPos(-this.getZ(), this.getY(), this.getX());
            case CLOCKWISE_180:
                return new BlockPos(-this.getX(), this.getY(), -this.getZ());
            case COUNTERCLOCKWISE_90:
                return new BlockPos(this.getZ(), this.getY(), -this.getX());
        }
    }

    public BlockPos crossProduct(Vec3i vec)
    {
        return new BlockPos(this.getY() * vec.getZ() - this.getZ() * vec.getY(), this.getZ() * vec.getX() - this.getX() * vec.getZ(), this.getX() * vec.getY() - this.getY() * vec.getX());
    }

    public long toLong()
    {
        return ((long)this.getX() & X_MASK) << X_SHIFT | ((long)this.getY() & Y_MASK) << Y_SHIFT | ((long)this.getZ() & Z_MASK) << 0;
    }

    public static BlockPos fromLong(long serialized)
    {
        int i = (int)(serialized << 64 - X_SHIFT - NUM_X_BITS >> 64 - NUM_X_BITS);
        int j = (int)(serialized << 64 - Y_SHIFT - NUM_Y_BITS >> 64 - NUM_Y_BITS);
        int k = (int)(serialized << 64 - NUM_Z_BITS >> 64 - NUM_Z_BITS);
        return new BlockPos(i, j, k);
    }

    public static Iterable<BlockPos> getAllInBox(BlockPos from, BlockPos to)
    {
        return getAllInBox(Math.min(from.getX(), to.getX()), Math.min(from.getY(), to.getY()), Math.min(from.getZ(), to.getZ()), Math.max(from.getX(), to.getX()), Math.max(from.getY(), to.getY()), Math.max(from.getZ(), to.getZ()));
    }

    public static Iterable<BlockPos> getAllInBox(final int x1, final int y1, final int z1, final int x2, final int y2, final int z2)
    {
        return new Iterable<BlockPos>()
        {
            public Iterator<BlockPos> iterator()
            {
                return new AbstractIterator<BlockPos>()
                {
                    private boolean first = true;
                    private int lastPosX;
                    private int lastPosY;
                    private int lastPosZ;
                    protected BlockPos computeNext()
                    {
                        if (this.first)
                        {
                            this.first = false;
                            this.lastPosX = x1;
                            this.lastPosY = y1;
                            this.lastPosZ = z1;
                            return new BlockPos(x1, y1, z1);
                        }
                        else if (this.lastPosX == x2 && this.lastPosY == y2 && this.lastPosZ == z2)
                        {
                            return (BlockPos)this.endOfData();
                        }
                        else
                        {
                            if (this.lastPosX < x2)
                            {
                                ++this.lastPosX;
                            }
                            else if (this.lastPosY < y2)
                            {
                                this.lastPosX = x1;
                                ++this.lastPosY;
                            }
                            else if (this.lastPosZ < z2)
                            {
                                this.lastPosX = x1;
                                this.lastPosY = y1;
                                ++this.lastPosZ;
                            }

                            return new BlockPos(this.lastPosX, this.lastPosY, this.lastPosZ);
                        }
                    }
                };
            }
        };
    }

    public BlockPos toImmutable()
    {
        return this;
    }

    public static Iterable<BlockPos.MutableBlockPos> getAllInBoxMutable(BlockPos from, BlockPos to)
    {
        return getAllInBoxMutable(Math.min(from.getX(), to.getX()), Math.min(from.getY(), to.getY()), Math.min(from.getZ(), to.getZ()), Math.max(from.getX(), to.getX()), Math.max(from.getY(), to.getY()), Math.max(from.getZ(), to.getZ()));
    }

    public static Iterable<BlockPos.MutableBlockPos> getAllInBoxMutable(final int x1, final int y1, final int z1, final int x2, final int y2, final int z2)
    {
        return new Iterable<BlockPos.MutableBlockPos>()
        {
            public Iterator<BlockPos.MutableBlockPos> iterator()
            {
                return new AbstractIterator<BlockPos.MutableBlockPos>()
                {
                    private BlockPos.MutableBlockPos pos;
                    protected BlockPos.MutableBlockPos computeNext()
                    {
                        if (this.pos == null)
                        {
                            this.pos = new BlockPos.MutableBlockPos(x1, y1, z1);
                            return this.pos;
                        }
                        else if (this.pos.x == x2 && this.pos.y == y2 && this.pos.z == z2)
                        {
                            return (BlockPos.MutableBlockPos)this.endOfData();
                        }
                        else
                        {
                            if (this.pos.x < x2)
                            {
                                ++this.pos.x;
                            }
                            else if (this.pos.y < y2)
                            {
                                this.pos.x = x1;
                                ++this.pos.y;
                            }
                            else if (this.pos.z < z2)
                            {
                                this.pos.x = x1;
                                this.pos.y = y1;
                                ++this.pos.z;
                            }

                            return this.pos;
                        }
                    }
                };
            }
        };
    }

    public static class MutableBlockPos extends BlockPos
        {
            protected int x;
            protected int y;
            protected int z;

            public MutableBlockPos()
            {
                this(0, 0, 0);
            }

            public MutableBlockPos(BlockPos pos)
            {
                this(pos.getX(), pos.getY(), pos.getZ());
            }

            public MutableBlockPos(int x_, int y_, int z_)
            {
                super(0, 0, 0);
                this.x = x_;
                this.y = y_;
                this.z = z_;
            }

            public BlockPos add(double x, double y, double z)
            {
                return super.add(x, y, z).toImmutable();
            }

            public BlockPos add(int x, int y, int z)
            {
                return super.add(x, y, z).toImmutable();
            }

            public BlockPos offset(EnumFacing facing, int n)
            {
                return super.offset(facing, n).toImmutable();
            }

            public BlockPos rotate(Rotation rotationIn)
            {
                return super.rotate(rotationIn).toImmutable();
            }

            public int getX()
            {
                return this.x;
            }

            public int getY()
            {
                return this.y;
            }

            public int getZ()
            {
                return this.z;
            }

            public BlockPos.MutableBlockPos setPos(int xIn, int yIn, int zIn)
            {
                this.x = xIn;
                this.y = yIn;
                this.z = zIn;
                return this;
            }

            public BlockPos.MutableBlockPos setPos(double xIn, double yIn, double zIn)
            {
                return this.setPos(MathHelper.floor(xIn), MathHelper.floor(yIn), MathHelper.floor(zIn));
            }

            @SideOnly(Side.CLIENT)
            public BlockPos.MutableBlockPos setPos(Entity entityIn)
            {
                return this.setPos(entityIn.posX, entityIn.posY, entityIn.posZ);
            }

            public BlockPos.MutableBlockPos setPos(Vec3i vec)
            {
                return this.setPos(vec.getX(), vec.getY(), vec.getZ());
            }

            public BlockPos.MutableBlockPos move(EnumFacing facing)
            {
                return this.move(facing, 1);
            }

            public BlockPos.MutableBlockPos move(EnumFacing facing, int n)
            {
                return this.setPos(this.x + facing.getFrontOffsetX() * n, this.y + facing.getFrontOffsetY() * n, this.z + facing.getFrontOffsetZ() * n);
            }

            public void setY(int yIn)
            {
                this.y = yIn;
            }

            public BlockPos toImmutable()
            {
                return new BlockPos(this);
            }
        }

    public static final class PooledMutableBlockPos extends BlockPos.MutableBlockPos
        {
            private boolean released;
            private static final List<BlockPos.PooledMutableBlockPos> POOL = Lists.<BlockPos.PooledMutableBlockPos>newArrayList();

            private PooledMutableBlockPos(int xIn, int yIn, int zIn)
            {
                super(xIn, yIn, zIn);
            }

            public static BlockPos.PooledMutableBlockPos retain()
            {
                return retain(0, 0, 0);
            }

            public static BlockPos.PooledMutableBlockPos retain(double xIn, double yIn, double zIn)
            {
                return retain(MathHelper.floor(xIn), MathHelper.floor(yIn), MathHelper.floor(zIn));
            }

            @SideOnly(Side.CLIENT)
            public static BlockPos.PooledMutableBlockPos retain(Vec3i vec)
            {
                return retain(vec.getX(), vec.getY(), vec.getZ());
            }

            public static BlockPos.PooledMutableBlockPos retain(int xIn, int yIn, int zIn)
            {
                synchronized (POOL)
                {
                    if (!POOL.isEmpty())
                    {
                        BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = POOL.remove(POOL.size() - 1);

                        if (blockpos$pooledmutableblockpos != null && blockpos$pooledmutableblockpos.released)
                        {
                            blockpos$pooledmutableblockpos.released = false;
                            blockpos$pooledmutableblockpos.setPos(xIn, yIn, zIn);
                            return blockpos$pooledmutableblockpos;
                        }
                    }
                }

                return new BlockPos.PooledMutableBlockPos(xIn, yIn, zIn);
            }

            public void release()
            {
                synchronized (POOL)
                {
                    if (POOL.size() < 100)
                    {
                        POOL.add(this);
                    }

                    this.released = true;
                }
            }

            public BlockPos.PooledMutableBlockPos setPos(int xIn, int yIn, int zIn)
            {
                if (this.released)
                {
                    BlockPos.LOGGER.error("PooledMutableBlockPosition modified after it was released.", new Throwable());
                    this.released = false;
                }

                return (BlockPos.PooledMutableBlockPos)super.setPos(xIn, yIn, zIn);
            }

            @SideOnly(Side.CLIENT)
            public BlockPos.PooledMutableBlockPos setPos(Entity entityIn)
            {
                return (BlockPos.PooledMutableBlockPos)super.setPos(entityIn);
            }

            public BlockPos.PooledMutableBlockPos setPos(double xIn, double yIn, double zIn)
            {
                return (BlockPos.PooledMutableBlockPos)super.setPos(xIn, yIn, zIn);
            }

            public BlockPos.PooledMutableBlockPos setPos(Vec3i vec)
            {
                return (BlockPos.PooledMutableBlockPos)super.setPos(vec);
            }

            public BlockPos.PooledMutableBlockPos move(EnumFacing facing)
            {
                return (BlockPos.PooledMutableBlockPos)super.move(facing);
            }

            public BlockPos.PooledMutableBlockPos move(EnumFacing facing, int n)
            {
                return (BlockPos.PooledMutableBlockPos)super.move(facing, n);
            }
        }
}