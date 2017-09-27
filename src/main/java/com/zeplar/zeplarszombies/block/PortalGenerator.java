package com.zeplar.zeplarszombies.block;

import net.minecraft.block.BlockBreakable;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemMonsterPlacer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;

import java.util.Random;

public class PortalGenerator extends BlockBreakable implements IZeplarModBlock {

    public PortalGenerator()
    {
        super(Material.ROCK, false);
        ModBlocks.registerBlock(this, "portalgenerator");
    }

    private AxisAlignedBB spawnArea;

    public boolean canBeBrokenByMonsters() {return false;}

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
    {
        worldIn.scheduleBlockUpdate(pos,state.getBlock(),0,0);
    }

    public void onBlockDestroyedByPlayer(World worldIn, BlockPos pos, IBlockState state)
    {
        spawnArea = null;
    }

    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
    {
        super.updateTick(worldIn, pos, state, rand);
        worldIn.scheduleBlockUpdate(pos,state.getBlock(),0,0);

        if (spawnArea == null)         spawnArea = new AxisAlignedBB(pos.add(new Vec3i(-3,0,-3)), pos.add(new Vec3i(3,0,3)));

        if (worldIn.getEntitiesWithinAABB(EntityMob.class, spawnArea).size() > 4) return;
        if (rand.nextInt(60) == 0)
        {
            worldIn.playSound(pos.getX(),pos.getY(),pos.getZ(), SoundEvents.BLOCK_PORTAL_TRIGGER, SoundCategory.BLOCKS, 5.0f, 1.0f, false );
            switch (rand.nextInt(2))
            {
                case 0:
                    spawnEntity(worldIn, pos, rand, EntityCreeper.class);
                    break;
                default:
                    spawnEntity(worldIn, pos, rand, EntityZombie.class);
                    break;
            }
        }
    }

    private <T extends EntityCreature>void spawnEntity(World worldIn, BlockPos pos, Random rand, Class<T> clss)
    {
        ItemMonsterPlacer.spawnCreature(
                worldIn, EntityList.getKey(clss),
                pos.getX() + rand.nextInt(3),
                pos.getY(),
                pos.getZ() + rand.nextInt(3)
        );
    }
}
