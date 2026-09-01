package com.mod.mozaik.reg;

import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class ResourceSupplier<T> implements Supplier<T> {

	private final Supplier<T> supplier;
	private final ResourceLocation id;

	public ResourceSupplier(Supplier<T> supplier, ResourceLocation id) {
		this.supplier = supplier;
		this.id = id;
	}

	@Override
	public T get() {
		return this.supplier.get();
	}

	public ResourceLocation id() {
		return this.id;
	}
}
