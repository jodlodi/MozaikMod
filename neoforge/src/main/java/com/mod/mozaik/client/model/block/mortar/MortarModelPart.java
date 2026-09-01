package com.mod.mozaik.client.model.block.mortar;

import com.mojang.math.Transformation;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.block.dispatch.ModelState;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ResolvableModel;
import net.minecraft.client.resources.model.ResolvedModel;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.geometry.QuadCollection;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.client.resources.model.sprite.TextureSlots;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import net.minecraft.MethodsReturnNonnullByDefault;
import javax.annotation.ParametersAreNonnullByDefault;
import org.jspecify.annotations.Nullable;

import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
// The model part representing a baked model
// useAmbientOcclusion and particleMaterial are implemented as part of the record
public record MortarModelPart(QuadCollection quads, boolean useAmbientOcclusion,
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
		public void resolveDependencies(ResolvableModel.Resolver resolver) {
			// Mark any models used by the model part
			resolver.markDependency(this.modelLocation);
		}

		@Override
		public MortarModelPart bake(ModelBaker baker) {
			// Get the model to bake
			ResolvedModel resolvedModel = baker.getModel(this.modelLocation);

			// Get the necessary settings for the model part
			TextureSlots slots = resolvedModel.getTopTextureSlots();
			boolean ao = resolvedModel.getTopAmbientOcclusion();
			Material.Baked particle = resolvedModel.resolveParticleMaterial(slots, baker);
			QuadCollection quads = resolvedModel.bakeTopGeometry(slots, baker, this.modelState);

			// Return the baked part
			return new MortarModelPart(quads, ao, particle);
		}
	}

	// The model state used to apply the necessary transformations
	// If you are using an intermediate object to hold the model state, it must be transformable to a ModelState
	public record MyModelState(Direction facing) implements ModelState {
		// Used for the unbaked block model part
		public static final MapCodec<MyModelState> CODEC = RecordCodecBuilder.mapCodec(
				instance -> instance.group(
						Direction.CODEC.fieldOf("facing").forGetter(MyModelState::facing)
				).apply(instance, MyModelState::new)
		);


		@Override
		public Transformation transformation() {
			// Returns the model rotation to apply to the baking vertices
			return new Transformation((new Matrix4f())
					.rotate(this.facing.getRotation()));
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