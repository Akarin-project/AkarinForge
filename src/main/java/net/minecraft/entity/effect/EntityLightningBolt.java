package net.minecraft.entity.effect;

import java.util.List;

import org.bukkit.craftbukkit.event.CraftEventFactory;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class EntityLightningBolt extends EntityWeatherEffect
{
    private int lightningState;
    public long boltVertex;
    private int boltLivingTime;
    private final boolean effectOnly;
    public boolean isEffect; // CraftBukkit
    public boolean isSilent = false; // Spigot
    // Spigot start
    public EntityLightningBolt(World world, double d0, double d1, double d2, boolean isEffect, boolean isSilent)
    {
        this( world, d0, d1, d2, isEffect );
        this.isSilent = isSilent;
    }
    // Spigot end

    public EntityLightningBolt(World worldIn, double x, double y, double z, boolean effectOnlyIn)
    {
        super(worldIn);
        this.isEffect = effectOnlyIn; // CraftBukkit
        this.setLocationAndAngles(x, y, z, 0.0F, 0.0F);
        this.lightningState = 2;
        this.boltVertex = this.rand.nextLong();
        this.boltLivingTime = this.rand.nextInt(3) + 1;
        this.effectOnly = effectOnlyIn;
        BlockPos blockpos = new BlockPos(this);

        if (!effectOnlyIn && !worldIn.isRemote && worldIn.getGameRules().getBoolean("doFireTick") && (worldIn.getDifficulty() == EnumDifficulty.NORMAL || worldIn.getDifficulty() == EnumDifficulty.HARD) && worldIn.isAreaLoaded(blockpos, 10))
        {
            if (worldIn.getBlockState(blockpos).getMaterial() == Material.AIR && Blocks.FIRE.canPlaceBlockAt(worldIn, blockpos))
            {
                // CraftBukkit start
                if (!CraftEventFactory.callBlockIgniteEvent(world, blockpos.getX(), blockpos.getY(), blockpos.getZ(), this).isCancelled()) {
                    world.setBlockState(blockpos, Blocks.FIRE.getDefaultState());
                }
                // CraftBukkit end
            }

            for (int i = 0; i < 4; ++i)
            {
                BlockPos blockpos1 = blockpos.add(this.rand.nextInt(3) - 1, this.rand.nextInt(3) - 1, this.rand.nextInt(3) - 1);

                if (worldIn.getBlockState(blockpos1).getMaterial() == Material.AIR && Blocks.FIRE.canPlaceBlockAt(worldIn, blockpos1))
                {
                    // CraftBukkit start
                    if (!CraftEventFactory.callBlockIgniteEvent(world, blockpos1.getX(), blockpos1.getY(), blockpos1.getZ(), this).isCancelled()) {
                        world.setBlockState(blockpos1, Blocks.FIRE.getDefaultState());
                    }
                    // CraftBukkit end
                }
            }
        }
    }

    public SoundCategory getSoundCategory()
    {
        return SoundCategory.WEATHER;
    }

    public void onUpdate()
    {
        super.onUpdate();

        if (!isSilent && this.lightningState == 2) // Spigot
        {
            // CraftBukkit start - Use relative location for far away sounds
            float pitch = 0.8F + this.rand.nextFloat() * 0.2F;
            int viewDistance = ((WorldServer) this.world).getServer().getViewDistance() * 16;
            for (EntityPlayerMP player : (List<EntityPlayerMP>) (List) this.world.playerEntities) {
                double deltaX = this.posX - player.posX;
                double deltaZ = this.posZ - player.posZ;
                double distanceSquared = deltaX * deltaX + deltaZ * deltaZ;
                if (distanceSquared > viewDistance * viewDistance) {
                    double deltaLength = Math.sqrt(distanceSquared);
                    double relativeX = player.posX + (deltaX / deltaLength) * viewDistance;
                    double relativeZ = player.posZ + (deltaZ / deltaLength) * viewDistance;
                    player.connection.sendPacket(new SPacketSoundEffect(SoundEvents.ENTITY_LIGHTNING_THUNDER, SoundCategory.WEATHER, relativeX, this.posY, relativeZ, 10000.0F, pitch));
                } else {
                    player.connection.sendPacket(new SPacketSoundEffect(SoundEvents.ENTITY_LIGHTNING_THUNDER, SoundCategory.WEATHER, this.posX, this.posY, this.posZ, 10000.0F, pitch));
                }
            }
            // CraftBukkit end
            this.world.playSound((EntityPlayer)null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_LIGHTNING_THUNDER, SoundCategory.WEATHER, 10000.0F, 0.8F + this.rand.nextFloat() * 0.2F);
            this.world.playSound((EntityPlayer)null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_LIGHTNING_IMPACT, SoundCategory.WEATHER, 2.0F, 0.5F + this.rand.nextFloat() * 0.2F);
        }

        --this.lightningState;

        if (this.lightningState < 0)
        {
            if (this.boltLivingTime == 0)
            {
                this.setDead();
            }
            else if (this.lightningState < -this.rand.nextInt(10))
            {
                --this.boltLivingTime;
                this.lightningState = 1;

                if (!this.effectOnly && !this.world.isRemote)
                {
                    this.boltVertex = this.rand.nextLong();
                    BlockPos blockpos = new BlockPos(this);

                    if (this.world.getGameRules().getBoolean("doFireTick") && this.world.isAreaLoaded(blockpos, 10) && this.world.getBlockState(blockpos).getMaterial() == Material.AIR && Blocks.FIRE.canPlaceBlockAt(this.world, blockpos))
                    {
                        // CraftBukkit start - add "!isEffect"
                        if (!isEffect && !CraftEventFactory.callBlockIgniteEvent(world, blockpos.getX(), blockpos.getY(), blockpos.getZ(), this).isCancelled()) {
                            this.world.setBlockState(blockpos, Blocks.FIRE.getDefaultState());
                        }
                        // CraftBukkit end
                    }
                }
            }
        }

        if (this.lightningState >= 0 && !this.isEffect) // CraftBukkit - add !this.isEffect
        {
            if (this.world.isRemote)
            {
                this.world.setLastLightningBolt(2);
            }
            else if (!this.effectOnly)
            {
                double d0 = 3.0D;
                List<Entity> list = this.world.getEntitiesWithinAABBExcludingEntity(this, new AxisAlignedBB(this.posX - 3.0D, this.posY - 3.0D, this.posZ - 3.0D, this.posX + 3.0D, this.posY + 6.0D + 3.0D, this.posZ + 3.0D));

                for (int i = 0; i < list.size(); ++i)
                {
                    Entity entity = list.get(i);
                    if (!net.minecraftforge.event.ForgeEventFactory.onEntityStruckByLightning(entity, this))
                        entity.onStruckByLightning(this);
                }
            }
        }
    }

    protected void entityInit()
    {
    }

    protected void readEntityFromNBT(NBTTagCompound compound)
    {
    }

    protected void writeEntityToNBT(NBTTagCompound compound)
    {
    }
}