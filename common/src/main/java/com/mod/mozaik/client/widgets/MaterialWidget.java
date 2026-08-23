package com.mod.mozaik.client.widgets;

import com.mod.mozaik.Constants;
import com.mod.mozaik.client.screens.MortarScreen;
import com.mod.mozaik.client.screens.PersonalPreferences;
import com.mod.mozaik.polyomino.TesseraMaterial;
import com.mod.mozaik.reg.ModItems;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.input.MouseButtonInfo;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class MaterialWidget extends AbstractMaterialWidget {
	private final int index;

	public MaterialWidget(MortarScreen screen, int offsetX, int offsetY, int index) {
		super(screen, offsetX, offsetY, true);
		this.index = index;
	}

	@Override
	public boolean mouseScrolled(double x, double y, double scrollX, double scrollY) {
		if (scrollY > 0) {
			this.screen.materialUpBy((int) scrollY);
			return true;
		} else if (scrollY < 0) {
			this.screen.materialDownBy((int) -scrollY);
			return true;
		}

		return false;
	}

	@Override
	protected void extractWidgetRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
		super.extractWidgetRenderState(graphics, mouseX, mouseY, a);
		if (PersonalPreferences.getPrimaryColor() == this.getMaterial()) {
			graphics.blit(RenderPipelines.GUI_TEXTURED, Constants.prefix("textures/gui/container/frame.png"), this.getX() - 1, this.getY() - 1, 0, 0, 18, 18, 18, 18);
		}
	}

	@Override
	protected boolean isValidClickButton(MouseButtonInfo buttonInfo) {
		return buttonInfo.button() == MortarScreen.LEFT_CLICK || buttonInfo.button() == MortarScreen.RIGHT_CLICK;
	}

	@Override
	public void onClick(MouseButtonEvent event, boolean doubleClick) {
		if (event.button() == MortarScreen.LEFT_CLICK) {
			PersonalPreferences.setPrimaryColor(this.screen, this.getMaterial());
		} else {
			PersonalPreferences.setSecondaryColor(this.getMaterial());
		}
	}

	@Override
	protected ItemStack getItemStack() {
		TesseraMaterial material = this.getMaterial();
		int count = Math.max(this.screen.getShardSource().getCount(material), 1);
		return new ItemStack(ModItems.SHARDS.pick(this.getMaterial()).get(), count);
	}

	@Override
	protected TesseraMaterial getMaterial() {
		return TesseraMaterial.values()[PersonalPreferences.minMaterial() + this.index];
	}
}
