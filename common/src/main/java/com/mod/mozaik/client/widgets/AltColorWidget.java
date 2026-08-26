package com.mod.mozaik.client.widgets;

import com.mod.mozaik.client.screens.MortarScreen;
import com.mod.mozaik.client.screens.PersonalPreferences;
import com.mod.mozaik.items.ShardItem;
import com.mod.mozaik.polyomino.ShardMaterial;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.MouseButtonInfo;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class AltColorWidget extends AbstractMaterialWidget {
	public AltColorWidget(MortarScreen screen, int offsetX, int offsetY) {
		super(screen, offsetX, offsetY, true);
	}

	@Override
	protected ItemStack getItemStack() {
		return ShardItem.SHARDS.get(PersonalPreferences.getSecondaryColor()).getDefaultInstance();
	}

	@Override
	protected ResourceKey<ShardMaterial> getMaterial() {
		return PersonalPreferences.getSecondaryColor();
	}

	@Override
	protected void renderMaterial(GuiGraphicsExtractor graphics) {
		super.renderMaterial(graphics);
		this.itemCount(graphics, this.minecraft.font, this.getX(), this.getY(), this.getCount());
	}

	protected String getCount() {
		return this.screen.getShardSource().isCreative() ? "∞" : String.valueOf(this.screen.getShardSource().getCount(this.getMaterial()));
	}

	private void itemCount(GuiGraphicsExtractor graphics, Font font, int x, int y, String amount) {
		graphics.pose().pushMatrix();
		graphics.text(font, amount, x + 19 - 2 - font.width(amount), y + 6 + 3, -1, true);
		graphics.pose().popMatrix();
	}

	@Override
	protected boolean isValidClickButton(MouseButtonInfo buttonInfo) {
		return false;
	}
}
