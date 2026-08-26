package com.mod.mozaik.networking.serverbound;

import com.mod.mozaik.Constants;
import com.mod.mozaik.items.ShardBagItem;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public final class SelectShardBagItemPacket implements IServerboundMessage {
	public static final Type<SelectShardBagItemPacket> TYPE = new Type<>(Constants.prefix("select_slot"));

	public static final Codec<SelectShardBagItemPacket> CODEC = RecordCodecBuilder.create((recordCodecBuilder) -> recordCodecBuilder.group(
			Codec.INT.fieldOf("slot_id").forGetter(message -> message.slotId),
			Codec.INT.fieldOf("selected_item_index").forGetter(message -> message.selectedItemIndex)
	).apply(recordCodecBuilder, SelectShardBagItemPacket::new));

	public static final StreamCodec<RegistryFriendlyByteBuf, SelectShardBagItemPacket> STREAM_CODEC = CustomPacketPayload.codec(SelectShardBagItemPacket::encode, SelectShardBagItemPacket::decode);

	private final int slotId;
	private final int selectedItemIndex;

	public SelectShardBagItemPacket(int slotId, int selectedItemIndex) {
		this.slotId = slotId;
		this.selectedItemIndex = selectedItemIndex;
	}

	public static SelectShardBagItemPacket decode(FriendlyByteBuf buf) {
		return buf.readLenientJsonWithCodec(CODEC);
	}

	@Override
	public void encode(FriendlyByteBuf buf) {
		buf.writeJsonWithCodec(CODEC, this);
	}

	@Override
	public void executeServerbound(ServerPlayer player) {
		if (this.slotId >= 0 && this.slotId < player.containerMenu.slots.size()) {
			ItemStack itemStack = player.containerMenu.slots.get(this.slotId).getItem();
			ShardBagItem.toggleSelectedItem(itemStack, this.selectedItemIndex);
		}
	}

	@Override
	public @NonNull Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
