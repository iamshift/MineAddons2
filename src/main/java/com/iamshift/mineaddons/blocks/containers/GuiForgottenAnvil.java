package com.iamshift.mineaddons.blocks.containers;

import java.io.IOException;

import org.lwjgl.input.Keyboard;

import com.iamshift.mineaddons.core.Refs;
import com.iamshift.mineaddons.init.ModNetwork;
import com.iamshift.mineaddons.network.PacketItemName;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiForgottenAnvil extends GuiContainer implements IContainerListener
{
	private static final ResourceLocation gui = new ResourceLocation(Refs.ID, "textures/gui/anvil.png");
	private ContainerForgottenAnvil anvil;
	private GuiTextField nameField;
	public InventoryPlayer playerInventory;
	
	public GuiForgottenAnvil(InventoryPlayer inventoryIn, World worldIn)
	{
		super(new ContainerForgottenAnvil(inventoryIn, worldIn, Minecraft.getMinecraft().player));
		this.playerInventory = inventoryIn;
		this.anvil = (ContainerForgottenAnvil) this.inventorySlots;
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		Keyboard.enableRepeatEvents(true);
		int i = (this.width - this.xSize) / 2;
		int j = (this.height - this.ySize) / 2;
		this.nameField = new GuiTextField(0, this.fontRenderer, i + 62, j + 24, 103, 12);
		this.nameField.setTextColor(-1);
		this.nameField.setDisabledTextColour(-1);
		this.nameField.setEnableBackgroundDrawing(false);
		this.nameField.setMaxStringLength(35);
		this.inventorySlots.removeListener(this);
		this.inventorySlots.addListener(this);
	}
	
	@Override
	public void onGuiClosed()
	{
		super.onGuiClosed();
		Keyboard.enableRepeatEvents(false);
		this.inventorySlots.removeListener(this);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		GlStateManager.disableLighting();
		GlStateManager.disableBlend();
		this.fontRenderer.drawString("Forgotten Anvil", 60, 6, 16777215);
		
		if(this.anvil.maxCost > 0)
		{
			int i = 8453920;
			boolean flag = true;
			String s = I18n.format("container.repair.cost", this.anvil.maxCost);
			
			if(!this.anvil.getSlot(2).getHasStack())
				flag = false;
			else if(!this.anvil.getSlot(2).canTakeStack(this.playerInventory.player))
				i = 16736352;
			
			if(flag)
			{
				int j = -16777216 | (i & 16579836) >> 2 | i & -16777216;
				int k = this.xSize - 8 - this.fontRenderer.getStringWidth(s);
				
				if(this.fontRenderer.getUnicodeFlag())
				{
					drawRect(k - 3, 65, this.xSize - 7, 77, -16777216);
					drawRect(k - 2, 65, this.xSize - 8, 76, -12895429);
				}
				else
				{
					this.fontRenderer.drawString(s, k, 68, j);
					this.fontRenderer.drawString(s, k + 1, 67, j);
                    this.fontRenderer.drawString(s, k + 1, 68, j);
				}
				
				this.fontRenderer.drawString(s, k, 67, i);
			}
		}
		
		GlStateManager.enableLighting();
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException
	{
		if(this.nameField.textboxKeyTyped(typedChar, keyCode))
			this.renameItem();
		else
			super.keyTyped(typedChar, keyCode);
	}
	
	private void renameItem()
	{
		String s = this.nameField.getText();
		Slot slot = this.anvil.getSlot(0);
		
		if(slot != null && slot.getHasStack() && !slot.getStack().hasDisplayName() && s.equals(slot.getStack().getDisplayName()))
				s = "";
		
		this.anvil.updateItemName(s);
		ModNetwork.INSTANCE.sendToServer(new PacketItemName(s));
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		super.mouseClicked(mouseX, mouseY, mouseButton);
		this.nameField.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
        GlStateManager.disableLighting();
        GlStateManager.disableBlend();
        this.nameField.drawTextBox();
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(gui);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
        this.drawTexturedModalRect(i + 59, j + 20, 0, this.ySize + (this.anvil.getSlot(0).getHasStack() ? 0 : 16), 110, 16);

        if ((this.anvil.getSlot(0).getHasStack() || this.anvil.getSlot(1).getHasStack()) && !this.anvil.getSlot(2).getHasStack())
            this.drawTexturedModalRect(i + 99, j + 45, this.xSize, 0, 28, 21);
	}
	
	@Override
	public void sendAllContents(Container containerToSend, NonNullList<ItemStack> itemsList)
	{
		this.sendSlotContents(containerToSend, 0, containerToSend.getSlot(0).getStack());
	}
	
	@Override
	public void sendSlotContents(Container containerToSend, int slotInd, ItemStack stack)
	{
		if(slotInd == 0)
		{
			this.nameField.setText(stack.isEmpty() ? "" : stack.getDisplayName());
			this.nameField.setEnabled(!stack.isEmpty());
			
			if(!stack.isEmpty())
				this.renameItem();
		}
	}
	
	@Override
	public void sendWindowProperty(Container containerIn, int varToUpdate, int newValue)
	{}
	
	@Override
	public void sendAllWindowProperties(Container containerIn, IInventory inventory)
	{}
}
