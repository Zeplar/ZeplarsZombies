package com.zeplar.zeplarszombies.Events;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
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
                int i = Integer.parseInt(num);
                makeShellAround(event.getPlayer(), i);
            }
        }

        else if (event.getMessage().equals("kill all"))
        {
            for (Entity entity : event.getPlayer().world.getLoadedEntityList())
            {
                if (entity instanceof EntityLiving && !(entity instanceof EntityPlayer) ) entity.setDead();
            }
        }
    }

    private static void makeShellAround(EntityPlayer player, int radius)
    {
        int x = player.getPosition().getX();
        int y = player.getPosition().getY();
        int z = player.getPosition().getZ();
        for (int dy=0; dy <= 3; dy++)
        {
            for (int dx=-radius; dx <= radius; dx++)
            {
                player.world.setBlockState(new BlockPos(x+dx, y+dy, z-radius), Blocks.STONE.getDefaultState());
                player.world.setBlockState(new BlockPos(x+dx, y+dy, z+radius), Blocks.STONE.getDefaultState());

            }
            for (int dz=-radius; dz <= radius; dz++)
            {
                player.world.setBlockState(new BlockPos(x-radius, y+dy, z+dz), Blocks.STONE.getDefaultState());
                player.world.setBlockState(new BlockPos(x+radius, y+dy, z+dz), Blocks.STONE.getDefaultState());
            }
        }
        for (int dx=-radius; dx <= radius; dx++)
            for (int dz=-radius; dz <= radius; dz++)
            {
                player.world.setBlockState(new BlockPos(x+dx, y+3, z+dz), Blocks.STONE.getDefaultState());
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
