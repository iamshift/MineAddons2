package com.iamshift.mineaddons.integration.tinkers;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.MobEffects;
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
		if(event.getEntityPlayer().isPotionActive(MobEffects.MINING_FATIGUE))
		{
			event.setNewSpeed(1.0F);
			return;
		}
		
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
			{
				if((mytool.contains("pickaxe") || mytool.contains("hammer")) && 
					(state.getMaterial() == Material.ROCK ||
					   state.getMaterial() == Material.IRON ||
					   state.getMaterial() == Material.ANVIL ||
					   state.getMaterial() == Material.ICE ||
					   state.getMaterial() == Material.PACKED_ICE ||
					   state.getMaterial() == Material.PISTON ||
					   state.getMaterial() == Material.REDSTONE_LIGHT))
				{
						speed = 50;
				}
				else if((mytool.contains("hatchet") || mytool.contains("mattock") || mytool.contains("lumberaxe")) &&
					(state.getMaterial() == Material.WOOD ||
					   state.getMaterial() == Material.GOURD ||
					   state.getMaterial() == Material.CACTUS ||
					   state.getMaterial() == Material.LEAVES))
				{
						speed = 50;
				}
				else if((mytool.contains("mattock") || mytool.contains("shovel") || mytool.contains("excavator")) &&
					(state.getMaterial() == Material.GRASS ||
					   state.getMaterial() == Material.GROUND ||
					   state.getMaterial() == Material.SAND ||
					   state.getMaterial() == Material.SNOW ||
					   state.getMaterial() == Material.CLAY))
				{
						speed = 50;
				}
				else if((mytool.contains("sword") || mytool.contains("cleaver") || mytool.contains("rapier")) &&
					(state.getMaterial() == Material.PLANTS ||
					   state.getMaterial() == Material.VINE ||
					   state.getMaterial() == Material.CORAL ||
					   state.getMaterial() == Material.LEAVES ||
					   state.getMaterial() == Material.GOURD ||
					   state.getMaterial() == Material.WEB))
				{
						speed = 50;
				}
				else if(state.getMaterial() == Material.GLASS)
					speed = 50;
				else
					speed = 10;
			}
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
				else if(toolType.equals("sword") && mytool.contains("sword"))
					speed = 50;
				else if(toolType.equals("sword") && mytool.contains("cleaver"))
					speed = 50;
				else if(toolType.equals("sword") && mytool.contains("rapier"))
					speed = 50;
			}
			
			if(hard == 0)
				hard = 1;
			
			event.setNewSpeed(hard * speed);
		}
	}
}
