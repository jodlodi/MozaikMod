package com.mod.mozaik.mixin;

import com.mod.mozaik.structure.piece.CustomStructurePiece;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.neoforged.neoforge.common.world.PieceBeardifierModifier;
import org.jspecify.annotations.NullMarked;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@NullMarked
@Mixin(value = CustomStructurePiece.class, remap = false)
public abstract class CustomStructurePieceMixin extends TemplateStructurePiece implements PieceBeardifierModifier {
	@Shadow
	@Final
	private CustomStructurePiece.Properties properties;

	public CustomStructurePieceMixin(StructurePieceType type, int genDepth, StructureTemplateManager structureTemplateManager, Identifier templateLocation, String templateName, StructurePlaceSettings placeSettings, BlockPos position) {
		super(type, genDepth, structureTemplateManager, templateLocation, templateName, placeSettings, position);
	}

	@Override
	public BoundingBox getBeardifierBox() {
		return this.getBoundingBox();
	}

	@Override
	public TerrainAdjustment getTerrainAdjustment() {
		return this.properties.adjustment();
	}

	@Override
	public int getGroundLevelDelta() {
		return this.properties.groundLevelDelta();
	}
}
