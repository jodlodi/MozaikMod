package com.mod.mozaik.client.screens;

import com.mod.mozaik.Constants;
import com.mod.mozaik.blocks.MortarBlock;
import com.mod.mozaik.client.GraphicsRenderHelper;
import com.mod.mozaik.client.PhaseRenderable;
import com.mod.mozaik.client.buttons.ClickableButton;
import com.mod.mozaik.client.buttons.CreatePolyominoButton;
import com.mod.mozaik.client.buttons.SpriteButton;
import com.mod.mozaik.client.buttons.ToolButton;
import com.mod.mozaik.client.widgets.AltColorWidget;
import com.mod.mozaik.client.widgets.HeldPolyominoWidget;
import com.mod.mozaik.client.widgets.MaterialWidget;
import com.mod.mozaik.client.widgets.PolyominoWidget;
import com.mod.mozaik.menus.MortarMenu;
import com.mod.mozaik.networking.bidirectional.UpdateGlueBidirectional;
import com.mod.mozaik.platform.Services;
import com.mod.mozaik.polyomino.Polyomino;
import com.mod.mozaik.polyomino.PrePolyominoShapes;
import com.mod.mozaik.polyomino.Tessera;
import com.mod.mozaik.polyomino.TesseraMaterial;
import com.mod.mozaik.reg.ModBlocks;
import com.mod.mozaik.reg.ResourceSupplier;
import com.mod.mozaik.util.FlatDirection;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.joml.Vector3i;
import org.jspecify.annotations.NullMarked;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@NullMarked
public class MortarScreen extends AbstractContainerScreen<MortarMenu> {
	private static final Identifier MORTAR_LOCATION = Constants.prefix("textures/gui/container/mortar.png");
	private static final int BACKGROUND_WIDTH = 242;
	private static final int BACKGROUND_HEIGHT = 256;
	private static final Vector2i GRID_START = new Vector2i(41, 76);
	private static final Vector2i BOWL_CENTER = new Vector2i(59, 25);
	private static final Vector2i MINI_BOWL_ITEM = new Vector2i(107, 7);
	private static final Vector2i MATERIAL_BAR = new Vector2i(4, 75);
	private static final Vector2i MATERIAL_BAR_UP = new Vector2i(4, 66);
	private static final Vector2i MATERIAL_BAR_DOWN = new Vector2i(4, 236);

	private static final Vector2i CHISEL = new Vector2i(154, 11);
	private static final Vector2i CURSOR = new Vector2i(154, 32);
	private static final Vector2i SWAP = new Vector2i(175, 11);
	private static final Vector2i PICKER = new Vector2i(175, 32);
	private static final Vector2i WAND = new Vector2i(196, 11);
	private static final Vector2i SELECT = new Vector2i(196, 32);

	public static final int LEFT_CLICK = 0;
	public static final int MIDDLE_CLICK = 2;
	public static final int RIGHT_CLICK = 1;

	int template = 0;
	public MozaikTool tool = MozaikTool.CURSOR;
	public int from = 0;
	public int to = 9;

	public List<PolyominoWidget> polyominos = new ArrayList<>();
	public List<PolyominoWidget> selected = new ArrayList<>();
	public List<HeldPolyominoWidget> carried = new ArrayList<>();
	private final List<PhaseRenderable> renderableWidgets = new ArrayList<>();

	private TesseraMaterial primaryColor = TesseraMaterial.values()[0];
	private TesseraMaterial secondaryColor = TesseraMaterial.values()[1];
	private Polyomino shape = Polyomino.EMPTY;

	public MortarScreen(MortarMenu menu, Inventory playerInventory, Component title) {
		super(menu, playerInventory, title, BACKGROUND_WIDTH, BACKGROUND_HEIGHT);
		if (shape == Polyomino.EMPTY) shape = PrePolyominoShapes.SQUARE.template.build(primaryColor, 10L);
	}

	public TesseraMaterial getPrimaryColor() {
		return this.primaryColor;
	}

	public void setPrimaryColor(TesseraMaterial primaryColor) {
		this.primaryColor = primaryColor;
		this.shape = this.shape.rebuild(this.primaryColor, Objects.requireNonNull(Minecraft.getInstance().level).getRandom().nextLong());

		this.carried.forEach(heldPolyominoWidget ->
				heldPolyominoWidget.setPolyomino(heldPolyominoWidget.getPolyomino().rebuild(primaryColor, Objects.requireNonNull(Minecraft.getInstance().level).getRandom().nextLong()))
		);
	}

	public TesseraMaterial getSecondaryColor() {
		return this.secondaryColor;
	}

	public void setSecondaryColor(TesseraMaterial secondaryColor) {
		this.secondaryColor = secondaryColor;
	}

	public Polyomino getShape() {
		return this.shape;
	}

	public void setShape(Polyomino shape) {
		this.shape = shape;
	}

	@Override
	protected void extractSlot(GuiGraphicsExtractor graphics, Slot slot, int mouseX, int mouseY) {
		super.extractSlot(graphics, slot, mouseX, mouseY);
	}

	public void markChanged() {
		if (this.menu.getMortar() != null) {
			Services.NETWORK.sendToServer(new UpdateGlueBidirectional(this.polyominos.stream().map(PolyominoWidget::getPlacedPolyomino).toList(), this.menu.getMortar().getBlockPos()));
		}
	}

	@Override
	protected void extractLabels(GuiGraphicsExtractor graphics, int xm, int ym) {

	}

	@Override
	public boolean keyPressed(KeyEvent event) {
		for (int i = 0; i < 6; ++i) {
			if (this.minecraft.options.keyHotbarSlots[i].matches(event)) {
				this.tool = MozaikTool.values()[i];
				this.selected.clear();
				return true;
			}
		}

		if (InputConstants.KEY_RETURN == event.key()) {
			this.selected.clear();
		}

		return super.keyPressed(event);
	}

	@Override
	public boolean mouseScrolled(double x, double y, double scrollX, double scrollY) {
		if (!this.carried.isEmpty()) {
			this.carried.forEach(heldPolyominoWidget -> {
				Rotation rotation = scrollY > 0 ? Rotation.CLOCKWISE_90 : Rotation.COUNTERCLOCKWISE_90;
				Polyomino polyomino = heldPolyominoWidget.rotate(rotation);

				Vector2i average = new Vector2i();
				for (HeldPolyominoWidget widget : this.carried) {
					average.add(widget.getGridX(), widget.getGridY());
				}
				average.div(this.carried.size());

				Vector2i diff = new Vector2i(heldPolyominoWidget.getGridX(), heldPolyominoWidget.getGridY()).sub(average);

				Vector3i vec = new Vector3i(diff.x(), 0, diff.y());
				Vector3i rotated = rotation.rotation().rotate(vec);

				heldPolyominoWidget.setPolyomino(new Polyomino.PlacedPolyomino(polyomino, average.x + rotated.x, average.y + rotated.z));
			});
			return true;
		}

		Optional<GuiEventListener> child = this.getChildAt(x, y);
		if (child.isPresent() && child.get().mouseScrolled(x, y, scrollX, scrollY)) {
			return true;
		}

		if (super.mouseScrolled(x, y, scrollX, scrollY)) {
			return true;
		} else if (Minecraft.getInstance().hasShiftDown()) { // FIXME
			int templateCount = PrePolyominoShapes.values().length;
			this.template = (this.template + templateCount - 1) % templateCount;
			this.shape = PrePolyominoShapes.values()[this.template].template.build(this.primaryColor, Objects.requireNonNull(Minecraft.getInstance().level).getRandom().nextLong());
			return true;
		}
		return false;
	}

	@Override
	public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
		int click = event.button();

		Optional<GuiEventListener> child = this.getChildAt(event.x(), event.y());
		if (child.isPresent()) {
			GuiEventListener widget = child.get();
			if (widget.mouseClicked(event, doubleClick) && widget.shouldTakeFocusAfterInteraction()) {
				this.setFocused(widget);
				if (event.button() == LEFT_CLICK) {
					this.setDragging(true);
				}
			}
			return true;
		}

		if (click == RIGHT_CLICK) {
			TesseraMaterial color = this.getPrimaryColor();
			this.setPrimaryColor(this.getSecondaryColor());
			this.setSecondaryColor(color);
			return true;
		}

		if (!this.carried.isEmpty()) {
			if (click == LEFT_CLICK) {
				this.carried.forEach(heldPolyominoWidget -> {
					Vector2i square = this.getGridForPlacement(heldPolyominoWidget);
					if (square != null) {
						this.placePolyomino(heldPolyominoWidget, square);
					}
					this.removeWidget(heldPolyominoWidget);
				});
				this.carried.clear();
				return true;
			} else if (click == MIDDLE_CLICK) {
				this.carried.forEach(heldPolyominoWidget -> {
					Vector2i square = this.getGridForPlacement(heldPolyominoWidget);
					if (square != null) {
						this.placePolyomino(heldPolyominoWidget, square);
						heldPolyominoWidget.setPolyomino(this.shape.rebuild(heldPolyominoWidget.getPolyomino().material(), Objects.requireNonNull(Minecraft.getInstance().level).getRandom().nextLong()));
					}
				});
				return true;
			}
			return true;
		}

		Vector2i square = this.getGridForTaking();

		for (PolyominoWidget widget : this.polyominos) {
			int widgetX = widget.gridX();
			int widgetY = widget.gridY();
			for (Tessera.PlacedTessera tessera : widget.getPlacedPolyomino().polyomino().placedTessera()) {
				int rX = widgetX + tessera.x();
				int rY = widgetY + tessera.y();
				if (rX == square.x && rY == square.y) {
					MozaikTool.useOn(this, event.hasShiftDown(), List.of(widget), this.tool);
					return true;
				}
			}
		}

		return false;
	}

	protected void placePolyomino(HeldPolyominoWidget selected, Vector2i square) {
		Vector2i gridPos = this.getGridPos(square);

		PolyominoWidget widget = new PolyominoWidget(this, gridPos.x, gridPos.y, new Polyomino.PlacedPolyomino(selected.getPolyomino(), square.x, square.y));
		this.addRenderableWidget(widget);

		this.polyominos.add(widget);
		this.markChanged();
	}

	public Vector2i getGridForTaking() {
		Minecraft minecraft = Minecraft.getInstance();
		MouseHandler mouse = Objects.requireNonNull(minecraft).mouseHandler;
		float mouseX = (float) mouse.xpos() * (float) minecraft.getWindow().getGuiScaledWidth() / (float) minecraft.getWindow().getScreenWidth();
		float mouseY = (float) mouse.ypos() * (float) minecraft.getWindow().getGuiScaledHeight() / (float) minecraft.getWindow().getScreenHeight();

		int gridX = this.leftPos + GRID_START.x;
		int gridY = this.topPos + GRID_START.y;

		int x = (int) (mouseX - gridX) / Tessera.TESSERA_SIZE;
		int y = (int) (mouseY - gridY) / Tessera.TESSERA_SIZE;
		return new Vector2i(x, y);
	}

	public Vector2i getGridPos(Vector2i target) {
		int gridX = this.leftPos + GRID_START.x;
		int gridY = this.topPos + GRID_START.y;
		return new Vector2i(gridX + target.x * Tessera.TESSERA_SIZE, gridY + target.y * Tessera.TESSERA_SIZE);
	}

	@Nullable
	public Vector2i getGridForPlacement(HeldPolyominoWidget polyominoWidget) {
		int gridX = this.leftPos + GRID_START.x;
		int gridY = this.topPos + GRID_START.y;

		Vector2f center = polyominoWidget.heldPos();

		int x = (int) (center.x - gridX) / Tessera.TESSERA_SIZE;
		int y = (int) (center.y - gridY) / Tessera.TESSERA_SIZE;
		Vector2i grid = new Vector2i(x, y);

		for (int offsetX : new int[]{0, 1, -1}) {
			for (int offsetY : new int[]{0, 1, -1}) {
				grid = new Vector2i(grid.x + offsetX, grid.y + offsetY);

				if (grid.x < -1 || grid.y < -1 || grid.x >= 17 || grid.y >= 17) {
					continue; // Out of bounds
				}

				boolean canFit = true;
				for (Tessera.PlacedTessera entry : polyominoWidget.placedTessera()) {
					int relativeX = grid.x + entry.x();
					int relativeY = grid.y + entry.y();

					if (relativeX < -1 || relativeY < -1 || relativeX >= 17 || relativeY >= 17) {
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

					for (Map.Entry<FlatDirection, MortarMenu.NeighbourMosaic> mosaicEntry : this.menu.getMap().entrySet()) {
						FlatDirection flatDirection = mosaicEntry.getKey();
						MortarMenu.NeighbourMosaic mosaic = mosaicEntry.getValue();

						for (Polyomino.PlacedPolyomino placedPolyomino : mosaic.placedPolyomino()) {
							Polyomino polyomino = placedPolyomino.polyomino();

							AtomicInteger index = new AtomicInteger(-1);
							for (Tessera.PlacedTessera tessera : polyomino.placedTessera()) {
								index.incrementAndGet();

								int rX = tessera.x() + flatDirection.getRelativeX() * 16 + placedPolyomino.x();
								int rY = tessera.y() + flatDirection.getRelativeY() * 16 + placedPolyomino.y();

								if (rX >= -1 && rY >= -1 && rX < 17 && rY < 17) {

									if (rX == relativeX && rY == relativeY) {
										canFit = false;
										break;
									}
								}
							}
							if (!canFit) break; // Occupied
						}
						if (!canFit) break; // Occupied
					}
				}
				if (canFit) return grid;
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
	protected void containerTick() {
		super.containerTick();
		this.renderableWidgets.forEach(phaseRenderable -> {
			if (phaseRenderable instanceof SpriteButton button) button.tick();
		});
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor graphicsExtractor, int mouseX, int mouseY, float partialTick) {
		GraphicsRenderHelper graphics = new GraphicsRenderHelper(graphicsExtractor);
		this.renderNeighbourTessera(graphicsExtractor);
		this.renderableWidgets.forEach(renderable -> renderable.renderBelowItems(graphics));
		super.extractRenderState(graphicsExtractor, mouseX, mouseY, partialTick);
		this.renderSelection(graphicsExtractor);
		this.renderableWidgets.forEach(renderable -> renderable.renderAboveItems(graphics));
		this.renderableWidgets.forEach(renderable -> renderable.renderOnTop(graphics));
	}

	protected void renderNeighbourTessera(GuiGraphicsExtractor graphicsExtractor) {
		GraphicsRenderHelper graphics = new GraphicsRenderHelper(graphicsExtractor);
		int gridX = this.leftPos + GRID_START.x;
		int gridY = this.topPos + GRID_START.y;

		this.menu.getMap().forEach((flatDirection, mosaic) -> mosaic.placedPolyomino().forEach(placedPolyomino -> graphics.pushPop(() -> {
			graphics.translate(
					placedPolyomino.x() * Tessera.TESSERA_SIZE + gridX,
					placedPolyomino.y() * Tessera.TESSERA_SIZE + gridY
			);
			Polyomino polyomino = placedPolyomino.polyomino();

			AtomicInteger index = new AtomicInteger(-1);
			polyomino.placedTessera().forEach(tessera -> graphics.pushPop(() -> {
				index.incrementAndGet();
				int x = tessera.x() + flatDirection.getRelativeX() * 16;
				int y = tessera.y() + flatDirection.getRelativeY() * 16;

				int relativeX = x + placedPolyomino.x();
				int relativeY = y + placedPolyomino.y();

				if (relativeX >= -1 && relativeY >= -1 && relativeX < 17 && relativeY < 17) {
					graphics.translate(x * Tessera.TESSERA_SIZE, y * Tessera.TESSERA_SIZE);
					graphics.blitTessera(polyomino.material(), tessera.tessera(), polyomino.seed(), index.get(), 0xFF999999);
				}
			}));
		})));
	}

	protected void renderSelection(GuiGraphicsExtractor graphics) {
		this.selected.forEach(polyominoWidget -> {
			PolyominoWidget.fill(
					new GraphicsRenderHelper(graphics),
					polyominoWidget.getPlacedPolyomino().polyomino(),
					polyominoWidget.getX(),
					polyominoWidget.getY(),
					0x670094FF
			);
			PolyominoWidget.selection(
					new GraphicsRenderHelper(graphics),
					polyominoWidget.getPlacedPolyomino().polyomino(),
					polyominoWidget.getX(),
					polyominoWidget.getY()
			);
		});
	}

	public static void scrollUpBy(MortarScreen screen, int by) {
		screen.from = Math.max(0, screen.from - by);
		screen.to = Math.max(9, screen.to - by);
	}

	public static void scrollDownBy(MortarScreen screen, int by) {
		screen.from = Math.min(TesseraMaterial.values().length - 9, screen.from + by);
		screen.to = Math.min(TesseraMaterial.values().length, screen.to + by);
	}

	@Override
	protected void init() {
		super.init();

		this.addRenderableWidget(new CreatePolyominoButton(this.leftPos + BOWL_CENTER.x, this.topPos + BOWL_CENTER.y, this));

		this.addRenderableWidget(new ClickableButton(this, MATERIAL_BAR_UP, SpriteButton.SpriteSet.UP_ARROW) {
			@Override
			public void onUnblockedPress(InputWithModifiers inputWithModifiers) {
				scrollUpBy(MortarScreen.this, inputWithModifiers.hasShiftDown() ? TesseraMaterial.values().length : 9);
			}

			@Override
			public boolean isBlocked() {
				return MortarScreen.this.from == 0;
			}
		});

		this.addRenderableWidget(new ClickableButton(this, MATERIAL_BAR_DOWN, SpriteButton.SpriteSet.DOWN_ARROW) {
			@Override
			public void onUnblockedPress(InputWithModifiers inputWithModifiers) {
				scrollDownBy(MortarScreen.this, inputWithModifiers.hasShiftDown() ? TesseraMaterial.values().length : 9);
			}

			@Override
			public boolean isBlocked() {
				return MortarScreen.this.to == TesseraMaterial.values().length;
			}
		});

		this.addRenderableWidget(new ToolButton(this, CHISEL, SpriteButton.SpriteSet.CHISEL, MozaikTool.CHISEL));
		this.addRenderableWidget(new ToolButton(this, CURSOR, SpriteButton.SpriteSet.CURSOR, MozaikTool.CURSOR));
		this.addRenderableWidget(new ToolButton(this, SWAP, SpriteButton.SpriteSet.SWAP, MozaikTool.SWAP));
		this.addRenderableWidget(new ToolButton(this, PICKER, SpriteButton.SpriteSet.PICKER, MozaikTool.PICKER));
		this.addRenderableWidget(new ToolButton(this, WAND, SpriteButton.SpriteSet.WAND, MozaikTool.WAND));
		this.addRenderableWidget(new ToolButton(this, SELECT, SpriteButton.SpriteSet.SELECT, MozaikTool.SELECT));

		Objects.requireNonNull(this.menu.getMortar()).getPolyominos().forEach(placedPolyomino -> {
			int gridX = this.leftPos + GRID_START.x;
			int gridY = this.topPos + GRID_START.y;

			PolyominoWidget polyominoWidget = new PolyominoWidget(this, gridX + placedPolyomino.x() * Tessera.TESSERA_SIZE, gridY + placedPolyomino.y() * Tessera.TESSERA_SIZE, placedPolyomino);
			this.polyominos.add(polyominoWidget);
			this.addRenderableWidget(polyominoWidget);
		});

		for (int i = 0; i < 9; i++) {
			this.addRenderableWidget(new MaterialWidget(this, this.leftPos + MATERIAL_BAR.x, this.topPos + MATERIAL_BAR.y + i * 18, i));
		}

		this.addRenderableWidget(new AltColorWidget(this, this.leftPos + MINI_BOWL_ITEM.x, this.topPos + MINI_BOWL_ITEM.y));
	}

	@Override
	protected void clearWidgets() {
		this.polyominos.clear();
		this.renderableWidgets.clear();
		super.clearWidgets();
	}

	public int getLeftPos() {
		return this.leftPos;
	}

	public int getTopPos() {
		return this.topPos;
	}

	@Override
	public void extractBackground(final GuiGraphicsExtractor graphics, final int mouseX, final int mouseY, final float a) {
		super.extractBackground(graphics, mouseX, mouseY, a);
		int xo = (this.width - this.imageWidth) / 2;
		int yo = (this.height - this.imageHeight) / 2;
		graphics.blit(RenderPipelines.GUI_TEXTURED, MORTAR_LOCATION, xo, yo, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);

		BlockEntity entity = this.menu.getMortar();
		if (entity == null) return;
		Block block = entity.getBlockState().getBlock();
		graphics.blit(RenderPipelines.GUI_TEXTURED, fromBlock(block), xo + GRID_START.x, yo + GRID_START.y, 0.0F, 0.0F, 160, 160, 160, 160);

		this.menu.getMap().forEach((flatDirection, mosaic) -> {
			int u = flatDirection.getRelativeX() * Tessera.TESSERA_SIZE;
			int v = flatDirection.getRelativeY() * Tessera.TESSERA_SIZE;

			int absU = Math.abs(u);
			int absV = Math.abs(v);

			int width = absU == 0 ? 160 : absU;
			int height = absV == 0 ? 160 : absV;

			int xSet = u < 0 ? u : (u > 0 ? 160 : 0);
			int ySet = v < 0 ? v : (v > 0 ? 160 : 0);

			graphics.blit(
					RenderPipelines.GUI_TEXTURED,
					mosaic.texture(),
					xo + GRID_START.x + xSet,
					yo + GRID_START.y + ySet,
					0, 0, width, height, width, height, 160, 160, 0xFF777777
			);
		});
	}

	public static Identifier fromBlock(Block block) {
		for (ResourceSupplier<MortarBlock> mortarBlockResourceSupplier : ModBlocks.MORTARS.asList()) {
			if (mortarBlockResourceSupplier.get() == block) {
				return Constants.prefix("textures/block/" + mortarBlockResourceSupplier.id().getPath() + ".png");
			}
		}
		return TextureManager.INTENTIONAL_MISSING_TEXTURE;
	}
}
