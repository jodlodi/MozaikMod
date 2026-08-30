package com.mod.mozaik.data;

import com.mod.mozaik.Constants;
import com.mod.mozaik.data.gen.*;
import com.mod.mozaik.data.gen.tag.ModBiomesTagGen;
import com.mod.mozaik.data.gen.tag.ModBlockTagGen;
import com.mod.mozaik.data.gen.tag.ModItemTagGen;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.jspecify.annotations.NullMarked;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.concurrent.CompletableFuture;

@NullMarked
@ParametersAreNonnullByDefault
@EventBusSubscriber(modid = Constants.MOD_ID)
public class ModDataGen {

	@SubscribeEvent
	public static void gatherDataClient(GatherDataEvent.Client event) {
		DataGenerator generator = event.getGenerator();
		PackOutput output = event.getGenerator().getPackOutput();

		CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

		ModRegistryGen datapackProvider = new ModRegistryGen(output, lookupProvider);
		CompletableFuture<HolderLookup.Provider> registryProvider = datapackProvider.getRegistryProvider();
		generator.addProvider(true, datapackProvider);
		generator.addProvider(true, new ModLootGen(output, lookupProvider));
		generator.addProvider(true, new ModRecipeProvider.ModRecipeRunner(output, registryProvider));
		generator.addProvider(true, new ModBlockTagGen(output, lookupProvider));
		generator.addProvider(true, new ModItemTagGen(output, lookupProvider));
		generator.addProvider(true, new ModBiomesTagGen(output, lookupProvider));

		generator.addProvider(true, new ModAdvancementProvider(output, registryProvider));
		generator.addProvider(true, new AtlasGen(output, lookupProvider));
		generator.addProvider(true, new ModelGen(output));
		generator.addProvider(true, new SoundGen(output));
		generator.addProvider(true, new ModLangGen(output));
	}
}
