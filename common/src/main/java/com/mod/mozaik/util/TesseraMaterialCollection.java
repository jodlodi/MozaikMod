package com.mod.mozaik.util;

import com.google.common.collect.ImmutableList;
import com.mod.mozaik.polyomino.TesseraMaterial;
import org.jspecify.annotations.NullMarked;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

@NullMarked
public final class TesseraMaterialCollection<T> {

	private final Map<TesseraMaterial, T> tMap;

	private TesseraMaterialCollection(Map<TesseraMaterial, T> tMap) {
		this.tMap = tMap;
	}

	public static class Builder<T> {
		private final Map<TesseraMaterial, T> tMap = new HashMap<>();

		public Builder() {

		}

		public void add(TesseraMaterial material, T t) {
			this.tMap.put(material, t);
		}

		public TesseraMaterialCollection<T> build() {
			return  new TesseraMaterialCollection<>(this.tMap);
		}
	}

	public static <T> TesseraMaterialCollection<T> create(T value) {
		Builder<T> builder = new Builder<>();
		for (TesseraMaterial material : TesseraMaterial.values()) {
			builder.add(material, value);
		}
		return builder.build();
	}

	public static TesseraMaterialCollection<String> prefixWithColor(TesseraMaterialCollection<String> ids) {
		return zipMap(ids, (color, id) -> color + "_" + id);
	}

	public List<T> asList() {
		ImmutableList.Builder<T> builder = ImmutableList.builderWithExpectedSize(16);
		Objects.requireNonNull(builder);
		this.forEach(builder::add);
		return builder.build();
	}

	public void forEach(Consumer<T> consumer) {
		for (TesseraMaterial material : TesseraMaterial.values()) {
			consumer.accept(this.pick(material));
		}
	}

	public T pick(TesseraMaterial material) {
		return this.tMap.get(material);
	}

	public <U> TesseraMaterialCollection<U> map(Function<T, U> mapper) {
		Builder<U> builder = new Builder<>();
		for (TesseraMaterial material : TesseraMaterial.values()) {
			builder.add(material, mapper.apply(this.pick(material)));
		}
		return builder.build();
	}

	public static <U> void zipApply(TesseraMaterialCollection<U> collection, BiConsumer<TesseraMaterial, U> consumer) {
		for (TesseraMaterial material : TesseraMaterial.values()) {
			consumer.accept(material, collection.pick(material));
		}
	}

	public static <T> TesseraMaterialCollection<T> zipMap(Function<TesseraMaterial, T> operation) {
		Builder<T> builder = new Builder<>();
		for (TesseraMaterial material : TesseraMaterial.values()) {
			builder.add(material, operation.apply(material));
		}
		return builder.build();
	}

	public static <U, T> TesseraMaterialCollection<T> zipMap(TesseraMaterialCollection<U> collection, BiFunction<TesseraMaterial, U, T> operation) {
		Builder<T> builder = new Builder<>();
		for (TesseraMaterial material : TesseraMaterial.values()) {
			builder.add(material, operation.apply(material, collection.pick(material)));
		}
		return builder.build();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj == null || obj.getClass() != this.getClass()) return false;
		if (obj instanceof TesseraMaterialCollection<?> that) {
			for (TesseraMaterial material : TesseraMaterial.values()) {
				if (!Objects.equals(this.pick(material), that.pick(material))) return false;
			}

			return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(this.tMap.values().toArray());
	}

	@Override
	public String toString() {
		return "TesseraMaterialCollection[" + this.tMap.values() + ']';
	}
}
