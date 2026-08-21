package com.mod.mozaik.mixin;

import com.mod.mozaik.blocks.entities.MortarBlockEntity;
import com.mod.mozaik.networking.bidirectional.UpdateGlueBidirectional;
import com.mod.mozaik.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(ChunkHolder.class)
public class ChunkHolderMixin {
	@Inject(method = "broadcastBlockEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/entity/BlockEntity;getUpdatePacket()Lnet/minecraft/network/protocol/Packet;"), locals = LocalCapture.CAPTURE_FAILSOFT, cancellable = true)
	private static void onBroadcastBlockEntity(List<ServerPlayer> players, Level level, BlockPos blockPos, CallbackInfo ci, BlockEntity blockEntity) {
		if (blockEntity instanceof MortarBlockEntity mortarBlockEntity) {
			ci.cancel();
			players.forEach(serverPlayer -> Services.NETWORK.sendToClient(serverPlayer, new UpdateGlueBidirectional(mortarBlockEntity.getPolyominos(), blockPos)));
		}
	}
}
