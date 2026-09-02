package com.mod.mozaik.client.buttons;

import com.mod.mozaik.Constants;
import com.mod.mozaik.client.screens.MortarScreen;
import com.mod.mozaik.client.screens.PersonalPreferences;
import com.mod.mozaik.items.ShardItem;
import com.mod.mozaik.polyomino.ShardMaterial;
import com.mod.mozaik.reg.ModSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MaterialButton extends AbstractMaterialButton {
	public static String FAVOURITE = "tooltip.mozaik.favourite";
	public static String COUNT = "tooltip.mozaik.count";
	private final int index;

	public MaterialButton(MortarScreen screen, int offsetX, int offsetY, int index) {
		super(screen, offsetX, offsetY, true);
		this.index = index;
	}

	@Override
	public void playDownSound(SoundManager soundManager) {
		playButtonClickSound(soundManager, Holder.direct(ModSounds.PICK_SHARD.get()));
	}

	public static void playButtonClickSound(SoundManager soundManager, Holder<SoundEvent> soundEvent) {
		soundManager.play(SimpleSoundInstance.forUI(soundEvent.value(), 1.0F, PersonalPreferences.getVolume()));
	}

	@Override
	public boolean mouseScrolled(double x, double y, double scrollX, double scrollY) {
		if (PersonalPreferences.getReverseScrollDirectionBars().get()) scrollY *= -1;

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
	protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
		super.renderWidget(graphics, mouseX, mouseY, partialTick);
		if (PersonalPreferences.getPrimaryColor() == this.getMaterial()) {
			graphics.blit(Constants.prefix("textures/gui/container/frame.png"), this.getX() - 1, this.getY() - 1, 0, 0, 18, 18, 18, 18);
		}
		for (int i = 1; i <= 9; i++) {
			if (PersonalPreferences.getFavourite(i - 1).material().orElse(null) == this.getMaterial()) {
				graphics.blit(Constants.prefix("textures/gui/container/favourite.png"), this.getX() - 1, this.getY() - 1, 0, 0, 18, 18, 18, 18);
			}
		}
	}

	@Override
	protected void extractTooltip(GuiGraphics graphics, int x, int y) {
		List<Component> components = new ArrayList<>();
		if (PersonalPreferences.getShardBarTooltipName().get()) {
			components.add(this.getItemStack().getHoverName());
		}

		if (PersonalPreferences.getShardBarTooltipCount().get()) {
			components.add(Component.translatable(COUNT, Component.literal(this.getCount()).withStyle(ChatFormatting.GOLD)));
		}

		StringBuilder favSlots = null;
		for (int i = 1; i <= 9; i++) {
			if (PersonalPreferences.getFavourite(i - 1).material().orElse(null) == this.getMaterial()) {
				if (favSlots == null) favSlots = new StringBuilder(Integer.toString(i));
				else favSlots.append(", ").append(i);
			}
		}

		if (favSlots != null) {
			components.add(Component.translatable(FAVOURITE, Component.literal(favSlots.toString()).withStyle(ChatFormatting.AQUA)));
		}

		graphics.renderComponentTooltip(this.minecraft.font, components, x, y);
	}

	@Override
	protected boolean isValidClickButton(int button) {
		return button == MortarScreen.LEFT_CLICK || button == MortarScreen.RIGHT_CLICK;
	}

	@Override
	protected void renderMaterial(GuiGraphics graphics) {
		super.renderMaterial(graphics);
		if (PersonalPreferences.getShardBarDisplayCount().get()) {
			this.itemCount(graphics, this.minecraft.font, this.getX(), this.getY(), this.getCount());
		}
	}

	private void itemCount(GuiGraphics graphics, Font font, int x, int y, String amount) {
		graphics.drawString(font, amount, x + 19 - 2 - font.width(amount), y + 6 + 3, -1, true);
	}

	protected String getCount() {
		if (this.screen.getShardSource().isCreative() && !PersonalPreferences.getCreativeInfinity().get()) return "";
		return this.screen.getShardSource().isCreative() ? "∞" : String.valueOf(this.screen.getShardSource().getCount(this.getMaterial()));
	}

	@Override
	public void onClick(double mouseX, double mouseY) {
		super.onClick(mouseX, mouseY);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (this.active && this.visible) {
			if (this.isValidClickButton(button)) {
				boolean flag = this.clicked(mouseX, mouseY);
				if (flag) {
					this.playDownSound(Minecraft.getInstance().getSoundManager());
					if (button == MortarScreen.LEFT_CLICK) {
						PersonalPreferences.setPrimaryColor(this.screen, this.getMaterial());
					} else {
						PersonalPreferences.setSecondaryColor(this.getMaterial());
					}
					return true;
				}
			}
		}
		return false;
	}

	@Override
	protected ItemStack getItemStack() {
		ResourceKey<ShardMaterial> material = this.getMaterial();
		int count = Math.max(this.screen.getShardSource().getCount(material), 1);
		return new ItemStack(ShardItem.SHARDS.get(this.getMaterial()), count);
	}

	@Override
	public ResourceKey<ShardMaterial> getMaterial() {
		return this.screen.getSortedMaterials().get(PersonalPreferences.minMaterial(this.screen) + this.index);
	}
}
