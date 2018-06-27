package com.iamshift.mineaddons.blocks;

import java.util.ArrayList;
import java.util.List;

import com.iamshift.mineaddons.MineAddons;
import com.iamshift.mineaddons.core.Refs;
import com.iamshift.mineaddons.init.ModBlocks;
import com.iamshift.mineaddons.init.ModFluids;
import com.iamshift.mineaddons.init.ModItems;
import com.iamshift.mineaddons.interfaces.IHasModel;
import com.iamshift.mineaddons.interfaces.IMetaName;
import com.iamshift.mineaddons.interfaces.IRecipeProvider;
import com.iamshift.mineaddons.utils.ItemBlockVariants;

import net.minecraft.block.BlockSandStone;
import net.minecraft.block.SoundType;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class BlockBlackSandStone extends BlockSandStone implements IHasModel, IRecipeProvider, IMetaName
{
	public BlockBlackSandStone(String name)
	{
		super();
		setUnlocalizedName(name);
		setRegistryName(new ResourceLocation(Refs.ID, name));
		setHardness(0.8F);
		setSoundType(SoundType.STONE);
		setHarvestLevel("pickaxe", 0);
		
		setCreativeTab(MineAddons.minetab);

		ModBlocks.BLOCKS.add(this);
		ModItems.ITEMS.add(new ItemBlockVariants(this).setRegistryName(this.getRegistryName()));
	}

	@Override
	public String getSpecialName(ItemStack stack)
	{
		return EnumType.byMetadata(stack.getMetadata()).getUnlocalizedName();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModels()
	{
		MineAddons.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
		MineAddons.proxy.registerItemRenderer(Item.getItemFromBlock(this), 1, "inventory");
		MineAddons.proxy.registerItemRenderer(Item.getItemFromBlock(this), 2, "inventory");
	}

	@Override
	public List<IRecipe> getRecipe()
	{
		List<IRecipe> list = new ArrayList<IRecipe>();

		list.add(
				new ShapedOreRecipe(new ResourceLocation(Refs.ID), 
						new ItemStack(this, 1, 0), 
						new Object[] {
								"SS", "SS",  
								'S', new ItemStack(ModBlocks.BlackSand)
				}).setRegistryName(new ResourceLocation(Refs.ID, "black_sandstone"))
				);

		list.add(
				new ShapedOreRecipe(new ResourceLocation(Refs.ID), 
						new ItemStack(this, 1, 1), 
						new Object[] {
								"S", "S",  
								'S', new ItemStack(ModBlocks.BlackSandstoneSlab)
				}).setRegistryName(new ResourceLocation(Refs.ID, "black_sandstone_chisel"))
				);

		list.add(
				new ShapedOreRecipe(new ResourceLocation(Refs.ID), 
						new ItemStack(this, 4, 2), 
						new Object[] {
								"SS", "SS",  
								'S', new ItemStack(ModBlocks.BlackSandstone, 1, 0)
				}).setRegistryName(new ResourceLocation(Refs.ID, "black_sandstone_smooth"))
				);
		
		list.add(
				new ShapedOreRecipe(new ResourceLocation(Refs.ID), 
						new ItemStack(ModBlocks.BlackSandstoneSlab, 6), 
						new Object[] {
								"SSS",  
								'S', new ItemStack(ModBlocks.BlackSandstone)
				}).setRegistryName(new ResourceLocation(Refs.ID, "black_sandstone_slab"))
				);

		return list;
	}
}
