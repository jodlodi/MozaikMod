package com.mod.mozaik.platform;

import com.mod.mozaik.blocks.FabricMortarBlock;
import com.mod.mozaik.blocks.MortarBlock;
import com.mod.mozaik.blocks.entities.FabricMortarBlockEntity;
import com.mod.mozaik.blocks.entities.MortarBlockEntity;
import com.mod.mozaik.platform.services.IModloaderHelper;
import com.mod.mozaik.util.IFabricKeyMapping;
import com.mod.mozaik.util.IMozaikKeyMapping;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.lwjgl.glfw.GLFW;

public class FabricModloaderHelper implements IModloaderHelper {

    @Override
    public MortarBlock mortarBlock(DyeColor color, BlockBehaviour.Properties properties) {
        return new FabricMortarBlock(color, properties);
    }

    @Override
    public MortarBlockEntity mortarBlockEntity(BlockPos pos, BlockState blockState) {
        return new FabricMortarBlockEntity(pos, blockState);
    }

    @Override
    public KeyMapping createKeyMapping(String name, InputConstants.Type type, int keyCode, int keyMod, KeyMapping.Category category) {
        KeyMapping mapping = new KeyMapping(name, type, keyCode, category);
        ((IFabricKeyMapping) mapping).mozaik$setModifier(
            switch (keyMod) {
                case GLFW.GLFW_MOD_SHIFT -> IMozaikKeyMapping.Modifier.SHIFT;
                case GLFW.GLFW_MOD_CONTROL -> IMozaikKeyMapping.Modifier.CONTROL;
                case GLFW.GLFW_MOD_ALT -> IMozaikKeyMapping.Modifier.ALT;
                default -> IMozaikKeyMapping.Modifier.NONE;
            }
        );
        return mapping;
    }
}