package com.mod.mozaik.data;

import com.mod.mozaik.Constants;
import com.mod.mozaik.data.gen.*;
import com.mod.mozaik.data.gen.model.ModBlockStateGen;
import com.mod.mozaik.data.gen.model.ModItemModelGen;
import com.mod.mozaik.data.gen.tag.ModBiomesTagGen;
import com.mod.mozaik.data.gen.tag.ModBlockTagGen;
import com.mod.mozaik.data.gen.tag.ModItemTagGen;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.concurrent.CompletableFuture;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@EventBusSubscriber(modid = Constants.MOD_ID)
public class ModDataGen {

	@SubscribeEvent
	public static void gatherDataClient(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		PackOutput output = event.getGenerator().getPackOutput();
		ExistingFileHelper helper = event.getExistingFileHelper();

		CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

		ModRegistryGen datapackProvider = new ModRegistryGen(output, lookupProvider);
		CompletableFuture<HolderLookup.Provider> registryProvider = datapackProvider.getRegistryProvider();
		generator.addProvider(true, datapackProvider);
		generator.addProvider(true, new ModLootGen(output, lookupProvider));
		generator.addProvider(true, new ModRecipeProvider(registryProvider, output));
		ModBlockTagGen blockTagGen = generator.addProvider(true, new ModBlockTagGen(output, lookupProvider, helper));
		generator.addProvider(true, new ModItemTagGen(output, lookupProvider, blockTagGen));
		generator.addProvider(true, new ModBiomesTagGen(output, lookupProvider, helper));

		generator.addProvider(true, new ModAdvancementProvider(output, registryProvider));
		generator.addProvider(true, new ModBlockStateGen(output, helper));
		generator.addProvider(true, new ModItemModelGen(output, helper));
		generator.addProvider(true, new SoundGen(output, helper));
		generator.addProvider(true, new ModLangGen(output));
	}
}
