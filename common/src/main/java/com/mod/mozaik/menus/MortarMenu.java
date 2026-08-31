package com.mod.mozaik.menus;

import com.google.common.collect.ImmutableList;
import com.mod.mozaik.Constants;
import com.mod.mozaik.blocks.MortarBlock;
import com.mod.mozaik.blocks.entities.MortarBlockEntity;
import com.mod.mozaik.client.widgets.PolyominoWidget;
import com.mod.mozaik.items.ShardBagItem;
import com.mod.mozaik.items.ShardItem;
import com.mod.mozaik.items.components.ShardBagContents;
import com.mod.mozaik.networking.bidirectional.AddPolyominoBidirectional;
import com.mod.mozaik.networking.bidirectional.RemovePolyominoBidirectional;
import com.mod.mozaik.networking.bidirectional.SignedMozaikBidirectional;
import com.mod.mozaik.networking.bidirectional.UpdateMozaikBidirectional;
import com.mod.mozaik.platform.Services;
import com.mod.mozaik.polyomino.Polyomino;
import com.mod.mozaik.polyomino.ShardMaterial;
import com.mod.mozaik.polyomino.ShardStack;
import com.mod.mozaik.polyomino.Tessera;
import com.mod.mozaik.reg.ModBlocks;
import com.mod.mozaik.reg.ModDataComponents;
import com.mod.mozaik.reg.ModMenus;
import com.mod.mozaik.reg.ResourceSupplier;
import com.mod.mozaik.util.FlatDirection;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
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

import java.util.*;

@NullMarked
public class MortarMenu extends AbstractContainerMenu {
	private final Inventory inventory;
	private final @Nullable MortarBlockEntity mortar;
	private final Map<FlatDirection, NeighbourMosaic> map = new HashMap<>();
	private final ShardSource shardSource;
	private final Rotation rotation;

	public MortarMenu(int containerId, Inventory inventory) {
		this(containerId, inventory, null, Rotation.NONE);
	}

	public MortarMenu(int containerId, Inventory inventory, @Nullable MortarBlockEntity mortar, Rotation rotation) {
		super(ModMenus.GLUE.get(), containerId);
		this.shardSource = new ShardSource(inventory);
		this.inventory = inventory;
		this.mortar = mortar;
		this.rotation = rotation;

		if (this.mortar != null && this.mortar.getLevel() != null) {
			Direction facing = this.mortar.getBlockState().getValue(MortarBlock.FACING_ROTATED).getDirection();
			BlockPos pos = this.mortar.getBlockPos();

			for (FlatDirection direction : FlatDirection.values()) {
				Vec3i diff = direction.facing(facing);

				FlatDirection rotated = switch (rotation) {
					case COUNTERCLOCKWISE_90 -> direction.counterClockWise(2);
					case CLOCKWISE_180 -> direction.clockWise(4);
					case CLOCKWISE_90 -> direction.clockWise(2);
					default -> direction;
				};

				BlockPos relative = new BlockPos(pos.getX() + diff.getX(), pos.getY() + diff.getY(), pos.getZ() + diff.getZ());
				MortarBlockEntity blockEntity = this.mortar.getLevel().getBlockEntity(relative) instanceof MortarBlockEntity entity ? entity : null;
				if (blockEntity != null) {
					List<Polyomino.PlacedPolyomino> copy = new ArrayList<>();

					Rotation blockRotation = blockEntity.getBlockState().getValue(MortarBlock.FACING_ROTATED).getRotation();
					blockEntity.getPolyomino().forEach(placedPolyomino -> copy.add(MortarMenu.rotate(placedPolyomino, Rotation.values()[(rotation.ordinal() + blockRotation.ordinal()) % 4])));

					Identifier identifier = fromBlock(blockEntity.getBlockState().getBlock());
					this.map.put(rotated, new NeighbourMosaic(identifier, copy));
				}
			}
		}
	}

	public void removeFromSource(UUID uuid) {
		if (this.mortar == null) return;
		Services.NETWORK.sendToServer(new RemovePolyominoBidirectional(uuid, this.mortar.getBlockPos(), this.inventory.player.getId()));
	}

	public void sign(@Nullable String title) {
		if (this.mortar == null) return;
		this.mortar.setSigned(true);
		Services.NETWORK.sendToServer(new SignedMozaikBidirectional(Optional.ofNullable(title), Optional.of(this.inventory.player.getPlainTextName()), this.mortar.getBlockPos()));
	}

	public void addToSource(Polyomino.PlacedPolyomino polyomino) {
		if (this.mortar == null) return;
		Rotation blockRotation = this.mortar.getBlockState().getValue(MortarBlock.FACING_ROTATED).getRotation();

		Rotation reverseRot = switch (Rotation.values()[(this.rotation.ordinal() + blockRotation.ordinal()) % 4]) {
			case NONE -> Rotation.NONE;
			case CLOCKWISE_90 -> Rotation.COUNTERCLOCKWISE_90;
			case CLOCKWISE_180 -> Rotation.CLOCKWISE_180;
			case COUNTERCLOCKWISE_90 -> Rotation.CLOCKWISE_90;
		};

		Services.NETWORK.sendToServer(new AddPolyominoBidirectional(rotate(polyomino, reverseRot), this.mortar.getBlockPos(), this.inventory.player.getId()));
	}

	public ShardSource getShardSource() {
		return this.shardSource;
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
		return this.getRotatedPolyomino(this.mortar.getPolyomino(), this.rotation);
	}

	private List<Polyomino.PlacedPolyomino> getRotatedPolyomino(List<Polyomino.PlacedPolyomino> input, Rotation rotation) {
		List<Polyomino.PlacedPolyomino> list = new ArrayList<>();
		if (this.mortar == null) return input;
		Rotation blockRotation = this.mortar.getBlockState().getValue(MortarBlock.FACING_ROTATED).getRotation();
		input.forEach(placedPolyomino -> list.add(rotate(placedPolyomino, Rotation.values()[(rotation.ordinal() + blockRotation.ordinal()) % 4])));
		return list;
	}

	public void setRotatedPolyomino(List<PolyominoWidget> polyomino) {
		if (this.mortar == null) return;
		Rotation blockRotation = this.mortar.getBlockState().getValue(MortarBlock.FACING_ROTATED).getRotation();

		Rotation reverseRot = switch (Rotation.values()[(this.rotation.ordinal() + blockRotation.ordinal()) % 4]) {
			case NONE -> Rotation.NONE;
			case CLOCKWISE_90 -> Rotation.COUNTERCLOCKWISE_90;
			case CLOCKWISE_180 -> Rotation.CLOCKWISE_180;
			case COUNTERCLOCKWISE_90 -> Rotation.CLOCKWISE_90;
		};

		List<Polyomino.PlacedPolyomino> list = new ArrayList<>();
		polyomino.forEach(placedPolyomino -> list.add(rotate(placedPolyomino.getPlacedPolyomino(), reverseRot)));

		Services.NETWORK.sendToServer(new UpdateMozaikBidirectional(list, this.mortar.getBlockPos()));
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
				new Polyomino(placedTessera, polyomino.polyomino().material(), polyomino.polyomino().uuid()),
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

	public static class ShardSource {
		private final Inventory inventory;

		public ShardSource(Inventory inventory) {
			this.inventory = inventory;
		}

		public int getCount(ResourceKey<ShardMaterial> material) {
			int count = 0;
			for (ItemStack stack : this.inventory) {
				if (stack.getItem() instanceof ShardItem item && item.getMaterial().identifier().equals(material.identifier())) {
					count += stack.getCount();
				} else if (stack.getItem() instanceof ShardBagItem) {
					ShardBagContents initialContents = stack.get(ModDataComponents.SHARD_BAG_CONTENTS.get());
					if (initialContents == null) continue;
					for (ShardStack shardStack : initialContents.items()) {
						if (shardStack.material().identifier().equals(material.identifier())) {
							count += shardStack.count();
							break;
						}
					}
				}
			}
			return count;
		}

		public void giveItem(ResourceKey<ShardMaterial> material) {
			for (ItemStack stack : this.inventory) {
				if (stack.getItem() instanceof ShardItem item && item.getMaterial().identifier().equals(material.identifier()) && stack.getCount() < stack.getMaxStackSize()) {
					stack.grow(1);
					return;
				} else if (stack.getItem() instanceof ShardBagItem) {
					ShardBagContents initialContents = stack.get(ModDataComponents.SHARD_BAG_CONTENTS.get());
					if (initialContents == null) continue;
					ShardBagContents.Mutable contents = new ShardBagContents.Mutable(initialContents);
					contents.tryInsert(new ItemStack(ShardItem.SHARDS.get(material)));
					stack.set(ModDataComponents.SHARD_BAG_CONTENTS.get(), contents.toImmutable());
					return;
				}
			}

			this.inventory.add(new ItemStack(ShardItem.SHARDS.get(material)));
		}

		public boolean takeItem(ResourceKey<ShardMaterial> material) {
			int value = Integer.MAX_VALUE;
			ItemStack smallest = ItemStack.EMPTY;
			for (ItemStack stack : this.inventory) {
				if (stack.getItem() instanceof ShardItem item && item.getMaterial().identifier().equals(material.identifier()) && stack.getCount() < value) {
					value = stack.getCount();
					smallest = stack;
				} else if (stack.getItem() instanceof ShardBagItem) {
					ShardBagContents initialContents = stack.get(ModDataComponents.SHARD_BAG_CONTENTS.get());
					if (initialContents == null) continue;

					ShardBagContents.Mutable contents = new ShardBagContents.Mutable(initialContents);
					if (contents.remove(material) != null) {
						stack.set(ModDataComponents.SHARD_BAG_CONTENTS.get(), contents.toImmutable());
						return true;
					}
				}
			}

			if (smallest == ItemStack.EMPTY) return false;
			smallest.shrink(1);
			return true;
		}

		public Player getPlayer() {
			return this.inventory.player;
		}

		public boolean isCreative() {
			return this.inventory.player.isCreative();
		}
	}
}
