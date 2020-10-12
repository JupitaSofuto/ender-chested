package com.legacy.ender_chest_horses.capabillity.util;

import com.legacy.ender_chest_horses.capabillity.IEnderHorse;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class CapabilityStorage implements IStorage<IEnderHorse>
{
	@Override
	public INBT writeNBT(Capability<IEnderHorse> capability, IEnderHorse instance, Direction side)
	{
		CompoundNBT compound = new CompoundNBT();
		instance.writeAdditional(compound);
		return compound;
	}

	@Override
	public void readNBT(Capability<IEnderHorse> capability, IEnderHorse instance, Direction side, INBT nbt)
	{
		CompoundNBT compound = (CompoundNBT) nbt;
		instance.read(compound);
	}

}