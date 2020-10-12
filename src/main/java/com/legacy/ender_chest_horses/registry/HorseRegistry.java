package com.legacy.ender_chest_horses.registry;

import net.minecraft.inventory.container.ContainerType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class HorseRegistry
{
	@SubscribeEvent
	public static void registerContainers(Register<ContainerType<?>> event)
	{
		HorseContainers.init(event);
	}

	public static <T extends IForgeRegistryEntry<T>> void register(IForgeRegistry<T> registry, ResourceLocation key, T object)
	{
		object.setRegistryName(key);
		registry.register(object);
	}
}
