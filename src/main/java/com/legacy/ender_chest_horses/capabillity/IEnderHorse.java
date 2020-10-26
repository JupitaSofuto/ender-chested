package com.legacy.ender_chest_horses.capabillity;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public interface IEnderHorse
{
	void writeAdditional(CompoundNBT nbt);

	void read(CompoundNBT nbt);

	boolean isEnderChested();

	void setEnderChested(boolean converting);

	void setMarkedTime(int time);

	void tick();
	
	void processInteract(PlayerInteractEvent.EntityInteract eventIn);
}
