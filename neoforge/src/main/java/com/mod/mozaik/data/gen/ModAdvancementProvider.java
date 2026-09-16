package com.mod.mozaik.data.gen;

import net.minecraft.advancements.Advancement;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.data.worldgen.BootstrapContext;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
public class ModAdvancementProvider {
	public static void bootstrap(BootstrapContext<Advancement> output) {
		new AdvancementProvider(List.of(ModAdvancementGen::new)).run(output);
	}
}
