package com.iamshift.mineaddons.items.armors;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import com.iamshift.mineaddons.MineAddons;
import com.iamshift.mineaddons.core.Config;
import com.iamshift.mineaddons.core.Refs;
import com.iamshift.mineaddons.events.ArmorEvents;
import com.iamshift.mineaddons.init.ModItems;
import com.iamshift.mineaddons.interfaces.IHasModel;
import com.iamshift.mineaddons.interfaces.IRecipeProvider;
import com.iamshift.mineaddons.items.tools.ItemFiberPickaxe;

import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ItemUltimateArmor extends ItemArmor implements IHasModel, IRecipeProvider
{
	public ItemUltimateArmor(String name, ArmorMaterial material, int renderIndex, EntityEquipmentSlot slot)
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
			if(toRepair.getItem() instanceof ItemUltimateArmor)
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
									"FFF", "FHF", " S ", 
									'H', new ItemStack(ModItems.FiberHelmet, 1),
									'F', new ItemStack(ModItems.Fiberglass, 1),
									'S', new ItemStack(Items.NETHER_STAR, 1)
					}).setRegistryName(new ResourceLocation(Refs.ID, "ulti_helmet"))
					);

			list.add(
					new ShapedOreRecipe(new ResourceLocation(Refs.ID), 
							new ItemStack(this, 1), 
							new Object[] {
									"FSF", "FCF", "FFF", 
									'C', new ItemStack(ModItems.FiberChestplate, 1),
									'F', new ItemStack(ModItems.Fiberglass, 1),
									'S', new ItemStack(Items.NETHER_STAR, 1)
					}).setRegistryName(new ResourceLocation(Refs.ID, "ulti_chest"))
					);
			
			list.add(
					new ShapedOreRecipe(new ResourceLocation(Refs.ID), 
							new ItemStack(this, 1), 
							new Object[] {
									"FFF", "FLF", "FSF", 
									'L', new ItemStack(ModItems.FiberLeggings, 1),
									'F', new ItemStack(ModItems.Fiberglass, 1),
									'S', new ItemStack(Items.NETHER_STAR, 1)
					}).setRegistryName(new ResourceLocation(Refs.ID, "ulti_legs"))
					);
			
			list.add(
					new ShapedOreRecipe(new ResourceLocation(Refs.ID), 
							new ItemStack(this, 1), 
							new Object[] {
									"FSF", "FBF", 
									'B', new ItemStack(ModItems.FiberBoots, 1),
									'F', new ItemStack(ModItems.Fiberglass, 1),
									'S', new ItemStack(Items.NETHER_STAR, 1)
					}).setRegistryName(new ResourceLocation(Refs.ID, "ulti_boots"))
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
	public boolean hasOverlay(ItemStack stack)
	{
		return true;
	}

	@Override
	public boolean hasColor(ItemStack stack)
	{
		NBTTagCompound nbttagcompound = stack.getTagCompound();
		return nbttagcompound != null && nbttagcompound.hasKey("display", 10) ? nbttagcompound.getCompoundTag("display").hasKey("color", 3) : false;
	}

	@Override
	public int getColor(ItemStack stack)
	{
		NBTTagCompound nbttagcompound = stack.getTagCompound();

		if (nbttagcompound != null)
		{
			NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");

			if (nbttagcompound1 != null && nbttagcompound1.hasKey("color", 3))
			{
				return nbttagcompound1.getInteger("color");
			}
		}

		return 16777215;
	}

	@Override
	public void removeColor(ItemStack stack)
	{
		NBTTagCompound nbttagcompound = stack.getTagCompound();

		if (nbttagcompound != null)
		{
			NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");

			if (nbttagcompound1.hasKey("color"))
			{
				nbttagcompound1.removeTag("color");
			}
		}
	}

	@Override
	public void setColor(ItemStack stack, int color)
	{
		NBTTagCompound nbttagcompound = stack.getTagCompound();

		if (nbttagcompound == null)
		{
			nbttagcompound = new NBTTagCompound();
			stack.setTagCompound(nbttagcompound);
		}

		NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag("display");

		if (!nbttagcompound.hasKey("display", 10))
		{
			nbttagcompound.setTag("display", nbttagcompound1);
		}

		nbttagcompound1.setInteger("color", color);
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

		tooltip.add(TextFormatting.WHITE + "Dyeable");
		tooltip.add(TextFormatting.WHITE + "Color: " + TextFormatting.AQUA + Integer.toHexString(getColor(stack)));

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

		tooltip.add(" ");

		if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT))
		{
			tooltip.add(TextFormatting.WHITE + "Set Bonus:");

			if(Minecraft.getMinecraft().player == null )
				return;	

			int total = 0;
			if(ArmorEvents.setItems.containsKey(Minecraft.getMinecraft().player.getGameProfile().getId()))
				total = ArmorEvents.setItems.get(Minecraft.getMinecraft().player.getGameProfile().getId());

			if(total >= 2 )
				tooltip.add(TextFormatting.GRAY + "(2): " + TextFormatting.GREEN + "Double HP");
			else
				tooltip.add(TextFormatting.GRAY + "(2): " + TextFormatting.GRAY + "Double HP");

			if(total >= 3 )
				tooltip.add(TextFormatting.GRAY + "(3): " + TextFormatting.GREEN + "Wither Proof");
			else
				tooltip.add(TextFormatting.GRAY + "(3): " + TextFormatting.GRAY + "Wither Proof");

			if(total == 4 )
				tooltip.add(TextFormatting.GRAY + "(4): " + TextFormatting.GREEN + "Flight");
			else
				tooltip.add(TextFormatting.GRAY + "(4): " + TextFormatting.GRAY + "Flight");
		}
		else
		{
			tooltip.add(TextFormatting.WHITE + "" + TextFormatting.ITALIC + "Press " + TextFormatting.AQUA + "" + TextFormatting.ITALIC + "Shift" + TextFormatting.WHITE + "" + TextFormatting.ITALIC + " for Set Bonus info");
		}
		
		if(stack.isItemEnchanted())
			tooltip.add("");
	}
}
