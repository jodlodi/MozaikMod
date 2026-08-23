package com.mod.mozaik.networking.bidirectional;

import com.mod.mozaik.Constants;
import com.mod.mozaik.blocks.entities.MortarBlockEntity;
import com.mod.mozaik.polyomino.Polyomino;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import org.jspecify.annotations.NonNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@ParametersAreNonnullByDefault
public final class UpdateMozaikBidirectional implements IBidirectionalMessage {
	public static final Type<UpdateMozaikBidirectional> TYPE = new Type<>(Constants.prefix("update_mozaik"));

	public static final Codec<UpdateMozaikBidirectional> CODEC = RecordCodecBuilder.create((recordCodecBuilder) -> recordCodecBuilder.group(
			Polyomino.PlacedPolyomino.CODEC.listOf().fieldOf("polyomino").forGetter(message -> message.polyomino),
			BlockPos.CODEC.fieldOf("block_pos").forGetter(message -> message.pos)
	).apply(recordCodecBuilder, UpdateMozaikBidirectional::new));

	public static final StreamCodec<RegistryFriendlyByteBuf, UpdateMozaikBidirectional> STREAM_CODEC = CustomPacketPayload.codec(UpdateMozaikBidirectional::encode, UpdateMozaikBidirectional::decode);

	private final List<Polyomino.PlacedPolyomino> polyomino;
	private final BlockPos pos;

	public UpdateMozaikBidirectional(List<Polyomino.PlacedPolyomino> polyomino, BlockPos pos) {
		this.polyomino = new ArrayList<>();
		this.polyomino.addAll(polyomino);
		this.pos = pos;
	}

	public static UpdateMozaikBidirectional decode(FriendlyByteBuf buf) {
		return buf.readLenientJsonWithCodec(CODEC);
	}

	@Override
	public void encode(FriendlyByteBuf buf) {
		buf.writeJsonWithCodec(CODEC, this);
	}

	@Override
	public void executeClientbound(LocalPlayer player) {
		if (player.level().getBlockEntity(this.pos) instanceof MortarBlockEntity blockEntity) {
			blockEntity.setPolyomino(this.polyomino);
		}
	}

	@Override
	public void executeServerbound(ServerPlayer player) {
		if (player.level().getBlockEntity(this.pos) instanceof MortarBlockEntity blockEntity) {
			blockEntity.setPolyomino(this.polyomino);
			blockEntity.setChanged();
		}
	}

	@Override
	public @NonNull Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
