package com.legacy.ender_chest_horses.registry;

import com.legacy.ender_chest_horses.EnderChestedMod;
import com.legacy.ender_chest_horses.container.EnderHorseInventoryMenu;

import net.minecraft.core.Registry;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.RegisterEvent;

public class HorseRegistry
{
	public static final Lazy<MenuType<EnderHorseInventoryMenu>> ENDER_HORSE_INVENTORY = Lazy.of(() -> new MenuType<>((IContainerFactory<EnderHorseInventoryMenu>) (id, playerInventory, buffer) -> new EnderHorseInventoryMenu(id, playerInventory, buffer)));

	@SubscribeEvent
	public static void onRegister(RegisterEvent event)
	{
		if (event.getRegistryKey().equals(Registry.MENU_REGISTRY))
			event.register(Registry.MENU_REGISTRY, EnderChestedMod.locate("ender_horse_inventory"), () -> ENDER_HORSE_INVENTORY.get());
	}
}
