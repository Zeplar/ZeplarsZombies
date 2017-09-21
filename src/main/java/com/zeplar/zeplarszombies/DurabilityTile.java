package com.zeplar.zeplarszombies;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DurabilityTile extends TileEntity {

    private int durability;

    public DurabilityTile()
    {
    }


    public static void setDurability(World world, BlockPos blockPos)
    {
        TileEntity te = world.getTileEntity((blockPos));
        if (te == null) {
            world.setTileEntity(blockPos, new DurabilityTile());
        }
        DurabilityTile de = (DurabilityTile)te;
        de.durability = 5;

    }

    public void readFromNBT(NBTTagCompound nbt) {
        if (nbt != null && nbt.hasKey("durability"))
        {
            this.durability = nbt.getInteger("durability");
            super.readFromNBT(nbt);
        }
    }
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        if (nbt == null) nbt = new NBTTagCompound();
        nbt.setInteger("durability", this.durability);
        return super.writeToNBT(nbt);
    }
}
