package com.mod.mozaik.data.util;

import com.mod.mozaik.Constants;
import com.mod.mozaik.data.gen.ModLangGen;
import com.mod.mozaik.reg.ResourceSupplier;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.common.data.SoundDefinition;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.Nullable;
import org.jspecify.annotations.NullMarked;

@NullMarked
public abstract class TFSoundProvider extends SoundDefinitionsProvider {

	protected TFSoundProvider(PackOutput output) {
		super(output, Constants.MOD_ID);
	}

	public void generateNewSoundWithSubtitle(ResourceSupplier<SoundEvent> event, String baseSoundDirectory, int numberOfSounds, String subtitle, float volume, float pitch) {
		this.generateNewSound(event, baseSoundDirectory, numberOfSounds, subtitle, volume, pitch);
	}

	public void generateNewSoundWithSubtitle(ResourceSupplier<SoundEvent> event, String baseSoundDirectory, int numberOfSounds, String subtitle) {
		this.generateNewSound(event, baseSoundDirectory, numberOfSounds, subtitle, 1.0F, 1.0F);
	}

	public void generateNewSound(ResourceSupplier<SoundEvent> event, String baseSoundDirectory, int numberOfSounds, @Nullable String subtitle, float volume, float pitch) {
		SoundDefinition definition = SoundDefinition.definition();
		if (subtitle != null) {
			this.createSubtitleAndLangEntry(event, definition, subtitle);
		}
		for (int i = 1; i <= numberOfSounds; i++) {
			definition.with(SoundDefinition.Sound.sound(Constants.prefix(baseSoundDirectory + (numberOfSounds > 1 ? i : "")), SoundDefinition.SoundType.SOUND).volume(volume).pitch(pitch));
		}
		this.add(event.get(), definition);
	}

	public void generateNewSoundMC(ResourceSupplier<SoundEvent> event, float volume, float pitch, String... baseSoundDirectory) {
		SoundDefinition definition = SoundDefinition.definition();
		for (String string : baseSoundDirectory) {
			definition.with(
					SoundDefinition.Sound
							.sound(Identifier.withDefaultNamespace(string), SoundDefinition.SoundType.SOUND)
							.volume(volume)
							.pitch(pitch)
			);
		}
		this.add(event.get(), definition);
	}

	public void generateExistingSoundWithSubtitle(ResourceSupplier<SoundEvent> event, SoundEvent referencedSound, String subtitle) {
		this.generateExistingSoundWithSubtitle(event, referencedSound, subtitle, 1.0F, 1.0F);
	}

	public void generateExistingSoundWithSubtitle(ResourceSupplier<SoundEvent> event, SoundEvent referencedSound, String subtitle, float volume, float pitch) {
		this.generateExistingSound(event, referencedSound, subtitle, volume, pitch);
	}

	public void generateSoundWithExistingSubtitle(ResourceSupplier<SoundEvent> event, SoundEvent referencedSound, String subtitle) {
		this.add(event.get(), SoundDefinition.definition()
				.subtitle(subtitle)
				.with(SoundDefinition.Sound.sound(referencedSound.location(), SoundDefinition.SoundType.EVENT)));
	}

	public void generateExistingSound(ResourceSupplier<SoundEvent> event, SoundEvent referencedSound, @Nullable String subtitle, float volume, float pitch) {
		SoundDefinition definition = SoundDefinition.definition();
		if (subtitle != null) {
			this.createSubtitleAndLangEntry(event, definition, subtitle);
		}
		this.add(event.get(), definition
				.with(SoundDefinition.Sound.sound(referencedSound.location(), SoundDefinition.SoundType.EVENT).volume(volume).pitch(pitch)));
	}

	public void makeStepSound(ResourceSupplier<SoundEvent> event, SoundEvent referencedSound) {
		this.add(event.get(), SoundDefinition.definition()
				.subtitle("subtitles.block.generic.footsteps")
				.with(SoundDefinition.Sound.sound(referencedSound.location(), SoundDefinition.SoundType.EVENT)));
	}

	public void makeNewStepSound(ResourceSupplier<SoundEvent> event, String baseSoundDirectory, int numberOfSounds) {
		SoundDefinition definition = SoundDefinition.definition();
		for (int i = 1; i <= numberOfSounds; i++) {
			definition.with(SoundDefinition.Sound.sound(Constants.prefix(baseSoundDirectory + (numberOfSounds > 1 ? i : "")), SoundDefinition.SoundType.SOUND));
		}
		this.add(event.get(), definition.subtitle("subtitles.block.generic.footsteps"));
	}

	public void makeNewGenericSound(ResourceSupplier<SoundEvent> event, String baseSoundDirectory, int numberOfSounds, @Nullable String type) {
		SoundDefinition definition = SoundDefinition.definition();
		for (int i = 1; i <= numberOfSounds; i++) {
			definition.with(SoundDefinition.Sound.sound(Constants.prefix(baseSoundDirectory + (numberOfSounds > 1 ? i : "")), SoundDefinition.SoundType.SOUND));
		}
		this.add(event.get(), type != null ? definition.subtitle("subtitles.block.generic." + type) : definition);
	}

	public void makeMusicDisc(ResourceSupplier<SoundEvent> event, String discName) {
		this.add(event.get(), SoundDefinition.definition()
				.with(SoundDefinition.Sound.sound(Constants.prefix("music/" + discName), SoundDefinition.SoundType.SOUND)
						.stream()));
	}

	public void generateParrotSound(ResourceSupplier<SoundEvent> event, SoundEvent referencedSound, String subtitle) {
		SoundDefinition definition = SoundDefinition.definition();
		this.createSubtitleAndLangEntry(event, definition, subtitle);

		this.add(event.get(), definition
				.with(SoundDefinition.Sound.sound(referencedSound.location(), SoundDefinition.SoundType.EVENT).pitch(1.8F).volume(0.6F)));
	}

	private void createSubtitleAndLangEntry(ResourceSupplier<SoundEvent> event, SoundDefinition definition, String subtitle) {
		String[] splitSoundName = event.id().getPath().split("\\.", 3);
		String subtitleKey = "subtitles.twilightforest." + splitSoundName[0] + "." + splitSoundName[2];
		definition.subtitle(subtitleKey);
		ModLangGen.SUBTITLE_GENERATOR.put(subtitleKey, subtitle);
	}
}
