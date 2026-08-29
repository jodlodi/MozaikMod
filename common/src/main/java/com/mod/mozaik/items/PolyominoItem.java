package com.mod.mozaik.items;

import com.mod.mozaik.polyomino.PolyominoShape;
import com.mod.mozaik.reg.ModRegistries;
import com.mod.mozaik.reg.ResourceSupplier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class PolyominoItem extends Item {
	private final ResourceKey<PolyominoShape> polyominoShape;

	public PolyominoItem(Properties properties, ResourceSupplier<PolyominoShape> polyominoShape) {
		this(properties, ResourceKey.create(ModRegistries.ModKeys.POLYOMINO_SHAPE, polyominoShape.id()));
	}

	public PolyominoItem(Properties properties, ResourceKey<PolyominoShape> polyominoShape) {
		super(properties);
		this.polyominoShape = polyominoShape;
	}

	public ResourceKey<PolyominoShape> getPolyominoShape() {
		return this.polyominoShape;
	}
}
