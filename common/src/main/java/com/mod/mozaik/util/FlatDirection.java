package com.mod.mozaik.util;

import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Mirror;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;

import java.util.List;
import java.util.Locale;

@NullMarked
public enum FlatDirection implements StringRepresentable {
	UP(true, 0, -1, 0.0F),
	UP_RIGHT(false, 1, -1, 45.0F),
	RIGHT(true, 1, 0, 90.0F),
	DOWN_RIGHT(false, 1, 1, 135.0F),
	DOWN(true, 0, 1, 180.0F),
	DOWN_LEFT(false, -1, 1, 225.0F),
	LEFT(true, -1, 0, 270.0F),
	UP_LEFT(false, -1, -1, 315.0F);

	public static final EnumCodec<FlatDirection> CODEC = StringRepresentable.fromEnum(FlatDirection::values);

	private final boolean isCardinal;
	private final int relativeX;
	private final int relativeY;
	private final float angle;

	FlatDirection(boolean isCardinal, int relativeX, int relativeY, float angle) {
		this.isCardinal = isCardinal;
		this.relativeX = relativeX;
		this.relativeY = relativeY;
		this.angle = angle;
	}

	public static @Unmodifiable List<FlatDirection> cardinalClockwise() {
		return List.of(UP, RIGHT, DOWN, LEFT);
	}

	public static @Unmodifiable List<FlatDirection> subClockwise() {
		return List.of(UP_RIGHT, DOWN_RIGHT, DOWN_LEFT, UP_LEFT);
	}

	public @Unmodifiable List<FlatDirection> getRelated() {
		return List.of(this.counterClockWise(1), this.clockWise(1));
	}

	public int getRelativeX() {
		return this.relativeX;
	}

	public int getRelativeY() {
		return this.relativeY;
	}

	public boolean isCardinal() {
		return this.isCardinal;
	}

	public float getAngle() {
		return this.angle;
	}

	public FlatDirection clockWise(int steps) {
		return values()[(this.ordinal() + steps) % values().length];
	}

	public FlatDirection counterClockWise(int steps) {
		return values()[(this.ordinal() + values().length - steps) % values().length];
	}

	public FlatDirection mirror(Mirror mirror) {
		return switch (mirror) {
			case NONE -> this;
			case LEFT_RIGHT -> switch (this) {
				case UP -> UP;
				case UP_RIGHT -> UP_LEFT;
				case RIGHT -> LEFT;
				case DOWN_RIGHT -> DOWN_LEFT;
				case DOWN -> DOWN;
				case DOWN_LEFT -> DOWN_RIGHT;
				case LEFT -> RIGHT;
				case UP_LEFT -> UP_RIGHT;
			};
			case FRONT_BACK -> switch (this) {
				case UP -> DOWN;
				case UP_RIGHT -> DOWN_RIGHT;
				case RIGHT -> RIGHT;
				case DOWN_RIGHT -> UP_RIGHT;
				case DOWN -> UP;
				case DOWN_LEFT -> UP_LEFT;
				case LEFT -> LEFT;
				case UP_LEFT -> DOWN_LEFT;
			};
		};
	}

	public Vec3i facing(Direction direction) {
		return switch (direction) {
			case UP -> new Vec3i(this.getRelativeX(), 0, this.getRelativeY());
			case DOWN -> new Vec3i(this.getRelativeX(), 0, -this.getRelativeY());
			case NORTH -> new Vec3i(-this.getRelativeX(), -this.getRelativeY(), 0);
			case EAST -> new Vec3i(0, -this.getRelativeY(), -this.getRelativeX());
			case SOUTH -> new Vec3i(this.getRelativeX(), -this.getRelativeY(), 0);
			case WEST -> new Vec3i(0, -this.getRelativeY(), this.getRelativeX());
		};
	}

	@Override
	public String getSerializedName() {
		return this.name().toLowerCase(Locale.ROOT);
	}
}
