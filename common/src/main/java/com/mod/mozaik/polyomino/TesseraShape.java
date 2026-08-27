package com.mod.mozaik.polyomino;

import com.mod.mozaik.util.FlatDirection;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import org.jspecify.annotations.NullMarked;

import java.util.List;
import java.util.Locale;

@NullMarked
public enum TesseraShape implements StringRepresentable {
	U(0, 2, new RotatedModel(ModelReference.END, Rotation.NONE), FlatDirection.UP),
	R(1, 3, new RotatedModel(ModelReference.END, Rotation.CLOCKWISE_90), FlatDirection.RIGHT),
	D(0, 0, new RotatedModel(ModelReference.END, Rotation.CLOCKWISE_180), FlatDirection.DOWN),
	L(3, 3, new RotatedModel(ModelReference.END, Rotation.COUNTERCLOCKWISE_90), FlatDirection.LEFT),

	U_D(0, 1, new RotatedModel(ModelReference.LONG, Rotation.NONE), FlatDirection.UP, FlatDirection.DOWN),
	L_R(2, 3, new RotatedModel(ModelReference.LONG, Rotation.CLOCKWISE_90), FlatDirection.LEFT, FlatDirection.RIGHT),

	R_D(1, 0, new RotatedModel(ModelReference.CORNER, Rotation.CLOCKWISE_90), FlatDirection.RIGHT, FlatDirection.DOWN),
	L_D(3, 0, new RotatedModel(ModelReference.CORNER, Rotation.CLOCKWISE_180), FlatDirection.LEFT, FlatDirection.DOWN),
	R_U(1, 2, new RotatedModel(ModelReference.CORNER, Rotation.NONE), FlatDirection.RIGHT, FlatDirection.UP),
	L_U(3, 2, new RotatedModel(ModelReference.CORNER, Rotation.COUNTERCLOCKWISE_90), FlatDirection.LEFT, FlatDirection.UP),

	R_D_DR(8, 0, new RotatedModel(ModelReference.FULL_CORNER, Rotation.CLOCKWISE_90), FlatDirection.RIGHT, FlatDirection.DOWN, FlatDirection.DOWN_RIGHT),
	L_D_DL(11, 0, new RotatedModel(ModelReference.FULL_CORNER, Rotation.CLOCKWISE_180), FlatDirection.LEFT, FlatDirection.DOWN, FlatDirection.DOWN_LEFT),
	R_U_UR(8, 3, new RotatedModel(ModelReference.FULL_CORNER, Rotation.NONE), FlatDirection.RIGHT, FlatDirection.UP, FlatDirection.UP_RIGHT),
	L_U_UL(11, 3, new RotatedModel(ModelReference.FULL_CORNER, Rotation.COUNTERCLOCKWISE_90), FlatDirection.LEFT, FlatDirection.UP, FlatDirection.UP_LEFT),

	L_R_D(2, 0, new RotatedModel(ModelReference.T, Rotation.CLOCKWISE_90), FlatDirection.LEFT, FlatDirection.RIGHT, FlatDirection.DOWN),
	L_R_U(2, 2, new RotatedModel(ModelReference.T, Rotation.COUNTERCLOCKWISE_90), FlatDirection.LEFT, FlatDirection.RIGHT, FlatDirection.UP),
	U_D_R(1, 1, new RotatedModel(ModelReference.T, Rotation.NONE), FlatDirection.UP, FlatDirection.DOWN, FlatDirection.RIGHT),
	U_D_L(3, 1, new RotatedModel(ModelReference.T, Rotation.CLOCKWISE_180), FlatDirection.UP, FlatDirection.DOWN, FlatDirection.LEFT),

	L_R_D_DR(5, 0, new RotatedModel(ModelReference.T_HALF_FULL_R, Rotation.NONE), FlatDirection.LEFT, FlatDirection.RIGHT, FlatDirection.DOWN, FlatDirection.DOWN_RIGHT),
	L_R_D_DL(6, 0, new RotatedModel(ModelReference.T_HALF_FULL_L, Rotation.NONE), FlatDirection.LEFT, FlatDirection.RIGHT, FlatDirection.DOWN, FlatDirection.DOWN_LEFT),
	L_R_U_UR(5, 3, new RotatedModel(ModelReference.T_HALF_FULL_L, Rotation.CLOCKWISE_180), FlatDirection.LEFT, FlatDirection.RIGHT, FlatDirection.UP, FlatDirection.UP_RIGHT),
	L_R_U_UL(6, 3, new RotatedModel(ModelReference.T_HALF_FULL_R, Rotation.CLOCKWISE_180), FlatDirection.LEFT, FlatDirection.RIGHT, FlatDirection.UP, FlatDirection.UP_LEFT),
	U_D_R_DR(4, 1, new RotatedModel(ModelReference.T_HALF_FULL_L, Rotation.COUNTERCLOCKWISE_90), FlatDirection.UP, FlatDirection.DOWN, FlatDirection.RIGHT, FlatDirection.DOWN_RIGHT),
	U_D_R_UR(4, 2, new RotatedModel(ModelReference.T_HALF_FULL_R, Rotation.COUNTERCLOCKWISE_90), FlatDirection.UP, FlatDirection.DOWN, FlatDirection.RIGHT, FlatDirection.UP_RIGHT),
	U_D_L_DL(7, 1, new RotatedModel(ModelReference.T_HALF_FULL_R, Rotation.CLOCKWISE_90), FlatDirection.UP, FlatDirection.DOWN, FlatDirection.LEFT, FlatDirection.DOWN_LEFT),
	U_D_L_UL(7, 2, new RotatedModel(ModelReference.T_HALF_FULL_L, Rotation.CLOCKWISE_90), FlatDirection.UP, FlatDirection.DOWN, FlatDirection.LEFT, FlatDirection.UP_LEFT),

	L_R_D_DR_DL(10, 0, new RotatedModel(ModelReference.WALL, Rotation.NONE), FlatDirection.LEFT, FlatDirection.RIGHT, FlatDirection.DOWN, FlatDirection.DOWN_RIGHT, FlatDirection.DOWN_LEFT),
	L_R_U_UR_UL(9, 3, new RotatedModel(ModelReference.WALL, Rotation.CLOCKWISE_180), FlatDirection.LEFT, FlatDirection.RIGHT, FlatDirection.UP, FlatDirection.UP_RIGHT, FlatDirection.UP_LEFT),
	U_D_R_UR_DR(8, 1, new RotatedModel(ModelReference.WALL, Rotation.COUNTERCLOCKWISE_90), FlatDirection.UP, FlatDirection.DOWN, FlatDirection.RIGHT, FlatDirection.UP_RIGHT, FlatDirection.DOWN_RIGHT),
	U_D_L_UL_DL(11, 2, new RotatedModel(ModelReference.WALL, Rotation.CLOCKWISE_90), FlatDirection.UP, FlatDirection.DOWN, FlatDirection.LEFT, FlatDirection.UP_LEFT, FlatDirection.DOWN_LEFT),

	PLUS(2, 1, new RotatedModel(ModelReference.PLUS, Rotation.NONE), FlatDirection.UP, FlatDirection.DOWN, FlatDirection.RIGHT, FlatDirection.LEFT),

	PLUS_UL(4, 0, new RotatedModel(ModelReference.PLUS_CORNER, Rotation.NONE), FlatDirection.UP, FlatDirection.DOWN, FlatDirection.RIGHT, FlatDirection.LEFT, FlatDirection.UP_LEFT),
	PLUS_UR(7, 0, new RotatedModel(ModelReference.PLUS_CORNER, Rotation.CLOCKWISE_90), FlatDirection.UP, FlatDirection.DOWN, FlatDirection.RIGHT, FlatDirection.LEFT, FlatDirection.UP_RIGHT),
	PLUS_DL(4, 0, new RotatedModel(ModelReference.PLUS_CORNER, Rotation.COUNTERCLOCKWISE_90), FlatDirection.UP, FlatDirection.DOWN, FlatDirection.RIGHT, FlatDirection.LEFT, FlatDirection.DOWN_LEFT),
	PLUS_DR(7, 0, new RotatedModel(ModelReference.PLUS_CORNER, Rotation.CLOCKWISE_180), FlatDirection.UP, FlatDirection.DOWN, FlatDirection.RIGHT, FlatDirection.LEFT, FlatDirection.DOWN_RIGHT),

	PLUS_UL_UR(10, 3, new RotatedModel(ModelReference.PLUS_WALL, Rotation.CLOCKWISE_180), FlatDirection.UP, FlatDirection.DOWN, FlatDirection.RIGHT, FlatDirection.LEFT, FlatDirection.UP_LEFT, FlatDirection.UP_RIGHT),
	PLUS_UR_DR(8, 2, new RotatedModel(ModelReference.PLUS_WALL, Rotation.COUNTERCLOCKWISE_90), FlatDirection.UP, FlatDirection.DOWN, FlatDirection.RIGHT, FlatDirection.LEFT, FlatDirection.UP_RIGHT, FlatDirection.DOWN_RIGHT),
	PLUS_DL_DR(9, 0, new RotatedModel(ModelReference.PLUS_WALL, Rotation.NONE), FlatDirection.UP, FlatDirection.DOWN, FlatDirection.RIGHT, FlatDirection.LEFT, FlatDirection.DOWN_LEFT, FlatDirection.DOWN_RIGHT),
	PLUS_DL_UL(11, 1, new RotatedModel(ModelReference.PLUS_WALL, Rotation.CLOCKWISE_90), FlatDirection.UP, FlatDirection.DOWN, FlatDirection.RIGHT, FlatDirection.LEFT, FlatDirection.DOWN_LEFT, FlatDirection.UP_LEFT),

	PLUS_NUL(5, 1, new RotatedModel(ModelReference.PLUS_ANTI_CORNER, Rotation.NONE), FlatDirection.UP, FlatDirection.DOWN, FlatDirection.RIGHT, FlatDirection.LEFT, FlatDirection.UP_RIGHT, FlatDirection.DOWN_LEFT, FlatDirection.DOWN_RIGHT),
	PLUS_NUR(6, 1, new RotatedModel(ModelReference.PLUS_ANTI_CORNER, Rotation.CLOCKWISE_90), FlatDirection.UP, FlatDirection.DOWN, FlatDirection.RIGHT, FlatDirection.LEFT, FlatDirection.UP_LEFT, FlatDirection.DOWN_LEFT, FlatDirection.DOWN_RIGHT),
	PLUS_NDL(5, 2, new RotatedModel(ModelReference.PLUS_ANTI_CORNER, Rotation.COUNTERCLOCKWISE_90), FlatDirection.UP, FlatDirection.DOWN, FlatDirection.RIGHT, FlatDirection.LEFT, FlatDirection.UP_RIGHT, FlatDirection.DOWN_RIGHT, FlatDirection.UP_LEFT),
	PLUS_NDR(6, 2, new RotatedModel(ModelReference.PLUS_ANTI_CORNER, Rotation.CLOCKWISE_180), FlatDirection.UP, FlatDirection.DOWN, FlatDirection.RIGHT, FlatDirection.LEFT, FlatDirection.UP_RIGHT, FlatDirection.DOWN_LEFT, FlatDirection.UP_LEFT),

	PLUS_NUL_NDR(9, 1, new RotatedModel(ModelReference.PLUS_DUAL_CORNER, Rotation.NONE), FlatDirection.UP, FlatDirection.DOWN, FlatDirection.RIGHT, FlatDirection.LEFT, FlatDirection.UP_RIGHT, FlatDirection.DOWN_LEFT),
	PLUS_NUR_NDL(10, 2, new RotatedModel(ModelReference.PLUS_DUAL_CORNER, Rotation.CLOCKWISE_90), FlatDirection.UP, FlatDirection.DOWN, FlatDirection.RIGHT, FlatDirection.LEFT, FlatDirection.UP_LEFT, FlatDirection.DOWN_RIGHT),

	TESSERA(0, 3, new RotatedModel(ModelReference.TESSERA, Rotation.NONE), List.of()),
	FULL(9, 2, new RotatedModel(ModelReference.FULL, Rotation.NONE), FlatDirection.values());

	private final int u;
	private final int v;
	private final RotatedModel model;
	private final List<FlatDirection> check;

	TesseraShape(int u, int v, RotatedModel model, List<FlatDirection> check) {
		this.u = u;
		this.v = v;
		this.model = model;
		this.check = check;
	}

	TesseraShape(int u, int v, RotatedModel model, FlatDirection... hasThis) {
		this(u, v, model, List.of(hasThis));
	}

	public int getU() {
		return this.u;
	}

	public int getV() {
		return this.v;
	}

	public RotatedModel getModel() {
		return this.model;
	}

	public TesseraShape rotate(Rotation rotation) {
		if (rotation == Rotation.NONE) return this;

		int steps = switch (rotation) {
			case CLOCKWISE_90 -> 2;
			case CLOCKWISE_180 -> 4;
			case COUNTERCLOCKWISE_90 -> 6;
			default -> 0;
		};

		return get(this.check.stream().map(flatDirection -> flatDirection.clockWise(steps)).toList());
	}

	public TesseraShape mirror(Mirror mirror) {
		return get(this.check.stream().map(flatDirection -> flatDirection.mirror(mirror)).toList());
	}

	public List<FlatDirection> getCheck() {
		return check;
	}

	public static TesseraShape get(List<FlatDirection> flatDirections) {
		for (TesseraShape shape : TesseraShape.values()) {
			if (shape.check.size() != flatDirections.size()) continue;
			boolean isSame = true;
			for (FlatDirection direction : flatDirections) {
				if (!shape.check.contains(direction)) {
					isSame = false;
					break;
				}
			}
			if (isSame) return shape;
		}
		return TESSERA;
	}

	@Override
	public String getSerializedName() {
		return this.name().toLowerCase(Locale.ROOT);
	}

	public record RotatedModel(ModelReference reference, Rotation rotation) {
		public Rotation getRotation() {
			return this.rotation;
		}
	}

	public enum ModelReference implements StringRepresentable {
		END,
		LONG,
		CORNER,
		FULL_CORNER,
		T,
		T_HALF_FULL_L,
		T_HALF_FULL_R,
		WALL,
		PLUS,
		PLUS_CORNER,
		PLUS_WALL,
		PLUS_ANTI_CORNER,
		PLUS_DUAL_CORNER,
		TESSERA,
		FULL;

		ModelReference() {

		}

		@Override
		public String getSerializedName() {
			return this.name().toLowerCase(Locale.ROOT);
		}
	}
}
