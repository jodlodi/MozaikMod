package com.mod.mozaik.util;

import java.util.Objects;

import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;

public final class FabricPacketDistributor {
    private FabricPacketDistributor() {}

    public static void sendToPlayer(ServerPlayer player, CustomPacketPayload payload, CustomPacketPayload... payloads) {
        send(player, payload, payloads);
    }

    public static void sendToPlayersTrackingChunk(ServerLevel level, ChunkPos chunkPos, CustomPacketPayload payload, CustomPacketPayload... payloads) {
        for (ServerPlayer player : PlayerLookup.tracking(level, chunkPos)) {
            send(player, payload, payloads);
        }
    }

    private static void send(ServerPlayer player, CustomPacketPayload payload, CustomPacketPayload... payloads ) {
        Objects.requireNonNull(payload, "Cannot send null payload");
        ServerPlayNetworking.send(player, payload);
        for (CustomPacketPayload otherPayload : payloads) {
            Objects.requireNonNull(otherPayload, "Cannot send null payload");
            ServerPlayNetworking.send(player, otherPayload);
        }
    }
}
