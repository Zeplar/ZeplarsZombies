package com.zeplar.zeplarszombies.Monsters;

import com.zeplar.zeplarszombies.Events.PlayerScentMap;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public class MoveStraightToPlayer extends EntityAIBase
{
    private final EntityCreature creature;
    private BlockPos targetPosition;
    private double movePosX;
    private double movePosY;
    private double movePosZ;
    private final double speed;
    /** If the distance to the target entity is further than this, this AI task will not run. */
    private final float maxTargetDistance;
    private int aliveTime=0;

    public MoveStraightToPlayer(EntityCreature creature, double speedIn, float targetMaxDistance)
    {
        this.creature = creature;
        this.speed = speedIn;
        this.maxTargetDistance = targetMaxDistance;
        this.setMutexBits(7);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (targetPosition == null) {
            targetPosition = PlayerScentMap.getNearestScent(creature.world, creature.getPosition());

        }
    //        creature.setAttackTarget(targetEntity);
    //        this.targetPosition = targetEntity.getPosition();


        if (this.creature.getDistanceSq(this.targetPosition) > (double)(this.maxTargetDistance * this.maxTargetDistance))
        {
            return false;
        }
        if (this.creature.getDistanceSq(this.targetPosition) < 4.0)
        {
            return false;
        }
        else
        {
            movePosX = targetPosition.getX();
            movePosY = targetPosition.getY();
            movePosZ = targetPosition.getZ();
            return true;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinueExecuting()
    {
        return !this.creature.getNavigator().noPath() && this.creature.getDistanceSq(targetPosition) < (double)(this.maxTargetDistance * this.maxTargetDistance);
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void resetTask()
    {
        //this.targetPosition = null;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        Vec3d moveTo = new Vec3d(movePosX,movePosY,movePosZ);
        while (moveTo.squareDistanceTo(creature.posX, creature.posY, creature.posZ) > 64)
        {
            Vec3d moveDirection = moveTo.subtract(creature.getPositionVector());
            moveTo = moveTo.subtract(moveDirection.scale(0.5));

        }
        this.creature.getNavigator().tryMoveToXYZ(moveTo.x, moveTo.y, moveTo.z, this.speed);
        ((EntityCreeper)creature).setCreeperState(1);
    }

    public void updateTask()
    {
        if (this.creature.getPosition().distanceSq(targetPosition) < 4) explode();

        if (this.creature.getRNG().nextInt(2) == 0)
            creature.world.playSound(null, creature.getPosition(), SoundEvents.ENTITY_GHAST_SCREAM, SoundCategory.HOSTILE, 9.0f, 1.0f);
        aliveTime++;
        if (aliveTime > 300) explode();
    }

    private void explode()
    {
        if (!creature.world.isRemote)
        {
            float explosionRadius = 0.25f;
            creature.setDead();
            creature.world.createExplosion(creature, creature.posX, creature.posY, creature.posZ, explosionRadius, true);
        }
    }


}