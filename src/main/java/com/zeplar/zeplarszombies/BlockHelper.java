package com.zeplar.zeplarszombies;

import net.minecraft.entity.EntityLiving;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
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

    public static Vec3i getForward(EntityLiving entity)
    {
        return getForward(entity.getForward());
    }

    public static Vec3i getForward(Vec3i forwardVector)
    {
        return getForward(new Vec3d(forwardVector));
    }

    public static Vec3i getForward(Vec3d forwardVector)
    {
        Vec3d fwd = forwardVector;
        double absX = Math.abs(fwd.x);
        double absZ = Math.abs(fwd.z);

        if (absX > absZ) return new Vec3i(fwd.x > 0 ? 1 : -1, 0, 0);
        else return new Vec3i(0, 0, fwd.z > 0 ? 1 : -1);
    }

    public static Vec3i[] getSides(EntityLiving entity)
    {
        return getSides(getForward(entity));
    }

    public static Vec3i[] getSides(Vec3i fwd)
    {
        Vec3i side1 = fwd.crossProduct(new Vec3i(0,1,0));
        Vec3i side2 = fwd.crossProduct(new Vec3i(0,-1,0));
        return new Vec3i[] {side1, side2};
    }

    public static BlockPos[] getAxis(BlockPos center, EntityLiving entity)
    {
        return getAxis(center, getForward(entity));
    }

    public static BlockPos[] getAxis(BlockPos center, Vec3i fwd)
    {
        Vec3i[] sides = getSides(fwd);
        return new BlockPos[] {center, center.add(sides[0]), center.add(sides[1])};
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
