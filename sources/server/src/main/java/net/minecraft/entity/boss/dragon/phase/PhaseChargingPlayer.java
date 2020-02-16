package net.minecraft.entity.boss.dragon.phase;

import javax.annotation.Nullable;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.util.math.Vec3d;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class PhaseChargingPlayer extends PhaseBase
{
    private static final Logger LOGGER = LogManager.getLogger();
    private Vec3d targetLocation;
    private int timeSinceCharge;

    public PhaseChargingPlayer(EntityDragon dragonIn)
    {
        super(dragonIn);
    }

    public void doLocalUpdate()
    {
        if (this.targetLocation == null)
        {
            LOGGER.warn("Aborting charge player as no target was set.");
            this.dragon.getPhaseManager().setPhase(PhaseList.HOLDING_PATTERN);
        }
        else if (this.timeSinceCharge > 0 && this.timeSinceCharge++ >= 10)
        {
            this.dragon.getPhaseManager().setPhase(PhaseList.HOLDING_PATTERN);
        }
        else
        {
            double d0 = this.targetLocation.squareDistanceTo(this.dragon.posX, this.dragon.posY, this.dragon.posZ);

            if (d0 < 100.0D || d0 > 22500.0D || this.dragon.collidedHorizontally || this.dragon.collidedVertically)
            {
                ++this.timeSinceCharge;
            }
        }
    }

    public void initPhase()
    {
        this.targetLocation = null;
        this.timeSinceCharge = 0;
    }

    public void setTarget(Vec3d p_188668_1_)
    {
        this.targetLocation = p_188668_1_;
    }

    public float getMaxRiseOrFall()
    {
        return 3.0F;
    }

    @Nullable
    public Vec3d getTargetLocation()
    {
        return this.targetLocation;
    }

    public PhaseList<PhaseChargingPlayer> getType()
    {
        return PhaseList.CHARGING_PLAYER;
    }
}