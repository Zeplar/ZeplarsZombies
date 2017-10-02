package com.zeplar.zeplarszombies.Monsters;

import com.zeplar.zeplarszombies.BlockHelper;
import com.zeplar.zeplarszombies.Events.PlayerScentMap;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

public class EntityAITunnel extends EntityAIBase {

    private EntityLiving entity;
    private BlockPos target;
    private double range;

    public EntityAITunnel(EntityLiving entityIn, double rangeIn)
    {
        this.setMutexBits(7);
        entity = entityIn;
        range = rangeIn;
    }

    @Override
    public boolean shouldExecute() {
        if (target == null) target = PlayerScentMap.getNearestScent(entity.world, entity.getPosition());
        return target.distanceSq(entity.getPosition()) < range;
    }

    @Override
    public void startExecuting()
    {
    }

    public boolean shouldContinueExecuting()
    {
        return target.distanceSq(entity.getPosition()) > 4;
    }

    public void resetTask()
    {
        super.resetTask();
        target = null;
    }

    private void setBlockToAir(BlockPos pos)
    {
        if (entity.world.getBlockState(pos).getBlock().equals(Blocks.SANDSTONE_STAIRS)) return;

        entity.world.setBlockToAir(pos);
    }

    private void reinforceWalls()
    {
        for (int i=2; i <= 2; i++)
        for (BlockPos pos : BlockHelper.getAxis(entity.getPosition().up(i), entity))
        {
            if (entity.world.getBlockState(pos).getBlock().equals(Blocks.SAND))
            {
                entity.world.setBlockState(pos, Blocks.SANDSTONE.getDefaultState());
            }
            else if (entity.world.getBlockState(pos).getBlock().equals(Blocks.GRAVEL))
            {
                entity.world.setBlockState(pos, Blocks.COBBLESTONE.getDefaultState());
            }
        }
    }

    private void makeStairs(BlockPos pos)
    {
        if (entity.world.getBlockState(pos.down()).getBlock() == Blocks.SANDSTONE_STAIRS) return;
        entity.world.setBlockState(pos, Blocks.SANDSTONE_STAIRS.getStateForPlacement(entity.world, pos,
                EnumFacing.getDirectionFromEntityLiving(pos, entity), 0, 0, 0, 0, entity, EnumHand.MAIN_HAND));
    }

    private void tunnelUp()
    {
        BlockPos forward = entity.getPosition().add(BlockHelper.getForward(entity));
        for (int i=1; i < 3; i++)
        for (BlockPos pos : BlockHelper.getAxis(forward.up(i), entity))
        {
            setBlockToAir(pos);
        }
        for (BlockPos pos : BlockHelper.getAxis(forward, entity))
        {
            makeStairs(pos);
        }
    }
    private void tunnelDown()
    {
        BlockPos forward = entity.getPosition().add(BlockHelper.getForward(entity));

        for (int i=-1; i < 2; i++)
        for (BlockPos pos : BlockHelper.getAxis(forward.down(i),entity))
        {
            setBlockToAir(pos);
        }
    }

    private void tunnelForward()
    {
        BlockPos forward = entity.getPosition().add(BlockHelper.getForward(entity));
        for (int i=0; i < 2; i++)
        for (BlockPos pos : BlockHelper.getAxis(forward.up(i),entity))
        {
            setBlockToAir(pos);
        }
    }

    public void updateTask()
    {
        Vec3i movePos = target.subtract(entity.getPosition());
        entity.getMoveHelper().setMoveTo(movePos.getX(),movePos.getY(),movePos.getZ(),1.8);

        double distanceFromTarget = target.distanceSq(entity.getPosition());
        double yDistance = target.getY() - entity.getPosition().getY();
        reinforceWalls();
        if (distanceFromTarget < 200 && yDistance > 0) tunnelUp();
        else if (yDistance < 10) tunnelDown();
        else tunnelForward();
    }

}
