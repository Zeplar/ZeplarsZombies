package com.zeplar.zeplarszombies.Events;

import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class BreakBlockEvent {
    @SubscribeEvent
    public static void onBreakBlock(BlockEvent.BreakEvent e)
    {

    }

}
