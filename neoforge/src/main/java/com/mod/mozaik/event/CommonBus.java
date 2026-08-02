package com.mod.mozaik.event;

import com.mod.mozaik.Constants;
import com.mod.mozaik.reg.ModBlockEntities;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.RegisterEvent;

@EventBusSubscriber(modid = Constants.MOD_ID)
public class CommonBus {

	@SubscribeEvent
	public static void registerScreens(RegisterEvent event) {
		if (event.getRegistry() == BuiltInRegistries.BLOCK) ModBlockEntities.init();
	}
}
