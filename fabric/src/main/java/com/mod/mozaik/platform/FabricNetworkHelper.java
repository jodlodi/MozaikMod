package com.mod.mozaik.platform;

import com.mod.mozaik.networking.clientbound.IClientboundMessage;
import com.mod.mozaik.networking.serverbound.IServerboundMessage;
import com.mod.mozaik.platform.services.INetworkHelper;
import com.mod.mozaik.util.FabricClientPacketDistributor;
import com.mod.mozaik.util.FabricPacketDistributor;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;

public class FabricNetworkHelper implements INetworkHelper {

	@Override
	public void sendToServer(IServerboundMessage payload, IServerboundMessage... payloads) {
		FabricClientPacketDistributor.sendToServer(payload, payloads);
	}

	@Override
	public void sendToClient(ServerPlayer player, IClientboundMessage payload, IClientboundMessage... payloads) {
		FabricPacketDistributor.sendToPlayer(player, payload, payloads);
	}

	@Override
	public void sendToPlayersTrackingChunk(ServerLevel level, ChunkPos chunkPos, IClientboundMessage payload, IClientboundMessage... payloads) {
		FabricPacketDistributor.sendToPlayersTrackingChunk(level, chunkPos, payload, payloads);
	}

	public static void onServerMessage(IServerboundMessage message, ServerPlayNetworking.Context ctx) {
		if (ctx.player() instanceof ServerPlayer serverPlayer) message.executeServerbound(serverPlayer);
	}

	public static void onClientMessage(IClientboundMessage message, ClientPlayNetworking.Context ctx) {
		message.executeClientbound(ctx.player());
	}
}
