package com.iamshift.mineaddons.utils;

import com.iamshift.mineaddons.blocks.BlockForgottenAnvil;
import com.iamshift.mineaddons.blocks.containers.ContainerForgottenAnvil;
import com.iamshift.mineaddons.blocks.containers.GuiForgottenAnvil;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler
{
	public enum GUI
	{
		ANVIL;
	}
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if(ID == GUI.ANVIL.ordinal())
			return world.getBlockState(new BlockPos(x, y, z)).getBlock() instanceof BlockForgottenAnvil ? 
					new ContainerForgottenAnvil(player.inventory, world, new BlockPos(x, y, z), player) : null;
		
		return null;
	}
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		if(ID == GUI.ANVIL.ordinal())
			return world.getBlockState(new BlockPos(x, y, z)).getBlock() instanceof BlockForgottenAnvil ?
					new GuiForgottenAnvil(player.inventory, world) : null;
					
		return null;
	}
}
