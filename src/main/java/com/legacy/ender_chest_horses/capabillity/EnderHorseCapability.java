package com.legacy.ender_chest_horses.capabillity;

import java.util.function.Consumer;
import java.util.function.Function;

import javax.annotation.Nullable;

import com.legacy.ender_chest_horses.HorseEvents;

import net.minecraft.entity.passive.horse.AbstractChestedHorseEntity;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.passive.horse.HorseEntity;
import net.minecraft.entity.passive.horse.LlamaEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.HorseArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class EnderHorseCapability implements IEnderHorse
{
	@CapabilityInject(IEnderHorse.class)
	public static Capability<IEnderHorse> INSTANCE = null;

	private boolean enderChested;
	private AbstractHorseEntity enderHorse;
	protected Inventory horseChest;

	public EnderHorseCapability()
	{
	}

	public EnderHorseCapability(AbstractHorseEntity horseIn)
	{
		super();
		this.enderHorse = horseIn;
	}

	public static IEnderHorse get(AbstractHorseEntity player)
	{
		return EnderHorseCapability.getIfPresent(player, (enderHorse) -> enderHorse);
	}

	public static <E extends AbstractHorseEntity> void ifPresent(E enderHorseIn, Consumer<IEnderHorse> action)
	{
		if (enderHorseIn != null && enderHorseIn.getCapability(INSTANCE).isPresent())
			action.accept(enderHorseIn.getCapability(INSTANCE).resolve().get());
	}

	public static <E extends AbstractHorseEntity> void ifPresent(E enderHorseIn, Consumer<IEnderHorse> action, Consumer<E> elseAction)
	{
		if (enderHorseIn != null)
		{
			if (enderHorseIn.getCapability(INSTANCE).isPresent())
				action.accept(enderHorseIn.getCapability(INSTANCE).resolve().get());
			else
				elseAction.accept(enderHorseIn);
		}
	}

	@Nullable
	public static <E extends AbstractHorseEntity, R> R getIfPresent(E enderHorseIn, Function<IEnderHorse, R> action)
	{
		if (enderHorseIn != null && enderHorseIn.getCapability(INSTANCE).isPresent())
			return action.apply(enderHorseIn.getCapability(INSTANCE).resolve().get());

		return null;
	}

	@Nullable
	public static <E extends AbstractHorseEntity, R> R getIfPresent(E enderHorseIn, Function<IEnderHorse, R> action, Function<E, R> elseAction)
	{
		if (enderHorseIn != null)
		{
			if (enderHorseIn.getCapability(INSTANCE).isPresent())
				return action.apply(enderHorseIn.getCapability(INSTANCE).resolve().get());
			else
				return elseAction.apply(enderHorseIn);
		}

		return null;
	}

	@Override
	public void writeAdditional(CompoundNBT compound)
	{
		compound.putBoolean("EnderChested", this.isEnderChested());
	}

	@Override
	public void read(CompoundNBT compound)
	{
		this.setEnderChested(compound.getBoolean("EnderChested"));
	}

	@Override
	public void tick()
	{
		if (!this.enderHorse.world.isRemote && this.isEnderChested())
			this.enderHorse.world.setEntityState(this.enderHorse, (byte) 8);
	}

	@Override
	public void processInteract(PlayerInteractEvent.EntityInteract event)
	{
		PlayerEntity player = event.getPlayer();
		Hand hand = event.getHand();
		ItemStack stack = player.getHeldItem(hand);
		boolean canChest = !(this.enderHorse instanceof LlamaEntity) && (this.enderHorse instanceof AbstractChestedHorseEntity && !((AbstractChestedHorseEntity) this.enderHorse).hasChest() || !(this.enderHorse instanceof AbstractChestedHorseEntity));

		if (this.enderHorse instanceof AbstractChestedHorseEntity && this.isEnderChested() && stack.getItem().getTags().contains(Tags.Blocks.CHESTS.getName()) && !stack.getItem().getTags().contains(Tags.Blocks.CHESTS_ENDER.getName()))
		{
			if (!player.world.isRemote)
				event.setCanceled(true);
		}
		else if (!this.isEnderChested() && canChest && stack.getItem().getTags().contains(Tags.Blocks.CHESTS_ENDER.getName()) && this.enderHorse.isTame())
		{
			if (!player.world.isRemote)
			{
				player.swing(hand, true);

				this.enderHorse.playSound(SoundEvents.ENTITY_DONKEY_CHEST, 1.0F, 1.0F);
				event.setCanceled(true);
			}

			this.setEnderChested(true);
		}
		else if (!player.world.isRemote && this.isEnderChested() && player instanceof ServerPlayerEntity && (player.isSneaking() || stack.getItem() == Items.SADDLE || this.enderHorse instanceof HorseEntity && stack.getItem() instanceof HorseArmorItem))
		{
			HorseEvents.openEnderHorseContainer((ServerPlayerEntity) player, this.enderHorse);
			event.setCanceled(true);
		}
	}

	@Override
	public boolean isEnderChested()
	{
		return enderChested;
	}

	@Override
	public boolean setEnderChested(boolean chestedIn)
	{
		return this.enderChested = chestedIn;
	}
}
