package com.mod.mozaik.client;

import com.mod.mozaik.Constants;
import com.mod.mozaik.FlatDirection;
import com.mod.mozaik.TesseraMaterial;
import com.mod.mozaik.client.buttons.VoxelButton;
import com.mod.mozaik.mixin.GuiGraphicsExtractorMixin;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ResolvedModel;
import net.minecraft.resources.Identifier;
import org.joml.Matrix3x2f;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.HashSet;
import java.util.List;
import java.util.function.Predicate;

@NullMarked
@SuppressWarnings({"UnusedReturnValue"})
public class GraphicsRenderHelper {
	@Nullable
	public static ModelBaker BAKER;
	private final GuiGraphicsExtractor graphics;

	public GraphicsRenderHelper(GuiGraphicsExtractor graphics) {
		this.graphics = graphics;
	}

	public void blitTessera(TesseraMaterial material, List<FlatDirection> connections, long seed, int index) {
		TileMapUVGetter getter = TileMapUVGetter.get(connections);
		this.blitScaled(fromMaterial(material, seed, index), getter.u, getter.v, 120, 40, VoxelButton.TESSERA_SIZE);
	}

	public static Identifier fromMaterial(TesseraMaterial material, long seed, int index) {
		return material.getGuiSheet(seed, index);
	}

	public void blitScaled(Identifier texture, int u, int v, int textureWidth, int textureHeight, int scale) {
		this.blit(texture, u * scale, v * scale, scale, scale, textureWidth, textureHeight);
	}

	public void blit(Identifier texture, int u, int v, int width, int height, int textureWidth, int textureHeight) {
		this.graphics.blitSprite(RenderPipelines.GUI_TEXTURED, texture, textureWidth, textureHeight, u, v, 0, 0, width, height);
	}

	public void pushPop(Runnable runnable) {
		this.graphics.pose().pushMatrix();
		runnable.run();
		this.graphics.pose().popMatrix();
	}

	public Matrix3x2f translate(float x, float y) {
		return this.graphics.pose().translate(x, y);
	}

	public Matrix3x2f scale(float x, float y) {
		return this.graphics.pose().scale(x, y);
	}

	private enum TileMapUVGetter {
		U(0, 2, FlatDirection.UP),
		R(1, 3, FlatDirection.RIGHT),
		D(0, 0, FlatDirection.DOWN),
		L(3, 3, FlatDirection.LEFT),

		U_D(0, 1, FlatDirection.UP, FlatDirection.DOWN),
		L_R(2, 3, FlatDirection.LEFT, FlatDirection.RIGHT),

		R_D(1, 0, FlatDirection.RIGHT, FlatDirection.DOWN),
		L_D(3, 0, FlatDirection.LEFT, FlatDirection.DOWN),
		R_U(1, 2, FlatDirection.RIGHT, FlatDirection.UP),
		L_U(3, 2, FlatDirection.LEFT, FlatDirection.UP),

		R_D_DR(8, 0, FlatDirection.RIGHT, FlatDirection.DOWN, FlatDirection.DOWN_RIGHT),
		L_D_DL(11, 0, FlatDirection.LEFT, FlatDirection.DOWN, FlatDirection.DOWN_LEFT),
		R_U_UR(8, 3, FlatDirection.RIGHT, FlatDirection.UP, FlatDirection.UP_RIGHT),
		L_U_UL(11, 3, FlatDirection.LEFT, FlatDirection.UP, FlatDirection.UP_LEFT),

		L_R_D(2, 0, FlatDirection.LEFT, FlatDirection.RIGHT, FlatDirection.DOWN),
		L_R_U(2, 2, FlatDirection.LEFT, FlatDirection.RIGHT, FlatDirection.UP),
		U_D_R(1, 1, FlatDirection.UP, FlatDirection.DOWN, FlatDirection.RIGHT),
		U_D_L(3, 1, FlatDirection.UP, FlatDirection.DOWN, FlatDirection.LEFT),

		L_R_D_DR(5, 0, FlatDirection.LEFT, FlatDirection.RIGHT, FlatDirection.DOWN, FlatDirection.DOWN_RIGHT),
		L_R_D_DL(6, 0, FlatDirection.LEFT, FlatDirection.RIGHT, FlatDirection.DOWN, FlatDirection.DOWN_LEFT),
		L_R_U_UR(5, 3, FlatDirection.LEFT, FlatDirection.RIGHT, FlatDirection.UP, FlatDirection.UP_RIGHT),
		L_R_U_UL(6, 3, FlatDirection.LEFT, FlatDirection.RIGHT, FlatDirection.UP, FlatDirection.UP_LEFT),
		U_D_R_DR(4, 1, FlatDirection.UP, FlatDirection.DOWN, FlatDirection.RIGHT, FlatDirection.DOWN_RIGHT),
		U_D_R_UR(4, 2, FlatDirection.UP, FlatDirection.DOWN, FlatDirection.RIGHT, FlatDirection.UP_RIGHT),
		U_D_L_DL(7, 1, FlatDirection.UP, FlatDirection.DOWN, FlatDirection.LEFT, FlatDirection.DOWN_LEFT),
		U_D_L_UL(7, 2, FlatDirection.UP, FlatDirection.DOWN, FlatDirection.LEFT, FlatDirection.UP_LEFT),

		L_R_D_DR_DL(10, 0, FlatDirection.LEFT, FlatDirection.RIGHT, FlatDirection.DOWN, FlatDirection.DOWN_RIGHT, FlatDirection.DOWN_LEFT),
		L_R_U_UR_UL(9, 3, FlatDirection.LEFT, FlatDirection.RIGHT, FlatDirection.UP, FlatDirection.UP_RIGHT, FlatDirection.UP_LEFT),
		U_D_R_UR_DR( 8, 1, FlatDirection.UP, FlatDirection.DOWN, FlatDirection.RIGHT, FlatDirection.UP_RIGHT, FlatDirection.DOWN_RIGHT),
		U_D_L_UL_DL(11, 2, FlatDirection.UP, FlatDirection.DOWN, FlatDirection.LEFT, FlatDirection.UP_LEFT, FlatDirection.DOWN_LEFT),

		PLUS(2, 1, FlatDirection.UP, FlatDirection.DOWN, FlatDirection.RIGHT, FlatDirection.LEFT),

		PLUS_UL(4, 0, FlatDirection.UP, FlatDirection.DOWN, FlatDirection.RIGHT, FlatDirection.LEFT, FlatDirection.UP_LEFT),
		PLUS_UR(7, 0, FlatDirection.UP, FlatDirection.DOWN, FlatDirection.RIGHT, FlatDirection.LEFT, FlatDirection.UP_RIGHT),
		PLUS_DL(4, 0, FlatDirection.UP, FlatDirection.DOWN, FlatDirection.RIGHT, FlatDirection.LEFT, FlatDirection.DOWN_LEFT),
		PLUS_DR(7, 0, FlatDirection.UP, FlatDirection.DOWN, FlatDirection.RIGHT, FlatDirection.LEFT, FlatDirection.DOWN_RIGHT),

		PLUS_UL_UR(10, 3, FlatDirection.UP, FlatDirection.DOWN, FlatDirection.RIGHT, FlatDirection.LEFT, FlatDirection.UP_LEFT, FlatDirection.UP_RIGHT),
		PLUS_UR_DR(8, 2, FlatDirection.UP, FlatDirection.DOWN, FlatDirection.RIGHT, FlatDirection.LEFT, FlatDirection.UP_RIGHT, FlatDirection.DOWN_RIGHT),
		PLUS_DL_DR(9, 0, FlatDirection.UP, FlatDirection.DOWN, FlatDirection.RIGHT, FlatDirection.LEFT, FlatDirection.DOWN_LEFT, FlatDirection.DOWN_RIGHT),
		PLUS_DL_UL(11, 1, FlatDirection.UP, FlatDirection.DOWN, FlatDirection.RIGHT, FlatDirection.LEFT, FlatDirection.DOWN_LEFT, FlatDirection.UP_LEFT),

		PLUS_NUL(5, 1, list -> list.size() == 7 && !new HashSet<>(list).contains(FlatDirection.UP_LEFT)),
		PLUS_NUR(6, 1, list -> list.size() == 7 && !new HashSet<>(list).contains(FlatDirection.UP_RIGHT)),
		PLUS_NDL(5, 2, list -> list.size() == 7 && !new HashSet<>(list).contains(FlatDirection.DOWN_LEFT)),
		PLUS_NDR(6, 2, list -> list.size() == 7 && !new HashSet<>(list).contains(FlatDirection.DOWN_RIGHT)),

		PLUS_NUL_NDR(9, 1, list -> list.size() == 6 && !new HashSet<>(list).contains(FlatDirection.UP_LEFT) && !new HashSet<>(list).contains(FlatDirection.DOWN_RIGHT)),
		PLUS_NUR_NDL(10, 2, list -> list.size() == 6 && !new HashSet<>(list).contains(FlatDirection.UP_RIGHT) && !new HashSet<>(list).contains(FlatDirection.DOWN_LEFT)),

		TESSERA(0, 3, List::isEmpty),
		FULL(9, 2, list -> list.size() == 8),
		BLANK(10, 1, _ -> false);

		private final int u;
		private final int v;
		private final Predicate<List<FlatDirection>> check;

		TileMapUVGetter(int u, int v, Predicate<List<FlatDirection>> check) {
			this.u = u;
			this.v = v;
			this.check = check;
		}

		TileMapUVGetter(int u, int v, FlatDirection... hasThis) {
			this(u, v, list -> list.size() == hasThis.length && new HashSet<>(list).containsAll(List.of(hasThis)));
		}

		public static TileMapUVGetter get(List<FlatDirection> flatDirections) {
			for (TileMapUVGetter getter : TileMapUVGetter.values()) {
				if (getter.check.test(flatDirections)) return getter;
			}
			return BLANK;
		}
	}
}
