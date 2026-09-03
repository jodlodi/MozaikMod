package com.mod.mozaik;

import com.mod.mozaik.blocks.entities.MortarBlockEntity;
import com.mod.mozaik.networking.bidirectional.AddPolyominoBidirectional;
import com.mod.mozaik.networking.bidirectional.RemovePolyominoBidirectional;
import com.mod.mozaik.networking.bidirectional.SignedMozaikBidirectional;
import com.mod.mozaik.networking.bidirectional.UpdateMozaikBidirectional;
import com.mod.mozaik.networking.clientbound.OpenGlueMenuClientbound;
import com.mod.mozaik.networking.serverbound.SelectShardBagItemPacket;
import com.mod.mozaik.platform.FabricNetworkHelper;
import com.mod.mozaik.platform.Services;
import com.mod.mozaik.util.FabricChunkWatchEvents;
import com.mod.mozaik.util.FabricServerLifecycleHooks;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class MozaikMod implements ModInitializer {
    
    @Override
    public void onInitialize() {
        FabricServerLifecycleHooks.init();

        CommonClass.init(true);

        registerCommonNetworking();
        registerEvents();
    }

    private static void registerCommonNetworking() {
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

    private static void registerEvents() {
        FabricChunkWatchEvents.SENT.register((entity, chunk, _) -> {
            chunk.getBlockEntities().forEach((pos, blockEntity) -> {
                if (blockEntity instanceof MortarBlockEntity mortarBlockEntity) {
                    Services.NETWORK.sendToClient(entity, new UpdateMozaikBidirectional(mortarBlockEntity.getPolyomino(), pos));
                }
            });
        });
    }
}