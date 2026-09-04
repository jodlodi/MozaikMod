package com.mod.mozaik.client.model.block.mortar;

import com.mod.mozaik.blocks.MortarBlock;
import com.mod.mozaik.blocks.entities.NeoMortarBlockEntity;
import com.mod.mozaik.client.GraphicsRenderHelper;
import com.mod.mozaik.client.model.block.mortar.sub.MortarModelPart;
import com.mod.mozaik.client.model.block.mortar.sub.TesseraModelPart;
import com.mod.mozaik.menus.MortarMenu;
import com.mod.mozaik.polyomino.Polyomino;
import com.mod.mozaik.polyomino.Tessera;
import com.mod.mozaik.reg.ModRegistries;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.IDynamicBakedModel;
import net.neoforged.neoforge.client.model.data.ModelData;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@SuppressWarnings("deprecation")
public class MosaicDynamicBakedModel implements IDynamicBakedModel {
	private static final Material MISSING_TEXTURE = new Material(TextureAtlas.LOCATION_BLOCKS, MissingTextureAtlasSprite.getLocation());

	private final BlockModel base;
	private final ModelBaker baker;
	private final TextureAtlasSprite particle;
	private final ItemOverrides overrides;

	public MosaicDynamicBakedModel(BlockModel base, ModelBaker baker, TextureAtlasSprite particle, ItemOverrides overrides) {
		this.base = base;
		this.baker = baker;
		this.particle = particle;
		this.overrides = overrides;
	}

	@Override
	public boolean useAmbientOcclusion() {
		return this.base.hasAmbientOcclusion();
	}

	@Override
	public boolean isGui3d() {
		return this.base.customData.isGui3d();
	}

	@Override
	public boolean usesBlockLight() {
		return this.base.customData.useBlockLight();
	}

	@Override
	public TextureAtlasSprite getParticleIcon() {
		return this.particle;
	}

	@Override
	public ItemOverrides getOverrides() {
		return this.overrides;
	}

	@Override
	public boolean isCustomRenderer() {
		return false;
	}

	@Override
	public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand, ModelData data, @Nullable RenderType renderType) {
		List<BakedQuad> quads = new ArrayList<>();

		if (state == null) return quads;
		Direction facing = state.getValue(MortarBlock.FACING_ROTATED).getDirection();

		if (state.getBlock() instanceof MortarBlock) {
			BakedModel mortar = MortarModelPart.bakeMortar(this.baker, this.base, facing);
			quads.addAll(mortar.getQuads(state, side, rand, data, renderType));
		}

		List<Polyomino.PlacedPolyomino> input = data.get(NeoMortarBlockEntity.PROPERTY);
		if (input == null || input.isEmpty()) return quads;
		ClientLevel clientLevel = Minecraft.getInstance().level;
		if (clientLevel == null) return quads;

		List<Polyomino.PlacedPolyomino> copy = new ArrayList<>(input);
		List<Polyomino.PlacedPolyomino> list = new ArrayList<>();

		Rotation blockRotation = state.getValue(MortarBlock.FACING_ROTATED).getRotation();
		copy.forEach(placedPolyomino -> list.add(MortarMenu.rotate(placedPolyomino, blockRotation)));

		list.forEach((@Nullable Polyomino.PlacedPolyomino polyomino) -> {
			if (polyomino == null) return;
			int x = polyomino.x();
			int y = polyomino.y();

			int index = -1;
			for (Tessera.PlacedTessera tessera : polyomino.polyomino().placedTessera()) {
				index++;
				int fx = x + tessera.x();
				int fy = y + tessera.y();
				BakedModel bakedTessera = TesseraModelPart.bakeTessera(
						this.baker,
						Objects.requireNonNull(clientLevel.registryAccess().registry(ModRegistries.ModKeys.SHARD_MATERIAL).orElseThrow().get(polyomino.polyomino().material())),
						polyomino.polyomino().material().location().getPath(),
						facing,
						fx,
						fy,
						polyomino.polyomino().uuid().getMostSignificantBits(),
						index,
						tessera.tessera().shape()
				);

				if (bakedTessera == null) continue;
				quads.addAll(bakedTessera.getQuads(state, side, rand, data, renderType));
			}
		});
		return quads;
	}
}
