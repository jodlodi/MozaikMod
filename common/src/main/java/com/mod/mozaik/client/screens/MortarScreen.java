package com.mod.mozaik.client.screens;

import com.mod.mozaik.Constants;
import com.mod.mozaik.Polyomino;
import com.mod.mozaik.TesseraMaterial;
import com.mod.mozaik.Voxel;
import com.mod.mozaik.blocks.MortarBlock;
import com.mod.mozaik.blocks.entities.MortarBlockEntity;
import com.mod.mozaik.client.GraphicsRenderHelper;
import com.mod.mozaik.client.PhaseRenderable;
import com.mod.mozaik.client.buttons.CreatePolyominoButton;
import com.mod.mozaik.client.buttons.SpriteButton;
import com.mod.mozaik.client.buttons.VoxelButton;
import com.mod.mozaik.client.widgets.GridWidget;
import com.mod.mozaik.client.widgets.PolyominoWidget;
import com.mod.mozaik.menus.MortarMenu;
import com.mod.mozaik.networking.bidirectional.UpdateGlueBidirectional;
import com.mod.mozaik.platform.Services;
import com.mod.mozaik.reg.ModBlocks;
import com.mod.mozaik.reg.ResourceSupplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@NullMarked
public class MortarScreen extends AbstractContainerScreen<MortarMenu> {
	private static final Identifier MORTAR_LOCATION = Constants.prefix("textures/gui/container/mortar.png");

	private static final Identifier LEFT = Constants.prefix("textures/gui/container/left.png");
	private static final Identifier LEFT_HIGHLIGHTED = Constants.prefix("textures/gui/container/left_highlighted.png");
	private static final Identifier RIGHT = Constants.prefix("textures/gui/container/right.png");
	private static final Identifier RIGHT_HIGHLIGHTED = Constants.prefix("textures/gui/container/right_highlighted.png");

	public static final int LEFT_CLICK = 0;
	public static final int MIDDLE_CLICK = 2;
	public static final int RIGHT_CLICK = 1;

	public GridWidget[][] matrix = new GridWidget[16][16];
	public List<PolyominoWidget> polyominos = new ArrayList<>();
	public @Nullable PolyominoWidget selected;
	public List<CreatePolyominoButton> addButtons = new ArrayList<>();
	private final List<PhaseRenderable> renderableWidgets = new ArrayList<>();
	private boolean hasTicked = false;

	private static final int iX = 48;
	private static final int iY = 77;

	public MortarScreen(MortarMenu menu, Inventory playerInventory, Component title) {
		super(menu, playerInventory, title, 256, 256);
	}

	@Override
	protected void extractLabels(GuiGraphicsExtractor graphics, int xm, int ym) {

	}

	@Override
	public boolean mouseScrolled(double x, double y, double scrollX, double scrollY) {
		if (this.selected != null) {
			this.selected.rotate(scrollY > 0 ? Rotation.CLOCKWISE_90 : Rotation.COUNTERCLOCKWISE_90);
			return true;
		}
		return super.mouseScrolled(x, y, scrollX, scrollY);
	}

	@Override
	public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
		if (this.selected != null) {
			int click = event.button();

			if (click == RIGHT_CLICK) {
				this.selected.mirror();
				return true;
			}

			PolyominoWidget widget;

			if (click == MIDDLE_CLICK) {
				widget = this.selected.copy();
				this.addRenderableWidget(widget);
			} else widget = this.selected;

			GridWidget square = this.getTargetWidget();
			if (square != null) {
				int x = square.relativeX();
				int y = square.relativeY();

				for (VoxelButton entry : widget.voxels) {
					int relativeX = x + entry.relativeX();
					int relativeY = y + entry.relativeY();

					GridWidget victim = this.matrix[relativeX][relativeY];

					victim.setVoxel(entry);
					entry.setGrid(victim);
				}

				widget.setX(square.getX());
				widget.setY(square.getY());
				this.polyominos.add(widget);
				widget.gridX = x;
				widget.gridY = y;

				if (click == LEFT_CLICK) this.selected = null;
				Services.NETWORK.sendToServer(new UpdateGlueBidirectional(this.polyominos.stream().map(Polyomino::asPlain).toList(), this.menu.getPos()));
				return true;
			}

			this.selected.remove();
			return true;
		}
		return super.mouseClicked(event, doubleClick);
	}

	@Nullable
	public GridWidget getTargetWidget() {
		Minecraft minecraft = Minecraft.getInstance();
		MouseHandler mouse = Objects.requireNonNull(minecraft).mouseHandler;
		float mouseX = (float) mouse.xpos() * (float) minecraft.getWindow().getGuiScaledWidth() / (float) minecraft.getWindow().getScreenWidth();
		float mouseY = (float) mouse.ypos() * (float) minecraft.getWindow().getGuiScaledHeight() / (float) minecraft.getWindow().getScreenHeight();
		return this.getTargetWidget(mouseX, mouseY);
	}

	@Nullable
	public GridWidget getTargetWidget(float mouseX, float mouseY) {
		if (this.selected != null) {
			GridWidget square = this.matrix[0][0];
			int gridX = square.getX();
			int gridY = square.getY();

			Vector2f center = this.selected.getGridCenter();
			mouseX -= center.x * VoxelButton.TESSERA_SIZE;
			mouseY -= center.y * VoxelButton.TESSERA_SIZE;

			for (int offsetX : new int[]{0, 1, -1}) {
				for (int offsetY : new int[]{0, 1, -1}) {
					int x = (int) (mouseX - gridX) / VoxelButton.TESSERA_SIZE + offsetX;
					int y = (int) (mouseY - gridY) / VoxelButton.TESSERA_SIZE + offsetY;

					if (x < 0 || y < 0 || x >= 16 || y >= 16) {
						continue; // Out of bounds
					}

					boolean canFit = true;
					for (VoxelButton entry : this.selected.voxels) {
						int relativeX = x + entry.relativeX();
						int relativeY = y + entry.relativeY();

						if (relativeX < 0 || relativeY < 0 || relativeX >= 16 || relativeY >= 16) {
							canFit = false;
							break; // Out of bounds
						}

						GridWidget victim = this.matrix[relativeX][relativeY];
						if (victim.getVoxel() != null) {
							canFit = false;
							break; // Occupied
						}
					}

					if (canFit) return this.matrix[x][y];
				}
			}
		}
		return null;
	}

	@Override
	protected void containerTick() {
		if (!this.hasTicked) {
			ClientLevel level = Minecraft.getInstance().level;
			if (level == null) return;
			if (level.getBlockEntity(this.menu.getPos()) instanceof MortarBlockEntity blockEntity) {
				blockEntity.getPolyominos().forEach(polyomino -> {
					List<Voxel.PlainVoxel> voxels = polyomino.allVoxels();
					int gridX = polyomino.gridX();
					int gridY = polyomino.gridY();
					GridWidget widget = this.matrix[gridX][gridY];

					PolyominoWidget polyominoWidget = new PolyominoWidget(this, widget.getX(), widget.getY(), polyomino.color(), polyomino.seed());
					polyominoWidget.gridX = gridX;
					polyominoWidget.gridY = gridY;
					this.polyominos.add(polyominoWidget);
					this.addRenderableWidget(polyominoWidget);

					voxels.forEach(info -> polyominoWidget.withVoxel(info.relativeX(), info.relativeY()));

					for (VoxelButton entry : polyominoWidget.voxels) {
						int relativeX = gridX + entry.relativeX();
						int relativeY = gridY + entry.relativeY();

						GridWidget victim = this.matrix[relativeX][relativeY];

						victim.setVoxel(entry);
						entry.setGrid(victim);
					}
				});
			}
			this.hasTicked = true;
		}
		super.containerTick();
	}

	@Override
	public <T extends GuiEventListener & Renderable & NarratableEntry> T addRenderableWidget(T widget) {
		if (widget instanceof PhaseRenderable phased) this.renderableWidgets.add(phased);
		return super.addRenderableWidget(widget);
	}

	@Override
	public void removeWidget(GuiEventListener widget) {
		if (widget instanceof PhaseRenderable phased) this.renderableWidgets.remove(phased);
		super.removeWidget(widget);
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor graphicsExtractor, int mouseX, int mouseY, float partialTick) {
		GraphicsRenderHelper graphics = new GraphicsRenderHelper(graphicsExtractor);
		this.renderableWidgets.forEach(renderable -> renderable.renderBelowItems(graphics));
		this.extractContents(graphicsExtractor, mouseX, mouseY, partialTick);
		this.renderableWidgets.forEach(renderable -> renderable.renderAboveItems(graphics));
		this.extractCarriedItem(graphicsExtractor, mouseX, mouseY);
		this.renderableWidgets.forEach(renderable -> renderable.renderOnTop(graphics));
		this.extractTooltip(graphicsExtractor, mouseX, mouseY);
	}

	@Override
	protected void rebuildWidgets() {
		super.rebuildWidgets();
		this.hasTicked = false;
	}

	@Override
	protected void init() {
		super.init();

		int midX = this.width / 2;

		for (int x = 0; x < 16; x++) {
			for (int y = 0; y < 16; y++) {
				this.matrix[x][y] = this.addRenderableWidget(
						new GridWidget(this.leftPos + iX + x * VoxelButton.TESSERA_SIZE, this.topPos + iY + y * VoxelButton.TESSERA_SIZE, x, y)
				);
			}
		}

		Polyomino.PolyominoShape[] values = Polyomino.PolyominoShape.values();
		int count = values.length;
		int size = CreatePolyominoButton.SIZE + 4;

		for (int i = 0; i < count; i++) {
			Polyomino.PolyominoShape shapes = values[i];
			float x = i - (count * 0.5F) + 0.5F;

			this.addButtons.add(this.addRenderableWidget(new CreatePolyominoButton((int) (midX - (size * x)), this.topPos + 4, this, shapes.template)));
		}

		int colorCount = TesseraMaterial.values().length;

		this.addRenderableWidget(SpriteButton.createArrow(midX - 92, this.topPos + 32, LEFT, LEFT_HIGHLIGHTED, (button, input) -> {
			this.addButtons.forEach(createPolyominoButton -> {
				do {
					createPolyominoButton.setColor((createPolyominoButton.getColor() + colorCount - 1) % colorCount);
				} while (TesseraMaterial.values()[createPolyominoButton.getColor()].isFakeMaterial());
			});
		}));

		this.addRenderableWidget(SpriteButton.createArrow(midX + 92, this.topPos + 32, RIGHT, RIGHT_HIGHLIGHTED, (button, input) -> {
			this.addButtons.forEach(createPolyominoButton -> {
				do {
					createPolyominoButton.setColor((createPolyominoButton.getColor() + 1) % colorCount);
				} while (TesseraMaterial.values()[createPolyominoButton.getColor()].isFakeMaterial());
			});
		}));
	}

	@Override
	public void extractBackground(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
		super.extractBackground(graphics, mouseX, mouseY, a);
		int xo = (this.width - this.imageWidth) / 2;
		int yo = (this.height - this.imageHeight) / 2;
		graphics.blit(RenderPipelines.GUI_TEXTURED, MORTAR_LOCATION, xo, yo, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);

		ClientLevel level = Minecraft.getInstance().level;
		if (level == null) return;
		BlockEntity entity = level.getBlockEntity(this.menu.getPos());
		if (entity == null) return;
		Block block = entity.getBlockState().getBlock();
		graphics.blit(RenderPipelines.GUI_TEXTURED, fromBlock(block), xo + iX, yo + iY, 0.0F, 0.0F, 160, 160, 160, 160);
	}

	private static Identifier fromBlock(Block block) {
		for (ResourceSupplier<MortarBlock> mortarBlockResourceSupplier : ModBlocks.MORTARS.asList()) {
			if (mortarBlockResourceSupplier.get() == block) {
				return Constants.prefix("textures/block/" + mortarBlockResourceSupplier.id().getPath() + ".png");
			}
		}
		return TextureManager.INTENTIONAL_MISSING_TEXTURE;
	}
}
