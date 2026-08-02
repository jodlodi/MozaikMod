package com.mod.mozaik.client;

import com.mod.mozaik.client.buttons.VoxelButton;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import org.joml.Matrix3x2f;
import org.jspecify.annotations.NullMarked;

@NullMarked
@SuppressWarnings({"UnusedReturnValue"})
public class GraphicsRenderHelper {
	private final GuiGraphicsExtractor graphics;

	public GraphicsRenderHelper(GuiGraphicsExtractor graphics) {
		this.graphics = graphics;
	}

	public void blitTessera(Identifier texture, int u, int v, int width, int height, int textureWidth, int textureHeight) {
		this.blitScaled(texture, u, v, width, height, textureWidth, textureHeight, VoxelButton.TESSERA_SIZE);
	}

	public void blitScaled(Identifier texture, int u, int v, int width, int height, int textureWidth, int textureHeight, int scale) {
		this.blit(texture, u * scale, v * scale, width * scale, height * scale, textureWidth * scale, textureHeight * scale);
	}

	public void blit(Identifier texture, int u, int v, int width, int height, int textureWidth, int textureHeight) {
		this.graphics.blit(RenderPipelines.GUI_TEXTURED, texture, 0, 0, u, v, width, height, textureWidth, textureHeight);
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
