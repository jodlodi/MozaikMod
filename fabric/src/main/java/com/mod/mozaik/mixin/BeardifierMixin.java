package com.mod.mozaik.mixin;

import com.mod.mozaik.structure.piece.CustomStructurePiece;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.levelgen.Beardifier;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.pools.JigsawJunction;
import org.jspecify.annotations.NullMarked;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.List;

@NullMarked
@Mixin(Beardifier.class)
public class BeardifierMixin {

	@Inject(method = "forStructuresInChunk", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/levelgen/Beardifier;includeBoundingBox(Lnet/minecraft/world/level/levelgen/structure/BoundingBox;Lnet/minecraft/world/level/levelgen/structure/BoundingBox;)Lnet/minecraft/world/level/levelgen/structure/BoundingBox;", ordinal = 2), locals = LocalCapture.CAPTURE_FAILSOFT)
	private static void ifCustom(StructureManager structureManager, ChunkPos chunkPos, CallbackInfoReturnable<Beardifier> cir, List<StructureStart> structureStarts, int chunkStartBlockX, int chunkStartBlockZ, List<Beardifier.Rigid> rigids, List<JigsawJunction> junctions, BoundingBox anyPieceBoundingBox, Iterator<?> var8, StructureStart start, TerrainAdjustment terrainAdjustment, Iterator<?> var11, StructurePiece piece) {
		if (piece instanceof CustomStructurePiece customStructurePiece) {
			rigids.removeLast();
			CustomStructurePiece.Properties properties = customStructurePiece.getProperties();
			rigids.add(new Beardifier.Rigid(customStructurePiece.getBoundingBox(), properties.adjustment(), properties.groundLevelDelta()));
		}
	}
}
