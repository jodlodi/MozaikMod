package com.mod.mozaik.data.gen;

import com.mod.mozaik.data.util.ModSoundProvider;
import com.mod.mozaik.reg.ModSounds;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class SoundGen extends ModSoundProvider {

	public SoundGen(PackOutput output, ExistingFileHelper helper) {
		super(output, helper);
	}

	@Override
	public void registerSounds() {
		this.generateNewSoundMC(ModSounds.SETTINGS_TAB, 1.0F, 0.65F,
				"item/spyglass/use"
		);
		this.generateNewSoundMC(ModSounds.SAVE_TAB, 1.0F, 0.89F,
				"ui/cartography_table/drawmap1",
				"ui/cartography_table/drawmap2",
				"ui/cartography_table/drawmap3"
		);
		this.generateNewSoundMC(ModSounds.EDIT_TAB, 1.0F, 1.0F,
				"item/book/open_flip1",
				"item/book/open_flip2",
				"item/book/open_flip3"
		);
		this.generateNewSoundMC(ModSounds.REMOVE_SHARD, 1.0F, 1.99F,
				"block/decorated_pot/step1",
				"block/decorated_pot/step2",
				"block/decorated_pot/step3",
				"block/decorated_pot/step4",
				"block/decorated_pot/step5"
		);
		this.generateNewSoundMC(ModSounds.PLACE_SHARD, 1.0F, 1.99F,
				"block/decorated_pot/break1",
				"block/decorated_pot/break2",
				"block/decorated_pot/break3",
				"block/decorated_pot/break4"
		);
		this.generateNewSoundMC(ModSounds.PICK_SHARD, 1.0F, 1.89F,
				"block/decorated_pot/insert1",
				"block/decorated_pot/insert2",
				"block/decorated_pot/insert3",
				"block/decorated_pot/insert4"
		);
	}
}
