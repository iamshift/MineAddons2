package com.iamshift.mineaddons.items.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;
import com.iamshift.mineaddons.MineAddons;
import com.iamshift.mineaddons.blocks.BlockInvLight;
import com.iamshift.mineaddons.core.Refs;
import com.iamshift.mineaddons.init.ModItems;
import com.iamshift.mineaddons.interfaces.IHasModel;
import com.iamshift.mineaddons.interfaces.IRecipeProvider;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTool;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.LeftClickBlock;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;

@Mod.EventBusSubscriber(modid = Refs.ID)
public class ItemBreaker extends ItemTool implements IHasModel, IRecipeProvider
{
	private static final Set<Block> EFFECTIVE_ON = Sets.newHashSet(Blocks.BEDROCK);

	public ItemBreaker(String name)
	{
		super(0F, 0F, Item.ToolMaterial.WOOD, EFFECTIVE_ON);
		setRegistryName(new ResourceLocation(Refs.ID, name));
		setMaxStackSize(1);
		setMaxDamage(1500);
		
		setCreativeTab(MineAddons.minetab);

		ModItems.ITEMS.add(this);
	}

	@Override
	public boolean canHarvestBlock(IBlockState blockIn)
	{
		return false;
	}

	public float getDestroySpeed(ItemStack stack, IBlockState state)
	{
		return 0F;
	}

	@Override
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
	{
		return false;
	}

	@Override
	public boolean canHarvestBlock(IBlockState state, ItemStack stack)
	{
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		super.addInformation(stack, worldIn, tooltip, flagIn);
		tooltip.add(TextFormatting.DARK_AQUA + "Can break the unbreakable and nothing else.");
	}

	@Override
	public boolean hasEffect(ItemStack stack)
	{
		return true;
	}

	@Override
	public boolean isEnchantable(ItemStack stack)
	{
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isFull3D()
	{
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModels() 
	{
		MineAddons.proxy.registerItemRenderer(this, 0, "inventory");
	}

	@Override
	public List<IRecipe> getRecipe()
	{
		List<IRecipe> list = new ArrayList<IRecipe>();
		list.add(
				new ShapedOreRecipe(new ResourceLocation(Refs.ID), 
						new ItemStack(this, 1), 
						new Object[] {
								" DS", " BD", "B  ", 
								'D', new ItemStack(ModItems.EnchantedItems, 1, 1), 
								'S', new ItemStack(ModItems.EnchantedItems, 1, 2),
								'B', new ItemStack(ModItems.EnchantedItems, 1, 0)
				}).setRegistryName(new ResourceLocation(Refs.ID, "breaker"))
				);

		return list;

	}

	@SubscribeEvent
	public static void onBlockMined(LeftClickBlock event)
	{
		EntityPlayer player = event.getEntityPlayer();
		World world = player.world;

		if(!player.capabilities.isCreativeMode)
		{
			if((player.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemBreaker))
			{
				if(!world.isRemote)
				{
					BlockPos pos = event.getPos();
					IBlockState state = world.getBlockState(pos);
					boolean unbreakable = state.getBlockHardness(world, pos) < 0.0F ? true : false;

					if(!unbreakable && !(state.getBlock() instanceof BlockInvLight))
					{
						event.setCanceled(true);
						return;
					}

					world.setBlockToAir(pos);
					world.playEvent(2001, pos, Block.getStateId(state));
					
					event.getItemStack().damageItem(1, player);

					ItemStack stack = new ItemStack(Item.getItemFromBlock(state.getBlock()), 1, state.getBlock().getMetaFromState(state));
					world.spawnEntity(new EntityItem(world, pos.getX(), pos.getY(), pos.getZ(), stack));
				}
			}
		}
	}	
}
