package com.mod.mozaik.recipe;

import com.mod.mozaik.items.ShardBagItem;
import com.mod.mozaik.reg.ModItems;
import com.mod.mozaik.reg.ModRecipeSerializers;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ShardBagColoring extends CustomRecipe {
	public ShardBagColoring(CraftingBookCategory category) {
		super(category);
	}

	@Override
	public boolean matches(CraftingInput input, Level level) {
		int i = 0;
		int j = 0;

		for(int k = 0; k < input.size(); ++k) {
			ItemStack itemStack = input.getItem(k);
			if (!itemStack.isEmpty()) {
				if (itemStack.getItem() instanceof ShardBagItem) {
					++i;
				} else {
					if (!(itemStack.getItem() instanceof DyeItem)) {
						return false;
					}

					++j;
				}

				if (j > 1 || i > 1) {
					return false;
				}
			}
		}

		return i == 1 && j == 1;
	}

	@Override
	public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
		ItemStack itemStack = ItemStack.EMPTY;
		DyeItem dyeItem = (DyeItem) Items.WHITE_DYE;

		for(int i = 0; i < input.size(); ++i) {
			ItemStack itemStack2 = input.getItem(i);
			if (!itemStack2.isEmpty()) {
				Item item = itemStack2.getItem();
				if (item instanceof ShardBagItem) {
					itemStack = itemStack2;
				} else if (item instanceof DyeItem dye) {
					dyeItem = dye;
				}
			}
		}

		return itemStack.transmuteCopy(ModItems.DYED_SHARD_BAG.pick(dyeItem.getDyeColor()).get(), 1);
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return width * height >= 2;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ModRecipeSerializers.SHARD_BAG_COLORING.get();
	}
}