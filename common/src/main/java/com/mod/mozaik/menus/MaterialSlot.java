package com.mod.mozaik.menus;

import com.mod.mozaik.client.screens.MortarScreen;
import com.mod.mozaik.items.ShardItem;
import com.mod.mozaik.polyomino.TesseraMaterial;
import com.mod.mozaik.reg.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class MaterialSlot extends Slot {
	private final Player player;
	private int removeCount;

	public MaterialSlot(final Player player, final Container container, final int id, final int x, final int y) {
		super(container, id, x, y);
		this.player = player;
	}

	@Override
	public ItemStack safeTake(int amount, int maxAmount, Player player) {
		if (Minecraft.getInstance().gui.screen() instanceof MortarScreen screen && this.getItem().getItem() instanceof ShardItem shard) {
			screen.addButton.setColor(shard.getMaterial().ordinal());
		}
		return ItemStack.EMPTY;
	}

	public static void scrollUpBy(MortarScreen screen, int by) {
		screen.from = Math.max(0, screen.from - by);
		screen.to = Math.max(9, screen.to - by);

		int s = 0;
		for (int i = screen.from; i < screen.to; i++) {
			ItemStack stack = ModItems.SHARDS.asList().get(i).get().getDefaultInstance();
			MortarScreen.MATERIALS.setItem(s, stack);
			screen.materialSlots.get(s++).set(stack);
		}

		MortarScreen.MATERIALS.setChanged();
	}

	public static void scrollDownBy(MortarScreen screen, int by) {
		screen.from = Math.min(TesseraMaterial.values().length - 9, screen.from + by);
		screen.to = Math.min(TesseraMaterial.values().length, screen.to + by);

		int s = 0;
		for (int i = screen.from; i < screen.to; i++) {
			ItemStack stack = ModItems.SHARDS.asList().get(i).get().getDefaultInstance();
			MortarScreen.MATERIALS.setItem(s, stack);
			screen.materialSlots.get(s++).set(stack);
		}

		MortarScreen.MATERIALS.setChanged();
	}

	@Override
	public void set(ItemStack itemStack) {
		super.set(itemStack);
	}

	@Override
	public void onTake(Player player, ItemStack carried) {

	}

	public boolean mayPlace(final ItemStack itemStack) {
		return false;
	}

	@Override
	public ItemStack remove(final int amount) {
		if (Minecraft.getInstance().gui.screen() instanceof MortarScreen screen && this.getItem().getItem() instanceof ShardItem shard && screen.addButton != null) {
			screen.addButton.setColor(shard.getMaterial().ordinal());
		}
		return ItemStack.EMPTY;
	}

	protected void onQuickCraft(final ItemStack picked, final int count) {
		this.removeCount += count;
		this.checkTakeAchievements(picked);
	}

	protected void onSwapCraft(final int count) {
		this.removeCount += count;
	}

	public ItemStack safeClone(final Player player) {
		ItemStack result = super.safeClone(player);
		result.getItem().onCraftedBy(result, player);
		return result;
	}

	protected void checkTakeAchievements(final ItemStack carried) {
		if (this.removeCount > 0) {
			carried.onCraftedBy(this.player, this.removeCount);
		}


		this.removeCount = 0;
	}

	private static NonNullList<ItemStack> copyAllInputItems(final CraftingInput input) {
		NonNullList<ItemStack> result = NonNullList.withSize(input.size(), ItemStack.EMPTY);

		for(int slot = 0; slot < result.size(); ++slot) {
			result.set(slot, input.getItem(slot));
		}

		return result;
	}

	private NonNullList<ItemStack> getRemainingItems(final CraftingInput input, final Level level) {
		if (level instanceof ServerLevel serverLevel) {
			return (NonNullList)serverLevel.recipeAccess().getRecipeFor(RecipeType.CRAFTING, input, serverLevel).map((recipe) -> ((CraftingRecipe)recipe.value()).getRemainingItems(input)).orElseGet(() -> copyAllInputItems(input));
		} else {
			return CraftingRecipe.defaultCraftingReminder(input);
		}
	}

	public boolean isFake() {
		return true;
	}
}
