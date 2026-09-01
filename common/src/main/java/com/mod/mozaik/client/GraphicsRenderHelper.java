package com.mod.mozaik.client;

import com.mod.mozaik.polyomino.ShardMaterial;
import com.mod.mozaik.polyomino.Tessera;
import com.mod.mozaik.reg.ModRegistries;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@SuppressWarnings({"UnusedReturnValue"})
public class GraphicsRenderHelper {
	@Nullable
	public static ModelBaker BAKER;
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
		this.graphics.blitSprite(texture, textureWidth, textureHeight, u, v, 0, 0, width, height, color);
	}

	public void pushPop(Runnable runnable) {
		this.graphics.pose().pushPose();
		runnable.run();
		this.graphics.pose().popPose();
	}

	public void translate(float x, float y) {
		this.graphics.pose().translate(x, 1.0F, y);
	}

	public void scale(float x, float y) {
		this.graphics.pose().scale(x, 1.0F, y);
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
}
