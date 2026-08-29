package com.mod.mozaik.networking.bidirectional;

import com.mod.mozaik.Constants;
import com.mod.mozaik.blocks.entities.MortarBlockEntity;
import com.mod.mozaik.items.ShardItem;
import com.mod.mozaik.polyomino.Polyomino;
import com.mod.mozaik.polyomino.ShardMaterial;
import com.mod.mozaik.polyomino.Tessera;
import com.mod.mozaik.reg.ModRegistries;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import org.jetbrains.annotations.UnknownNullability;
import org.jspecify.annotations.NonNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

@ParametersAreNonnullByDefault
public final class UpdateMozaikBidirectional implements IBidirectionalMessage {
	public static final List<ResourceKey<ShardMaterial>> SORTED_INSTANCE_KEY_SET = new ArrayList<>();
	public static final Type<UpdateMozaikBidirectional> TYPE = new Type<>(Constants.prefix("update_mozaik"));

	public static final Codec<Tessera> TESSERA_CODEC = RecordCodecBuilder.create((recordCodecBuilder) -> recordCodecBuilder.group(
			Codec.BYTE.fieldOf("s").forGetter(tessera -> (byte) tessera.shape().ordinal())
	).apply(recordCodecBuilder, Tessera::new));

	public static final Codec<Tessera.PlacedTessera> PLACED_TESSERA_CODEC = RecordCodecBuilder.create((recordCodecBuilder) -> recordCodecBuilder.group(
			TESSERA_CODEC.fieldOf("t").forGetter(Tessera.PlacedTessera::tessera),
			Codec.BYTE.fieldOf("x").forGetter(t -> (byte) t.x()),
			Codec.BYTE.fieldOf("y").forGetter(t -> (byte) t.y())
	).apply(recordCodecBuilder, Tessera.PlacedTessera::new));

	public static final Codec<Polyomino> POLYOMINO_CODEC = RecordCodecBuilder.create((recordCodecBuilder) -> recordCodecBuilder.group(
			PLACED_TESSERA_CODEC.listOf().fieldOf("t").forGetter(Polyomino::placedTessera),
			Codec.INT.fieldOf("m").forGetter(t -> updateListIfEmpty().indexOf(t.material())),
			UUIDUtil.LENIENT_CODEC.fieldOf("u").forGetter(Polyomino::uuid)
	).apply(recordCodecBuilder, (List<Tessera.PlacedTessera> placedTessera, Integer material, UUID uuid) -> new Polyomino(placedTessera, updateListIfEmpty().get(material), uuid)));

	public static final Codec<Polyomino.PlacedPolyomino> PLACED_POLYOMINO_CODEC = RecordCodecBuilder.create((recordCodecBuilder) -> recordCodecBuilder.group(
			POLYOMINO_CODEC.fieldOf("p").forGetter(Polyomino.PlacedPolyomino::polyomino),
			Codec.BYTE.fieldOf("x").forGetter(t -> (byte) t.x()),
			Codec.BYTE.fieldOf("y").forGetter(t -> (byte) t.y())
	).apply(recordCodecBuilder, Polyomino.PlacedPolyomino::new));

	public static final Codec<UpdateMozaikBidirectional> CODEC = RecordCodecBuilder.create((recordCodecBuilder) -> recordCodecBuilder.group(
			PLACED_POLYOMINO_CODEC.listOf().fieldOf("polyomino").forGetter(message -> message.polyomino),
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

	public static List<ResourceKey<ShardMaterial>> updateListIfEmpty() {
		if (SORTED_INSTANCE_KEY_SET.isEmpty()) {
			SORTED_INSTANCE_KEY_SET.addAll(ShardItem.SHARDS.keySet());
			SORTED_INSTANCE_KEY_SET.sort(Comparator.comparing(e -> e.identifier().toShortString()));
		}
		return SORTED_INSTANCE_KEY_SET;
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
