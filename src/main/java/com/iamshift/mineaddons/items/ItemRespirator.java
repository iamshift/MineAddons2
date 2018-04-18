package com.iamshift.mineaddons.items;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.iamshift.mineaddons.MineAddons;
import com.iamshift.mineaddons.core.Refs;
import com.iamshift.mineaddons.init.ModItems;

import net.minecraft.block.BlockDispenser;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;

@Mod.EventBusSubscriber(modid = Refs.ID)
public class ItemRespirator extends ItemBase
{
	public ItemRespirator(String name)
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
		return EntityEquipmentSlot.HEAD;
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
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		super.addInformation(stack, worldIn, tooltip, flagIn);

		tooltip.add(TextFormatting.DARK_AQUA + "Allows to breath under water if you have O2 Bottles in your inventory.");
		tooltip.add(TextFormatting.DARK_GREEN + "Can be infused in any helmet at an anvil.");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModels()
	{
		MineAddons.proxy.registerItemRenderer(this, 0, "inventory");
	}

	@Override
	public List<IRecipe> getRecipe()
	{
		List<IRecipe> list = new ArrayList<IRecipe>();

		list.add(
				new ShapedOreRecipe(new ResourceLocation(Refs.ID), 
						new ItemStack(this, 1), 
						new Object[] {
								"III", "IGI", " I ", 
								'I', Items.IRON_INGOT, 
								'G', new ItemStack(Blocks.GLASS_PANE)
				}).setRegistryName(new ResourceLocation(Refs.ID, "respirator"))
				);

		return list;
	}

	//EVENTS
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void waterBreathing(PlayerTickEvent event)
	{
		if(event.player.world.isRemote)
			return;

		if(!event.player.isInWater())
			return;

		if(event.player.getAir() > 0)
			return;

		ItemStack stack = event.player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
		if(stack.isEmpty())
			return;
		
		if(!isValid(stack))
			return;

		ItemStack bottle = new ItemStack(ModItems.O2Bottle);
		if(event.player.inventory.hasItemStack(bottle))
		{
			int index = getSlot(bottle, event.player.inventory);
			if(index == -1)
				return;
			
			event.player.inventory.getStackInSlot(index).shrink(1);
			
			ItemStack empty = new ItemStack(Items.GLASS_BOTTLE);
			if(!event.player.inventory.addItemStackToInventory(empty))
				event.player.dropItem(empty, false);

			event.player.setAir(300);

			Random rand = new Random();
			event.player.world.playSound(null, new BlockPos(event.player.posX, event.player.posY, event.player.posZ), SoundEvents.ENTITY_PLAYER_BREATH, SoundCategory.PLAYERS, 1.5F, 1.5F);
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public static void respiratorInfusion(AnvilUpdateEvent event)
	{
		if(isHelmet(event.getLeft()))
		{
			ItemStack out = event.getLeft().copy();

			if(isRespirator(event.getRight()))
			{
				out.getOrCreateSubCompound("Respirator");
				event.setOutput(out);
				event.setMaterialCost(1);
				event.setCost(10);
			}
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onTooltipHandler(ItemTooltipEvent event)
	{
		ItemStack stack = event.getItemStack();

		if(isHelmet(stack))
		{
			if(stack.hasTagCompound() && stack.getTagCompound().hasKey("Respirator"))
			{
				if(!event.getToolTip().contains("Respirator"))
				{
					event.getToolTip().add(" ");
					event.getToolTip().add(TextFormatting.AQUA + "Respirator Infused");
					event.getToolTip().add(" ");
				}
			}
		}
	}

	private static boolean isHelmet(ItemStack stack)
	{
		if(!(stack.getItem() instanceof ItemArmor))
			return false;

		return ((ItemArmor)stack.getItem()).armorType == EntityEquipmentSlot.HEAD;
	}

	private static boolean isRespirator(ItemStack stack)
	{
		if((stack.getItem() instanceof ItemRespirator))
			return true;

		return false;
	}

	private static boolean isValid(ItemStack stack)
	{
		if(stack.getItem() instanceof ItemArmor && ((ItemArmor)stack.getItem()).armorType == EntityEquipmentSlot.HEAD)
			return (stack.hasTagCompound() && stack.getTagCompound().hasKey("Respirator"));

		return stack.getItem() instanceof ItemRespirator;
	}
	
	private static int getSlot(ItemStack stack, InventoryPlayer inventory)
	{
		for(int i = 0; i < inventory.mainInventory.size(); ++i)
		{
			if(!((ItemStack)inventory.mainInventory.get(i)).isEmpty() && stackEquals(stack, inventory.mainInventory.get(i)))
				return i;
		}
		
		return -1;
	}
	
	private static boolean stackEquals(ItemStack stack1, ItemStack stack2)
	{
		return stack1.getItem() == stack2.getItem() && (!stack1.getHasSubtypes() || stack1.getMetadata() == stack2.getMetadata()) && ItemStack.areItemStackTagsEqual(stack1, stack2); 
	}
}
