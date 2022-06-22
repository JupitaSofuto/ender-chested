package com.legacy.ender_chest_horses.network;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import com.legacy.ender_chest_horses.EnderChestedMod;
import com.legacy.ender_chest_horses.network.s_to_c.HorseStatusPacket;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler
{
	private static final String PROTOCOL_VERSION = "1";
	private static int index = 0;
	public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(EnderChestedMod.locate("main"), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

	public static void register()
	{
		// Client -> Server
		register(HorseStatusPacket.class, HorseStatusPacket::encoder, HorseStatusPacket::decoder, HorseStatusPacket::handler);
	}

	private static <MSG> void register(Class<MSG> packet, BiConsumer<MSG, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, MSG> decoder, BiConsumer<MSG, Supplier<NetworkEvent.Context>> messageConsumer)
	{
		INSTANCE.registerMessage(index, packet, encoder, decoder, messageConsumer);
		index++;
	}

	/**
	 * Server -> Client
	 * 
	 * @param packet
	 * @param serverPlayer
	 */
	public static void sendToClient(Object packet, ServerPlayer serverPlayer)
	{
		if (!(serverPlayer instanceof FakePlayer) && serverPlayer.connection != null)
			INSTANCE.sendTo(packet, serverPlayer.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
	}

	/**
	 * Server -> Clients in same world
	 * 
	 * @param packet
	 * @param world
	 */
	public static void sendToAllClients(Object packet, Level world)
	{
		world.players().forEach(player -> sendToClient(packet, (ServerPlayer) player));
	}

	/**
	 * Client -> Server
	 * 
	 * @param packet
	 */
	public static void sendToServer(Object packet)
	{
		INSTANCE.sendToServer(packet);
	}
}
