package com.mod.mozaik.networking.bidirectional;

import com.mod.mozaik.networking.clientbound.IClientboundMessage;
import com.mod.mozaik.networking.serverbound.IServerboundMessage;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface IBidirectionalMessage extends IClientboundMessage, IServerboundMessage {

	void encode(FriendlyByteBuf buf);

	void executeClientbound(LocalPlayer player);

	void executeServerbound(ServerPlayer player);
}
