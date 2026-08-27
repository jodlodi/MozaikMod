package com.mod.mozaik.structure.piece;

import com.mod.mozaik.platform.Services;
import com.mod.mozaik.reg.ResourceSupplier;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

public class ModStructurePieces {
	public static final ResourceSupplier<StructurePieceType> CUSTOM_STRUCTURE_PIECE = Services.REGISTRY.registerStructurePieceType("custom_structure_piece", CustomStructurePiece::new);

	public static void init() {

	}
}
