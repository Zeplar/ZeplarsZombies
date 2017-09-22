package com.zeplar.zeplarszombies.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class DurabilityTile extends TileEntity {

    private int durability = 0;

    public DurabilityTile()
    {
    }


    public int decrement() {
        durability--;
        markDirty();
        return durability;
    }

    public int getDurability() {
        return durability;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        if (nbt != null && nbt.hasKey("durability"))
        {
            this.durability = nbt.getInteger("durability");
            super.readFromNBT(nbt);
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        if (nbt == null) nbt = new NBTTagCompound();
        nbt.setInteger("durability", this.durability);
        return super.writeToNBT(nbt);
    }
}
