package com.zeplar.zeplarszombies.Events;

import com.zeplar.zeplarszombies.Monsters.EntityAIBreakBlock;
import com.zeplar.zeplarszombies.Monsters.MoveStraightToPlayer;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class SpawnEvent {

    private static final String LOGIN_KEY = "zeplar.firstjoin";
    private static final float chargerChance = 0.1f;
    private static int maxMobsPerPlayer = 10;

    @SubscribeEvent
    public static void onSpawn(EntityJoinWorldEvent e)
    {
        if (e.getWorld().getLoadedEntityList().size() > e.getWorld().playerEntities.size() * maxMobsPerPlayer) e.setCanceled(true);

        else if (e.getEntity() instanceof EntityZombie) {
           EntityMob entity = (EntityMob)e.getEntity();
           entity.tasks.addTask(1, new EntityAIBreakBlock(entity,50, 20));
        }
        else if (e.getEntity() instanceof EntityCreeper) {
            spawnCharger(e);

        }
        else if (!(e.getEntity() instanceof EntityPlayer)) {
            e.setCanceled(true);
        }
    }

    private static void spawnCharger(EntityJoinWorldEvent e)
    {
        EntityCreeper entity = (EntityCreeper)e.getEntity();
        if (entity.getRNG().nextFloat() < chargerChance) {
            entity.setGlowing(true);
            entity.tasks.addTask(1, new MoveStraightToPlayer(entity, 2.0, 200));
            entity.tasks.addTask(1, new EntityAIBreakBlock(entity, 1, 100));
        }
        if (entity.isGlowing() == false) e.setCanceled(true);
    }

}
