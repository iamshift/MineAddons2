package com.iamshift.mineaddons.proxy;

import org.lwjgl.input.Keyboard;

import com.iamshift.mineaddons.core.Refs;
import com.iamshift.mineaddons.entities.EntityAncientCarp;
import com.iamshift.mineaddons.entities.EntityBlazelier;
import com.iamshift.mineaddons.entities.EntityEnderCarp;
import com.iamshift.mineaddons.entities.EntityHellhound;
import com.iamshift.mineaddons.entities.EntityPeaceCreeper;
import com.iamshift.mineaddons.entities.EntityTrueCreeper;
import com.iamshift.mineaddons.entities.EntityVoidCreeper;
import com.iamshift.mineaddons.entities.EntityZlama;
import com.iamshift.mineaddons.entities.EntityiSheep;
import com.iamshift.mineaddons.entities.boss.EntityDeadHorse;
import com.iamshift.mineaddons.entities.boss.EntityGhostRider;
import com.iamshift.mineaddons.entities.boss.EntityVoix;
import com.iamshift.mineaddons.entities.boss.EntityWitherBlaze;
import com.iamshift.mineaddons.entities.boss.renders.RenderDeadHorse;
import com.iamshift.mineaddons.entities.boss.renders.RenderGhostRider;
import com.iamshift.mineaddons.entities.boss.renders.RenderVoix;
import com.iamshift.mineaddons.entities.boss.renders.RenderWitherBlaze;
import com.iamshift.mineaddons.entities.items.EntitySpiritBomb;
import com.iamshift.mineaddons.entities.items.EntityVoidball;
import com.iamshift.mineaddons.entities.renders.RenderAncientCarp;
import com.iamshift.mineaddons.entities.renders.RenderBlazelier;
import com.iamshift.mineaddons.entities.renders.RenderEnderCarp;
import com.iamshift.mineaddons.entities.renders.RenderHellhound;
import com.iamshift.mineaddons.entities.renders.RenderPeaceCreeper;
import com.iamshift.mineaddons.entities.renders.RenderSpiritBomb;
import com.iamshift.mineaddons.entities.renders.RenderTrueCreeper;
import com.iamshift.mineaddons.entities.renders.RenderVoidCreeper;
import com.iamshift.mineaddons.entities.renders.RenderVoidball;
import com.iamshift.mineaddons.entities.renders.RenderZlama;
import com.iamshift.mineaddons.entities.renders.RenderiSheep;
import com.iamshift.mineaddons.entities.renders.layers.LayerElytraEnchant;
import com.iamshift.mineaddons.entities.renders.layers.LayerRespirator;
import com.iamshift.mineaddons.entities.renders.layers.LayerWings;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{
	public static KeyBinding toggleRocket;
	public static KeyBinding toggleVision;
	
	@Override
	public void registerItemRenderer(Item item, int meta, String id) 
	{
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(item.getRegistryName(), id));
	}

	@Override
	public void registerVariantRenderer(Item item, int meta, String filename, String id)
	{
		ModelLoader.setCustomModelResourceLocation(item, meta, new ModelResourceLocation(new ResourceLocation(Refs.ID, filename), id));
	}

	@Override
	public void registerEntityRender()
	{
		RenderingRegistry.registerEntityRenderingHandler(EntityTrueCreeper.class, new IRenderFactory() 
		{
			@Override
			public Render createRenderFor(RenderManager manager) 
			{
				return new RenderTrueCreeper(manager);
			}

		});

		RenderingRegistry.registerEntityRenderingHandler(EntityPeaceCreeper.class, new IRenderFactory() 
		{
			@Override
			public Render createRenderFor(RenderManager manager) 
			{
				return new RenderPeaceCreeper(manager);
			}

		});
		
		RenderingRegistry.registerEntityRenderingHandler(EntityDeadHorse.class, new IRenderFactory() 
		{
			@Override
			public Render createRenderFor(RenderManager manager) 
			{
				return new RenderDeadHorse(manager);
			}

		});
		
		RenderingRegistry.registerEntityRenderingHandler(EntityGhostRider.class, new IRenderFactory() 
		{
			@Override
			public Render createRenderFor(RenderManager manager) 
			{
				return new RenderGhostRider(manager);
			}

		});

		
		RenderingRegistry.registerEntityRenderingHandler(EntityEnderCarp.class, new IRenderFactory() 
		{
			@Override
			public Render createRenderFor(RenderManager manager) 
			{
				return new RenderEnderCarp(manager);
			}

		});

		RenderingRegistry.registerEntityRenderingHandler(EntityAncientCarp.class, new IRenderFactory() 
		{
			@Override
			public Render createRenderFor(RenderManager manager) 
			{
				return new RenderAncientCarp(manager);
			}

		});

		RenderingRegistry.registerEntityRenderingHandler(EntityVoidCreeper.class, new IRenderFactory() 
		{
			@Override
			public Render createRenderFor(RenderManager manager) 
			{
				return new RenderVoidCreeper(manager);
			}

		});
		
		RenderingRegistry.registerEntityRenderingHandler(EntityVoix.class, new IRenderFactory() 
		{
			@Override
			public Render createRenderFor(RenderManager manager) 
			{
				return new RenderVoix(manager);
			}

		});
		
		RenderingRegistry.registerEntityRenderingHandler(EntitySpiritBomb.class, new IRenderFactory() 
		{
			@Override
			public Render createRenderFor(RenderManager manager) 
			{
				return new RenderSpiritBomb(manager);
			}

		});
		

		RenderingRegistry.registerEntityRenderingHandler(EntityHellhound.class, new IRenderFactory() 
		{
			@Override
			public Render createRenderFor(RenderManager manager) 
			{
				return new RenderHellhound(manager);
			}

		});

		RenderingRegistry.registerEntityRenderingHandler(EntityiSheep.class, new IRenderFactory() 
		{
			@Override
			public Render createRenderFor(RenderManager manager) 
			{
				return new RenderiSheep(manager);
			}

		});

		RenderingRegistry.registerEntityRenderingHandler(EntityZlama.class, new IRenderFactory() 
		{
			@Override
			public Render createRenderFor(RenderManager manager) 
			{
				return new RenderZlama(manager);
			}

		});
		
		RenderingRegistry.registerEntityRenderingHandler(EntityVoidball.class, new IRenderFactory() 
		{
			@Override
			public Render createRenderFor(RenderManager manager) 
			{
				return new RenderVoidball(manager);
			}

		});
		
		RenderingRegistry.registerEntityRenderingHandler(EntityWitherBlaze.class, new IRenderFactory() 
		{
			@Override
			public Render createRenderFor(RenderManager manager) 
			{
				return new RenderWitherBlaze(manager);
			}

		});
		
		RenderingRegistry.registerEntityRenderingHandler(EntityBlazelier.class, new IRenderFactory() 
		{
			@Override
			public Render createRenderFor(RenderManager manager) 
			{
				return new RenderBlazelier(manager);
			}

		});
	}
	
	@Override
	public void addLayers()
	{
		for(RenderPlayer rp : Minecraft.getMinecraft().getRenderManager().getSkinMap().values())
		{
			rp.addLayer(new LayerElytraEnchant(rp));
			rp.addLayer(new LayerWings(rp));
			rp.addLayer(new LayerRespirator(rp));
		}
		
		Render<?> render = Minecraft.getMinecraft().getRenderManager().getEntityClassRenderObject(EntityArmorStand.class);
		((RenderLivingBase<?>) render).addLayer(new LayerElytraEnchant((RenderLivingBase<?>) render));
		((RenderLivingBase<?>) render).addLayer(new LayerWings((RenderLivingBase<?>) render));
		((RenderLivingBase<?>) render).addLayer(new LayerRespirator((RenderLivingBase<?>) render));
 	}
	
	@Override
	public void addKeybind()
	{
		toggleRocket = new KeyBinding("key.mineaddons.toggle.rocket.desc", Keyboard.KEY_Z, "key.mineaddons.cat");
		ClientRegistry.registerKeyBinding(toggleRocket);
		
		toggleVision = new KeyBinding("key.mineaddons.toggle.vision.desc", Keyboard.KEY_NUMPAD1, "key.mineaddons.cat");
		ClientRegistry.registerKeyBinding(toggleVision);
	}
}
