package com.mod.mozaik;

import com.mod.mozaik.client.model.MozaikModelLoadingPlugin;
import com.mod.mozaik.client.tooltips.ClientShardBagTooltip;
import com.mod.mozaik.client.tooltips.PolyominoTooltip;
import com.mod.mozaik.items.PolyominoItem;
import com.mod.mozaik.items.ShardBagItem;
import com.mod.mozaik.platform.FabricNetworkHelper;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.rendering.v1.ClientTooltipComponentCallback;

public class MozaikClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        FabricNetworkHelper.clientPacketRegistration();
        ModelLoadingPlugin.register(new MozaikModelLoadingPlugin());

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