package com.mod.mozaik.client.widgets;

import com.mod.mozaik.polyomino.Tessera;

public class GridWidget extends UnclickableWidget {
	private final int relativeX;
	private final int relativeY;

	public GridWidget(int x, int y, int relativeX, int relativeY) {
		super(x, y, Tessera.TESSERA_SIZE, Tessera.TESSERA_SIZE);
		this.relativeX = relativeX;
		this.relativeY = relativeY;
	}

	public int relativeX() {
		return this.relativeX;
	}

	public int relativeY() {
		return this.relativeY;
	}
}
