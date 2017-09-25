package com.zeplar.zeplarszombies.Monsters;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;

public class EntityAITunnelToPlayer extends EntityAIBreakBlock
{
    private float moveSpeed;
    public EntityAITunnelToPlayer(EntityLiving entityIn) {
        super(entityIn);
        breakTime = 10;
        moveSpeed = entity.getAIMoveSpeed() * 4;
    }

    @Override
    public boolean shouldExecute()
    {
        if (entity.world.getClosestPlayerToEntity(entity, playerRange) != null)
        {
            return true;
        }
        return false;
    }

    @Override
    public void startExecuting()
    {
        EntityPlayer player = entity.world.getClosestPlayerToEntity(entity, playerRange);
        targetPosition = player.getPosition();
        entity.setAttackTarget(player);
        entity.setAIMoveSpeed(moveSpeed);
        breakTime = 5;
        super.startExecuting();
    }

   @Override
    public boolean shouldContinueExecuting()
   {
       if (entity.getDistanceSq(targetPosition) > 1)
       {
           return super.shouldContinueExecuting();
       }
       else
       {
           entity.setAIMoveSpeed(moveSpeed / 20);
           breakTime = 100;
           return false;
       }
   }

}