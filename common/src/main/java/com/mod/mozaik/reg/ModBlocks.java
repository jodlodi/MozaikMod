package com.mod.mozaik.reg;

import com.mod.mozaik.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

import java.util.function.Function;
import java.util.function.Supplier;

public class ModBlocks {

	public static final ResourceSupplier<Block> GLUE = registerBlock("glue", Services.MODLOADER::mortarBlock, () ->
			BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(0.6F).sound(SoundType.DRIPSTONE_BLOCK).isViewBlocking(ModBlocks::always).isSuffocating(ModBlocks::always)
	);

	public static void init() {

	}

	private static boolean always(BlockState state, BlockGetter blockGetter, BlockPos blockPos) {
		return true;
	}

	private static <T extends Block> ResourceSupplier<T> registerBlock(String id, Function<BlockBehaviour.Properties, T> block, Supplier<BlockBehaviour.Properties> properties) {
		return Services.REGISTRY.registerBlock(id, block, properties);
	}
}
