package com.iamshift.mineaddons.integration.tinkers;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import slimeknights.tconstruct.library.traits.AbstractTrait;

public class TraitLightspeed extends AbstractTrait
{
	public TraitLightspeed()
	{
		super("lightspeed", TextFormatting.WHITE);
	}

	@Override
	public void miningSpeed(ItemStack tool, BreakSpeed event)
	{
		World world = event.getEntityPlayer().getEntityWorld();
		BlockPos pos = event.getPos();
		IBlockState state = world.getBlockState(pos);
		float hard = state.getBlockHardness(world, pos);

		Block block = state.getBlock();
		if(block != null)
		{
			String toolType = block.getHarvestTool(state) == null ? "default" : block.getHarvestTool(state).toLowerCase();
			String mytool = tool.getDisplayName().toLowerCase();

			int speed = 1;
			
			if(toolType.equals("default"))
				speed = 25;
			else
			{
				if(toolType.equals("pickaxe") && mytool.contains("pickaxe"))
					speed = 50;
				else if(toolType.equals("pickaxe") && mytool.contains("hammer"))
					speed = 50;
				else if(toolType.equals("axe") && mytool.contains("hatchet"))
					speed = 50;
				else if(toolType.equals("axe") && mytool.contains("mattock"))
					speed = 50;
				else if(toolType.equals("axe") && mytool.contains("lumberaxe"))
					speed = 50;
				else if(toolType.equals("shovel") && mytool.contains("mattock"))
					speed = 50;
				else if(toolType.equals("shovel") && mytool.contains("shovel"))
					speed = 50;
				else if(toolType.equals("shovel") && mytool.contains("excavator"))
					speed = 50;
			}

			event.setNewSpeed(hard * speed);
		}
	}
}
