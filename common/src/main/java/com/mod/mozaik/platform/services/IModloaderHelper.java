package com.mod.mozaik.platform.services;

import com.mod.mozaik.blocks.MortarBlock;
import com.mod.mozaik.blocks.entities.MortarBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public interface IModloaderHelper {
	default MortarBlock mortarBlock(BlockBehaviour.Properties properties) {
		return new MortarBlock(properties);
	}

	default MortarBlockEntity mortarBlockEntity(BlockPos pos, BlockState blockState) {
		return new MortarBlockEntity(pos, blockState);
	}
}
