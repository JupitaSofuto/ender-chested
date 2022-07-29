package com.legacy.ender_chest_horses.capabillity;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public interface IEnderHorse
{
	public static final Capability<IEnderHorse> INSTANCE = CapabilityManager.get(new CapabilityToken<>()
	{
	});

	void writeAdditional(CompoundTag nbt);

	void read(CompoundTag nbt);

	boolean isEnderChested();

	void setEnderChested(boolean converting);
	
	void processInteract(PlayerInteractEvent.EntityInteract eventIn);
}
