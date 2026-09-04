package com.mod.mozaik.data.gen;

import com.mod.mozaik.items.ShardItem;
import com.mod.mozaik.polyomino.ShardMaterial;
import com.mod.mozaik.reg.ModItems;
import com.mod.mozaik.reg.ModShardMaterials;
import com.mod.mozaik.reg.ModTags;
import com.mod.mozaik.reg.ResourceSupplier;
import com.mod.mozaik.util.ColorCollection;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.concurrent.CompletableFuture;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ModRecipeProvider extends RecipeProvider {
	private static final int PER_BLOCK = 8;
	private static final int PER_STAIR = 6;
	private static final int PER_WALL = 6;
	private static final int PER_SLAB = 4;
	private static final int PER_FENCE = 4;

	public ModRecipeProvider(CompletableFuture<HolderLookup.Provider> registries, PackOutput output) {
		super(output, registries);
	}

	@Override
	protected void buildRecipes(RecipeOutput output, HolderLookup.Provider provider) {
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModItems.SHARD_BAG.get(), 1)
				.pattern(" S ")
				.pattern("LVL")
				.pattern(" L ")
				.define('S', Items.STRING)
				.define('L', Items.LEATHER)
				.define('V', ModTags.Items.SHARDS)
				.unlockedBy("has_item", has(ModTags.Items.SHARDS))
				.save(output);

		for (DyeColor color : DyeColor.values()) {
			this.mortar(output, color);
		}

		this.shard(output, ModShardMaterials.STONE, Items.STONE, PER_BLOCK);
		this.shard(output, ModShardMaterials.STONE, Items.STONE_STAIRS, PER_STAIR);
		this.shard(output, ModShardMaterials.STONE, Items.STONE_SLAB, PER_SLAB);
		this.shard(output, ModShardMaterials.STONE, Items.STONE_BRICKS, PER_BLOCK);
		this.shard(output, ModShardMaterials.STONE, Items.STONE_BRICK_STAIRS, PER_STAIR);
		this.shard(output, ModShardMaterials.STONE, Items.STONE_BRICK_SLAB, PER_SLAB);
		this.shard(output, ModShardMaterials.STONE, Items.STONE_BRICK_WALL, PER_WALL);
		this.shard(output, ModShardMaterials.STONE, Items.COBBLESTONE, PER_BLOCK);
		this.shard(output, ModShardMaterials.STONE, Items.COBBLESTONE_STAIRS, PER_STAIR);
		this.shard(output, ModShardMaterials.STONE, Items.COBBLESTONE_SLAB, PER_SLAB);
		this.shard(output, ModShardMaterials.STONE, Items.COBBLESTONE_WALL, PER_WALL);

		this.shard(output, ModShardMaterials.BLACKSTONE, Items.BLACKSTONE, PER_BLOCK);
		this.shard(output, ModShardMaterials.BLACKSTONE, Items.BLACKSTONE_STAIRS, PER_STAIR);
		this.shard(output, ModShardMaterials.BLACKSTONE, Items.BLACKSTONE_SLAB, PER_SLAB);
		this.shard(output, ModShardMaterials.BLACKSTONE, Items.BLACKSTONE_WALL, PER_WALL);
		this.shard(output, ModShardMaterials.BLACKSTONE, Items.POLISHED_BLACKSTONE, PER_BLOCK);
		this.shard(output, ModShardMaterials.BLACKSTONE, Items.POLISHED_BLACKSTONE_STAIRS, PER_STAIR);
		this.shard(output, ModShardMaterials.BLACKSTONE, Items.POLISHED_BLACKSTONE_SLAB, PER_SLAB);
		this.shard(output, ModShardMaterials.BLACKSTONE, Items.POLISHED_BLACKSTONE_WALL, PER_WALL);
		this.shard(output, ModShardMaterials.BLACKSTONE, Items.POLISHED_BLACKSTONE_BRICKS, PER_BLOCK);
		this.shard(output, ModShardMaterials.BLACKSTONE, Items.POLISHED_BLACKSTONE_BRICK_STAIRS, PER_STAIR);
		this.shard(output, ModShardMaterials.BLACKSTONE, Items.POLISHED_BLACKSTONE_BRICK_SLAB, PER_SLAB);
		this.shard(output, ModShardMaterials.BLACKSTONE, Items.POLISHED_BLACKSTONE_BRICK_WALL, PER_WALL);

		this.shard(output, ModShardMaterials.GRANITE, Items.GRANITE, PER_BLOCK);
		this.shard(output, ModShardMaterials.GRANITE, Items.GRANITE_STAIRS, PER_STAIR);
		this.shard(output, ModShardMaterials.GRANITE, Items.GRANITE_SLAB, PER_SLAB);
		this.shard(output, ModShardMaterials.GRANITE, Items.GRANITE_WALL, PER_WALL);
		this.shard(output, ModShardMaterials.GRANITE, Items.POLISHED_GRANITE, PER_BLOCK);
		this.shard(output, ModShardMaterials.GRANITE, Items.POLISHED_GRANITE_STAIRS, PER_STAIR);
		this.shard(output, ModShardMaterials.GRANITE, Items.POLISHED_GRANITE_SLAB, PER_SLAB);

		this.shard(output, ModShardMaterials.DIORITE, Items.DIORITE, PER_BLOCK);
		this.shard(output, ModShardMaterials.DIORITE, Items.DIORITE_STAIRS, PER_STAIR);
		this.shard(output, ModShardMaterials.DIORITE, Items.DIORITE_SLAB, PER_SLAB);
		this.shard(output, ModShardMaterials.DIORITE, Items.DIORITE_WALL, PER_WALL);
		this.shard(output, ModShardMaterials.DIORITE, Items.POLISHED_DIORITE, PER_BLOCK);
		this.shard(output, ModShardMaterials.DIORITE, Items.POLISHED_DIORITE_STAIRS, PER_STAIR);
		this.shard(output, ModShardMaterials.DIORITE, Items.POLISHED_DIORITE_SLAB, PER_SLAB);

		this.shard(output, ModShardMaterials.ANDESITE, Items.ANDESITE, PER_BLOCK);
		this.shard(output, ModShardMaterials.ANDESITE, Items.ANDESITE_STAIRS, PER_STAIR);
		this.shard(output, ModShardMaterials.ANDESITE, Items.ANDESITE_SLAB, PER_SLAB);
		this.shard(output, ModShardMaterials.ANDESITE, Items.ANDESITE_WALL, PER_WALL);
		this.shard(output, ModShardMaterials.ANDESITE, Items.POLISHED_ANDESITE, PER_BLOCK);
		this.shard(output, ModShardMaterials.ANDESITE, Items.POLISHED_ANDESITE_STAIRS, PER_STAIR);
		this.shard(output, ModShardMaterials.ANDESITE, Items.POLISHED_ANDESITE_SLAB, PER_SLAB);

		this.shard(output, ModShardMaterials.DEEPSLATE, Items.DEEPSLATE, PER_BLOCK);
		this.shard(output, ModShardMaterials.DEEPSLATE, Items.DEEPSLATE_TILES, PER_BLOCK);
		this.shard(output, ModShardMaterials.DEEPSLATE, Items.DEEPSLATE_TILE_STAIRS, PER_STAIR);
		this.shard(output, ModShardMaterials.DEEPSLATE, Items.DEEPSLATE_TILE_SLAB, PER_SLAB);
		this.shard(output, ModShardMaterials.DEEPSLATE, Items.DEEPSLATE_TILE_WALL, PER_WALL);
		this.shard(output, ModShardMaterials.DEEPSLATE, Items.DEEPSLATE_BRICKS, PER_BLOCK);
		this.shard(output, ModShardMaterials.DEEPSLATE, Items.DEEPSLATE_BRICK_STAIRS, PER_STAIR);
		this.shard(output, ModShardMaterials.DEEPSLATE, Items.DEEPSLATE_BRICK_SLAB, PER_SLAB);
		this.shard(output, ModShardMaterials.DEEPSLATE, Items.DEEPSLATE_BRICK_WALL, PER_WALL);
		this.shard(output, ModShardMaterials.DEEPSLATE, Items.POLISHED_DEEPSLATE, PER_BLOCK);
		this.shard(output, ModShardMaterials.DEEPSLATE, Items.POLISHED_DEEPSLATE_STAIRS, PER_STAIR);
		this.shard(output, ModShardMaterials.DEEPSLATE, Items.POLISHED_DEEPSLATE_SLAB, PER_SLAB);
		this.shard(output, ModShardMaterials.DEEPSLATE, Items.POLISHED_DEEPSLATE_WALL, PER_WALL);
		this.shard(output, ModShardMaterials.DEEPSLATE, Items.COBBLED_DEEPSLATE, PER_BLOCK);
		this.shard(output, ModShardMaterials.DEEPSLATE, Items.COBBLED_DEEPSLATE_STAIRS, PER_STAIR);
		this.shard(output, ModShardMaterials.DEEPSLATE, Items.COBBLED_DEEPSLATE_SLAB, PER_SLAB);
		this.shard(output, ModShardMaterials.DEEPSLATE, Items.COBBLED_DEEPSLATE_WALL, PER_WALL);

		this.shard(output, ModShardMaterials.TUFF, Items.TUFF, PER_BLOCK);
		this.shard(output, ModShardMaterials.TUFF, Items.TUFF_STAIRS, PER_STAIR);
		this.shard(output, ModShardMaterials.TUFF, Items.TUFF_SLAB, PER_SLAB);
		this.shard(output, ModShardMaterials.TUFF, Items.TUFF_WALL, PER_WALL);
		this.shard(output, ModShardMaterials.TUFF, Items.POLISHED_TUFF, PER_BLOCK);
		this.shard(output, ModShardMaterials.TUFF, Items.POLISHED_TUFF_STAIRS, PER_STAIR);
		this.shard(output, ModShardMaterials.TUFF, Items.POLISHED_TUFF_SLAB, PER_SLAB);

		this.shard(output, ModShardMaterials.CALCITE, Items.CALCITE, PER_BLOCK);

		this.shard(output, ModShardMaterials.DRIPSTONE, Items.DRIPSTONE_BLOCK, PER_BLOCK);

		this.shard(output, ModShardMaterials.MOSSY, Items.MOSSY_COBBLESTONE, PER_BLOCK);
		this.shard(output, ModShardMaterials.MOSSY, Items.MOSSY_COBBLESTONE_STAIRS, PER_STAIR);
		this.shard(output, ModShardMaterials.MOSSY, Items.MOSSY_COBBLESTONE_SLAB, PER_SLAB);
		this.shard(output, ModShardMaterials.MOSSY, Items.MOSSY_COBBLESTONE_WALL, PER_WALL);
		this.shard(output, ModShardMaterials.MOSSY, Items.MOSSY_STONE_BRICKS, PER_BLOCK);
		this.shard(output, ModShardMaterials.MOSSY, Items.MOSSY_STONE_BRICK_STAIRS, PER_STAIR);
		this.shard(output, ModShardMaterials.MOSSY, Items.MOSSY_STONE_BRICK_SLAB, PER_SLAB);
		this.shard(output, ModShardMaterials.MOSSY, Items.MOSSY_STONE_BRICK_WALL, PER_WALL);

		this.shard(output, ModShardMaterials.AMETHYST, Items.AMETHYST_BLOCK, PER_BLOCK);
		this.shard(output, ModShardMaterials.AMETHYST, Items.BUDDING_AMETHYST, PER_BLOCK);

		this.shard(output, ModShardMaterials.BRICK, Items.BRICKS, PER_BLOCK);
		this.shard(output, ModShardMaterials.BRICK, Items.BRICK_STAIRS, PER_STAIR);
		this.shard(output, ModShardMaterials.BRICK, Items.BRICK_SLAB, PER_SLAB);
		this.shard(output, ModShardMaterials.BRICK, Items.BRICK_WALL, PER_WALL);

		this.shard(output, ModShardMaterials.PACKED_MUD, Items.PACKED_MUD, PER_BLOCK);
		this.shard(output, ModShardMaterials.PACKED_MUD, Items.MUD_BRICKS, PER_BLOCK);
		this.shard(output, ModShardMaterials.PACKED_MUD, Items.MUD_BRICK_STAIRS, PER_STAIR);
		this.shard(output, ModShardMaterials.PACKED_MUD, Items.MUD_BRICK_SLAB, PER_SLAB);
		this.shard(output, ModShardMaterials.PACKED_MUD, Items.MUD_BRICK_WALL, PER_WALL);

		this.shard(output, ModShardMaterials.SANDSTONE, Items.CHISELED_SANDSTONE, PER_BLOCK);
		this.shard(output, ModShardMaterials.SANDSTONE, Items.CUT_SANDSTONE, PER_BLOCK);
		this.shard(output, ModShardMaterials.SANDSTONE, Items.CUT_STANDSTONE_SLAB, PER_SLAB);
		this.shard(output, ModShardMaterials.SANDSTONE, Items.SANDSTONE, PER_BLOCK);
		this.shard(output, ModShardMaterials.SANDSTONE, Items.SANDSTONE_STAIRS, PER_STAIR);
		this.shard(output, ModShardMaterials.SANDSTONE, Items.SANDSTONE_SLAB, PER_SLAB);
		this.shard(output, ModShardMaterials.SANDSTONE, Items.SANDSTONE_WALL, PER_WALL);
		this.shard(output, ModShardMaterials.SANDSTONE, Items.SMOOTH_SANDSTONE, PER_BLOCK);
		this.shard(output, ModShardMaterials.SANDSTONE, Items.SMOOTH_SANDSTONE_STAIRS, PER_STAIR);
		this.shard(output, ModShardMaterials.SANDSTONE, Items.SMOOTH_SANDSTONE_SLAB, PER_SLAB);

		this.shard(output, ModShardMaterials.RED_SANDSTONE, Items.CHISELED_RED_SANDSTONE, PER_BLOCK);
		this.shard(output, ModShardMaterials.RED_SANDSTONE, Items.CUT_RED_SANDSTONE, PER_BLOCK);
		this.shard(output, ModShardMaterials.RED_SANDSTONE, Items.CUT_RED_SANDSTONE_SLAB, PER_SLAB);
		this.shard(output, ModShardMaterials.RED_SANDSTONE, Items.RED_SANDSTONE, PER_BLOCK);
		this.shard(output, ModShardMaterials.RED_SANDSTONE, Items.RED_SANDSTONE_STAIRS, PER_STAIR);
		this.shard(output, ModShardMaterials.RED_SANDSTONE, Items.RED_SANDSTONE_SLAB, PER_SLAB);
		this.shard(output, ModShardMaterials.RED_SANDSTONE, Items.RED_SANDSTONE_WALL, PER_WALL);
		this.shard(output, ModShardMaterials.RED_SANDSTONE, Items.SMOOTH_RED_SANDSTONE, PER_BLOCK);
		this.shard(output, ModShardMaterials.RED_SANDSTONE, Items.SMOOTH_RED_SANDSTONE_STAIRS, PER_STAIR);
		this.shard(output, ModShardMaterials.RED_SANDSTONE, Items.SMOOTH_RED_SANDSTONE_SLAB, PER_SLAB);

		this.shard(output, ModShardMaterials.BONE, Items.BONE_BLOCK, PER_BLOCK);

		this.shard(output, ModShardMaterials.NETHERRACK, Items.NETHERRACK, PER_BLOCK);

		this.shard(output, ModShardMaterials.NETHER_BRICK, Items.NETHER_BRICK, PER_BLOCK);
		this.shard(output, ModShardMaterials.NETHER_BRICK, Items.CHISELED_NETHER_BRICKS, PER_BLOCK);
		this.shard(output, ModShardMaterials.NETHER_BRICK, Items.CRACKED_NETHER_BRICKS, PER_BLOCK);
		this.shard(output, ModShardMaterials.NETHER_BRICK, Items.NETHER_BRICK_STAIRS, PER_STAIR);
		this.shard(output, ModShardMaterials.NETHER_BRICK, Items.NETHER_BRICK_SLAB, PER_SLAB);
		this.shard(output, ModShardMaterials.NETHER_BRICK, Items.NETHER_BRICK_WALL, PER_WALL);
		this.shard(output, ModShardMaterials.NETHER_BRICK, Items.NETHER_BRICK_FENCE, PER_FENCE);

		this.shard(output, ModShardMaterials.RED_NETHER_BRICK, Items.RED_NETHER_BRICKS, PER_BLOCK);
		this.shard(output, ModShardMaterials.RED_NETHER_BRICK, Items.RED_NETHER_BRICK_STAIRS, PER_STAIR);
		this.shard(output, ModShardMaterials.RED_NETHER_BRICK, Items.RED_NETHER_BRICK_SLAB, PER_SLAB);
		this.shard(output, ModShardMaterials.RED_NETHER_BRICK, Items.RED_NETHER_BRICK_WALL, PER_WALL);

		this.shard(output, ModShardMaterials.QUARTZ, Items.QUARTZ_BRICKS, PER_BLOCK);
		this.shard(output, ModShardMaterials.QUARTZ, Items.QUARTZ_PILLAR, PER_BLOCK);
		this.shard(output, ModShardMaterials.QUARTZ, Items.QUARTZ_BLOCK, PER_BLOCK);
		this.shard(output, ModShardMaterials.QUARTZ, Items.QUARTZ_STAIRS, PER_STAIR);
		this.shard(output, ModShardMaterials.QUARTZ, Items.QUARTZ_SLAB, PER_SLAB);
		this.shard(output, ModShardMaterials.QUARTZ, Items.SMOOTH_QUARTZ, PER_BLOCK);
		this.shard(output, ModShardMaterials.QUARTZ, Items.SMOOTH_QUARTZ_STAIRS, PER_STAIR);
		this.shard(output, ModShardMaterials.QUARTZ, Items.SMOOTH_QUARTZ_SLAB, PER_SLAB);

		this.shard(output, ModShardMaterials.GLOWSTONE, Items.GLOWSTONE, PER_BLOCK);

		this.shard(output, ModShardMaterials.ANCIENT_DEBRIS, Items.ANCIENT_DEBRIS, PER_BLOCK);

		this.shard(output, ModShardMaterials.BASALT, Items.BASALT, PER_BLOCK);
		this.shard(output, ModShardMaterials.BASALT, Items.SMOOTH_BASALT, PER_BLOCK);
		this.shard(output, ModShardMaterials.BASALT, Items.POLISHED_BASALT, PER_BLOCK);

		this.shard(output, ModShardMaterials.OBSIDIAN, Items.OBSIDIAN, PER_BLOCK);

		this.shard(output, ModShardMaterials.CRYING_OBSIDIAN, Items.CRYING_OBSIDIAN, PER_BLOCK);

		this.shard(output, ModShardMaterials.END_STONE, Items.END_STONE, PER_BLOCK);
		this.shard(output, ModShardMaterials.END_STONE, Items.END_STONE_BRICKS, PER_BLOCK);
		this.shard(output, ModShardMaterials.END_STONE, Items.END_STONE_BRICK_STAIRS, PER_STAIR);
		this.shard(output, ModShardMaterials.END_STONE, Items.END_STONE_BRICK_SLAB, PER_SLAB);
		this.shard(output, ModShardMaterials.END_STONE, Items.END_STONE_BRICK_WALL, PER_WALL);

		this.shard(output, ModShardMaterials.PURPUR, Items.PURPUR_BLOCK, PER_BLOCK);
		this.shard(output, ModShardMaterials.PURPUR, Items.PURPUR_PILLAR, PER_BLOCK);
		this.shard(output, ModShardMaterials.PURPUR, Items.PURPUR_STAIRS, PER_STAIR);
		this.shard(output, ModShardMaterials.PURPUR, Items.PURPUR_SLAB, PER_SLAB);

		this.shard(output, ModShardMaterials.RAW_IRON, Items.RAW_IRON_BLOCK, PER_BLOCK);
		this.shard(output, ModShardMaterials.RAW_COPPER, Items.RAW_COPPER_BLOCK, PER_BLOCK);
		this.shard(output, ModShardMaterials.RAW_GOLD, Items.RAW_GOLD_BLOCK, PER_BLOCK);

		this.shard(output, ModShardMaterials.DARK_PRISMARINE, Items.DARK_PRISMARINE, PER_BLOCK);
		this.shard(output, ModShardMaterials.DARK_PRISMARINE, Items.DARK_PRISMARINE_STAIRS, PER_STAIR);
		this.shard(output, ModShardMaterials.DARK_PRISMARINE, Items.DARK_PRISMARINE_SLAB, PER_SLAB);

		this.shard(output, ModShardMaterials.PRISMARINE, Items.PRISMARINE, PER_BLOCK);
		this.shard(output, ModShardMaterials.PRISMARINE, Items.PRISMARINE_STAIRS, PER_STAIR);
		this.shard(output, ModShardMaterials.PRISMARINE, Items.PRISMARINE_SLAB, PER_SLAB);
		this.shard(output, ModShardMaterials.PRISMARINE, Items.PRISMARINE_WALL, PER_WALL);
		this.shard(output, ModShardMaterials.PRISMARINE, Items.PRISMARINE_BRICKS, PER_BLOCK);
		this.shard(output, ModShardMaterials.PRISMARINE, Items.PRISMARINE_BRICK_STAIRS, PER_STAIR);
		this.shard(output, ModShardMaterials.PRISMARINE, Items.PRISMARINE_BRICK_SLAB, PER_SLAB);

		this.shard(output, ModShardMaterials.SEA_LANTERN, Items.SEA_LANTERN, PER_BLOCK);

		this.shard(output, ModShardMaterials.TERRACOTTA, Items.TERRACOTTA, PER_BLOCK);
		ColorCollection.zipApply(ModShardMaterials.DYED_TERRACOTTA, ColorCollection.BlockCollections.DYED_TERRACOTTA, (shard, block) -> this.shard(output, shard, block, PER_BLOCK));
		ColorCollection.zipApply(ModShardMaterials.GLAZED_TERRACOTTA, ColorCollection.BlockCollections.GLAZED_TERRACOTTA, (shard, block) -> this.shard(output, shard, block, PER_BLOCK));
		ColorCollection.zipApply(ModShardMaterials.STAINED_GLASS, ColorCollection.BlockCollections.STAINED_GLASS, (shard, block) -> this.shard(output, shard, block, PER_BLOCK));
	}

	protected void shard(RecipeOutput output, ResourceSupplier<ShardMaterial> material, ItemLike source, int count) {
		stonecutterResultFromBase(output, RecipeCategory.DECORATIONS, ShardItem.SHARDS.get(ModShardMaterials.ofMaterial(material)), source, count);
	}

	protected void mortar(RecipeOutput output, DyeColor color) {
		ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModItems.MORTARS.pick(color).get(), 4)
				.pattern("CSC")
				.pattern("SWS")
				.pattern("CSC")
				.define('C', ColorCollection.ItemCollections.CONCRETE_POWDER.pick(color))
				.define('S', Items.SLIME_BALL)
				.define('W', Items.WATER_BUCKET)
				.unlockedBy("has_item", has(ColorCollection.ItemCollections.CONCRETE_POWDER.pick(color)))
				.save(output);
	}
}
