package com.zeplar.zeplarszombies.Monsters;

import com.zeplar.zeplarszombies.BlockHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;

public class EntityAIBreakBlock extends EntityAIBase
{
    protected EntityLiving entity;
    protected BlockPos blockPos = null;
    protected double playerRange = 10; //How close entity must be to its target to start tunneling
    protected int breakTime = 20; //How many ticks on average to break a block

    protected BlockPos targetPosition = null;   //AI will target this instead of a player
    private BlockPos target;

    public EntityAIBreakBlock(EntityLiving entityIn, int breakTimeIn, double playerRangeIn)
    {
        entity = entityIn;
        setMutexBits(8);
        this.breakTime = Math.max(1,breakTimeIn);
        this.playerRange = playerRangeIn;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */

    @Nullable
    protected BlockPos tryBlocksAround(BlockPos center, BlockPos target)
    {
        double d0 = center.distanceSq(target);
        double dCandidate;
        BlockPos bestCandidate = center;
        if (d0 > 100) return null;

        for (BlockPos candidate : BlockHelper.getAround(center)) {
            dCandidate = candidate.distanceSq(target);
            if (dCandidate <= d0) {
                d0 = dCandidate;
                bestCandidate = candidate;
            }
        }
        if (!blockExists(bestCandidate)) return bestCandidate.up();
        return bestCandidate;
    }


    public boolean shouldExecute() {

        if (targetPosition == null)
        {
            EntityPlayer player = entity.world.getClosestPlayerToEntity(entity,playerRange);
            if (player == null) return false;
            else target = player.getPosition();
        }
        else target = targetPosition;
        if (target == null) return false;

        BlockPos candidate = tryBlocksAround(entity.getPosition(), target);
        if (candidate == null) return false;
        target = candidate;
        return true;
    }

    private boolean blockExists(BlockPos pos)
    {
        if (pos == null) return false;
        if (!entity.world.getBlockState(pos).getMaterial().blocksMovement()) return false;

        return true;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {

        super.startExecuting();

        if (blockExists(target))
        {
            blockPos = target;
        }
        else if (blockExists(target.up()))
        {
            blockPos = target.up();
        }
        else if (blockExists(target.down()))
        {
            blockPos = target.down();
        }

    //    entity.getNavigator().tryMoveToXYZ(target.getX(),target.getY(),target.getZ(), 1.0);


    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinueExecuting() {
        if (!blockExists(blockPos)) return false;
        double d0 = entity.getPosition().distanceSq(blockPos.getX(), blockPos.getY(), blockPos.getZ());

        return d0 < 4.0;
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void resetTask()
    {

        super.resetTask();
        blockPos = null;
      //  entity.getNavigator().clearPathEntity();
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void updateTask()
    {
        super.updateTask();
        if (this.entity.getRNG().nextInt(breakTime) == 0)
        {
            if (blockExists(blockPos))
            {
                this.entity.world.playEvent(1021, blockPos, 0);
                this.entity.world.destroyBlock(blockPos, true);
                blockPos = null;
            }
        }
    }
}