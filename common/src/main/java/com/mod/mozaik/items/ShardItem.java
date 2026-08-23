package com.mod.mozaik.items;

import com.mod.mozaik.polyomino.ShardMaterial;
import com.mod.mozaik.reg.ModRegistries;
import com.mod.mozaik.reg.ResourceSupplier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import org.jspecify.annotations.NullMarked;

import java.util.HashMap;
import java.util.Map;

@NullMarked
public class ShardItem extends Item {
	public static Map<ResourceKey<ShardMaterial>, ShardItem> SHARDS = new HashMap<>();

	private final ResourceKey<ShardMaterial> material;

	public ShardItem(Properties properties, ResourceSupplier<ShardMaterial> material) {
		this(properties, ResourceKey.create(ModRegistries.ModKeys.SHARD_MATERIAL, material.id()));
	}

	public ShardItem(Properties properties, ResourceKey<ShardMaterial> material) {
		super(properties);
		this.material = material;
		SHARDS.put(this.material, this);
	}

	public ResourceKey<ShardMaterial> getMaterial() {
		return this.material;
	}
}
