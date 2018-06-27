package com.iamshift.mineaddons.blocks;

import java.util.ArrayList;
import java.util.List;

import com.iamshift.mineaddons.MineAddons;
import com.iamshift.mineaddons.core.Refs;
import com.iamshift.mineaddons.interfaces.IMetaName;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class BlockColorablePlank extends BlockBase implements IMetaName
{
	public static final PropertyEnum<EnumDyeColor> COLOR = PropertyEnum.<EnumDyeColor>create("color", EnumDyeColor.class);

	public BlockColorablePlank(String name)
	{
		super(name, Material.WOOD, true);
		setHardness(2.0F);
		setResistance(10.0F);
		setSoundType(SoundType.WOOD);
		this.setDefaultState(this.blockState.getBaseState().withProperty(COLOR, EnumDyeColor.WHITE));
	}

	@Override
	public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items)
	{
		for(EnumDyeColor color : EnumDyeColor.values())
			items.add(new ItemStack(this, 1, color.getMetadata()));
	}

	@Override
	public int damageDropped(IBlockState state)
	{
		return ((EnumDyeColor)state.getValue(COLOR)).getMetadata();
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(COLOR, EnumDyeColor.byMetadata(meta));
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return ((EnumDyeColor)state.getValue(COLOR)).getMetadata();
	}

	@Override
	public MapColor getMapColor(IBlockState state, IBlockAccess worldIn, BlockPos pos)
	{
		return MapColor.getBlockColor((EnumDyeColor)state.getValue(COLOR));
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] {COLOR});
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if(worldIn.isRemote)
			return false;

		ItemStack heldItem = playerIn.getHeldItem(hand);

		if(heldItem == null)
			return false;

		if(heldItem.getItem() == null)
			return false;

		if(!(heldItem.getItem() instanceof ItemDye))
			return false;

		if((15 - heldItem.getMetadata()) == state.getBlock().getMetaFromState(state))
			return false;

		IBlockState newState = state.getBlock().getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, 15 - heldItem.getMetadata(), playerIn);
		worldIn.setBlockState(pos, newState);
		
		if(!playerIn.capabilities.isCreativeMode)
			heldItem.shrink(1);

		return true;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerModels()
	{
		for(EnumDyeColor color : EnumDyeColor.values())
			MineAddons.proxy.registerVariantRenderer(Item.getItemFromBlock(this), color.getMetadata(), "colorable_plank_" + color.getName(), "inventory");
	}

	@Override
	public List<IRecipe> getRecipe()
	{
		List<IRecipe> list = new ArrayList<IRecipe>();

		for(EnumDyeColor color : EnumDyeColor.values())
		{
			list.add(
					new ShapedOreRecipe(new ResourceLocation(Refs.ID), 
							new ItemStack(this, 8, color.getMetadata()), 
							new Object[] {
									"PPP", "PDP", "PPP", 
									'P', "plankWood", 
									'D', new ItemStack(Items.DYE, 1, 15 - color.getMetadata())
					}).setRegistryName(new ResourceLocation(Refs.ID, "colorable_plank_" + color.getName()))
					);
		}

		return list;
	}

	@Override
	public String getSpecialName(ItemStack stack)
	{
		return EnumDyeColor.byMetadata(stack.getMetadata()).getName();
	}
}
