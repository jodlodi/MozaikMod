package com.mod.mozaik.util;

import com.google.common.collect.ImmutableList;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
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
public record ColorCollection<T>(T white, T orange, T magenta, T lightBlue, T yellow, T lime, T pink, T gray, T lightGray, T cyan, T purple, T blue, T brown, T green, T red, T black) {
	public static final ColorCollection<DyeColor> VALUES = new ColorCollection<>(DyeColor.WHITE, DyeColor.ORANGE, DyeColor.MAGENTA, DyeColor.LIGHT_BLUE, DyeColor.YELLOW, DyeColor.LIME, DyeColor.PINK, DyeColor.GRAY, DyeColor.LIGHT_GRAY, DyeColor.CYAN, DyeColor.PURPLE, DyeColor.BLUE, DyeColor.BROWN, DyeColor.GREEN, DyeColor.RED, DyeColor.BLACK);
	public static final ColorCollection<String> NAMES = VALUES.map(DyeColor::getName);

	public static class BlockCollections {
		public static final ColorCollection<Block> DYED_TERRACOTTA = new ColorCollection<>(Blocks.WHITE_TERRACOTTA, Blocks.ORANGE_TERRACOTTA, Blocks.MAGENTA_TERRACOTTA, Blocks.LIGHT_BLUE_TERRACOTTA, Blocks.YELLOW_TERRACOTTA, Blocks.LIME_TERRACOTTA, Blocks.PINK_TERRACOTTA, Blocks.GRAY_TERRACOTTA, Blocks.LIGHT_GRAY_TERRACOTTA, Blocks.CYAN_TERRACOTTA, Blocks.PURPLE_TERRACOTTA, Blocks.BLUE_TERRACOTTA, Blocks.BROWN_TERRACOTTA, Blocks.GREEN_TERRACOTTA, Blocks.RED_TERRACOTTA, Blocks.BLACK_TERRACOTTA);
		public static final ColorCollection<Block> GLAZED_TERRACOTTA = new ColorCollection<>(Blocks.WHITE_GLAZED_TERRACOTTA, Blocks.ORANGE_GLAZED_TERRACOTTA, Blocks.MAGENTA_GLAZED_TERRACOTTA, Blocks.LIGHT_BLUE_GLAZED_TERRACOTTA, Blocks.YELLOW_GLAZED_TERRACOTTA, Blocks.LIME_GLAZED_TERRACOTTA, Blocks.PINK_GLAZED_TERRACOTTA, Blocks.GRAY_GLAZED_TERRACOTTA, Blocks.LIGHT_GRAY_GLAZED_TERRACOTTA, Blocks.CYAN_GLAZED_TERRACOTTA, Blocks.PURPLE_GLAZED_TERRACOTTA, Blocks.BLUE_GLAZED_TERRACOTTA, Blocks.BROWN_GLAZED_TERRACOTTA, Blocks.GREEN_GLAZED_TERRACOTTA, Blocks.RED_GLAZED_TERRACOTTA, Blocks.BLACK_GLAZED_TERRACOTTA);
		public static final ColorCollection<Block> STAINED_GLASS = new ColorCollection<>(Blocks.WHITE_STAINED_GLASS, Blocks.ORANGE_STAINED_GLASS, Blocks.MAGENTA_STAINED_GLASS, Blocks.LIGHT_BLUE_STAINED_GLASS, Blocks.YELLOW_STAINED_GLASS, Blocks.LIME_STAINED_GLASS, Blocks.PINK_STAINED_GLASS, Blocks.GRAY_STAINED_GLASS, Blocks.LIGHT_GRAY_STAINED_GLASS, Blocks.CYAN_STAINED_GLASS, Blocks.PURPLE_STAINED_GLASS, Blocks.BLUE_STAINED_GLASS, Blocks.BROWN_STAINED_GLASS, Blocks.GREEN_STAINED_GLASS, Blocks.RED_STAINED_GLASS, Blocks.BLACK_STAINED_GLASS);
	}

	public static class ItemCollections {
		public static final ColorCollection<Item> CONCRETE_POWDER = new ColorCollection<>(Items.WHITE_CONCRETE_POWDER, Items.ORANGE_CONCRETE_POWDER, Items.MAGENTA_CONCRETE_POWDER, Items.LIGHT_BLUE_CONCRETE_POWDER, Items.YELLOW_CONCRETE_POWDER, Items.LIME_CONCRETE_POWDER, Items.PINK_CONCRETE_POWDER, Items.GRAY_CONCRETE_POWDER, Items.LIGHT_GRAY_CONCRETE_POWDER, Items.CYAN_CONCRETE_POWDER, Items.PURPLE_CONCRETE_POWDER, Items.BLUE_CONCRETE_POWDER, Items.BROWN_CONCRETE_POWDER, Items.GREEN_CONCRETE_POWDER, Items.RED_CONCRETE_POWDER, Items.BLACK_CONCRETE_POWDER);
	}

	public static <T> ColorCollection<T> create(T value) {
		return new ColorCollection<>(value, value, value, value, value, value, value, value, value, value, value, value, value, value, value, value);
	}

	public static ColorCollection<String> prefixWithColor(ColorCollection<String> ids) {
		return zipMap(NAMES, ids, (color, id) -> color + "_" + id);
	}

	public @Unmodifiable List<T> asList() {
		ImmutableList.Builder<T> builder = ImmutableList.builderWithExpectedSize(16);
		Objects.requireNonNull(builder);
		this.forEach(builder::add);
		return builder.build();
	}

	public void forEach(Consumer<T> consumer) {
		consumer.accept(this.white);
		consumer.accept(this.orange);
		consumer.accept(this.magenta);
		consumer.accept(this.lightBlue);
		consumer.accept(this.yellow);
		consumer.accept(this.lime);
		consumer.accept(this.pink);
		consumer.accept(this.gray);
		consumer.accept(this.lightGray);
		consumer.accept(this.cyan);
		consumer.accept(this.purple);
		consumer.accept(this.blue);
		consumer.accept(this.brown);
		consumer.accept(this.green);
		consumer.accept(this.red);
		consumer.accept(this.black);
	}

	public T pick(DyeColor dyeColor) {
		return switch (dyeColor) {
			case WHITE -> this.white;
			case ORANGE -> this.orange;
			case MAGENTA -> this.magenta;
			case LIGHT_BLUE -> this.lightBlue;
			case YELLOW -> this.yellow;
			case LIME -> this.lime;
			case PINK -> this.pink;
			case GRAY -> this.gray;
			case LIGHT_GRAY -> this.lightGray;
			case CYAN -> this.cyan;
			case PURPLE -> this.purple;
			case BLUE -> this.blue;
			case BROWN -> this.brown;
			case GREEN -> this.green;
			case RED -> this.red;
			case BLACK -> this.black;
		};
	}

	public <U> ColorCollection<U> map(Function<T, U> mapper) {
		return new ColorCollection<>(mapper.apply(this.white), mapper.apply(this.orange), mapper.apply(this.magenta), mapper.apply(this.lightBlue), mapper.apply(this.yellow), mapper.apply(this.lime), mapper.apply(this.pink), mapper.apply(this.gray), mapper.apply(this.lightGray), mapper.apply(this.cyan), mapper.apply(this.purple), mapper.apply(this.blue), mapper.apply(this.brown), mapper.apply(this.green), mapper.apply(this.red), mapper.apply(this.black));
	}

	public static <T, U> void zipApply(ColorCollection<T> first, ColorCollection<U> second, BiConsumer<T, U> consumer) {
		consumer.accept(first.white(), second.white());
		consumer.accept(first.orange(), second.orange());
		consumer.accept(first.magenta(), second.magenta());
		consumer.accept(first.lightBlue(), second.lightBlue());
		consumer.accept(first.yellow(), second.yellow());
		consumer.accept(first.lime(), second.lime());
		consumer.accept(first.pink(), second.pink());
		consumer.accept(first.gray(), second.gray());
		consumer.accept(first.lightGray(), second.lightGray());
		consumer.accept(first.cyan(), second.cyan());
		consumer.accept(first.purple(), second.purple());
		consumer.accept(first.blue(), second.blue());
		consumer.accept(first.brown(), second.brown());
		consumer.accept(first.green(), second.green());
		consumer.accept(first.red(), second.red());
		consumer.accept(first.black(), second.black());
	}

	public static <T, U, R> ColorCollection<R> zipMap(ColorCollection<T> first, ColorCollection<U> second, BiFunction<T, U, R> operation) {
		return new ColorCollection<>(operation.apply(first.white(), second.white()), operation.apply(first.orange(), second.orange()), operation.apply(first.magenta(), second.magenta()), operation.apply(first.lightBlue(), second.lightBlue()), operation.apply(first.yellow(), second.yellow()), operation.apply(first.lime(), second.lime()), operation.apply(first.pink(), second.pink()), operation.apply(first.gray(), second.gray()), operation.apply(first.lightGray(), second.lightGray()), operation.apply(first.cyan(), second.cyan()), operation.apply(first.purple(), second.purple()), operation.apply(first.blue(), second.blue()), operation.apply(first.brown(), second.brown()), operation.apply(first.green(), second.green()), operation.apply(first.red(), second.red()), operation.apply(first.black(), second.black()));
	}
}
