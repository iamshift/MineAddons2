package com.iamshift.mineaddons.integration.waila;

import java.util.List;

import com.iamshift.mineaddons.blocks.tiles.TileMover;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

public class WailaProviderMover implements IWailaDataProvider
{
	@Override
	public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) { return null ; }
	
	@Override
	public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)  { return null ; }
	
	@Override
	public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)  { return null ; }
	
	@Override
	public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, BlockPos pos)  { return null ; }
	
	@Override
	public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
	{
		TileEntity te = accessor.getWorld().getTileEntity(accessor.getPosition());
		if(te != null && te instanceof TileMover && ((TileMover)te).hasTarget())
		{
			currenttip.add("");
			
			currenttip.add(TextFormatting.DARK_AQUA + "Target Block: " + TextFormatting.WHITE + ((TileMover)te).getTarget().getLocalizedName());
			currenttip.add(TextFormatting.DARK_AQUA + "Posistion: " + TextFormatting.WHITE + ((TileMover)te).getTargetPos().getX() + " " + ((TileMover)te).getTargetPos().getY() + " " + ((TileMover)te).getTargetPos().getZ());
			currenttip.add(TextFormatting.DARK_AQUA + "Direction: " + TextFormatting.WHITE + ((TileMover)te).getDirection().getOpposite().getName());
			currenttip.add(TextFormatting.DARK_AQUA + "Range: " + TextFormatting.WHITE + ((TileMover)te).getRange());
			
			currenttip.add("");
		}
		
		return currenttip;
	}
}
