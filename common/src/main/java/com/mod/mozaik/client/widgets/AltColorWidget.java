package com.mod.mozaik.client.widgets;

import com.mod.mozaik.client.screens.MortarScreen;
import com.mod.mozaik.reg.ModItems;
import net.minecraft.client.input.MouseButtonInfo;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class AltColorWidget extends AbstractItemWidget {
	public AltColorWidget(MortarScreen screen, int offsetX, int offsetY) {
		super(screen, offsetX, offsetY, true, true);
	}

	@Override
	protected ItemStack getItemStack() {
		return ModItems.SHARDS.pick(this.screen.getSecondaryColor()).get().getDefaultInstance();
	}

	@Override
	protected boolean isValidClickButton(MouseButtonInfo buttonInfo) {
		return false;
	}
}
