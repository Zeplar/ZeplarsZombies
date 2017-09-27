package com.zeplar.zeplarszombies.Events;

import com.sun.jmx.remote.internal.ArrayQueue;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Iterator;

@Mod.EventBusSubscriber
public class PlayerScentMap {

    private static ArrayQueue<BlockPos> recentPlayerPositions = new ArrayQueue<>(32);

    @SubscribeEvent
    public static void onTick(TickEvent.WorldTickEvent event)
    {
        for (EntityPlayer player: event.world.playerEntities)
        {
            if (recentPlayerPositions.size() > 0) recentPlayerPositions.remove(0);
            recentPlayerPositions.add(player.getPosition());
        }
    }

    public static Iterator<BlockPos> getScent()
    {
        return recentPlayerPositions.iterator();
    }

}
