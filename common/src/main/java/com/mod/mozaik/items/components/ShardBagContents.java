package com.mod.mozaik.items.components;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import com.mod.mozaik.reg.ModDataComponents;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.component.Bees;
import net.minecraft.world.level.block.entity.BeehiveBlockEntity;
import org.apache.commons.lang3.math.Fraction;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

@NullMarked
public class ShardBagContents implements TooltipComponent {
	public static final ShardBagContents EMPTY = new ShardBagContents(List.of());
	public static final Codec<ShardBagContents> CODEC;
	public static final StreamCodec<RegistryFriendlyByteBuf, ShardBagContents> STREAM_CODEC;
	private static final Fraction BUNDLE_IN_BUNDLE_WEIGHT;
	private static final int NO_STACK_INDEX = -1;
	public static final int NO_SELECTED_ITEM_INDEX = -1;
	public static final DataResult<Fraction> BEEHIVE_WEIGHT;
	private final List<ItemStackTemplate> items;
	private final int selectedItem;
	private final Supplier<DataResult<Fraction>> weight;

	private ShardBagContents(List<ItemStackTemplate> items, int selectedItem) {
		this.items = items;
		this.selectedItem = selectedItem;
		this.weight = Suppliers.memoize(() -> computeContentWeight(this.items));
	}

	public ShardBagContents(List<ItemStackTemplate> items) {
		this(items, -1);
	}

	private static DataResult<Fraction> computeContentWeight(List<? extends ItemInstance> items) {
		try {
			Fraction weight = Fraction.ZERO;

			for(ItemInstance stack : items) {
				DataResult<Fraction> itemWeight = getWeight(stack);
				if (itemWeight.isError()) {
					return itemWeight;
				}

				weight = weight.add(itemWeight.getOrThrow().multiplyBy(Fraction.getFraction(stack.count(), 1)));
			}

			return DataResult.success(weight);
		} catch (ArithmeticException var5) {
			return DataResult.error(() -> "Excessive total bundle weight");
		}
	}

	private static DataResult<Fraction> getWeight(ItemInstance item) {
		ShardBagContents bundle = item.get(ModDataComponents.SHARD_BAG_CONTENTS.get());
		if (bundle != null) {
			return bundle.weight().map((nestedWeight) -> nestedWeight.add(BUNDLE_IN_BUNDLE_WEIGHT));
		} else {
			List<BeehiveBlockEntity.Occupant> bees = item.getOrDefault(DataComponents.BEES, Bees.EMPTY).bees();
			return !bees.isEmpty() ? BEEHIVE_WEIGHT : DataResult.success(Fraction.getFraction(1, item.getMaxStackSize()));
		}
	}

	public static boolean canItemBeInBundle(ItemStack itemToAdd) {
		return !itemToAdd.isEmpty() && itemToAdd.getItem().canFitInsideContainerItems();
	}

	public int getNumberOfItemsToShow() {
		int numberOfItemStacks = this.size();
		int availableItemsToShow = numberOfItemStacks > 12 ? 11 : 12;
		int itemsOnNonFullRow = numberOfItemStacks % 4;
		int emptySpaceOnNonFullRow = itemsOnNonFullRow == 0 ? 0 : 4 - itemsOnNonFullRow;
		return Math.min(numberOfItemStacks, availableItemsToShow - emptySpaceOnNonFullRow);
	}

	public Stream<ItemStack> itemCopyStream() {
		return this.items.stream().map(ItemStackTemplate::create);
	}

	public List<ItemStackTemplate> items() {
		return this.items;
	}

	public int size() {
		return this.items.size();
	}

	public DataResult<Fraction> weight() {
		return this.weight.get();
	}

	public boolean isEmpty() {
		return this.items.isEmpty();
	}

	public int getSelectedItemIndex() {
		return this.selectedItem;
	}

	public @Nullable ItemStackTemplate getSelectedItem() {
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

	static {
		CODEC = ItemStackTemplate.CODEC.listOf().xmap(ShardBagContents::new, (contents) -> contents.items);
		STREAM_CODEC = ItemStackTemplate.STREAM_CODEC.apply(ByteBufCodecs.list()).map(ShardBagContents::new, (contents) -> contents.items);
		BUNDLE_IN_BUNDLE_WEIGHT = Fraction.getFraction(1, 16);
		BEEHIVE_WEIGHT = DataResult.success(Fraction.ONE);
	}

	public static class Mutable {
		private final List<ItemStack> items;
		private Fraction weight;
		private int selectedItem;

		public Mutable(ShardBagContents contents) {
			DataResult<Fraction> currentWeight = contents.weight.get();
			if (currentWeight.isError()) {
				this.items = new ArrayList();
				this.weight = Fraction.ZERO;
				this.selectedItem = -1;
			} else {
				this.items = new ArrayList(contents.items.size());

				for(ItemStackTemplate item : contents.items) {
					this.items.add(item.create());
				}

				this.weight = currentWeight.getOrThrow();
				this.selectedItem = contents.selectedItem;
			}

		}

		public ShardBagContents.Mutable clearItems() {
			this.items.clear();
			this.weight = Fraction.ZERO;
			this.selectedItem = -1;
			return this;
		}

		private int findStackIndex(ItemStack itemsToAdd) {
			if (!itemsToAdd.isStackable()) {
				return -1;
			} else {
				for(int i = 0; i < this.items.size(); ++i) {
					if (ItemStack.isSameItemSameComponents(this.items.get(i), itemsToAdd)) {
						return i;
					}
				}

				return -1;
			}
		}

		private int getMaxAmountToAdd(Fraction itemWeight) {
			Fraction remainingWeight = Fraction.ONE.subtract(this.weight);
			return Math.max(remainingWeight.divideBy(itemWeight).intValue(), 0);
		}

		public int tryInsert(ItemStack itemsToAdd) {
			if (!ShardBagContents.canItemBeInBundle(itemsToAdd)) {
				return 0;
			} else {
				DataResult<Fraction> maybeItemWeight = ShardBagContents.getWeight(itemsToAdd);
				if (maybeItemWeight.isError()) {
					return 0;
				} else {
					Fraction itemWeight = maybeItemWeight.getOrThrow();
					int amountToAdd = Math.min(itemsToAdd.getCount(), this.getMaxAmountToAdd(itemWeight));
					if (amountToAdd == 0) {
						return 0;
					} else {
						this.weight = this.weight.add(itemWeight.multiplyBy(Fraction.getFraction(amountToAdd, 1)));
						int stackIndex = this.findStackIndex(itemsToAdd);
						if (stackIndex != -1) {
							ItemStack removedStack = this.items.remove(stackIndex);
							ItemStack mergedStack = removedStack.copyWithCount(removedStack.getCount() + amountToAdd);
							itemsToAdd.shrink(amountToAdd);
							this.items.add(0, mergedStack);
						} else {
							this.items.add(0, itemsToAdd.split(amountToAdd));
						}

						return amountToAdd;
					}
				}
			}
		}

		public int tryTransfer(Slot slot, Player player) {
			ItemStack other = slot.getItem();
			DataResult<Fraction> itemWeight = ShardBagContents.getWeight(other);
			if (itemWeight.isError()) {
				return 0;
			} else {
				int maxAmount = this.getMaxAmountToAdd(itemWeight.getOrThrow());
				return ShardBagContents.canItemBeInBundle(other) ? this.tryInsert(slot.safeTake(other.getCount(), maxAmount, player)) : 0;
			}
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
				this.weight = this.weight.subtract(ShardBagContents.getWeight(stack).getOrThrow().multiplyBy(Fraction.getFraction(stack.getCount(), 1)));
				this.toggleSelectedItem(-1);
				return stack;
			}
		}

		public Fraction weight() {
			return this.weight;
		}

		public ShardBagContents toImmutable() {
			ImmutableList.Builder<ItemStackTemplate> builder = ImmutableList.builder();

			for(ItemStack item : this.items) {
				builder.add(ItemStackTemplate.fromNonEmptyStack(item));
			}

			return new ShardBagContents(builder.build(), this.selectedItem);
		}
	}
}
