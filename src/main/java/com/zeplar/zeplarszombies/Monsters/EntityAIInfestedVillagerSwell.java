package com.zeplar.zeplarszombies.Monsters;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAIInfestedVillagerSwell extends EntityAIBase {

    /** The creeper that is swelling. */
    EntityInfestedVillager swellingVillager;
    /** The creeper's attack target. This is used for the changing of the creeper's state. */
    EntityLivingBase villagerAttackTarget;

    public EntityAIInfestedVillagerSwell(EntityInfestedVillager entityVillagerIn)
    {
        this.swellingVillager = entityVillagerIn;
        this.setMutexBits(0);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        EntityLivingBase entitylivingbase = this.swellingVillager.getAttackTarget();
        return this.swellingVillager.getInfestedVillagerState() > 0 || entitylivingbase != null && this.swellingVillager.getDistanceSqToEntity(entitylivingbase) < 9.0D;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.swellingVillager.getNavigator().clearPathEntity();
        this.villagerAttackTarget = this.swellingVillager.getAttackTarget();
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void resetTask()
    {
        this.villagerAttackTarget = null;
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void updateTask()
    {
        if (this.villagerAttackTarget == null)
        {
            this.swellingVillager.setInfestedVillagerState(-1);
        }
        else if (this.swellingVillager.getDistanceSqToEntity(this.villagerAttackTarget) > 49.0D)
        {
            this.swellingVillager.setInfestedVillagerState(-1);
        }
        else if (!this.swellingVillager.getEntitySenses().canSee(this.villagerAttackTarget))
        {
            this.swellingVillager.setInfestedVillagerState(-1);
        }
        else
        {
            this.swellingVillager.setInfestedVillagerState(1);
        }
    }
}
