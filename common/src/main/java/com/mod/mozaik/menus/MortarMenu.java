package com.mod.mozaik.menus;

import com.mod.mozaik.polyomino.Mosaic;
import com.mod.mozaik.polyomino.Polyomino;
import com.mod.mozaik.reg.ModMenus;
import com.mod.mozaik.util.FlatDirection;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NullMarked;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NullMarked
public class MortarMenu extends AbstractContainerMenu {
	private final BlockPos pos;
	private final List<Polyomino.PlacedPolyomino> mosaic;
	private final Map<FlatDirection, Mosaic> map = new HashMap<>();

	public MortarMenu(int containerId, Inventory inventory) {
		this(containerId, inventory, new ArrayList<>(), BlockPos.ZERO);
	}

	public MortarMenu(int containerId, Inventory inventory, List<Polyomino.PlacedPolyomino> mosaic, BlockPos pos) {
		super(ModMenus.GLUE.get(), containerId);
		this.mosaic = mosaic;
		this.pos = pos;
	}

	public BlockPos getPos() {
		return this.pos;
	}

	public List<Polyomino.PlacedPolyomino> getMosaic() {
		return this.mosaic;
	}

	public Map<FlatDirection, Mosaic> getMap() {
		return map;
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
