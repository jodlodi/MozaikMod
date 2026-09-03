package com.mod.mozaik.blocks.entities;

import net.fabricmc.fabric.api.blockgetter.v2.RenderDataBlockEntity;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
public class FabricMortarBlockEntity extends MortarBlockEntity implements RenderDataBlockEntity {
    public FabricMortarBlockEntity(BlockPos pos, BlockState blockState) {
        super(pos, blockState);
    }

    @Override
    public void markChanged() {
        super.markChanged();
        if (this.level instanceof ClientLevel clientLevel) {
            clientLevel.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
        }
    }

    @Override
    public Object getRenderData() {
        return List.copyOf(this.getPolyomino());
    }
}
