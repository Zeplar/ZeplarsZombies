package com.zeplar.zeplarszombies;

import com.zeplar.zeplarszombies.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(name=ModInfo.NAME, modid = ModInfo.MODID, version = ModInfo.VERSION)
public class zeplarszombies {

    @Mod.Instance
    public static zeplarszombies instance;

    @SidedProxy(clientSide = ModInfo.PROXY_BASE + ".ClientProxy",
    serverSide = ModInfo.PROXY_BASE + ".ServerProxy")
    public static CommonProxy proxy;
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init(event);
    }

    @EventHandler
    public void preinit(FMLPreInitializationEvent e)
    {
        proxy.preinit(e);
    }

    @EventHandler
    public void postinit(FMLPostInitializationEvent e)
    {
        proxy.postinit(e);
    }
}
