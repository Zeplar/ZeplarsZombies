package com.zeplar.zeplarszombies.Events;

import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class ChatEvent {

    @SubscribeEvent
    public static void onMessage(ServerChatEvent event)
    {
        if (event.getMessage().equals("makeHouse"))
        {
            BlockPos pos = event.getPlayer().getPosition();
            for (int y=pos.getY(); y < pos.getY()+3; y++)
            {
                makeHouseAround(event.getPlayer(), pos.getX(), y, pos.getZ());
            }
        }

        else if (event.getMessage().equals("makeCharger"))
        {
            EntityZombie zombie = new EntityZombie(event.getPlayer().world);
            //zombie.tasks.addTask(0, new EntityAITunnelToPlayer(zombie));
            BlockPos position = event.getPlayer().getPosition().north(20);
            zombie.setPosition(position.getX(),position.getY(),position.getZ());
            event.getPlayer().world.spawnEntity(zombie);

        }
    }

    private static void makeHouseAround(EntityPlayer player, int x, int y, int z)
    {
        for (int i=-2; i <= 2; i++)
        {
            for (int j = -2; j <= 2; j++)
            {
                if (i == 0 && j == 0) continue;
                else
                {
                    player.world.setBlockState(new BlockPos(x+i,y,z+j), Blocks.STONE.getDefaultState());
                }
            }
        }
    }
}
