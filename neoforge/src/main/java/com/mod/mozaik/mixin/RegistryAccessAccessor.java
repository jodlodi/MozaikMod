package com.mod.mozaik.mixin;

import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import org.jetbrains.annotations.Contract;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(RegistryAccess.class)
public interface RegistryAccessAccessor {
	@Contract(pure = true)
	@Invoker("fromRegistryOfRegistries")
	static RegistryAccess.Frozen invokeFromRegistryOfRegistries(final Registry<? extends Registry<?>> registryOfRegistries) {
		throw new AssertionError();
	}
}
