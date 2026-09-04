package com.mod.mozaik.data.util;

import com.mod.mozaik.Constants;
import com.mod.mozaik.data.gen.ModLangGen;
import com.mod.mozaik.reg.ResourceSupplier;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SoundDefinition;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;
import org.jetbrains.annotations.Nullable;
import net.minecraft.MethodsReturnNonnullByDefault;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class ModSoundProvider extends SoundDefinitionsProvider {

	protected ModSoundProvider(PackOutput output, ExistingFileHelper helper) {
		super(output, Constants.MOD_ID, helper);
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
							.sound(ResourceLocation.withDefaultNamespace(string), SoundDefinition.SoundType.SOUND)
							.volume(volume)
							.pitch(pitch)
			);
		}
		this.add(event.get(), definition);
	}

	private void createSubtitleAndLangEntry(ResourceSupplier<SoundEvent> event, SoundDefinition definition, String subtitle) {
		String[] splitSoundName = event.id().getPath().split("\\.", 3);
		String subtitleKey = "subtitles.mozaik." + splitSoundName[0] + "." + splitSoundName[2];
		definition.subtitle(subtitleKey);
		ModLangGen.SUBTITLE_GENERATOR.put(subtitleKey, subtitle);
	}
}
