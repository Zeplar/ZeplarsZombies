package com.zeplar.zeplarszombies.Events;

import com.zeplar.zeplarszombies.Monsters.EntityAIBreakBlock;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class SpawnEvent {

    private static final String LOGIN_KEY = "zeplar.firstjoin";

    @SubscribeEvent
    public static void onSpawn(EntityJoinWorldEvent e)
    {
        if (e.getEntity() instanceof EntityZombie) {
           EntityMob entity = (EntityMob)e.getEntity();
           entity.tasks.addTask(1, new EntityAIBreakBlock(entity));
           System.out.println("Added AI");

        }
    }

}
