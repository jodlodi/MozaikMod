package com.mod.mozaik.util;

import com.google.common.collect.ImmutableList;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.apache.commons.lang3.function.TriFunction;
import org.jetbrains.annotations.Unmodifiable;
import net.minecraft.MethodsReturnNonnullByDefault;
import javax.annotation.ParametersAreNonnullByDefault;

import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public record NaturalDigitCollection<T>(T n1, T n2, T n3, T n4, T n5, T n6, T n7, T n8, T n9) {
	public static final NaturalDigitCollection<Integer> VALUES = new NaturalDigitCollection<>(1, 2, 3, 4, 5, 6, 7, 8, 9);
	public static final NaturalDigitCollection<String> NAMES = new NaturalDigitCollection<>("one", "two", "three", "four", "five", "six", "seven", "eight", "nine");

	public static <T> NaturalDigitCollection<T> create(T value) {
		return new NaturalDigitCollection<T>(value, value, value, value, value, value, value, value, value);
	}

	public static <Id> NaturalDigitCollection<Item> registerBlockItems(NaturalDigitCollection<Id> ids, NaturalDigitCollection<Block> blocks, TriFunction<Id, Block, Integer, Item> itemFactory) {
		return zipMap(VALUES, ids, (color, id) -> itemFactory.apply(id, blocks.pick(color), color));
	}

	public static <Id> NaturalDigitCollection<Item> registerItems(NaturalDigitCollection<Id> ids, BiFunction<Id, Integer, Item> itemFactory) {
		return zipMap(VALUES, ids, (color, id) -> itemFactory.apply(id, color));
	}

	public static NaturalDigitCollection<String> prefixWithColor(NaturalDigitCollection<String> ids) {
		return zipMap(NAMES, ids, (color, id) -> color + "_" + id);
	}

	public @Unmodifiable List<T> asList() {
		ImmutableList.Builder<T> builder = ImmutableList.builderWithExpectedSize(9);
		Objects.requireNonNull(builder);
		this.forEach(builder::add);
		return builder.build();
	}

	public void forEach(Consumer<T> consumer) {
		consumer.accept(this.n1);
		consumer.accept(this.n2);
		consumer.accept(this.n3);
		consumer.accept(this.n4);
		consumer.accept(this.n5);
		consumer.accept(this.n6);
		consumer.accept(this.n7);
		consumer.accept(this.n8);
		consumer.accept(this.n9);
	}

	public T pick(Integer Integer) {
		return switch (Integer) {
			case 1 -> this.n1;
			case 2 -> this.n2;
			case 3 -> this.n3;
			case 4 -> this.n4;
			case 5 -> this.n5;
			case 6 -> this.n6;
			case 7 -> this.n7;
			case 8 -> this.n8;
			case 9 -> this.n9;
			default -> throw new MatchException(null, null);
		};
	}

	public <U> NaturalDigitCollection<U> map(Function<T, U> mapper) {
		return new NaturalDigitCollection<>(
				mapper.apply(this.n1),
				mapper.apply(this.n2),
				mapper.apply(this.n3),
				mapper.apply(this.n4),
				mapper.apply(this.n5),
				mapper.apply(this.n6),
				mapper.apply(this.n7),
				mapper.apply(this.n8),
				mapper.apply(this.n9)
		);
	}

	public static <T, U> void zipApply(NaturalDigitCollection<T> first, NaturalDigitCollection<U> second, BiConsumer<T, U> consumer) {
		consumer.accept(first.n1(), second.n1());
		consumer.accept(first.n2(), second.n2());
		consumer.accept(first.n3(), second.n3());
		consumer.accept(first.n4(), second.n4());
		consumer.accept(first.n5(), second.n5());
		consumer.accept(first.n6(), second.n6());
		consumer.accept(first.n7(), second.n7());
		consumer.accept(first.n8(), second.n8());
		consumer.accept(first.n9(), second.n9());
	}

	public static <T, U, R> NaturalDigitCollection<R> zipMap(NaturalDigitCollection<T> first, NaturalDigitCollection<U> second, BiFunction<T, U, R> operation) {
		return new NaturalDigitCollection<R>(
				operation.apply(first.n1(), second.n1()),
				operation.apply(first.n2(), second.n2()),
				operation.apply(first.n3(), second.n3()),
				operation.apply(first.n4(), second.n4()),
				operation.apply(first.n5(), second.n5()),
				operation.apply(first.n6(), second.n6()),
				operation.apply(first.n7(), second.n7()),
				operation.apply(first.n8(), second.n8()),
				operation.apply(first.n9(), second.n9())
		);
	}
}
