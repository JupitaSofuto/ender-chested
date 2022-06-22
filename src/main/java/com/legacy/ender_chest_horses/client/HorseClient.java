package com.legacy.ender_chest_horses.client;

import com.legacy.ender_chest_horses.EnderChestedMod;
import com.legacy.ender_chest_horses.client.render.EnderChestLayer;
import com.legacy.ender_chest_horses.client.render.EnderChestedHorseModel;

import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.AbstractHorseRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraftforge.client.event.EntityRenderersEvent;

public class HorseClient
{
	public static final ModelLayerLocation ENDER_CHEST = new ModelLayerLocation(EnderChestedMod.locate("ender_chest"), "main");

	@SuppressWarnings({ "rawtypes", "unchecked", "resource" })
	public static void initRenderLayers(EntityRenderersEvent.AddLayers event)
	{
		for (EntityRenderer<?> renderer : Minecraft.getInstance().getEntityRenderDispatcher().renderers.values())
		{
			if (renderer instanceof AbstractHorseRenderer horseRender)
				horseRender.addLayer(new EnderChestLayer(horseRender));
		}
	}

	public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event)
	{
		event.registerLayerDefinition(ENDER_CHEST, () -> EnderChestedHorseModel.createChestLayer());
	}
}
