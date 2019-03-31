package net.minecraft.entity.ai;

import com.google.common.collect.Sets;
import java.util.Iterator;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.profiler.Profiler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EntityAITasks
{
    private static final Logger LOGGER = LogManager.getLogger();
    public final Set<EntityAITasks.EntityAITaskEntry> taskEntries = Sets.<EntityAITasks.EntityAITaskEntry>newLinkedHashSet();
    private final Set<EntityAITasks.EntityAITaskEntry> executingTaskEntries = Sets.<EntityAITasks.EntityAITaskEntry>newLinkedHashSet();
    private final Profiler profiler;
    private int tickCount;
    private int tickRate = 3;
    private int disabledControlFlags;

    public EntityAITasks(Profiler profilerIn)
    {
        this.profiler = profilerIn;
    }

    public void addTask(int priority, EntityAIBase task)
    {
        this.taskEntries.add(new EntityAITasks.EntityAITaskEntry(priority, task));
    }

    public void removeTask(EntityAIBase task)
    {
        Iterator<EntityAITasks.EntityAITaskEntry> iterator = this.taskEntries.iterator();

        while (iterator.hasNext())
        {
            EntityAITasks.EntityAITaskEntry entityaitasks$entityaitaskentry = iterator.next();
            EntityAIBase entityaibase = entityaitasks$entityaitaskentry.action;

            if (entityaibase == task)
            {
                if (entityaitasks$entityaitaskentry.using)
                {
                    entityaitasks$entityaitaskentry.using = false;
                    entityaitasks$entityaitaskentry.action.resetTask();
                    this.executingTaskEntries.remove(entityaitasks$entityaitaskentry);
                }

                iterator.remove();
                return;
            }
        }
    }

    public void onUpdateTasks()
    {
        this.profiler.startSection("goalSetup");

        if (this.tickCount++ % this.tickRate == 0)
        {
            for (EntityAITasks.EntityAITaskEntry entityaitasks$entityaitaskentry : this.taskEntries)
            {
                if (entityaitasks$entityaitaskentry.using)
                {
                    if (!this.canUse(entityaitasks$entityaitaskentry) || !this.canContinue(entityaitasks$entityaitaskentry))
                    {
                        entityaitasks$entityaitaskentry.using = false;
                        entityaitasks$entityaitaskentry.action.resetTask();
                        this.executingTaskEntries.remove(entityaitasks$entityaitaskentry);
                    }
                }
                else if (this.canUse(entityaitasks$entityaitaskentry) && entityaitasks$entityaitaskentry.action.shouldExecute())
                {
                    entityaitasks$entityaitaskentry.using = true;
                    entityaitasks$entityaitaskentry.action.startExecuting();
                    this.executingTaskEntries.add(entityaitasks$entityaitaskentry);
                }
            }
        }
        else
        {
            Iterator<EntityAITasks.EntityAITaskEntry> iterator = this.executingTaskEntries.iterator();

            while (iterator.hasNext())
            {
                EntityAITasks.EntityAITaskEntry entityaitasks$entityaitaskentry1 = iterator.next();

                if (!this.canContinue(entityaitasks$entityaitaskentry1))
                {
                    entityaitasks$entityaitaskentry1.using = false;
                    entityaitasks$entityaitaskentry1.action.resetTask();
                    iterator.remove();
                }
            }
        }

        this.profiler.endSection();

        if (!this.executingTaskEntries.isEmpty())
        {
            this.profiler.startSection("goalTick");

            for (EntityAITasks.EntityAITaskEntry entityaitasks$entityaitaskentry2 : this.executingTaskEntries)
            {
                entityaitasks$entityaitaskentry2.action.updateTask();
            }

            this.profiler.endSection();
        }
    }

    private boolean canContinue(EntityAITasks.EntityAITaskEntry taskEntry)
    {
        return taskEntry.action.shouldContinueExecuting();
    }

    private boolean canUse(EntityAITasks.EntityAITaskEntry taskEntry)
    {
        if (this.executingTaskEntries.isEmpty())
        {
            return true;
        }
        else if (this.isControlFlagDisabled(taskEntry.action.getMutexBits()))
        {
            return false;
        }
        else
        {
            for (EntityAITasks.EntityAITaskEntry entityaitasks$entityaitaskentry : this.executingTaskEntries)
            {
                if (entityaitasks$entityaitaskentry != taskEntry)
                {
                    if (taskEntry.priority >= entityaitasks$entityaitaskentry.priority)
                    {
                        if (!this.areTasksCompatible(taskEntry, entityaitasks$entityaitaskentry))
                        {
                            return false;
                        }
                    }
                    else if (!entityaitasks$entityaitaskentry.action.isInterruptible())
                    {
                        return false;
                    }
                }
            }

            return true;
        }
    }

    private boolean areTasksCompatible(EntityAITasks.EntityAITaskEntry taskEntry1, EntityAITasks.EntityAITaskEntry taskEntry2)
    {
        return (taskEntry1.action.getMutexBits() & taskEntry2.action.getMutexBits()) == 0;
    }

    public boolean isControlFlagDisabled(int p_188528_1_)
    {
        return (this.disabledControlFlags & p_188528_1_) > 0;
    }

    public void disableControlFlag(int p_188526_1_)
    {
        this.disabledControlFlags |= p_188526_1_;
    }

    public void enableControlFlag(int p_188525_1_)
    {
        this.disabledControlFlags &= ~p_188525_1_;
    }

    public void setControlFlag(int p_188527_1_, boolean p_188527_2_)
    {
        if (p_188527_2_)
        {
            this.enableControlFlag(p_188527_1_);
        }
        else
        {
            this.disableControlFlag(p_188527_1_);
        }
    }

    public class EntityAITaskEntry
    {
        public final EntityAIBase action;
        public final int priority;
        public boolean using;

        public EntityAITaskEntry(int priorityIn, EntityAIBase task)
        {
            this.priority = priorityIn;
            this.action = task;
        }

        public boolean equals(@Nullable Object p_equals_1_)
        {
            if (this == p_equals_1_)
            {
                return true;
            }
            else
            {
                return p_equals_1_ != null && this.getClass() == p_equals_1_.getClass() ? this.action.equals(((EntityAITasks.EntityAITaskEntry)p_equals_1_).action) : false;
            }
        }

        public int hashCode()
        {
            return this.action.hashCode();
        }
    }
}