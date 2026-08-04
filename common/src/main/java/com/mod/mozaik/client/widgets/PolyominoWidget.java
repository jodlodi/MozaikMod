package com.mod.mozaik.client.widgets;

import com.mod.mozaik.*;
import com.mod.mozaik.client.GraphicsRenderHelper;
import com.mod.mozaik.client.PhaseRenderable;
import com.mod.mozaik.client.buttons.VoxelButton;
import com.mod.mozaik.client.screens.MortarScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.Rotation;
import org.joml.Vector2f;
import org.joml.Vector3i;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@NullMarked
public class PolyominoWidget extends UnclickableWidget implements Polyomino<VoxelButton>, PhaseRenderable {
	private static final int SHADOW = 0x80000000;

	public final List<VoxelButton> voxels = new ArrayList<>();
	public final MortarScreen screen;
	private final int color;
	private final long seed;

	public int gridX = 0;
	public int gridY = 0;

	public PolyominoWidget(MortarScreen screen, int x, int y, int color, long seed) {
		super(x, y, 0, 0);
		this.screen = screen;
		this.color = color;
		this.seed = seed;
	}

	public PolyominoWidget withVoxel(int x, int y) {
		VoxelButton button = new VoxelButton(this, x, y);
		this.screen.addRenderableWidget(button);
		this.voxels.add(button);
		return this;
	}

	public PolyominoWidget copy() {
		PolyominoWidget copy = new PolyominoWidget(this.screen, this.getX(), this.getY(), this.color, this.seed);
		this.voxels.forEach(voxelButton -> copy.withVoxel(voxelButton.relativeX(), voxelButton.relativeY()));
		return copy;
	}

	@Override
	public PolyominoWidget rotate(Rotation rotation) {

		this.allVoxels().forEach(voxel -> {
			Vector3i vec = new Vector3i(voxel.relativeX(), 0, voxel.relativeY());
			Vector3i rotated = rotation.rotation().rotate(vec);
			voxel.setX(rotated.x);
			voxel.setY(rotated.z);
		});

		return this;
	}

	@Override
	public Polyomino<VoxelButton> mirror() {

		this.allVoxels().forEach(voxel -> {
			voxel.setX(voxel.relativeX() * -1);
		});

		return this;
	}

	@Override
	public long seed() {
		return this.seed;
	}

	public void remove() {
		for (VoxelButton tessera : this.voxels) this.screen.removeWidget(tessera);
		this.voxels.clear();
		this.screen.removeWidget(this);
		this.screen.selected = null;
	}

	@Override
	public List<VoxelButton> allVoxels() {
		return this.voxels;
	}

	@Override
	public void renderBelowItems(GraphicsRenderHelper graphics) {
		if (this.screen.selected == this) return;
		renderVoxels(graphics, this, TesseraMaterial.values()[this.color()], this.getX(), this.getY());
	}

	@Override
	public void renderAboveItems(GraphicsRenderHelper graphics) {
		if (this.screen.selected != this) return;
		Minecraft minecraft = Minecraft.getInstance();
		MouseHandler mouse = Objects.requireNonNull(minecraft).mouseHandler;
		float x = (float) mouse.xpos() * (float) minecraft.getWindow().getGuiScaledWidth() / (float) minecraft.getWindow().getScreenWidth();
		float y = (float) mouse.ypos() * (float) minecraft.getWindow().getGuiScaledHeight() / (float) minecraft.getWindow().getScreenHeight();

		Vector2f center = this.getGridCenter();
		GridWidget square = this.screen.getTargetWidget(x, y);

		if (square == null) {
			renderVoxels(
					graphics,
					this,
					TesseraMaterial.GLASS,
					x + (VoxelButton.TESSERA_SIZE * 0.1F) + (-center.x * VoxelButton.TESSERA_SIZE) + 1,
					y + (VoxelButton.TESSERA_SIZE * 0.1F) + (-center.y * VoxelButton.TESSERA_SIZE) + 1
			);
		} else renderVoxels(graphics, this, TesseraMaterial.GLASS, square.getX(), square.getY());
	}

	@Override
	public void renderOnTop(GraphicsRenderHelper graphics) {
		if (this.screen.selected != this) return;
		Minecraft minecraft = Minecraft.getInstance();
		MouseHandler mouse = Objects.requireNonNull(minecraft).mouseHandler;
		float x = (float) mouse.xpos() * (float) minecraft.getWindow().getGuiScaledWidth() / (float) minecraft.getWindow().getScreenWidth();
		float y = (float) mouse.ypos() * (float) minecraft.getWindow().getGuiScaledHeight() / (float) minecraft.getWindow().getScreenHeight();

		Vector2f center = this.getGridCenter();

		renderVoxels(
				graphics,
				this,
				TesseraMaterial.values()[this.color()],
				x + (VoxelButton.TESSERA_SIZE * 0.1F) + (-center.x * VoxelButton.TESSERA_SIZE),
				y + (VoxelButton.TESSERA_SIZE * 0.1F) + (-center.y * VoxelButton.TESSERA_SIZE)
		);
	}

	public static <T extends Voxel> void renderVoxels(GraphicsRenderHelper graphics, Polyomino<T> polyomino, TesseraMaterial material, float x, float y) {
		graphics.pushPop(() -> {
			graphics.translate(x, y);

			AtomicInteger index = new AtomicInteger(-1);
			polyomino.allVoxels().forEach(voxel -> graphics.pushPop(() -> {
				index.incrementAndGet();
				graphics.translate(
						voxel.relativeX() * VoxelButton.TESSERA_SIZE,
						voxel.relativeY() * VoxelButton.TESSERA_SIZE
				);

				List<FlatDirection> connections = new ArrayList<>();

				for (FlatDirection direction : FlatDirection.cardinalClockwise()) {
					if (PolyominoWidget.checkConnection(polyomino, voxel, direction).isPresent()) {
						connections.add(direction);
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

						connections.add(direction);
					}
				}

				graphics.blitTessera(material, connections);
			}));
		});
	}

	public static <T extends Voxel> Optional<T> checkConnection(Polyomino<T> polyomino, T voxel, FlatDirection direction) {
		return polyomino.allVoxels().stream().filter(relative -> {
			int diffX = relative.relativeX() - voxel.relativeX();
			int diffY = relative.relativeY() - voxel.relativeY();
			return diffX == direction.getRelativeX() && diffY == direction.getRelativeY();
		}).findFirst();
	}

	public static Identifier byaDirection(@Nullable FlatDirection direction, TesseraMaterial material) {
		if (direction == null) return Constants.prefix("textures/block/mural/" + material.getSerializedName() + "/tessera.png");
		return switch (direction) {
			case UP -> Constants.prefix("textures/block/mural/" + material.getSerializedName() + "/bridge_up.png");
			case UP_RIGHT -> Constants.prefix("textures/block/mural/" + material.getSerializedName() + "/corner_up_right.png");
			case RIGHT -> Constants.prefix("textures/block/mural/" + material.getSerializedName() + "/bridge_right.png");
			case DOWN_RIGHT -> Constants.prefix("textures/block/mural/" + material.getSerializedName() + "/corner_down_right.png");
			case DOWN -> Constants.prefix("textures/block/mural/" + material.getSerializedName() + "/bridge_down.png");
			case DOWN_LEFT -> Constants.prefix("textures/block/mural/" + material.getSerializedName() + "/corner_down_left.png");
			case LEFT -> Constants.prefix("textures/block/mural/" + material.getSerializedName() + "/bridge_left.png");
			case UP_LEFT -> Constants.prefix("textures/block/mural/" + material.getSerializedName() + "/corner_up_left.png");
		};
	}

	@Override
	public int gridX() {
		return this.gridX;
	}

	@Override
	public int gridY() {
		return this.gridY;
	}

	@Override
	public int color() {
		return this.color;
	}
}
