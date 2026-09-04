package com.mod.mozaik.client;

import com.mod.mozaik.polyomino.ShardMaterial;
import com.mod.mozaik.polyomino.Tessera;
import com.mod.mozaik.reg.ModRegistries;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@SuppressWarnings({"UnusedReturnValue"})
public class GraphicsRenderHelper {
	private final GuiGraphics graphics;
	private final RegistryAccess.Frozen registryAccess;

	public GraphicsRenderHelper(GuiGraphics graphics) {
		this.graphics = graphics;
		this.registryAccess = Objects.requireNonNull(Minecraft.getInstance().getConnection()).registryAccess();
	}

	public void blitTessera(ResourceKey<ShardMaterial> material, Tessera tessera, long seed, int index, int color) {
		this.blitScaled(fromMaterial(material, seed, index), tessera.getU(), tessera.getV(), 120, 40, Tessera.TESSERA_SIZE, color);
	}

	public ResourceLocation fromMaterial(ResourceKey<ShardMaterial> material, long seed, int index) {
		return this.registryAccess.registry(ModRegistries.ModKeys.SHARD_MATERIAL).orElseThrow().get(material).getGuiSheet(material.location().getPath(), seed, index);
	}

	public void blitScaled(ResourceLocation texture, int u, int v, int textureWidth, int textureHeight, int scale, int color) {
		this.blit(texture, u * scale, v * scale, scale, scale, textureWidth, textureHeight, color);
	}

	public void blit(ResourceLocation texture, int u, int v, int width, int height, int textureWidth, int textureHeight, int color) {
		int alpha = (color >> 24) & 0xFF;
		int red = (color >> 16) & 0xFF;
		int green = (color >> 8) & 0xFF;
		int blue = color & 0xFF;

		ResourceLocation location = texture;

		RenderSystem.enableBlend();
		this.graphics.setColor(red / 255.0F, green / 255.0F, blue / 255.0F, alpha / 255.0F);
		this.graphics.blit(location, 0, 0, 0, u, v, width, height, textureWidth, textureHeight);
		this.graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.disableBlend();
	}

	public void pushPop(Runnable runnable) {
		this.graphics.pose().pushPose();
		runnable.run();
		this.graphics.pose().popPose();
	}

	public void translate(float x, float y) {
		this.graphics.pose().translate(x, y, 0.0F);
	}

	public void scale(float x, float y) {
		this.graphics.pose().scale(x, y, 0.0F);
	}

	public void fill(int x0, int y0, int x1, int y1, int col) {
		this.graphics.fill(x0, y0, x1, y1, col);
	}

	public void selection(int x0, int y0, int x1, int y1) {
		long time = Objects.requireNonNull(Minecraft.getInstance().level).getGameTime();
		int xMin = Math.min(x0, x1);
		int xMax = Math.max(x0, x1);
		int yMin = Math.min(y0, y1);
		int yMax = Math.max(y0, y1);

		for (int x = xMin; x < xMax; x++) {
			for (int y = yMin; y < yMax; y++) {
				if ((x + y) % 2 == (time / 10) % 2) {
					this.graphics.fill(x, y, x + 1, y + 1, 0xFFFFFFFF);
				}
			}
		}
	}

	public static void blit(GuiGraphics graphics, ResourceLocation atlasLocation, int x, int y, int textureWidth, int textureHeight, int color) {
		int alpha = (color >> 24) & 0xFF;
		int red = (color >> 16) & 0xFF;
		int green = (color >> 8) & 0xFF;
		int blue = color & 0xFF;

		RenderSystem.enableBlend();
		graphics.setColor(red / 255.0F, green / 255.0F, blue / 255.0F, alpha / 255.0F);
		graphics.blit(atlasLocation, x, y, 0, 0, 0, textureWidth, textureHeight, textureWidth, textureHeight);
		graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.disableBlend();
	}
}
