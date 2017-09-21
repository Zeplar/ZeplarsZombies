package com.zeplar.zeplarszombies.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class ItemTutorial extends Item {

    public ItemTutorial()
    {
        ModItems.registerItem(this, "item_tutorial");
    }


    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
    {
        if (!world.isRemote) {
            ItemStack item = player.getHeldItem(hand);
            NBTTagCompound nbt = item.getTagCompound();
            if (nbt == null) nbt = new NBTTagCompound();
            if (nbt.hasKey("Clicks")) nbt.setInteger("Clicks", nbt.getInteger("Clicks")+1);
            else nbt.setInteger("Clicks",1);
            player.sendMessage(new TextComponentString("Clicks: " + nbt.getInteger("Clicks")));
            item.setTagCompound(nbt);
            }
        return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
    }

}
