package com.zeplar.zeplarszombies.Monsters;

import com.zeplar.zeplarszombies.Fortress;
import com.zeplar.zeplarszombies.ModInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryNamespaced;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.VillagerRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.GameData;

import javax.annotation.Nullable;

public class EntityInfestedVillager extends EntityVillager implements IMob {

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
        //super(worldIn, 6);
        setSize(0.6F, 1.95F);
    }


    public static void init() {
        RegistryNamespaced<ResourceLocation, VillagerRegistry.VillagerProfession> REGISTRY = GameData.getWrapper(VillagerRegistry.VillagerProfession.class);

        VillagerRegistry.VillagerProfession prof = new VillagerRegistry.VillagerProfession("zeplarszombies:infested",
                "minecraft:textures/entity/villager/villager.png",
                "minecraft:textures/entity/zombie_villager/zombie_villager.png");
        {
            REGISTRY.register(6, new ResourceLocation("zeplarszombies:infested"), prof);


            ITradeList tradeList = new EmeraldForItems(Items.STRING, new PriceInfo(4,8));

            new VillagerRegistry.VillagerCareer(prof,"zeplarszombies:infested").addTrade(0, tradeList);
        }
    }

    @Override
    public boolean getCanSpawnHere()
    {
        if (super.getCanSpawnHere())
        {
            return Fortress.isInFortress(this.getPosition());
        }
        return false;
    }

    public void replaceWithVillager()
    {
        EntityVillager villager = new EntityVillager(world);
        villager.setPosition(this.posX,this.posY,this.posZ);
        world.spawnEntity(villager);
    }

    @Override
    protected void initEntityAI() {
        super.initEntityAI();
        this.tasks.addTask(2, new EntityAIInfestedVillagerSwell(this));
        this.tasks.addTask(6, new EntityAIWander(this, 0.7));
        this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
    }



    @Override
    protected void entityInit()
    {
        super.entityInit();
        this.dataManager.register(STATE, -1);
        this.dataManager.register(IGNITED, false);
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
        return (this.dataManager.get(STATE));
    }

    /**
     * Sets the state of creeper, -1 to idle and 1 to be 'in fuse'
     */
    public void setInfestedVillagerState(int state)
    {
        this.dataManager.set(STATE, state);
    }


    public boolean isPreventingPlayerRest(EntityPlayer p_191990_1_)
    {
        return false;
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
        return (this.dataManager.get(IGNITED));
    }

    public void ignite()
    {
        this.dataManager.set(IGNITED, true);
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
