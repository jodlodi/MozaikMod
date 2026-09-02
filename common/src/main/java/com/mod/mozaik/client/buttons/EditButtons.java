package com.mod.mozaik.client.buttons;

import com.mod.mozaik.client.screens.MortarScreen;
import com.mod.mozaik.client.widgets.PolyominoWidget;
import com.mod.mozaik.menus.MortarMenu;
import com.mod.mozaik.polyomino.Polyomino;
import com.mod.mozaik.polyomino.Tessera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import org.joml.Vector2i;
import net.minecraft.MethodsReturnNonnullByDefault;
import javax.annotation.ParametersAreNonnullByDefault;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class EditButtons extends ClickableButton {
	private final Edition edition;

	public EditButtons(MortarScreen screen, Vector2i pos, SpriteSet spriteSet, Edition edition) {
		super(screen, pos, spriteSet);
		this.edition = edition;
	}

	@Override
	protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
		super.renderWidget(graphics, mouseX, mouseY, partialTick);

		if (this.isHovered()) {
			graphics.renderTooltip(Minecraft.getInstance().font, List.of(
					Component.translatable(this.edition.asTranslationString())
			), Optional.empty(), mouseX, mouseY);
		}
	}

	@Override
	public void onUnblockedPress() {
		switch (this.edition) {
			case FLIP_VERTICAL -> {
				this.screen.polyomino.replaceAll(widget -> {
					this.screen.removeWidget(widget);
					Polyomino.PlacedPolyomino placedPolyomino = widget.getPlacedPolyomino().mirror(Mirror.FRONT_BACK);

					int gridX = this.screen.getLeftPos() + MortarScreen.GRID_START.x;
					int gridY = this.screen.getTopPos() + MortarScreen.GRID_START.y;
					PolyominoWidget polyominoWidget = new PolyominoWidget(this.screen, gridX + placedPolyomino.x() * Tessera.TESSERA_SIZE, gridY + placedPolyomino.y() * Tessera.TESSERA_SIZE, placedPolyomino);
					this.screen.addRenderableWidget(polyominoWidget);
					return polyominoWidget;
				});
				this.screen.markChanged();
			}
			case FLIP_HORIZONTAL -> {
				this.screen.polyomino.replaceAll(widget -> {
					this.screen.removeWidget(widget);
					Polyomino.PlacedPolyomino placedPolyomino = widget.getPlacedPolyomino().mirror(Mirror.LEFT_RIGHT);

					int gridX = this.screen.getLeftPos() + MortarScreen.GRID_START.x;
					int gridY = this.screen.getTopPos() + MortarScreen.GRID_START.y;
					PolyominoWidget polyominoWidget = new PolyominoWidget(this.screen, gridX + placedPolyomino.x() * Tessera.TESSERA_SIZE, gridY + placedPolyomino.y() * Tessera.TESSERA_SIZE, placedPolyomino);
					this.screen.addRenderableWidget(polyominoWidget);
					return polyominoWidget;
				});
				this.screen.markChanged();
			}
			case ROTATE_180 -> {
				this.screen.polyomino.replaceAll(widget -> {
					this.screen.removeWidget(widget);
					Polyomino.PlacedPolyomino placedPolyomino = MortarMenu.rotate(widget.getPlacedPolyomino(), Rotation.CLOCKWISE_180);

					int gridX = this.screen.getLeftPos() + MortarScreen.GRID_START.x;
					int gridY = this.screen.getTopPos() + MortarScreen.GRID_START.y;
					PolyominoWidget polyominoWidget = new PolyominoWidget(this.screen, gridX + placedPolyomino.x() * Tessera.TESSERA_SIZE, gridY + placedPolyomino.y() * Tessera.TESSERA_SIZE, placedPolyomino);
					this.screen.addRenderableWidget(polyominoWidget);
					return polyominoWidget;
				});
				this.screen.markChanged();
			}
			case ROTATE_270 -> {
					this.screen.polyomino.replaceAll(widget -> {
						this.screen.removeWidget(widget);
						Polyomino.PlacedPolyomino placedPolyomino = MortarMenu.rotate(widget.getPlacedPolyomino(), Rotation.COUNTERCLOCKWISE_90);

						int gridX = this.screen.getLeftPos() + MortarScreen.GRID_START.x;
						int gridY = this.screen.getTopPos() + MortarScreen.GRID_START.y;
						PolyominoWidget polyominoWidget = new PolyominoWidget(this.screen, gridX + placedPolyomino.x() * Tessera.TESSERA_SIZE, gridY + placedPolyomino.y() * Tessera.TESSERA_SIZE, placedPolyomino);
						this.screen.addRenderableWidget(polyominoWidget);
						return polyominoWidget;
					});
					this.screen.markChanged();
			}
			case ROTATE_90 -> {
				this.screen.polyomino.replaceAll(widget -> {
					this.screen.removeWidget(widget);
					Polyomino.PlacedPolyomino placedPolyomino = MortarMenu.rotate(widget.getPlacedPolyomino(), Rotation.CLOCKWISE_90);

					int gridX = this.screen.getLeftPos() + MortarScreen.GRID_START.x;
					int gridY = this.screen.getTopPos() + MortarScreen.GRID_START.y;
					PolyominoWidget polyominoWidget = new PolyominoWidget(this.screen, gridX + placedPolyomino.x() * Tessera.TESSERA_SIZE, gridY + placedPolyomino.y() * Tessera.TESSERA_SIZE, placedPolyomino);
					this.screen.addRenderableWidget(polyominoWidget);
					return polyominoWidget;
				});
				this.screen.markChanged();
			}
		}
	}

	public enum Edition implements StringRepresentable {
		FLIP_VERTICAL,
		FLIP_HORIZONTAL,
		ROTATE_180,
		ROTATE_270,
		ROTATE_90;

		public String asTranslationString() {
			return "tooltip.mozaik.mode." + this.getSerializedName();
		}

		@Override
		public String getSerializedName() {
			return this.name().toLowerCase(Locale.ROOT);
		}
	}
}
