package com.legacy.ender_chest_horses;

import com.legacy.ender_chest_horses.capabillity.IEnderHorse;
import com.legacy.ender_chest_horses.client.HorseClient;
import com.legacy.ender_chest_horses.client.gui.EnderChestHorseScreen;
import com.legacy.ender_chest_horses.network.PacketHandler;
import com.legacy.ender_chest_horses.registry.HorseRegistry;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(EnderChestedMod.MODID)
public class EnderChestedMod
{
	public static final String NAME = "Ender Chested";
	public static final String MODID = "ender_chested";

	public static ResourceLocation locate(String name)
	{
		return new ResourceLocation(EnderChestedMod.MODID, name);
	}

	public static String find(String name)
	{
		return EnderChestedMod.MODID + ":" + name;
	}

	public EnderChestedMod()
	{
		IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
		IEventBus forgeBus = MinecraftForge.EVENT_BUS;

		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
		{
			modBus.addListener(EnderChestedMod::clientInit);

			modBus.addListener(HorseClient::registerLayers);
			modBus.addListener(HorseClient::initRenderLayers);
		});

		modBus.register(HorseRegistry.class);

		forgeBus.addListener(EnderChestedMod::onCapsRegistered);
		forgeBus.register(HorseEvents.class);
		
		modBus.addListener((FMLCommonSetupEvent event) -> PacketHandler.register());
	}

	public static void onCapsRegistered(final RegisterCapabilitiesEvent event)
	{
		event.register(IEnderHorse.class);
	}

	public static void clientInit(FMLClientSetupEvent event)
	{
		MenuScreens.register(HorseRegistry.ENDER_HORSE_INVENTORY.get(), EnderChestHorseScreen::new);
	}
}
