package com.mod.mozaik.networking.bidirectional;

import com.mod.mozaik.Constants;
import com.mod.mozaik.blocks.entities.MortarBlockEntity;
import com.mod.mozaik.menus.MortarMenu;
import com.mod.mozaik.platform.Services;
import com.mod.mozaik.polyomino.Polyomino;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import org.jspecify.annotations.NullMarked;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

@NullMarked
@ParametersAreNonnullByDefault
public final class RemovePolyominoBidirectional implements IBidirectionalMessage {
	public static final Type<RemovePolyominoBidirectional> TYPE = new Type<>(Constants.prefix("remove_polyomino"));

	public static final Codec<RemovePolyominoBidirectional> CODEC = RecordCodecBuilder.create((recordCodecBuilder) -> recordCodecBuilder.group(
			UUIDUtil.CODEC.fieldOf("polyomino").forGetter(message -> message.polyomino),
			BlockPos.CODEC.fieldOf("block_pos").forGetter(message -> message.pos),
			Codec.INT.fieldOf("player").forGetter(message -> message.player)
	).apply(recordCodecBuilder, RemovePolyominoBidirectional::new));

	public static final StreamCodec<RegistryFriendlyByteBuf, RemovePolyominoBidirectional> STREAM_CODEC = CustomPacketPayload.codec(RemovePolyominoBidirectional::encode, RemovePolyominoBidirectional::decode);

	private final UUID polyomino;
	private final BlockPos pos;
	private final int player;

	public RemovePolyominoBidirectional(UUID polyomino, BlockPos pos, int player) {
		this.polyomino = polyomino;
		this.pos = pos;
		this.player = player;
	}

	public static RemovePolyominoBidirectional decode(FriendlyByteBuf buf) {
		return buf.readLenientJsonWithCodec(CODEC);
	}

	@Override
	public void encode(FriendlyByteBuf buf) {
		buf.writeJsonWithCodec(CODEC, this);
	}

	@Override
	public void executeClientbound(Player player) {
		if (player.level().getBlockEntity(this.pos) instanceof MortarBlockEntity blockEntity) {
			for (Polyomino.PlacedPolyomino polyomino : blockEntity.getPolyomino()) {
				if (polyomino.polyomino().uuid().equals(this.polyomino)) {
					new MortarMenu.ShardSource(player.getInventory()).giveItem(polyomino.polyomino().material());
					blockEntity.getPolyomino().remove(polyomino);
					return;
				}
			}
		}
	}

	@Override
	public void executeServerbound(ServerPlayer player) {
		AtomicBoolean atomicBoolean = new AtomicBoolean(false);
		if (player.level().getBlockEntity(this.pos) instanceof MortarBlockEntity blockEntity) {
			blockEntity.getPolyomino().removeIf(placedPolyomino -> {
				if (placedPolyomino.polyomino().uuid().equals(this.polyomino)) {
					new MortarMenu.ShardSource(player.getInventory()).giveItem(placedPolyomino.polyomino().material());
					atomicBoolean.set(true);
					return true;
				}
				return false;
			});
			if (atomicBoolean.get()) {
				Services.NETWORK.sendToPlayersTrackingChunk(player.level(), ChunkPos.containing(this.pos), new UpdateMozaikBidirectional(blockEntity.getPolyomino(), this.pos));
			}
		}
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
