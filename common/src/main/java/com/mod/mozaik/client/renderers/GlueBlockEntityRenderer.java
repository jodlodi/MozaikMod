package com.mod.mozaik.client.renderers;

import com.mod.mozaik.*;
import com.mod.mozaik.blocks.MortarBlock;
import com.mod.mozaik.blocks.entities.MortarBlockEntity;
import com.mod.mozaik.client.widgets.PolyominoWidget;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mojang.math.Transformation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.util.Util;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@NullMarked
public class GlueBlockEntityRenderer implements BlockEntityRenderer<MortarBlockEntity, GlueBlockEntityRenderer.GlueBlockEntityRenderState> {
	private static final Map<Direction, Transformation> TRANSFORMATIONS = Util.makeEnumMap(Direction.class, GlueBlockEntityRenderer::createModelTransform);
	private static final RenderType RENDER_TYPE = RenderTypes.entityTranslucent(Constants.prefix("textures/mural/voxel.png"));
	public static final float PIXEL = 1.0F / 16.0F;
	public static final float HALF = PIXEL * 0.5F;
	public static final float VOXEL_SIZE = 0.8F;

	public final ModelPart voxel;
	public final ModelPart bridge;
	public final ModelPart corner;

	public GlueBlockEntityRenderer(BlockEntityRendererProvider.Context ignoredContext) {
		MeshDefinition definition = new MeshDefinition();
		PartDefinition root = definition.getRoot();

		this.voxel = this.createMesh(definition, root, "voxel", -VOXEL_SIZE * 0.5F, -VOXEL_SIZE * 0.5F, VOXEL_SIZE, VOXEL_SIZE);
		this.bridge = this.createMesh(definition, root, "bridge", -VOXEL_SIZE * 0.5F, VOXEL_SIZE * 0.5F, VOXEL_SIZE, (1.0F - VOXEL_SIZE) * 0.5F);
		this.corner = this.createMesh(definition, root, "corner", VOXEL_SIZE * 0.5F, VOXEL_SIZE * 0.5F, (1.0F - VOXEL_SIZE) * 0.5F, (1.0F - VOXEL_SIZE) * 0.5F);
	}

	private ModelPart createMesh(MeshDefinition definition, PartDefinition root, String name, float x0, float y0, float x1, float y1) {
		root.addOrReplaceChild(
				name, CubeListBuilder.create()
						.texOffs(128, 128)
						.addBox(
								x0, -0.5F, y0, x1, 1.0F, y1
						), PartPose.ZERO
		);

		return LayerDefinition.create(definition, 256, 256).bakeRoot();
	}

	private static Transformation createModelTransform(final Direction direction) {
		return new Transformation((new Matrix4f())
				.translation(0.5F, 0.5F, 0.5F)
				.rotate(direction.getRotation())
				.translate(-0.5F, 0.5F, -0.5F)
				.translate(HALF, -HALF, HALF));
	}

	public static Transformation modelTransform(final Direction direction) {
		return TRANSFORMATIONS.get(direction);
	}

	@Override
	public GlueBlockEntityRenderState createRenderState() {
		return new GlueBlockEntityRenderState();
	}

	@Override
	public void extractRenderState(MortarBlockEntity blockEntity, GlueBlockEntityRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.@org.jspecify.annotations.Nullable CrumblingOverlay breakProgress) {
		BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTicks, cameraPosition, breakProgress);
		state.polyominos.clear();
		state.polyominos.addAll(blockEntity.getPolyominos());
		state.direction = blockEntity.getBlockState().getValue(MortarBlock.FACING);
		state.lightCoords = blockEntity.getLevel() != null ? LightCoordsUtil.getLightCoords(blockEntity.getLevel(), blockEntity.getBlockPos().relative(state.direction)) : 15728880;
	}

	@Override
	public void submit(GlueBlockEntityRenderState state, PoseStack stack, SubmitNodeCollector nodeCollector, CameraRenderState cameraRenderState) {
		if (true) return;
		stack.pushPose();
		stack.mulPose(modelTransform(state.direction));

		for (Polyomino.PlainPolyomino polyomino : state.polyominos) {
			stack.pushPose();
			stack.translate(polyomino.gridX() * PIXEL, 0.0F, polyomino.gridY() * PIXEL);

			AtomicInteger index = new AtomicInteger(-1);
			for (Voxel.PlainVoxel voxel : polyomino.allVoxels()) {
				index.incrementAndGet();
				stack.pushPose();
				stack.translate(voxel.relativeX() * PIXEL, 0.0F, voxel.relativeY() * PIXEL);

				for (FlatDirection direction : FlatDirection.cardinalClockwise()) {
					if (PolyominoWidget.checkConnection(polyomino, voxel, direction).isPresent()) {
						stack.pushPose();
						stack.mulPose(Axis.YP.rotationDegrees(direction.getAngle()));
						render(nodeCollector, stack, this.bridge, state.lightCoords, TesseraMaterial.values()[polyomino.color()].getColor(polyomino.seed(), index.get()));
						stack.popPose();
					}
				}

				for (FlatDirection direction : FlatDirection.subClockwise()) {
					if (PolyominoWidget.checkConnection(polyomino, voxel, direction).isPresent()) {
						boolean shouldExist = true;
						for (FlatDirection related : direction.getRelated()) {
							if (PolyominoWidget.checkConnection(polyomino, voxel, related).isEmpty())
								shouldExist = false;
						}
						if (!shouldExist) continue;
						stack.pushPose();
						stack.mulPose(Axis.YP.rotationDegrees(direction.counterClockWise(1).getAngle()));
						render(nodeCollector, stack, this.corner, state.lightCoords, TesseraMaterial.values()[polyomino.color()].getColor(polyomino.seed(), index.get()));
						stack.popPose();
					}
				}

				render(nodeCollector, stack, this.voxel, state.lightCoords, TesseraMaterial.values()[polyomino.color()].getColor(polyomino.seed(), index.get()));
				stack.popPose();
			}
			stack.popPose();
		}
		stack.popPose();
	}

	public static void render(SubmitNodeCollector nodeCollector, PoseStack stack, ModelPart modelPart, int lightCoords, int color) {
		nodeCollector.submitModelPart(modelPart, stack, RENDER_TYPE, lightCoords, OverlayTexture.NO_OVERLAY, null, color, null, 0);
	}

	public static class GlueBlockEntityRenderState extends BlockEntityRenderState {
		private final List<Polyomino.PlainPolyomino> polyominos = new ArrayList<>();
		private Direction direction = Direction.UP;
	}
}
