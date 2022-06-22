package com.legacy.ender_chest_horses.network.s_to_c;

import java.util.function.Supplier;

import com.legacy.ender_chest_horses.capabillity.EnderHorseCapability;
import com.legacy.ender_chest_horses.capabillity.IEnderHorse;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class HorseStatusPacket
{
	private final int entityId;
	private final boolean chested;

	public HorseStatusPacket(int entityIdIn, boolean chest)
	{
		this.entityId = entityIdIn;
		this.chested = chest;
	}

	public static void encoder(HorseStatusPacket packet, FriendlyByteBuf buff)
	{
		buff.writeInt(packet.entityId);
		buff.writeBoolean(packet.chested);
	}

	public static HorseStatusPacket decoder(FriendlyByteBuf buff)
	{
		return new HorseStatusPacket(buff.readInt(), buff.readBoolean());
	}

	public static void handler(HorseStatusPacket packet, Supplier<NetworkEvent.Context> context)
	{
		context.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> handlePacket(packet)));
		context.get().setPacketHandled(true);
	}

	@SuppressWarnings("resource")
	@OnlyIn(Dist.CLIENT)
	private static void handlePacket(HorseStatusPacket packet)
	{		
		IEnderHorse cap;
		if (net.minecraft.client.Minecraft.getInstance().level.getEntity(packet.entityId)instanceof AbstractHorse horse && (cap = EnderHorseCapability.get(horse)) != null)
			cap.setEnderChested(packet.chested);
	}
}
