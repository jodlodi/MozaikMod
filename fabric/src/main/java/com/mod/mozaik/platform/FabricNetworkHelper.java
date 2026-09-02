package com.mod.mozaik.platform;

import com.mod.mozaik.networking.bidirectional.AddPolyominoBidirectional;
import com.mod.mozaik.networking.bidirectional.RemovePolyominoBidirectional;
import com.mod.mozaik.networking.bidirectional.SignedMozaikBidirectional;
import com.mod.mozaik.networking.bidirectional.UpdateMozaikBidirectional;
import com.mod.mozaik.networking.clientbound.IClientboundMessage;
import com.mod.mozaik.networking.clientbound.OpenGlueMenuClientbound;
import com.mod.mozaik.networking.serverbound.IServerboundMessage;
import com.mod.mozaik.networking.serverbound.SelectShardBagItemPacket;
import com.mod.mozaik.platform.services.INetworkHelper;
import com.mod.mozaik.util.FabricClientPacketDistributor;
import com.mod.mozaik.util.FabricPacketDistributor;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;

public class FabricNetworkHelper implements INetworkHelper {
	public static void commonPacketRegistration() {
		PayloadTypeRegistry.serverboundPlay().register(UpdateMozaikBidirectional.TYPE, UpdateMozaikBidirectional.STREAM_CODEC);
		PayloadTypeRegistry.clientboundPlay().register(UpdateMozaikBidirectional.TYPE, UpdateMozaikBidirectional.STREAM_CODEC);
		PayloadTypeRegistry.serverboundPlay().register(RemovePolyominoBidirectional.TYPE, RemovePolyominoBidirectional.STREAM_CODEC);
		PayloadTypeRegistry.clientboundPlay().register(RemovePolyominoBidirectional.TYPE, RemovePolyominoBidirectional.STREAM_CODEC);
		PayloadTypeRegistry.serverboundPlay().register(SignedMozaikBidirectional.TYPE, SignedMozaikBidirectional.STREAM_CODEC);
		PayloadTypeRegistry.clientboundPlay().register(SignedMozaikBidirectional.TYPE, SignedMozaikBidirectional.STREAM_CODEC);
		PayloadTypeRegistry.serverboundPlay().register(AddPolyominoBidirectional.TYPE, AddPolyominoBidirectional.STREAM_CODEC);
		PayloadTypeRegistry.clientboundPlay().register(AddPolyominoBidirectional.TYPE, AddPolyominoBidirectional.STREAM_CODEC);

		PayloadTypeRegistry.clientboundPlay().register(OpenGlueMenuClientbound.TYPE, OpenGlueMenuClientbound.STREAM_CODEC);
		PayloadTypeRegistry.serverboundPlay().register(SelectShardBagItemPacket.TYPE, SelectShardBagItemPacket.STREAM_CODEC);

		ServerPlayNetworking.registerGlobalReceiver(UpdateMozaikBidirectional.TYPE, FabricNetworkHelper::onServerMessage);
		ServerPlayNetworking.registerGlobalReceiver(RemovePolyominoBidirectional.TYPE, FabricNetworkHelper::onServerMessage);
		ServerPlayNetworking.registerGlobalReceiver(SignedMozaikBidirectional.TYPE, FabricNetworkHelper::onServerMessage);
		ServerPlayNetworking.registerGlobalReceiver(AddPolyominoBidirectional.TYPE, FabricNetworkHelper::onServerMessage);
		ServerPlayNetworking.registerGlobalReceiver(SelectShardBagItemPacket.TYPE, FabricNetworkHelper::onServerMessage);
	}

	public static void clientPacketRegistration() {
		ClientPlayNetworking.registerGlobalReceiver(UpdateMozaikBidirectional.TYPE, FabricNetworkHelper::onClientMessage);
		ClientPlayNetworking.registerGlobalReceiver(RemovePolyominoBidirectional.TYPE, FabricNetworkHelper::onClientMessage);
		ClientPlayNetworking.registerGlobalReceiver(SignedMozaikBidirectional.TYPE, FabricNetworkHelper::onClientMessage);
		ClientPlayNetworking.registerGlobalReceiver(AddPolyominoBidirectional.TYPE, FabricNetworkHelper::onClientMessage);
		ClientPlayNetworking.registerGlobalReceiver(OpenGlueMenuClientbound.TYPE, FabricNetworkHelper::onClientMessage);
	}

	@Override
	public void sendToServer(IServerboundMessage payload, IServerboundMessage... payloads) {
		FabricClientPacketDistributor.sendToServer(payload, payloads);
	}

	@Override
	public void sendToClient(ServerPlayer player, IClientboundMessage payload, IClientboundMessage... payloads) {
		FabricPacketDistributor.sendToPlayer(player, payload, payloads);
	}

	@Override
	public void sendToPlayersTrackingChunk(ServerLevel level, ChunkPos chunkPos, IClientboundMessage payload, IClientboundMessage... payloads) {
		FabricPacketDistributor.sendToPlayersTrackingChunk(level, chunkPos, payload, payloads);
	}

	public static void onServerMessage(IServerboundMessage message, ServerPlayNetworking.Context ctx) {
		if (ctx.player() instanceof ServerPlayer serverPlayer) message.executeServerbound(serverPlayer);
	}

	public static void onClientMessage(IClientboundMessage message, ClientPlayNetworking.Context ctx) {
		message.executeClientbound(ctx.player());
	}
}
