package com.mod.mozaik.client.model;

import com.mod.mozaik.client.model.block.mortar.MosaicStateModel;
import net.fabricmc.fabric.api.client.model.loading.v1.CustomUnbakedBlockStateModel;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import org.jspecify.annotations.NonNull;

public class MozaikModelLoadingPlugin implements ModelLoadingPlugin {

    @Override
    public void initialize(@NonNull Context pluginContext) {
        CustomUnbakedBlockStateModel.register(MosaicStateModel.Unbaked.ID, MosaicStateModel.Unbaked.CODEC);
    }
}