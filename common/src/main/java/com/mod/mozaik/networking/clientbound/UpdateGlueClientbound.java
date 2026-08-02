package com.mod.mozaik.networking.clientbound;

import com.mod.mozaik.Constants;
import com.mod.mozaik.Voxel;
import com.mod.mozaik.menus.MortarMenu;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jspecify.annotations.NonNull;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public final class UpdateGlueClientbound implements IClientboundMessage {
	public static final Type<UpdateGlueClientbound> TYPE = new Type<>(Constants.prefix("update_glue"));

	public static final StreamCodec<RegistryFriendlyByteBuf, UpdateGlueClientbound> STREAM_CODEC = CustomPacketPayload.codec(UpdateGlueClientbound::encode, UpdateGlueClientbound::decode);

	private final Voxel[][] matrix;
	private final BlockPos pos;

	public UpdateGlueClientbound(Voxel[][] matrix, BlockPos pos) {
		this.matrix = matrix;
		this.pos = pos;
	}

	public static UpdateGlueClientbound decode(FriendlyByteBuf buf) {
		Voxel[][] matrix = new Voxel[16][16];
		return new UpdateGlueClientbound(matrix, BlockPos.of(buf.readLong()));
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
	public void executeClientbound(LocalPlayer player) {
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
