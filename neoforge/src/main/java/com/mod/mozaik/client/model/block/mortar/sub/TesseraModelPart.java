package com.mod.mozaik.client.model.block.mortar.sub;

import com.mod.mozaik.Constants;
import com.mod.mozaik.client.GraphicsRenderHelper;
import com.mod.mozaik.polyomino.ShardMaterial;
import com.mod.mozaik.polyomino.TesseraShape;
import com.mojang.math.Transformation;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Rotation;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TesseraModelPart {
	public static @Nullable BakedModel bakeTessera(ModelBaker baker, ShardMaterial material, String path, Direction facing, int x, int y, long seed, int index, TesseraShape shape) {
		return new Unbaked(Constants.prefix("mozaik/" + path + "/" + material.getBlockId(seed, index) + "/" + shape.getModel().reference().getSerializedName()), new MyModelState(shape, facing, x, y)).bake(baker);
	}

	public record Unbaked(ResourceLocation modelLocation, MyModelState modelState) {
		public @Nullable BakedModel bake(ModelBaker baker) {
			UnbakedModel model = baker.getModel(this.modelLocation);
			return model.bake(baker, Material::sprite, this.modelState);
		}
	}

	public record MyModelState(TesseraShape shape, Direction facing, int x, int y) implements ModelState {
		public static final float FACTOR = 1.0F;
		public static final float PIXEL = FACTOR / 16.0F;
		public static final float VOXEL_SIZE = 10.0F;
		public static final float SCALE = FACTOR / VOXEL_SIZE;
		public static final float HALF_BLOCK = 0.5F;
		public static final float HALF = PIXEL * 0.5F;

		@Override
		public Transformation getRotation() {
			return new Transformation(new Matrix4f()
					.rotate(this.facing.getRotation())
					.translate(HALF, 1F - PIXEL * 0.2F, HALF)
					.translate(this.x() * PIXEL, 0.0F, this.y() * PIXEL)
					.scale(SCALE)
					.translate(-HALF_BLOCK / SCALE, -HALF_BLOCK / SCALE, -HALF_BLOCK / SCALE)
					.rotate(this.getRotation(this.shape.getModel().rotation())));
		}

		public Quaternionf getRotation(Rotation rotation) {
			return switch (rotation) {
				case Rotation.COUNTERCLOCKWISE_90 -> new Quaternionf().rotationY(((float) Math.PI / 2F));
				case Rotation.NONE -> new Quaternionf();
				case Rotation.CLOCKWISE_90 -> new Quaternionf().rotationY(((float) Math.PI / 2F) * 3.0F);
				case Rotation.CLOCKWISE_180 -> new Quaternionf().rotationY(((float) Math.PI / 2F) * 2.0F);
			};
		}
	}
}