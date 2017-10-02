package com.zeplar.zeplarszombies.Monsters;

import com.zeplar.zeplarszombies.ModInfo;
import com.zeplar.zeplarszombies.zeplarszombies;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Biomes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModEntities {

    public static void init() {
        // Every entity in our mod has an ID (local to this mod)
        int id = 1;
        EntityRegistry.registerModEntity(new ResourceLocation(ModInfo.MODID, "infested_villager"),EntityInfestedVillager.class, "infested_villager", id++, zeplarszombies.instance, 64, 3, true, 0x996600, 0x00ff00);
        EntityRegistry.registerModEntity(new ResourceLocation(ModInfo.MODID, "tunneling_zombie"),EntityTunnelingZombie.class, "tunneling_zombie", id++, zeplarszombies.instance, 64, 3, true, 0x436600, 0x00ff00);


        // We want our mob to spawn in Plains and ice plains biomes. If you don't add this then it will not spawn automatically
        // but you can of course still make it spawn manually
        EntityRegistry.addSpawn(EntityInfestedVillager.class, 100, 3, 5, EnumCreatureType.MONSTER, Biomes.PLAINS, Biomes.ICE_PLAINS);
        EntityRegistry.addSpawn(EntityTunnelingZombie.class, 100, 3, 5, EnumCreatureType.MONSTER, Biomes.PLAINS, Biomes.ICE_PLAINS);


        // This is the loot table for our mob
        LootTableList.register(EntityInfestedVillager.LOOT);
    }

    @SideOnly(Side.CLIENT)
    public static void initModels() {
        RenderingRegistry.registerEntityRenderingHandler(EntityInfestedVillager.class, RenderInfestedVillager.FACTORY);
    }
}
