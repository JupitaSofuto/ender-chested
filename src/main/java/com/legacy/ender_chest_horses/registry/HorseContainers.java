package com.legacy.ender_chest_horses.registry;

import java.util.ArrayList;
import java.util.List;

import com.legacy.ender_chest_horses.EnderChestedMod;
import com.legacy.ender_chest_horses.container.EnderHorseInventoryContainer;

import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.network.IContainerFactory;

public class HorseContainers
{
	private static List<ContainerType<?>> CONTAINERS = new ArrayList<>();

	// IForgeContainerType
	public static final ContainerType<EnderHorseInventoryContainer> ENDER_HORSE_INVENTORY = register("ender_horse_inventory", (IContainerFactory<EnderHorseInventoryContainer>) (id, playerInventory, buffer) -> new EnderHorseInventoryContainer(id, playerInventory, buffer));

	public static <T extends Container> ContainerType<T> register(String key, ContainerType.IFactory<T> factory)
	{
		ContainerType<T> type = new ContainerType<>(factory);
		type.setRegistryName(EnderChestedMod.locate(key));
		CONTAINERS.add(type);
		return type;
	}

	public static void init(Register<ContainerType<?>> event)
	{
		CONTAINERS.forEach(event.getRegistry()::register);
	}
}
