package com.mod.mozaik.platform.services;

import com.mod.mozaik.blocks.MortarBlock;
import com.mod.mozaik.blocks.entities.MortarBlockEntity;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public interface IModloaderHelper {
	default MortarBlock mortarBlock(DyeColor color, BlockBehaviour.Properties properties) {
		return new MortarBlock(color, properties);
	}

	default MortarBlockEntity mortarBlockEntity(BlockPos pos, BlockState blockState) {
		return new MortarBlockEntity(pos, blockState);
	}

	KeyMapping createKeyMapping(String name, InputConstants.Type type, int keyCode, int keyMod, KeyMapping.Category category);
}
