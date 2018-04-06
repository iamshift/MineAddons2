package com.iamshift.mineaddons.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import com.google.common.collect.Lists;
import com.iamshift.mineaddons.MineAddons;
import com.iamshift.mineaddons.blocks.items.ItemLavaSponge;
import com.iamshift.mineaddons.core.Refs;
import com.iamshift.mineaddons.init.ModItems;
import com.iamshift.mineaddons.interfaces.IMetaName;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class BlockLavaSponge extends BlockBase implements IMetaName
{
	public static final PropertyBool WET = PropertyBool.create("wet");
	
	public BlockLavaSponge(String name) 
	{
		super(name, Material.SPONGE, true);
		setHardness(0.3F);
		setSoundType(SoundType.PLANT);
		
		setCreativeTab(MineAddons.minetab);
		
		this.setDefaultState(this.getDefaultState().withProperty(WET, Boolean.valueOf(false)));
	}
	
	@Override
	public int damageDropped(IBlockState state) 
	{
		return ((Boolean)state.getValue(WET)) ? 1 : 0;
	}
	
	@Override
	public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) 
	{
		items.add(new ItemStack(this, 1, 0));
		items.add(new ItemStack(this, 1, 1));
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(WET, Boolean.valueOf(meta == 1));
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return ((Boolean)state.getValue(WET)) ? 1 : 0;
	}
	
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
	{
		return new ItemStack(Item.getItemFromBlock(this), 1, this.getMetaFromState(state));
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] { WET } );
	}
	
	@Override
	public String getSpecialName(ItemStack stack) 
	{
		return stack.getItemDamage() == 0 ? "dry" : "wet";
	}
	
	@Override
	public void registerItemVariants()
	{
		ModItems.ITEMS.add(new ItemLavaSponge(this).setRegistryName(this.getRegistryName()));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerModels()
	{
		MineAddons.proxy.registerVariantRenderer(Item.getItemFromBlock(this), 0, "lava_sponge_dry", "inventory");
		MineAddons.proxy.registerVariantRenderer(Item.getItemFromBlock(this), 1, "lava_sponge_wet", "inventory");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand)
	{
		if ((Boolean)stateIn.getValue(WET))
		{
			EnumFacing enumfacing = EnumFacing.random(rand);

			if (enumfacing != EnumFacing.UP && !worldIn.getBlockState(pos.offset(enumfacing)).isFullCube())
			{
				double d0 = (double)pos.getX();
				double d1 = (double)pos.getY();
				double d2 = (double)pos.getZ();

				if (enumfacing == EnumFacing.DOWN)
				{
					d1 = d1 - 0.05D;
					d0 += rand.nextDouble();
					d2 += rand.nextDouble();
				}
				else
				{
					d1 = d1 + rand.nextDouble() * 0.8D;

					if (enumfacing.getAxis() == EnumFacing.Axis.X)
					{
						d2 += rand.nextDouble();

						if (enumfacing == EnumFacing.EAST)
						{
							++d0;
						}
						else
						{
							d0 += 0.05D;
						}
					}
					else
					{
						d0 += rand.nextDouble();

						if (enumfacing == EnumFacing.SOUTH)
						{
							++d2;
						}
						else
						{
							d2 += 0.05D;
						}
					}
				}

				worldIn.spawnParticle(EnumParticleTypes.DRIP_LAVA, d0, d1, d2, 0.0D, 0.0D, 0.0D, new int[0]);
			}
		}
	}
	
	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state)
	{
		this.tryAbsorb(worldIn, pos, state);
	}
	
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
	{
		this.tryAbsorb(worldIn, pos, state);
		super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
	}
	
	protected void tryAbsorb(World worldIn, BlockPos pos, IBlockState state)
	{
		if((!(Boolean)state.getValue(WET)) && this.absorb(worldIn, pos))
		{
			worldIn.setBlockState(pos, state.withProperty(WET, Boolean.valueOf(true)), 2);
			worldIn.playEvent(2001, pos, Block.getIdFromBlock(Blocks.LAVA));
		}
	}
	
	private boolean absorb(World worldIn, BlockPos pos)
	{
		Queue<Tuple<BlockPos, Integer>> queue = Lists.<Tuple<BlockPos, Integer>>newLinkedList();
		List<BlockPos> list = Lists.<BlockPos>newArrayList();
		queue.add(new Tuple(pos, Integer.valueOf(0)));
		int i = 0;

		while (!((Queue)queue).isEmpty())
		{
			Tuple<BlockPos, Integer> tuple = (Tuple)queue.poll();
			BlockPos blockpos = (BlockPos)tuple.getFirst();
			int j = ((Integer)tuple.getSecond()).intValue();

			for (EnumFacing enumfacing : EnumFacing.values())
			{
				BlockPos blockpos1 = blockpos.offset(enumfacing);

				if (worldIn.getBlockState(blockpos1).getMaterial() == Material.LAVA)
				{
					worldIn.setBlockState(blockpos1, Blocks.AIR.getDefaultState(), 2);
					list.add(blockpos1);
					++i;

					if (j < 6)
					{
						queue.add(new Tuple(blockpos1, Integer.valueOf(j + 1)));
					}
				}
			}

			if (i > 64)
			{
				break;
			}
		}

		for (BlockPos blockpos2 : list)
		{
			worldIn.notifyNeighborsOfStateChange(blockpos2, Blocks.AIR, true);
		}

		return i > 0;
	}
	
	@Override
	public List<IRecipe> getRecipe()
	{
		List<IRecipe> list = new ArrayList<IRecipe>();
		list.add(
				new ShapelessOreRecipe(new ResourceLocation(Refs.ID), 
				new ItemStack(this, 1), 
				new Object[] {
				new ItemStack(Blocks.SOUL_SAND, 1, 0), 
				new ItemStack(ModItems.Cellulose, 1, 0),
				new ItemStack(ModItems.Cellulose, 1, 0),
				new ItemStack(ModItems.Cellulose, 1, 0),
				new ItemStack(ModItems.Cellulose, 1, 0)
				}).setRegistryName(new ResourceLocation(Refs.ID, "lava_sponge"))
				);
		
		list.add(
				new ShapelessOreRecipe(new ResourceLocation(Refs.ID), 
				new ItemStack(Blocks.SPONGE, 1, 0), 
				new Object[] {
				"sand", 
				new ItemStack(ModItems.Cellulose, 1, 0),
				new ItemStack(ModItems.Cellulose, 1, 0),
				new ItemStack(ModItems.Cellulose, 1, 0),
				new ItemStack(ModItems.Cellulose, 1, 0)
				}).setRegistryName(new ResourceLocation(Refs.ID, "sponge"))
				);
		
		return list;
	}
}
