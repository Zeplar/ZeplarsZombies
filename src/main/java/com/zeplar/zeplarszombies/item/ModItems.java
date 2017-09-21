package com.zeplar.zeplarszombies.item;

import com.zeplar.zeplarszombies.ModInfo;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class ModItems {

    public static final List<Item> items = new ArrayList<Item>();

    public static final void commonPreinit()
    {
        items.add(new ItemTutorial());

    }

    @SideOnly(Side.CLIENT)
    public static final void initModels()
    {
        for (Item item : items) {
            ModelLoader.setCustomModelResourceLocation(item,0,new ModelResourceLocation(item.getRegistryName(),"inventory"));
        }
    }

    public static final Item registerItem(Item item, String name)
    {
        item.setRegistryName(name);
        item.setUnlocalizedName(ModInfo.MODID + "." + name);
        return item;
    }
}
