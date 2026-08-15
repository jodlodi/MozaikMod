package com.mod.mozaik.platform.services;

import com.mod.mozaik.networking.clientbound.IClientboundMessage;
import com.mod.mozaik.networking.serverbound.IServerboundMessage;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;

public interface INetworkHelper {
	void sendToServer(IServerboundMessage payload, IServerboundMessage... payloads);

	void sendToClient(ServerPlayer player, IClientboundMessage payload, IClientboundMessage... payloads);

	void sendToPlayersTrackingChunk(ServerLevel level, ChunkPos chunkPos, IClientboundMessage payload, IClientboundMessage... payloads);
}
