package com.mod.mozaik.networking.clientbound;

import com.mod.mozaik.Constants;
import com.mod.mozaik.blocks.entities.MortarBlockEntity;
import com.mod.mozaik.client.screens.MortarScreen;
import com.mod.mozaik.menus.MortarMenu;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Inventory;
import org.jspecify.annotations.NonNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

@ParametersAreNonnullByDefault
public final class OpenGlueMenuClientbound implements IClientboundMessage {
	public static final Type<OpenGlueMenuClientbound> TYPE = new Type<>(Constants.prefix("open_glue"));

	public static final Codec<OpenGlueMenuClientbound> CODEC = RecordCodecBuilder.create((recordCodecBuilder) -> recordCodecBuilder.group(
			BlockPos.CODEC.fieldOf("block_pos").forGetter(message -> message.pos),
			Codec.INT.fieldOf("container_id").forGetter(message -> message.containerId)
	).apply(recordCodecBuilder, OpenGlueMenuClientbound::new));

	public static final StreamCodec<RegistryFriendlyByteBuf, OpenGlueMenuClientbound> STREAM_CODEC = CustomPacketPayload.codec(OpenGlueMenuClientbound::encode, OpenGlueMenuClientbound::decode);

	private final BlockPos pos;
	private final int containerId;

	public OpenGlueMenuClientbound(BlockPos pos, int containerId) {
		this.pos = pos;
		this.containerId = containerId;
	}

	public static OpenGlueMenuClientbound decode(FriendlyByteBuf buf) {
		return buf.readLenientJsonWithCodec(CODEC);
	}

	@Override
	public void encode(FriendlyByteBuf buf) {
		buf.writeJsonWithCodec(CODEC, this);
	}

	@Override
	public void executeClientbound(LocalPlayer player) {
		Minecraft minecraft = Minecraft.getInstance();
		Inventory inventory = Objects.requireNonNull(minecraft.player).getInventory();
		MortarMenu menu = new MortarMenu(this.containerId, inventory, (MortarBlockEntity) minecraft.player.level().getBlockEntity(this.pos));
		MortarScreen screen = new MortarScreen(menu, inventory, Component.literal("Glu"));
		minecraft.player.containerMenu = screen.getMenu();
		minecraft.gui.setScreen(screen);
	}

	@Override
	public @NonNull Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
