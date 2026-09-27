package com.mod.mozaik.client.buttons;

import com.mod.mozaik.client.screens.MortarScreen;
import com.mod.mozaik.client.screens.PersonalPreferences;
import com.mod.mozaik.items.ShardItem;
import com.mod.mozaik.polyomino.ShardMaterial;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.MethodsReturnNonnullByDefault;
import javax.annotation.ParametersAreNonnullByDefault;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
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
	protected void renderMaterial(GuiGraphics graphics) {
		super.renderMaterial(graphics);
		this.itemCount(graphics, this.minecraft.font, this.getX(), this.getY(), this.getCount());
	}

	private void itemCount(GuiGraphics graphics, Font font, int x, int y, String amount) {
		graphics.drawString(font, amount, x + 19 - 2 - font.width(amount), y + 6 + 3, -1, true);
	}

	protected String getCount() {
		if (this.screen.getShardSource().isCreative() && !PersonalPreferences.getCreativeInfinity().get()) return "";
		return this.screen.getShardSource().isCreative() ? "∞" : String.valueOf(this.screen.getShardSource().getCount(this.getMaterial()));
	}

	@Override
	protected void extractTooltip(GuiGraphics graphics, int x, int y) {
		List<Component> components = new ArrayList<>();
		components.add(this.getItemStack().getHoverName());
		components.add(Component.translatable("screen.mozaik.swap"));
		graphics.renderTooltip(Minecraft.getInstance().font, components, Optional.empty(), x, y);
	}

	@Override
	protected boolean isValidClickButton(int button) {
		return false;
	}
}
