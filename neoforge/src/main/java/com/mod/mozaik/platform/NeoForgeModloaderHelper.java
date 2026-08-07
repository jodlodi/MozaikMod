package com.mod.mozaik.platform;

import com.mod.mozaik.blocks.MortarBlock;
import com.mod.mozaik.blocks.NeoMortarBlock;
import com.mod.mozaik.blocks.entities.MortarBlockEntity;
import com.mod.mozaik.blocks.entities.NeoMortarBlockEntity;
import com.mod.mozaik.platform.services.IModloaderHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class NeoForgeModloaderHelper implements IModloaderHelper {
	@Override
	public MortarBlock mortarBlock(DyeColor color, BlockBehaviour.Properties properties) {
		return new NeoMortarBlock(color, properties);
	}

	@Override
	public MortarBlockEntity mortarBlockEntity(BlockPos pos, BlockState blockState) {
		return new NeoMortarBlockEntity(pos, blockState);
	}
}
