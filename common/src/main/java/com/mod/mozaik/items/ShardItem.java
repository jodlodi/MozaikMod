package com.mod.mozaik.items;

import com.mod.mozaik.polyomino.TesseraMaterial;
import net.minecraft.world.item.Item;

public class ShardItem extends Item {
	private final TesseraMaterial material;

	public ShardItem(Properties properties, TesseraMaterial material) {
		super(properties);
		this.material = material;
	}

	public TesseraMaterial getMaterial() {
		return this.material;
	}
}
