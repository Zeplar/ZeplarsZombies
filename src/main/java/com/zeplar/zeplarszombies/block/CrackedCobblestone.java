package com.zeplar.zeplarszombies.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class CrackedCobblestone extends Block {

    private static final String name = "crackedcobblestone";

    public CrackedCobblestone() {
        super(Material.ROCK);
        ModBlocks.registerBlock(this, name);
    }

}
