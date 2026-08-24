package com.mod.mozaik.reg;

import com.mod.mozaik.items.components.ShardBagContents;
import com.mod.mozaik.platform.Services;
import net.minecraft.core.component.DataComponentType;

public class ModDataComponents {
	public static final ResourceSupplier<DataComponentType<ShardBagContents>> SHARD_BAG_CONTENTS = Services.REGISTRY.registerDataComponent("shard_bag_contents", ShardBagContents.CODEC, ShardBagContents.STREAM_CODEC);

	public static void init() {

	}
}
