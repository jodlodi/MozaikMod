package com.mod.mozaik.reg;

import com.mod.mozaik.Constants;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class ModTags {
	public static class Blocks {
		public static final TagKey<Block> MORTARS = TagKey.create(Registries.BLOCK, Constants.prefix("mortars"));
	}

	public static class Items {
		public static final TagKey<Item> SHARDS = TagKey.create(Registries.ITEM, Constants.prefix("shards"));
		public static final TagKey<Item> MORTARS = TagKey.create(Registries.ITEM, Constants.prefix("mortars"));
	}
}
