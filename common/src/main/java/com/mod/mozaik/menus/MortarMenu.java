package com.mod.mozaik.menus;

import com.google.common.collect.ImmutableList;
import com.mod.mozaik.Constants;
import com.mod.mozaik.blocks.MortarBlock;
import com.mod.mozaik.blocks.entities.MortarBlockEntity;
import com.mod.mozaik.client.widgets.PolyominoWidget;
import com.mod.mozaik.networking.bidirectional.UpdateGlueBidirectional;
import com.mod.mozaik.platform.Services;
import com.mod.mozaik.polyomino.Polyomino;
import com.mod.mozaik.polyomino.Tessera;
import com.mod.mozaik.reg.ModBlocks;
import com.mod.mozaik.reg.ModMenus;
import com.mod.mozaik.reg.ResourceSupplier;
import com.mod.mozaik.util.FlatDirection;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import org.joml.Vector2i;
import org.joml.Vector3i;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NullMarked
public class MortarMenu extends AbstractContainerMenu {
	private final @Nullable MortarBlockEntity mortar;
	private final Map<FlatDirection, NeighbourMosaic> map = new HashMap<>();
	private final Rotation rotation;

	public MortarMenu(int containerId, Inventory inventory) {
		this(containerId, inventory, null, Rotation.NONE);
	}

	public MortarMenu(int containerId, Inventory inventory, @Nullable MortarBlockEntity mortar, Rotation rotation) {
		super(ModMenus.GLUE.get(), containerId);
		this.mortar = mortar;
		this.rotation = rotation;

		if (this.mortar != null && this.mortar.getLevel() != null) {
			Direction facing = this.mortar.getBlockState().getValue(MortarBlock.FACING);
			BlockPos pos = this.mortar.getBlockPos();

			for (FlatDirection direction : FlatDirection.values()) {
				Vec3i diff = direction.facing(facing);
				BlockPos relative = new BlockPos(pos.getX() + diff.getX(), pos.getY() + diff.getY(), pos.getZ() + diff.getZ());
				MortarBlockEntity blockEntity = this.mortar.getLevel().getBlockEntity(relative) instanceof MortarBlockEntity entity ? entity : null;
				if (blockEntity != null) {
					Identifier identifier = fromBlock(blockEntity.getBlockState().getBlock());
					this.map.put(direction, new NeighbourMosaic(identifier, blockEntity.getPolyomino()));
				}
			}
		}
	}

	public Identifier getTexture() {
		if (this.mortar == null) return TextureManager.INTENTIONAL_MISSING_TEXTURE;
		return fromBlock(this.mortar.getBlockState().getBlock());
	}

	public static Identifier fromBlock(Block block) {
		for (ResourceSupplier<MortarBlock> mortarBlockResourceSupplier : ModBlocks.MORTARS.asList()) {
			if (mortarBlockResourceSupplier.get() == block) {
				return Constants.prefix("textures/block/" + mortarBlockResourceSupplier.id().getPath() + ".png");
			}
		}
		return TextureManager.INTENTIONAL_MISSING_TEXTURE;
	}

	public Iterable<Polyomino.PlacedPolyomino> getRotatedPolyomino() {
		if (this.mortar == null) return ImmutableList.of();

		List<Polyomino.PlacedPolyomino> list = new ArrayList<>();
		this.mortar.getPolyomino().forEach(placedPolyomino -> list.add(rotate(placedPolyomino, this.rotation)));

		return list;
	}

	public void setRotatedPolyomino(List<PolyominoWidget> polyomino) {
		if (this.mortar == null) return;

		Rotation reverseRot = switch (this.rotation) {
			case NONE -> Rotation.NONE;
			case CLOCKWISE_90 -> Rotation.COUNTERCLOCKWISE_90;
			case CLOCKWISE_180 -> Rotation.CLOCKWISE_180;
			case COUNTERCLOCKWISE_90 -> Rotation.CLOCKWISE_90;
		};

		List<Polyomino.PlacedPolyomino> list = new ArrayList<>();
		polyomino.forEach(placedPolyomino -> list.add(rotate(placedPolyomino.getPlacedPolyomino(), reverseRot)));

		Services.NETWORK.sendToServer(new UpdateGlueBidirectional(list, this.mortar.getBlockPos()));
	}

	public static Polyomino.PlacedPolyomino rotate(Polyomino.PlacedPolyomino polyomino, Rotation rotation) {
		if (rotation == Rotation.NONE) return polyomino;

		List<Tessera.PlacedTessera> placedTessera = new ArrayList<>();

		polyomino.polyomino().placedTessera().forEach(voxel -> {
					Vector3i vec = new Vector3i(voxel.x(), 0, voxel.y());
					Vector3i rotated = rotation.rotation().rotate(vec);
					placedTessera.add(new Tessera.PlacedTessera(new Tessera(voxel.tessera().shape().rotate(rotation)), rotated.x(), rotated.z()));
				}
		);

		Vector3i vec = new Vector3i(polyomino.x(), 0, polyomino.y());
		Vector3i rotated = rotation.rotation().rotate(vec);

		Vector2i finalPos = switch (rotation) {
			case COUNTERCLOCKWISE_90 -> new Vector2i(rotated.x(), rotated.z() + 15);
			case CLOCKWISE_180 -> new Vector2i(rotated.x() + 15, rotated.z() + 15);
			case CLOCKWISE_90 -> new Vector2i(rotated.x() + 15, rotated.z());
			default -> new Vector2i(rotated.x(), rotated.z());
		};

		return new Polyomino.PlacedPolyomino(
				new Polyomino(placedTessera, polyomino.polyomino().material(), polyomino.polyomino().seed()),
				finalPos.x(),
				finalPos.y()
		);
	}

	public Map<FlatDirection, NeighbourMosaic> getMap() {
		return this.map;
	}

	@Override
	public ItemStack quickMoveStack(Player player, int i) {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean stillValid(Player player) {
		return this.mortar != null && !this.mortar.isRemoved();
	}

	public record NeighbourMosaic(Identifier texture, List<Polyomino.PlacedPolyomino> placedPolyomino) {

	}
}
