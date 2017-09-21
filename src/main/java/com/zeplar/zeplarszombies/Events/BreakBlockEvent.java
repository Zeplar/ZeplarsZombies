package com.zeplar.zeplarszombies.Events;

import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class BreakBlockEvent {
/*
    @SubscribeEvent
    public static void onBreakBlock(BlockEvent.BreakEvent e)
    {
        if (e.getWorld().isRemote)
        {
            BlockPos bp = e.getPos();
            TileEntity te = e.getWorld().getTileEntity(bp);
            EntityPlayer player = e.getPlayer();
            if (te == null) e.getWorld().setTileEntity(bp, new DurabilityTile());
            NBTTagCompound nbt = te.getTileData();
            if (nbt == null) nbt = new NBTTagCompound();

            if (nbt.hasKey("durability")) nbt.setInteger("durability", nbt.getInteger("durability")+1);
            else nbt.setInteger("durability",1);

            te.
        }
    }*/

}
