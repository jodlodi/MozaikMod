package com.mod.mozaik.data;

import com.mod.mozaik.Constants;
import com.mod.mozaik.structure.ModStructureSets;
import com.mod.mozaik.structure.ModStructures;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModRegistryGen extends DatapackBuiltinEntriesProvider {
	public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
			.add(Registries.STRUCTURE, ModStructures::bootstrap)
			.add(Registries.STRUCTURE_SET, ModStructureSets::bootstrap);

	public ModRegistryGen(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
		super(output, provider, BUILDER, Set.of("minecraft", Constants.MOD_ID));
	}
}
