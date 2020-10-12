package com.legacy.ender_chest_horses;

import com.legacy.ender_chest_horses.capabillity.EnderHorseCapability;
import com.legacy.ender_chest_horses.capabillity.util.CapabilityProvider;
import com.legacy.ender_chest_horses.container.EnderHorseInventoryContainer;

import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.HorseInventoryContainer;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.stats.Stats;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.NetworkHooks;

public class HorseEvents
{
	@SubscribeEvent
	public void onLivingUpdate(LivingUpdateEvent event)
	{
		if (event.getEntityLiving() instanceof AbstractHorseEntity)
			EnderHorseCapability.ifPresent((AbstractHorseEntity) event.getEntityLiving(), (horse) -> horse.tick());
	}

	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent event)
	{
		if (event.getEntity() instanceof AbstractHorseEntity)
		{
			AbstractHorseEntity horse = (AbstractHorseEntity) event.getEntity();
			EnderHorseCapability.ifPresent(horse, (enderHorse) ->
			{
			});

		}
	}

	@SubscribeEvent
	public void onEntityDeath(LivingDeathEvent event)
	{
		if (event.getEntityLiving() instanceof AbstractHorseEntity)
		{
			EnderHorseCapability.ifPresent((AbstractHorseEntity) event.getEntityLiving(), (horse) ->
			{
				if (horse.isEnderChested())
				{
					if (!event.getEntityLiving().world.isRemote)
					{
						event.getEntityLiving().entityDropItem(Blocks.ENDER_CHEST);
					}

					horse.setEnderChested(false);
				}
			});
		}
	}

	@SubscribeEvent
	public void onPlayerInteract(PlayerInteractEvent.EntityInteract event)
	{
		if (event.getTarget() instanceof AbstractHorseEntity)
		{
			AbstractHorseEntity horse = (AbstractHorseEntity) event.getTarget();
			EnderHorseCapability.ifPresent(horse, (enderHorse) -> enderHorse.processInteract(event));
		}
	}

	@SubscribeEvent
	public void onCapabilityAttached(AttachCapabilitiesEvent<Entity> event)
	{
		if (event.getObject() instanceof AbstractHorseEntity && !event.getObject().getCapability(EnderHorseCapability.INSTANCE).isPresent())
		{
			event.addCapability(EnderChestedMod.locate("ender_horse_capability"), new CapabilityProvider(new EnderHorseCapability((AbstractHorseEntity) event.getObject())));
		}
	}

	@SubscribeEvent
	public void onContainerOpened(PlayerContainerEvent.Open event)
	{
		if (event.getContainer() instanceof HorseInventoryContainer && event.getPlayer().getRidingEntity() != null && event.getPlayer().getRidingEntity() instanceof AbstractHorseEntity && event.getPlayer() instanceof ServerPlayerEntity)
		{
			EnderHorseCapability.ifPresent((AbstractHorseEntity) event.getPlayer().getRidingEntity(), (enderHorse) ->
			{
				if (enderHorse.isEnderChested())
				{
					HorseEvents.openEnderHorseContainer((ServerPlayerEntity) event.getPlayer(), (AbstractHorseEntity) event.getPlayer().getRidingEntity());
				}
			});
		}
	}

	public static void openEnderHorseContainer(ServerPlayerEntity player, AbstractHorseEntity enderHorse)
	{
		NetworkHooks.openGui((ServerPlayerEntity) player, new SimpleNamedContainerProvider((id, inventory, playerIn) ->
		{
			return new EnderHorseInventoryContainer(id, inventory, enderHorse);
		}, enderHorse.getName()), (buffer) -> buffer.writeInt(enderHorse.getEntityId()));

		player.addStat(Stats.OPEN_ENDERCHEST);
	}
}
