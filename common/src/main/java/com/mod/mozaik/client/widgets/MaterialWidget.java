package com.mod.mozaik.client.widgets;

import com.mod.mozaik.client.screens.MortarScreen;
import com.mod.mozaik.polyomino.TesseraMaterial;
import com.mod.mozaik.reg.ModItems;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.input.MouseButtonInfo;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class MaterialWidget extends AbstractItemWidget {
	private final int index;

	public MaterialWidget(MortarScreen screen, int offsetX, int offsetY, int index) {
		super(screen, offsetX, offsetY, true, true, true);
		this.index = index;
	}

	@Override
	public boolean mouseScrolled(double x, double y, double scrollX, double scrollY) {
		if (scrollY > 0) {
			MortarScreen.materialUpBy(this.screen, (int) scrollY);
			return true;
		} else if (scrollY < 0) {
			MortarScreen.materialDownBy(this.screen, (int) -scrollY);
			return true;
		}

		return false;
	}

	@Override
	protected boolean isValidClickButton(MouseButtonInfo buttonInfo) {
		return buttonInfo.button() == MortarScreen.LEFT_CLICK || buttonInfo.button() == MortarScreen.RIGHT_CLICK;
	}

	@Override
	public void onClick(MouseButtonEvent event, boolean doubleClick) {
		if (event.button() == MortarScreen.LEFT_CLICK) {
			this.screen.setPrimaryColor(TesseraMaterial.values()[this.screen.materialFrom + this.index]);
		} else {
			this.screen.setSecondaryColor(TesseraMaterial.values()[this.screen.materialFrom + this.index]);
		}
	}

	@Override
	protected ItemStack getItemStack() {
		return ModItems.SHARDS.pick(TesseraMaterial.values()[this.screen.materialFrom + this.index]).get().getDefaultInstance();
	}
}
