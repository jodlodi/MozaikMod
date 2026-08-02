package com.mod.mozaik.reg;

import com.mod.mozaik.platform.Services;
import net.minecraft.world.item.CreativeModeTab;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class ModTabs {
	public static final ResourceSupplier<CreativeModeTab> TAB = Services.REGISTRY.registerCreativeTab("tab", () -> ModItems.GLUE.get().getDefaultInstance(), (itemDisplayParameters, output) -> {
		output.accept(ModItems.GLUE.get());
	});

	public static void init() {

	}
}
