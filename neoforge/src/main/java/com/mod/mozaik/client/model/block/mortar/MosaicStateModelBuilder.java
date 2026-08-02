package com.mod.mozaik.client.model.block.mortar;

import net.minecraft.client.renderer.block.dispatch.VariantMutator;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.model.generators.blockstate.CustomBlockStateModelBuilder;
import net.neoforged.neoforge.client.model.generators.blockstate.UnbakedMutator;
import org.jspecify.annotations.NullMarked;

@NullMarked
// The builder used to construct the block state JSON
public class MosaicStateModelBuilder extends CustomBlockStateModelBuilder {
	private final Identifier model;

	public MosaicStateModelBuilder(Identifier model) {
		this.model = model;
	}

	// Add fields and setters for the fields here. The fields can then be used below.

	@Override
	public MosaicStateModelBuilder with(VariantMutator variantMutator) {
		// If you want to apply any mutators that assumes your unbaked model part is a `Variant`
		// If not, this should do nothing
		return this;
	}

	// This is for generalized unbaked blockstate models
	@Override
	public MosaicStateModelBuilder with(UnbakedMutator unbakedMutator) {
		return this;
	}

	// Converts the builder to its unbaked variant to encode
	@Override
	public MosaicStateModel.Unbaked toUnbaked() {
		return new MosaicStateModel.Unbaked(this.model);
	}
}