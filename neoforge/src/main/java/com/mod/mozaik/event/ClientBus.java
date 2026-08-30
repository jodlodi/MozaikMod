package com.mod.mozaik.event;

import com.mod.mozaik.Constants;
import com.mod.mozaik.client.tooltips.ClientShardBagTooltip;
import com.mod.mozaik.client.ModKeyMappings;
import com.mod.mozaik.client.model.block.mortar.MosaicStateModel;
import com.mod.mozaik.client.screens.MortarScreen;
import com.mod.mozaik.client.tooltips.PolyominoTooltip;
import com.mod.mozaik.items.PolyominoItem;
import com.mod.mozaik.items.ShardBagItem;
import com.mod.mozaik.platform.NeoForgeRegistryHelper;
import com.mod.mozaik.polyomino.TesseraShape;
import com.mod.mozaik.reg.ModMenus;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.*;
import net.neoforged.neoforge.client.model.standalone.SimpleUnbakedStandaloneModel;
import net.neoforged.neoforge.client.model.standalone.StandaloneModelKey;
import org.jspecify.annotations.NullMarked;

@NullMarked
@EventBusSubscriber(modid = Constants.MOD_ID, value = Dist.CLIENT)
public class ClientBus {
	@SubscribeEvent
	public static void registerScreens(RegisterMenuScreensEvent event) {
		event.register(ModMenus.GLUE.get(), MortarScreen::new);
	}

	@SubscribeEvent
	public static void registerTooltips(RegisterClientTooltipComponentFactoriesEvent event) {
		event.register(ShardBagItem.ShardBagTooltip.class, ClientShardBagTooltip::new);
		event.register(PolyominoItem.ShapeTooltip.class, PolyominoTooltip::new);
	}

	@SubscribeEvent
	public static void registerDefinitions(RegisterBlockStateModels event) {
		event.registerModel(MosaicStateModel.Unbaked.ID, MosaicStateModel.Unbaked.CODEC);
	}

	@SubscribeEvent
	public static void registerAdditionalModels(ModelEvent.RegisterStandalone event) {
		NeoForgeRegistryHelper.SHARD_MATERIALS.getEntries().forEach(holder -> {
			for (int i = 0; i < holder.get().shades(); i++) {
				for (TesseraShape.ModelReference shape : TesseraShape.ModelReference.values()) {
					registerModel(event, "mozaik/" + holder.getId().getPath() + "/" + i + "/" + shape.getSerializedName());
				}
			}
		});
	}

	private static void registerModel(ModelEvent.RegisterStandalone event, String name) {
		event.register(new StandaloneModelKey<>(() -> Constants.prefix(name).toString()), SimpleUnbakedStandaloneModel.simpleModelWrapper(Constants.prefix(name)));
	}

	@SubscribeEvent
	public static void registerKeyMappingsEvent(RegisterKeyMappingsEvent event) {
		ModKeyMappings.KEY_MAPPINGS.forEach(event::register);
		event.registerCategory(ModKeyMappings.MOD_TOOLS);
		event.registerCategory(ModKeyMappings.MOD_ACTIONS);
	}
}
