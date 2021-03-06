package com.zeplar.zeplarszombies.Events;

import com.zeplar.zeplarszombies.Monsters.EntityAIBreakBlock;
import com.zeplar.zeplarszombies.Monsters.EntityInfestedVillager;
import com.zeplar.zeplarszombies.Monsters.EntityTunnelingZombie;
import com.zeplar.zeplarszombies.Monsters.MoveStraightToPlayer;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.IMob;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class SpawnEvent {

    private static final String LOGIN_KEY = "zeplar.firstjoin";
    private static final float chargerChance = 0.1f;
    private static int maxMobsPerPlayer = 50;

    @SubscribeEvent
    public static void onSpawn(EntityJoinWorldEvent e)
    {
        if (!(e.getEntity() instanceof IMob)) return;
       // if (e.getWorld().getLoadedEntityList().size() > e.getWorld().playerEntities.size() * maxMobsPerPlayer) e.setCanceled(true);

        else if (e.getEntity() instanceof EntityCreeper) {
            spawnCharger(e);
        }
        else if (e.getEntity() instanceof EntityInfestedVillager)
        {
            /*if (((EntityInfestedVillager) e.getEntity()).getRNG().nextInt(10) != 0)
            {
                ((EntityInfestedVillager)e.getEntity()).replaceWithVillager();
                e.setCanceled(true);
            }*/
        }
        else if (e.getEntity() instanceof EntityTunnelingZombie)
        {

        }
        else if (e.getEntity() instanceof EntityZombie) {
            EntityMob entity = (EntityMob)e.getEntity();
            entity.tasks.addTask(1, new EntityAIBreakBlock(entity,50, 20));
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
