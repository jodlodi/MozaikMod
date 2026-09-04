package com.mod.mozaik.mixin;

import com.mod.mozaik.util.FabricChunkWatchEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.network.PlayerChunkSender;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerChunkSender.class)
public class PlayerChunkSenderMixin {

    @Inject(
        method = "sendChunk(Lnet/minecraft/server/network/ServerGamePacketListenerImpl;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/level/chunk/LevelChunk;)V",
        at = @At("TAIL")
    )
    private static void mozaik$fireChunkSent(
        ServerGamePacketListenerImpl connection,
        ServerLevel level,
        LevelChunk chunk,
        CallbackInfo ci
    ) {
        FabricChunkWatchEvents.SENT.invoker().fireChunkSent(connection.player, chunk, level);
    }
}