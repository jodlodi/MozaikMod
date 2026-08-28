package com.mod.mozaik.client.buttons;

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
public class AltColorButton extends AbstractMaterialButton {
	public AltColorButton(MortarScreen screen, int offsetX, int offsetY) {
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

	private void itemCount(GuiGraphicsExtractor graphics, Font font, int x, int y, String amount) {
		graphics.text(font, amount, x + 19 - 2 - font.width(amount), y + 6 + 3, -1, true);
	}

	protected String getCount() {
		return this.screen.getShardSource().isCreative() ? "∞" : String.valueOf(this.screen.getShardSource().getCount(this.getMaterial()));
	}

	@Override
	protected boolean isValidClickButton(MouseButtonInfo buttonInfo) {
		return false;
	}
}
