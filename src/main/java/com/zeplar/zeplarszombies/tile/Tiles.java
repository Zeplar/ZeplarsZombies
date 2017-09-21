package com.zeplar.zeplarszombies.tile;

import net.minecraftforge.fml.common.registry.GameRegistry;

public class Tiles {

    public static final void commonPreinit() {
        GameRegistry.registerTileEntity(DurabilityTile.class, "zeplarszombies_durabilityblock");
    }

}
