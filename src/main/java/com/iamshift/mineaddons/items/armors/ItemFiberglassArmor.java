package com.iamshift.mineaddons.items.armors;

import java.util.List;

import com.iamshift.mineaddons.MineAddons;
import com.iamshift.mineaddons.core.Refs;
import com.iamshift.mineaddons.events.ArmorEvents;
import com.iamshift.mineaddons.init.ModItems;
import com.iamshift.mineaddons.interfaces.IHasModel;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemFiberglassArmor extends ItemArmor implements IHasModel
{
	public ItemFiberglassArmor(String name, ArmorMaterial material, int renderIndex, EntityEquipmentSlot slot)
	{
		super(material, renderIndex, slot);
		setUnlocalizedName(name);
		setRegistryName(Refs.ID, name);
		setCreativeTab(MineAddons.minetab);

		ModItems.ITEMS.add(this);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModels()
	{
		MineAddons.proxy.registerItemRenderer(this, 0, "inventory");
	}
	
	@Override
	public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack)
	{
		super.onArmorTick(world, player, itemStack);
		
		Potion potion = ArmorEvents.armorEffects.get(this.armorType).getPotion();
		if (!player.isPotionActive(potion) || (player.isPotionActive(potion) && player.getActivePotionEffect(potion).getDuration() <= 900005))
		{
			PotionEffect effect = new PotionEffect(ArmorEvents.armorEffects.get(this.armorType));
			
			if (world.isRemote)
				effect.setPotionDurationMax(true);
			
			player.addPotionEffect(effect);	
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		super.addInformation(stack, worldIn, tooltip, flagIn);
		
		tooltip.add(" ");
		
		String a = TextFormatting.WHITE + "Ability: ";
		switch(this.armorType)
		{
			case HEAD:
				a += TextFormatting.AQUA + "Night Vision";
				break;
			case CHEST:
				a += TextFormatting.AQUA + "Haste";
				break;
			case LEGS:
				a += TextFormatting.AQUA + "Speed";
				break;
			case FEET:
				a += TextFormatting.AQUA + "Jump Boost";
				break;
			default:
				a += TextFormatting.AQUA + "None";
				break;
		}
		tooltip.add(a);
	}
}
