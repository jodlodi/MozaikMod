package com.mod.mozaik.client.widgets;

import com.mod.mozaik.client.screens.MortarScreen;
import com.mod.mozaik.client.screens.PersonalPreferences;
import com.mod.mozaik.polyomino.TesseraMaterial;
import com.mod.mozaik.reg.ModItems;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.MouseButtonInfo;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class AltColorWidget extends AbstractMaterialWidget {
	public AltColorWidget(MortarScreen screen, int offsetX, int offsetY) {
		super(screen, offsetX, offsetY, true);
	}

	@Override
	protected ItemStack getItemStack() {
		return ModItems.SHARDS.pick(PersonalPreferences.getSecondaryColor()).get().getDefaultInstance();
	}

	@Override
	protected TesseraMaterial getMaterial() {
		return PersonalPreferences.getSecondaryColor();
	}

	@Override
	protected void renderMaterial(GuiGraphicsExtractor graphics) {
		super.renderMaterial(graphics);
		graphics.pose().pushMatrix();
		this.itemCount(graphics, this.minecraft.font, this.getX(), this.getY(), this.getCount());
		graphics.pose().popMatrix();
	}

	protected String getCount() {
		return this.screen.getShardSource().isCreative() ? "∞" : String.valueOf(this.screen.getShardSource().getCount(this.getMaterial()));
	}

	private void itemCount(GuiGraphicsExtractor graphics, Font font, int x, int y, String amount) {
		graphics.text(font, amount, x + 19 - 2 - font.width(amount), y + 6 + 3, -1, true);
	}

	@Override
	protected boolean isValidClickButton(MouseButtonInfo buttonInfo) {
		return false;
	}
}
