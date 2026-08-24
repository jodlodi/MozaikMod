package com.mod.mozaik.items;

import com.mod.mozaik.items.components.ShardBagContents;
import com.mod.mozaik.reg.ModDataComponents;
import com.mojang.serialization.DataResult;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.BundleTooltip;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.BundleContents;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.math.Fraction;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

@NullMarked
public class ShardBagItem extends Item {
	public static final int MAX_SHOWN_GRID_ITEMS_X = 4;
	public static final int MAX_SHOWN_GRID_ITEMS_Y = 3;
	public static final int MAX_SHOWN_GRID_ITEMS = 12;
	public static final int OVERFLOWING_MAX_SHOWN_GRID_ITEMS = 11;
	private static final int FULL_BAR_COLOR = ARGB.colorFromFloat(1.0F, 1.0F, 0.33F, 0.33F);
	private static final int BAR_COLOR = ARGB.colorFromFloat(1.0F, 0.44F, 0.53F, 1.0F);
	private static final int TICKS_AFTER_FIRST_THROW = 10;
	private static final int TICKS_BETWEEN_THROWS = 2;
	private static final int TICKS_MAX_THROW_DURATION = 200;

	public ShardBagItem(Item.Properties properties) {
		super(properties);
	}

	private static Fraction getWeightSafe(ShardBagContents contents) {
		DataResult<Fraction> var10000 = contents.weight();
		Fraction var5;
		switch (var10000) {
			case DataResult.Success<Fraction> success -> var5 = success.value();
			case DataResult.Error<?> error -> var5 = Fraction.ONE;
		}

		return var5;
	}

	public static float getFullnessDisplay(ItemStack itemStack) {
		ShardBagContents contents = itemStack.getOrDefault(ModDataComponents.SHARD_BAG_CONTENTS.get(), ShardBagContents.EMPTY);
		return getWeightSafe(contents).floatValue();
	}

	public boolean overrideStackedOnOther(ItemStack self, Slot slot, ClickAction clickAction, Player player) {
		ShardBagContents initialContents = self.get(ModDataComponents.SHARD_BAG_CONTENTS.get());
		if (initialContents == null) {
			return false;
		} else {
			ItemStack other = slot.getItem();
			ShardBagContents.Mutable contents = new ShardBagContents.Mutable(initialContents);
			if (clickAction == ClickAction.PRIMARY && !other.isEmpty()) {
				if (contents.tryTransfer(slot, player) > 0) {
					playInsertSound(player);
				} else {
					playInsertFailSound(player);
				}

				self.set(ModDataComponents.SHARD_BAG_CONTENTS.get(), contents.toImmutable());
				this.broadcastChangesOnContainerMenu(player);
				return true;
			} else if (clickAction == ClickAction.SECONDARY && other.isEmpty()) {
				ItemStack itemStack = contents.removeOne();
				if (itemStack != null) {
					ItemStack remainder = slot.safeInsert(itemStack);
					if (remainder.getCount() > 0) {
						contents.tryInsert(remainder);
					} else {
						playRemoveOneSound(player);
					}
				}

				self.set(ModDataComponents.SHARD_BAG_CONTENTS.get(), contents.toImmutable());
				this.broadcastChangesOnContainerMenu(player);
				return true;
			} else {
				return false;
			}
		}
	}

	public boolean overrideOtherStackedOnMe(ItemStack self, ItemStack other, Slot slot, ClickAction clickAction, Player player, SlotAccess carriedItem) {
		if (clickAction == ClickAction.PRIMARY && other.isEmpty()) {
			toggleSelectedItem(self, -1);
			return false;
		} else {
			ShardBagContents initialContents = self.get(ModDataComponents.SHARD_BAG_CONTENTS.get());
			if (initialContents == null) {
				return false;
			} else {
				ShardBagContents.Mutable contents = new ShardBagContents.Mutable(initialContents);
				if (clickAction == ClickAction.PRIMARY && !other.isEmpty()) {
					if (slot.allowModification(player) && contents.tryInsert(other) > 0) {
						playInsertSound(player);
					} else {
						playInsertFailSound(player);
					}

					self.set(ModDataComponents.SHARD_BAG_CONTENTS.get(), contents.toImmutable());
					this.broadcastChangesOnContainerMenu(player);
					return true;
				} else if (clickAction == ClickAction.SECONDARY && other.isEmpty()) {
					if (slot.allowModification(player)) {
						ItemStack removed = contents.removeOne();
						if (removed != null) {
							playRemoveOneSound(player);
							carriedItem.set(removed);
						}
					}

					self.set(ModDataComponents.SHARD_BAG_CONTENTS.get(), contents.toImmutable());
					this.broadcastChangesOnContainerMenu(player);
					return true;
				} else {
					toggleSelectedItem(self, -1);
					return false;
				}
			}
		}
	}

	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		player.startUsingItem(hand);
		return InteractionResult.SUCCESS;
	}

	private void dropContent(Level level, Player player, ItemStack itemStack) {
		if (this.dropContent(itemStack, player)) {
			playDropContentsSound(level, player);
			player.awardStat(Stats.ITEM_USED.get(this));
		}

	}

	public boolean isBarVisible(ItemStack stack) {
		ShardBagContents contents = stack.getOrDefault(ModDataComponents.SHARD_BAG_CONTENTS.get(), ShardBagContents.EMPTY);
		return getWeightSafe(contents).compareTo(Fraction.ZERO) > 0;
	}

	public int getBarWidth(ItemStack stack) {
		ShardBagContents contents = stack.getOrDefault(ModDataComponents.SHARD_BAG_CONTENTS.get(), ShardBagContents.EMPTY);
		return Math.min(1 + Mth.mulAndTruncate(getWeightSafe(contents), 12), 13);
	}

	public int getBarColor(ItemStack stack) {
		ShardBagContents contents = stack.getOrDefault(ModDataComponents.SHARD_BAG_CONTENTS.get(), ShardBagContents.EMPTY);
		return getWeightSafe(contents).compareTo(Fraction.ONE) >= 0 ? FULL_BAR_COLOR : BAR_COLOR;
	}

	public static void toggleSelectedItem(ItemStack stack, int selectedItem) {
		ShardBagContents initialContents = stack.get(ModDataComponents.SHARD_BAG_CONTENTS.get());
		if (initialContents != null) {
			ShardBagContents.Mutable contents = new ShardBagContents.Mutable(initialContents);
			contents.toggleSelectedItem(selectedItem);
			stack.set(ModDataComponents.SHARD_BAG_CONTENTS.get(), contents.toImmutable());
		}

	}

	public static int getSelectedItemIndex(ItemStack stack) {
		return stack.getOrDefault(ModDataComponents.SHARD_BAG_CONTENTS.get(), ShardBagContents.EMPTY).getSelectedItemIndex();
	}

	public static @Nullable ItemStackTemplate getSelectedItem(ItemStack stack) {
		return stack.getOrDefault(ModDataComponents.SHARD_BAG_CONTENTS.get(), ShardBagContents.EMPTY).getSelectedItem();
	}

	public static int getNumberOfItemsToShow(ItemStack stack) {
		ShardBagContents contents = stack.getOrDefault(ModDataComponents.SHARD_BAG_CONTENTS.get(), ShardBagContents.EMPTY);
		return contents.getNumberOfItemsToShow();
	}

	private boolean dropContent(ItemStack bundle, Player player) {
		ShardBagContents contents = bundle.get(ModDataComponents.SHARD_BAG_CONTENTS.get());
		if (contents != null && !contents.isEmpty()) {
			Optional<ItemStack> itemStack = removeOneItemFromBundle(bundle, player, contents);
			if (itemStack.isPresent()) {
				player.drop(itemStack.get(), true);
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	private static Optional<ItemStack> removeOneItemFromBundle(ItemStack self, Player player, ShardBagContents initialContents) {
		ShardBagContents.Mutable contents = new ShardBagContents.Mutable(initialContents);
		ItemStack removed = contents.removeOne();
		if (removed != null) {
			playRemoveOneSound(player);
			self.set(ModDataComponents.SHARD_BAG_CONTENTS.get(), contents.toImmutable());
			return Optional.of(removed);
		} else {
			return Optional.empty();
		}
	}

	public void onUseTick(Level level, LivingEntity livingEntity, ItemStack itemStack, int ticksRemaining) {
		if (livingEntity instanceof Player player) {
			int useDuration = this.getUseDuration(itemStack, livingEntity);
			boolean isFirstTick = ticksRemaining == useDuration;
			if (isFirstTick || ticksRemaining < useDuration - 10 && ticksRemaining % 2 == 0) {
				this.dropContent(level, player, itemStack);
			}
		}

	}

	public int getUseDuration(ItemStack itemStack, LivingEntity entity) {
		return 200;
	}

	public ItemUseAnimation getUseAnimation(ItemStack itemStack) {
		return ItemUseAnimation.BUNDLE;
	}

	public Optional<TooltipComponent> getTooltipImage(ItemStack bundle) {
		TooltipDisplay display = bundle.getOrDefault(DataComponents.TOOLTIP_DISPLAY, TooltipDisplay.DEFAULT);
		return !display.shows(ModDataComponents.SHARD_BAG_CONTENTS.get()) ? Optional.empty() : Optional.ofNullable(bundle.get(ModDataComponents.SHARD_BAG_CONTENTS.get())).map(ShardBagTooltip::new);
	}

	public void onDestroyed(ItemEntity entity) {
		ShardBagContents contents = entity.getItem().get(ModDataComponents.SHARD_BAG_CONTENTS.get());
		if (contents != null) {
			entity.getItem().set(ModDataComponents.SHARD_BAG_CONTENTS.get(), ShardBagContents.EMPTY);
			ItemUtils.onContainerDestroyed(entity, contents.itemCopyStream());
		}

	}

	private static void playRemoveOneSound(Entity entity) {
		entity.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
	}

	private static void playInsertSound(Entity entity) {
		entity.playSound(SoundEvents.BUNDLE_INSERT, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
	}

	private static void playInsertFailSound(Entity entity) {
		entity.playSound(SoundEvents.BUNDLE_INSERT_FAIL, 1.0F, 1.0F);
	}

	private static void playDropContentsSound(Level level, Entity entity) {
		level.playSound(null, entity.blockPosition(), SoundEvents.BUNDLE_DROP_CONTENTS, SoundSource.PLAYERS, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
	}

	private void broadcastChangesOnContainerMenu(Player player) {
		AbstractContainerMenu containerMenu = player.containerMenu;
		if (containerMenu != null) {
			containerMenu.slotsChanged(player.getInventory());
		}

	}

	public record ShardBagTooltip(ShardBagContents contents) implements TooltipComponent {
	}

}
