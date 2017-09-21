package com.zeplar.zeplarszombies.Events;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class BreakBlockEvent {

    @SubscribeEvent
    public static void onBreakBlock(BlockEvent.BreakEvent e)
    {
       EntityPlayer player = e.getPlayer();
       player.sendMessage(new TextComponentString("Block broken."));

        TileEntity te = e.getWorld().getTileEntity(e.getPos());
        if (te == null) player.sendMessage(new TextComponentString("Null tile."));
        else
        {
            player.sendMessage(new TextComponentString("Has durability is " +  te.getTileData().hasKey("durability")));
        }
    }

}
