package com.mod.mozaik.data.gen;

import com.mod.mozaik.Constants;
import com.mod.mozaik.polyomino.TesseraMaterial;
import com.mod.mozaik.reg.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.packs.VanillaRecipeProvider;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
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
		for (DyeColor color : DyeColor.values()) {
			this.mortar(color);
		}

		this.shard(TesseraMaterial.STONE, Items.STONE, PER_BLOCK);
		this.shard(TesseraMaterial.STONE, Items.STONE_STAIRS, PER_STAIR);
		this.shard(TesseraMaterial.STONE, Items.STONE_SLAB, PER_SLAB);
		this.shard(TesseraMaterial.STONE, Items.STONE_BRICKS, PER_BLOCK);
		this.shard(TesseraMaterial.STONE, Items.STONE_BRICK_STAIRS, PER_STAIR);
		this.shard(TesseraMaterial.STONE, Items.STONE_BRICK_SLAB, PER_SLAB);
		this.shard(TesseraMaterial.STONE, Items.STONE_BRICK_WALL, PER_WALL);

		this.shard(TesseraMaterial.COBBLESTONE, Items.COBBLESTONE, PER_BLOCK);
		this.shard(TesseraMaterial.COBBLESTONE, Items.COBBLESTONE_STAIRS, PER_STAIR);
		this.shard(TesseraMaterial.COBBLESTONE, Items.COBBLESTONE_SLAB, PER_SLAB);
		this.shard(TesseraMaterial.COBBLESTONE, Items.COBBLESTONE_WALL, PER_WALL);

		this.shard(TesseraMaterial.BLACKSTONE, Items.BLACKSTONE, PER_BLOCK);
		this.shard(TesseraMaterial.BLACKSTONE, Items.BLACKSTONE_STAIRS, PER_STAIR);
		this.shard(TesseraMaterial.BLACKSTONE, Items.BLACKSTONE_SLAB, PER_SLAB);
		this.shard(TesseraMaterial.BLACKSTONE, Items.BLACKSTONE_WALL, PER_WALL);
		this.shard(TesseraMaterial.BLACKSTONE, Items.POLISHED_BLACKSTONE, PER_BLOCK);
		this.shard(TesseraMaterial.BLACKSTONE, Items.POLISHED_BLACKSTONE_STAIRS, PER_STAIR);
		this.shard(TesseraMaterial.BLACKSTONE, Items.POLISHED_BLACKSTONE_SLAB, PER_SLAB);
		this.shard(TesseraMaterial.BLACKSTONE, Items.POLISHED_BLACKSTONE_WALL, PER_WALL);
		this.shard(TesseraMaterial.BLACKSTONE, Items.POLISHED_BLACKSTONE_BRICKS, PER_BLOCK);
		this.shard(TesseraMaterial.BLACKSTONE, Items.POLISHED_BLACKSTONE_BRICK_STAIRS, PER_STAIR);
		this.shard(TesseraMaterial.BLACKSTONE, Items.POLISHED_BLACKSTONE_BRICK_SLAB, PER_SLAB);
		this.shard(TesseraMaterial.BLACKSTONE, Items.POLISHED_BLACKSTONE_BRICK_WALL, PER_WALL);

		this.shard(TesseraMaterial.GRANITE, Items.GRANITE, PER_BLOCK);
		this.shard(TesseraMaterial.GRANITE, Items.GRANITE_STAIRS, PER_STAIR);
		this.shard(TesseraMaterial.GRANITE, Items.GRANITE_SLAB, PER_SLAB);
		this.shard(TesseraMaterial.GRANITE, Items.GRANITE_WALL, PER_WALL);
		this.shard(TesseraMaterial.GRANITE, Items.POLISHED_GRANITE, PER_BLOCK);
		this.shard(TesseraMaterial.GRANITE, Items.POLISHED_GRANITE_STAIRS, PER_STAIR);
		this.shard(TesseraMaterial.GRANITE, Items.POLISHED_GRANITE_SLAB, PER_SLAB);

		this.shard(TesseraMaterial.DIORITE, Items.DIORITE, PER_BLOCK);
		this.shard(TesseraMaterial.DIORITE, Items.DIORITE_STAIRS, PER_STAIR);
		this.shard(TesseraMaterial.DIORITE, Items.DIORITE_SLAB, PER_SLAB);
		this.shard(TesseraMaterial.DIORITE, Items.DIORITE_WALL, PER_WALL);
		this.shard(TesseraMaterial.DIORITE, Items.POLISHED_DIORITE, PER_BLOCK);
		this.shard(TesseraMaterial.DIORITE, Items.POLISHED_DIORITE_STAIRS, PER_STAIR);
		this.shard(TesseraMaterial.DIORITE, Items.POLISHED_DIORITE_SLAB, PER_SLAB);

		this.shard(TesseraMaterial.ANDESITE, Items.ANDESITE, PER_BLOCK);
		this.shard(TesseraMaterial.ANDESITE, Items.ANDESITE_STAIRS, PER_STAIR);
		this.shard(TesseraMaterial.ANDESITE, Items.ANDESITE_SLAB, PER_SLAB);
		this.shard(TesseraMaterial.ANDESITE, Items.ANDESITE_WALL, PER_WALL);
		this.shard(TesseraMaterial.ANDESITE, Items.POLISHED_ANDESITE, PER_BLOCK);
		this.shard(TesseraMaterial.ANDESITE, Items.POLISHED_ANDESITE_STAIRS, PER_STAIR);
		this.shard(TesseraMaterial.ANDESITE, Items.POLISHED_ANDESITE_SLAB, PER_SLAB);

		this.shard(TesseraMaterial.DEEPSLATE, Items.DEEPSLATE, PER_BLOCK);
		this.shard(TesseraMaterial.DEEPSLATE, Items.DEEPSLATE_TILES, PER_BLOCK);
		this.shard(TesseraMaterial.DEEPSLATE, Items.DEEPSLATE_TILE_STAIRS, PER_STAIR);
		this.shard(TesseraMaterial.DEEPSLATE, Items.DEEPSLATE_TILE_SLAB, PER_SLAB);
		this.shard(TesseraMaterial.DEEPSLATE, Items.DEEPSLATE_TILE_WALL, PER_WALL);
		this.shard(TesseraMaterial.DEEPSLATE, Items.DEEPSLATE_BRICKS, PER_BLOCK);
		this.shard(TesseraMaterial.DEEPSLATE, Items.DEEPSLATE_BRICK_STAIRS, PER_STAIR);
		this.shard(TesseraMaterial.DEEPSLATE, Items.DEEPSLATE_BRICK_SLAB, PER_SLAB);
		this.shard(TesseraMaterial.DEEPSLATE, Items.DEEPSLATE_BRICK_WALL, PER_WALL);
		this.shard(TesseraMaterial.DEEPSLATE, Items.POLISHED_DEEPSLATE, PER_BLOCK);
		this.shard(TesseraMaterial.DEEPSLATE, Items.POLISHED_DEEPSLATE_STAIRS, PER_STAIR);
		this.shard(TesseraMaterial.DEEPSLATE, Items.POLISHED_DEEPSLATE_SLAB, PER_SLAB);
		this.shard(TesseraMaterial.DEEPSLATE, Items.POLISHED_DEEPSLATE_WALL, PER_WALL);
		this.shard(TesseraMaterial.DEEPSLATE, Items.COBBLED_DEEPSLATE, PER_BLOCK);
		this.shard(TesseraMaterial.DEEPSLATE, Items.COBBLED_DEEPSLATE_STAIRS, PER_STAIR);
		this.shard(TesseraMaterial.DEEPSLATE, Items.COBBLED_DEEPSLATE_SLAB, PER_SLAB);
		this.shard(TesseraMaterial.DEEPSLATE, Items.COBBLED_DEEPSLATE_WALL, PER_WALL);

		this.shard(TesseraMaterial.TUFF, Items.TUFF, PER_BLOCK);
		this.shard(TesseraMaterial.TUFF, Items.TUFF_STAIRS, PER_STAIR);
		this.shard(TesseraMaterial.TUFF, Items.TUFF_SLAB, PER_SLAB);
		this.shard(TesseraMaterial.TUFF, Items.TUFF_WALL, PER_WALL);
		this.shard(TesseraMaterial.TUFF, Items.POLISHED_TUFF, PER_BLOCK);
		this.shard(TesseraMaterial.TUFF, Items.POLISHED_TUFF_STAIRS, PER_STAIR);
		this.shard(TesseraMaterial.TUFF, Items.POLISHED_TUFF_SLAB, PER_SLAB);

		this.shard(TesseraMaterial.BRICK, Items.BRICKS, PER_BLOCK);
		this.shard(TesseraMaterial.BRICK, Items.BRICK_STAIRS, PER_STAIR);
		this.shard(TesseraMaterial.BRICK, Items.BRICK_SLAB, PER_SLAB);
		this.shard(TesseraMaterial.BRICK, Items.BRICK_WALL, PER_WALL);

		this.shard(TesseraMaterial.PACKED_MUD, Items.PACKED_MUD, PER_BLOCK);
		this.shard(TesseraMaterial.PACKED_MUD, Items.MUD_BRICKS, PER_BLOCK);
		this.shard(TesseraMaterial.PACKED_MUD, Items.MUD_BRICK_STAIRS, PER_STAIR);
		this.shard(TesseraMaterial.PACKED_MUD, Items.MUD_BRICK_SLAB, PER_SLAB);
		this.shard(TesseraMaterial.PACKED_MUD, Items.MUD_BRICK_WALL, PER_WALL);

		this.shard(TesseraMaterial.CALCITE, Items.CALCITE, PER_BLOCK);

		this.shard(TesseraMaterial.SANDSTONE, Items.CHISELED_SANDSTONE, PER_BLOCK);
		this.shard(TesseraMaterial.SANDSTONE, Items.CUT_SANDSTONE, PER_BLOCK);
		this.shard(TesseraMaterial.SANDSTONE, Items.CUT_STANDSTONE_SLAB, PER_SLAB);
		this.shard(TesseraMaterial.SANDSTONE, Items.SANDSTONE, PER_BLOCK);
		this.shard(TesseraMaterial.SANDSTONE, Items.SANDSTONE_STAIRS, PER_STAIR);
		this.shard(TesseraMaterial.SANDSTONE, Items.SANDSTONE_SLAB, PER_SLAB);
		this.shard(TesseraMaterial.SANDSTONE, Items.SANDSTONE_WALL, PER_WALL);
		this.shard(TesseraMaterial.SANDSTONE, Items.SMOOTH_SANDSTONE, PER_BLOCK);
		this.shard(TesseraMaterial.SANDSTONE, Items.SMOOTH_SANDSTONE_STAIRS, PER_STAIR);
		this.shard(TesseraMaterial.SANDSTONE, Items.SMOOTH_SANDSTONE_SLAB, PER_SLAB);

		this.shard(TesseraMaterial.RED_SANDSTONE, Items.CHISELED_RED_SANDSTONE, PER_BLOCK);
		this.shard(TesseraMaterial.RED_SANDSTONE, Items.CUT_RED_SANDSTONE, PER_BLOCK);
		this.shard(TesseraMaterial.RED_SANDSTONE, Items.CUT_RED_SANDSTONE_SLAB, PER_SLAB);
		this.shard(TesseraMaterial.RED_SANDSTONE, Items.RED_SANDSTONE, PER_BLOCK);
		this.shard(TesseraMaterial.RED_SANDSTONE, Items.RED_SANDSTONE_STAIRS, PER_STAIR);
		this.shard(TesseraMaterial.RED_SANDSTONE, Items.RED_SANDSTONE_SLAB, PER_SLAB);
		this.shard(TesseraMaterial.RED_SANDSTONE, Items.RED_SANDSTONE_WALL, PER_WALL);
		this.shard(TesseraMaterial.RED_SANDSTONE, Items.SMOOTH_RED_SANDSTONE, PER_BLOCK);
		this.shard(TesseraMaterial.RED_SANDSTONE, Items.SMOOTH_RED_SANDSTONE_STAIRS, PER_STAIR);
		this.shard(TesseraMaterial.RED_SANDSTONE, Items.SMOOTH_RED_SANDSTONE_SLAB, PER_SLAB);

		this.shard(TesseraMaterial.CINNABAR, Items.CHISELED_CINNABAR, PER_BLOCK);
		this.shard(TesseraMaterial.CINNABAR, Items.CINNABAR, PER_BLOCK);
		this.shard(TesseraMaterial.CINNABAR, Items.CINNABAR_STAIRS, PER_STAIR);
		this.shard(TesseraMaterial.CINNABAR, Items.CINNABAR_SLAB, PER_SLAB);
		this.shard(TesseraMaterial.CINNABAR, Items.CINNABAR_WALL, PER_WALL);
		this.shard(TesseraMaterial.CINNABAR, Items.CINNABAR_BRICKS, PER_BLOCK);
		this.shard(TesseraMaterial.CINNABAR, Items.CINNABAR_BRICK_STAIRS, PER_STAIR);
		this.shard(TesseraMaterial.CINNABAR, Items.CINNABAR_BRICK_SLAB, PER_SLAB);
		this.shard(TesseraMaterial.CINNABAR, Items.CINNABAR_BRICK_WALL, PER_WALL);
		this.shard(TesseraMaterial.CINNABAR, Items.POLISHED_CINNABAR, PER_BLOCK);
		this.shard(TesseraMaterial.CINNABAR, Items.POLISHED_CINNABAR_STAIRS, PER_STAIR);
		this.shard(TesseraMaterial.CINNABAR, Items.POLISHED_CINNABAR_SLAB, PER_SLAB);
		this.shard(TesseraMaterial.CINNABAR, Items.POLISHED_CINNABAR_WALL, PER_WALL);

		this.shard(TesseraMaterial.NETHERRACK, Items.NETHERRACK, PER_BLOCK);

		this.shard(TesseraMaterial.NETHER_BRICK, Items.NETHER_BRICK, PER_BLOCK);
		this.shard(TesseraMaterial.NETHER_BRICK, Items.CHISELED_NETHER_BRICKS, PER_BLOCK);
		this.shard(TesseraMaterial.NETHER_BRICK, Items.CRACKED_NETHER_BRICKS, PER_BLOCK);
		this.shard(TesseraMaterial.NETHER_BRICK, Items.NETHER_BRICK_STAIRS, PER_STAIR);
		this.shard(TesseraMaterial.NETHER_BRICK, Items.NETHER_BRICK_SLAB, PER_SLAB);
		this.shard(TesseraMaterial.NETHER_BRICK, Items.NETHER_BRICK_WALL, PER_WALL);
		this.shard(TesseraMaterial.NETHER_BRICK, Items.NETHER_BRICK_FENCE, PER_FENCE);

		this.shard(TesseraMaterial.RED_NETHER_BRICK, Items.RED_NETHER_BRICKS, PER_BLOCK);
		this.shard(TesseraMaterial.RED_NETHER_BRICK, Items.RED_NETHER_BRICK_STAIRS, PER_STAIR);
		this.shard(TesseraMaterial.RED_NETHER_BRICK, Items.RED_NETHER_BRICK_SLAB, PER_SLAB);
		this.shard(TesseraMaterial.RED_NETHER_BRICK, Items.RED_NETHER_BRICK_WALL, PER_WALL);

		this.shard(TesseraMaterial.QUARTZ, Items.QUARTZ_BRICKS, PER_BLOCK);
		this.shard(TesseraMaterial.QUARTZ, Items.QUARTZ_PILLAR, PER_BLOCK);
		this.shard(TesseraMaterial.QUARTZ, Items.QUARTZ_BLOCK, PER_BLOCK);
		this.shard(TesseraMaterial.QUARTZ, Items.QUARTZ_STAIRS, PER_STAIR);
		this.shard(TesseraMaterial.QUARTZ, Items.QUARTZ_SLAB, PER_SLAB);
		this.shard(TesseraMaterial.QUARTZ, Items.SMOOTH_QUARTZ, PER_BLOCK);
		this.shard(TesseraMaterial.QUARTZ, Items.SMOOTH_QUARTZ_STAIRS, PER_STAIR);
		this.shard(TesseraMaterial.QUARTZ, Items.SMOOTH_QUARTZ_SLAB, PER_SLAB);

		this.shard(TesseraMaterial.GLOWSTONE, Items.GLOWSTONE, PER_BLOCK);

		this.shard(TesseraMaterial.ANCIENT_DEBRIS, Items.ANCIENT_DEBRIS, PER_BLOCK);

		this.shard(TesseraMaterial.BASALT, Items.BASALT, PER_BLOCK);
		this.shard(TesseraMaterial.BASALT, Items.SMOOTH_BASALT, PER_BLOCK);
		this.shard(TesseraMaterial.BASALT, Items.POLISHED_BASALT, PER_BLOCK);

		this.shard(TesseraMaterial.END_STONE, Items.END_STONE, PER_BLOCK);
		this.shard(TesseraMaterial.END_STONE, Items.END_STONE_BRICKS, PER_BLOCK);
		this.shard(TesseraMaterial.END_STONE, Items.END_STONE_BRICK_STAIRS, PER_STAIR);
		this.shard(TesseraMaterial.END_STONE, Items.END_STONE_BRICK_SLAB, PER_SLAB);
		this.shard(TesseraMaterial.END_STONE, Items.END_STONE_BRICK_WALL, PER_WALL);

		this.shard(TesseraMaterial.PURPUR, Items.PURPUR_BLOCK, PER_BLOCK);
		this.shard(TesseraMaterial.PURPUR, Items.PURPUR_PILLAR, PER_BLOCK);
		this.shard(TesseraMaterial.PURPUR, Items.PURPUR_STAIRS, PER_STAIR);
		this.shard(TesseraMaterial.PURPUR, Items.PURPUR_SLAB, PER_SLAB);

		this.shard(TesseraMaterial.BLOCK_OF_RAW_IRON, Items.RAW_IRON_BLOCK, PER_BLOCK);
		this.shard(TesseraMaterial.BLOCK_OF_RAW_COPPER, Items.RAW_COPPER_BLOCK, PER_BLOCK);
		this.shard(TesseraMaterial.BLOCK_OF_RAW_GOLD, Items.RAW_GOLD_BLOCK, PER_BLOCK);

		this.shard(TesseraMaterial.DARK_PRISMARINE, Items.DARK_PRISMARINE, PER_BLOCK);
		this.shard(TesseraMaterial.DARK_PRISMARINE, Items.DARK_PRISMARINE_STAIRS, PER_STAIR);
		this.shard(TesseraMaterial.DARK_PRISMARINE, Items.DARK_PRISMARINE_SLAB, PER_SLAB);

		this.shard(TesseraMaterial.PRISMARINE, Items.PRISMARINE, PER_BLOCK);
		this.shard(TesseraMaterial.PRISMARINE, Items.PRISMARINE_STAIRS, PER_STAIR);
		this.shard(TesseraMaterial.PRISMARINE, Items.PRISMARINE_SLAB, PER_SLAB);
		this.shard(TesseraMaterial.PRISMARINE, Items.PRISMARINE_WALL, PER_WALL);
		this.shard(TesseraMaterial.PRISMARINE, Items.PRISMARINE_BRICKS, PER_BLOCK);
		this.shard(TesseraMaterial.PRISMARINE, Items.PRISMARINE_BRICK_STAIRS, PER_STAIR);
		this.shard(TesseraMaterial.PRISMARINE, Items.PRISMARINE_BRICK_SLAB, PER_SLAB);

		this.shard(TesseraMaterial.SEA_LANTERN, Items.SEA_LANTERN, PER_BLOCK);

		this.shard(TesseraMaterial.TERRACOTTA, Items.TERRACOTTA, PER_BLOCK);
		this.shard(TesseraMaterial.BLACK_TERRACOTTA, Items.DYED_TERRACOTTA.black(), PER_BLOCK);
		this.shard(TesseraMaterial.BLUE_TERRACOTTA, Items.DYED_TERRACOTTA.blue(), PER_BLOCK);
		this.shard(TesseraMaterial.BROWN_TERRACOTTA, Items.DYED_TERRACOTTA.brown(), PER_BLOCK);
		this.shard(TesseraMaterial.CYAN_TERRACOTTA, Items.DYED_TERRACOTTA.cyan(), PER_BLOCK);
		this.shard(TesseraMaterial.GRAY_TERRACOTTA, Items.DYED_TERRACOTTA.gray(), PER_BLOCK);
		this.shard(TesseraMaterial.GREEN_TERRACOTTA, Items.DYED_TERRACOTTA.green(), PER_BLOCK);
		this.shard(TesseraMaterial.LIGHT_BLUE_TERRACOTTA, Items.DYED_TERRACOTTA.lightBlue(), PER_BLOCK);
		this.shard(TesseraMaterial.LIGHT_GRAY_TERRACOTTA, Items.DYED_TERRACOTTA.lightGray(), PER_BLOCK);
		this.shard(TesseraMaterial.LIME_TERRACOTTA, Items.DYED_TERRACOTTA.lime(), PER_BLOCK);
		this.shard(TesseraMaterial.MAGENTA_TERRACOTTA, Items.DYED_TERRACOTTA.magenta(), PER_BLOCK);
		this.shard(TesseraMaterial.ORANGE_TERRACOTTA, Items.DYED_TERRACOTTA.orange(), PER_BLOCK);
		this.shard(TesseraMaterial.PINK_TERRACOTTA, Items.DYED_TERRACOTTA.pink(), PER_BLOCK);
		this.shard(TesseraMaterial.PURPLE_TERRACOTTA, Items.DYED_TERRACOTTA.purple(), PER_BLOCK);
		this.shard(TesseraMaterial.RED_TERRACOTTA, Items.DYED_TERRACOTTA.red(), PER_BLOCK);
		this.shard(TesseraMaterial.WHITE_TERRACOTTA, Items.DYED_TERRACOTTA.white(), PER_BLOCK);
		this.shard(TesseraMaterial.YELLOW_TERRACOTTA, Items.DYED_TERRACOTTA.yellow(), PER_BLOCK);

		this.shard(TesseraMaterial.BLACK_GLAZED_TERRACOTTA, Items.GLAZED_TERRACOTTA.black(), PER_BLOCK);
		this.shard(TesseraMaterial.BLUE_GLAZED_TERRACOTTA, Items.GLAZED_TERRACOTTA.blue(), PER_BLOCK);
		this.shard(TesseraMaterial.BROWN_GLAZED_TERRACOTTA, Items.GLAZED_TERRACOTTA.brown(), PER_BLOCK);
		this.shard(TesseraMaterial.CYAN_GLAZED_TERRACOTTA, Items.GLAZED_TERRACOTTA.cyan(), PER_BLOCK);
		this.shard(TesseraMaterial.GRAY_GLAZED_TERRACOTTA, Items.GLAZED_TERRACOTTA.gray(), PER_BLOCK);
		this.shard(TesseraMaterial.GREEN_GLAZED_TERRACOTTA, Items.GLAZED_TERRACOTTA.green(), PER_BLOCK);
		this.shard(TesseraMaterial.LIGHT_BLUE_GLAZED_TERRACOTTA, Items.GLAZED_TERRACOTTA.lightBlue(), PER_BLOCK);
		this.shard(TesseraMaterial.LIGHT_GRAY_GLAZED_TERRACOTTA, Items.GLAZED_TERRACOTTA.lightGray(), PER_BLOCK);
		this.shard(TesseraMaterial.LIME_GLAZED_TERRACOTTA, Items.GLAZED_TERRACOTTA.lime(), PER_BLOCK);
		this.shard(TesseraMaterial.MAGENTA_GLAZED_TERRACOTTA, Items.GLAZED_TERRACOTTA.magenta(), PER_BLOCK);
		this.shard(TesseraMaterial.ORANGE_GLAZED_TERRACOTTA, Items.GLAZED_TERRACOTTA.orange(), PER_BLOCK);
		this.shard(TesseraMaterial.PINK_GLAZED_TERRACOTTA, Items.GLAZED_TERRACOTTA.pink(), PER_BLOCK);
		this.shard(TesseraMaterial.PURPLE_GLAZED_TERRACOTTA, Items.GLAZED_TERRACOTTA.purple(), PER_BLOCK);
		this.shard(TesseraMaterial.RED_GLAZED_TERRACOTTA, Items.GLAZED_TERRACOTTA.red(), PER_BLOCK);
		this.shard(TesseraMaterial.WHITE_GLAZED_TERRACOTTA, Items.GLAZED_TERRACOTTA.white(), PER_BLOCK);
		this.shard(TesseraMaterial.YELLOW_GLAZED_TERRACOTTA, Items.GLAZED_TERRACOTTA.yellow(), PER_BLOCK);

		this.shard(TesseraMaterial.BLACK_STAINED_GLASS, Items.STAINED_GLASS.black(), PER_BLOCK);
		this.shard(TesseraMaterial.BLUE_STAINED_GLASS, Items.STAINED_GLASS.blue(), PER_BLOCK);
		this.shard(TesseraMaterial.BROWN_STAINED_GLASS, Items.STAINED_GLASS.brown(), PER_BLOCK);
		this.shard(TesseraMaterial.CYAN_STAINED_GLASS, Items.STAINED_GLASS.cyan(), PER_BLOCK);
		this.shard(TesseraMaterial.GRAY_STAINED_GLASS, Items.STAINED_GLASS.gray(), PER_BLOCK);
		this.shard(TesseraMaterial.GREEN_STAINED_GLASS, Items.STAINED_GLASS.green(), PER_BLOCK);
		this.shard(TesseraMaterial.LIGHT_BLUE_STAINED_GLASS, Items.STAINED_GLASS.lightBlue(), PER_BLOCK);
		this.shard(TesseraMaterial.LIGHT_GRAY_STAINED_GLASS, Items.STAINED_GLASS.lightGray(), PER_BLOCK);
		this.shard(TesseraMaterial.LIME_STAINED_GLASS, Items.STAINED_GLASS.lime(), PER_BLOCK);
		this.shard(TesseraMaterial.MAGENTA_STAINED_GLASS, Items.STAINED_GLASS.magenta(), PER_BLOCK);
		this.shard(TesseraMaterial.ORANGE_STAINED_GLASS, Items.STAINED_GLASS.orange(), PER_BLOCK);
		this.shard(TesseraMaterial.PINK_STAINED_GLASS, Items.STAINED_GLASS.pink(), PER_BLOCK);
		this.shard(TesseraMaterial.PURPLE_STAINED_GLASS, Items.STAINED_GLASS.purple(), PER_BLOCK);
		this.shard(TesseraMaterial.RED_STAINED_GLASS, Items.STAINED_GLASS.red(), PER_BLOCK);
		this.shard(TesseraMaterial.WHITE_STAINED_GLASS, Items.STAINED_GLASS.white(), PER_BLOCK);
		this.shard(TesseraMaterial.YELLOW_STAINED_GLASS, Items.STAINED_GLASS.yellow(), PER_BLOCK);
	}

	protected void shard(TesseraMaterial material, ItemLike source, int count) {
		this.stonecutterResultFromBase(RecipeCategory.DECORATIONS, ModItems.SHARDS.pick(material).get(), source, count);
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
