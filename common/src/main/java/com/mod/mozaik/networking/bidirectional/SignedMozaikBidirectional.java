package com.mod.mozaik.networking.bidirectional;

import com.mod.mozaik.Constants;
import com.mod.mozaik.blocks.entities.MortarBlockEntity;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.MethodsReturnNonnullByDefault;
import javax.annotation.ParametersAreNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public final class SignedMozaikBidirectional implements IBidirectionalMessage {
	public static final Type<SignedMozaikBidirectional> TYPE = new Type<>(Constants.prefix("sign_mozaik"));

	public static final Codec<SignedMozaikBidirectional> CODEC = RecordCodecBuilder.create((recordCodecBuilder) -> recordCodecBuilder.group(
			Codec.STRING.optionalFieldOf("title").forGetter(message -> message.title),
			Codec.STRING.optionalFieldOf("by").forGetter(message -> message.by),
			BlockPos.CODEC.fieldOf("block_pos").forGetter(message -> message.pos)
	).apply(recordCodecBuilder, SignedMozaikBidirectional::new));

	public static final StreamCodec<RegistryFriendlyByteBuf, SignedMozaikBidirectional> STREAM_CODEC = CustomPacketPayload.codec(SignedMozaikBidirectional::encode, SignedMozaikBidirectional::decode);

	private final Optional<String> title;
	private final Optional<String> by;
	private final BlockPos pos;

	public SignedMozaikBidirectional(Optional<String> title, Optional<String> by, BlockPos pos) {
		this.title = title;
		this.by = by;
		this.pos = pos;
	}

	public static SignedMozaikBidirectional decode(FriendlyByteBuf buf) {
		return buf.readLenientJsonWithCodec(CODEC);
	}

	@Override
	public void encode(FriendlyByteBuf buf) {
		buf.writeJsonWithCodec(CODEC, this);
	}

	@Override
	public void executeClientbound(Player player) {
		if (player.level().getBlockEntity(this.pos) instanceof MortarBlockEntity blockEntity) {
			this.title.ifPresent(tit -> blockEntity.setCustomName(Component.literal(tit)));
			this.by.ifPresent(blockEntity::setAuthorName);
			blockEntity.setSigned(true);
		}
	}

	@Override
	public void executeServerbound(ServerPlayer player) {
		if (player.level().getBlockEntity(this.pos) instanceof MortarBlockEntity blockEntity) {
			this.title.ifPresent(tit -> blockEntity.setCustomName(Component.literal(tit)));
			this.by.ifPresent(blockEntity::setAuthorName);
			blockEntity.setSigned(true);
			blockEntity.setChanged();
		}
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
