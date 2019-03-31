package net.minecraft.entity.ai;

import javax.annotation.Nullable;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;

public abstract class EntityAITarget extends EntityAIBase
{
    protected final EntityCreature taskOwner;
    protected boolean shouldCheckSight;
    private final boolean nearbyOnly;
    private int targetSearchStatus;
    private int targetSearchDelay;
    private int targetUnseenTicks;
    protected EntityLivingBase target;
    protected int unseenMemoryTicks;

    public EntityAITarget(EntityCreature creature, boolean checkSight)
    {
        this(creature, checkSight, false);
    }

    public EntityAITarget(EntityCreature creature, boolean checkSight, boolean onlyNearby)
    {
        this.unseenMemoryTicks = 60;
        this.taskOwner = creature;
        this.shouldCheckSight = checkSight;
        this.nearbyOnly = onlyNearby;
    }

    public boolean shouldContinueExecuting()
    {
        EntityLivingBase entitylivingbase = this.taskOwner.getAttackTarget();

        if (entitylivingbase == null)
        {
            entitylivingbase = this.target;
        }

        if (entitylivingbase == null)
        {
            return false;
        }
        else if (!entitylivingbase.isEntityAlive())
        {
            return false;
        }
        else
        {
            Team team = this.taskOwner.getTeam();
            Team team1 = entitylivingbase.getTeam();

            if (team != null && team1 == team)
            {
                return false;
            }
            else
            {
                double d0 = this.getTargetDistance();

                if (this.taskOwner.getDistanceSq(entitylivingbase) > d0 * d0)
                {
                    return false;
                }
                else
                {
                    if (this.shouldCheckSight)
                    {
                        if (this.taskOwner.getEntitySenses().canSee(entitylivingbase))
                        {
                            this.targetUnseenTicks = 0;
                        }
                        else if (++this.targetUnseenTicks > this.unseenMemoryTicks)
                        {
                            return false;
                        }
                    }

                    if (entitylivingbase instanceof EntityPlayer && ((EntityPlayer)entitylivingbase).capabilities.disableDamage)
                    {
                        return false;
                    }
                    else
                    {
                        this.taskOwner.setAttackTarget(entitylivingbase);
                        return true;
                    }
                }
            }
        }
    }

    protected double getTargetDistance()
    {
        IAttributeInstance iattributeinstance = this.taskOwner.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE);
        return iattributeinstance == null ? 16.0D : iattributeinstance.getAttributeValue();
    }

    public void startExecuting()
    {
        this.targetSearchStatus = 0;
        this.targetSearchDelay = 0;
        this.targetUnseenTicks = 0;
    }

    public void resetTask()
    {
        this.taskOwner.setAttackTarget((EntityLivingBase)null);
        this.target = null;
    }

    public static boolean isSuitableTarget(EntityLiving attacker, @Nullable EntityLivingBase target, boolean includeInvincibles, boolean checkSight)
    {
        if (target == null)
        {
            return false;
        }
        else if (target == attacker)
        {
            return false;
        }
        else if (!target.isEntityAlive())
        {
            return false;
        }
        else if (!attacker.canAttackClass(target.getClass()))
        {
            return false;
        }
        else if (attacker.isOnSameTeam(target))
        {
            return false;
        }
        else
        {
            if (attacker instanceof IEntityOwnable && ((IEntityOwnable)attacker).getOwnerId() != null)
            {
                if (target instanceof IEntityOwnable && ((IEntityOwnable)attacker).getOwnerId().equals(((IEntityOwnable)target).getOwnerId()))
                {
                    return false;
                }

                if (target == ((IEntityOwnable)attacker).getOwner())
                {
                    return false;
                }
            }
            else if (target instanceof EntityPlayer && !includeInvincibles && ((EntityPlayer)target).capabilities.disableDamage)
            {
                return false;
            }

            return !checkSight || attacker.getEntitySenses().canSee(target);
        }
    }

    protected boolean isSuitableTarget(@Nullable EntityLivingBase target, boolean includeInvincibles)
    {
        if (!isSuitableTarget(this.taskOwner, target, includeInvincibles, this.shouldCheckSight))
        {
            return false;
        }
        else if (!this.taskOwner.isWithinHomeDistanceFromPosition(new BlockPos(target)))
        {
            return false;
        }
        else
        {
            if (this.nearbyOnly)
            {
                if (--this.targetSearchDelay <= 0)
                {
                    this.targetSearchStatus = 0;
                }

                if (this.targetSearchStatus == 0)
                {
                    this.targetSearchStatus = this.canEasilyReach(target) ? 1 : 2;
                }

                if (this.targetSearchStatus == 2)
                {
                    return false;
                }
            }

            return true;
        }
    }

    private boolean canEasilyReach(EntityLivingBase target)
    {
        this.targetSearchDelay = 10 + this.taskOwner.getRNG().nextInt(5);
        Path path = this.taskOwner.getNavigator().getPathToEntityLiving(target);

        if (path == null)
        {
            return false;
        }
        else
        {
            PathPoint pathpoint = path.getFinalPathPoint();

            if (pathpoint == null)
            {
                return false;
            }
            else
            {
                int i = pathpoint.x - MathHelper.floor(target.posX);
                int j = pathpoint.z - MathHelper.floor(target.posZ);
                return (double)(i * i + j * j) <= 2.25D;
            }
        }
    }

    public EntityAITarget setUnseenMemoryTicks(int p_190882_1_)
    {
        this.unseenMemoryTicks = p_190882_1_;
        return this;
    }
}