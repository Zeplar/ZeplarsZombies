package com.zeplar.zeplarszombies.Events;

import com.zeplar.zeplarszombies.Monsters.EntityAIBreakBlock;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

@Mod.EventBusSubscriber
public class SpawnEvent {

    private static final String LOGIN_KEY = "zeplar.firstjoin";

    @SubscribeEvent
    public static void onSpawn(PlayerEvent.PlayerLoggedInEvent e)
    {
        if (!e.player.world.isRemote) {
            e.player.sendMessage(new TextComponentString("Hello there."));

            EntityZombie zombie = new EntityZombie(e.player.world);
            zombie.setPosition(
                    e.player.getPosition().getX(),
                    e.player.getPosition().getY(),
                    e.player.getPosition().getZ() + 2
            );
            zombie.tasks.addTask(0, new EntityAIBreakBlock(zombie));

            e.player.world.spawnEntity(zombie);
        }
    }

}
