package com.mod.mozaik.polyomino;

import com.mod.mozaik.util.FlatDirection;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public record Tessera(Shape shape) {
	public static final int TESSERA_SIZE = 10;

	private Tessera(int ordinal) {
		this(Shape.values()[ordinal]);
	}

	public static final Codec<Tessera> CODEC = RecordCodecBuilder.create((recordCodecBuilder) -> recordCodecBuilder.group(
			Codec.INT.fieldOf("shape").forGetter(tessera -> tessera.shape.ordinal())
	).apply(recordCodecBuilder, Tessera::new));

	public int getU() {
		return this.shape.u;
	}

	public int getV() {
		return this.shape.v;
	}

	public enum Shape {
		U(0, 2, FlatDirection.UP),
		R(1, 3, FlatDirection.RIGHT),
		D(0, 0, FlatDirection.DOWN),
		L(3, 3, FlatDirection.LEFT),

		U_D(0, 1, FlatDirection.UP, FlatDirection.DOWN),
		L_R(2, 3, FlatDirection.LEFT, FlatDirection.RIGHT),

		R_D(1, 0, FlatDirection.RIGHT, FlatDirection.DOWN),
		L_D(3, 0, FlatDirection.LEFT, FlatDirection.DOWN),
		R_U(1, 2, FlatDirection.RIGHT, FlatDirection.UP),
		L_U(3, 2, FlatDirection.LEFT, FlatDirection.UP),

		R_D_DR(8, 0, FlatDirection.RIGHT, FlatDirection.DOWN, FlatDirection.DOWN_RIGHT),
		L_D_DL(11, 0, FlatDirection.LEFT, FlatDirection.DOWN, FlatDirection.DOWN_LEFT),
		R_U_UR(8, 3, FlatDirection.RIGHT, FlatDirection.UP, FlatDirection.UP_RIGHT),
		L_U_UL(11, 3, FlatDirection.LEFT, FlatDirection.UP, FlatDirection.UP_LEFT),

		L_R_D(2, 0, FlatDirection.LEFT, FlatDirection.RIGHT, FlatDirection.DOWN),
		L_R_U(2, 2, FlatDirection.LEFT, FlatDirection.RIGHT, FlatDirection.UP),
		U_D_R(1, 1, FlatDirection.UP, FlatDirection.DOWN, FlatDirection.RIGHT),
		U_D_L(3, 1, FlatDirection.UP, FlatDirection.DOWN, FlatDirection.LEFT),

		L_R_D_DR(5, 0, FlatDirection.LEFT, FlatDirection.RIGHT, FlatDirection.DOWN, FlatDirection.DOWN_RIGHT),
		L_R_D_DL(6, 0, FlatDirection.LEFT, FlatDirection.RIGHT, FlatDirection.DOWN, FlatDirection.DOWN_LEFT),
		L_R_U_UR(5, 3, FlatDirection.LEFT, FlatDirection.RIGHT, FlatDirection.UP, FlatDirection.UP_RIGHT),
		L_R_U_UL(6, 3, FlatDirection.LEFT, FlatDirection.RIGHT, FlatDirection.UP, FlatDirection.UP_LEFT),
		U_D_R_DR(4, 1, FlatDirection.UP, FlatDirection.DOWN, FlatDirection.RIGHT, FlatDirection.DOWN_RIGHT),
		U_D_R_UR(4, 2, FlatDirection.UP, FlatDirection.DOWN, FlatDirection.RIGHT, FlatDirection.UP_RIGHT),
		U_D_L_DL(7, 1, FlatDirection.UP, FlatDirection.DOWN, FlatDirection.LEFT, FlatDirection.DOWN_LEFT),
		U_D_L_UL(7, 2, FlatDirection.UP, FlatDirection.DOWN, FlatDirection.LEFT, FlatDirection.UP_LEFT),

		L_R_D_DR_DL(10, 0, FlatDirection.LEFT, FlatDirection.RIGHT, FlatDirection.DOWN, FlatDirection.DOWN_RIGHT, FlatDirection.DOWN_LEFT),
		L_R_U_UR_UL(9, 3, FlatDirection.LEFT, FlatDirection.RIGHT, FlatDirection.UP, FlatDirection.UP_RIGHT, FlatDirection.UP_LEFT),
		U_D_R_UR_DR( 8, 1, FlatDirection.UP, FlatDirection.DOWN, FlatDirection.RIGHT, FlatDirection.UP_RIGHT, FlatDirection.DOWN_RIGHT),
		U_D_L_UL_DL(11, 2, FlatDirection.UP, FlatDirection.DOWN, FlatDirection.LEFT, FlatDirection.UP_LEFT, FlatDirection.DOWN_LEFT),

		PLUS(2, 1, FlatDirection.UP, FlatDirection.DOWN, FlatDirection.RIGHT, FlatDirection.LEFT),

		PLUS_UL(4, 0, FlatDirection.UP, FlatDirection.DOWN, FlatDirection.RIGHT, FlatDirection.LEFT, FlatDirection.UP_LEFT),
		PLUS_UR(7, 0, FlatDirection.UP, FlatDirection.DOWN, FlatDirection.RIGHT, FlatDirection.LEFT, FlatDirection.UP_RIGHT),
		PLUS_DL(4, 0, FlatDirection.UP, FlatDirection.DOWN, FlatDirection.RIGHT, FlatDirection.LEFT, FlatDirection.DOWN_LEFT),
		PLUS_DR(7, 0, FlatDirection.UP, FlatDirection.DOWN, FlatDirection.RIGHT, FlatDirection.LEFT, FlatDirection.DOWN_RIGHT),

		PLUS_UL_UR(10, 3, FlatDirection.UP, FlatDirection.DOWN, FlatDirection.RIGHT, FlatDirection.LEFT, FlatDirection.UP_LEFT, FlatDirection.UP_RIGHT),
		PLUS_UR_DR(8, 2, FlatDirection.UP, FlatDirection.DOWN, FlatDirection.RIGHT, FlatDirection.LEFT, FlatDirection.UP_RIGHT, FlatDirection.DOWN_RIGHT),
		PLUS_DL_DR(9, 0, FlatDirection.UP, FlatDirection.DOWN, FlatDirection.RIGHT, FlatDirection.LEFT, FlatDirection.DOWN_LEFT, FlatDirection.DOWN_RIGHT),
		PLUS_DL_UL(11, 1, FlatDirection.UP, FlatDirection.DOWN, FlatDirection.RIGHT, FlatDirection.LEFT, FlatDirection.DOWN_LEFT, FlatDirection.UP_LEFT),

		PLUS_NUL(5, 1, FlatDirection.UP, FlatDirection.DOWN, FlatDirection.RIGHT, FlatDirection.LEFT, FlatDirection.UP_RIGHT, FlatDirection.DOWN_LEFT, FlatDirection.DOWN_RIGHT),
		PLUS_NUR(6, 1, FlatDirection.UP, FlatDirection.DOWN, FlatDirection.RIGHT, FlatDirection.LEFT, FlatDirection.UP_LEFT, FlatDirection.DOWN_LEFT, FlatDirection.DOWN_RIGHT),
		PLUS_NDL(5, 2, FlatDirection.UP, FlatDirection.DOWN, FlatDirection.RIGHT, FlatDirection.LEFT, FlatDirection.UP_RIGHT, FlatDirection.DOWN_RIGHT, FlatDirection.UP_LEFT),
		PLUS_NDR(6, 2, FlatDirection.UP, FlatDirection.DOWN, FlatDirection.RIGHT, FlatDirection.LEFT, FlatDirection.UP_RIGHT, FlatDirection.DOWN_LEFT, FlatDirection.UP_LEFT),

		PLUS_NUL_NDR(9, 1, FlatDirection.UP, FlatDirection.DOWN, FlatDirection.RIGHT, FlatDirection.LEFT, FlatDirection.UP_RIGHT, FlatDirection.DOWN_LEFT),
		PLUS_NUR_NDL(10, 2, FlatDirection.UP, FlatDirection.DOWN, FlatDirection.RIGHT, FlatDirection.LEFT, FlatDirection.UP_LEFT, FlatDirection.DOWN_RIGHT),

		TESSERA(0, 3, List.of()),
		FULL(9, 2, FlatDirection.values());

		private final int u;
		private final int v;
		private final List<FlatDirection> check;

		Shape(int u, int v, List<FlatDirection> check) {
			this.u = u;
			this.v = v;
			this.check = check;
		}

		Shape(int u, int v, FlatDirection... hasThis) {
			this(u, v, List.of(hasThis));
		}

		public Shape clockWise() {
			return get(this.check.stream().map(flatDirection -> flatDirection.clockWise(2)).toList());
		}

		public Shape counterClockWise() {
			return get(this.check.stream().map(flatDirection -> flatDirection.counterClockWise(2)).toList());
		}

		public Shape horizontalMirror() {
			return get(this.check.stream().map(FlatDirection::horizontalMirror).toList());
		}

		public static Shape get(List<FlatDirection> flatDirections) {
			for (Shape shape : Shape.values()) {
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
	}
}
