package com.mod.mozaik.client.model.block.mortar.sub;

import com.mod.mozaik.polyomino.TesseraShape;
import com.mojang.math.Transformation;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.block.dispatch.ModelState;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ResolvedModel;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.geometry.QuadCollection;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.client.resources.model.sprite.TextureSlots;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Rotation;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Quaternionf;
import net.minecraft.MethodsReturnNonnullByDefault;
import javax.annotation.ParametersAreNonnullByDefault;
import org.jspecify.annotations.Nullable;

import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
// The model part representing a baked model
// useAmbientOcclusion and particleMaterial are implemented as part of the record
public record TesseraModelPart(QuadCollection quads, boolean useAmbientOcclusion,
							   Material.Baked particleMaterial) implements BlockStateModelPart {

	// Get the baked quads to render
	@Override
	public List<BakedQuad> getQuads(@Nullable Direction direction) {
		return this.quads.getQuads(direction);
	}

	// The flags of the materials backing the quads.
	@Override
	public int materialFlags() {
		return this.quads.materialFlags();
	}

	// The unbaked model that is read from the block state json
	public record Unbaked(ResourceLocation modelLocation, MyModelState modelState) implements BlockStateModelPart.Unbaked {

		@Override
		public void resolveDependencies(Resolver resolver) {
			// Mark any models used by the model part
			resolver.markDependency(this.modelLocation);
		}

		@Override
		public TesseraModelPart bake(ModelBaker baker) {
			// Get the model to bake
			ResolvedModel resolvedModel = baker.getModel(this.modelLocation);

			// Get the necessary settings for the model part
			TextureSlots slots = resolvedModel.getTopTextureSlots();
			boolean ao = resolvedModel.getTopAmbientOcclusion();
			Material.Baked particle = resolvedModel.resolveParticleMaterial(slots, baker);
			QuadCollection quads = resolvedModel.bakeTopGeometry(slots, baker, this.modelState);

			// Return the baked part
			return new TesseraModelPart(quads, ao, particle);
		}
	}

	// The model state used to apply the necessary transformations
	// If you are using an intermediate object to hold the model state, it must be transformable to a ModelState
	public record MyModelState(TesseraShape shape, Direction facing, int x, int y) implements ModelState {
		public static final float FACTOR = 1.0F;
		public static final float PIXEL = FACTOR / 16.0F;
		public static final float VOXEL_SIZE = 10.0F;
		public static final float SCALE = FACTOR / VOXEL_SIZE;
		public static final float HALF_BLOCK = 0.5F;
		public static final float HALF = PIXEL * 0.5F;

		@Override
		public Transformation transformation() {
			// Returns the model rotation to apply to the baking vertices
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
				case Rotation.COUNTERCLOCKWISE_90 -> new Quaternionf().rotationY(((float)Math.PI / 2F));
				case Rotation.NONE -> new Quaternionf();
				case Rotation.CLOCKWISE_90 -> new Quaternionf().rotationY(((float)Math.PI / 2F) * 3.0F);
				case Rotation.CLOCKWISE_180 -> new Quaternionf().rotationY(((float)Math.PI / 2F) * 2.0F);
			};
		}

		@Override
		public Matrix4fc faceTransformation(Direction direction) {
			// Returns the matrix that is applied to a given face on the model after the transformation
			// This is currently unused in Vanilla
			return NO_TRANSFORM;
		}

		@Override
		public Matrix4fc inverseFaceTransformation(Direction direction) {
			// Returns the inverse of faceTransformation that is applied to a given face on the model
			// This is passed to the FaceBakery
			return NO_TRANSFORM;
		}
	}
}