package com.legacy.ender_chest_horses.capabillity.util;

import com.legacy.ender_chest_horses.capabillity.IEnderHorse;
import com.legacy.ender_chest_horses.capabillity.EnderHorseCapability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class CapabilityProvider implements ICapabilitySerializable<CompoundNBT>
{
	private final LazyOptional<IEnderHorse> enderHorseHandler;

	public CapabilityProvider(IEnderHorse horseCapability)
	{
		this.enderHorseHandler = LazyOptional.of(() -> horseCapability);
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side)
	{
		return cap == EnderHorseCapability.INSTANCE ? this.enderHorseHandler.cast() : LazyOptional.empty();
	}

	@Override
	public void deserializeNBT(CompoundNBT compound)
	{
		this.enderHorseHandler.orElse(null).read(compound);
	}

	@Override
	public CompoundNBT serializeNBT()
	{
		CompoundNBT compound = new CompoundNBT();
		this.enderHorseHandler.orElse(null).writeAdditional(compound);
		return compound;
	}
}