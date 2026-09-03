package com.mod.mozaik;

import com.mod.mozaik.client.model.MozaikModelLoadingPlugin;
import com.mod.mozaik.client.tooltips.ClientShardBagTooltip;
import com.mod.mozaik.client.tooltips.PolyominoTooltip;
import com.mod.mozaik.items.PolyominoItem;
import com.mod.mozaik.items.ShardBagItem;
import com.mod.mozaik.networking.bidirectional.AddPolyominoBidirectional;
import com.mod.mozaik.networking.bidirectional.RemovePolyominoBidirectional;
import com.mod.mozaik.networking.bidirectional.SignedMozaikBidirectional;
import com.mod.mozaik.networking.bidirectional.UpdateMozaikBidirectional;
import com.mod.mozaik.networking.clientbound.OpenGlueMenuClientbound;
import com.mod.mozaik.platform.FabricNetworkHelper;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.ClientTooltipComponentCallback;

public class MozaikClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ModelLoadingPlugin.register(new MozaikModelLoadingPlugin());
        registerClientNetworking();
        registerEvents();
    }

    private static void registerClientNetworking() {
        ClientPlayNetworking.registerGlobalReceiver(UpdateMozaikBidirectional.TYPE, FabricNetworkHelper::onClientMessage);
        ClientPlayNetworking.registerGlobalReceiver(RemovePolyominoBidirectional.TYPE, FabricNetworkHelper::onClientMessage);
        ClientPlayNetworking.registerGlobalReceiver(SignedMozaikBidirectional.TYPE, FabricNetworkHelper::onClientMessage);
        ClientPlayNetworking.registerGlobalReceiver(AddPolyominoBidirectional.TYPE, FabricNetworkHelper::onClientMessage);
        ClientPlayNetworking.registerGlobalReceiver(OpenGlueMenuClientbound.TYPE, FabricNetworkHelper::onClientMessage);
    }

    private static void registerEvents() {
        ClientTooltipComponentCallback.EVENT.register(component -> {
            if (component instanceof ShardBagItem.ShardBagTooltip tooltip) {
                return new ClientShardBagTooltip(tooltip);
            }

            if (component instanceof PolyominoItem.ShapeTooltip tooltip) {
                return new PolyominoTooltip(tooltip);
            }

            return null;
        });
    }
}