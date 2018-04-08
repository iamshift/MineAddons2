package com.iamshift.mineaddons.entities.boss;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import com.iamshift.mineaddons.init.ModSounds;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BossInfo;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;

public abstract class EntityBoss extends EntityMob
{
	private final BossInfoServer bossInfo1 = (BossInfoServer)(new BossInfoServer(this.getDisplayName(), BossInfo.Color.RED, BossInfo.Overlay.PROGRESS)).setDarkenSky(true);
	private final BossInfoServer bossInfo2 = (BossInfoServer)(new BossInfoServer(this.getDisplayName(), BossInfo.Color.YELLOW, BossInfo.Overlay.PROGRESS)).setDarkenSky(true);
	private final BossInfoServer bossInfo3 = (BossInfoServer)(new BossInfoServer(this.getDisplayName(), BossInfo.Color.GREEN, BossInfo.Overlay.PROGRESS)).setDarkenSky(true);
	
	private static final DataParameter<Integer> INVULNERABILITY_TIME = EntityDataManager.<Integer>createKey(EntityBoss.class, DataSerializers.VARINT);
	
	private boolean stage1 = false;
	private boolean stage2 = false;
	private boolean stage3 = false;
	
	public List<EntityPlayerMP> players = new ArrayList<EntityPlayerMP>();
	
	private static final DataParameter<Byte> SPELL = EntityDataManager.<Byte>createKey(EntityBoss.class, DataSerializers.BYTE);
    protected int spellTicks;
    private EntityBoss.SpellType activeSpell = EntityBoss.SpellType.NONE;
	
	public EntityBoss(World worldIn)
	{
		super(worldIn);
	}
	
	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(10.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(450.0F);
	}
	
	public int getInvulTime()
	{
		return ((Integer)this.dataManager.get(INVULNERABILITY_TIME)).intValue();
	}

	public void setInvulTime(int time)
	{
		this.dataManager.set(INVULNERABILITY_TIME, Integer.valueOf(time));
	}
	
	@Override
	public void addTrackingPlayer(EntityPlayerMP player)
	{
		super.addTrackingPlayer(player);

		if(!players.contains(player))
			players.add(player);

		if(getStage() == 3)
		{
			this.bossInfo1.addPlayer(player);
			this.bossInfo2.removePlayer(player);
			this.bossInfo3.removePlayer(player);
		}
		else if(getStage() == 2)
		{
			this.bossInfo2.addPlayer(player);
			this.bossInfo3.removePlayer(player);
		}
		else
		{
			this.bossInfo3.addPlayer(player);
		}
	}
	
	@Override
	public void removeTrackingPlayer(EntityPlayerMP player)
	{
		super.removeTrackingPlayer(player);

		this.bossInfo1.removePlayer(player);
		this.bossInfo2.removePlayer(player);
		this.bossInfo3.removePlayer(player);

		if(players.contains(player))
			players.remove(player);
	}
	
	public void updateBars()
	{
		for (EntityPlayerMP p : players)
			this.addTrackingPlayer(p);
	}
	
	public int getStage()
	{
		if(this.getHealth() <= 150F)
			return 3;
		else if(this.getHealth() > 150F && this.getHealth() <= 300F)
			return 2;
		else
			return 1;
	}
	
	public void setBossBar(float perc, int bar)
	{
		switch(bar)
		{
			case 1:
				this.bossInfo1.setPercent(perc);
				return;
			case 2:
				this.bossInfo2.setPercent(perc);
				return;
			case 3:
				this.bossInfo3.setPercent(perc);
				return;
			default:
				return;
		}
	}
	
	@Override
	public boolean isEntityInvulnerable(DamageSource source)
	{
		if(this.getInvulTime() > 0)
			return true;
		
		return super.isEntityInvulnerable(source);
	}
	
	@Override
	public boolean getIsInvulnerable()
	{
		if(this.getInvulTime() > 0)
			return true;
		
		return super.getIsInvulnerable();
	}
	
	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		
		if(this.getIsInvulnerable())
		{
			for (int i1 = 0; i1 < 3; ++i1)
				this.world.spawnParticle(EnumParticleTypes.SPELL_MOB, this.posX + this.rand.nextGaussian(), this.posY + (double)(this.rand.nextFloat() * 3.3F), this.posZ + this.rand.nextGaussian(), 0.699999988079071D, 0.699999988079071D, 0.8999999761581421D);
			
			if(getStage() == 1 && (this.getInvulTime() % 20) == 0)
			{
				float v = (1.0F - ((this.getInvulTime() / 20.0F) / 10.0F));
				this.world.playSound((EntityPlayer)null, this.posX, this.posY, this.posZ, ModSounds.bossfight, SoundCategory.NEUTRAL, 1000.0F, v);
			}
		}
		else
		{
			if(players.size() > 0)
			{
				EntityPlayer p = players.get(this.rand.nextInt(players.size()));
				if(!p.capabilities.isCreativeMode)
					this.setAttackTarget(p);
				else
					this.setAttackTarget(null);
			}
			
			if(getStage() == 1) //450
			{
				if(!this.stage1)
				{
					this.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(450.0F);
					this.setHealth(450F);
					this.stage1 = true;
				}

				this.bossInfo3.setPercent((this.getHealth() - 300F) / 150F);
			}
			else if(getStage() == 2) //300
			{
				if(!this.stage2)
				{
					this.setHealth(300F);
					this.stage2 = true;
				}

				this.bossInfo2.setPercent((this.getHealth() - 150F) / 150F);
			}
			else if(getStage() == 3) //150
			{
				if(!this.stage3)
				{
					this.setHealth(150F);
					this.stage3 = true;
				}

				this.bossInfo1.setPercent((this.getHealth()) / 150F);
			}
		}
	}
	
	@Override
	protected void entityInit()
	{
		super.entityInit();
		this.dataManager.register(INVULNERABILITY_TIME, Integer.valueOf(0));
		this.dataManager.register(SPELL, Byte.valueOf((byte)0));
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		super.readEntityFromNBT(compound);
		this.spellTicks = compound.getInteger("SpellTicks");
		
		this.stage1 = compound.getBoolean("Stage1");
		this.stage2 = compound.getBoolean("Stage2");
		this.stage3 = compound.getBoolean("Stage3");
		
		this.setInvulTime(compound.getInteger("Invul"));
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		super.writeEntityToNBT(compound);
		compound.setInteger("SpellTicks", this.spellTicks);
		
		compound.setBoolean("Stage1", this.stage1);
		compound.setBoolean("Stage2", this.stage2);
		compound.setBoolean("Stage3", this.stage3);
		
		compound.setInteger("Invul", this.getInvulTime());
	}
	
	public boolean isSpellcasting()
	{
		if(this.world.isRemote)
			return ((Byte)this.dataManager.get(SPELL)).byteValue() > 0;
		else
			return this.spellTicks > 0;
	}
	
	public void setSpellType(EntityBoss.SpellType spellType)
	{
		this.activeSpell = spellType;
		this.dataManager.set(SPELL, Byte.valueOf((byte)spellType.id));
	}
	
	protected EntityBoss.SpellType getSpellType()
	{
		return !this.world.isRemote ? this.activeSpell : EntityBoss.SpellType.getFromId(((Byte)this.dataManager.get(SPELL)).byteValue());
	}
	
	@Override
	protected void updateAITasks()
	{
		if(this.getIsInvulnerable())
		{
			int i = this.getInvulTime() - 1;
			this.setInvulTime(i);
		}
		else
		{
			super.updateAITasks();
			updateBars();
			
			if(this.spellTicks > 0)
				--this.spellTicks;
		}
	}
	
	@Override
	public void onUpdate()
	{
		super.onUpdate();
	}
	
	protected int getSpellTicks()
	{
		return this.spellTicks;
	}
	
	protected abstract SoundEvent getSpellSound();
	
	public class AICastingSpell extends EntityAIBase
	{
		public AICastingSpell()
		{
			this.setMutexBits(3);
		}
		
		@Override
		public boolean shouldExecute()
		{
			if(EntityBoss.this.getIsInvulnerable())
				return false;
			else
				return EntityBoss.this.getSpellTicks() > 0;
		}
		
		@Override
		public void startExecuting() 
		{
			super.startExecuting();
			EntityBoss.this.navigator.clearPath();
		}
		
		@Override
		public void resetTask()
		{
			super.resetTask();
			EntityBoss.this.setSpellType(EntityBoss.SpellType.NONE);
		}
		
		@Override
		public void updateTask()
		{
			if(EntityBoss.this.getAttackTarget() != null)
				EntityBoss.this.getLookHelper().setLookPositionWithEntity(EntityBoss.this.getAttackTarget(), (float)EntityBoss.this.getHorizontalFaceSpeed(), (float)EntityBoss.this.getVerticalFaceSpeed());
		}
	}
	
	public abstract class AIUseSpell extends EntityAIBase
	{
		protected int spellWarmup;
		protected int spellCooldown;
		
		@Override
		public boolean shouldExecute()
		{
			if(EntityBoss.this.getAttackTarget() == null)
				return false;
			else if(EntityBoss.this.isSpellcasting())
				return false;
			else if(EntityBoss.this.getInvulTime() > 0)
				return false;
			else
				return EntityBoss.this.ticksExisted >= this.spellCooldown;
		}
		
		@Override
		public boolean shouldContinueExecuting()
		{
			return EntityBoss.this.getAttackTarget() != null && this.spellWarmup > 0;
		}
		
		@Override
		public void startExecuting()
		{
			this.spellWarmup = this.getCastWarmupTime();
			EntityBoss.this.spellTicks = this.getCastingTime();
			this.spellCooldown = EntityBoss.this.ticksExisted + this.getCastingInterval();
			SoundEvent soundevent = this.getSpellPrepareSound();
			
			if(soundevent != null)
				EntityBoss.this.playSound(soundevent, 1.0F, 1.0F);
			
			EntityBoss.this.setSpellType(this.getSpellType());
		}
		
		@Override
		public void updateTask()
		{
			--this.spellWarmup;
			
			if(this.spellWarmup == 0)
			{
				this.castSpell();
				EntityBoss.this.playSound(EntityBoss.this.getSpellSound(), 1.0F, 1.0F);
			}
		}
		
		protected abstract void castSpell();
		
		protected int getCastWarmupTime()
		{
			return 20;
		}
		
		protected abstract int getCastingTime();
		
		protected abstract int getCastingInterval();
		
		@Nullable
		protected abstract SoundEvent getSpellPrepareSound();
		
		protected abstract EntityBoss.SpellType getSpellType();
	}
	
	public static enum SpellType
    {
        NONE(0, 0.0D, 0.0D, 0.0D),
        SUMMON(1, 0.7D, 0.7D, 0.8D),
        SPELL(2, 0.0D, 0.0D, 0.0D);

        private final int id;
        /** Particle motion speed. An array with 3 values: x, y, and z. */
        private final double[] particleSpeed;

        private SpellType(int idIn, double xParticleSpeed, double yParticleSpeed, double zParticleSpeed)
        {
            this.id = idIn;
            this.particleSpeed = new double[] {xParticleSpeed, yParticleSpeed, zParticleSpeed};
        }

        public static EntityBoss.SpellType getFromId(int idIn)
        {
            for (EntityBoss.SpellType spelltype : values())
            {
                if (idIn == spelltype.id)
                {
                    return spelltype;
                }
            }

            return NONE;
        }
    }
}
