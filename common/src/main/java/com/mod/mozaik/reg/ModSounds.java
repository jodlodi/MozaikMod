package com.mod.mozaik.reg;

import com.mod.mozaik.platform.Services;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.MethodsReturnNonnullByDefault;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ModSounds {
	public static final ResourceSupplier<SoundEvent> SETTINGS_TAB = Services.REGISTRY.registerSoundEvent(prefix("tab", "settings"), SoundEvent::createVariableRangeEvent);
	public static final ResourceSupplier<SoundEvent> SAVE_TAB = Services.REGISTRY.registerSoundEvent(prefix("tab", "save"), SoundEvent::createVariableRangeEvent);
	public static final ResourceSupplier<SoundEvent> EDIT_TAB = Services.REGISTRY.registerSoundEvent(prefix("tab", "edit"), SoundEvent::createVariableRangeEvent);
	public static final ResourceSupplier<SoundEvent> REMOVE_SHARD = Services.REGISTRY.registerSoundEvent(prefix("shard", "remove"), SoundEvent::createVariableRangeEvent);
	public static final ResourceSupplier<SoundEvent> PLACE_SHARD = Services.REGISTRY.registerSoundEvent(prefix("shard", "place"), SoundEvent::createVariableRangeEvent);
	public static final ResourceSupplier<SoundEvent> PICK_SHARD = Services.REGISTRY.registerSoundEvent(prefix("shard", "pick"), SoundEvent::createVariableRangeEvent);

	private static String prefix(String soundType, String soundName) {
		return soundType + "." + soundName;
	}

	public static void init() {

	}
}
