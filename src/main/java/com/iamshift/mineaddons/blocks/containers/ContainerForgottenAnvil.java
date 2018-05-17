package com.iamshift.mineaddons.blocks.containers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.StringUtils;

import com.iamshift.mineaddons.blocks.BlockForgottenAnvil;
import com.iamshift.mineaddons.events.AnvilOutuputEvent;
import com.iamshift.mineaddons.init.ModFluids;
import com.iamshift.mineaddons.utils.AnvilRecipe;
import com.iamshift.mineaddons.utils.ForgottenAnvilHelper;
import com.iamshift.mineaddons.utils.GuiHandler;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerForgottenAnvil extends Container
{
	public static Map<Item, ArrayList<AnvilRecipe>> recipes = new HashMap<Item, ArrayList<AnvilRecipe>>();

	private IInventory outputSlot;
	private IInventory inputSlots;

	private World world;
	private BlockPos pos;

	public int maxCost;
	public int matCost;

	public String repairedItemName;

	private boolean disenchanting = false;
	private Enchantment denchantment;
	private boolean forgotten = false;
	private boolean shouldDestroy = false;

	private final EntityPlayer player;

	@SideOnly(Side.CLIENT)
	public ContainerForgottenAnvil(InventoryPlayer playerInventory, World worldIn, EntityPlayer player)
	{
		this(playerInventory, worldIn, BlockPos.ORIGIN, player);
	}

	public ContainerForgottenAnvil(InventoryPlayer playerInventory, final World worldIn, final BlockPos blockPosIn, EntityPlayer player)
	{
		this.outputSlot = new InventoryCraftResult();
		this.inputSlots = new InventoryBasic("Repair", true, 2)
		{
			@Override
			public void markDirty()
			{
				super.markDirty();
				ContainerForgottenAnvil.this.onCraftMatrixChanged(this);
			}
		};
		this.pos = blockPosIn;
		this.world = worldIn;
		this.player = player;

		this.addSlotToContainer(new Slot(this.inputSlots, 0, 27, 47));
		this.addSlotToContainer(new Slot(this.inputSlots, 1, 76, 47));
		this.addSlotToContainer(new Slot(this.outputSlot, 2, 134, 47)
		{
			@Override
			public boolean isItemValid(ItemStack stack)
			{
				return false;
			}

			@Override
			public boolean canTakeStack(EntityPlayer playerIn)
			{
				return (playerIn.capabilities.isCreativeMode || playerIn.experienceLevel >= ContainerForgottenAnvil.this.maxCost) 
						&& ContainerForgottenAnvil.this.maxCost > 0 && this.getHasStack();
			}

			@Nonnull
			@Override
			public ItemStack onTake(EntityPlayer playerIn, @Nonnull ItemStack stack)
			{
				if(!playerIn.capabilities.isCreativeMode)
					player.addExperienceLevel(-ContainerForgottenAnvil.this.maxCost);

				if(!ContainerForgottenAnvil.this.disenchanting && !ContainerForgottenAnvil.this.forgotten && ContainerForgottenAnvil.this.shouldDestroy &&
						onOutputTake(playerIn, ContainerForgottenAnvil.this.outputSlot.getStackInSlot(0), ContainerForgottenAnvil.this.inputSlots.getStackInSlot(0), ContainerForgottenAnvil.this.inputSlots.getStackInSlot(1)))
				{
					if(ContainerForgottenAnvil.this.matCost == 0)
						ContainerForgottenAnvil.this.matCost = 1;

					ItemStack item = ContainerForgottenAnvil.this.inputSlots.getStackInSlot(0);
					if(!item.isEmpty() && item.getCount() > ContainerForgottenAnvil.this.matCost)
					{
						item.shrink(ContainerForgottenAnvil.this.matCost);
						ContainerForgottenAnvil.this.inputSlots.setInventorySlotContents(0, item);
					}
					else
					{
						ContainerForgottenAnvil.this.inputSlots.setInventorySlotContents(0, ItemStack.EMPTY);
					}
				}

				if(ContainerForgottenAnvil.this.disenchanting && ContainerForgottenAnvil.this.denchantment != null)
				{
					ItemStack item = ContainerForgottenAnvil.this.inputSlots.getStackInSlot(0);
					Map<Enchantment, Integer> map1 = EnchantmentHelper.getEnchantments(item);

					map1.remove(denchantment);
					EnchantmentHelper.setEnchantments(map1, item);
					ContainerForgottenAnvil.this.inputSlots.setInventorySlotContents(0, item);
				}

				if(ContainerForgottenAnvil.this.matCost > 0 && !ContainerForgottenAnvil.this.forgotten)
				{
					ItemStack ingredient = ContainerForgottenAnvil.this.inputSlots.getStackInSlot(1);

					if(!ingredient.isEmpty() && ingredient.getCount() > ContainerForgottenAnvil.this.matCost)
					{
						ingredient.shrink(ContainerForgottenAnvil.this.matCost);
						ContainerForgottenAnvil.this.inputSlots.setInventorySlotContents(1, ingredient);
					}
					else
						ContainerForgottenAnvil.this.inputSlots.setInventorySlotContents(1, ItemStack.EMPTY);
				}
				else if(!ContainerForgottenAnvil.this.forgotten)
					ContainerForgottenAnvil.this.inputSlots.setInventorySlotContents(1, ItemStack.EMPTY);

				if(ContainerForgottenAnvil.this.forgotten)
				{
					ItemStack item = ContainerForgottenAnvil.this.inputSlots.getStackInSlot(0);
					if(!item.isEmpty() && item.getCount() > ContainerForgottenAnvil.this.matCost)
					{
						item.shrink(ContainerForgottenAnvil.this.matCost);
						ContainerForgottenAnvil.this.inputSlots.setInventorySlotContents(0, item);
					}
					else
					{
						ContainerForgottenAnvil.this.inputSlots.setInventorySlotContents(0, ItemStack.EMPTY);
					}

					ContainerForgottenAnvil.this.inputSlots.setInventorySlotContents(1, new ItemStack(Items.BUCKET, 1));
				}

				ContainerForgottenAnvil.this.maxCost = 0;

				if(!worldIn.isRemote)
					worldIn.playEvent(1030, blockPosIn, 0);

				return stack;
			}
		});

		for (int i = 0; i < 3; ++i) 
		{
			for (int j = 0; j < 9; ++j) 
			{
				this.addSlotToContainer(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}

		for (int k = 0; k < 9; ++k) 
		{
			this.addSlotToContainer(new Slot(playerInventory, k, 8 + k * 18, 142));
		}
	}

	@Override
	public void onCraftMatrixChanged(IInventory inventoryIn)
	{
		super.onCraftMatrixChanged(inventoryIn);

		if(inventoryIn == this.inputSlots)
			this.updateOutput();
	}

	public void updateOutput()
	{
		ItemStack stack = this.inputSlots.getStackInSlot(0);
		this.outputSlot.setInventorySlotContents(0, ItemStack.EMPTY);
		this.maxCost = 1;
		this.matCost = 1;
		this.disenchanting = false;
		this.denchantment = null;
		this.forgotten = false;
		int i = 0;
		int j = 0;
		int k = 0;

		if(stack.isEmpty())
		{
			this.outputSlot.setInventorySlotContents(0, ItemStack.EMPTY);
			this.maxCost = 0;
		}
		else
		{
			ItemStack item = stack.copy();
			ItemStack ingredient = this.inputSlots.getStackInSlot(1);
			FluidStack fluid = new FluidStack(ModFluids.ForgottenWater, 1000);

			Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(item);
			j = j + stack.getRepairCost() + (ingredient.isEmpty() ? 0 : ingredient.getRepairCost());
			boolean flag = false;

			if(!ingredient.isEmpty())
			{
				if(!onAnvilChange(this, stack, ingredient, outputSlot, repairedItemName, j)) return;
				flag = ingredient.getItem() == Items.ENCHANTED_BOOK && !ItemEnchantedBook.getEnchantments(ingredient).hasNoTags();

				if(item.isItemStackDamageable() && item.getItem().getIsRepairable(stack, ingredient))
				{
					int l2 = Math.min(item.getItemDamage(), item.getMaxDamage() / 4);

					if(l2 <= 0)
					{
						this.outputSlot.setInventorySlotContents(0, ItemStack.EMPTY);
						this.maxCost = 0;

						return;
					}

					int i3;

					for(i3 = 0; l2 > 0 && i3 < ingredient.getCount(); ++i3)
					{
						int j3 = item.getItemDamage() - l2;
						item.setItemDamage(j3);
						++i;
						l2 = Math.min(item.getItemDamage(), item.getMaxDamage() / 4);
					}

					this.matCost = i3;
				}
				else
				{
//					if (!flag && (item.getItem() != ingredient.getItem() || !item.isItemStackDamageable()) && !fluid.isFluidEqual(ingredient) && ingredient.getItem() != Items.BOOK)
//					{
//						this.outputSlot.setInventorySlotContents(0, ItemStack.EMPTY);
//						this.maxCost = 0;
//
//						return;
//					}

					if (item.isItemStackDamageable() && !flag && !fluid.isFluidEqual(ingredient))
					{
						int l = stack.getMaxDamage() - stack.getItemDamage();
						int i1 = ingredient.getMaxDamage() - ingredient.getItemDamage();
						int j1 = i1 + item.getMaxDamage() * 12 / 100;
						int k1 = l + j1;
						int l1 = item.getMaxDamage() - k1;

						if (l1 < 0)
						{
							l1 = 0;
						}

						if (l1 < item.getItemDamage())
						{
							item.setItemDamage(l1);
							i += 2;
						}
					}

					boolean flag2 = false;
					boolean flag3 = false;
					boolean flag4 = false;
					if(flag || ingredient.isItemEnchanted())
					{
						Map<Enchantment, Integer> map1 = EnchantmentHelper.getEnchantments(ingredient);

						for (Enchantment enchantment1 : map1.keySet())
						{
							if (enchantment1 != null)
							{
								int i2 = map.containsKey(enchantment1) ? ((Integer)map.get(enchantment1)).intValue() : 0;
								int j2 = ((Integer)map1.get(enchantment1)).intValue();

								boolean flag1 = enchantment1.canApply(stack);

								if(ForgottenAnvilHelper.enchantments.containsKey(enchantment1) && j2 == ForgottenAnvilHelper.enchantments.get(enchantment1))
									flag4 = true;
								else
									j2 = i2 == j2 ? j2 + 1 : Math.max(j2, i2);

								if (this.player.capabilities.isCreativeMode || stack.getItem() == Items.ENCHANTED_BOOK)
								{
									flag1 = true;
								}

								for (Enchantment enchantment : map.keySet())
								{
									if (enchantment != enchantment1 && !enchantment1.isCompatibleWith(enchantment))
									{
										flag1 = false;
										++i;
									}
								}

								if (!flag1)
								{
									flag3 = true;
								}
								else
								{
									flag2 = true;

									if(!flag4)
									{
										if (j2 > enchantment1.getMaxLevel())
										{
											j2 = enchantment1.getMaxLevel();
										}
									}

									map.put(enchantment1, Integer.valueOf(j2));
									int k3 = 0;

									switch (enchantment1.getRarity())
									{
										case COMMON:
											k3 = 1;
											break;
										case UNCOMMON:
											k3 = 2;
											break;
										case RARE:
											k3 = 4;
											break;
										case VERY_RARE:
											k3 = 8;
									}

									if (flag)
									{
										k3 = Math.max(1, k3 / 2);
									}

									i += k3 * j2;

									if (stack.getCount() > 1)
									{
										i = 40;
									}
								}
							}
						}
					}

					Random rand = new Random();

					if(fluid.isFluidEqual(ingredient) && (item.isItemEnchantable() || item.isItemEnchanted()))
					{
						List<EnchantmentData> list = new ArrayList<>();
						for(Enchantment ed : ForgottenAnvilHelper.enchantments.keySet())
						{
							if(ed.canApply(item))
							{
								EnchantmentData temp = new EnchantmentData(ed, ForgottenAnvilHelper.enchantments.get(ed));
								if(map.isEmpty())
									list.add(temp);
								else
								{
									if(ForgottenAnvilHelper.isCompatible(temp, map))
										list.add(temp);
								}
							}

						}

						if(list.isEmpty())
							return;

						int index = rand.nextInt(list.size());
						EnchantmentData fe = list.get(index);

						if(fe != null)
						{
							if(map.isEmpty())
							{
								map.put(fe.enchantment, fe.enchantmentLevel);
								forgotten = true;
							}
							else
							{
								if (map.get(fe.enchantment) == null)
								{
									map.put(fe.enchantment, fe.enchantmentLevel);
									forgotten = true;
								}
								else
								{
									map.replace(fe.enchantment, fe.enchantmentLevel);
									forgotten = true;
								}
							}

							if(forgotten)
							{
								EnchantmentHelper.setEnchantments(map, item);
								this.outputSlot.setInventorySlotContents(0, item);
								this.detectAndSendChanges();

								return;
							}
						}
					}

					if(ingredient.getItem() == Items.BOOK && item.isItemEnchanted() && !world.isRemote)
					{
						List<Enchantment> ilist = new ArrayList<Enchantment>();
						ilist.addAll(map.keySet());
						int r = rand.nextInt(ilist.size());
						this.denchantment = ilist.get(r);

						ItemStack ebook = new ItemStack(Items.ENCHANTED_BOOK, 1, 0);
						ItemEnchantedBook.addEnchantment(ebook, new EnchantmentData(this.denchantment, map.get(this.denchantment).intValue()));

						this.outputSlot.setInventorySlotContents(0, ebook);
						this.disenchanting = true;
						this.matCost = 1;
						this.maxCost = 1;

						this.detectAndSendChanges();
						return;
					}

					if(ingredient != null)
					{
						if(item != null)
						{
							if(!recipes.isEmpty() && recipes.containsKey(ingredient.getItem()))
							{
								for(AnvilRecipe recipe : recipes.get(ingredient.getItem()))
								{
									if(recipe.matches(item, ingredient))
									{
										this.outputSlot.setInventorySlotContents(0, recipe.getOut().copy());
										this.shouldDestroy = recipe.shouldDestroy();
										this.matCost = recipe.getCost();
										this.maxCost = recipe.getXp();

										this.detectAndSendChanges();
										return;
									}
								}
							}
						}
					}

					if (flag3 && !flag2)
					{
						this.outputSlot.setInventorySlotContents(0, ItemStack.EMPTY);
						this.maxCost = 0;
						return;
					}
				}
			}

			if (StringUtils.isBlank(this.repairedItemName))
			{
				if (stack.hasDisplayName())
				{
					k = 1;
					i += k;
					item.clearCustomName();
				}
			}
			else if (!this.repairedItemName.equals(stack.getDisplayName()))
			{
				k = 1;
				i += k;
				item.setStackDisplayName(this.repairedItemName);
			}
			if (flag && !item.getItem().isBookEnchantable(item, ingredient)) item = ItemStack.EMPTY;

			this.maxCost = j + i;

			if (i <= 0)
			{
				item = ItemStack.EMPTY;
			}

			if (!item.isEmpty())
			{
				int k2 = item.getRepairCost();

				if (!ingredient.isEmpty() && k2 < ingredient.getRepairCost())
				{
					k2 = ingredient.getRepairCost();
				}

				if (k != i || k == 0)
				{
					k2 = k2 * 2 + 1;
				}

				item.setRepairCost(k2);
				EnchantmentHelper.setEnchantments(map, item);
			}

			this.outputSlot.setInventorySlotContents(0, item);
			this.detectAndSendChanges();
		}
	}

	@Override
	public void addListener(IContainerListener listener)
	{
		super.addListener(listener);
		listener.sendWindowProperty(this, GuiHandler.GUI.ANVIL.ordinal(), this.maxCost);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int id, int data)
	{
		if(id == 0)
			this.maxCost = data;
	}

	@Override
	public void onContainerClosed(EntityPlayer playerIn)
	{
		super.onContainerClosed(playerIn);

		if(!this.world.isRemote)
		{
			this.clearContainer(playerIn, this.world, this.inputSlots);
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer playerIn)
	{
		return this.world.getBlockState(this.pos).getBlock() instanceof BlockForgottenAnvil
				&& playerIn.getDistanceSq((double) this.pos.getX() + 0.5D, (double) this.pos.getY() + 0.5D, (double) this.pos.getZ() + 0.5D) <= 64.0D;
	}

	@Override
	@Nonnull
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
	{
		ItemStack stack = ItemStack.EMPTY;
		Slot slot = this.inventorySlots.get(index);

		if(slot != null && slot.getHasStack())
		{
			ItemStack stack1 = slot.getStack();
			stack = stack1.copy();

			if(index == 2)
			{
				if(!this.mergeItemStack(stack1, 3, 39, true))
					return ItemStack.EMPTY;

				slot.onSlotChange(stack1, stack);
			}
			else if(index != 0 && index != 1)
			{
				if(index >= 3 && index < 39 && !this.mergeItemStack(stack1, 0, 2, false))
					return ItemStack.EMPTY;
			}
			else if(!this.mergeItemStack(stack1, 3, 39, false))
			{
				return ItemStack.EMPTY;
			}

			if(stack1.isEmpty())
				slot.putStack(ItemStack.EMPTY);
			else
				slot.onSlotChanged();

			if(stack1.getCount() == stack.getCount())
				return ItemStack.EMPTY;

			slot.onTake(playerIn, stack1);
		}

		this.detectAndSendChanges();
		return stack;
	}

	public void updateItemName(String newName) 
	{
		this.repairedItemName = newName;

		if (this.getSlot(2).getHasStack()) 
		{
			ItemStack itemstack = this.getSlot(2).getStack();

			if (StringUtils.isBlank(newName)) 
				itemstack.clearCustomName();
			else 
				itemstack.setStackDisplayName(this.repairedItemName);
		}

		this.updateOutput();
	}

	public static boolean onAnvilChange(ContainerForgottenAnvil container, ItemStack left, ItemStack right, IInventory outputSlot, String name, int baseCost) {
		AnvilUpdateEvent e = new AnvilUpdateEvent(left, right, name, baseCost);
		if (MinecraftForge.EVENT_BUS.post(e)) return false;
		if (e.getOutput().isEmpty()) return true;

		outputSlot.setInventorySlotContents(0, e.getOutput());
		container.maxCost = e.getCost();
		container.matCost = e.getMaterialCost();
		container.shouldDestroy = true;
		return false;
	}

	public static boolean onOutputTake(EntityPlayer player, @Nonnull ItemStack output, @Nonnull ItemStack left, @Nonnull ItemStack right)
	{
		AnvilOutuputEvent e = new AnvilOutuputEvent(player, left, right, output);
		MinecraftForge.EVENT_BUS.post(e);
		return e.getShouldDestroy();
	}

}
