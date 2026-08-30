package com.mod.mozaik.data.gen;

import com.mod.mozaik.Constants;
import com.mod.mozaik.items.ShardItem;
import com.mod.mozaik.polyomino.ShardMaterial;
import com.mod.mozaik.reg.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.packs.VanillaRecipeProvider;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.ColorCollection;
import org.jspecify.annotations.NullMarked;

import java.util.concurrent.CompletableFuture;

@NullMarked
public class ModRecipeProvider extends VanillaRecipeProvider {
	private static final int PER_BLOCK = 8;
	private static final int PER_STAIR = 6;
	private static final int PER_WALL = 6;
	private static final int PER_SLAB = 4;
	private static final int PER_FENCE = 4;

	protected ModRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
		super(registries, output);
	}

	@Override
	protected void buildRecipes() {
		this.shaped(RecipeCategory.DECORATIONS, ModItems.SHARD_BAG.get(), 1)
				.pattern(" S ")
				.pattern("LVL")
				.pattern(" L ")
				.define('S', Items.STRING)
				.define('L', Items.LEATHER)
				.define('V', ModTags.Items.SHARDS)
				.unlockedBy("has_item", has(ModTags.Items.SHARDS))
				.save(this.output);

		for (DyeColor color : DyeColor.values()) {
			this.mortar(color);
		}

		this.shard(ModShardMaterials.STONE, Items.STONE, PER_BLOCK);
		this.shard(ModShardMaterials.STONE, Items.STONE_STAIRS, PER_STAIR);
		this.shard(ModShardMaterials.STONE, Items.STONE_SLAB, PER_SLAB);
		this.shard(ModShardMaterials.STONE, Items.STONE_BRICKS, PER_BLOCK);
		this.shard(ModShardMaterials.STONE, Items.STONE_BRICK_STAIRS, PER_STAIR);
		this.shard(ModShardMaterials.STONE, Items.STONE_BRICK_SLAB, PER_SLAB);
		this.shard(ModShardMaterials.STONE, Items.STONE_BRICK_WALL, PER_WALL);
		this.shard(ModShardMaterials.STONE, Items.COBBLESTONE, PER_BLOCK);
		this.shard(ModShardMaterials.STONE, Items.COBBLESTONE_STAIRS, PER_STAIR);
		this.shard(ModShardMaterials.STONE, Items.COBBLESTONE_SLAB, PER_SLAB);
		this.shard(ModShardMaterials.STONE, Items.COBBLESTONE_WALL, PER_WALL);

		this.shard(ModShardMaterials.BLACKSTONE, Items.BLACKSTONE, PER_BLOCK);
		this.shard(ModShardMaterials.BLACKSTONE, Items.BLACKSTONE_STAIRS, PER_STAIR);
		this.shard(ModShardMaterials.BLACKSTONE, Items.BLACKSTONE_SLAB, PER_SLAB);
		this.shard(ModShardMaterials.BLACKSTONE, Items.BLACKSTONE_WALL, PER_WALL);
		this.shard(ModShardMaterials.BLACKSTONE, Items.POLISHED_BLACKSTONE, PER_BLOCK);
		this.shard(ModShardMaterials.BLACKSTONE, Items.POLISHED_BLACKSTONE_STAIRS, PER_STAIR);
		this.shard(ModShardMaterials.BLACKSTONE, Items.POLISHED_BLACKSTONE_SLAB, PER_SLAB);
		this.shard(ModShardMaterials.BLACKSTONE, Items.POLISHED_BLACKSTONE_WALL, PER_WALL);
		this.shard(ModShardMaterials.BLACKSTONE, Items.POLISHED_BLACKSTONE_BRICKS, PER_BLOCK);
		this.shard(ModShardMaterials.BLACKSTONE, Items.POLISHED_BLACKSTONE_BRICK_STAIRS, PER_STAIR);
		this.shard(ModShardMaterials.BLACKSTONE, Items.POLISHED_BLACKSTONE_BRICK_SLAB, PER_SLAB);
		this.shard(ModShardMaterials.BLACKSTONE, Items.POLISHED_BLACKSTONE_BRICK_WALL, PER_WALL);

		this.shard(ModShardMaterials.GRANITE, Items.GRANITE, PER_BLOCK);
		this.shard(ModShardMaterials.GRANITE, Items.GRANITE_STAIRS, PER_STAIR);
		this.shard(ModShardMaterials.GRANITE, Items.GRANITE_SLAB, PER_SLAB);
		this.shard(ModShardMaterials.GRANITE, Items.GRANITE_WALL, PER_WALL);
		this.shard(ModShardMaterials.GRANITE, Items.POLISHED_GRANITE, PER_BLOCK);
		this.shard(ModShardMaterials.GRANITE, Items.POLISHED_GRANITE_STAIRS, PER_STAIR);
		this.shard(ModShardMaterials.GRANITE, Items.POLISHED_GRANITE_SLAB, PER_SLAB);

		this.shard(ModShardMaterials.DIORITE, Items.DIORITE, PER_BLOCK);
		this.shard(ModShardMaterials.DIORITE, Items.DIORITE_STAIRS, PER_STAIR);
		this.shard(ModShardMaterials.DIORITE, Items.DIORITE_SLAB, PER_SLAB);
		this.shard(ModShardMaterials.DIORITE, Items.DIORITE_WALL, PER_WALL);
		this.shard(ModShardMaterials.DIORITE, Items.POLISHED_DIORITE, PER_BLOCK);
		this.shard(ModShardMaterials.DIORITE, Items.POLISHED_DIORITE_STAIRS, PER_STAIR);
		this.shard(ModShardMaterials.DIORITE, Items.POLISHED_DIORITE_SLAB, PER_SLAB);

		this.shard(ModShardMaterials.ANDESITE, Items.ANDESITE, PER_BLOCK);
		this.shard(ModShardMaterials.ANDESITE, Items.ANDESITE_STAIRS, PER_STAIR);
		this.shard(ModShardMaterials.ANDESITE, Items.ANDESITE_SLAB, PER_SLAB);
		this.shard(ModShardMaterials.ANDESITE, Items.ANDESITE_WALL, PER_WALL);
		this.shard(ModShardMaterials.ANDESITE, Items.POLISHED_ANDESITE, PER_BLOCK);
		this.shard(ModShardMaterials.ANDESITE, Items.POLISHED_ANDESITE_STAIRS, PER_STAIR);
		this.shard(ModShardMaterials.ANDESITE, Items.POLISHED_ANDESITE_SLAB, PER_SLAB);

		this.shard(ModShardMaterials.DEEPSLATE, Items.DEEPSLATE, PER_BLOCK);
		this.shard(ModShardMaterials.DEEPSLATE, Items.DEEPSLATE_TILES, PER_BLOCK);
		this.shard(ModShardMaterials.DEEPSLATE, Items.DEEPSLATE_TILE_STAIRS, PER_STAIR);
		this.shard(ModShardMaterials.DEEPSLATE, Items.DEEPSLATE_TILE_SLAB, PER_SLAB);
		this.shard(ModShardMaterials.DEEPSLATE, Items.DEEPSLATE_TILE_WALL, PER_WALL);
		this.shard(ModShardMaterials.DEEPSLATE, Items.DEEPSLATE_BRICKS, PER_BLOCK);
		this.shard(ModShardMaterials.DEEPSLATE, Items.DEEPSLATE_BRICK_STAIRS, PER_STAIR);
		this.shard(ModShardMaterials.DEEPSLATE, Items.DEEPSLATE_BRICK_SLAB, PER_SLAB);
		this.shard(ModShardMaterials.DEEPSLATE, Items.DEEPSLATE_BRICK_WALL, PER_WALL);
		this.shard(ModShardMaterials.DEEPSLATE, Items.POLISHED_DEEPSLATE, PER_BLOCK);
		this.shard(ModShardMaterials.DEEPSLATE, Items.POLISHED_DEEPSLATE_STAIRS, PER_STAIR);
		this.shard(ModShardMaterials.DEEPSLATE, Items.POLISHED_DEEPSLATE_SLAB, PER_SLAB);
		this.shard(ModShardMaterials.DEEPSLATE, Items.POLISHED_DEEPSLATE_WALL, PER_WALL);
		this.shard(ModShardMaterials.DEEPSLATE, Items.COBBLED_DEEPSLATE, PER_BLOCK);
		this.shard(ModShardMaterials.DEEPSLATE, Items.COBBLED_DEEPSLATE_STAIRS, PER_STAIR);
		this.shard(ModShardMaterials.DEEPSLATE, Items.COBBLED_DEEPSLATE_SLAB, PER_SLAB);
		this.shard(ModShardMaterials.DEEPSLATE, Items.COBBLED_DEEPSLATE_WALL, PER_WALL);

		this.shard(ModShardMaterials.TUFF, Items.TUFF, PER_BLOCK);
		this.shard(ModShardMaterials.TUFF, Items.TUFF_STAIRS, PER_STAIR);
		this.shard(ModShardMaterials.TUFF, Items.TUFF_SLAB, PER_SLAB);
		this.shard(ModShardMaterials.TUFF, Items.TUFF_WALL, PER_WALL);
		this.shard(ModShardMaterials.TUFF, Items.POLISHED_TUFF, PER_BLOCK);
		this.shard(ModShardMaterials.TUFF, Items.POLISHED_TUFF_STAIRS, PER_STAIR);
		this.shard(ModShardMaterials.TUFF, Items.POLISHED_TUFF_SLAB, PER_SLAB);

		this.shard(ModShardMaterials.CALCITE, Items.CALCITE, PER_BLOCK);

		this.shard(ModShardMaterials.CINNABAR, Items.CHISELED_CINNABAR, PER_BLOCK);
		this.shard(ModShardMaterials.CINNABAR, Items.CINNABAR, PER_BLOCK);
		this.shard(ModShardMaterials.CINNABAR, Items.CINNABAR_STAIRS, PER_STAIR);
		this.shard(ModShardMaterials.CINNABAR, Items.CINNABAR_SLAB, PER_SLAB);
		this.shard(ModShardMaterials.CINNABAR, Items.CINNABAR_WALL, PER_WALL);
		this.shard(ModShardMaterials.CINNABAR, Items.CINNABAR_BRICKS, PER_BLOCK);
		this.shard(ModShardMaterials.CINNABAR, Items.CINNABAR_BRICK_STAIRS, PER_STAIR);
		this.shard(ModShardMaterials.CINNABAR, Items.CINNABAR_BRICK_SLAB, PER_SLAB);
		this.shard(ModShardMaterials.CINNABAR, Items.CINNABAR_BRICK_WALL, PER_WALL);
		this.shard(ModShardMaterials.CINNABAR, Items.POLISHED_CINNABAR, PER_BLOCK);
		this.shard(ModShardMaterials.CINNABAR, Items.POLISHED_CINNABAR_STAIRS, PER_STAIR);
		this.shard(ModShardMaterials.CINNABAR, Items.POLISHED_CINNABAR_SLAB, PER_SLAB);
		this.shard(ModShardMaterials.CINNABAR, Items.POLISHED_CINNABAR_WALL, PER_WALL);

		this.shard(ModShardMaterials.SULFUR, Items.POTENT_SULFUR, PER_BLOCK);
		this.shard(ModShardMaterials.SULFUR, Items.SULFUR, PER_BLOCK);
		this.shard(ModShardMaterials.SULFUR, Items.SULFUR_STAIRS, PER_STAIR);
		this.shard(ModShardMaterials.SULFUR, Items.SULFUR_SLAB, PER_SLAB);
		this.shard(ModShardMaterials.SULFUR, Items.SULFUR_WALL, PER_WALL);
		this.shard(ModShardMaterials.SULFUR, Items.SULFUR_BRICKS, PER_BLOCK);
		this.shard(ModShardMaterials.SULFUR, Items.SULFUR_BRICK_STAIRS, PER_STAIR);
		this.shard(ModShardMaterials.SULFUR, Items.SULFUR_BRICK_SLAB, PER_SLAB);
		this.shard(ModShardMaterials.SULFUR, Items.SULFUR_BRICK_WALL, PER_WALL);
		this.shard(ModShardMaterials.SULFUR, Items.POLISHED_SULFUR, PER_BLOCK);
		this.shard(ModShardMaterials.SULFUR, Items.POLISHED_SULFUR_STAIRS, PER_STAIR);
		this.shard(ModShardMaterials.SULFUR, Items.POLISHED_SULFUR_SLAB, PER_SLAB);
		this.shard(ModShardMaterials.SULFUR, Items.POLISHED_SULFUR_WALL, PER_WALL);

		this.shard(ModShardMaterials.DRIPSTONE, Items.DRIPSTONE_BLOCK, PER_BLOCK);

		this.shard(ModShardMaterials.MOSSY, Items.MOSSY_COBBLESTONE, PER_BLOCK);
		this.shard(ModShardMaterials.MOSSY, Items.MOSSY_COBBLESTONE_STAIRS, PER_STAIR);
		this.shard(ModShardMaterials.MOSSY, Items.MOSSY_COBBLESTONE_SLAB, PER_SLAB);
		this.shard(ModShardMaterials.MOSSY, Items.MOSSY_COBBLESTONE_WALL, PER_WALL);
		this.shard(ModShardMaterials.MOSSY, Items.MOSSY_STONE_BRICKS, PER_BLOCK);
		this.shard(ModShardMaterials.MOSSY, Items.MOSSY_STONE_BRICK_STAIRS, PER_STAIR);
		this.shard(ModShardMaterials.MOSSY, Items.MOSSY_STONE_BRICK_SLAB, PER_SLAB);
		this.shard(ModShardMaterials.MOSSY, Items.MOSSY_STONE_BRICK_WALL, PER_WALL);

		this.shard(ModShardMaterials.RESIN, Items.RESIN_BLOCK, PER_BLOCK);
		this.shard(ModShardMaterials.RESIN, Items.RESIN_BRICKS, PER_BLOCK);
		this.shard(ModShardMaterials.RESIN, Items.RESIN_BRICK_STAIRS, PER_STAIR);
		this.shard(ModShardMaterials.RESIN, Items.RESIN_BRICK_SLAB, PER_SLAB);
		this.shard(ModShardMaterials.RESIN, Items.RESIN_BRICK_WALL, PER_WALL);
		this.shard(ModShardMaterials.RESIN, Items.CHISELED_RESIN_BRICKS, PER_BLOCK);

		this.shard(ModShardMaterials.AMETHYST, Items.AMETHYST_BLOCK, PER_BLOCK);
		this.shard(ModShardMaterials.AMETHYST, Items.BUDDING_AMETHYST, PER_BLOCK);

		this.shard(ModShardMaterials.BRICK, Items.BRICKS, PER_BLOCK);
		this.shard(ModShardMaterials.BRICK, Items.BRICK_STAIRS, PER_STAIR);
		this.shard(ModShardMaterials.BRICK, Items.BRICK_SLAB, PER_SLAB);
		this.shard(ModShardMaterials.BRICK, Items.BRICK_WALL, PER_WALL);

		this.shard(ModShardMaterials.PACKED_MUD, Items.PACKED_MUD, PER_BLOCK);
		this.shard(ModShardMaterials.PACKED_MUD, Items.MUD_BRICKS, PER_BLOCK);
		this.shard(ModShardMaterials.PACKED_MUD, Items.MUD_BRICK_STAIRS, PER_STAIR);
		this.shard(ModShardMaterials.PACKED_MUD, Items.MUD_BRICK_SLAB, PER_SLAB);
		this.shard(ModShardMaterials.PACKED_MUD, Items.MUD_BRICK_WALL, PER_WALL);

		this.shard(ModShardMaterials.SANDSTONE, Items.CHISELED_SANDSTONE, PER_BLOCK);
		this.shard(ModShardMaterials.SANDSTONE, Items.CUT_SANDSTONE, PER_BLOCK);
		this.shard(ModShardMaterials.SANDSTONE, Items.CUT_STANDSTONE_SLAB, PER_SLAB);
		this.shard(ModShardMaterials.SANDSTONE, Items.SANDSTONE, PER_BLOCK);
		this.shard(ModShardMaterials.SANDSTONE, Items.SANDSTONE_STAIRS, PER_STAIR);
		this.shard(ModShardMaterials.SANDSTONE, Items.SANDSTONE_SLAB, PER_SLAB);
		this.shard(ModShardMaterials.SANDSTONE, Items.SANDSTONE_WALL, PER_WALL);
		this.shard(ModShardMaterials.SANDSTONE, Items.SMOOTH_SANDSTONE, PER_BLOCK);
		this.shard(ModShardMaterials.SANDSTONE, Items.SMOOTH_SANDSTONE_STAIRS, PER_STAIR);
		this.shard(ModShardMaterials.SANDSTONE, Items.SMOOTH_SANDSTONE_SLAB, PER_SLAB);

		this.shard(ModShardMaterials.RED_SANDSTONE, Items.CHISELED_RED_SANDSTONE, PER_BLOCK);
		this.shard(ModShardMaterials.RED_SANDSTONE, Items.CUT_RED_SANDSTONE, PER_BLOCK);
		this.shard(ModShardMaterials.RED_SANDSTONE, Items.CUT_RED_SANDSTONE_SLAB, PER_SLAB);
		this.shard(ModShardMaterials.RED_SANDSTONE, Items.RED_SANDSTONE, PER_BLOCK);
		this.shard(ModShardMaterials.RED_SANDSTONE, Items.RED_SANDSTONE_STAIRS, PER_STAIR);
		this.shard(ModShardMaterials.RED_SANDSTONE, Items.RED_SANDSTONE_SLAB, PER_SLAB);
		this.shard(ModShardMaterials.RED_SANDSTONE, Items.RED_SANDSTONE_WALL, PER_WALL);
		this.shard(ModShardMaterials.RED_SANDSTONE, Items.SMOOTH_RED_SANDSTONE, PER_BLOCK);
		this.shard(ModShardMaterials.RED_SANDSTONE, Items.SMOOTH_RED_SANDSTONE_STAIRS, PER_STAIR);
		this.shard(ModShardMaterials.RED_SANDSTONE, Items.SMOOTH_RED_SANDSTONE_SLAB, PER_SLAB);

		this.shard(ModShardMaterials.BONE, Items.BONE_BLOCK, PER_BLOCK);

		this.shard(ModShardMaterials.NETHERRACK, Items.NETHERRACK, PER_BLOCK);

		this.shard(ModShardMaterials.NETHER_BRICK, Items.NETHER_BRICK, PER_BLOCK);
		this.shard(ModShardMaterials.NETHER_BRICK, Items.CHISELED_NETHER_BRICKS, PER_BLOCK);
		this.shard(ModShardMaterials.NETHER_BRICK, Items.CRACKED_NETHER_BRICKS, PER_BLOCK);
		this.shard(ModShardMaterials.NETHER_BRICK, Items.NETHER_BRICK_STAIRS, PER_STAIR);
		this.shard(ModShardMaterials.NETHER_BRICK, Items.NETHER_BRICK_SLAB, PER_SLAB);
		this.shard(ModShardMaterials.NETHER_BRICK, Items.NETHER_BRICK_WALL, PER_WALL);
		this.shard(ModShardMaterials.NETHER_BRICK, Items.NETHER_BRICK_FENCE, PER_FENCE);

		this.shard(ModShardMaterials.RED_NETHER_BRICK, Items.RED_NETHER_BRICKS, PER_BLOCK);
		this.shard(ModShardMaterials.RED_NETHER_BRICK, Items.RED_NETHER_BRICK_STAIRS, PER_STAIR);
		this.shard(ModShardMaterials.RED_NETHER_BRICK, Items.RED_NETHER_BRICK_SLAB, PER_SLAB);
		this.shard(ModShardMaterials.RED_NETHER_BRICK, Items.RED_NETHER_BRICK_WALL, PER_WALL);

		this.shard(ModShardMaterials.QUARTZ, Items.QUARTZ_BRICKS, PER_BLOCK);
		this.shard(ModShardMaterials.QUARTZ, Items.QUARTZ_PILLAR, PER_BLOCK);
		this.shard(ModShardMaterials.QUARTZ, Items.QUARTZ_BLOCK, PER_BLOCK);
		this.shard(ModShardMaterials.QUARTZ, Items.QUARTZ_STAIRS, PER_STAIR);
		this.shard(ModShardMaterials.QUARTZ, Items.QUARTZ_SLAB, PER_SLAB);
		this.shard(ModShardMaterials.QUARTZ, Items.SMOOTH_QUARTZ, PER_BLOCK);
		this.shard(ModShardMaterials.QUARTZ, Items.SMOOTH_QUARTZ_STAIRS, PER_STAIR);
		this.shard(ModShardMaterials.QUARTZ, Items.SMOOTH_QUARTZ_SLAB, PER_SLAB);

		this.shard(ModShardMaterials.GLOWSTONE, Items.GLOWSTONE, PER_BLOCK);

		this.shard(ModShardMaterials.ANCIENT_DEBRIS, Items.ANCIENT_DEBRIS, PER_BLOCK);

		this.shard(ModShardMaterials.BASALT, Items.BASALT, PER_BLOCK);
		this.shard(ModShardMaterials.BASALT, Items.SMOOTH_BASALT, PER_BLOCK);
		this.shard(ModShardMaterials.BASALT, Items.POLISHED_BASALT, PER_BLOCK);

		this.shard(ModShardMaterials.OBSIDIAN, Items.OBSIDIAN, PER_BLOCK);

		this.shard(ModShardMaterials.CRYING_OBSIDIAN, Items.CRYING_OBSIDIAN, PER_BLOCK);

		this.shard(ModShardMaterials.END_STONE, Items.END_STONE, PER_BLOCK);
		this.shard(ModShardMaterials.END_STONE, Items.END_STONE_BRICKS, PER_BLOCK);
		this.shard(ModShardMaterials.END_STONE, Items.END_STONE_BRICK_STAIRS, PER_STAIR);
		this.shard(ModShardMaterials.END_STONE, Items.END_STONE_BRICK_SLAB, PER_SLAB);
		this.shard(ModShardMaterials.END_STONE, Items.END_STONE_BRICK_WALL, PER_WALL);

		this.shard(ModShardMaterials.PURPUR, Items.PURPUR_BLOCK, PER_BLOCK);
		this.shard(ModShardMaterials.PURPUR, Items.PURPUR_PILLAR, PER_BLOCK);
		this.shard(ModShardMaterials.PURPUR, Items.PURPUR_STAIRS, PER_STAIR);
		this.shard(ModShardMaterials.PURPUR, Items.PURPUR_SLAB, PER_SLAB);

		this.shard(ModShardMaterials.RAW_IRON, Items.RAW_IRON_BLOCK, PER_BLOCK);
		this.shard(ModShardMaterials.RAW_COPPER, Items.RAW_COPPER_BLOCK, PER_BLOCK);
		this.shard(ModShardMaterials.RAW_GOLD, Items.RAW_GOLD_BLOCK, PER_BLOCK);

		this.shard(ModShardMaterials.DARK_PRISMARINE, Items.DARK_PRISMARINE, PER_BLOCK);
		this.shard(ModShardMaterials.DARK_PRISMARINE, Items.DARK_PRISMARINE_STAIRS, PER_STAIR);
		this.shard(ModShardMaterials.DARK_PRISMARINE, Items.DARK_PRISMARINE_SLAB, PER_SLAB);

		this.shard(ModShardMaterials.PRISMARINE, Items.PRISMARINE, PER_BLOCK);
		this.shard(ModShardMaterials.PRISMARINE, Items.PRISMARINE_STAIRS, PER_STAIR);
		this.shard(ModShardMaterials.PRISMARINE, Items.PRISMARINE_SLAB, PER_SLAB);
		this.shard(ModShardMaterials.PRISMARINE, Items.PRISMARINE_WALL, PER_WALL);
		this.shard(ModShardMaterials.PRISMARINE, Items.PRISMARINE_BRICKS, PER_BLOCK);
		this.shard(ModShardMaterials.PRISMARINE, Items.PRISMARINE_BRICK_STAIRS, PER_STAIR);
		this.shard(ModShardMaterials.PRISMARINE, Items.PRISMARINE_BRICK_SLAB, PER_SLAB);

		this.shard(ModShardMaterials.SEA_LANTERN, Items.SEA_LANTERN, PER_BLOCK);

		this.shard(ModShardMaterials.TERRACOTTA, Items.TERRACOTTA, PER_BLOCK);
		ColorCollection.zipApply(ModShardMaterials.DYED_TERRACOTTA, Items.DYED_TERRACOTTA, (shard, block) -> this.shard(shard, block, PER_BLOCK));
		ColorCollection.zipApply(ModShardMaterials.GLAZED_TERRACOTTA, Items.GLAZED_TERRACOTTA, (shard, block) -> this.shard(shard, block, PER_BLOCK));
		ColorCollection.zipApply(ModShardMaterials.STAINED_GLASS, Items.STAINED_GLASS, (shard, block) -> this.shard(shard, block, PER_BLOCK));
	}

	protected void shard(ResourceSupplier<ShardMaterial> material, ItemLike source, int count) {
		this.stonecutterResultFromBase(RecipeCategory.DECORATIONS, ShardItem.SHARDS.get(ModShardMaterials.ofMaterial(material)), source, count);
	}

	protected void mortar(DyeColor color) {
		this.shaped(RecipeCategory.DECORATIONS, ModItems.MORTARS.pick(color).get(), 4)
				.pattern("CSC")
				.pattern("SWS")
				.pattern("CSC")
				.define('C', Items.CONCRETE_POWDER.pick(color))
				.define('S', Items.SLIME_BALL)
				.define('W', Items.WATER_BUCKET)
				.unlockedBy("has_item", has(Items.CONCRETE_POWDER.pick(color)))
				.save(this.output);
	}

	public static class ModRecipeRunner extends RecipeProvider.Runner {
		public ModRecipeRunner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
			super(packOutput, registries);
		}

		@Override
		protected RecipeProvider createRecipeProvider(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
			return new ModRecipeProvider(provider, recipeOutput);
		}

		@Override
		public String getName() {
			return Constants.MOD_NAME + " recipes";
		}
	}
}
