package com.iamshift.mineaddons.entities.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.init.MobEffects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntitySpiritBomb extends Entity
{
	private static final DataParameter<Float> SCALE = EntityDataManager.<Float>createKey(EntitySpiritBomb.class, DataSerializers.FLOAT);

	private boolean shoot = false;

	public EntityLivingBase shootingEntity;
	private int ticksAlive;
	private int ticksInAir;
	public double accelerationX;
	public double accelerationY;
	public double accelerationZ;

	public EntitySpiritBomb(World worldIn)
	{
		super(worldIn);
		this.setSize(6.0F, 6.0F);
	}

	public EntitySpiritBomb(World worldIn, EntityLivingBase shooter, double accelX, double accelY, double accelZ, float scale)
	{
		super(worldIn);
		this.setSize(6.0F, 6.0F);
		this.setScale(scale);
		this.shootingEntity = shooter;
		this.motionX = 0.0D;
		this.motionY = 0.0D;
		this.motionZ = 0.0D;
		accelX = accelX + this.rand.nextGaussian() * 0.4D;
		accelY = accelY + this.rand.nextGaussian() * 0.4D;
		accelZ = accelZ + this.rand.nextGaussian() * 0.4D;
		double d0 = (double)MathHelper.sqrt(accelX * accelX + accelY * accelY + accelZ * accelZ);
		this.accelerationX = accelX / d0 * 0.1D;
		this.accelerationY = accelY / d0 * 0.1D;
		this.accelerationZ = accelZ / d0 * 0.1D;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isInRangeToRenderDist(double distance)
	{
		double d0 = this.getEntityBoundingBox().getAverageEdgeLength() * 4.0D;

		if (Double.isNaN(d0))
		{
			d0 = 4.0D;
		}

		d0 = d0 * 128.0D;
		return distance < d0 * d0;
	}

	@Override
	protected void entityInit()
	{
		this.dataManager.register(SCALE, Float.valueOf(0.3F));
	}

	@Override
	public boolean isBurning()
	{
		return false;
	}

	@Override
	public boolean canBeCollidedWith()
	{
		return this.shoot;
	}

	@Override
	public float getCollisionBorderSize()
	{
		return 6.0F;
	}

		@Override
		public void onCollideWithPlayer(EntityPlayer entityIn)
		{
			super.onCollideWithPlayer(entityIn);
	
			if (!this.world.isRemote && this.shoot)
			{
				int i = 1;
	
				if (this.world.getDifficulty() == EnumDifficulty.NORMAL)
				{
					i = 10;
				}
				else if (this.world.getDifficulty() == EnumDifficulty.HARD)
				{
					i = 40;
				}
	
				entityIn.addPotionEffect(new PotionEffect(MobEffects.WITHER, 40 * i, 1));
				entityIn.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 40 * i, 1));
	
				boolean flag = this.world.getGameRules().getBoolean("mobGriefing");
				this.world.newExplosion((Entity)null, this.posX, this.posY, this.posZ, 3.0F, flag, flag);
				this.setDead();
			}
		}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		return false;
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound compound)
	{
		if(compound.hasKey("Scale"))
			this.setScale(compound.getFloat("Scale"));

		if(compound.hasKey("Shoot"))
			this.shoot = compound.getBoolean("Shoot");

		if (compound.hasKey("power", 9))
		{
			NBTTagList nbttaglist = compound.getTagList("power", 6);

			if (nbttaglist.tagCount() == 3)
			{
				this.accelerationX = nbttaglist.getDoubleAt(0);
				this.accelerationY = nbttaglist.getDoubleAt(1);
				this.accelerationZ = nbttaglist.getDoubleAt(2);
			}
		}

		this.ticksAlive = compound.getInteger("life");

		if (compound.hasKey("direction", 9) && compound.getTagList("direction", 6).tagCount() == 3)
		{
			NBTTagList nbttaglist1 = compound.getTagList("direction", 6);
			this.motionX = nbttaglist1.getDoubleAt(0);
			this.motionY = nbttaglist1.getDoubleAt(1);
			this.motionZ = nbttaglist1.getDoubleAt(2);
		}
		else
		{
			this.setDead();
		}
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound)
	{
		compound.setTag("direction", this.newDoubleNBTList(new double[] {this.motionX, this.motionY, this.motionZ}));
		compound.setTag("power", this.newDoubleNBTList(new double[] {this.accelerationX, this.accelerationY, this.accelerationZ}));
		compound.setInteger("life", this.ticksAlive);

		compound.setFloat("Scale", this.getScale());
		compound.setBoolean("Shoot", this.shoot);
	}

	@Override
	public float getBrightness()
	{
		return 1.0F;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getBrightnessForRender()
	{
		return 15728880;
	}

	public void setScale(float s)
	{
		this.dataManager.set(SCALE, Float.valueOf(s));
	}

	public float getScale()
	{
		return ((Float)this.dataManager.get(SCALE)).floatValue();
	}

	public void shoot()
	{
		this.shoot = true;
	}

	@Override
	public void onUpdate()
	{
		if(this.world.getDifficulty() == EnumDifficulty.PEACEFUL)
			this.setDead();

		if(this.shoot)
		{
			if (this.world.isRemote || (this.shootingEntity == null || !this.shootingEntity.isDead) && this.world.isBlockLoaded(new BlockPos(this)))
			{
				super.onUpdate();

				++this.ticksInAir;
				RayTraceResult raytraceresult = ProjectileHelper.forwardsRaycast(this, true, this.ticksInAir >= 25, this.shootingEntity);

				if (raytraceresult != null && !net.minecraftforge.event.ForgeEventFactory.onProjectileImpact(this, raytraceresult))
				{
					this.onImpact(raytraceresult);
				}

				this.posX += this.motionX;
				this.posY += this.motionY;
				this.posZ += this.motionZ;
				ProjectileHelper.rotateTowardsMovement(this, 0.2F);
				float f = 0.95F;

				this.motionX += this.accelerationX;
				this.motionY += this.accelerationY;
				this.motionZ += this.accelerationZ;
				this.motionX *= (double)f;
				this.motionY *= (double)f;
				this.motionZ *= (double)f;
				this.setPosition(this.posX, this.posY, this.posZ);

//				this.doBlockCollisions();
			}
			else
			{
				this.setDead();
			}
		}
		else
		{
			if(this.world.isRemote)
			{
				double mX, mY, mZ, pX, pY, pZ;
				int j;
				for(int i = 0; i < 4; i++)
				{
					pX = (double)((float)posX + rand.nextFloat());
					pY = (double)((float)posY + rand.nextFloat());
					pZ = (double)((float)posZ + rand.nextFloat());
					mX = ((double)rand.nextFloat() - 0.5D) * 0.5D;;
					mY = ((double)rand.nextFloat() - 0.5D) * 0.5D;;
					mZ = ((double)rand.nextFloat() - 0.5D) * 0.5D;;
					j = rand.nextInt(4) * 2 - 1;

					if(rand.nextInt(2) == 0)
					{
						pX = (double)posX + 0.5D + 0.25D * (double)j;
						mX = (double)(rand.nextFloat() * 2.0F * (float)j);
					}
					else
					{
						pZ = (double)posZ + 0.5D + 0.25D * (double)j;
						mZ = (double)(rand.nextFloat() * 2.0F * (float)j);
					}

					if(rand.nextInt(2) == 0)
					{
						pY = -pY;
						mY = -mY;
					}

					world.spawnParticle(EnumParticleTypes.PORTAL, pX, pY, pZ, mX, mY, mZ);
				}
			}
		}
	}

	private void onImpact(RayTraceResult result)
	{
		if(!this.world.isRemote && this.shoot)
		{
			if(result.entityHit != null && result.entityHit instanceof EntityLivingBase)
			{
				EntityLivingBase entity = (EntityLivingBase) result.entityHit;
				int i = 1;

				if (this.world.getDifficulty() == EnumDifficulty.NORMAL)
				{
					i = 10;
				}
				else if (this.world.getDifficulty() == EnumDifficulty.HARD)
				{
					i = 40;
				}

				entity.addPotionEffect(new PotionEffect(MobEffects.WITHER, 40 * i, 1));
				entity.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 40 * i, 1));
			}
			
			boolean flag = this.world.getGameRules().getBoolean("mobGriefing");
			this.world.newExplosion((Entity)null, this.posX, this.posY, this.posZ, 3.0F, flag, flag);
			this.setDead();
		}
	}
}
