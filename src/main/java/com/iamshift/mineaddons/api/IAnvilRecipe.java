package com.iamshift.mineaddons.api;

import net.minecraft.item.ItemStack;

public interface IAnvilRecipe
{
	/**
	 * @return output
	 */
	public ItemStack getOut();
	
	/**
	 * @return item (left slot)
	 */
	public ItemStack getIn();
	
	/**
	 * @return ingredient (midle slot)
	 */
	public ItemStack getIng();
	
	/**
	 * @return material cost
	 */
	public int getCost();
	
	/**
	 * @return experience required
	 */
	public int getXp();
	
	/**
	 * @return if should destroy item
	 */
	public boolean shouldDestroy();
	
	
	public boolean matches(ItemStack in, ItemStack ing);
}
