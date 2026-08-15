package com.mod.mozaik.client.widgets;

import com.mod.mozaik.client.GraphicsRenderHelper;
import com.mod.mozaik.client.PhaseRenderable;
import com.mod.mozaik.client.screens.MortarScreen;
import com.mod.mozaik.polyomino.Polyomino;
import com.mod.mozaik.polyomino.Tessera;
import com.mod.mozaik.polyomino.TesseraMaterial;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.world.level.block.Rotation;
import org.joml.Vector2f;
import org.joml.Vector3i;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@NullMarked
public class HeldPolyominoWidget extends UnclickableWidget implements PhaseRenderable {
	public final MortarScreen screen;
	public Polyomino polyomino;

	public HeldPolyominoWidget(MortarScreen screen, int x, int y, Polyomino polyomino) {
		super(x, y, 0, 0);
		this.screen = screen;
		this.polyomino = polyomino;
	}

	public PolyominoWidget build(int x, int y) {
		return new PolyominoWidget(this.screen, this.getX(), this.getY(), new Polyomino.PlacedPolyomino(this.polyomino, x, y));
	}

	public void rotate(Rotation rotation) {
		List<Tessera.PlacedTessera> placedTessera = new ArrayList<>();

		this.placedTessera().forEach(voxel -> {
					Vector3i vec = new Vector3i(voxel.x(), 0, voxel.y());
					Vector3i rotated = rotation.rotation().rotate(vec);
					placedTessera.add(new Tessera.PlacedTessera(new Tessera(rotation == Rotation.CLOCKWISE_90 ? voxel.tessera().shape().clockWise() : voxel.tessera().shape().counterClockWise()), rotated.x(), rotated.z()));
				}
		);

		this.polyomino = new Polyomino(placedTessera, this.polyomino.material(), this.polyomino.seed());
	}

	public void mirror() {
		List<Tessera.PlacedTessera> placedTessera = new ArrayList<>();

		this.placedTessera().forEach(voxel ->
				placedTessera.add(new Tessera.PlacedTessera(new Tessera(voxel.tessera().shape().horizontalMirror()), voxel.x() * -1, voxel.y()))
		);

		this.polyomino = new Polyomino(placedTessera, this.polyomino.material(), this.polyomino.seed());
	}

	public void remove() {
		this.screen.removeWidget(this);
		this.screen.selected = null;
	}

	public List<Tessera.PlacedTessera> placedTessera() {
		return this.polyomino.placedTessera();
	}

	@Override
	public void renderAboveItems(GraphicsRenderHelper graphics) {
		Minecraft minecraft = Minecraft.getInstance();
		MouseHandler mouse = Objects.requireNonNull(minecraft).mouseHandler;
		float x = (float) mouse.xpos() * (float) minecraft.getWindow().getGuiScaledWidth() / (float) minecraft.getWindow().getScreenWidth();
		float y = (float) mouse.ypos() * (float) minecraft.getWindow().getGuiScaledHeight() / (float) minecraft.getWindow().getScreenHeight();

		Vector2f center = this.polyomino.getGridCenter();
		GridWidget square = this.screen.getTargetWidget(x, y);

		if (square == null) {
			renderVoxels(
					graphics,
					this.polyomino,
					TesseraMaterial.CANT_PLACE,
					x + (Tessera.TESSERA_SIZE * 0.1F) + (-center.x * Tessera.TESSERA_SIZE) + 1,
					y + (Tessera.TESSERA_SIZE * 0.1F) + (-center.y * Tessera.TESSERA_SIZE) + 1
			);
		} else renderVoxels(graphics, this.polyomino, TesseraMaterial.CAN_PLACE, square.getX(), square.getY());
	}

	@Override
	public void renderOnTop(GraphicsRenderHelper graphics) {
		Minecraft minecraft = Minecraft.getInstance();
		MouseHandler mouse = Objects.requireNonNull(minecraft).mouseHandler;
		float x = (float) mouse.xpos() * (float) minecraft.getWindow().getGuiScaledWidth() / (float) minecraft.getWindow().getScreenWidth();
		float y = (float) mouse.ypos() * (float) minecraft.getWindow().getGuiScaledHeight() / (float) minecraft.getWindow().getScreenHeight();

		Vector2f center = this.polyomino.getGridCenter();

		renderVoxels(
				graphics,
				this.polyomino,
				this.polyomino.material(),
				x + (Tessera.TESSERA_SIZE * 0.1F) + (-center.x * Tessera.TESSERA_SIZE),
				y + (Tessera.TESSERA_SIZE * 0.1F) + (-center.y * Tessera.TESSERA_SIZE)
		);
	}

	public static void renderVoxels(GraphicsRenderHelper graphics, Polyomino polyomino, TesseraMaterial material, float x, float y) {
		graphics.pushPop(() -> {
			graphics.translate(x, y);

			AtomicInteger index = new AtomicInteger(-1);
			polyomino.placedTessera().forEach(tessera -> graphics.pushPop(() -> {
				index.incrementAndGet();
				graphics.translate(
						tessera.x() * Tessera.TESSERA_SIZE,
						tessera.y() * Tessera.TESSERA_SIZE
				);

				graphics.blitTessera(material, tessera.tessera(), polyomino.seed(), index.get());
			}));
		});
	}
}
