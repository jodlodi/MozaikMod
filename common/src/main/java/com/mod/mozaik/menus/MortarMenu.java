package com.mod.mozaik.menus;

import com.mod.mozaik.reg.ModMenus;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class MortarMenu extends AbstractContainerMenu {
	public final ContainerLevelAccess access;
	private final ContainerData data;

	public MortarMenu(int containerId, Inventory inventory) {
		this(containerId, inventory, ContainerLevelAccess.NULL, new SimpleContainerData(3));
	}

	public MortarMenu(int containerId, Inventory inventory, final ContainerLevelAccess access, ContainerData data) {
		super(ModMenus.GLUE.get(), containerId);
		this.access = access;
		this.data = data;
		this.addDataSlots(this.data);
	}

	public BlockPos getPos() {
		return new BlockPos(this.data.get(0), this.data.get(1), this.data.get(2));
	}

	@Override
	public ItemStack quickMoveStack(Player player, int i) {
		return null;
	}

	@Override
	public boolean stillValid(Player player) {
		return true;
	}
}
