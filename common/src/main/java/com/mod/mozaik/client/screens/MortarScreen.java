package com.mod.mozaik.client.screens;

import com.mod.mozaik.Constants;
import com.mod.mozaik.client.GraphicsRenderHelper;
import com.mod.mozaik.client.ModKeyMappings;
import com.mod.mozaik.client.PhaseRenderable;
import com.mod.mozaik.client.buttons.*;
import com.mod.mozaik.client.widgets.AltColorWidget;
import com.mod.mozaik.client.widgets.HeldPolyominoWidget;
import com.mod.mozaik.client.widgets.MaterialWidget;
import com.mod.mozaik.client.widgets.PolyominoWidget;
import com.mod.mozaik.items.ShardItem;
import com.mod.mozaik.menus.MortarMenu;
import com.mod.mozaik.polyomino.Polyomino;
import com.mod.mozaik.polyomino.PrePolyominoShapes;
import com.mod.mozaik.polyomino.ShardMaterial;
import com.mod.mozaik.polyomino.Tessera;
import com.mod.mozaik.reg.ModTabs;
import com.mod.mozaik.util.FlatDirection;
import com.mod.mozaik.util.IMozaikKeyMapping;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.client.gui.navigation.ScreenDirection;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.block.Rotation;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.jspecify.annotations.NullMarked;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@NullMarked
public class MortarScreen extends AbstractContainerScreen<MortarMenu> {
	private static final int BACKGROUND_WIDTH = 242;
	private static final int BACKGROUND_HEIGHT = 256;

	public static final Vector2i GRID_START = new Vector2i(41, 76);
	public static final Vector2i BOWL_CENTER = new Vector2i(59, 25);
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

	private static final Vector2i SETTINGS = new Vector2i(221, 15);
	private static final Vector2i EDIT = new Vector2i(221, 30);
	private static final Vector2i LOCK = new Vector2i(221, 45);

	private static final Vector2i FLIP_VERTICAL = new Vector2i(153, 12);
	private static final Vector2i FLIP_HORIZONTAL = new Vector2i(174, 12);
	private static final Vector2i ROTATE_180 = new Vector2i(153, 33);
	private static final Vector2i ROTATE_270 = new Vector2i(174, 33);
	private static final Vector2i ROTATE_90 = new Vector2i(195, 33);

	private static final Vector2i TOGGLE_OPTION_START = new Vector2i(27, 12);

	public static final int LEFT_CLICK = 0;
	public static final int MIDDLE_CLICK = 2;
	public static final int RIGHT_CLICK = 1;

	public MozaikTool tool = MozaikTool.CURSOR;
	public Mode mode = Mode.MORTAR;

	public List<PolyominoWidget> polyomino = new ArrayList<>();
	public List<PolyominoWidget> selected = new ArrayList<>();
	public List<HeldPolyominoWidget> carried = new ArrayList<>();
	private final List<PhaseRenderable> renderableWidgets = new ArrayList<>();

	private @Nullable Vector2i selectionStart = null;
	private @Nullable EditBox titleBox = null;

	public MortarScreen(MortarMenu menu, Inventory playerInventory, Component title) {
		super(menu, playerInventory, title, BACKGROUND_WIDTH, BACKGROUND_HEIGHT);
		if (PersonalPreferences.getShape() == Polyomino.EMPTY) {
			PersonalPreferences.setShape(PrePolyominoShapes.values()[PersonalPreferences.getTemplate()].template.build(PersonalPreferences.getPrimaryColor(), UUID.randomUUID()));
		}
	}

	private final List<ResourceKey<ShardMaterial>> materials = new ArrayList<>();

	public List<ResourceKey<ShardMaterial>> getSortedList() {
		if (this.materials.isEmpty()) {
			ModTabs.TAB.get().getSearchTabDisplayItems().forEach(itemStack -> {
				if (itemStack.getItem() instanceof ShardItem item)
					this.materials.add(item.getMaterial());
			});

			if (!this.getShardSource().isCreative()) {
				this.materials.sort(Comparator.comparing(key -> this.getShardSource().getCount(key) == 0));
			}
		}
		return this.materials;
	}

	public MortarMenu.ShardSource getShardSource() {
		return this.menu.getShardSource();
	}

	public static long randomSeed() {
		long seed = 0L;
		if (Minecraft.getInstance().level != null) seed = Minecraft.getInstance().level.getRandom().nextLong();
		return seed;
	}

	public void markChanged() {
		this.menu.setRotatedPolyomino(this.polyomino);
	}

	public void removeFromSource(PolyominoWidget polyomino) {
		this.polyomino.remove(polyomino);
		if (this.menu.getShardSource().isCreative()) {
			this.markChanged();
		} else {
			this.menu.removeFromSource(polyomino.getPlacedPolyomino().polyomino().uuid());
			this.getMenu().getShardSource().giveItem(polyomino.getPlacedPolyomino().polyomino().material());
		}
	}

	public void addToSource(Polyomino.PlacedPolyomino polyomino) {
		if (this.menu.getShardSource().isCreative()) {
			this.markChanged();
		} else {
			this.menu.addToSource(polyomino);
			this.getMenu().getShardSource().takeItem(polyomino.polyomino().material());
		}
	}

	@Override
	protected void extractLabels(GuiGraphicsExtractor graphics, int xm, int ym) {

	}

	public List<PolyominoWidget> getPolyomino() {
		return this.polyomino;
	}

	@Override
	public boolean keyPressed(KeyEvent event) {
		if (event.isEscape() && this.shouldCloseOnEsc()) {
			this.onClose();
			return true;
		}

		if (this.getFocused() != null && this.getFocused().keyPressed(event)) {
			return true;
		}

		if (this.titleBox != null && this.titleBox.isFocused() && !this.titleBox.getValue().isEmpty() && event.isConfirmation()) {
			this.menu.sign(this.titleBox.getValue());
			this.minecraft.gui.setScreen(null);
			return true;
		}

		if (IMozaikKeyMapping.matches(ModKeyMappings.SELECT_ALL, event)) {
			this.selected.clear();
			this.selected.addAll(this.getPolyomino());
			return true;
		}

		for (MozaikTool tool : MozaikTool.values()) {
			if (IMozaikKeyMapping.matches(tool.getKeyMapping(), event)) {
				this.tool = tool;
				return true;
			}
		}

		if (IMozaikKeyMapping.matches(ModKeyMappings.DELETE, event)) {
			if (this.selected.isEmpty()) {
				Vector2i square = this.getGridForTaking();

				for (PolyominoWidget widget : this.getPolyomino()) {
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
				this.selected.clear();
			}

			return true;
		}

		for (int i = 1; i <= 9; ++i) {
			if (IMozaikKeyMapping.matches(ModKeyMappings.FAVOURITE.pick(i), event)) {
				Minecraft minecraft = Minecraft.getInstance();
				MouseHandler mouse = Objects.requireNonNull(minecraft).mouseHandler;
				float mouseX = (float) mouse.xpos() * (float) minecraft.getWindow().getGuiScaledWidth() / (float) minecraft.getWindow().getScreenWidth();
				float mouseY = (float) mouse.ypos() * (float) minecraft.getWindow().getGuiScaledHeight() / (float) minecraft.getWindow().getScreenHeight();

				Optional<GuiEventListener> child = this.getChildAt(mouseX, mouseY);
				if (child.isPresent()) {
					if (child.get() instanceof MaterialWidget materialWidget) {
						if (PersonalPreferences.getFavourite(i - 1).material().orElse(null) == materialWidget.getMaterial()) {
							PersonalPreferences.setFavouriteMaterial(i - 1, null);
						} else {
							PersonalPreferences.setFavouriteMaterial(i - 1, materialWidget.getMaterial());
						}
						return true;
					} else if (child.get() instanceof ShapeButton button) {
						if (PersonalPreferences.getFavourite(i - 1).template().orElse(-1) == button.getShape()) {
							PersonalPreferences.setFavouriteShape(i - 1, null);
						} else {
							PersonalPreferences.setFavouriteShape(i - 1, button.getShape());
						}
						return true;
					}
				}

				PersonalPreferences.Favourite favourite = PersonalPreferences.getFavourite(i - 1);
				ResourceKey<ShardMaterial> material = favourite.material().orElse(PersonalPreferences.getPrimaryColor());
				Integer template = favourite.template().orElse(PersonalPreferences.getTemplate());

				PersonalPreferences.setTemplate(template);
				PersonalPreferences.setPrimaryColor(this, material);
				PersonalPreferences.setShape(PrePolyominoShapes.values()[template].template.build(material, UUID.randomUUID()));
				return true;
			}
		}

		if (InputConstants.KEY_RETURN == event.key()) {
			this.selected.clear();
		}
		FocusNavigationEvent navigationEvent = switch (event.key()) {
			case 258 -> new FocusNavigationEvent.TabNavigation(!event.hasShiftDown());
			case 262 -> new FocusNavigationEvent.ArrowNavigation(ScreenDirection.RIGHT);
			case 263 -> new FocusNavigationEvent.ArrowNavigation(ScreenDirection.LEFT);
			case 264 -> new FocusNavigationEvent.ArrowNavigation(ScreenDirection.DOWN);
			case 265 -> new FocusNavigationEvent.ArrowNavigation(ScreenDirection.UP);
			default -> null;
		};

		if (navigationEvent != null) {
			ComponentPath focusPath = super.nextFocusPath(navigationEvent);
			if (focusPath == null && navigationEvent instanceof FocusNavigationEvent.TabNavigation) {
				this.clearFocus();
				focusPath = super.nextFocusPath(navigationEvent);
			}

			if (focusPath != null) {
				this.changeFocus(focusPath);
			}
		}

		return false;
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
				Map<HeldPolyominoWidget, Vector2i> map = this.getOffsetForPlacement(this.carried);
				if (map != null) {
					this.carried.forEach(heldPolyominoWidget -> {
						Vector2i vector2i = map.get(heldPolyominoWidget);
						this.placePolyomino(heldPolyominoWidget, vector2i);
						heldPolyominoWidget.setPolyomino(heldPolyominoWidget.getPolyomino().rebuild(heldPolyominoWidget.getPolyomino().material()));
					});
					MortarMenu.ShardSource shardSource = this.getShardSource();
					if (!shardSource.isCreative()) {
						int count = shardSource.getCount(this.carried.getFirst().getPolyomino().material());
						int carry = this.carried.size();
						if (count < carry) {
							for (int i = 0; i < carry; i++) {
								if (i >= count) this.carried.removeLast();
							}
						}
					}
				}
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
			ResourceKey<ShardMaterial> color = PersonalPreferences.getPrimaryColor();
			PersonalPreferences.setPrimaryColor(this, PersonalPreferences.getSecondaryColor());
			PersonalPreferences.setSecondaryColor(color);
			return true;
		}

		if (this.tool == MozaikTool.SELECT) {
			this.selectionStart = new Vector2i((int) event.x(), (int) event.y());
			return true;
		}

		Vector2i square = this.getGridForTaking();

		for (PolyominoWidget widget : this.getPolyomino()) {
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

			for (PolyominoWidget widget : this.getPolyomino()) {
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

		this.getPolyomino().add(widget);
		this.addToSource(widget.getPlacedPolyomino());
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
	public Map<HeldPolyominoWidget, Vector2i> getOffsetForPlacement(List<HeldPolyominoWidget> widgets) {
		int gridX = this.leftPos + GRID_START.x;
		int gridY = this.topPos + GRID_START.y;

		HeldPolyominoWidget first = widgets.getFirst();

		Vector2f center = first.heldPos();
		float fX = (center.x - gridX) / Tessera.TESSERA_SIZE;
		float fY = (center.y - gridY) / Tessera.TESSERA_SIZE;

		int x = Math.round(fX);
		int y = Math.round(fY);

		Map<HeldPolyominoWidget, Vector2i> test = new HashMap<>();
		for (HeldPolyominoWidget widget : widgets) {
			Vector2i onCheck = this.getClosestGrid(widget, 0, 0);
			if (onCheck == null) break;
			else test.put(widget, onCheck);
		}
		if (test.size() == widgets.size()) return test;

		Map<FlatDirection, Float> distanceCheck = new HashMap<>();

		for (FlatDirection direction : FlatDirection.values()) {
			distanceCheck.put(direction, new Vector2f(fX, fY).distanceSquared(x + direction.getRelativeX(), y + direction.getRelativeY()));
		}

		for (int i = 0; i < 8; i++) {
			FlatDirection shortest = FlatDirection.DOWN;
			float smallest = Float.MAX_VALUE;
			for (FlatDirection direction : FlatDirection.values()) {
				if (!distanceCheck.containsKey(direction)) continue;

				float distance = distanceCheck.get(direction);
				if (distance < smallest) {
					shortest = direction;
					smallest = distance;
				}
			}

			test = new HashMap<>();
			for (HeldPolyominoWidget widget : widgets) {
				Vector2i onCheck = this.getClosestGrid(widget, shortest.getRelativeX(), shortest.getRelativeY());
				if (onCheck == null) break;
				else test.put(widget, onCheck);
			}
			if (test.size() == widgets.size()) return test;
			else distanceCheck.remove(shortest);
		}

		return null;
	}

	@Nullable
	public Vector2i getClosestGrid(HeldPolyominoWidget polyominoWidget, int diffX, int diffY) {
		int gridX = this.leftPos + GRID_START.x;
		int gridY = this.topPos + GRID_START.y;

		Vector2f center = polyominoWidget.heldPos();

		float fX = (center.x - gridX) / Tessera.TESSERA_SIZE;
		float fY = (center.y - gridY) / Tessera.TESSERA_SIZE;

		int x = Math.round(fX);
		int y = Math.round(fY);

		Vector2i grid = new Vector2i(x + diffX, y + diffY);
		if (this.isOutOfBounds(grid)) return null; // Out of bounds

		boolean canFit = true;
		for (Tessera.PlacedTessera entry : polyominoWidget.placedTessera()) {
			int relativeX = grid.x + entry.x();
			int relativeY = grid.y + entry.y();

			if (this.isOutOfBounds(relativeX, relativeY)) {
				canFit = false;
				break; // Out of bounds
			}

			for (PolyominoWidget widget : this.getPolyomino()) {
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

						if (!this.isOutOfBounds(rX, rY) && rX == relativeX && rY == relativeY) {
							canFit = false;
							break;
						}
					}
					if (!canFit) break; // Occupied
				}
				if (!canFit) break; // Occupied
			}
		}
		if (canFit) return grid;
		else return null;
	}

	protected boolean isOutOfBounds(Vector2i grid) {
		return isOutOfBounds(grid.x, grid.y);
	}

	protected boolean isOutOfBounds(int x, int y) {
		if (x >= 0 && y >= 0 && x <= 15 && y <= 15) return false;
		if (x < -1 || y < -1 || x > 16 || y > 16) return true;

		if (x == 16 && y == -1) return !this.menu.getMap().containsKey(FlatDirection.UP_RIGHT);
		if (x == 16 && y == 16) return !this.menu.getMap().containsKey(FlatDirection.DOWN_RIGHT);
		if (x == -1 && y == 16) return !this.menu.getMap().containsKey(FlatDirection.DOWN_LEFT);
		if (x == -1 && y == -1) return !this.menu.getMap().containsKey(FlatDirection.UP_LEFT);

		if (y == -1) return !this.menu.getMap().containsKey(FlatDirection.UP);
		if (x == 16) return !this.menu.getMap().containsKey(FlatDirection.RIGHT);
		if (y == 16) return !this.menu.getMap().containsKey(FlatDirection.DOWN);
		return !this.menu.getMap().containsKey(FlatDirection.LEFT);
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
					graphics.blitTessera(polyomino.material(), tessera.tessera(), polyomino.uuid().getMostSignificantBits(), index.get(), 0xFF999999);
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
		int ordinal = Math.max(this.getSortedList().indexOf(PersonalPreferences.getPrimaryColor()) - by, 0);
		PersonalPreferences.setPrimaryColor(this, this.getSortedList().get(ordinal));
	}

	public void materialDownBy(int by) {
		int ordinal = Math.min(this.getSortedList().indexOf(PersonalPreferences.getPrimaryColor()) + by, this.getSortedList().size() - 1);
		PersonalPreferences.setPrimaryColor(this, this.getSortedList().get(ordinal));
	}

	public static void templateUpBy(int by) {
		PersonalPreferences.setTemplate(Math.max(PersonalPreferences.getTemplate() - by, 0));
		PersonalPreferences.setShape(PrePolyominoShapes.values()[PersonalPreferences.getTemplate()].template.build(PersonalPreferences.getPrimaryColor(), UUID.randomUUID()));
	}

	public static void templateDownBy(int by) {
		PersonalPreferences.setTemplate(Math.min(PersonalPreferences.getTemplate() + by, PrePolyominoShapes.values().length - 1));
		PersonalPreferences.setShape(PrePolyominoShapes.values()[PersonalPreferences.getTemplate()].template.build(PersonalPreferences.getPrimaryColor(), UUID.randomUUID()));
	}

	@Override
	public void rebuildWidgets() {
		super.rebuildWidgets();
	}

	@Override
	protected void init() {
		super.init();
		MortarScreen.this.titleBox = null;

		switch (this.mode) {
			case MORTAR -> {
				this.addRenderableWidget(new CreatePolyominoButton(this.leftPos + BOWL_CENTER.x, this.topPos + BOWL_CENTER.y, this));
				this.addRenderableWidget(new AltColorWidget(this, this.leftPos + MINI_BOWL_ITEM.x, this.topPos + MINI_BOWL_ITEM.y));

				this.addRenderableWidget(new ToolButton(this, CHISEL, SpriteButton.SpriteSet.CHISEL, MozaikTool.CHISEL));
				this.addRenderableWidget(new ToolButton(this, CURSOR, SpriteButton.SpriteSet.CURSOR, MozaikTool.CURSOR));
				this.addRenderableWidget(new ToolButton(this, SWAP, SpriteButton.SpriteSet.SWAP, MozaikTool.SWAP));
				this.addRenderableWidget(new ToolButton(this, PICKER, SpriteButton.SpriteSet.PICKER, MozaikTool.PICKER));
				this.addRenderableWidget(new ToolButton(this, WAND, SpriteButton.SpriteSet.WAND, MozaikTool.WAND));
				this.addRenderableWidget(new ToolButton(this, SELECT, SpriteButton.SpriteSet.SELECT, MozaikTool.SELECT));
			}
			case SETTINGS -> {
				int i = 0;
				for (PersonalPreferences.ToggleOption toggleOption : PersonalPreferences.getOptions()) {
					this.addRenderableWidget(new ToggleButton(this, new Vector2i(TOGGLE_OPTION_START.x, TOGGLE_OPTION_START.y + 16 * i++), toggleOption));
				}
			}
			case EDIT -> {
				this.addRenderableWidget(new CreatePolyominoButton(this.leftPos + BOWL_CENTER.x, this.topPos + BOWL_CENTER.y, this));
				this.addRenderableWidget(new AltColorWidget(this, this.leftPos + MINI_BOWL_ITEM.x, this.topPos + MINI_BOWL_ITEM.y));

				this.addRenderableWidget(new EditButtons(this, FLIP_VERTICAL, SpriteButton.SpriteSet.FLIP_VERTICAL, EditButtons.Edition.FLIP_VERTICAL));
				this.addRenderableWidget(new EditButtons(this, FLIP_HORIZONTAL, SpriteButton.SpriteSet.FLIP_HORIZONTAL, EditButtons.Edition.FLIP_HORIZONTAL));
				this.addRenderableWidget(new EditButtons(this, ROTATE_180, SpriteButton.SpriteSet.ROTATE_180, EditButtons.Edition.ROTATE_180));
				this.addRenderableWidget(new EditButtons(this, ROTATE_270, SpriteButton.SpriteSet.ROTATE_270, EditButtons.Edition.ROTATE_270));
				this.addRenderableWidget(new EditButtons(this, ROTATE_90, SpriteButton.SpriteSet.ROTATE_90, EditButtons.Edition.ROTATE_90));
			}
			case LOCK -> {
				int xo = (this.width - this.imageWidth) / 2;
				int yo = (this.height - this.imageHeight) / 2;
				MortarScreen.this.titleBox = this.addRenderableWidget(new EditBox(
						this.minecraft.font,
						xo + 64,
						yo + 28,
						114,
						20,
						Component.translatable("book.sign.titlebox")
				));
				MortarScreen.this.titleBox.setMaxLength(15);
				MortarScreen.this.titleBox.setBordered(false);
				MortarScreen.this.titleBox.setCentered(true);
				MortarScreen.this.titleBox.setTextShadow(true);
				MortarScreen.this.titleBox.setTextColor(0xFFFFFFFF);
				//titleBox.setResponder((value) -> finalizeButton.active = !StringUtil.isBlank(value));
				MortarScreen.this.titleBox.setValue("");
				MortarScreen.this.setFocused(MortarScreen.this.titleBox);
			}
		}

		this.addRenderableWidget(new TabButton(this, SETTINGS, Mode.SETTINGS));
		this.addRenderableWidget(new TabButton(this, EDIT, Mode.EDIT));
		this.addRenderableWidget(new TabButton(this, LOCK, Mode.LOCK));

		this.addRenderableWidget(new ClickableButton(this, MATERIAL_BAR_UP, SpriteButton.SpriteSet.UP_ARROW) {
			@Override
			public void onUnblockedPress(InputWithModifiers inputWithModifiers) {
				materialUpBy(inputWithModifiers.hasShiftDown() ? MortarScreen.this.getSortedList().size() : 9);
			}

			@Override
			public boolean isBlocked() {
				return PersonalPreferences.minMaterial(MortarScreen.this) == 0;
			}
		});

		this.addRenderableWidget(new ClickableButton(this, MATERIAL_BAR_DOWN, SpriteButton.SpriteSet.DOWN_ARROW) {
			@Override
			public void onUnblockedPress(InputWithModifiers inputWithModifiers) {
				materialDownBy(inputWithModifiers.hasShiftDown() ? MortarScreen.this.getSortedList().size() : 9);
			}

			@Override
			public boolean isBlocked() {
				return PersonalPreferences.minMaterial(MortarScreen.this) + 9 == MortarScreen.this.getSortedList().size();
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

		for (int i = 0; i < 9; i++) {
			this.addRenderableWidget(new MaterialWidget(this, this.leftPos + MATERIAL_BAR.x, this.topPos + MATERIAL_BAR.y + i * 18, i));
		}

		for (int i = 0; i < 9; i++) {
			this.addRenderableWidget(new ShapeButton(this, this.leftPos + SHAPE_BAR.x, this.topPos + SHAPE_BAR.y + i * 18, i));
		}

		if (this.mode == Mode.SETTINGS) return;

		this.menu.getRotatedPolyomino().forEach(placedPolyomino -> {
			int gridX = this.leftPos + GRID_START.x;
			int gridY = this.topPos + GRID_START.y;

			PolyominoWidget polyominoWidget = new PolyominoWidget(this, gridX + placedPolyomino.x() * Tessera.TESSERA_SIZE, gridY + placedPolyomino.y() * Tessera.TESSERA_SIZE, placedPolyomino);
			this.getPolyomino().add(polyominoWidget);
			this.addRenderableWidget(polyominoWidget);
		});
	}

	@Override
	public void setFocused(@Nullable GuiEventListener focused) {
		if (focused instanceof TabButton button && button.getMode() == Mode.LOCK && this.mode == Mode.LOCK) {
			super.setFocused(this.titleBox);
			return;
		}
		super.setFocused(focused);
	}

	@Override
	public boolean isInGameUi() {
		return true;
	}

	@Override
	protected void setInitialFocus() {
		if (this.titleBox != null) this.setInitialFocus(this.titleBox);
		else super.setInitialFocus();
	}

	@Override
	protected void clearWidgets() {
		this.getPolyomino().clear();
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
		graphics.blit(RenderPipelines.GUI_TEXTURED, this.mode.getTexture(), xo, yo, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);

		if (this.mode == Mode.SETTINGS) return;
		graphics.blit(RenderPipelines.GUI_TEXTURED, this.menu.getTexture(), xo + GRID_START.x, yo + GRID_START.y, 0.0F, 0.0F, 160, 160, 160, 160);

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

		if (this.mode == Mode.LOCK) {
			graphics.text(
					this.font,
					Component.literal("Enter Mosaic Title:").withStyle(ChatFormatting.GRAY),
					xo + this.imageWidth / 2 - this.font.width(Component.literal("Enter Mosaic Title:").withStyle(ChatFormatting.GRAY)) / 2,
					yo + 16,
					-1,
					true
			);
			graphics.text(
					this.font,
					Component.translatable("book.byAuthor", Objects.requireNonNull(Minecraft.getInstance().player).getName()).withStyle(ChatFormatting.GRAY),
					xo + this.imageWidth / 2 - this.font.width(Component.translatable("book.byAuthor", Minecraft.getInstance().player.getName()).withStyle(ChatFormatting.GRAY)) / 2,
					yo + 40,
					-1,
					true
			);
		}
	}

	public enum Mode implements StringRepresentable {
		MORTAR,
		SETTINGS,
		EDIT,
		LOCK;

		private final Identifier texture;

		Mode() {
			this.texture = Constants.prefix("textures/gui/container/")
					.withSuffix(this.getSerializedName())
					.withSuffix(".png");
		}

		public Identifier getTexture() {
			return this.texture;
		}

		public String asTranslationString() {
			return "tooltip.mozaik.mode." + this.getSerializedName();
		}

		@Override
		public String getSerializedName() {
			return this.name().toLowerCase(Locale.ROOT);
		}
	}
}
