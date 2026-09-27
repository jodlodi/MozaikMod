package com.mod.mozaik.reg;

import com.mod.mozaik.platform.Services;
import com.mod.mozaik.recipe.MortarColoring;
import com.mod.mozaik.recipe.ShardBagColoring;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;

public class ModRecipeSerializers {
	public static final ResourceSupplier<RecipeSerializer<ShardBagColoring>> SHARD_BAG_COLORING = Services.REGISTRY.registerRecipeSerializer("shard_bag_coloring", () -> new SimpleCraftingRecipeSerializer<>(ShardBagColoring::new));
	public static final ResourceSupplier<RecipeSerializer<MortarColoring>> MORTAR_COLORING = Services.REGISTRY.registerRecipeSerializer("mortar_coloring", () -> new SimpleCraftingRecipeSerializer<>(MortarColoring::new));

	public static void init() {

	}

}
