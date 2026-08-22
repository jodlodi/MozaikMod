package com.mod.mozaik.client.screens;

import com.mod.mozaik.Constants;
import com.mod.mozaik.blocks.MortarBlock;
import com.mod.mozaik.client.GraphicsRenderHelper;
import com.mod.mozaik.client.PhaseRenderable;
import com.mod.mozaik.client.buttons.*;
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

	private static final Vector2i SHAPE_BAR = new Vector2i(222, 75);
	private static final Vector2i SHAPE_BAR_UP = new Vector2i(222, 66);
	private static final Vector2i SHAPE_BAR_DOWN = new Vector2i(222, 236);

	private static final Vector2i PICKER = new Vector2i(154, 11);
	private static final Vector2i CURSOR = new Vector2i(154, 32);
	private static final Vector2i SELECT = new Vector2i(175, 11);
	private static final Vector2i SWAP = new Vector2i(175, 32);
	private static final Vector2i WAND = new Vector2i(196, 11);
	private static final Vector2i CHISEL = new Vector2i(196, 32);

	public static final int LEFT_CLICK = 0;
	public static final int MIDDLE_CLICK = 2;
	public static final int RIGHT_CLICK = 1;

	public MozaikTool tool = MozaikTool.CURSOR;

	public List<PolyominoWidget> polyominos = new ArrayList<>();
	public List<PolyominoWidget> selected = new ArrayList<>();
	public List<HeldPolyominoWidget> carried = new ArrayList<>();
	private final List<PhaseRenderable> renderableWidgets = new ArrayList<>();

	private @Nullable Vector2i selectionStart = null;

	public MortarScreen(MortarMenu menu, Inventory playerInventory, Component title) {
		super(menu, playerInventory, title, BACKGROUND_WIDTH, BACKGROUND_HEIGHT);
		if (PersonalPreferences.getShape() == Polyomino.EMPTY) {
			PersonalPreferences.setShape(PrePolyominoShapes.SQUARE.template.build(PersonalPreferences.getPrimaryColor(), 10L));
		}
	}

	public static long randomSeed() {
		long seed = 0L;
		if (Minecraft.getInstance().level != null) seed = Minecraft.getInstance().level.getRandom().nextLong();
		return seed;
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
		if (event.isSelectAll()) {
			this.selected.clear();
			this.selected.addAll(this.polyominos);
			return true;
		}

		for (MozaikTool tool : MozaikTool.values()) {
			if (tool.getKeyMapping().matches(event)) {
				this.tool = tool;
				return true;
			}
		}

		if (InputConstants.KEY_DELETE == event.key()) {
			if (this.selected.isEmpty()) {
				Vector2i square = this.getGridForTaking();

				for (PolyominoWidget widget : this.polyominos) {
					int widgetX = widget.gridX();
					int widgetY = widget.gridY();
					for (Tessera.PlacedTessera tessera : widget.getPlacedPolyomino().polyomino().placedTessera()) {
						int rX = widgetX + tessera.x();
						int rY = widgetY + tessera.y();
						if (rX == square.x && rY == square.y) {
							MozaikTool.useOn(this, event.hasShiftDown(), widget, MozaikTool.CHISEL);
							return true;
						}
					}
				}
			} else {
				MozaikTool.useOn(this, event.hasShiftDown(), this.selected, MozaikTool.CHISEL);
			}

			return true;
		}

		for (int i = 0; i < 9; ++i) {
			if (this.minecraft.options.keyHotbarSlots[i].matches(event)) {
				if (!event.hasShiftDown()) {
					PersonalPreferences.setPrimaryColor(this, TesseraMaterial.values()[PersonalPreferences.minMaterial() + i]);
				} else {
					PersonalPreferences.setTemplate(PersonalPreferences.minTemplate() + i);
					PersonalPreferences.setShape(PrePolyominoShapes.values()[PersonalPreferences.getTemplate()].template.build(
							PersonalPreferences.getPrimaryColor(),
							MortarScreen.randomSeed()
					));
				}
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
			Rotation rotation = scrollY > 0 ? Rotation.CLOCKWISE_90 : Rotation.COUNTERCLOCKWISE_90;
			this.carried.forEach(heldPolyominoWidget -> heldPolyominoWidget.setPolyomino(heldPolyominoWidget.rotate(rotation)));
			return true;
		}

		Optional<GuiEventListener> child = this.getChildAt(x, y);
		if (child.isPresent() && child.get().mouseScrolled(x, y, scrollX, scrollY)) {
			return true;
		}

		return super.mouseScrolled(x, y, scrollX, scrollY);
	}

	@Override
	public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
		int click = event.button();

		if (!this.carried.isEmpty()) {
			if (click == LEFT_CLICK) {
				this.carried.forEach(heldPolyominoWidget -> {
					Vector2i square = this.getGridForPlacement(heldPolyominoWidget);
					if (square != null) {
						this.placePolyomino(heldPolyominoWidget, square);
						heldPolyominoWidget.setPolyomino(heldPolyominoWidget.getPolyomino().rebuild(heldPolyominoWidget.getPolyomino().material()));
					}
				});
				return true;
			} else if (click == MIDDLE_CLICK) {
				this.carried.removeIf(widget -> {
					this.removeWidget(widget);
					return true;
				});
				return true;
			}
			return true;
		}

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
			TesseraMaterial color = PersonalPreferences.getPrimaryColor();
			PersonalPreferences.setPrimaryColor(this, PersonalPreferences.getSecondaryColor());
			PersonalPreferences.setSecondaryColor(color);
			return true;
		}

		if (this.tool == MozaikTool.SELECT) {
			this.selectionStart = new Vector2i((int) event.x(), (int) event.y());
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
					MozaikTool.useOn(this, event.hasShiftDown(), widget, this.tool);
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public boolean mouseReleased(MouseButtonEvent event) {
		if (this.selectionStart != null) {
			if (!event.hasShiftDown()) this.selected.clear();

			int minX = Math.min(this.selectionStart.x, (int) Minecraft.getInstance().mouseHandler.getScaledXPos(Minecraft.getInstance().getWindow()));
			int minY = Math.min(this.selectionStart.y, (int) Minecraft.getInstance().mouseHandler.getScaledYPos(Minecraft.getInstance().getWindow()));
			int maxX = Math.max(this.selectionStart.x, (int) Minecraft.getInstance().mouseHandler.getScaledXPos(Minecraft.getInstance().getWindow()));
			int maxY = Math.max(this.selectionStart.y, (int) Minecraft.getInstance().mouseHandler.getScaledYPos(Minecraft.getInstance().getWindow()));

			for (PolyominoWidget widget : this.polyominos) {
				int widgetX = widget.gridX();
				int widgetY = widget.gridY();
				for (Tessera.PlacedTessera tessera : widget.getPlacedPolyomino().polyomino().placedTessera()) {
					int rX = widgetX + tessera.x();
					int rY = widgetY + tessera.y();

					int xPos = this.leftPos + GRID_START.x + rX * Tessera.TESSERA_SIZE + 5;
					int yPos = this.topPos + GRID_START.y + rY * Tessera.TESSERA_SIZE + 5;

					if (xPos > minX && yPos > minY && xPos < maxX && yPos < maxY) {
						if (!this.selected.contains(widget)) this.selected.add(widget);
						break;
					}
				}
			}

			this.selectionStart = null;
		}
		return super.mouseReleased(event);
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
		List<HeldPolyominoWidget> wrong = new ArrayList<>();
		this.renderableWidgets.forEach(phaseRenderable -> {
			if (phaseRenderable instanceof SpriteButton button) {
				button.tick();
			} else if (phaseRenderable instanceof HeldPolyominoWidget widget && !this.carried.contains(widget)) {
				wrong.add(widget);
			}
		});
		wrong.forEach(this::removeWidget);
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

	protected void renderSelection(GuiGraphicsExtractor graphicsExtractor) {
		GraphicsRenderHelper graphics = new GraphicsRenderHelper(graphicsExtractor);
		this.selected.forEach(polyominoWidget -> {
			PolyominoWidget.fill(
					graphics,
					polyominoWidget.getPlacedPolyomino().polyomino(),
					polyominoWidget.getX(),
					polyominoWidget.getY(),
					0x500094FF
			);
			PolyominoWidget.selection(
					graphics,
					polyominoWidget.getPlacedPolyomino().polyomino(),
					polyominoWidget.getX(),
					polyominoWidget.getY()
			);
		});

		if (this.selectionStart != null) {
			int minX = this.selectionStart.x;
			int minY = this.selectionStart.y;
			int maxX = (int) Minecraft.getInstance().mouseHandler.getScaledXPos(Minecraft.getInstance().getWindow());
			int maxY = (int) Minecraft.getInstance().mouseHandler.getScaledYPos(Minecraft.getInstance().getWindow());

			graphics.fill(minX, minY, maxX, maxY, 0x500094FF);

			graphics.selection(minX, minY, minX + 1, maxY);
			graphics.selection(maxX, minY, maxX + 1, maxY);
			graphics.selection(minX, minY, maxX, minY + 1);
			graphics.selection(minX, maxY, maxX, maxY + 1);
		}
	}

	public void materialUpBy(int by) {
		int ordinal = Math.max(PersonalPreferences.getPrimaryColor().ordinal() - by, 0);
		PersonalPreferences.setPrimaryColor(this, TesseraMaterial.values()[ordinal]);
	}

	public void materialDownBy(int by) {
		int ordinal = Math.min(PersonalPreferences.getPrimaryColor().ordinal() + by, TesseraMaterial.values().length - 1);
		PersonalPreferences.setPrimaryColor(this, TesseraMaterial.values()[ordinal]);
	}

	public static void templateUpBy(int by) {
		PersonalPreferences.setTemplate(Math.max(PersonalPreferences.getTemplate() - by, 0));
		PersonalPreferences.setShape(PrePolyominoShapes.values()[PersonalPreferences.getTemplate()].template.build(PersonalPreferences.getPrimaryColor(), randomSeed()));
	}

	public static void templateDownBy(int by) {
		PersonalPreferences.setTemplate(Math.min(PersonalPreferences.getTemplate() + by, PrePolyominoShapes.values().length - 1));
		PersonalPreferences.setShape(PrePolyominoShapes.values()[PersonalPreferences.getTemplate()].template.build(PersonalPreferences.getPrimaryColor(), randomSeed()));
	}

	@Override
	protected void init() {
		super.init();

		this.addRenderableWidget(new CreatePolyominoButton(this.leftPos + BOWL_CENTER.x, this.topPos + BOWL_CENTER.y, this));

		this.addRenderableWidget(new ClickableButton(this, MATERIAL_BAR_UP, SpriteButton.SpriteSet.UP_ARROW) {
			@Override
			public void onUnblockedPress(InputWithModifiers inputWithModifiers) {
				materialUpBy(inputWithModifiers.hasShiftDown() ? TesseraMaterial.values().length : 9);
			}

			@Override
			public boolean isBlocked() {
				return PersonalPreferences.minMaterial() == 0;
			}
		});

		this.addRenderableWidget(new ClickableButton(this, MATERIAL_BAR_DOWN, SpriteButton.SpriteSet.DOWN_ARROW) {
			@Override
			public void onUnblockedPress(InputWithModifiers inputWithModifiers) {
				materialDownBy(inputWithModifiers.hasShiftDown() ? TesseraMaterial.values().length : 9);
			}

			@Override
			public boolean isBlocked() {
				return PersonalPreferences.minMaterial() + 9 == TesseraMaterial.values().length;
			}
		});

		this.addRenderableWidget(new ClickableButton(this, SHAPE_BAR_UP, SpriteButton.SpriteSet.UP_ARROW) {
			@Override
			public void onUnblockedPress(InputWithModifiers inputWithModifiers) {
				templateUpBy(inputWithModifiers.hasShiftDown() ? PrePolyominoShapes.values().length : 9);
			}

			@Override
			public boolean isBlocked() {
				return PersonalPreferences.minTemplate() == 0;
			}
		});

		this.addRenderableWidget(new ClickableButton(this, SHAPE_BAR_DOWN, SpriteButton.SpriteSet.DOWN_ARROW) {
			@Override
			public void onUnblockedPress(InputWithModifiers inputWithModifiers) {
				templateDownBy(inputWithModifiers.hasShiftDown() ? PrePolyominoShapes.values().length : 9);
			}

			@Override
			public boolean isBlocked() {
				return PersonalPreferences.minTemplate() + 9 == PrePolyominoShapes.values().length;
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

		for (int i = 0; i < 9; i++) {
			this.addRenderableWidget(new ShapeButton(this, this.leftPos + SHAPE_BAR.x, this.topPos + SHAPE_BAR.y + i * 18, i));
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
