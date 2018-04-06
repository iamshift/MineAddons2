package com.iamshift.mineaddons.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.iamshift.mineaddons.MineAddons;
import com.iamshift.mineaddons.core.Refs;
import com.iamshift.mineaddons.init.ModBlocks;
import com.iamshift.mineaddons.init.ModItems;
import com.iamshift.mineaddons.interfaces.IHasModel;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBreakable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderHell;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockFrozenforgottenWater extends BlockBreakable implements IHasModel
{
	public BlockFrozenforgottenWater(String name)
	{
		super(Material.ICE, false);
		setUnlocalizedName(name);
		setRegistryName(new ResourceLocation(Refs.ID, name));
		this.slipperiness = 0.98F;
		this.setLightOpacity(3);
		this.setHardness(0.5F);
		this.setSoundType(SoundType.GLASS);
		this.setTickRandomly(true);
		
		setCreativeTab(MineAddons.minetab);
		
		ModBlocks.BLOCKS.add(this);
		ModItems.ITEMS.add(new ItemBlock(this).setRegistryName(this.getRegistryName()));		
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerModels() 
	{
		MineAddons.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
	}
	
	@Override
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.TRANSLUCENT;
	}
	
	@Override
	public void harvestBlock(World worldIn, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te, ItemStack stack)
	{
		player.addExhaustion(0.005F);
		
		if(this.canSilkHarvest(worldIn, pos, state, player) && EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, stack) > 0)
		{
			List<ItemStack> items = new ArrayList<ItemStack>();
			items.add(this.getSilkTouchDrop(state));
			
			net.minecraftforge.event.ForgeEventFactory.fireBlockHarvesting(items, worldIn, pos, state, 0, 1.0f, true, player);
            for (ItemStack is : items)
                spawnAsEntity(worldIn, pos, is);
		}
		else
		{
			if (!worldIn.provider.doesWaterVaporize())
            {
                worldIn.setBlockToAir(pos);
                return;
            }
			
			Material material = worldIn.getBlockState(pos.down()).getMaterial();
			
			if (material.blocksMovement() || material.isLiquid())
            {
                worldIn.setBlockState(pos, ModBlocks.ForgottenWater.getDefaultState());
            }
		}
	}
	
	@Override
	public int quantityDropped(Random random)
	{
		return 0;
	}
	
	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		if (worldIn.getLightFor(EnumSkyBlock.BLOCK, pos) > 9 - this.getDefaultState().getLightOpacity())
        {
            this.turnIntoWater(worldIn, pos);
        }
	}
	
	protected void turnIntoWater(World worldIn, BlockPos pos)
    {
        if (!worldIn.provider.doesWaterVaporize())
        {
            worldIn.setBlockToAir(pos);
        }
        else
        {
            this.dropBlockAsItem(worldIn, pos, worldIn.getBlockState(pos), 0);
            worldIn.setBlockState(pos, ModBlocks.ForgottenWater.getDefaultState());
            worldIn.neighborChanged(pos, ModBlocks.ForgottenWater, pos);
        }
    }
	
	@Override
	public EnumPushReaction getMobilityFlag(IBlockState state)
	{
		return EnumPushReaction.NORMAL;
	}
}
