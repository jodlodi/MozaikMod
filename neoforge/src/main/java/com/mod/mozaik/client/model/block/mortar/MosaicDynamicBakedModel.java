package com.mod.mozaik.client.model.block.mortar;

import com.mod.mozaik.Constants;
import com.mod.mozaik.blocks.MortarBlock;
import com.mod.mozaik.client.GraphicsRenderHelper;
import com.mod.mozaik.client.model.block.mortar.sub.MortarModelPart;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.IDynamicBakedModel;
import net.neoforged.neoforge.client.model.data.ModelData;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@SuppressWarnings("deprecation")
public class MosaicDynamicBakedModel implements IDynamicBakedModel {
	private static final Material MISSING_TEXTURE = new Material(TextureAtlas.LOCATION_BLOCKS, MissingTextureAtlasSprite.getLocation());

	private final boolean useAmbientOcclusion;
	private final boolean isGui3d;
	private final boolean usesBlockLight;
	private final TextureAtlasSprite particle;
	private final ItemOverrides overrides;

	public MosaicDynamicBakedModel(boolean useAmbientOcclusion, boolean isGui3d, boolean usesBlockLight, TextureAtlasSprite particle, ItemOverrides overrides) {
		this.useAmbientOcclusion = useAmbientOcclusion;
		this.isGui3d = isGui3d;
		this.usesBlockLight = usesBlockLight;
		this.particle = particle;
		this.overrides = overrides;
	}

	@Override
	public boolean useAmbientOcclusion() {
		return this.useAmbientOcclusion;
	}

	@Override
	public boolean isGui3d() {
		return this.isGui3d;
	}

	@Override
	public boolean usesBlockLight() {
		return this.usesBlockLight;
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
	public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand, ModelData extraData, @Nullable RenderType renderType) {
		List<BakedQuad> quads = new ArrayList<>();

		if (state == null) return quads;
		if (GraphicsRenderHelper.BAKER == null) return quads;

		if (state.getBlock() instanceof MortarBlock block) {
			DyeColor dye = block.getColor();
			ResourceLocation model = Constants.prefix(dye.getSerializedName() + "_mortar");
			Direction facing = state.getValue(MortarBlock.FACING_ROTATED).getDirection();

			BakedModel mortar = MortarModelPart.bakeMortar(model, facing);
			if (mortar == null) return quads;
			else return mortar.getQuads(state, side, rand, extraData, renderType);
		}/*


		parts.add(this.mortarMap.get(facing));

		if (GraphicsRenderHelper.BAKER == null) return;

		ModelData data = level.getModelData(pos);
		List<Polyomino.PlacedPolyomino> input = data.get(NeoMortarBlockEntity.PROPERTY);
		if (input == null) return;
		List<Polyomino.PlacedPolyomino> copy = new ArrayList<>(input);
		List<Polyomino.PlacedPolyomino> list = new ArrayList<>();

		Rotation blockRotation = state.getValue(MortarBlock.FACING_ROTATED).getRotation();
		copy.forEach(placedPolyomino -> list.add(MortarMenu.rotate(placedPolyomino, blockRotation)));

		ClientLevel clientLevel = Minecraft.getInstance().level;
		if (clientLevel == null) return;

		list.forEach((@org.jetbrains.annotations.Nullable Polyomino.PlacedPolyomino polyomino) -> {
			if (polyomino == null) return;
			int x = polyomino.x();
			int y = polyomino.y();


			int index = -1;
			for (Tessera.PlacedTessera tessera : polyomino.polyomino().placedTessera()) {
				index++;
				int fx = x + tessera.x();
				int fy = y + tessera.y();
				parts.add(TesseraModelPart.bakeTessera(clientLevel.registryAccess().get(polyomino.polyomino().material()).orElseThrow().value(), polyomino.polyomino().material().identifier().getPath(), facing, fx, fy, polyomino.polyomino().uuid().getMostSignificantBits(), index, tessera.tessera().shape()));
			}
		});*/
		return quads;
	}
}
