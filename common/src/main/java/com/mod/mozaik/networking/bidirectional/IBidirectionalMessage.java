package com.mod.mozaik.networking.bidirectional;

import com.mod.mozaik.networking.clientbound.IClientboundMessage;
import com.mod.mozaik.networking.serverbound.IServerboundMessage;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.jspecify.annotations.NullMarked;

import javax.annotation.ParametersAreNonnullByDefault;

@NullMarked
@ParametersAreNonnullByDefault
public interface IBidirectionalMessage extends IClientboundMessage, IServerboundMessage {

	void encode(FriendlyByteBuf buf);

	void executeClientbound(Player player);

	void executeServerbound(ServerPlayer player);
}
