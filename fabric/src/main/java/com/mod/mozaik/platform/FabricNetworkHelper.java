package com.mod.mozaik.platform;

import com.mod.mozaik.networking.clientbound.IClientboundMessage;
import com.mod.mozaik.networking.serverbound.IServerboundMessage;
import com.mod.mozaik.platform.services.INetworkHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;

import java.util.ArrayList;
import java.util.List;

public class FabricNetworkHelper implements INetworkHelper {
	public static void initServerbound() {
		registerServerbound(OpenGlueMenuServerbound.TYPE);
	}

	private static <T extends IServerboundMessage> void registerServerbound(CustomPacketPayload.Type<T> type) {
		ServerPlayNetworking.registerGlobalReceiver(type, (message, context) -> message.executeServerbound(context.player()));
	}

	@Override
	public void sendToServer(IServerboundMessage payload, IServerboundMessage... payloads) {
		ClientPlayNetworking.send(payload);
		for (CustomPacketPayload packet : payloads) {
			ClientPlayNetworking.send(packet);
		}
	}

	@Override
	public void sendToClient(ServerPlayer player, IClientboundMessage payload, IClientboundMessage... payloads) {

	}

	@Override
	public void sendToPlayersTrackingChunk(ServerLevel level, ChunkPos chunkPos, IClientboundMessage payload, IClientboundMessage... payloads) {
		List<CustomPacketPayload> list = new ArrayList<>();
		list.add(payload);
		list.addAll(List.of(payloads));

		for (ServerPlayer player : level.getChunkSource().chunkMap.getPlayers(chunkPos, false)) {
			list.forEach(customPacketPayload -> ServerPlayNetworking.send(player, customPacketPayload));
		}
	}
}
