package com.zeplar.zeplarszombies;

import net.minecraft.block.BlockStairs;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod.EventBusSubscriber
public class PathTester {

    private static BlockPos currentPosition;
    private static BlockPos targetPosition;

    private static int ticksSinceUpdate=0;
    private static World world;

    @SubscribeEvent
    public static void setTarget(ServerChatEvent event)
    {
        if (event.getMessage().equals("set"))
        {
            targetPosition = event.getPlayer().getPosition();
            world = event.getPlayer().world;
            world.setBlockState(targetPosition, Blocks.REDSTONE_BLOCK.getDefaultState());
        }

        else if (event.getMessage().equals("go"))
        {
            currentPosition = event.getPlayer().getPosition();
            ticksSinceUpdate = 0;
        }
    }


    private static void tunnelToTarget()
    {
        if (currentPosition == null || targetPosition == null) return;
        if (currentPosition.equals(targetPosition)) return;

        world.setBlockToAir(currentPosition);

        Vec3i forward = targetPosition.subtract(currentPosition);

        if (currentPosition.distanceSq(targetPosition) < 200 && currentPosition.getY() < targetPosition.getY())
        {
            buildUp(forward);
        }
        else if (currentPosition.distanceSq(targetPosition) > 200 && currentPosition.getY() > targetPosition.getY() - 10)
        {
            buildDown(forward);
        }
        else buildForward(forward);

        world.setBlockState(currentPosition, Blocks.DIAMOND_BLOCK.getDefaultState());
    }

    private static void buildForward(Vec3i fwdVector)
    {
        Vec3i forward = BlockHelper.getForward(fwdVector);
        for (BlockPos pos : BlockHelper.getAxis(currentPosition.add(forward), forward))
        {
            world.setBlockToAir(pos);
        }
        for (BlockPos pos : BlockHelper.getAxis(currentPosition.add(forward).up(), forward))
        {
            world.setBlockToAir(pos);
        }
        currentPosition = currentPosition.add(forward);
    }

    private static void buildDown(Vec3i fwdVector)
    {
        Vec3i forward = BlockHelper.getForward(fwdVector);
        for (BlockPos pos : BlockHelper.getAxis(currentPosition.add(forward), forward))
        {
            world.setBlockToAir(pos);
        }
        for (BlockPos pos : BlockHelper.getAxis(currentPosition.add(forward).down(), forward))
        {
            world.setBlockToAir(pos);
        }
        for (BlockPos pos : BlockHelper.getAxis(currentPosition.add(forward).up(), forward))
        {
            world.setBlockToAir(pos);
        }
        currentPosition = currentPosition.add(forward).down();
    }

    private static void buildUp(Vec3i fwdVector)
    {
        Vec3i forward = BlockHelper.getForward(fwdVector);
        for (BlockPos pos : BlockHelper.getAxis(currentPosition.add(forward).up(), forward))
        {
            world.setBlockToAir(pos);
        }
        for (BlockPos pos : BlockHelper.getAxis(currentPosition.add(forward).up(2), forward))
        {
            world.setBlockToAir(pos);
        }
        for (BlockPos pos : BlockHelper.getAxis(currentPosition.up(2), forward))
        {
            world.setBlockToAir(pos);
        }
        for (BlockPos pos : BlockHelper.getAxis(currentPosition.add(forward), forward))
        {
            world.setBlockState(pos, Blocks.SANDSTONE_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.getFacingFromVector(forward.getX(),forward.getY(),forward.getZ())));

        }
        currentPosition = currentPosition.add(forward).up();
    }

    @SubscribeEvent
    public static void onUpdate(TickEvent.WorldTickEvent event)
    {
        if (world == null || event.world != world) return;
        ticksSinceUpdate++;
        if (ticksSinceUpdate > 20)
        {
            ticksSinceUpdate = 0;
            tunnelToTarget();
        }
    }

}
