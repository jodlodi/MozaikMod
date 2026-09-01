package com.mod.mozaik.reg;

import com.google.common.collect.Sets;
import com.mod.mozaik.Constants;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.Set;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ModLootTables {
	private static final Set<ResourceKey<LootTable>> MOD_LOOT_TABLES = Sets.newHashSet();
	private static final Set<ResourceKey<LootTable>> MOD_IMMUTABLE_LOCATIONS = Collections.unmodifiableSet(MOD_LOOT_TABLES);
	public static final int DEFAULT_PLACE_FLAG = Block.UPDATE_CLIENTS;

	// Chest loot
	public static final ResourceKey<LootTable> WARPED_MOSAIC_CHEST = register("chests/warped_mosaic");
	public static final ResourceKey<LootTable> LUKEWARM_MOSAIC_CHEST = register("chests/lukewarm_mosaic");
	public static final ResourceKey<LootTable> COLD_MOSAIC_CHEST = register("chests/cold_mosaic");

	public static final ResourceKey<LootTable> BUTTON_TEMPLATE_CHEST = register("chests/button_template");
	public static final ResourceKey<LootTable> BONE_TEMPLATE_CHEST = register("chests/bone_template");
	public static final ResourceKey<LootTable> BUBBLE_TEMPLATE_CHEST = register("chests/bubble_template");
	public static final ResourceKey<LootTable> WORM_TEMPLATE_CHEST = register("chests/worm_template");
	public static final ResourceKey<LootTable> CANE_TEMPLATE_CHEST = register("chests/cane_template");
	public static final ResourceKey<LootTable> POINT_TEMPLATE_CHEST = register("chests/point_template");
	public static final ResourceKey<LootTable> HORN_TEMPLATE_CHEST = register("chests/horn_template");
	public static final ResourceKey<LootTable> TREE_TEMPLATE_CHEST = register("chests/tree_template");
	public static final ResourceKey<LootTable> FORK_TEMPLATE_CHEST = register("chests/fork_template");

	// Archaeology drops
	public static final ResourceKey<LootTable> DESERT_ARCHAEOLOGY = register("archaeology/desert_mosaic");
	public static final ResourceKey<LootTable> LUKEWARM_MOSAIC_ARCHAEOLOGY = register("archaeology/lukewarm_mosaic");
	public static final ResourceKey<LootTable> COLD_MOSAIC_ARCHAEOLOGY = register("archaeology/cold_mosaic");

	public static void generateChest(WorldGenLevel world, BlockPos pos, Direction dir, boolean trapped, ResourceKey<LootTable> lootTable) {
		generateLootContainer(world, pos, (trapped ? Blocks.TRAPPED_CHEST : Blocks.CHEST).defaultBlockState().setValue(ChestBlock.FACING, dir), DEFAULT_PLACE_FLAG, lootTable);
	}

	public static void generateLootContainer(WorldGenLevel world, BlockPos pos, BlockState state, int flags, ResourceKey<LootTable> lootTable) {
		world.setBlock(pos, state, flags);
		generateChestContents(world, pos, lootTable);
	}

	public static void generateLootContainer(LevelAccessor world, BlockPos pos, BlockState state, int flags, long seed, ResourceKey<LootTable> lootTable) {
		world.setBlock(pos, state, flags);
		generateChestContents(world, pos, seed, lootTable);
	}

	public static void generateChestContents(WorldGenLevel level, BlockPos pos, ResourceKey<LootTable> lootTable) {
		generateChestContents(level, pos, level.getSeed() * pos.getX() + pos.getY() ^ pos.getZ(), lootTable);
	}

	public static void generateChestContents(LevelAccessor level, BlockPos pos, long seed, ResourceKey<LootTable> lootTable) {
		if (level.getBlockEntity(pos) instanceof RandomizableContainerBlockEntity lootContainer) lootContainer.setLootTable(lootTable, seed);
	}

	private static ResourceKey<LootTable> register(String id) {
		return register(ResourceKey.create(Registries.LOOT_TABLE, Constants.prefix(id)));
	}

	private static ResourceKey<LootTable> register(ResourceKey<LootTable> id) {
		if (MOD_LOOT_TABLES.add(id)) {
			return id;
		} else {
			throw new IllegalArgumentException(id + " is already a registered built-in loot table");
		}
	}

	public static LootParams.Builder createLootParams(LivingEntity entity, boolean checkPlayerKill, DamageSource source) {
		LootParams.Builder lootcontext$builder = (new LootParams.Builder((ServerLevel) entity.level())).withParameter(LootContextParams.THIS_ENTITY, entity).withParameter(LootContextParams.ORIGIN, entity.position()).withParameter(LootContextParams.DAMAGE_SOURCE, source).withOptionalParameter(LootContextParams.ATTACKING_ENTITY, source.getEntity()).withOptionalParameter(LootContextParams.DIRECT_ATTACKING_ENTITY, source.getDirectEntity());
		if (checkPlayerKill && entity.getKillCredit() instanceof Player player) {
			lootcontext$builder = lootcontext$builder.withParameter(LootContextParams.LAST_DAMAGE_PLAYER, player).withLuck(player.getLuck());
		}

		return lootcontext$builder;
	}

	public static Set<ResourceKey<LootTable>> allBuiltin() {
		return MOD_IMMUTABLE_LOCATIONS;
	}
}
