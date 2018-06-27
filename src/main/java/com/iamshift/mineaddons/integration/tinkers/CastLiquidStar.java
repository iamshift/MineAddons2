package com.iamshift.mineaddons.integration.tinkers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.iamshift.mineaddons.core.Refs;
import com.iamshift.mineaddons.init.ModFluids;
import com.iamshift.mineaddons.items.ItemBase;

import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import slimeknights.mantle.util.RecipeMatch;
import slimeknights.tconstruct.library.TinkerRegistry;
import slimeknights.tconstruct.library.Util;
import slimeknights.tconstruct.library.materials.Material;
import slimeknights.tconstruct.library.smeltery.Cast;
import slimeknights.tconstruct.library.smeltery.CastingRecipe;
import slimeknights.tconstruct.library.smeltery.ICast;
import slimeknights.tconstruct.library.tinkering.MaterialItem;
import slimeknights.tconstruct.library.tools.IToolPart;
import slimeknights.tconstruct.library.tools.Pattern;
import slimeknights.tconstruct.library.utils.TagUtil;
import slimeknights.tconstruct.smeltery.TinkerSmeltery;
import slimeknights.tconstruct.tools.TinkerMaterials;

public class CastLiquidStar extends ItemBase implements ICast
{
	protected HashMap<String, String> parts = new HashMap<>();
	protected List<ItemStack> stacks = new ArrayList<ItemStack>();
	private String cast;

	public CastLiquidStar()
	{
		super("liquidstar");
		this.cast = "liquid_star";

		this.setHasSubtypes(true);
	}

	public ItemStack addPart(String part, Item toolPart)
	{
		ItemStack stack = Pattern.setTagForPart(new ItemStack(this), toolPart);

		NBTTagCompound tag = TagUtil.getTagSafe(stack);
		String partType = tag.getString(Pattern.TAG_PARTTYPE);

		if (!parts.containsKey(part))
		{
			parts.put(part, partType);
			stacks.add(stack);
		}

		return stack;
	}

	public void addParts()
	{
		for(Material mat : TinkerRegistry.getAllMaterials())
		{
			for(IToolPart part : TinkerRegistry.getToolParts())
			{
				if(!part.canBeCasted())
					continue;

				if(!part.canUseMaterial(mat))
					continue;
				
				if(part instanceof MaterialItem)
				{
					ItemStack stack = part.getItemstackWithMaterial(mat);

					ItemStack origin = Cast.setTagForPart(new ItemStack(TinkerSmeltery.cast), stack.getItem());
					String p = origin.getDisplayName().replaceAll(" Cast", "");
					
					if(p.equals("Arrow Shaft") || p.equals("Fletching") || p.equals("Bowstring"))
						continue;
					
					ItemStack cast = this.addPart(p, stack.getItem());
					
					TinkerRegistry.registerTableCasting(new CastingRecipe(cast,
							RecipeMatch.ofNBT(stack),
							new FluidStack(ModFluids.LiquidStar, Material.VALUE_Ingot),
							true, true));
				}
			}
		}
	}

	@Override
	public String getItemStackDisplayName(ItemStack stack)
	{
		if (CastLiquidStar.getPartName(stack).equals("Blank"))
			return I18n.translateToLocal(this.getUnlocalizedNameInefficiently(stack) + ".blank").trim();
		else
			return I18n.translateToLocalFormatted(getUnlocalizedName(stack) + ".name", CastLiquidStar.getPartName(stack)).trim();
	}

	@Override
	public String getUnlocalizedName() 
	{
		return "cast." + cast.toLowerCase();
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) 
	{
		return "cast." + cast.toLowerCase();
	}

	public static String getPartName(ItemStack stack)
	{
		if (!(stack.getItem() instanceof CastLiquidStar))
			return "";

		CastLiquidStar cast = (CastLiquidStar)stack.getItem();

		HashMap<String, String> invert = new HashMap<>();
		for(String key : cast.parts.keySet())
		{
			invert.put(cast.parts.get(key), key);
		}

		NBTTagCompound tag = TagUtil.getTagSafe(stack);
		String part = tag.getString(Pattern.TAG_PARTTYPE);

		if (!invert.containsKey(part))
			return "Blank";

		return invert.get(part);
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items)
	{
		super.getSubItems(tab, items);
		
		if(this.isInCreativeTab(tab))
			items.addAll(stacks);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModels()
	{
		PartMeshDefinition mesh = new PartMeshDefinition();
		ModelLoader.setCustomMeshDefinition(this, mesh);

		ArrayList<ModelResourceLocation> locations = new ArrayList<>();
		locations.add(new ModelResourceLocation(new ResourceLocation(Refs.ID, "casts/blank"), "inventory"));

		for(String key : parts.keySet())
			locations.add(new ModelResourceLocation(new ResourceLocation(Refs.ID, "casts/" + key.toLowerCase(Locale.US).replaceAll(" ", "_")), "inventory"));

		ModelBakery.registerItemVariants(this, locations.toArray(new ModelResourceLocation[0]));
	}

	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		Item part = Pattern.getPartFromTag(stack);
		if(part != null && part instanceof IToolPart) 
		{
			float cost = ((IToolPart) part).getCost() / (float) Material.VALUE_Ingot;
			tooltip.add(Util.translateFormatted("tooltip.pattern.cost", Util.df.format(cost)));
		}
	}

	@SideOnly(Side.CLIENT)
	public class PartMeshDefinition implements ItemMeshDefinition
	{
		@Override
		public ModelResourceLocation getModelLocation(ItemStack stack)
		{
			Item item = stack.getItem();

			if(!(item instanceof CastLiquidStar))
				return new ModelResourceLocation(new ResourceLocation(Refs.ID, "casts/blank"), "inventory");

			return new ModelResourceLocation(new ResourceLocation(Refs.ID, "casts/" + CastLiquidStar.getPartName(stack).toLowerCase(Locale.US).replaceAll(" ", "_")), "inventory");
		}
	}
}
