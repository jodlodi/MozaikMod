package com.mod.mozaik.networking.clientbound;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface IClientboundMessage extends CustomPacketPayload {

	void encode(FriendlyByteBuf buf);

	void executeClientbound(Player player);
}
