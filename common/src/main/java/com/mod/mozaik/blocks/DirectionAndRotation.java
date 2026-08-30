package com.mod.mozaik.blocks;

import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import org.jspecify.annotations.NullMarked;

@NullMarked
public enum DirectionAndRotation implements StringRepresentable {
	DOWN_0(Direction.DOWN, Rotation.NONE),
	DOWN_90(Direction.DOWN, Rotation.CLOCKWISE_90),
	DOWN_180(Direction.DOWN, Rotation.CLOCKWISE_180),
	DOWN_270(Direction.DOWN, Rotation.COUNTERCLOCKWISE_90),

	UP_0(Direction.UP, Rotation.NONE),
	UP_90(Direction.UP, Rotation.CLOCKWISE_90),
	UP_180(Direction.UP, Rotation.CLOCKWISE_180),
	UP_270(Direction.UP, Rotation.COUNTERCLOCKWISE_90),

	NORTH(Direction.NORTH),
	EAST(Direction.EAST),
	SOUTH(Direction.SOUTH),
	WEST(Direction.WEST);

	private final Direction direction;
	private final Rotation rotation;

	DirectionAndRotation(Direction direction) {
		this(direction, Rotation.NONE);
	}

	DirectionAndRotation(Direction direction, Rotation rotation) {
		this.direction = direction;
		this.rotation = rotation;
	}

	public Direction getDirection() {
		return this.direction;
	}

	public Rotation getRotation() {
		return this.rotation;
	}

	public DirectionAndRotation rotate(Rotation rotation) {
		if (rotation == Rotation.NONE) return this;
		int ordinal = this.ordinal();
		int subSect = ordinal / 4;
		int subIndex = ordinal % 4;

		int addition = switch (rotation) {
			case CLOCKWISE_90 -> 1;
			case CLOCKWISE_180 -> 2;
			default -> 3;
		};

		return DirectionAndRotation.values()[(subIndex + addition) % 4 + (subSect * 4)];
	}

	public DirectionAndRotation mirror(Mirror mirror) {
		if (mirror == Mirror.NONE) return this;
		if (mirror == Mirror.FRONT_BACK && this.getDirection().getAxis() == Direction.Axis.X) {
			return this.rotate(Rotation.CLOCKWISE_180);
		} else if (mirror == Mirror.LEFT_RIGHT && this.getDirection().getAxis() == Direction.Axis.Z) {
			return this.rotate(Rotation.CLOCKWISE_180);
		} else if (mirror == Mirror.FRONT_BACK && (this.getRotation() == Rotation.NONE || this.getRotation() == Rotation.CLOCKWISE_180)) {
			return this.rotate(Rotation.CLOCKWISE_180);
		} else if (mirror == Mirror.LEFT_RIGHT && (this.getRotation() == Rotation.CLOCKWISE_90 || this.getRotation() == Rotation.COUNTERCLOCKWISE_90)) {
			return this.rotate(Rotation.CLOCKWISE_180);
		} else return this;
	}

	@Override
	public String getSerializedName() {
		return switch (this.getRotation()) {
			case NONE -> this.getDirection().getSerializedName();
			case CLOCKWISE_90 -> this.getDirection().getSerializedName() + "_90";
			case CLOCKWISE_180 -> this.getDirection().getSerializedName() + "_180";
			case COUNTERCLOCKWISE_90 -> this.getDirection().getSerializedName() + "_270";
		};
	}
}
