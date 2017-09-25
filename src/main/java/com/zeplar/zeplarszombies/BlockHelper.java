package com.zeplar.zeplarszombies;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;

public class BlockHelper {

    private static Vec3i around[] = null;

    private static void populateAround()
    {
        around = new Vec3i[26];
        int index=0;
        for (int i=-1; i < 2; i++)
            for (int j=-1; j < 2; j++)
                for (int k=-1; k < 2; k++)
                {
                    if ((i|j|k)==0) continue;
                    else around[index++] = new Vec3i(i,j,k);
                }
    }

    public static BlockPos[] getAround(BlockPos position)
    {
        if (around == null) populateAround();
        BlockPos ret[] = new BlockPos[26];
        int i=0;
        for (Vec3i a: around)
        {
            ret[i++] = position.add(a);
        }
        return ret;
    }

    /**
     *
     * @param start Entity position
     * @param end   Block directly in front of start
     * @return
     */
    public static BlockPos[] getStairDown(BlockPos start, BlockPos end)
    {
        BlockPos ret[] = new BlockPos[]
                {
                        end.up(),
                        end,
                        end.down()
                };
        return ret;
    }

    /**
     *
     * @param start Entity position
     * @param end Block directly in front of start
     * @return
     */
    public static BlockPos[] getStairUp(BlockPos start, BlockPos end)
    {
        BlockPos ret[] = new BlockPos[]
                {
                        start.up(2),
                        end.up(),
                        end.up(2)
                };
        return ret;
    }


}
