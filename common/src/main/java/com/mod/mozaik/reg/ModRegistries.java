package com.mod.mozaik.reg;

import com.mod.mozaik.Constants;
import com.mod.mozaik.platform.Services;
import com.mod.mozaik.polyomino.PolyominoShape;
import com.mod.mozaik.polyomino.ShardMaterial;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public class ModRegistries {
	public static final Registry<ShardMaterial> SHARD_MATERIALS = Services.REGISTRY.createRegistry(ModKeys.SHARD_MATERIAL);
	public static final Registry<PolyominoShape> POLYOMINO_SHAPES = Services.REGISTRY.createRegistry(ModKeys.POLYOMINO_SHAPE);

	public static class ModKeys {
		public static final ResourceKey<Registry<ShardMaterial>> SHARD_MATERIAL = ResourceKey.createRegistryKey(Constants.prefix("shard_material"));
		public static final ResourceKey<Registry<PolyominoShape>> POLYOMINO_SHAPE = ResourceKey.createRegistryKey(Constants.prefix("polyomino_shape"));
	}

	public static void init() {

	}
}
