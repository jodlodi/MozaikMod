package com.mod.mozaik.reg;

import com.mod.mozaik.platform.Services;
import com.mod.mozaik.polyomino.PolyominoShape;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.NullMarked;

import java.util.List;

@NullMarked
public class ModPolyominoShapes {
	public static final ResourceSupplier<PolyominoShape> SUN = Services.REGISTRY.registerPolyominoShape("sun", () -> new PolyominoShape()
			.addLine(true, true)
			.addLine(true, true)
	);
	public static final ResourceSupplier<PolyominoShape> PLUS = Services.REGISTRY.registerPolyominoShape("plus", () -> new PolyominoShape()
			.addLine(false, true, false)
			.addLine(true, true, true)
			.addLine(false, true, false)
	);
	public static final ResourceSupplier<PolyominoShape> LEAF = Services.REGISTRY.registerPolyominoShape("leaf", () -> new PolyominoShape()
			.addLine(true, true, false)
			.addLine(true, true, true)
			.addLine(false, true, true)
	);
	public static final ResourceSupplier<PolyominoShape> THUMB = Services.REGISTRY.registerPolyominoShape("thumb", () -> new PolyominoShape()
			.addLine(true, false)
			.addLine(true, true)
			.addLine(true, true)
	);
	public static final ResourceSupplier<PolyominoShape> STEM = Services.REGISTRY.registerPolyominoShape("stem", () -> new PolyominoShape()
			.addLine(false, true)
			.addLine(true, true)
			.addLine(true, true)
	);
	public static final ResourceSupplier<PolyominoShape> HEART = Services.REGISTRY.registerPolyominoShape("heart", () -> new PolyominoShape()
			.addLine(false, true)
			.addLine(true, true)
	);
	public static final ResourceSupplier<PolyominoShape> HELMET = Services.REGISTRY.registerPolyominoShape("helmet", () -> new PolyominoShape()
			.addLine(false, true, false)
			.addLine(true, true, true)
			.addLine(true, true, true)
	);
	public static final ResourceSupplier<PolyominoShape> LOG = Services.REGISTRY.registerPolyominoShape("log", () -> new PolyominoShape()
			.addLine(true)
			.addLine(true)
			.addLine(true)
	);
	public static final ResourceSupplier<PolyominoShape> LEVER = Services.REGISTRY.registerPolyominoShape("lever", () -> new PolyominoShape()
			.addLine(false, true)
			.addLine(true, true)
			.addLine(false, true)
	);
	public static final ResourceSupplier<PolyominoShape> SNAKE = Services.REGISTRY.registerPolyominoShape("snake", () -> new PolyominoShape()
			.addLine(false, true)
			.addLine(true, true)
			.addLine(true, false)
	);
	public static final ResourceSupplier<PolyominoShape> LIZARD = Services.REGISTRY.registerPolyominoShape("lizard", () -> new PolyominoShape()
			.addLine(true, false)
			.addLine(true, true)
			.addLine(false, true)
	);
	public static final ResourceSupplier<PolyominoShape> MOON = Services.REGISTRY.registerPolyominoShape("moon", () -> new PolyominoShape()
			.addLine(true, true)
			.addLine(true, false)
			.addLine(true, true)
	);

	public static final ResourceSupplier<PolyominoShape> BUTTON = Services.REGISTRY.registerPolyominoShape("button", () -> new PolyominoShape()
			.addLine(true)
	);
	public static final ResourceSupplier<PolyominoShape> BONE = Services.REGISTRY.registerPolyominoShape("bone", () -> new PolyominoShape()
			.addLine(true, true, true)
			.addLine(false, true, false)
			.addLine(true, true, true)
	);
	public static final ResourceSupplier<PolyominoShape> BUBBLE = Services.REGISTRY.registerPolyominoShape("bubble", () -> new PolyominoShape()
			.addLine(true, true, true)
			.addLine(true, false, true)
			.addLine(true, true, true)
	);
	public static final ResourceSupplier<PolyominoShape> WORM = Services.REGISTRY.registerPolyominoShape("worm", () -> new PolyominoShape()
			.addLine(false, true, true)
			.addLine(true, true, false)
			.addLine(true, false, false)
	);
	public static final ResourceSupplier<PolyominoShape> CANE = Services.REGISTRY.registerPolyominoShape("cane", () -> new PolyominoShape()
			.addLine(true, false)
			.addLine(true, false)
			.addLine(true, true)
	);
	public static final ResourceSupplier<PolyominoShape> POINT = Services.REGISTRY.registerPolyominoShape("point", () -> new PolyominoShape()
			.addLine(false, true)
			.addLine(false, true)
			.addLine(true, true)
	);
	public static final ResourceSupplier<PolyominoShape> HORN = Services.REGISTRY.registerPolyominoShape("horn", () -> new PolyominoShape()
			.addLine(false, false, true)
			.addLine(true, true, true)
			.addLine(false, true, false)
	);
	public static final ResourceSupplier<PolyominoShape> TREE = Services.REGISTRY.registerPolyominoShape("tree", () -> new PolyominoShape()
			.addLine(true, false, false)
			.addLine(true, true, true)
			.addLine(false, true, false)
	);
	public static final ResourceSupplier<PolyominoShape> FORK = Services.REGISTRY.registerPolyominoShape("fork", () -> new PolyominoShape()
			.addLine(true, false, true)
			.addLine(true, true, true)
			.addLine(false, true, false)
	);

	@Contract(pure = true)
	public static @Unmodifiable List<ResourceSupplier<PolyominoShape>> alwaysShapes() {
		return List.of(
				SUN,
				PLUS,
				LEAF,
				THUMB,
				STEM,
				HEART,
				HELMET,
				LOG,
				LEVER,
				SNAKE,
				LIZARD,
				MOON
		);
	}

	public static void init() {

	}

	public static ResourceKey<PolyominoShape> ofShape(ResourceSupplier<PolyominoShape> material) {
		return ResourceKey.create(ModRegistries.ModKeys.POLYOMINO_SHAPE, material.id());
	}
}
