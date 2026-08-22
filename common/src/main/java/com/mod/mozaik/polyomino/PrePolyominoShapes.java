package com.mod.mozaik.polyomino;

public enum PrePolyominoShapes {
	SQUARE(new Polyomino.Builder()
			.addLine(true, true)
			.addLine(true, true)
	),
	PLUS(new Polyomino.Builder()
			.addLine(false, true, false)
			.addLine(true, true, true)
			.addLine(false, true, false)
	),
	BLOB(new Polyomino.Builder()
			.addLine(true, true, false)
			.addLine(true, true, true)
			.addLine(false, true, true)
	),
	R_THUMBS_UP(new Polyomino.Builder()
			.addLine(true, false)
			.addLine(true, true)
			.addLine(true, true)
	),
	L_THUMBS_UP(new Polyomino.Builder()
			.addLine(false, true)
			.addLine(true, true)
			.addLine(true, true)
	),
	MIN_L(new Polyomino.Builder()
			.addLine(false, true)
			.addLine(true, true)
	),
	MIDDLE_FINGER(new Polyomino.Builder()
			.addLine(false, true, false)
			.addLine(true, true, true)
			.addLine(true, true, true)
	),
	III(new Polyomino.Builder()
			.addLine(true)
			.addLine(true)
			.addLine(true)
	),
	T(new Polyomino.Builder()
			.addLine(false, true)
			.addLine(true, true)
			.addLine(false, true)
	),
	Z(new Polyomino.Builder()
			.addLine(false, true)
			.addLine(true, true)
			.addLine(true, false)
	),
	S(new Polyomino.Builder()
			.addLine(true, false)
			.addLine(true, true)
			.addLine(false, true)
	),
	C(new Polyomino.Builder()
			.addLine(true, true)
			.addLine(true, false)
			.addLine(true, true)
	);

	public final Polyomino.Builder template;

	PrePolyominoShapes(Polyomino.Builder template) {
		this.template = template;
	}
}
