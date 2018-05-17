package com.iamshift.mineaddons.items.armors;

import java.util.ArrayList;
import java.util.List;

import com.iamshift.mineaddons.MineAddons;
import com.iamshift.mineaddons.core.Config;
import com.iamshift.mineaddons.core.Refs;
import com.iamshift.mineaddons.events.ArmorEvents;
import com.iamshift.mineaddons.init.ModItems;
import com.iamshift.mineaddons.interfaces.IHasModel;
import com.iamshift.mineaddons.interfaces.IRecipeProvider;
import com.iamshift.mineaddons.items.tools.ItemFiberPickaxe;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ItemFiberglassArmor extends ItemArmor implements IHasModel, IRecipeProvider
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
	public boolean getIsRepairable(ItemStack toRepair, ItemStack repair)
	{
		if(!Config.Tinker)
		{
			if(toRepair.getItem() instanceof ItemFiberglassArmor)
			{
				if(repair.isItemEqual(new ItemStack(ModItems.Fiberglass)))
					return true;

				return false;
			}
		}
		return super.getIsRepairable(toRepair, repair);
	}
	
	@Override
	public List<IRecipe> getRecipe()
	{
		if(!Config.Tinker && !Config.Foundry)
		{
			List<IRecipe> list = new ArrayList<IRecipe>();
			list.add(
					new ShapedOreRecipe(new ResourceLocation(Refs.ID), 
							new ItemStack(this, 1), 
							new Object[] {
									"FFF", "FHF", 
									'H', new ItemStack(Items.DIAMOND_HELMET, 1),
									'F', new ItemStack(ModItems.Fiberglass, 1)
					}).setRegistryName(new ResourceLocation(Refs.ID, "fiber_helmet"))
					);

			list.add(
					new ShapedOreRecipe(new ResourceLocation(Refs.ID), 
							new ItemStack(this, 1), 
							new Object[] {
									"F F", "FCF", "FFF", 
									'C', new ItemStack(Items.DIAMOND_CHESTPLATE, 1),
									'F', new ItemStack(ModItems.Fiberglass, 1)
					}).setRegistryName(new ResourceLocation(Refs.ID, "fiber_chest"))
					);
			
			list.add(
					new ShapedOreRecipe(new ResourceLocation(Refs.ID), 
							new ItemStack(this, 1), 
							new Object[] {
									"FFF", "FLF", "F F", 
									'L', new ItemStack(Items.DIAMOND_LEGGINGS, 1),
									'F', new ItemStack(ModItems.Fiberglass, 1)
					}).setRegistryName(new ResourceLocation(Refs.ID, "fiber_legs"))
					);
			
			list.add(
					new ShapedOreRecipe(new ResourceLocation(Refs.ID), 
							new ItemStack(this, 1), 
							new Object[] {
									"F F", "FBF", 
									'B', new ItemStack(Items.DIAMOND_BOOTS, 1),
									'F', new ItemStack(ModItems.Fiberglass, 1)
					}).setRegistryName(new ResourceLocation(Refs.ID, "fiber_boots"))
					);
			
			return list;
		}

		return null;
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
			effect.setCurativeItems(new ArrayList<>());
			
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
		
		if(stack.isItemEnchanted())
			tooltip.add("");
	}
}
