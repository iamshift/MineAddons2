package com.iamshift.mineaddons.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.iamshift.mineaddons.blocks.tiles.TileMover;
import com.iamshift.mineaddons.core.Refs;
import com.iamshift.mineaddons.init.ModBlocks;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class BlockMover extends BlockBase implements ITileEntityProvider
{
	public static PropertyBool POWERED = PropertyBool.create("powered");
	public static PropertyBool CONNECTED = PropertyBool.create("connected");
	
	public BlockMover(String name)
	{
		super(name, Material.CIRCUITS, false);
		setResistance(2000.0F);
		
		this.setDefaultState(this.blockState.getBaseState().withProperty(POWERED, Boolean.valueOf(false)).withProperty(CONNECTED, Boolean.valueOf(false)));
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] {POWERED, CONNECTED});
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return new TileMover();
	}

	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(POWERED, Boolean.valueOf(meta == 1 || meta == 3)).withProperty(CONNECTED, Boolean.valueOf(meta == 2 || meta == 3));
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return (state.getValue(CONNECTED) ? 2 : 0) + (state.getValue(POWERED) ? 1 : 0);
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
	{
		if(worldIn.getTileEntity(pos) != null)
			worldIn.getTileEntity(pos).invalidate();

		super.breakBlock(worldIn, pos, state);
	}

	@Override
	public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
	{
		return true;
	}

	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
	{
		if(!worldIn.isRemote)
		{
			worldIn.setBlockState(pos, state.withProperty(POWERED, Boolean.valueOf(worldIn.isBlockPowered(pos))));
		}
	}
	
	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack)
	{
		if(!worldIn.isRemote)
		{
			if(worldIn.getTileEntity(pos) != null && worldIn.getTileEntity(pos) instanceof TileMover)
				worldIn.setBlockState(pos, state.withProperty(POWERED, Boolean.valueOf(worldIn.isBlockPowered(pos))).withProperty(CONNECTED, Boolean.valueOf(((TileMover)worldIn.getTileEntity(pos)).hasTarget())));
		}
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if(worldIn.isRemote)
			return false;
		
		ItemStack stack = playerIn.getHeldItem(hand);
		
		if(worldIn.getTileEntity(pos) != null && worldIn.getTileEntity(pos) instanceof TileMover)
			worldIn.setBlockState(pos, state.withProperty(CONNECTED, Boolean.valueOf(((TileMover)worldIn.getTileEntity(pos)).hasTarget())));
		
		return false;
	}
	
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
	{
		if(!worldIn.isRemote)
		{
			if(worldIn.getTileEntity(pos) != null && worldIn.getTileEntity(pos) instanceof TileMover)
			{
				boolean flag = worldIn.isBlockPowered(pos);
				
				if((state.getValue(POWERED).booleanValue() && !flag) || (!state.getValue(POWERED).booleanValue() && flag))
				{
					worldIn.setBlockState(pos, state.withProperty(POWERED, Boolean.valueOf(flag)).withProperty(CONNECTED, Boolean.valueOf(((TileMover)worldIn.getTileEntity(pos)).hasTarget())));
				
					((TileMover)worldIn.getTileEntity(pos)).updateBlock();
				}
			}
		}
	}
	
	@Override
	public int damageDropped(IBlockState state)
	{
		return 0;
	}
	
	@Override
	public int quantityDropped(IBlockState state, int fortune, Random random)
	{
		return 1;
	}
	
	@Override
	public int quantityDroppedWithBonus(int fortune, Random random)
	{
		return 1;
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return Item.getItemFromBlock(this);
	}

	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
	{
		return new ItemStack(this);
	}
	
	@Override
	protected ItemStack getSilkTouchDrop(IBlockState state)
	{
		return new ItemStack(ModBlocks.Mover);
	}
	
	@Override
	public void addInformation(ItemStack stack, World player, List<String> tooltip, ITooltipFlag advanced)
	{
		super.addInformation(stack, player, tooltip, advanced);
		
		tooltip.add(TextFormatting.GREEN + "Move blocks at a distance.");
	}

	@Override
	public List<IRecipe> getRecipe()
	{
		List<IRecipe> list = new ArrayList<IRecipe>();
		list.add(
				new ShapedOreRecipe(new ResourceLocation(Refs.ID), 
						new ItemStack(this, 1), 
						new Object[] {
								"OOO", "OEO", "OPO", 
								'O', new ItemStack(Blocks.OBSIDIAN, 1, 0), 
								'E', new ItemStack(Items.ENDER_EYE, 1, 0),
								'P', new ItemStack(Blocks.PISTON, 1, 0)
				}).setRegistryName(new ResourceLocation(Refs.ID, "mover"))
				);

		return list;
	}
}
