package com.legacy.ender_chest_horses.client;

import com.legacy.ender_chest_horses.client.render.EnderChestLayer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.AbstractHorseRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.model.HorseModel;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class HorseClient
{
	@SuppressWarnings({ "unchecked", "resource" })
	public static <T extends AbstractHorseEntity, M extends HorseModel<T>> void initLayers()
	{
		for (EntityRenderer<?> renderer : Minecraft.getInstance().getRenderManager().renderers.values())
			if (renderer instanceof AbstractHorseRenderer)
				addChestLayer((AbstractHorseRenderer<T, M>) renderer);
	}

	private static <T extends AbstractHorseEntity, M extends HorseModel<T>> void addChestLayer(AbstractHorseRenderer<T, M> renderer)
	{
		renderer.addLayer(new EnderChestLayer<T, M>(renderer));
	}

	public static class Events
	{
		@SubscribeEvent
		public void onOpenGui(GuiOpenEvent event)
		{
		}

		@SubscribeEvent
		public void onGuiInit(GuiScreenEvent.InitGuiEvent.Post event)
		{
		}
	}
}
