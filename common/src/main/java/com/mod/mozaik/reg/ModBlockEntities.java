package com.mod.mozaik.reg;

import com.mod.mozaik.blocks.entities.MortarBlockEntity;
import com.mod.mozaik.platform.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.NullMarked;

import java.util.List;
import java.util.function.BiFunction;

@NullMarked
public class ModBlockEntities {
	public static final ResourceSupplier<BlockEntityType<MortarBlockEntity>> MORTAR = registerBlockEntity("mortar", Services.MODLOADER::mortarBlockEntity, ModBlocks.MORTARS.asList());

	public static void init() {

	}

	private static <T extends BlockEntity, B extends Block> ResourceSupplier<BlockEntityType<T>> registerBlockEntity(String id, BiFunction<BlockPos, BlockState, T> supplier, List<ResourceSupplier<B>> blocks){
		return Services.REGISTRY.registerBlockEntityType(id, supplier, blocks);
	}
}
