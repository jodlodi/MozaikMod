package com.mod.mozaik.items.components;

import com.google.common.collect.ImmutableList;
import com.mod.mozaik.items.ShardBagItem;
import com.mod.mozaik.items.ShardItem;
import com.mod.mozaik.polyomino.ShardMaterial;
import com.mod.mozaik.polyomino.ShardStack;
import com.mod.mozaik.reg.ModDataComponents;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.math.Fraction;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@NullMarked
public class ShardBagContents implements TooltipComponent {
	public static final ShardBagContents EMPTY = new ShardBagContents(List.of());
	public static final Codec<ShardBagContents> CODEC = ShardStack.CODEC.listOf().xmap(ShardBagContents::new, (contents) -> contents.items);
	public static final StreamCodec<RegistryFriendlyByteBuf, ShardBagContents> STREAM_CODEC = ShardStack.STREAM_CODEC.apply(ByteBufCodecs.list()).map(ShardBagContents::new, (contents) -> contents.items);
	private static final int NO_STACK_INDEX = -1;
	public static final int NO_SELECTED_ITEM_INDEX = -1;
	public static final int MAX_VISIBLE_SLOTS = 16;
	private final List<ShardStack> items;
	private final int selectedItem;

	private ShardBagContents(List<ShardStack> items, int selectedItem) {
		this.items = items;
		this.selectedItem = selectedItem;
	}

	public ShardBagContents(List<ShardStack> items) {
		this(items, -1);
	}

	public static boolean canItemBeInBundle(ItemStack itemToAdd) {
		if (!(itemToAdd.getItem() instanceof ShardItem)) return false;
		return !itemToAdd.isEmpty() && itemToAdd.getItem().canFitInsideContainerItems();
	}

	public int getNumberOfItemsToShow() {
		int numberOfItemStacks = this.size();
		int availableItemsToShow = numberOfItemStacks > MAX_VISIBLE_SLOTS ? MAX_VISIBLE_SLOTS - 1 : MAX_VISIBLE_SLOTS;
		int itemsOnNonFullRow = numberOfItemStacks % 4;
		int emptySpaceOnNonFullRow = itemsOnNonFullRow == 0 ? 0 : 4 - itemsOnNonFullRow;
		return Math.min(numberOfItemStacks, availableItemsToShow - emptySpaceOnNonFullRow);
	}

	public Stream<ItemStack> itemCopyStream() {
		return this.items.stream().map(ShardStack::create);
	}

	public List<ShardStack> items() {
		return this.items;
	}

	public int size() {
		return this.items.size();
	}

	public boolean isEmpty() {
		return this.items.isEmpty();
	}

	public int getSelectedItemIndex() {
		return this.selectedItem;
	}

	public @Nullable ShardStack getSelectedItem() {
		return this.selectedItem == -1 ? null : this.items.get(this.selectedItem);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else {
			boolean var10000;
			if (obj instanceof ShardBagContents contents) {
				var10000 = this.items.equals(contents.items());
			} else {
				var10000 = false;
			}

			return var10000;
		}
	}

	@Override
	public int hashCode() {
		return this.items.hashCode();
	}

	@Override
	public String toString() {
		return "ShardBagContents" + this.items;
	}

	public static class Mutable {
		private final List<ItemStack> items;
		private int selectedItem;

		public Mutable(ShardBagContents contents) {
			this.items = new ArrayList<>(contents.items.size());

			for(ShardStack item : contents.items) {
				this.items.add(item.create());
			}

			this.selectedItem = contents.selectedItem;
		}

		public ShardBagContents.Mutable clearItems() {
			this.items.clear();
			this.selectedItem = -1;
			return this;
		}

		private int findStackIndex(ItemStack itemsToAdd) {
			if (itemsToAdd.isStackable()) {
				for (int i = 0; i < this.items.size(); ++i) {
					if (ItemStack.isSameItemSameComponents(this.items.get(i), itemsToAdd)) {
						return i;
					}
				}
			}
			return -1;
		}

		public int tryInsert(ItemStack itemsToAdd) {
			if (!ShardBagContents.canItemBeInBundle(itemsToAdd)) {
				return 0;
			} else {
				int amountToAdd = itemsToAdd.getCount();
				if (amountToAdd == 0) {
					return 0;
				} else {
					int stackIndex = this.findStackIndex(itemsToAdd);
					if (stackIndex != -1) {
						ItemStack removedStack = this.items.remove(stackIndex);
						ItemStack mergedStack = removedStack.copyWithCount(removedStack.getCount() + amountToAdd);
						itemsToAdd.shrink(amountToAdd);
						this.items.addFirst(mergedStack);
					} else {
						this.items.addFirst(itemsToAdd.split(amountToAdd));
					}

					return amountToAdd;
				}
			}
		}

		public int tryTransfer(Slot slot, Player player) {
			ItemStack other = slot.getItem();
			return ShardBagContents.canItemBeInBundle(other) ? this.tryInsert(slot.safeTake(other.getCount(), other.getMaxStackSize(), player)) : 0;
		}

		public void toggleSelectedItem(int selectedItem) {
			this.selectedItem = this.selectedItem != selectedItem && !this.indexIsOutsideAllowedBounds(selectedItem) ? selectedItem : -1;
		}

		private boolean indexIsOutsideAllowedBounds(int selectedItem) {
			return selectedItem < 0 || selectedItem >= this.items.size();
		}

		public @Nullable ItemStack removeOne() {
			if (this.items.isEmpty()) {
				return null;
			} else {
				int removeIndex = this.indexIsOutsideAllowedBounds(this.selectedItem) ? 0 : this.selectedItem;
				ItemStack stack = this.items.remove(removeIndex).copy();
				this.toggleSelectedItem(-1);
				return stack;
			}
		}

		public @Nullable ItemStack remove(ResourceKey<ShardMaterial> material) {
			if (this.items.isEmpty()) return null;

			int removeIndex = -1;
			for (int i = 0; i < this.items.size(); i++) {
				ItemStack stack = this.items.get(i);
				if (stack.getItem() instanceof ShardItem item && item.getMaterial().identifier().equals(material.identifier())) {
					if (stack.getCount() <= 1) {
						removeIndex = i;
						break;
					} else {
						stack.shrink(1);
						return stack.copyWithCount(1);
					}
				}
			}

			if (removeIndex == -1) return null;

			return this.items.remove(removeIndex).copy();
		}

		public ShardBagContents toImmutable() {
			ImmutableList.Builder<ShardStack> builder = ImmutableList.builder();

			for(ItemStack item : this.items) {
				builder.add(ShardStack.fromNonEmptyStack(item));
			}

			return new ShardBagContents(builder.build(), this.selectedItem);
		}
	}
}
