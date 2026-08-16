package com.mod.mozaik.menus;

import com.mod.mozaik.blocks.MortarBlock;
import com.mod.mozaik.blocks.entities.MortarBlockEntity;
import com.mod.mozaik.client.screens.MortarScreen;
import com.mod.mozaik.polyomino.Polyomino;
import com.mod.mozaik.reg.ModMenus;
import com.mod.mozaik.util.FlatDirection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NullMarked
public class MortarMenu extends AbstractContainerMenu {
	private final @Nullable MortarBlockEntity mortar;
	private final Map<FlatDirection, NeighbourMosaic> map = new HashMap<>();

	public MortarMenu(int containerId, Inventory inventory) {
		this(containerId, inventory, null);
	}

	public MortarMenu(int containerId, Inventory inventory, @Nullable MortarBlockEntity mortar) {
		super(ModMenus.GLUE.get(), containerId);
		this.mortar = mortar;

		if (this.mortar != null && this.mortar.getLevel() != null) {
			Direction facing = this.mortar.getBlockState().getValue(MortarBlock.FACING);
			BlockPos pos = this.mortar.getBlockPos();

			for (FlatDirection direction : FlatDirection.values()) {
				Vec3i diff = direction.facing(facing);
				BlockPos relative = new BlockPos(pos.getX() + diff.getX(), pos.getY() + diff.getY(), pos.getZ() + diff.getZ());
				MortarBlockEntity blockEntity = this.mortar.getLevel().getBlockEntity(relative) instanceof MortarBlockEntity entity ? entity : null;
				if (blockEntity != null) {
					Identifier identifier = MortarScreen.fromBlock(blockEntity.getBlockState().getBlock());
					this.map.put(direction, new NeighbourMosaic(identifier, blockEntity.getPolyominos()));
				}
			}
		}
	}

	public @Nullable MortarBlockEntity getMortar() {
		return this.mortar;
	}

	public Map<FlatDirection, NeighbourMosaic> getMap() {
		return this.map;
	}

	@Override
	public ItemStack quickMoveStack(Player player, int i) {
		return null;
	}

	@Override
	public boolean stillValid(Player player) {
		return this.mortar != null && !this.mortar.isRemoved();
	}

	public record NeighbourMosaic(Identifier texture, List<Polyomino.PlacedPolyomino> placedPolyomino) {

	}
}
