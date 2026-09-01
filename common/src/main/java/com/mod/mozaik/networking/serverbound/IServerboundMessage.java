package com.mod.mozaik.networking.serverbound;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.MethodsReturnNonnullByDefault;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public interface IServerboundMessage extends CustomPacketPayload {

	void encode(FriendlyByteBuf buf);

	void executeServerbound(ServerPlayer player);
}
