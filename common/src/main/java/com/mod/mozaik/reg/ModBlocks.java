package com.mod.mozaik.reg;

import com.mod.mozaik.blocks.MortarBlock;
import com.mod.mozaik.platform.Services;
import com.mod.mozaik.util.ColorCollection;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.MethodsReturnNonnullByDefault;
import javax.annotation.ParametersAreNonnullByDefault;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ModBlocks {

	public static final ColorCollection<ResourceSupplier<MortarBlock>> MORTARS = registerColoredBlocks("mortar", Services.MODLOADER::mortarBlock, () ->
			BlockBehaviour.Properties.of().strength(4.0F, 6.0F).sound(SoundType.CALCITE).isViewBlocking(ModBlocks::always).isSuffocating(ModBlocks::always)
	);

	public static void init() {

	}

	private static boolean always(BlockState state, BlockGetter blockGetter, BlockPos blockPos) {
		return true;
	}

	private static <T extends Block> ResourceSupplier<T> registerBlock(String id, Function<BlockBehaviour.Properties, T> block, Supplier<BlockBehaviour.Properties> properties) {
		return Services.REGISTRY.registerBlock(id, block, properties);
	}

	public static <T extends Block> ColorCollection<ResourceSupplier<T>> registerColoredBlocks(String baseName, BiFunction<DyeColor, BlockBehaviour.Properties, T> block, Supplier<BlockBehaviour.Properties> properties) {
		return ColorCollection.zipMap(
				ColorCollection.VALUES,
				ColorCollection.prefixWithColor(ColorCollection.create(baseName)),
				(color, id) -> registerBlock(id, gennedProperties -> block.apply(color, gennedProperties.mapColor(color)), properties)
		);
	}
}
