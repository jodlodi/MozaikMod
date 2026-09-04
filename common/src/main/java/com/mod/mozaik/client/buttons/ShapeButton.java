package com.mod.mozaik.client.buttons;

import com.mod.mozaik.Constants;
import com.mod.mozaik.client.GraphicsRenderHelper;
import com.mod.mozaik.client.PhaseRenderable;
import com.mod.mozaik.client.screens.MortarScreen;
import com.mod.mozaik.client.screens.PersonalPreferences;
import com.mod.mozaik.items.PolyominoItem;
import com.mod.mozaik.mixin.GuiGraphicsAccessor;
import com.mod.mozaik.polyomino.PolyominoShape;
import com.mod.mozaik.reg.ModSounds;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

import java.util.*;
import java.util.stream.Stream;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ShapeButton extends ModButton implements PhaseRenderable {
	protected final MortarScreen screen;
	private final int index;

	public ShapeButton(MortarScreen screen, int x, int y, int index) {
		super(x, y, 18, 18, Component.empty());
		this.screen = screen;
		this.index = index;
	}

	protected ResourceLocation getTexture() {
		ResourceKey<PolyominoShape> dis = this.getShape();
		return Constants.prefix("textures/gui/container/shapes/" + dis.location().getPath() + "/" + (dis == PersonalPreferences.getPolyominoShape() ? "pressed" : "unselected") + ".png");
	}

	@Override
	public boolean mouseScrolled(double x, double y, double scrollX, double scrollY) {
		if (PersonalPreferences.getReverseScrollDirectionBars().get()) scrollY *= -1;

		if (scrollY > 0) {
			this.screen.templateUpBy((int) scrollY);
			return true;
		} else if (scrollY < 0) {
			this.screen.templateDownBy((int) -scrollY);
			return true;
		}

		return false;
	}

	@Override
	protected boolean isValidClickButton(int button) {
		return button == MortarScreen.LEFT_CLICK || button == MortarScreen.RIGHT_CLICK;
	}

	@Override
	public void playDownSound(SoundManager soundManager) {
		playButtonClickSound(soundManager, Holder.direct(ModSounds.PICK_SHARD.get()));
	}

	public static void playButtonClickSound(SoundManager soundManager, Holder<SoundEvent> soundEvent) {
		soundManager.play(SimpleSoundInstance.forUI(soundEvent.value(), 1.0F, PersonalPreferences.getVolume()));
	}

	@Override
	public void onPress() {
		ResourceKey<PolyominoShape> key = this.getShape();
		PersonalPreferences.setPolyominoShape(key);
		PolyominoShape.tryBuild(key).ifPresent(PersonalPreferences::setShape);
	}

	public boolean playerHas() {
		return this.screen.getShardSource().isCreative() || !this.screen.noPoly(this.getShape().location());
	}

	@Override
	protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
		int color = this.playerHas() ? -1 : 0x44777777;
		GraphicsRenderHelper.blit(graphics, this.getTexture(), this.getX() - 1, this.getY() - 1, 18, 18, color);

		List<Integer> favSlots = new ArrayList<>();
		for (int i = 1; i <= 9; i++) {
			if (PersonalPreferences.getFavourite(i - 1).polyomino().orElse(null) == this.getShape()) {
				graphics.blit(Constants.prefix("textures/gui/container/favourite.png"), this.getX() - 1, this.getY() - 1, 0, 0, 18, 18, 18, 18);
				favSlots.add(i);
			}
		}

		if (this.isHovered()) {
			Optional<TooltipComponent> component = PersonalPreferences.getShapeTooltip().get()
					? Optional.ofNullable(PolyominoShape.tryBuild(this.getShape(), PersonalPreferences.getPrimaryColor(), PersonalPreferences.getShape().uuid()).map(PolyominoItem.ShapeTooltip::new).orElse(null))
					: Optional.empty();

			if (!favSlots.isEmpty()) {
				graphics.renderTooltip(Minecraft.getInstance().font, List.of(
						Component.translatable(MaterialButton.FAVOURITE, Component.literal(favSlots.toString()).withStyle(ChatFormatting.AQUA))
				), component, mouseX, mouseY);
			} else if (component.isPresent()) {
				List<ClientTooltipComponent> list = new ArrayList<>();
				component.ifPresent((tooltipComponent) -> list.add(ClientTooltipComponent.create(tooltipComponent)));
				((GuiGraphicsAccessor)graphics).invokeRenderTooltipInternal(Minecraft.getInstance().font, list, mouseX, mouseY, DefaultTooltipPositioner.INSTANCE); //Skip NeoForge's hook that crashes the game if component list is empty!
			}
		}
	}

	public ResourceKey<PolyominoShape> getShape() {
		return this.screen.getSortedShapes().get(this.getShapeIndex());
	}

	public int getShapeIndex() {
		return PersonalPreferences.minTemplate(this.screen) + this.index;
	}
}
