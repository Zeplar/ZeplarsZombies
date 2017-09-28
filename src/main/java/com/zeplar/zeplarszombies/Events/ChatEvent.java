package com.zeplar.zeplarszombies.Events;

import com.zeplar.zeplarszombies.Monsters.EntityAIBreakBlock;
import com.zeplar.zeplarszombies.Monsters.EntityInfestedVillager;
import com.zeplar.zeplarszombies.Monsters.MoveStraightToPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityCreeper;
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
        if (event.getMessage().contains("makeHouse"))
        {
            if (event.getMessage().equals("makeHouse"))
                makeHouseAround(event.getPlayer(), 2);
            else
            {
                String num = event.getMessage().substring("makeHouse ".length(), event.getMessage().length());
                System.out.println("Parsing: " + num + ".");
                int i = Integer.parseInt(num);
                makeHouseAround(event.getPlayer(), i);
            }
        }
        else if (event.getMessage().equals("makeInfested"))
        {
            EntityInfestedVillager villager = new EntityInfestedVillager(event.getPlayer().world);
            BlockPos position = event.getPlayer().getPosition().north(2);
            villager.setPosition(position.getX(),position.getY(),position.getZ());
            event.getPlayer().world.spawnEntity(villager);
            System.out.println("villager");
        }

        else if (event.getMessage().equals("kill all"))
        {
            for (Entity entity : event.getPlayer().world.getLoadedEntityList())
            {
                if (entity instanceof EntityLiving && !(entity instanceof EntityPlayer) ) entity.setDead();
            }
        }

        else if (event.getMessage().equals("makeCharger"))
        {
            EntityCreeper creeper = new EntityCreeper(event.getPlayer().world);
            BlockPos position = event.getPlayer().getPosition().north(20);
            creeper.setPosition(position.getX(),position.getY(),position.getZ());
            creeper.setGlowing(true);
            creeper.tasks.addTask(1, new MoveStraightToPlayer(creeper, 2.0, 200));
            creeper.tasks.addTask(1, new EntityAIBreakBlock(creeper, 1, 100));
            event.getPlayer().world.spawnEntity(creeper);
        }
    }

    private static void makeHouseAround(EntityPlayer player, int radius)
    {
        int x = player.getPosition().getX();
        int y = player.getPosition().getY();
        int z = player.getPosition().getZ();
        for (int dy = 0; dy <= 3; dy++)
        for (int dx=-radius; dx <= radius; dx ++)
        {
            for (int dz = -radius; dz <= radius; dz ++)
            {
                BlockPos nextBlock = new BlockPos(x+dx,y+dy,z+dz);
                if (nextBlock.distanceSq(player.getPosition()) < 3) continue;
                else
                {
                    player.world.setBlockState(nextBlock, Blocks.GLASS.getDefaultState());
                }
            }
        }
        for (int dx=-radius; dx <=radius; dx++)
            for (int dz=-radius; dz<=radius; dz++)
                player.world.setBlockState(new BlockPos(x+dx,y+4,z+dz),Blocks.GLASS.getDefaultState());
    }
}
