package com.zeplar.zeplarszombies.block;

import com.zeplar.zeplarszombies.tile.DurabilityTile;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class DurabilityBlock extends Block implements ITileEntityProvider {

    public static DurabilityBlock durabilityBlock;
    public DurabilityBlock() {
        super(Material.ROCK);
        ModBlocks.registerBlock(this, "durabilityblock");
        setHardness(1.0f);
        durabilityBlock = this;
    }

    @SideOnly(Side.CLIENT)
    public void initModel() {
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }



    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new DurabilityTile();
    }

}
