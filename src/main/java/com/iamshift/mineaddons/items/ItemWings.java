package com.iamshift.mineaddons.items;

import com.iamshift.mineaddons.MineAddons;
import com.iamshift.mineaddons.interfaces.IHasModel;

import net.minecraft.block.BlockDispenser;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemWings extends ItemBase implements IHasModel
{
	public ItemWings(String name)
	{
		super(name);
		setMaxStackSize(1);
		

		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, ItemArmor.DISPENSER_BEHAVIOR);
	}
	
	@Override
	public boolean isDamageable()
	{
		return false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public EntityEquipmentSlot getEquipmentSlot(ItemStack stack)
	{
		return EntityEquipmentSlot.CHEST;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
	{
		ItemStack itemstack = playerIn.getHeldItem(handIn);
        EntityEquipmentSlot entityequipmentslot = EntityLiving.getSlotForItemStack(itemstack);
        ItemStack itemstack1 = playerIn.getItemStackFromSlot(entityequipmentslot);

        if (itemstack1.isEmpty())
        {
            playerIn.setItemStackToSlot(entityequipmentslot, itemstack.copy());
            itemstack.setCount(0);
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, itemstack);
        }
        else
        {
            return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
        }
	}
	
	@Override
	public void registerModels()
	{
		MineAddons.proxy.registerItemRenderer(this, 0, "inventory");
	}
}
