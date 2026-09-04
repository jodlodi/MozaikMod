package com.mod.mozaik.client.model.block.mortar.sub;

import com.mod.mozaik.client.GraphicsRenderHelper;
import com.mojang.math.Transformation;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.Direction;
import org.joml.Matrix4f;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MortarModelPart {
	public static BakedModel bakeMortar(ModelBaker baker, BlockModel base, Direction facing) {
		return new Unbaked(base, new MyModelState(facing)).bake(baker);
	}

	@SuppressWarnings("deprecation")
	public record Unbaked(BlockModel model, MyModelState modelState) {
		public BakedModel bake(ModelBaker baker) {

			UnbakedModel unbakedModel = new BlockModel(
					this.model.getParentLocation(),
					this.model.getElements(),
					this.model.textureMap,
					this.model.hasAmbientOcclusion,
					this.model.getGuiLight(),
					this.model.getTransforms(),
					this.model.getOverrides()
			);

			return unbakedModel.bake(baker, Material::sprite, this.modelState);
		}
	}

	public record MyModelState(Direction facing) implements ModelState {
		@Override
		public Transformation getRotation() {
			return new Transformation(new Matrix4f().rotate(this.facing.getRotation()));
		}
	}
}