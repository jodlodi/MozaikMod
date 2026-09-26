package com.mod.mozaik.data;

import com.mod.mozaik.Constants;
import com.mod.mozaik.data.gen.ModAdvancementProvider;
import com.mod.mozaik.data.gen.ModLootGen;
import com.mod.mozaik.data.gen.ModRecipeProvider;
import com.mod.mozaik.structure.ModStructureSets;
import com.mod.mozaik.structure.ModStructures;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.MultiRegistryBootstrap;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.registries.RegistryPatchGenerator;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.registries.DataPackRegistriesHooks;
import org.jspecify.annotations.NullMarked;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

@NullMarked
public class ModRegistryGen {
	public static final RegistrySetBuilder WORLD_BUILDER = new RegistrySetBuilder()
			.add(Registries.STRUCTURE, ModStructures::bootstrap)
			.add(Registries.STRUCTURE_SET, ModStructureSets::bootstrap);

	public static final RegistrySetBuilder RELOADABLE_BUILDER = new RegistrySetBuilder()
			.add(Registries.LOOT_TABLE, ModLootGen::bootstrap)
			.add(Registries.ADVANCEMENT, ModAdvancementProvider::bootstrap)
			.add(new MultiRegistryBootstrap() {
				@Override
				public Set<ResourceKey<? extends Registry<?>>> requestedRegistries() {
					return Set.of(Registries.RECIPE, Registries.ADVANCEMENT);
				}

				@Override
				public void run(MultiRegistryBootstrap.BootstrapGetter registries) {
					new ModRecipeProvider(registries.get(Registries.RECIPE), registries.get(Registries.ADVANCEMENT)).buildRecipes();
				}
			});

	public static DatapackBuiltinEntriesProvider forWorldLayer(PackOutput output, CompletableFuture<HolderLookup.Provider> worldRegistries) {
		return new DatapackBuiltinEntriesProvider(
				output,
				Constants.MOD_ID + "_world",
				DataPackRegistriesHooks.getWorldRegistries(),
				RegistryPatchGenerator.createWorldLookup(worldRegistries, WORLD_BUILDER),
				Set.of("minecraft", Constants.MOD_ID)
		);
	}

	public static DatapackBuiltinEntriesProvider forReloadableLayer(PackOutput output, CompletableFuture<HolderLookup.Provider> worldRegistries, CompletableFuture<HolderLookup.Provider> reloadableRegistries) {
		return new DatapackBuiltinEntriesProvider(
				output,
				Constants.MOD_ID + "_reloadable",
				RegistryDataLoader.RELOADABLE_REGISTRIES,
				RegistryPatchGenerator.createReloadableLookup(worldRegistries, reloadableRegistries, RELOADABLE_BUILDER),
				Set.of("minecraft", Constants.MOD_ID)
		);
	}
}
