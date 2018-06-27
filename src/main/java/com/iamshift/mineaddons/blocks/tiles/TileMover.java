package com.iamshift.mineaddons.blocks.tiles;

import com.iamshift.mineaddons.core.Config;

import net.minecraft.block.Block;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileMover extends TileEntity
{
	private BlockPos target;
	private EnumFacing face;
	private int range = 1;
	
	public void updateBlock()
	{
		if(target == null || face == null)
			return;

		EnumFacing op = face.getOpposite();
		BlockPos rangePos = new BlockPos(target.getX(), target.getY(), target.getZ()).offset(op, range);
		BlockPos targetPos = new BlockPos(target.getX(), target.getY(), target.getZ());

		if(world.isBlockPowered(pos))
		{
			if (world.getBlockState(targetPos).getBlock() == Blocks.AIR || 
					!BlockPistonBase.canPush(world.getBlockState(targetPos), world, targetPos, op, false, op) || 
					!world.getChunkFromBlockCoords(targetPos).isLoaded() || 
					world.getBlockState(rangePos).getBlock() != Blocks.AIR)
				return;
			
			world.setBlockState(rangePos, world.getBlockState(targetPos));
			world.setBlockToAir(targetPos);
		}
		else
		{
			if (world.getBlockState(rangePos).getBlock() == Blocks.AIR || 
					!BlockPistonBase.canPush(world.getBlockState(rangePos), world, rangePos, op, false, op) || 
					!world.getChunkFromBlockCoords(rangePos).isLoaded() || 
					world.getBlockState(targetPos).getBlock() != Blocks.AIR)
				return;
			
			world.setBlockState(targetPos, world.getBlockState(rangePos));
			world.setBlockToAir(rangePos);
		}
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
	{
		return oldState.getBlock() != newSate.getBlock();
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		super.writeToNBT(compound);

		NBTTagCompound tag = new NBTTagCompound();

		tag.setInteger("wrange", this.range);

		if(target != null)
		{
			tag.setInteger("targetX", target.getX());
			tag.setInteger("targetY", target.getY());
			tag.setInteger("targetZ", target.getZ());
			tag.setInteger("targetF", face.getIndex());
		}

		compound.setTag("wtarget", tag);
		return compound;
	}

	public void readFromNBT(NBTTagCompound compound) 
	{
		super.readFromNBT(compound);

		NBTTagCompound tag = compound.getCompoundTag("wtarget");

		this.range = tag.getInteger("wrange");

		if(tag.hasKey("targetX"))
		{
			face = EnumFacing.values()[tag.getInteger("targetF")];
			target = new BlockPos(tag.getInteger("targetX"), tag.getInteger("targetY"), tag.getInteger("targetZ"));
		}
	}

	@Override
	public NBTTagCompound getUpdateTag()
	{
		NBTTagCompound tag = super.getUpdateTag();
		writeToNBT(tag);
		return tag;
	}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket()
	{
		return new SPacketUpdateTileEntity(getPos(), 0, this.getUpdateTag());
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt)
	{
		handleUpdateTag(pkt.getNbtCompound());
	}

	@Override
	public void handleUpdateTag(NBTTagCompound tag)
	{
		readFromNBT(tag);
	}

	public void setTarget(BlockPos linkTo, int faceIndex)
	{
		if(!linkTo.equals(target))
		{
			target = linkTo;
			face = EnumFacing.values()[faceIndex];

			IBlockState state = this.world.getBlockState(this.pos);
			world.notifyBlockUpdate(this.pos, state, state, 3);
			state.neighborChanged(world, this.pos, state.getBlock(), this.pos);
			world.notifyNeighborsOfStateChange(this.pos, state.getBlock(), false);
		}
	}

	public void addRange(int i)
	{
		this.range += i;
		
		if(this.range < 1)
			this.range = 1;
		
		if(this.range > Config.DistanceLimit)
			this.range = Config.DistanceLimit;
	}

	public int getRange()
	{
		return this.range;
	}
	
	public Block getTarget()
	{
		if (target == null)
			return Blocks.AIR;
		
		EnumFacing op = face.getOpposite();
		BlockPos rangePos = new BlockPos(target.getX(), target.getY(), target.getZ()).offset(op, range);
		BlockPos targetPos = new BlockPos(target.getX(), target.getY(), target.getZ());
		
		return world.getBlockState(targetPos).getBlock() == Blocks.AIR ? world.getBlockState(rangePos).getBlock() : world.getBlockState(targetPos).getBlock();
	}
	
	public boolean hasTarget()
	{
		if (target == null || face == null)
			return false;
		
		EnumFacing op = face.getOpposite();
		BlockPos rangePos = new BlockPos(target.getX(), target.getY(), target.getZ()).offset(op, range);
		BlockPos targetPos = new BlockPos(target.getX(), target.getY(), target.getZ());
		
		if (world.getBlockState(targetPos).getBlock() == Blocks.AIR)
			return world.getBlockState(rangePos) != null && world.getBlockState(rangePos).getBlock() != Blocks.AIR;
		else
			return world.getBlockState(targetPos) != null && world.getBlockState(targetPos).getBlock() != Blocks.AIR;
	}
	
	public void clearTarget()
	{
		target = null;
		face = null;
	}
	
	public BlockPos getTargetPos()
	{
		return target;
	}
	
	public EnumFacing getDirection()
	{
		return face;
	}
}
