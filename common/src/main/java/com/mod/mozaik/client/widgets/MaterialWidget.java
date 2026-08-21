package com.mod.mozaik.client.widgets;

import com.mod.mozaik.Constants;
import com.mod.mozaik.client.screens.MortarScreen;
import com.mod.mozaik.polyomino.PrePolyominoShapes;
import com.mod.mozaik.polyomino.TesseraMaterial;
import com.mod.mozaik.reg.ModItems;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.input.MouseButtonInfo;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NullMarked;

import java.util.Locale;

@NullMarked
public class MaterialWidget extends AbstractItemWidget {
	private final int index;

	public MaterialWidget(MortarScreen screen, int offsetX, int offsetY, int index) {
		super(screen, offsetX, offsetY, true, true);
		this.index = index;
	}

	protected Identifier getTexture() {
		return Constants.prefix("textures/gui/container/frame.png");
	}

	@Override
	public boolean mouseScrolled(double x, double y, double scrollX, double scrollY) {
		if (scrollY > 0) {
			MortarScreen.materialUpBy((int) scrollY);
			return true;
		} else if (scrollY < 0) {
			MortarScreen.materialDownBy((int) -scrollY);
			return true;
		}

		return false;
	}

	@Override
	protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
		super.extractWidgetRenderState(graphics, mouseX, mouseY, a);
		if (this.screen.getPrimaryColor() == TesseraMaterial.values()[MortarScreen.materialFrom + this.index]) {
			graphics.blit(RenderPipelines.GUI_TEXTURED, this.getTexture(), this.getX() - 1, this.getY() - 1, 0, 0, 18, 18, 18, 18);
		}
	}

	@Override
	protected boolean isValidClickButton(MouseButtonInfo buttonInfo) {
		return buttonInfo.button() == MortarScreen.LEFT_CLICK || buttonInfo.button() == MortarScreen.RIGHT_CLICK;
	}

	@Override
	public void onClick(MouseButtonEvent event, boolean doubleClick) {
		if (event.button() == MortarScreen.LEFT_CLICK) {
			this.screen.setPrimaryColor(TesseraMaterial.values()[MortarScreen.materialFrom + this.index]);
		} else {
			this.screen.setSecondaryColor(TesseraMaterial.values()[MortarScreen.materialFrom + this.index]);
		}
	}

	@Override
	protected ItemStack getItemStack() {
		return ModItems.SHARDS.pick(TesseraMaterial.values()[MortarScreen.materialFrom + this.index]).get().getDefaultInstance();
	}
}
