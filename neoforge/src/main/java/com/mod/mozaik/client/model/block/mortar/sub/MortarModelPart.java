package com.mod.mozaik.client.model.block.mortar.sub;

import com.mod.mozaik.Constants;
import com.mod.mozaik.client.GraphicsRenderHelper;
import com.mojang.math.Transformation;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MortarModelPart {
	public static @Nullable BakedModel bakeMortar(ResourceLocation mortar, Direction facing) {
		return new Unbaked(Constants.prefix("mozaik/block/" + mortar.getPath()), new MyModelState(facing)).bake(GraphicsRenderHelper.BAKER);
	}

	public record Unbaked(ResourceLocation modelLocation, MyModelState modelState) {
		public @Nullable BakedModel bake(ModelBaker baker) {
			UnbakedModel model = baker.getModel(this.modelLocation);
			return model.bake(baker, Material::sprite, this.modelState);
		}
	}

	public record MyModelState(Direction facing) implements ModelState {
		@Override
		public Transformation getRotation() {
			return new Transformation(new Matrix4f().rotate(this.facing.getRotation()));
		}
	}
}