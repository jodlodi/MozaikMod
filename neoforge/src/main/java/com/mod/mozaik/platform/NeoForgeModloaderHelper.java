package com.mod.mozaik.platform;

import com.mod.mozaik.blocks.MortarBlock;
import com.mod.mozaik.blocks.NeoMortarBlock;
import com.mod.mozaik.blocks.entities.MortarBlockEntity;
import com.mod.mozaik.blocks.entities.NeoMortarBlockEntity;
import com.mod.mozaik.client.screens.MortarScreen;
import com.mod.mozaik.platform.services.IModloaderHelper;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.settings.IKeyConflictContext;
import net.neoforged.neoforge.client.settings.KeyModifier;
import org.lwjgl.glfw.GLFW;

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

	@Override
	public KeyMapping createKeyMapping(String name, InputConstants.Type type, int keyCode, int keyMod, String category) {
		KeyModifier modifier = switch (keyMod) {
			case GLFW.GLFW_MOD_SHIFT -> KeyModifier.SHIFT;
			case GLFW.GLFW_MOD_CONTROL -> KeyModifier.CONTROL;
			case GLFW.GLFW_MOD_ALT -> KeyModifier.ALT;
			default -> KeyModifier.NONE;
		};
		return new KeyMapping(name, OurConflicts.INSTANCE, modifier, type, keyCode, category);
	}

	private static class OurConflicts implements IKeyConflictContext {
		private static final OurConflicts INSTANCE = new OurConflicts();

		@Override
		public boolean isActive() {
			return Minecraft.getInstance().screen instanceof MortarScreen;
		}

		@Override
		public boolean conflicts(IKeyConflictContext other) {
			return this == other;
		}
	}
}
