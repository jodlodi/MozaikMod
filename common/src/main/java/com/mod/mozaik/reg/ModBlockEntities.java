package com.mod.mozaik.reg;

import com.mod.mozaik.blocks.entities.MortarBlockEntity;
import com.mod.mozaik.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.BiFunction;

public class ModBlockEntities {
	public static final ResourceSupplier<BlockEntityType<MortarBlockEntity>> GLUE = registerBlockEntity("glue", Services.MODLOADER::mortarBlockEntity, ModBlocks.GLUE);

	public static void init() {

	}

	@SafeVarargs
	private static <T extends BlockEntity> ResourceSupplier<BlockEntityType<T>> registerBlockEntity(String id, BiFunction<BlockPos, BlockState, T> supplier, ResourceSupplier<Block>... blocks){
		return Services.REGISTRY.registerBlockEntityType(id, supplier, blocks);
	}
}
