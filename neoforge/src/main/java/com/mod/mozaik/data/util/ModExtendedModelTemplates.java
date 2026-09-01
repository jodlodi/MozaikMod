package com.mod.mozaik.data.util;

import com.mod.mozaik.util.FlatDirection;
import com.mod.mozaik.client.model.block.mortar.sub.TesseraModelPart;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.template.ExtendedModelTemplate;
import net.neoforged.neoforge.client.model.generators.template.ExtendedModelTemplateBuilder;
import org.jspecify.annotations.NullUnmarked;

@NullUnmarked
public class ModExtendedModelTemplates extends ModModelTemplates {
	public static final ExtendedModelTemplate MORTAR = ExtendedModelTemplateBuilder.builder()
			.parent(ResourceLocation.withDefaultNamespace("block/block"))
			.requiredTextureSlot(TextureSlot.PARTICLE)
			.requiredTextureSlot(TextureSlot.SIDE)
			.requiredTextureSlot(TextureSlot.BOTTOM)
			.requiredTextureSlot(TextureSlot.TOP)
			.element(elementBuilder ->
					elementBuilder.from(0.0F, 0.0F, 0.0F).to(16.0F, 15.5F, 16.0F)
							.face(Direction.UP, faceBuilder -> faceBuilder.texture(TextureSlot.TOP))
							.face(Direction.DOWN, faceBuilder -> faceBuilder.texture(TextureSlot.BOTTOM).cullface(Direction.DOWN))
							.face(Direction.NORTH, faceBuilder -> faceBuilder.texture(TextureSlot.SIDE).cullface(Direction.NORTH))
							.face(Direction.SOUTH, faceBuilder -> faceBuilder.texture(TextureSlot.SIDE).cullface(Direction.SOUTH))
							.face(Direction.WEST, faceBuilder -> faceBuilder.texture(TextureSlot.SIDE).cullface(Direction.WEST))
							.face(Direction.EAST, faceBuilder -> faceBuilder.texture(TextureSlot.SIDE).cullface(Direction.EAST))
			).build();

	public static ExtendedModelTemplate createTessera(int uv) {
		return ExtendedModelTemplateBuilder.builder()
				.parent(ResourceLocation.withDefaultNamespace("block/block"))
				.requiredTextureSlot(TextureSlot.TEXTURE)
				.element(elementBuilder ->
						elementBuilder
								.from(0.1F * TesseraModelPart.MyModelState.VOXEL_SIZE, 0.0F * TesseraModelPart.MyModelState.VOXEL_SIZE, 0.1F * TesseraModelPart.MyModelState.VOXEL_SIZE)
								.to(0.9F * TesseraModelPart.MyModelState.VOXEL_SIZE, 0.8F * TesseraModelPart.MyModelState.VOXEL_SIZE, 0.9F * TesseraModelPart.MyModelState.VOXEL_SIZE)
								.face(Direction.UP, faceBuilder -> faceBuilder.texture(TextureSlot.TEXTURE).uvs(uv, uv, uv + 1, uv + 1))
								/*.face(Direction.NORTH, faceBuilder -> faceBuilder.texture(TextureSlot.TEXTURE).uvs(uv, uv, uv + 1, uv + 1))
								.face(Direction.SOUTH, faceBuilder -> faceBuilder.texture(TextureSlot.TEXTURE).uvs(uv, uv, uv + 1, uv + 1))
								.face(Direction.WEST, faceBuilder -> faceBuilder.texture(TextureSlot.TEXTURE).uvs(uv, uv, uv + 1, uv + 1))
								.face(Direction.EAST, faceBuilder -> faceBuilder.texture(TextureSlot.TEXTURE).uvs(uv, uv, uv + 1, uv + 1))*/
				).build();
	}

	public static ExtendedModelTemplate createBridge(FlatDirection direction, int uv) {
		int relativeX = direction.getRelativeX();
		int relativeY = direction.getRelativeY();

		float xMin = (relativeX == 0 ? 0.1F : (relativeX == 1 ? 0.9F : 0.0F)) * TesseraModelPart.MyModelState.VOXEL_SIZE;
		float yMin = (relativeY == 0 ? 0.1F : (relativeY == 1 ? 0.9F : 0.0F)) * TesseraModelPart.MyModelState.VOXEL_SIZE;

		float xMax = (relativeX == 0 ? 0.9F : (relativeX == 1 ? 1.0F : 0.1F)) * TesseraModelPart.MyModelState.VOXEL_SIZE;
		float yMax = (relativeY == 0 ? 0.9F : (relativeY == 1 ? 1.0F : 0.1F)) * TesseraModelPart.MyModelState.VOXEL_SIZE;

		return ExtendedModelTemplateBuilder.builder()
				.parent(ResourceLocation.withDefaultNamespace("block/block"))
				.requiredTextureSlot(TextureSlot.TEXTURE)
				.element(elementBuilder ->
						elementBuilder.from(xMin, 0.0F, yMin).to(xMax, 8.0F, yMax)//east
								.face(Direction.UP, faceBuilder -> faceBuilder.texture(TextureSlot.TEXTURE).uvs(uv, uv, uv + 1, uv + 1))
								/*.face(Direction.NORTH, faceBuilder -> faceBuilder.texture(TextureSlot.TEXTURE).uvs(uv, uv, uv + 1, uv + 1))
								.face(Direction.SOUTH, faceBuilder -> faceBuilder.texture(TextureSlot.TEXTURE).uvs(uv, uv, uv + 1, uv + 1))
								.face(Direction.WEST, faceBuilder -> faceBuilder.texture(TextureSlot.TEXTURE).uvs(uv, uv, uv + 1, uv + 1))
								.face(Direction.EAST, faceBuilder -> faceBuilder.texture(TextureSlot.TEXTURE).uvs(uv, uv, uv + 1, uv + 1))*/
				).build();
	}
}
