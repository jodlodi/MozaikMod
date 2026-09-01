package com.mod.mozaik.networking.clientbound;

import com.mod.mozaik.Constants;
import com.mod.mozaik.blocks.MortarBlock;
import com.mod.mozaik.blocks.entities.MortarBlockEntity;
import com.mod.mozaik.client.screens.MortarScreen;
import com.mod.mozaik.menus.MortarMenu;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Rotation;
import org.jspecify.annotations.NullMarked;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

@NullMarked
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
	public void executeClientbound(Player player) {
		Handler.handle(this, player);
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}

	public static class Handler {
		public static void handle(OpenGlueMenuClientbound packet, Player player) {
			MortarBlockEntity blockEntity = (MortarBlockEntity) player.level().getBlockEntity(packet.pos);
			Direction facing = Objects.requireNonNull(blockEntity).getBlockState().getValue(MortarBlock.FACING_ROTATED).getDirection();

			Rotation fromYRot = switch (facing) {
				case DOWN -> switch (Direction.fromYRot(player.getVisualRotationYInDegrees())) {
					case Direction.EAST -> Rotation.CLOCKWISE_90;
					case Direction.SOUTH -> Rotation.CLOCKWISE_180;
					case Direction.WEST -> Rotation.COUNTERCLOCKWISE_90;
					default -> Rotation.NONE;
				};
				case UP -> switch (Direction.fromYRot(player.getVisualRotationYInDegrees())) {
					case Direction.EAST -> Rotation.COUNTERCLOCKWISE_90;
					case Direction.SOUTH -> Rotation.CLOCKWISE_180;
					case Direction.WEST -> Rotation.CLOCKWISE_90;
					default -> Rotation.NONE;
				};
				default -> Rotation.NONE;
			};

			MortarMenu menu = new MortarMenu(packet.containerId, player.getInventory(), blockEntity, fromYRot);
			MortarScreen screen = new MortarScreen(menu, player.getInventory(), Component.literal("Glue"));
			player.containerMenu = screen.getMenu();
			Minecraft.getInstance().setScreen(screen);
		}
	}
}
