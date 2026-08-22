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
import org.joml.Vector2i;
import org.joml.Vector3i;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@NullMarked
public class HeldPolyominoWidget extends UnclickableWidget implements PhaseRenderable {
	public final MortarScreen screen;
	private Polyomino.PlacedPolyomino polyomino;

	public HeldPolyominoWidget(MortarScreen screen, int x, int y, Polyomino.PlacedPolyomino polyomino) {
		super(x, y, 0, 0);
		this.screen = screen;
		this.polyomino = polyomino;
	}

	public HeldPolyominoWidget(MortarScreen screen, int x, int y, Polyomino polyomino) {
		this(screen, x, y, new Polyomino.PlacedPolyomino(polyomino, 0, 0));
	}

	public void setPolyomino(Polyomino.PlacedPolyomino polyomino) {
		this.polyomino = polyomino;
	}

	public Polyomino getPolyomino() {
		return this.polyomino.polyomino();
	}

	public void setPolyomino(Polyomino polyomino) {
		this.polyomino = new Polyomino.PlacedPolyomino(polyomino, this.getGridX(), this.getGridY());
	}

	public int getGridX() {
		return this.polyomino.x();
	}

	public void setGridX(int gridX) {
		this.polyomino = new Polyomino.PlacedPolyomino(this.polyomino.polyomino(), gridX, this.getGridY());
	}

	public int getGridY() {
		return this.polyomino.y();
	}

	public void setGridY(int gridY) {
		this.polyomino = new Polyomino.PlacedPolyomino(this.polyomino.polyomino(), this.getGridX(), gridY);
	}

	public Polyomino.PlacedPolyomino rotate(Rotation rotation) {
		List<Tessera.PlacedTessera> placedTessera = new ArrayList<>();

		this.placedTessera().forEach(voxel -> {
					Vector3i vec = new Vector3i(voxel.x(), 0, voxel.y());
					Vector3i rotated = rotation.rotation().rotate(vec);
					placedTessera.add(new Tessera.PlacedTessera(new Tessera(voxel.tessera().shape().rotate(rotation)), rotated.x(), rotated.z()));
				}
		);

		Vector3i vec = new Vector3i(this.getGridX(), 0, this.getGridY());
		Vector3i rotated = rotation.rotation().rotate(vec);

		return new Polyomino.PlacedPolyomino(
				new Polyomino(placedTessera, this.getPolyomino().material(), this.getPolyomino().seed()),
				rotated.x(),
				rotated.z()
		);
	}

	public List<Tessera.PlacedTessera> placedTessera() {
		return this.getPolyomino().placedTessera();
	}

	@Override
	public void renderAboveItems(GraphicsRenderHelper graphics) {
		if (this.screen.carried.getFirst() != this) return;
		Map<HeldPolyominoWidget, Vector2i> map = this.screen.getOffsetForPlacement(this.screen.carried);

		if (map == null) {
			for (HeldPolyominoWidget widget : this.screen.carried) {
				Vector2f center = widget.heldPos();
				renderVoxels(
						graphics,
						widget.getPolyomino(),
						widget.getPolyomino().material(),
						center.x(),
						center.y(),
						0x67222222
				);
			}
		} else {
			for (HeldPolyominoWidget widget : this.screen.carried) {
				Vector2i square = map.get(widget);
				square = this.screen.getGridPos(square);
				renderVoxels(graphics, widget.getPolyomino(), widget.getPolyomino().material(), square.x(), square.y(), 0x77FFFFFF);
			}
		}
	}

	@Override
	public void renderOnTop(GraphicsRenderHelper graphics) {
		Vector2f center = this.heldPos();

		renderVoxels(
				graphics,
				this.getPolyomino(),
				this.getPolyomino().material(),
				center.x(),
				center.y()
		);
	}

	public Vector2f heldPos() {
		Vector2f held = new Vector2f();

		for (HeldPolyominoWidget widget : this.screen.carried) {
			held.add(widget.offsetPos());
		}
		held.div(this.screen.carried.size());

		return held.add(this.getGridX() * Tessera.TESSERA_SIZE, this.getGridY() * Tessera.TESSERA_SIZE);
	}

	private Vector2f offsetPos() {
		Minecraft minecraft = Minecraft.getInstance();
		MouseHandler mouse = Objects.requireNonNull(minecraft).mouseHandler;
		float x = (float) mouse.xpos() * (float) minecraft.getWindow().getGuiScaledWidth() / (float) minecraft.getWindow().getScreenWidth();
		float y = (float) mouse.ypos() * (float) minecraft.getWindow().getGuiScaledHeight() / (float) minecraft.getWindow().getScreenHeight();

		Vector2f center = this.getPolyomino().getGridCenter();

		return new Vector2f(
				x + (Tessera.TESSERA_SIZE * 0.1F) + (-center.x * Tessera.TESSERA_SIZE),
				y + (Tessera.TESSERA_SIZE * 0.1F) + (-center.y * Tessera.TESSERA_SIZE)
		);
	}

	public static void renderVoxels(GraphicsRenderHelper graphics, Polyomino polyomino, TesseraMaterial material, float x, float y) {
		renderVoxels(graphics, polyomino, material, x, y, -1);
	}

	public static void renderVoxels(GraphicsRenderHelper graphics, Polyomino polyomino, TesseraMaterial material, float x, float y, int color) {
		graphics.pushPop(() -> {
			graphics.translate(x, y);

			AtomicInteger index = new AtomicInteger(-1);
			polyomino.placedTessera().forEach(tessera -> graphics.pushPop(() -> {
				index.incrementAndGet();
				graphics.translate(
						tessera.x() * Tessera.TESSERA_SIZE,
						tessera.y() * Tessera.TESSERA_SIZE
				);

				graphics.blitTessera(material, tessera.tessera(), polyomino.seed(), index.get(), color);
			}));
		});
	}
}
