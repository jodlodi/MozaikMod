package com.mod.mozaik.networking.bidirectional;

import com.mod.mozaik.Constants;
import com.mod.mozaik.blocks.entities.MortarBlockEntity;
import com.mod.mozaik.items.ShardItem;
import com.mod.mozaik.menus.MortarMenu;
import com.mod.mozaik.platform.Services;
import com.mod.mozaik.polyomino.Polyomino;
import com.mod.mozaik.polyomino.ShardMaterial;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import org.jspecify.annotations.NonNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public final class AddPolyominoBidirectional implements IBidirectionalMessage {
	public static final Type<AddPolyominoBidirectional> TYPE = new Type<>(Constants.prefix("add_polyomino"));

	public static final Codec<AddPolyominoBidirectional> CODEC = RecordCodecBuilder.create((recordCodecBuilder) -> recordCodecBuilder.group(
			Polyomino.PlacedPolyomino.CODEC.fieldOf("polyomino").forGetter(message -> message.polyomino),
			BlockPos.CODEC.fieldOf("block_pos").forGetter(message -> message.pos),
			Codec.INT.fieldOf("player").forGetter(message -> message.player)
	).apply(recordCodecBuilder, AddPolyominoBidirectional::new));

	public static final StreamCodec<RegistryFriendlyByteBuf, AddPolyominoBidirectional> STREAM_CODEC = CustomPacketPayload.codec(AddPolyominoBidirectional::encode, AddPolyominoBidirectional::decode);

	private final Polyomino.PlacedPolyomino polyomino;
	private final BlockPos pos;
	private final int player;

	public AddPolyominoBidirectional(Polyomino.PlacedPolyomino polyomino, BlockPos pos, int player) {
		this.polyomino = polyomino;
		this.pos = pos;
		this.player = player;
	}

	public static AddPolyominoBidirectional decode(FriendlyByteBuf buf) {
		return buf.readLenientJsonWithCodec(CODEC);
	}

	@Override
	public void encode(FriendlyByteBuf buf) {
		buf.writeJsonWithCodec(CODEC, this);
	}

	@Override
	public void executeClientbound(LocalPlayer player) {
		if (player.level().getBlockEntity(this.pos) instanceof MortarBlockEntity blockEntity) {
			for (Polyomino.PlacedPolyomino polyomino : blockEntity.getPolyomino()) {
				if (polyomino.polyomino().uuid().equals(this.polyomino)) {
					ResourceKey<ShardMaterial> material = polyomino.polyomino().material();

					MortarMenu.ShardCount count = new MortarMenu.ShardSource(player.getInventory()).get(material);

					for (ItemStack stack : count.stacks()) {
						if (stack.getCount() < stack.getMaxStackSize()) {
							stack.grow(1);
							blockEntity.getPolyomino().remove(polyomino);
							return;
						}
					}

					player.getInventory().add(new ItemStack(ShardItem.SHARDS.get(material)));
					blockEntity.getPolyomino().remove(polyomino);
					return;
				}
			}
		}
	}

	@Override
	public void executeServerbound(ServerPlayer player) {
		if (player.level().getBlockEntity(this.pos) instanceof MortarBlockEntity blockEntity) {
			ResourceKey<ShardMaterial> material = this.polyomino.polyomino().material();
			MortarMenu.ShardCount count = new MortarMenu.ShardSource(player.getInventory()).get(material);
			if (count.count() <= 0) return;

			int value = Integer.MAX_VALUE;
			ItemStack smallest = ItemStack.EMPTY;
			for (ItemStack stack : count.stacks()) {
				if (stack.getCount() < value) {
					value = stack.getCount();
					smallest = stack;
				}
			}

			smallest.shrink(1);
			blockEntity.getPolyomino().add(this.polyomino);
			Services.NETWORK.sendToPlayersTrackingChunk(player.level(), ChunkPos.containing(this.pos), new UpdateMozaikBidirectional(blockEntity.getPolyomino(), this.pos));
		}
	}

	@Override
	public @NonNull Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
