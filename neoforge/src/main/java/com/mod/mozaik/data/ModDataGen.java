package com.mod.mozaik.data;

import com.mod.mozaik.Constants;
import com.mod.mozaik.data.gen.ModLangGen;
import com.mod.mozaik.data.gen.ModLootGen;
import com.mod.mozaik.data.gen.ModelGen;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.jspecify.annotations.NullMarked;

import javax.annotation.ParametersAreNonnullByDefault;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

@NullMarked
@ParametersAreNonnullByDefault
@EventBusSubscriber(modid = Constants.MOD_ID)
public class ModDataGen {

	private static final String FABRIC_PROPERTY = "archies.fabric.datagen.path";
	private static final String FORGE_PROPERTY = "archies.forge.datagen.path";

	@SubscribeEvent
	public static void gatherDataClient(GatherDataEvent.Client event) {
		DataGenerator generator = event.getGenerator();
		PackOutput output = event.getGenerator().getPackOutput();
		PackOutput fabricOutput = new PackOutput(Path.of(System.getProperty(FABRIC_PROPERTY)));
		PackOutput forgeOutput = new PackOutput(Path.of(System.getProperty(FORGE_PROPERTY)));

		CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

		ModRegistryGen datapackProvider = new ModRegistryGen(lookupProvider, forgeOutput, fabricOutput);
		CompletableFuture<HolderLookup.Provider> registryProvider = datapackProvider.getRegistryProvider();
		generator.addProvider(true, datapackProvider);
		generator.addProvider(true, new ModLootGen(output, lookupProvider));

		generator.addProvider(true, new ModelGen(output));
		generator.addProvider(true, new ModLangGen(output));
	}
}
