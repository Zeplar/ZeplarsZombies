package com.zeplar.zeplarszombies.Events;

import com.sun.jmx.remote.internal.ArrayQueue;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockFurnace;
import net.minecraft.block.BlockWorkbench;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber
public class PlayerScentMap {

    private static final int capacity = 32;
    private static ArrayQueue<BlockPos> recentPlayerPositions = new ArrayQueue<>(capacity);
    private static List<BlockPos> permanentPlayerPositions = new LinkedList<>();
    private static int ticksSinceLastUpdate = 0;
    private static final int ticksPerMinute = 2400;

    private static Map<BlockPos, IBlockState> debugMemory = new HashMap<>();

    private static Class[] manmade = new Class[] {BlockFurnace.class, BlockBed.class, BlockWorkbench.class};

    @SubscribeEvent
    public static void onSleep(PlayerSleepInBedEvent event)
    {
        addScent(event.getPos(), true);
    }

    @SubscribeEvent
    public static void onFurnace(PlayerInteractEvent event)
    {
        Block block = event.getWorld().getBlockState(event.getPos()).getBlock();
        for (Class c: manmade)
        {
            if (c.isInstance(block))
            {
                addScent(event.getPos(), true);
                return;
            }
        }
    }

    @SubscribeEvent
    public static void onTick(TickEvent.WorldTickEvent event)
    {
        if (event.world.provider.getDimension() != 0) return;
        if (++ticksSinceLastUpdate > ticksPerMinute) {
            ticksSinceLastUpdate = 0;
            for (EntityPlayer player : event.world.playerEntities)
            {
                addScent(player.getPosition());
            }
        }
    }

    @SubscribeEvent
    public static void onMsg(ServerChatEvent event)
    {
        if (event.getMessage().equals("debug"))
        {
            debug(event.getPlayer().world);
        }
        else if (event.getMessage().equals("rebug"))
        {
            rebug(event.getPlayer().world);
        }
    }

    private static void debug(World world)
    {
        for (BlockPos pos : recentPlayerPositions)
        {
            debugMemory.put(pos, world.getBlockState(pos));
            world.setBlockState(pos, Blocks.OBSIDIAN.getDefaultState());
        }
        for (BlockPos pos : permanentPlayerPositions)
        {
            debugMemory.put(pos, world.getBlockState(pos));
            world.setBlockState(pos, Blocks.REDSTONE_BLOCK.getDefaultState());
        }
    }
    private static void rebug(World world)
    {
        for (Map.Entry<BlockPos, IBlockState> keyval : debugMemory.entrySet())
        {
            world.setBlockState(keyval.getKey(), keyval.getValue());
        }
        debugMemory.clear();
    }

    public static void addScent(BlockPos pos)
    {
        addScent(pos, false);
    }
    public static void addScent(BlockPos pos, boolean permanent)
    {
        if (!permanent) {
            if (recentPlayerPositions.size() >= capacity) recentPlayerPositions.remove(0);
            recentPlayerPositions.add(pos);
        }
        else {
            for (BlockPos pos1 : permanentPlayerPositions)
            {
                if (pos1.distanceSq(pos) < 100) return;
            }
            if (permanentPlayerPositions.size() >= capacity) permanentPlayerPositions.remove(permanentPlayerPositions.size()-1);
            permanentPlayerPositions.add(pos);
        }
    }

    public static BlockPos getNearestScent(World world, BlockPos pos)
    {
        double d = -1;
        double candidate;
        BlockPos candidatePosition = BlockPos.ORIGIN;
        for (EntityPlayer player : world.playerEntities)
        {
            candidate = player.getPosition().distanceSq(pos);
            if (candidate < d || d == -1) {
                d = candidate;
                candidatePosition = player.getPosition();
            }
        }
        for (BlockPos tryPos : permanentPlayerPositions)
        {
            candidate = tryPos.distanceSq(pos) + 25;
            if (candidate < d)
            {
                d = candidate;
                candidatePosition = tryPos;
            }
        }
        for (BlockPos tryPos : recentPlayerPositions)
        {
            candidate = tryPos.distanceSq(pos) + 100;
            if (candidate < d || d == -1)
            {
                d = candidate;
                candidatePosition = tryPos;
            }
        }
        return candidatePosition;
    }

}
