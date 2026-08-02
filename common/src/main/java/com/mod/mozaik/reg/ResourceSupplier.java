package com.mod.mozaik.reg;

import net.minecraft.resources.Identifier;

import java.util.function.Supplier;

public class ResourceSupplier<T> implements Supplier<T> {

	private final Supplier<T> supplier;
	private final Identifier id;

	public ResourceSupplier(Supplier<T> supplier, Identifier id) {
		this.supplier = supplier;
		this.id = id;
	}

	@Override
	public T get() {
		return this.supplier.get();
	}

	public Identifier id() {
		return this.id;
	}
}
