package com.zeplar.zeplarszombies.Monsters;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.math.BlockPos;

/**
 * Causes entity to attack blocks
 */
public class BreakBlockAI extends EntityAIBase {

    protected EntityLiving entity;
    protected Block block;
    protected BlockPos blockPos;
    protected int damageDivider;
    private int damageDealt;

    public BreakBlockAI(EntityLiving entityIn)
        {
            this.entity = entityIn;
            this.damageDivider = 20;

            if (!(entityIn.getNavigator() instanceof PathNavigateGround))
            {
                throw new IllegalArgumentException("Unsupported mob type for DoorInteractGoal");
            }
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        public boolean shouldExecute()
        {
            if (!this.entity.isCollidedHorizontally)
            {
                return false;
            }
            else if (!this.entity.world.getGameRules().getBoolean("mobGriefing") ||
                    !net.minecraftforge.event.ForgeEventFactory.onEntityDestroyBlock(this.entity, this.blockPos, this.entity.world.getBlockState(this.blockPos)))
            {
                return false;
            }
            else
            {
                return true;
            }
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
        public boolean shouldContinueExecuting()
        {
            double d0 = this.entity.getDistanceSq(this.blockPos);

            return d0 < 4.0;
        }

        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        public void resetTask()
        {
            super.resetTask();
        }

        /**
         * Keep ticking a continuous task that has already been started
         */
        public void updateTask()
        {
            super.updateTask();

            ++this.damageDealt;

            if (this.damageDealt > this.damageDivider)
            {
                //DurabilityBlock.decreaseDurability(this.entity.world, blockPos);
                this.damageDealt = 0;
            }
        }
}
