package com.mod.mozaik.items;

import com.mod.mozaik.items.components.ShardBagContents;
import com.mod.mozaik.reg.ModDataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.MethodsReturnNonnullByDefault;

import javax.annotation.ParametersAreNonnullByDefault;

import java.util.Optional;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ShardBagItem extends Item {
	public ShardBagItem(Item.Properties properties) {
		super(properties);
	}

	@Override
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

	@Override
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

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		player.startUsingItem(hand);
		return InteractionResultHolder.consume(player.getItemInHand(hand));
	}

	private void dropContent(Level level, Player player, ItemStack itemStack) {
		if (this.dropContent(itemStack, player)) {
			playDropContentsSound(level, player);
			player.awardStat(Stats.ITEM_USED.get(this));
		}

	}

	public static void toggleSelectedItem(ItemStack stack, int selectedItem) {
		ShardBagContents initialContents = stack.get(ModDataComponents.SHARD_BAG_CONTENTS.get());
		if (initialContents != null) {
			ShardBagContents.Mutable contents = new ShardBagContents.Mutable(initialContents);
			contents.toggleSelectedItem(selectedItem);
			stack.set(ModDataComponents.SHARD_BAG_CONTENTS.get(), contents.toImmutable());
		}
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

	@Override
	public void onUseTick(Level level, LivingEntity livingEntity, ItemStack itemStack, int ticksRemaining) {
		if (livingEntity instanceof Player player) {
			int useDuration = this.getUseDuration(itemStack, livingEntity);
			boolean isFirstTick = ticksRemaining == useDuration;
			if (isFirstTick || ticksRemaining < useDuration - 10 && ticksRemaining % 2 == 0) {
				this.dropContent(level, player, itemStack);
			}
		}
	}

	@Override
	public int getUseDuration(ItemStack itemStack, LivingEntity entity) {
		return 200;
	}

	@Override
	public UseAnim getUseAnimation(ItemStack itemStack) {
		return UseAnim.BLOCK;
	}

	@Override
	public Optional<TooltipComponent> getTooltipImage(ItemStack bundle) {
		return Optional.ofNullable(bundle.get(ModDataComponents.SHARD_BAG_CONTENTS.get())).map(ShardBagTooltip::new);
	}

	@Override
	public void onDestroyed(ItemEntity entity) {
		ShardBagContents contents = entity.getItem().get(ModDataComponents.SHARD_BAG_CONTENTS.get());
		if (contents != null) {
			entity.getItem().set(ModDataComponents.SHARD_BAG_CONTENTS.get(), ShardBagContents.EMPTY);
			ItemUtils.onContainerDestroyed(entity, contents.itemCopyStream().toList());
		}
	}

	private static void playRemoveOneSound(Entity entity) {
		entity.playSound(SoundEvents.BUNDLE_REMOVE_ONE, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
	}

	private static void playInsertSound(Entity entity) {
		entity.playSound(SoundEvents.BUNDLE_INSERT, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
	}

	private static void playDropContentsSound(Level level, Entity entity) {
		level.playSound(null, entity.blockPosition(), SoundEvents.BUNDLE_DROP_CONTENTS, SoundSource.PLAYERS, 0.8F, 0.8F + entity.level().getRandom().nextFloat() * 0.4F);
	}

	private void broadcastChangesOnContainerMenu(Player player) {
		AbstractContainerMenu containerMenu = player.containerMenu;
		containerMenu.slotsChanged(player.getInventory());
	}

	public record ShardBagTooltip(ShardBagContents contents) implements TooltipComponent {

	}
}
