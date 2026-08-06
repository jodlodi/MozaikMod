package com.mod.mozaik.event;

import com.mod.mozaik.Constants;
import com.mod.mozaik.TesseraMaterial;
import com.mod.mozaik.client.model.block.mortar.MosaicStateModel;
import com.mod.mozaik.client.screens.MortarScreen;
import com.mod.mozaik.data.gen.model.ModBlockStateGen;
import com.mod.mozaik.reg.ModBlockEntities;
import com.mod.mozaik.reg.ModMenus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.model.standalone.SimpleUnbakedStandaloneModel;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;
import org.jspecify.annotations.NullMarked;

@NullMarked
@EventBusSubscriber(modid = Constants.MOD_ID)
public class ClientBus {
	@SubscribeEvent
	public static void registerScreens(RegisterMenuScreensEvent event) {
		event.register(ModMenus.GLUE.get(), MortarScreen::new);
	}

	@SubscribeEvent
	public static void registerScreens(EntityRenderersEvent.RegisterRenderers event) {

	}

	@SubscribeEvent
	public static void registerDefinitions(RegisterBlockStateModels event) {
		event.registerModel(MosaicStateModel.Unbaked.ID, MosaicStateModel.Unbaked.CODEC);
	}

	@SubscribeEvent
	public static void registerAdditionalModels(ModelEvent.RegisterStandalone event) {
		for (TesseraMaterial material : TesseraMaterial.values()) {
			for (int i = 0; i < material.getSpriteSheets().size(); i++) {
				registerModel(event, material.getSerializedName() + "/" + i + "/" + ModBlockStateGen.TESSERA);

				registerModel(event, material.getSerializedName() + "/" + i + "/" + ModBlockStateGen.BRIDGE_UP);
				registerModel(event, material.getSerializedName() + "/" + i + "/" + ModBlockStateGen.BRIDGE_NO_UP);

				registerModel(event, material.getSerializedName() + "/" + i + "/" + ModBlockStateGen.BRIDGE_RIGHT);
				registerModel(event, material.getSerializedName() + "/" + i + "/" + ModBlockStateGen.BRIDGE_NO_RIGHT);

				registerModel(event, material.getSerializedName() + "/" + i + "/" + ModBlockStateGen.BRIDGE_DOWN);
				registerModel(event, material.getSerializedName() + "/" + i + "/" + ModBlockStateGen.BRIDGE_NO_DOWN);

				registerModel(event, material.getSerializedName() + "/" + i + "/" + ModBlockStateGen.BRIDGE_LEFT);
				registerModel(event, material.getSerializedName() + "/" + i + "/" + ModBlockStateGen.BRIDGE_NO_LEFT);

				registerModel(event, material.getSerializedName() + "/" + i + "/" + ModBlockStateGen.CORNER_UP_RIGHT);
				registerModel(event, material.getSerializedName() + "/" + i + "/" + ModBlockStateGen.CORNER_UP_NO_RIGHT);

				registerModel(event, material.getSerializedName() + "/" + i + "/" + ModBlockStateGen.CORNER_DOWN_RIGHT);
				registerModel(event, material.getSerializedName() + "/" + i + "/" + ModBlockStateGen.CORNER_RIGHT_NO_DOWN);

				registerModel(event, material.getSerializedName() + "/" + i + "/" + ModBlockStateGen.CORNER_DOWN_LEFT);
				registerModel(event, material.getSerializedName() + "/" + i + "/" + ModBlockStateGen.CORNER_DOWN_NO_LEFT);

				registerModel(event, material.getSerializedName() + "/" + i + "/" + ModBlockStateGen.CORNER_UP_LEFT);
				registerModel(event, material.getSerializedName() + "/" + i + "/" + ModBlockStateGen.CORNER_LEFT_NO_UP);
			}
		}
	}

	private static void registerModel(ModelEvent.RegisterStandalone event, String name) {
		event.register(new StandaloneModelKey<>(() -> Constants.prefix(name).toString()), SimpleUnbakedStandaloneModel.simpleModelWrapper(Constants.prefix(name)));
	}
}
