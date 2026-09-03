package com.mod.mozaik.client.model.block.mortar;

import com.mod.mozaik.Constants;
import com.mod.mozaik.blocks.MortarBlock;
import com.mod.mozaik.client.GraphicsRenderHelper;
import com.mod.mozaik.client.model.TesseraHelper;
import com.mod.mozaik.menus.MortarMenu;
import com.mod.mozaik.polyomino.Polyomino;
import com.mod.mozaik.polyomino.ShardMaterial;
import com.mod.mozaik.polyomino.Tessera;
import com.mod.mozaik.polyomino.TesseraShape;
import com.mod.mozaik.reg.ModRegistries;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.fabric.api.client.model.loading.v1.CustomUnbakedBlockStateModel;
import net.fabricmc.fabric.api.client.renderer.v1.mesh.QuadEmitter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.BlockAndTintGetter;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ResolvableModel;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;

@NullMarked
public final class MosaicStateModel implements BlockStateModel {
    private final Map<Direction, MortarModelPart> mortarMap = new HashMap<>();

    public MosaicStateModel() {
    }

    @Override
    public Material.Baked particleMaterial() {
        return this.mortarMap.get(Direction.UP).particleMaterial();
    }

    @Override
    public Material.Baked particleMaterial(BlockAndTintGetter level, BlockPos pos, BlockState state) {
        return this.particleMaterial();
    }

    @Override
    public @BakedQuad.MaterialFlags int materialFlags() {
        return this.mortarMap.get(Direction.UP).materialFlags();
    }

    @Override
    public @BakedQuad.MaterialFlags int materialFlags(BlockAndTintGetter level, BlockPos pos, BlockState state, RandomSource random) {
        return this.materialFlags();
    }

    @Override
    public Object createGeometryKey(BlockAndTintGetter level, BlockPos pos, BlockState state, RandomSource random) {
        return this;
    }

    @Override
    public void collectParts(RandomSource random, List<BlockStateModelPart> output) {
        // Does nothing, this is a Fabric model.
    }

    @Override
    public void emitQuads(QuadEmitter emitter, BlockAndTintGetter level, BlockPos pos, BlockState state, RandomSource random, Predicate<@Nullable Direction> cullTest) {
        Direction facing = state.getValue(MortarBlock.FACING_ROTATED).getDirection();
        BlockStateModelPart part = this.mortarMap.get(facing);
        part.emitQuads(emitter, cullTest);

        if (GraphicsRenderHelper.BAKER == null) return;

        Object data = level.getBlockEntityRenderData(pos);
        if (!(data instanceof List<?> input)) return;


        List<Polyomino.PlacedPolyomino> copy = input.stream().map(Polyomino.PlacedPolyomino.class::cast).toList();
        List<Polyomino.PlacedPolyomino> list = new ArrayList<>();

        Rotation blockRotation = state.getValue(MortarBlock.FACING_ROTATED).getRotation();
        copy.forEach(placedPolyomino -> list.add(MortarMenu.rotate(placedPolyomino, blockRotation)));

        ClientLevel clientLevel = Minecraft.getInstance().level;
        if (clientLevel == null) return;

        list.forEach((Polyomino.PlacedPolyomino polyomino) -> {
            if (polyomino == null) return;
            int x = polyomino.x();
            int y = polyomino.y();

            int index = -1;
            for (Tessera.PlacedTessera tessera : polyomino.polyomino().placedTessera()) {
                index++;
                int fx = x + tessera.x();
                int fy = y + tessera.y();
                BlockStateModelPart tesseraPart = TesseraHelper.bakeTessera(clientLevel.registryAccess().get(polyomino.polyomino().material()).orElseThrow().value(), polyomino.polyomino().material().identifier().getPath(), facing, fx, fy, polyomino.polyomino().uuid().getMostSignificantBits(), index, tessera.tessera().shape());
                tesseraPart.emitQuads(emitter, cullTest);
            }
        });
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (MosaicStateModel) obj;
        return Objects.equals(this.mortarMap, that.mortarMap);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mortarMap.get(Direction.UP));
    }

    @Override
    public String toString() {
        return "MyBlockStateModel[" +
            "model=" + mortarMap.get(Direction.UP) + ']';
    }

    public record Unbaked(Identifier model) implements CustomUnbakedBlockStateModel {
        public static final MapCodec<Unbaked> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                Identifier.CODEC.fieldOf("model").forGetter(Unbaked::model)
            ).apply(instance, Unbaked::new)
        );
        public static final Identifier ID = Constants.prefix("my_custom_model_loader");

        @Override
        public void resolveDependencies(ResolvableModel.Resolver resolver) {
            resolver.markDependency(this.model);

            for (Map.Entry<ResourceKey<ShardMaterial>, ShardMaterial> entry : ModRegistries.SHARD_MATERIALS.entrySet()) {
                Identifier materialId = entry.getKey().identifier();
                ShardMaterial material = entry.getValue();
                for (int blockId = 0; blockId < material.shades(); blockId++) {
                    for (TesseraShape.ModelReference shape : TesseraShape.ModelReference.values()) {
                        resolver.markDependency(
                            Constants.prefix(
                                "mozaik/" +
                                    materialId.getPath() +
                                    "/" +
                                    blockId +
                                    "/" +
                                    shape.getSerializedName()
                            )
                        );
                    }
                }
            }
        }

        @Override
        public BlockStateModel bake(ModelBaker baker) {
            GraphicsRenderHelper.BAKER = baker;

            MosaicStateModel blockStateModel = new MosaicStateModel();
            for (Direction direction : Direction.values()) {
                blockStateModel.mortarMap.put(direction, new MortarModelPart.Unbaked(this.model, new MortarModelPart.MyModelState(direction)).bake(baker));
            }

            return blockStateModel;
        }

        @Override
        public MapCodec<? extends CustomUnbakedBlockStateModel> codec() {
            return CODEC;
        }
    }
}