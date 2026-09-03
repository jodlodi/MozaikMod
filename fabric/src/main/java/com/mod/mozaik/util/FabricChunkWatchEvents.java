package com.mod.mozaik.util;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.chunk.LevelChunk;

public final class FabricChunkWatchEvents {
    public static final Event<Sent> SENT = EventFactory.createArrayBacked(Sent.class, callbacks -> (entity, chunk, level) -> {
        for (Sent callback : callbacks) {
            callback.fireChunkSent(entity, chunk, level);
        }
    });

    @FunctionalInterface
    public interface Sent {
        void fireChunkSent(ServerPlayer entity, LevelChunk chunk, ServerLevel level);
    }
}