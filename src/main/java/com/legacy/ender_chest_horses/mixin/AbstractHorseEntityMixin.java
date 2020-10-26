package com.legacy.ender_chest_horses.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.legacy.ender_chest_horses.capabillity.EnderHorseCapability;

import net.minecraft.entity.passive.horse.AbstractHorseEntity;

@Mixin(AbstractHorseEntity.class)
public class AbstractHorseEntityMixin
{
	// We don't have to make a packet system if we do this.
	@Inject(at = @At("HEAD"), method = "handleStatusUpdate(B)V", cancellable = true)
	private void handleStatusUpdate(byte id, CallbackInfo callback)
	{
		AbstractHorseEntity horse = (AbstractHorseEntity) (Object) this;

		if (id == 8)
		{
			EnderHorseCapability.ifPresent(horse, (enderHorse) ->
			{
				if (!enderHorse.isEnderChested())
					enderHorse.setEnderChested(true);
			});

			callback.cancel();
		}
		else if (id == 9)
		{
			EnderHorseCapability.ifPresent(horse, (enderHorse) ->
			{
				enderHorse.setEnderChested(false);
			});

			callback.cancel();
		}
	}
}
