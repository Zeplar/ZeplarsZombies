package com.zeplar.zeplarszombies.proxy;

import com.zeplar.zeplarszombies.block.ModBlocks;
import com.zeplar.zeplarszombies.item.ModItems;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy {

    public void init(FMLInitializationEvent event)
    {
        super.init(event);
    }

    public void preinit(FMLPreInitializationEvent e)
    {
        super.preinit(e);
    }

    public void postinit(FMLPostInitializationEvent e)
    {
        super.postinit(e);
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event)
    {
        ModBlocks.initModels();
        ModItems.initModels();
    }

}
