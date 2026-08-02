package com.mod.mozaik.networking.clientbound;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public interface IClientboundMessage extends CustomPacketPayload {

	void encode(FriendlyByteBuf buf);

	void executeClientbound(LocalPlayer player);
}
