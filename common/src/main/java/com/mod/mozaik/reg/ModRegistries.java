package com.mod.mozaik.reg;

import com.mod.mozaik.Constants;
import com.mod.mozaik.platform.Services;
import com.mod.mozaik.polyomino.ShardMaterial;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class ModRegistries {
	public static final Registry<ShardMaterial> SHARD_MATERIALS = Services.REGISTRY.createRegistry(ModKeys.SHARD_MATERIAL);

	public static class ModKeys {
		public static final ResourceKey<Registry<ShardMaterial>> SHARD_MATERIAL = ResourceKey.createRegistryKey(Constants.prefix("shard_material"));
	}

	public static void init() {

	}
}
