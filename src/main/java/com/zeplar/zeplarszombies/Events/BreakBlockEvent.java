package com.zeplar.zeplarszombies.Events;

import com.zeplar.zeplarszombies.tile.DurabilityTile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class BreakBlockEvent {

    //@SubscribeEvent
    public static void onBreakBlock(BlockEvent.BreakEvent e)
    {
       EntityPlayer player = e.getPlayer();
       player.sendMessage(new TextComponentString("Block broken."));

        TileEntity te = e.getWorld().getTileEntity(e.getPos());
        if ((te != null) && (te instanceof DurabilityTile))
        {
            DurabilityTile dt = (DurabilityTile)te;
            player.sendMessage(new TextComponentString("Ddurability is " + (dt).decrement()));
            e.setCanceled(dt.getDurability() > 0);
        }
    }

}
