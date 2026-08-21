package com.mod.mozaik.event;

import com.mod.mozaik.Constants;
import com.mod.mozaik.blocks.entities.MortarBlockEntity;
import com.mod.mozaik.networking.bidirectional.UpdateGlueBidirectional;
import com.mod.mozaik.platform.Services;
import com.mod.mozaik.reg.ModBlockEntities;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.ChunkWatchEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import org.jspecify.annotations.NullMarked;

@NullMarked
@EventBusSubscriber(modid = Constants.MOD_ID)
public class CommonBus {

	@SubscribeEvent
	public static void registerEvent(RegisterEvent event) {
		if (event.getRegistry() == BuiltInRegistries.BLOCK) ModBlockEntities.init();
	}

	@SubscribeEvent
	public static void onChunkWatchEventSent(ChunkWatchEvent.Sent event) {
		event.getChunk().getBlockEntities().forEach((pos, blockEntity) -> {
			if (blockEntity instanceof MortarBlockEntity entity) {
				Services.NETWORK.sendToClient(event.getPlayer(), new UpdateGlueBidirectional(entity.getPolyominos(), pos));
			}
		});
	}
}
