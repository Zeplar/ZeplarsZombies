package com.zeplar.zeplarszombies.Events;

import com.zeplar.zeplarszombies.block.ModBlocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

@Mod.EventBusSubscriber
public class SpawnEvent {

    private static final String LOGIN_KEY = "zeplar.firstjoin";

    @SubscribeEvent
    public static void onSpawn(PlayerEvent.PlayerLoggedInEvent e)
    {
        e.player.inventory.addItemStackToInventory(new ItemStack(ModBlocks.crackedCobblestone));
    }

}
