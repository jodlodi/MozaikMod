package com.mod.mozaik.client.screens;

import com.mod.mozaik.Constants;
import com.mod.mozaik.blocks.MortarBlock;
import com.mod.mozaik.client.GraphicsRenderHelper;
import com.mod.mozaik.client.PhaseRenderable;
import com.mod.mozaik.client.buttons.ClickableButton;
import com.mod.mozaik.client.buttons.CreatePolyominoButton;
import com.mod.mozaik.client.buttons.SpriteButton;
import com.mod.mozaik.client.buttons.ToolButton;
import com.mod.mozaik.client.widgets.HeldPolyominoWidget;
import com.mod.mozaik.client.widgets.PolyominoWidget;
import com.mod.mozaik.menus.MaterialSlot;
import com.mod.mozaik.menus.MortarMenu;
import com.mod.mozaik.networking.bidirectional.UpdateGlueBidirectional;
import com.mod.mozaik.platform.Services;
import com.mod.mozaik.polyomino.Polyomino;
import com.mod.mozaik.polyomino.PrePolyominoShapes;
import com.mod.mozaik.polyomino.Tessera;
import com.mod.mozaik.polyomino.TesseraMaterial;
import com.mod.mozaik.reg.ModBlocks;
import com.mod.mozaik.reg.ModItems;
import com.mod.mozaik.reg.ResourceSupplier;
import com.mod.mozaik.util.FlatDirection;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MouseHandler;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

@NullMarked
public class MortarScreen extends AbstractContainerScreen<MortarMenu> {
	private static final Identifier MORTAR_LOCATION = Constants.prefix("textures/gui/container/mortar.png");
	private static final int BACKGROUND_WIDTH = 242;
	private static final int BACKGROUND_HEIGHT = 256;
	private static final Vector2i GRID_START = new Vector2i(41, 76);
	private static final Vector2i BOWL_CENTER = new Vector2i(59, 26);
	private static final Vector2i MATERIAL_BAR = new Vector2i(4, 75);
	private static final Vector2i MATERIAL_BAR_UP = new Vector2i(4, 66);
	private static final Vector2i MATERIAL_BAR_DOWN = new Vector2i(4, 236);

	private static final Vector2i CHISEL = new Vector2i(154, 11);
	private static final Vector2i CURSOR = new Vector2i(154, 32);
	private static final Vector2i SWAP = new Vector2i(175, 11);
	private static final Vector2i PICKER = new Vector2i(175, 32);
	private static final Vector2i WAND = new Vector2i(196, 11);
	private static final Vector2i SELECT = new Vector2i(196, 32);

	public static final SimpleContainer MATERIALS = new SimpleContainer(9);

	public static final int LEFT_CLICK = 0;
	public static final int MIDDLE_CLICK = 2;
	public static final int RIGHT_CLICK = 1;

	int template = 0;
	public int mode = 0;
	public int from = 0;
	public int to = 9;

	public List<PolyominoWidget> polyominos = new ArrayList<>();
	public @Nullable HeldPolyominoWidget selected;
	public @Nullable CreatePolyominoButton addButton;
	private final List<PhaseRenderable> renderableWidgets = new ArrayList<>();
	public final List<MaterialSlot> materialSlots = new ArrayList<>();

	public MortarScreen(MortarMenu menu, Inventory playerInventory, Component title) {
		super(menu, playerInventory, title, BACKGROUND_WIDTH, BACKGROUND_HEIGHT);

		for (int i = 0; i < MATERIALS.getContainerSize(); i++) {
			MATERIALS.setItem(i, ModItems.SHARDS.asList().get(i).get().getDefaultInstance());
			MaterialSlot slot = new MaterialSlot(playerInventory.player, MATERIALS, i, MATERIAL_BAR.x, MATERIAL_BAR.y + i * 18);
			this.materialSlots.add(slot);
			this.menu.addSlot(slot);
		}
	}

	protected void markChanged() {
		if (this.menu.getMortar() != null) {
			Services.NETWORK.sendToServer(new UpdateGlueBidirectional(this.polyominos.stream().map(PolyominoWidget::getPlacedPolyomino).toList(), this.menu.getMortar().getBlockPos()));
		}
	}

	@Override
	protected void extractLabels(GuiGraphicsExtractor graphics, int xm, int ym) {

	}

	@Override
	public boolean mouseScrolled(double x, double y, double scrollX, double scrollY) {
		if (this.hoveredSlot instanceof MaterialSlot) {
			if (scrollY > 0) {
				MaterialSlot.scrollUpBy(this, (int) scrollY);
				return true;
			} else if (scrollY < 0) {
				MaterialSlot.scrollDownBy(this, (int) - scrollY);
				return true;
			}
		}
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
				Vector2i square = this.getGridForPlacement();
				if (square != null) {
					this.placePolyomino(this.selected, square);
				}
				this.selected.remove();
				return true;
			} else if (click == MIDDLE_CLICK && this.addButton != null) {
				Vector2i square = this.getGridForPlacement();
				if (square != null) {
					this.placePolyomino(this.selected, square);
					this.selected.polyomino = this.addButton.getTemplate().build(this.selected.polyomino.material(), Objects.requireNonNull(Minecraft.getInstance().level).getRandom().nextLong());
				}
				return true;
			}
			return true;
		} else {
			Vector2i square = this.getGridForTaking();

			for (PolyominoWidget widget : this.polyominos) {
				int widgetX = widget.gridX();
				int widgetY = widget.gridY();
				for (Tessera.PlacedTessera tessera : widget.getPlacedPolyomino().polyomino().placedTessera()) {
					int rX = widgetX + tessera.x();
					int rY = widgetY + tessera.y();
					if (rX == square.x && rY == square.y) {
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

	protected void placePolyomino(HeldPolyominoWidget selected, Vector2i square) {
		Vector2i gridPos = this.getGridPos(square);

		PolyominoWidget widget = new PolyominoWidget(this, gridPos.x, gridPos.y, new Polyomino.PlacedPolyomino(selected.polyomino, square.x, square.y));
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
	public Vector2i getGridForPlacement() {
		if (this.selected != null) {
			Minecraft minecraft = Minecraft.getInstance();
			MouseHandler mouse = Objects.requireNonNull(minecraft).mouseHandler;
			float mouseX = (float) mouse.xpos() * (float) minecraft.getWindow().getGuiScaledWidth() / (float) minecraft.getWindow().getScreenWidth();
			float mouseY = (float) mouse.ypos() * (float) minecraft.getWindow().getGuiScaledHeight() / (float) minecraft.getWindow().getScreenHeight();

			int gridX = this.leftPos + GRID_START.x;
			int gridY = this.topPos + GRID_START.y;

			Vector2f center = this.selected.polyomino.getGridCenter();
			mouseX -= center.x * Tessera.TESSERA_SIZE;
			mouseY -= center.y * Tessera.TESSERA_SIZE;

			int x = (int) (mouseX - gridX) / Tessera.TESSERA_SIZE;
			int y = (int) (mouseY - gridY) / Tessera.TESSERA_SIZE;
			Vector2i grid = new Vector2i(x, y);

			for (int offsetX : new int[]{0, 1, -1}) {
				for (int offsetY : new int[]{0, 1, -1}) {
					grid = new Vector2i(grid.x + offsetX, grid.y + offsetY);

					if (grid.x < -1 || grid.y < -1 || grid.x >= 17 || grid.y >= 17) {
						continue; // Out of bounds
					}

					boolean canFit = true;
					for (Tessera.PlacedTessera entry : this.selected.polyomino.placedTessera()) {
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
					graphics.blitTessera(polyomino.material(), tessera.tessera(), polyomino.seed(), index.get());
				}
			}));
		})));
	}

	@Override
	protected void init() {
		super.init();

		PrePolyominoShapes[] values = PrePolyominoShapes.values();
		this.addButton = this.addRenderableWidget(new CreatePolyominoButton(this.leftPos + BOWL_CENTER.x, this.topPos + BOWL_CENTER.y, this, values[this.template].template));

		this.addRenderableWidget(new ClickableButton(this, MATERIAL_BAR_UP, SpriteButton.SpriteSet.UP_ARROW) {
			@Override
			public void onUnblockedPress(InputWithModifiers inputWithModifiers) {
				MaterialSlot.scrollUpBy(MortarScreen.this, inputWithModifiers.hasShiftDown() ? TesseraMaterial.values().length : 9);
			}

			@Override
			public boolean isBlocked() {
				return MortarScreen.this.from == 0;
			}
		});

		this.addRenderableWidget(new ClickableButton(this, MATERIAL_BAR_DOWN, SpriteButton.SpriteSet.DOWN_ARROW) {
			@Override
			public void onUnblockedPress(InputWithModifiers inputWithModifiers) {
				MaterialSlot.scrollDownBy(MortarScreen.this, inputWithModifiers.hasShiftDown() ? TesseraMaterial.values().length : 9);
			}

			@Override
			public boolean isBlocked() {
				return MortarScreen.this.to == TesseraMaterial.values().length;
			}
		});

		this.addRenderableWidget(new ToolButton(this, CHISEL, SpriteButton.SpriteSet.CHISEL, 0));
		this.addRenderableWidget(new ToolButton(this, CURSOR, SpriteButton.SpriteSet.CURSOR, 1));
		this.addRenderableWidget(new ToolButton(this, SWAP, SpriteButton.SpriteSet.SWAP, 2));
		this.addRenderableWidget(new ToolButton(this, PICKER, SpriteButton.SpriteSet.PICKER, 3));
		this.addRenderableWidget(new ToolButton(this, WAND, SpriteButton.SpriteSet.WAND, 4));
		this.addRenderableWidget(new ToolButton(this, SELECT, SpriteButton.SpriteSet.SELECT, 5));

		Objects.requireNonNull(this.menu.getMortar()).getPolyominos().forEach(placedPolyomino -> {
			int gridX = this.leftPos + GRID_START.x;
			int gridY = this.topPos + GRID_START.y;

			PolyominoWidget polyominoWidget = new PolyominoWidget(this, gridX + placedPolyomino.x() * Tessera.TESSERA_SIZE, gridY + placedPolyomino.y() * Tessera.TESSERA_SIZE, placedPolyomino);
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
