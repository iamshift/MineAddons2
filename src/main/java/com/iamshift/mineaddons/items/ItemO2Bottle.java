package com.iamshift.mineaddons.items;

import java.util.ArrayList;
import java.util.List;

import com.iamshift.mineaddons.core.Refs;
import com.iamshift.mineaddons.init.ModFluids;
import com.iamshift.mineaddons.init.ModItems;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemFirework;
import net.minecraft.item.ItemGlassBottle;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickItem;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.ShapedOreRecipe;

@Mod.EventBusSubscriber(modid = Refs.ID)
public class ItemO2Bottle extends ItemBase
{
	public ItemO2Bottle(String name)
	{
		super(name);
	}

	@SubscribeEvent
	public static void creation(RightClickItem event)
	{
		if(event.getWorld().isRemote)
			return;
		
		if(event.getItemStack() == null)
			return;

		EntityPlayer player = event.getEntityPlayer();
		if(player == null)
			return;

		if((event.getItemStack().getItem() instanceof ItemGlassBottle))
		{	
			RayTraceResult raytraceresult = trace(event.getWorld(), player, true);

            if (raytraceresult == null)
            {
            	ItemStack stack = new ItemStack(ModItems.O2Bottle, 1, 0);
            	
            	if(!player.inventory.addItemStackToInventory(stack))
            		player.dropItem(stack, false);
            	
            	if(!player.capabilities.isCreativeMode)
    				event.getItemStack().shrink(1);
            }
		}
	}
	
	private static RayTraceResult trace(World worldIn, EntityPlayer playerIn, boolean useLiquids)
    {
        float f = playerIn.rotationPitch;
        float f1 = playerIn.rotationYaw;
        double d0 = playerIn.posX;
        double d1 = playerIn.posY + (double)playerIn.getEyeHeight();
        double d2 = playerIn.posZ;
        Vec3d vec3d = new Vec3d(d0, d1, d2);
        float f2 = MathHelper.cos(-f1 * 0.017453292F - (float)Math.PI);
        float f3 = MathHelper.sin(-f1 * 0.017453292F - (float)Math.PI);
        float f4 = -MathHelper.cos(-f * 0.017453292F);
        float f5 = MathHelper.sin(-f * 0.017453292F);
        float f6 = f3 * f4;
        float f7 = f2 * f4;
        double d3 = playerIn.getEntityAttribute(EntityPlayer.REACH_DISTANCE).getAttributeValue();
        Vec3d vec3d1 = vec3d.addVector((double)f6 * d3, (double)f5 * d3, (double)f7 * d3);
        return worldIn.rayTraceBlocks(vec3d, vec3d1, useLiquids, !useLiquids, false);
    }
}
