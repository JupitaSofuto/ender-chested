package com.legacy.ender_chest_horses;

import com.legacy.ender_chest_horses.capabillity.EnderHorseCapability;
import com.legacy.ender_chest_horses.capabillity.IEnderHorse;
import com.legacy.ender_chest_horses.capabillity.util.CapabilityStorage;
import com.legacy.ender_chest_horses.client.HorseClient;
import com.legacy.ender_chest_horses.client.gui.EnderChestHorseScreen;
import com.legacy.ender_chest_horses.registry.HorseContainers;
import com.legacy.ender_chest_horses.registry.HorseRegistry;

import net.minecraft.client.gui.ScreenManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(EnderChestedMod.MODID)
public class EnderChestedMod
{
	public static final String NAME = "Ender Chested";
	public static final String MODID = "ender_chest_horses";

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
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () ->
		{
			FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientInit);
			FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientLoadComplete);
		});

		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonInit);
		FMLJavaModLoadingContext.get().getModEventBus().register(HorseRegistry.class);
	}

	public void commonInit(FMLCommonSetupEvent event)
	{
		MinecraftForge.EVENT_BUS.register(new HorseEvents());

		CapabilityManager.INSTANCE.register(IEnderHorse.class, new CapabilityStorage(), EnderHorseCapability::new);
	}

	public void clientInit(FMLClientSetupEvent event)
	{
		ScreenManager.registerFactory(HorseContainers.ENDER_HORSE_INVENTORY, EnderChestHorseScreen::new);

		MinecraftForge.EVENT_BUS.register(new HorseClient.Events());
	}

	public void clientLoadComplete(FMLLoadCompleteEvent event)
	{
		HorseClient.initLayers();
	}
}
