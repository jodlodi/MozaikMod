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
	public static final ResourceSupplier<PolyominoShape> SMASHBOY = Services.REGISTRY.registerPolyominoShape("smashboy", () -> new PolyominoShape()
			.addLine(true, true)
			.addLine(true, true)
	);
	public static final ResourceSupplier<PolyominoShape> CONWAY_X = Services.REGISTRY.registerPolyominoShape("conway_x", () -> new PolyominoShape()
			.addLine(false, true, false)
			.addLine(true, true, true)
			.addLine(false, true, false)
	);
	public static final ResourceSupplier<PolyominoShape> REFLECTED_HEPTOMINO = Services.REGISTRY.registerPolyominoShape("reflected_heptomino", () -> new PolyominoShape()
			.addLine(true, true, false)
			.addLine(true, true, true)
			.addLine(false, true, true)
	);
	public static final ResourceSupplier<PolyominoShape> CONWAY_P_LEFT = Services.REGISTRY.registerPolyominoShape("conway_p_left", () -> new PolyominoShape()
			.addLine(true, false)
			.addLine(true, true)
			.addLine(true, true)
	);
	public static final ResourceSupplier<PolyominoShape> CONWAY_P_RIGHT = Services.REGISTRY.registerPolyominoShape("conway_p_right", () -> new PolyominoShape()
			.addLine(false, true)
			.addLine(true, true)
			.addLine(true, true)
	);
	public static final ResourceSupplier<PolyominoShape> L_TROMINO = Services.REGISTRY.registerPolyominoShape("l_tromino", () -> new PolyominoShape()
			.addLine(false, true)
			.addLine(true, true)
	);
	public static final ResourceSupplier<PolyominoShape> MIDDLE_FINGER = Services.REGISTRY.registerPolyominoShape("middle_finger", () -> new PolyominoShape()
			.addLine(false, true, false)
			.addLine(true, true, true)
			.addLine(true, true, true)
	);
	public static final ResourceSupplier<PolyominoShape> I_TROMINO = Services.REGISTRY.registerPolyominoShape("i_tromino", () -> new PolyominoShape()
			.addLine(true)
			.addLine(true)
			.addLine(true)
	);
	public static final ResourceSupplier<PolyominoShape> TEEWEE = Services.REGISTRY.registerPolyominoShape("teewee", () -> new PolyominoShape()
			.addLine(false, true)
			.addLine(true, true)
			.addLine(false, true)
	);
	public static final ResourceSupplier<PolyominoShape> CLEVELAND_Z = Services.REGISTRY.registerPolyominoShape("cleveland_z", () -> new PolyominoShape()
			.addLine(false, true)
			.addLine(true, true)
			.addLine(true, false)
	);
	public static final ResourceSupplier<PolyominoShape> RHODE_ISLAND_Z = Services.REGISTRY.registerPolyominoShape("rhode_island_z", () -> new PolyominoShape()
			.addLine(true, false)
			.addLine(true, true)
			.addLine(false, true)
	);
	public static final ResourceSupplier<PolyominoShape> CONWAY_U = Services.REGISTRY.registerPolyominoShape("conway_u", () -> new PolyominoShape()
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
				SMASHBOY,
				CONWAY_X,
				REFLECTED_HEPTOMINO,
				CONWAY_P_LEFT,
				CONWAY_P_RIGHT,
				L_TROMINO,
				MIDDLE_FINGER,
				I_TROMINO,
				TEEWEE,
				CLEVELAND_Z,
				RHODE_ISLAND_Z,
				CONWAY_U
		);
	}

	public static void init() {

	}

	public static ResourceKey<PolyominoShape> ofShape(ResourceSupplier<PolyominoShape> material) {
		return ResourceKey.create(ModRegistries.ModKeys.POLYOMINO_SHAPE, material.id());
	}
}
