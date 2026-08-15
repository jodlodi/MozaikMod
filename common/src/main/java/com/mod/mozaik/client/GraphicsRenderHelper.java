package com.mod.mozaik.client;

import com.mod.mozaik.polyomino.Tessera;
import com.mod.mozaik.polyomino.TesseraMaterial;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.resources.Identifier;
import org.joml.Matrix3x2f;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@SuppressWarnings({"UnusedReturnValue"})
public class GraphicsRenderHelper {
	@Nullable
	public static ModelBaker BAKER;
	private final GuiGraphicsExtractor graphics;

	public GraphicsRenderHelper(GuiGraphicsExtractor graphics) {
		this.graphics = graphics;
	}

	public void blitTessera(TesseraMaterial material, Tessera tessera, long seed, int index) {
		this.blitScaled(fromMaterial(material, seed, index), tessera.getU(), tessera.getV(), 120, 40, Tessera.TESSERA_SIZE);
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
}
