package com.mod.mozaik.client.widgets;

import com.mod.mozaik.Constants;
import com.mod.mozaik.client.screens.MortarScreen;
import com.mod.mozaik.client.screens.PersonalPreferences;
import com.mod.mozaik.items.ShardItem;
import com.mod.mozaik.polyomino.ShardMaterial;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.input.MouseButtonInfo;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@NullMarked
public class MaterialWidget extends AbstractMaterialWidget {
	public static String FAVOURITE = "tooltip.mozaik.favourite";
	public static String COUNT = "tooltip.mozaik.count";
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
		for (int i = 1; i <= 9; i++) {
			if (PersonalPreferences.getFavourite(i - 1).material().orElse(null) == this.getMaterial()) {
				graphics.blit(RenderPipelines.GUI_TEXTURED, Constants.prefix("textures/gui/container/favourite.png"), this.getX() - 1, this.getY() - 1, 0, 0, 18, 18, 18, 18);
			}
		}
	}

	@Override
	protected void extractTooltip(GuiGraphicsExtractor graphics, int x, int y) {
		List<Component> components = new ArrayList<>();
		if (PersonalPreferences.getShardBarTooltipName().get()) {
			components.add(this.getItemStack().getHoverName());
		}

		if (PersonalPreferences.getShardBarTooltipCount().get()) {
			components.add(Component.translatable(COUNT, Component.literal(this.getCount()).withStyle(ChatFormatting.GOLD)));
		}

		List<Integer> favSlots = new ArrayList<>();
		for (int i = 1; i <= 9; i++) {
			if (PersonalPreferences.getFavourite(i - 1).material().orElse(null) == this.getMaterial()) {
				favSlots.add(i);
			}
		}

		if (!favSlots.isEmpty()) {
			components.add(Component.translatable(FAVOURITE, Component.literal(favSlots.toString()).withStyle(ChatFormatting.AQUA)));
		}

		graphics.setTooltipForNextFrame(Minecraft.getInstance().font, components, Optional.empty(), x, y);
	}

	@Override
	protected boolean isValidClickButton(MouseButtonInfo buttonInfo) {
		return buttonInfo.button() == MortarScreen.LEFT_CLICK || buttonInfo.button() == MortarScreen.RIGHT_CLICK;
	}

	@Override
	protected void renderMaterial(GuiGraphicsExtractor graphics) {
		super.renderMaterial(graphics);
		if (PersonalPreferences.getShardBarDisplayCount().get()) {
			this.itemCount(graphics, this.minecraft.font, this.getX(), this.getY(), this.getCount());
		}
	}

	private void itemCount(GuiGraphicsExtractor graphics, Font font, int x, int y, String amount) {
		graphics.text(font, amount, x + 19 - 2 - font.width(amount), y + 6 + 3, -1, true);
	}

	protected String getCount() {
		return this.screen.getShardSource().isCreative() ? "∞" : String.valueOf(this.screen.getShardSource().getCount(this.getMaterial()));
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
		ResourceKey<ShardMaterial> material = this.getMaterial();
		int count = Math.max(this.screen.getShardSource().getCount(material), 1);
		return new ItemStack(ShardItem.SHARDS.get(this.getMaterial()), count);
	}

	@Override
	public ResourceKey<ShardMaterial> getMaterial() {
		return this.screen.getSortedList().get(PersonalPreferences.minMaterial(this.screen) + this.index);
	}
}
