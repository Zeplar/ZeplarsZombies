package com.zeplar.zeplarszombies.Monsters;

import com.zeplar.zeplarszombies.ModInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class EntityInfestedVillager extends EntityMob {

    private static final DataParameter<Integer> STATE = EntityDataManager.<Integer>createKey(EntityInfestedVillager.class, DataSerializers.VARINT);
    private static final DataParameter<Boolean> IGNITED = EntityDataManager.<Boolean>createKey(EntityInfestedVillager.class, DataSerializers.BOOLEAN);
    /**
     * Time when this creeper was last in an active state (Messed up code here, probably causes creeper animation to go
     * weird)
     */
    private int lastActiveTime;
    /** The amount of time since the creeper was close enough to the player to ignite */
    private int timeSinceIgnited;
    private int fuseTime = 60;

    public static final ResourceLocation LOOT = new ResourceLocation(ModInfo.MODID, "entities/infested_villager");

    public EntityInfestedVillager(World worldIn)
    {
        super(worldIn);
        setSize(0.6F, 1.95F);
    }

    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(35.0D);
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.13D);
        this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3.0D);
        this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(2.0D);
    }

    @Override
    protected void initEntityAI() {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIInfestedVillagerSwell(this));
        this.tasks.addTask(4, new EntityAIAttackMelee(this, 1.0D, false));
        this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
        this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(8, new EntityAILookIdle(this));
        this.tasks.addTask(6, new EntityAIMoveThroughVillage(this, 1.0D, false));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
        this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false, new Class[0]));
    }

    @Override
    protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(STATE, Integer.valueOf(-1));
        this.dataManager.register(IGNITED, Boolean.valueOf(false));
    }

    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);

        compound.setShort("Fuse", (short)this.fuseTime);
        compound.setBoolean("ignited", this.hasIgnited());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);

        if (compound.hasKey("Fuse", 99))
        {
            this.fuseTime = compound.getShort("Fuse");
        }

        if (compound.getBoolean("ignited"))
        {
            this.ignite();
        }
    }

    @SideOnly(Side.CLIENT)
    public float getInfestedVillagerFlashIntensity(float p_70831_1_)
    {
        return ((float)this.lastActiveTime + (float)(this.timeSinceIgnited - this.lastActiveTime) * p_70831_1_) / (float)(this.fuseTime - 2);
    }

    /**
     * Returns the current state of creeper, -1 is idle, 1 is 'in fuse'
     */
    public int getInfestedVillagerState()
    {
        return ((Integer)this.dataManager.get(STATE)).intValue();
    }

    /**
     * Sets the state of creeper, -1 to idle and 1 to be 'in fuse'
     */
    public void setInfestedVillagerState(int state)
    {
        this.dataManager.set(STATE, Integer.valueOf(state));
    }

    /**
     * Creates an explosion as determined by this creeper's power and explosion radius.
     */
    private void explode()
    {
        if (!this.world.isRemote)
        {
            this.dead = true;
            this.world.createExplosion(this, this.posX, this.posY, this.posZ, 0.25f, true);
            this.setDead();
            this.spawnSpiders();
        }
    }

    private void spawnSpiders()
    {
        int numSpiders = this.rand.nextInt(3) + 3;
        for (int i=0; i < numSpiders; i++)
        {
            EntitySpider spider = new EntitySpider(world);
            BlockPos spawnPosition = getPosition().up(i);
            spider.setPosition(spawnPosition.getX(), spawnPosition.getY(), spawnPosition.getZ());
            world.spawnEntity(spider);
        }
    }

    public boolean hasIgnited()
    {
        return ((Boolean)this.dataManager.get(IGNITED)).booleanValue();
    }

    public void ignite()
    {
        this.dataManager.set(IGNITED, Boolean.valueOf(true));
    }

    @Override
    public boolean attackEntityAsMob(Entity entityIn) {
        return true;
    }

    @Override
    @Nullable
    protected ResourceLocation getLootTable() {
        return LOOT;
    }

    @Override
    protected boolean isValidLightLevel() {
        return true;
    }

    @Override
    public void onUpdate()
    {
        if (this.isEntityAlive())
        {
            this.lastActiveTime = this.timeSinceIgnited;

            if (this.hasIgnited())
            {
                this.setInfestedVillagerState(1);
            }

            int i = this.getInfestedVillagerState();

            if (i > 0 && this.timeSinceIgnited == 0)
            {
                this.playSound(SoundEvents.ENTITY_CREEPER_PRIMED, 1.0F, 0.5F);
            }

            this.timeSinceIgnited += i;

            if (this.timeSinceIgnited < 0)
            {
                this.timeSinceIgnited = 0;
            }

            if (this.timeSinceIgnited >= this.fuseTime)
            {
                this.timeSinceIgnited = this.fuseTime;
                this.explode();
            }
        }

        super.onUpdate();
    }



}
