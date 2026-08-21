package com.mod.mozaik.mixin;

import com.mod.mozaik.blocks.entities.MortarBlockEntity;
import com.mod.mozaik.networking.bidirectional.UpdateGlueBidirectional;
import com.mod.mozaik.platform.Services;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.network.PlayerChunkSender;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jspecify.annotations.NullMarked;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.ParametersAreNonnullByDefault;

@NullMarked
@Mixin(PlayerChunkSender.class)
public class PlayerChunkSenderMixin {
	@Inject(method = "sendChunk", at = @At(value = "TAIL"))
	private static void onSendChunk(ServerGamePacketListenerImpl connection, ServerLevel level, LevelChunk chunk, CallbackInfo ci) {
		chunk.getBlockEntities().forEach((pos, blockEntity) -> {
			if (blockEntity instanceof MortarBlockEntity entity) {
				Services.NETWORK.sendToClient(connection.player, new UpdateGlueBidirectional(entity.getPolyominos(), pos));
			}
		});
	}
}
