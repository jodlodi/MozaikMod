package com.mod.mozaik.platform;

import com.mod.mozaik.Constants;
import com.mod.mozaik.networking.bidirectional.AddPolyominoBidirectional;
import com.mod.mozaik.networking.bidirectional.RemovePolyominoBidirectional;
import com.mod.mozaik.networking.bidirectional.SignedMozaikBidirectional;
import com.mod.mozaik.networking.bidirectional.UpdateMozaikBidirectional;
import com.mod.mozaik.networking.clientbound.IClientboundMessage;
import com.mod.mozaik.networking.clientbound.OpenGlueMenuClientbound;
import com.mod.mozaik.networking.serverbound.IServerboundMessage;
import com.mod.mozaik.networking.serverbound.SelectShardBagItemPacket;
import com.mod.mozaik.platform.services.INetworkHelper;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@EventBusSubscriber(modid = Constants.MOD_ID)
public class NeoForgeNetworkHelper implements INetworkHelper {
	private static final String PROTOCOL_VERSION = "1.0.0";

	@SubscribeEvent
	public static void onRegEvent(RegisterPayloadHandlersEvent event) {
		PayloadRegistrar registrar = event.registrar(Constants.MOD_ID).versioned(PROTOCOL_VERSION).optional();
		registrar.playBidirectional(UpdateMozaikBidirectional.TYPE, UpdateMozaikBidirectional.STREAM_CODEC, NeoForgeNetworkHelper::onServerMessage, NeoForgeNetworkHelper::onClientMessage);
		registrar.playBidirectional(RemovePolyominoBidirectional.TYPE, RemovePolyominoBidirectional.STREAM_CODEC, NeoForgeNetworkHelper::onServerMessage, NeoForgeNetworkHelper::onClientMessage);
		registrar.playBidirectional(SignedMozaikBidirectional.TYPE, SignedMozaikBidirectional.STREAM_CODEC, NeoForgeNetworkHelper::onServerMessage, NeoForgeNetworkHelper::onClientMessage);
		registrar.playBidirectional(AddPolyominoBidirectional.TYPE, AddPolyominoBidirectional.STREAM_CODEC, NeoForgeNetworkHelper::onServerMessage, NeoForgeNetworkHelper::onClientMessage);
		registrar.playToClient(OpenGlueMenuClientbound.TYPE, OpenGlueMenuClientbound.STREAM_CODEC, NeoForgeNetworkHelper::onClientMessage);
		registrar.playToServer(SelectShardBagItemPacket.TYPE, SelectShardBagItemPacket.STREAM_CODEC, NeoForgeNetworkHelper::onServerMessage);
	}

	@Override
	public void sendToServer(IServerboundMessage payload, IServerboundMessage... payloads) {
		ClientPacketDistributor.sendToServer(payload, payloads);
	}

	@Override
	public void sendToClient(ServerPlayer player, IClientboundMessage payload, IClientboundMessage... payloads) {
		PacketDistributor.sendToPlayer(player, payload, payloads);
	}

	@Override
	public void sendToPlayersTrackingChunk(ServerLevel level, ChunkPos chunkPos, IClientboundMessage payload, IClientboundMessage... payloads) {
		PacketDistributor.sendToPlayersTrackingChunk(level, chunkPos, payload, payloads);
	}

	public static void onServerMessage(IServerboundMessage message, IPayloadContext ctx) {
		ctx.enqueueWork(() -> {
			if (ctx.player() instanceof ServerPlayer serverPlayer) message.executeServerbound(serverPlayer);
		});
	}

	public static void onClientMessage(IClientboundMessage message, IPayloadContext ctx) {
		ctx.enqueueWork(() -> message.executeClientbound(ctx.player()));
	}
}
