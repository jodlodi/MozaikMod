package com.mod.mozaik.client.buttons;

import com.mod.mozaik.client.screens.MortarScreen;
import com.mod.mozaik.reg.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.InputWithModifiers;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import org.joml.Vector2i;
import org.jspecify.annotations.NullMarked;

import java.util.List;
import java.util.Optional;

@NullMarked
public class TabButton extends ModButton {
	protected final MortarScreen screen;
	private final MortarScreen.Mode mode;

	public TabButton(MortarScreen screen, Vector2i pos, MortarScreen.Mode mode) {
		super(screen.getLeftPos() + pos.x, screen.getTopPos() + pos.y, 13, 14, Component.empty());
		this.screen = screen;
		this.mode = mode;
	}

	public MortarScreen.Mode getMode() {
		return this.mode;
	}

	@Override
	public void playDownSound(SoundManager soundManager) {
		switch (this.getMode()) {
			case SETTINGS -> playButtonClickSound(soundManager, Holder.direct(ModSounds.SETTINGS_TAB.get()));
			case EDIT -> playButtonClickSound(soundManager, Holder.direct(ModSounds.EDIT_TAB.get()));
			case LOCK -> playButtonClickSound(soundManager, Holder.direct(ModSounds.SAVE_TAB.get()));
		}
	}

	public static void playButtonClickSound(SoundManager soundManager, Holder<SoundEvent> soundEvent) {
		soundManager.play(SimpleSoundInstance.forUI(soundEvent, 1.0F));
	}

	@Override
	protected void extractContents(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
		if (this.isHovered()) {
			graphics.setTooltipForNextFrame(Minecraft.getInstance().font, List.of(
					Component.translatable(this.mode.asTranslationString())
			), Optional.empty(), mouseX, mouseY);
		}
	}

	@Override
	public void onPress(InputWithModifiers inputWithModifiers) {
		this.screen.mode = this.screen.mode != this.mode ? this.mode : MortarScreen.Mode.MORTAR;
		this.screen.rebuildWidgets();
	}
}
