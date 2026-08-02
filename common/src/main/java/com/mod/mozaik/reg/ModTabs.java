package com.mod.mozaik.reg;

import com.mod.mozaik.platform.Services;
import net.minecraft.world.item.CreativeModeTab;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class ModTabs {
	public static final ResourceSupplier<CreativeModeTab> GLUE = Services.REGISTRY.registerCreativeTab("w", () -> ModItems.GLUE.get().getDefaultInstance(), new CreativeModeTab.DisplayItemsGenerator() {
		@Override
		public void accept(CreativeModeTab.ItemDisplayParameters itemDisplayParameters, CreativeModeTab.Output output) {
			output.accept(ModItems.GLUE.get());
		}
	});

	public static void init() {

	}
}
