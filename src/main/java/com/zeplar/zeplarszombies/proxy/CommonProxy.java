package com.zeplar.zeplarszombies.proxy;

import com.zeplar.zeplarszombies.block.CrackedCobblestone;
import com.zeplar.zeplarszombies.block.DurabilityBlock;
import com.zeplar.zeplarszombies.block.ModBlocks;
import com.zeplar.zeplarszombies.block.PortalGenerator;
import com.zeplar.zeplarszombies.item.ModItems;
import com.zeplar.zeplarszombies.tile.Tiles;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class CommonProxy {

    public void init(FMLInitializationEvent event)
    {

    }

    public void preinit(FMLPreInitializationEvent e) {
        ModBlocks.commonPreinit();
        ModItems.commonPreinit();
        Tiles.commonPreinit();
    }

    public void postinit(FMLPostInitializationEvent e)
    {

    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event)
    {
        event.getRegistry().register(new CrackedCobblestone());
        event.getRegistry().register(new DurabilityBlock());
        event.getRegistry().register(new PortalGenerator());
    }

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event)
    {
        for (Block block : ModBlocks.blocks.keySet())
        event.getRegistry().register(new ItemBlock(block).setRegistryName((block.getRegistryName())));


        for (Item item : ModItems.items)
            event.getRegistry().register(item);
    }
}
