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
	public static class EntityTypes {
		public static final TagKey<EntityType<?>> DINO_RESISTS_DAMAGE_FROM = TagKey.create(Registries.ENTITY_TYPE, Constants.prefix("dino_resists_damage_from"));
		public static final TagKey<EntityType<?>> SCULKSAUR_DEALS_BONUS_DAMAGE_TO = TagKey.create(Registries.ENTITY_TYPE, Constants.prefix("sculksaur_deals_bonus_damage_to"));
		public static final TagKey<EntityType<?>> CORALOSAUR_TURN = TagKey.create(Registries.ENTITY_TYPE, Constants.prefix("coralosaur_turn"));
	}

	public static class Blocks {
		public static final TagKey<Block> LUSHSAUR_EDIBLE_BLOCK = TagKey.create(Registries.BLOCK, Constants.prefix("lushsaur_edible"));
		public static final TagKey<Block> LUSHSAUR_EGG_HATCHABLE_BLOCKS = TagKey.create(Registries.BLOCK, Constants.prefix("lushsaur_egg_hatchable"));
		public static final TagKey<Block> CHORUSOATL_EGG_HATCHABLE_BLOCKS = TagKey.create(Registries.BLOCK, Constants.prefix("chorusoatl_egg_hatchable"));
		public static final TagKey<Block> OBSIDICERATOPS_EGG_HATCHABLE_BLOCKS = TagKey.create(Registries.BLOCK, Constants.prefix("obsidiceratops_egg_hatchable"));
	}

	public static class Items {
		public static final TagKey<Item> SHARDS = TagKey.create(Registries.ITEM, Constants.prefix("shards"));
		public static final TagKey<Item> MORTARS = TagKey.create(Registries.ITEM, Constants.prefix("mortars"));
		public static final TagKey<Item> OBSIDICERATOPS_EDIBLE_ITEM = TagKey.create(Registries.ITEM, Constants.prefix("obsidiceratops_edible"));
		public static final TagKey<Item> CORALOSAUR_EDIBLE_ITEM = TagKey.create(Registries.ITEM, Constants.prefix("coralosaur_edible"));
	}

	public static class Biomes {
		public static final TagKey<Biome> SPAWNS_SCULKRAPTORS = TagKey.create(Registries.BIOME, Constants.prefix("spawns_sculkraptors"));
		public static final TagKey<Biome> SPAWNS_CORALOSAURS = TagKey.create(Registries.BIOME, Constants.prefix("spawns_coralosaurs"));
		public static final TagKey<Biome> SPAWNS_ARCHIE = TagKey.create(Registries.BIOME, Constants.prefix("spawns_archie"));
	}

	public static class GameEvents {
		public static final TagKey<GameEvent> SCULKSAUR_CAN_LISTEN = TagKey.create(Registries.GAME_EVENT, Constants.prefix("sculksaur_can_listen"));

		public static final TagKey<GameEvent> SCULKRAPTOR_WILD_CAN_LISTEN = TagKey.create(Registries.GAME_EVENT, Constants.prefix("sculkraptor_wild_can_listen"));
		public static final TagKey<GameEvent> SCULKRAPTOR_TAMED_CAN_LISTEN = TagKey.create(Registries.GAME_EVENT, Constants.prefix("sculkraptor_tamed_can_listen"));
		public static final TagKey<GameEvent> SCULKRAPTOR_IGNORE_COOLDOWN = TagKey.create(Registries.GAME_EVENT, Constants.prefix("sculkraptor_ignore_cooldwon"));
	}

	public static class DamageTypes {
		public static final TagKey<DamageType> CAN_KILL_SCULKSAUR = TagKey.create(Registries.DAMAGE_TYPE, Constants.prefix("can_kill_sculksaur"));
	}
}
