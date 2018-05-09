package com.iamshift.mineaddons.events;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.IContextSetter;

public class AnvilOutuputEvent extends Event implements IContextSetter
{
	private EntityPlayer player;
	@Nonnull
	private final ItemStack left;
	@Nonnull
	private final ItemStack right;
	@Nonnull
	private final ItemStack output;
	private boolean shouldDestroy;
	
	public AnvilOutuputEvent(EntityPlayer player, @Nonnull ItemStack left, @Nonnull ItemStack right, @Nonnull ItemStack output)
	{
		this.player = player;
		this.output = output;
		this.left = left;
		this.right = right;
		this.setShouldDestroy(true);
	}
	
	public EntityPlayer getPlayer() { return player; }
	
	@Nonnull
	public ItemStack getItemResult() { return output; }
	
	@Nonnull
	public ItemStack getItemInput() { return left; }
	
	@Nonnull
	public ItemStack getIngredient() { return right; }
	
	public boolean getShouldDestroy() { return shouldDestroy; }
	public void setShouldDestroy(boolean shouldDestroy) { this.shouldDestroy = shouldDestroy; }
}
