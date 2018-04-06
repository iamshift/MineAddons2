package com.iamshift.mineaddons.items;

import java.util.List;

import com.iamshift.mineaddons.core.Refs;
import com.iamshift.mineaddons.entities.boss.EntityDeadHorse;
import com.iamshift.mineaddons.interfaces.ISpawner;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemDeadHorseEgg extends ItemBase implements ISpawner
{
	public ItemDeadHorseEgg(String name)
	{
		super(name);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if(!worldIn.isRemote)
		{
			ItemStack stack = player.getHeldItem(hand);
			BlockPos spawnPos = pos.offset(facing);

			EntityDeadHorse horse = (EntityDeadHorse) spawnCreature(worldIn, new ResourceLocation(Refs.ID, "dead_horse"), (double)spawnPos.getX() + .5D, (double)spawnPos.getY() + .5D, (double)spawnPos.getZ() + .5D, false);
			horse.setPositionAndUpdate((double)spawnPos.getX() + .5D, (double)spawnPos.getY() + .5D, (double)spawnPos.getZ() + .5D);
			horse.setHealth(horse.getMaxHealth());
			horse.enablePersistence();
			horse.setHorseTamed(true);
			horse.setHorseSaddled(true);
			horse.setSaddle();
			horse.setOwnerUniqueId(player.getUniqueID());
			horse.setGrowingAge(0);

			if(!player.capabilities.isCreativeMode)
				stack.shrink(1);
		}

		return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		super.addInformation(stack, worldIn, tooltip, flagIn);
		tooltip.add("Summons a Dead Horse");
	}

}
