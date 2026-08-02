package com.mod.mozaik.networking.serverbound;

import com.mod.mozaik.Constants;
import com.mod.mozaik.Voxel;
import com.mod.mozaik.menus.MortarMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jspecify.annotations.NonNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public final class UpdateGlueServerbound implements IServerboundMessage {
	public static final Type<UpdateGlueServerbound> TYPE = new Type<>(Constants.prefix("update_glue"));

	public static final StreamCodec<RegistryFriendlyByteBuf, UpdateGlueServerbound> STREAM_CODEC = CustomPacketPayload.codec(UpdateGlueServerbound::encode, UpdateGlueServerbound::decode);

	private final Voxel[][] matrix;
	private final BlockPos pos;

	public UpdateGlueServerbound(Voxel[][] matrix, BlockPos pos) {
		this.matrix = matrix;
		this.pos = pos;
	}

	public static UpdateGlueServerbound decode(FriendlyByteBuf buf) {
		Voxel[][] matrix = new Voxel[16][16];

		for (int x = 0; x < 16; x++) {
			for (int y = 0; y < 16; y++) {
			}
		}

		return new UpdateGlueServerbound(matrix, BlockPos.of(buf.readLong()));
	}

	@Override
	public void encode(FriendlyByteBuf buf) {
		for (int x = 0; x < 16; x++) {
			for (int y = 0; y < 16; y++) {
				Voxel voxel = this.matrix[x][y];
				if (voxel == null) buf.writeInt(-1);
			}
		}
		buf.writeLong(this.pos.asLong());
	}

	@Override
	public void executeServerbound(ServerPlayer player) {
		AbstractContainerMenu var3 = player.containerMenu;
		if (var3 instanceof MortarMenu menu) {
			if (!player.containerMenu.stillValid(player)) {
				Constants.LOG.debug("Player {} interacted with invalid menu {}", player, player.containerMenu);
				return;
			}
		}
	}

	@Override
	public @NonNull Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
