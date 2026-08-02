package com.mod.mozaik.mixin;

import com.google.gson.JsonElement;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Encoder;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.registries.RegistriesDatapackGenerator;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.WithConditions;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@SuppressWarnings("deprecation")
@Mixin(RegistriesDatapackGenerator.class)
public interface RegistriesDatapackGeneratorAccessor {
	@Accessor("conditions")
	Map<ResourceKey<?>, List<ICondition>> getConditions();

	@Contract(pure = true)
	@Invoker("dumpValue")
	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	static @Nullable <E> CompletableFuture<?> invokeDumpValue(
			Path path, CachedOutput output, DynamicOps<JsonElement> ops, Encoder<Optional<WithConditions<E>>> encoder, Optional<WithConditions<E>> optional
	) {
		throw new AssertionError();
	}
}
