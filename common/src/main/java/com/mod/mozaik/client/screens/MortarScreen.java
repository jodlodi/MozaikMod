package com.mod.mozaik.client.screens;

import com.mod.mozaik.Constants;
import com.mod.mozaik.blocks.MortarBlock;
import com.mod.mozaik.client.GraphicsRenderHelper;
import com.mod.mozaik.client.PhaseRenderable;
import com.mod.mozaik.client.buttons.CreatePolyominoButton;
import com.mod.mozaik.client.buttons.SpriteButton;
import com.mod.mozaik.client.widgets.GridWidget;
import com.mod.mozaik.client.widgets.HeldPolyominoWidget;
import com.mod.mozaik.client.widgets.PolyominoWidget;
import com.mod.mozaik.menus.MortarMenu;
import com.mod.mozaik.networking.bidirectional.UpdateGlueBidirectional;
import com.mod.mozaik.platform.Services;
import com.mod.mozaik.polyomino.PrePolyominoShapes;
import com.mod.mozaik.polyomino.Tessera;
import com.mod.mozaik.polyomino.TesseraMaterial;
import com.mod.mozaik.reg.ModBlocks;
import com.mod.mozaik.reg.ResourceSupplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
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
	private static final int BACKGROUND_WIDTH = 242;
	private static final int BACKGROUND_HEIGHT = 256;
	private static final int GRID_START_X = 41;
	private static final int GRID_START_Y = 76;
	private static final int BOWL_CENTER_X = 58;
	private static final int BOWL_CENTER_Y = 26;

	public static final int LEFT_CLICK = 0;
	public static final int MIDDLE_CLICK = 2;
	public static final int RIGHT_CLICK = 1;

	int template = 0;

	public GridWidget[][] matrix = new GridWidget[16][16];
	public List<PolyominoWidget> polyominos = new ArrayList<>();
	public @Nullable HeldPolyominoWidget selected;
	public @Nullable CreatePolyominoButton addButton;
	private final List<PhaseRenderable> renderableWidgets = new ArrayList<>();

	public MortarScreen(MortarMenu menu, Inventory playerInventory, Component title) {
		super(menu, playerInventory, title, BACKGROUND_WIDTH, BACKGROUND_HEIGHT);
	}

	protected void markChanged() {
		Services.NETWORK.sendToServer(new UpdateGlueBidirectional(this.polyominos.stream().map(PolyominoWidget::getPlacedPolyomino).toList(), this.menu.getPos()));
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
			} else if (click == LEFT_CLICK) {
				GridWidget square = this.getTargetWidget();
				if (square != null) {
					int x = square.relativeX();
					int y = square.relativeY();

					PolyominoWidget widget = this.selected.build(x, y);
					this.addRenderableWidget(widget);

					widget.setX(square.getX());
					widget.setY(square.getY());
					this.polyominos.add(widget);
					this.markChanged();
				}
				this.selected.remove();
				return true;
			} else if (click == MIDDLE_CLICK && this.addButton != null) {
				this.selected.polyomino = this.addButton.getTemplate().build(this.selected.polyomino.material(), Objects.requireNonNull(Minecraft.getInstance().level).getRandom().nextLong());

				GridWidget square = this.getTargetWidget();
				if (square != null) {
					int x = square.relativeX();
					int y = square.relativeY();

					PolyominoWidget widget = this.selected.build(x, y);
					this.addRenderableWidget(widget);

					widget.setX(square.getX());
					widget.setY(square.getY());
					this.polyominos.add(widget);
					this.markChanged();

					return true;
				}
			}
			return true;
		} else {
			Minecraft minecraft = Minecraft.getInstance();
			MouseHandler mouse = Objects.requireNonNull(minecraft).mouseHandler;
			float mouseX = (float) mouse.xpos() * (float) minecraft.getWindow().getGuiScaledWidth() / (float) minecraft.getWindow().getScreenWidth();
			float mouseY = (float) mouse.ypos() * (float) minecraft.getWindow().getGuiScaledHeight() / (float) minecraft.getWindow().getScreenHeight();

			GridWidget square = this.matrix[0][0];
			int gridX = square.getX();
			int gridY = square.getY();

			int x = (int) (mouseX - gridX) / Tessera.TESSERA_SIZE;
			int y = (int) (mouseY - gridY) / Tessera.TESSERA_SIZE;

			for (PolyominoWidget widget : this.polyominos) {
				int widgetX = widget.gridX();
				int widgetY = widget.gridY();
				for (Tessera.PlacedTessera tessera : widget.getPlacedPolyomino().polyomino().placedTessera()) {
					int rX = widgetX + tessera.x();
					int rY = widgetY + tessera.y();
					if (rX == x && rY == y) {
						this.selected = new HeldPolyominoWidget(this, widget.getX(), widget.getY(), widget.getPlacedPolyomino().polyomino());
						this.addRenderableWidget(this.selected);
						this.polyominos.remove(widget);
						this.removeWidget(widget);
						this.markChanged();
						return true;
					}
				}
			}
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

			Vector2f center = this.selected.polyomino.getGridCenter();
			mouseX -= center.x * Tessera.TESSERA_SIZE;
			mouseY -= center.y * Tessera.TESSERA_SIZE;

			for (int offsetX : new int[]{0, 1, -1}) {
				for (int offsetY : new int[]{0, 1, -1}) {
					int x = (int) (mouseX - gridX) / Tessera.TESSERA_SIZE + offsetX;
					int y = (int) (mouseY - gridY) / Tessera.TESSERA_SIZE + offsetY;

					if (x < 0 || y < 0 || x >= 16 || y >= 16) {
						continue; // Out of bounds
					}

					boolean canFit = true;
					for (Tessera.PlacedTessera entry : this.selected.polyomino.placedTessera()) {
						int relativeX = x + entry.x();
						int relativeY = y + entry.y();

						if (relativeX < 0 || relativeY < 0 || relativeX >= 16 || relativeY >= 16) {
							canFit = false;
							break; // Out of bounds
						}

						for (PolyominoWidget widget : this.polyominos) {
							int widgetX = widget.gridX();
							int widgetY = widget.gridY();
							for (Tessera.PlacedTessera tessera : widget.getPlacedPolyomino().polyomino().placedTessera()) {
								int rX = widgetX + tessera.x();
								int rY = widgetY + tessera.y();
								if (rX == relativeX && rY == relativeY) {
									canFit = false;
									break;
								}
							}
							if (!canFit) break; // Occupied
						}
					}

					if (canFit) return this.matrix[x][y];
				}
			}
		}
		return null;
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
		super.extractRenderState(graphicsExtractor, mouseX, mouseY, partialTick);
		this.renderableWidgets.forEach(renderable -> renderable.renderAboveItems(graphics));
		this.renderableWidgets.forEach(renderable -> renderable.renderOnTop(graphics));
	}

	@Override
	protected void init() {
		super.init();

		int midX = this.width / 2;

		for (int x = 0; x < 16; x++) {
			for (int y = 0; y < 16; y++) {
				this.matrix[x][y] = this.addRenderableWidget(
						new GridWidget(this.leftPos + GRID_START_X + x * Tessera.TESSERA_SIZE, this.topPos + GRID_START_Y + y * Tessera.TESSERA_SIZE, x, y)
				);
			}
		}

		PrePolyominoShapes[] values = PrePolyominoShapes.values();
		this.addButton = this.addRenderableWidget(new CreatePolyominoButton(this.leftPos + BOWL_CENTER_X, this.topPos + BOWL_CENTER_Y, this, values[this.template].template));

		int colorCount = TesseraMaterial.values().length;

		this.addRenderableWidget(SpriteButton.createArrow(midX - 92, this.topPos + 32, LEFT, LEFT_HIGHLIGHTED, (button, input) -> {
			do {
				this.addButton.setColor((this.addButton.getColor() + colorCount - 1) % colorCount);
			} while (TesseraMaterial.values()[this.addButton.getColor()].isFakeMaterial());
		}));

		this.addRenderableWidget(SpriteButton.createArrow(midX + 92, this.topPos + 32, RIGHT, RIGHT_HIGHLIGHTED, (button, input) -> {
			do {
				this.addButton.setColor((this.addButton.getColor() + 1) % colorCount);
			} while (TesseraMaterial.values()[this.addButton.getColor()].isFakeMaterial());
		}));

		int templateCount = PrePolyominoShapes.values().length;

		this.addRenderableWidget(SpriteButton.createArrow(midX - 92, this.topPos + 16, LEFT, LEFT_HIGHLIGHTED, (button, input) -> {
			this.template = (this.template + templateCount - 1) % templateCount;
			this.addButton.setTemplate(values[this.template].template);
		}));

		this.addRenderableWidget(SpriteButton.createArrow(midX + 92, this.topPos + 16, RIGHT, RIGHT_HIGHLIGHTED, (button, input) -> {
			this.template = (this.template + 1) % templateCount;
			this.addButton.setTemplate(values[this.template].template);
		}));

		this.menu.getMosaic().forEach(placedPolyomino -> {
			int gridX = placedPolyomino.x();
			int gridY = placedPolyomino.y();
			GridWidget widget = this.matrix[gridX][gridY];

			PolyominoWidget polyominoWidget = new PolyominoWidget(this, widget.getX(), widget.getY(), placedPolyomino);
			this.polyominos.add(polyominoWidget);
			this.addRenderableWidget(polyominoWidget);
		});
	}

	@Override
	protected void clearWidgets() {
		this.polyominos.clear();
		this.renderableWidgets.clear();
		super.clearWidgets();
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
		graphics.blit(RenderPipelines.GUI_TEXTURED, fromBlock(block), xo + GRID_START_X, yo + GRID_START_Y, 0.0F, 0.0F, 160, 160, 160, 160);
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
