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
public final class UpdateGlueBidirectional implements IBidirectionalMessage {
	public static final Type<UpdateGlueBidirectional> TYPE = new Type<>(Constants.prefix("update_glue"));

	public static final Codec<UpdateGlueBidirectional> CODEC = RecordCodecBuilder.create((recordCodecBuilder) -> recordCodecBuilder.group(
			Polyomino.PlacedPolyomino.CODEC.listOf().fieldOf("polyominos").forGetter(message -> message.polyominos),
			BlockPos.CODEC.fieldOf("block_pos").forGetter(message -> message.pos)
	).apply(recordCodecBuilder, UpdateGlueBidirectional::new));

	public static final StreamCodec<RegistryFriendlyByteBuf, UpdateGlueBidirectional> STREAM_CODEC = CustomPacketPayload.codec(UpdateGlueBidirectional::encode, UpdateGlueBidirectional::decode);

	private final List<Polyomino.PlacedPolyomino> polyominos;
	private final BlockPos pos;

	public UpdateGlueBidirectional(List<Polyomino.PlacedPolyomino> polyominos, BlockPos pos) {
		this.polyominos = new ArrayList<>();
		this.polyominos.addAll(polyominos);
		this.pos = pos;
	}

	public static UpdateGlueBidirectional decode(FriendlyByteBuf buf) {
		return buf.readLenientJsonWithCodec(CODEC);
	}

	@Override
	public void encode(FriendlyByteBuf buf) {
		buf.writeJsonWithCodec(CODEC, this);
	}

	@Override
	public void executeClientbound(LocalPlayer player) {
		if (player.level().getBlockEntity(this.pos) instanceof MortarBlockEntity blockEntity) {
			blockEntity.setPolyominos(this.polyominos);
		}
	}

	@Override
	public void executeServerbound(ServerPlayer player) {
		if (player.level().getBlockEntity(this.pos) instanceof MortarBlockEntity blockEntity) {
			blockEntity.setPolyominos(this.polyominos);
			blockEntity.setChanged();
		}
	}

	@Override
	public @NonNull Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
