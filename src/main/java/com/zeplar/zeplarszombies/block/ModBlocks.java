package com.zeplar.zeplarszombies.block;

import com.zeplar.zeplarszombies.ModInfo;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;

public class ModBlocks {

    public static final HashMap<Block,Item> blocks = new HashMap<>();

    public static final void commonPreinit() {

    }

    public static final Block registerBlock(Block block, String name) {
        block.setRegistryName(name);
        block.setUnlocalizedName(ModInfo.MODID + "." + name);
        Item blockItem = Item.getItemFromBlock(block);
        blocks.put(block,blockItem);
        return block;
    }

    @SideOnly(Side.CLIENT)
    public static void initModels()
    {
        for (Block block : blocks.keySet())
        ModelLoader.setCustomModelResourceLocation(ItemBlock.getItemFromBlock((block)), 0, new ModelResourceLocation(block.getRegistryName(), "inventory"));
    }
}
