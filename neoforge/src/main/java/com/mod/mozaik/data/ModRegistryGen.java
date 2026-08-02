package com.mod.mozaik.data;

import com.mod.mozaik.Constants;
import com.mod.mozaik.mixin.RegistriesDatapackGeneratorAccessor;
import com.mod.mozaik.mixin.RegistryAccessAccessor;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.*;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.common.conditions.ConditionalOps;
import net.neoforged.neoforge.common.conditions.WithConditions;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.registries.DataPackRegistriesHooks;
import org.jspecify.annotations.NullMarked;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@NullMarked
public class ModRegistryGen extends DatapackBuiltinEntriesProvider {
	private static final Set<String> SET = Set.of("minecraft", Constants.MOD_ID);

	private final CompletableFuture<HolderLookup.Provider> neoForgeRegistries;
	private final PackOutput neoForgeOutput;

	public ModRegistryGen(CompletableFuture<HolderLookup.Provider> provider, PackOutput neoForgeOutput, PackOutput fabricOutput) {
		super(fabricOutput, provider, addCommonRegistries(new RegistrySetBuilder()), SET);
		this.neoForgeOutput = neoForgeOutput;
		this.neoForgeRegistries = provider.thenApply(forgeProvider ->
				constructRegistries(forgeProvider, addForgeAndCommonRegistries(new RegistrySetBuilder()))
		).thenApply(RegistrySetBuilder.PatchedRegistries::patches);
	}

	private static RegistrySetBuilder addCommonRegistries(RegistrySetBuilder builder) {
		return builder;
	}

	private static RegistrySetBuilder addForgeAndCommonRegistries(RegistrySetBuilder builder) {
		return addCommonRegistries(builder);
	}

	@SuppressWarnings("UnstableApiUsage")
	private static RegistrySetBuilder.PatchedRegistries constructRegistries(HolderLookup.Provider original, RegistrySetBuilder datapackEntriesBuilder) {
		HashSet<? extends ResourceKey<? extends Registry<?>>> builderKeys = new HashSet<>(datapackEntriesBuilder.getEntryKeys());
		DataPackRegistriesHooks.getDataPackRegistriesWithDimensions().filter(data -> !builderKeys.contains(data.key())).forEach(data -> datapackEntriesBuilder.add(data.key(), _ -> {}));
		Cloner.Factory factory = new Cloner.Factory();
		DataPackRegistriesHooks.getDataPackRegistriesWithDimensions().forEach(data -> data.runWithArguments(factory::addCodec));
		return datapackEntriesBuilder.buildPatch(RegistryAccessAccessor.invokeFromRegistryOfRegistries(BuiltInRegistries.REGISTRY), original, factory);
	}

	@Override
	@SuppressWarnings("UnstableApiUsage")
	public CompletableFuture<?> run(CachedOutput output) {
		CompletableFuture<?> fabric = super.run(output);

		CompletableFuture<?> forge = this.neoForgeRegistries.thenCompose(provider -> {
			DynamicOps<JsonElement> dynamicops = RegistryOps.create(JsonOps.INSTANCE, provider);
			return CompletableFuture.allOf(DataPackRegistriesHooks.getDataPackRegistriesWithDimensions().flatMap(registryData ->
					this.dumpNeoForgeRegistryCap(output, provider, dynamicops, registryData).stream()
			).toArray(CompletableFuture[]::new));
		});

		return CompletableFuture.allOf(fabric, forge);
	}

	private <T> Optional<CompletableFuture<?>> dumpNeoForgeRegistryCap(CachedOutput output, HolderLookup.Provider registries, DynamicOps<JsonElement> ops, RegistryDataLoader.RegistryData<T> registryData) {
		ResourceKey<? extends Registry<T>> key = registryData.key();
		Codec<Optional<WithConditions<T>>> codec = ConditionalOps.createConditionalCodecWithConditions(registryData.elementCodec());
		return registries.lookup(key).map((holderLookup) -> {
			PackOutput.PathProvider pathProvider = this.neoForgeOutput.createRegistryElementsPathProvider(key);
			return CompletableFuture.allOf(holderLookup.listElements().filter((holder) ->
					SET.contains(holder.key().identifier().getNamespace())).map((holder) ->
					RegistriesDatapackGeneratorAccessor.invokeDumpValue(pathProvider.json(holder.key().identifier()), output, ops, codec, Optional.of(new WithConditions<>(((RegistriesDatapackGeneratorAccessor) this).getConditions().getOrDefault(holder.key(), List.of()), holder.value())))
			).toArray(CompletableFuture[]::new));
		});
	}
}