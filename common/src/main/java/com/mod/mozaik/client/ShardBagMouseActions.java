package com.mod.mozaik.client;

import com.mod.mozaik.items.ShardBagItem;
import com.mod.mozaik.networking.serverbound.SelectShardBagItemPacket;
import com.mod.mozaik.platform.Services;
import com.mod.mozaik.reg.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.ScrollWheelHandler;
import net.minecraft.client.gui.ItemSlotMouseAction;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.inventory.ContainerInput;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Vector2i;
import net.minecraft.MethodsReturnNonnullByDefault;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ShardBagMouseActions implements ItemSlotMouseAction {
	private final Minecraft minecraft;
	private final ScrollWheelHandler scrollWheelHandler;

	public ShardBagMouseActions(Minecraft minecraft) {
		this.minecraft = minecraft;
		this.scrollWheelHandler = new ScrollWheelHandler();
	}

	@Override
	public boolean matches(Slot slot) {
		return slot.getItem().is(ModItems.SHARD_BAG.get());
	}

	@Override
	public boolean onMouseScrolled(double scrollX, double scrollY, int slotIndex, ItemStack itemStack) {
		int amountOfShownItems = ShardBagItem.getNumberOfItemsToShow(itemStack);
		if (amountOfShownItems == 0) return false;

		Vector2i wheelXY = this.scrollWheelHandler.onMouseScroll(scrollX, scrollY);
		int wheel = wheelXY.y == 0 ? -wheelXY.x : wheelXY.y;
		if (wheel != 0) {
			int selectedItem = ShardBagItem.getSelectedItemIndex(itemStack);
			int updatedSelectedItem = ScrollWheelHandler.getNextScrollWheelSelection(wheel, selectedItem, amountOfShownItems);
			if (selectedItem != updatedSelectedItem) {
				this.toggleSelectedShardBagItem(itemStack, slotIndex, updatedSelectedItem);
			}
		}

		return true;
	}

	@Override
	public void onStopHovering(Slot hoveredSlot) {
		this.unselectedShardBagItem(hoveredSlot.getItem(), hoveredSlot.index);
	}

	@Override
	public void onSlotClicked(Slot slot, ContainerInput containerInput) {
		if (containerInput == ContainerInput.QUICK_MOVE || containerInput == ContainerInput.SWAP) {
			this.unselectedShardBagItem(slot.getItem(), slot.index);
		}
	}

	private void toggleSelectedShardBagItem(ItemStack shardBag, int slotIndex, int selectedItem) {
		if (this.minecraft.getConnection() != null && selectedItem < ShardBagItem.getNumberOfItemsToShow(shardBag)) {
			ShardBagItem.toggleSelectedItem(shardBag, selectedItem);
			Services.NETWORK.sendToServer(new SelectShardBagItemPacket(slotIndex, selectedItem));
		}
	}

	public void unselectedShardBagItem(ItemStack ShardBagItem, int slotIndex) {
		this.toggleSelectedShardBagItem(ShardBagItem, slotIndex, -1);
	}
}
