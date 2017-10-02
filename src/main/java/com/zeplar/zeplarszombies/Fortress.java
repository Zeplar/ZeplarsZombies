package com.zeplar.zeplarszombies;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.TreeSet;

@Mod.EventBusSubscriber
public class Fortress {

    private Fortress parent = null;    //Null if this is the whole fortress
    public World world;
    private boolean enclosed;
    private HashMap<BlockPos, Block> layout;
    private BlockPos center;

    private static LinkedList<Fortress> fortresses = new LinkedList<>();
    private static int index=0;

    @SubscribeEvent
    public static void makeFortress(PlayerSleepInBedEvent event)
    {
        if (isInFortress(event.getPos())) return;

        Fortress fort = new Fortress(event.getEntity().world,event.getEntityPlayer().getPosition());
        if (fort.isEnclosed())
        {
            event.getEntityPlayer().sendMessage(new TextComponentString("Fortress is enclosed."));
            fortresses.add(fort);
        }
        else
        {
            event.getEntityPlayer().sendMessage(new TextComponentString("Fortress is not enclosed."));
        }
    }

    @SubscribeEvent
    public static void checkFortresses(TickEvent.WorldTickEvent event)
    {
        if (fortresses.isEmpty()) return;
        if (event.world.provider.getDimension() != 0) return;
        index = (index+1)%1023;
        if (index != 0) return;
        BlockPos center = fortresses.getFirst().center;
        fortresses.removeFirst();
        Fortress replacement = new Fortress(event.world, center);
        if (replacement.isEnclosed()) {
            event.world.getMinecraftServer().getPlayerList().sendMessage(new TextComponentString("Fortress is secure."));
            fortresses.add(replacement);
        }
        else
        {
            event.world.getMinecraftServer().getPlayerList().sendMessage(new TextComponentString("Fortress was removed."));
        }

    }

    public static boolean isInFortress(BlockPos position)
    {
        for (Fortress f: fortresses)
        {
            if (f.layout.containsKey(position)) return true;
        }
        return false;
    }


    public boolean isEnclosed()
    {
        return enclosed;
    }


    private boolean hasRoof(BlockPos pos)
    {
        if (!world.canSeeSky(pos.up())) return true;
        else
        {
            for (int i=1; i < 256; i++)
            {
                if (!world.isAirBlock(pos.up(i))) return true;
            }
            return false;
        }
    }

    private static Comparator<BlockPos> blockPosComparator = new Comparator<BlockPos>() {
        @Override
        public int compare(BlockPos o1, BlockPos o2) {
            if (o1.getX() != o2.getX()) return (o1.getX() < o2.getX() ? -1 : 1);
            if (o1.getY() != o2.getY()) return (o1.getY() < o2.getY() ? -1 : 1);
            if (o1.getZ() != o2.getZ()) return (o1.getZ() < o2.getZ() ? -1 : 1);
            return 0;
        }
    };


    public Fortress(World worldIn, BlockPos start)
    {
        world = worldIn;
        center = start;
        TreeSet<BlockPos> openSet = new TreeSet<>();
        openSet.add(start.up());
        HashMap<BlockPos, Block> closedSet = new HashMap<>();
        closedSet.put(start, world.getBlockState(start).getBlock());

        while (!openSet.isEmpty()) {
            BlockPos pos = openSet.first();
            openSet.remove(pos);
            closedSet.put(pos, world.getBlockState(pos).getBlock());
            boolean indoors = hasRoof(pos) && world.isAirBlock(pos);
            if (indoors)
                for (BlockPos a: BlockHelper.getAround(pos)) {
                    if (!closedSet.containsKey(a)) {
                        openSet.add(a);
                    }
                }
            else if (world.isAirBlock(pos))
            {
                enclosed = false;
                return;
            }
            else if (world.getBlockState(pos).getBlock() instanceof BlockBed)
            {
                closedSet.put(pos,world.getBlockState(pos).getBlock());
            }
        }
        layout = closedSet;
        enclosed = true;

    }


}
