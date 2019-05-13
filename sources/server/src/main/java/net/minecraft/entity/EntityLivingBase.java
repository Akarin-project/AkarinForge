package net.minecraft.entity;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentFrostWalker;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.AttributeMap;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SPacketAnimation;
import net.minecraft.network.play.server.SPacketCollectItem;
import net.minecraft.network.play.server.SPacketEntityEquipment;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stats.StatList;
import net.minecraft.util.CombatRules;
import net.minecraft.util.CombatTracker;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.craftbukkit.v1_12_R1.attribute.CraftAttributeMap;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.event.entity.EntityRegainHealthEvent;

public abstract class EntityLivingBase extends Entity
{
    private static final Logger LOGGER = LogManager.getLogger();
    private static final UUID SPRINTING_SPEED_BOOST_ID = UUID.fromString("662A6B8D-DA3E-4C1C-8813-96EA6097278D");
    private static final AttributeModifier SPRINTING_SPEED_BOOST = (new AttributeModifier(SPRINTING_SPEED_BOOST_ID, "Sprinting speed boost", 0.30000001192092896D, 2)).setSaved(false);
    public static final net.minecraft.entity.ai.attributes.IAttribute SWIM_SPEED = new net.minecraft.entity.ai.attributes.RangedAttribute(null, "forge.swimSpeed", 1.0D, 0.0D, 1024.0D).setShouldWatch(true);
    protected static final DataParameter<Byte> HAND_STATES = EntityDataManager.<Byte>createKey(EntityLivingBase.class, DataSerializers.BYTE);
    public static final DataParameter<Float> HEALTH = EntityDataManager.<Float>createKey(EntityLivingBase.class, DataSerializers.FLOAT);
    private static final DataParameter<Integer> POTION_EFFECTS = EntityDataManager.<Integer>createKey(EntityLivingBase.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> HIDE_PARTICLES = EntityDataManager.<Boolean>createKey(EntityLivingBase.class, DataSerializers.BOOLEAN);
    private static final DataParameter<Integer> ARROW_COUNT_IN_ENTITY = EntityDataManager.<Integer>createKey(EntityLivingBase.class, DataSerializers.VARINT);
    private AbstractAttributeMap attributeMap;
    protected CombatTracker _combatTracker = new CombatTracker(this); // Akarin
    public final Map<Potion, PotionEffect> activePotionsMap = Maps.<Potion, PotionEffect>newHashMap();
    private final NonNullList<ItemStack> handInventory = NonNullList.<ItemStack>withSize(2, ItemStack.EMPTY);
    private final NonNullList<ItemStack> armorArray = NonNullList.<ItemStack>withSize(4, ItemStack.EMPTY);
    public boolean isSwingInProgress;
    public EnumHand swingingHand;
    public int swingProgressInt;
    public int arrowHitTimer;
    public int hurtTime;
    public int maxHurtTime;
    public float attackedAtYaw;
    public int deathTime;
    public float prevSwingProgress;
    public float swingProgress;
    protected int ticksSinceLastSwing;
    public float prevLimbSwingAmount;
    public float limbSwingAmount;
    public float limbSwing;
    public int maxHurtResistantTime = 20;
    public float prevCameraPitch;
    public float cameraPitch;
    public float randomUnused2;
    public float randomUnused1;
    public float renderYawOffset;
    public float prevRenderYawOffset;
    public float rotationYawHead;
    public float prevRotationYawHead;
    public float jumpMovementFactor = 0.02F;
    public EntityPlayer attackingPlayer;
    protected int recentlyHit;
    protected boolean dead;
    protected int idleTime;
    protected float prevOnGroundSpeedFactor;
    protected float onGroundSpeedFactor;
    protected float movedDistance;
    protected float prevMovedDistance;
    protected float unused180;
    protected int scoreValue;
    public float lastDamage;
    protected boolean isJumping;
    public float moveStrafing;
    public float moveVertical;
    public float moveForward;
    public float randomYawVelocity;
    protected int newPosRotationIncrements;
    protected double interpTargetX;
    protected double interpTargetY;
    protected double interpTargetZ;
    protected double interpTargetYaw;
    protected double interpTargetPitch;
    protected boolean potionsNeedUpdate = true; // Akarin - protected
    protected EntityLivingBase revengeTarget; // Akarin
    private int revengeTimer;
    private EntityLivingBase lastAttackedEntity;
    private int lastAttackedEntityTime;
    private float landMovementFactor;
    private int jumpTicks;
    private float absorptionAmount;
    protected ItemStack activeItemStack = ItemStack.EMPTY;
    protected int activeItemStackUseCount;
    protected int ticksElytraFlying;
    private BlockPos prevBlockpos;
    private DamageSource lastDamageSource;
    private long lastDamageStamp;
    // CraftBukkit start
    public int expToDrop;
    public int maxAirTicks = 300;
    boolean forceDrops;
    protected ArrayList<org.bukkit.inventory.ItemStack> drops = new ArrayList<org.bukkit.inventory.ItemStack>();
    public CraftAttributeMap craftAttributes;
    public boolean collides = true;
    public boolean canPickUpLoot;
    
    private boolean isTickingEffects = false;
    private List<Object> effectsToProcess = Lists.newArrayList();

    @Override
    public float getBukkitYaw() {
        return getRotationYawHead();
    }
    
    public int getExpReward() {
        int exp = this.getExperiencePoints(this.attackingPlayer);

        if (!this.world.isRemote && (this.recentlyHit > 0 || this.isPlayer()) && this.canDropLoot() && this.world.getGameRules().getBoolean("doMobLoot")) {
            return exp;
        } else {
            return 0;
        }
    }
    // CraftBukkit end

    public void onKillCommand()
    {
        this.attackEntityFrom(DamageSource.OUT_OF_WORLD, Float.MAX_VALUE);
    }

    public EntityLivingBase(World worldIn)
    {
        super(worldIn);
        this.applyEntityAttributes();
        // CraftBukkit - setHealth(getMaxHealth()) inlined and simplified to skip the instanceof check for EntityPlayer, as getBukkitEntity() is not initialized in constructor
        this.preventEntitySpawning = true;
        this.randomUnused1 = (float)((Math.random() + 1.0D) * 0.009999999776482582D);
        this.setPosition(this.posX, this.posY, this.posZ);
        this.randomUnused2 = (float)Math.random() * 12398.0F;
        this.rotationYaw = (float)(Math.random() * (Math.PI * 2D));
        this.rotationYawHead = this.rotationYaw;
        this.stepHeight = 0.6F;
    }

    protected void entityInit()
    {
        this.dataManager.register(HAND_STATES, Byte.valueOf((byte)0));
        this.dataManager.register(POTION_EFFECTS, Integer.valueOf(0));
        this.dataManager.register(HIDE_PARTICLES, Boolean.valueOf(false));
        this.dataManager.register(ARROW_COUNT_IN_ENTITY, Integer.valueOf(0));
        this.dataManager.register(HEALTH, Float.valueOf(1.0F));
    }

    protected void applyEntityAttributes()
    {
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.MAX_HEALTH);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ARMOR);
        this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS);
        this.getAttributeMap().registerAttribute(SWIM_SPEED);
    }

    protected void updateFallState(double y, boolean onGroundIn, IBlockState state, BlockPos pos)
    {
        if (!this.isInWater())
        {
            this.handleWaterMovement();
        }

        if (!this.world.isRemote && this.fallDistance > 3.0F && onGroundIn)
        {
            float f = (float)MathHelper.ceil(this.fallDistance - 3.0F);

            if (!state.getBlock().isAir(state, world, pos))
            {
                double d0 = Math.min((double)(0.2F + f / 15.0F), 2.5D);
                int i = (int)(150.0D * d0);
                // CraftBukkit start - visiblity api
                if (this instanceof EntityPlayerMP && !state.getBlock().addLandingEffects(state, (WorldServer)this.world, pos, state, this, i)) {
                    ((WorldServer) this.world).sendParticles((EntityPlayerMP) this, EnumParticleTypes.BLOCK_DUST, false, this.posX, this.posY, this.posZ, i, 0.0D, 0.0D, 0.0D, 0.15000000596046448D, new int[] { Block.getStateId(state)});
                } else {
                    ((WorldServer) this.world).spawnParticle(EnumParticleTypes.BLOCK_DUST, this.posX, this.posY, this.posZ, i, 0.0D, 0.0D, 0.0D, 0.15000000596046448D, new int[] { Block.getStateId(state)});
                }
                // CraftBukkit end
            }
        }

        super.updateFallState(y, onGroundIn, state, pos);
    }

    public boolean canBreatheUnderwater()
    {
        return false;
    }

    public void onEntityUpdate()
    {
        this.prevSwingProgress = this.swingProgress;
        super.onEntityUpdate();
        this.world.profiler.startSection("livingEntityBaseTick");
        boolean flag = this instanceof EntityPlayer;

        if (this.isEntityAlive())
        {
            if (this.isEntityInsideOpaqueBlock())
            {
                this.attackEntityFrom(DamageSource.IN_WALL, 1.0F);
            }
            else if (flag && !this.world.getWorldBorder().contains(this.getEntityBoundingBox()))
            {
                double d0 = this.world.getWorldBorder().getClosestDistance(this) + this.world.getWorldBorder().getDamageBuffer();

                if (d0 < 0.0D)
                {
                    double d1 = this.world.getWorldBorder().getDamageAmount();

                    if (d1 > 0.0D)
                    {
                        this.attackEntityFrom(DamageSource.IN_WALL, (float)Math.max(1, MathHelper.floor(-d0 * d1)));
                    }
                }
            }
        }

        if (this.isImmuneToFire() || this.world.isRemote)
        {
            this.extinguish();
        }

        boolean flag1 = flag && ((EntityPlayer)this).capabilities.disableDamage;

        if (this.isEntityAlive())
        {
            if (!this.isInsideOfMaterial(Material.WATER))
            {
                // CraftBukkit start - Only set if needed to work around a DataWatcher inefficiency
                if (this.getAir() != 300) {
                    this.setAir(maxAirTicks);
                }
                // CraftBukkit end
            }
            else
            {
                if (!this.canBreatheUnderwater() && !this.isPotionActive(MobEffects.WATER_BREATHING) && !flag1)
                {
                    this.setAir(this.decreaseAirSupply(this.getAir()));

                    if (this.getAir() == -20)
                    {
                        this.setAir(0);

                        for (int i = 0; i < 8; ++i)
                        {
                            float f2 = this.rand.nextFloat() - this.rand.nextFloat();
                            float f = this.rand.nextFloat() - this.rand.nextFloat();
                            float f1 = this.rand.nextFloat() - this.rand.nextFloat();
                            this.world.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX + (double)f2, this.posY + (double)f, this.posZ + (double)f1, this.motionX, this.motionY, this.motionZ);
                        }

                        this.attackEntityFrom(DamageSource.DROWN, 2.0F);
                    }
                }

                if (!this.world.isRemote && this.isRiding() && this.getRidingEntity() != null && this.getRidingEntity().shouldDismountInWater(this))
                {
                    this.dismountRidingEntity();
                }
            }

            if (!this.world.isRemote)
            {
                BlockPos blockpos = new BlockPos(this);

                if (!Objects.equal(this.prevBlockpos, blockpos))
                {
                    this.prevBlockpos = blockpos;
                    this.frostWalk(blockpos);
                }
            }
        }

        if (this.isEntityAlive() && this.isWet())
        {
            this.extinguish();
        }

        this.prevCameraPitch = this.cameraPitch;

        if (this.hurtTime > 0)
        {
            --this.hurtTime;
        }

        if (this.hurtResistantTime > 0 && !(this instanceof EntityPlayerMP))
        {
            --this.hurtResistantTime;
        }

        if (this.getHealth() <= 0.0F)
        {
            this.onDeathUpdate();
        }

        if (this.recentlyHit > 0)
        {
            --this.recentlyHit;
        }
        else
        {
            this.attackingPlayer = null;
        }

        if (this.lastAttackedEntity != null && !this.lastAttackedEntity.isEntityAlive())
        {
            this.lastAttackedEntity = null;
        }

        if (this.revengeTarget != null)
        {
            if (!this.revengeTarget.isEntityAlive())
            {
                this.setRevengeTarget((EntityLivingBase)null);
            }
            else if (this.ticksExisted - this.revengeTimer > 100)
            {
                this.setRevengeTarget((EntityLivingBase)null);
            }
        }

        this.updatePotionEffects();
        this.prevMovedDistance = this.movedDistance;
        this.prevRenderYawOffset = this.renderYawOffset;
        this.prevRotationYawHead = this.rotationYawHead;
        this.prevRotationYaw = this.rotationYaw;
        this.prevRotationPitch = this.rotationPitch;
        this.world.profiler.endSection();
    }

    protected void frostWalk(BlockPos pos)
    {
        int i = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.FROST_WALKER, this);

        if (i > 0)
        {
            EnchantmentFrostWalker.freezeNearby(this, this.world, pos, i);
        }
    }

    public boolean isChild()
    {
        return false;
    }

    protected void onDeathUpdate()
    {
        ++this.deathTime;

        if (this.deathTime >= 20 && !this.isDead) // CraftBukkit - (this.deathTicks == 20) -> (this.deathTicks >= 20 && !this.dead)
        {
        	// Akarin start
            int i = this.expToDrop;
            while (i > 0) {
                int j = EntityXPOrb.getXPSplit(i);
                i -= j;
                this.world.spawnEntity(new EntityXPOrb(this.world, this.posX, this.posY, this.posZ, j));
            }
            
            this.expToDrop = 0;
            /* // Akarin end
            if (!this.world.isRemote && (this.isPlayer() || this.recentlyHit > 0 && this.canDropLoot() && this.world.getGameRules().getBoolean("doMobLoot")))
            {
                int i = this.getExperiencePoints(this.attackingPlayer);
                i = net.minecraftforge.event.ForgeEventFactory.getExperienceDrop(this, this.attackingPlayer, i);
                while (i > 0)
                {
                    int j = EntityXPOrb.getXPSplit(i);
                    i -= j;
                    this.world.spawnEntity(new EntityXPOrb(this.world, this.posX, this.posY, this.posZ, j));
                }
            }
            */ // Akarin end

            this.setDead();

            for (int k = 0; k < 20; ++k)
            {
                double d2 = this.rand.nextGaussian() * 0.02D;
                double d0 = this.rand.nextGaussian() * 0.02D;
                double d1 = this.rand.nextGaussian() * 0.02D;
                this.world.spawnParticle(EnumParticleTypes.EXPLOSION_NORMAL, this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, this.posY + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, d2, d0, d1);
            }
        }
    }

    protected boolean canDropLoot()
    {
        return !this.isChild();
    }

    protected int decreaseAirSupply(int air)
    {
        int i = EnchantmentHelper.getRespirationModifier(this);
        return i > 0 && this.rand.nextInt(i + 1) > 0 ? air : air - 1;
    }

    protected int getExperiencePoints(EntityPlayer player)
    {
        return 0;
    }

    protected boolean isPlayer()
    {
        return false;
    }

    public Random getRNG()
    {
        return this.rand;
    }

    @Nullable
    public EntityLivingBase getRevengeTarget()
    {
        return this.revengeTarget;
    }

    public int getRevengeTimer()
    {
        return this.revengeTimer;
    }

    public void setRevengeTarget(@Nullable EntityLivingBase livingBase)
    {
        this.revengeTarget = livingBase;
        this.revengeTimer = this.ticksExisted;
    }

    public EntityLivingBase getLastAttackedEntity()
    {
        return this.lastAttackedEntity;
    }

    public int getLastAttackedEntityTime()
    {
        return this.lastAttackedEntityTime;
    }

    public void setLastAttackedEntity(Entity entityIn)
    {
        if (entityIn instanceof EntityLivingBase)
        {
            this.lastAttackedEntity = (EntityLivingBase)entityIn;
        }
        else
        {
            this.lastAttackedEntity = null;
        }

        this.lastAttackedEntityTime = this.ticksExisted;
    }

    public int getIdleTime()
    {
        return this.idleTime;
    }

    protected void playEquipSound(ItemStack stack)
    {
        if (!stack.isEmpty())
        {
            SoundEvent soundevent = SoundEvents.ITEM_ARMOR_EQUIP_GENERIC;
            Item item = stack.getItem();

            if (item instanceof ItemArmor)
            {
                soundevent = ((ItemArmor)item).getArmorMaterial().getSoundEvent();
            }
            else if (item == Items.ELYTRA)
            {
                soundevent = SoundEvents.ITEM_ARMOR_EQIIP_ELYTRA;
            }

            this.playSound(soundevent, 1.0F, 1.0F);
        }
    }

    public void writeEntityToNBT(NBTTagCompound compound)
    {
        compound.setFloat("Health", this.getHealth());
        compound.setShort("HurtTime", (short)this.hurtTime);
        compound.setInteger("HurtByTimestamp", this.revengeTimer);
        compound.setShort("DeathTime", (short)this.deathTime);
        compound.setFloat("AbsorptionAmount", this.getAbsorptionAmount());

        for (EntityEquipmentSlot entityequipmentslot : EntityEquipmentSlot.values())
        {
            ItemStack itemstack = this.getItemStackFromSlot(entityequipmentslot);

            if (!itemstack.isEmpty())
            {
                this.getAttributeMap().removeAttributeModifiers(itemstack.getAttributeModifiers(entityequipmentslot));
            }
        }

        compound.setTag("Attributes", SharedMonsterAttributes.writeBaseAttributeMapToNBT(this.getAttributeMap()));

        for (EntityEquipmentSlot entityequipmentslot1 : EntityEquipmentSlot.values())
        {
            ItemStack itemstack1 = this.getItemStackFromSlot(entityequipmentslot1);

            if (!itemstack1.isEmpty())
            {
                this.getAttributeMap().applyAttributeModifiers(itemstack1.getAttributeModifiers(entityequipmentslot1));
            }
        }

        if (!this.activePotionsMap.isEmpty())
        {
            NBTTagList nbttaglist = new NBTTagList();

            for (PotionEffect potioneffect : this.activePotionsMap.values())
            {
                nbttaglist.appendTag(potioneffect.writeCustomPotionEffectToNBT(new NBTTagCompound()));
            }

            compound.setTag("ActiveEffects", nbttaglist);
        }

        compound.setBoolean("FallFlying", this.isElytraFlying());
    }

    public void readEntityFromNBT(NBTTagCompound compound)
    {
        this.setAbsorptionAmount(compound.getFloat("AbsorptionAmount"));

        if (compound.hasKey("Attributes", 9) && this.world != null && !this.world.isRemote)
        {
            SharedMonsterAttributes.setAttributeModifiers(this.getAttributeMap(), compound.getTagList("Attributes", 10));
        }

        if (compound.hasKey("ActiveEffects", 9))
        {
            NBTTagList nbttaglist = compound.getTagList("ActiveEffects", 10);

            for (int i = 0; i < nbttaglist.tagCount(); ++i)
            {
                NBTTagCompound nbttagcompound = nbttaglist.getCompoundTagAt(i);
                PotionEffect potioneffect = PotionEffect.readCustomPotionEffectFromNBT(nbttagcompound);

                if (potioneffect != null)
                {
                    this.activePotionsMap.put(potioneffect.getPotion(), potioneffect);
                }
            }
        }
        // CraftBukkit start
        if (compound.hasKey("Bukkit.MaxHealth")) {
            NBTBase nbtbase = compound.getTag("Bukkit.MaxHealth");
            if (nbtbase.getId() == 5) {
                this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(((NBTTagFloat) nbtbase).getDouble());
            } else if (nbtbase.getId() == 3) {
                this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(((NBTTagInt) nbtbase).getDouble());
            }
        }
        // CraftBukkit end

        if (compound.hasKey("Health", 99))
        {
            this.setHealth(compound.getFloat("Health"));
        }

        this.hurtTime = compound.getShort("HurtTime");
        this.deathTime = compound.getShort("DeathTime");
        this.revengeTimer = compound.getInteger("HurtByTimestamp");

        if (compound.hasKey("Team", 8))
        {
            String s = compound.getString("Team");
            boolean flag = this.world.getScoreboard().addPlayerToTeam(this.getCachedUniqueIdString(), s);

            if (!flag)
            {
                LOGGER.warn("Unable to add mob to team \"" + s + "\" (that team probably doesn't exist)");
            }
        }

        if (compound.getBoolean("FallFlying"))
        {
            this.setFlag(7, true);
        }
    }

    protected void updatePotionEffects()
    {
        Iterator<Potion> iterator = this.activePotionsMap.keySet().iterator();

        isTickingEffects = true; // CraftBukkit
        try
        {
            while (iterator.hasNext())
            {
                Potion potion = iterator.next();
                PotionEffect potioneffect = this.activePotionsMap.get(potion);

                if (!potioneffect.onUpdate(this))
                {
                    if (!this.world.isRemote && !net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.entity.living.PotionEvent.PotionExpiryEvent(this, potioneffect)))
                    {
                        iterator.remove();
                        this.onFinishedPotionEffect(potioneffect);
                    }
                }
                else if (potioneffect.getDuration() % 600 == 0)
                {
                    this.onChangedPotionEffect(potioneffect, false);
                }
            }
        }
        catch (ConcurrentModificationException var11)
        {
            ;
        }
        // CraftBukkit start
        isTickingEffects = false;
        for (Object e : effectsToProcess) {
            if (e instanceof PotionEffect) {
                addPotionEffect((PotionEffect) e);
            } else {
                removePotionEffect((Potion) e);
            }
        }
        effectsToProcess.clear();
        // CraftBukkit end

        if (this.potionsNeedUpdate)
        {
            if (!this.world.isRemote)
            {
                this.updatePotionMetadata();
            }

            this.potionsNeedUpdate = false;
        }

        int i = ((Integer)this.dataManager.get(POTION_EFFECTS)).intValue();
        boolean flag1 = ((Boolean)this.dataManager.get(HIDE_PARTICLES)).booleanValue();

        if (i > 0)
        {
            boolean flag;

            if (this.isInvisible())
            {
                flag = this.rand.nextInt(15) == 0;
            }
            else
            {
                flag = this.rand.nextBoolean();
            }

            if (flag1)
            {
                flag &= this.rand.nextInt(5) == 0;
            }

            if (flag && i > 0)
            {
                double d0 = (double)(i >> 16 & 255) / 255.0D;
                double d1 = (double)(i >> 8 & 255) / 255.0D;
                double d2 = (double)(i >> 0 & 255) / 255.0D;
                this.world.spawnParticle(flag1 ? EnumParticleTypes.SPELL_MOB_AMBIENT : EnumParticleTypes.SPELL_MOB, this.posX + (this.rand.nextDouble() - 0.5D) * (double)this.width, this.posY + this.rand.nextDouble() * (double)this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * (double)this.width, d0, d1, d2);
            }
        }
    }

    protected void updatePotionMetadata()
    {
        if (this.activePotionsMap.isEmpty())
        {
            this.resetPotionEffectMetadata();
            this.setInvisible(false);
        }
        else
        {
            Collection<PotionEffect> collection = this.activePotionsMap.values();
            net.minecraftforge.event.entity.living.PotionColorCalculationEvent event = new net.minecraftforge.event.entity.living.PotionColorCalculationEvent(this, PotionUtils.getPotionColorFromEffectList(collection), areAllPotionsAmbient(collection), collection);
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event);
            this.dataManager.set(HIDE_PARTICLES, event.areParticlesHidden());
            this.dataManager.set(POTION_EFFECTS, event.getColor());
            this.setInvisible(this.isPotionActive(MobEffects.INVISIBILITY));
        }
    }

    public static boolean areAllPotionsAmbient(Collection<PotionEffect> potionEffects)
    {
        for (PotionEffect potioneffect : potionEffects)
        {
            if (!potioneffect.getIsAmbient())
            {
                return false;
            }
        }

        return true;
    }

    protected void resetPotionEffectMetadata()
    {
        this.dataManager.set(HIDE_PARTICLES, Boolean.valueOf(false));
        this.dataManager.set(POTION_EFFECTS, Integer.valueOf(0));
    }

    public void clearActivePotions()
    {
        if (!this.world.isRemote)
        {
            Iterator<PotionEffect> iterator = this.activePotionsMap.values().iterator();

            while (iterator.hasNext())
            {
                PotionEffect effect = iterator.next();
                if(net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.entity.living.PotionEvent.PotionRemoveEvent(this, effect))) continue;

                this.onFinishedPotionEffect(effect);
                iterator.remove();
            }
        }
    }

    public Collection<PotionEffect> getActivePotionEffects()
    {
        return this.activePotionsMap.values();
    }

    public Map<Potion, PotionEffect> getActivePotionMap()
    {
        return this.activePotionsMap;
    }

    public boolean isPotionActive(Potion potionIn)
    {
        return this.activePotionsMap.containsKey(potionIn);
    }

    @Nullable
    public PotionEffect getActivePotionEffect(Potion potionIn)
    {
        return this.activePotionsMap.get(potionIn);
    }

    public void addPotionEffect(PotionEffect potioneffectIn)
    {
        // CraftBukkit start
        if (isTickingEffects) {
            effectsToProcess.add(potioneffectIn);
            return;
        }
        // CraftBukkit end
        if (this.isPotionApplicable(potioneffectIn))
        {
            PotionEffect potioneffect = this.activePotionsMap.get(potioneffectIn.getPotion());

            net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.entity.living.PotionEvent.PotionAddedEvent(this, potioneffect, potioneffectIn));
            if (potioneffect == null)
            {
                this.activePotionsMap.put(potioneffectIn.getPotion(), potioneffectIn);
                this.onNewPotionEffect(potioneffectIn);
            }
            else
            {
                potioneffect.combine(potioneffectIn);
                this.onChangedPotionEffect(potioneffect, true);
            }
        }
    }

    public boolean isPotionApplicable(PotionEffect potioneffectIn)
    {
        net.minecraftforge.event.entity.living.PotionEvent.PotionApplicableEvent event = new net.minecraftforge.event.entity.living.PotionEvent.PotionApplicableEvent(this, potioneffectIn);
        net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event);
        if (event.getResult() != net.minecraftforge.fml.common.eventhandler.Event.Result.DEFAULT) return event.getResult() == net.minecraftforge.fml.common.eventhandler.Event.Result.ALLOW;
        if (this.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD)
        {
            Potion potion = potioneffectIn.getPotion();

            if (potion == MobEffects.REGENERATION || potion == MobEffects.POISON)
            {
                return false;
            }
        }

        return true;
    }

    public boolean isEntityUndead()
    {
        return this.getCreatureAttribute() == EnumCreatureAttribute.UNDEAD;
    }

    @Nullable
    public PotionEffect removeActivePotionEffect(@Nullable Potion potioneffectin)
    {
        // CraftBukkit start
        if (isTickingEffects) {
            effectsToProcess.add(potioneffectin);
            return null;
        }
        // CraftBukkit end
        return this.activePotionsMap.remove(potioneffectin);
    }

    public void removePotionEffect(Potion potionIn)
    {
        if(net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.entity.living.PotionEvent.PotionRemoveEvent(this, potionIn))) return;
        PotionEffect potioneffect = this.removeActivePotionEffect(potionIn);

        if (potioneffect != null)
        {
            this.onFinishedPotionEffect(potioneffect);
        }
    }

    protected void onNewPotionEffect(PotionEffect id)
    {
        this.potionsNeedUpdate = true;

        if (!this.world.isRemote)
        {
            id.getPotion().applyAttributesModifiersToEntity(this, this.getAttributeMap(), id.getAmplifier());
        }
    }

    protected void onChangedPotionEffect(PotionEffect id, boolean p_70695_2_)
    {
        this.potionsNeedUpdate = true;

        if (p_70695_2_ && !this.world.isRemote)
        {
            Potion potion = id.getPotion();
            potion.removeAttributesModifiersFromEntity(this, this.getAttributeMap(), id.getAmplifier());
            potion.applyAttributesModifiersToEntity(this, this.getAttributeMap(), id.getAmplifier());
        }
    }

    protected void onFinishedPotionEffect(PotionEffect effect)
    {
        this.potionsNeedUpdate = true;

        if (!this.world.isRemote)
        {
            effect.getPotion().removeAttributesModifiersFromEntity(this, this.getAttributeMap(), effect.getAmplifier());
        }
    }

    public void heal(float healAmount)
    {
        // Akarin start
        heal(healAmount, EntityRegainHealthEvent.RegainReason.CUSTOM);
    }

    public void heal(float healAmount, EntityRegainHealthEvent.RegainReason regainReason) {
        heal(healAmount, regainReason, false);
    }

    public void heal(float healAmount, EntityRegainHealthEvent.RegainReason regainReason, boolean isFastRegen) {
        // Akarin end
        healAmount = net.minecraftforge.event.ForgeEventFactory.onLivingHeal(this, healAmount);
        if (healAmount <= 0) return;
        float f = this.getHealth();

        if (f > 0.0F)
        {
            // Akarin start
            EntityRegainHealthEvent event = new EntityRegainHealthEvent(this.getBukkitEntity(), healAmount, regainReason);
            MinecraftServer.instance().server.getPluginManager().callEvent(event);

            if (!event.isCancelled()) {
                this.setHealth((float) (f + event.getAmount()));
            }
            // Akarin end
        }
    }

    public final float getHealth()
    {
        // CraftBukkit start - Use unscaled health
        if (this instanceof EntityPlayerMP) {
            return (float) ((EntityPlayerMP) this).getBukkitEntity().getHealth();
        }
        // CraftBukkit end
        return ((Float)this.dataManager.get(HEALTH)).floatValue();
    }

    public void setHealth(float health)
    {
        // CraftBukkit start - Handle scaled health
        if (this instanceof EntityPlayerMP) {
            org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer player = (CraftPlayer) ((EntityPlayerMP) this).getBukkitEntity();
            // Squeeze
            if (health < 0.0F) {
                player.setRealHealth(0.0D);
            } else if (health > player.getMaxHealth()) {
                player.setRealHealth(player.getMaxHealth());
            } else {
                player.setRealHealth(health);
            }

            player.updateScaledHealth();
            return;
        }
        // CraftBukkit end
        this.dataManager.set(HEALTH, Float.valueOf(MathHelper.clamp(health, 0.0F, this.getMaxHealth())));
    }

    public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (!net.minecraftforge.common.ForgeHooks.onLivingAttack(this, source, amount)) return false;
        if (this.isEntityInvulnerable(source))
        {
            return false;
        }
        else if (this.world.isRemote)
        {
            return false;
        }
        else
        {
            this.idleTime = 0;

            if (this.getHealth() <= 0.0F)
            {
                return false;
            }
            else if (source.isFireDamage() && this.isPotionActive(MobEffects.FIRE_RESISTANCE))
            {
                return false;
            }
            else
            {
                float f = amount;

                if ((source == DamageSource.ANVIL || source == DamageSource.FALLING_BLOCK) && !this.getItemStackFromSlot(EntityEquipmentSlot.HEAD).isEmpty())
                {
                    this.getItemStackFromSlot(EntityEquipmentSlot.HEAD).damageItem((int)(amount * 4.0F + this.rand.nextFloat() * amount * 2.0F), this);
                    amount *= 0.75F;
                }

                boolean flag = false;

                if (amount > 0.0F && this.canBlockDamageSource(source))
                {
                    this.damageShield(amount);
                    amount = 0.0F;

                    if (!source.isProjectile())
                    {
                        Entity entity = source.getImmediateSource();

                        if (entity instanceof EntityLivingBase)
                        {
                            this.blockUsingShield((EntityLivingBase)entity);
                        }
                    }

                    flag = true;
                }

                this.limbSwingAmount = 1.5F;
                boolean flag1 = true;

                if ((float)this.hurtResistantTime > (float)this.maxHurtResistantTime / 2.0F)
                {
                    if (amount <= this.lastDamage)
                    {
                        return false;
                    }

                    this.damageEntity(source, amount - this.lastDamage);
                    this.lastDamage = amount;
                    flag1 = false;
                }
                else
                {
                    this.lastDamage = amount;
                    this.hurtResistantTime = this.maxHurtResistantTime;
                    this.damageEntity(source, amount);
                    this.maxHurtTime = 10;
                    this.hurtTime = this.maxHurtTime;
                }

                this.attackedAtYaw = 0.0F;
                Entity entity1 = source.getTrueSource();

                if (entity1 != null)
                {
                    if (entity1 instanceof EntityLivingBase)
                    {
                        this.setRevengeTarget((EntityLivingBase)entity1);
                    }

                    if (entity1 instanceof EntityPlayer)
                    {
                        this.recentlyHit = 100;
                        this.attackingPlayer = (EntityPlayer)entity1;
                    }
                    else if (entity1 instanceof net.minecraft.entity.passive.EntityTameable)
                    {
                        net.minecraft.entity.passive.EntityTameable entitywolf = (net.minecraft.entity.passive.EntityTameable)entity1;

                        if (entitywolf.isTamed())
                        {
                            this.recentlyHit = 100;
                            this.attackingPlayer = null;
                        }
                    }
                }

                if (flag1)
                {
                    if (flag)
                    {
                        this.world.setEntityState(this, (byte)29);
                    }
                    else if (source instanceof EntityDamageSource && ((EntityDamageSource)source).getIsThornsDamage())
                    {
                        this.world.setEntityState(this, (byte)33);
                    }
                    else
                    {
                        byte b0;

                        if (source == DamageSource.DROWN)
                        {
                            b0 = 36;
                        }
                        else if (source.isFireDamage())
                        {
                            b0 = 37;
                        }
                        else
                        {
                            b0 = 2;
                        }

                        this.world.setEntityState(this, b0);
                    }

                    if (source != DamageSource.DROWN && (!flag || amount > 0.0F))
                    {
                        this.markVelocityChanged();
                    }

                    if (entity1 != null)
                    {
                        double d1 = entity1.posX - this.posX;
                        double d0;

                        for (d0 = entity1.posZ - this.posZ; d1 * d1 + d0 * d0 < 1.0E-4D; d0 = (Math.random() - Math.random()) * 0.01D)
                        {
                            d1 = (Math.random() - Math.random()) * 0.01D;
                        }

                        this.attackedAtYaw = (float)(MathHelper.atan2(d0, d1) * (180D / Math.PI) - (double)this.rotationYaw);
                        this.knockBack(entity1, 0.4F, d1, d0);
                    }
                    else
                    {
                        this.attackedAtYaw = (float)((int)(Math.random() * 2.0D) * 180);
                    }
                }

                if (this.getHealth() <= 0.0F)
                {
                    if (!this.checkTotemDeathProtection(source))
                    {
                        SoundEvent soundevent = this.getDeathSound();

                        if (flag1 && soundevent != null)
                        {
                            this.playSound(soundevent, this.getSoundVolume(), this.getSoundPitch());
                        }

                        this.onDeath(source);
                    }
                }
                else if (flag1)
                {
                    this.playHurtSound(source);
                }

                boolean flag2 = !flag || amount > 0.0F;

                if (flag2)
                {
                    this.lastDamageSource = source;
                    this.lastDamageStamp = this.world.getTotalWorldTime();
                }

                if (this instanceof EntityPlayerMP)
                {
                    CriteriaTriggers.ENTITY_HURT_PLAYER.trigger((EntityPlayerMP)this, source, f, amount, flag);
                }

                if (entity1 instanceof EntityPlayerMP)
                {
                    CriteriaTriggers.PLAYER_HURT_ENTITY.trigger((EntityPlayerMP)entity1, this, source, f, amount, flag);
                }

                return flag2;
            }
        }
    }

    protected void blockUsingShield(EntityLivingBase p_190629_1_)
    {
        p_190629_1_.knockBack(this, 0.5F, this.posX - p_190629_1_.posX, this.posZ - p_190629_1_.posZ);
    }

    private boolean checkTotemDeathProtection(DamageSource p_190628_1_)
    {
        if (p_190628_1_.canHarmInCreative())
        {
            return false;
        }
        else
        {
            ItemStack itemstack = null;

            for (EnumHand enumhand : EnumHand.values())
            {
                ItemStack itemstack1 = this.getHeldItem(enumhand);

                if (itemstack1.getItem() == Items.TOTEM_OF_UNDYING)
                {
                    itemstack = itemstack1.copy();
                    itemstack1.shrink(1);
                    break;
                }
            }

            if (itemstack != null)
            {
                if (this instanceof EntityPlayerMP)
                {
                    EntityPlayerMP entityplayermp = (EntityPlayerMP)this;
                    entityplayermp.addStat(StatList.getObjectUseStats(Items.TOTEM_OF_UNDYING));
                    CriteriaTriggers.USED_TOTEM.trigger(entityplayermp, itemstack);
                }

                this.setHealth(1.0F);
                this.clearActivePotions();
                this.addPotionEffect(new PotionEffect(MobEffects.REGENERATION, 900, 1));
                this.addPotionEffect(new PotionEffect(MobEffects.ABSORPTION, 100, 1));
                this.world.setEntityState(this, (byte)35);
            }

            return itemstack != null;
        }
    }

    @Nullable
    public DamageSource getLastDamageSource()
    {
        if (this.world.getTotalWorldTime() - this.lastDamageStamp > 40L)
        {
            this.lastDamageSource = null;
        }

        return this.lastDamageSource;
    }

    protected void playHurtSound(DamageSource source)
    {
        SoundEvent soundevent = this.getHurtSound(source);

        if (soundevent != null)
        {
            this.playSound(soundevent, this.getSoundVolume(), this.getSoundPitch());
        }
    }

    private boolean canBlockDamageSource(DamageSource damageSourceIn)
    {
        if (!damageSourceIn.isUnblockable() && this.isActiveItemStackBlocking())
        {
            Vec3d vec3d = damageSourceIn.getDamageLocation();

            if (vec3d != null)
            {
                Vec3d vec3d1 = this.getLook(1.0F);
                Vec3d vec3d2 = vec3d.subtractReverse(new Vec3d(this.posX, this.posY, this.posZ)).normalize();
                vec3d2 = new Vec3d(vec3d2.x, 0.0D, vec3d2.z);

                if (vec3d2.dotProduct(vec3d1) < 0.0D)
                {
                    return true;
                }
            }
        }

        return false;
    }

    public void renderBrokenItemStack(ItemStack stack)
    {
        this.world.playSound(null, this.posX, this.posY, this.posZ, SoundEvents.ENTITY_ITEM_BREAK, this.getSoundCategory(), 0.8F, 0.8F + this.world.rand.nextFloat() * 0.4F); //Forge: Fix MC-2518 Items are not damaged on the client so client needs packet as well.

        for (int i = 0; i < 5; ++i)
        {
            Vec3d vec3d = new Vec3d(((double)this.rand.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);
            vec3d = vec3d.rotatePitch(-this.rotationPitch * 0.017453292F);
            vec3d = vec3d.rotateYaw(-this.rotationYaw * 0.017453292F);
            double d0 = (double)(-this.rand.nextFloat()) * 0.6D - 0.3D;
            Vec3d vec3d1 = new Vec3d(((double)this.rand.nextFloat() - 0.5D) * 0.3D, d0, 0.6D);
            vec3d1 = vec3d1.rotatePitch(-this.rotationPitch * 0.017453292F);
            vec3d1 = vec3d1.rotateYaw(-this.rotationYaw * 0.017453292F);
            vec3d1 = vec3d1.addVector(this.posX, this.posY + (double)this.getEyeHeight(), this.posZ);
            if (this.world instanceof WorldServer) //Forge: Fix MC-2518 spawnParticle is nooped on server, need to use server specific variant
                ((WorldServer)this.world).spawnParticle(EnumParticleTypes.ITEM_CRACK, vec3d1.x, vec3d1.y, vec3d1.z, 0,  vec3d.x, vec3d.y + 0.05D, vec3d.z, 0.0D, Item.getIdFromItem(stack.getItem()), stack.getMetadata());
            else //Fix the fact that spawning ItemCrack uses TWO arguments.
                this.world.spawnParticle(EnumParticleTypes.ITEM_CRACK, vec3d1.x, vec3d1.y, vec3d1.z, vec3d.x, vec3d.y + 0.05D, vec3d.z, Item.getIdFromItem(stack.getItem()), stack.getMetadata());

        }
    }

    public void onDeath(DamageSource cause)
    {
        if (net.minecraftforge.common.ForgeHooks.onLivingDeath(this, cause)) return;
        if (!this.dead)
        {
            Entity entity = cause.getTrueSource();
            EntityLivingBase entitylivingbase = this.getAttackingEntity();

            if (this.scoreValue >= 0 && entitylivingbase != null)
            {
                entitylivingbase.awardKillScore(this, this.scoreValue, cause);
            }

            if (entity != null)
            {
                entity.onKillEntity(this);
            }

            this.dead = true;
            this.getCombatTracker().reset();

            if (!this.world.isRemote)
            {
                int i = net.minecraftforge.common.ForgeHooks.getLootingLevel(this, entity, cause);

                captureDrops = true;
                capturedDrops.clear();

                if (this.canDropLoot() && this.world.getGameRules().getBoolean("doMobLoot"))
                {
                    boolean flag = this.recentlyHit > 0;
                    this.dropLoot(flag, i, cause);
                }

                captureDrops = false;

                if (!net.minecraftforge.common.ForgeHooks.onLivingDrops(this, cause, capturedDrops, i, recentlyHit > 0))
                {
                    for (EntityItem item : capturedDrops)
                    {
                        world.spawnEntity(item);
                    }
                }
            }

            this.world.setEntityState(this, (byte)3);
        }
    }

    protected void dropLoot(boolean wasRecentlyHit, int lootingModifier, DamageSource source)
    {
        this.dropFewItems(wasRecentlyHit, lootingModifier);
        this.dropEquipment(wasRecentlyHit, lootingModifier);
    }

    protected void dropEquipment(boolean wasRecentlyHit, int lootingModifier)
    {
    }

    public void knockBack(Entity entityIn, float strength, double xRatio, double zRatio)
    {
        net.minecraftforge.event.entity.living.LivingKnockBackEvent event = net.minecraftforge.common.ForgeHooks.onLivingKnockBack(this, entityIn, strength, xRatio, zRatio);
        if(event.isCanceled()) return;
        strength = event.getStrength(); xRatio = event.getRatioX(); zRatio = event.getRatioZ();
        if (this.rand.nextDouble() >= this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).getAttributeValue())
        {
            this.isAirBorne = true;
            float f = MathHelper.sqrt(xRatio * xRatio + zRatio * zRatio);
            this.motionX /= 2.0D;
            this.motionZ /= 2.0D;
            this.motionX -= xRatio / (double)f * (double)strength;
            this.motionZ -= zRatio / (double)f * (double)strength;

            if (this.onGround)
            {
                this.motionY /= 2.0D;
                this.motionY += (double)strength;

                if (this.motionY > 0.4000000059604645D)
                {
                    this.motionY = 0.4000000059604645D;
                }
            }
        }
    }

    @Nullable
    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return SoundEvents.ENTITY_GENERIC_HURT;
    }

    @Nullable
    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_GENERIC_DEATH;
    }

    protected SoundEvent getFallSound(int heightIn)
    {
        return heightIn > 4 ? SoundEvents.ENTITY_GENERIC_BIG_FALL : SoundEvents.ENTITY_GENERIC_SMALL_FALL;
    }

    protected void dropFewItems(boolean wasRecentlyHit, int lootingModifier)
    {
    }

    public boolean isOnLadder()
    {
        int i = MathHelper.floor(this.posX);
        int j = MathHelper.floor(this.getEntityBoundingBox().minY);
        int k = MathHelper.floor(this.posZ);

        if (this instanceof EntityPlayer && ((EntityPlayer)this).isSpectator())
        {
            return false;
        }
        else
        {
            BlockPos blockpos = new BlockPos(i, j, k);
            IBlockState iblockstate = this.world.getBlockState(blockpos);
            Block block = iblockstate.getBlock();
            return net.minecraftforge.common.ForgeHooks.isLivingOnLadder(iblockstate, world, new BlockPos(i, j, k), this);
        }
    }

    private boolean canGoThroughtTrapDoorOnLadder(BlockPos pos, IBlockState state)
    {
        if (((Boolean)state.getValue(BlockTrapDoor.OPEN)).booleanValue())
        {
            IBlockState iblockstate = this.world.getBlockState(pos.down());

            if (iblockstate.getBlock() == Blocks.LADDER && iblockstate.getValue(BlockLadder.FACING) == state.getValue(BlockTrapDoor.FACING))
            {
                return true;
            }
        }

        return false;
    }

    public boolean isEntityAlive()
    {
        return !this.isDead && this.getHealth() > 0.0F;
    }

    public void fall(float distance, float damageMultiplier)
    {
        float[] ret = net.minecraftforge.common.ForgeHooks.onLivingFall(this, distance, damageMultiplier);
        if (ret == null) return;
        distance = ret[0]; damageMultiplier = ret[1];
        super.fall(distance, damageMultiplier);
        PotionEffect potioneffect = this.getActivePotionEffect(MobEffects.JUMP_BOOST);
        float f = potioneffect == null ? 0.0F : (float)(potioneffect.getAmplifier() + 1);
        int i = MathHelper.ceil((distance - 3.0F - f) * damageMultiplier);

        if (i > 0)
        {
            this.playSound(this.getFallSound(i), 1.0F, 1.0F);
            this.attackEntityFrom(DamageSource.FALL, (float)i);
            int j = MathHelper.floor(this.posX);
            int k = MathHelper.floor(this.posY - 0.20000000298023224D);
            int l = MathHelper.floor(this.posZ);
            IBlockState iblockstate = this.world.getBlockState(new BlockPos(j, k, l));

            if (iblockstate.getMaterial() != Material.AIR)
            {
                SoundType soundtype = iblockstate.getBlock().getSoundType(iblockstate, world, new BlockPos(j, k, l), this);
                this.playSound(soundtype.getFallSound(), soundtype.getVolume() * 0.5F, soundtype.getPitch() * 0.75F);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void performHurtAnimation()
    {
        this.maxHurtTime = 10;
        this.hurtTime = this.maxHurtTime;
        this.attackedAtYaw = 0.0F;
    }

    public int getTotalArmorValue()
    {
        IAttributeInstance iattributeinstance = this.getEntityAttribute(SharedMonsterAttributes.ARMOR);
        return MathHelper.floor(iattributeinstance.getAttributeValue());
    }

    protected void damageArmor(float damage)
    {
    }

    protected void damageShield(float damage)
    {
    }

    protected float applyArmorCalculations(DamageSource source, float damage)
    {
        if (!source.isUnblockable())
        {
            this.damageArmor(damage);
            damage = CombatRules.getDamageAfterAbsorb(damage, (float)this.getTotalArmorValue(), (float)this.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
        }

        return damage;
    }

    protected float applyPotionDamageCalculations(DamageSource source, float damage)
    {
        if (source.isDamageAbsolute())
        {
            return damage;
        }
        else
        {
            if (this.isPotionActive(MobEffects.RESISTANCE) && source != DamageSource.OUT_OF_WORLD)
            {
                int i = (this.getActivePotionEffect(MobEffects.RESISTANCE).getAmplifier() + 1) * 5;
                int j = 25 - i;
                float f = damage * (float)j;
                damage = f / 25.0F;
            }

            if (damage <= 0.0F)
            {
                return 0.0F;
            }
            else
            {
                int k = EnchantmentHelper.getEnchantmentModifierDamage(this.getArmorInventoryList(), source);

                if (k > 0)
                {
                    damage = CombatRules.getDamageAfterMagicAbsorb(damage, (float)k);
                }

                return damage;
            }
        }
    }

    protected void damageEntity(DamageSource damageSrc, float damageAmount)
    {
        if (!this.isEntityInvulnerable(damageSrc))
        {
            damageAmount = net.minecraftforge.common.ForgeHooks.onLivingHurt(this, damageSrc, damageAmount);
            if (damageAmount <= 0) return;
            damageAmount = this.applyArmorCalculations(damageSrc, damageAmount);
            damageAmount = this.applyPotionDamageCalculations(damageSrc, damageAmount);
            float f = damageAmount;
            damageAmount = Math.max(damageAmount - this.getAbsorptionAmount(), 0.0F);
            this.setAbsorptionAmount(this.getAbsorptionAmount() - (f - damageAmount));
            damageAmount = net.minecraftforge.common.ForgeHooks.onLivingDamage(this, damageSrc, damageAmount);

            if (damageAmount != 0.0F)
            {
                float f1 = this.getHealth();
                this.getCombatTracker().trackDamage(damageSrc, f1, damageAmount);
                this.setHealth(f1 - damageAmount); // Forge: moved to fix MC-121048
                this.setAbsorptionAmount(this.getAbsorptionAmount() - damageAmount);
            }
        }
    }

    public CombatTracker getCombatTracker()
    {
        return this._combatTracker;
    }

    @Nullable
    public EntityLivingBase getAttackingEntity()
    {
        if (this._combatTracker.getBestAttacker() != null)
        {
            return this._combatTracker.getBestAttacker();
        }
        else if (this.attackingPlayer != null)
        {
            return this.attackingPlayer;
        }
        else
        {
            return this.revengeTarget != null ? this.revengeTarget : null;
        }
    }

    public final float getMaxHealth()
    {
        return (float)this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getAttributeValue();
    }

    public final int getArrowCountInEntity()
    {
        return ((Integer)this.dataManager.get(ARROW_COUNT_IN_ENTITY)).intValue();
    }

    public final void setArrowCountInEntity(int count)
    {
        this.dataManager.set(ARROW_COUNT_IN_ENTITY, Integer.valueOf(count));
    }

    private int getArmSwingAnimationEnd()
    {
        if (this.isPotionActive(MobEffects.HASTE))
        {
            return 6 - (1 + this.getActivePotionEffect(MobEffects.HASTE).getAmplifier());
        }
        else
        {
            return this.isPotionActive(MobEffects.MINING_FATIGUE) ? 6 + (1 + this.getActivePotionEffect(MobEffects.MINING_FATIGUE).getAmplifier()) * 2 : 6;
        }
    }

    public void swingArm(EnumHand hand)
    {
        ItemStack stack = this.getHeldItem(hand);
        if (!stack.isEmpty())
        {
            if (stack.getItem().onEntitySwing(this, stack)) return;
        }
        if (!this.isSwingInProgress || this.swingProgressInt >= this.getArmSwingAnimationEnd() / 2 || this.swingProgressInt < 0)
        {
            this.swingProgressInt = -1;
            this.isSwingInProgress = true;
            this.swingingHand = hand;

            if (this.world instanceof WorldServer)
            {
                ((WorldServer)this.world).getEntityTracker().sendToTracking(this, new SPacketAnimation(this, hand == EnumHand.MAIN_HAND ? 0 : 3));
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void handleStatusUpdate(byte id)
    {
        boolean flag = id == 33;
        boolean flag1 = id == 36;
        boolean flag2 = id == 37;

        if (id != 2 && !flag && !flag1 && !flag2)
        {
            if (id == 3)
            {
                SoundEvent soundevent1 = this.getDeathSound();

                if (soundevent1 != null)
                {
                    this.playSound(soundevent1, this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
                }

                this.setHealth(0.0F);
                this.onDeath(DamageSource.GENERIC);
            }
            else if (id == 30)
            {
                this.playSound(SoundEvents.ITEM_SHIELD_BREAK, 0.8F, 0.8F + this.world.rand.nextFloat() * 0.4F);
            }
            else if (id == 29)
            {
                this.playSound(SoundEvents.ITEM_SHIELD_BLOCK, 1.0F, 0.8F + this.world.rand.nextFloat() * 0.4F);
            }
            else
            {
                super.handleStatusUpdate(id);
            }
        }
        else
        {
            this.limbSwingAmount = 1.5F;
            this.hurtResistantTime = this.maxHurtResistantTime;
            this.maxHurtTime = 10;
            this.hurtTime = this.maxHurtTime;
            this.attackedAtYaw = 0.0F;

            if (flag)
            {
                this.playSound(SoundEvents.ENCHANT_THORNS_HIT, this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
            }

            DamageSource damagesource;

            if (flag2)
            {
                damagesource = DamageSource.ON_FIRE;
            }
            else if (flag1)
            {
                damagesource = DamageSource.DROWN;
            }
            else
            {
                damagesource = DamageSource.GENERIC;
            }

            SoundEvent soundevent = this.getHurtSound(damagesource);

            if (soundevent != null)
            {
                this.playSound(soundevent, this.getSoundVolume(), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
            }

            this.attackEntityFrom(DamageSource.GENERIC, 0.0F);
        }
    }

    protected void outOfWorld()
    {
        this.attackEntityFrom(DamageSource.OUT_OF_WORLD, 4.0F);
    }

    protected void updateArmSwingProgress()
    {
        int i = this.getArmSwingAnimationEnd();

        if (this.isSwingInProgress)
        {
            ++this.swingProgressInt;

            if (this.swingProgressInt >= i)
            {
                this.swingProgressInt = 0;
                this.isSwingInProgress = false;
            }
        }
        else
        {
            this.swingProgressInt = 0;
        }

        this.swingProgress = (float)this.swingProgressInt / (float)i;
    }

    public IAttributeInstance getEntityAttribute(IAttribute attribute)
    {
        return this.getAttributeMap().getAttributeInstance(attribute);
    }

    public AbstractAttributeMap getAttributeMap()
    {
        if (this.attributeMap == null)
        {
            this.attributeMap = new AttributeMap();
        }

        return this.attributeMap;
    }

    public EnumCreatureAttribute getCreatureAttribute()
    {
        return EnumCreatureAttribute.UNDEFINED;
    }

    public ItemStack getHeldItemMainhand()
    {
        return this.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
    }

    public ItemStack getHeldItemOffhand()
    {
        return this.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND);
    }

    public ItemStack getHeldItem(EnumHand hand)
    {
        if (hand == EnumHand.MAIN_HAND)
        {
            return this.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND);
        }
        else if (hand == EnumHand.OFF_HAND)
        {
            return this.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND);
        }
        else
        {
            throw new IllegalArgumentException("Invalid hand " + hand);
        }
    }

    public void setHeldItem(EnumHand hand, ItemStack stack)
    {
        if (hand == EnumHand.MAIN_HAND)
        {
            this.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, stack);
        }
        else
        {
            if (hand != EnumHand.OFF_HAND)
            {
                throw new IllegalArgumentException("Invalid hand " + hand);
            }

            this.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, stack);
        }
    }

    public boolean hasItemInSlot(EntityEquipmentSlot p_190630_1_)
    {
        return !this.getItemStackFromSlot(p_190630_1_).isEmpty();
    }

    public abstract Iterable<ItemStack> getArmorInventoryList();

    public abstract ItemStack getItemStackFromSlot(EntityEquipmentSlot slotIn);

    public abstract void setItemStackToSlot(EntityEquipmentSlot slotIn, ItemStack stack);

    public void setSprinting(boolean sprinting)
    {
        super.setSprinting(sprinting);
        IAttributeInstance iattributeinstance = this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);

        if (iattributeinstance.getModifier(SPRINTING_SPEED_BOOST_ID) != null)
        {
            iattributeinstance.removeModifier(SPRINTING_SPEED_BOOST);
        }

        if (sprinting)
        {
            iattributeinstance.applyModifier(SPRINTING_SPEED_BOOST);
        }
    }

    protected float getSoundVolume()
    {
        return 1.0F;
    }

    protected float getSoundPitch()
    {
        return this.isChild() ? (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.5F : (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F;
    }

    protected boolean isMovementBlocked()
    {
        return this.getHealth() <= 0.0F;
    }

    public void dismountEntity(Entity entityIn)
    {
        if (!(entityIn instanceof EntityBoat) && !(entityIn instanceof AbstractHorse))
        {
            double d1 = entityIn.posX;
            double d13 = entityIn.getEntityBoundingBox().minY + (double)entityIn.height;
            double d14 = entityIn.posZ;
            EnumFacing enumfacing1 = entityIn.getAdjustedHorizontalFacing();

            if (enumfacing1 != null)
            {
                EnumFacing enumfacing = enumfacing1.rotateY();
                int[][] aint1 = new int[][] {{0, 1}, {0, -1}, { -1, 1}, { -1, -1}, {1, 1}, {1, -1}, { -1, 0}, {1, 0}, {0, 1}};
                double d5 = Math.floor(this.posX) + 0.5D;
                double d6 = Math.floor(this.posZ) + 0.5D;
                double d7 = this.getEntityBoundingBox().maxX - this.getEntityBoundingBox().minX;
                double d8 = this.getEntityBoundingBox().maxZ - this.getEntityBoundingBox().minZ;
                AxisAlignedBB axisalignedbb = new AxisAlignedBB(d5 - d7 / 2.0D, entityIn.getEntityBoundingBox().minY, d6 - d8 / 2.0D, d5 + d7 / 2.0D, Math.floor(entityIn.getEntityBoundingBox().minY) + (double)this.height, d6 + d8 / 2.0D);

                for (int[] aint : aint1)
                {
                    double d9 = (double)(enumfacing1.getFrontOffsetX() * aint[0] + enumfacing.getFrontOffsetX() * aint[1]);
                    double d10 = (double)(enumfacing1.getFrontOffsetZ() * aint[0] + enumfacing.getFrontOffsetZ() * aint[1]);
                    double d11 = d5 + d9;
                    double d12 = d6 + d10;
                    AxisAlignedBB axisalignedbb1 = axisalignedbb.offset(d9, 0.0D, d10);

                    if (!this.world.collidesWithAnyBlock(axisalignedbb1))
                    {
                        if (this.world.getBlockState(new BlockPos(d11, this.posY, d12)).isSideSolid(world, new BlockPos(d11, this.posY, d12), EnumFacing.UP))
                        {
                            this.setPositionAndUpdate(d11, this.posY + 1.0D, d12);
                            return;
                        }

                        BlockPos blockpos = new BlockPos(d11, this.posY - 1.0D, d12);

                        if (this.world.getBlockState(blockpos).isSideSolid(world, blockpos, EnumFacing.UP) || this.world.getBlockState(blockpos).getMaterial() == Material.WATER)
                        {
                            d1 = d11;
                            d13 = this.posY + 1.0D;
                            d14 = d12;
                        }
                    }
                    else if (!this.world.collidesWithAnyBlock(axisalignedbb1.offset(0.0D, 1.0D, 0.0D)) && this.world.getBlockState(new BlockPos(d11, this.posY + 1.0D, d12)).isSideSolid(world, new BlockPos(d11, this.posY + 1.0D, d12), EnumFacing.UP))
                    {
                        d1 = d11;
                        d13 = this.posY + 2.0D;
                        d14 = d12;
                    }
                }
            }

            this.setPositionAndUpdate(d1, d13, d14);
        }
        else
        {
            double d0 = (double)(this.width / 2.0F + entityIn.width / 2.0F) + 0.4D;
            float f;

            if (entityIn instanceof EntityBoat)
            {
                f = 0.0F;
            }
            else
            {
                f = ((float)Math.PI / 2F) * (float)(this.getPrimaryHand() == EnumHandSide.RIGHT ? -1 : 1);
            }

            float f1 = -MathHelper.sin(-this.rotationYaw * 0.017453292F - (float)Math.PI + f);
            float f2 = -MathHelper.cos(-this.rotationYaw * 0.017453292F - (float)Math.PI + f);
            double d2 = Math.abs(f1) > Math.abs(f2) ? d0 / (double)Math.abs(f1) : d0 / (double)Math.abs(f2);
            double d3 = this.posX + (double)f1 * d2;
            double d4 = this.posZ + (double)f2 * d2;
            this.setPosition(d3, entityIn.posY + (double)entityIn.height + 0.001D, d4);

            if (this.world.collidesWithAnyBlock(this.getEntityBoundingBox()))
            {
                this.setPosition(d3, entityIn.posY + (double)entityIn.height + 1.001D, d4);

                if (this.world.collidesWithAnyBlock(this.getEntityBoundingBox()))
                {
                    this.setPosition(entityIn.posX, entityIn.posY + (double)this.height + 0.001D, entityIn.posZ);
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public boolean getAlwaysRenderNameTagForRender()
    {
        return this.getAlwaysRenderNameTag();
    }

    protected float getJumpUpwardsMotion()
    {
        return 0.42F;
    }

    protected void jump()
    {
        this.motionY = (double)this.getJumpUpwardsMotion();

        if (this.isPotionActive(MobEffects.JUMP_BOOST))
        {
            this.motionY += (double)((float)(this.getActivePotionEffect(MobEffects.JUMP_BOOST).getAmplifier() + 1) * 0.1F);
        }

        if (this.isSprinting())
        {
            float f = this.rotationYaw * 0.017453292F;
            this.motionX -= (double)(MathHelper.sin(f) * 0.2F);
            this.motionZ += (double)(MathHelper.cos(f) * 0.2F);
        }

        this.isAirBorne = true;
        net.minecraftforge.common.ForgeHooks.onLivingJump(this);
    }

    protected void handleJumpWater()
    {
        this.motionY += 0.03999999910593033D * this.getEntityAttribute(SWIM_SPEED).getAttributeValue();
    }

    protected void handleJumpLava()
    {
        this.motionY += 0.03999999910593033D * this.getEntityAttribute(SWIM_SPEED).getAttributeValue();
    }

    protected float getWaterSlowDown()
    {
        return 0.8F;
    }

    public void travel(float strafe, float vertical, float forward)
    {
        if (this.isServerWorld() || this.canPassengerSteer())
        {
            if (!this.isInWater() || this instanceof EntityPlayer && ((EntityPlayer)this).capabilities.isFlying)
            {
                if (!this.isInLava() || this instanceof EntityPlayer && ((EntityPlayer)this).capabilities.isFlying)
                {
                    if (this.isElytraFlying())
                    {
                        if (this.motionY > -0.5D)
                        {
                            this.fallDistance = 1.0F;
                        }

                        Vec3d vec3d = this.getLookVec();
                        float f = this.rotationPitch * 0.017453292F;
                        double d6 = Math.sqrt(vec3d.x * vec3d.x + vec3d.z * vec3d.z);
                        double d8 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
                        double d1 = vec3d.lengthVector();
                        float f4 = MathHelper.cos(f);
                        f4 = (float)((double)f4 * (double)f4 * Math.min(1.0D, d1 / 0.4D));
                        this.motionY += -0.08D + (double)f4 * 0.06D;

                        if (this.motionY < 0.0D && d6 > 0.0D)
                        {
                            double d2 = this.motionY * -0.1D * (double)f4;
                            this.motionY += d2;
                            this.motionX += vec3d.x * d2 / d6;
                            this.motionZ += vec3d.z * d2 / d6;
                        }

                        if (f < 0.0F)
                        {
                            double d10 = d8 * (double)(-MathHelper.sin(f)) * 0.04D;
                            this.motionY += d10 * 3.2D;
                            this.motionX -= vec3d.x * d10 / d6;
                            this.motionZ -= vec3d.z * d10 / d6;
                        }

                        if (d6 > 0.0D)
                        {
                            this.motionX += (vec3d.x / d6 * d8 - this.motionX) * 0.1D;
                            this.motionZ += (vec3d.z / d6 * d8 - this.motionZ) * 0.1D;
                        }

                        this.motionX *= 0.9900000095367432D;
                        this.motionY *= 0.9800000190734863D;
                        this.motionZ *= 0.9900000095367432D;
                        this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);

                        if (this.collidedHorizontally && !this.world.isRemote)
                        {
                            double d11 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ);
                            double d3 = d8 - d11;
                            float f5 = (float)(d3 * 10.0D - 3.0D);

                            if (f5 > 0.0F)
                            {
                                this.playSound(this.getFallSound((int)f5), 1.0F, 1.0F);
                                this.attackEntityFrom(DamageSource.FLY_INTO_WALL, f5);
                            }
                        }

                        if (this.onGround && !this.world.isRemote)
                        {
                            this.setFlag(7, false);
                        }
                    }
                    else
                    {
                        float f6 = 0.91F;
                        BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain(this.posX, this.getEntityBoundingBox().minY - 1.0D, this.posZ);

                        if (this.onGround)
                        {
                            IBlockState underState = this.world.getBlockState(blockpos$pooledmutableblockpos);
                            f6 = underState.getBlock().getSlipperiness(underState, this.world, blockpos$pooledmutableblockpos, this) * 0.91F;
                        }

                        float f7 = 0.16277136F / (f6 * f6 * f6);
                        float f8;

                        if (this.onGround)
                        {
                            f8 = this.getAIMoveSpeed() * f7;
                        }
                        else
                        {
                            f8 = this.jumpMovementFactor;
                        }

                        this.moveRelative(strafe, vertical, forward, f8);
                        f6 = 0.91F;

                        if (this.onGround)
                        {
                            IBlockState underState = this.world.getBlockState(blockpos$pooledmutableblockpos.setPos(this.posX, this.getEntityBoundingBox().minY - 1.0D, this.posZ));
                            f6 = underState.getBlock().getSlipperiness(underState, this.world, blockpos$pooledmutableblockpos, this) * 0.91F;
                        }

                        if (this.isOnLadder())
                        {
                            float f9 = 0.15F;
                            this.motionX = MathHelper.clamp(this.motionX, -0.15000000596046448D, 0.15000000596046448D);
                            this.motionZ = MathHelper.clamp(this.motionZ, -0.15000000596046448D, 0.15000000596046448D);
                            this.fallDistance = 0.0F;

                            if (this.motionY < -0.15D)
                            {
                                this.motionY = -0.15D;
                            }

                            boolean flag = this.isSneaking() && this instanceof EntityPlayer;

                            if (flag && this.motionY < 0.0D)
                            {
                                this.motionY = 0.0D;
                            }
                        }

                        this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);

                        if (this.collidedHorizontally && this.isOnLadder())
                        {
                            this.motionY = 0.2D;
                        }

                        if (this.isPotionActive(MobEffects.LEVITATION))
                        {
                            this.motionY += (0.05D * (double)(this.getActivePotionEffect(MobEffects.LEVITATION).getAmplifier() + 1) - this.motionY) * 0.2D;
                        }
                        else
                        {
                            blockpos$pooledmutableblockpos.setPos(this.posX, 0.0D, this.posZ);

                            if (!this.world.isRemote || this.world.isBlockLoaded(blockpos$pooledmutableblockpos) && this.world.getChunkFromBlockCoords(blockpos$pooledmutableblockpos).isLoaded())
                            {
                                if (!this.hasNoGravity())
                                {
                                    this.motionY -= 0.08D;
                                }
                            }
                            else if (this.posY > 0.0D)
                            {
                                this.motionY = -0.1D;
                            }
                            else
                            {
                                this.motionY = 0.0D;
                            }
                        }

                        this.motionY *= 0.9800000190734863D;
                        this.motionX *= (double)f6;
                        this.motionZ *= (double)f6;
                        blockpos$pooledmutableblockpos.release();
                    }
                }
                else
                {
                    double d4 = this.posY;
                    this.moveRelative(strafe, vertical, forward, 0.02F);
                    this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
                    this.motionX *= 0.5D;
                    this.motionY *= 0.5D;
                    this.motionZ *= 0.5D;

                    if (!this.hasNoGravity())
                    {
                        this.motionY -= 0.02D;
                    }

                    if (this.collidedHorizontally && this.isOffsetPositionInLiquid(this.motionX, this.motionY + 0.6000000238418579D - this.posY + d4, this.motionZ))
                    {
                        this.motionY = 0.30000001192092896D;
                    }
                }
            }
            else
            {
                double d0 = this.posY;
                float f1 = this.getWaterSlowDown();
                float f2 = 0.02F;
                float f3 = (float)EnchantmentHelper.getDepthStriderModifier(this);

                if (f3 > 3.0F)
                {
                    f3 = 3.0F;
                }

                if (!this.onGround)
                {
                    f3 *= 0.5F;
                }

                if (f3 > 0.0F)
                {
                    f1 += (0.54600006F - f1) * f3 / 3.0F;
                    f2 += (this.getAIMoveSpeed() - f2) * f3 / 3.0F;
                }

                this.moveRelative(strafe, vertical, forward, f2);
                this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
                this.motionX *= (double)f1;
                this.motionY *= 0.800000011920929D;
                this.motionZ *= (double)f1;

                if (!this.hasNoGravity())
                {
                    this.motionY -= 0.02D;
                }

                if (this.collidedHorizontally && this.isOffsetPositionInLiquid(this.motionX, this.motionY + 0.6000000238418579D - this.posY + d0, this.motionZ))
                {
                    this.motionY = 0.30000001192092896D;
                }
            }
        }

        this.prevLimbSwingAmount = this.limbSwingAmount;
        double d5 = this.posX - this.prevPosX;
        double d7 = this.posZ - this.prevPosZ;
        double d9 = this instanceof net.minecraft.entity.passive.EntityFlying ? this.posY - this.prevPosY : 0.0D;
        float f10 = MathHelper.sqrt(d5 * d5 + d9 * d9 + d7 * d7) * 4.0F;

        if (f10 > 1.0F)
        {
            f10 = 1.0F;
        }

        this.limbSwingAmount += (f10 - this.limbSwingAmount) * 0.4F;
        this.limbSwing += this.limbSwingAmount;
    }

    public float getAIMoveSpeed()
    {
        return this.landMovementFactor;
    }

    public void setAIMoveSpeed(float speedIn)
    {
        this.landMovementFactor = speedIn;
    }

    public boolean attackEntityAsMob(Entity entityIn)
    {
        this.setLastAttackedEntity(entityIn);
        return false;
    }

    public boolean isPlayerSleeping()
    {
        return false;
    }

    public void onUpdate()
    {
        if (net.minecraftforge.common.ForgeHooks.onLivingUpdate(this)) return;
        super.onUpdate();
        this.updateActiveHand();

        if (!this.world.isRemote)
        {
            int i = this.getArrowCountInEntity();

            if (i > 0)
            {
                if (this.arrowHitTimer <= 0)
                {
                    this.arrowHitTimer = 20 * (30 - i);
                }

                --this.arrowHitTimer;

                if (this.arrowHitTimer <= 0)
                {
                    this.setArrowCountInEntity(i - 1);
                }
            }

            for (EntityEquipmentSlot entityequipmentslot : EntityEquipmentSlot.values())
            {
                ItemStack itemstack;

                switch (entityequipmentslot.getSlotType())
                {
                    case HAND:
                        itemstack = this.handInventory.get(entityequipmentslot.getIndex());
                        break;
                    case ARMOR:
                        itemstack = this.armorArray.get(entityequipmentslot.getIndex());
                        break;
                    default:
                        continue;
                }

                ItemStack itemstack1 = this.getItemStackFromSlot(entityequipmentslot);

                if (!ItemStack.areItemStacksEqual(itemstack1, itemstack))
                {
                    if (!ItemStack.areItemStacksEqualUsingNBTShareTag(itemstack1, itemstack))
                    ((WorldServer)this.world).getEntityTracker().sendToTracking(this, new SPacketEntityEquipment(this.getEntityId(), entityequipmentslot, itemstack1));
                    net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.entity.living.LivingEquipmentChangeEvent(this, entityequipmentslot, itemstack, itemstack1));

                    if (!itemstack.isEmpty())
                    {
                        this.getAttributeMap().removeAttributeModifiers(itemstack.getAttributeModifiers(entityequipmentslot));
                    }

                    if (!itemstack1.isEmpty())
                    {
                        this.getAttributeMap().applyAttributeModifiers(itemstack1.getAttributeModifiers(entityequipmentslot));
                    }

                    switch (entityequipmentslot.getSlotType())
                    {
                        case HAND:
                            this.handInventory.set(entityequipmentslot.getIndex(), itemstack1.isEmpty() ? ItemStack.EMPTY : itemstack1.copy());
                            break;
                        case ARMOR:
                            this.armorArray.set(entityequipmentslot.getIndex(), itemstack1.isEmpty() ? ItemStack.EMPTY : itemstack1.copy());
                    }
                }
            }

            if (this.ticksExisted % 20 == 0)
            {
                this.getCombatTracker().reset();
            }

            if (!this.glowing)
            {
                boolean flag = this.isPotionActive(MobEffects.GLOWING);

                if (this.getFlag(6) != flag)
                {
                    this.setFlag(6, flag);
                }
            }
        }

        this.onLivingUpdate();
        double d0 = this.posX - this.prevPosX;
        double d1 = this.posZ - this.prevPosZ;
        float f3 = (float)(d0 * d0 + d1 * d1);
        float f4 = this.renderYawOffset;
        float f5 = 0.0F;
        this.prevOnGroundSpeedFactor = this.onGroundSpeedFactor;
        float f = 0.0F;

        if (f3 > 0.0025000002F)
        {
            f = 1.0F;
            f5 = (float)Math.sqrt((double)f3) * 3.0F;
            float f1 = (float)MathHelper.atan2(d1, d0) * (180F / (float)Math.PI) - 90.0F;
            float f2 = MathHelper.abs(MathHelper.wrapDegrees(this.rotationYaw) - f1);

            if (95.0F < f2 && f2 < 265.0F)
            {
                f4 = f1 - 180.0F;
            }
            else
            {
                f4 = f1;
            }
        }

        if (this.swingProgress > 0.0F)
        {
            f4 = this.rotationYaw;
        }

        if (!this.onGround)
        {
            f = 0.0F;
        }

        this.onGroundSpeedFactor += (f - this.onGroundSpeedFactor) * 0.3F;
        this.world.profiler.startSection("headTurn");
        f5 = this.updateDistance(f4, f5);
        this.world.profiler.endSection();
        this.world.profiler.startSection("rangeChecks");

        while (this.rotationYaw - this.prevRotationYaw < -180.0F)
        {
            this.prevRotationYaw -= 360.0F;
        }

        while (this.rotationYaw - this.prevRotationYaw >= 180.0F)
        {
            this.prevRotationYaw += 360.0F;
        }

        while (this.renderYawOffset - this.prevRenderYawOffset < -180.0F)
        {
            this.prevRenderYawOffset -= 360.0F;
        }

        while (this.renderYawOffset - this.prevRenderYawOffset >= 180.0F)
        {
            this.prevRenderYawOffset += 360.0F;
        }

        while (this.rotationPitch - this.prevRotationPitch < -180.0F)
        {
            this.prevRotationPitch -= 360.0F;
        }

        while (this.rotationPitch - this.prevRotationPitch >= 180.0F)
        {
            this.prevRotationPitch += 360.0F;
        }

        while (this.rotationYawHead - this.prevRotationYawHead < -180.0F)
        {
            this.prevRotationYawHead -= 360.0F;
        }

        while (this.rotationYawHead - this.prevRotationYawHead >= 180.0F)
        {
            this.prevRotationYawHead += 360.0F;
        }

        this.world.profiler.endSection();
        this.movedDistance += f5;

        if (this.isElytraFlying())
        {
            ++this.ticksElytraFlying;
        }
        else
        {
            this.ticksElytraFlying = 0;
        }
    }

    protected float updateDistance(float p_110146_1_, float p_110146_2_)
    {
        float f = MathHelper.wrapDegrees(p_110146_1_ - this.renderYawOffset);
        this.renderYawOffset += f * 0.3F;
        float f1 = MathHelper.wrapDegrees(this.rotationYaw - this.renderYawOffset);
        boolean flag = f1 < -90.0F || f1 >= 90.0F;

        if (f1 < -75.0F)
        {
            f1 = -75.0F;
        }

        if (f1 >= 75.0F)
        {
            f1 = 75.0F;
        }

        this.renderYawOffset = this.rotationYaw - f1;

        if (f1 * f1 > 2500.0F)
        {
            this.renderYawOffset += f1 * 0.2F;
        }

        if (flag)
        {
            p_110146_2_ *= -1.0F;
        }

        return p_110146_2_;
    }

    public void onLivingUpdate()
    {
        if (this.jumpTicks > 0)
        {
            --this.jumpTicks;
        }

        if (this.newPosRotationIncrements > 0 && !this.canPassengerSteer())
        {
            double d0 = this.posX + (this.interpTargetX - this.posX) / (double)this.newPosRotationIncrements;
            double d1 = this.posY + (this.interpTargetY - this.posY) / (double)this.newPosRotationIncrements;
            double d2 = this.posZ + (this.interpTargetZ - this.posZ) / (double)this.newPosRotationIncrements;
            double d3 = MathHelper.wrapDegrees(this.interpTargetYaw - (double)this.rotationYaw);
            this.rotationYaw = (float)((double)this.rotationYaw + d3 / (double)this.newPosRotationIncrements);
            this.rotationPitch = (float)((double)this.rotationPitch + (this.interpTargetPitch - (double)this.rotationPitch) / (double)this.newPosRotationIncrements);
            --this.newPosRotationIncrements;
            this.setPosition(d0, d1, d2);
            this.setRotation(this.rotationYaw, this.rotationPitch);
        }
        else if (!this.isServerWorld())
        {
            this.motionX *= 0.98D;
            this.motionY *= 0.98D;
            this.motionZ *= 0.98D;
        }

        if (Math.abs(this.motionX) < 0.003D)
        {
            this.motionX = 0.0D;
        }

        if (Math.abs(this.motionY) < 0.003D)
        {
            this.motionY = 0.0D;
        }

        if (Math.abs(this.motionZ) < 0.003D)
        {
            this.motionZ = 0.0D;
        }

        this.world.profiler.startSection("ai");

        if (this.isMovementBlocked())
        {
            this.isJumping = false;
            this.moveStrafing = 0.0F;
            this.moveForward = 0.0F;
            this.randomYawVelocity = 0.0F;
        }
        else if (this.isServerWorld())
        {
            this.world.profiler.startSection("newAi");
            this.updateEntityActionState();
            this.world.profiler.endSection();
        }

        this.world.profiler.endSection();
        this.world.profiler.startSection("jump");

        if (this.isJumping)
        {
            if (this.isInWater())
            {
                this.handleJumpWater();
            }
            else if (this.isInLava())
            {
                this.handleJumpLava();
            }
            else if (this.onGround && this.jumpTicks == 0)
            {
                this.jump();
                this.jumpTicks = 10;
            }
        }
        else
        {
            this.jumpTicks = 0;
        }

        this.world.profiler.endSection();
        this.world.profiler.startSection("travel");
        this.moveStrafing *= 0.98F;
        this.moveForward *= 0.98F;
        this.randomYawVelocity *= 0.9F;
        this.updateElytra();
        this.travel(this.moveStrafing, this.moveVertical, this.moveForward);
        this.world.profiler.endSection();
        this.world.profiler.startSection("push");
        this.collideWithNearbyEntities();
        this.world.profiler.endSection();
    }

    private void updateElytra()
    {
        boolean flag = this.getFlag(7);

        if (flag && !this.onGround && !this.isRiding())
        {
            ItemStack itemstack = this.getItemStackFromSlot(EntityEquipmentSlot.CHEST);

            if (itemstack.getItem() == Items.ELYTRA && ItemElytra.isUsable(itemstack))
            {
                flag = true;

                if (!this.world.isRemote && (this.ticksElytraFlying + 1) % 20 == 0)
                {
                    itemstack.damageItem(1, this);
                }
            }
            else
            {
                flag = false;
            }
        }
        else
        {
            flag = false;
        }

        if (!this.world.isRemote)
        {
            this.setFlag(7, flag);
        }
    }

    protected void updateEntityActionState()
    {
    }

    protected void collideWithNearbyEntities()
    {
        List<Entity> list = this.world.getEntitiesInAABBexcluding(this, this.getEntityBoundingBox(), EntitySelectors.getTeamCollisionPredicate(this));

        if (!list.isEmpty())
        {
            int i = this.world.getGameRules().getInt("maxEntityCramming");

            if (i > 0 && list.size() > i - 1 && this.rand.nextInt(4) == 0)
            {
                int j = 0;

                for (int k = 0; k < list.size(); ++k)
                {
                    if (!((Entity)list.get(k)).isRiding())
                    {
                        ++j;
                    }
                }

                if (j > i - 1)
                {
                    this.attackEntityFrom(DamageSource.CRAMMING, 6.0F);
                }
            }

            for (int l = 0; l < list.size(); ++l)
            {
                Entity entity = list.get(l);
                this.collideWithEntity(entity);
            }
        }
    }

    protected void collideWithEntity(Entity entityIn)
    {
        entityIn.applyEntityCollision(this);
    }

    public void dismountRidingEntity()
    {
        Entity entity = this.getRidingEntity();
        super.dismountRidingEntity();

        if (entity != null && entity != this.getRidingEntity() && !this.world.isRemote)
        {
            this.dismountEntity(entity);
        }
    }

    public void updateRidden()
    {
        super.updateRidden();
        this.prevOnGroundSpeedFactor = this.onGroundSpeedFactor;
        this.onGroundSpeedFactor = 0.0F;
        this.fallDistance = 0.0F;
    }

    @SideOnly(Side.CLIENT)
    public void setPositionAndRotationDirect(double x, double y, double z, float yaw, float pitch, int posRotationIncrements, boolean teleport)
    {
        this.interpTargetX = x;
        this.interpTargetY = y;
        this.interpTargetZ = z;
        this.interpTargetYaw = (double)yaw;
        this.interpTargetPitch = (double)pitch;
        this.newPosRotationIncrements = posRotationIncrements;
    }

    public void setJumping(boolean jumping)
    {
        this.isJumping = jumping;
    }

    public void onItemPickup(Entity entityIn, int quantity)
    {
        if (!entityIn.isDead && !this.world.isRemote)
        {
            EntityTracker entitytracker = ((WorldServer)this.world).getEntityTracker();

            if (entityIn instanceof EntityItem || entityIn instanceof EntityArrow || entityIn instanceof EntityXPOrb)
            {
                entitytracker.sendToTracking(entityIn, new SPacketCollectItem(entityIn.getEntityId(), this.getEntityId(), quantity));
            }
        }
    }

    public boolean canEntityBeSeen(Entity entityIn)
    {
        return this.world.rayTraceBlocks(new Vec3d(this.posX, this.posY + (double)this.getEyeHeight(), this.posZ), new Vec3d(entityIn.posX, entityIn.posY + (double)entityIn.getEyeHeight(), entityIn.posZ), false, true, false) == null;
    }

    public Vec3d getLook(float partialTicks)
    {
        if (partialTicks == 1.0F)
        {
            return this.getVectorForRotation(this.rotationPitch, this.rotationYawHead);
        }
        else
        {
            float f = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * partialTicks;
            float f1 = this.prevRotationYawHead + (this.rotationYawHead - this.prevRotationYawHead) * partialTicks;
            return this.getVectorForRotation(f, f1);
        }
    }

    @SideOnly(Side.CLIENT)
    public float getSwingProgress(float partialTickTime)
    {
        float f = this.swingProgress - this.prevSwingProgress;

        if (f < 0.0F)
        {
            ++f;
        }

        return this.prevSwingProgress + f * partialTickTime;
    }

    public boolean isServerWorld()
    {
        return !this.world.isRemote;
    }

    public boolean canBeCollidedWith()
    {
        return !this.isDead;
    }

    public boolean canBePushed()
    {
        return this.isEntityAlive() && !this.isOnLadder();
    }

    protected void markVelocityChanged()
    {
        this.velocityChanged = this.rand.nextDouble() >= this.getEntityAttribute(SharedMonsterAttributes.KNOCKBACK_RESISTANCE).getAttributeValue();
    }

    public float getRotationYawHead()
    {
        return this.rotationYawHead;
    }

    public void setRotationYawHead(float rotation)
    {
        this.rotationYawHead = rotation;
    }

    public void setRenderYawOffset(float offset)
    {
        this.renderYawOffset = offset;
    }

    public float getAbsorptionAmount()
    {
        return this.absorptionAmount;
    }

    public void setAbsorptionAmount(float amount)
    {
        if (amount < 0.0F)
        {
            amount = 0.0F;
        }

        this.absorptionAmount = amount;
    }

    public void sendEnterCombat()
    {
    }

    public void sendEndCombat()
    {
    }

    protected void markPotionsDirty()
    {
        this.potionsNeedUpdate = true;
    }

    /***
     * Removes all potion effects that have curativeItem as a curative item for its effect
     * @param curativeItem The itemstack we are using to cure potion effects
     */
    public void curePotionEffects(ItemStack curativeItem)
    {
        if (world.isRemote) return;
        Iterator<PotionEffect> iterator = this.activePotionsMap.values().iterator();

        while (iterator.hasNext())
        {
            PotionEffect effect = iterator.next();

            if (effect.isCurativeItem(curativeItem) && !net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.entity.living.PotionEvent.PotionRemoveEvent(this, effect)))
            {
                onFinishedPotionEffect(effect);
                iterator.remove();
                this.potionsNeedUpdate = true;
            }
        }
    }

    /**
     * Returns true if the entity's rider (EntityPlayer) should face forward when mounted.
     * currently only used in vanilla code by pigs.
     *
     * @param player The player who is riding the entity.
     * @return If the player should orient the same direction as this entity.
     */
    public boolean shouldRiderFaceForward(EntityPlayer player)
    {
        return this instanceof net.minecraft.entity.passive.EntityPig;
    }

    public abstract EnumHandSide getPrimaryHand();

    public boolean isHandActive()
    {
        return (((Byte)this.dataManager.get(HAND_STATES)).byteValue() & 1) > 0;
    }

    public EnumHand getActiveHand()
    {
        return (((Byte)this.dataManager.get(HAND_STATES)).byteValue() & 2) > 0 ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
    }

    protected void updateActiveHand()
    {
        if (this.isHandActive())
        {
            ItemStack itemstack = this.getHeldItem(this.getActiveHand());
            if (net.minecraftforge.common.ForgeHooks.canContinueUsing(this.activeItemStack, itemstack)) this.activeItemStack = itemstack;

            if (itemstack == this.activeItemStack)
            {
                if (!this.activeItemStack.isEmpty())
                {
                    activeItemStackUseCount = net.minecraftforge.event.ForgeEventFactory.onItemUseTick(this, activeItemStack, activeItemStackUseCount);
                    if (activeItemStackUseCount > 0)
                        activeItemStack.getItem().onUsingTick(activeItemStack, this, activeItemStackUseCount);
                }

                if (this.getItemInUseCount() <= 25 && this.getItemInUseCount() % 4 == 0)
                {
                    this.updateItemUse(this.activeItemStack, 5);
                }

                if (--this.activeItemStackUseCount <= 0 && !this.world.isRemote)
                {
                    this.onItemUseFinish();
                }
            }
            else
            {
                this.resetActiveHand();
            }
        }
    }

    public void setActiveHand(EnumHand hand)
    {
        ItemStack itemstack = this.getHeldItem(hand);

        if (!itemstack.isEmpty() && !this.isHandActive())
        {
            int duration = net.minecraftforge.event.ForgeEventFactory.onItemUseStart(this, itemstack, itemstack.getMaxItemUseDuration());
            if (duration <= 0) return;
            this.activeItemStack = itemstack;
            this.activeItemStackUseCount = duration;

            if (!this.world.isRemote)
            {
                int i = 1;

                if (hand == EnumHand.OFF_HAND)
                {
                    i |= 2;
                }

                this.dataManager.set(HAND_STATES, Byte.valueOf((byte)i));
            }
        }
    }

    public void notifyDataManagerChange(DataParameter<?> key)
    {
        super.notifyDataManagerChange(key);

        if (HAND_STATES.equals(key) && this.world.isRemote)
        {
            if (this.isHandActive() && this.activeItemStack.isEmpty())
            {
                this.activeItemStack = this.getHeldItem(this.getActiveHand());

                if (!this.activeItemStack.isEmpty())
                {
                    this.activeItemStackUseCount = this.activeItemStack.getMaxItemUseDuration();
                }
            }
            else if (!this.isHandActive() && !this.activeItemStack.isEmpty())
            {
                this.activeItemStack = ItemStack.EMPTY;
                this.activeItemStackUseCount = 0;
            }
        }
    }

    protected void updateItemUse(ItemStack stack, int eatingParticleCount)
    {
        if (!stack.isEmpty() && this.isHandActive())
        {
            if (stack.getItemUseAction() == EnumAction.DRINK)
            {
                this.playSound(SoundEvents.ENTITY_GENERIC_DRINK, 0.5F, this.world.rand.nextFloat() * 0.1F + 0.9F);
            }

            if (stack.getItemUseAction() == EnumAction.EAT)
            {
                for (int i = 0; i < eatingParticleCount; ++i)
                {
                    Vec3d vec3d = new Vec3d(((double)this.rand.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, 0.0D);
                    vec3d = vec3d.rotatePitch(-this.rotationPitch * 0.017453292F);
                    vec3d = vec3d.rotateYaw(-this.rotationYaw * 0.017453292F);
                    double d0 = (double)(-this.rand.nextFloat()) * 0.6D - 0.3D;
                    Vec3d vec3d1 = new Vec3d(((double)this.rand.nextFloat() - 0.5D) * 0.3D, d0, 0.6D);
                    vec3d1 = vec3d1.rotatePitch(-this.rotationPitch * 0.017453292F);
                    vec3d1 = vec3d1.rotateYaw(-this.rotationYaw * 0.017453292F);
                    vec3d1 = vec3d1.addVector(this.posX, this.posY + (double)this.getEyeHeight(), this.posZ);

                    if (stack.getHasSubtypes())
                    {
                        this.world.spawnParticle(EnumParticleTypes.ITEM_CRACK, vec3d1.x, vec3d1.y, vec3d1.z, vec3d.x, vec3d.y + 0.05D, vec3d.z, Item.getIdFromItem(stack.getItem()), stack.getMetadata());
                    }
                    else
                    {
                        this.world.spawnParticle(EnumParticleTypes.ITEM_CRACK, vec3d1.x, vec3d1.y, vec3d1.z, vec3d.x, vec3d.y + 0.05D, vec3d.z, Item.getIdFromItem(stack.getItem()));
                    }
                }

                this.playSound(SoundEvents.ENTITY_GENERIC_EAT, 0.5F + 0.5F * (float)this.rand.nextInt(2), (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + 1.0F);
            }
        }
    }

    protected void onItemUseFinish()
    {
        if (!this.activeItemStack.isEmpty() && this.isHandActive())
        {
            this.updateItemUse(this.activeItemStack, 16);
            ItemStack activeItemStackCopy = this.activeItemStack.copy();
            ItemStack itemstack = this.activeItemStack.onItemUseFinish(this.world, this);
            itemstack = net.minecraftforge.event.ForgeEventFactory.onItemUseFinish(this, activeItemStackCopy, getItemInUseCount(), itemstack);
            this.setHeldItem(this.getActiveHand(), itemstack);
            this.resetActiveHand();
        }
    }

    public ItemStack getActiveItemStack()
    {
        return this.activeItemStack;
    }

    public int getItemInUseCount()
    {
        return this.activeItemStackUseCount;
    }

    public int getItemInUseMaxCount()
    {
        return this.isHandActive() ? this.activeItemStack.getMaxItemUseDuration() - this.getItemInUseCount() : 0;
    }

    public void stopActiveHand()
    {
        if (!this.activeItemStack.isEmpty())
        {
            if (!net.minecraftforge.event.ForgeEventFactory.onUseItemStop(this, activeItemStack, this.getItemInUseCount()))
                this.activeItemStack.onPlayerStoppedUsing(this.world, this, this.getItemInUseCount());
        }

        this.resetActiveHand();
    }

    public void resetActiveHand()
    {
        if (!this.world.isRemote)
        {
            this.dataManager.set(HAND_STATES, Byte.valueOf((byte)0));
        }

        this.activeItemStack = ItemStack.EMPTY;
        this.activeItemStackUseCount = 0;
    }

    public boolean isActiveItemStackBlocking()
    {
        if (this.isHandActive() && !this.activeItemStack.isEmpty())
        {
            Item item = this.activeItemStack.getItem();

            if (item.getItemUseAction(this.activeItemStack) != EnumAction.BLOCK)
            {
                return false;
            }
            else
            {
                return item.getMaxItemUseDuration(this.activeItemStack) - this.activeItemStackUseCount >= 5;
            }
        }
        else
        {
            return false;
        }
    }

    public boolean isElytraFlying()
    {
        return this.getFlag(7);
    }

    @SideOnly(Side.CLIENT)
    public int getTicksElytraFlying()
    {
        return this.ticksElytraFlying;
    }

    public boolean attemptTeleport(double x, double y, double z)
    {
        double d0 = this.posX;
        double d1 = this.posY;
        double d2 = this.posZ;
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        boolean flag = false;
        BlockPos blockpos = new BlockPos(this);
        World world = this.world;
        Random random = this.getRNG();

        if (world.isBlockLoaded(blockpos))
        {
            boolean flag1 = false;

            while (!flag1 && blockpos.getY() > 0)
            {
                BlockPos blockpos1 = blockpos.down();
                IBlockState iblockstate = world.getBlockState(blockpos1);

                if (iblockstate.getMaterial().blocksMovement())
                {
                    flag1 = true;
                }
                else
                {
                    --this.posY;
                    blockpos = blockpos1;
                }
            }

            if (flag1)
            {
                this.setPositionAndUpdate(this.posX, this.posY, this.posZ);

                if (world.getCollisionBoxes(this, this.getEntityBoundingBox()).isEmpty() && !world.containsAnyLiquid(this.getEntityBoundingBox()))
                {
                    flag = true;
                }
            }
        }

        if (!flag)
        {
            this.setPositionAndUpdate(d0, d1, d2);
            return false;
        }
        else
        {
            int i = 128;

            for (int j = 0; j < 128; ++j)
            {
                double d6 = (double)j / 127.0D;
                float f = (random.nextFloat() - 0.5F) * 0.2F;
                float f1 = (random.nextFloat() - 0.5F) * 0.2F;
                float f2 = (random.nextFloat() - 0.5F) * 0.2F;
                double d3 = d0 + (this.posX - d0) * d6 + (random.nextDouble() - 0.5D) * (double)this.width * 2.0D;
                double d4 = d1 + (this.posY - d1) * d6 + random.nextDouble() * (double)this.height;
                double d5 = d2 + (this.posZ - d2) * d6 + (random.nextDouble() - 0.5D) * (double)this.width * 2.0D;
                world.spawnParticle(EnumParticleTypes.PORTAL, d3, d4, d5, (double)f, (double)f1, (double)f2);
            }

            if (this instanceof EntityCreature)
            {
                ((EntityCreature)this).getNavigator().clearPath();
            }

            return true;
        }
    }

    public boolean canBeHitWithPotion()
    {
        return true;
    }

    // FORGE
    private final net.minecraftforge.items.IItemHandlerModifiable handHandler = new net.minecraftforge.items.wrapper.EntityHandsInvWrapper(this);
    private final net.minecraftforge.items.IItemHandlerModifiable armorHandler = new net.minecraftforge.items.wrapper.EntityArmorInvWrapper(this);
    private final net.minecraftforge.items.IItemHandler joinedHandler = new net.minecraftforge.items.wrapper.CombinedInvWrapper(armorHandler, handHandler);

    @SuppressWarnings("unchecked")
    @Override
    @Nullable
    public <T> T getCapability(net.minecraftforge.common.capabilities.Capability<T> capability, @Nullable net.minecraft.util.EnumFacing facing)
    {
        if (capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
        {
            if (facing == null) return (T) joinedHandler;
            else if (facing.getAxis().isVertical()) return (T) handHandler;
            else if (facing.getAxis().isHorizontal()) return (T) armorHandler;
        }
        return super.getCapability(capability, facing);
    }

    @Override
    public boolean hasCapability(net.minecraftforge.common.capabilities.Capability<?> capability, @Nullable net.minecraft.util.EnumFacing facing)
    {
        return capability == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    public boolean attackable()
    {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public void setPartying(BlockPos pos, boolean p_191987_2_)
    {
    }
    
    @Override
    public void moveRelative(float strafe, float up, float forward, float friction)
    {
        float f = strafe * strafe + up * up + forward * forward;
        if (f >= 1.0E-4F)
        {
            f = MathHelper.sqrt(f);
            if (f < 1.0F) f = 1.0F;
            f = friction / f;
            strafe = strafe * f;
            up = up * f;
            forward = forward * f;
            if(this.isInWater() || this.isInLava())
            {
                strafe = strafe * (float)this.getEntityAttribute(SWIM_SPEED).getAttributeValue();
                up = up * (float)this.getEntityAttribute(SWIM_SPEED).getAttributeValue();
                forward = forward * (float)this.getEntityAttribute(SWIM_SPEED).getAttributeValue();
            }
            float f1 = MathHelper.sin(this.rotationYaw * 0.017453292F);
            float f2 = MathHelper.cos(this.rotationYaw * 0.017453292F);
            this.motionX += (double)(strafe * f2 - forward * f1);
            this.motionY += (double)up;
            this.motionZ += (double)(forward * f2 + strafe * f1);
        }
    }
}