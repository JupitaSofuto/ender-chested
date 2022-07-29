package com.legacy.ender_chest_horses;

import com.legacy.ender_chest_horses.capabillity.EnderHorseCapability;
import com.legacy.ender_chest_horses.capabillity.util.CapabilityProvider;
import com.legacy.ender_chest_horses.container.EnderHorseInventoryMenu;
import com.legacy.ender_chest_horses.network.PacketHandler;
import com.legacy.ender_chest_horses.network.s_to_c.HorseStatusPacket;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.inventory.HorseInventoryMenu;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.NetworkHooks;

public class HorseEvents
{
	@SubscribeEvent
	public static void onTrack(PlayerEvent.StartTracking event)
	{
		if (event.getTarget()instanceof AbstractHorse horse && event.getEntity()instanceof ServerPlayer sp)
			EnderHorseCapability.ifPresent(horse, (enderHorse) -> PacketHandler.sendToClient(new HorseStatusPacket(horse.getId(), enderHorse.isEnderChested()), sp));
	}

	@SubscribeEvent
	public static void onEntityDeath(LivingDeathEvent event)
	{
		if (event.getEntity() instanceof AbstractHorse)
		{
			EnderHorseCapability.ifPresent((AbstractHorse) event.getEntity(), (horse) ->
			{
				if (horse.isEnderChested())
				{
					if (!event.getEntity().level.isClientSide)
						event.getEntity().spawnAtLocation(Blocks.ENDER_CHEST);

					horse.setEnderChested(false);
				}
			});
		}
	}

	@SubscribeEvent
	public static void onPlayerInteract(PlayerInteractEvent.EntityInteract event)
	{
		if (event.getTarget() instanceof AbstractHorse horse)
			EnderHorseCapability.ifPresent(horse, (enderHorse) -> enderHorse.processInteract(event));
	}

	@SubscribeEvent
	public static void onCapabilityAttached(AttachCapabilitiesEvent<Entity> event)
	{
		if (event.getObject()instanceof AbstractHorse horse && !event.getObject().getCapability(EnderHorseCapability.INSTANCE).isPresent())
			event.addCapability(EnderChestedMod.locate("ender_horse_capability"), new CapabilityProvider(new EnderHorseCapability(horse)));
	}

	@SubscribeEvent
	public static void onContainerOpened(PlayerContainerEvent.Open event)
	{
		if (event.getContainer() instanceof HorseInventoryMenu && event.getEntity()instanceof ServerPlayer player && player.getVehicle() != null && player.getVehicle()instanceof AbstractHorse horse)
		{
			EnderHorseCapability.ifPresent(horse, (enderHorse) ->
			{
				if (enderHorse.isEnderChested())
				{
					HorseEvents.openEnderHorseContainer(player, horse);
				}
			});
		}
	}

	public static void openEnderHorseContainer(ServerPlayer player, AbstractHorse enderHorse)
	{
		NetworkHooks.openScreen((ServerPlayer) player, new SimpleMenuProvider((id, inventory, playerIn) ->
		{
			return new EnderHorseInventoryMenu(id, inventory, enderHorse);
		}, enderHorse.getName()), (buffer) -> buffer.writeInt(enderHorse.getId()));

		player.awardStat(Stats.OPEN_ENDERCHEST);
	}
}
