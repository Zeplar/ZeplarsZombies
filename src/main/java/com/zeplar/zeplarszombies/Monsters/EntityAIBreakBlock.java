package com.zeplar.zeplarszombies.Monsters;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.BlockPos;

public class EntityAIBreakBlock extends EntityAIBase
{
    private EntityLiving entity;
    private BlockPos blockPosTop = null;
    private BlockPos blockPosBot = null;

    public EntityAIBreakBlock(EntityLiving entityIn)
    {
        entity = entityIn;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        {
           blockPosBot = new BlockPos(
                   entity.posX + entity.getLookVec().x,
                   entity.posY,
                   entity.posZ + entity.getLookVec().z
           );
           blockPosTop = new BlockPos(
                   entity.posX + entity.getLookVec().x,
                   entity.posY + 1,
                   entity.posZ + entity.getLookVec().z
           );

           if (blockExists(blockPosTop) || blockExists(blockPosBot))
           {
               return true;
           }
           return false;

        }
    }

    private boolean blockExists(BlockPos pos)
    {
        if (pos == null) return false;
        if (entity.world.isAirBlock(pos)) return false;
        else return entity.world.getBlockState(pos).getBlock() != null;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        super.startExecuting();
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinueExecuting() {
        boolean topFlag = blockExists(blockPosTop);
        boolean botFlag = blockExists(blockPosBot);
        if (blockPosBot == null) return false;
        double d0 = entity.getPosition().distanceSq(blockPosBot.getX(), blockPosBot.getY(), blockPosBot.getZ());

        return (topFlag || botFlag) && (d0 < 4.0);

    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void resetTask()
    {
        blockPosTop = null;
        blockPosBot = null;
        super.resetTask();
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void updateTask()
    {
        super.updateTask();
        entity.getLookHelper().setLookPosition(blockPosTop.getX(), blockPosTop.getY(), blockPosTop.getZ(), entity.getHorizontalFaceSpeed(), entity.getVerticalFaceSpeed());
        if (this.entity.getRNG().nextInt(20) == 0)
        {
            this.entity.world.playEvent(1021, this.blockPosTop, 0);
            if (blockExists(blockPosTop))
            {
                this.entity.world.setBlockToAir(blockPosTop);
                blockPosTop = null;
            }
            if (blockExists(blockPosBot)) {
                this.entity.world.setBlockToAir(blockPosBot);
                blockPosBot = null;
            }
        }
    }
}