package com.legacy.ender_chest_horses.capabillity;

import java.util.function.Consumer;
import java.util.function.Function;

import javax.annotation.Nullable;

import com.legacy.ender_chest_horses.HorseEvents;
import com.legacy.ender_chest_horses.network.PacketHandler;
import com.legacy.ender_chest_horses.network.s_to_c.HorseStatusPacket;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.HorseArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class EnderHorseCapability implements IEnderHorse
{
	private boolean enderChested;
	private int markedTime = 20;
	private AbstractHorse enderHorse;
	protected SimpleContainer horseChest;

	public EnderHorseCapability()
	{
	}

	public EnderHorseCapability(AbstractHorse horseIn)
	{
		super();
		this.enderHorse = horseIn;
	}

	public static IEnderHorse get(AbstractHorse horse)
	{
		return EnderHorseCapability.getIfPresent(horse, (enderHorse) -> enderHorse);
	}

	public static <E extends AbstractHorse> void ifPresent(E enderHorseIn, Consumer<IEnderHorse> action)
	{
		if (enderHorseIn != null && enderHorseIn.getCapability(IEnderHorse.INSTANCE).isPresent())
			action.accept(enderHorseIn.getCapability(IEnderHorse.INSTANCE).resolve().get());
	}

	public static <E extends AbstractHorse> void ifPresent(E enderHorseIn, Consumer<IEnderHorse> action, Consumer<E> elseAction)
	{
		if (enderHorseIn != null)
		{
			if (enderHorseIn.getCapability(IEnderHorse.INSTANCE).isPresent())
				action.accept(enderHorseIn.getCapability(IEnderHorse.INSTANCE).resolve().get());
			else
				elseAction.accept(enderHorseIn);
		}
	}

	@Nullable
	public static <E extends AbstractHorse, R> R getIfPresent(E enderHorseIn, Function<IEnderHorse, R> action)
	{
		if (enderHorseIn != null && enderHorseIn.getCapability(IEnderHorse.INSTANCE).isPresent())
			return action.apply(enderHorseIn.getCapability(IEnderHorse.INSTANCE).resolve().get());

		return null;
	}

	@Nullable
	public static <E extends AbstractHorse, R> R getIfPresent(E enderHorseIn, Function<IEnderHorse, R> action, Function<E, R> elseAction)
	{
		if (enderHorseIn != null)
		{
			if (enderHorseIn.getCapability(IEnderHorse.INSTANCE).isPresent())
				return action.apply(enderHorseIn.getCapability(IEnderHorse.INSTANCE).resolve().get());
			else
				return elseAction.apply(enderHorseIn);
		}

		return null;
	}

	@Override
	public void writeAdditional(CompoundTag compound)
	{
		compound.putBoolean("EnderChested", this.isEnderChested());
	}

	@Override
	public void read(CompoundTag compound)
	{
		this.setEnderChested(compound.getBoolean("EnderChested"));
	}

	@Override
	public void tick()
	{
		/*if (!this.enderHorse.level.isClientSide && this.isEnderChested() && this.enderHorse.tickCount % 20 == 0)
		{
			PacketHandler.sendToAllClients(new HorseStatusPacket(this.enderHorse.getId(), this.isEnderChested()), this.enderHorse.getLevel());
		}*/
	}

	@Override
	public void processInteract(PlayerInteractEvent.EntityInteract event)
	{
		Player player = event.getPlayer();
		InteractionHand hand = event.getHand();
		ItemStack stack = player.getItemInHand(hand);
		boolean canChest = !(this.enderHorse instanceof Llama) && (this.enderHorse instanceof AbstractChestedHorse chestHorse && !chestHorse.hasChest() || !(this.enderHorse instanceof AbstractChestedHorse));

		if (this.enderHorse instanceof AbstractChestedHorse && this.isEnderChested() && stack.is(Tags.Items.CHESTS) && !stack.is(Tags.Items.CHESTS_ENDER))
		{
			if (this.isEnderChested())
			{
				this.setEnderChested(false);
				this.enderHorse.spawnAtLocation(new ItemStack(Blocks.ENDER_CHEST.asItem()));
				this.enderHorse.level.broadcastEntityEvent(this.enderHorse, (byte) 9);
			}
			/*else
			{
				if (!player.world.isRemote)
					event.setCanceled(true);
			}*/

			return;
		}
		else if (!this.isEnderChested() && canChest && stack.is(Tags.Items.CHESTS_ENDER) && this.enderHorse.isTamed())
		{
			if (!player.level.isClientSide)
			{
				player.swing(hand, true);

				if (!player.isCreative())
					stack.shrink(1);

				this.enderHorse.playSound(SoundEvents.DONKEY_CHEST, 1.0F, 1.0F);
				event.setCanceled(true);
			}

			this.setEnderChested(true);
		}
		else if (!player.level.isClientSide && this.isEnderChested() && player instanceof ServerPlayer sp && (player.isShiftKeyDown() || stack.getItem() == Items.SADDLE || this.enderHorse instanceof Horse && stack.getItem() instanceof HorseArmorItem))
		{
			HorseEvents.openEnderHorseContainer(sp, this.enderHorse);
			event.setCanceled(true);
		}
	}

	@Override
	public boolean isEnderChested()
	{
		return enderChested;
	}

	@Override
	public void setEnderChested(boolean chestedIn)
	{
		this.enderChested = chestedIn;
		
		//PacketHandler.sendToAllClients(new HorseStatusPacket(this.enderHorse.getId(), this.isEnderChested()), this.enderHorse.getLevel());
	}

	@Override
	public void setMarkedTime(int time)
	{
		this.markedTime = time;
	}
}
